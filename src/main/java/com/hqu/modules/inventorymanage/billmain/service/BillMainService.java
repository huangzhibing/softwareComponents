/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.billmain.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.hqu.modules.basedata.account.entity.Account;
import com.hqu.modules.basedata.account.mapper.AccountMapper;
import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.basedata.period.mapper.PeriodMapper;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetailCode;
import com.hqu.modules.inventorymanage.billtype.entity.BillType;
import com.hqu.modules.inventorymanage.billtype.mapper.BillTypeMapper;
import com.hqu.modules.inventorymanage.billtype.service.BillTypeService;
import com.hqu.modules.inventorymanage.bin.entity.Bin;
import com.hqu.modules.inventorymanage.bin.mapper.BinMapper;
import com.hqu.modules.inventorymanage.bin.service.BinService;
import com.hqu.modules.inventorymanage.employee.entity.Employee;
import com.hqu.modules.inventorymanage.employee.mapper.EmployeeMapper;
import com.hqu.modules.inventorymanage.employee.service.EmployeeService;
import com.hqu.modules.inventorymanage.location.entity.Location;
import com.hqu.modules.inventorymanage.location.mapper.LocationMapper;
import com.hqu.modules.inventorymanage.use.entity.Use;
import com.hqu.modules.inventorymanage.use.mapper.UseMapper;
import com.hqu.modules.inventorymanage.use.service.UseService;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.hqu.modules.inventorymanage.warehouse.mapper.WareHouseMapper;
import com.hqu.modules.inventorymanage.warehouse.service.WareHouseService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.OfficeMapper;
import com.jeeplus.modules.sys.mapper.UserMapper;
import com.jeeplus.modules.sys.service.OfficeService;
import com.swetake.util.Qrcode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billmain.mapper.BillMainMapper;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetail;
import com.hqu.modules.inventorymanage.billmain.mapper.BillDetailMapper;

/**
 * 出入库单据Service
 * @author M1ngz
 * @version 2018-04-16
 */
@Service
@Transactional(readOnly = true)
public class BillMainService extends CrudService<BillMainMapper, BillMain> {






	@Autowired
	private BillDetailMapper billDetailMapper;
	@Autowired
	private OfficeMapper officeMapper;
	@Autowired
	BillTypeMapper billTypeMapper;
	@Autowired
	WareHouseMapper wareHouseMapper;
	@Autowired
	BinMapper binMapper;
	@Autowired
	LocationMapper locationMapper;
	@Autowired
	EmployeeMapper employeeMapper;
	@Autowired
	UseMapper useMapper;
	@Autowired
	UserMapper userMapper;
	@Autowired
	PeriodMapper periodMapper;
	@Autowired
	AccountMapper accountMapper;
	@Autowired
	BillMainMapper billMainMapper;
	public BillMain get(String id) {
		BillMain billMain = super.get(id);
		if(billMain != null)
		billMain.setBillDetailList(billDetailMapper.findList(new BillDetail(billMain)));
		return billMain;
	}
	public List<BillDetailCode> findCodeList(String billNum){
		return mapper.findCodeList(billNum);
	}
	@Transactional(readOnly = false)
	public void saveBillCode(BillMain billMain,BillDetailCode billDetailCode){
		if(StringUtils.isBlank(billDetailCode.getId())){
			billDetailCode.setBillNum(billMain);
			billDetailCode.preInsert();
			billMainMapper.saveBillCode(billDetailCode);
		}else{
			billDetailCode.setBillNum(billMain);
			billDetailCode.preUpdate();
			billMainMapper.updateBillCode(billDetailCode);
		}
	}


	public List<BillMain> findList(BillMain billMain) {
		return super.findList(billMain);
	}
	
	public Page<BillMain> findPage(Page<BillMain> page, BillMain billMain) {
		return super.findPage(page, billMain);
	}
	
	public Page<BillMain> findPageforCheck(Page<BillMain> page, BillMain billMain) {
		billMain.setPage(page);
		page.setList(billMainMapper.findListforCheck(billMain));
		return page;
	}
	
	public Page<BillMain> findPageforCancelCheck(Page<BillMain> page, BillMain billMain) {
		billMain.setPage(page);
		page.setList(billMainMapper.findListforCancelCheck(billMain));
		return page;
	}
	
	public Page<BillMain> findPageforQuery(Page<BillMain> page, BillMain billMain) {
		billMain.setPage(page);
		page.setList(billMainMapper.findListforQuery(billMain));
		return page;
	}
	
	
	
	@Transactional(readOnly = false)
	public void save(BillMain billMain) {

	/*
	*
	*
		LEFT JOIN sys_office dept ON dept.code = a.dept_id
		LEFT JOIN inv_bill_type io ON io.io_type = a.io_type
		LEFT JOIN inv_warehouse ware ON ware.ware_id = a.ware_id
		LEFT JOIN sys_user billPerson ON billPerson.no = a.bill_person
		LEFT JOIN inv_employee wareEmp ON wareEmp.emp_id = a.ware_empid
		LEFT JOIN inv_use invuse ON invuse.use_id = a.use_id
		LEFT JOIN sys_user recEmp ON recEmp.no = a.rec_empid
		LEFT JOIN sys_user billEmp ON billEmp.no = a.bill_empid
		LEFT JOIN mdm_period period ON period.period_id = a.period_id

	*/
		if(billMain.getDept() != null && billMain.getDept().getCode().length() == 32){
			Office office = officeMapper.get(billMain.getDept().getCode());
			billMain.setDept(office);
		}
		if(billMain.getIo()!= null && billMain.getIo().getIoType().length() == 32){
			BillType billType = billTypeMapper.get(billMain.getIo().getIoType());
			billMain.setIo(billType);
		}
		if(billMain.getWare() != null && billMain.getWare().getWareID().length() == 32){
			WareHouse wareHouse = wareHouseMapper.get(billMain.getWare().getWareID());
			billMain.setWare(wareHouse);
		}
		if(billMain.getBillPerson()!=null && billMain.getBillPerson().getNo().length() == 32){
			User user = userMapper.get(billMain.getBillPerson().getNo());
			billMain.setBillPerson(user);
		}
		if(billMain.getWareEmp() != null && billMain.getWareEmp().getUser().getNo().length() == 32){
			Employee employee = employeeMapper.get(billMain.getWareEmp().getUser().getNo());
			billMain.setWareEmp(employee);
		}
		if(billMain.getInvuse() != null && billMain.getInvuse().getUseId().length() == 32){
			Use use = useMapper.get(billMain.getInvuse().getUseId());
			billMain.setInvuse(use);
		}
		if(billMain.getRecEmp() != null && billMain.getRecEmp().getNo().length() == 32){
			User user = userMapper.get(billMain.getRecEmp().getNo());
			billMain.setRecEmp(user);
		}
		if(billMain.getBillEmp() != null && billMain.getBillEmp().getNo().length() == 32){
			User user = userMapper.get(billMain.getBillEmp().getNo());
			billMain.setBillEmp(user);
		}
		if(billMain.getPeriod() != null && billMain.getPeriod().getId().length() == 32){
			Period period = periodMapper.get(billMain.getPeriod().getId());
			billMain.setPeriod(period);
		}
		if(billMain.getAccount() != null && billMain.getAccount().getAccountCode().length() == 32){
			Account account = accountMapper.get(billMain.getAccount().getAccountCode());
			billMain.setAccount(account);
		}
		super.save(billMain);

		for (BillDetail billDetail : billMain.getBillDetailList()){
			if (billDetail.getId() == null){
				continue;
			}
			if (BillDetail.DEL_FLAG_NORMAL.equals(billDetail.getDelFlag())){
				if (StringUtils.isBlank(billDetail.getId())){
					billDetail.setBillNum(billMain);
					billDetail.preInsert();
					billDetailMapper.insert(billDetail);
				}else{
					billDetail.preUpdate();
					billDetailMapper.update(billDetail);
				}
			}else{
				billDetailMapper.delete(billDetail);
			}
		}
	}
	@Transactional(readOnly = false)
	public void delete(BillMain billMain) {
		super.delete(billMain);
		billDetailMapper.delete(new BillDetail(billMain));
		BillDetailCode billDetailCode =new BillDetailCode(billMain);
		billMainMapper.deleteBillCode(billDetailCode);
	}


	public 	String getMaxIdByTypeAndDate(String para){
	    return mapper.getMaxIdByTypeAndDate(para);
    }

    public Period findPeriodByTime(Date date) {

        Map<String, Object> map = Maps.newHashMap();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("today", sdf1.format(date));
        return mapper.findPeriodByTime(map);
    }

}