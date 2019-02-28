/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.linkman.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.purchasemanage.linkman.entity.Paccount;
import com.hqu.modules.purchasemanage.linkman.mapper.PaccountMapper;
import com.hqu.modules.purchasemanage.linkman.entity.LinkMan;
import com.hqu.modules.purchasemanage.linkman.mapper.LinkManMapper;

/**
 * 供应商联系人Service
 * @author luyumiao
 * @version 2018-04-15
 */
@Service
@Transactional(readOnly = true)
public class PaccountService extends CrudService<PaccountMapper, Paccount> {

	@Autowired
	private LinkManMapper linkManMapper;
	
	public Paccount get(String id) {
		Paccount paccount = super.get(id);
		paccount.setLinkManList(linkManMapper.findList(new LinkMan(paccount)));
		return paccount;
	}
	
	public List<Paccount> findList(Paccount paccount) {
		return super.findList(paccount);
	}
	
	public Page<Paccount> findPage(Page<Paccount> page, Paccount paccount) {
		return super.findPage(page, paccount);
	}
	
	@Transactional(readOnly = false)
	public void save(Paccount paccount) {
		super.save(paccount);
		for (LinkMan linkMan : paccount.getLinkManList()){
			if (linkMan.getId() == null){
				continue;
			}
			if (LinkMan.DEL_FLAG_NORMAL.equals(linkMan.getDelFlag())){
				if (StringUtils.isBlank(linkMan.getId())){
					linkMan.setMdmAccount(paccount);
					linkMan.preInsert();
					linkManMapper.insert(linkMan);
				}else{
					linkMan.preUpdate();
					linkManMapper.update(linkMan);
				}
			}else{
				linkManMapper.delete(linkMan);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(Paccount paccount) {
		super.delete(paccount);
		linkManMapper.delete(new LinkMan(paccount));
	}
	
	public String getMaxLinkCode(){
		return linkManMapper.getMaxLinkCode();
	}
	
}