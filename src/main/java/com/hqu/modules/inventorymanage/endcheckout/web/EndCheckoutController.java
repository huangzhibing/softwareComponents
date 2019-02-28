package com.hqu.modules.inventorymanage.endcheckout.web;


import com.alibaba.fastjson.JSON;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hqu.modules.basedata.account.entity.Account;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.basedata.period.service.PeriodService;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billmain.service.BillMainService;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
import com.hqu.modules.inventorymanage.resumecheckout.service.ResumeCheckoutService;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.hqu.modules.inventorymanage.warehouse.service.WareHouseService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 月末结账Controller
 * @author hzb
 * @version 2018-04-30
 */
@Controller
@RequestMapping(value = "${adminPath}/endcheckout/endCheckout")
public class EndCheckoutController extends BaseController {

    @Autowired
    private InvAccountService invAccountService;
    @Autowired
    private BillMainService billMainService;
    @Autowired
    private ResumeCheckoutService resumeCheckoutService;
    @Autowired
    private WareHouseService wareHouseService;
    @Autowired
    private PeriodService periodService;

    @ModelAttribute
    public InvAccount get(@RequestParam(required = false) String id){
        InvAccount entity = null;
        if(StringUtils.isNotBlank(id)){
            entity = invAccountService.get(id);
        }
        if(entity == null){
            entity = new InvAccount();
        }
        return entity;
    }
    /**
     * 结账核算期
     */
    @RequestMapping(value = "")
    public String index(BillMain billMain){
        Date date=new Date();
        //添加日期
        billMain.setBillDate(date);
        return "inventorymanage/endcheckout/endCheckout";
    }

    /**
     * 生成核算期
     * @param date
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "resume")
    public String resume(Date date){
        logger.debug("date:"+date);
        //添加日期
        return billMainService.findPeriodByTime(date).getPeriodId();
    }

    /**
     * 月末结账
     * @param billMain
     * @param redirectAttributes
     * @return
     */

    @RequestMapping(value = "checkoutLast")
    public String checkoutLast(BillMain billMain, RedirectAttributes redirectAttributes) {
        //通过核算期查出本月和上个月的库存帐
        List<String> periods = resumeCheckoutService.findLastByDate(billMain.getBillDate());
        logger.debug("本月核算期:" + periods.get(0));
        logger.debug("上月核算期:" + periods.get(1));
        InvAccount nowinvAccount = new InvAccount();
        nowinvAccount.setWare(billMain.getWare());
        nowinvAccount.setYear(periods.get(0).substring(0,4));
        nowinvAccount.setPeriod(periods.get(0).substring(4,6));
        List<InvAccount> invAccounts = invAccountService.findList(nowinvAccount);
        //如果本月的核算期的记录为空，才能进行结账，将上个月的帐结到这个月
        if(invAccounts.size() == 0){
            //遍历上个月核算期的库存账
            InvAccount invAccount = new InvAccount();
            invAccount.setWare(billMain.getWare());
            invAccount.setYear(periods.get(1).substring(0, 4));
            invAccount.setPeriod(periods.get(1).substring(4, 6));
            List<InvAccount> nacounts = invAccountService.findList(invAccount);
            logger.debug("上月核算期库存帐记录数数:" + nacounts.size());
            //修改当前核算期的记录
            for (InvAccount ainvAccount : nacounts) {
                try{
                double nowQty = ainvAccount.getNowQty();
                double realQty = ainvAccount.getRealQty();
                double nowSum = ainvAccount.getNowSum();
                InvAccount invAccount1 = new InvAccount();
                WareHouse wareHouse = ainvAccount.getWare();
                Item item = ainvAccount.getItem();
                invAccount1.setWare(wareHouse);
                invAccount1.setItem(item);
                invAccount1.setYear(periods.get(0).substring(0, 4));
                invAccount1.setPeriod(periods.get(0).substring(4, 6));
                invAccount1.setBeginQty(nowQty);
                invAccount1.setNowQty(nowQty);
                invAccount1.setRealQty(realQty);
                invAccount1.setBeginSum(nowSum);
                invAccount1.setNowSum(nowSum);
                if (ainvAccount.getBin() != null) {
                    invAccount1.setBin(ainvAccount.getBin());
                }
                if (ainvAccount.getLoc() != null) {
                    invAccount1.setLoc(ainvAccount.getLoc());
                }
                invAccountService.save(invAccount1);
            }catch (Exception e){

                }
        }
        WareHouse wareHouse = wareHouseService.get(billMain.getWare());
        wareHouse.setCurrPeriod(periods.get(0));
        wareHouseService.save(wareHouse);
        addMessage(redirectAttributes, "结账成功");
        return "redirect:" + Global.getAdminPath() + "/endcheckout/endCheckout";
        }
        else{           //如果这个月的记录有了，说明核算期未结束，无法结账
            addMessage(redirectAttributes, "当前核算期未结束，结账失败");
            return "redirect:" + Global.getAdminPath() + "/endcheckout/endCheckout";
        }

    }
}

