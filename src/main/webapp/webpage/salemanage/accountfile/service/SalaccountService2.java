/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.accountfile.service;

import java.util.List;

import com.hqu.modules.salemanage.accountfile.mapper.LinkManMapper2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.salemanage.accountfile.entity.Salaccount;
import com.hqu.modules.salemanage.accountfile.mapper.SalaccountMapper2;
import com.hqu.modules.salemanage.accountfile.entity.Contract;
import com.hqu.modules.salemanage.accountfile.mapper.ContractMapper2;
import com.hqu.modules.salemanage.accountfile.entity.LinkMan;
import com.hqu.modules.salemanage.accountfile.mapper.LinkManMapper2;
import com.hqu.modules.salemanage.accountfile.entity.PickBill;
import com.hqu.modules.salemanage.accountfile.mapper.PickBillMapper2;

/**
 * 客户档案维护Service
 * @author hzb
 * @version 2018-05-15
 */
@Service
@Transactional(readOnly = true)
public class SalaccountService2 extends CrudService<SalaccountMapper2, Salaccount> {

	@Autowired
	private ContractMapper2 contractMapper2;
	@Autowired
	private LinkManMapper2 linkManMapper2;
	@Autowired
	private PickBillMapper2 pickBillMapper2;
	
	public Salaccount get(String id) {
		Salaccount salaccount = super.get(id);
		salaccount.setContractList(contractMapper2.findList(new Contract(salaccount)));
		salaccount.setLinkManList(linkManMapper2.findList(new LinkMan(salaccount)));
		salaccount.setPickBillList(pickBillMapper2.findList(new PickBill(salaccount)));
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
		for (Contract contract : salaccount.getContractList()){
			if (contract.getId() == null){
				continue;
			}
			if (Contract.DEL_FLAG_NORMAL.equals(contract.getDelFlag())){
				if (StringUtils.isBlank(contract.getId())){
					contract.setAccount(salaccount);
					contract.preInsert();
					contractMapper2.insert(contract);
				}else{
					contract.preUpdate();
					contractMapper2.update(contract);
				}
			}else{
				contractMapper2.delete(contract);
			}
		}
		for (LinkMan linkMan : salaccount.getLinkManList()){
			if (linkMan.getId() == null){
				continue;
			}
			if (LinkMan.DEL_FLAG_NORMAL.equals(linkMan.getDelFlag())){
				if (StringUtils.isBlank(linkMan.getId())){
					linkMan.setAccountCode(salaccount);
					linkMan.preInsert();
					linkManMapper2.insert(linkMan);
				}else{
					linkMan.preUpdate();
					linkManMapper2.update(linkMan);
				}
			}else{
				linkManMapper2.delete(linkMan);
			}
		}
		for (PickBill pickBill : salaccount.getPickBillList()){
			if (pickBill.getId() == null){
				continue;
			}
			if (PickBill.DEL_FLAG_NORMAL.equals(pickBill.getDelFlag())){
				if (StringUtils.isBlank(pickBill.getId())){
					pickBill.setRcvAccount(salaccount);
					pickBill.preInsert();
					pickBillMapper2.insert(pickBill);
				}else{
					pickBill.preUpdate();
					pickBillMapper2.update(pickBill);
				}
			}else{
				pickBillMapper2.delete(pickBill);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(Salaccount salaccount) {
		super.delete(salaccount);
		contractMapper2.delete(new Contract(salaccount));
		linkManMapper2.delete(new LinkMan(salaccount));
		pickBillMapper2.delete(new PickBill(salaccount));
	}
	
}