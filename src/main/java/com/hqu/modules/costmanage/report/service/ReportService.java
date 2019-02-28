package com.hqu.modules.costmanage.report.service;


import com.hqu.modules.costmanage.report.entity.Report;
import com.hqu.modules.costmanage.report.mapper.ReportMapper;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReportService extends CrudService<ReportMapper, Report> {
    public Page<Report> findPage(Page<Report> page, Report report) {
        return super.findPage(page,report);
    }
}
