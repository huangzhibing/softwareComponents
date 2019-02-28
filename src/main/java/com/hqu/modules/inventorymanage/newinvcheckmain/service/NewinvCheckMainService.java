/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.newinvcheckmain.service;

import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.service.ItemService;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetail;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetailCode;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billtype.entity.BillType;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
import com.hqu.modules.inventorymanage.invaccountcode.entity.InvAccountCode;
import com.hqu.modules.inventorymanage.invaccountcode.service.InvAccountCodeService;
import com.hqu.modules.inventorymanage.invcheckmain.entity.ReinvCheckMain;
import com.hqu.modules.inventorymanage.newinvcheckmain.entity.NewinvCheckDetail;
import com.hqu.modules.inventorymanage.newinvcheckmain.entity.NewinvCheckDetailCode;
import com.hqu.modules.inventorymanage.newinvcheckmain.entity.NewinvCheckMain;
import com.hqu.modules.inventorymanage.newinvcheckmain.mapper.NewinvCheckDetailCodeMapper;
import com.hqu.modules.inventorymanage.newinvcheckmain.mapper.NewinvCheckDetailMapper;
import com.hqu.modules.inventorymanage.newinvcheckmain.mapper.NewinvCheckMainMapper;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.hqu.modules.inventorymanage.warehouse.service.WareHouseService;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckDetailCode;
import com.hqu.modules.qualitymanage.purreport.entity.PurReport;
import com.hqu.modules.qualitymanage.purreport.entity.PurReportRSn;
import com.hqu.modules.qualitymanage.purreport.mapper.PurReportRSnMapper;
import com.hqu.modules.qualitymanage.purreport.service.PurReportService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.UDP.UdpUtil;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 超期复验Service
 * @author Neil
 * @version 2018-08-21
 */
@Service
@Transactional(readOnly = true)
public class NewinvCheckMainService extends CrudService<NewinvCheckMainMapper, NewinvCheckMain> {

	@Autowired
	private NewinvCheckDetailCodeMapper newinvCheckDetailCodeMapper;

	@Autowired
	private InvAccountService invAccountService;

	@Autowired
	private InvAccountCodeService invAccountCodeService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private NewinvCheckDetailMapper newinvCheckDetailMapper;

	@Autowired
    private InvCheckService billMainInvCheckService;

	@Autowired
	private PurReportService purReportService;

	@Autowired
	private PurReportRSnMapper purReportRSnMapper;

	@Autowired
	private NewinvCheckMainMapper newinvCheckMainMapper;

	@Override
	public NewinvCheckMain get(String id) {
		NewinvCheckMain newinvCheckMain = super.get(id);
		newinvCheckMain.setNewinvCheckDetailCodeList(newinvCheckDetailCodeMapper.findList(new NewinvCheckDetailCode(newinvCheckMain)));
		newinvCheckMain.setNewinvCheckDetailList(newinvCheckDetailMapper.findList(new NewinvCheckDetail(newinvCheckMain)));
		return newinvCheckMain;
	}

	public List<NewinvCheckMain> findList(NewinvCheckMain newinvCheckMain) {
		return super.findList(newinvCheckMain);
	}

	public Page<NewinvCheckMain> findPage(Page<NewinvCheckMain> page, NewinvCheckMain newinvCheckMain) {
		return super.findPage(page, newinvCheckMain);
	}

	@Transactional(readOnly = false)
	public void save(NewinvCheckMain newinvCheckMain) {
		super.save(newinvCheckMain);
		for (NewinvCheckDetailCode newinvCheckDetailCode : newinvCheckMain.getNewinvCheckDetailCodeList()){
			if (newinvCheckDetailCode.getId() == null){
				continue;
			}
			if (NewinvCheckDetailCode.DEL_FLAG_NORMAL.equals(newinvCheckDetailCode.getDelFlag())){
				if (StringUtils.isBlank(newinvCheckDetailCode.getId())){
					newinvCheckDetailCode.setBillNum(newinvCheckMain);
					newinvCheckDetailCode.preInsert();
					newinvCheckDetailCodeMapper.insert(newinvCheckDetailCode);
				}else{
					newinvCheckDetailCode.preUpdate();
					newinvCheckDetailCodeMapper.update(newinvCheckDetailCode);
				}
			}else{
				newinvCheckDetailCodeMapper.delete(newinvCheckDetailCode);
			}
		}
		for (NewinvCheckDetail newinvCheckDetail : newinvCheckMain.getNewinvCheckDetailList()){
			if (newinvCheckDetail.getId() == null){
				continue;
			}
			if (NewinvCheckDetail.DEL_FLAG_NORMAL.equals(newinvCheckDetail.getDelFlag())){
				if (StringUtils.isBlank(newinvCheckDetail.getId())){
					newinvCheckDetail.setBillNum(newinvCheckMain);
					newinvCheckDetail.preInsert();
					newinvCheckDetailMapper.insert(newinvCheckDetail);
				}else{
					newinvCheckDetail.preUpdate();
					newinvCheckDetailMapper.update(newinvCheckDetail);
				}
			}else{
				newinvCheckDetailMapper.delete(newinvCheckDetail);
			}
		}
	}

	@Transactional(readOnly = false)
	public void delete(NewinvCheckMain newinvCheckMain) {
		super.delete(newinvCheckMain);
		newinvCheckDetailCodeMapper.delete(new NewinvCheckDetailCode(newinvCheckMain));
		newinvCheckDetailMapper.delete(new NewinvCheckDetail(newinvCheckMain));
	}

	public String getMaxIdByBillnum(String para){
		return mapper.getMaxIdByBillnum(para);
	}

	public String getMaxHGBillnum(String para){
		return mapper.getMaxHGBillnum(para);
	}

	public String getMaxBFBillnum(String para){
		return mapper.getMaxBFBillnum(para);
	}

	public List<InvAccountCode> getOutDateList(String wareId){
		Item item=new Item();
		List<Item> list=itemService.findList(item);
		InvAccountCode invAccountCode=new InvAccountCode();
		String ware=newinvCheckMainMapper.getWare(wareId);
		WareHouse wareHouse=new WareHouse();
		if(ware!=null&&!ware.isEmpty()){
			wareHouse.setId(wareId);
			wareHouse.setWareID(ware);
		}else {
			List<InvAccountCode> codeList=new ArrayList<>();
			return codeList;
		}
		invAccountCode.setWare(wareHouse);
		List<InvAccountCode> listCode=invAccountCodeService.findList(invAccountCode);
		for(int i=0;i<listCode.size();){
			if(!listCode.get(i).getOflag().equals("0")||listCode.get(i).getItem()==null||listCode.get(i).getItem().equals("")){
				listCode.remove(i);
			}else {
				i++;
			}
		}
		for(int i=0;i<list.size();i++){
			Item itemtemp=list.get(i);
			if(itemtemp.getCycleTime()==null||itemtemp.getCycleTime().equals("")){
				for(int j=0;j<listCode.size();){
					if(listCode.get(j).getItem().getCode().equals(list.get(i).getCode())){
						listCode.remove(j);
					}else{
						j++;
					}
				}
			}else{
				int cycDay=list.get(i).getCycleTime();
				if(list.get(i).getCode()==null||list.get(i).getCode().equals("")){
					continue;
				}
				String code=list.get(i).getCode();
				Date date=new Date();
				Long cDate=(date.getTime()-(long)cycDay*24*60*60*1000);
				for(int h=0;h<listCode.size();){
					if(listCode.get(h).getItem().getCode().equals(code)){
						if(listCode.get(h).getCreateDate().getTime()<cDate){
							h++;
						}else{
							listCode.remove(h);
						}
					}else{
						h++;
					}
				}
			}
		}
		return listCode;
	}

	/*
	@Transactional(readOnly = false)
	public void lockItem(){
		List<InvAccountCode> list=this.getOutDateList();
		//InvAccountCode invAccountCode=null;
		
		for(int i=0;i<list.size();i++){			
			InvAccountCode invAccountCode = new InvAccountCode();			
			invAccountCode=list.get(i);
			invAccountCode.setOflag("2");
			invAccountCodeService.save(invAccountCode);
			
			invAccountCode = list.get(i);
			int num=1;
			Item item=invAccountCode.getItem();
			for(int j=1;j<list.size();){
				if(list.get(j).getItem().equals(item)){
					num++;
					list.remove(j);
				}else{
					j++;
				}
			}
			InvAccount invAccount=new InvAccount();
			invAccount.setItem(item);
			if(invAccountService.findList(invAccount).isEmpty())
				return;
			invAccount=invAccountService.findList(invAccount).get(0);
			invAccount.setRealQty(invAccount.getRealQty()-num);
			invAccountService.save(invAccount);
		}
	}

	*/

	@Transactional(readOnly = false)
    public void dispose(NewinvCheckMain newinvCheckMain) throws Exception{
	    List<BillDetail> HGList=new ArrayList<>();
	    List<BillDetail> BFList=new ArrayList<>();
	    List<BillDetailCode> HGListCode=new ArrayList<>();
	    List<BillDetailCode> BFListCode=new ArrayList<>();
	    List<NewinvCheckDetail> list=newinvCheckMain.getNewinvCheckDetailList();
	    List<NewinvCheckDetailCode> listcode=newinvCheckMain.getNewinvCheckDetailCodeList();
		PurReport purReport=new PurReport();
		ReinvCheckMain reinvCheckMain=new ReinvCheckMain();
		reinvCheckMain.setId(newinvCheckMain.getId());
		purReport.setReinvCheckMain(reinvCheckMain);
		PurReportRSn purReportRSn=new PurReportRSn();
		if(!purReportService.findList(purReport).isEmpty()){
			purReportRSn.setPurReport(purReportService.findList(purReport).get(0));
		}
	    List<PurReportRSn> listRsn=purReportRSnMapper.findList(purReportRSn);
	    while (!listcode.isEmpty()){
	        NewinvCheckDetailCode newinvCheckDetailCode=listcode.get(0);
	        String itemCode=newinvCheckDetailCode.getItemCode();
	        int HGNum=0;
	        int BFNum=0;
	        for(int i=0;i<listcode.size();){
	            if(listcode.get(i).getItemCode().equals(itemCode)){
	                String itemBarcode=listcode.get(i).getItemBarcode();
                    Item item=new Item();
                    item.setCode(listcode.get(i).getItemCode());
                    String result="";
                    for(PurReportRSn purReportRSn1:listRsn){
						if (purReportRSn1.getObjSn().equals(itemBarcode)){
							result=purReportRSn1.getCheckResult();
							break;
						}
					}
					if("合格".equals(result)){
						HGNum++;
						BillDetailCode billDetailCode=new BillDetailCode();
						billDetailCode.setItemBarcode(listcode.get(i).getItemBarcode());
						billDetailCode.setSupBarcode(listcode.get(i).getSupBarcode());
						billDetailCode.setItem(item);
						HGListCode.add(billDetailCode);
					}else {
						BFNum++;
						BillDetailCode billDetailCode=new BillDetailCode();
						billDetailCode.setItemBarcode(listcode.get(i).getItemBarcode());
						billDetailCode.setSupBarcode(listcode.get(i).getSupBarcode());
						billDetailCode.setItem(item);
						BFListCode.add(billDetailCode);
					}
					listcode.remove(i);
                }else {
	                i++;
                }
            }
	        Item item=new Item();
	        item.setCode(newinvCheckDetailCode.getItemCode());
	        if (HGNum!=0){
                BillDetail HGBillDetail=new BillDetail();
                HGBillDetail.setItem(item);
                HGBillDetail.setItemQty((double) HGNum);
                HGList.add(HGBillDetail);
            }
            if(BFNum!=0){
                BillDetail BFBillDetail=new BillDetail();
                BFBillDetail.setItem(item);
                BFBillDetail.setItemQty((double) BFNum);
                BFList.add(BFBillDetail);
            }
        }
	    if(!HGList.isEmpty()){
            BillMain hgBillMain=new BillMain();
			BillType billType=new BillType();
			billType.setIoType("HG01");
			hgBillMain.setIo(billType);
            hgBillMain.setBillDetailList(HGList);
            hgBillMain.setBillDetailCodeList(HGListCode);
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String billnum =getMaxHGBillnum("HG"+sdf.format(date));
			if(StringUtils.isEmpty(billnum)) {
				billnum="00000";
			}else {
				billnum=String.format("%05d", Integer.parseInt(billnum.substring(10,15))+1);
			}
			hgBillMain.setBillNum("HG"+sdf.format(date)+billnum);
			hgBillMain.setWare(newinvCheckMain.getWareId());
			for(int h=0;h<HGList.size();h++){
				HGList.get(h).setBillNum(hgBillMain);
			}
            billMainInvCheckService.save(hgBillMain);
            for(int h=0;h<hgBillMain.getBillDetailCodeList().size();h++){
				billMainInvCheckService.saveBillCode(hgBillMain, hgBillMain.getBillDetailCodeList().get(h));
				hgBillMain.getBillDetailCodeList().get(h).setBillNum(hgBillMain);
			}

        }
        if(!BFList.isEmpty()){
	        BillMain bfBillMain=new BillMain();
			BillType billType=new BillType();
			billType.setIoType("BF01");
			bfBillMain.setIo(billType);
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String billnumBf=getMaxBFBillnum("BF"+sdf.format(date));
			if(StringUtils.isEmpty(billnumBf)){
				billnumBf="00000";
			}else{
				billnumBf=String.format("%05d", Integer.parseInt(billnumBf.substring(10,15))+1);
			}
			bfBillMain.setBillNum("BF"+sdf.format(date)+billnumBf);
	        bfBillMain.setBillDetailList(BFList);
	        bfBillMain.setBillDetailCodeList(BFListCode);
	        bfBillMain.setWare(newinvCheckMain.getWareId());
	        for(int h=0;h<BFList.size();h++){
	        	BFList.get(h).setBillNum(bfBillMain);
			}
			billMainInvCheckService.save(bfBillMain);
			for(int h=0;h<bfBillMain.getBillDetailCodeList().size();h++){
				UdpUtil.recheckInTreasury(bfBillMain.getBillDetailCodeList().get(h).getItemBarcode());
				billMainInvCheckService.saveBillCode(bfBillMain, bfBillMain.getBillDetailCodeList().get(h));
				bfBillMain.getBillDetailCodeList().get(h).setBillNum(bfBillMain);
			}
        }
    }

	@Transactional(readOnly = false)
	public void audit(NewinvCheckMain newinvCheckMain) {
		WareHouse wareHouse=newinvCheckMain.getWareId();
		String autoflag=newinvCheckMainMapper.getAutoFlag(wareHouse.getWareID());
		if (autoflag.equals("Y")){
			List<NewinvCheckDetailCode> list=newinvCheckMain.getNewinvCheckDetailCodeList();
			for(int i=0;i<list.size();i++){
				UdpUtil.recheckOutTreasury(list.get(i).getItemBarcode());
			}
		}
		List<NewinvCheckDetail> detailList=newinvCheckMain.getNewinvCheckDetailList();
		List<NewinvCheckDetailCode> detailCodeList=newinvCheckMain.getNewinvCheckDetailCodeList();
		for(int i=0;i<detailCodeList.size();i++){
			newinvCheckMainMapper.updateoflag(detailCodeList.get(i).getItemBarcode());
		}
		for(int i=0;i<detailList.size();i++){
			String itemcode=detailList.get(i).getItemCode().getCode();
			Double realnum=newinvCheckMainMapper.getrealnum(itemcode,wareHouse.getWareID());
			String id=newinvCheckMainMapper.getid(itemcode,wareHouse.getWareID());
			if(realnum==null){
				realnum=0.0;
			}else {
				realnum=realnum-detailList.get(i).getItemQty();
			}
			newinvCheckMainMapper.updaterealnum(realnum,id);
		}
		newinvCheckMain.setQmsFlag("将要质检");
		save(newinvCheckMain);
	}



}