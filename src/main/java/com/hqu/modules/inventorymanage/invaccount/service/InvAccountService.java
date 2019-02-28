/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.invaccount.service;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.service.ItemService;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetail;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billtype.entity.BillType;
import com.hqu.modules.inventorymanage.billtype.service.BillTypeService;
import com.hqu.modules.inventorymanage.bin.entity.Bin;
import com.hqu.modules.inventorymanage.bin.service.BinService;
import com.hqu.modules.inventorymanage.location.entity.Location;
import com.hqu.modules.inventorymanage.location.service.LocationService;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.hqu.modules.inventorymanage.warehouse.service.WareHouseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.mapper.InvAccountMapper;
import org.springframework.util.StringUtils;

/**
 * 库存账Service
 * @author M1ngz
 * @version 2018-04-22
 */
@Service
@Transactional(readOnly = true)
public class InvAccountService extends CrudService<InvAccountMapper, InvAccount> {
    @Autowired
    private BillTypeService billTypeService;
    @Autowired
    private WareHouseService wareHouseService;
    @Autowired
    private BinService binService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private ItemService itemService;

	@Transactional(readOnly=false)
	public Boolean updateRealQty(Map<String, Object>map) {
    	return mapper.updateRealQty(map);
    }
	public InvAccount get(String id) {
		return super.get(id);
	}


    public List<InvAccount> findList(InvAccount invAccount) {
		return super.findList(invAccount);
	}
	
	public Page<InvAccount> findPage(Page<InvAccount> page, InvAccount invAccount) {
		return super.findPage(page, invAccount);
	}
	
	@Transactional(readOnly = false)
	public void save(InvAccount invAccount) {
		/*
		*
		LEFT JOIN inv_warehouse ware ON ware.ware_id = a.ware_id
		LEFT JOIN mdm_item item ON item.code = a.item_code
		LEFT JOIN inv_bin bin ON bin.bin_id = a.bin_id
		LEFT JOIN inv_location loc ON loc.loc_id = a.loc_id

		* */
		if(invAccount.getWare().getWareID().length() == 32){
		    WareHouse wareHouse =  wareHouseService.get(invAccount.getWare().getWareID());
		    invAccount.setWare(wareHouse);
        }
        if(invAccount.getItem().getCode().length() == 32){
		    Item item = itemService.get(invAccount.getItem().getCode());
		    invAccount.setItem(item);
        }
        if(invAccount.getBin() != null && invAccount.getBin().getBinId().length() == 32){
		    Bin bin = binService.get(invAccount.getBin().getBinId());
		    invAccount.setBin(bin);
        }
        if(invAccount.getLoc() != null && invAccount.getLoc().getLocId().length() == 32){
            Location location = locationService.get(invAccount.getLoc().getLocId());
            invAccount.setLoc(location);
        }
	    super.save(invAccount);
	}
	
	@Transactional(readOnly = false)
	public void delete(InvAccount invAccount) {
		super.delete(invAccount);
	}

//	@Transactional(readOnly = false,rollbackFor = Exception.class)
//	public Boolean post(BillMain billMain){
//		if(StringUtils.isEmpty(billMain)){
//			logger.error("传入BillMain为空");
//			return false;
//		}
//		if(StringUtils.isEmpty(billMain.getWare())){
//			logger.error("传入BillMain Ware为空");
//			return false;
//		}
//		if(StringUtils.isEmpty(billMain.getBillDetailList())){
//			logger.error("传入BillMain billDetailList为空");
//			return false;
//		}
//
//		InvAccount dbInvAccount;
//		InvAccount invAccount;
//		BillType billType;
//		//计算单据中的每一个单据详情
//		for(BillDetail detail : billMain.getBillDetailList()){
//			if(StringUtils.isEmpty(detail)){
//				logger.error("billDetail为空");
//				continue;
//			}
//            if(StringUtils.isEmpty(detail.getItem())){
//                logger.error("billDetail item为空");
//                continue;
//            }
//            invAccount = new InvAccount();
//			dbInvAccount = new InvAccount();
//            getInvAccount(billMain, dbInvAccount, invAccount, detail);
//
//			//设置仓库
//			invAccount.setWare(wareHouseService.get(billMain.getWare().getId()));
//            //根据仓库AdminMode判断时候需要设置
//            handleAdminMode(billMain, invAccount, detail);
//
//            billType = billTypeService.get(billMain.getIo().getId());
//
//            if (count(invAccount, billType, detail)) return Boolean.FALSE;
//
//            try {
//			    if(StringUtils.isEmpty(invAccount.getId())){
//                    super.save(invAccount);
//                } else {
//			        invAccount.preUpdate();
//			        mapper.update(invAccount);
//                }
//			}catch (Exception e){
//				e.printStackTrace();
//				logger.debug("保存库存帐出错");
//				return false;
//			}
//		}
//
//		return true;
//	}

    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public Map<String,Object> post(BillMain billMain){
        if(StringUtils.isEmpty(billMain)){
            logger.error("传入BillMain为空");
            return postFailed("传入BillMain为空");
        }
        if(StringUtils.isEmpty(billMain.getWare())){
            logger.error("传入BillMain Ware为空");
            return postFailed("传入BillMain Ware为空");
        }
        if(StringUtils.isEmpty(billMain.getBillDetailList())){
            logger.error("传入BillMain billDetailList为空");
            return postFailed("传入BillMain billDetailList为空");
        }

        InvAccount dbInvAccount;
        InvAccount invAccount;
        BillType billType;
        //计算单据中的每一个单据详情
        for(BillDetail detail : billMain.getBillDetailList()){
            if(StringUtils.isEmpty(detail)){
                logger.error("billDetail为空");
                continue;
            }
            if(StringUtils.isEmpty(detail.getItem())){
                logger.error("billDetail item为空");
                continue;
            }
            invAccount = new InvAccount();
            dbInvAccount = new InvAccount();
            getInvAccount(billMain, dbInvAccount, invAccount, detail);

            //设置仓库
            invAccount.setWare(wareHouseService.get(billMain.getWare().getId()));

            //根据仓库AdminMode判断时候需要设置
            handleAdminMode(billMain, invAccount, detail);


            billType = billTypeService.get(billMain.getIo().getIoType());

            if (count(invAccount, billType, detail))
                return postFailed("传入BillMain Ware为空");

            try {
                if(StringUtils.isEmpty(invAccount.getId())){
                	logger.debug("save--->----");
                    super.save(invAccount);
                } else {
                	logger.debug("update---<-----");
                    invAccount.preUpdate();
                    mapper.update(invAccount);
                }
            }catch (Exception e){
                e.printStackTrace();
                logger.debug("保存库存帐出错");

                return postFailed("保存库存帐出错");
            }
        }

        return postSuccess("过账成功");
    }

    private void getInvAccount(BillMain billMain, InvAccount dbInvAccount, InvAccount invAccount, BillDetail detail) {
        dbInvAccount.setWare(billMain.getWare());
        dbInvAccount.setItem(detail.getItem());
        dbInvAccount.setYear(billMain.getPeriod().getPeriodId().substring(0,4));
        dbInvAccount.setPeriod(billMain.getPeriod().getPeriodId().substring(4,6));
        //根据wareId+itemCode+period查询
        dbInvAccount = mapper.getByInvAccount(dbInvAccount);
        if(!StringUtils.isEmpty(dbInvAccount)){
            BeanUtils.copyProperties(dbInvAccount,invAccount);
        } else {
            logger.debug("查询库存失败");
            logger.debug("新增库存帐记录");
            //记录不存在时为新增
            invAccount.setWare(billMain.getWare());
            invAccount.setItem(detail.getItem());
            invAccount.setYear(billMain.getPeriod().getPeriodId().substring(0,4));
            invAccount.setPeriod(billMain.getPeriod().getPeriodId().substring(4,6));

        }
    }

    private void handleAdminMode(BillMain billMain, InvAccount invAccount, BillDetail detail) {
	    logger.debug("库房标志1："+wareHouseService.get(billMain.getWare().getId()).getAdminMode());
	    logger.debug("库房标志2："+wareHouseService.get(billMain.getWare().getId()).getAdminMode());
        if("B".equals(wareHouseService.get(billMain.getWare().getId()).getAdminMode())){
            invAccount.setBin(binService.get(detail.getBin().getId()));
        } else if("L".equals(wareHouseService.get(billMain.getWare().getId()).getAdminMode())){
            invAccount.setBin(binService.get(detail.getBin().getId()));
            invAccount.setLoc(locationService.get(detail.getLoc().getId()));
        }
    }

    private boolean count(InvAccount invAccount, BillType billType, BillDetail detail) {
	    //init
        if(StringUtils.isEmpty(invAccount.getNowQty())){
            invAccount.setNowQty(0.0);
        }
        if(StringUtils.isEmpty(invAccount.getNowSum())){
            invAccount.setNowSum(0.0);
        }
        if(StringUtils.isEmpty(invAccount.getBeginQty())){
            invAccount.setBeginQty(0.0);
        }
        if(StringUtils.isEmpty(invAccount.getBeginSum())){
            invAccount.setBeginSum(0.0);
        }
        if(StringUtils.isEmpty(invAccount.getCurrInQty())){
            invAccount.setCurrInQty(0.0);
        }
        if(StringUtils.isEmpty(invAccount.getCurrInSum())){
            invAccount.setCurrInSum(0.0);
        }
        if(StringUtils.isEmpty(invAccount.getCurrOutQty())){
            invAccount.setCurrOutQty(0.0);
        }
        if(StringUtils.isEmpty(invAccount.getCurrOutSum())){
            invAccount.setCurrOutSum(0.0);
        }
        if(StringUtils.isEmpty(invAccount.getCostPrice())){
            invAccount.setCostPrice(0.0);
        }


        try {
            //根据单据类型中设置 计算各字段
            if (!"0".equals(billType.getCurrQty())) {
                invAccount.setNowQty(operate(billType.getCurrQty(), invAccount.getNowQty(), detail.getItemQty()));
                invAccount.setNowSum(operate(billType.getCurrQty(), invAccount.getNowSum(), detail.getItemQty() * detail.getItemPrice()));
            }
            if (!"0".equals(billType.getBeginQty())) {

                invAccount.setBeginQty(operate(billType.getBeginQty(), invAccount.getBeginQty(), detail.getItemQty()));
                invAccount.setBeginSum(operate(billType.getBeginQty(), invAccount.getBeginSum(), detail.getItemQty() * detail.getItemPrice()));

            }
            if (!"0".equals(billType.getInQty())) {
                invAccount.setCurrInQty(operate(billType.getInQty(), invAccount.getCurrInQty(), detail.getItemQty()));
                invAccount.setCurrInSum(operate(billType.getInQty(), invAccount.getCurrInSum(), detail.getItemQty() * detail.getItemPrice()));

            }
            if (!"0".equals(billType.getOutQty())) {
                invAccount.setCurrOutQty(operate(billType.getOutQty(), invAccount.getCurrOutQty(), detail.getItemQty()));
                invAccount.setCurrOutSum(operate(billType.getOutQty(), invAccount.getCurrOutSum(), detail.getItemQty() * detail.getItemPrice()));

            }
            if (!"0".equals(billType.getCostPrice())) {
                invAccount.setCostPrice(invAccount.getNowSum() / invAccount.getNowQty());
                if(invAccount.getNowQty() == 0){
                    invAccount.setCostPrice(detail.getItemPrice());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    public List<InvAccount> findAllItemByWareCondition(InvAccount invAccount){
	    return mapper.findAllItemByWareCondition(invAccount);
    }

    private Double operate(String flag,Double source,Double para){
        if(StringUtils.isEmpty(flag)){
            return 0.0;
        }
        if( StringUtils.isEmpty(source)){
            source = 0.0;
        }
        if(StringUtils.isEmpty(para)){
            para = 0.0;
        }
        if("+".equals(flag)){
            return source + para;
        } else if("-".equals(flag)){
            return source - para;
        } else {
            return source;
        }
    }
    @Transactional(readOnly=false)
    public InvAccount getByInvAccount(InvAccount invAccount) {
    	return mapper.getByInvAccount(invAccount);
    }

    private Map<String,Object> postFailed(String msg){
        Map<String,Object> map = Maps.newHashMap();
        map.put("result",false);
        map.put("msg",msg);
        return map;
    }
    private Map<String,Object> postSuccess(String msg){
        Map<String,Object> map = Maps.newHashMap();
        map.put("result",true);
        map.put("msg",msg);
        return map;
    }
    
}