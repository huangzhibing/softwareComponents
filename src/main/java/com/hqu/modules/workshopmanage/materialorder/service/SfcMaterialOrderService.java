/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.materialorder.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.basedata.period.mapper.PeriodMapper;
import com.hqu.modules.inventorymanage.outsourcingoutput.service.OutsourcingOutputService;
import com.hqu.modules.workshopmanage.ppc.xhcUtil;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.time.DateFormatUtil;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.activiti.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.act.service.ActTaskService;
import com.jeeplus.modules.act.utils.ActUtils;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.workshopmanage.materialorder.entity.SfcMaterialOrder;
import com.hqu.modules.workshopmanage.materialorder.mapper.SfcMaterialOrderMapper;
import com.hqu.modules.workshopmanage.materialorder.entity.SfcMaterialOrderDetail;
import com.hqu.modules.workshopmanage.materialorder.mapper.SfcMaterialOrderDetailMapper;
//import com.hqu.modules.workshopmanage.processroutine.service.ProcessRoutineService.*;

/**
 * 领料单Service
 * @author zhangxin
 * @version 2018-05-22
 */
@Service
@Transactional(readOnly = true)
public class SfcMaterialOrderService extends CrudService<SfcMaterialOrderMapper, SfcMaterialOrder> {

	@Autowired
	private SfcMaterialOrderDetailMapper sfcMaterialOrderDetailMapper;

	@Autowired
	private SfcMaterialOrderMapper sfcMaterialOrderMapper;

	@Autowired
	private PeriodMapper periodMapper;
	
	@Autowired
	private ActTaskService actTaskService;
	
	@Autowired
	private IdentityService identityService;


	//删除当天的系统生成的且还没审核的领料单数据
	@Transactional(readOnly = false)
	public void deleteNoPassMaterialOrderWithDate(SfcMaterialOrder sfcMaterialOrder){
		super.executeDeleteSql("DELETE FROM sfc_materialordermainsingle;");
		super.executeDeleteSql("DELETE FROM sfc_materialorderdetailsingle;");
		/**
		 * 最新的处理方案已不需要删除存在的领料单，已经生成的领料单就不再集中生成了。
		 */
		//sfcMaterialOrderMapper.deleteNoPassMaterialOrderWithDateTime(sfcMaterialOrder);
	}


	/**
	 * 	是否存在领料日期为指定日期的系统生成的领料单
	 * 	存在领料单，但都是未审核的 0（只查找还没审核的领料单，即状态为P或者C）
	 * 	存在审核通过的领料单-1
	 * 	不存在指定日期的领料单1
	 */

	public int isExistNoPassMaterialOrder(SfcMaterialOrder sfcMaterialOrder){
        sfcMaterialOrder.setBillStateFlag("P");//编辑中
        //sfcMaterialOrder.setReceiveType("C");
        int pState=0;
        List<SfcMaterialOrder> sfcMaterialOrders=sfcMaterialOrderMapper.findList(sfcMaterialOrder);
        if(sfcMaterialOrders!=null){
            pState=sfcMaterialOrders.size();
        }
        int cState=0;
        sfcMaterialOrder.setBillStateFlag("C");//已提交
        sfcMaterialOrders=sfcMaterialOrderMapper.findList(sfcMaterialOrder);
        if(sfcMaterialOrders!=null){
            cState=sfcMaterialOrders.size();
        }

		int passState=0;
		sfcMaterialOrder.setBillStateFlag("S");//审核通过
		sfcMaterialOrders=sfcMaterialOrderMapper.findList(sfcMaterialOrder);
		if(sfcMaterialOrders!=null){
			passState=sfcMaterialOrders.size();
		}
		if(passState!=0){
			return -1;
		}
        if(cState!=0||pState!=0){
            return 0;
        }else {
            return 1;
        }

    }


	//设置领料单主表的默认值（手工新增领料单时调用）
	public void setDefalutNewMaterialOrder(SfcMaterialOrder sfcMaterialOrder) {
		//生成新的领料单号(SMOYYYYMMDD-流水号)(SMO+当前年月日+流水号)
		String tmpSql;
		String materialNo;
		String prefix = "SMO" + DateFormatUtil.formatDate("yyyyMMdd",new Date()) + "-";  //订单号前缀(SMOYYYYMMDD-)
		//读取领料单表格的最大中当前的最大序号,
		tmpSql = "select max(substring(materialorder,13)+0) from sfc_materialorder"
				+ " where substring(materialorder,1,12)='" + prefix + "'";
		List<Object> maxOrderList = super.executeSelectSql(tmpSql);
		if (maxOrderList.get(0) == null || maxOrderList.get(0).toString().equals("")) {
			//如果为空的话
			materialNo = prefix + "1";
		} else {
			int tmpNo = Double.valueOf(maxOrderList.get(0).toString()).intValue() + 1;
			materialNo = prefix + tmpNo;
		}
		sfcMaterialOrder.setMaterialOrder(materialNo);//设置领料单号
		sfcMaterialOrder.setId(materialNo);//设置领料单ID号（同为领料单号）

		sfcMaterialOrder.setRespName(UserUtils.getUser().getName());//领料人
		sfcMaterialOrder.setResponser(UserUtils.getUser().getId());//领料人id
		sfcMaterialOrder.setEditor(UserUtils.getUser().getName());//制单人
		sfcMaterialOrder.setEditorId(UserUtils.getUser().getId());//制单人id
		sfcMaterialOrder.setEditDate(new Date());//制定日期
		sfcMaterialOrder.setOperDate(new Date());//领料日期默认为当天
		sfcMaterialOrder.setQualityFlag("良品");
		sfcMaterialOrder.setReceiveType("M");//手工录入
		sfcMaterialOrder.setBillType("G");//领料
		sfcMaterialOrder.setShopId(UserUtils.getUserOfficeCode());//部门编码
		sfcMaterialOrder.setShopName(UserUtils.getUserOfficeName());//部门名称
		//sfcMaterialOrder.setWareName("不确定");
		//sfcMaterialOrder.setWareEmpname("不确定");

		//----------设定核算期-----------------
		Period period = new Period();
		period.setCurrentDate(new Date());
		period = periodMapper.getCurrentPeriod(period);
		if(period!=null){
			sfcMaterialOrder.setPeriodId(period.getPeriodId());
		}

		sfcMaterialOrder.setIsNewRecord(true);
	}

	
	public SfcMaterialOrder get(String id) {
		SfcMaterialOrder sfcMaterialOrder = sfcMaterialOrderMapper.get(id);
		if(sfcMaterialOrder!=null) {
			sfcMaterialOrder.setSfcMaterialOrderDetailList(sfcMaterialOrderDetailMapper.findList(new SfcMaterialOrderDetail(sfcMaterialOrder)));
		}
		return sfcMaterialOrder;
	}
	
	public List<SfcMaterialOrder> findList(SfcMaterialOrder sfcMaterialOrder) {
		return super.findList(sfcMaterialOrder);
	}
	
	public Page<SfcMaterialOrder> findPage(Page<SfcMaterialOrder> page, SfcMaterialOrder sfcMaterialOrder) {
		page=super.findPage(page, sfcMaterialOrder);
		//查询每个领料单所包含的子表是否存在缺料
		for(SfcMaterialOrder sfcMaterialOrder1:page.getList()){
            sfcMaterialOrder1.setIsLack("N");
		    if("R".equals(sfcMaterialOrder1.getBillType())){
				sfcMaterialOrder1.setIsLack("-");
				continue;
			}
			List<SfcMaterialOrderDetail> sfcMaterialOrderDetails=sfcMaterialOrderDetailMapper.findList(new SfcMaterialOrderDetail(sfcMaterialOrder1));
			for(SfcMaterialOrderDetail sfcMaterialOrderDetail:sfcMaterialOrderDetails){
				if("Y".equals(sfcMaterialOrderDetail.getIsLack())){
					sfcMaterialOrder1.setIsLack("Y");
					break;
				}
			}
		}
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(SfcMaterialOrder sfcMaterialOrder) {
		super.save(sfcMaterialOrder);
		for (SfcMaterialOrderDetail sfcMaterialOrderDetail : sfcMaterialOrder.getSfcMaterialOrderDetailList()){
			if (sfcMaterialOrderDetail.getId() == null){
				continue;
			}
			if (SfcMaterialOrderDetail.DEL_FLAG_NORMAL.equals(sfcMaterialOrderDetail.getDelFlag())){
				if (StringUtils.isBlank(sfcMaterialOrderDetail.getId())){
					sfcMaterialOrderDetail.setMaterialOrder(sfcMaterialOrder);
					sfcMaterialOrderDetail.preInsert();
					sfcMaterialOrderDetailMapper.insert(sfcMaterialOrderDetail);
				}else{
					sfcMaterialOrderDetail.preUpdate();
					sfcMaterialOrderDetailMapper.update(sfcMaterialOrderDetail);
				}
			}else{
				sfcMaterialOrderDetailMapper.delete(sfcMaterialOrderDetail);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(SfcMaterialOrder sfcMaterialOrder) {
		super.delete(sfcMaterialOrder);
		sfcMaterialOrderDetailMapper.delete(new SfcMaterialOrderDetail(sfcMaterialOrder));
	}
	
	@Transactional(readOnly = false)
	public void submit(SfcMaterialOrder sfcMaterialOrder) {
		String sql = "update sfc_materialorder set billstateflag='W' where materialorder='"+sfcMaterialOrder.getMaterialOrder()+"'";
		super.executeUpdateSql(sql);
		
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(sfcMaterialOrder.getCurrentUser().getLoginName());
		// 启动流程
		actTaskService.startProcess(ActUtils.PD_TEST_AUDIT[0], ActUtils.PD_TEST_AUDIT[1], sfcMaterialOrder.getId(), sfcMaterialOrder.getMaterialOrder());
	}

    /**
     * 退补料程序：
     * 1.生成退料单，采用负的数量和负的金额形式；
     * 2.补料单，走正常的领料程序
     *
     * @param materialOrderNo  领料单主表中领料单号
     * @param sequenceID 子表中的序号，之所以用序号而不是物料编码，是为了避免子表中存在重名物料的问题
     * @param returnQty 退料数量
     */
	@Transactional(readOnly = false)
	public void returnAndGetMaterial(String materialOrderNo,int sequenceID,int returnQty,String quality) {
		/******************生成退料单*************************/
		returnOldMaterial(materialOrderNo,sequenceID,returnQty,quality);

		/*****************生成新的领料单*********************/
		getNewMaterial(materialOrderNo,sequenceID,returnQty);
	}



	/**
	 * 退补料程序：生成退料单
	 * @param materialOrderNo  领料单主表中领料单号
	 * @param sequenceID 子表中的序号，之所以用序号而不是物料编码，是为了避免子表中存在重名物料的问题
	 * @param returnQty 领料数量
	 */
	@Transactional(readOnly = false)
	public void returnOldMaterial(String materialOrderNo,int sequenceID,int returnQty,String quality){

		/****************处理退料单主表*****************/
		//1.根据领料单主表中领料单号，获取领料单的ID
		String attributes = "id";
		String selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
				+ ") from sfc_materialorder a"
				+ " where a.materialorder='" + materialOrderNo + "'";
		String attrFormat = attributes;
		List<Map<String, String>> idList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");

		SfcMaterialOrder sfcMaOrder = new SfcMaterialOrder();
		sfcMaOrder = sfcMaterialOrderMapper.get(idList.get(0).get("id")); //将原有的计划信息读取出来

		//2.生成新的领料单号
		//读取领料单表格中当前的最大序号,生成新的序号
		String prefix = sfcMaOrder.getMaterialOrder().substring(0,12);
		selectSql = "select max(substring(materialorder,13)+0) from sfc_materialorder"
				+ " where substring(materialorder,1,12)='" + prefix + "'";
		//logger.debug("  找到最大的当前序号 " + selectSql);
		List<Object> maxOrderList = super.executeSelectSql(selectSql);

		if (maxOrderList.get(0) == null || maxOrderList.get(0).toString().equals("")) {
			//如果为空的话
			sfcMaOrder.setMaterialOrder(prefix + "1");
		} else {
			int tmpNo = Double.valueOf(maxOrderList.get(0).toString()).intValue() + 1;
			sfcMaOrder.setMaterialOrder(prefix + tmpNo);
		}

		//单据类型：退料R
		sfcMaOrder.setBillType("R");
		sfcMaOrder.setBillStateFlag("C");//退补料的领料单自动提交到领料单审核
        sfcMaOrder.setReceiveType("C");//单据为自动生成类型
		sfcMaOrder.setQualityFlag(quality);//设置退料为良品或者不良品

		//领料人代码、领料人姓名、制单人编码、制单人名称、制单日期
		sfcMaOrder.setResponser(UserUtils.getUser().getId());
		sfcMaOrder.setRespName(UserUtils.getUser().getName());
		sfcMaOrder.setEditorId(UserUtils.getUser().getId());
		sfcMaOrder.setEditor(UserUtils.getUser().getName());
		sfcMaOrder.setEditDate(new Date());


		//设置框架自动生成的字段
		sfcMaOrder.setId(sfcMaOrder.getMaterialOrder());
		sfcMaOrder.setRemarks("");
		sfcMaOrder.setCreateBy(UserUtils.getUser());
		sfcMaOrder.setCreateDate(new Date());
		sfcMaOrder.setUpdateBy(UserUtils.getUser());
		sfcMaOrder.setUpdateDate(new Date());
		sfcMaOrder.setDelFlag("0");

		//退料对应领料单号
		sfcMaOrder.setMaterialOrderInReturn(materialOrderNo);

		//存入主表数据
		sfcMaterialOrderMapper.insert(sfcMaOrder);



		/**************生成附表数据******************************/

		//1.根据领料单号和序号，获取领料单子表的ID
		attributes = "id";
		selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
				+ ") from sfc_materialorderdetail"
				+ " where materialorderdetail='" + materialOrderNo + "' and "
				+ " sequenceid=" + sequenceID;
		attrFormat = attributes;
		idList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");

		//根据ID读取子表数据
		SfcMaterialOrderDetail sfcMaterialOrderDetail = new SfcMaterialOrderDetail();
		sfcMaterialOrderDetail=sfcMaterialOrderDetailMapper.get(idList.get(0).get("id"));

		//修改子表中的数量
		sfcMaterialOrderDetail.setRequireNum(returnQty*-1.0);

		//修改sequenceID，为1
		sfcMaterialOrderDetail.setSequenceId(1);

		//修改框架要用到的东西
		sfcMaterialOrderDetail.setId(sfcMaOrder.getMaterialOrder() + sfcMaterialOrderDetail.getMaterialId());
		sfcMaterialOrderDetail.setMaterialOrder(sfcMaOrder); //主表中的外键
		sfcMaterialOrderDetail.setmaterialOrderDetail(sfcMaOrder.getMaterialOrder()); //主表中的外键

		sfcMaterialOrderDetailMapper.insert(sfcMaterialOrderDetail);


		/******************同库存接口*****************/
		//用来保存主表entity中的子表List数据，同库存做接口生成出库单
		List<SfcMaterialOrderDetail> sfcMaterialOrderDetailList = new ArrayList<>();
		sfcMaterialOrderDetailList.add(sfcMaterialOrderDetail);

		//写入主表中附表的List数据
		sfcMaOrder.setSfcMaterialOrderDetailList(sfcMaterialOrderDetailList);

		// 调用库存提供的接口，完成出库操作
		// 根据接口给出的返回值，回填出库单号，已完成追溯操作。
	/*	String billNum = outsourcingOutputService.transferMaterialOrder(sfcMaOrder);
		//logger.debug("   billNum   " + billNum);

		String tmpSql = "update sfc_materialorder "
				+ " set InvBillNo='" + billNum +"'"
				+ " where materialorder='" + sfcMaOrder.getMaterialOrder() +"'";
		sfcMaterialOrderDetailMapper.execInsertSql(tmpSql);*/


	}


	/**
	 * 退补料程序：生成新的领料单
	 * @param materialOrderNo  领料单主表中领料单号
	 * @param sequenceID 子表中的序号，之所以用序号而不是物料编码，是为了避免子表中存在重名物料的问题
	 * @param getQty 领料数量
	 */
	public void getNewMaterial(String materialOrderNo,int sequenceID,int getQty){
		/****************处理新领料单的主表*****************/
		//1.根据领料单主表中领料单号，获取领料单的ID
		String attributes = "id";
		String selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
				+ ") from sfc_materialorder a"
				+ " where a.materialorder='" + materialOrderNo + "'";
		String attrFormat = attributes;
		List<Map<String, String>> idList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");

		SfcMaterialOrder sfcMaOrder = new SfcMaterialOrder();
		sfcMaOrder = sfcMaterialOrderMapper.get(idList.get(0).get("id")); //将原有的计划信息读取出来
		//logger.debug("  测试下类的读取是否好使 " + sfcMaOrder.toString());

		//2.生成新的领料单号
		//读取领料单表格中当前的最大序号,生成新的序号
		String prefix = sfcMaOrder.getMaterialOrder().substring(0,12);
		selectSql = "select max(substring(materialorder,13)+0) from sfc_materialorder"
				+ " where substring(materialorder,1,12)='" + prefix + "'";
		//logger.debug("  找到最大的当前序号 " + selectSql);
		List<Object> maxOrderList = super.executeSelectSql(selectSql);

		if (maxOrderList.get(0) == null || maxOrderList.get(0).toString().equals("")) {
			//如果为空的话
			sfcMaOrder.setMaterialOrder(prefix + "1");
		} else {
			int tmpNo = Double.valueOf(maxOrderList.get(0).toString()).intValue() + 1;
			sfcMaOrder.setMaterialOrder(prefix + tmpNo);
		}

		//单据类型：领料G
		sfcMaOrder.setBillType("G");
        sfcMaOrder.setBillStateFlag("C");//退补料处理的领料单自动提交到领料单审核
		sfcMaOrder.setReceiveType("C");//单据为自动生成类型
        sfcMaOrder.setQualityFlag("良品");//领料单中良率标志均为良品C

        //领料人代码、领料人姓名、制单人编码、制单人名称、制单日期
		sfcMaOrder.setResponser(UserUtils.getUser().getId());
		sfcMaOrder.setRespName(UserUtils.getUser().getName());
		sfcMaOrder.setEditorId(UserUtils.getUser().getId());
		sfcMaOrder.setEditor(UserUtils.getUser().getName());
		sfcMaOrder.setEditDate(new Date());


		//设置框架自动生成的字段
		sfcMaOrder.setId(sfcMaOrder.getMaterialOrder());
		sfcMaOrder.setRemarks("");
		sfcMaOrder.setCreateBy(UserUtils.getUser());
		sfcMaOrder.setCreateDate(new Date());
		sfcMaOrder.setUpdateBy(UserUtils.getUser());
		sfcMaOrder.setUpdateDate(new Date());
		sfcMaOrder.setDelFlag("0");

		//退料对应领料单号
		sfcMaOrder.setMaterialOrderInReturn(materialOrderNo);

		//存入主表数据
		sfcMaterialOrderMapper.insert(sfcMaOrder);



		/**************生成附表数据******************************/

		//1.根据领料单号和序号，获取领料单子表的ID
		attributes = "id";
		selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
				+ ") from sfc_materialorderdetail"
				+ " where materialorderdetail='" + materialOrderNo + "' and "
				+ " sequenceid=" + sequenceID;
		attrFormat = attributes;
		idList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");

		//根据ID读取子表数据
		SfcMaterialOrderDetail sfcMaterialOrderDetail = new SfcMaterialOrderDetail();
		sfcMaterialOrderDetail=sfcMaterialOrderDetailMapper.get(idList.get(0).get("id"));

		//修改子表中的数量
		sfcMaterialOrderDetail.setRequireNum(getQty+0.0);

		//修改sequenceID，为1
		sfcMaterialOrderDetail.setSequenceId(1);

		//修改框架要用到的东西
		sfcMaterialOrderDetail.setId(sfcMaOrder.getMaterialOrder() + sfcMaterialOrderDetail.getMaterialId());
		sfcMaterialOrderDetail.setMaterialOrder(sfcMaOrder); //主表中的外键
		sfcMaterialOrderDetail.setmaterialOrderDetail(sfcMaOrder.getMaterialOrder()); //主表中的外键

		sfcMaterialOrderDetailMapper.insert(sfcMaterialOrderDetail);


		/******************同库存接口*****************/
		//用来保存主表entity中的子表List数据，同库存做接口生成出库单
		List<SfcMaterialOrderDetail> sfcMaterialOrderDetailList = new ArrayList<>();
		sfcMaterialOrderDetailList.add(sfcMaterialOrderDetail);

		//写入主表中附表的List数据
		sfcMaOrder.setSfcMaterialOrderDetailList(sfcMaterialOrderDetailList);

		/*// 调用库存提供的接口，完成出库操作
		// 根据接口给出的返回值，回填出库单号，已完成追溯操作。
		String billNum = outsourcingOutputService.transferMaterialOrder(sfcMaOrder);
		//logger.debug("   billNum   " + billNum);

		String tmpSql = "update sfc_materialorder "
				+ " set InvBillNo='" + billNum +"'"
				+ " where materialorder='" + sfcMaOrder.getMaterialOrder() +"'";
		sfcMaterialOrderDetailMapper.execInsertSql(tmpSql);*/
	}


	/**
	 * 判断领料单缺料情况
	 *
	 * 思路：
	 * 1.找到所有领料单中单据类型为G的（领料）
	 * 2.根据领料单中的出库单据号信息，在出库单中找到所有状态为“新增”和“待过账”的单据
	 * （其余类型的单据是已经处理完的）
	 * 3.计算所有的单据需求数量
	 */
	@Transactional(readOnly = false)
	public void judgeMaterialLack(){
        //sfcMaterialOrderDetailMapper.execInsertSql("update sfc_materialorderdetail set isLack='N',lackQty=0");
		/*************找到领料单中所有需要计算是否缺料的单据，满足如下条件：**************/
		//1.单据类型为G（领料），既不考虑退料情况
		//2.通过出库单单据号字段，连接出库单单单据；
		//3.出库单中只考虑单据状态为“新增”和“待过账”两种状态


		//1.首先找到需要计算的领料单（其对应的出库单过账状态为“新增”或者“待过账”，表示正在处理）
		String attributes = "a.materialOrder,a.InvBillNo,a.operdate,b.requirenum,b.materialid,b.sequenceid";
		String selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
				+ ") from sfc_materialorder a,sfc_materialorderdetail b"
				+ " where a.materialorder=b.materialorderdetail and "
				+ " a.InvBillNo in (select bill_num from inv_billmain where bill_flag='A' or bill_flag='N')"
				+ " and a.billtype='G'"
				+ " order by b.materialid,a.operdate";
		String attrFormat = attributes;
		logger.debug("  查询领料单程序SQL   " + selectSql);
		List<Map<String, String>> maOrderList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");


		//为下面的计算进行初始化计算
		String tmpMaID="########"; //用来保存当前物料编码
		int allocatedQty=0; //保存已经分配的数量
		int curQty=0;//保存当前库存量
		int availableQty=0;//保存可用量

		//计算当前最大的期别
		attributes = "a.year,a.period";
		selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
				+ ") from inv_account a"
				+ " order by a.year,a.period DESC";
		attrFormat = attributes;
		List<Map<String, String>> curYearAndPeriodList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");
		String curYear=curYearAndPeriodList.get(0).get("a.year");
		String curPeriod=curYearAndPeriodList.get(0).get("a.period");


		/**********2.逐一处理每条领料单************/
		for(Map<String, String> maOrder : maOrderList){
			/***********2.1 如果物料编码首次出现，则计算在库量和已分配量**************/
			if (!maOrder.get("b.materialid").equals(tmpMaID)){
				tmpMaID=maOrder.get("b.materialid"); //修改当前物料编码

				/* 计算出库单中已经分配“待过账”的数量 */
				selectSql = "select sum(b.item_qty) from inv_billmain a,inv_billdetail b"
						+ " where a.id=b.bill_num and a.bill_flag='N'"
						+ " and b.item_code='" + tmpMaID + "'";
				List<Object> allocatedQtyList= super.executeSelectSql(selectSql);
				if (allocatedQtyList==null || allocatedQtyList.get(0)==null)
					allocatedQty=0;
				else
					allocatedQty=Double.valueOf(allocatedQtyList.get(0).toString()).intValue(); //得到总的已分配量
				logger.debug("  查询出库单数量的SQL   " + selectSql);
				logger.debug("  已经分配数量   " + allocatedQty);


				/* 计算物料在库存中的现有量 */
				selectSql = "select sum(now_qty) from inv_account"
						+ " where year='" + curYear + "'"
						+ " and period='" + curPeriod + "'"
						+ " and item_code='" + tmpMaID + "'";
				List<Object> curQtyList= super.executeSelectSql(selectSql);
				if(curQtyList.get(0)!=null){//防止库存数量为空而报错{
					curQty = Double.valueOf(curQtyList.get(0).toString()).intValue(); //得到当前库存量
				}
				//logger.debug("  查询在库数量的SQL   " + selectSql);
				logger.debug("  在库量   " + curQty);

				availableQty=curQty-allocatedQty;  //保存现有可用量
			}

			/*************2.2 判断是否缺料******************/


			//查询当前单据的出库单状态和已分配数量
			attributes = "a.bill_flag,b.item_qty";
			selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
					+ ") from inv_billmain a,inv_billdetail b"
					+ " where a.id=b.bill_num and a.bill_num='" + maOrder.get("a.InvBillNo") + "'";
			attrFormat = attributes;
			List<Map<String, String>> billFlagList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");
			logger.debug("  查询出库单状态的SQL   " + selectSql);
			logger.debug("  出库单状态   " + billFlagList.get(0).get("a.bill_flag"));
			logger.debug("  出库单数量   " + billFlagList.get(0).get("b.item_qty"));


			String isLack="N";
			int lackQty=0;

			//情况1：如果出库单状态为N，待审核
			if (billFlagList.get(0).get("a.bill_flag").equals("N")){
				//情况1.1：如果出库数量不小于申领数量，则不缺料
				if (Integer.valueOf(billFlagList.get(0).get("b.item_qty"))>=Integer.valueOf(maOrder.get("b.requirenum"))){
					isLack="N";
					lackQty=0;
				}else{//情况1.2：如果出库数量小于申领数量，则缺料，此时意味着虽然已经生成出库单，但是数量不够
					isLack="Y";
					lackQty=Integer.valueOf(maOrder.get("b.requirenum"))-Integer.valueOf(billFlagList.get(0).get("b.item_qty"));
				}
				logger.debug("  缺料状态   " + isLack);
				logger.debug("  缺料数量   " + lackQty);
			}
			//情况2：出库单状态为A(新增)，则计算可用量，需求量<=可用量，则认为不缺料，否则认为缺料。修改领料单缺料状态和数量
			else{
				//情况2.1：需求量<=可用量
				if(Integer.valueOf(maOrder.get("b.requirenum"))<=availableQty){
					isLack="N";
					lackQty=0;
					availableQty=availableQty-Integer.valueOf(maOrder.get("b.requirenum"));
					logger.debug("  缺料状态2.1   " + isLack);
					logger.debug("  缺料数量2.1   " + lackQty);
					logger.debug(" 可用量状态2.1   " + availableQty);
				}else{ //情况2.2：需求量>可用量
					isLack="Y";
					if (availableQty>0){
						lackQty= Integer.valueOf(maOrder.get("b.requirenum"))-availableQty;
						availableQty=0;  //后面如果再有物料的话，则不用此量
						logger.debug("  缺料状态2.2.1   " + isLack);
						logger.debug("  缺料数量2.2.1   " + lackQty);
						logger.debug(" 可用量状态2.2.1   " + availableQty);
					}
					else {
						lackQty = Integer.valueOf(maOrder.get("b.requirenum"));
						logger.debug("  缺料状态2.2.2   " + isLack);
						logger.debug("  缺料数量2.2.2   " + lackQty);
						logger.debug(" 可用量状态2.2.2   " + availableQty);
					}
				}


			}

			/*************更新领料单子表数据中的是否缺料和数量信息***************/
			String updateSql = "update sfc_materialorderdetail"
					+ " set isLack='" + isLack +"',"
					+ " lackQty=" + lackQty
					+ " where materialorderdetail='" + maOrder.get("a.materialOrder") +"'"
					+ " and sequenceid=" + maOrder.get("b.sequenceid");
			logger.debug(" 更新缺料数量的SQL   " + updateSql);
			sfcMaterialOrderDetailMapper.execInsertSql(updateSql);
		}





	}

}

