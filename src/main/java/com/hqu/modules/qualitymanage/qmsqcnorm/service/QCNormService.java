/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmsqcnorm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.qualitymanage.qmsqcnorm.entity.QCNorm;
import com.hqu.modules.qualitymanage.qmsqcnorm.mapper.QCNormMapper;
import com.hqu.modules.qualitymanage.qmsqcnorm.entity.QCNormItem;
import com.hqu.modules.qualitymanage.qmsqcnorm.mapper.QCNormItemMapper;

/**
 * 检验标准定义Service
 * @author luyumiao
 * @version 2018-05-05
 */
@Service
@Transactional(readOnly = true)
public class QCNormService extends CrudService<QCNormMapper, QCNorm> {

	@Autowired
	private QCNormItemMapper qCNormItemMapper;
	
	public QCNorm get(String id) {
		QCNorm qCNorm = super.get(id);
		qCNorm.setQCNormItemList(qCNormItemMapper.findList(new QCNormItem(qCNorm)));
		return qCNorm;
	}
	
	public List<QCNorm> findList(QCNorm qCNorm) {
		return super.findList(qCNorm);
	}
	
	public Page<QCNorm> findPage(Page<QCNorm> page, QCNorm qCNorm) {
		return super.findPage(page, qCNorm);
	}
	
	@Transactional(readOnly = false)
	public void save(QCNorm qCNorm) {
		super.save(qCNorm);
		for (QCNormItem qCNormItem : qCNorm.getQCNormItemList()){
			if (qCNormItem.getId() == null){
				continue;
			}
			if (QCNormItem.DEL_FLAG_NORMAL.equals(qCNormItem.getDelFlag())){
				if (StringUtils.isBlank(qCNormItem.getId())){
					qCNormItem.setQCNorm(qCNorm);
					qCNormItem.preInsert();
					qCNormItemMapper.insert(qCNormItem);
				}else{
					qCNormItem.preUpdate();
					qCNormItemMapper.update(qCNormItem);
				}
			}else{
				qCNormItemMapper.delete(qCNormItem);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(QCNorm qCNorm) {
		super.delete(qCNorm);
		qCNormItemMapper.delete(new QCNormItem(qCNorm));
	}
	
}