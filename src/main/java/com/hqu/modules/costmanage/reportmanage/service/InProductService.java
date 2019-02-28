package com.hqu.modules.costmanage.reportmanage.service;

import com.hqu.modules.costmanage.reportmanage.entity.ResourceImport;
import com.hqu.modules.costmanage.reportmanage.inproductmapper.inproductMapper;
import com.hqu.modules.costmanage.reportmanage.resourceImportOutputmapper.ResourceOutputMapper;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class InProductService extends CrudService<inproductMapper, ResourceImport>{
	public Page<ResourceImport> findPage(Page<ResourceImport> page, ResourceImport resourceImport) {
		return super.findPage(page, resourceImport);
	}
}
