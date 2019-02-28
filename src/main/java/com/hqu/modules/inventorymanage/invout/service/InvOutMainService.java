/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.invout.service;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billmain.mapper.BillMainMapper;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.hqu.modules.inventorymanage.warehouse.mapper.WareHouseMapper;
import com.jeeplus.modules.act.service.ActTaskService;
import com.jeeplus.modules.act.utils.ActUtils;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.OfficeMapper;
import com.jeeplus.modules.sys.mapper.UserMapper;
import org.activiti.engine.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.inventorymanage.invout.entity.InvOutMain;
import com.hqu.modules.inventorymanage.invout.mapper.InvOutMainMapper;
import com.hqu.modules.inventorymanage.invout.entity.InvOutDetail;
import com.hqu.modules.inventorymanage.invout.mapper.InvOutDetailMapper;

/**
 * 放行单Service
 * @author M1ngz
 * @version 2018-06-02
 */
@Service
@Transactional(readOnly = true)
public class InvOutMainService extends CrudService<InvOutMainMapper, InvOutMain> {

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	protected TaskService taskService;
	@Autowired
	protected HistoryService historyService;
	@Autowired
	protected RepositoryService repositoryService;
	@Autowired
	private IdentityService identityService;


	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private InvOutDetailMapper invOutDetailMapper;

	/*
	*

		LEFT JOIN sys_user makeEmp ON makeEmp.no = a.make_emp_id
		LEFT JOIN inv_warehouse ware ON ware.ware_id = a.ware_id
		LEFT JOIN sys_user wareEmp ON wareEmp.no = a.ware_emp_id
		LEFT JOIN sys_office dept ON dept.code = a.dept_id
		LEFT JOIN inv_billmain corBillNum ON corBillNum.bill_num = a.cor_bill_num
		*/
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private WareHouseMapper wareHouseMapper;
	@Autowired
	OfficeMapper officeMapper;
	@Autowired
	BillMainMapper billMainMapper;

	@Override
	public InvOutMain get(String id) {
		InvOutMain invOutMain = super.get(id);
		invOutMain.setInvOutDetailList(invOutDetailMapper.findList(new InvOutDetail(invOutMain)));
		return invOutMain;
	}
	
	@Override
	public List<InvOutMain> findList(InvOutMain invOutMain) {
		return super.findList(invOutMain);
	}
	
	@Override
	public Page<InvOutMain> findPage(Page<InvOutMain> page, InvOutMain invOutMain) {
		return super.findPage(page, invOutMain);
	}
	
	@Transactional(readOnly = false)
	public void save(InvOutMain invOutMain, Map<String, Object> variables, boolean startProc) {
		/*

		LEFT JOIN sys_user makeEmp ON makeEmp.no = a.make_emp_id
		LEFT JOIN inv_warehouse ware ON ware.ware_id = a.ware_id
		LEFT JOIN sys_user wareEmp ON wareEmp.no = a.ware_emp_id
		LEFT JOIN sys_office dept ON dept.code = a.dept_id
		LEFT JOIN inv_billmain corBillNum ON corBillNum.bill_num = a.cor_bill_num
		*/

		if(invOutMain.getMakeEmp().getNo().length() == 32){
			User user = userMapper.get(invOutMain.getMakeEmp().getNo());
			invOutMain.setMakeEmp(user);
		}
		if(invOutMain.getWare().getWareID().length() == 32){
			WareHouse wareHouse = wareHouseMapper.get(invOutMain.getWare().getWareID());
			invOutMain.setWare(wareHouse);
		}
		if(invOutMain.getWareEmp().getNo().length() == 32){
			User user = userMapper.get(invOutMain.getWareEmp().getNo());
			invOutMain.setWareEmp(user);
		}
		if(invOutMain.getDept().getCode().length() == 32){
			Office office = officeMapper.get(invOutMain.getDept().getCode());
			invOutMain.setDept(office);
		}
		if(invOutMain.getCorBillNum().getBillNum().length() == 32){
			BillMain billMain = billMainMapper.get(invOutMain.getCorBillNum().getBillNum());
			invOutMain.setCorBillNum(billMain);
		}


		super.save(invOutMain);
		for (InvOutDetail invOutDetail : invOutMain.getInvOutDetailList()){
			if (invOutDetail.getId() == null){
				continue;
			}
			if (InvOutDetail.DEL_FLAG_NORMAL.equals(invOutDetail.getDelFlag())){
				if (StringUtils.isBlank(invOutDetail.getId())){
					invOutDetail.setBillNum(invOutMain);
					invOutDetail.preInsert();
					invOutDetailMapper.insert(invOutDetail);
				}else{
					invOutDetail.preUpdate();
					invOutDetailMapper.update(invOutDetail);
				}
			}else{
				invOutDetailMapper.delete(invOutDetail);
			}
		}
		if(startProc) {
			identityService.setAuthenticatedUserId(invOutMain.getCurrentUser().getLoginName());

			// 启动流程
			String businessKey = invOutMain.getId().toString();
			variables.put("type", "outMain");
			variables.put("busId", businessKey);
			variables.put("title", invOutMain.getBillNum());//设置标题；
			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(ActUtils.PD_OutMain[0], businessKey, variables);
			invOutMain.setProcessInstance(processInstance);

			// 更新流程实例ID
			invOutMain.setProcessInstanceId(processInstance.getId());
			mapper.updateProcessInstanceId(invOutMain);
			invOutMain.setBillFlag("W");
			mapper.update(invOutMain);

			logger.debug("start process of {key={}, bkey={}, pid={}, variables={}}", new Object[]{
					ActUtils.PD_OutMain[0], businessKey, processInstance.getId(), variables});
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(InvOutMain invOutMain) {
		super.delete(invOutMain);
		invOutDetailMapper.delete(new InvOutDetail(invOutMain));
	}


	@Transactional(readOnly = false)
	public void auditSave(InvOutMain invOutMain) {

		// 设置意见
		invOutMain.getAct().setComment(("yes".equals(invOutMain.getAct().getFlag())?"[同意] ":"[驳回] ")+invOutMain.getAct().getComment());

		invOutMain.preUpdate();

		// 对不同环节的业务逻辑进行操作
		String taskDefKey = invOutMain.getAct().getTaskDefKey();

		//业务逻辑对应的条件表达式
		String exp = "";
		// 审核环节
		if ("outMainAudit".equals(taskDefKey)){

			exp = "auditPass";
			InvOutMain dbOutMain = mapper.get(invOutMain.getId());
			if("yes".equals(invOutMain.getAct().getFlag())){
				dbOutMain.setBillFlag("E");
			} else {
				dbOutMain.setBillFlag("B");
			}
			mapper.update(dbOutMain);

		}
		else if ("modifyOutMain".equals(taskDefKey)){
			exp = "reApply";
			InvOutMain dbOutMain = mapper.get(invOutMain.getId());
			if("yes".equals(invOutMain.getAct().getFlag())){
				dbOutMain.setBillFlag("A");
			} else {
				dbOutMain.setBillFlag("V");
			}
			mapper.update(dbOutMain);
		}
		else if ("apply_end".equals(taskDefKey)){

		}

		// 未知环节，直接返回
		else{
			return;
		}

		// 提交流程任务
		Map<String, Object> vars = Maps.newHashMap();
		vars.put(exp, "yes".equals(invOutMain.getAct().getFlag())? true : false);
		// 提交流程任务
		actTaskService.complete(invOutMain.getAct().getTaskId(), invOutMain.getAct().getProcInsId(), invOutMain.getAct().getComment(), vars);

	}
	public InvOutMain getByProInsId(String id){
		return mapper.getByProInsId(id);
	}
	public String getMaxId(){
		return mapper.getMaxId();
	}
}