package com.hqu.modules.costmanage.reportmanage.resourceInportOutputservice;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hqu.modules.costmanage.reportmanage.entity.ResourceImport;
import com.hqu.modules.costmanage.reportmanage.resourceImportOutputmapper.PayDetailMapper;
import com.jeeplus.core.service.CrudService;

@Service
@Transactional(readOnly=true)
public class PayDetailService extends CrudService<PayDetailMapper, ResourceImport> {

}
