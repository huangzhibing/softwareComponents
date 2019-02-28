/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.invcheckmain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.inventorymanage.invcheckmain.entity.ReinvCheckMain;
import com.hqu.modules.inventorymanage.invcheckmain.mapper.ReinvCheckMainMapper;
import com.hqu.modules.inventorymanage.invcheckmain.entity.ReInvCheckDetail;
import com.hqu.modules.inventorymanage.invcheckmain.mapper.ReInvCheckDetailMapper;

/**
 * 超期复检主表Service
 * @author syc代建
 * @version 2018-05-28
 */
@Service
@Transactional(readOnly = true)
public class ReinvCheckMainService extends CrudService<ReinvCheckMainMapper, ReinvCheckMain> {

	@Autowired
	private ReInvCheckDetailMapper reInvCheckDetailMapper;
	
	public ReinvCheckMain get(String id) {
		ReinvCheckMain reinvCheckMain = super.get(id);
		reinvCheckMain.setReInvCheckDetailList(reInvCheckDetailMapper.findList(new ReInvCheckDetail(reinvCheckMain)));
		return reinvCheckMain;
	}
	
	public List<ReinvCheckMain> findList(ReinvCheckMain reinvCheckMain) {
		return super.findList(reinvCheckMain);
	}
	
	public Page<ReinvCheckMain> findPage(Page<ReinvCheckMain> page, ReinvCheckMain reinvCheckMain) {
		return super.findPage(page, reinvCheckMain);
	}
	
	@Transactional(readOnly = false)
	public void save(ReinvCheckMain reinvCheckMain) {
		super.save(reinvCheckMain);
		for (ReInvCheckDetail reInvCheckDetail : reinvCheckMain.getReInvCheckDetailList()){
			if (reInvCheckDetail.getId() == null){
				continue;
			}
			if (ReInvCheckDetail.DEL_FLAG_NORMAL.equals(reInvCheckDetail.getDelFlag())){
				if (StringUtils.isBlank(reInvCheckDetail.getId())){
					reInvCheckDetail.setReInvCheckMain(reinvCheckMain);
					reInvCheckDetail.preInsert();
					reInvCheckDetailMapper.insert(reInvCheckDetail);
				}else{
					reInvCheckDetail.preUpdate();
					reInvCheckDetailMapper.update(reInvCheckDetail);
				}
			}else{
				reInvCheckDetailMapper.delete(reInvCheckDetail);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(ReinvCheckMain reinvCheckMain) {
		super.delete(reinvCheckMain);
		reInvCheckDetailMapper.delete(new ReInvCheckDetail(reinvCheckMain));
	}
	
}