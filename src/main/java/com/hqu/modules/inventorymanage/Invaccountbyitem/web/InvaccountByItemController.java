package com.hqu.modules.inventorymanage.Invaccountbyitem.web;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hqu.modules.basedata.account.entity.Account;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.entity.ItemClassNew;
import com.hqu.modules.basedata.item.service.ItemClassNewService;
import com.hqu.modules.basedata.item.service.ItemService;
import com.hqu.modules.basedata.itemclass.entity.ItemClass;
import com.hqu.modules.basedata.itemclass.entity.ItemClassF;
import com.hqu.modules.basedata.itemclass.service.ItemClassFService;
import com.hqu.modules.basedata.itemclass.service.ItemClassService;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
import com.hqu.modules.inventorymanage.outsourcingoutput.service.OutsourcingOutputService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.sun.corba.se.spi.ior.ObjectKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 仓库账按物料查询Controller
 * @author Hzb
 * @version 2018-06-15
 */

@Controller
@RequestMapping(value = "${adminPath}/invaccountbyitem/invaccountByItem")
public class InvaccountByItemController extends BaseController{

    @Autowired
    InvAccountService invAccountService;
    @Autowired
    ItemService itemService;
    @Autowired
    ItemClassService itemClassService;
    @Autowired
    ItemClassFService itemClassFService;
    @Autowired
    ItemClassNewService itemClassNewService;
    @Autowired
    OutsourcingOutputService outsourcingOutputService;

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
    public String list(){ return "inventorymanage/invaccountbyitem/invaccountByItemList";}

    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(InvAccount invAccount, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<InvAccount> page = invAccountService.findPage(new Page<InvAccount>(request, response), invAccount);

        return getBootstrapData(page);
    }

    @ResponseBody
    @RequestMapping(value = "treeData")
    public Map<String,Object> treeData(@RequestParam(required = false)String extId,HttpServletResponse response) {
        Map<String,Object> result = Maps.newHashMap();
        List<Map<String,Object>> mapList = Lists.newArrayList();
        Map<String,Object> itemclassFMap = null;
        Map<String,Object> itemclassMap = null;
        Map<String,Object> itemMap = null;
        List<ItemClassF> list = itemClassFService.findList(new ItemClassF());
        for(int i=0;i<list.size();i++){
            ItemClassF f = list.get(i);
            itemclassFMap = Maps.newHashMap();
            itemclassFMap.put("id",f.getId());
            itemclassFMap.put("code",f.getClassCode());
            itemclassFMap.put("data",f.getClassName());
            ItemClass itemClass = new ItemClass();
            itemClass.setClassId(f.getClassCode());
            List<ItemClass> itemClasss = itemClassService.findList(itemClass);
            List<Map<String,Object>> itemClassList = Lists.newArrayList();
            for(ItemClass s : itemClasss) {
                itemclassMap = Maps.newHashMap();
                itemclassMap.put("id", s.getId());
                itemclassMap.put("code", s.getClassId());
                itemclassMap.put("data", s.getParent());
                ItemClassNew classNew = new ItemClassNew();
                classNew.setClassId(s.getClassId());
                Item item = new Item();
                item.setClassCode(classNew);
                List<Item> items = itemService.findList(item);
                List<Map<String, Object>> itemList = Lists.newArrayList();
                for (Item item1 : items) {
                    itemMap = Maps.newHashMap();
                    itemMap.put("id", item1.getId());
                    itemMap.put("code", item1.getCode());
                    itemMap.put("data", item1.getName());
                    itemList.add(itemMap);
                }
                itemclassMap.put("children",itemList);
                itemClassList.add(itemclassMap);
            }
            mapList.add(itemMap);
        }
        result.put("children",mapList);
        logger.debug("itemtree:"+ JSON.toJSONString(result));
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "treeData1")
    public List<Map<String, Object>> treeData1(@RequestParam(required = false) String extId,
                                              HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<ItemClass> list = itemClassService.findList(new ItemClass());
        for (int i = 0; i < list.size(); i++) {
            ItemClass e = list.get(i);
            if (StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId())
                    && e.getParentIds().indexOf("," + extId + ",") == -1)) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                ItemClassNew itemClassNew = new ItemClassNew();
                itemClassNew.setClassId(e.getClassId());
                List<ItemClassNew> itemClassNews = itemClassNewService.findList(itemClassNew);
                Item item = new Item();
                item.setClassCode(itemClassNews.get(0));
                if(itemService.findList(item).size() != 0) {
                    List<Item> items = itemService.findList(item);
                    map.put("itemId", items.get(0).getCode());
                }
                map.put("text", e.getClassId()+" "+e.getName());
                map.put("classId", e.getClassId());
                if (StringUtils.isBlank(e.getParentId()) || "0".equals(e.getParentId())) {
                    map.put("parent", "#");
                    Map<String, Object> state = Maps.newHashMap();
                    state.put("opened", true);
                    map.put("state", state);
                } else {
                    map.put("parent", e.getParentId());
                }
                mapList.add(map);
            }
        }
        logger.debug("tree:"+mapList);
        return mapList;
    }

}
