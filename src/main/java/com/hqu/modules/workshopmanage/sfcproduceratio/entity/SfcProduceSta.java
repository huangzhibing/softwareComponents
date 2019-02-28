/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.sfcproduceratio.entity;

import java.util.Date;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 车间产出量统计Entity
 * @author yxb
 * @version 2018-11-25
 */
public class SfcProduceSta extends DataEntity<SfcProduceSta> {
	
	private static final long serialVersionUID = 1L;
	private String no;		// NO(工作中心)
	private Integer planNum;		// 标准产出
	private Integer actualNum;		// 实际产出
	private Double accRatio;		// 达成率
	private Integer failNum;		// 品质异常
	private String failPro;		// 异常项目
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	
	public SfcProduceSta() {
		super();
	}

	public SfcProduceSta(String id){
		super(id);
	}

	@ExcelField(title="NO(工作中心)", align=2, sort=7)
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}
	
	@ExcelField(title="标准产出", align=2, sort=8)
	public Integer getPlanNum() {
		return planNum;
	}

	public void setPlanNum(Integer planNum) {
		this.planNum = planNum;
	}
	
	@ExcelField(title="实际产出", align=2, sort=9)
	public Integer getActualNum() {
		return actualNum;
	}

	public void setActualNum(Integer actualNum) {
		this.actualNum = actualNum;
	}
	
	@ExcelField(title="达成率", align=2, sort=10)
	public Double getAccRatio() {
		return accRatio;
	}

	public void setAccRatio(Double accRatio) {
		this.accRatio = accRatio;
	}
	
	@ExcelField(title="品质异常", align=2, sort=11)
	public Integer getFailNum() {
		return failNum;
	}

	public void setFailNum(Integer failNum) {
		this.failNum = failNum;
	}
	
	@ExcelField(title="异常项目", align=2, sort=12)
	public String getFailPro() {
		return failPro;
	}

	public void setFailPro(String failPro) {
		this.failPro = failPro;
	}
	
	public Date getBeginCreateDate() {
		return beginCreateDate;
	}

	public void setBeginCreateDate(Date beginCreateDate) {
		this.beginCreateDate = beginCreateDate;
	}
	
	public Date getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate = endCreateDate;
	}
		
}