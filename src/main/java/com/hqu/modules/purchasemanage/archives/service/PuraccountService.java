/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.archives.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.purchasemanage.archives.entity.Puraccount;
import com.hqu.modules.purchasemanage.archives.mapper.PuraccountMapper;
import com.hqu.modules.purchasemanage.archives.entity.PurContractMain;
import com.hqu.modules.purchasemanage.archives.mapper.PurContractMainMapper;
import com.hqu.modules.purchasemanage.archives.entity.PinvCheckMain;
import com.hqu.modules.purchasemanage.archives.mapper.PinvCheckMainMapper;
import com.hqu.modules.purchasemanage.archives.entity.PurLinkMan;
import com.hqu.modules.purchasemanage.archives.mapper.PurLinkManMapper;

/**
 * 供应商档案Service
 * @author luyumiao
 * @version 2018-05-01
 */
@Service
@Transactional(readOnly = true)
public class PuraccountService extends CrudService<PuraccountMapper, Puraccount> {

	@Autowired
	private PurContractMainMapper purContractMainMapper;
	@Autowired
	private PinvCheckMainMapper pinvCheckMainMapper;
	@Autowired
	private PurLinkManMapper purLinkManMapper;
	
	public Puraccount get(String id) {
		Puraccount puraccount = super.get(id);
		puraccount.setPurContractMainList(purContractMainMapper.findList(new PurContractMain(puraccount)));
		puraccount.setPinvCheckMainList(pinvCheckMainMapper.findList(new PinvCheckMain(puraccount)));
		puraccount.setPurLinkManList(purLinkManMapper.findList(new PurLinkMan(puraccount)));
		return puraccount;
	}
	
	public List<Puraccount> findList(Puraccount puraccount) {
		return super.findList(puraccount);
	}
	
	public Page<Puraccount> findPage(Page<Puraccount> page, Puraccount puraccount) {
		return super.findPage(page, puraccount);
	}
	
	@Transactional(readOnly = false)
	public void save(Puraccount puraccount) {
		super.save(puraccount);
		for (PurContractMain purContractMain : puraccount.getPurContractMainList()){
			if (purContractMain.getId() == null){
				continue;
			}
			if (PurContractMain.DEL_FLAG_NORMAL.equals(purContractMain.getDelFlag())){
				if (StringUtils.isBlank(purContractMain.getId())){
					purContractMain.setPuraccount(puraccount);
					purContractMain.preInsert();
					purContractMainMapper.insert(purContractMain);
				}else{
					purContractMain.preUpdate();
					purContractMainMapper.update(purContractMain);
				}
			}else{
				purContractMainMapper.delete(purContractMain);
			}
		}
		for (PinvCheckMain pinvCheckMain : puraccount.getPinvCheckMainList()){
			if (pinvCheckMain.getId() == null){
				continue;
			}
			if (PinvCheckMain.DEL_FLAG_NORMAL.equals(pinvCheckMain.getDelFlag())){
				if (StringUtils.isBlank(pinvCheckMain.getId())){
					pinvCheckMain.setPuraccount(puraccount);
					pinvCheckMain.preInsert();
					pinvCheckMainMapper.insert(pinvCheckMain);
				}else{
					pinvCheckMain.preUpdate();
					pinvCheckMainMapper.update(pinvCheckMain);
				}
			}else{
				pinvCheckMainMapper.delete(pinvCheckMain);
			}
		}
		for (PurLinkMan purLinkMan : puraccount.getPurLinkManList()){
			if (purLinkMan.getId() == null){
				continue;
			}
			if (PurLinkMan.DEL_FLAG_NORMAL.equals(purLinkMan.getDelFlag())){
				if (StringUtils.isBlank(purLinkMan.getId())){
					purLinkMan.setPuraccount(puraccount);
					purLinkMan.preInsert();
					purLinkManMapper.insert(purLinkMan);
				}else{
					purLinkMan.preUpdate();
					purLinkManMapper.update(purLinkMan);
				}
			}else{
				purLinkManMapper.delete(purLinkMan);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(Puraccount puraccount) {
		super.delete(puraccount);
		purContractMainMapper.delete(new PurContractMain(puraccount));
		pinvCheckMainMapper.delete(new PinvCheckMain(puraccount));
		purLinkManMapper.delete(new PurLinkMan(puraccount));
	}
	
}