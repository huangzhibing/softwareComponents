/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.salorder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.basedata.product.entity.Product;
import com.hqu.modules.salemanage.salorder.entity.SalOrder;
import com.hqu.modules.salemanage.salorder.mapper.SalOrderMapper;
import com.hqu.modules.salemanage.salorder.entity.SalOrderAudit;
import com.hqu.modules.salemanage.salorder.mapper.SalOrderAuditMapper;
import com.hqu.modules.salemanage.salorder.entity.SalOrderItem;
import com.hqu.modules.salemanage.salorder.mapper.SalOrderItemMapper;

/**
 * 内部订单Service
 * @author dongqida
 * @version 2018-05-27
 */
@Service
@Transactional(readOnly = true)
public class SalOrderService extends CrudService<SalOrderMapper, SalOrder> {

	@Autowired
	private SalOrderAuditMapper salOrderAuditMapper;
	@Autowired
	private SalOrderItemMapper salOrderItemMapper;
	
	public SalOrder get(String id) {
		SalOrder salOrder = super.get(id);
		salOrder.setSalOrderAuditList(salOrderAuditMapper.findList(new SalOrderAudit(salOrder)));
		salOrder.setSalOrderItemList(salOrderItemMapper.findList(new SalOrderItem(salOrder)));
		return salOrder;
	}
	
	public List<SalOrder> findList(SalOrder salOrder) {
		return super.findList(salOrder);
	}
	
	public Page<SalOrder> findPage(Page<SalOrder> page, SalOrder salOrder) {
		return super.findPage(page, salOrder);
	}
	
	@Transactional(readOnly = false)
	public void save(SalOrder salOrder) {
		super.save(salOrder);
		for (SalOrderAudit salOrderAudit : salOrder.getSalOrderAuditList()){
			if (salOrderAudit.getId() == null){
				continue;
			}
			if (SalOrderAudit.DEL_FLAG_NORMAL.equals(salOrderAudit.getDelFlag())){
				if (StringUtils.isBlank(salOrderAudit.getId())){
					salOrderAudit.setOrderCode(salOrder);
					salOrderAudit.preInsert();
					salOrderAuditMapper.insert(salOrderAudit);
				}else{
					salOrderAudit.preUpdate();
					salOrderAuditMapper.update(salOrderAudit);
				}
			}else{
				salOrderAuditMapper.delete(salOrderAudit);
			}
		}
		for (SalOrderItem salOrderItem : salOrder.getSalOrderItemList()){
			if (salOrderItem.getId() == null){
				continue;
			}
			if (SalOrderItem.DEL_FLAG_NORMAL.equals(salOrderItem.getDelFlag())){
				if (StringUtils.isBlank(salOrderItem.getId())){
					logger.debug("orderId="+salOrder.getId());
					salOrderItem.setOrderCode(salOrder);
					salOrderItem.preInsert();
					salOrderItemMapper.insert(salOrderItem);
				}else{
					salOrderItem.preUpdate();
					salOrderItemMapper.update(salOrderItem);
				}
			}else{
				salOrderItemMapper.delete(salOrderItem);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(SalOrder salOrder) {
		super.delete(salOrder);
		salOrderAuditMapper.delete(new SalOrderAudit(salOrder));
		salOrderItemMapper.delete(new SalOrderItem(salOrder));
	}
	public 	String getMaxIdByTypeAndDate(String para){
	    return mapper.getMaxIdByTypeAndDate(para);
    }
	
	public List<String> findProductList(Product product){
		return mapper.findProductList(product);
	}

	public List<SalOrderItem> selectPartCode(List<String> proCode){
		return mapper.selectPartCode(proCode);
	}
}