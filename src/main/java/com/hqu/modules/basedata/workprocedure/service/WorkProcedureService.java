/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.workprocedure.service;

import java.util.List;

import com.hqu.modules.basedata.machinetype.entity.MachineType;
import com.hqu.modules.basedata.machinetype.mapper.MachineTypeMapper;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.OfficeMapper;
import com.jeeplus.modules.sys.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.basedata.workprocedure.entity.WorkProcedure;
import com.hqu.modules.basedata.workprocedure.mapper.WorkProcedureMapper;

/**
 * 工序定义Service
 * @author liujiachen
 * @version 2018-04-06
 */
@Service
@Transactional(readOnly = true)
public class WorkProcedureService extends CrudService<WorkProcedureMapper, WorkProcedure> {
	@Autowired
	WorkProcedureMapper workProcedureMapper;
	@Autowired
	OfficeMapper officeMapper;
	@Autowired
	MachineTypeMapper machineTypeMapper;
	@Autowired
	UserMapper userMapper;

	public WorkProcedure get(String id) {
		return super.get(id);
	}
	
	public List<WorkProcedure> findList(WorkProcedure workProcedure) {
		return super.findList(workProcedure);
	}
	
	public Page<WorkProcedure> findPage(Page<WorkProcedure> page, WorkProcedure workProcedure) {
		return super.findPage(page, workProcedure);
	}
	
	@Transactional(readOnly = false)
	public void save(WorkProcedure workProcedure) {
		if(workProcedure.getDeptCode().getCode().length() == 32){
			Office office = officeMapper.get(workProcedure.getDeptCode().getCode());
			workProcedure.setDeptCode(office);
		}
		if(workProcedure.getMachineTypeCode().getMachineTypeCode().length() == 32){
			MachineType machineType = machineTypeMapper.get(workProcedure.getMachineTypeCode().getMachineTypeCode());
			workProcedure.setMachineTypeCode(machineType);
		}
		if(workProcedure.getUser().getNo().length() == 32){
			User user = userMapper.get(workProcedure.getUser().getNo());
			workProcedure.setUser(user);
		}
		super.save(workProcedure);
	}
	
	@Transactional(readOnly = false)
	public void delete(WorkProcedure workProcedure) {
		super.delete(workProcedure);
	}

	public String getMaxWorkProcedureId(){ return workProcedureMapper.getMaxWorkProcedureId(); }
}