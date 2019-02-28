/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package  com.hqu.modules.purchasemanage.applymain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hqu.modules.purchasemanage.applymain.entity.InvAccountPur;
import com.hqu.modules.purchasemanage.applymain.mapper.InvAccountPurMapper;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;


/**
 * 库存账Service
 * @author M1ngz
 * @version 2018-04-22
 */
@Service
@Transactional(readOnly = true)
public class InvAccountPurService extends CrudService<InvAccountPurMapper, InvAccountPur> {

	public InvAccountPur get(String id) {
		return super.get(id);
	}
	
	public List<InvAccountPur> findList(InvAccountPur InvAccountPur) {
		return super.findList(InvAccountPur);
	}
	
	public Page<InvAccountPur> findPage(Page<InvAccountPur> page, InvAccountPur InvAccountPur) {
		return super.findPage(page, InvAccountPur);
	}
	
	@Transactional(readOnly = false)
	public void save(InvAccountPur InvAccountPur) {
		super.save(InvAccountPur);
	}
	
	@Transactional(readOnly = false)
	public void delete(InvAccountPur InvAccountPur) {
		super.delete(InvAccountPur);
	}
	
}