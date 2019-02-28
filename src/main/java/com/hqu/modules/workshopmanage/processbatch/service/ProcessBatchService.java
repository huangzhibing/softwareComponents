/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.processbatch.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hqu.modules.workshopmanage.ppc.xhcUtil;
import com.hqu.modules.workshopmanage.process.entity.SfcProcessDetail;
import com.hqu.modules.workshopmanage.process.mapper.SfcProcessDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.workshopmanage.processbatch.entity.ProcessBatch;
import com.hqu.modules.workshopmanage.processbatch.mapper.ProcessBatchMapper;
import com.hqu.modules.workshopmanage.processroutine.entity.ProcessRoutine;
import com.hqu.modules.workshopmanage.processroutine.mapper.ProcessRoutineMapper;
/**
 * 车间作业计划分批Service
 * @author xhc
 * @version 2018-08-04
 */
@Service
@Transactional(readOnly = true)
public class ProcessBatchService extends CrudService<SfcProcessDetailMapper, SfcProcessDetail> {

	@Autowired
	private ProcessBatchMapper processBatchMapper;
	@Autowired
	private SfcProcessDetailMapper sfcProcessDetailMapper;
	
	@Autowired
	ProcessRoutineMapper processRoutineMapper;

	public SfcProcessDetail get(String id) {
		SfcProcessDetail sfcProcessDetail = super.get(id);
		sfcProcessDetail.setProcessBatchList(processBatchMapper.findList(new ProcessBatch(sfcProcessDetail)));
		return sfcProcessDetail;
	}

	public List<SfcProcessDetail> findList(SfcProcessDetail sfcProcessDetail) {
		return super.findList(sfcProcessDetail);
	}

	/**
	 * 查找车间作业计划表中billState = 'S' 的计划 (已经下达)，且 assignedState = "P"(未处理或者正在计划)的分页数据
	 * @param page 分页对象
	 * @param sfcProcessDetail
	 * @return
	 */
	public Page<SfcProcessDetail> findPage(Page<SfcProcessDetail> page, SfcProcessDetail sfcProcessDetail) {
		dataRuleFilter(sfcProcessDetail);
		sfcProcessDetail.setPage(page);
		page.setList(sfcProcessDetailMapper.findAssignedList(sfcProcessDetail));
		return page;
	}

	/**
	 * 查找车间作业计划表中billState = 'S' 的计划 (已经下达)，且 assignedState = "P","C"(所有状态)的分页数据
	 * @param page 分页对象
	 * @param sfcProcessDetail
	 * @return
	 */
	public Page<SfcProcessDetail> findQueryPage(Page<SfcProcessDetail> page, SfcProcessDetail sfcProcessDetail) {
		dataRuleFilter(sfcProcessDetail);
		sfcProcessDetail.setPage(page);
		page.setList(sfcProcessDetailMapper.findAssignedAllList(sfcProcessDetail));
		return page;
	}

	/**
	 * 保存车间作业计划分批状态以及分批表（子表）数据
	 * @param sfcProcessDetail
	 */
	@Transactional(readOnly = false)
	public void save(SfcProcessDetail sfcProcessDetail) {
		super.save(sfcProcessDetail);
		for (ProcessBatch processBatch : sfcProcessDetail.getProcessBatchList()){
			if (processBatch.getId() == null){
				continue;
			}
			if (ProcessBatch.DEL_FLAG_NORMAL.equals(processBatch.getDelFlag())){
				if (StringUtils.isBlank(processBatch.getId())){
					processBatch.preInsert();
					processBatch.setProcessBillNo(sfcProcessDetail.getProcessBillNo());//设置车间作业计划的编号（关联分批表数据）
					processBatchMapper.insert(processBatch);
				}else{
					processBatch.preUpdate();
					processBatch.setProcessBillNo(sfcProcessDetail.getProcessBillNo());//设置车间作业计划的编号
					processBatchMapper.update(processBatch);
				}
			}else{
				processBatchMapper.delete(processBatch);
			}
		}
		try {
			if ("C".equals(sfcProcessDetail.getAssignedState())) {//提交时 1.逐条把分批计划 自动生成 加工路线单中的工艺数据2.回填加工路线单号和分批号
				for (ProcessBatch processBatch : sfcProcessDetail.getProcessBatchList()) {
					generateRoutineInfo(processBatch.getProcessBillNo(), processBatch.getBatchNo());
					fillProcessBillBatchNo(processBatch.getProcessBillNo(),processBatch.getBatchNo());
				}
			}
		}catch (Exception e){
			logger.debug("ProcessBatchService.save.generateRoutineInfo_________________error:"+e.getMessage());
		}
	}

	@Transactional(readOnly = false)
	public void delete(SfcProcessDetail sfcProcessDetail) {
		super.delete(sfcProcessDetail);
		processBatchMapper.delete(new ProcessBatch(sfcProcessDetail));
	}
	

	// 根据车间作业计划号和批次序号，依据工艺路线表mdm_routing中的数据，自动生成加工路线单中的工艺数据
	@Transactional(readOnly = false)
	public void generateRoutineInfo(String processBillNo, int batchNo) {
		
		// 读取工艺路线表中的数据
		String attributes = "a.routing_code,a.routing_name,a.product_code,a.product_name,b.planqty,"
				+"b.planbdate,a.routing_seq_no";
		String selectSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
				+ ") FROM mdm_routing a, sfc_processbatch b" 
				+ " where b.processbillno='" + processBillNo.trim()
				+ "' and b.batchno=" + batchNo + " and a.product_code=b.prodcode "
				+ " order by a.routing_seq_no";
		logger.debug(" 读取工艺路线表中数据的SQL "+selectSql);
		String attrFormat = "routing_code,routing_name,product_code,product_name,planqty,planbdate,routing_seq_no";
		List<Map<String, String>> routineList = xhcUtil.resultToList(super.executeSelectSql(selectSql), attrFormat, "###");
		
		//读取最后一道工艺的编号   此处2018年9月4日新增，要确保mdm_routing表格中填写正确
		//此处2018年12月13日修改，修改为判断是否是最后一道装配工艺
		selectSql = "select max(a.routing_seq_no) from mdm_routing a, sfc_processbatch b "
				+" where b.processbillno='" + processBillNo.trim()
				+ "' and b.batchno=" + batchNo
				+ " and a.product_code=b.prodcode"
				+ " and a.assembleflag='Y'";
		List<Object> maxRouteNoList = super.executeSelectSql(selectSql);

		String maxRouteNo="0";
		if (maxRouteNoList!=null || maxRouteNoList.get(0).toString()!=null){
			maxRouteNo=maxRouteNoList.get(0).toString();
			logger.debug(" 最大工艺号   "+maxRouteNo);
		}


		for (Map<String, String> routine : routineList) {	

			ProcessRoutine processRoutine = new ProcessRoutine();
			
			processRoutine.setProcessBillNo(processBillNo.trim());  //车间作业计划号
			processRoutine.setBatchNo(batchNo); //批次号
			processRoutine.setRoutineBillNo(processBillNo+"-"+batchNo);  //加工路线单号
			processRoutine.setProduceNo(Integer.valueOf(routine.get("routing_seq_no")));  //显式顺序号
			processRoutine.setRoutingCode(routine.get("routing_code"));  //工艺路线号
			processRoutine.setRoutingName(routine.get("routing_name"));
			processRoutine.setProdCode(routine.get("product_code")); //物料编码
			processRoutine.setPlanQty(Double.valueOf(routine.get("planqty"))); //计划数量
			processRoutine.setPlanBData(DateUtils.parseDate(routine.get("planbdate"))); //计划开始日期
			processRoutine.setRealQty(Double.valueOf(routine.get("planqty"))); //实际生产数量，缺省等同于计划
			processRoutine.setPlanEDate(DateUtils.parseDate(routine.get("planbdate"))); //实际完工日期，缺省等同于计划
			processRoutine.setBillState("P");//单据状态
			processRoutine.setAssignedState("P");//派工状态
			processRoutine.setMakeDate(new Date());//编制日期
			processRoutine.setMakePID((UserUtils.getUser()).getId());  //编制人
			processRoutine.setShiftname("白班"); //班次，缺省为白班

			
			//处理最后一道工艺的标识
			if (routine.get("routing_seq_no").equals(maxRouteNo)){
				processRoutine.setIsLastRouting("Y");

			}else
				processRoutine.setIsLastRouting("N");
			
			//处理额定工时，目前是通过汇总工艺对应工序中的额定工时得到工艺的额定工时
			selectSql = "select sum(work_time) from mdm_routingwork"
					+" where routing_code='" + routine.get("routing_code") + "'" ;
			List<Object> sumWorkHourList = super.executeSelectSql(selectSql);			
			int sumWorkHour=0;
			if (sumWorkHourList==null)
				sumWorkHour=0;
			else 
				if (sumWorkHourList.get(0)==null || sumWorkHourList.get(0).toString()=="")
					sumWorkHour=0;
				else
					sumWorkHour=Integer.valueOf(sumWorkHourList.get(0).toString());
			
			processRoutine.setWorkhour(sumWorkHour*Double.valueOf(routine.get("planqty")));			
			
			//设置框架自动生成的字段
			processRoutine.setId(processBillNo+"-"+batchNo+"-"+routine.get("routing_code"));
			processRoutine.setRemarks("");
			processRoutine.setCreateBy(UserUtils.getUser());
			processRoutine.setCreateDate(new Date());
			processRoutine.setUpdateBy(UserUtils.getUser());
			processRoutine.setUpdateDate(new Date());
			processRoutine.setDelFlag("0");

			processRoutineMapper.insert(processRoutine);

		}
	
	}


	/**
	 * 回填ppc_mserialno表格中的车间作业计划分批信息
	 * 当分批计划提交时执行
	 * 由于分批计划提交时，会自动生成加工路线单信息，因此此时也可以回填 加工路线单号信息
	 * 注意：此程序要同调用加工路线单生成程序一起执行
	 */
	@Transactional(readOnly = false)
	public void fillProcessBillBatchNo(String processBillNo, int batchNo){

		//1.根据车间作业计划号和批次序号，从sfc_processbatch表格中获取批次的生产数量信息
		String attributes = "a.processbillno,a.batchno,a.planqty";
		String tmpSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
				+ ") FROM sfc_processbatch a"
				+ " where a.processbillno='" + processBillNo + "' "
				+ " and a.batchno=" + batchNo;
		logger.debug(" 车间作业计划分批信息    " + tmpSql);
		String attrFormat = attributes;
		List<Map<String, String>> processBatchInfoList = xhcUtil.resultToList(super.executeSelectSql(tmpSql), attrFormat, "###");

		if (processBatchInfoList==null ||processBatchInfoList.size()==0|| processBatchInfoList.get(0)==null)
			return;
		else if (Double.valueOf(processBatchInfoList.get(0).get("a.planqty"))<=0)
			return;

		int planQty= Double.valueOf(processBatchInfoList.get(0).get("a.planqty")).intValue();  //本批次需要生产的数量

		//2.从ppc_mserialno表格中，找到该processbillno对应的记录中，没有批次号的记录，取planQty个填写批次号
		attributes = "a.mserialno,a.processbillno";
		tmpSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
				+ ") FROM ppc_mserialno a"
				+ " where processbillno='" + processBillNo + "' and "
				+ " batchno=0 order by a.mserialno";
		logger.debug(" 设备序列号数据SQL    " + tmpSql);
		attrFormat = attributes;
		List<Map<String, String>> mSerialNoList = xhcUtil.resultToList(super.executeSelectSql(tmpSql), attrFormat, "###");

		int counter=1;
		for(Map<String, String> serialNo:mSerialNoList){
			if (counter>planQty) break;  //只取planQty数量的序列号

			//更新ppc_mserialno表格中的车间作业计划批次号和加工路线单编制信息
			String updateSql="update ppc_mserialno "
					+ " set batchno=" + batchNo + ","
					+ " routinebillno='" + processBillNo+"-"+ batchNo + "'"
					+ " where mserialno='" + serialNo.get("a.mserialno") + "'";
			logger.debug(" 更新车间作业计划分批号,加工路线单号SQL    " + updateSql);

			super.mapper.execUpdateSql(updateSql);

			counter++;

		}

	}

}