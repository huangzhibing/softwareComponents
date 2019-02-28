package com.hqu.modules.inventorymanage.sortcount.web;

import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.entity.ItemClassNew;
import com.hqu.modules.basedata.item.service.ItemClassNewService;
import com.hqu.modules.basedata.item.service.ItemService;
import com.hqu.modules.basedata.itemclass.entity.ItemClass;
import com.hqu.modules.basedata.itemclass.entity.ItemClassF;
import com.hqu.modules.inventorymanage.billmain.service.BillMainService;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
import com.hqu.modules.inventorymanage.sortcount.entity.SortCount;
import com.hqu.modules.inventorymanage.sortcount.service.SortCountService;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 资金及ABC分类统计Controller
 * @author hzb
 * @version 2018-05-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sortcount/sortCount")
public class SortCountController extends BaseController{

    @Autowired
    InvAccountService invAccountService;
    @Autowired
    SortCountService sortCountService;
    @Autowired
    BillMainService billMainService;
    @Autowired
    ItemService itemService;
    @Autowired
    ItemClassNewService itemClassNewService;

    @ModelAttribute
    public InvAccount get(@RequestParam(required=false) String id) {
        InvAccount entity = null;
        if (StringUtils.isNotBlank(id)){
            entity = invAccountService.get(id);
        }
        if (entity == null){
            entity = new InvAccount();
        }
        return entity;
    }

    /**
     * 通过物料类获取物料信息（未完成）
     * @param classCodeId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getItemClass")
    public Item getItemByItemClass(String classCodeId){
        ItemClassNew itemClassNew = new ItemClassNew();
        itemClassNew.setClassId(classCodeId);
        String itemId = itemClassNewService.findList(itemClassNew).get(0).getParentId();
        logger.debug("物料编码为："+itemId);
        Item item = itemService.get(itemId);
        return item;
    }

    @RequestMapping(value = "")
    public String list() {
        return "inventorymanage/sortcount/sortCountList";
    }

    /**
     *获取当前核算期的信息
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(String className, String sortType, InvAccount invAccount, HttpServletRequest request, HttpServletResponse response, Model model) {

        logger.debug("className:"+className);
        Date date = new Date();
        String year = billMainService.findPeriodByTime(date).getPeriodId().substring(0,4);
        String period = billMainService.findPeriodByTime(date).getPeriodId().substring(4,6);
        logger.debug("当前核算期年份："+year);
        logger.debug("当前核算期月份："+period);

        //获取当前核算期的物料数量和金额比例
        invAccount.setYear(year);
        invAccount.setPeriod(period);
        List<InvAccount> invAccounts = invAccountService.findList(invAccount);
        Page<SortCount> sortCountPage = new Page<>();
        NumberFormat numberFormat = NumberFormat.getPercentInstance();
        for(int i=0;i<invAccounts.size();i++) {
            double qty = 0;
            double sum = 0;
            String wareId = invAccounts.get(i).getWare().getWareID();
            double nowqty = invAccounts.get(i).getNowQty();
            double nowsum = invAccounts.get(i).getNowSum();
            SortCount sortCount;
            for(int j=0;j<invAccounts.size();j++) {
                if (invAccounts.get(j).getWare().getWareID().equals(wareId)) {
                    qty = qty + invAccounts.get(j).getNowQty();
                    sum = sum + invAccounts.get(j).getNowSum();
                }
            }

            sortCount = new SortCount(invAccounts.get(i));
            double ratioqty = 0;
            double ratiosum = 0;
            if(qty ==0){
                sortCount.setRatioQty("0%");
            }
            else {
                ratioqty = nowqty/qty;
                sortCount.setRatioQty(numberFormat.format(ratioqty));
            }
            if(sum ==0){
                sortCount.setRatioSum("0%");
            }
            else {
                ratiosum = nowsum/sum;
                sortCount.setRatioSum(numberFormat.format(ratiosum));
            }
            //设置ABC类别
            if(ratiosum >=70 && ratiosum<=75 && ratioqty>=5&& ratioqty<=10){
                sortCount.setSortType("A");
            }
            if(ratiosum >=10 && ratiosum<=20 && ratioqty>=10&& ratioqty<=25){
                sortCount.setSortType("B");
            }
            if(ratiosum >=5 && ratiosum<=10 && ratioqty>=60&& ratioqty<=80){
                sortCount.setSortType("C");
            }
            else{
                sortCount.setSortType("");
            }
            sortCountPage.getList().add(sortCount);
            //查询ABC类别时回调data
            if(StringUtils.isNotEmpty(sortType)&&sortCount.getSortType() != sortType){
                sortCountPage.getList().remove(sortCount);
            }
            logger.debug("sortcount:"+sortCount);
            logger.debug("invaccount:"+invAccounts.get(i));
            //查询物料类时回调data
            if(StringUtils.isNotEmpty(className)&&sortCountService.findInvByClassname(className,invAccounts.get(i))){
                sortCountPage.getList().remove(sortCount);
            }
        }
        return getBootstrapData(sortCountPage);
    }

}
