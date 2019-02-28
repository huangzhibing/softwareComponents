/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreport.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hqu.modules.qualitymanage.purreport.entity.PurReport;
import com.hqu.modules.qualitymanage.purreport.entity.PurReportRSn;
import com.hqu.modules.qualitymanage.purreport.mapper.InvReportSubmitMapper;
import com.hqu.modules.qualitymanage.purreport.mapper.PurReportRSnSubmitMapper;
import com.hqu.modules.qualitymanage.purreport.mapper.SfcReportSubmitMapper;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;

/**
 * 检验单(采购/装配/整机检测)Service
 * @author 孙映川
 * @version 2018-04-13
 */
@Service
@Transactional(readOnly = true)
public class InvReportSubmitService extends CrudService<InvReportSubmitMapper, PurReport> {

	@Autowired
	private PurReportRSnSubmitMapper purReportRSnMapper;
	@Autowired
	private InvReportSubmitMapper invReportSubmitMapper;
	public PurReport get(String id) {
		PurReport purReport = super.get(id);
		purReport.setPurReportRSnList(purReportRSnMapper.findList(new PurReportRSn(purReport)));
		return purReport;
	}
	
	public List<PurReport> findList(PurReport purReport) {
		return super.findList(purReport);
	}
	public List<PurReport> findListNew(PurReport purReport) {
		return invReportSubmitMapper.findListNew(purReport);
	}
	public Page<PurReport> findPage(Page<PurReport> page, PurReport purReport) {
		return super.findPage(page, purReport);
	}
	
	@Transactional(readOnly = false)
	public void save(PurReport purReport) {
		super.save(purReport);
		for (PurReportRSn purReportRSn : purReport.getPurReportRSnList()){
			if (purReportRSn.getId() == null){
				continue;
			}
			if (PurReportRSn.DEL_FLAG_NORMAL.equals(purReportRSn.getDelFlag())){
				if (StringUtils.isBlank(purReportRSn.getId())){
					purReportRSn.setPurReport(purReport);
					purReportRSn.preInsert();
					purReportRSnMapper.insert(purReportRSn);
				}else{
					purReportRSn.preUpdate();
					purReportRSnMapper.update(purReportRSn);
				}
			}else{
				purReportRSnMapper.delete(purReportRSn);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(PurReport purReport) {
		super.delete(purReport);
		purReportRSnMapper.delete(new PurReportRSn(purReport));
	}
	
	
}