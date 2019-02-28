/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.sfcinvcheckmain.service;

import java.util.List;

import com.hqu.modules.workshopmanage.processroutinedetail.entity.ProcessRoutineDetail;
import org.activiti.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.act.service.ActTaskService;
import com.jeeplus.modules.act.utils.ActUtils;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.workshopmanage.sfcinvcheckmain.entity.SfcInvCheckMain;
import com.hqu.modules.workshopmanage.sfcinvcheckmain.mapper.SfcInvCheckMainMapper;
import com.hqu.modules.workshopmanage.sfcinvcheckmain.entity.SfcInvCheckDetail;
import com.hqu.modules.workshopmanage.sfcinvcheckmain.mapper.SfcInvCheckDetailMapper;

/**
 * 完工入库通知单Service
 * @author zhangxin
 * @version 2018-06-01
 */
@Service
@Transactional(readOnly = true)
public class SfcInvCheckMainService extends CrudService<SfcInvCheckMainMapper, SfcInvCheckMain> {

	@Autowired
	private SfcInvCheckDetailMapper sfcInvCheckDetailMapper;
	
	@Autowired
	private ActTaskService actTaskService;
	
	@Autowired
	private IdentityService identityService;



	public SfcInvCheckMain get(String id) {
		SfcInvCheckMain sfcInvCheckMain = super.get(id);
		if(sfcInvCheckMain!=null) {
			sfcInvCheckMain.setSfcInvCheckDetailList(sfcInvCheckDetailMapper.findList(new SfcInvCheckDetail(sfcInvCheckMain)));
		}
		return sfcInvCheckMain;
	}
	
	public List<SfcInvCheckMain> findList(SfcInvCheckMain sfcInvCheckMain) {
		return super.findList(sfcInvCheckMain);
	}
	
	public Page<SfcInvCheckMain> findPage(Page<SfcInvCheckMain> page, SfcInvCheckMain sfcInvCheckMain) {
		return super.findPage(page, sfcInvCheckMain);
	}

	
	@Transactional(readOnly = false)
	public void save(SfcInvCheckMain sfcInvCheckMain) {
		super.save(sfcInvCheckMain);
		for (SfcInvCheckDetail sfcInvCheckDetail : sfcInvCheckMain.getSfcInvCheckDetailList()){
			if (sfcInvCheckDetail.getId() == null){
				continue;
			}
			if (SfcInvCheckDetail.DEL_FLAG_NORMAL.equals(sfcInvCheckDetail.getDelFlag())){
				if (StringUtils.isBlank(sfcInvCheckDetail.getId())){
					sfcInvCheckDetail.setSfcInvCheckMain(sfcInvCheckMain);
					sfcInvCheckDetail.preInsert();
					sfcInvCheckDetailMapper.insert(sfcInvCheckDetail);
				}else{
					sfcInvCheckDetail.preUpdate();
					sfcInvCheckDetailMapper.update(sfcInvCheckDetail);
				}
			}else{
				sfcInvCheckDetailMapper.delete(sfcInvCheckDetail);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(SfcInvCheckMain sfcInvCheckMain) {
		super.delete(sfcInvCheckMain);
		sfcInvCheckDetailMapper.delete(new SfcInvCheckDetail(sfcInvCheckMain));
	}
	
	@Transactional(readOnly = false)
	public void submit(SfcInvCheckMain sfcInvCheckMain) {
		String sql = "update sfc_invcheckmain set bill_state_flag='W' where bill_no='"+sfcInvCheckMain.getBillNo()+"'";
		super.executeUpdateSql(sql);
		
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(sfcInvCheckMain.getCurrentUser().getLoginName());
		// 启动流程
		actTaskService.startProcess(ActUtils.PD_TEST_AUDIT[0], ActUtils.PD_TEST_AUDIT[1], sfcInvCheckMain.getId(), sfcInvCheckMain.getBillNo());
	}
	
}