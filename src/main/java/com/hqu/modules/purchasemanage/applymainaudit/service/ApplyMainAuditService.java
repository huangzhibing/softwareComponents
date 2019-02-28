/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.applymainaudit.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.mapper.ItemMapper;
import com.hqu.modules.purchasemanage.adtdetail.entity.AdtDetail;
import com.hqu.modules.purchasemanage.adtdetail.service.AdtDetailService;
import com.hqu.modules.purchasemanage.applymain.entity.ApplyDetail;
import com.hqu.modules.purchasemanage.applymain.entity.ApplyMain;
import com.hqu.modules.purchasemanage.applymainaudit.entity.ApplyDetailAudit;
import com.hqu.modules.purchasemanage.applymainaudit.entity.ApplyMainAudit;
import com.hqu.modules.purchasemanage.applymainaudit.mapper.ApplyDetailAuditMapper;
import com.hqu.modules.purchasemanage.applymainaudit.mapper.ApplyMainAuditMapper;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 采购需求审批Service
 * @author syc
 * @version 2018-05-08
 */
@Service
@Transactional(readOnly = true)
public class ApplyMainAuditService extends CrudService<ApplyMainAuditMapper, ApplyMainAudit> {

	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ApplyDetailAuditMapper applyDetailAuditMapper;
	
	@Autowired
	private AdtDetailService adtDetailService;
	
	@Autowired
	private ApplyMainAuditMapper applyMainAuditMapper;
	
	public ApplyMainAudit get(String id) {
		ApplyMainAudit applyMainAudit = super.get(id);
		applyMainAudit.setApplyDetailAuditList(applyDetailAuditMapper.findList(new ApplyDetailAudit(applyMainAudit)));
		return applyMainAudit;
	}
	
	public List<ApplyMainAudit> findList(ApplyMainAudit applyMainAudit) {
		return super.findList(applyMainAudit);
		
	}
	
	
	
	public Page<ApplyMainAudit> findPage(Page<ApplyMainAudit> page, ApplyMainAudit applyMainAudit) {
		dataRuleFilter(applyMainAudit);
		applyMainAudit.setPage(page);
		//根据筛选条件（adt）进行筛选，包括自动查询功能
		List<ApplyMainAudit> findList = filterMethod(applyMainAudit);
		//将状态转换成文字
		List<ApplyMainAudit> resultList = new ArrayList<ApplyMainAudit>();
		Iterator<ApplyMainAudit> it = findList.iterator();
		while(it.hasNext()){
			ApplyMainAudit applyMainAuditTemp = it.next();
			String billStateFlag = applyMainAuditTemp.getBillStateFlag();
			if("A".equals(billStateFlag)){
				applyMainAuditTemp.setBillStateFlag("正在修改");
			}else{
				if("W".equals(billStateFlag)){
					applyMainAuditTemp.setBillStateFlag("录入完毕");
				}
				if("E".equals(billStateFlag)){
					applyMainAuditTemp.setBillStateFlag("审批通过");
				}
				if("B".equals(billStateFlag)){
					applyMainAuditTemp.setBillStateFlag("审批未通过");
				}
				if("V".equals(billStateFlag)){
					applyMainAuditTemp.setBillStateFlag("作废单据");
				}
				resultList.add(applyMainAuditTemp);
			}
			
		}
		page.setList(resultList);
		page.setCount(resultList.size());
		return page;
	}

	private List<ApplyMainAudit> filterMethod(ApplyMainAudit applyMainAudit) {
		
		List<ApplyMainAudit> resultList = new ArrayList<ApplyMainAudit>();
		//获得当前人所属角色列表
		List<Role> roleList = UserUtils.getUser().getRoleList();
		Iterator<Role> itRule = roleList.iterator();
		while(itRule.hasNext()){
			AdtDetail adtDetail = new AdtDetail();
			Role role = itRule.next();
			String roleId = role.getId();
			//查询审核表adtdetail
			//设置采购需求标号
			adtDetail.setBillTypeCode("APP001");
			//设置未结束查询条件
			adtDetail.setIsFinished("0");
			//设置为当前人
			
			adtDetail.setJpositionCode(roleId);
//			adtDetail.setJpersonCode(roleId);
			List<AdtDetail> adtDetailList = adtDetailService.findList(adtDetail);
			//遍历adtDetailList
			Iterator<AdtDetail> it = adtDetailList.iterator();
			//此处判断查询
			String billNum = applyMainAudit.getBillNum();
			if(billNum.equals("")){
				while(it.hasNext()){
					AdtDetail adtDetailTemp = it.next();
					String adtBillNum = adtDetailTemp.getBillNum();
					if(adtBillNum==null){continue;}
					applyMainAudit.setBillNum(adtBillNum);
					List<ApplyMainAudit> tempList = findSonList(applyMainAudit);
					resultList.addAll(tempList);
					applyMainAudit.setBillNum("");
				}
			}else{
				//不是空的，与if中取交集
			//	return super.findList(applyMainAudit);
				//先查，得到模糊的结果
				List<ApplyMainAudit> tempList = findSonList(applyMainAudit);
				//再去遍历，进行条件筛选
				while(it.hasNext()){
					AdtDetail adtDetailTemp = it.next();
					String adtBillNum = adtDetailTemp.getBillNum();
					if(adtBillNum==null){continue;}
					//遍历之前模糊的查询
					Iterator<ApplyMainAudit> it1 = tempList.iterator();
					while(it1.hasNext()){
						ApplyMainAudit next = it1.next();
						if(adtBillNum.equals(next.getBillNum())){
							resultList.add(next);
							//applyMainAudit.setBillNum("");
						}
					}
					
				}
			}
		}
		return resultList;
	}
	
	public List<ApplyMainAudit> findSonList(ApplyMainAudit entity){
		//item 中存放子表的筛选条件
		List<ApplyMainAudit> resultList = new ArrayList<ApplyMainAudit>();
			Item item = entity.getItem();
			Item item1 = itemMapper.get(item.getId());
			if(item1==null){
				item1 = new Item();
			}
			item.setCode(item1.getCode());
			item.setName(entity.getItemName());
			item.setSpecModel(entity.getItemSpecmodel());
			String billStateFlag = entity.getBillStateFlag();
			//遍历主表
			List<ApplyMainAudit> findList = applyMainAuditMapper.findList(entity);
			Iterator<ApplyMainAudit> it = findList.iterator();
			while(it.hasNext()){
				ApplyMainAudit applyMainAudit = it.next();
				//根据筛选条件获取主表下的子表
				ApplyDetailAudit applyDetailAudit = new ApplyDetailAudit(applyMainAudit);
				applyDetailAudit.setItem(item);
				applyDetailAudit.setItemName(item.getName());
				applyDetailAudit.setItemSpecmodel(item.getSpecModel());
				List<ApplyDetailAudit> ApplyDetailAuditList = applyDetailAuditMapper.findList(applyDetailAudit);
				//如果子表bu为空或长度bu为零
				if(ApplyDetailAuditList!=null&&ApplyDetailAuditList.size()!=0){
					//将A改成正在录入
					if(applyMainAudit.getBillStateFlag().equals("A")){
						applyMainAudit.setBillStateFlag("正在录入");
					}
					//将W改成正在录入
					if(applyMainAudit.getBillStateFlag().equals("W")){
						applyMainAudit.setBillStateFlag("录入完毕");
						resultList.add(applyMainAudit);
					}
					
					//将B改成正在录入
					if(applyMainAudit.getBillStateFlag().equals("B")){
						applyMainAudit.setBillStateFlag("审批未通过");
						resultList.add(applyMainAudit);
					}
					
					
				}
			}
			return resultList;
		
		//return null;
	}

	
	@Transactional(readOnly = false)
	public void save(ApplyMainAudit applyMainAudit) {
		super.save(applyMainAudit);
		for (ApplyDetailAudit applyDetailAudit : applyMainAudit.getApplyDetailAuditList()){
			if (applyDetailAudit.getId() == null){
				continue;
			}
			if (ApplyDetailAudit.DEL_FLAG_NORMAL.equals(applyDetailAudit.getDelFlag())){
				if (StringUtils.isBlank(applyDetailAudit.getId())){
					applyDetailAudit.setApplyMainQuery(applyMainAudit);
					applyDetailAudit.preInsert();
					applyDetailAuditMapper.insert(applyDetailAudit);
				}else{
					applyDetailAudit.preUpdate();
					applyDetailAuditMapper.update(applyDetailAudit);
				}
			}else{
				applyDetailAuditMapper.delete(applyDetailAudit);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(ApplyMainAudit applyMainAudit) {
		super.delete(applyMainAudit);
		applyDetailAuditMapper.delete(new ApplyDetailAudit(applyMainAudit));
	}
	
	@Transactional(readOnly = false)
	public ApplyMainAudit getByProInId(String pid){
		return applyMainAuditMapper.getByProInId(pid);
	}
	
	public ApplyMainAudit setHist(ApplyMainAudit applyMainAudit){
		//获取年月日
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd"); 
		String c=sdf.format(new Date()); 
		int date = Integer.parseInt(c);
		// get user name
		String name = UserUtils.getUser().getName();
		List<ApplyDetailAudit> ApplyDetailAuditListT = new ArrayList<ApplyDetailAudit>();
		List<ApplyDetailAudit> applyDetailAuditList = applyMainAudit.getApplyDetailAuditList();
		Iterator<ApplyDetailAudit> it = applyDetailAuditList.iterator();
		while(it.hasNext()){
			ApplyDetailAudit applyDetailAudit = it.next();
			String changeNum = applyDetailAudit.getApplyQty()+"";
			if(changeNum!=null&&!changeNum.equals("")){ 
				String log = applyDetailAudit.getLog();
				String addStr = name+"-"+date+"-"+changeNum;
				log = log+addStr+"; ";
				applyDetailAudit.setLog(log);
			}
			ApplyDetailAuditListT.add(applyDetailAudit);
		}
		applyMainAudit.setApplyDetailAuditList(ApplyDetailAuditListT);
		return applyMainAudit;
	}
	
	
}