/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contractmain.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.act.service.ActTaskService;
import com.jeeplus.modules.act.utils.ActUtils;
import com.jeeplus.modules.sys.entity.DictValue;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.common.utils.Collections3;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractMain;
import com.hqu.modules.purchasemanage.contractmain.mapper.ContractMainMapper;
import com.hqu.modules.purchasemanage.contracttype.entity.ContractType;
import com.hqu.modules.purchasemanage.contracttype.mapper.ContractTypeMapper;
import com.hqu.modules.purchasemanage.group.entity.GroupBuyer;
import com.hqu.modules.purchasemanage.group.mapper.GroupBuyerMapper;
import com.hqu.modules.purchasemanage.linkman.entity.LinkMan;
import com.hqu.modules.purchasemanage.linkman.entity.Paccount;
import com.hqu.modules.purchasemanage.linkman.mapper.LinkManMapper;
import com.hqu.modules.purchasemanage.paymode.entity.PayMode;
import com.hqu.modules.purchasemanage.paymode.mapper.PayModeMapper;
import com.hqu.modules.purchasemanage.supprice.entity.PurSupPrice;
import com.hqu.modules.purchasemanage.supprice.entity.PurSupPriceDetail;
import com.hqu.modules.purchasemanage.supprice.mapper.PurSupPriceDetailMapper;
import com.hqu.modules.purchasemanage.transtype.entity.TranStype;
import com.hqu.modules.purchasemanage.transtype.mapper.TranStypeMapper;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.ItemforInv;
import com.hqu.modules.qualitymanage.purinvcheckmain.mapper.ItemforInvMapper;
import com.google.common.collect.Maps;
import com.hqu.modules.basedata.account.entity.Account;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractDetail;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractDetailSummary;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractDetailView;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractDetailWarning;
import com.hqu.modules.purchasemanage.contractmain.mapper.ContractDetailMapper;
import com.hqu.modules.purchasemanage.contractmain.mapper.ContractDetailSummaryMapper;
import com.hqu.modules.purchasemanage.contractmain.mapper.ContractDetailViewMapper;
import com.hqu.modules.purchasemanage.contractmain.mapper.ContractDetailWarningMapper;

/**
 * 采购订单汇总Service
 * @author ltq
 * @version 2018-04-24
 */
@Service
@Transactional(readOnly = true)
public class ContractMainViewService extends CrudService<ContractDetailViewMapper, ContractDetailView> {
	
	@Autowired
	private ContractDetailViewMapper contractDetailViewMapper;
	
		
	
	public List<ContractDetailView> findList(ContractDetailView contractDetailView) {
		return contractDetailViewMapper.findList(contractDetailView);
	}	
	
	public Page<ContractDetailView> findPage(Page<ContractDetailView> page, ContractDetailView contractDetailView) {
		 //存储最终查找结果的数据
        List<ContractDetailView> pageList= new ArrayList<ContractDetailView>();  
		//设置排序字段
		contractDetailView.setOrderBy(page.getOrderBy());
		//查询未到货的订单物料
		pageList=contractDetailViewMapper.findList(contractDetailView);
		//找出已到货的订单的物料
		List<ContractDetailView> pageList1=contractDetailViewMapper.findListNotArriveAll(contractDetailView);
		pageList.addAll(pageList1);
   	      //分页
	       int pageNo= page.getPageNo();
	       int pageSize= page.getPageSize();
	       if(pageNo==1&&pageSize==-1){
	    	   page.setList(pageList);
	       }else{
	    	   List<ContractDetailView> reportL=  new ArrayList<ContractDetailView>();        
		       for(int i=(pageNo-1)*pageSize;i<pageList.size() && i<pageNo*pageSize;i++){
		        	reportL.add(pageList.get(i));
			    } 
		        page.setList(reportL);
	       }
	       //设置列表的总个数
	       page.setCount(pageList.size());
		return page;
	}
	
}