/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreport.service;

import java.io.Console;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.poi.util.SystemOutLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.qualitymanage.purreport.entity.PurReport;
import com.hqu.modules.qualitymanage.purreport.mapper.PurReportQmMapper;
import com.hqu.modules.qualitymanage.purreport.entity.PurReportRSn;
import com.hqu.modules.qualitymanage.purreport.mapper.PurReportRSnQmMapper;

/**
 * 检验单(采购/装配/整机检测)Service
 * @author 杨贤邦
 * @version 2018-04-13
 */
@Service
@Transactional(readOnly = true)
public class PurReportQmService extends CrudService<PurReportQmMapper, PurReport> {

	@Autowired
	private PurReportRSnQmMapper purReportRSnMapper;
	
	public PurReport get(String id) {
		PurReport purReport = super.get(id);
		purReport.setPurReportRSnList(purReportRSnMapper.findList(new PurReportRSn(purReport)));
		return purReport;
	}
	
	public List<PurReport> findList(PurReport purReport) {
		return super.findList(purReport);
	}
	
	public Page<PurReport> findPage(Page<PurReport> page, PurReport purReport) {
		return super.findPage(page, purReport);
	}
	
	@Transactional(readOnly = false)
	public void save(PurReport purReport) {
		super.save(purReport);
		for (PurReportRSn purReportRSn : purReport.getPurReportRSnList()){
			if (purReportRSn.getId() == null){
				continue;
			}
			if (PurReportRSn.DEL_FLAG_NORMAL.equals(purReportRSn.getDelFlag())){
				if (StringUtils.isBlank(purReportRSn.getId())){
					purReportRSn.setPurReport(purReport);
					purReportRSn.preInsert();
					purReportRSnMapper.insert(purReportRSn);
				}else{
					purReportRSn.preUpdate();
					purReportRSnMapper.update(purReportRSn);
				}
			}else{
				purReportRSnMapper.delete(purReportRSn);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(PurReport purReport) {
		super.delete(purReport);
		purReportRSnMapper.delete(new PurReportRSn(purReport));
	}
	
	
	/**
	 * @author xianbang
	 * 质量问题处理标志归零预处理，解决 删除不合格对象把处理标志归零问题（目前只在QmService保存操作时调用）
	 * @param purReportRSnIds
	 */
	@Transactional(readOnly = false)
	public void preDealQMatter(List<String> purReportRSnIds) {
		for(String id : purReportRSnIds) {
			PurReportRSn purSn= purReportRSnMapper.get(id);
			if(purSn!=null) {
				purSn.setIsQmatter("0");
				purReportRSnMapper.update(purSn);
			}else {
				System.out.println("++++++++++++++没有在检验单子表中找到id为"+id+"的检验对象！+++++++++++++++");
			}
		}
	}
	
	/**
	 * @author xianbang 若删除不是用isDel标记的方式处理，此处可能会存在BUG
	 * 质量问题处理标志修改（目前只在QmService保存操作时调用）
	 * @param purReportRSnIds flag表示当前调用该函数时，QMReport是执行的操作；删除：0，编辑保存：1，提交：2
	 */
	@Transactional(readOnly = false)
	public void dealQMatter(List<String> purReportRSnIds,List<String> purReportRSnDelIds,int flag) {
		Set<String> purReportIds=new HashSet<String>(); 
		
		//处理检验对象的标志
		for(String id : purReportRSnIds) {
			PurReportRSn purSn= purReportRSnMapper.get(id);
			if(purSn!=null) {
				if(flag==1) {                                //QMreport是保存，删除，还是提交
					purSn.setIsQmatter("1");
					purReportRSnMapper.update(purSn);
				}else if(flag==2) {
					purSn.setIsQmatter("2");
					purReportRSnMapper.update(purSn);
				}
				purReportIds.add(purSn.getPurReport().getId()); //记录所有更改过不合格检验对象所涉及的检验单
			}else {
				System.out.println("++++++++++++++没有在检验单子表中找到id为"+id+"的检验对象！+++++++++++++++");
			}
		}
		if(flag==1||flag==2) {
			for(String id : purReportRSnDelIds) {
				PurReportRSn purSn= purReportRSnMapper.get(id);
				if(purSn!=null) {
					purReportIds.add(purSn.getPurReport().getId()); //记录所有更改过不合格检验对象所涉及的检验单(此处为要删除的不合格对象)
				}else {
					System.out.println("++++++++++++++没有在检验单子表中找到id为"+id+"的检验对象！+++++++++++++++");
				}
			}	
		}
		//处理检验对象关联的检验单的标志
		for(String purId:purReportIds) {
			
			List<Object> purRs=super.executeSelectSql("select * from qms_purreport where id='"+purId+"'");
			if(purRs==null||purRs.size()==0)
			{
				System.out.println("++++++++++++++没有在检验单表中找到id为"+purId+"的检验单！+++++++++++++++");
				continue;
			}
			PurReport purReport=super.get(purRs.get(0).toString());
			List<PurReportRSn> purRsns=purReportRSnMapper.findList(new PurReportRSn(purReport));
			
			boolean isEdit=false;         //用来标记检验单不合格对象是否都被编辑处理了
			boolean isSubmit=false;       //用来标记检验单不合格对象是否都被提交处理了
			if(flag==1) {                                           //QMreport是保存,删除还是已提交
				isEdit=true;
				for(PurReportRSn purRsn : purRsns) {
					if(!purRsn.getCheckResult().equals("合格")) {  //查看不合格的对象是不是都处理了（都为1）(除开此次要处理的不合格对象以及要删除的不合格对象)
						if((!(("2".equals(purRsn.getIsQmatter())||"1".equals(purRsn.getIsQmatter())))&&!purReportRSnIds.contains(purRsn.getId()))||purReportRSnDelIds.contains(purRsn.getId())) {
							//此处的逻辑为 标志不等于1也不等于2，同时若此次要处理的对象不包涵这个对象；或者此处要删除的对象包涵了这个对象  也直接走这个分支
							isEdit=false;
							break;
						}
					}
					
				}
			}else if(flag==2) {
				isEdit=true;
				isSubmit=true;
				for(PurReportRSn purRsn : purRsns) {
					if(!purRsn.getCheckResult().equals("合格")) {  //查看不合格的对象是不是都提交处理了（都为2）(除开此次要处理的不合格对象以及要删除的不合格对象)
						if((!"2".equals(purRsn.getIsQmatter())&&!purReportRSnIds.contains(purRsn.getId()))||purReportRSnDelIds.contains(purRsn.getId())) {
							//此处的逻辑和上述类似
							isSubmit=false;
							if((!(("2".equals(purRsn.getIsQmatter())||"1".equals(purRsn.getIsQmatter())))&&!purReportRSnIds.contains(purRsn.getId()))||purReportRSnDelIds.contains(purRsn.getId())) { //重复检查一遍都不为1和2
								isEdit=false;
								break;
							}
						}
					}
					
				}
			}else if(flag==0) {isEdit=false;isSubmit=false;//为删除时直接不做其他处理，因为删除了那些处理了的检验对象，所涉及的检验单就都应该变为未处理状态！
			}
			
			if(isEdit) {                 
				purReport.setIsQmatter("1");
			}else {
				purReport.setIsQmatter("0");
			}
			if(isSubmit) {    //可能会存在isEdit和isSubmit都为true的情况，此时取状态为已提交；因为已提交包涵了编辑中状态。
				purReport.setIsQmatter("2");
			}
			super.save(purReport);
		}
		
	}
	
}