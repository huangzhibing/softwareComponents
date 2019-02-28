/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.dispatchbill.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.act.service.ActTaskService;
import com.jeeplus.modules.act.utils.ActUtils;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.workshopmanage.dispatchbill.entity.DispatchBill;
import com.hqu.modules.workshopmanage.dispatchbill.mapper.DispatchBillMapper;
import com.hqu.modules.workshopmanage.dispatchbill.entity.DispatchBillPerson;
import com.hqu.modules.workshopmanage.dispatchbill.mapper.DispatchBillPersonMapper;
import com.hqu.modules.workshopmanage.sfcinvcheckmain.entity.SfcInvCheckDetail;
import com.hqu.modules.workshopmanage.sfcinvcheckmain.entity.SfcInvCheckMain;
import com.hqu.modules.workshopmanage.sfcinvcheckmain.mapper.SfcInvCheckDetailMapper;
import com.hqu.modules.workshopmanage.sfcinvcheckmain.mapper.SfcInvCheckMainMapper;

/**
 * 车间派工单Service
 * @author zhangxin
 * @version 2018-05-31
 */
@Service
@Transactional(readOnly = true)
public class DispatchBillService extends CrudService<DispatchBillMapper, DispatchBill> {

	@Autowired
	private DispatchBillPersonMapper dispatchBillPersonMapper;
	
	@Autowired
	private SfcInvCheckMainMapper sfcInvCheckMainMapper;
	
	@Autowired
	private SfcInvCheckDetailMapper sfcInvCheckDetailMapper;
	
	@Autowired
	private ActTaskService actTaskService;
	
	@Autowired
	private IdentityService identityService;
	
	public DispatchBill get(String id) {
		DispatchBill dispatchBill = super.get(id);
		dispatchBill.setDispatchBillPersonList(dispatchBillPersonMapper.findList(new DispatchBillPerson(dispatchBill)));
		return dispatchBill;
	}
	
	public List<DispatchBill> findList(DispatchBill dispatchBill) {
		return super.findList(dispatchBill);
	}
	
	public Page<DispatchBill> findPage(Page<DispatchBill> page, DispatchBill dispatchBill) {
		return super.findPage(page, dispatchBill);
	}
	
	@Transactional(readOnly = false)
	public void save(DispatchBill dispatchBill) {
		super.save(dispatchBill);
		for (DispatchBillPerson dispatchBillPerson : dispatchBill.getDispatchBillPersonList()){
			if (dispatchBillPerson.getId() == null){
				continue;
			}
			if (DispatchBillPerson.DEL_FLAG_NORMAL.equals(dispatchBillPerson.getDelFlag())){
				if (StringUtils.isBlank(dispatchBillPerson.getId())){
					dispatchBillPerson.setDispatchNo(dispatchBill);
					
					dispatchBillPerson.setRoutineBillNo(dispatchBill.getRoutineBillNo());
					
					dispatchBillPerson.preInsert();
					dispatchBillPersonMapper.insert(dispatchBillPerson);
				}else{
					dispatchBillPerson.preUpdate();
					dispatchBillPersonMapper.update(dispatchBillPerson);
				}
			}else{
				dispatchBillPersonMapper.delete(dispatchBillPerson);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(DispatchBill dispatchBill) {
		super.delete(dispatchBill);
		dispatchBillPersonMapper.delete(new DispatchBillPerson(dispatchBill));
	}
	
	@Transactional(readOnly = false)
	public void submit(DispatchBill dispatchBill) {
		//String sql = "update sfc_dispatchbill set planstatus='W' where dispatchno='"+dispatchBill.getDispatchNo()+"'";
		//super.executeUpdateSql(sql);
		
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(dispatchBill.getCurrentUser().getLoginName());
		// 启动流程
		actTaskService.startProcess(ActUtils.PD_TEST_AUDIT[0], ActUtils.PD_TEST_AUDIT[1], dispatchBill.getId(), dispatchBill.getDispatchNo());
		
		this.insBill(dispatchBill);
	}
	
	@Transactional(readOnly = false)
	public void insBill(DispatchBill dispatchBill) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String bill = sdf.format(new Date());
		String num = "";
		int max = 0;
		
		String sql = "select max(substring(_t1.bill_no,13,5)) as bill from sfc_invcheckmain _t1 where _t1.bill_no like 'WGRK"+bill+"%'";
		
		List billList = super.executeSelectSql(sql);
		if (billList.get(0) != null) {
			max = Integer.valueOf((String) billList.get(0));
		}

		max++;
		
		num = String.valueOf(max);
		
		while(num.length()<5){
			num = "0" + num;
		}
		
		User tuser = UserUtils.getUser();
		
		SfcInvCheckMain sfcInvCheckMain = new SfcInvCheckMain();
		
		sfcInvCheckMain.setBillNo("WGRK"+bill+num);
		sfcInvCheckMain.setBillType("C");
		sfcInvCheckMain.setMakeDate(new Date());
		sfcInvCheckMain.setMakepId(tuser.getId());
		sfcInvCheckMain.setMakepName(tuser.getName());
		sfcInvCheckMain.setBillStateFlag("A");
		sfcInvCheckMain.setWorkerpId(tuser.getId());
		sfcInvCheckMain.setWorkerpName(tuser.getName());
		sfcInvCheckMain.setFinishDate(new Date());
		sfcInvCheckMain.setShopId(tuser.getOffice().getId());
		sfcInvCheckMain.setShopName(tuser.getOffice().getName());
		
		int seq = 0;
		
		String sql2 = "select max(id) as bill from sfc_invcheckmain";
		
		List billList2 = super.executeSelectSql(sql2);
		if (billList2.get(0) != null) {
			seq = Integer.valueOf((String) billList2.get(0));
		}

		seq++;
		
		String id = String.valueOf(seq);
		
		sfcInvCheckMain.setId(id);
		sfcInvCheckMain.setDelFlag("0");
		
		sfcInvCheckMainMapper.insert(sfcInvCheckMain);
		
		SfcInvCheckDetail sfcInvCheckDetail = new SfcInvCheckDetail();
		
		sfcInvCheckDetail.setSfcInvCheckMain(sfcInvCheckMain);
		
		int ser = 0;
		
		String sql3 = "select max(id) as bill from sfc_invcheckdetail";
		
		List billList3 = super.executeSelectSql(sql3);
		if (billList3.get(0) != null) {
			ser = Integer.valueOf((String) billList3.get(0));
		}

		ser++;
		
		String idd = String.valueOf(ser);
		
		sfcInvCheckDetail.setId(idd);
		sfcInvCheckDetail.setDelFlag("0");
		sfcInvCheckDetail.setSerialNo(ser);
		
		sfcInvCheckDetailMapper.insert(sfcInvCheckDetail);
	}
	
}