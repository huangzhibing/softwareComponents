/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.processmaterial.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;

//import com.hqu.modules.workshopmanage.processmaterial.mapper.ProcessRoutineDetailMapper;
import com.hqu.modules.workshopmanage.processroutinedetail.entity.ProcessRoutineDetail;
import com.hqu.modules.workshopmanage.processroutinedetail.mapper.ProcessRoutineDetailMapper;
import com.hqu.modules.workshopmanage.processmaterial.entity.ProcessMaterial;
import com.hqu.modules.workshopmanage.processmaterial.mapper.ProcessMaterialMapper;

/**
 * 车间物料安装单Service
 * @author chen
 * @version 2018-08-10
 */
@Service
@Transactional(readOnly = true)
public class ProcessMaterialService extends CrudService<ProcessRoutineDetailMapper, ProcessRoutineDetail> {

	@Autowired
	private ProcessMaterialMapper processMaterialMapper;
	
	public ProcessRoutineDetail get(String id) {
		ProcessRoutineDetail processRoutineDetail = super.get(id);
		processRoutineDetail.setProcessMaterialList(processMaterialMapper.findList(new ProcessMaterial(processRoutineDetail)));
		return processRoutineDetail;
	}
	
	public List<ProcessRoutineDetail> findList(ProcessRoutineDetail processRoutineDetail) {
		return super.findList(processRoutineDetail);
	}
	
	public Page<ProcessRoutineDetail> findPage(Page<ProcessRoutineDetail> page, ProcessRoutineDetail processRoutineDetail) {
		return super.findPage(page, processRoutineDetail);
	}

	/**
	 * 保存加工路线明细表状态和物料安装单子表信息
	 * @param processRoutineDetail
	 */
	@Transactional(readOnly = false)
	public void save(ProcessRoutineDetail processRoutineDetail) {
		super.save(processRoutineDetail);
		for (ProcessMaterial processMaterial : processRoutineDetail.getProcessMaterialList()){
			if (processMaterial.getId() == null){
				continue;
			}
			if (ProcessMaterial.DEL_FLAG_NORMAL.equals(processMaterial.getDelFlag())){
				if (StringUtils.isBlank(processMaterial.getId())){
					processMaterial.preInsert();
					processMaterial.setSeqNo(processRoutineDetail.getSeqno());
					processMaterial.setRoutineBillNo(processRoutineDetail.getRoutinebillno());
					processMaterial.setRoutingCode(processRoutineDetail.getRoutingcode());
					processMaterialMapper.insert(processMaterial);
				}else{
					processMaterial.preUpdate();
					processMaterialMapper.update(processMaterial);
				}
			}else{
				processMaterialMapper.delete(processMaterial);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(ProcessRoutineDetail processRoutineDetail) {
		super.delete(processRoutineDetail);
		processMaterialMapper.delete(new ProcessMaterial(processRoutineDetail));
	}
	
}