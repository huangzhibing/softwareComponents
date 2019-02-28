/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.incomingstatistics.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hqu.modules.qualitymanage.incomingstatistics.entity.Incomingstatistics;
import com.hqu.modules.qualitymanage.incomingstatistics.mapper.IncomingstatisticsMapper;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckMain;
import com.hqu.modules.qualitymanage.purinvcheckmain.service.InvCheckMainService;
import com.hqu.modules.qualitymanage.purreportnew.entity.PurReportNew;
import com.hqu.modules.qualitymanage.purreportnew.entity.Purreportfundetail;
import com.hqu.modules.qualitymanage.purreportnew.entity.Purreportnewsn;
import com.hqu.modules.qualitymanage.purreportnew.entity.Purreportsizedetail;
import com.hqu.modules.qualitymanage.purreportnew.service.PurReportNewService;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;

/**
 * 主要供应商来料不良批次统计Service
 * @author syc
 * @version 2018-08-25
 */
@Service
@Transactional(readOnly = true)
public class IncomingstatisticsService extends CrudService<IncomingstatisticsMapper, Incomingstatistics> {

	@Autowired
	private PurReportNewService purReportNewService;
	
	@Autowired
	private InvCheckMainService invCheckMainService;
	
	public Incomingstatistics get(String id) {
		return super.get(id);
	}
	
	public List<Incomingstatistics> findList(Incomingstatistics incomingstatistics) {
		return super.findList(incomingstatistics);
	}
	
	public Page<Incomingstatistics> findPage(Page<Incomingstatistics> page, Incomingstatistics incomingstatistics) {
		dataRuleFilter(incomingstatistics);
		incomingstatistics.setPage(page);
		Integer m =null;
		Integer y =null;
		if(!"".equals(incomingstatistics.getM())){
			m = Integer.parseInt(incomingstatistics.getM());
		}
		if(!"".equals(incomingstatistics.getY())){
			y = Integer.parseInt(incomingstatistics.getY());
		}
		
		if(m==null){
			m = Calendar.getInstance().get(Calendar.MONTH)+1;
		}
		if(y==null){
			y = Calendar.getInstance().get(Calendar.YEAR);
		}
		//获取该年月的头一天和最后一天
		Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.YEAR,y) ;
		cal.set(Calendar.MONTH, m); 
		cal.set(Calendar.DAY_OF_MONTH, 1); 
		cal.add(Calendar.DAY_OF_MONTH, -1); 
		Date lastDate = cal.getTime(); 
		cal.set(Calendar.DAY_OF_MONTH, 1); 
		Date firstDate = cal.getTime();
		
		PurReportNew purReportNew = new PurReportNew();
		purReportNew.setBeginItemArriveDate(firstDate);
		purReportNew.setEndItemArriveDate(lastDate);
		
		List<PurReportNew> list = purReportNewService.findList(purReportNew);
		
		//一张到货单对应多个检验单,一个到货单一个批次
		Map<String,List<String>> map = new HashMap<>();
		for(PurReportNew temp:list){
			InvCheckMain inv = temp.getInv();
			String billnum = inv.getBillnum();
			List<String> list2 = map.get(billnum);
			if(list2==null){
				ArrayList<String> arrayList = new ArrayList<String>();
				arrayList.add(temp.getReportId());
				map.put(billnum, arrayList);
			}else{
				list2.add(temp.getReportId());
				map.put(billnum, list2);
			}
		}
		
		//供应商：javaBean
		Map<String,Incomingstatistics> beanMap = new HashMap<>();
		for(String temp :map.keySet()){
			//temp 为到货单号   list2为检验单编号
			List<String> list2 = map.get(temp);
			//通过到货单号找供应商
			InvCheckMain invCheckMain = new InvCheckMain();
			invCheckMain.setBillnum(temp);
			InvCheckMain invCheckMain2 = invCheckMainService.findList(invCheckMain).get(0);
			String supName =invCheckMain2.getSupName();
			//判断是否不良
			boolean isGood = true;
			for(String str : list2){
				//str为检验单编号
				PurReportNew p = new PurReportNew();
				p.setReportId(str);
				PurReportNew purReportNew2 = purReportNewService.findList(p).get(0);
				purReportNew2 = purReportNewService.get(purReportNew2.getId());
				//获取子表
				List<Purreportfundetail> purreportfundetailList = purReportNew2.getPurreportfundetailList();
				List<Purreportnewsn> purreportnewsnList = purReportNew2.getPurreportnewsnList();
				List<Purreportsizedetail> purreportsizedetailList = purReportNew2.getPurreportsizedetailList();
				if(purreportfundetailList.size()!=0){
					isGood = false;
				}
				if(purreportnewsnList.size()!=0){
					isGood = false;
				}
				if(purreportsizedetailList.size()!=0){
					isGood = false;
				}
			}
			//对beanMap统计
			Incomingstatistics incomingstatistics2 = beanMap.get(supName);
			if(incomingstatistics2==null){
				Incomingstatistics i = new Incomingstatistics();
				i.setSupName(supName);
				i.setSumBatch(1);
				if(!isGood){
					i.setBadBatch(1);
				}else{
					i.setBadBatch(0);
				}
				beanMap.put(supName, i);
			}else{
				Integer sumBatch = incomingstatistics2.getSumBatch();
				sumBatch = sumBatch+1;
				incomingstatistics2.setSumBatch(sumBatch);
				if(!isGood){
					Integer badBatch = incomingstatistics2.getBadBatch();
					badBatch = badBatch+1;
					incomingstatistics2.setBadBatch(badBatch);
				}
				beanMap.put(supName, incomingstatistics2);
			}
		}
		 
		List<Incomingstatistics> resultList = new ArrayList<>();
		List<Incomingstatistics> tempList = new ArrayList<>();
		//map->list
		for(String temp :beanMap.keySet()){
			Incomingstatistics incomingstatistics2 = beanMap.get(temp);
			tempList.add(incomingstatistics2);
		}
		//对	tempList放入resultList
//		int max = 0;
//		Incomingstatistics temp=null;
//		for(Incomingstatistics a : tempList){
//			Integer badBatch = a.getBadBatch();
//			if(badBatch>max){
//				max = badBatch;
//			}
//		}

		TreeSet<Incomingstatistics> ts=new TreeSet<Incomingstatistics>();
		for(Incomingstatistics a : tempList){
			ts.add(a);
		}
		for(Incomingstatistics a : ts){
			resultList.add(a);
		}
		
		
		
		
		
		//List<Incomingstatistics> findList = mapper.findList(incomingstatistics);
		page.setList(resultList);
		page.setCount(resultList.size());
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(Incomingstatistics incomingstatistics) {
		super.save(incomingstatistics);
	}
	
	@Transactional(readOnly = false)
	public void delete(Incomingstatistics incomingstatistics) {
		super.delete(incomingstatistics);
	}
	
}