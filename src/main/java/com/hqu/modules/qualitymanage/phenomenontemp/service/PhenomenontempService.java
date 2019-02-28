/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.phenomenontemp.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hqu.modules.qualitymanage.mattertype.entity.MatterType;
import com.hqu.modules.qualitymanage.phenomenontemp.entity.Phenomenontemp;
import com.hqu.modules.qualitymanage.phenomenontemp.mapper.PhenomenontempMapper;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckMain;
import com.hqu.modules.qualitymanage.purreportnew.entity.PurReportNew;
import com.hqu.modules.qualitymanage.purreportnew.entity.Purreportfundetail;
import com.hqu.modules.qualitymanage.purreportnew.entity.Purreportnewsn;
import com.hqu.modules.qualitymanage.purreportnew.entity.Purreportsizedetail;
import com.hqu.modules.qualitymanage.purreportnew.service.PurReportNewService;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;

/**
 * 来料不良主要现象Service
 * @author syc
 * @version 2018-08-24
 */
@Service
@Transactional(readOnly = true)
public class PhenomenontempService extends CrudService<PhenomenontempMapper, Phenomenontemp> {

	@Autowired
	private PurReportNewService purReportNewService;
	
	public Phenomenontemp get(String id) {
		return super.get(id);
	}
	
	public List<Phenomenontemp> findList(Phenomenontemp phenomenontemp) {
		return super.findList(phenomenontemp);
	}
	
	public Page<Phenomenontemp> findPage(Page<Phenomenontemp> page, Phenomenontemp phenomenontemp) {
		dataRuleFilter(phenomenontemp);
		phenomenontemp.setPage(page);
		Integer m = phenomenontemp.getM();
		Integer y = phenomenontemp.getY();
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
		
		//--------------------------------------------------
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
		//一张到货单对应多个问题
		Map<String,List<String>> newMap = new HashMap<>(); 
		for(String temp: map.keySet()){
			List<String> list2 = map.get(temp);
			List<String> tempList = new ArrayList<>();
			for(String te:list2){
				List<String> reportList = getReportList(te);
				for(String str :reportList){
					if(!tempList.contains(str)){
						tempList.add(str);
					}
				}
			}
			newMap.put(temp, tempList);
		}
		//问题：数量
		Map<List<String>,Integer> countMap = new HashMap<>();
		for(String temp :newMap.keySet()){
			List<String> list2 = newMap.get(temp);
			int mark = 0;
			for(List<String> tList : countMap.keySet()){
				
				if(isEqu(list2,tList)){
					Integer integer = countMap.get(tList);
					integer++;
					countMap.put(tList, integer);
					mark = 1;
				}
				
			}
			if(mark==0){
				countMap.put(list2, 1);
			}
		}
		
		
		//将map转成<String，int>
		Map<String,Integer> mapStr = new HashMap<String,Integer>();
		for(List<String> temp:countMap.keySet()){
			Integer integer = countMap.get(temp);
			String longStr = "";
			for(String str:temp){
				longStr=longStr+str+",";
			}
			mapStr.put(longStr, integer);
		}
		List<Phenomenontemp> findList = new ArrayList<Phenomenontemp>();
		int i =0;
		int total = 0;
		for(String temp:mapStr.keySet()){
			if(!"".equals(temp)){
				i=i+1;
				String sn = i+"";
				String phenomenon = temp;
				int sum = mapStr.get(temp);
				
				Phenomenontemp p = new Phenomenontemp();
				p.setSn(sn);
				p.setPhenomenon(phenomenon);
				p.setSum(sum);
			
				findList.add(p);
				total+=sum;
			}
		}
		Phenomenontemp p = new Phenomenontemp();
		++i;
		p.setSn("总计");
		p.setSum(total);
		findList.add(p);
		
		
		//--------------------------------------------------
//		Map<List<String>,Integer> map = new HashMap<List<String>,Integer>(); 
//		//对该list进行统计
//		List<PurReportNew> list = purReportNewService.findList(purReportNew);
//		Iterator<PurReportNew> it = list.iterator();
//		while(it.hasNext()){
//			PurReportNew purReportNewTemp = it.next();
//			purReportNewTemp = purReportNewService.get(purReportNewTemp.getId());
//			List<String> re = new ArrayList<>();
//			
//			List<Purreportfundetail> purreportfundetailList = purReportNewTemp.getPurreportfundetailList();
//			List<Purreportnewsn> purreportnewsnList = purReportNewTemp.getPurreportnewsnList();
//			List<Purreportsizedetail> purreportsizedetailList = purReportNewTemp.getPurreportsizedetailList();
//			for(Purreportfundetail temp:purreportfundetailList){
//				MatterType matterType = temp.getMatterType();
//				String mattername = matterType.getMattername();
//				re.add(mattername);
//			}
//			for(Purreportnewsn temp:purreportnewsnList){
//				MatterType matterType = temp.getMatterType();
//				String mattername = matterType.getMattername();
//				re.add(mattername);
//			}
//			for(Purreportsizedetail temp:purreportsizedetailList){
//				MatterType matterType = temp.getMatterType();
//				String mattername = matterType.getMattername();
//				re.add(mattername);
//			}
//			int mark = 0;
//			for(List<String> temp :map.keySet()){
//				boolean equ = isEqu(temp,re);
//				if(equ){
//					Integer a = map.get(temp);
//					a++;
//					map.put(temp, a);
//					mark=1;
//				}
//			}
//			if(mark==0){
//				map.put(re, 1);
//			}
//			
//		}
//		//将map转成<String，int>
//		Map<String,Integer> mapStr = new HashMap<String,Integer>();
//		for(List<String> temp:map.keySet()){
//			Integer integer = map.get(temp);
//			String longStr = "";
//			for(String str:temp){
//				longStr=longStr+str+",";
//			}
//			mapStr.put(longStr, integer);
//		}
//		List<Phenomenontemp> findList = new ArrayList<Phenomenontemp>();
//		int i =0;
//		int total = 0;
//		for(String temp:mapStr.keySet()){
//			i=i+1;
//			String sn = i+"";
//			String phenomenon = temp;
//			int sum = mapStr.get(temp);
//			total+=sum;
//			Phenomenontemp p = new Phenomenontemp();
//			p.setSn(sn);
//			p.setPhenomenon(phenomenon);
//			p.setSum(sum);
//			findList.add(p);
//		}
//		Phenomenontemp p = new Phenomenontemp();
//		++i;
//		p.setSn(""+i);
//		p.setSum(total);
//		findList.add(p);
		
		//List<Phenomenontemp> findList = mapper.findList(phenomenontemp);
		page.setList(findList);
		page.setCount(findList.size());
		return page;
	}
	//通过reportID获取问题的list
	public List<String> getReportList(String reportId){
		PurReportNew purReportNew = new PurReportNew();
		purReportNew.setReportId(reportId);
		purReportNew = purReportNewService.findList(purReportNew).get(0);
		purReportNew = purReportNewService.get(purReportNew.getId());
		
		List<String> re = new ArrayList<>();
		
		List<Purreportfundetail> purreportfundetailList = purReportNew.getPurreportfundetailList();
		List<Purreportnewsn> purreportnewsnList = purReportNew.getPurreportnewsnList();
		List<Purreportsizedetail> purreportsizedetailList = purReportNew.getPurreportsizedetailList();
		for(Purreportfundetail temp:purreportfundetailList){
			MatterType matterType = temp.getMatterType();
			String mattername = matterType.getMattername();
			re.add(mattername);
		}
		for(Purreportnewsn temp:purreportnewsnList){
			MatterType matterType = temp.getMatterType();
			String mattername = matterType.getMattername();
			re.add(mattername);
		}
		for(Purreportsizedetail temp:purreportsizedetailList){
			MatterType matterType = temp.getMatterType();
			String mattername = matterType.getMattername();
			re.add(mattername);
		}
		return re;
	}
	
	//若listA 中的element 与listB内容相同，返回true，否则返回false
	public boolean isEqu(List<String> listA ,List<String> listB ){
		if(listA.size()!= listB.size()){
			return false;
		}
		for(String tempA:listA){
			int flag = 1;
			for(String tempB:listB){
				if(tempA.equals(tempB)){
					flag = 0;
				}
			}
			if(flag==1){
				return false;
			}
		}
		return true;
	}
	
	@Transactional(readOnly = false)
	public void save(Phenomenontemp phenomenontemp) {
		super.save(phenomenontemp);
	}
	
	@Transactional(readOnly = false)
	public void delete(Phenomenontemp phenomenontemp) {
		super.delete(phenomenontemp);
	}
	
}