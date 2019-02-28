package com.hqu.modules.inventorymanage.plananduse.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hqu.modules.inventorymanage.billmain.entity.BillDetail;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.materialconsumption.entity.MaterialConsumption;
import com.hqu.modules.inventorymanage.plananduse.entity.PlanAndUse;
import com.hqu.modules.inventorymanage.plananduse.mapper.PlanAndUseMapper;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanDetail;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanMain;
import com.hqu.modules.purchasemanage.purplan.mapper.PurPlanDetailMapper;
import com.hqu.modules.purchasemanage.purplan.mapper.PurPlanMainMapper;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.service.OfficeService;

@Service
@Transactional(readOnly = true)
public class PlanAndUseService extends CrudService<PlanAndUseMapper, PurPlanMain>{
	@Autowired
	private PlanAndUseMapper planAndUseMapper;
	@Autowired
	private OfficeService officeService;
	public Page<PlanAndUse> findPage(Page<PurPlanMain> page,PurPlanMain purPlanMain,PurPlanDetail purPlanDetail) {
		Page<PurPlanMain> purs=super.findPage(page,purPlanMain);
		for(PurPlanMain pur:purs.getList()) {
			purPlanDetail.setBillNum(pur);
			pur.setPurPlanDetailList(planAndUseMapper.findListTest(purPlanDetail));
		}
		logger.debug("起初:"+page.getList().toString());
		sumByDepartAndItem(purs);
		return alterPage(purs);
	}
	
	/*
	 * 统计部门领取的各个物料的总数量和金额
	 */
	private void sumByDepartAndItem(Page<PurPlanMain> page){
		List<PurPlanMain> billMains=page.getList();
		for(int i=0;i<billMains.size();i++) {
			for(int j=i+1;j<billMains.size();j++) {
				PurPlanMain ib=billMains.get(i);
				PurPlanMain jb=billMains.get(j);
				if(ib.getUserDeptCode().equals(jb.getUserDeptCode())) {
					combineByDepart(ib.getPurPlanDetailList(),jb.getPurPlanDetailList());
					billMains.remove(j--);
				}
			}
		}
	}
	
	/*
	 * 合并相同部门数据
	 */
	private void combineByDepart(List<PurPlanDetail> a,List<PurPlanDetail> b) {
		for(int i=0;i<a.size();i++) {
			for(int j=0;j<b.size();j++) {
				if(a.get(i).getItemName().equals(b.get(j).getItemName())) {
					a.get(i).setPlanQty(a.get(i).getPlanQty()+b.get(j).getPlanQty());
					a.get(i).setPlanSum(a.get(i).getPlanSum()+b.get(j).getPlanSum());
					logger.debug("合并:"+j+"->"+i);
					b.remove(j--);
				}
			}
		}
		a.addAll(b);
	}
	
	/*
	 * 更改page内数据（PurPlanDetail->PlanAndUse)
	 */
	private Page<PlanAndUse> alterPage(Page<PurPlanMain> page) {
		logger.debug("mate:"+page.getList().toString());
		List<PurPlanMain> billList=page.getList();
		Page<PlanAndUse> mate=new Page<PlanAndUse>();
		for(PurPlanMain b:billList) {
			for(PurPlanDetail detail:b.getPurPlanDetailList()) {
				PlanAndUse m=new PlanAndUse();
				m.setDeptCode(b.getUserDeptCode());
				try {
					m.setDeptName(officeService.getByCode(m.getDeptCode()).getName());
				}catch(Exception e) {
					e.printStackTrace();
				}
				m.setItemCode(detail.getItemCode().getCode());
				m.setItemName(detail.getItemName());
				logger.debug("物料:"+detail.getItemName());
				m.setItemSpec(detail.getItemSpecmodel());
				m.setMeasUnit(detail.getUnitName());
				m.setPlanQty(detail.getPlanQty());
				m.setPlanSum(detail.getPlanSum());
				mate.getList().add(m);
			}
		}
		logger.debug("mate:"+mate.getList().toString());
		return mate;
	}
}
