package com.hqu.modules.costmanage.report.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.hqu.modules.basedata.account.entity.Account;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetail;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetailCode;
import com.hqu.modules.inventorymanage.billtype.entity.BillType;
import com.hqu.modules.inventorymanage.employee.entity.Employee;
import com.hqu.modules.inventorymanage.use.entity.Use;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class Report extends DataEntity<Report> {
    private static final long serialVersionUID = 1L;
    private String billNum;		// 单据号
    private Date billDate;		// 出入库日期
    private Office dept;		// 部门编码
    private String deptName;		// 部门名称
    private BillType io;		// 单据类型
    private String ioDesc;		// 单据名称
    private String ioFlag;		// 单据标记
    private WareHouse ware;		// 库房号
    private String wareName;		// 库房名称
    private User billPerson;		// 经办人
    private Employee wareEmp;		// 库管员代码
    private String wareEmpname;		// 库管员名称
    private String corBillNum;		// 对应单据号
    private String corId;		// 来往单位代码
    private String corName;		// 来往单位名称
    private Use invuse;		// 用途/车间班组代码
    private String useName;		// 用途/车间班组名称
    private String billFlag;		// 过账标志
    private String estimateFlag;		// 估价标志
    private Period period;		// 核算期间
    private String billDesc;		// 单据说明
    private User recEmp;		// 记账人编码
    private String recEmpname;		// 记账人
    private Date recDate;		// 记账日期
    private User billEmp;		// 制单人编码
    private String billEmpname;		// 制单人
    private String note;		// 备注
    private Integer orderCode;		// 接口序号
    private Double taxRatio;		// 税率
    private String inoId;		// 财务凭证号
    private String classId;		// 科目代码
    private String classDesc;		// 科目名称
    private String cflag;		// 传财务标志
    private Integer fnumber;		// 发票号
    private String finvoiceID;		// 发票内码
    private String billType;		// 冲估类型
    private String redFlag;		// 红单标志
    private String cgFlag;		// 冲估标志
    private Double totalSum;		// 金额合计
    private Date operDate;		// 业务日期
    private String billSource;		// 单据来源
    private String cgNum;		// 冲估单号
    private String workPoscode;		// 工位代码
    private String wordPosname;		// 工位名称
    private String rcvAddr;		// 收货地址
    private String transAccount;		// 承运人
    private String carNum;		// 承运车号
    private Account account;		// 供应商编码
    private String accountName;		// 供应商名称
    private Date beginBillDate;		// 开始 出入库日期
    private Date endBillDate;		// 结束 出入库日期
    private Date beginRecDate;		// 开始 记账日期
    private Date endRecDate;		// 结束 记账日期
    private List<BillDetail> billDetailList = Lists.newArrayList();		// 子表列表
    private List<BillDetailCode> billDetailCodeList = Lists.newArrayList();
    private String itemCode;  //记录查询条件物料id
    private String userId;
    private String workshopFlag; //车间转换标识
    private String cosBillStatus;		// 单据处理状态
    private Item item;  //关联子表物料
    private String itemQty;
    private String itemPrice;
    private String itemSum;
    private String orderNum;
    private String orderId;

    public String getWorkshopFlag() {
        return workshopFlag;
    }

    public void setWorkshopFlag(String workshopFlag) {
        this.workshopFlag = workshopFlag;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Report() {
        super();
    }

    public Report(String id){
        super(id);
    }


    @ExcelField(title="单据号", align=2, sort=7)
    public String getBillNum() {
        return billNum;
    }

    public void setBillNum(String billNum) {
        this.billNum = billNum;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message="出入库日期不能为空")
    @ExcelField(title="出入库日期", align=2, sort=8)
    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    @ExcelField(title="部门编码", fieldType=Office.class, value="dept.code", align=2, sort=9)
    public Office getDept() {
        return dept;
    }

    public void setDept(Office dept) {
        this.dept = dept;
    }

    @ExcelField(title="部门名称", align=2, sort=10)
    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @NotNull(message="单据类型不能为空")
    @ExcelField(title="单据类型", fieldType=BillType.class, value="io.ioType", align=2, sort=11)
    public BillType getIo() {
        return io;
    }

    public void setIo(BillType io) {
        this.io = io;
    }

    @ExcelField(title="单据名称", align=2, sort=12)
    public String getIoDesc() {
        return ioDesc;
    }

    public void setIoDesc(String ioDesc) {
        this.ioDesc = ioDesc;
    }

    @ExcelField(title="单据标记", dictType="ioFlag", align=2, sort=13)
    public String getIoFlag() {
        return ioFlag;
    }

    public void setIoFlag(String ioFlag) {
        this.ioFlag = ioFlag;
    }

    @NotNull(message="库房号不能为空")
    @ExcelField(title="库房号", fieldType=WareHouse.class, value="ware.wareID", align=2, sort=14)
    public WareHouse getWare() {
        return ware;
    }

    public void setWare(WareHouse ware) {
        this.ware = ware;
    }

    @ExcelField(title="库房名称", align=2, sort=15)
    public String getWareName() {
        return wareName;
    }

    public void setWareName(String wareName) {
        this.wareName = wareName;
    }

    @ExcelField(title="经办人", fieldType=User.class, value="billPerson.no", align=2, sort=16)
    public User getBillPerson() {
        return billPerson;
    }

    public void setBillPerson(User billPerson) {
        this.billPerson = billPerson;
    }

    @ExcelField(title="库管员代码", fieldType=Employee.class, value="wareEmp.empID", align=2, sort=17)
    public Employee getWareEmp() {
        return wareEmp;
    }

    public void setWareEmp(Employee wareEmp) {
        this.wareEmp = wareEmp;
    }

    @ExcelField(title="库管员名称", align=2, sort=18)
    public String getWareEmpname() {
        return wareEmpname;
    }

    public void setWareEmpname(String wareEmpname) {
        this.wareEmpname = wareEmpname;
    }

    @ExcelField(title="对应单据号", align=2, sort=19)
    public String getCorBillNum() {
        return corBillNum;
    }

    public void setCorBillNum(String corBillNum) {
        this.corBillNum = corBillNum;
    }

    @ExcelField(title="来往单位代码", align=2, sort=20)
    public String getCorId() {
        return corId;
    }

    public void setCorId(String corId) {
        this.corId = corId;
    }

    @ExcelField(title="来往单位名称", align=2, sort=21)
    public String getCorName() {
        return corName;
    }

    public void setCorName(String corName) {
        this.corName = corName;
    }

    @ExcelField(title="用途/车间班组代码", fieldType=Use.class, value="invuse.useId", align=2, sort=22)
    public Use getInvuse() {
        return invuse;
    }

    public void setInvuse(Use invuse) {
        this.invuse = invuse;
    }

    @ExcelField(title="用途/车间班组名称", align=2, sort=23)
    public String getUseName() {
        return useName;
    }

    public void setUseName(String useName) {
        this.useName = useName;
    }

    @ExcelField(title="过账标志", dictType="billFlag", align=2, sort=24)
    public String getBillFlag() {
        return billFlag;
    }

    public void setBillFlag(String billFlag) {
        this.billFlag = billFlag;
    }

    @ExcelField(title="估价标志", dictType="estimateFlag", align=2, sort=25)
    public String getEstimateFlag() {
        return estimateFlag;
    }

    public void setEstimateFlag(String estimateFlag) {
        this.estimateFlag = estimateFlag;
    }



    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    @ExcelField(title="单据说明", align=2, sort=27)
    public String getBillDesc() {
        return billDesc;
    }

    public void setBillDesc(String billDesc) {
        this.billDesc = billDesc;
    }

    @ExcelField(title="记账人编码", fieldType=User.class, value="recEmp.no", align=2, sort=28)
    public User getRecEmp() {
        return recEmp;
    }

    public void setRecEmp(User recEmp) {
        this.recEmp = recEmp;
    }

    @ExcelField(title="记账人", align=2, sort=29)
    public String getRecEmpname() {
        return recEmpname;
    }

    public void setRecEmpname(String recEmpname) {
        this.recEmpname = recEmpname;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title="记账日期", align=2, sort=30)
    public Date getRecDate() {
        return recDate;
    }

    public void setRecDate(Date recDate) {
        this.recDate = recDate;
    }

    @NotNull(message="制单人编码不能为空")
    @ExcelField(title="制单人编码", fieldType=User.class, value="billEmp.no", align=2, sort=31)
    public User getBillEmp() {
        return billEmp;
    }

    public void setBillEmp(User billEmp) {
        this.billEmp = billEmp;
    }

    @ExcelField(title="制单人", align=2, sort=32)
    public String getBillEmpname() {
        return billEmpname;
    }

    public void setBillEmpname(String billEmpname) {
        this.billEmpname = billEmpname;
    }

    @ExcelField(title="备注", align=2, sort=33)
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @NotNull(message="接口序号不能为空")
    @ExcelField(title="接口序号", align=2, sort=34)
    public Integer getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(Integer orderCode) {
        this.orderCode = orderCode;
    }

    @ExcelField(title="税率", align=2, sort=35)
    public Double getTaxRatio() {
        return taxRatio;
    }

    public void setTaxRatio(Double taxRatio) {
        this.taxRatio = taxRatio;
    }

    @ExcelField(title="财务凭证号", align=2, sort=36)
    public String getInoId() {
        return inoId;
    }

    public void setInoId(String inoId) {
        this.inoId = inoId;
    }

    @ExcelField(title="科目代码", align=2, sort=37)
    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @ExcelField(title="科目名称", align=2, sort=38)
    public String getClassDesc() {
        return classDesc;
    }

    public void setClassDesc(String classDesc) {
        this.classDesc = classDesc;
    }

    @ExcelField(title="传财务标志", dictType="cflag", align=2, sort=39)
    public String getCflag() {
        return cflag;
    }

    public void setCflag(String cflag) {
        this.cflag = cflag;
    }

    @ExcelField(title="发票号", align=2, sort=40)
    public Integer getFnumber() {
        return fnumber;
    }

    public void setFnumber(Integer fnumber) {
        this.fnumber = fnumber;
    }

    @ExcelField(title="发票内码", align=2, sort=41)
    public String getFinvoiceID() {
        return finvoiceID;
    }

    public void setFinvoiceID(String finvoiceID) {
        this.finvoiceID = finvoiceID;
    }

    @ExcelField(title="冲估类型", dictType="cgType", align=2, sort=42)
    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    @ExcelField(title="红单标志", align=2, sort=43)
    public String getRedFlag() {
        return redFlag;
    }

    public void setRedFlag(String redFlag) {
        this.redFlag = redFlag;
    }

    @ExcelField(title="冲估标志", align=2, sort=44)
    public String getCgFlag() {
        return cgFlag;
    }

    public void setCgFlag(String cgFlag) {
        this.cgFlag = cgFlag;
    }

    @ExcelField(title="金额合计", align=2, sort=45)
    public Double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(Double totalSum) {
        this.totalSum = totalSum;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title="业务日期", align=2, sort=46)
    public Date getOperDate() {
        return operDate;
    }

    public void setOperDate(Date operDate) {
        this.operDate = operDate;
    }

    @ExcelField(title="单据来源", dictType="billSource", align=2, sort=47)
    public String getBillSource() {
        return billSource;
    }

    public void setBillSource(String billSource) {
        this.billSource = billSource;
    }

    @ExcelField(title="冲估单号", align=2, sort=48)
    public String getCgNum() {
        return cgNum;
    }

    public void setCgNum(String cgNum) {
        this.cgNum = cgNum;
    }

    @ExcelField(title="工位代码", align=2, sort=49)
    public String getWorkPoscode() {
        return workPoscode;
    }

    public void setWorkPoscode(String workPoscode) {
        this.workPoscode = workPoscode;
    }

    @ExcelField(title="工位名称", align=2, sort=50)
    public String getWordPosname() {
        return wordPosname;
    }

    public void setWordPosname(String wordPosname) {
        this.wordPosname = wordPosname;
    }

    public Date getBeginBillDate() {
        return beginBillDate;
    }

    public void setBeginBillDate(Date beginBillDate) {
        this.beginBillDate = beginBillDate;
    }

    public Date getEndBillDate() {
        return endBillDate;
    }

    public void setEndBillDate(Date endBillDate) {
        this.endBillDate = endBillDate;
    }

    public Date getBeginRecDate() {
        return beginRecDate;
    }

    public void setBeginRecDate(Date beginRecDate) {
        this.beginRecDate = beginRecDate;
    }

    public Date getEndRecDate() {
        return endRecDate;
    }

    public void setEndRecDate(Date endRecDate) {
        this.endRecDate = endRecDate;
    }

    public List<BillDetail> getBillDetailList() {
        return billDetailList;
    }

    public void setBillDetailList(List<BillDetail> billDetailList) {
        this.billDetailList = billDetailList;
    }

    public List<BillDetailCode> getBillDetailCodeList() {
        return billDetailCodeList;
    }

    public void setBillDetailCodeList(List<BillDetailCode> billDetailCodeList) {
        this.billDetailCodeList = billDetailCodeList;
    }

    public String getTransAccount() {
        return transAccount;
    }

    public void setTransAccount(String transAccount) {
        this.transAccount = transAccount;
    }

    public String getRcvAddr() {
        return rcvAddr;
    }

    public void setRcvAddr(String rcvAddr) {
        this.rcvAddr = rcvAddr;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    @ExcelField(title="供应商编码", fieldType=Account.class, value="")
    public Account getAccount() { return account; }

    public void setAccount(Account account) { this.account = account; }
    @ExcelField(title="供应商名称")
    public String getAccountName() { return accountName; }

    public void setAccountName(String accountName) { this.accountName = accountName; }

    @ExcelField(title="单据处理状态", align=2, sort=10)
    public String getCosBillStatus() {
        return cosBillStatus;
    }

    public void setCosBillStatus(String cosBillStatus) {
        this.cosBillStatus = cosBillStatus;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getItemQty() {
        return itemQty;
    }

    public void setItemQty(String itemQty) {
        this.itemQty = itemQty;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemSum() {
        return itemSum;
    }

    public void setItemSum(String itemSum) {
        this.itemSum = itemSum;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
