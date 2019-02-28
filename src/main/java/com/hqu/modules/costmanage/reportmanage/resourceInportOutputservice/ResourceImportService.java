package com.hqu.modules.costmanage.reportmanage.resourceInportOutputservice;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hqu.modules.costmanage.reportmanage.entity.ResourceImport;
import com.hqu.modules.costmanage.reportmanage.resourceImportOutputmapper.resourceImputMapper;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;

@Service
@Transactional(readOnly=true)
public class ResourceImportService extends CrudService<resourceImputMapper, ResourceImport>{
	public Page<ResourceImport> findPage(Page<ResourceImport> page, ResourceImport resourceImport) {
		return super.findPage(page, resourceImport);
	}
}
