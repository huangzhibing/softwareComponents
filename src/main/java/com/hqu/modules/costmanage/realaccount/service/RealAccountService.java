/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.realaccount.service;

import java.util.List;

import com.hqu.modules.costmanage.personwage.entity.PersonWage;
import com.hqu.modules.costmanage.personwage.mapper.PersonWageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.costmanage.realaccount.entity.RealAccount;
import com.hqu.modules.costmanage.realaccount.mapper.RealAccountMapper;
import com.hqu.modules.costmanage.realaccount.entity.RealAccountDetail;
import com.hqu.modules.costmanage.realaccount.mapper.RealAccountDetailMapper;

/**
 * 材料凭证单据管理Service
 * @author hzb
 * @version 2018-09-05
 */
@Service
@Transactional(readOnly = true)
public class RealAccountService extends CrudService<RealAccountMapper, RealAccount> {

	@Autowired
	private RealAccountDetailMapper realAccountDetailMapper;
	@Autowired
	private RealAccountMapper realAccountMapper;
	@Autowired
	private PersonWageMapper personWageMapper;

	public RealAccount get(String id) {
		RealAccount realAccount = super.get(id);
		realAccount.setRealAccountDetailList(realAccountDetailMapper.findList(new RealAccountDetail(realAccount)));
		realAccount.setPersonWageList(personWageMapper.getPersonWageList(realAccount.getBillNum()));
		return realAccount;
	}
	
	public List<RealAccount> findList(RealAccount realAccount) {
		return super.findList(realAccount);
	}
	
	public Page<RealAccount> findPage(Page<RealAccount> page, RealAccount realAccount) {
		return super.findPage(page, realAccount);
	}
	
	@Transactional(readOnly = false)
	public void save(RealAccount realAccount) {
		super.save(realAccount);
		for (RealAccountDetail realAccountDetail : realAccount.getRealAccountDetailList()){
			if (realAccountDetail.getId() == null){
				continue;
			}
			if (RealAccountDetail.DEL_FLAG_NORMAL.equals(realAccountDetail.getDelFlag())){
				if (StringUtils.isBlank(realAccountDetail.getId())){
					realAccountDetail.setBillNumId(realAccount);
					realAccountDetail.preInsert();
					realAccountDetailMapper.insert(realAccountDetail);
				}else{
					realAccountDetail.preUpdate();
					realAccountDetailMapper.update(realAccountDetail);
				}
			}else{
				realAccountDetailMapper.delete(realAccountDetail);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(RealAccount realAccount) {
		super.delete(realAccount);
		realAccountDetailMapper.delete(new RealAccountDetail(realAccount));
	}

	public String getBillNum() {
		String billNum = realAccountMapper.getMaxBillNum();
		return billNum;
	}

	public Page<RealAccount> findAccountingPage(Page<RealAccount> page, RealAccount realAccount) {
		realAccount.setPage(page);
		page.setList(realAccountMapper.findAccountingList(realAccount));
		return page;
	}
	
	public String getCLCBBillNum() {
		String billNum = realAccountMapper.getMaxCLCBBillNum();
		return billNum;
	}

	public String getRGCBBillNum() {
		String billNum = realAccountMapper.getMaxRGCBBillNum();
		return billNum;
	}
	
}