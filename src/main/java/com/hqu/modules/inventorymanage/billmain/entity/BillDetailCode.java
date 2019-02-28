package com.hqu.modules.inventorymanage.billmain.entity;

import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.inventorymanage.bin.entity.Bin;
import com.hqu.modules.inventorymanage.location.entity.Location;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;
import com.swetake.util.Qrcode;

import javax.validation.constraints.NotNull;

public class BillDetailCode extends DataEntity<BillDetailCode> {

    private static final long serialVersionUID = 1L;
    private BillMain billNum;		// 单据号 父类
    private Item item;    //物料编号
    private Integer serialNum;		// 序号
    private String itemBarcode;     //物料二维码
    private String supBarcode;    //供应商二维码
    private Bin bin;		// 货区号
    private String binName;		// 货区名称
    private Location loc;		// 货位号
    private String locName;		// 货位名称
    private String machineBarcode;
    private String activationBarcode;
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    public BillDetailCode() {
        super();
    }

    public BillDetailCode(String id){
        super(id);
    }

    public BillDetailCode(BillMain billNum){
        this.billNum = billNum;
    }
    @NotNull(message="单据编号不能为空")
    @ExcelField(title="单据编号", align=2, sort=1)
    public BillMain getBillNum() {
        return billNum;
    }

    public void setBillNum(BillMain billNum) {
        this.billNum = billNum;
    }
    @NotNull(message="物料代码不能为空")
    @ExcelField(title="物料代码", align=2, sort=2)
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
    @NotNull(message="序号不能为空")
    @ExcelField(title="序号", align=2, sort=3)
    public Integer getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(Integer serialNum) {
        this.serialNum = serialNum;
    }
    @NotNull(message="物料二维码不能为空")
    @ExcelField(title="物料二维码", align=2, sort=4)
    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }
    @NotNull(message="供应商二维码不能为空")
    @ExcelField(title="供应商二维码", align=2, sort=5)
    public String getSupBarcode() {
        return supBarcode;
    }

    public void setSupBarcode(String supBarcode) {
        this.supBarcode = supBarcode;
    }
    @NotNull(message="货区编码不能为空")
    @ExcelField(title="获取编码", align=2, sort=6)
    public Bin getBin() {
        return bin;
    }

    public void setBin(Bin bin) {
        this.bin = bin;
    }

    public String getBinName() {
        return binName;
    }

    public void setBinName(String binName) {
        this.binName = binName;
    }
    @NotNull(message="货位编码不能为空")
    @ExcelField(title="货位编码", align=2, sort=7)
    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    public String getMachineBarcode() {
        return machineBarcode;
    }

    public String getActivationBarcode() {
        return activationBarcode;
    }

    public void setActivationBarcode(String activationBarcode) {
        this.activationBarcode = activationBarcode;
    }

    public void setMachineBarcode(String machineBarcode) {
        this.machineBarcode = machineBarcode;
    }
}
