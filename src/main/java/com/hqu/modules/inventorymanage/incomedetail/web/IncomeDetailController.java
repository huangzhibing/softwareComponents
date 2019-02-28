package com.hqu.modules.inventorymanage.incomedetail.web;


import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.basedata.period.service.PeriodService;
import com.hqu.modules.inventorymanage.billmain.service.BillMainService;
import com.hqu.modules.inventorymanage.incomedetail.entity.IncomeDetail;
import com.hqu.modules.inventorymanage.incomedetail.service.IncomeDetailService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/incomedetail/incomeDetail")
public class IncomeDetailController extends BaseController{

    @Autowired
    private IncomeDetailService incomeDetailService;
    @Autowired
    private BillMainService billMainService;

    @ModelAttribute
    public IncomeDetail get(){
        IncomeDetail entity = new IncomeDetail();
        return entity;
    }
    @RequestMapping(value = {"list", ""})
    public String list(String flag,Model model){
        model.addAttribute("flag",flag);
        Period period=billMainService.findPeriodByTime(new Date());
        if(period!=null){
            model.addAttribute("period",period.getPeriodId());
        }
        return "inventorymanage/incomedetail/incomeDetailList";
    }

    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String,Object> data(IncomeDetail incomeDetail, HttpServletRequest request, HttpServletResponse response, Model model){
        Page<IncomeDetail> page = incomeDetailService.findPage(new Page<IncomeDetail>(request,response),incomeDetail);
        return getBootstrapData(page);

    }
}
