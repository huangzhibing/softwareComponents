/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreport.mapper;

import java.util.List;

import com.hqu.modules.inventorymanage.invcheckmain.entity.ReinvCheckMain;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckMain;
import com.hqu.modules.qualitymanage.purreport.entity.PurReport;
import com.hqu.modules.qualitymanage.verifyqcnorm.entity.VerifyQCNorm;
import com.hqu.modules.workshopmanage.sfcinvcheckmain.entity.SfcInvCheckMain;
import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;

/**
 * 检验单(采购/装配/整机检测)MAPPER接口
 * @author 孙映川
 * @version 2018-04-05
 */
@MyBatisMapper
public interface PurReportMapper extends BaseMapper<PurReport> {
	
	//查找条件满足为检验状态‘未审核’并且满足cperson_code是当前人的记录
	public List<PurReport> findListWithOutCheck(PurReport PurReport);
	
	//按照业务主键查找PurReport对象
	public List<PurReport> getByReportId(String reportId);
	
	public void updateByReportId(PurReport PurReport);
	
	public int updateProcessInstanceId(PurReport PurReport);
	
	public void insertVerifyQCNorm(VerifyQCNorm verifyQCNorm);
	
	public void myInsert(PurReport PurReport);
	
	public void myUpdata(PurReport PurReport);

	void myInsertREINV(PurReport PurReport);

	void myUpdataREINV(PurReport PurReport);
	
	public List<InvCheckMain> findInvList(InvCheckMain invCheckMain);
	
	public List<SfcInvCheckMain> findSFCList(SfcInvCheckMain sfcInvCheckMain);
	
	public List<ReinvCheckMain> findReInvList(ReinvCheckMain reinvCheckMain);
	//审核用
	public void updateforAudit(PurReport PurReport);
}