/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.pickbill.service;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.hqu.modules.basedata.account.entity.Account;
import com.hqu.modules.basedata.account.mapper.AccountMapper;
import com.hqu.modules.basedata.product.mapper.ProductMapper;
import com.hqu.modules.salemanage.salcontract.entity.Contract;
import com.hqu.modules.salemanage.salcontract.entity.CtrItem;
import com.hqu.modules.salemanage.salcontract.mapper.ContractMapper;
import com.hqu.modules.salemanage.salcontract.mapper.CtrItemMapper;
import com.hqu.modules.salemanage.salcontract.service.ContractService;
import com.jeeplus.common.utils.Collections3;
import com.jeeplus.modules.act.service.ActTaskService;
import com.jeeplus.modules.act.utils.ActUtils;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.salemanage.pickbill.entity.PickBill;
import com.hqu.modules.salemanage.pickbill.mapper.PickBillMapper;
import com.hqu.modules.salemanage.pickbill.entity.PickBillItem;
import com.hqu.modules.salemanage.pickbill.mapper.PickBillItemMapper;

/**
 * 销售发货单据Service
 * @author M1ngz
 * @version 2018-05-15
 */
@Service
@Transactional(readOnly = true)
public class PickBillService extends CrudService<PickBillMapper, PickBill> {

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
	private PickBillItemMapper pickBillItemMapper;
	@Autowired
	UserMapper userMapper;
	@Autowired
	ContractMapper contractMapper;
	@Autowired
	ContractService contractService;
	@Autowired
	private CtrItemMapper ctrItemMapper;
	@Autowired
	AccountMapper accountMapper;
	@Autowired
	ProductMapper productMapper;

	public PickBill get(String id) {
		PickBill pickBill = mapper.get(id);
		pickBill.setPickBillItemList(pickBillItemMapper.findList(new PickBillItem(pickBill)));
		if(org.springframework.util.StringUtils.isEmpty(pickBill.getProcessInstanceId())){
			return pickBill;
		}
		Map<String,Object> variables=null;
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(pickBill.getProcessInstanceId()).singleResult();
		if(historicProcessInstance!=null) {
			variables = Collections3.extractToMap(historyService.createHistoricVariableInstanceQuery().processInstanceId(historicProcessInstance.getId()).list(), "variableName", "value");
		} else {
			variables = runtimeService.getVariables(runtimeService.createProcessInstanceQuery().processInstanceId(pickBill.getProcessInstanceId()).active().singleResult().getId());
		}
		return pickBill;
	}
	
	public List<PickBill> findList(PickBill pickBill) {
		return super.findList(pickBill);
	}
	
	public Page<PickBill> findPage(Page<PickBill> page, PickBill pickBill) {
		return super.findPage(page, pickBill);
	}
	
	@Transactional(readOnly = false,rollbackFor = Exception.class)
	public void save(PickBill pickBill, Map<String, Object> variables,boolean startProc) {
		if(pickBill.getPbillPerson().getNo().length() == 32){
			User user = userMapper.get(pickBill.getPbillPerson().getNo());
			pickBill.setPbillPerson(user);
		}
		if(pickBill.getContract().getContractCode().length() == 32){
			Contract contract = contractMapper.get(pickBill.getContract().getContractCode());
			pickBill.setContract(contract);
		}
		if(pickBill.getRcvAccount().getAccountCode().length() == 32){
			Account account = accountMapper.get(pickBill.getRcvAccount().getAccountCode());
			pickBill.setRcvAccount(account);
		}
		super.save(pickBill);
		for (PickBillItem pickBillItem : pickBill.getPickBillItemList()){
			if (pickBillItem.getSeqId() == null){
				continue;
			}
			if (PickBillItem.DEL_FLAG_NORMAL.equals(pickBillItem.getDelFlag())){
				if (StringUtils.isBlank(pickBillItem.getId())){
					pickBillItem.setPbillCode(pickBill);
					pickBillItem.setProdCode(productMapper.get(pickBillItem.getProdCode().getId()));
					pickBillItem.preInsert();
					pickBillItemMapper.insert(pickBillItem);
				}else{
					pickBillItem.preUpdate();
					pickBillItemMapper.update(pickBillItem);
				}
			}else{
				pickBillItemMapper.delete(pickBillItem);
			}
		}
		if(startProc) {
			identityService.setAuthenticatedUserId(pickBill.getCurrentUser().getLoginName());

			// 启动流程
			String businessKey = pickBill.getId().toString();
			variables.put("type", "pickBill");
			variables.put("busId", businessKey);
			variables.put("title", pickBill.getPbillCode());//设置标题；
			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(ActUtils.PD_PickBill[0], businessKey, variables);
			pickBill.setProcessInstance(processInstance);

			// 更新流程实例ID
			pickBill.setProcessInstanceId(processInstance.getId());
			mapper.updateProcessInstanceId(pickBill);
			pickBill.setPbillStat("W");
			mapper.update(pickBill);

			logger.debug("start process of {key={}, bkey={}, pid={}, variables={}}", new Object[]{
					ActUtils.PD_PickBill[0], businessKey, processInstance.getId(), variables});
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(PickBill pickBill) {
		super.delete(pickBill);
		pickBillItemMapper.delete(new PickBillItem(pickBill));
	}


	@Transactional(readOnly = false)
	public void auditSave(PickBill pickBill) {

		// 设置意见
		pickBill.getAct().setComment(("yes".equals(pickBill.getAct().getFlag())?"[同意] ":"[驳回] ")+pickBill.getAct().getComment());

		pickBill.preUpdate();

		// 对不同环节的业务逻辑进行操作
		String taskDefKey = pickBill.getAct().getTaskDefKey();

		//业务逻辑对应的条件表达式
		String exp = "";
		// 审核环节
		if ("pickBillAudit".equals(taskDefKey)){

			exp = "auditPass";
			PickBill dbPickBill = mapper.get(pickBill.getId());
			if("yes".equals(pickBill.getAct().getFlag())){
				dbPickBill.setPbillStat("E");
				Contract contract = contractService.getByCode(dbPickBill.getContract().getContractCode());
			    List<CtrItem> items = contract.getCtrItemList();
			    List<PickBillItem> pickBillItems = pickBillItemMapper.findList(new PickBillItem(dbPickBill));
			    if(pickBillItems.size()>0 && items.size() > 0) {
			    	for(PickBillItem pickBillItem : pickBillItems){
			    		for(CtrItem item : items) {
			    			if(item.getItemCode().equals(pickBillItem.getItemCode())) {
			    				item.setPickQty(item.getPickQty() + pickBillItem.getPickQty());
			    				ctrItemMapper.update(item);
			    				break;
							}
						}
					}
				}
			} else {
				dbPickBill.setPbillStat("B");
			}
			mapper.update(dbPickBill);

		}
		else if ("modifyPickBill".equals(taskDefKey)){
			exp = "reApply";
			PickBill dbPickBill = mapper.get(pickBill.getId());
			if("yes".equals(pickBill.getAct().getFlag())){
				dbPickBill.setPbillStat("A");
			} else {
				dbPickBill.setPbillStat("V");
			}
			mapper.update(dbPickBill);
		}
		else if ("apply_end".equals(taskDefKey)){

		}

		// 未知环节，直接返回
		else{
			return;
		}

		// 提交流程任务
		Map<String, Object> vars = Maps.newHashMap();
		vars.put(exp, "yes".equals(pickBill.getAct().getFlag())? true : false);
		// 提交流程任务
		actTaskService.complete(pickBill.getAct().getTaskId(), pickBill.getAct().getProcInsId(), pickBill.getAct().getComment(), vars);

	}

	public PickBill getByProInsId(String id){
		return mapper.getByProInsId(id);
	}
	public String getMaxId(){
		return mapper.getMaxId();
	}
}