/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.salcontract.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.salemanage.salcontract.entity.Contract;
import com.hqu.modules.salemanage.salcontract.mapper.ContractMapper;
import com.hqu.modules.salemanage.salcontract.entity.CtrFIle;
import com.hqu.modules.salemanage.salcontract.mapper.CtrFIleMapper;
import com.hqu.modules.salemanage.salcontract.entity.CtrItem;
import com.hqu.modules.salemanage.salcontract.mapper.CtrItemMapper;

/**
 * 销售合同Service
 * @author dongqida
 * @version 2018-05-12
 */
@Service
@Transactional(readOnly = true)
public class ContractService extends CrudService<ContractMapper, Contract> {

	@Autowired
	private CtrFIleMapper ctrFIleMapper;
	@Autowired
	private CtrItemMapper ctrItemMapper;
	
	public Contract get(String id) {
		Contract contract = super.get(id);
		contract.setCtrFIleList(ctrFIleMapper.findList(new CtrFIle(contract)));
		contract.setCtrItemList(ctrItemMapper.findList(new CtrItem(contract)));
		return contract;
	}
	public Contract getByCode(String id) {
		Contract contract = mapper.getByCode(id);
		contract.setCtrFIleList(ctrFIleMapper.findList(new CtrFIle(contract)));
		contract.setCtrItemList(ctrItemMapper.findList(new CtrItem(contract)));
		return contract;
	}
	
	public List<Contract> findList(Contract contract) {
		return super.findList(contract);
	}
	
	public Page<Contract> findPage(Page<Contract> page, Contract contract) {
		return super.findPage(page, contract);
	}
	
	@Transactional(readOnly = false)
	public void save(Contract contract) {
		super.save(contract);
		for (CtrFIle ctrFIle : contract.getCtrFIleList()){
			if (ctrFIle.getId() == null){
				continue;
			}
			if (CtrFIle.DEL_FLAG_NORMAL.equals(ctrFIle.getDelFlag())){
				if (StringUtils.isBlank(ctrFIle.getId())){
					ctrFIle.setContract(contract);
					ctrFIle.preInsert();
					ctrFIleMapper.insert(ctrFIle);
				}else{
					ctrFIle.preUpdate();
					ctrFIleMapper.update(ctrFIle);
				}
			}else{
				ctrFIleMapper.delete(ctrFIle);
			}
		}
		for (CtrItem ctrItem : contract.getCtrItemList()){
			if (ctrItem.getId() == null){
				continue;
			}
			if (CtrItem.DEL_FLAG_NORMAL.equals(ctrItem.getDelFlag())){
				if (StringUtils.isBlank(ctrItem.getId())){
					ctrItem.setContract(contract);
					ctrItem.preInsert();
					ctrItemMapper.insert(ctrItem);
				}else{
					ctrItem.preUpdate();
					ctrItemMapper.update(ctrItem);
				}
			}else{
				ctrItemMapper.delete(ctrItem);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(Contract contract) {
		super.delete(contract);
		ctrFIleMapper.delete(new CtrFIle(contract));
		ctrItemMapper.delete(new CtrItem(contract));
	}
	public 	String getMaxIdByTypeAndDate(String para){
	    return mapper.getMaxIdByTypeAndDate(para);
    }
	@Transactional(readOnly = false)
	public Boolean updateStat(Map<String, Object>map) {
		return mapper.updateStat(map);
	}
	@Transactional(readOnly = false)
	public Set<String> findByProdCode(String prodCode){
		return ctrItemMapper.findByProdCode(prodCode);
	}
	@Transactional(readOnly = false)
	public Boolean updateSuggestions(Map<String, Object>map) {
		return mapper.updateSuggestions(map);
	}
}