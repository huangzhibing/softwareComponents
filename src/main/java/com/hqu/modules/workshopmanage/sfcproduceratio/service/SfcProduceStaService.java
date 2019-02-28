/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.sfcproduceratio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.workshopmanage.sfcproduceratio.entity.SfcProduceSta;
import com.hqu.modules.workshopmanage.sfcproduceratio.mapper.SfcProduceStaMapper;

/**
 * 车间产出量统计Service
 * @author yxb
 * @version 2018-11-25
 */
@Service
@Transactional(readOnly = true)
public class SfcProduceStaService extends CrudService<SfcProduceStaMapper, SfcProduceSta> {

	@Autowired
	private SfcProduceStaMapper sfcProduceStaMapper;

	public SfcProduceSta get(String id) {
		return super.get(id);
	}
	
	public List<SfcProduceSta> findList(SfcProduceSta sfcProduceSta) {
		return super.findList(sfcProduceSta);
	}
	
	public Page<SfcProduceSta> findPage(Page<SfcProduceSta> page, SfcProduceSta sfcProduceSta) {
		/**
		 * 各工位的统计信息分两次sql查询得到，第一次统计指定时间段每个工位的产出量，第二次统计指定时间段每个工位生产的产品的异常信息
		 */
		Page<SfcProduceSta> sfcProduceStaPage =super.findPage(page, sfcProduceSta);
		List<SfcProduceSta>sfcProduceFails = sfcProduceStaMapper.findFailList(sfcProduceSta);
		//把每个工位的异常数量和项目的记录拼接到集合里
		for(SfcProduceSta sfcProduceStaLoop : sfcProduceStaPage.getList()){
			for(SfcProduceSta sfcProduceFail : sfcProduceFails){
				if(sfcProduceStaLoop.getNo().equals(sfcProduceFail.getNo())){
					sfcProduceStaLoop.setFailNum(sfcProduceFail.getFailNum());
					sfcProduceStaLoop.setFailPro(sfcProduceFail.getFailPro());
					break;
				}
			}
		}
		return super.findPage(page, sfcProduceSta);
	}
	
	@Transactional(readOnly = false)
	public void save(SfcProduceSta sfcProduceSta) {
		super.save(sfcProduceSta);
	}
	
	@Transactional(readOnly = false)
	public void delete(SfcProduceSta sfcProduceSta) {
		super.delete(sfcProduceSta);
	}
	
}