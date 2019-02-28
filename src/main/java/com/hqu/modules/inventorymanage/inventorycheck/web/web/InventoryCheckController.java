package com.hqu.modules.inventorymanage.inventorycheck.web.web;


import com.hqu.modules.inventorymanage.billmain.service.BillMainService;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
import com.hqu.modules.inventorymanage.inventorycheck.web.entity.InventoryCheck;
import com.hqu.modules.inventorymanage.sortcount.service.SortCountService;
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

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 库存盘点表Controller
 * @author hzb
 * @version 2018-05-28
 */

@Controller
@RequestMapping(value = "${adminPath}/inventorycheck/inventoryCheck")
public class InventoryCheckController extends BaseController{

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
        else {
            entity = new InvAccount();
        }
        return entity;
    }

    /**
     * 单据列表界面
     */
    @RequestMapping(value = "")
    public String list(){
        return "inventorymanage/inventorycheck/inventoryCheckList";
    }

    /**
     * 单据列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String,Object> data(String className,InvAccount invAccount, HttpServletRequest request, HttpServletResponse response, Model model){


        List<InvAccount> invAccounts = invAccountService.findList(invAccount);
        Page<InventoryCheck> inventoryCheckPage = new Page<>();
        //设置盘盈盘亏的金额和数量
        for(int i=0; i<invAccounts.size();i++) {
            InventoryCheck inventoryCheck;
            inventoryCheck = new InventoryCheck(invAccounts.get(i));

            try {
                inventoryCheck.setYear(invAccounts.get(i).getYear());
                inventoryCheck.setPeriod(invAccounts.get(i).getPeriod());
                inventoryCheck.setNowQty(invAccounts.get(i).getNowQty());
                inventoryCheck.setNowSum(invAccounts.get(i).getNowSum());
                double qty = invAccounts.get(i).getPoutQty() - invAccounts.get(i).getPinQty();
                double sum = invAccounts.get(i).getPoutSum() - invAccounts.get(i).getPinSum();

                if (qty > 0) {
                    inventoryCheck.setProfitQty(qty);
                }
                if (qty == 0.0) {
                    inventoryCheck.setProfitQty(0.0);
                    inventoryCheck.setLossQty(0.0);
                }
                if (qty < 0) {
                    inventoryCheck.setLossQty(-qty);
                }
                if (sum > 0) {
                    inventoryCheck.setProfitSum(sum);
                }
                if (sum == 0.0) {
                    inventoryCheck.setProfitSum(0.0);
                    inventoryCheck.setLossSum(0.0);
                }
                if (sum < 0) {
                    inventoryCheck.setLossSum(-sum);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            inventoryCheckPage.getList().add(inventoryCheck);
            //查询物料类时回调data
            if(StringUtils.isNotEmpty(className)&&sortCountService.findInvByClassname(className,invAccounts.get(i))){
                inventoryCheckPage.getList().remove(inventoryCheck);
            }
        }
        return getBootstrapData(inventoryCheckPage);
    }

}
