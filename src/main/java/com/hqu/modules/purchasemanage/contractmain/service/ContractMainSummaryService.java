/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contractmain.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractDetailSummary;

import com.hqu.modules.purchasemanage.contractmain.mapper.ContractDetailSummaryMapper;

/**
 * 采购订单汇总Service
 * @author ltq
 * @version 2018-04-24
 */
@Service
@Transactional(readOnly = true)
public class ContractMainSummaryService extends CrudService<ContractDetailSummaryMapper, ContractDetailSummary> {
	
	@Autowired
	private ContractDetailSummaryMapper contractDetailSummaryMapper;
	
		
	
	public List<ContractDetailSummary> findList(ContractDetailSummary contractDetailSummary) {
		return contractDetailSummaryMapper.findList(contractDetailSummary);
	}	
	
	public Page<ContractDetailSummary> findPage(Page<ContractDetailSummary> page, ContractDetailSummary contractDetailSummary) {
		//设置排序字段
		if("madeDate desc".equals(page.getOrderBy())){
			contractDetailSummary.setOrderBy("itemBeginDate desc");
		}else if("madeDate asc".equals(page.getOrderBy())){
			contractDetailSummary.setOrderBy("itemBeginDate asc");
		}
		else{
			contractDetailSummary.setOrderBy(page.getOrderBy());
		}
		List<ContractDetailSummary> pageList=contractDetailSummaryMapper.findList(contractDetailSummary);
		
		   //分页
	       int pageNo= page.getPageNo();
	       int pageSize= page.getPageSize();
	       if(pageNo==1&&pageSize==-1){
	    	   page.setList(pageList);
	       }else{
	    	   List<ContractDetailSummary> reportL=  new ArrayList<ContractDetailSummary>();        
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