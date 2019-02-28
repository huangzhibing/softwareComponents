package com.hqu.modules.inventorymanage.cpth.web;

import com.hqu.modules.inventorymanage.billmain.entity.BillDetail;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billmain.service.BillMainService;
import com.hqu.modules.inventorymanage.billtype.entity.BillType;
import com.hqu.modules.inventorymanage.billtype.service.BillTypeService;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
import com.hqu.modules.inventorymanage.warehouse.service.WareHouseService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
@Controller
@RequestMapping(value = "${adminPath}/cpth/cpth")
public class CpthController extends BaseController {
    @Autowired
    private BillMainService billMainService;
    @Autowired
    private InvAccountService invAccountService;
    @Autowired
    private BillTypeService billTypeService;
    @Autowired
    private WareHouseService wareHouseService;
    @ModelAttribute
    public BillMain get(@RequestParam(required=false) String id) {
        BillMain entity = null;
        if (StringUtils.isNotBlank(id)){
            entity = billMainService.get(id);
        }
        if (entity == null){
            entity = new BillMain();
        }
        return entity;
    }

    @RequestMapping(value = {"list", ""})
    public String list(String mtype,Model model) {
        model.addAttribute("mtype",mtype);
        return "inventorymanage/cpth/cpth";
    }

    @RequestMapping(value = "form")
    public String form(@RequestParam(required=false) String mtype,BillMain billMain, Model model) {
        model.addAttribute("mtype",mtype);
        model.addAttribute("billMain", billMain);
        //如果ID是空为添加
        if(StringUtils.isBlank(billMain.getId())){
            model.addAttribute("isAdd", true);
        }
        return "inventorymanage/cpth/cpthForm";
    }
    @RequestMapping(value = "save")
    public String save(BillMain billMain, Model model, String type, RedirectAttributes redirectAttributes, String page, String saleId) throws Exception{
        billMain.setId(null);
        for(BillDetail detail:billMain.getBillDetailList()){
            detail.setId("");
        }
        billMain.setBillDate(new Date());
        if(billMainService.findPeriodByTime(billMain.getBillDate())!=null) {
            billMain.setPeriod(billMainService.findPeriodByTime(billMain.getBillDate()));
        }
        BillType billType = new BillType();
        billType.setIoType("CT01");
        if(billTypeService.findList(billType).size()==1){
            billMain.setIo(billTypeService.findList(billType).get(0));
        }
        billMain.setBillFlag("N");
        billMain.setCorBillNum(billMain.getBillNum());
        billMainService.save(billMain);
        addMessage(redirectAttributes, "保存单据成功");
        return "redirect:"+Global.getAdminPath()+"/cpth/cpth?mtype=i";
    }
    @Transactional
    @RequestMapping(value = "post")
    public String post(BillMain billMain, Model model, String type, RedirectAttributes redirectAttributes, String page, String saleId) throws Exception {
        String curperiod = wareHouseService.get(billMain.getWare().getId()).getCurrPeriod();
        //判断核算期是否一致
        if (!curperiod.equals(billMain.getPeriod().getPeriodId())) {
            addMessage(redirectAttributes, "过账失败，当前核算期与仓库核算期不一致！");
            return "redirect:" + Global.getAdminPath() + "/cpth/cpth?mtype=p";
        }
        try {
            List<BillDetail> detaillist = billMain.getBillDetailList();
            for (int i = 0; i < detaillist.size(); i++) {
                detaillist.get(i).setItemQty(detaillist.get(i).getItemQty() * (-1));
            }
            billMain.setBillDetailList(detaillist);
            BillType db=billMain.getIo();
            BillType billType=new BillType();
            billType.setIoType("CO01");
            billMain.setIo(billTypeService.findList(billType).get(0));
            Map<String, Object> resultMap = invAccountService.post(billMain);
            Boolean judge = (Boolean) resultMap.get("result");

            if (judge) {
                List<BillDetail> dlist = billMain.getBillDetailList();
                for (int i = 0; i < dlist.size(); i++) {
                    dlist.get(i).setItemQty(detaillist.get(i).getItemQty() * (-1));
                }
                billMain.setBillDetailList(dlist);
                billMain.setIo(db);
                if(restoreAcount(billMain)) {
                    billMain.setBillFlag("T");
                    billMainService.save(billMain);
                    addMessage(redirectAttributes, "过账成功");
                }else {
                    addMessage(redirectAttributes, "过账失败");
                }
            } else {
                addMessage(redirectAttributes, "过账失败: " + resultMap.get("msg"));
            }
            return "redirect:" + Global.getAdminPath() + "/cpth/cpth?mtype=p";
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            addMessage(redirectAttributes, "过账失败");
            return "redirect:" + Global.getAdminPath() + "/cpth/cpth?mtype=p";
        }
    }
    /**
     * 还原库存帐的可用量
     */
    public boolean restoreAcount(BillMain billMain) {
        InvAccount invAccount=new InvAccount();
        InvAccount dbInvAccount=new InvAccount();
        double num;
        for(BillDetail billDetail:billMain.getBillDetailList()) {
            if(billDetail.getItem()!=null) {
                billDetail.getItem().setCode(billDetail.getItem().getId());
            }
            invAccount.setWare(billMain.getWare());
            invAccount.setItem(billDetail.getItem());
            invAccount.setYear(billMain.getPeriod().getPeriodId().substring(0,4));
            invAccount.setPeriod(billMain.getPeriod().getPeriodId().substring(4,6));
            if(StringUtils.isNotBlank(billDetail.getBin().getId())){
                billDetail.getBin().setBinId(billDetail.getBin().getId());
                invAccount.setBin(billDetail.getBin());
                if(StringUtils.isNotBlank(billDetail.getLoc().getId())){
                    billDetail.getLoc().setLocId(billDetail.getLoc().getId());
                    invAccount.setLoc(billDetail.getLoc());
                }
            }
            dbInvAccount=invAccountService.getByInvAccount(invAccount);
            num=dbInvAccount.getRealQty()+billDetail.getItemQty();
            dbInvAccount.setRealQty(num);
            invAccountService.save(dbInvAccount);
        }
        return true;
    }
}
