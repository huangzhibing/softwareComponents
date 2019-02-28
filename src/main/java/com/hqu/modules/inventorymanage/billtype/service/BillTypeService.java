/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.billtype.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.inventorymanage.billtype.entity.BillType;
import com.hqu.modules.inventorymanage.billtype.mapper.BillTypeMapper;
import com.hqu.modules.inventorymanage.billtype.entity.BillTypeWareHouse;
import com.hqu.modules.inventorymanage.billtype.mapper.BillTypeWareHouseMapper;

/**
 * 单据类型定义Service
 * @author dongqida
 * @version 2018-04-14
 */
@Service
@Transactional(readOnly = true)
public class BillTypeService extends CrudService<BillTypeMapper, BillType> {

	@Autowired
	private BillTypeWareHouseMapper billTypeWareHouseMapper;
	
	public BillType get(String id) {
		BillType billType = super.get(id);
		billType.setBillTypeWareHouseList(billTypeWareHouseMapper.findList(new BillTypeWareHouse(billType)));
		return billType;
	}
	
	public List<BillType> findList(BillType billType) {
		return super.findList(billType);
	}
	
	public Page<BillType> findPage(Page<BillType> page, BillType billType) {
		return super.findPage(page, billType);
	}
	
	@Transactional(readOnly = false)
	public void save(BillType billType) {
		super.save(billType);
		for (BillTypeWareHouse billTypeWareHouse : billType.getBillTypeWareHouseList()){
			if (billTypeWareHouse.getId() == null){
				continue;
			}
			if (BillTypeWareHouse.DEL_FLAG_NORMAL.equals(billTypeWareHouse.getDelFlag())){
				if (StringUtils.isBlank(billTypeWareHouse.getId())){
					billTypeWareHouse.setBillType(billType);
					billTypeWareHouse.preInsert();
					billTypeWareHouseMapper.insert(billTypeWareHouse);
				}else{
					billTypeWareHouse.preUpdate();
					billTypeWareHouseMapper.update(billTypeWareHouse);
				}
			}else{
				billTypeWareHouseMapper.delete(billTypeWareHouse);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(BillType billType) {
		super.delete(billType);
		billTypeWareHouseMapper.delete(new BillTypeWareHouse(billType));
	}
	
}