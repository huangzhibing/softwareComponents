/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.ppc.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hqu.modules.workshopmanage.ppc.entity.MSerialNo;
import com.hqu.modules.workshopmanage.ppc.entity.MrpPlan;
import com.hqu.modules.workshopmanage.ppc.entity.MSerialNo;
import com.hqu.modules.workshopmanage.ppc.mapper.MrpPlanMapper;
import com.hqu.modules.workshopmanage.ppc.mapper.MSerialNoMapper;
import com.hqu.modules.workshopmanage.ppc.xhcUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.time.DateUtil;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.workshopmanage.ppc.entity.MpsPlan;

import com.hqu.modules.workshopmanage.ppc.mapper.MpsPlanMapper;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * MPSPlanService
 * 
 * @author XHC
 * @version 2018-05-31
 */
@Service
@Transactional(readOnly = true)
public class MpsPlanService extends CrudService<MpsPlanMapper, MpsPlan> {

	@Autowired
	private MrpPlanMapper mrpPlanMapper;

	@Autowired
	private MSerialNoMapper mSerialNoMapper;

	@Autowired
	private MSerialNoService mSerialNoService;

	public MpsPlan get(String id) {
		return super.get(id);
	}

	/**
	 * 通过Mps id 获取MpsPlan对象（包含了该Mps对应的Mrp列表）
	 *
	 * @param id
	 * @return
	 */
	public MpsPlan getMRPbyMPS(String id) {
		MpsPlan mpsPlan = super.get(id);
		MrpPlan mrpPlan = new MrpPlan();
		mrpPlan.setMPSplanid(mpsPlan.getMpsPlanid());
		mpsPlan.setMrpPlanList(mrpPlanMapper.findList(mrpPlan));//获取该Mps对应的所有Mrp;
		return mpsPlan;
	}

	public List<MpsPlan> findList(MpsPlan mpsPlan) {
		return super.findList(mpsPlan);
	}

	public List<MpsPlan> findMyList(MpsPlan mpsPlan) {
		return mapper.findMyList(mpsPlan);
	}

	public Page<MpsPlan> findPage(Page<MpsPlan> page, MpsPlan mpsPlan) {
		return super.findPage(page, mpsPlan);
	}

	/**
	 * 状态为待审核C的主生产计划分页数据（MPS审核列表）
	 */
	public Page<MpsPlan> findAuditPage(Page<MpsPlan> page, MpsPlan mpsPlan) {
		dataRuleFilter(mpsPlan);
		mpsPlan.setPage(page);
		page.setList(mapper.findAuditList(mpsPlan));
		return page;
	}

	/**
	 * MRPDealStatus状态为N或者P 且计划状态planstatus为下达S的主生产计划分页数据(MRP编辑列表)
	 */
	public Page<MpsPlan> findMRPDealPage(Page<MpsPlan> page, MpsPlan mpsPlan) {
		dataRuleFilter(mpsPlan);
		mpsPlan.setPage(page);
		page.setList(mapper.findMRPDealList(mpsPlan));
		return page;
	}

	/**
	 * MRPDealStatus状态为提交C 且计划状态planstatus为下达S的主生产计划分页数据（MRP审核列表）
	 */
	public Page<MpsPlan> findMRPAuditPage(Page<MpsPlan> page, MpsPlan mpsPlan) {
		dataRuleFilter(mpsPlan);
		mpsPlan.setPage(page);
		page.setList(mapper.findMRPAuditList(mpsPlan));
		return page;
	}

	@Transactional(readOnly = false)
	public void save(MpsPlan mpsPlan) {
		super.save(mpsPlan);
		//主生产计划提交时，生成机器序列码
		if("C".equals(mpsPlan.getPlanStatus())) {
			generateMSerialNo(mpsPlan.getMpsPlanid());
		}

	}

	@Transactional(readOnly = false)
	public void delete(MpsPlan mpsPlan) {
		super.delete(mpsPlan);
	}

	/**
	 * 从内部定单表格中读取数据到MPS_plan表中 1.定单状态orderstat为W 2.在mps_plan表格中尚未出现过的
	 * 3.写入时产生mps_planid 4.为方便处理，在mps_planid表格中，将内部订单号和序号合并到一起处理
	 */
	@Transactional(readOnly = false)
	public void getInnerSalOrder() {

		//1.从内部定单主表和附表中读取数据，尚未导入到mps_plan表格中的数据
		String attributes = "a.order_code,order_stat,send_date,b.seq_id,b.prod_code,b.prod_name,b.prod_qty";//此处的属性字段已依据销售系统中已建立的内部订单子表更正 2018/6/12
		String selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
				+ ") FROM sal_order a, sal_order_item b "     //销售系统中已建立的内部订单子表名为sal_order_item,且b.order_code字段存的是sal_order的id  2018/6/12
				+ " where a.id=b.order_code and CONCAT_WS('##',a.order_code,b.seq_id) not in (select reqid from ppc_mpsplan)"
				+ " and order_stat='W'";
		//logger.debug("    选择销售订单     "    +selectSql);
		String attrFormat = "ordercode,orderstat,senddate,seqid,prodcode,prodname,prodqty";
		List<Map<String, String>> list = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");

		/* 2.生成主生产计划号
		* 计划号格式：“MPS”+“YYYYMM”+“-”+5位序号（不需要如此长，为了同机器二维码40位长度对应上）
		* */
		String newPlanid = "";
		String prefix = "MPS" + DateUtils.getYear() + DateUtils.getMonth() +"-";
		String tmpSql = "select max(right(mpsplanid,5)+0) from ppc_mpsplan where left(mpsplanid,10)='" + prefix + "'";
		logger.debug("   选择计划号的SQL：   " + tmpSql);
		List<Object> maxId = super.executeSelectSql(tmpSql); // 读取当月最大的序号值
		int idNum;
		if (maxId.get(0) == null) { // 说明没有当月的计划，则序号设置为1
			idNum = 1;
			// logger.debug(newPlanid);
		} else {// 序号加1
			idNum = ((Double) maxId.get(0)).intValue();
			idNum++;
		}
		newPlanid = prefix + String.format("%05d", idNum);
		//logger.debug("   新的计划号是：   " + newPlanid);

		for (Map<String, String> ma : list) {
			if (ma != null) {
				MpsPlan mpsplan = new MpsPlan(); // 生成新的要插入的MpsPlan

				/* 生成主生产计划号 */
				mpsplan.setMpsPlanid(newPlanid);

				// 处理其他字段
				mpsplan.setProdCode(ma.get("prodcode"));
				mpsplan.setProdName(ma.get("prodname"));
				mpsplan.setPlanQty(Integer.valueOf(ma.get("prodqty")));
				mpsplan.setPlanBDate(DateUtils.addDays(new Date(), 1));  //此处后续需要根据能力平衡结果进行优化，目前暂时设置为读取日期+1
				mpsplan.setPlanEDate(DateUtils.parseDate(ma.get("senddate")));
				mpsplan.setInvAllocatedQty(0);
				mpsplan.setFinishQty(0);
				mpsplan.setSourceFlag("O");
				mpsplan.setReqID(ma.get("ordercode") + "##" + ma.get("seqid"));
				mpsplan.setPlanStatus("P");
				mpsplan.setMRPDealStatus("N");
				mpsplan.setSFCDealStatus("N");
				mpsplan.setAssignedQty(0);
				mpsplan.setMakeDate(new Date());
				mpsplan.setMakePID((UserUtils.getUser()).getId());

				//设置框架自动生成的字段
				mpsplan.setId(newPlanid);
				mpsplan.setRemarks("");
				mpsplan.setCreateBy(UserUtils.getUser());
				mpsplan.setCreateDate(new Date());
				mpsplan.setUpdateBy(UserUtils.getUser());
				mpsplan.setUpdateDate(new Date());
				mpsplan.setDelFlag("0");

				super.mapper.insert(mpsplan);
				newPlanid = prefix + String.format("%05d", ++idNum);//(++idNum); mps位数bug 2019/1/3

			}
		}
	}

	/**
	 * 产生每年每周的编号和每周周一和周日日期
	 */
	@Transactional(readOnly = false)
	public void generateWeeks(){
		//产生2000年到2050年的数据
		int weekNo;
		Date tmpDate=new Date();
		String insertSQL="";

		//处理每一年的数据
		for(int year=2000;year<=2050;year++){

			Date firstDayofYear= xhcUtil.newDate(year + "-01-01");  //得到year的第1天日期
			if (DateUtil.getDayOfWeek(firstDayofYear)==1){  //判断每年的第1天是否是周一，以确定每年第1周的开始日期
				tmpDate=firstDayofYear;
			}else
				tmpDate=DateUtil.addDays(firstDayofYear, 7 - DateUtil.getDayOfWeek(firstDayofYear) + 1);

			weekNo=1;
			for (; ; ) {
				insertSQL = "insert into ppc_week("
						+ "year,"
						+ "weekno,"
						+ "monday,"
						+ "sunday)"

						+ " values('"
						+ year + "',"  //年号
						+ weekNo + ",'" //周号
						+ DateUtils.formatDate(tmpDate,"yyyyMMdd")+ "','"  //周一日期
						+ DateUtils.formatDate(DateUtil.addDays(tmpDate, 6), "yyyyMMdd")+ "')";//周日日期
				//logger.debug(" week insert" + insertSQL);
				super.mapper.execInsertSql(insertSQL);

				weekNo++;
				tmpDate = DateUtil.addDays(tmpDate, 7);
				//logger.debug("  tmpDate " + tmpDate.toString());

				//得到要处理周所在的年，如果不等于当前年，说明该年已经处理结束，跳出循环，处理下一年的数据
				String curYear=String.format("%tY", tmpDate);
				if (Integer.valueOf(curYear).intValue()!=year)
					break;
			}

		}

	}



	/**
	 * 根据提供的主生产计划号，生成机器序列号。将MPS计划中的生产数量拆分到单件，每件一个机器序列号
	 *  在主生产计划提交时执行此程序。
	 */
	 
	@Transactional(readOnly = false)
	public void generateMSerialNo(String mpsPlanID) {

		// 从主生产计划表格中根据主生产计划号，读取主生产计划信息
		String attributes = "a.mpsplanid,a.prodcode,a.prodname,a.planqty";
		String tmpSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
				+ ") FROM ppc_mpsplan a"
				+ " where mpsplanid='" + mpsPlanID + "'";				
		//logger.debug(" 主生产计划信息    " + tmpSql);
		String attrFormat = "mpsplanid,prodcode,prodname,planqty";
		List<Map<String, String>> mpsPlanInfoList = xhcUtil.resultToList(super.executeSelectSql(tmpSql), attrFormat, "###");
		
		for (Map<String, String> ma : mpsPlanInfoList) {  //应该只有一条记录，用循环避免判断是否为空的问题。
			

			//机器编号生成规则：6位日期+4位型号+4位流水号(日期为4位年2位周，依据主生产计划的提交日期确定；型号来自产品表)

			//1.获取周号
			String curDate = DateUtils.getYear() + DateUtils.getMonth() + DateUtils.getDay();
			attributes = "a.year,a.weekno,a.monday,a.sunday";
			tmpSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
					+ ") FROM ppc_week a "
					+ " where monday<='" + curDate + "' and sunday>='" + curDate + "'";
			//logger.debug(" 周号SQL    " + tmpSql);
			attrFormat = attributes;
			List<Map<String, String>> weekNoList = xhcUtil.resultToList(super.executeSelectSql(tmpSql), attrFormat, "###");
			//得到周号，2位，左侧补充0
			String weekNo = String.format("%02d", Integer.valueOf(weekNoList.get(0).get("a.weekno")).intValue());

			//2.获取产品型号
			//code的前4位
			String productSpec = ma.get("prodcode").substring(0, 4);

			//3.生成机器序列号前缀
			String prefix = DateUtils.getYear() + weekNo + productSpec;
			//logger.debug(" 机器序列号前缀    " + prefix);

			//4.根据前缀读取当前最大的序号
			tmpSql = "select max(right(mserialno,4)+0) from ppc_mserialno where left(mserialno,10)='" + prefix + "'";
			List<Object> maxNo = super.executeSelectSql(tmpSql); // 读取最大的序号值
			int curMaxNo;
			if (maxNo.get(0) == null) { // 说明没有当月的计划，则序号设置为1
				curMaxNo = 1;
			} else {// 序号加1
				curMaxNo = ((Double) maxNo.get(0)).intValue();
				curMaxNo++;
			}
			logger.debug(" 机器序列号最大序号  " + curMaxNo);

			/*
			//5.读取机器二维码最大序号
			// “15位主生产计划单据号+12位物料号+8位批次号（主生产计划日期）+5位流水号”共计40位组成
			String prefixObjSN =ma.get("mpsplanid")+ ma.get("prodcode")
				+ DateUtils.getYear()+DateUtils.getMonth()+DateUtils.getDay();
			tmpSql = "select max(right(obj_sn,5)+0) from ppc_mserialno where left(obj_sn,35)='" + prefixObjSN + "'";
			logger.debug(" 最大二维码序号  " + tmpSql);
			List<Object> maxObjSN = super.executeSelectSql(tmpSql); // 读取二维码最大的序号

			int curMaxObjSN;
			if (maxObjSN.get(0) == null) { // 没有，序号设置为1
				curMaxObjSN = 1;
			} else {// 序号加1
				curMaxObjSN = ((Double) maxObjSN.get(0)).intValue();
				curMaxObjSN++;
			}
			logger.debug(" 机器序列号最大序号  " + curMaxObjSN);
			*/

			//获得生产数量，然后为每一个产品生成出一个机器序列号
			int planQty = Integer.valueOf(ma.get("planqty")).intValue();
			logger.debug(" 生产数量 " + planQty);

			String NA="N/A";  //暂时无法填写的字段用此值来填写

			if(planQty>=1) {
				for (int i=0;i<planQty;i++){
					//循环生成机器序列号
					String serialNo = prefix + String.format("%04d", curMaxNo);
					logger.debug(" 机器序列号 " + serialNo);
					curMaxNo++;

					/*
					//生成机器二维码：
					// “15位主生产计划单据号+12位物料号+8位批次号（主生产计划日期）+5位流水号”共计40位组成
					String objSn=prefixObjSN+String.format("%05d",curMaxObjSN);
					curMaxObjSN++;
					//logger.debug(" 机器二维码 " + mSerialNo.getObjSn());*/
					String insertSQL = "insert into ppc_mserialno("
							+ "mserialno,"
							+ "id,"
							+ "mpsplanid,"
							+ "processbillno,"
							+ "batchno,"
							+ "routinebillno,"
							+ "routingcode,"
							+ "seqno,"
							+ "isassigned,"
							+ "prodcode,"
							+ "prodname,"
							+ "obj_sn)"

							+ " values('"
							+ serialNo + "','"  //机器序列号
							+ serialNo + "','" //
							+ ma.get("mpsplanid")+ "','"  //主生产计划号
							+ "N/A" + "'," //车间作业计划号
							+ "0"  + ",'"// 批次号
							+ "N/A" + "','" //加工路线单号
							+ "N/A"  + "',"// 工艺代码
							+ "0"  + ",'"// 单间序号
							+ "N"  + "','"// 是否安排完成
							+ ma.get("prodcode")  + "','" //产品编码
							+ ma.get("prodname")  + "','" //产品名称
							+ "N/A"+ "')" ;//产品二维码


					logger.debug(" week insert" + insertSQL);
					super.mapper.execInsertSql(insertSQL);

					/*  不知道为什么，save方法不好使，时间关系，利用insert 语句搞定
					MSerialNo mSerialNo = new MSerialNo();
					mSerialNo.setMserialno(serialNo);
					mSerialNo.setId(serialNo);
					mSerialNo.setMpsplanid(ma.get("mpsplanid"));
					mSerialNo.setProcessbillno(NA);
					mSerialNo.setBatchno(0);
					mSerialNo.setRoutinebillno(NA);
					mSerialNo.setRoutingcode(NA);
					mSerialNo.setSeqno(0);
					mSerialNo.setIsassigned("N");
					mSerialNo.setProdcode(ma.get("prodcode"));
					mSerialNo.setProdname(ma.get("prodname"));
					*/
					//生成机器二维码：
					// “15位主生产计划单据号+12位物料号+8位批次号（主生产计划日期）+5位流水号”共计40位组成
					//mSerialNo.setObjSn(prifixObjSN+String.format("%05d",curMaxObjSN));
					//curMaxObjSN++;
					//logger.debug(" 机器二维码 " + mSerialNo.getObjSn());

					//super.mapper.insert(mSerialNo);
					//mSerialNoService.save(mSerialNo);//save(mSerialNo);

				}

			}
		}
		
	}

}