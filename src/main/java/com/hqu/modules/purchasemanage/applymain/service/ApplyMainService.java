/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.applymain.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.mapper.ItemMapper;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.mapper.InvAccountMapper;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
import com.hqu.modules.purchasemanage.adtbilltype.entity.AdtBillType;
import com.hqu.modules.purchasemanage.adtdetail.service.AdtDetailService;
import com.hqu.modules.purchasemanage.adtmodel.entity.AdtModel;
import com.hqu.modules.purchasemanage.adtmodel.service.AdtModelService;
import com.hqu.modules.purchasemanage.applymain.entity.ApplyDetail;
import com.hqu.modules.purchasemanage.applymain.entity.ApplyMain;
import com.hqu.modules.purchasemanage.applymain.entity.ItemCustmer;
import com.hqu.modules.purchasemanage.applymain.mapper.ApplyDetailMapper;
import com.hqu.modules.purchasemanage.applymain.mapper.ApplyMainMapper;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.OfficeMapper;
import com.jeeplus.modules.sys.mapper.UserMapper;

/**
 * 采购需求Service
 * @author syc
 * @version 2018-04-21
 */
@Service
@Transactional(readOnly = true)
public class ApplyMainService extends CrudService<ApplyMainMapper, ApplyMain> {

	@Autowired
	private ApplyDetailMapper applyDetailMapper;
	@Autowired
	private ApplyMainMapper applyMainMapper;
	@Autowired
	private InvAccountMapper invAccountMapper;
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	protected OfficeMapper officeMapper;
	@Autowired
	protected UserMapper userMapper;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	protected InvAccountService invAccountService;
	@Autowired
	protected AdtModelService adtModelService;
	@Autowired
	protected TaskService taskService;
	
	@Autowired
	protected AdtDetailService adtDetailService;
	
	
	public String getBillNum(){
		String numStr = "app";
		
		//获取年月日
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd"); 
		String c=sdf.format(new Date()); 
		int date = Integer.parseInt(c);
		
		//根据系统时间产生4位随机数
		long currentTimeMillis = System.currentTimeMillis();
		
		Random random = new Random(currentTimeMillis);
		int next1 = random.nextInt(10);
		
		currentTimeMillis+=(Math.random()*10000);
		random = new Random(currentTimeMillis);
		int next2 = random.nextInt(10);
		
		currentTimeMillis+=(Math.random()*10000);
		random = new Random(currentTimeMillis);
		int next3 = random.nextInt(10);
		
		currentTimeMillis+=(Math.random()*10000);
		random = new Random(currentTimeMillis);
		int next4 = random.nextInt(10);
		//return 结果
		numStr = numStr+date+next1+next2+next3+next4;
		List<ApplyMain> applyMainList = applyMainMapper.findList(new ApplyMain());
		Iterator<ApplyMain> it = applyMainList.iterator();
		while(it.hasNext()){
			ApplyMain applyMain = it.next();
			if(applyMain.getBillNum().equals(numStr)){
				return getBillNum();
			}
		}
		return numStr;
	}
	
	@Transactional(readOnly = false)
	public void insertIntoADTDetail(ApplyMain applyMain){
		String applytypename = applyMain.getApplyType().getApplytypeid();
		AdtModel adtModel = new AdtModel();
		adtModel.setModelCode("APP001");
		adtModel.setIsFirstperson("1");
		
		List<AdtModel> adtModelList = adtModelService.findList(adtModel);
		if(adtModelList!=null&&adtModelList.size()!=0){
			AdtModel adtModel2 = adtModelList.get(0);
			AdtBillType adtBillType = new AdtBillType();
			adtBillType.setBillTypeCode("APP001");
			adtDetailService.nextStep(applyMain.getBillNum(), adtBillType, adtModel2.getModelCode(), true, null);
			//向ADTDETAIL表里插入数据
//			AdtDetail adtDetail = new AdtDetail();
//			adtDetail.setBillNum(applyMain.getBillNum());
//			adtDetail.setBillTypeCode("APP001");
//			adtDetail.setJpositionCode(adtModel2.getJpositionCode());
//			adtDetail.setModelCode(adtModel2.getModelCode());
//			adtDetail.setIsFinished("0");
//			//完成插入
//			adtDetailService.save(adtDetail);
		}
	}
	
	
	public ApplyMain setHist(ApplyMain applyMain){
		//获取年月日
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd"); 
		String c=sdf.format(new Date()); 
		int date = Integer.parseInt(c);
		// get user name
		String name = applyMain.getUser().getName();
		List<ApplyDetail> applyDetailListT = new ArrayList<ApplyDetail>();
		List<ApplyDetail> applyDetailList = applyMain.getApplyDetailList();
		Iterator<ApplyDetail> it = applyDetailList.iterator();
		while(it.hasNext()){
			ApplyDetail applyDetail = it.next();
			String changeNum = applyDetail.getChangeNum();
			if(changeNum!=null&&!changeNum.equals("")){ 
				String log = applyDetail.getLog();
				String addStr = name+"-"+date+"-"+changeNum;
				log = log+addStr+"; ";
				applyDetail.setLog(log);
			}
			applyDetailListT.add(applyDetail);
		}
		applyMain.setApplyDetailList(applyDetailListT);
		return applyMain;
	}
	
	
	public ApplyMain get(String id) {
		
		ApplyMain applyMain = super.get(id);
		applyMain.setApplyDetailList(applyDetailMapper.findList(new ApplyDetail(applyMain)));
		return applyMain;
	}
	/*
	public InvAccountPur getInvAccountByItemCode(String itemCode){
		
		return invAccountPurMapper.getInvAccountByItemCode(itemCode);
		
	}
	
	public InvAccountPur getInvAccountById(String id){
		
		return invAccountPurMapper.get(id);
		
	}
	*/
	
	
	
	
	public List<ApplyMain> findList(ApplyMain applyMain) {
		return super.findList(applyMain);
	}
	@Transactional(readOnly = false)
	public Page<ApplyMain> findPage(Page<ApplyMain> page, ApplyMain entity) {
		dataRuleFilter(entity);
		entity.setPage(page);
		List<ApplyMain> resultList = new ArrayList<ApplyMain>();
		resultList = findChange(entity, resultList);
		entity.setBillStateFlag("B");
		resultList = findChange(entity, resultList);
		page.setList(resultList);
		page.setCount(resultList.size());
		return page;
	}

	private List<ApplyMain> findChange(ApplyMain entity, List<ApplyMain> resultList) {
		//item 中存放子表的筛选条件
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
		List<ApplyMain> findList = applyMainMapper.findList(entity);
		Iterator<ApplyMain> it = findList.iterator();
		while(it.hasNext()){
			ApplyMain applyMain = it.next();
			//根据筛选条件获取主表下的子表
			ApplyDetail applyDetail = new ApplyDetail(applyMain);
			applyDetail.setItem(item);
			applyDetail.setItemName(item.getName());
			applyDetail.setItemSpecmodel(item.getSpecModel());
			List<ApplyDetail> ApplyDetailList = applyDetailMapper.findList(applyDetail);
			//如果子表bu为空或长度bu为零
			if(ApplyDetailList!=null&&ApplyDetailList.size()!=0){
				//将A改成正在录入
				if(applyMain.getBillStateFlag().equals("A")){
					applyMain.setBillStateFlag("正在录入");
				}
				//将W改成正在录入
				if(applyMain.getBillStateFlag().equals("W")){
					applyMain.setBillStateFlag("录入完毕");
				}
				
				//将B改成正在录入
				if(applyMain.getBillStateFlag().equals("B")){
					applyMain.setBillStateFlag("审批未通过");
				}
				
				if(applyMain.getBillStateFlag().equals("E")){
					applyMain.setBillStateFlag("审批通过");
				}
				resultList.add(applyMain);
			}
		}
		return resultList;
	}
	
	
	public Page<Office> findOfficePage(Page<Office> page, Office office) {
		dataRuleFilter(office);
		office.setPage(page);
		List<Office> findList = applyMainMapper.findOfficeList(office);
		page.setList(findList);
		return page;
	}
	
	
	
	
	
	
	@Transactional(readOnly = false)
	public Page<User> findUserPage(Page<User> page, User entity) {
		dataRuleFilter(entity);
		entity.setPage(page);
		List<User> findList = applyMainMapper.findUserList(entity);
		page.setList(findList);
		return page;
	}
	@Transactional(readOnly = false)
	public Page<Item> findItemPage(Page<Item> page, Item entity) {
		dataRuleFilter(entity);
		entity.setPage(page);
		List<Item> findList = itemMapper.findList(entity);
		page.setList(findList);
		return page;
	}
	
	
	/*
	 * 
	 * public Page<T> findPage(Page<T> page, T entity) {
		dataRuleFilter(entity);
		entity.setPage(page);
		List<T> findList = mapper.findList(entity);
		page.setList(findList);
		return page;
	}
	 * 
	 * 
	 * */
	
	@Transactional(readOnly = false)
	public Page<ItemCustmer> findMyPage(Page<ItemCustmer> page, ItemCustmer itemCustmer1) {
		dataRuleFilter(itemCustmer1);
//		itemCustmer1.setPage(page);
		//////
//		List<ApplyMain> resultList = new ArrayList<ApplyMain>();
//		List<ApplyMain> findList = mapper.findList(applyMain);
//		//itemMapper
//		Iterator<ApplyMain> it = findList.iterator();
//		while(it.hasNext()){
//			ApplyMain applymain = it.next();
//			List<ApplyDetail> detailResultList = new ArrayList<ApplyDetail>();
//			List<ApplyDetail> applyDetailList = applymain.getApplyDetailList();
//			Iterator<ApplyDetail> it1 = applyDetailList.iterator();
//			ApplyDetail applyDetail =null;
//			while(it1.hasNext()){
//				applyDetail= it1.next();
//				String code = applyDetail.getItem().getCode();
//				InvAccount invAccountByItemCode = applyMainMapper.getInvAccountByItemCode(code);
//				applyDetail.setNowSum(""+invAccountByItemCode.getNowSum());
//				applyDetail.setCostPrice(invAccountByItemCode.getCostPrice()+"");
//				detailResultList.add(applyDetail);
//			}
//			applymain.setApplyDetailList(detailResultList);
//			resultList.add(applymain);
//		}
		//////
		Item i1 = new Item();
		i1.setName(itemCustmer1.getName());
		i1.setCode(itemCustmer1.getCode());
		List<Item> findList = applyMainMapper.findItemList(i1);
		List<ItemCustmer> custmerList = new ArrayList<ItemCustmer>();
		Iterator<Item> it = findList.iterator();
		while(it.hasNext()){
			ItemCustmer itemCustmer = new ItemCustmer();
			Item item = it.next();
			BeanUtils.copyProperties(item, itemCustmer);
			custmerList.add(itemCustmer);
		}
		List<ItemCustmer> resultCustmerList = new ArrayList<ItemCustmer>();
		Iterator<ItemCustmer> it1 = custmerList.iterator();
		while(it1.hasNext()){
			ItemCustmer itemCustmer = it1.next();
//code无用			
//			String code = itemCustmer.getCode();
			String id = itemCustmer.getId();
			InvAccount invAccountByItemCode = null;
			List<InvAccount> invAccountList = applyMainMapper.getInvAccountByItemCode(id);
			if(invAccountList!=null&&invAccountList.size()!=0){
				invAccountByItemCode = invAccountList.get(0);
			}
//			InvAccount invAccount = new InvAccount();
//			invAccount.
//			InvAccount invAccountByItemCode = invAccountMapper.findList();
			if(invAccountByItemCode!=null){
				itemCustmer.setNowSum(invAccountByItemCode.getNowSum()+"");
				itemCustmer.setCostPrice(invAccountByItemCode.getCostPrice()+"");
			}
			resultCustmerList.add(itemCustmer);
			
		}
		
		
		page.setList(resultCustmerList);
		
		return page;
	}
	
	

	
	@Transactional(readOnly = false)
	public void save(ApplyMain applyMain) {
		super.save(applyMain);
		for (ApplyDetail applyDetail : applyMain.getApplyDetailList()){
			if (applyDetail.getId() == null){
				continue;
			}
			if (ApplyDetail.DEL_FLAG_NORMAL.equals(applyDetail.getDelFlag())){
				if (StringUtils.isBlank(applyDetail.getId())){
					applyDetail.setApplyMain(applyMain);
					applyDetail.preInsert();
					applyDetailMapper.insert(applyDetail);
				}else{
					applyDetail.preUpdate();
					applyDetailMapper.update(applyDetail);
				}
			}else{
				applyDetailMapper.delete(applyDetail);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(ApplyMain applyMain) {
		super.delete(applyMain);
		applyDetailMapper.delete(new ApplyDetail(applyMain));
	}
	
	public String getUUID(){
		UUID uuid=UUID.randomUUID();
        String str = uuid.toString(); 
        String uuidStr=str.replace("-", "");
        return uuidStr;
	}
	
	
	@Transactional(readOnly = false)
	public void mySave(ApplyMain applyMain){
		String billNum = applyMain.getBillNum();
		List<ApplyMain> applyMainByBillNumList = applyMainMapper.getApplyMainByBillNum(billNum);
		if(applyMainByBillNumList!=null&&applyMainByBillNumList.size()!=0){
			//update
			applyMain.setId(applyMainByBillNumList.get(0).getId());
			applyMain.preUpdate();
			applyMainMapper.myUpdate(applyMain);
		}else{
			//insert
			applyMain.setId(getUUID());
			applyMain.preInsert();
			applyMainMapper.myInsert(applyMain);
		}
		
		applyDetailMapper.deleteByBillNum(applyMain.getId());
		for (ApplyDetail applyDetail : applyMain.getApplyDetailList()){
			if (applyDetail.getId() == null){
				continue;
			}
			
			applyDetail.setApplyMain(applyMain);
			applyDetail.preInsert();
			applyDetailMapper.insert(applyDetail);
//			if (ApplyDetail.DEL_FLAG_NORMAL.equals(applyDetail.getDelFlag())){
//				if (StringUtils.isBlank(applyDetail.getId())){
//					applyDetailMapper.deleteByBillNum(applyMain.getId());
//					applyDetail.setApplyMain(applyMain);
//					applyDetail.preInsert();
//					applyDetailMapper.insert(applyDetail);
//				}else{
//					applyDetail.preUpdate();
//					applyDetailMapper.update(applyDetail);
//				}
//			}else{
//				applyDetailMapper.delete(applyDetail);
//			}
		}
		
	}
	public ApplyMain getByProInId(String pid){
		ApplyMain byProInId = applyMainMapper.getByProInId(pid);
		if(byProInId==null){
			byProInId = new ApplyMain();
		}else{
			byProInId = get(byProInId.getId());
		}
		return byProInId;
	}
	/**
	 * 通过流程实例ID判断改流程是否结束，结束返回true，否则返回false
	 */
	public boolean isProcessFinish(String processInstanceId){
		List<Task> list = taskService.createTaskQuery()
			.processInstanceId(processInstanceId)
			.active()
			.list();
		if(list==null||list.size()==0){
			return true;
		}else{
			return false;
		}
	}
	
}