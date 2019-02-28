/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.group.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.entity.User;
import com.hqu.modules.purchasemanage.group.entity.Group;
import com.hqu.modules.purchasemanage.group.entity.GroupBuyer;
import com.hqu.modules.purchasemanage.group.mapper.GroupBuyerQueryMapper;
import com.hqu.modules.purchasemanage.group.mapper.GroupMapper;
import com.jeeplus.common.utils.StringUtils;

/**
 * GroupService
 * @author 杨贤邦
 * @version 2018-04-23
 */
@Service
@Transactional(readOnly = true)
public class GroupQueryService extends CrudService<GroupMapper, Group> {
	@Autowired
	GroupMapper groupMapper;

	@Autowired
	private GroupBuyerQueryMapper groupBuyerMapper;
/*	@Autowired
	private GroupItemClassQueryMapper groupItemClassMapper;
	@Autowired
	private GroupOrgzQueryMapper groupOrgzMapper;
	@Autowired
	private GroupSupQueryMapper groupSupMapper;
	@Autowired
	private GroupWareQueryMapper groupWareMapper;*/
	
	
	public List<GroupBuyer> getBuyer(User user){
		GroupBuyer gb=new GroupBuyer();
		gb.setUser(user);
		return groupBuyerMapper.findList(gb);
	}
	

	/**
	 * 查询对应User所在采购组中的所有采购员
	 * @param user
	 * @param page
	 * @param group
	 * @return
	 */
	public Page<GroupBuyer> findBuyersPage(User user,Page<GroupBuyer> page, GroupBuyer groupbuyer) {
		GroupBuyer gb=new GroupBuyer();
		gb.setUser(user);
		List<GroupBuyer> buyers=groupBuyerMapper.findList(gb);
		List<String> buyeridSet=new ArrayList<String>();  //利用采购组ID去重
		List<String> groupidSet=new ArrayList<String>();  //利用采购员用户ID去重
		List<Group> groupList=new ArrayList<>();
		List<GroupBuyer> buyerList = new ArrayList<>();
		for(int i=0;i<buyers.size();i++) {
			if(!groupidSet.contains(buyers.get(i).getGroup().getGroupid())){
				groupidSet.add(buyers.get(i).getGroup().getGroupid());
				groupList.add(buyers.get(i).getGroup());
			}
		}
		for(Group g:groupList) {
			List<GroupBuyer> groupBuyers=groupBuyerMapper.findList(new GroupBuyer(g));
			for(GroupBuyer buyer:groupBuyers) {
				if(!buyeridSet.contains(buyer.getUser().getId())) {
					buyeridSet.add(buyer.getUser().getId());
					buyerList.add(buyer);
				}
			}
		}
		
		//对结果进行模糊查询
		if(groupbuyer!=null&&groupbuyer.getBuyername()!=null&&!groupbuyer.getBuyername().equals("")) {
			Iterator<GroupBuyer> it=buyerList.iterator();
			while(it.hasNext()) {
				if(!it.next().getBuyername().contains(groupbuyer.getBuyername())) {
					it.remove();
				}
			}
		}
		if(groupbuyer.getUser()!=null&&groupbuyer.getUser().getNo()!=null&&!groupbuyer.getUser().getNo().equals("")) {
			Iterator<GroupBuyer> it=buyerList.iterator();
			while(it.hasNext()) {
				if(!it.next().getUser().getNo().contains(groupbuyer.getUser().getNo())) {
					it.remove();
				}
			}
		}
	
		page.setCount(buyerList.size());
		page.setList(buyerList);
		return page;
	}
	
	
	
	
	
}