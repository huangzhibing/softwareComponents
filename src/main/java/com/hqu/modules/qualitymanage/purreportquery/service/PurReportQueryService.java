/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreportquery.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hqu.modules.qualitymanage.purreportquery.entity.PurReportQuery;
import com.hqu.modules.qualitymanage.purreportquery.entity.PurReportRSnQuery;
import com.hqu.modules.qualitymanage.purreportquery.mapper.PurReportQueryMapper;
import com.hqu.modules.qualitymanage.purreportquery.mapper.PurReportRSnQueryMapper;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.OfficeMapper;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 检验单查询(采购/装配/整机检测)Service
 * @author 孙映川
 * @version 2018-04-17
 */
@Service
@Transactional(readOnly = true)
public class PurReportQueryService extends CrudService<PurReportQueryMapper, PurReportQuery> {

	@Autowired
	private PurReportRSnQueryMapper purReportRSnQueryMapper;
	@Autowired
	private OfficeMapper officeMapper;
	@Autowired
	private PurReportQueryMapper purReportQueryMapper;
	
	public PurReportQuery get(String id) {
		PurReportQuery purReportQuery = super.get(id);
		List<PurReportRSnQuery> findList = purReportRSnQueryMapper.findList(new PurReportRSnQuery(purReportQuery));
		purReportQuery.setPurReportRSnQueryList(findList);
		return purReportQuery;
	}
	
	public List<PurReportQuery> findList(PurReportQuery purReportQuery) {
		
		return super.findList(purReportQuery);
	}
	
	
	
	public Page<PurReportQuery> findyMyPage(Page<PurReportQuery> page, PurReportQuery purReportQuery) {
		return super.findPage(page, purReportQuery);
	}
	
	
	
	public Page<PurReportQuery> findPage(Page<PurReportQuery> page, PurReportQuery purReportQuery) {
		
		String rndFul = purReportQuery.getRndFul();
		if("qj".equals(rndFul)){
			purReportQuery.setRndFul("全检");
		}
		if("cj".equals(rndFul)){
			purReportQuery.setRndFul("抽检");
		}
		String checktName = purReportQuery.getChecktName();
		if("cg".equals(checktName)){
			purReportQuery.setChecktName("采购");
		}
		
		if("hg".equals(purReportQuery.getCheckResult())){
			purReportQuery.setCheckResult("合格");
		}
		if("bfhg".equals(purReportQuery.getCheckResult())){
			purReportQuery.setCheckResult("部分合格");
		}
		if("bhg".equals(purReportQuery.getCheckResult())){
			purReportQuery.setCheckResult("不合格");
		}
		
		
		
		
		dataRuleFilter(purReportQuery);
		purReportQuery.setPage(page);
		//修改前的所有List
		//List<PurReportQuery> findList = mapper.findList(purReportQuery);
		//----------
		Office office = purReportQuery.getOffice();
		if(office==null){
			office = new Office();
		}
		
		List<PurReportQuery> listSum = new ArrayList<PurReportQuery>();
		String cpersonCode = purReportQuery.getCpersonCode();
		String id = office.getId();
		if("".equals(id)&&"".equals(cpersonCode)){
			User currentUser = UserUtils.getUser();
			
			List<User> userList = new ArrayList<User>();
			//根据当前用户ID去获取所属部门列表
			List<Office> officeList = purReportQueryMapper.getOfficeListByUserId(currentUser.getId());
			Iterator<Office> it = officeList.iterator();
			//根据用户列表获取所有职员
			while(it.hasNext()){
				Office next = it.next();
				List<User> user = purReportQueryMapper.getUserByOfficeId(next.getId());
				userList.addAll(user);
//				purReportQuery.setOffice(next);
//				List<PurReportQuery> findListResult =  mapper.findList(purReportQuery);
//				listSum.addAll(findListResult);
			}
			//return listSum;
			Iterator<User> itUser = userList.iterator();
			while(itUser.hasNext()){
				User user = itUser.next();
				purReportQuery.setCpersonCode(user.getNo());
				List<PurReportQuery> findListResult =  mapper.findList(purReportQuery);
				listSum.addAll(findListResult);
			}
		}else{
			String officeId = purReportQuery.getOffice().getId();
			purReportQuery.getOffice().setId("");
			cpersonCode = purReportQuery.getCpersonCode();
			//purReportQuery.getOffice().setName("");
			//purReportQuery.setCpersonCode("");
			if(!officeId.equals("")&&cpersonCode.equals("")){
				List<User> userList = purReportQueryMapper.getUserByOfficeId(officeId);
				Iterator<User> itUser = userList.iterator();
				while(itUser.hasNext()){
					User user = itUser.next();
					purReportQuery.setCpersonCode(user.getNo());
					List<PurReportQuery> findListResult =  mapper.findList(purReportQuery);
					listSum.addAll(findListResult);
				}
				
			}else if(officeId.equals("")&&!cpersonCode.equals("")){
				purReportQuery.setCpersonCode(cpersonCode);
				listSum = mapper.findList(purReportQuery);
			}else if(!officeId.equals("")&&!cpersonCode.equals("")){
				List<User> userList = purReportQueryMapper.getUserByOfficeId(officeId);
				Iterator<User> itUser = userList.iterator();
				while(itUser.hasNext()){
					User user = itUser.next();
					if(user.getNo().equals(cpersonCode)){
						purReportQuery.setCpersonCode(user.getNo());
						List<PurReportQuery> findListResult =  mapper.findList(purReportQuery);
						listSum.addAll(findListResult);
					}
					
				}
			}
			
		}
		
	
		
		//listSum 筛选出的代码
		
		//交集
		//listSum.retainAll(findList);
		
		
		
		
		
		//--------------
		
		
		
		page.setList(listSum);
		
		
		return page;
	}
	//根据office的id获取所辖office
	public List<Office> getAllOfficeChildrenByOfficeId(String officeId){
		List<Office> listSum = new ArrayList<Office>();
		List<Office> officeList = officeMapper.getChildren(officeId);
		Iterator<Office> it = officeList.iterator();
		while(it.hasNext()){
			Office office = it.next();
			List<Office> childrenList = officeMapper.getChildren(office.getId());
			if(childrenList!=null&&childrenList.size()!=0){
				listSum.addAll(getAllOfficeChildrenByOfficeId(office.getId()));
				
			}
		}
		listSum.addAll(officeList);
		return listSum;
	}
	
	
	@Transactional(readOnly = false)
	public void save(PurReportQuery purReportQuery) {
		super.save(purReportQuery);
		for (PurReportRSnQuery purReportRSnQuery : purReportQuery.getPurReportRSnQueryList()){
			if (purReportRSnQuery.getId() == null){
				continue;
			}
			if (PurReportRSnQuery.DEL_FLAG_NORMAL.equals(purReportRSnQuery.getDelFlag())){
				if (StringUtils.isBlank(purReportRSnQuery.getId())){
					purReportRSnQuery.setPurReport(purReportQuery);
					purReportRSnQuery.preInsert();
					purReportRSnQueryMapper.insert(purReportRSnQuery);
				}else{
					purReportRSnQuery.preUpdate();
					purReportRSnQueryMapper.update(purReportRSnQuery);
				}
			}else{
				purReportRSnQueryMapper.delete(purReportRSnQuery);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(PurReportQuery purReportQuery) {
		super.delete(purReportQuery);
		purReportRSnQueryMapper.delete(new PurReportRSnQuery(purReportQuery));
	}
	
}