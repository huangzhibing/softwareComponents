/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.warehousemaintenance.countdata.entity;

import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import javax.validation.constraints.NotNull;
import com.hqu.modules.inventorymanage.bin.entity.Bin;
import com.hqu.modules.inventorymanage.location.entity.Location;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.sys.entity.User;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 盘点数据录入Entity
 * @author zb
 * @version 2018-04-23
 */
public class CountMain extends DataEntity<CountMain> {
	
	private static final long serialVersionUID = 1L;
	private String billNum;		// 盘点单号
	private WareHouse ware;		// 仓库代码
	private String wareName;		// 仓库名称
	private String batchFlag;		// 批次管理标志
	private Bin bin;		// 货区代码
	private String binName;		// 货区名称
	private Location loc;		// 货位代码
	private String locName;		// 货位名称
	private Date checkDate;		// 盘点日期
	private User checkEmpId;		// 盘点人
	private User adjEmpId;		// 调账人
	private String procFlag;		// 处理标志
	private String note;		// 备注
	private Date beginCheckDate;		// 开始 盘点日期
	private Date endCheckDate;		// 结束 盘点日期
	private List<CountDetail> countDetailList = Lists.newArrayList();		// 子表列表
	
	public CountMain() {
		super();
	}

	public CountMain(String id){
		super(id);
	}

	@ExcelField(title="盘点单号", align=2, sort=7)
	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	
	@NotNull(message="仓库代码不能为空")
	@ExcelField(title="仓库代码", fieldType=WareHouse.class, value="ware.wareName", align=2, sort=8)
	public WareHouse getWare() {
		return ware;
	}

	public void setWare(WareHouse ware) {
		this.ware = ware;
	}
	
	@ExcelField(title="仓库名称", align=2, sort=9)
	public String getWareName() {
		return wareName;
	}

	public void setWareName(String wareName) {
		this.wareName = wareName;
	}
	
	@ExcelField(title="批次管理标志", dictType="batchFlag", align=2, sort=10)
	public String getBatchFlag() {
		return batchFlag;
	}

	public void setBatchFlag(String batchFlag) {
		this.batchFlag = batchFlag;
	}
	
	@ExcelField(title="货区代码", fieldType=Bin.class, value="bin.binId", align=2, sort=11)
	public Bin getBin() {
		return bin;
	}

	public void setBin(Bin bin) {
		this.bin = bin;
	}
	
	@ExcelField(title="货区名称", align=2, sort=12)
	public String getBinName() {
		return binName;
	}

	public void setBinName(String binName) {
		this.binName = binName;
	}
	
	@ExcelField(title="货位代码", fieldType=Location.class, value="loc.locId", align=2, sort=13)
	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}
	
	@ExcelField(title="货位名称", align=2, sort=14)
	public String getLocName() {
		return locName;
	}

	public void setLocName(String locName) {
		this.locName = locName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="盘点日期不能为空")
	@ExcelField(title="盘点日期", align=2, sort=15)
	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	
	@NotNull(message="盘点人不能为空")
	@ExcelField(title="盘点人", fieldType=User.class, value="checkEmpId.name", align=2, sort=16)
	public User getCheckEmpId() {
		return checkEmpId;
	}

	public void setCheckEmpId(User checkEmpId) {
		this.checkEmpId = checkEmpId;
	}
	
	@ExcelField(title="调账人", fieldType=User.class, value="adjEmpId.name", align=2, sort=17)
	public User getAdjEmpId() {
		return adjEmpId;
	}

	public void setAdjEmpId(User adjEmpId) {
		this.adjEmpId = adjEmpId;
	}
	
	@ExcelField(title="处理标志", dictType="procFlag", align=2, sort=18)
	public String getProcFlag() {
		return procFlag;
	}

	public void setProcFlag(String procFlag) {
		this.procFlag = procFlag;
	}
	
	@ExcelField(title="备注", align=2, sort=19)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public Date getBeginCheckDate() {
		return beginCheckDate;
	}

	public void setBeginCheckDate(Date beginCheckDate) {
		this.beginCheckDate = beginCheckDate;
	}
	
	public Date getEndCheckDate() {
		return endCheckDate;
	}

	public void setEndCheckDate(Date endCheckDate) {
		this.endCheckDate = endCheckDate;
	}
		
	public List<CountDetail> getCountDetailList() {
		return countDetailList;
	}

	public void setCountDetailList(List<CountDetail> countDetailList) {
		this.countDetailList = countDetailList;
	}
}