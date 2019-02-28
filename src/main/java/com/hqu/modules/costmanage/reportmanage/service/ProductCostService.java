package com.hqu.modules.costmanage.reportmanage.service;

import com.hqu.modules.costmanage.reportmanage.entity.ReportRealAccount;
import com.hqu.modules.costmanage.reportmanage.entity.ResourceImport;
import com.hqu.modules.costmanage.reportmanage.inproductmapper.inproductMapper;
import com.hqu.modules.costmanage.reportmanage.productCostMapper.ProductCostMapper;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class ProductCostService extends CrudService<ProductCostMapper, ReportRealAccount>{
	public Page<ReportRealAccount> findPage(Page<ReportRealAccount> page, ReportRealAccount realAccount) {
		return super.findPage(page, realAccount);
	}
}
