package com.hqu.modules.inventorymanage.backloganalyse.web;

import com.hqu.modules.inventorymanage.billmain.service.BillMainService;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
import com.hqu.modules.inventorymanage.sortcount.service.SortCountService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 存货积压分析Controller
 * @author hzb
 * @version 2018-05-30
 */
@Controller
@RequestMapping(value = "${adminPath}/backloganalyse/backlogAnalyse")
public class backlogAnalyseController extends BaseController {

    @Autowired
    InvAccountService invAccountService;

    @Autowired
    BillMainService billMainService;

    @Autowired
    SortCountService sortCountService;

    @ModelAttribute
    public InvAccount get(@RequestParam(required = false) String id){
        InvAccount entity = null;
        if(StringUtils.isNotBlank(id)){
            entity = invAccountService.get(id);
        }
        else{
            entity = new InvAccount();
        }
        return entity;
    }

    @RequestMapping(value = "")
    public String list(){
        return "inventorymanage/backloganalyse/backlogAnalyseList";
    }

    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String,Object> data(String className,InvAccount invAccount, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Model model){

        //获得最新核算期的库存帐记录
        Date date = new Date();
        String year = billMainService.findPeriodByTime(date).getPeriodId().substring(0,4);
        String mouth = billMainService.findPeriodByTime(date).getPeriodId().substring(4,6);
        invAccount.setYear(year);
        invAccount.setPeriod(mouth);
        List<InvAccount> invAccountList = invAccountService.findList(invAccount);

        Page<InvAccount> page = invAccountService.findPage(new Page<InvAccount>(httpServletRequest,httpServletResponse),invAccount);
        for(int i=0;i<invAccountList.size();i++){
            //查询物料类时回调data
            logger.debug("invaccount:"+invAccountList.get(i));
            if(StringUtils.isNotEmpty(className)&&sortCountService.findInvByClassname(className,invAccountList.get(i))){
                page.getList().remove(invAccountList.get(i));
            }
        }

        return getBootstrapData(page);
    }
}
