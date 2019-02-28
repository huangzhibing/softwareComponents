/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.routing.service;

import java.util.List;

import com.hqu.modules.basedata.item.service.ItemService;
import com.hqu.modules.basedata.product.entity.Product;
import com.hqu.modules.basedata.product.service.ProductService;
import com.hqu.modules.basedata.routing.entity.RoutingItem;
import com.hqu.modules.basedata.routing.mapper.RoutingItemMapper;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.service.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.basedata.routing.entity.Routing;
import com.hqu.modules.basedata.routing.mapper.RoutingMapper;
import com.hqu.modules.basedata.routing.entity.RoutingWork;
import com.hqu.modules.basedata.routing.mapper.RoutingWorkMapper;

/**
 * 工艺路线Service
 * @author huang.youcai
 * @version 2018-06-02
 */
@Service
@Transactional(readOnly = true)
public class RoutingService extends CrudService<RoutingMapper, Routing> {

	@Autowired
	private RoutingItemMapper routingItemMapper;
	@Autowired
	private RoutingWorkMapper routingWorkMapper;
	@Autowired
	private ProductService productService;
	@Autowired
	private OfficeService officeService;
	public Routing get(String id) {
		Routing routing = super.get(id);
		routing.setRoutingWorkList(routingWorkMapper.findList(new RoutingWork(routing)));
		routing.setRoutingItemList(routingItemMapper.findList(new RoutingItem(routing)));
		return routing;
	}
	
	public List<Routing> findList(Routing routing) {
		return super.findList(routing);
	}
	
	public Page<Routing> findPage(Page<Routing> page, Routing routing) {
		return super.findPage(page, routing);
	}
	
	@Transactional(readOnly = false)
	public void save(Routing routing) {
		if(routing.getProduct().getItem().getCode().length() == 32){
			Product product = productService.get(routing.getProduct().getItem().getCode());
			routing.setProduct(product);
		}
		if(routing.getOffice().getCode().length() == 32){
			Office office = officeService.get(routing.getOffice().getCode());
			routing.setOffice(office);
		}
		super.save(routing);
		for (RoutingItem routingItem : routing.getRoutingItemList()){
			routingItem.setProductCode(routing.getProduct().getItem().getCode());
			if (routingItem.getId() == null){
				continue;
			}
			if (RoutingItem.DEL_FLAG_NORMAL.equals(routingItem.getDelFlag())){
				if (StringUtils.isBlank(routingItem.getId())){
					routingItem.setRoutingCode(routing);
					routingItem.preInsert();
					routingItemMapper.insert(routingItem);
				}else{
					routingItem.preUpdate();
					routingItemMapper.update(routingItem);
				}
			}else{
				routingItemMapper.delete(routingItem);
			}
		}
		for (RoutingWork routingWork : routing.getRoutingWorkList()){
			if (routingWork.getId() == null){
				continue;
			}
			if (RoutingWork.DEL_FLAG_NORMAL.equals(routingWork.getDelFlag())){
				if (StringUtils.isBlank(routingWork.getId())){
					routingWork.setRoutingCode(routing);
					routingWork.preInsert();
					routingWorkMapper.insert(routingWork);
				}else{
					routingWork.preUpdate();
					routingWorkMapper.update(routingWork);
				}
			}else{
				routingWorkMapper.delete(routingWork);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(Routing routing) {
		super.delete(routing);
		routingWorkMapper.delete(new RoutingWork(routing));
		routingItemMapper.delete(new RoutingItem(routing));
	}

	public String getMaxRoutingCode(){
		return mapper.getMaxRoutingCode();
	}
	public List findListByCode(Routing routing){ return  mapper.findListByCode(routing);}
}