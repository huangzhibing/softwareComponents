/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.applymain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.purchasemanage.applymain.entity.ApplyMain;
import com.hqu.modules.purchasemanage.applymainaudit.entity.ApplyMainAudit;
import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;

/**
 * 采购需求MAPPER接口
 * @author syc
 * @version 2018-04-21
 */
@MyBatisMapper
public interface ApplyMainMapper extends BaseMapper<ApplyMain> {
	public List<InvAccount> getInvAccountByItemCode(@Param("itemCode")String itemCode);
	
	public List<Item> findMyList(Item item);
	
	public Item getItemByCode(String code);
	
	public List<ApplyMain> getApplyMainByBillNum(String billNum);
	
	public void myInsert(ApplyMain applyMain);
	
	public void myUpdate(ApplyMain applyMain);
	
	public List<Office> findOfficeList(Office office);
	
	public List<User> findUserList(User user);
	
	public List<Item> findItemList(Item item);
	
	public ApplyMain getByProInId(String pid);
	
	
}