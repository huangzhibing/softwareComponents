/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.salcontract.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 合同附件Entity
 * @author dongqida
 * @version 2018-05-12
 */
public class CtrFIle extends DataEntity<CtrFIle> {
	
	private static final long serialVersionUID = 1L;
	private Contract contract;		// 合同编码 父类
	private String fileCode;		// 附件编号
	private String fileName;		// 附件名称
	private String filePath;		// 附件路径
	private String fileState;		// 附件状态
	
	public CtrFIle() {
		super();
	}

	public CtrFIle(String id){
		super(id);
	}

	public CtrFIle(Contract contract){
		this.contract = contract;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}
	
	@ExcelField(title="附件编号", align=2, sort=8)
	public String getFileCode() {
		return fileCode;
	}

	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}
	
	@ExcelField(title="附件名称", align=2, sort=9)
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@ExcelField(title="附件路径", align=2, sort=10)
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	@ExcelField(title="附件状态", align=2, sort=11)
	public String getFileState() {
		return fileState;
	}

	public void setFileState(String fileState) {
		this.fileState = fileState;
	}
	
}