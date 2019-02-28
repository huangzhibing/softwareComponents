package com.hqu.modules.inventorymanage.outsourcingoutput.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetail;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetailCode;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billtype.entity.BillType;
import com.hqu.modules.inventorymanage.employee.entity.Employee;
import com.hqu.modules.inventorymanage.employee.entity.EmployeeWareHouse;
import com.hqu.modules.inventorymanage.outsourcingoutput.mapper.BillDetailOutsourcingMapper;
import com.hqu.modules.inventorymanage.outsourcingoutput.mapper.OutsourcingOutputMapper;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.hqu.modules.workshopmanage.materialorder.entity.SfcMaterialOrder;
import com.hqu.modules.workshopmanage.materialorder.entity.SfcMaterialOrderDetail;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;

@Service
public class OutsourcingOutputService {
	@Autowired
	private OutsourcingOutputMapper outsourcingOutputMapper;
	@Autowired
	private BillDetailOutsourcingMapper billDetailOutsourcingMapper;
	@Autowired
	private BillMainOutsourcingService billMainOutsourcingService;
	public Period findPeriodByTime(Map<String, Object> map) {
		return outsourcingOutputMapper.findPeriodByTime(map);
	}
	public List<BillMain> findBillMainByItemCode(String itemCode){
		List<String> ids=billDetailOutsourcingMapper.findIdByItemCode(itemCode);
		if(ids.size()==0) return null;
		List<BillMain> blist=new ArrayList<>();
		for(int i=0;i<ids.size();i++) {
			blist.add(billMainOutsourcingService.get(ids.get(i)));
		}
		return blist;
	}
	
	public Boolean changeRealQty(Map<String, Object>map) {
		return outsourcingOutputMapper.changeRealQty(map);
	}
	
	public Boolean changeBillFlagById(Map<String, Object> map) {
		return outsourcingOutputMapper.changeBillFlagById(map);
	}
	public List<BillDetailCode> findCodeByBillNum(String billNum){
		return outsourcingOutputMapper.findCodeByBillNum(billNum);
	}
	public Boolean updateOflagByBarcode(Map<String, Object> map) {
		return outsourcingOutputMapper.updateOflagByBarcode(map);
	}
	public String transferMaterialOrder(SfcMaterialOrder sfcMaterialOrder) {
		BillMain billMain = new BillMain();
		Office office = new Office();
		String deptCode = outsourcingOutputMapper.findDeptCode(sfcMaterialOrder.getShopId());
		office.setCode(deptCode);
		office.setName(sfcMaterialOrder.getShopName());
		billMain.setDept(office);
		billMain.setDeptName(sfcMaterialOrder.getShopName());
		Period period = new Period();
		period.setPeriodId(sfcMaterialOrder.getPeriodId());
		period.setId(period.getPeriodId());
		billMain.setPeriod(period);
		WareHouse ware = new WareHouse();
		ware.setWareID(sfcMaterialOrder.getWareId());
		billMain.setWare(ware);
		billMain.setWareName(sfcMaterialOrder.getWareName());
		Employee emp = new Employee();
		User user = new User();
		user.setNo(sfcMaterialOrder.getWareEmpid());
		emp.setUser(user);
		billMain.setWareEmp(emp);
		billMain.setWareEmpname(sfcMaterialOrder.getWareEmpname());
		billMain.setBillFlag("A");
		billMain.setWorkshopFlag("1");
		BillType ioType=new BillType();
		ioType.setIoType("WO01");
		billMain.setIo(ioType);
		billMain.setBillDate(new Date());

		billMain.setBillSource("L");
		//billMain.setBillPerson(UserUtils.getUser());

		//billMain.setBillPerson(UserUtils.getUser());

		billMain.setBillPerson(UserUtils.get(sfcMaterialOrder.getResponser()));

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String newBillNum = billMainOutsourcingService.getMaxIdByTypeAndDate("wgo" + sdf.format(date));
		if (StringUtils.isEmpty(newBillNum)) {
			newBillNum = "0000";
		} else {
			newBillNum = String.format("%04d", Integer.parseInt(newBillNum.substring(11, 15)) + 1);
		}
		billMain.setBillNum("wgo" + sdf.format(date) + newBillNum);
		List<BillDetail> blist = new ArrayList<>();
		List<SfcMaterialOrderDetail> mlist = sfcMaterialOrder.getSfcMaterialOrderDetailList();
		for(int i=0;i<mlist.size();i++) {
			BillDetail billDetail = new BillDetail();
			billDetail.setItemName(mlist.get(i).getMaterialName());
			billDetail.setItemSpec(mlist.get(i).getMaterialSpec());
			Item item = new Item();
			item.setCode(mlist.get(i).getMaterialId());
			billDetail.setItem(item);
			billDetail.setSerialNum(mlist.get(i).getSequenceId());
			billDetail.setMeasUnit(mlist.get(i).getNumUnit());
			billDetail.setItemQty(mlist.get(i).getRequireNum());
			billDetail.setDelFlag("0");
			billDetail.setId("");
			blist.add(billDetail);
		}

		billMain.setIsGood("1");//1良品，0不良品 默认为良品，只有车间填写的领料单为不良品时才把该字段修改为不良品。
		if("不良品".equals(sfcMaterialOrder.getQualityFlag())){
			billMain.setIsGood("0");
		}
		billMain.setBillDetailList(blist);
		billMainOutsourcingService.save(billMain);
		return billMain.getBillNum();
	}
	public static void main(String[] args) {
		SfcMaterialOrder scfm=new SfcMaterialOrder();
		scfm.setShopId(UserUtils.getUserOfficeCode());
		scfm.setShopName(UserUtils.getUserOfficeName());
		scfm.setPeriodId("201808");
		SfcMaterialOrderDetail detail = new SfcMaterialOrderDetail();
		detail.setMaterialId("010100000001");
		detail.setMaterialName("VTM60NO.1");
		detail.setMaterialSpec("1");
		detail.setNumUnit("件");
		detail.setRequireNum(14.0);
		detail.setSequenceId(1);
		List<SfcMaterialOrderDetail> list = new ArrayList<>();
		list.add(detail);
		scfm.setSfcMaterialOrderDetailList(list);
	}
}
