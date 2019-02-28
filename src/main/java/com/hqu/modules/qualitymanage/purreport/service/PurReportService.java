/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreport.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.service.ItemService;
import com.hqu.modules.inventorymanage.invcheckmain.entity.ReinvCheckMain;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckDetail;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckMain;
import com.hqu.modules.qualitymanage.purinvcheckmain.mapper.InvCheckDetailMapper;
import com.hqu.modules.qualitymanage.purinvcheckmain.mapper.InvCheckMainMapper;
import com.hqu.modules.qualitymanage.purreport.entity.PurReport;
import com.hqu.modules.qualitymanage.purreport.entity.PurReportRSn;
import com.hqu.modules.qualitymanage.purreport.mapper.PurReportMapper;
import com.hqu.modules.qualitymanage.purreport.mapper.PurReportRSnMapper;
import com.hqu.modules.qualitymanage.purreportnew.entity.PurReportNew;
import com.hqu.modules.qualitymanage.purreportnew.entity.PurReportNewCust;
import com.hqu.modules.qualitymanage.qmreport.entity.QmReport;
import com.hqu.modules.qualitymanage.qmschecktype.entity.Checktype;
import com.hqu.modules.qualitymanage.qmschecktype.mapper.ChecktypeMapper;
import com.hqu.modules.qualitymanage.verifyqcnorm.entity.VerifyQCNorm;
import com.hqu.modules.qualitymanage.verifyqcnorm.mapper.VerifyQCNormMapper;
import com.hqu.modules.qualitymanage.verifyqcnorm.service.VerifyQCNormService;
import com.hqu.modules.qualitymanage.verifyrule.entity.VerifyRule;
import com.hqu.modules.qualitymanage.verifyrule.mapper.VerifyRuleMapper;
import com.hqu.modules.workshopmanage.sfcinvcheckmain.entity.SfcInvCheckMain;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.act.service.ActTaskService;

/**
 * 检验单(采购/装配/整机检测)Service
 * @author 孙映川
 * @version 2018-04-05
 */
@Service
@Transactional(readOnly = true)
public class PurReportService extends CrudService<PurReportMapper, PurReport> {

	@Autowired
	private PurReportRSnMapper purReportRSnMapper;
	
	@Autowired
	private InvCheckDetailMapper invCheckDetailMapper;
	@Autowired
	private PurReportMapper purReportMapper;
	
	@Autowired
	private ChecktypeMapper checktypeMapper;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	protected TaskService taskService;
	@Autowired
	protected HistoryService historyService;
	@Autowired
	protected RepositoryService repositoryService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private VerifyQCNormMapper verifyQCNormMapper;
	
	@Autowired
	private VerifyRuleMapper verifyRuleMapper;
	
	@Autowired
	private VerifyQCNormService verifyQCNormService;
	
	
	@Autowired
	private InvCheckMainMapper invCheckMainMapper;
	
	
	public PurReport get(String id) {
		PurReport purReport = super.get(id);
		if(purReport!=null){
			purReport.setPurReportRSnList(purReportRSnMapper.findList(new PurReportRSn(purReport)));
		}
		return purReport;
	}
	
	public List<PurReport> findList(PurReport purReport) {
		return super.findList(purReport);
	}
	
	public Page<PurReport> findPage(Page<PurReport> page, PurReport purReport) {
		return findNewPage(page, purReport);
		//return super.findPage(page, purReport);
	}
	
	public Page<InvCheckMain> findInvCheckMainPage(Page<InvCheckMain> page, InvCheckMain invCheckMain){
		dataRuleFilter(invCheckMain);
		invCheckMain.setPage(page);
		List<InvCheckMain> findList = purReportMapper.findInvList(invCheckMain);
		page.setList(findList);
		return page;
	}
	
	
	public Page<PurReportNew> findInvCheckMainPage2(Page<PurReportNew> page, PurReportNewCust purReportNewCust){
		dataRuleFilter(purReportNewCust);
		//purReportNew.setPage(page);
		InvCheckMain invCheckMain1 = new InvCheckMain();
		/*
		 * 手动对象赋值
		 * */
		
		
//		try {
//			BeanUtils.copyProperties(purReportNewCust, invCheckMain1);
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		invCheckMain1.setBillnum(purReportNewCust.getBillnum());
		invCheckMain1.setMainItemSpecmodel(purReportNewCust.getItemSpecmodel());
		invCheckMain1.setSupId(purReportNewCust.getSupId());
		invCheckMain1.setSupName(purReportNewCust.getSupName());
		
		
 		List<InvCheckMain> findList = purReportMapper.findInvList(invCheckMain1);

		List<InvCheckMain> tempList = new ArrayList<>();
		for(InvCheckMain temp : findList){
			String qmsFlg = temp.getQmsFlag();
			if(!"已质检".equals(qmsFlg)&&"E".equals(temp.getBillStateFlag())){
				tempList.add(temp);
			}
		}
		findList = tempList ;

		Iterator<InvCheckMain> it = findList.iterator();
		List<PurReportNew> list = new ArrayList<PurReportNew>();
		while(it.hasNext()){
			InvCheckMain invCheckMainit = it.next();
		if((invCheckMainit.getContractId()!=null&&invCheckMainit.getContractId().length()!=0)||(invCheckMainit.getPlanId()!=null && invCheckMainit.getPlanId().length()!=0)||true){
			InvCheckMain invCheckMain = invCheckMainit;
			InvCheckDetail de = new InvCheckDetail(invCheckMain);
			de.setItemName(purReportNewCust.getItemName());
			Item item = new Item();
			if(purReportNewCust.getItemCode()==null||"".equals(purReportNewCust.getItemCode())){
				de.setItem(item);
			}else{
				item.setCode(purReportNewCust.getItemCode());
				List<Item> findList2 = itemService.findList(item);
				if(findList2!=null&&findList2.size()!=0){
					item = findList2.get(0);
				}else{
					item = null;
				}
				de.setItem(item);
			}
			
			invCheckMain.setInvCheckDetailList(invCheckDetailMapper.findList(de));
			List<InvCheckDetail> invCheckDetailList = invCheckMain.getInvCheckDetailList();
			Iterator<InvCheckDetail> detail = invCheckDetailList.iterator();
			while(detail.hasNext()){
				InvCheckDetail invCheckDetail = detail.next();

				//-----------------------------------------
				String qmsFlag = invCheckDetail.getQmsFlag();
				if("0".equals(qmsFlag)){
					continue;
				}



				PurReportNewCust purReportNew2 = new PurReportNewCust();
				purReportNew2.setBillnum(invCheckMain.getBillnum());
				purReportNew2.setItemCode(invCheckDetail.getItemCode());
				//purReportNew2.setItemCode(invCheckDetail.getItem().getCode());
				purReportNew2.setSupId(invCheckMain.getSupId());
				purReportNew2.setSupName(invCheckMain.getSupName());
				purReportNew2.setItemSpecmodel(invCheckDetail.getItemSpecmodel());
				purReportNew2.setItemName(invCheckDetail.getItemName());
				if(invCheckDetail.getCheckQty()==null){
					//purReportNew2.setItemCount(0.0);
				}else{
					double d = invCheckDetail.getCheckQty();
					purReportNew2.setItemCount(d);
				}
				
				InvCheckMain inv = new InvCheckMain();
				inv.setId(invCheckMain.getId());
				purReportNew2.setInv(inv);
				purReportNew2.setItemArriveDate(invCheckMain.getArriveDate());
				purReportNew2.setUnitCode(invCheckDetail.getUnitCode());
				if(invCheckDetail.getRealPriceTaxed()==null){
					//purReportNew2.setRealPriceTaxed(0.0);
				}else{
					purReportNew2.setRealPriceTaxed(invCheckDetail.getRealPriceTaxed());
				}
				
				list.add(purReportNew2);
			}
			
		}
		}
		
		page.setList(list);
		page.setCount(list.size());
		return page;
	}
	
	
	public Page<SfcInvCheckMain> findSFCPage(Page<SfcInvCheckMain> page, SfcInvCheckMain sfcInvCheckMain) {
		
		dataRuleFilter(sfcInvCheckMain);
		sfcInvCheckMain.setPage(page);
		List<SfcInvCheckMain> findList = purReportMapper.findSFCList(sfcInvCheckMain);
		page.setList(findList);
		return page;
		
	}
	
	
	public Page<ReinvCheckMain> findReInvPage(Page<ReinvCheckMain> page, ReinvCheckMain reinvCheckMain) {
		dataRuleFilter(reinvCheckMain);
		reinvCheckMain.setPage(page);
		List<ReinvCheckMain> findReInvList = purReportMapper.findReInvList(reinvCheckMain);
		page.setList(findReInvList);
		return page;
	}
	
	
	
	/*
	  dataRuleFilter(entity);
		entity.setPage(page);
		page.setList(mapper.findList(entity));
		return super.findPage(page, purReport);
	 
	 */
	
	
	
	
	
	
	
	public Page<PurReport> findNewPage(Page<PurReport> page, PurReport purReport) {
		dataRuleFilter(purReport);
		purReport.setPage(page);
		String rndFul = purReport.getRndFul();
		if("qj".equals(rndFul)){
			purReport.setRndFul("全检");
		}
		if("cj".equals(rndFul)){
			purReport.setRndFul("抽检");
		}
		if("cg".equals(purReport.getChecktName())){
			purReport.setChecktName("采购");
		}
		
		if("hg".equals(purReport.getCheckResult())){
			purReport.setCheckResult("合格");
		}
		if("bfhg".equals(purReport.getCheckResult())){
			purReport.setCheckResult("部分合格");
		}
		if("bhg".equals(purReport.getCheckResult())){
			purReport.setCheckResult("不合格");
		}
		List<PurReport> resultList = new ArrayList<PurReport>();
		String audit = purReport.getAudit();
		
		
		List<String> findVeryfyQCNorm = findVeryfyQCNorm(audit);
		List<PurReport> findListWithOutCheck = mapper.findListWithOutCheck(purReport);
		
		// 按照reportId查
		for(String temp:findVeryfyQCNorm){
			for(PurReport pur:findListWithOutCheck){
				String reportId = pur.getReportId();
				if(temp.equals(reportId)){
					resultList.add(pur);
				}
			}
		}
		
		
		
		page.setList(findListWithOutCheck);
		return page;
	}
	
	
	public Page<PurReport> findNewPageForCom(Page<PurReport> page, PurReport purReport) {
		//dataRuleFilter(purReport);
		purReport.setPage(page);

		String rndFul = purReport.getRndFul();
		if("qj".equals(rndFul)){
			String s="qw";
			purReport.setRndFul("全检");
		}
		if("cj".equals(rndFul)){
			purReport.setRndFul("抽检");
		}
		if("cg".equals(purReport.getChecktName())){
			purReport.setChecktName("采购");
		}
		
		if("hg".equals(purReport.getCheckResult())){
			purReport.setCheckResult("合格");
		}
		if("bfhg".equals(purReport.getCheckResult())){
			purReport.setCheckResult("部分合格");
		}
		if("bhg".equals(purReport.getCheckResult())){
			purReport.setCheckResult("不合格");
		}
		List<PurReport> resultList = new ArrayList<PurReport>();
		String audit = purReport.getAudit();
		
		
		List<String> findVeryfyQCNorm = findVeryfyQCNorm(audit);
		List<PurReport> findListWithOutCheck = mapper.findListWithOutCheck(purReport);
		
		// 按照reportId查
		for(String temp:findVeryfyQCNorm){
			for(PurReport pur:findListWithOutCheck){
				String reportId = pur.getReportId();
				if(temp.equals(reportId)){
					resultList.add(pur);
				}
			}
		}
		
		//扫码检索
		List<PurReport> newresultList = new ArrayList<PurReport>();
		if(!"".equals(purReport.getScanCode())){
			for(PurReport temp : resultList){
				temp = get(temp.getId());
				List<PurReportRSn> purReportRSnList = temp.getPurReportRSnList();
				PurReportRSn purReportRSn = purReportRSnList.get(0);
				String objSn = purReportRSn.getObjSn();
				if(purReport.getScanCode().equals(objSn)){
					newresultList.add(temp);
					page.setList(newresultList);
					return page;
				}
			}
			//如果没查着，返回空页面
			page.setList(new ArrayList<PurReport>());
			return page;
		}
		
		page.setList(resultList);
		return page;
	}
	
	
	
	
	/**
	 * QMS_VerifyQCNorm
	 */
	
	public List<String> findVeryfyQCNorm(String audit){
		VerifyQCNorm verifyQCNorm = new VerifyQCNorm();
		verifyQCNorm.setCheckprj(audit);
		
		List<String> reportIdList = new ArrayList<String>();
		
		List<String> resultList = new ArrayList<String>();
		List<VerifyQCNorm> findList = verifyQCNormMapper.findList(verifyQCNorm);
		Iterator<VerifyQCNorm> it = findList.iterator();
		while(it.hasNext()){
			VerifyQCNorm temp = it.next();
			String isfisished = temp.getIsfisished();
			String[] split = isfisished.split(",");
			//如果是0或者1，加入resultList，重复不加
			if(split!=null&&split.length!=0){
				if("0".equals(split[0])||"1".equals(split[0])){
					String reportId = temp.getReportId();
					if(!reportIdList.contains(reportId)){
						reportIdList.add(reportId);
						resultList.add(reportId);
					}
				}
			}
		}
		return resultList;
	}
	
	@Transactional(readOnly = false)
	public void save(PurReport purReport) {

		super.save(purReport);
		
		
	
		
		
		for (PurReportRSn purReportRSn : purReport.getPurReportRSnList()){
			if (purReportRSn.getId() == null){
				continue;
			}
			String a2="s";
			List<PurReport> findList = findList(purReport);
			if(findList!=null&&findList.size()!=0){
				purReport = findList.get(0);
			}
			//purReport = get(uuid);
			
			if (PurReportRSn.DEL_FLAG_NORMAL.equals(purReportRSn.getDelFlag())){
				PurReportRSn purReportRSn2 = purReportRSnMapper.get(purReportRSn.getId());
				
				if (purReportRSn2!=null&&purReportRSn2.getId()!=null&&!purReportRSn2.getId().equals("")){
					String s = "4";
					purReportRSn.setPurReport(purReport);
				//	purReportRSn.preInsert();
				//	purReportRSnMapper.insert(purReportRSn);
					purReportRSn.preUpdate();
					purReportRSnMapper.myUpdate(purReportRSn);
				}else{
					String s1 = "2";
					purReportRSn.setPurReport(purReport);
					purReportRSn.preInsert();
					purReportRSnMapper.myInsert(purReportRSn);
					
				}
			}else{
				purReportRSnMapper.delete(purReportRSn);
			}
		}
	}
	
	
	
	
	@Transactional(readOnly = false)
	public void save1(PurReport purReport) {
//		purReport.setIsNewRecord(true);
//		String uuid = getUUID();
//		purReport.setId(uuid);
//		super.save(purReport);
		
		String id = purReport.getReportId();
		List<PurReport> byReportId = purReportMapper.getByReportId(id);
		
		//PurReport purReport2 = get(purReport.getId());
		if(byReportId!=null&&byReportId.size()!=0){
			String s ="s";
			if(byReportId.get(0)!=null){
				purReport.setId(byReportId.get(0).getId());
			}
			id = purReport.getId();
			purReport.preUpdate();
			if(purReport.getReinvCheckMain()!=null){
				purReportMapper.myUpdataREINV(purReport);
			}else {
				purReportMapper.myUpdata(purReport);
			}
		}else{
			id = getUUID();


			purReport.setId(id);
			purReport.preInsert();
			if(purReport.getReinvCheckMain()!=null){
				purReportMapper.myInsertREINV(purReport);
			}else {
				purReportMapper.myInsert(purReport);
			}

		}
		
		
		
		
		
		
		purReportRSnMapper.deleteAllByReportId(purReport.getId());
		
		for (PurReportRSn purReportRSn : purReport.getPurReportRSnList()){
			if (purReportRSn.getId() == null){
				continue;
			}
			purReportRSn.setPurReport(purReport);
			purReportRSn.preInsert();
			purReportRSn.setId(getUUID());
			purReportRSnMapper.insert(purReportRSn);
//			if (PurReportRSn.DEL_FLAG_NORMAL.equals(purReportRSn.getDelFlag())){
//				if (StringUtils.isBlank(purReportRSn.getId())){
//					purReportRSn.setPurReport(purReport);
//					purReportRSn.preInsert();
//					purReportRSn.setId(getUUID());
//					purReportRSnMapper.insert(purReportRSn);
//				}else{
//					purReportRSn.preUpdate();
//					purReportRSnMapper.update(purReportRSn);
//				}
//			}else{
//				purReportRSnMapper.delete(purReportRSn);
//			}
//			if (PurReportRSn.DEL_FLAG_NORMAL.equals(purReportRSn.getDelFlag())){
//				PurReportRSn purReportRSn2 = purReportRSnMapper.get(purReportRSn.getId());
//				
//				if (purReportRSn2!=null&&purReportRSn2.getId()!=null&&!purReportRSn2.getId().equals("")){
//					String s = "4";
//					purReportRSn.setPurReport(purReport);
//					//purReportRSn.setId(getUUID());
//				//	purReportRSn.preInsert();
//				//	purReportRSnMapper.insert(purReportRSn);
//					purReportRSn.preUpdate();
//					purReportRSnMapper.myUpdate(purReportRSn);
//				}else{
//					String s1 = "2";
//					purReportRSn.setPurReport(purReport);
//					purReportRSn.setId(getUUID());
//					purReportRSn.preInsert();
//					purReportRSnMapper.myInsert(purReportRSn);
//					
//				}
//			}else{
//				purReportRSnMapper.delete(purReportRSn);
//			}
		}
	}
	
	
	
	//判断合格和写入未审核
	public PurReport subsave(PurReport purReport){
		//写入CheckTCode到对象purReport中
		purReport= getCheckTCode(purReport);
		int rCount=0;
		int fCount=0;
		List<PurReportRSn> purReportRSnList = purReport.getPurReportRSnList();
		Iterator<PurReportRSn> it = purReportRSnList.iterator();
		while(it.hasNext()){
			PurReportRSn purReportRSn = it.next();
			String checkResult = purReportRSn.getCheckResult();
			if("合格".equals(checkResult)){
				rCount++;
			}else{
				fCount++;
			}
			
		}
		if(rCount==0&&fCount!=0){
			purReport.setCheckResult("不合格");
		}
		if(rCount!=0&&fCount==0){
			purReport.setCheckResult("合格");
		}
		if(rCount!=0&&fCount!=0){
			purReport.setCheckResult("部分合格");
		}
		if(rCount==0&&fCount==0){
			purReport.setCheckResult("");
		}
		if((rCount+fCount)==0){
			purReport.setPercentPass(0);
		}else{
			purReport.setPercentPass(rCount/(rCount+fCount));
		}
		
		
		return purReport;
	}
	
	public String getReportID(){
		String numStr = "CHL";
		
		//获取年月日
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd"); 
		String c=sdf.format(new Date()); 
		int date = Integer.parseInt(c);
		
		//根据系统时间产生4位随机数
		Random random = new Random(System.currentTimeMillis());
		int nextInt = random.nextInt(10001);
		
		String newNextInt = "0000"+nextInt;
		newNextInt = newNextInt.substring(newNextInt.length()-4,newNextInt.length());
		
		//return 结果
		numStr = numStr+date+newNextInt;
		List<PurReport> purReportList = purReportMapper.findList(new PurReport());
		Iterator<PurReport> it = purReportList.iterator();
		while(it.hasNext()){
			PurReport purReport = it.next();
			if(purReport.getReportId().equals(numStr)){
				return getReportID();
			}
		}
		return numStr;
	}
	
	@Transactional(readOnly = false)
	public void updata(PurReport purReport){
		
	//	purReport= getCheckTCode(purReport);
		String reportId = purReport.getReportId();
		PurReport purReport2 = purReportMapper.get(purReport.getId());
	//	String reportId = purReport2.getReportId();
	//	purReport.setCdeptCode(purReport.getOffice().getId());
		if(purReport2==null){
			purReport.preInsert();
			purReport.setId(getUUID());
			purReportMapper.myInsert(purReport);
			
		}else{
			purReport.preUpdate();
			purReportMapper.updateByReportId(purReport);
		}
		for (PurReportRSn purReportRSn : purReport.getPurReportRSnList()){
			if (purReportRSn.getId() == null){
				continue;
			}
			if (PurReportRSn.DEL_FLAG_NORMAL.equals(purReportRSn.getDelFlag())){
				PurReportRSn purReportRSn2 = purReportRSnMapper.get(purReportRSn.getId());
				
				if (purReportRSn2!=null&&purReportRSn2.getId()!=null&&!purReportRSn2.getId().equals("")){
					purReportRSn.setPurReport(purReport);
				//	purReportRSn.preInsert();
				//	purReportRSnMapper.insert(purReportRSn);
					purReportRSn.preUpdate();
					purReportRSnMapper.update(purReportRSn);
				}else{
					String s = "2";
					UUID uuid=UUID.randomUUID();
			        String str = uuid.toString(); 
			        String uuidStr=str.replace("-", "");
			        purReportRSn.setId(uuidStr);
					//purReportRSn.preInsert();
					purReportRSn.setPurReport(purReport);
					purReportRSn.preInsert();
					purReportRSnMapper.insert(purReportRSn);
					
				}
			}else{
				purReportRSnMapper.delete(purReportRSn);
			}
		}
		
	}
	
	public String getUUID(){
		UUID uuid=UUID.randomUUID();
        String str = uuid.toString(); 
        String uuidStr=str.replace("-", "");
        return uuidStr;
	}
	
	
	
	@Transactional(readOnly = false)
	public void myUpdata(PurReport purReport){
		
	//	purReport= getCheckTCode(purReport);
	//	PurReport purReport2 = null;
		String reportId = purReport.getReportId();
//		List<PurReport> byReportIdList = purReportMapper.getByReportId(reportId);
//		if(byReportIdList!=null&&byReportIdList.size()!=0){
//			purReport2 = purReport2.get(0);
//		}
		PurReport purReport2 = purReportMapper.get(purReport.getId());
	//	String reportId = purReport2.getReportId();
	//	purReport.setCdeptCode(purReport.getOffice().getId());
		if(purReport2==null){
			purReport.preInsert();
			purReportMapper.insert(purReport);
			
		}else{
			purReport.preUpdate();
			purReportMapper.updateByReportId(purReport);
		}
		List<PurReportRSn> purReportRSnList = purReport.getPurReportRSnList();
		List<PurReport> findList = findList(purReport);
		if(findList!=null&&findList.size()!=0){
			purReport = findList.get(0);
			//purReport = get(purReport.getId());
		}
		for (PurReportRSn purReportRSn : purReportRSnList){
			if (purReportRSn.getId() == null){
				continue;
			}
			if (PurReportRSn.DEL_FLAG_NORMAL.equals(purReportRSn.getDelFlag())){
				PurReportRSn purReportRSn2 = purReportRSnMapper.get(purReportRSn.getId());
				
				if (purReportRSn2!=null&&purReportRSn2.getId()!=null&&!purReportRSn2.getId().equals("")){
					String s = "4";
					purReportRSn.setPurReport(purReport);
				//	purReportRSn.preInsert();
				//	purReportRSnMapper.insert(purReportRSn);
					purReportRSn.preUpdate();
					purReportRSnMapper.myUpdate(purReportRSn);
				}else{
					String s1 = "2";
					purReportRSn.setPurReport(purReport);
					purReportRSn.preInsert();
					purReportRSnMapper.myInsert(purReportRSn);
					
				}
			}else{
				purReportRSnMapper.delete(purReportRSn);
			}
		}
		
	}
	
	
	
	
	public PurReport getCheckTCode(PurReport purReport){
		String checktName = purReport.getChecktName();
		
		List<Checktype> checkTypeList = checktypeMapper.findList(new Checktype());
		Iterator<Checktype> it = checkTypeList.iterator();
		while(it.hasNext()){
			Checktype checktype = it.next();
			if(checktype.getChecktname().equals(checktName)){
				purReport.setChecktCode(checktype.getChecktcode());
				return purReport;
			}
		}
		return purReport;
	}
	
	
	
	
	@Transactional(readOnly = false)
	public void delete(PurReport purReport) {
		super.delete(purReport);
		purReportRSnMapper.delete(new PurReportRSn(purReport));
	}
	
	
	/**
	 * 添加修改历史
	 * @param purReport
	 * @return
	 */
	@Transactional(readOnly = false)
	public PurReport addJustifyLog(PurReport purReport) {
		List<PurReportRSn> purReportRSnList = purReport.getPurReportRSnList();
		List<PurReportRSn> list = new ArrayList<PurReportRSn>();
		Iterator<PurReportRSn> it = purReportRSnList.iterator();
		while(it.hasNext()){
			PurReportRSn purReportRSn = it.next();
			//获取审核结论
			String checkResult = purReportRSn.getCheckResult();
			//获取审核人
			String checkPerson = purReportRSn.getCheckPerson();
			//获取检验时间
			//获取年月日
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd"); 
			String string=sdf.format(new Date()); 
			
			
			//String string = checkDate.toString();
			String justifyLog = purReportRSn.getJustifyLog();
			if(justifyLog==null){
				justifyLog="";
			}
			purReportRSn.setJustifyLog(justifyLog+checkResult+","+checkPerson+","+string+";");
			list.add(purReportRSn);
		}
		purReport.setPurReportRSnList(list);
		return purReport;
	}
	
	
	
	/**
	 * @Autowired
	 *private VerifyQCNormMapper verifyQCNormMapper;
	 *
	 *@Autowired
	 *private VerifyRuleMapper verifyRuleMapper;
	 * 
	 * 向整机检验验证表中插入数据
	 * 
	 * billNo 为整机检验单编号
	 */
	@Transactional(readOnly = false)
	public void insertVerifyQCNorm(PurReport purReport){
		//获得verifyRule里的所有信息
		List<VerifyRule> ruleList = verifyRuleMapper.findList(new VerifyRule());
		//获取billno
		purReport = get(purReport.getId());
		String reportId = purReport.getReportId();
		//获得子表信息
		List<PurReportRSn> purReportRSnList = purReport.getPurReportRSnList();
		//遍历子表
		Iterator<PurReportRSn> it = purReportRSnList.iterator();
		while(it.hasNext()){
			PurReportRSn purReportRSn = it.next();
			VerifyQCNorm verifyQCNorm = new VerifyQCNorm();
			//赋值
			verifyQCNorm.setReportId(reportId);
			verifyQCNorm.setObjSn(purReportRSn.getObjSn());
			
			if(purReportRSn.getObjectDef()!=null){
				verifyQCNorm.setObjCode(purReportRSn.getObjectDef().getId());
			}
			
			verifyQCNorm.setObjCode(purReportRSn.getObjCode());
			
			verifyQCNorm.setObjName(purReportRSn.getObjName());
			//遍历ruleList
			Iterator<VerifyRule> itRole = ruleList.iterator();
			while(itRole.hasNext()){
				VerifyRule verifyRule = itRole.next();
				verifyQCNorm.setRuleId(verifyRule.getRuleId());
				verifyQCNorm.setCheckprj(verifyRule.getCheckprj());
				if(verifyRule.getQcnorm()!=null){
					verifyQCNorm.setQcnormId(verifyRule.getQcnorm().getQcnormId());
				}
				verifyQCNorm.setQcnormName(verifyRule.getQcnormName());
				verifyQCNorm.setRoleCode(verifyRule.getRole().getId());
				verifyQCNorm.setRoleName(verifyRule.getRoleName());
				//判断数据库中是否有该条记录
				List<VerifyQCNorm> findList = verifyQCNormMapper.findList(verifyQCNorm);
				if(findList==null||findList.size()==0){
					String str1 = "2";
					//设置状态
					//verifyQCNorm.setIsfisished("");
					if(verifyQCNorm.getCheckprj().equals("安规")){
						verifyQCNorm.setIsfisished("0, , ");
					}else{
						verifyQCNorm.setIsfisished(" , , ");
					}
					//设置ID
//					UUID uuid=UUID.randomUUID();
//			        String str = uuid.toString(); 
//			        String uuidStr=str.replace("-", "");
//			        verifyQCNorm.setId(uuidStr);
					//插入数据
			        verifyQCNormService.save(verifyQCNorm);
				}
			}
		}
		
		
		
	}
	
	
	
	/**
	 * 将安规、功能老化等不合格的整机存入问题处理单
	 * 问题处理单
	 */
	@Transactional(readOnly = false)
	public void addToQMReport(VerifyQCNorm verifyQCNorm){
		String reportId = verifyQCNorm.getReportId();
		String objSn = verifyQCNorm.getObjSn();
		if(reportId==null||"".equals(reportId)||objSn==null||"".equals(objSn)){
			return ;
		}
		//目标
		PurReport target = null;
		//按照reportId查询
		PurReport purReport = new PurReport();
		purReport.setReportId(reportId);
		List<PurReport> findList = findList(purReport);
		//遍历找子表，objSn
		Iterator<PurReport> it = findList.iterator();
		while(it.hasNext()){
			PurReport temp = it.next();
			List<PurReportRSn> purReportRSnList = temp.getPurReportRSnList();
			Iterator<PurReportRSn> itSn = purReportRSnList.iterator();
			while(itSn.hasNext()){
				PurReportRSn purReportRSn = itSn.next();
				if(purReportRSn!=null){
					String objSn2 = purReportRSn.getObjSn();
					if(objSn.equals(objSn2)){
						target = temp;
					}
				}
			}
		}
		//根据target生成问题处理单
		QmReport qmReport = new QmReport();
		
	}
	
	
}