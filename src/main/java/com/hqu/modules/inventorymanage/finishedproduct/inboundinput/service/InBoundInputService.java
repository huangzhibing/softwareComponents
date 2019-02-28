package com.hqu.modules.inventorymanage.finishedproduct.inboundinput.service;

import com.hqu.modules.inventorymanage.billmain.entity.BillDetail;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.warehouse.service.WareHouseService;
import com.jeeplus.core.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.mapper.InvAccountMapper;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.logging.*;


/**
 * 成品入库Service
 * @author hzb
 * @version 2018-05-08
 */
@Service
@Transactional(readOnly = true)
public class InBoundInputService{

    @Autowired
    WareHouseService wareHouseService;

    /**
     * 判断仓库管理标识是库房管还是货区货位管
     * @param billMain
     * @return
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public Boolean judgeAdminMode(BillMain billMain){
        String adminMode = wareHouseService.get(billMain.getWare().getWareID()).getAdminMode();
        if(adminMode.equals("B")){
            for(BillDetail billDetail:billMain.getBillDetailList()){
                if(StringUtils.isEmpty(billDetail.getBinName())){
                    return false;
                }
            }
        }
        if(adminMode.equals("L")){
            for(BillDetail billDetail:billMain.getBillDetailList()){
                if(StringUtils.isEmpty(billDetail.getBinName())){
                    return false;
                }
                if(StringUtils.isEmpty(billDetail.getLocName())){
                    return false;
                }
            }
        }
        return true;
    }

}
