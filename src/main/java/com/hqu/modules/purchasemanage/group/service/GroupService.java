/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.group.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.purchasemanage.group.entity.Group;
import com.hqu.modules.purchasemanage.group.mapper.GroupMapper;
import com.hqu.modules.purchasemanage.group.entity.GroupBuyer;
import com.hqu.modules.purchasemanage.group.mapper.GroupBuyerMapper;
import com.hqu.modules.purchasemanage.group.entity.GroupItemClass;
import com.hqu.modules.purchasemanage.group.mapper.GroupItemClassMapper;
import com.hqu.modules.purchasemanage.group.entity.GroupOrgz;
import com.hqu.modules.purchasemanage.group.mapper.GroupOrgzMapper;
import com.hqu.modules.purchasemanage.group.entity.GroupSup;
import com.hqu.modules.purchasemanage.group.mapper.GroupSupMapper;
import com.hqu.modules.purchasemanage.group.entity.GroupWare;
import com.hqu.modules.purchasemanage.group.mapper.GroupWareMapper;

/**
 * GroupService
 * @author 方翠虹,石豪迈,陶瑶
 * @version 2018-04-18
 */
@Service
@Transactional(readOnly = true)
public class GroupService extends CrudService<GroupMapper, Group> {
	@Autowired
	GroupMapper groupMapper;

	@Autowired
	private GroupBuyerMapper groupBuyerMapper;
	@Autowired
	private GroupItemClassMapper groupItemClassMapper;
	@Autowired
	private GroupOrgzMapper groupOrgzMapper;
	@Autowired
	private GroupSupMapper groupSupMapper;
	@Autowired
	private GroupWareMapper groupWareMapper;
	
	public Group get(String id) {
		Group group = super.get(id);
		group.setGroupBuyerList(groupBuyerMapper.findList(new GroupBuyer(group)));
		group.setGroupItemClassList(groupItemClassMapper.findList(new GroupItemClass(group)));
		group.setGroupOrgzList(groupOrgzMapper.findList(new GroupOrgz(group)));
		group.setGroupSupList(groupSupMapper.findList(new GroupSup(group)));
		group.setGroupWareList(groupWareMapper.findList(new GroupWare(group)));
		return group;
	}
	
	public List<Group> findList(Group group) {
		return super.findList(group);
	}
	
	public Page<Group> findPage(Page<Group> page, Group group) {
		return super.findPage(page, group);
	}
	
	@Transactional(readOnly = false)
	public void save(Group group) {
		super.save(group);
		for (GroupBuyer groupBuyer : group.getGroupBuyerList()){
			if (groupBuyer.getId() == null){
				continue;
			}
			if (GroupBuyer.DEL_FLAG_NORMAL.equals(groupBuyer.getDelFlag())){
				if (StringUtils.isBlank(groupBuyer.getId())){
					groupBuyer.setGroup(group);
					groupBuyer.preInsert();
					groupBuyerMapper.insert(groupBuyer);
				}else{
					groupBuyer.preUpdate();
					groupBuyerMapper.update(groupBuyer);
				}
			}else{
				groupBuyerMapper.delete(groupBuyer);
			}
		}
		for (GroupItemClass groupItemClass : group.getGroupItemClassList()){
			if (groupItemClass.getId() == null){
				continue;
			}
			if (GroupItemClass.DEL_FLAG_NORMAL.equals(groupItemClass.getDelFlag())){
				if (StringUtils.isBlank(groupItemClass.getId())){
					groupItemClass.setGroup(group);
					groupItemClass.preInsert();
					groupItemClassMapper.insert(groupItemClass);
				}else{
					groupItemClass.preUpdate();
					groupItemClassMapper.update(groupItemClass);
				}
			}else{
				groupItemClassMapper.delete(groupItemClass);
			}
		}
		for (GroupOrgz groupOrgz : group.getGroupOrgzList()){
			if (groupOrgz.getId() == null){
				continue;
			}
			if (GroupOrgz.DEL_FLAG_NORMAL.equals(groupOrgz.getDelFlag())){
				if (StringUtils.isBlank(groupOrgz.getId())){
					groupOrgz.setGroup(group);
					groupOrgz.preInsert();
					groupOrgzMapper.insert(groupOrgz);
				}else{
					groupOrgz.preUpdate();
					groupOrgzMapper.update(groupOrgz);
				}
			}else{
				groupOrgzMapper.delete(groupOrgz);
			}
		}
		for (GroupSup groupSup : group.getGroupSupList()){
			if (groupSup.getId() == null){
				continue;
			}
			if (GroupSup.DEL_FLAG_NORMAL.equals(groupSup.getDelFlag())){
				if (StringUtils.isBlank(groupSup.getId())){
					groupSup.setGroup(group);
					groupSup.preInsert();
					groupSupMapper.insert(groupSup);
				}else{
					groupSup.preUpdate();
					groupSupMapper.update(groupSup);
				}
			}else{
				groupSupMapper.delete(groupSup);
			}
		}
		for (GroupWare groupWare : group.getGroupWareList()){
			if (groupWare.getId() == null){
				continue;
			}
			if (GroupWare.DEL_FLAG_NORMAL.equals(groupWare.getDelFlag())){
				if (StringUtils.isBlank(groupWare.getId())){
					groupWare.setGroup(group);
					groupWare.preInsert();
					groupWareMapper.insert(groupWare);
				}else{
					groupWare.preUpdate();
					groupWareMapper.update(groupWare);
				}
			}else{
				groupWareMapper.delete(groupWare);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(Group group) {
		super.delete(group);
		groupBuyerMapper.delete(new GroupBuyer(group));
		groupItemClassMapper.delete(new GroupItemClass(group));
		groupOrgzMapper.delete(new GroupOrgz(group));
		groupSupMapper.delete(new GroupSup(group));
		groupWareMapper.delete(new GroupWare(group));
	}
	public String getMaxGroupid(){
		return groupMapper.getMaxGroupid();
	}
	
	
}