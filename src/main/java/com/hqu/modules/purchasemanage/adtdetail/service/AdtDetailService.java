/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.adtdetail.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.hqu.modules.purchasemanage.adtbilltype.entity.AdtBillType;
import com.hqu.modules.purchasemanage.adtmodel.entity.AdtModel;
import com.hqu.modules.purchasemanage.adtmodel.mapper.AdtModelMapper;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.purchasemanage.adtdetail.entity.AdtDetail;
import com.hqu.modules.purchasemanage.adtdetail.mapper.AdtDetailMapper;

/**
 * 审核表Service
 * @author ckw
 * @version 2018-05-08
 */
@Service
@Transactional(readOnly = true)
public class AdtDetailService extends CrudService<AdtDetailMapper, AdtDetail> {

	@Autowired
	AdtDetailMapper detailMapper;
	@Autowired
	AdtModelMapper modelMapper;
	@Autowired
	UserMapper userMapper;

	public AdtDetail get(String id) {
		return super.get(id);
	}
	
	public List<AdtDetail> findList(AdtDetail adtDetail) {
		return super.findList(adtDetail);
	}
	
	public Page<AdtDetail> findPage(Page<AdtDetail> page, AdtDetail adtDetail) {
		return super.findPage(page, adtDetail);
	}
	
	@Transactional(readOnly = false)
	public void save(AdtDetail adtDetail) {
		super.save(adtDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(AdtDetail adtDetail) {
		super.delete(adtDetail);
	}

	/**
	 * 审核不通过，发回制单人时
     * 这时插入时，写入的是adt_detail表的jpersoncode字段，而不写岗位表，写审核角色表
	 * 插入下一个审核角色
	 * @param billNum 业务单据编号
	 * @param billType 业务单据类型
	 * @param modelCode 使用的模型编号
	 * @param user 下一个审核角色
	 */
	@Transactional(readOnly = false)
	public void nextStepForUnpass(String billNum, AdtBillType billType,String modelCode,User user){
		AdtDetail ad=new AdtDetail();
		ad.setBillNum(billNum);//记录业务表编码
		ad.setBillTypeCode(billType.getBillTypeCode());//记录业务表类型
		ad.setBillTypeName(billType.getBillTypeName());//记录业务表类型名称 计划/需求/合同
		ad.setModelCode(modelCode);//记录对应工作流模型编号
		ad.setIsFinished("1");//设置成待审（原制单人没有审核状态）
		ad.setFinishFlag("0");//设置为结束标志

        AdtDetail adtDetail=new AdtDetail();
        adtDetail.setBillNum(billNum);
        adtDetail.setBillTypeCode(billType.getBillTypeCode());
        ad.setStep(detailMapper.findStep(adtDetail)+1);//记录下一步骤号
		//当单据为“打回原制单人”时，写原制单人ID到detail表的审核人的角色ID（jpersoncode），而不是岗位ID(jpositoncode)，通过这种方式中断后续的审核
//        ad.setJpositionCode(role.getId());//记录下一个审核角色/岗位ID
//        ad.setJpositionName(role.getName());//记录下一个审核角色/岗位名称
        //将原制单人的id写入detail表的下一条流转记录中的personcode字段，而不是jpositioncode字段
		ad.setJpersonCode(user.getId());
		user=userMapper.get(user.getId());
		ad.setJustifyPerson(user.getName());
		ad.setJustifyResult("原制单人修改");


		save(ad);
	}


	/**
	 * 插入下一个审核角色
	 * @param billNum 业务单据编号
	 * @param billType 业务单据类型
	 * @param modelCode 使用的模型编号
	 * @param isFirst 是否是提交到第一个审核角色
	 * @param role 下一个审核角色
	 * @author yxb 2018/5/12
	 */
	@Transactional(readOnly = false)
	public void nextStep(String billNum, AdtBillType billType,String modelCode, boolean isFirst, Role role){
		AdtDetail ad=new AdtDetail();
		ad.setBillNum(billNum);//记录业务表编码
		ad.setBillTypeCode(billType.getBillTypeCode());//记录业务表类型
		ad.setBillTypeName(billType.getBillTypeName());//记录业务表类型名称 计划/需求/合同
		ad.setModelCode(modelCode);//记录对应工作流模型编号
		ad.setIsFinished("0");//设置成待审
		ad.setFinishFlag("0");//设置结束标志
		if(isFirst) {
			AdtDetail adtDetail=new AdtDetail();
			adtDetail.setBillNum(billNum);
			adtDetail.setBillTypeCode(billType.getBillTypeCode());
			Integer step=detailMapper.findStep(adtDetail);
			if(step!=null&&step!=0)
			{
				ad.setStep(step+1);//记录下一步骤号
			}else{
				ad.setStep(1);
			}
			AdtModel am=new AdtModel();
			am.setModelCode(modelCode);
			am.setIsFirstperson("1");
			List<AdtModel> models=modelMapper.findList(am);
			if(models.size()>0&&models.get(0)!=null&&models.get(0).getRole()!=null){
				ad.setJpositionCode(models.get(0).getRole().getId());//第一个审核角色（岗位）ID
				ad.setJpositionName(models.get(0).getRole().getName());//第一个审核角色（岗位）名称
			}
		} else {
			AdtDetail adtDetail=new AdtDetail();
			adtDetail.setBillNum(billNum);
			adtDetail.setBillTypeCode(billType.getBillTypeCode());
			ad.setStep(detailMapper.findStep(adtDetail)+1);//记录下一步骤号
			ad.setJpositionCode(role.getId());//记录下一个审核角色/岗位ID
			ad.setJpositionName(role.getName());//记录下一个审核角色/岗位名称
		}
		save(ad);
	}

	/**
	 * 审核不通过处理（补充流转表中当前审核人的信息）
	 * @param adtDetail 记录了当前审核人信息的流转表记录
	 * @param isEnd   指定下一个审核人时是否选择了结束
	 */
	@Transactional(readOnly = false)
	public void auditFail(AdtDetail adtDetail,boolean isEnd){
		AdtDetail detail=new AdtDetail();
		detail.setBillNum(adtDetail.getBillNum());
		detail.setBillTypeCode(adtDetail.getBillTypeCode());
		detail.setBillTypeName(adtDetail.getBillTypeName());
		List<AdtDetail> details= findList(detail);
		int step=0;
		for(AdtDetail d:details){
			if(d.getStep()>step){
				step=d.getStep();
				detail=d;
			}
		}

		detail.setJustifyRemark(adtDetail.getJustifyRemark());
		detail.setJpersonCode(UserUtils.getUser().getId());
		detail.setJustifyPerson(UserUtils.getUser().getName());
		detail.setJdeptCode(UserUtils.getUserOfficeCode());
		detail.setJdeptName(UserUtils.getUser().getOffice().getName());
		detail.setJustifyDate(new Date());
		detail.setJustifyResult("不通过");
		detail.setIsFinished("1");
		if(isEnd) {
			detail.setFinishFlag("1");
		}else {
			detail.setFinishFlag("0");
		}
		save(detail);

	}


	/**
	 * 审核通过处理（补充流转表中当前审核人的信息）
	 * @param adtDetail 记录了当前审核人信息的流转表记录
	 * @param isEnd   指定下一个审核人时是否选择了结束
	 * @author yxb 2018/5/12
	 */
	@Transactional(readOnly = false)
	public void auditPass(AdtDetail adtDetail,boolean isEnd){
		AdtDetail detail=new AdtDetail();
		detail.setBillNum(adtDetail.getBillNum());
		detail.setBillTypeCode(adtDetail.getBillTypeCode());
		detail.setBillTypeName(adtDetail.getBillTypeName());
		List<AdtDetail> details= findList(detail);
		int step=0;
		for(AdtDetail d:details){
			if(d.getStep()>step){
				step=d.getStep();
				detail=d;
			}
		}

		detail.setJpersonCode(UserUtils.getUser().getId());
		detail.setJustifyPerson(UserUtils.getUser().getName());
		detail.setJdeptCode(UserUtils.getUserOfficeCode());
		detail.setJdeptName(UserUtils.getUser().getOffice().getName());
		detail.setJustifyDate(new Date());
		detail.setJustifyResult("通过");
		detail.setIsFinished("1");
		if(isEnd) {
			detail.setFinishFlag("1");
		}else {
			detail.setFinishFlag("0");
		}
		save(detail);

	}

	/**
	 * 获取历史审核流程记录
	 * @param billNum 业务单据编号
	 * @param billTypeCode 业务单据类型 PLN001/APP001/CON001
	 * @return 按照步骤号升序顺序的List<AdtDetail></>
	 */
	public List<AdtDetail> getHistoryDetails(String billNum,String billTypeCode){
		AdtDetail detail=new AdtDetail();
		detail.setBillNum(billNum);
		detail.setBillTypeCode(billTypeCode);
		List<AdtDetail> details= findList(detail);
		if(details!=null&&details.size()>0) {
			Collections.sort(details, new Comparator<AdtDetail>() {
				/*
				 * int compare(AdtDetail o1, AdtDetail o2) 返回一个基本类型的整型，
				 * 返回负数表示：o1 小于o2，
				 * 返回0 表示：o1和o2相等，
				 * 返回正数表示：o1大于o2。
				 */
				public int compare(AdtDetail o1, AdtDetail o2) {

					//按照步骤号进行升序排列
					if (o1.getStep() > o2.getStep()) {
						return 1;
					}
					if (o1.getStep() == o2.getStep()) {
						return 0;
					}
					return -1;
				}
			});
			return details;
		}
		return null;

	}
	
}