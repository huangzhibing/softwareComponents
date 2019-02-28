/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.purplan.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.hqu.modules.purchasemanage.adtdetail.entity.AdtDetail;
import com.hqu.modules.purchasemanage.adtdetail.service.AdtDetailService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanMainCheck;
import com.hqu.modules.purchasemanage.purplan.mapper.PurPlanMainCheckMapper;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanDetailCheck;
import com.hqu.modules.purchasemanage.purplan.mapper.PurPlanDetailCheckMapper;

/**
 * 采购计划审核Service
 * @author ckw
 * @version 2018-05-03
 */
@Service
@Transactional(readOnly = true)
public class PurPlanMainCheckService extends CrudService<PurPlanMainCheckMapper, PurPlanMainCheck> {

	@Autowired
	private PurPlanDetailCheckMapper purPlanDetailCheckMapper;

	//查询adt表，用于筛选记录
	@Autowired
	private AdtDetailService adtDetailService;

	
	public PurPlanMainCheck get(String id) {
		PurPlanMainCheck purPlanMainCheck = super.get(id);
		purPlanMainCheck.setPurPlanDetailCheckList(purPlanDetailCheckMapper.findList(new PurPlanDetailCheck(purPlanMainCheck)));
		return purPlanMainCheck;
	}
	
	public List<PurPlanMainCheck> findList(PurPlanMainCheck purPlanMainCheck) {
		return super.findList(purPlanMainCheck);
	}
	
	public Page<PurPlanMainCheck> findPage(Page<PurPlanMainCheck> page, PurPlanMainCheck purPlanMainCheck) {
		return super.findPage(page, purPlanMainCheck);
	}

	public Page<PurPlanMainCheck> findMyPage(Page<PurPlanMainCheck> page, PurPlanMainCheck purPlanMainCheck) {

		purPlanMainCheck.setPage(page);

		//首先查询所有单据状态位为“W”的记录
		purPlanMainCheck.setBillStateFlag("W");
		List<PurPlanMainCheck> purPlanMainCheckList=this.findList(purPlanMainCheck);

/*		//下面开始查ADT_detail表中符合条件的记录
		开始审核时，主界面显示要审核的单据要满足(以采购单审批为例，这里写死成PUR_ApplyMain)：
		select * from PUR_ApplyMain,ADT_Detail  where billTypeCode='APP001' and
		PUR_ApplyMain.billNum=ADT_Detail.billNum and ADT_Detail.JPerson_Code=当前登录用户ID and
		isFinished='0';
*/
        //获取当前登录用户对应的角色名
		List<String> roleNames = UserUtils.getUser().getRoleIdList();

		//用来保存当前用户对应角色对应的ADT表数据
		ArrayList<AdtDetail> result= new ArrayList<>();

        //从审核表中查找"采购需求单"中所有未审核状态的单据
		AdtDetail adtDetail=new AdtDetail();
		adtDetail.setIsFinished("0");
//      APP001采购需求单，PLN 001采购计划单，CON0001 采购合同
		adtDetail.setBillTypeCode("PLN001");
        List<AdtDetail> detailList=adtDetailService.findList(adtDetail);


        List<AdtDetail> adtDetailRes=new LinkedList<AdtDetail>();

        //筛选出当前用户具有审核权限的待审核记录
        //因为审核表中可能有多条isfinished状态为“0”的记录，所以对于每一个审核记录,都要判断其对应的role是否在当前用户所属的角色们中
		for (int i = 0; i < detailList.size(); i++) {
		    //对每一条符合条件的记录，判断当前用户对应的角色们是否对其具有审核权限
			AdtDetail adtDetailTmp=detailList.get(i);

			for (int j = 0; j < roleNames.size(); j++) {
                //具有审核权限，保存此ADT记录，即保存当前用户有审核权限的记录
				if((adtDetailTmp.getJpositionCode().equals(roleNames.get(j)))){
					result.add(adtDetailTmp);
					break;
				}
			}
		}

//		循环后result列表保存着审核表中在当前用户对应的角色审核权限之内的所有待审核记录


        //然后对result列表中对应的记录进行过滤，此时的条件是result[x].billNum==purPlanMainCheckList[x].billNum
        List<PurPlanMainCheck> finalResult=new ArrayList<PurPlanMainCheck>();

//		purPlanMainCheckList保存着所有状态位为“W”的待审核采购计划,接下来判断待审核采购计划记录的审核人是否是当前用户对应的角色
		for (int i = 0; i < purPlanMainCheckList.size(); i++) {
		    PurPlanMainCheck tmp=purPlanMainCheckList.get(i);

			for (int j = 0; j < result.size(); j++) {

				AdtDetail  tmpAdtDetail=result.get(j);

				if(tmp.getBillNum().equals(tmpAdtDetail.getBillNum())){
					finalResult.add(tmp);
                }
            }
		}

		//手动设置记录数，不然记录显示条数和提示总条数可能不一致
		page.setCount(finalResult.size());

		page.setList(finalResult);
		return page;
	}


	@Transactional(readOnly = false)
	public void save(PurPlanMainCheck purPlanMainCheck) {
		super.save(purPlanMainCheck);
		for (PurPlanDetailCheck purPlanDetailCheck : purPlanMainCheck.getPurPlanDetailCheckList()){
			if (purPlanDetailCheck.getId() == null){
				continue;
			}
			if (PurPlanDetailCheck.DEL_FLAG_NORMAL.equals(purPlanDetailCheck.getDelFlag())){
				if (StringUtils.isBlank(purPlanDetailCheck.getId())){
					purPlanDetailCheck.setBillNum(purPlanMainCheck);
					purPlanDetailCheck.preInsert();
					purPlanDetailCheckMapper.insert(purPlanDetailCheck);
				}else{
					purPlanDetailCheck.preUpdate();
					purPlanDetailCheckMapper.update(purPlanDetailCheck);
				}
			}else{
				purPlanDetailCheckMapper.delete(purPlanDetailCheck);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(PurPlanMainCheck purPlanMainCheck) {
		super.delete(purPlanMainCheck);
		purPlanDetailCheckMapper.delete(new PurPlanDetailCheck(purPlanMainCheck));
	}
	
}