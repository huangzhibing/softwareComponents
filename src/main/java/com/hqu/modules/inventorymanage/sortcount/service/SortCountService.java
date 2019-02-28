package com.hqu.modules.inventorymanage.sortcount.service;


import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.service.ItemService;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
import com.hqu.modules.inventorymanage.sortcount.entity.SortCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 资金及ABC分类统计Service
 * @author 黄志兵
 * @version 2018-05-21
 */

@Service
public class SortCountService{

    @Autowired
    private ItemService itemService;
    @Autowired
    private InvAccountService invAccountService;

    /**
     * 通过物料类查询库存帐对应的物料库存帐信息
     */
    public boolean findInvByClassname(String className,InvAccount invAccount){

        Item item = new Item();
        item.setClassName(className);
        List<Item> itemList = itemService.findList(item);
        for(int i=0;i<itemList.size();i++){
            itemList.get(i).getCode();
            System.out.println("code:"+itemList.get(i).getCode());
            System.out.println("code:"+invAccount.getItem().getCode());
            if(!invAccount.getItem().getCode().equals(itemList.get(i).getCode())){
                continue;
            }
            else {
                return false;
            }
        }
        return true;
    }


}
