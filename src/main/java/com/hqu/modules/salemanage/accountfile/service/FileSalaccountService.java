/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.accountfile.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.salemanage.accountfile.entity.FileSalaccount;
import com.hqu.modules.salemanage.accountfile.mapper.FileSalaccountMapper;
import com.hqu.modules.salemanage.accountfile.entity.FileContract;
import com.hqu.modules.salemanage.accountfile.mapper.FileContractMapper;
import com.hqu.modules.salemanage.accountfile.entity.FileLinkMan;
import com.hqu.modules.salemanage.accountfile.mapper.FileLinkManMapper;
import com.hqu.modules.salemanage.accountfile.entity.FilePickBill;
import com.hqu.modules.salemanage.accountfile.mapper.FilePickBillMapper;

/**
 * 客户档案维护Service
 * @author hzb
 * @version 2018-05-16
 */
@Service
@Transactional(readOnly = true)
public class FileSalaccountService extends CrudService<FileSalaccountMapper, FileSalaccount> {

	@Autowired
	private FileContractMapper fileContractMapper;
	@Autowired
	private FileLinkManMapper fileLinkManMapper;
	@Autowired
	private FilePickBillMapper filePickBillMapper;
	@Autowired
	private FileSalaccountMapper fileSalaccountMapper;
	
	public FileSalaccount get(String id) {
		FileSalaccount fileSalaccount = super.get(id);
		fileSalaccount.setFileContractList(fileContractMapper.findList(new FileContract(fileSalaccount)));
		fileSalaccount.setFileLinkManList(fileLinkManMapper.findList(new FileLinkMan(fileSalaccount)));
		fileSalaccount.setFilePickBillList(filePickBillMapper.findList(new FilePickBill(fileSalaccount)));
		return fileSalaccount;
	}
	
	public List<FileSalaccount> findList(FileSalaccount fileSalaccount) {
		return super.findList(fileSalaccount);
	}
	
	public Page<FileSalaccount> findPage(Page<FileSalaccount> page, FileSalaccount fileSalaccount) {
		return super.findPage(page, fileSalaccount);
	}
	
	@Transactional(readOnly = false)
	public void save(FileSalaccount fileSalaccount) {
		super.save(fileSalaccount);
		for (FileContract fileContract : fileSalaccount.getFileContractList()){
			if (fileContract.getId() == null){
				continue;
			}
			if (FileContract.DEL_FLAG_NORMAL.equals(fileContract.getDelFlag())){
				if (StringUtils.isBlank(fileContract.getId())){
					fileContract.setAccount(fileSalaccount);
					fileContract.preInsert();
					fileContractMapper.insert(fileContract);
				}else{
					fileContract.preUpdate();
					fileContractMapper.update(fileContract);
				}
			}else{
				fileContractMapper.delete(fileContract);
			}
		}
		for (FileLinkMan fileLinkMan : fileSalaccount.getFileLinkManList()){
			if (fileLinkMan.getId() == null){
				continue;
			}
			if (FileLinkMan.DEL_FLAG_NORMAL.equals(fileLinkMan.getDelFlag())){
				if (StringUtils.isBlank(fileLinkMan.getId())){
					fileLinkMan.setAccount(fileSalaccount);
					fileLinkMan.preInsert();
					fileLinkManMapper.insert(fileLinkMan);
				}else{
					fileLinkMan.preUpdate();
					fileLinkManMapper.update(fileLinkMan);
				}
			}else{
				fileLinkManMapper.delete(fileLinkMan);
			}
		}
		for (FilePickBill filePickBill : fileSalaccount.getFilePickBillList()){
			if (filePickBill.getId() == null){
				continue;
			}
			if (FilePickBill.DEL_FLAG_NORMAL.equals(filePickBill.getDelFlag())){
				if (StringUtils.isBlank(filePickBill.getId())){
					filePickBill.setAccount(fileSalaccount);
					filePickBill.preInsert();
					filePickBillMapper.insert(filePickBill);
				}else{
					filePickBill.preUpdate();
					filePickBillMapper.update(filePickBill);
				}
			}else{
				filePickBillMapper.delete(filePickBill);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(FileSalaccount fileSalaccount) {
		super.delete(fileSalaccount);
		fileContractMapper.delete(new FileContract(fileSalaccount));
		fileLinkManMapper.delete(new FileLinkMan(fileSalaccount));
		filePickBillMapper.delete(new FilePickBill(fileSalaccount));
	}
	
}