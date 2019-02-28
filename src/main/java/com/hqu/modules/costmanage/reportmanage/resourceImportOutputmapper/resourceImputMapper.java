package com.hqu.modules.costmanage.reportmanage.resourceImportOutputmapper;

import java.util.List;
import java.util.Map;

import com.hqu.modules.costmanage.reportmanage.entity.ResourceImport;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;

@MyBatisMapper
public interface resourceImputMapper extends BaseMapper<ResourceImport>{
	//public List<ResourceImport> findByConditions(ResourceImport resourceImport);
}
