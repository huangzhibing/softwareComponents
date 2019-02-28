/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contractmain.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.hqu.modules.purchasemanage.contractmain.mapper.ContractDetailWarningMapper;

/**
 * 采购订单汇总Service
 * @author ltq
 * @version 2018-04-24
 */
@Service
@Transactional(readOnly = true)
public class ContractMainWarningService extends CrudService<ContractDetailWarningMapper, ContractDetailWarning> {
	
	@Autowired
	private ContractDetailWarningMapper contractDetailWarningMapper;
	
		
	
	public List<ContractDetailWarning> findList(ContractDetailWarning contractDetailWarning) {
		return contractDetailWarningMapper.findList(contractDetailWarning);
	}	
	
	public Page<ContractDetailWarning> findPage(Page<ContractDetailWarning> page, ContractDetailWarning contractDetailWarning) {
		        //设置排序字段
		        contractDetailWarning.setOrderBy(page.getOrderBy());
		        //存储最终查找结果的数据
		        List<ContractDetailWarning> pageList= new ArrayList<ContractDetailWarning>();  
		        //检索栏的到货日期为不为空，表示检索的数据是已经到货的
//				if(contractDetailWarning.getArriveBeginDate()!=null||contractDetailWarning.getArriveEndDate()!=null){
//					//未全部到货
//					pageList=contractDetailWarningMapper.findListNotArriveAll(contractDetailWarning);
//				}else{
//					pageList=contractDetailWarningMapper.findList(contractDetailWarning);
//					List<ContractDetailWarning> pageList1=contractDetailWarningMapper.findListNotArriveAll(contractDetailWarning);
//					pageList.addAll(pageList1);
//				}
		         //订单中的物料全部没到货
		         pageList=contractDetailWarningMapper.findList(contractDetailWarning);
		         //订单中的物料部分没到货
				 List<ContractDetailWarning> pageList1=contractDetailWarningMapper.findListNotArriveAll(contractDetailWarning);
				 pageList.addAll(pageList1);
				   //分页
			       int pageNo= page.getPageNo();
			       int pageSize= page.getPageSize();
			       if(pageNo==1&&pageSize==-1){
			    	   page.setList(pageList);
			       }else{
			    	   List<ContractDetailWarning> reportL=  new ArrayList<ContractDetailWarning>();        
				       for(int i=(pageNo-1)*pageSize;i<pageList.size() && i<pageNo*pageSize;i++){
				    	   ContractDetailWarning conDetailWarning =pageList.get(i);
				    	   /*
				    	    * 过去三天及未来七天强调显示
				    	    */
				    	   
				    	   Date curDate = new Date();//获取系统当前时间
				    	   Date planArriveDate = conDetailWarning.getPlanArriveDate();//订单的计划交货日期
				    	   Calendar cal = Calendar.getInstance();
				    	   cal.setTime(curDate);//设置起时间
				    	   cal.add(Calendar.DATE, -3);//减少三天
				    	  
				    	   //判断该订单是否在当前时间的过去三天内
						   if(planArriveDate==null){
//							   conDetailWarning.setPastTD(false);
//						   	continue;
						   }else
				    	   if((cal.getTime()).before(planArriveDate)&&planArriveDate.before(curDate)){
				    		   conDetailWarning.setPastTD(true);
				    	   }else{

					    	   Calendar newCal = Calendar.getInstance();
					    	   newCal.setTime(curDate);//设置起时间
					    	   newCal.add(Calendar.DATE, 7);//加7天
					    	   //判断该订单是否在当前时间的将来七天内
					    	   if(curDate.before(planArriveDate)&&planArriveDate.before(newCal.getTime())){
					    		   conDetailWarning.setFutureSD(true);
					    	   }else{
					    		   //该订单非过去三天内，将来七天内到货，不需要在页面强调显示
					    		   conDetailWarning.setPastTD(false);
					    		   conDetailWarning.setFutureSD(false);
					    	   }
				    	   }

				           reportL.add(conDetailWarning);
					    } 
				        page.setList(reportL);
			       }
			       //设置列表的总个数
			       page.setCount(pageList.size());
			       return page;
	}
    /*
	public List<ContractDetailWarning> findListWarning(ContractDetailWarning contractDetailWarning) {
		//检索栏的到货日期为不为空，表示检索的数据是已经到货的
		if(contractDetailWarning.getArriveBeginDate()!=null||contractDetailWarning.getArriveEndDate()!=null){
			//未全部到货
			List<ContractDetailWarning> pageList2=contractDetailWarningMapper.findListNotArriveAll(contractDetailWarning);			
			return pageList2;
		}else{
			List<ContractDetailWarning> pageList1=contractDetailWarningMapper.findList(contractDetailWarning);
			List<ContractDetailWarning> pageList2=contractDetailWarningMapper.findListNotArriveAll(contractDetailWarning);
			
			pageList1.addAll(pageList2);			
	
			return pageList1;
		}		
	}
	*/
	
}