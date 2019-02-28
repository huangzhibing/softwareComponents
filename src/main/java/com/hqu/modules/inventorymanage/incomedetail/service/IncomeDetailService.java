package com.hqu.modules.inventorymanage.incomedetail.service;


import com.hqu.modules.inventorymanage.incomedetail.entity.IncomeDetail;
import com.hqu.modules.inventorymanage.incomedetail.mapper.IncomeDetailMapper;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class IncomeDetailService extends CrudService<IncomeDetailMapper,IncomeDetail>{

    public Page<IncomeDetail> findPage(Page<IncomeDetail> page, IncomeDetail incomeDetail) {
        return super.findPage(page, incomeDetail);
    }

}
