/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.newinvcheckmain.mapper;

import com.hqu.modules.inventorymanage.billdetailcodes.entity.BillDetailCodes;
import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.inventorymanage.newinvcheckmain.entity.NewinvCheckMain;

import java.util.List;

/**
 * 超期复验MAPPER接口
 * @author Neil
 * @version 2018-06-15
 */
@MyBatisMapper
public interface NewinvCheckMainMapper extends BaseMapper<NewinvCheckMain> {

    String getWare(String wareId);
    String getAutoFlag(String wareId);
    List<BillDetailCodes> getBillDetailCodes(String billNum);
    Double getnownum(String itemcode,String wareId);
    Double getrealnum(String itemcode,String wareId);
    String getid(String itemcode,String wareId);
    void updatenownum(double nownum,String id);
    void updaterealnum(double realnum,String id);
    void updateoflag(String itembarcode);
    String getMaxIdByBillnum(String para);
    String getMaxHGBillnum(String para);
    String getMaxBFBillnum(String para);
}