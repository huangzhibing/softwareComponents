package com.hqu.modules.costmanage.reportmanage.resourceInportOutputservice;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hqu.modules.costmanage.reportmanage.entity.ResourceImport;
import com.hqu.modules.costmanage.reportmanage.resourceImportOutputmapper.ResourceOutputMapper;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;

@Service
@Transactional(readOnly=true)
public class ResourceOutputService extends CrudService<ResourceOutputMapper, ResourceImport>{
	public Page<ResourceImport> findPage(Page<ResourceImport> page, ResourceImport resourceImport) {
		return super.findPage(page, resourceImport);
	}
}
