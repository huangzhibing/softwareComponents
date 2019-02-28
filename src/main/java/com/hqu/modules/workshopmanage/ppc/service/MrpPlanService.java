/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.ppc.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.hqu.modules.workshopmanage.ppc.entity.MpsPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.time.DateUtil;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.workshopmanage.ppc.xhcUtil;
import com.hqu.modules.workshopmanage.ppc.entity.MrpPlan;
import com.hqu.modules.workshopmanage.ppc.mapper.MrpPlanMapper;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * MRP计划Service
 * 
 * @author XHC
 * @version 2018-06-28
 */
@Service
@Transactional(readOnly = true)
public class MrpPlanService extends CrudService<MrpPlanMapper, MrpPlan> {

	@Autowired
	MpsPlanService mpsPlanService;

	@Autowired
	private OfficeService officeService;

	public MrpPlan get(String id) {
		return super.get(id);
	}

	public List<MrpPlan> findList(MrpPlan mrpPlan) {
		return super.findList(mrpPlan);
	}

	public Page<MrpPlan> findPage(Page<MrpPlan> page, MrpPlan mrpPlan) {
		return super.findPage(page, mrpPlan);
	}

	@Transactional(readOnly = false)
	public void save(MrpPlan mrpPlan) {
		super.save(mrpPlan);
	}

	@Transactional(readOnly = false)
	public void saveMpsAndMrp(MpsPlan mpsPlan) {
		mpsPlanService.save(mpsPlan);
		for (MrpPlan mrpPlan : mpsPlan.getMrpPlanList()) {
			save(mrpPlan);
		}

		if("S".equals(mpsPlan.getMRPDealStatus())){ //物料需求审核通过后写入到滚动计划那里
			for (MrpPlan mrpPlan : mpsPlan.getMrpPlanList()){
				generatePurRollPlan(mrpPlan.getMRPPlanID());
			}
		}
	}

	@Transactional(readOnly = false)
	public void delete(MrpPlan mrpPlan) {
		super.delete(mrpPlan);
	}

	/**
	 * 从MPS表格中读取数据，生成MRP计划，保存入MRP表中 1.ppc_mpsplan表格中的计划状态planstatus为S下达状态
	 * 2.在ppc_mrpplan表格中尚未出现过(即尚未处理的) 3.写入时产生mrpplanid
	 * 
	 * 需要处理的内容： 1.BOM的根节点如何定义的，是否是product_code和father_code相等？
	 */
	@Transactional(readOnly = false)
	public void makeMRP() {

		// 从ppc_mpsplan表格中读取已经下达并且尚未处理的MPS计划
		String attributes = "a.mpsplanid,a.prodcode,a.prodname,a.planqty,a.planbdate,a.planedate";
		String selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###") + ") FROM ppc_mpsplan a"
				+ " where a.mpsplanid not in (select distinct mpsplanid from ppc_mrpplan)" + " and a.planstatus='S'";
		String attrFormat = "mpsplanid,prodcode,prodname,planqty,planbdate,planedate";
		logger.debug("  获取MPS计划" + selectSql);
		List<Map<String, String>> mpsList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");

		// 计算得到当前的最大MRP计划序号，为后续新生成计划号使用
		int maxPlanid = 0;
		String prefix = "MRP" + DateUtils.getYear()+ DateUtils.getMonth() + "-";
		selectSql = "select max(substring(mrpplanid,11)+0) from ppc_mrpplan where left(mrpplanid,10)='" + prefix + "'";
		List<Object> maxId = super.executeSelectSql(selectSql); // 读取当月最大的计划号序号值
		if (maxId==null||maxId.get(0) == null) { // 说明没有当月的计划，则序号设置为1
			maxPlanid = 0;
		} else {// 序号加1
			//maxPlanid = ((Double) maxId.get(0)).intValue() + 1;
			maxPlanid = (Double.valueOf(maxId.get(0).toString())).intValue() + 1;
		}

		// 逐一处理每一条MPS计划计划
		for (Map<String, String> mps : mpsList) {

			// 获得当前计划对应的BOM数据。注：此处是之前code中保存ID时的程序代码，现在已经修正，不用了。
			String productCode = mps.get("prodcode");
			
			// 定义2个队列
			// 队列queueForCount用于计算，存入节点，然后取出计算，当此队列为空时，计算结束
			// queueForSave用于保存节点，所有值计算完成后，统一存入数据库中
			Queue<MrpPlan> queueForCount = new LinkedList<>();
			Queue<MrpPlan> queueForSave = new LinkedList<>();

			/***************1. 读取第一层节点（根节点），保存入排队中****************/
			attributes = "a.product_item_code,a.item_code,a.father_item_code,a.product_item_name,a.item_name,"
						+"a.father_item_name,a.num_in_father,a.lead_Time_Assemble,a.is_Leaf,b.is_part";
			selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###") + ") "
					+ " FROM mdm_productbom a,mdm_item b"
					+ " where product_item_code='" + productCode + "'" 
					+ " and father_item_code='" + productCode + "'"
					+ " and a.item_code=b.code";
			logger.debug("  查询根节点  "  + selectSql);
			attrFormat = "product_item_code,item_code,father_item_code,product_item_name,item_name,father_item_name,"
					+ "num_in_father,lead_Time_Assemble,is_Leaf,is_part";
			List<Map<String, String>> firstLevelList = xhcUtil.resultToList(super.executeSelectSql(selectSql),
					attrFormat, "###");

			for (Map<String, String> firstLevelNode : firstLevelList) {
				//如果节点为自制件，不处理。下面的物料计算，留给该自制件MRP计划时处理
				if (firstLevelNode.get("is_part").equals("Y")||firstLevelNode.get("is_part").equals("y"))
					continue;

				// 处理当前节点的MRP计划
				MrpPlan mrpplan = new MrpPlan();

				// 生成当前节点的MRP计划号
				maxPlanid++;
				mrpplan.setMRPPlanID(prefix + maxPlanid); // MRP计划号

				mrpplan.setItemCode(firstLevelNode.get("item_code")); // 物料编码
				mrpplan.setItemName(firstLevelNode.get("item_name")); // 物料名称
				mrpplan.setMPSplanid(mps.get("mpsplanid"));// 主生产计划号

				// 需求数量=MPS需求数量*父件对子件的需求
				mrpplan.setPlanQty(
						Integer.valueOf(mps.get("planqty")) * Integer.valueOf(firstLevelNode.get("num_in_father")));

				// 处理计划开始日期和结束日期
				// 计划完工日期=MPS的开始日期-1，因为是顶层节点
				mrpplan.setPlanEDate(DateUtil.subDays(DateUtils.parseDate(mps.get("planbdate")), 1));
				Date planBDate = DateUtil.subDays(mrpplan.getPlanEDate(),
						Integer.valueOf(firstLevelNode.get("lead_Time_Assemble")));
				mrpplan.setPlanBdate(planBDate); // 计划开始日期=计划完工日期-提前期，****此处需要优化
				//logger.debug("planBdate     " + mrpplan.getPlanBdate().toString());
				//xhcUtil..println("planEdate     " + mrpplan.getPlanEDate().toString());

				// 处理其余字段
				mrpplan.setInvAllocatedQty(0);// 库存占用量，暂时不用，设置为0
				mrpplan.setFinishQty(0); // 已完成数量,0
				mrpplan.setPlanStatus("P"); // 计划状态
				mrpplan.setPurDealStatus("N");// 采购处理状态
				mrpplan.setDelFlag("N"); // 车间处理状态
				mrpplan.setMakeDate(new Date()); // 计划制定日期
				mrpplan.setMakePID((UserUtils.getUser()).getId()); // 计划指定人
				
				//判断当前节点是否是叶节点
				selectSql="select count(*) from mdm_productbom where product_item_code='" + firstLevelNode.get("product_item_code")
						+ "' and father_item_code='" + firstLevelNode.get("item_code") + "'";
				logger.debug("    判断是否是叶节点     " + selectSql);
				List<Object> isLeafList = super.executeSelectSql(selectSql);
				if (isLeafList==null || isLeafList.get(0).toString().equals("0"))
					mrpplan.setIsLeaf("Y");
				else
					mrpplan.setIsLeaf("N");

				// 设置框架自动生成的字段
				mrpplan.setId(mrpplan.getMRPPlanID());
				mrpplan.setRemarks("");
				mrpplan.setCreateBy(UserUtils.getUser());
				mrpplan.setCreateDate(new Date());
				mrpplan.setUpdateBy(UserUtils.getUser());
				mrpplan.setUpdateDate(new Date());
				mrpplan.setDelFlag("0");

				// 将该节点加入到队列中
				queueForCount.add(mrpplan);
				queueForSave.add(mrpplan);
			}
			logger.debug("队列中元素数量，第一层：  " + queueForSave.size());

			/***********2.开始从队列中逐一读取节点处理，读取后删除队列中该元素，当队列为空时，说明计算完毕。**********/
			// 取出节点后的计算逻辑同之前计算第一层的相同
			while (queueForCount.size() > 0) {
				MrpPlan tmpMrp = queueForCount.poll(); // 取出并从队列中删除此节点

				// 读取该节点的下层节点，保存入排队中
				attributes = "product_item_code,item_code,father_item_code,product_item_name,item_name,"
						+ "father_item_name,num_in_father,lead_Time_Assemble,is_Leaf,is_part";
				selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###") + ") " 
						+ " FROM mdm_productbom a,mdm_item b"
						+ " where product_item_code='" + productCode + "'" 
						+ " and father_item_code='" + tmpMrp.getItemCode() + "'"
						+ " and a.item_code=b.code";
				logger.debug("第二层及之后的节点  " + selectSql);
				attrFormat = "product_item_code,item_code,father_item_code,product_item_name,item_name,"
						+ "father_item_name,num_in_father,lead_Time_Assemble,is_Leaf,is_part";
				List<Map<String, String>> afterFirstLevelList = xhcUtil.resultToList(super.executeSelectSql(selectSql),
						attrFormat, "###");
				for (Map<String, String> afterFirstNode : afterFirstLevelList) {

					//如果节点为自制件，不处理。下面的物料计算，留给该自制件MRP计划时处理
					if (afterFirstNode.get("is_part").equals("Y")||afterFirstNode.get("is_part").equals("y"))
						continue;

					// 处理当前节点的MRP计划
					MrpPlan mrpplan = new MrpPlan();

					// 生成当前节点的MRP计划号
					maxPlanid++;
					mrpplan.setMRPPlanID(prefix + maxPlanid); // MRP计划号

					mrpplan.setItemCode(afterFirstNode.get("item_code")); // 物料编码
					mrpplan.setItemName(afterFirstNode.get("item_name")); // 物料名称
					mrpplan.setMPSplanid(mps.get("mpsplanid"));// 主生产计划号
					// 需求数量=父件需求数量*父件对子件的需求
					mrpplan.setPlanQty(Integer.valueOf(tmpMrp.getPlanQty())
							* Integer.valueOf(afterFirstNode.get("num_in_father")));

					// 处理计划开始日期和结束日期
					mrpplan.setPlanEDate(DateUtil.subDays(tmpMrp.getPlanBdate(), 1));// 计划完工日期=父件的开始日期-1
					Date planBDate = DateUtil.subDays(mrpplan.getPlanEDate(),
							Integer.valueOf(afterFirstNode.get("lead_Time_Assemble")));
					mrpplan.setPlanBdate(planBDate); // 计划开始日期=计划完工日期-提前期，****此处需要优化

					// 处理其余字段
					mrpplan.setInvAllocatedQty(0);// 库存占用量，暂时不用，设置为0
					mrpplan.setFinishQty(0); // 已完成数量,0
					mrpplan.setPlanStatus("P"); // 计划状态
					mrpplan.setPurDealStatus("N");// 采购处理状态
					mrpplan.setDelFlag("N"); // 车间处理状态
					mrpplan.setMakeDate(new Date()); // 计划制定日期
					mrpplan.setMakePID((UserUtils.getUser()).getId()); // 计划指定人
					
					//判断当前节点是否是叶节点
					selectSql="select count(*) from mdm_productbom "
							+ " where product_item_code='" + afterFirstNode.get("product_item_code") + "' and"
							+ " father_item_code='" + afterFirstNode.get("item_code") + "'";
					logger.debug("    判断是否是叶节点     " + selectSql);
					List<Object> isLeafList = super.executeSelectSql(selectSql);

					if (isLeafList==null || isLeafList.get(0).toString().equals("0"))
						mrpplan.setIsLeaf("Y");
					else
						mrpplan.setIsLeaf("N");

					// 设置框架自动生成的字段
					mrpplan.setId(mrpplan.getMRPPlanID());
					mrpplan.setRemarks("");
					mrpplan.setCreateBy(UserUtils.getUser());
					mrpplan.setCreateDate(new Date());
					mrpplan.setUpdateBy(UserUtils.getUser());
					mrpplan.setUpdateDate(new Date());
					mrpplan.setDelFlag("0");

					// 将该节点加入到队列中
					queueForCount.add(mrpplan);
					queueForSave.add(mrpplan);

				}

			}

			// 保存queueForSave队列中的所有节点到ppc_mrpplan表格中
			for (MrpPlan mrpplan : queueForSave) {
				super.mapper.insert(mrpplan);
			}
		}
	}

	// 根据提交的MRP计划，生成采购系统中的滚动需求 PUR_RollPlanNew
	// 此程序在MRP计划点击提交按钮时执行
	@Transactional(readOnly = false)
	public void generatePurRollPlan(String mrpPlanID) {
		/*****
		 * 大致思路： 从MRP计划表中根据MRP计划号读取数据 写入采购系统对应的滚动计划表中
		 * 
		 */

		/* 读取MRP计划信息 */
		String attributes = "a.mrpplanid,a.planqty,a.planbdate,a.planedate,a.isleaf,"
				+ "a.itemcode,b.name,b.unit,b.spec_model,b.id";
		String selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
				+ ") FROM ppc_mrpplan a left join mdm_item b on a.itemcode=b.code" + " where a.mrpplanid='"
				+ mrpPlanID.trim() + "' and a.mrpplanid not in (select distinct plan_num from pur_rollplannew where plan_num!=null)";
		logger.debug(" XXX YYYYYYYYYY SQL " + selectSql);
		String attrFormat = attributes;
		List<Map<String, String>> mrpplanList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat,
				"###");

		// 为空退出
		if (mrpplanList == null || mrpplanList.size() == 0) {
			return;
		}

		// 不是叶节点，退出。
		if (!mrpplanList.get(0).get("a.isleaf").trim().equals("Y")) {
			logger.debug("  is not leaf   ");
			return;
		}

		// 获取部门代码和名称
		User u = UserUtils.getUser();
		Office o = officeService.get(u.getOffice().getId());

		// 生成插入数据库中的SQL语句
		String insertSql = "insert into pur_rollplannew (" 
				+ "bill_num," // 单据号
				+ "bill_type," // 单据类型
				+ "serial_num," // 序号
				+ "item_code," // 物料编码
				+ "item_name,"// 物料名称
				+ "item_specmodel," // 规格型号
				+ "unit_name," // 计量单位
				+ "apply_qty," // 需求数量
				+ "request_date," // 需求日期
				+ "apply_dept_id," // 需求部门ID
				+ "apply_dept," // 需求部门名称
				+ "sourse_flag," // 需求来源
				//+ "plan_num,"  //对应的MRP计划号
				+ "id," 
				+ "del_flag)"

				+ " values('" 
				+ mrpPlanID + "','"  // 单据号
				+ "MRP',"  // 单据类型
				+ "1,'" // 序号就按照1处理
				+ mrpplanList.get(0).get("b.id") + "','"//滚动计划中itemcode保存的是物料ID
				//+ mrpplanList.get(0).get("a.itemcode") + "','" // 物料编码
				+ mrpplanList.get(0).get("b.name") + "','" // 物料名称
				+ mrpplanList.get(0).get("b.spec_model") + "','"  // 规格型号
				+ mrpplanList.get(0).get("b.unit") + "'," // 计量单位
				+ mrpplanList.get(0).get("a.planqty") + ",'"  // 需求数量
				+ mrpplanList.get(0).get("a.planedate") + "','" // 需求日期
				+ o.getCode() + "','" // 部门代码
				+ o.getName() + "','" // 部门名称
				+ "3','" // 需求来源
				//+ mrpPlanID.trim() + "','"  //对应MRP计划号
				+ mrpPlanID.trim() + "','"  //UID
				+ "0')";  //

		logger.debug("   insertSql   " + insertSql);

		super.mapper.execInsertSql(insertSql);

	}

}