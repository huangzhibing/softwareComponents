/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.supprice.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.purchasemanage.supprice.entity.PurSupPrice;
import com.hqu.modules.purchasemanage.supprice.mapper.PurSupPriceMapper;
import com.hqu.modules.purchasemanage.supprice.entity.PurSupPriceDetail;
import com.hqu.modules.purchasemanage.supprice.mapper.PurSupPriceDetailMapper;

/**
 * 供应商价格维护Service
 * @author syc
 * @version 2018-08-02
 */
@Service
@Transactional(readOnly = true)
public class PurSupPriceService extends CrudService<PurSupPriceMapper, PurSupPrice> {

	@Autowired
	private PurSupPriceDetailMapper purSupPriceDetailMapper;
	
	public PurSupPrice get(String id) {
		PurSupPrice purSupPrice = super.get(id);
		purSupPrice.setPurSupPriceDetailList(purSupPriceDetailMapper.findList(new PurSupPriceDetail(purSupPrice)));
		return purSupPrice;
	}
	
	public List<PurSupPrice> findList(PurSupPrice purSupPrice) {
		
		return super.findList(purSupPrice);
	}
	
	public Page<PurSupPrice> findPage(Page<PurSupPrice> page, PurSupPrice purSupPrice) {
		dataRuleFilter(purSupPrice);
		purSupPrice.setPage(page);
		
		/*
		String itemName = purSupPrice.getItemName();
		Item item = purSupPrice.getItem();
		List<PurSupPriceDetail> purSupPriceDetailList = new ArrayList<PurSupPriceDetail>();
		PurSupPriceDetail purSupPriceDetail = new PurSupPriceDetail();
		purSupPriceDetail.setItem(item);
		purSupPriceDetail.setItemName(itemName);
		purSupPriceDetailList.add(purSupPriceDetail);
		List<PurSupPrice> resultList = new ArrayList<PurSupPrice>();
		purSupPrice.setPurSupPriceDetailList(purSupPriceDetailList);
		List<PurSupPrice> findList = findList(purSupPrice);
		Iterator<PurSupPrice> it = findList.iterator();
		
		
		
		it = findList.iterator();
		resultList = new ArrayList<>();
		if(item!=null&&!"".equals(item.getCode())){
			while(it.hasNext()){
				PurSupPrice psp = it.next();
				psp = get(psp.getId());
				List<PurSupPriceDetail> purSupPriceDetailList2 = psp.getPurSupPriceDetailList();
				for(PurSupPriceDetail temp:purSupPriceDetailList2){
					String code = item.getCode();
					if(code.equals(temp.getItem().getCode())){
						resultList.add(psp);
						break;
					}
				}
				
			}
			findList = resultList;
		}
		
		
		it = findList.iterator();
		resultList = new ArrayList<>();
		if(!"".equals(itemName)){
			while(it.hasNext()){
				PurSupPrice psp = it.next();
				psp = get(psp.getId());
				List<PurSupPriceDetail> purSupPriceDetailList2 = psp.getPurSupPriceDetailList();
				for(PurSupPriceDetail temp:purSupPriceDetailList2){
					//String code = item.getCode();
					if(itemName.equals(temp.getItemName())){
						resultList.add(psp);
						break;
					}
				}
				
			}
			findList = resultList;
		}
		
		it = findList.iterator();
		resultList = new ArrayList<>();
		if(purSupPrice.getDate()!=null){
			while(it.hasNext()){
				PurSupPrice next = it.next();
				next = get(next.getId());
				List<PurSupPriceDetail> purSupPriceDetailList2 = next.getPurSupPriceDetailList();
				for(PurSupPriceDetail temp:purSupPriceDetailList2){
					
						long midTime = purSupPrice.getDate().getTime();
						long startTime = temp.getStartDate().getTime();
						long endTime = temp.getEndDate().getTime();
						if(midTime>=startTime&&midTime<=endTime){
							resultList.add(next);
							break;
						}
					
				}
			}
			findList = resultList;
		}*/
		//List<PurSupPrice> findList = mapper.findList(purSupPrice);
		page.setList(ft(purSupPrice));
		return page;
	}
	public List<PurSupPrice> ft(PurSupPrice purSupPrice){
		String itemName = purSupPrice.getItemName();
		Item item = purSupPrice.getItem();
		Date date = purSupPrice.getDate();
		List<PurSupPrice> findList = findList(purSupPrice);
		List<PurSupPrice> resultList = new ArrayList<>();
		if(!"".equals(itemName)||!"".equals(item.getCode())||date!=null){
			for(PurSupPrice temp:findList){
				temp = get(temp.getId());
				List<PurSupPriceDetail> purSupPriceDetailList = temp.getPurSupPriceDetailList();
				for(PurSupPriceDetail pspd:purSupPriceDetailList){
					if(item!=null&&!"".equals(item.getCode())){
						if(!item.getCode().equals(pspd.getItem().getCode())){
							continue;
						}
					}
					
					if(!"".equals(itemName)){
						if(!itemName.equals(pspd.getItemName())){
							continue;
						}
					}
					
					if(date!=null){
						long midTime = date.getTime();
						long startTime = pspd.getStartDate().getTime();
						long endTime = pspd.getEndDate().getTime();
						if(midTime<startTime||midTime>endTime){
							continue;
						}
					}
					if(!resultList.contains(temp)){
						resultList.add(temp);
					}
					
				}
			}
			findList = resultList;
		}
		return findList;
	}
	
	
	
	
	@Transactional(readOnly = false)
	public void save(PurSupPrice purSupPrice) {
		super.save(purSupPrice);
		for (PurSupPriceDetail purSupPriceDetail : purSupPrice.getPurSupPriceDetailList()){
			if (purSupPriceDetail.getId() == null){
				continue;
			}
			if (PurSupPriceDetail.DEL_FLAG_NORMAL.equals(purSupPriceDetail.getDelFlag())){
				if (StringUtils.isBlank(purSupPriceDetail.getId())){
					purSupPriceDetail.setAccount(purSupPrice);
					purSupPriceDetail.preInsert();
					purSupPriceDetailMapper.insert(purSupPriceDetail);
				}else{
					purSupPriceDetail.preUpdate();
					purSupPriceDetailMapper.update(purSupPriceDetail);
				}
			}else{
				purSupPriceDetailMapper.delete(purSupPriceDetail);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(PurSupPrice purSupPrice) {
		super.delete(purSupPrice);
		purSupPriceDetailMapper.delete(new PurSupPriceDetail(purSupPrice));
	}
	
}