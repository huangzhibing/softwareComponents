package com.hqu.modules.workshopmanage.processroutine.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hqu.modules.workshopmanage.processmaterial.entity.ProcessMaterial;
import com.hqu.modules.workshopmanage.processmaterial.mapper.ProcessMaterialMapper;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.time.DateUtil;
import com.jeeplus.modules.sys.entity.User;

import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetail;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.workshopmanage.ppc.xhcUtil;
import com.hqu.modules.workshopmanage.processbatch.entity.ProcessBatch;
import com.hqu.modules.workshopmanage.processbatch.mapper.ProcessBatchMapper;
import com.jeeplus.modules.sys.entity.Office;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.workshopmanage.processroutine.entity.ProcessRoutine;
import com.hqu.modules.workshopmanage.processroutine.mapper.ProcessRoutineMapper;
import com.hqu.modules.workshopmanage.materialorder.entity.*;
import com.hqu.modules.workshopmanage.materialorder.mapper.*;
import com.hqu.modules.workshopmanage.sfcmaterialordersingle.entity.*;
import com.hqu.modules.workshopmanage.sfcmaterialordersingle.mapper.*;
import com.hqu.modules.workshopmanage.sfcinvcheckmain.mapper.*;
import com.hqu.modules.workshopmanage.sfcinvcheckmain.entity.*;
import com.hqu.modules.workshopmanage.processmaterialdetail.mapper.ProcessMaterialDetailMapper;

import com.hqu.modules.inventorymanage.outsourcingoutput.service.*;

/**
 * 车间加工路线管理Service
 * @author xhc
 * @version 2018-08-06
 */
@Service
@Transactional(readOnly = true)
public class ProcessRoutineService extends CrudService<ProcessBatchMapper, ProcessBatch> {

    @Autowired
    private ProcessRoutineMapper processRoutineMapper;
    @Autowired
    private ProcessBatchMapper processBatchMapper;
    @Autowired
    private SfcMaterialOrderMapper sfcMaterialOrderMapper;
    @Autowired
    private SfcMaterialOrderDetailMapper sfcMaterialOrderDetailMapper;
    @Autowired
    private SfcInvCheckMainMapper sfcInvCheckMainMapper;
    @Autowired
    private SfcInvCheckDetailMapper sfcInvCheckDetailMapper;
    @Autowired
    private ProcessMaterialMapper processMaterialMapper;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private BillMainOutsourcingService billMainOutsourcingService;
    @Autowired
    private OutsourcingOutputService outsourcingOutputService;
    @Autowired
    private SfcMaterialordermainsingleMapper sfcMaterialordermainsingleMapper;
    @Autowired
    private SfcMaterialorderdetailsingleMapper sfcMaterialorderdetailsingleMapper;
    @Autowired
    private ProcessMaterialDetailMapper processMaterialDetailMapper;

	public ProcessBatch get(String id) {
		ProcessBatch processBatch = super.get(id);
		processBatch.setProcessRoutineList(processRoutineMapper.findList(new ProcessRoutine(processBatch)));
		return processBatch;
	}
	
	public List<ProcessBatch> findList(ProcessBatch processBatch) {
		return super.findList(processBatch);
	}
	
	public Page<ProcessBatch> findPage(Page<ProcessBatch> page, ProcessBatch processBatch) {
		dataRuleFilter(processBatch);
		processBatch.setPage(page);
		page.setList(processBatchMapper.findListForProcessRoutine(processBatch));
		return page;
	}

	public Page<ProcessBatch> findQueryPage(Page<ProcessBatch> page, ProcessBatch processBatch) {
		dataRuleFilter(processBatch);
		processBatch.setPage(page);
		page.setList(processBatchMapper.findListForProcessRoutineQuery(processBatch));
		return page;
	}


	@Transactional(readOnly = false)
	public void save(ProcessBatch processBatch) {
		super.save(processBatch);
		for (ProcessRoutine processRoutine : processBatch.getProcessRoutineList()){
			if (processRoutine.getId() == null){
				continue;
			}
			if (ProcessRoutine.DEL_FLAG_NORMAL.equals(processRoutine.getDelFlag())){
				if (StringUtils.isBlank(processRoutine.getId())){
					processRoutine.preInsert();
					processRoutineMapper.insert(processRoutine);
				}else{
					processRoutine.preUpdate();
					processRoutineMapper.update(processRoutine);
				}
			}else{
				processRoutineMapper.delete(processRoutine);
			}
		}
		if("C".equals(processBatch.getAssignedState())){
			for (ProcessRoutine processRoutine : processBatch.getProcessRoutineList()){//车间路线单编制（这道工艺指定哪个工位哪个负责人来做）提交时做的算法操作
			    try {
                    generateRoutineDetail(processRoutine.getRoutineBillNo(), processRoutine.getRoutingCode());//逐条把加工路线按其数量 自动拆分到单件中

                }catch (Exception e){
			        logger.debug("ProcessRoutine.generateRoutineDetail_________________error:"+e.getMessage());
                }

                //不再在此处的调用领料单自动生成，改用领料单页面中的生成集中领料单按钮调用
               /* try {
                    generateMaterialOrderMain(processRoutine.getRoutineBillNo(), processRoutine.getRoutingCode());//逐条把加工路线的工艺依据相应工艺所需物料生成领料单
                }catch (Exception e){
                    logger.debug("ProcessRoutine.generateMaterialOrderMain_________________error:"+e.getMessage());
                }*/

			}
            for (ProcessRoutine processRoutine : processBatch.getProcessRoutineList()){
                fillProcessRoutineSeqNo(processRoutine.getRoutineBillNo());//逐条把加工路线回填到ppc_mserialno表格中
            }

		}
	}
	
	@Transactional(readOnly = false)
	public void delete(ProcessBatch processBatch) {
		super.delete(processBatch);
		processRoutineMapper.delete(new ProcessRoutine(processBatch));
	}


	
	/* 按照加工路线单中的数量，拆分成每个单件的生产。
	   加工路线单关键字：1. 加工路线单号；2.工艺号。
	   此程序在加工路线单编制程序，Form界面提交时执行
	*/
	@Transactional(readOnly = false)
	public void generateRoutineDetail(String routineBillNo, String routingCode) {
						
		String tmpSql;
		//1.查询生产数量
		tmpSql = "select planQty from sfc_processroutine "
				+ " where routineBillNo='" + routineBillNo.trim()
				+ "' and routingCode='" + routingCode.trim()+"'";
		List<Object> planQtyList = super.executeSelectSql(tmpSql);
		int planQty=(Double.valueOf(planQtyList.get(0).toString())).intValue();
		//logger.debug(" 生产数量查询SQL "+ planQty);
		
		//2.逐一处理每个产品
		for (int seqNo=1;seqNo<=planQty;seqNo++){

			//清空临时表
			tmpSql="delete from sfc_processroutinetmp";
			processRoutineMapper.execDeleteSql(tmpSql);
			
			//将指定的加工路线单信息写入到临时表中
			tmpSql="insert into sfc_processroutinetmp"
					+ " select * from sfc_processroutine"
					+ " where routineBillNo='" +routineBillNo.trim()+"' and routingCode='"
					+ routingCode.trim()+"'";
			processRoutineMapper.execInsertSql(tmpSql);
			
			//修改临时表中数据：计划数量1，实作数量1，序号等于seqNo，ID=原有ID+seqno
			tmpSql="update sfc_processroutinetmp"
					+" set planqty=1,realqty=1,planedate=null,seqNo="+seqNo
					+",id='"+routineBillNo+"-"+routingCode+"-"+seqNo+"'";
			//logger.debug(" XXX YYYYYYYYYY tmpSql "+ tmpSql);
			processRoutineMapper.execUpdateSql(tmpSql);
			
			//将临时表中的数据写入到sfc_processroutinedetail表中
			tmpSql="insert into sfc_processroutinedetail"
					+" select * from sfc_processroutinetmp";
			processRoutineMapper.execInsertSql(tmpSql);


			generateProcessMaterial(routineBillNo,seqNo,routingCode);//生成每道工艺所需的物料信息

		}
	}


    /**
     * 回填ppc_mserialno表格中的加工路线单对应单件序号
     * 当加工路线单 form表单中信息提交时执行
     * 说明：此操作之所以没有放到回填加工路线单号时一起完成（可以一起完成），是因为加工路线单虽然确定了，但是工作中心、人员
     * 等信息尚未填写，此时回填seqNo的话，利用此值进行查询时，会得到空或者错误的数据。
     * 注：此函数需要在加工路线单明细生成程序之后执行
     *
     **/
    @Transactional(readOnly = false)
    public void fillProcessRoutineSeqNo(String routineBillNo){

        //1.根据加工路线单号，从sfc_processroutine加工路线单表中获取生产数量信息
        String attributes = "a.planqty";
        String tmpSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
                + ") FROM sfc_processroutine a"
                + " where routineBillNo='" + routineBillNo.trim() + "'";
        logger.debug(" 加工路线单信息SQL    " + tmpSql);
        String attrFormat = attributes;
        List<Map<String, String>> routineInfoList = xhcUtil.resultToList(super.executeSelectSql(tmpSql), attrFormat, "###");


        if (routineInfoList==null || routineInfoList.get(0)==null)
            return;
        else if (Double.valueOf(routineInfoList.get(0).get("a.planqty"))<=0)
            return;

        int planQty= Double.valueOf(routineInfoList.get(0).get("a.planqty")).intValue();  //本批次需要生产的数量
        //logger.debug(" 加工路线单生产数量    " + planQty);

        //2.从ppc_mserialno表格中，找到该加工路线单routineBillNo对应的记录，更新他们的seqNo
        attributes = "a.mserialno,a.obj_sn";
        tmpSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
                + ") FROM ppc_mserialno a"
                + " where routinebillno='" + routineBillNo + "'";
        logger.debug(" 机器序列号数据SQL    " + tmpSql);
        attrFormat = attributes;
        List<Map<String, String>> mSerialNoList = xhcUtil.resultToList(super.executeSelectSql(tmpSql), attrFormat, "###");

        int seqNo=1;
        for(Map<String, String> serialNo:mSerialNoList){
            if (seqNo>planQty) break;

            //2.1更新ppc_mserialno表格中的单件序号
            String updateSql="update ppc_mserialno "
                    + " set seqno=" + seqNo +","
                    + "isAssigned='Y'"  //此时，已经完成了机器序列号的全部分配过程，关联了全部的计划号
                    + " where mserialno='" + serialNo.get("a.mserialno") + "'";
            logger.debug(" 更新单件序号SQL    " + updateSql);

            super.mapper.execUpdateSql(updateSql);


            //2.2在加工路线单明细表中填写入机器序列号信息和二维码信息
            updateSql="update sfc_processroutinedetail "
                    + " set mserialno='" + serialNo.get("a.mserialno") +"',"
                    + "obj_sn='" + serialNo.get("a.obj_sn") + "'"
                    + " where routinebillno='" + routineBillNo + "'"
                    + " and seqno=" + seqNo;
            logger.debug(" 更新加工路线单明细SQL    " + updateSql);

            super.mapper.execUpdateSql(updateSql);

            seqNo++;

        }
    }


    /*
    新的领料单生成程序，按照工作中心汇总、按照所领物料所在仓库分单子

    参数：
    程序的主参数为：领料时间。处理该日期所有的加工路线单，生成领料单。

    Step0. 在领料单页面中新增按钮，让用户输入领料日期。输入日期后，判断是否已经有该日期的领料单，如果有给出提示“已经存在该日期领料单，
    继续操作的话，将删除已有领料单，重新生成，是否继续？”
    如果选择是，则：
    Step1. 删除该日期内所有自动生成的领料单。（单据类型为自动生成的“C”）
    注：此处为了处理方便，不判断领料单状态是计划还是审批，全部删除（因为涉及到多个加工路线单的多个物料的合并问题，如果对已经审核的单独处理，
    则逻辑过于复杂）。删除领料单后，同时删除对应的出库单（尚未过账的），此处存在一个不好处理的地方，就是如果出库单已经过账，会有问题
    ，暂时不管）

    ++实施中，要求用户将所有的加工路线单都生成完之后，再统一生成领料单。
    ++或者判断只有所有的加工路线单都已经提交后，才允许调用领料单生成程序。以避免上面的问题

    Step2：
    从加工路线单表格中，找到该日期内所有的工作中心。
    循环处理每个工作中心。
    2.1 为了处理方便，还是调用之前的领料单程序，生成该日期所有的领料单，每个子表只有一条记录那种。
        主表和子表数据写入各自的临时表tmp中，以备处理。
        将主表和子表连接在一起，可以考虑用视图技术
        2.1.1 找到视图中的所有仓库，循环处理每个仓库
              为每个仓库生成一个主表
              汇总该仓库下的每个物料的需求数量group by 物料，每种物料写入到子表中的一行数据
     */


    /** 合批领料主程序
     * @param orderDate 领料日期，需要处理该日期内所有的加工路线单数据，生成领料单。
     *                  要求日期格式为YYYY-MM-DD
     */

    @Transactional(readOnly = false)
    public void generateBatchMaterialOrder(String orderDate) {
        /*1.读取该日期内所有加工路线单的工作中心代码，以工作中心代码来组织领料单的生成 */
        String selectSql = "select distinct a.centercode,a.planbdate"
                + " from sfc_processroutine a"
                + " where date_format(a.planbdate,'%Y-%m-%d')='" + orderDate.trim() + "'"
                + " and billstate='C' and assignedstate<>'C'";
        List<Object> centerCodeList = super.executeSelectSql(selectSql);
        logger.debug("  获取工作中心代码   " +  selectSql);
        if (centerCodeList==null || centerCodeList.size()==0)
            return;

        for (int i=0;i<centerCodeList.size();i++){
            //2. 循环处理每个工作中心的领料单

            //2.1 找到该工作中心领料日期内所有的加工路线单信息
            String attributes = "a.routinebillno,a.routingcode";
            selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
                    + ") from sfc_processroutine a"
                    + " where date_format(a.planbdate,'%Y-%m-%d')='" + orderDate.trim() + "'"
                    + " and billstate='C' and centercode='"+ centerCodeList.get(i).toString()
                    + "' and assignedstate<>'C'";
            logger.debug("  获取加工路线单信息   " +  selectSql);
            String attrFormat = attributes;
            List<Map<String, String>> processRoutineList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");

            for (Map<String, String> processRoutine: processRoutineList){
                //2.1.1 处理每条加工路线单，调用原来每个领料单子表只保存一条数据的程序
                this.generateMaterialOrderMainForBatch(processRoutine.get("a.routinebillno"),processRoutine.get("a.routingcode"));
                //2019年1月20日  回填加工路线单表sfc_processroutine，修改其中的assignedstate字段为C，表示已经处理完毕。
                String tmpSQL="update sfc_processroutine"
                        +" set assignedstate='C'"
                        +" where routinebillno='" + processRoutine.get("a.routinebillno") +"' and"
                        +" routingcode='" + processRoutine.get("a.routingcode") +"'";
                logger.debug("  更新加工路线单中集中领料状态的SQL" + tmpSQL);
                super.mapper.execUpdateSql(tmpSQL);

            }

            //2.2 将该工作中心产生的主子表连接在一起，按照仓库分组，每组生成一行主表和多行子表
            selectSql = "select distinct a.wareid from sfc_materialordermainsingle a,sfc_materialorderdetailsingle b"
                    + " where a.materialorder=b.materialorderdetail "
                    + " and date_format(a.operdate,'%Y-%m-%d')='" + orderDate.trim()
                    + "' and a.opercode='"+ centerCodeList.get(i).toString()+"'";
            logger.debug("  分组信息   " +  selectSql);
            List<Object> groupList = super.executeSelectSql(selectSql);
            if (groupList==null || groupList.size()==0)
                continue; //跳出该工作中心的处理

            //重新计算领料单号，读取领料单表格中当前的最大序号,生成新的序号
            int tmpNo=0;
            String prefix = "SMO" + orderDate.substring(0, 4) + orderDate.substring(5, 7)
                    + orderDate.substring(8, 10) + "-";  //订单号前缀(SMOYYYYMMDD-)
            selectSql = "select max(substring(materialorder,13)+0) from sfc_materialorder"
                    + " where substring(materialorder,1,12)='" + prefix + "'";
            logger.debug("  获取最大单号   " +  selectSql);
            List<Object> maxOrderList = super.executeSelectSql(selectSql);
            if (maxOrderList==null || maxOrderList.get(0) == null || maxOrderList.get(0).toString().equals("")) {
                //如果为空的话
                tmpNo=1;
            } else {
                tmpNo=Double.valueOf(maxOrderList.get(0).toString()).intValue() + 1;
            }


            for(int j=0;j<groupList.size();j++){
                 //循环处理该工作中心下的每个仓库
                //2.2.1 生成新的主表：从已经生成的数据中找到一个主表行，直接复制过去

                //找到当前分组对应的id，可能有多行，主表中的数据应该是一样的，只取第一个
                attributes = "a.id";
                selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
                        + ") from sfc_materialordermainsingle a,sfc_materialorderdetailsingle b"
                        + " where a.materialorder=b.materialorderdetail "
                        + " and date_format(a.operdate,'%Y-%m-%d')='" + orderDate.trim()
                        + "' and a.opercode='"+ centerCodeList.get(i).toString()+"'"
                        + " and a.wareid='" + groupList.get(j).toString() + "'";
                logger.debug("  获取已有主表信息   " +  selectSql);
                attrFormat = attributes;
                List<Map<String, String>> orderMainList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");

                //将数据从single表格中复制到正式表主表中
                String tmpSQL="";
                tmpSQL=" insert into sfc_materialorder "
                        + " select * from sfc_materialordermainsingle a"
                        + " where a.id='" + orderMainList.get(0).get("a.id") + "'";
                logger.debug("  新增主表SQL  " +  tmpSQL);
                super.mapper.execInsertSql(tmpSQL);

                //更新主表的计划号
                tmpSQL=" update sfc_materialorder a"
                        + " set materialorder='" + prefix + tmpNo + "'"
                        + " where a.id='" + orderMainList.get(0).get("a.id") + "'";
                logger.debug(" 修改主表中订单号  " +  tmpSQL);
                super.mapper.execUpdateSql(tmpSQL);
                tmpNo++;

                //2.2.2 生成子表数据：从已经生成的数据中找到一个子表行，直接复制过去，然后修改数量为汇总值
                //找到该工作中心、该仓库中的所有物料，并汇总，每一个物料对应一个子表行
                attributes = "sum(b.requirenum),b.materialid";
                selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
                        + ") from sfc_materialordermainsingle a,sfc_materialorderdetailsingle b"
                        + " where a.materialorder=b.materialorderdetail "
                        + " and date_format(a.operdate,'%Y-%m-%d')='" + orderDate.trim()
                        + "' and a.opercode='"+ centerCodeList.get(i).toString()+"'"
                        + " and a.wareid='" + groupList.get(j).toString() +"'"
                        + " group by b.materialid";
                logger.debug("  求和信息   " +  selectSql);
                attrFormat = "sumQty,b.materialid";
                List<Map<String, String>> sumQtyList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");

                if (sumQtyList==null || sumQtyList.size()==0)
                    continue; //跳出该仓库的处理

                int sequenceid=1;  //子表序号

                for(Map<String, String> sumQty:sumQtyList){
                    //循环处理每一个物料

                    //找到当前物料分组对应的id，可能有多行，只取第一个
                    attributes = "b.id";
                    selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
                            + ") from sfc_materialordermainsingle a,sfc_materialorderdetailsingle b"
                            + " where a.materialorder=b.materialorderdetail "
                            + " and date_format(a.operdate,'%Y-%m-%d')='" + orderDate.trim()
                            + "' and a.opercode='"+ centerCodeList.get(i).toString()+"'"
                            + " and a.wareid='" + groupList.get(j).toString() + "'"
                            + " and b.materialid='" + sumQty.get("b.materialid")+"'";
                    logger.debug("  获取已有子表表信息   " +  selectSql);
                    attrFormat = attributes;
                    List<Map<String, String>> orderDetailList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");

                    //将数据从single子表格中复制到正式表子表中
                    tmpSQL="";
                    tmpSQL=" insert into sfc_materialorderdetail "
                            + " select * from sfc_materialorderdetailsingle a"
                            + " where a.id='" + orderDetailList.get(0).get("b.id") + "'";
                    logger.debug("  新增子表SQL  " +  tmpSQL);
                    super.mapper.execInsertSql(tmpSQL);

                    //更新子表的需求数量、对应的主表单据号
                    tmpSQL=" update sfc_materialorderdetail a"
                            + " set materialorder='" + orderMainList.get(0).get("a.id") + "',"
                            + "a.materialorderdetail='" + prefix + (tmpNo-1) + "',"
                            + "a.requirenum=" + Double.valueOf(sumQty.get("sumQty")) + ","
                            + "a.sequenceid=" + sequenceid
                            + " where a.id='" + orderDetailList.get(0).get("b.id") + "'";
                    logger.debug(" 修改子表中订单号和数量信息  " +  tmpSQL);
                    super.mapper.execUpdateSql(tmpSQL);
                    sequenceid++;
                }
            }
        }
    }



    /*
   按照加工路线单中的数据，生成车间领料单。  集中领料模式
   加工路线单关键字：1. 加工路线单号；2.工艺号。
   */
    @Transactional(readOnly = false)
    public void generateMaterialOrderMainForBatch(String routineBillNo, String routeCode) {

        //1.从加工路线单中读取加工路线和工艺对应的产品编码
        String attributes = "a.prodCode,null";
        String selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
                + ") from sfc_processroutine a"
                + " where a.routineBillNo='" + routineBillNo.trim() + "' and a.routingCode='"
                + routeCode.trim() + "'";
        String attrFormat = attributes;
        List<Map<String, String>> prodCodeList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");

        //2.从工艺路线物料关系表 MDM_RoutingItem中获取工艺中需要的物料,MDM_routingItem表格中保存的routing_code是UID,需要转换
        attributes = "a.item_code";
        selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
                + ") from MDM_RoutingItem a"
                + " where a.routing_code in (select distinct id from mdm_routing where routing_code='"
                + routeCode.trim() + "') and a.product_code='" + prodCodeList.get(0).get("a.prodCode") + "'";
        attrFormat = "itemCode";
        List<Map<String, String>> routingItemList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");

        //***************如果该工艺的生产不需要物料，则不操作。
        if (routingItemList == null || routingItemList.isEmpty())
            return;

        //循环处理每个物料，每个物料单独生成一张领料单
        for (Map<String, String> item:routingItemList){
            generateMaterialOrderDetailForBatch(routineBillNo,routeCode,item.get("itemCode"));
        }
    }


    /*
     按照加工路线单中的数据，生成车间领料单。（领料单号：SMO+计划生产日期+当前生产日期最大序号）
     加工路线单关键字：1. 加工路线单号；2.工艺号；3.物料编码
     */
    @Transactional(readOnly = false)
    public void generateMaterialOrderDetailForBatch(String routineBillNo, String routeCode,String itemCode) {

        /*********1.生成领料单主表数据***********/
        SfcMaterialordermainsingle sfcMaOrder = new SfcMaterialordermainsingle();

        //1.从加工路线单中读取需要的信息
        String attributes = "a.centerCode,a.prodCode,a.planQty,a.planBDate,b.center_Name,"
                + "b.dept_Code,b.dept_Name,a.personincharge";
        String selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
                + ") from sfc_processroutine a left join MDM_WorkCenter b on a.centerCode=b.center_Code"
                + " where a.routineBillNo='" + routineBillNo.trim() + "' and a.routingCode='"
                + routeCode.trim() + "'";
        logger.debug("   selectSql    " + selectSql);
        String attrFormat = "centerCode,prodCode,planQty,planBDate,centerName,deptCode,deptName,personIncharge";
        List<Map<String, String>> processRouteList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");

        //读取计划日期
        String planBDate = processRouteList.get(0).get("planBDate").toString().substring(0, 10);

        //读取领料单表格中当前的最大序号,生成新的序号
        String tmpSql;
        String prefix = "SMO" + planBDate.substring(0, 4) + planBDate.substring(5, 7)
                + planBDate.substring(8, 10) + "-";  //订单号前缀(SMOYYYYMMDD-)
        tmpSql = "select max(substring(materialorder,13)+0) from sfc_materialordermainsingle"
                + " where substring(materialorder,1,12)='" + prefix + "'";
        List<Object> maxOrderList = super.executeSelectSql(tmpSql);
        if (maxOrderList==null || maxOrderList.get(0) == null || maxOrderList.get(0).toString().equals("")) {
            //如果为空的话
            sfcMaOrder.setMaterialorder(prefix + "1");
        } else {
            int tmpNo = Double.valueOf(maxOrderList.get(0).toString()).intValue() + 1;
            sfcMaOrder.setMaterialorder(prefix + tmpNo);
        }


        //获取并写入核算期
        tmpSql = "select period_id from mdm_period"
                + " where period_begin<='" + processRouteList.get(0).get("planBDate").toString().substring(0, 10) + "'"
                + " and period_end>='" + processRouteList.get(0).get("planBDate").toString().substring(0, 10) + "'";
        List<Object> periodIDList = super.executeSelectSql(tmpSql);
        if(periodIDList.size()==0||periodIDList.get(0)==null){
            sfcMaOrder.setPeriodid(DateUtils.getDate("yyyyMM"));
            //sfcMaOrder.setPeriodid("不确定");
        }
        else {
            sfcMaOrder.setPeriodid(periodIDList.get(0).toString());
        }
        logger.debug("  获取核算期ID  " + sfcMaOrder.getPeriodid());

        //读取库存id和name，库管员id
        attributes = "a.ware_id,a.item_code,a.period,a.real_qty,b.ware_name,c.emp_id";
        selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
                + ") from inv_account a left join inv_employee_warehouse c on a.ware_id=c.ware_id,inv_warehouse b"
                + " where a.ware_id=b.ware_id and "
                + " concat(a.year,a.period)='" + sfcMaOrder.getPeriodid()/*periodIDList.get(0).toString()*/ +"' and a.item_code='" + itemCode + "'"
                + " order by a.real_qty desc";
        logger.debug("   获取库房信息的SQL语句     " + selectSql);
        attrFormat = attributes;
        List<Map<String, String>> wareInfoList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");
        //logger.debug("  库房信息  " + wareInfoList.get(0).toString());
        if (wareInfoList==null || wareInfoList.size()==0){
            sfcMaOrder.setWareempname("不确定");
            sfcMaOrder.setWareempid("不确定");
            sfcMaOrder.setWareid("不确定");
            sfcMaOrder.setWarename("不确定");

        }else{
            sfcMaOrder.setWareid(wareInfoList.get(0).get("a.ware_id"));
            sfcMaOrder.setWarename(wareInfoList.get(0).get("b.ware_name"));
            sfcMaOrder.setWareempid(wareInfoList.get(0).get("c.emp_id"));
        }

        //读取库管员name,避免上面的语句过于复杂，此处单独处理下库管员名字
        attributes = "emp_name";
        selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
                + ") from inv_employee"
                + " where emp_id='" + sfcMaOrder.getWareempid() + "'";
        attrFormat = attributes;
        List<Map<String, String>> empNameList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");
        if (!(empNameList==null || empNameList.size()==0)){
            sfcMaOrder.setWareempname(empNameList.get(0).get("emp_name"));
        }


        //领料类型 ："自动生成";
        sfcMaOrder.setReceivetype("C");
        sfcMaOrder.setBilltype("G"); //单据类型：领料
        sfcMaOrder.setQualityflag("良品");//领料的物料均为良品

        //获取工作中心名称、部门代码和部门名称
        sfcMaOrder.setOpercode(processRouteList.get(0).get("centerCode"));
        sfcMaOrder.setOpername(processRouteList.get(0).get("centerName"));
        sfcMaOrder.setShopid(processRouteList.get(0).get("deptCode"));
        sfcMaOrder.setShopname(processRouteList.get(0).get("deptName"));

        //领料人代码、领料人姓名、制单人编码、制单人名称、制单日期
        sfcMaOrder.setResponser(UserUtils.getUser().getId());
        //sfcMaOrder.setRespname(UserUtils.getUser().getName());
        sfcMaOrder.setRespname(processRouteList.get(0).get("personIncharge"));
        sfcMaOrder.setEditorid(UserUtils.getUser().getId());
        sfcMaOrder.setEditor(UserUtils.getUser().getName());
        sfcMaOrder.setEditdate(new Date());


        sfcMaOrder.setBillstateflag("P");//单据状态
        sfcMaOrder.setOperdate(DateUtils.parseDate(processRouteList.get(0).get("planBDate")));//业务日期

        //加工路线单号和工艺号，作为外键
        sfcMaOrder.setRoutinebillno(routineBillNo);
        sfcMaOrder.setRoutingcode(routeCode);

        //设置框架自动生成的字段
        //sfcMaOrder.setId(sfcMaOrder.getMaterialorder()+"X"); //为了避免从single表复制到正式表中重复的问题，X
        sfcMaOrder.setId(IdGen.uuid());
        sfcMaOrder.setRemarks("");
        sfcMaOrder.setCreateBy(UserUtils.getUser());
        sfcMaOrder.setCreateDate(new Date());
        sfcMaOrder.setUpdateBy(UserUtils.getUser());
        sfcMaOrder.setUpdateDate(new Date());
        sfcMaOrder.setDelFlag("0");

        //存入主表数据
        sfcMaterialordermainsingleMapper.insert(sfcMaOrder);

        //从工艺路线物料关系表 MDM_RoutingItem 和物料主文件中获取工艺中需要的物料信息
        attributes = "a.product_code,a.routing_code,a.item_code,a.req_qty,b.name,b.unit,b.spec_model";/*b.unit_code*/
        selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
                + ") from MDM_RoutingItem a left join mdm_item b on a.item_code=b.code"
                + " where a.routing_code in (select distinct id from mdm_routing where routing_code='"
                + routeCode.trim() + "') and a.product_code='" + processRouteList.get(0).get("prodCode") + "'"
                + " and a.item_code='" + itemCode +"'";
        attrFormat = "productCode,routingCode,itemCode,reqQty,itemName,unit,spec_model";
        List<Map<String, String>> routingItemList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");


        //用来保存主表entity中的子表List数据，同库存做接口生成出库单
        List<SfcMaterialorderdetailsingle> sfcMaterialOrderDetailList = new ArrayList<>();


        /**********************************生成领料单附表数据****************************************/
        //注：当前设计中，为了库存处理方便，每条主表数据只对应一条子表数据
        int sequenceId = 1;
        for (Map<String, String> item : routingItemList) {

            //计算数量
            int reqQty, planQty;
            reqQty = Integer.valueOf(item.get("reqQty")).intValue();
            planQty = Integer.valueOf(processRouteList.get(0).get("planQty")).intValue();

            tmpSql = "insert into sfc_materialorderdetailsingle ("
                    + "materialOrder,"
                    + "materialOrderDetail,"
                    + "sequenceId,"
                    + "materialId,"
                    + "materialName,"
                    + "numUnit,"
                    + "materialSpec,"
                    + "requireNum,"
                    + "id,"
                    + "del_flag)"


                    + " values('"
                    + sfcMaOrder.getId() + "','"
                    + sfcMaOrder.getMaterialorder() + "',"
                    + sequenceId + ",'"
                    + item.get("itemCode") + "','"
                    + item.get("itemName") + "','"
                    + item.get("unit") + "','"
                    + item.get("spec_model") + "',"
                    + Double.valueOf((double) reqQty * planQty) + ",'"
                    + IdGen.uuid() +"','"//sfcMaOrder.getMaterialorder() + item.get("itemCode") + "','"
                    + "0'"
                    + ")";
            logger.debug(" 保存子表数据的SQL语句  " + tmpSql);
            sequenceId++;

            //保存子表数据到数据库中
            sfcMaterialorderdetailsingleMapper.execInsertSql(tmpSql);
        }
    }




    /*

    按照加工路线单中的数据，生成车间领料单。
    加工路线单关键字：1. 加工路线单号；2.工艺号。
    */

    @Transactional(readOnly = false)
    public void generateMaterialOrderMain(String routineBillNo, String routeCode) {

        //1.从加工路线单中读取加工路线和工艺对应的产品编码
        String attributes = "a.prodCode,null";
        String selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
                + ") from sfc_processroutine a"
                + " where a.routineBillNo='" + routineBillNo.trim() + "' and a.routingCode='"
                + routeCode.trim() + "'";
        String attrFormat = attributes;
        List<Map<String, String>> prodCodeList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");

        //2.从工艺路线物料关系表 MDM_RoutingItem中获取工艺中需要的物料,MDM_routingItem表格中保存的routing_code是UID,需要转换
        attributes = "a.item_code";
        selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
                + ") from MDM_RoutingItem a"
                + " where a.routing_code in (select distinct id from mdm_routing where routing_code='"
                + routeCode.trim() + "') and a.product_code='" + prodCodeList.get(0).get("a.prodCode") + "'";
        attrFormat = "itemCode";
        List<Map<String, String>> routingItemList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");

        //***************如果该工艺的生产不需要物料，则不操作。
        if (routingItemList == null || routingItemList.isEmpty())
            return;

        //循环处理每个物料，每个物料单独生成一张领料单
        for (Map<String, String> item:routingItemList){
            generateMaterialOrderDetail(routineBillNo,routeCode,item.get("itemCode"));
        }

    }




    /*
    按照加工路线单中的数据，生成车间领料单。（领料单号：SMO+计划生产日期+当前生产日期最大序号）
     加工路线单关键字：1. 加工路线单号；2.工艺号；3.物料编码
     */

    @Transactional(readOnly = false)
    public void generateMaterialOrderDetail(String routineBillNo, String routeCode,String itemCode) {

        /*********生成领料单主表数据***********/

        SfcMaterialOrder sfcMaOrder = new SfcMaterialOrder();

        //1.从加工路线单中读取需要的信息
        String attributes = "a.centerCode,a.prodCode,a.planQty,a.planBDate,a.personincharge,b.center_Name,"
                + "b.dept_Code,b.dept_Name";
        String selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
                + ") from sfc_processroutine a left join MDM_WorkCenter b on a.centerCode=b.center_Code"
                + " where a.routineBillNo='" + routineBillNo.trim() + "' and a.routingCode='"
                + routeCode.trim() + "'";
        //logger.debug("   selectSql    " + selectSql);
        String attrFormat = "centerCode,prodCode,planQty,planBDate,personIncharge,centerName,deptCode,deptName";
        List<Map<String, String>> processRouteList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");

        //读取计划日期
        String planBDate = processRouteList.get(0).get("planBDate").toString().substring(0, 10);

        //读取领料单表格中当前的最大序号,生成新的序号
        String tmpSql;
        String prefix = "SMO" + planBDate.substring(0, 4) + planBDate.substring(5, 7)
                + planBDate.substring(8, 10) + "-";  //订单号前缀(SMOYYYYMMDD-)
        tmpSql = "select max(substring(materialorder,13)+0) from sfc_materialorder"
                + " where substring(materialorder,1,12)='" + prefix + "'";
        List<Object> maxOrderList = super.executeSelectSql(tmpSql);
        if (maxOrderList.get(0) == null || maxOrderList.get(0).toString().equals("")) {
            //如果为空的话
            sfcMaOrder.setMaterialOrder(prefix + "1");
        } else {
            int tmpNo = Double.valueOf(maxOrderList.get(0).toString()).intValue() + 1;
            sfcMaOrder.setMaterialOrder(prefix + tmpNo);
        }


        //获取并写入核算期
        tmpSql = "select period_id from mdm_period"
                + " where period_begin<='" + processRouteList.get(0).get("planBDate").toString().substring(0, 10) + "'"
                + " and period_end>='" + processRouteList.get(0).get("planBDate").toString().substring(0, 10) + "'";
        List<Object> periodIDList = super.executeSelectSql(tmpSql);
        if(periodIDList.size()==0||periodIDList.get(0)==null){
            sfcMaOrder.setPeriodId(DateUtils.getDate("yyyyMM"));
            //sfcMaOrder.setPeriodId("不确定");
        }
        else {
            sfcMaOrder.setPeriodId(periodIDList.get(0).toString());
        }
        //logger.debug("  getPeriodId  " + sfcMaOrder.getPeriodId());

        //读取库存id和name，库管员id
        attributes = "a.ware_id,a.item_code,a.period,a.real_qty,b.ware_name,c.emp_id";
        selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
                + ") from inv_account a left join inv_employee_warehouse c on a.ware_id=c.ware_id,inv_warehouse b"
                + " where a.ware_id=b.ware_id and "
                + " concat(a.year,a.period)='" + periodIDList.get(0).toString() +"' and a.item_code='" + itemCode + "'"
                + " order by a.real_qty desc";
        //logger.debug("   获取库房信息的SQL语句     " + selectSql);
        attrFormat = attributes;
        List<Map<String, String>> wareInfoList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");
        //logger.debug("  库房信息  " + wareInfoList.get(0).toString());
        if (wareInfoList==null || wareInfoList.size()==0){
            //return;
//            sfcMaOrder.setWareId(wareInfoList.get(0).get("a.ware_id"));
            sfcMaOrder.setWareName("不确定");
            sfcMaOrder.setWareEmpname("不确定");
            sfcMaOrder.setWareEmpid("不确定");
//            sfcMaOrder.setWareEmpid(wareInfoList.get(0).get("c.emp_id"));
        }else{
            sfcMaOrder.setWareId(wareInfoList.get(0).get("a.ware_id"));
            sfcMaOrder.setWareName(wareInfoList.get(0).get("b.ware_name"));
            sfcMaOrder.setWareEmpid(wareInfoList.get(0).get("c.emp_id"));
        }

        //读取库管员name,避免上面的语句过于复杂，此处单独处理下库管员名字
        attributes = "emp_name";
        selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
                + ") from inv_employee"
                + " where emp_id='" + sfcMaOrder.getWareEmpid() + "'";
        attrFormat = attributes;
        List<Map<String, String>> empNameList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");
        //logger.debug("  库房信息  " + empNameList.get(0).toString());
        if (!(empNameList==null || empNameList.size()==0)){
            sfcMaOrder.setWareEmpname(empNameList.get(0).get("emp_name"));
        }


        //领料类型 ："自动生成";//单据类型：领料;//良品标识：领料的物料均是良品
        sfcMaOrder.setReceiveType("C");
        sfcMaOrder.setBillType("G");
        sfcMaOrder.setQualityFlag("良品");


        //获取工作中心名称、部门代码和部门名称
        sfcMaOrder.setOperCode(processRouteList.get(0).get("centerCode"));
        sfcMaOrder.setOperName(processRouteList.get(0).get("centerName"));
        sfcMaOrder.setShopId(processRouteList.get(0).get("deptCode"));
        sfcMaOrder.setShopName(processRouteList.get(0).get("deptName"));

        //领料人代码、领料人姓名、制单人编码、制单人名称、制单日期
        sfcMaOrder.setResponser(UserUtils.getUser().getId());
        //sfcMaOrder.setRespName(UserUtils.getUser().getName());
        sfcMaOrder.setRespName(processRouteList.get(0).get("personIncharge"));//领料姓名为这道工艺的负责人
        sfcMaOrder.setEditorId(UserUtils.getUser().getId());
        sfcMaOrder.setEditor(UserUtils.getUser().getName());
        sfcMaOrder.setEditDate(new Date());


        sfcMaOrder.setBillStateFlag("P");//单据状态
        sfcMaOrder.setOperDate(DateUtils.parseDate(processRouteList.get(0).get("planBDate")));//业务日期

        //加工路线单号和工艺号，作为外键
        sfcMaOrder.setroutineBillNo(routineBillNo);
        sfcMaOrder.setroutingCode(routeCode);

        //设置框架自动生成的字段
        sfcMaOrder.setId(sfcMaOrder.getMaterialOrder());
        sfcMaOrder.setRemarks("");
        sfcMaOrder.setCreateBy(UserUtils.getUser());
        sfcMaOrder.setCreateDate(new Date());
        sfcMaOrder.setUpdateBy(UserUtils.getUser());
        sfcMaOrder.setUpdateDate(new Date());
        sfcMaOrder.setDelFlag("0");

        //存入主表数据
        sfcMaterialOrderMapper.insert(sfcMaOrder);

        //从工艺路线物料关系表 MDM_RoutingItem 和物料主文件中获取工艺中需要的物料信息
        attributes = "a.product_code,a.routing_code,a.item_code,a.req_qty,b.name,b.unit_code,b.spec_model";
        selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
                + ") from MDM_RoutingItem a left join mdm_item b on a.item_code=b.code"
                + " where a.routing_code in (select distinct id from mdm_routing where routing_code='"
                + routeCode.trim() + "') and a.product_code='" + processRouteList.get(0).get("prodCode") + "'"
                + " and a.item_code='" + itemCode +"'";
        //logger.debug("   ddddddddddddd    " + selectSql);
        attrFormat = "productCode,routingCode,itemCode,reqQty,itemName,unit,spec_model";
        List<Map<String, String>> routingItemList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");


        //用来保存主表entity中的子表List数据，同库存做接口生成出库单
        List<SfcMaterialOrderDetail> sfcMaterialOrderDetailList = new ArrayList<>();



        /**********************************生成领料单附表数据****************************************/

        //注：当前设计中，为了库存处理方便，每条主表数据只对应一条子表数据
        int sequenceId = 1;
        for (Map<String, String> item : routingItemList) {

            //计算数量
            int reqQty, planQty;
            reqQty = Integer.valueOf(item.get("reqQty")).intValue();
            planQty = Integer.valueOf(processRouteList.get(0).get("planQty")).intValue();

            tmpSql = "insert into sfc_materialorderdetail("
                    + "materialOrder,"
                    + "materialOrderDetail,"
                    + "sequenceId,"
                    + "materialId,"
                    + "materialName,"
                    + "numUnit,"
                    + "materialSpec,"
                    + "requireNum,"
                    + "id,"
                    + "del_flag)"


                    + " values('"
                    + sfcMaOrder.getId() + "','"
                    + sfcMaOrder.getId() + "',"
                    + sequenceId + ",'"
                    + item.get("itemCode") + "','"
                    + item.get("itemName") + "','"
                    + item.get("unit") + "','"
                    + item.get("spec_model") + "',"
                    + Double.valueOf((double) reqQty * planQty) + ",'"
                    + sfcMaOrder.getMaterialOrder() + item.get("itemCode") + "','"
                    + "0'"
                    + ")";
            //logger.debug(tmpSql);

            sequenceId++;

            //保存子表数据到数据库中
            sfcMaterialOrderDetailMapper.execInsertSql(tmpSql);

            //写入主表中附表的List数据，用于同库存的出库单进行接口
            SfcMaterialOrderDetail sfcMaOrderDetail = new SfcMaterialOrderDetail();
            sfcMaOrderDetail.setMaterialId(item.get("itemCode")); //材料编码
            sfcMaOrderDetail.setMaterialName(item.get("itemName")); //材料名称
            sfcMaOrderDetail.setMaterialSpec(item.get("spec_model")); //材料规格型号
            sfcMaOrderDetail.setSequenceId(Integer.valueOf(sequenceId - 1)); //序号
            sfcMaOrderDetail.setNumUnit(item.get("unit")); //计量单位
            sfcMaOrderDetail.setRequireNum(Double.valueOf((double) reqQty * planQty)); //领料数量

            sfcMaterialOrderDetailList.add(sfcMaOrderDetail);
        }

        //写入主表中附表的List数据
        sfcMaOrder.setSfcMaterialOrderDetailList(sfcMaterialOrderDetailList);

        // 调用库存提供的接口，完成出库操作
        // 根据接口给出的返回值，回填出库单号，已完成追溯操作。
        String billNum = outsourcingOutputService.transferMaterialOrder(sfcMaOrder);

        tmpSql = "update sfc_materialorder "
                + " set InvBillNo='" + billNum +"'"
                + " where materialorder='" + sfcMaOrder.getMaterialOrder() +"'";
        sfcMaterialOrderDetailMapper.execInsertSql(tmpSql);
    }




    // 根据加工路线单明细表（最后一道工艺的完成信息）生成完工入库通知单的主表和子表
    // 此程序在装配工艺完工后执行，只有最后一道装配工艺完成时才能执行
    //参数只有加工路线单号和工艺号，程序中要判断是否为最后一道装配工艺，如果是则生成完工入库通知单，否则do nothing


    @Transactional(readOnly = false)
    public void generateInvCheckByRoutineDetail(String routineBillNo, String routingCode,int seqNo) {

        /*1.读取加工路线单明细信息 */
        String attributes = "a.centerCode,a.isLastRouting,a.prodCode,a.planQty,a.planBDate,"
                + "a.realQty,a.planEDate,a.billState,a.seqno,a.personInCharge,a.actualcentercode,a.mserialno,"
                + "b.code,b.name,b.spec_model,b.unit_code,b.draw_no,b.plan_price";
        String selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
                + ") FROM sfc_processroutinedetail a left join mdm_item b on a.prodCode=b.code"
                + " where a.routineBillNo='" + routineBillNo.trim()
                + "' and a.routingCode='" + routingCode + "' and a.seqno=" + seqNo;
        //logger.debug(" 加工路线单明细表信息 SQL " + selectSql);
        String attrFormat = attributes;
        List<Map<String, String>> processRoutineList = xhcUtil.resultToList(super.executeSelectSql(selectSql),
                attrFormat, "###");

        for (Map<String, String> list : processRoutineList) { //不用循环，暂时先保留

            /** 2.判断下是否是最后一道装配工艺,不是则不处理，退出***/
            if (!list.get("a.isLastRouting").equals("Y")) {
                logger.debug("不是最后一道装配工艺，不能办理完工入库");
                break;
            }

            /*****************处理完工入库单主表********************/
            SfcInvCheckMain sfcInvCheckMain = new SfcInvCheckMain();

            // 读取完工入库通知单主表中当前的最大序号,生成新的序号
            // 订单号格式 SICYYYYMMDD-3位序号
            String prefix = "SIC" + DateUtils.getYear() + DateUtils.getMonth() + DateUtils.getDay() + "-"; // 订单号前缀(SICYYYYMMDD-)
            selectSql = "select max(substring(bill_no,13)+0) from sfc_invcheckmain"
                    + " where substring(bill_no,1,12)='" + prefix + "'";
            logger.debug("  获取完工入库通知单最大序号的SQL语句      " + selectSql);
            List<Object> maxNoList = super.executeSelectSql(selectSql);

            if (maxNoList.get(0) == null || maxNoList.get(0).toString().equals("")) { // 如果为空的话
                sfcInvCheckMain.setBillNo(prefix + "001");
            } else {
                int tmpNo = Double.valueOf(maxNoList.get(0).toString()).intValue() + 1;
                //logger.debug("  tmpNo       " + tmpNo);
                sfcInvCheckMain.setBillNo(prefix + String.format("%03d",tmpNo));
            }

            sfcInvCheckMain.setBillType("C"); // 单据类型：自动生成
            sfcInvCheckMain.setMakeDate(new Date()); // 制单日期
            sfcInvCheckMain.setMakepId(UserUtils.getUser().getId()); // 制单人Id
            sfcInvCheckMain.setMakepName(UserUtils.getUser().getName()); // 制单人Name
            sfcInvCheckMain.setBillStateFlag("P"); // 单据标志：正在编辑
            sfcInvCheckMain.setWorkerpId(list.get("a.personInCharge")); // 完工人的ID为负责人
            sfcInvCheckMain.setWorkerpName(list.get("a.personInCharge")); // 完工人的姓名为负责人

            //处理半成品质检衔接问题；目前是半成品质检通过人为控制是否已质检，不通过系统控制。半成品（物料编码前缀为20）直接把完工单状态改为待提交（Q）
            if(list.get("b.code")!=null&&list.get("b.code").startsWith("20")){
                sfcInvCheckMain.setBillStateFlag("Q");
            }

            // 完成日期:如果填写了实际的就是实际的，否则就是计划的
            if (list.get("a.planEDate") == null || list.get("a.planEDate") == "")
                sfcInvCheckMain.setFinishDate(DateUtils.parseDate(list.get("a.planBDate")));
            else
                sfcInvCheckMain.setFinishDate(DateUtils.parseDate(list.get("a.planEDate")));
            // logger.debug(sfcInvCheckMain.getFinishDate().toString());

            // 车间编号:用工作中心替代，如果填写了实际的就是实际的，否则就是计划的
            if (list.get("a.actualcentercode") == null || list.get("a.actualcentercode") == "") {
                sfcInvCheckMain.setShopId(list.get("a.centerCode"));
            }
            else
                sfcInvCheckMain.setShopId(list.get("a.actualcentercode"));
            // logger.debug(sfcInvCheckMain.getShopId());

            //填写工作中心名称
            selectSql="select center_name from mdm_workcenter where center_code='"+sfcInvCheckMain.getShopId()+"'";
            List<Object> nameList = super.executeSelectSql(selectSql);
            if (nameList != null&&nameList.get(0) != null && !nameList.get(0).toString().equals("")) { // 如果不为为空的话
                sfcInvCheckMain.setShopName(nameList.get(0).toString());
            }

            //设置质检标志
            sfcInvCheckMain.setQmsFlag("0");

            // 外键：加工路线单号、单件序号、工艺编码
            sfcInvCheckMain.setRoutineBillNo(routineBillNo);
            sfcInvCheckMain.setSeqNo(seqNo);
            sfcInvCheckMain.setRoutingCode(routingCode);

            // 设置框架自动生成的字段
            sfcInvCheckMain.setId(sfcInvCheckMain.getBillNo());
            sfcInvCheckMain.setRemarks("");
            sfcInvCheckMain.setCreateBy(UserUtils.getUser());
            sfcInvCheckMain.setCreateDate(new Date());
            sfcInvCheckMain.setUpdateBy(UserUtils.getUser());
            sfcInvCheckMain.setUpdateDate(new Date());
            sfcInvCheckMain.setDelFlag("0");

            //保存主表数据
            sfcInvCheckMainMapper.insert(sfcInvCheckMain);

            //??问题：新插入的主表数据中，此三条数据项无法写入，可能是缓存的问题。
            //外键：加工路线单号、单件序号、工艺编码
            //暂时这样处理，找到问题后删除
            selectSql = "update sfc_invcheckmain "
                    + " set routineBillNo='" + routineBillNo +"',"
                    + "routingCode='" + routingCode + "',"
                    + "SeqNo=" + seqNo
                    + " where bill_no='" + sfcInvCheckMain.getBillNo() +"'";
            logger.debug("  补充信息的SQL   " + selectSql);
            sfcInvCheckMainMapper.execUpdateSql(selectSql);



            /*************处理完工入库单子表*************************/
            SfcInvCheckDetail sfcInvCheckDetail = new SfcInvCheckDetail();

            //入库通知单主表的主键（单据号）
            sfcInvCheckDetail.setSfcInvCheckMain(sfcInvCheckMain);
            sfcInvCheckDetail.setBillNoDetail(sfcInvCheckMain.getBillNo()); //子表中单独保存主表的单据号


            //子表序号：同质检商量后，每个子表只有一条记录，商品数量为1
            sfcInvCheckDetail.setSerialNo(1);

            //机器序列号
            sfcInvCheckDetail.setMSerialNo(list.get("a.mserialno"));
            //机器二维码
            String objSN=this.generateObjSN(sfcInvCheckMain.getBillNo(),list.get("b.code"));
            sfcInvCheckDetail.setObjSn(objSN);
            this.fillObjSN(list.get("a.mserialno"),objSN);
            //物料相关的信息
            sfcInvCheckDetail.setItemCode(list.get("b.code")); //物料编码
            sfcInvCheckDetail.setItemName(list.get("b.name")); //物料名称
            sfcInvCheckDetail.setItemSpecmodel(list.get("b.spec_model"));//规格型号
            sfcInvCheckDetail.setUnitCode(list.get("b.unit_code")); //计量单位
            sfcInvCheckDetail.setItemPdn(list.get("b.draw_no")); //图号
            if(!list.get("b.plan_price").equals(""))
                sfcInvCheckDetail.setRealPrice(Double.valueOf(list.get("b.plan_price"))); //价格


            //处理数量信息
            sfcInvCheckDetail.setRealQty(Double.valueOf("1.0"));

            // 设置框架自动生成的字段
            sfcInvCheckDetail.setId(sfcInvCheckMain.getId()+"01");
            sfcInvCheckDetail.setRemarks("");
            sfcInvCheckDetail.setCreateBy(UserUtils.getUser());
            sfcInvCheckDetail.setCreateDate(new Date());
            sfcInvCheckDetail.setUpdateBy(UserUtils.getUser());
            sfcInvCheckDetail.setUpdateDate(new Date());
            sfcInvCheckDetail.setDelFlag("0");

            sfcInvCheckDetailMapper.insert(sfcInvCheckDetail);
            sfcInvCheckMainMapper.execUpdateSql(selectSql);

        }
    }


    // 根据完工入库通知单生成入库单
    // 此程序在完工入库通知单中质检通过后执行，点击提交按钮时执行
    // 增加mpsPlanId和orderNo参数，成本核算时需要用到（生成入库单主表时把整机对应的mpsplanid和orderNo填写进去） xianbang 2018-11-16
    @Transactional(readOnly = false)
    public void generateInvImportBill(String InvCheckBillNo,String mpsPlanId,String orderNo) {
        /*****
         * 大致思路：
         * 从完工入库通知单主表中根据BillNo获取主表信息，生成入库单主表
         * 从完工入库通知单子表中根据BillNo获取子表信息，生成入库单子表
         *
         */

        /**************************生成主表信息****************/
        //处理入库单号,格式CPI201808250000
        int maxNum=0;
        String newBillNum="";
        String prefix = "CPI" + DateUtils.getYear() + DateUtils.getMonth() + DateUtils.getDay(); // 订单号前缀(CPIYYYYMMDD)
        String tmpSql = "select max(substring(bill_num,12)+0) from inv_billmain" + " where left(bill_num,11)='" + prefix + "'";
        logger.debug("  获取当前最大入库单号       " + tmpSql);
        List<Object> maxNumList = super.executeSelectSql(tmpSql);
        if (maxNumList.get(0) == null || maxNumList.get(0).toString().equals("")) { // 如果为空的话
            maxNum=1;
        } else {
            maxNum = Double.valueOf(maxNumList.get(0).toString()).intValue() + 1;
        }

        newBillNum+=prefix;
        if ((maxNum+"").length()==4)
            newBillNum+=maxNum;
        else{
            for(int i=0;i<4-(maxNum+"").length();i++){
                newBillNum+="0";
            }
            newBillNum+=maxNum;
        }
        logger.debug("  tmpNo       " + newBillNum);

        //获取核算期
        tmpSql = "select period_id from mdm_period"
                + " where period_begin<='" + DateUtils.getDate() +"'"
                + " and period_end>='"+ DateUtils.getDate() +"'";
        List<Object> periodIDList = super.executeSelectSql(tmpSql);
        String periodID = periodIDList.get(0).toString();

        //获取部门代码和名称
        User u = UserUtils.getUser();
        UserUtils.getUser();
        Office o = officeService.get(u.getOffice().getId());

        //生成主表的SQL语句
        String insertSql="insert into inv_billmain ("
                +"bill_num,"  //单据号
                +"bill_date," //日期
                +"dept_id,"  //部门号
                +"dept_name,"// 部门名称
                +"io_type,"  //单据类型
                +"io_flag,"  //单据标记
                +"bill_person,"  //经办人
                +"cor_bill_num,"  //对应单据号
                +"bill_flag," //过帐标志
                +"period_id," //核算期
                +"bill_empid,"  //制单人编码
                +"bill_empname,"
                +"id,"
                +"mps_plan_id,"
                +"order_id,"
                +"del_flag)" //制单人

                +" values('"
                +newBillNum+"','"
                +DateUtils.getDate("yyyy-MM-dd HH:mm:ss")+"','"
                +o.getCode()+"','"  //部门代码
                +o.getName()+"','"  //部门名称
                +"CI01','"
                +"I','"
                +UserUtils.getUser().getId()+"','"
                +InvCheckBillNo+"','"
                +"A','"
                +periodID+"','"
                +UserUtils.getUser().getId()+"','"
                +UserUtils.getUser().getName()+"','"
                +newBillNum+"','"
                +mpsPlanId+"','"
                +orderNo+"','"
                +"0')";

        super.mapper.execInsertSql(insertSql);


        /**************************生成子表信息****************/

        /* 读取完工入库通知单子表信息 */
        String attributes = "b.id,a.item_code,a.item_name,a.item_pdn,a.item_specmodel,a.unit_code,"
                + "a.real_price,a.real_qty,a.serial_no,a.mserialno";
        String selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
                + ") FROM sfc_invcheckdetail a left join mdm_item b on b.code=a.item_code "
                + " where a.bill_No='" + InvCheckBillNo.trim() + "'";
        logger.debug(" 读取完工入库通知单子表 SQL " + selectSql);
        String attrFormat = attributes;
        List<Map<String, String>> invCheckDetailList = xhcUtil.resultToList(super.executeSelectSql(selectSql),
                attrFormat, "###");

        for (Map<String, String> invCheckDetail:invCheckDetailList) {
            //从订单表中获取实际价格。
            selectSql = "select prod_price FROM sal_order_item "
                    + " where order_code in (select id from sal_order where order_code='" + orderNo + "')"
                    + " and prod_code='" + invCheckDetail.get("a.item_name")+"'"; //订单子表中保存的ordercode还是ID，需要处理下
            //logger.debug(" 读取需求订单号 SQL " + selectSql);
            List<Object> reqIDList = super.executeSelectSql(selectSql);

            //如果读取的实际价格失败，则为计划价格填充
            double prod_price=0.0;
            double plan_price= Double.valueOf(("".equals(invCheckDetail.get("a.real_price"))?"0":invCheckDetail.get("a.real_price")));
            if (reqIDList!=null && reqIDList.size()>0){
                prod_price=Double.valueOf(reqIDList.get(0).toString());
            }
            else
                prod_price=plan_price;

            //产品数量
            double item_qty=Double.valueOf(("".equals(invCheckDetail.get("a.real_qty"))?"0":invCheckDetail.get("a.real_qty")));


            //生成子表的SQL语句
            insertSql="insert into inv_billdetail ("
                    +"bill_num,"  //单据号
                    +"serial_num," //序号
                    +"item_code,"  //物料编码
                    +"item_name,"// 物料名称
                    +"item_pdn,"  //图号
                    +"item_spec,"  //规格型号
                    +"meas_unit," //计量单位
                    +"item_batch," //物料批号
                    +"item_qty,"  //数量
                    +"plan_price," //计划价格
                    +"item_price," //实际单价
                    +"item_sum," //实际金额
                    +"id," //框架ID
                    +"del_flag)" //制单人

                    +" values('"
                    + newBillNum+"','"  //主表的单据号，同主表的ID
                    + invCheckDetail.get("a.serial_no")+"','"
                    + invCheckDetail.get("a.item_code")+"','"
                    + invCheckDetail.get("a.item_name")+"','"
                    + invCheckDetail.get("a.item_pdn")+"','"
                    + invCheckDetail.get("a.item_specmodel")+"','"
                    + invCheckDetail.get("a.unit_code")+"','"
                    + DateUtils.getYear() + DateUtils.getMonth() + DateUtils.getDay() +"',"
                    + ("".equals(invCheckDetail.get("a.real_qty"))?"0":invCheckDetail.get("a.real_qty"))+","
                    + ("".equals(invCheckDetail.get("a.real_price"))?"0":invCheckDetail.get("a.real_price"))+","
                    + prod_price + ","
                    + prod_price*item_qty + ",'"
                    + newBillNum+invCheckDetail.get("a.serial_no")+"','"
                    +"0')";

            logger.debug(insertSql);
            super.mapper.execInsertSql(insertSql);

        }
    }


    /*生成机器二维码号
     *
     * @param invBillNo  完工入库单号
     * @param prodCode    产品号
     *
     */

    @Transactional(readOnly = false)
    public String generateObjSN(String invBillNo, String prodCode) {


        //机器二维码格式： “15位完工路线单单据号+12位物料号+8位批次号（主生产计划日期）+5位流水号”共计40位组成
        //

        //读取机器二维码最大序号
        String prefixObjSN =invBillNo + prodCode + DateUtils.getYear()+DateUtils.getMonth()+DateUtils.getDay();
        String selectSql = "select max(right(obj_sn,5)+0) from ppc_mserialno where left(obj_sn,35)='" + prefixObjSN + "'";
        logger.debug(" 最大二维码序号  " + selectSql);
        List<Object> maxObjSN = super.executeSelectSql(selectSql); // 读取二维码最大的序号

        int curMaxObjSN;
        if (maxObjSN==null || maxObjSN.get(0) == null) { // 没有，序号设置为1
            curMaxObjSN = 1;
        } else {// 序号加1
            curMaxObjSN = Double.valueOf(maxObjSN.get(0).toString()).intValue();
            curMaxObjSN++;
        }
        logger.debug(" 机器序列号最大序号  " + curMaxObjSN);
        return prefixObjSN+String.format("%05d",curMaxObjSN);
    }

    /*在ppc_mserialno表格中回填机器二维码号
     * 在sfc_processroutinedetail表格中填写
     *
     * @param mSerialNo  机器序列号
     *
     */

    @Transactional(readOnly = false)
    public void fillObjSN(String mSerialNo,String objSN) {

        //1.在ppc_mserialno表格中回填机器二维码号
        String updateSql = "update ppc_mserialno "
                + " set obj_sn='" + objSN +"'"
                + " where mserialno='" + mSerialNo +"'";
        logger.debug("  回填机器二维码的SQL   " + updateSql);
        sfcInvCheckMainMapper.execUpdateSql(updateSql);

        //2.在sfc_processroutinedetail表格中填写机器二维码
        updateSql = "update sfc_processroutinedetail "
                + " set obj_sn='" + objSN +"'"
                + " where mserialno='" + mSerialNo +"'";
        logger.debug("  在工艺路线明细中回填机器二维码的SQL   " + updateSql);
        sfcInvCheckMainMapper.execUpdateSql(updateSql);



    }



    // 根据加工路线单明细表，生成每个产品在每个工艺上的物料需求
    @Transactional(readOnly = false)
    public void generateProcessMaterial(String routineBillNo,int seqNo,String routingCode) {
        /**
         * 思路：
         * 1.从加工路线单明细表格中，获取加工路线单中的单据号、产品编码、工作中心等信息；
         * 2.从工艺路线材料关系表中获取需要的材料信息
         */


        /**********************************************处理主表*******************************************/

        /*****读取需要的加工路线单明细信息 *****/
        //部分信息可能无用，暂时保留
        String attributes = "a.centerCode,a.isLastRouting,a.prodCode,a.planQty,a.planBDate,"
                + "a.realQty,a.planEDate,a.billState,a.seqno,a.personInCharge,a.actualcentercode,"
                + "b.code,b.name,c.center_name,d.routing_name";
        String selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
                + ") FROM sfc_processroutinedetail a left join mdm_item b on a.prodCode=b.code "
                + " left join mdm_workcenter c on a.centercode=c.center_code"
                + " left join mdm_routing d on a.routingCode=d.routing_code"
                + " where a.routineBillNo='" + routineBillNo.trim()
                + "' and a.routingCode='" + routingCode + "' and a.seqno=" + seqNo;
        logger.debug(" 加工路线单明细表信息 SQL " + selectSql);
        String attrFormat = attributes;
        List<Map<String, String>> processRoutineList = xhcUtil.resultToList(super.executeSelectSql(selectSql),
                attrFormat, "###");


        /*****读取需要的物料*****/
        //从工艺路线物料关系表 MDM_RoutingItem中获取工艺中需要的物料,MDM_routingItem表格中保存的routing_code是UID,需要转换
        attributes = "a.item_code,b.name,a.req_qty";
        selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
                + ") from MDM_RoutingItem a left join mdm_item b on a.item_code=b.code"
                + " where a.routing_code in (select distinct id from mdm_routing where routing_code='"
                + routingCode.trim() + "') and a.product_code='" + processRoutineList.get(0).get("a.prodCode") + "'";
        attrFormat = attributes;
        List<Map<String, String>> routingItemList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");


        //循环处理每个物料
        for (Map<String, String> itemList:routingItemList){

            ProcessMaterial processMaterial = new ProcessMaterial();

            //加工路线单号、单件序号、工艺路线号
            processMaterial.setRoutineBillNo(routineBillNo);
            processMaterial.setSeqNo(seqNo);
            processMaterial.setRoutingCode(routingCode);
            processMaterial.setRoutingName(processRoutineList.get(0).get("d.routing_name")); //工艺路线名称

            //工作中心代码和名称
            processMaterial.setCenterCode(processRoutineList.get(0).get("a.centerCode"));
            processMaterial.setCenterName(processRoutineList.get(0).get("c.center_name"));

            //物料编码和名称
            processMaterial.setItemCode(itemList.get("a.item_code"));
            processMaterial.setItemName(itemList.get("b.name"));

            //需安装数量和已安装数量
            processMaterial.setAssembleQty(Double.valueOf(itemList.get("a.req_qty")));
            processMaterial.setFinishedQty(Double.valueOf("0.0"));

            // 设置框架自动生成的字段
            processMaterial.setId(routineBillNo+"-"+routingCode+"-"+seqNo+processMaterial.getItemCode());
            processMaterial.setRemarks("");
            processMaterial.setCreateBy(UserUtils.getUser());
            processMaterial.setCreateDate(new Date());
            processMaterial.setUpdateBy(UserUtils.getUser());
            processMaterial.setUpdateDate(new Date());
            processMaterial.setDelFlag("0");

            logger.debug("  需要插入的数据  " + processMaterial.toString());
            processMaterialMapper.insert(processMaterial);

            /**********************************************处理子表*******************************************/
            //按照数量展开成单个物料，以用来记录二维码数据
            for(int i=0;i<Integer.valueOf(itemList.get("a.req_qty"));i++){
                String tmpSql = "insert into sfc_processmaterialdetail "
                        + "(id,routinebillno,seqno,routingcode,routingname,itemcode,itemname,"
                        + "assembleqty,finishedqty,centercode,centername,itemSN)"
                        + " select '" + processMaterial.getId()+i+"',routine_bill_no,seq_no,routing_code,routing_name,item_code,item_name,"
                        + "1,0,center_code,center_name,''"
                        + " from sfc_processmaterial where id='" + processMaterial.getId()+"'";
                logger.debug("  需要插入的子表SQL  " + tmpSql);
                super.mapper.execInsertSql(tmpSql);
            }
        }
    }

    /*
    返修换件程序

    1.用户通过物料安装清单录入界面，选择某道装配工艺（必须是已经完成的，且为装配工艺），点击“换件”操作。
    2.设计两个表格，一个用来保存被换掉物料的信息（物料替换日志），一个用来保存需要重新执行的工艺信息（工艺重新执行日志）
    2.1 在物料替换日志表中，记录原物料（被替换的）的信息，然后修改该工艺的状态为未完成，允许用户替换物料，替换完后重新走提交流程（原来操作）
    2.2 修改之后所有的非装配工艺状态。如果工艺状态为已经完成，则记录原工艺状态到工艺重新执行日志中，修改工艺状态为未完成，如果工艺状态为未完成，则不动。
    @param 参数ID为表格sfc_processmaterialdetail中的ID，用来定位所选行数据，需要替换返修的物料
     */
    @Transactional(readOnly = false)
    public void replaceAndReassemble (String ID) {

        //读取物料安装单原有信息
        String attributes = "a.routinebillno,a.seqno,a.routingcode,a.itemcode,a.itemsn";
        String selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
                + ") FROM sfc_processmaterialdetail a"
                + " where a.id='" + ID +"'";
        logger.debug(" 物料安装明细表信息 SQL " + selectSql);
        String attrFormat = attributes;
        List<Map<String, String>> processMaterialList = xhcUtil.resultToList(super.executeSelectSql(selectSql),
                attrFormat, "###");
        if(processMaterialList==null)
            return;

        //将原有的装配信息写入到日志表中
        //1.日志表中的ID重新生成，避免重复
        //2.日志表中增加增加了新的字段，保存原有数据的ID
        //3.转配时间问题：如果更换新件的时候，会记录更换时间，也就是说物料安装表中保存了最新的安装时间，则此处不用处理；
        //否则，需要记录更新时间。因为如果存在多次替换的问题，则需要通过替换时间来区分最新的一次。
        String insertSQL;
        insertSQL=" insert into sfc_material_replace_log (" +
                "sfc_material_replace_log.id," +
                "sfc_material_replace_log.create_by," +
                "sfc_material_replace_log.create_date," +
                "sfc_material_replace_log.update_by," +
                "sfc_material_replace_log.update_date," +
                "sfc_material_replace_log.remarks," +
                "sfc_material_replace_log.del_flag," +
                "sfc_material_replace_log.routinebillno," +
                "sfc_material_replace_log.seqno," +
                "sfc_material_replace_log.routingcode," +
                "sfc_material_replace_log.itemcode," +
                "sfc_material_replace_log.itemsn," +
                "sfc_material_replace_log.centercode," +
                "sfc_material_replace_log.routingname," +
                "sfc_material_replace_log.itemname," +
                "sfc_material_replace_log.assembleqty," +
                "sfc_material_replace_log.finishedqty," +
                "sfc_material_replace_log.centername," +
                "sfc_material_replace_log.assemblePersonID," +
                "sfc_material_replace_log.assemblePersonName," +
                "sfc_material_replace_log.assembleTime," +
                "sfc_material_replace_log.actualTeamCode," +
                "sfc_material_replace_log.hasQualityPro," +
                "sfc_material_replace_log.qualityProblem," +
                "originalid)" +

                " select " +
                "'" + IdGen.uuid() +"'," +
                "sfc_processmaterialdetail.create_by," +
                "sfc_processmaterialdetail.create_date," +
                "sfc_processmaterialdetail.update_by," +
                "sfc_processmaterialdetail.update_date," +
                "sfc_processmaterialdetail.remarks," +
                "sfc_processmaterialdetail.del_flag," +
                "sfc_processmaterialdetail.routinebillno," +
                "sfc_processmaterialdetail.seqno," +
                "sfc_processmaterialdetail.routingcode," +
                "sfc_processmaterialdetail.itemcode," +
                "sfc_processmaterialdetail.itemsn," +
                "sfc_processmaterialdetail.centercode," +
                "sfc_processmaterialdetail.routingname," +
                "sfc_processmaterialdetail.itemname," +
                "sfc_processmaterialdetail.assembleqty," +
                "sfc_processmaterialdetail.finishedqty," +
                "sfc_processmaterialdetail.centername," +
                "sfc_processmaterialdetail.assemblePersonID," +
                "sfc_processmaterialdetail.assemblePersonName," +
                "sfc_processmaterialdetail.assembleTime," +
                "sfc_processmaterialdetail.actualTeamCode," +
                "sfc_processmaterialdetail.hasQualityPro," +
                "sfc_processmaterialdetail.qualityProblem," +
                "sfc_processmaterialdetail.id" +

                " FROM sfc_processmaterialdetail" +
                " where sfc_processmaterialdetail.id='" + ID +"'";

        logger.debug(" 插入日志SQL " + insertSQL);
        processMaterialDetailMapper.execInsertSql(insertSQL);

        //修改总装工艺的状态为“未完工”，以重新扫描新的零件二维码
        String updateSQL=" update sfc_processroutinedetail" +
                " set billstate='C' "
                + " where routinebillno='" + processMaterialList.get(0).get("a.routinebillno") + "'"  //相同加工路线
                + " and seqno=" + processMaterialList.get(0).get("a.seqno")  //相同单件序号，即同一产品
                + " and routingcode='" + processMaterialList.get(0).get("a.routingcode") + "'";//相同工艺路线号
        processMaterialDetailMapper.execUpdateSql(updateSQL);



        //查询后续的工艺号，如果是非装配工艺，且状态为已经处理。
        attributes = "a.id,a.billstate";
        selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
                + ") FROM sfc_processroutinedetail a,mdm_routing b"
                + " where a.routingcode=b.routing_code and a.prodcode=b.product_code"  //连接工艺路线表
                + " and a.routinebillno='" + processMaterialList.get(0).get("a.routinebillno") + "'"  //相同加工路线
                + " and a.seqno=" + processMaterialList.get(0).get("a.seqno")  //相同单件序号，即同一产品
                + " and (b.assembleflag='N' or b.assembleflag='n')" //非装配工艺的
                + " and a.billstate='O'"; //已经完工的
        logger.debug(" 加工路线明细信息 SQL " + selectSql);
        attrFormat = attributes;
        List<Map<String, String>> processRoutineDetailList = xhcUtil.resultToList(super.executeSelectSql(selectSql),
                attrFormat, "###");

        for(Map<String, String> processRoutineDetail: processRoutineDetailList){
            //1.记录信息到日志中；
            insertSQL="insert into sfc_processroutine_change_log(sfc_processroutine_change_log.id," +
                    "sfc_processroutine_change_log.create_by," +
                    "sfc_processroutine_change_log.create_date," +
                    "sfc_processroutine_change_log.update_by," +
                    "sfc_processroutine_change_log.update_date," +
                    "sfc_processroutine_change_log.remarks," +
                    "sfc_processroutine_change_log.del_flag," +
                    "sfc_processroutine_change_log.processbillno," +
                    "sfc_processroutine_change_log.batchno," +
                    "sfc_processroutine_change_log.routinebillno," +
                    "sfc_processroutine_change_log.produceno," +
                    "sfc_processroutine_change_log.routingcode," +
                    "sfc_processroutine_change_log.workprocedurecode," +
                    "sfc_processroutine_change_log.centercode," +
                    "sfc_processroutine_change_log.islastrouting," +
                    "sfc_processroutine_change_log.prodcode," +
                    "sfc_processroutine_change_log.planqty," +
                    "sfc_processroutine_change_log.planbdate," +
                    "sfc_processroutine_change_log.realqty," +
                    "sfc_processroutine_change_log.planedate," +
                    "sfc_processroutine_change_log.billstate," +
                    "sfc_processroutine_change_log.assignedstate," +
                    "sfc_processroutine_change_log.makepid," +
                    "sfc_processroutine_change_log.actualcentercode," +
                    "sfc_processroutine_change_log.personincharge," +
                    "sfc_processroutine_change_log.teamcode," +
                    "sfc_processroutine_change_log.actualteamcode," +
                    "sfc_processroutine_change_log.shiftname," +
                    "sfc_processroutine_change_log.workhour," +
                    "sfc_processroutine_change_log.actualworkhour," +
                    "sfc_processroutine_change_log.makedate," +
                    "sfc_processroutine_change_log.confirmpid," +
                    "sfc_processroutine_change_log.confirmdate," +
                    "sfc_processroutine_change_log.deliverypid," +
                    "sfc_processroutine_change_log.deliverydate," +
                    "sfc_processroutine_change_log.prodname," +
                    "sfc_processroutine_change_log.routingname," +
                    "sfc_processroutine_change_log.seqno," +
                    "sfc_processroutine_change_log.invcheckstate," +
                    "sfc_processroutine_change_log.mserialno," +
                    "sfc_processroutine_change_log.obj_sn," +
                    "sfc_processroutine_change_log.cosbillnum," +
                    "sfc_processroutine_change_log.actualbdate," +
                    "sfc_processroutine_change_log.originalid)" +

                    "SELECT" +
                    "'" + IdGen.uuid() +"'," +
                    "sfc_processroutinedetail.create_by," +
                    "sfc_processroutinedetail.create_date," +
                    "sfc_processroutinedetail.update_by," +
                    "sfc_processroutinedetail.update_date," +
                    "sfc_processroutinedetail.remarks," +
                    "sfc_processroutinedetail.del_flag," +
                    "sfc_processroutinedetail.processbillno," +
                    "sfc_processroutinedetail.batchno," +
                    "sfc_processroutinedetail.routinebillno," +
                    "sfc_processroutinedetail.produceno," +
                    "sfc_processroutinedetail.routingcode," +
                    "sfc_processroutinedetail.workprocedurecode," +
                    "sfc_processroutinedetail.centercode," +
                    "sfc_processroutinedetail.islastrouting," +
                    "sfc_processroutinedetail.prodcode," +
                    "sfc_processroutinedetail.planqty," +
                    "sfc_processroutinedetail.planbdate," +
                    "sfc_processroutinedetail.realqty," +
                    "sfc_processroutinedetail.planedate," +
                    "sfc_processroutinedetail.billstate," +
                    "sfc_processroutinedetail.assignedstate," +
                    "sfc_processroutinedetail.makepid," +
                    "sfc_processroutinedetail.actualcentercode," +
                    "sfc_processroutinedetail.personincharge," +
                    "sfc_processroutinedetail.teamcode," +
                    "sfc_processroutinedetail.actualteamcode," +
                    "sfc_processroutinedetail.shiftname," +
                    "sfc_processroutinedetail.workhour," +
                    "sfc_processroutinedetail.actualworkhour," +
                    "sfc_processroutinedetail.makedate," +
                    "sfc_processroutinedetail.confirmpid," +
                    "sfc_processroutinedetail.confirmdate," +
                    "sfc_processroutinedetail.deliverypid," +
                    "sfc_processroutinedetail.deliverydate," +
                    "sfc_processroutinedetail.prodname," +
                    "sfc_processroutinedetail.routingname," +
                    "sfc_processroutinedetail.seqno," +
                    "sfc_processroutinedetail.invcheckstate," +
                    "sfc_processroutinedetail.mserialno," +
                    "sfc_processroutinedetail.obj_sn," +
                    "sfc_processroutinedetail.cosbillnum," +
                    "sfc_processroutinedetail.actualbdate," +
                    "sfc_processroutinedetail.id" +

                    " FROM sfc_processroutinedetail" +
                    " where sfc_processroutinedetail.id='" + processRoutineDetail.get("a.id") + "'";

            logger.debug(" 插入加工路线单明细更新日志SQL " + insertSQL);
            processMaterialDetailMapper.execInsertSql(insertSQL);

            //2.修改状态为未处理。
            updateSQL=" update sfc_processroutinedetail" +
                    " set billstate='C' " +
                    " where id='" + processRoutineDetail.get("a.id") + "'";
            processMaterialDetailMapper.execUpdateSql(updateSQL);
       }
    }
}