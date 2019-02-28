package com.hqu.modules.costmanage.report.web;

import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.costmanage.report.entity.Report;
import com.hqu.modules.costmanage.report.service.ReportService;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/report/report")
public class ReportController extends BaseController {
    @Autowired
    ReportService reportService;
    @RequiresPermissions(value = "report:inv1:list")
    @RequestMapping(value = "inv1")
    public String inv1(Model model){
        model.addAttribute("invAccount",new InvAccount());
        return "costmanage/report/inv1";
    }
    @RequiresPermissions(value = "report:inv2:list")
    @RequestMapping(value = "inv2")
    public String inv2(Model model){
        model.addAttribute("invAccount",new InvAccount());
        return "costmanage/report/inv1";
    }

    @RequiresPermissions(value = "report:billMain:list")
    @RequestMapping(value = "billMain")
    public String billMain(Model model){
        model.addAttribute("billMain",new BillMain());
        return "costmanage/report/billMain";
    }
    @ResponseBody
    @RequestMapping(value = "billMainData")
    public Map<String, Object> data(Report report, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Report> page = reportService.findPage(new Page<Report>(request, response), report);
        return getBootstrapData(page);
    }
}
