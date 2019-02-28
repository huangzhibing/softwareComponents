/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.process.service;

import com.hqu.modules.workshopmanage.ppc.entity.MpsPlan;
import com.hqu.modules.workshopmanage.ppc.mapper.MpsPlanMapper;
import com.hqu.modules.workshopmanage.ppc.xhcUtil;
import com.hqu.modules.workshopmanage.process.entity.SfcProcessDetail;
import com.hqu.modules.workshopmanage.process.mapper.SfcProcessDetailMapper;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 车间作业计划管理Service
 * @author xhc
 * @version 2018-08-02
 */
@Service
@Transactional(readOnly = true)
public class ProcessService extends CrudService<MpsPlanMapper, MpsPlan> {

	@Autowired
	private SfcProcessDetailMapper sfcProcessDetailMapper;
	@Autowired
	private MpsPlanMapper  mpsPlanMapper;

	public MpsPlan get(String id) {
		MpsPlan mpsPlan = super.get(id);
		mpsPlan.setSfcProcessDetailList(sfcProcessDetailMapper.findList(new SfcProcessDetail(mpsPlan)));
		return mpsPlan;
	}

	public List<MpsPlan> findList(MpsPlan mpsPlan) {
		return mpsPlanMapper.findSFCDealList(mpsPlan);
	}


	/**
	 * 查找MPS计划状态为 已下达（S），车间处理状态为所有的MPS分页数据
	 * @param page 分页对象
	 * @param mpsPlan
	 * @return
	 */
	public Page<MpsPlan> findAllPage(Page<MpsPlan> page, MpsPlan mpsPlan) {
		dataRuleFilter(mpsPlan);
		mpsPlan.setPage(page);
		page.setList(mpsPlanMapper.findSFCDealAllList(mpsPlan));
		return page;

	}

	/**
	 * 查找MPS计划状态为 已下达（S），车间处理状态为“未处理”和“已编计划”的计划 N or P的MPS分页数据
	 * @param page 分页对象
	 * @param mpsPlan
	 * @return
	 */
	public Page<MpsPlan> findPage(Page<MpsPlan> page, MpsPlan mpsPlan) {
		dataRuleFilter(mpsPlan);
		mpsPlan.setPage(page);
		page.setList(mpsPlanMapper.findSFCDealList(mpsPlan));
		return page;

	}


	/**
	 * 查找MPS计划状态为 已下达（S），车间处理状态为“已提交”C的MPS分页数据
	 * @param page 分页对象
	 * @param mpsPlan
	 * @return
	 */
	public Page<MpsPlan> findOrderPage(Page<MpsPlan> page, MpsPlan mpsPlan) {
		dataRuleFilter(mpsPlan);
		mpsPlan.setPage(page);
		page.setList(mpsPlanMapper.findSFCDealCList(mpsPlan));
		return page;
	}

	/**
	 * 保存Mps状态和车间作业计划（子表）数据
	 * @param mpsPlan
	 */
	@Transactional(readOnly = false)
	public void save(MpsPlan mpsPlan) {
		super.save(mpsPlan);
		for (SfcProcessDetail sfcProcessDetail : mpsPlan.getSfcProcessDetailList()){
			if (sfcProcessDetail.getId() == null){
				continue;
			}
			if (SfcProcessDetail.DEL_FLAG_NORMAL.equals(sfcProcessDetail.getDelFlag())){
				if (StringUtils.isBlank(sfcProcessDetail.getId())){
					sfcProcessDetail.preInsert();
					sfcProcessDetail.setMpsPlanId(mpsPlan.getMpsPlanid());//设置MPSPlanID关联MPS;
					sfcProcessDetailMapper.insert(sfcProcessDetail);
				}else{
					sfcProcessDetail.preUpdate();
					sfcProcessDetail.setMpsPlanId(mpsPlan.getMpsPlanid());//设置MPSPlanID关联MPS;
					sfcProcessDetailMapper.update(sfcProcessDetail);
				}
			}else{
				sfcProcessDetailMapper.delete(sfcProcessDetail);
			}
		}

		//提交时回填车间作业计划号
		if("C".equals(mpsPlan.getSFCDealStatus())){
			for(SfcProcessDetail sfcProcessDetail:mpsPlan.getSfcProcessDetailList()){
				if(sfcProcessDetail.DEL_FLAG_NORMAL.equals(sfcProcessDetail.getDelFlag())) {//该项不为删除项才回填
					fillProcessBillNo(sfcProcessDetail.getProcessBillNo());
				}
			}
		}
	}

	@Transactional(readOnly = false)
	public void delete(MpsPlan mpsPlan) {
		super.delete(mpsPlan);
		sfcProcessDetailMapper.delete(new SfcProcessDetail(mpsPlan));
	}

	/**
	 * 回填ppc_mserialno表格中的车间作业计划号信息
	 * 当车间作业计划提交时处理
	 */
	@Transactional(readOnly = false)
	public void fillProcessBillNo(String processBillNo){

		//1.根据车间作业计划号，从sfc_processdetail表格中获取主生产计划号、生产数量信息
		String attributes = "a.processbillno,a.mpsplanid,a.planqty";
		String tmpSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
				+ ") FROM sfc_processdetail a"
				+ " where processbillno='" + processBillNo + "'";
		logger.debug(" 车间作业计划信息    " + tmpSql);
		String attrFormat = attributes;
		List<Map<String, String>> processBillInfoList = xhcUtil.resultToList(super.executeSelectSql(tmpSql), attrFormat, "###");

		if (processBillInfoList==null ||processBillInfoList.size()==0|| processBillInfoList.get(0)==null)
			return;
		else if (Double.valueOf(processBillInfoList.get(0).get("a.planqty"))<=0)
			return;

		int planQty= Double.valueOf(processBillInfoList.get(0).get("a.planqty")).intValue();  //需要生产的数量

		//2.从ppc_mserialno表格中，根据主生产计划号获取processbillno没有填写的记录，取planQty个填写车间作业计划号
		attributes = "a.mserialno,a.mpsplanid";
		tmpSql = "select concat(" + xhcUtil.addSplitter(attributes, "###")
				+ ") FROM ppc_mserialno a"
				+ " where mpsplanid='" + processBillInfoList.get(0).get("a.mpsplanid") + "' and "
		        + " processbillno='N/A' order by a.mserialno";
		logger.debug(" 设备序列号数据SQL    " + tmpSql);
		attrFormat = attributes;
		List<Map<String, String>> mSerialNoList = xhcUtil.resultToList(super.executeSelectSql(tmpSql), attrFormat, "###");

		int counter=1;
		for(Map<String, String> serialNo:mSerialNoList){
			if (counter>planQty) break;  //只取planQty数量的序列号

			//更新ppc_mserialno表格中的车间作业计划号信息
			String updateSql="update ppc_mserialno "
					+ " set processbillno='" + processBillNo + "'"
					+ " where mserialno='" + serialNo.get("a.mserialno") + "'";
			logger.debug(" 更新车间作业计划号SQL    " + updateSql);

			super.mapper.execUpdateSql(updateSql);

			counter++;

		}



	}

}