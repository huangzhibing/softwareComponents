/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.mserialnoprint.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

import java.util.Date;

/**
 * 机器序列号打印Entity
 * @author yxb
 * @version 2019-01-10
 */
public class MSerialNoPrint extends DataEntity<MSerialNoPrint> {
	
	private static final long serialVersionUID = 1L;
	private String mserialno;		// 机器序列号
	private String mpsplanid;		// 主生产计划号
	private String processbillno;		// 车间作业计划号
	private Integer batchno;		// 车间作业计划分批号
	private String routinebillno;		// 加工路线单号
	private String routingcode;		// 工艺编码
	private Integer seqno;		// 加工路线单单件序号
	private String isassigned;		// 已分配标志
	private String prodcode;		// 产品编码
	private String prodspec;   //产品型号
    private Date proddate; //生产日期
	private String prodname;		// 产品名称
    private String objsn;//整机二维码
	
	public MSerialNoPrint() {
		super();
	}

	public MSerialNoPrint(String id){
		super(id);
	}

	@ExcelField(title="机器序列号", align=2, sort=6)
	public String getMserialno() {
		return mserialno;
	}

	public void setMserialno(String mserialno) {
		this.mserialno = mserialno;
	}
	
	@ExcelField(title="主生产计划号", align=2, sort=7)
	public String getMpsplanid() {
		return mpsplanid;
	}

	public void setMpsplanid(String mpsplanid) {
		this.mpsplanid = mpsplanid;
	}
	
	@ExcelField(title="车间作业计划号", align=2, sort=8)
	public String getProcessbillno() {
		return processbillno;
	}

	public void setProcessbillno(String processbillno) {
		this.processbillno = processbillno;
	}
	
	@ExcelField(title="车间作业计划分批号", align=2, sort=9)
	public Integer getBatchno() {
		return batchno;
	}

	public void setBatchno(Integer batchno) {
		this.batchno = batchno;
	}
	
	@ExcelField(title="加工路线单号", align=2, sort=10)
	public String getRoutinebillno() {
		return routinebillno;
	}

	public void setRoutinebillno(String routinebillno) {
		this.routinebillno = routinebillno;
	}
	
	@ExcelField(title="工艺编码", align=2, sort=11)
	public String getRoutingcode() {
		return routingcode;
	}

	public void setRoutingcode(String routingcode) {
		this.routingcode = routingcode;
	}
	
	@ExcelField(title="加工路线单单件序号", align=2, sort=12)
	public Integer getSeqno() {
		return seqno;
	}

	public void setSeqno(Integer seqno) {
		this.seqno = seqno;
	}
	
	@ExcelField(title="已分配标志", align=2, sort=13)
	public String getIsassigned() {
		return isassigned;
	}

	public void setIsassigned(String isassigned) {
		this.isassigned = isassigned;
	}
	
	@ExcelField(title="产品编码", align=2, sort=14)
	public String getProdcode() {
		return prodcode;
	}

	public void setProdcode(String prodcode) {
		this.prodcode = prodcode;
	}
	
	@ExcelField(title="产品名称", align=2, sort=15)
	public String getProdname() {
		return prodname;
	}

	public void setProdname(String prodname) {
		this.prodname = prodname;
	}

    public String getObjsn() {
        return objsn;
    }

    public void setObjsn(String objsn) {
        this.objsn = objsn;
    }

	public String getProdspec() {
		return prodspec;
	}

	public void setProdspec(String prodspec) {
		this.prodspec = prodspec;
	}
	@JsonFormat(pattern = "yyyy-MM-dd")
    public Date getProddate() {
        return proddate;
    }

    public void setProddate(Date proddate) {
        this.proddate = proddate;
    }
}