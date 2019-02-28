/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.warehousemaintenance.countdata.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.inventorymanage.warehousemaintenance.countdata.entity.CountMain;
import com.hqu.modules.inventorymanage.warehousemaintenance.countdata.mapper.CountMainMapper;
import com.hqu.modules.inventorymanage.warehousemaintenance.countdata.entity.CountDetail;
import com.hqu.modules.inventorymanage.warehousemaintenance.countdata.mapper.CountDetailMapper;

/**
 * 盘点数据录入Service
 * @author zb
 * @version 2018-04-23
 */
@Service
@Transactional(readOnly = true)
public class CountMainService extends CrudService<CountMainMapper, CountMain> {

	@Autowired
	private CountDetailMapper countDetailMapper;
	
	public CountMain get(String id) {
		CountMain countMain = super.get(id);
		countMain.setCountDetailList(countDetailMapper.findList(new CountDetail(countMain)));
		return countMain;
	}
	
	public List<CountMain> findList(CountMain countMain) {
		return super.findList(countMain);
	}
	
	public Page<CountMain> findPage(Page<CountMain> page, CountMain countMain) {
		return super.findPage(page, countMain);
	}
	
	@Transactional(readOnly = false)
	public void save(CountMain countMain) {
		super.save(countMain);
		for (CountDetail countDetail : countMain.getCountDetailList()){
			if (countDetail.getId() == null){
				continue;
			}
			if (CountDetail.DEL_FLAG_NORMAL.equals(countDetail.getDelFlag())){
				if (StringUtils.isBlank(countDetail.getId())){
					countDetail.setCountMain(countMain);
					countDetail.preInsert();
					countDetailMapper.insert(countDetail);
				}else{
					countDetail.preUpdate();
					countDetailMapper.update(countDetail);
				}
			}else{
				countDetailMapper.delete(countDetail);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(CountMain countMain) {
		super.delete(countMain);
		countDetailMapper.delete(new CountDetail(countMain));
	}
    public String getMaxIdByTypeAndDate(String para){
        return mapper.getMaxIdByTypeAndDate(para);
    }
}