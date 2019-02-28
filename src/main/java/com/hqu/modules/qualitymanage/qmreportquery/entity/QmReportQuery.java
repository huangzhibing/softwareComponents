/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmreportquery.entity;

import com.jeeplus.modules.sys.entity.User;
import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hqu.modules.qualitymanage.matterhandling.entity.MatterHandling;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 查询质量问题报告Entity
 * @author yangxianbang
 * @version 2018-04-17
 */
public class QmReportQuery extends DataEntity<QmReportQuery> {
	
	private static final long serialVersionUID = 1L;
	private String sn;		// 序号
	private String qmreportId;		// 问题处理报告单编号
	private User qmPerson;		// 问题处理人
	private Date qmDate;		// 问题处理日期
	private String mhandlingCode;		// 问题处理编码
	private MatterHandling mhandlingName;		// 问题处理意见
	private String state;		// 单据状态
	private String processInstanceId;		// 工作流程处理ID
	private Date beginQmDate;		// 开始 问题处理日期
	private Date endQmDate;		// 结束 问题处理日期
	private String qmType; //检验类型 采购/装配
	private List<QmReportRSnQuery> qmReportRSnQueryList = Lists.newArrayList();		// 子表列表

	private String purreportId; //检验单编号

	/*搜索临时变量*/
	private String isDeal;//是否已处理

	public String getIsDeal() {
		return isDeal;
	}

	public void setIsDeal(String isDeal) {
		this.isDeal = isDeal;
	}

	public String getPurreportId() {
		return purreportId;
	}

	public void setPurreportId(String purreportId) {
		this.purreportId = purreportId;
	}

	public String getQmType() {
		return qmType;
	}

	public void setQmType(String qmType) {
		this.qmType = qmType;
	}

	public QmReportQuery() {
		super();
	}

	public QmReportQuery(String id){
		super(id);
	}

	@ExcelField(title="序号", align=2, sort=6)
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
	
	@ExcelField(title="问题处理报告单编号", align=2, sort=7)
	public String getQmreportId() {
		return qmreportId;
	}

	public void setQmreportId(String qmreportId) {
		this.qmreportId = qmreportId;
	}
	
	@NotNull(message="问题处理人不能为空")
	@ExcelField(title="问题处理人", fieldType=User.class, value="qmPerson.name", align=2, sort=8)
	public User getQmPerson() {
		return qmPerson;
	}

	public void setQmPerson(User qmPerson) {
		this.qmPerson = qmPerson;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="问题处理日期不能为空")
	@ExcelField(title="问题处理日期", align=2, sort=9)
	public Date getQmDate() {
		return qmDate;
	}

	public void setQmDate(Date qmDate) {
		this.qmDate = qmDate;
	}
	
	@ExcelField(title="问题处理编码", align=2, sort=10)
	public String getMhandlingCode() {
		return mhandlingCode;
	}

	public void setMhandlingCode(String mhandlingCode) {
		this.mhandlingCode = mhandlingCode;
	}
	
	@NotNull(message="问题处理意见不能为空")
	@ExcelField(title="问题处理意见", fieldType=MatterHandling.class, value="mhandlingName.mhandlingname", align=2, sort=11)
	public MatterHandling getMhandlingName() {
		return mhandlingName;
	}

	public void setMhandlingName(MatterHandling mhandlingName) {
		this.mhandlingName = mhandlingName;
	}
	
	@ExcelField(title="单据状态", dictType="done_or_not", align=2, sort=12)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	@ExcelField(title="工作流程处理ID", align=2, sort=14)
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	public Date getBeginQmDate() {
		return beginQmDate;
	}

	public void setBeginQmDate(Date beginQmDate) {
		this.beginQmDate = beginQmDate;
	}
	
	public Date getEndQmDate() {
		return endQmDate;
	}

	public void setEndQmDate(Date endQmDate) {
		this.endQmDate = endQmDate;
	}
		
	public List<QmReportRSnQuery> getQmReportRSnQueryList() {
		return qmReportRSnQueryList;
	}

	public void setQmReportRSnQueryList(List<QmReportRSnQuery> qmReportRSnQueryList) {
		this.qmReportRSnQueryList = qmReportRSnQueryList;
	}
}