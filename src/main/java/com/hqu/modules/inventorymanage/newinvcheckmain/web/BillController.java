/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.newinvcheckmain.web;

import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.inventorymanage.billdetailcodes.entity.BillDetailCodes;
import com.hqu.modules.inventorymanage.billdetailcodes.mapper.BillDetailCodesMapper;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetail;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetailCode;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billmain.mapper.BillDetailMapper;
import com.hqu.modules.inventorymanage.billmain.service.BillMainService;
import com.hqu.modules.inventorymanage.bin.entity.Bin;
import com.hqu.modules.inventorymanage.location.entity.Location;
import com.hqu.modules.inventorymanage.newinvcheckmain.mapper.NewinvCheckMainMapper;
import com.hqu.modules.inventorymanage.newinvcheckmain.service.NewinvCheckMainService;
import com.hqu.modules.inventorymanage.outsourcingoutput.service.OutsourcingOutputService;
import com.hqu.modules.inventorymanage.outsourcingoutput.web.OutSourcingOutput;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 超期复验Controller
 * @author Neil
 * @version 2018-06-15
 */
@Controller
@RequestMapping(value = "${adminPath}/newinvcheckmain/newinvCheck")
public class BillController extends BaseController {
	@Autowired
	BillDetailMapper billDetailMapper;

	@Autowired
	private NewinvCheckMainService newinvCheckMainService;

	@Autowired
	private BillMainService billMainService;

	@Autowired
	private BillDetailCodesMapper billDetailCodesMapper;

	@Autowired
	private NewinvCheckMainMapper newinvCheckMainMapper;

	@Autowired
	private OutsourcingOutputService outsourcingOutputService;

	@ModelAttribute
	public BillMain get(@RequestParam(required=false) String id) {
		BillMain entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = billMainService.get(id);
			entity.setBillDetailList(billDetailMapper.findList(new BillDetail(entity)));
			List<BillDetailCodes> list= newinvCheckMainMapper.getBillDetailCodes(entity.getBillNum());
			List<BillDetailCode> codeList=new ArrayList<>();
			for(int i=0;i<list.size();i++){
				BillDetailCode billDetailCode=new BillDetailCode();
				billDetailCode.setBillNum(entity);
				Item item=new Item();
				item.setCode(list.get(i).getItemCode());
				billDetailCode.setItem(item);
				billDetailCode.setItemBarcode(list.get(i).getItemBarcode());
				billDetailCode.setSupBarcode(list.get(i).getSupBarcode());
				Bin bin=new Bin();
				bin.setBinId(list.get(i).getBinId());
				billDetailCode.setBin(bin);
				Location location=new Location();
				location.setLocId(list.get(i).getLocId());
				billDetailCode.setLoc(location);
				codeList.add(billDetailCode);
			}
			entity.setBillDetailCodeList(codeList);
		}
		if (entity == null){
			entity = new BillMain();
		}
		return entity;
	}
	@RequiresPermissions(value={"newinvcheckmain:newinvCheckMain:view","newinvcheckmain:newinvCheckMain:add","newinvcheckmain:newinvCheckMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "HGForm")
	public String HGForm(BillMain billMain, Model model,String id) {
		List<BillDetailCode> codeList = outsourcingOutputService.findCodeByBillNum(billMain.getBillNum());
		billMain.setBillDetailCodeList(codeList);
		model.addAttribute("billMain", billMain);
		return "inventorymanage/newinvcheckmain/HGBillForm";
	}
	@RequiresPermissions(value={"newinvcheckmain:newinvCheckMain:view","newinvcheckmain:newinvCheckMain:add","newinvcheckmain:newinvCheckMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "HGGForm")
	public String HGGForm(BillMain billMain, Model model) {
		List<BillDetailCode> codeList = outsourcingOutputService.findCodeByBillNum(billMain.getBillNum());
		billMain.setBillDetailCodeList(codeList);
		model.addAttribute("billMain", billMain);
		return "inventorymanage/newinvcheckmain/HGGBillForm";
	}
	@RequiresPermissions(value={"newinvcheckmain:newinvCheckMain:view","newinvcheckmain:newinvCheckMain:add","newinvcheckmain:newinvCheckMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "BFForm")
	public String BFForm(BillMain billMain, Model model) {
		List<BillDetailCode> codeList = outsourcingOutputService.findCodeByBillNum(billMain.getBillNum());
		billMain.setBillDetailCodeList(codeList);
		model.addAttribute("billMain", billMain);
		return "inventorymanage/newinvcheckmain/BFBillForm";
	}
	@RequiresPermissions(value={"newinvcheckmain:newinvCheckMain:view","newinvcheckmain:newinvCheckMain:add","newinvcheckmain:newinvCheckMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "BFCForm")
	public String BFCForm(BillMain billMain, Model model) {
		List<BillDetailCode> codeList = outsourcingOutputService.findCodeByBillNum(billMain.getBillNum());
		billMain.setBillDetailCodeList(codeList);
		model.addAttribute("billMain", billMain);
		return "inventorymanage/newinvcheckmain/BFCBillForm";
	}


	@RequiresPermissions(value={"newinvcheckmain:newinvCheckMain:view","newinvcheckmain:newinvCheckMain:add","newinvcheckmain:newinvCheckMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "HGuozhang")
	public String HGuozhang(BillMain billMain, RedirectAttributes redirectAttributes){
		String billnum=billMain.getBillNum();
		List<BillDetailCodes> list= newinvCheckMainMapper.getBillDetailCodes(billnum);
		for(int i=0;i<list.size();i++){
			if(list.get(i).getBinId()==null||list.get(i).getLocId()==null){
				addMessage(redirectAttributes, "还有合格物料没有入库");
				return "redirect:"+ Global.getAdminPath()+"/newinvcheckmain/newinvCheckMain/HGGSearch/?repage";
			}
		}
		addMessage(redirectAttributes, "过账成功");
		return "redirect:"+ Global.getAdminPath()+"/newinvcheckmain/newinvCheckMain/HGGSearch/?repage";
	}

	@RequiresPermissions(value={"newinvcheckmain:newinvCheckMain:view","newinvcheckmain:newinvCheckMain:add","newinvcheckmain:newinvCheckMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "BGuozhang")
	public String BGuozhang(BillMain billMain, RedirectAttributes redirectAttributes){
		String billnum=billMain.getBillNum();
		List<BillDetailCodes> list= newinvCheckMainMapper.getBillDetailCodes(billnum);
		while (!list.isEmpty()){
			BillDetailCodes billDetailCodes=list.get(0);
			String itemcode=billDetailCodes.getItemCode();
			int num=1;
			list.remove(0);
			for(int i=0;i<list.size();){
				if(itemcode.equals(list.get(i).getItemCode())){
					list.remove(i);
					num++;
				}else {
					i++;
				}
			}
			if(billMain.getWare()==null){
				break;
			}
			String wareId=billMain.getWare().getWareID();
			if(wareId==null||wareId==""){
				addMessage(redirectAttributes, "过账成功");
				return "redirect:"+ Global.getAdminPath()+"/newinvcheckmain/newinvCheckMain/BFCSearch/?repage";
			}
			double nownum=newinvCheckMainMapper.getnownum(itemcode,billMain.getWare().getWareID());
			nownum=nownum-num;
			String id=newinvCheckMainMapper.getid(itemcode,wareId);
			newinvCheckMainMapper.updatenownum(nownum,id);
		}
		addMessage(redirectAttributes, "过账成功");
		return "redirect:"+ Global.getAdminPath()+"/newinvcheckmain/newinvCheckMain/BFCSearch/?repage";
	}

}