/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmreportquery.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.qualitymanage.qmreportquery.entity.QmReportQuery;
import com.hqu.modules.qualitymanage.qmreportquery.mapper.QmReportQueryMapper;
import com.hqu.modules.qualitymanage.qmreportquery.entity.QmReportRSnQuery;
import com.hqu.modules.qualitymanage.qmreportquery.mapper.QmReportRSnQueryMapper;

/**
 * 查询质量问题报告Service
 * @author yangxianbang
 * @version 2018-04-17
 */
@Service
@Transactional(readOnly = true)
public class QmReportQueryService extends CrudService<QmReportQueryMapper, QmReportQuery> {

	@Autowired
	private QmReportRSnQueryMapper qmReportRSnQueryMapper;
	
	public QmReportQuery get(String id) {
		QmReportQuery qmReportQuery = super.get(id);
		qmReportQuery.setQmReportRSnQueryList(qmReportRSnQueryMapper.findList(new QmReportRSnQuery(qmReportQuery)));
		return qmReportQuery;
	}
	
	public List<QmReportQuery> findList(QmReportQuery qmReportQuery) {
		return super.findList(qmReportQuery);
	}
	/**
	 * 
	 * @param page
	 * @param qmReportQuery
	 * @param office
	 * @return
	 */
	public Page<QmReportQuery> findPageByOffice(Page<QmReportQuery> page, QmReportQuery qmReportQuery,Office office){
		
		return page;
	}
	
	public Page<QmReportQuery> findPage(Page<QmReportQuery> page, QmReportQuery qmReportQuery) {

		dataRuleFilter(qmReportQuery);
		qmReportQuery.setPage(page);
		page.setList(mapper.findListWithDetail(qmReportQuery));

		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(QmReportQuery qmReportQuery) {
		super.save(qmReportQuery);
		for (QmReportRSnQuery qmReportRSnQuery : qmReportQuery.getQmReportRSnQueryList()){
			if (qmReportRSnQuery.getId() == null){
				continue;
			}
			if (QmReportRSnQuery.DEL_FLAG_NORMAL.equals(qmReportRSnQuery.getDelFlag())){
				if (StringUtils.isBlank(qmReportRSnQuery.getId())){
					qmReportRSnQuery.setQmReportId(qmReportQuery);
					qmReportRSnQuery.preInsert();
					qmReportRSnQueryMapper.insert(qmReportRSnQuery);
				}else{
					qmReportRSnQuery.preUpdate();
					qmReportRSnQueryMapper.update(qmReportRSnQuery);
				}
			}else{
				qmReportRSnQueryMapper.delete(qmReportRSnQuery);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(QmReportQuery qmReportQuery) {
		super.delete(qmReportQuery);
		qmReportRSnQueryMapper.delete(new QmReportRSnQuery(qmReportQuery));
	}
	
}