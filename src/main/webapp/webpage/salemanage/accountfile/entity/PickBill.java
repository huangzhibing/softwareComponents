/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.accountfile.entity;

import com.hqu.modules.salemanage.salcontract.entity.Contract;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;
import com.jeeplus.modules.sys.entity.User;
import com.hqu.modules.basedata.account.entity.Account;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.hqu.modules.inventorymanage.billtype.entity.BillType;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 销售发货单主表Entity
 * @author hzb
 * @version 2018-05-15
 */
public class PickBill extends DataEntity<PickBill> {
	
	private static final long serialVersionUID = 1L;
	private String pbillCode;		// 发货单编码
	private Contract contract;		// 合同编码
	private Date pbillDate;		// 制单日期
	private User pbillPerson;		// 制单人编码
	private String pbillPsnName;		// 制单人姓名
	private Salaccount rcvAccount;		// 收货客户编码 父类
	private String rcvAccountName;		// 收货客户名称
	private String rcvAddr;		// 收货地址
	private String rcvPerson;		// 收货人
	private String rcvTel;		// 收货人电话
	private String pbillStat;		// 发货单状态
	private String transAccount;		// 承运人
	private Date pickDate;		// 发货日期
	private String qmsFlag;		// 质检标志
	private WareHouse ware;		// 仓库编号
	private String wareName;		// 仓库名称
	private BillType io;		// 出库类型
	private String iodesc;		// 出库类型描述
	private String passFlag;		// 生成出库单标记
	private String outBillCode;		// 出货单编码
	private String processInstanceId;		// 流程实例ID
	private Date beginPbillDate;		// 开始 制单日期
	private Date endPbillDate;		// 结束 制单日期
	private Date beginPickDate;		// 开始 发货日期
	private Date endPickDate;		// 结束 发货日期
	
	public PickBill() {
		super();
	}

	public PickBill(String id){
		super(id);
	}

	public PickBill(Salaccount rcvAccount){
		this.rcvAccount = rcvAccount;
	}

	@ExcelField(title="发货单编码", align=2, sort=7)
	public String getPbillCode() {
		return pbillCode;
	}

	public void setPbillCode(String pbillCode) {
		this.pbillCode = pbillCode;
	}
	
	@ExcelField(title="合同编码", fieldType=Contract.class, value="contract.contractCode", align=2, sort=8)
	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="制单日期不能为空")
	@ExcelField(title="制单日期", align=2, sort=9)
	public Date getPbillDate() {
		return pbillDate;
	}

	public void setPbillDate(Date pbillDate) {
		this.pbillDate = pbillDate;
	}
	
	@NotNull(message="制单人编码不能为空")
	@ExcelField(title="制单人编码", fieldType=User.class, value="pbillPerson.no", align=2, sort=10)
	public User getPbillPerson() {
		return pbillPerson;
	}

	public void setPbillPerson(User pbillPerson) {
		this.pbillPerson = pbillPerson;
	}
	
	@ExcelField(title="制单人姓名", align=2, sort=11)
	public String getPbillPsnName() {
		return pbillPsnName;
	}

	public void setPbillPsnName(String pbillPsnName) {
		this.pbillPsnName = pbillPsnName;
	}
	
	public Salaccount getRcvAccount() {
		return rcvAccount;
	}

	public void setRcvAccount(Salaccount rcvAccount) {
		this.rcvAccount = rcvAccount;
	}
	
	@ExcelField(title="收货客户名称", align=2, sort=13)
	public String getRcvAccountName() {
		return rcvAccountName;
	}

	public void setRcvAccountName(String rcvAccountName) {
		this.rcvAccountName = rcvAccountName;
	}
	
	@ExcelField(title="收货地址", align=2, sort=14)
	public String getRcvAddr() {
		return rcvAddr;
	}

	public void setRcvAddr(String rcvAddr) {
		this.rcvAddr = rcvAddr;
	}
	
	@ExcelField(title="收货人", align=2, sort=15)
	public String getRcvPerson() {
		return rcvPerson;
	}

	public void setRcvPerson(String rcvPerson) {
		this.rcvPerson = rcvPerson;
	}
	
	@ExcelField(title="收货人电话", align=2, sort=16)
	public String getRcvTel() {
		return rcvTel;
	}

	public void setRcvTel(String rcvTel) {
		this.rcvTel = rcvTel;
	}
	
	@ExcelField(title="发货单状态", dictType="pbillStat", align=2, sort=17)
	public String getPbillStat() {
		return pbillStat;
	}

	public void setPbillStat(String pbillStat) {
		this.pbillStat = pbillStat;
	}
	
	@ExcelField(title="承运人", align=2, sort=18)
	public String getTransAccount() {
		return transAccount;
	}

	public void setTransAccount(String transAccount) {
		this.transAccount = transAccount;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="发货日期", align=2, sort=19)
	public Date getPickDate() {
		return pickDate;
	}

	public void setPickDate(Date pickDate) {
		this.pickDate = pickDate;
	}
	
	@ExcelField(title="质检标志", dictType="qmsFlagSal", align=2, sort=20)
	public String getQmsFlag() {
		return qmsFlag;
	}

	public void setQmsFlag(String qmsFlag) {
		this.qmsFlag = qmsFlag;
	}
	
	@ExcelField(title="仓库编号", align=2, sort=21)
	public WareHouse getWare() {
		return ware;
	}

	public void setWare(WareHouse ware) {
		this.ware = ware;
	}
	
	@ExcelField(title="仓库名称", align=2, sort=22)
	public String getWareName() {
		return wareName;
	}

	public void setWareName(String wareName) {
		this.wareName = wareName;
	}
	
	@ExcelField(title="出库类型", align=2, sort=23)
	public BillType getIo() {
		return io;
	}

	public void setIo(BillType io) {
		this.io = io;
	}
	
	@ExcelField(title="出库类型描述", align=2, sort=24)
	public String getIodesc() {
		return iodesc;
	}

	public void setIodesc(String iodesc) {
		this.iodesc = iodesc;
	}
	
	@ExcelField(title="生成出库单标记", dictType="passFlag", align=2, sort=25)
	public String getPassFlag() {
		return passFlag;
	}

	public void setPassFlag(String passFlag) {
		this.passFlag = passFlag;
	}
	
	@ExcelField(title="出货单编码", align=2, sort=26)
	public String getOutBillCode() {
		return outBillCode;
	}

	public void setOutBillCode(String outBillCode) {
		this.outBillCode = outBillCode;
	}
	
	@ExcelField(title="流程实例ID", align=2, sort=27)
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	public Date getBeginPbillDate() {
		return beginPbillDate;
	}

	public void setBeginPbillDate(Date beginPbillDate) {
		this.beginPbillDate = beginPbillDate;
	}
	
	public Date getEndPbillDate() {
		return endPbillDate;
	}

	public void setEndPbillDate(Date endPbillDate) {
		this.endPbillDate = endPbillDate;
	}
		
	public Date getBeginPickDate() {
		return beginPickDate;
	}

	public void setBeginPickDate(Date beginPickDate) {
		this.beginPickDate = beginPickDate;
	}
	
	public Date getEndPickDate() {
		return endPickDate;
	}

	public void setEndPickDate(Date endPickDate) {
		this.endPickDate = endPickDate;
	}
		
}