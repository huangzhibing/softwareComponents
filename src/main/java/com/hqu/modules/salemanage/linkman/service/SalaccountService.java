/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.linkman.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.salemanage.linkman.entity.Salaccount;
import com.hqu.modules.salemanage.linkman.mapper.SalaccountMapper;
import com.hqu.modules.salemanage.linkman.entity.SalLinkMan;
import com.hqu.modules.salemanage.linkman.mapper.SalLinkManMapper;

/**
 * 联系人维护Service
 * @author hzm
 * @version 2018-05-13
 */
@Service
@Transactional(readOnly = true)
public class SalaccountService extends CrudService<SalaccountMapper, Salaccount> {

	@Autowired
	private SalLinkManMapper linkManMapper;
	
	public Salaccount get(String id) {
		Salaccount salaccount = super.get(id);
		salaccount.setLinkManList(linkManMapper.findList(new SalLinkMan(salaccount)));
		return salaccount;
	}
	
	public List<Salaccount> findList(Salaccount salaccount) {
		return super.findList(salaccount);
	}
	
	public Page<Salaccount> findPage(Page<Salaccount> page, Salaccount salaccount) {
		return super.findPage(page, salaccount);
	}
	
	@Transactional(readOnly = false)
	public void save(Salaccount salaccount) {
		super.save(salaccount);
		for (SalLinkMan linkMan : salaccount.getLinkManList()){
			if (linkMan.getId() == null){
				continue;
			}
			if (SalLinkMan.DEL_FLAG_NORMAL.equals(linkMan.getDelFlag())){
				if (StringUtils.isBlank(linkMan.getId())){
					linkMan.setAccountCode(salaccount);
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
	public void delete(Salaccount salaccount) {
		super.delete(salaccount);
		linkManMapper.delete(new SalLinkMan(salaccount));
	}
	
}