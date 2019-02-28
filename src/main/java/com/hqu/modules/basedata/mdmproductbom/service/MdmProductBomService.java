/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.mdmproductbom.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.basedata.mdmproductbom.entity.MdmProductBom;
import com.hqu.modules.basedata.mdmproductbom.mapper.MdmProductBomMapper;

/**
 * 产品结构维护Service
 * @author liujiachen
 * @version 2018-04-03
 */
@Service
@Transactional(readOnly = true)
public class MdmProductBomService extends CrudService<MdmProductBomMapper, MdmProductBom> {

	public MdmProductBom get(String id) {
		return super.get(id);
	}
	
	public List<MdmProductBom> findList(MdmProductBom mdmProductBom) {
		return super.findList(mdmProductBom);
	}
	
	public Page<MdmProductBom> findPage(Page<MdmProductBom> page, MdmProductBom mdmProductBom) {
		return super.findPage(page, mdmProductBom);
	}
	
	@Transactional(readOnly = false)
	public void save(MdmProductBom mdmProductBom) {
		super.save(mdmProductBom);
	}
	
	@Transactional(readOnly = false)
	public void delete(MdmProductBom mdmProductBom) {
		super.delete(mdmProductBom);
	}
	
}