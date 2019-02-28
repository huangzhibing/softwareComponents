package com.hqu.modules.inventorymanage.invaccountbydetail.web;


import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billmain.service.BillMainService;
import com.hqu.modules.inventorymanage.billtype.service.BillTypeService;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 仓库账明细查询Controller
 * @author Hzb
 * @version 2018-06-15
 */

@Controller
@RequestMapping(value = "${adminPath}/invaccountbydetail/invaccountByDetail")
public class InvaccountByDetailController extends BaseController{

    @Autowired
    InvAccountService invAccountService;
    @Autowired
    BillMainService billMainService;
    @Autowired
    BillTypeService billTypeService;

    @ModelAttribute
    public BillMain get(@RequestParam(required = false) String id){
        BillMain entity = null;
        if(StringUtils.isNotBlank(id)){
            entity = billMainService.get(id);
        }
        else {
            entity = new BillMain();
        }
        return entity;
    }

    @RequestMapping(value = "")
    public String list(){ return "inventorymanage/invaccountbydetail/invaccountByDetailList";}

    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BillMain billMain, HttpServletRequest request, HttpServletResponse response, Model model) {

//        Page<BillMain> billMainPage = new Page<>();
//
//        List<BillMain> billMainList = billMainService.findList(billMain);
//        for(int i=0;i<billMainList.size();i++) {
//            logger.debug("billtype:"+billMainList.get(i).getIo());
//            logger.debug("billtype:"+billMainList.get(i).getIo().getId());
//            String ioflag = billTypeService.get(billMainList.get(i).getIo()).getIoFlag();
//            billMainList.get(i).setIoFlag(ioflag);
//        }
//        billMainPage.setList(billMainList);
//        billMainPage.setPageSize(10);
//        billMainPage.setPageNo(10);
//        return getBootstrapData(billMainPage);
        Page<BillMain> page = billMainService.findPage(new Page<BillMain>(request, response), billMain);
        return getBootstrapData(page);
    }

}
