/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreport.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hqu.modules.qualitymanage.purreport.entity.PurReport;
import com.hqu.modules.qualitymanage.purreport.entity.PurReportRSn;
import com.hqu.modules.qualitymanage.purreport.mapper.PurReportAuditMapper;
import com.hqu.modules.qualitymanage.purreport.mapper.PurReportMapper;
import com.hqu.modules.qualitymanage.purreport.mapper.PurReportRSnAuditMapper;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 检验单审核(采购/装配/整机检测)Service
 * @author 张铮
 * @version 2018-04-03
 */
@Service
@Transactional(readOnly = true)
public class PurReportAuditService extends CrudService<PurReportAuditMapper, PurReport> {
	
	@Autowired
	private PurReportRSnAuditMapper purReportRSnMapper;
	@Autowired
	private PurReportMapper purReportMapper;
	public PurReport get(String id) {
		PurReport purReport = super.get(id);
		purReport.setPurReportRSnList(purReportRSnMapper.findList(new PurReportRSn(purReport)));
		return purReport;
	}
	
	public List<PurReport> findList(PurReport purReport) {
		return super.findList(purReport);
	}
	
	public Page<PurReport> findPage(Page<PurReport> page, PurReport purReport) {
		return findPageByJustifyPerson(page, purReport);
	}
	public Page<PurReport> findQueryPage(Page<PurReport> page, PurReport purReport) {
		dataRuleFilter(purReport);
		List<PurReport> list = mapper.findListByAuditQuery(purReport);
		Iterator<PurReport> it = list.iterator();
		List<PurReport> purlist = new ArrayList<PurReport>();
		while(it.hasNext()){
			PurReport purReportit = it.next();
				if(!("整机".equals(purReportit.getChecktName()))){
					if(!("编辑中".equals(purReportit.getState()))){
						purlist.add(purReportit);
				    }
				}
			}
		
		page.setList(purlist);
		page.setCount(purlist.size());
		return page;
	}
	
	public Page<PurReport> findPageByJustifyPerson(Page<PurReport> page, PurReport purReport){
		dataRuleFilter(purReport);
		purReport.setPage(page);
		page.setList(mapper.findListByJustifyPerson(purReport));
		page.setCount(mapper.findListByJustifyPerson(purReport).size());
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(PurReport purReport) {
		purReportMapper.updateforAudit(purReport);
		for (PurReportRSn purReportRSn : purReport.getPurReportRSnList()){
			if (purReportRSn.getId() == null){
				continue;
			}
			//if (PurReportRSn.DEL_FLAG_NORMAL.equals(purReportRSn.getDelFlag())){}
			purReportRSn.preUpdate();
			purReportRSnMapper.update(purReportRSn);
			
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(PurReport purReport) {
		super.delete(purReport);
		purReportRSnMapper.delete(new PurReportRSn(purReport));
	}
	
}