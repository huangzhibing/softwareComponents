package com.hqu.modules.inventorymanage.invaccountbyware.web;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.bin.entity.Bin;
import com.hqu.modules.inventorymanage.bin.service.BinService;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
import com.hqu.modules.inventorymanage.location.entity.Location;
import com.hqu.modules.inventorymanage.location.service.LocationService;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.hqu.modules.inventorymanage.warehouse.service.WareHouseService;
import com.jeeplus.common.json.AjaxJson;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 仓库账按仓库查询Controller
 * @author Hzb
 * @version 2018-06-15
 */

@Controller
@RequestMapping(value = "${adminPath}/invaccountbyware/invaccountByWare")
public class InvaccountByWareController extends BaseController{

    @Autowired
    InvAccountService invAccountService;
    @Autowired
    WareHouseService wareHouseService;
    @Autowired
    LocationService locationService;
    @Autowired
    BinService binService;

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

    @RequestMapping(value = "")
    public String list(){ return "inventorymanage/invaccountbyware/invaccountByWareList";}

    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(InvAccount invAccount, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<InvAccount> page = invAccountService.findPage(new Page<InvAccount>(request, response), invAccount);
        return getBootstrapData(page);
    }

    @ResponseBody
    @RequestMapping(value = "treeData")
    public Map<String, Object> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
        Map<String,Object> result = Maps.newHashMap();
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<WareHouse> list = wareHouseService.findList(new WareHouse());
        result.put("id","");
        result.put("code","");
        result.put("text","总结点");
        Map<String,Object> warehouseMap = null;
        Map<String,Object> binMap = null;
        Map<String,Object> locationMap = null;
        for (int i=0; i<list.size(); i++){
            WareHouse e = list.get(i);
            warehouseMap = Maps.newHashMap();
            warehouseMap.put("code",e.getWareID());
            warehouseMap.put("id",e.getId());
            warehouseMap.put("data",e.getAdminMode());
            warehouseMap.put("text",e.getWareName());
            List<Bin> bins = binService.findAllByWareId(e.getWareID());
            List<Map<String, Object>> binList = Lists.newArrayList();
            for(Bin b : bins){
                binMap = Maps.newHashMap();
                binMap.put("code",b.getBinId());
                binMap.put("id",b.getBinId());
                binMap.put("text",b.getBinDesc());
                List<Location> locations = locationService.findAllByBinId(b.getBinId());
                List<Map<String, Object>> locationList = Lists.newArrayList();
                for(Location l : locations){
                    locationMap = Maps.newHashMap();
                    locationMap.put("code",l.getLocId());
                    locationMap.put("id",l.getLocId());
                    locationMap.put("text",l.getLocDesc());
                    locationList.add(locationMap);
                }
                binMap.put("children",locationList);
                binList.add(binMap);
            }
            warehouseMap.put("children",binList);
            mapList.add(warehouseMap);
        }
        result.put("children",mapList);
        logger.debug("tree"+ JSON.toJSONString(result));
        return result;
    }

    @RequestMapping("print")
    public String print(String ids,Model model){
        String idArray[] =ids.split(",");
        List<InvAccount> invAccounts = new ArrayList<>();
        for(int i=0;i<idArray.length;i++){
            InvAccount invAccount = new InvAccount();
            invAccount = invAccountService.get(idArray[i]);
            invAccount.setPeriod(invAccount.getYear()+"/"+invAccount.getPeriod());
            invAccounts.add(invAccount);
        }

        model.addAttribute("invAccounts",invAccounts);
        return "inventorymanage/invaccountbyware/print";
    }

}
