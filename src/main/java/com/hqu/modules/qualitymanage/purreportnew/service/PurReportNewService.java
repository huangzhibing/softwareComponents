/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreportnew.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.qualitymanage.purreport.entity.PurReport;
import com.hqu.modules.qualitymanage.purreportnew.entity.PurReportNew;
import com.hqu.modules.qualitymanage.purreportnew.mapper.PurReportNewMapper;
import com.hqu.modules.qualitymanage.purreportnew.entity.Purreportfundetail;
import com.hqu.modules.qualitymanage.purreportnew.mapper.PurreportfundetailMapper;
import com.hqu.modules.qualitymanage.purreportnew.entity.Purreportnewsn;
import com.hqu.modules.qualitymanage.purreportnew.mapper.PurreportnewsnMapper;
import com.hqu.modules.qualitymanage.purreportnew.entity.Purreportsizedetail;
import com.hqu.modules.qualitymanage.purreportnew.mapper.PurreportsizedetailMapper;

/**
 * IQC来料检验报告Service
 * @author syc
 * @version 2018-08-18
 */
@Service
@Transactional(readOnly = true)
public class PurReportNewService extends CrudService<PurReportNewMapper, PurReportNew> {

	@Autowired
	private PurreportfundetailMapper purreportfundetailMapper;
	@Autowired
	private PurreportnewsnMapper purreportnewsnMapper;
	@Autowired
	private PurreportsizedetailMapper purreportsizedetailMapper;
	
	@Autowired
	private PurReportNewMapper purReportNewMapper;
	
	public PurReportNew get(String id) {
		PurReportNew purReportNew = super.get(id);
		purReportNew.setPurreportfundetailList(purreportfundetailMapper.findList(new Purreportfundetail(purReportNew)));
		purReportNew.setPurreportnewsnList(purreportnewsnMapper.findList(new Purreportnewsn(purReportNew)));
		purReportNew.setPurreportsizedetailList(purreportsizedetailMapper.findList(new Purreportsizedetail(purReportNew)));
		return purReportNew;
	}
	
	public List<PurReportNew> findList(PurReportNew purReportNew) {
		return super.findList(purReportNew);
	}
	
	public Page<PurReportNew> findPage(Page<PurReportNew> page, PurReportNew purReportNew) {
		return super.findPage(page, purReportNew);
	}
	
	@Transactional(readOnly = false)
	public void save(PurReportNew purReportNew) {
		super.save(purReportNew);
		for (Purreportfundetail purreportfundetail : purReportNew.getPurreportfundetailList()){
			if (purreportfundetail.getId() == null){
				continue;
			}
			if (Purreportfundetail.DEL_FLAG_NORMAL.equals(purreportfundetail.getDelFlag())){
				if (StringUtils.isBlank(purreportfundetail.getId())){
					purreportfundetail.setReportId(purReportNew);
					purreportfundetail.preInsert();
					purreportfundetailMapper.insert(purreportfundetail);
				}else{
					purreportfundetail.preUpdate();
					purreportfundetailMapper.update(purreportfundetail);
				}
			}else{
				purreportfundetailMapper.delete(purreportfundetail);
			}
		}
		for (Purreportnewsn purreportnewsn : purReportNew.getPurreportnewsnList()){
			if (purreportnewsn.getId() == null){
				continue;
			}
			if (Purreportnewsn.DEL_FLAG_NORMAL.equals(purreportnewsn.getDelFlag())){
				if (StringUtils.isBlank(purreportnewsn.getId())){
					purreportnewsn.setReportId(purReportNew);
					purreportnewsn.preInsert();
					purreportnewsnMapper.insert(purreportnewsn);
				}else{
					purreportnewsn.preUpdate();
					purreportnewsnMapper.update(purreportnewsn);
				}
			}else{
				purreportnewsnMapper.delete(purreportnewsn);
			}
		}
		for (Purreportsizedetail purreportsizedetail : purReportNew.getPurreportsizedetailList()){
			if (purreportsizedetail.getId() == null){
				continue;
			}
			if (Purreportsizedetail.DEL_FLAG_NORMAL.equals(purreportsizedetail.getDelFlag())){
				if (StringUtils.isBlank(purreportsizedetail.getId())){
					purreportsizedetail.setReportId(purReportNew);
					purreportsizedetail.preInsert();
					purreportsizedetailMapper.insert(purreportsizedetail);
				}else{
					purreportsizedetail.preUpdate();
					purreportsizedetailMapper.update(purreportsizedetail);
				}
			}else{
				purreportsizedetailMapper.delete(purreportsizedetail);
			}
		}
	}
	
	public String getReportID(){
		String numStr = "CHL";
		
		//获取年月日
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd"); 
		String c=sdf.format(new Date()); 
		int date = Integer.parseInt(c);
		
		//根据系统时间产生4位随机数
		Random random = new Random(System.currentTimeMillis());
		int nextInt = random.nextInt(10001);
		
		String newNextInt = "0000"+nextInt;
		newNextInt = newNextInt.substring(newNextInt.length()-4,newNextInt.length());
		
		//return 结果
		numStr = numStr+date+newNextInt;
		List<PurReportNew> purReportList = purReportNewMapper.findList(new PurReportNew());
		Iterator<PurReportNew> it = purReportList.iterator();
		while(it.hasNext()){
			PurReportNew purReportNew = it.next();
			if(purReportNew.getReportId().equals(numStr)){
				return getReportID();
			}
		}
		return numStr;
	}
	
	@Transactional(readOnly = false)
	public void delete(PurReportNew purReportNew) {
		super.delete(purReportNew);
		purreportfundetailMapper.delete(new Purreportfundetail(purReportNew));
		purreportnewsnMapper.delete(new Purreportnewsn(purReportNew));
		purreportsizedetailMapper.delete(new Purreportsizedetail(purReportNew));
	}
	
}