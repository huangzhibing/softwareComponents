/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.invaccountcode.service;

import java.util.List;
import java.util.Map;

import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.mapper.ItemMapper;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetailCode;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.bin.entity.Bin;
import com.hqu.modules.inventorymanage.bin.mapper.BinMapper;
import com.hqu.modules.inventorymanage.location.entity.Location;
import com.hqu.modules.inventorymanage.location.mapper.LocationMapper;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.hqu.modules.inventorymanage.warehouse.mapper.WareHouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.inventorymanage.invaccountcode.entity.InvAccountCode;
import com.hqu.modules.inventorymanage.invaccountcode.mapper.InvAccountCodeMapper;

/**
 * 库存帐扫码Service
 * @author M1ngz
 * @version 2018-06-03
 */
@Service
@Transactional(readOnly = true)
public class InvAccountCodeService extends CrudService<InvAccountCodeMapper, InvAccountCode> {
	@Autowired
	WareHouseMapper wareHouseMapper;
	@Autowired
	ItemMapper itemMapper;
	@Autowired
	BinMapper binMapper;
	@Autowired
	LocationMapper locationMapper;

	public InvAccountCode get(String id) {
		return super.get(id);
	}
	
	public List<InvAccountCode> findList(InvAccountCode invAccountCode) {
		return super.findList(invAccountCode);
	}
	
	public Page<InvAccountCode> findPage(Page<InvAccountCode> page, InvAccountCode invAccountCode) {
		return super.findPage(page, invAccountCode);
	}

	@Transactional(readOnly = false)
	public void saveProduct(BillMain billMain){
		for(BillDetailCode billDetailCode:billMain.getBillDetailCodeList()){
			billDetailCode.preInsert();
			mapper.insertByProduct(billDetailCode);
		}
	}
	
	@Transactional(readOnly = false)
	public void save(InvAccountCode invAccountCode) {
		if(invAccountCode.getWare().getWareID().length() == 32){
			WareHouse wareHouse = wareHouseMapper.get(invAccountCode.getWare().getWareID());
			invAccountCode.setWare(wareHouse);
		}
		if(invAccountCode.getBin() != null && invAccountCode.getBin().getBinId().length() == 32){
			Bin bin = binMapper.get(invAccountCode.getBin().getBinId());
			invAccountCode.setBin(bin);
		}
		if(invAccountCode.getLoc() != null && invAccountCode.getLoc().getLocId().length() == 32){
			Location location = locationMapper.get(invAccountCode.getLoc().getLocId());
			invAccountCode.setLoc(location);
		}
		if(invAccountCode.getItem().getCode().length() == 32){
			Item item = itemMapper.get(invAccountCode.getItem().getCode());
			invAccountCode.setItem(item);
		}
		super.save(invAccountCode);
	}
	
	@Transactional(readOnly = false)
	public void delete(InvAccountCode invAccountCode) {
		super.delete(invAccountCode);
	}


//	public Map<String,Object> post()










}