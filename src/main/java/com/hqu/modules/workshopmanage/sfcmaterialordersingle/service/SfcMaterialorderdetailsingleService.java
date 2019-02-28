/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.sfcmaterialordersingle.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.workshopmanage.sfcmaterialordersingle.entity.SfcMaterialorderdetailsingle;
import com.hqu.modules.workshopmanage.sfcmaterialordersingle.mapper.SfcMaterialorderdetailsingleMapper;

/**
 * 原单行领料单Service
 * @author xhc
 * @version 2018-12-09
 */
@Service
@Transactional(readOnly = true)
public class SfcMaterialorderdetailsingleService extends CrudService<SfcMaterialorderdetailsingleMapper, SfcMaterialorderdetailsingle> {

	public SfcMaterialorderdetailsingle get(String id) {
		return super.get(id);
	}
	
	public List<SfcMaterialorderdetailsingle> findList(SfcMaterialorderdetailsingle sfcMaterialorderdetailsingle) {
		return super.findList(sfcMaterialorderdetailsingle);
	}
	
	public Page<SfcMaterialorderdetailsingle> findPage(Page<SfcMaterialorderdetailsingle> page, SfcMaterialorderdetailsingle sfcMaterialorderdetailsingle) {
		return super.findPage(page, sfcMaterialorderdetailsingle);
	}
	
	@Transactional(readOnly = false)
	public void save(SfcMaterialorderdetailsingle sfcMaterialorderdetailsingle) {
		super.save(sfcMaterialorderdetailsingle);
	}
	
	@Transactional(readOnly = false)
	public void delete(SfcMaterialorderdetailsingle sfcMaterialorderdetailsingle) {
		super.delete(sfcMaterialorderdetailsingle);
	}
	
}