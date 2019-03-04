/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.outsourcinginput.web;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.mapper.ItemMapper;
import com.hqu.modules.basedata.item.service.ItemService;
import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.basedata.period.service.PeriodService;
import com.hqu.modules.inventorymanage.billdetailcodes.entity.BillDetailCodes;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetail;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetailCode;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billmain.mapper.BillDetailMapper;
import com.hqu.modules.inventorymanage.billmain.mapper.BillMainMapper;
import com.hqu.modules.inventorymanage.billmain.service.BillMainService;
import com.hqu.modules.inventorymanage.billtype.entity.BillType;
import com.hqu.modules.inventorymanage.billtype.entity.BillTypeWareHouse;
import com.hqu.modules.inventorymanage.billtype.service.BillTypeService;
import com.hqu.modules.inventorymanage.bin.entity.Bin;
import com.hqu.modules.inventorymanage.bin.mapper.BinMapper;
import com.hqu.modules.inventorymanage.bin.service.BinService;
import com.hqu.modules.inventorymanage.cpth.web.OutsourcingCpthController;
import com.hqu.modules.inventorymanage.employee.entity.Employee;
import com.hqu.modules.inventorymanage.employee.entity.EmployeeWareHouse;
import com.hqu.modules.inventorymanage.employee.service.EmployeeService;
import com.hqu.modules.inventorymanage.employee.service.EmployeeWareHouseService;
import com.hqu.modules.inventorymanage.finishedproduct.inboundinput.service.InBoundInputService;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
import com.hqu.modules.inventorymanage.invaccountcode.entity.InvAccountCode;
import com.hqu.modules.inventorymanage.invaccountcode.service.InvAccountCodeService;
import com.hqu.modules.inventorymanage.location.entity.Location;
import com.hqu.modules.inventorymanage.location.mapper.LocationMapper;
import com.hqu.modules.inventorymanage.location.service.LocationService;
import com.hqu.modules.inventorymanage.outsourcingoutput.service.OutsourcingOutputService;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.hqu.modules.inventorymanage.warehouse.service.WareHouseService;
import com.hqu.modules.purchasemanage.relations.entity.Relations;
import com.hqu.modules.purchasemanage.relations.service.RelationsService;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckDetail;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckMain;
import com.hqu.modules.qualitymanage.purinvcheckmain.mapper.InvCheckDetailMapper;
import com.hqu.modules.qualitymanage.purinvcheckmain.service.InvCheckMainService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators;
import com.swetake.util.Qrcode;
import jdk.nashorn.internal.runtime.Debug;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import javax.validation.ConstraintViolationException;
import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 外购件单据录入Controller
 * @author ljc
 * @version 2018-04-16
 */
@Controller
@RequestMapping(value = "${adminPath}/outsourcinginput/outsourcingInput")
public class OutsourcingInputController extends BaseController {

    @Autowired
    private BillMainService billMainService;
    @Autowired
    private BillTypeService billTypeService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private InvAccountService invAccountService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private WareHouseService wareHouseService;
    @Autowired
    private PeriodService periodService;
    @Autowired
    private BinService binService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private InvAccountCodeService invAccountCodeService;
    @Autowired
    private EmployeeWareHouseService employeeWareHouseService;
    @Autowired
    private InvCheckMainService invCheckMainService;
    @Autowired
    private RelationsService relationsService;
    @Autowired
    private BillMainMapper billMainMapper;
    @Autowired
    private BillDetailMapper billDetailMapper;
    @Autowired
    private InvCheckDetailMapper invCheckDetailMapper;
    @Autowired
    private OutsourcingOutputService outsourcingOutputService;
    @Autowired
    private OutsourcingCpthController outsourcingCpthController;
    //尝试用cache缓存存储审批意见的值
    public static HashMap<String,Object> auditCache;

    @ModelAttribute
    public BillMain get(@RequestParam(required = false) String id) {
        BillMain entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = billMainService.get(id);
        }
        if (entity == null) {
            entity = new BillMain();
        }
        return entity;
    }

    /**
     * 获取库管员
     */
    @ResponseBody
    @RequestMapping(value = {"getEmp"})
    public Employee getEmp(String wareId) {
        EmployeeWareHouse employeeWareHouse = new EmployeeWareHouse();
        WareHouse wareHouse = new WareHouse();
        wareHouse.setId(wareId);
        employeeWareHouse.setWareHouse(wareHouse);
        String id = employeeWareHouseService.findList(employeeWareHouse).get(0).getEmp().getId();
        Employee employee = employeeService.get(id);
        return employee;
    }

    /**
     * 获取仓库
     */
    @ResponseBody
    @RequestMapping(value = {"getWare"})
    public WareHouse getWare(String wareName) {
        WareHouse wareHouse = new WareHouse();
        wareHouse.setWareName(wareName);
        if (wareHouseService.findList(wareHouse) != null) {
            return wareHouseService.findList(wareHouse).get(0);
        } else return null;
    }

    /**
     * 获取批次管理标识
     */
    @ResponseBody
    @RequestMapping(value = {"getBatchFlag"})
    public String getBatchFlag(String wareId) {
        String BatchFlag = wareHouseService.get(wareId).getBatchFlag();
        logger.debug("管理标识为：" + BatchFlag);
        return BatchFlag;
    }

    /**
     * 单据列表页面
     */
    @RequiresPermissions("billmain:billMain:list")
    @RequestMapping(value = {"list", ""})
    public String list(String type, Model model) {
        model.addAttribute("type", type);
        return "inventorymanage/outsourcinginput/outsourcingInputList";
    }

    /**
     * 退货单据列表页面
     */
    @RequiresPermissions("billmain:billMain:list")
    @RequestMapping(value = {"Blist"})
    public String Blist(String type, Model model) {
        model.addAttribute("type", type);
        return "inventorymanage/outsourcinginput/outsourcingBackList";
    }

    /**
     * 单据列表数据
     */

    @ResponseBody
    @RequiresPermissions("billmain:billMain:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BillMain billMain, HttpServletRequest request, String type, HttpServletResponse response, Model model) {
        billMain.setIo(billTypeService.get("4bbbe6c045b8437b8a17433951bb84d3"));
        if ("Input".equals(type) || "Post".equals(type)) {
            billMain.setBillFlag("N");
        }
        if("Unpost".equals(type)){
            billMain.setBillFlag("T");
        }
        Page<BillMain> page = billMainService.findPage(new Page<BillMain>(request, response), billMain);
        BillMain mbillMain = null;
        for (int i = 0; i < page.getList().size(); i++) {
            mbillMain = page.getList().get(i);
//            mbillMain.setBillPerson(UserUtils.get(mbillMain.getBillPerson().getNo()));
            mbillMain.setWareEmp(employeeService.get(mbillMain.getWareEmp().getUser().getNo()));
        }
        if (!StringUtils.isEmpty(billMain.getItemCode())) {
            List<BillMain> blist = outsourcingOutputService.findBillMainByItemCode(billMain.getItemCode());
            List<BillMain> btlist = new ArrayList<>();
            if (blist != null) {
                for (int i = 0; i < blist.size(); i++) {
                    if (page.getList().contains(blist.get(i))) {
                        btlist.add(blist.get(i));
                    }
                }
            }
            page.setList(btlist);
        }System.out.println("--222---");
        return getBootstrapData(page);
    }

    /**
     * 退货单据列表数据
     */

    @ResponseBody
    @RequiresPermissions("billmain:billMain:list")
    @RequestMapping(value = "Back")
    public Map<String, Object> Back(BillMain billMain, HttpServletRequest request, String type, HttpServletResponse response, Model model) {
        billMain.setIo(billTypeService.get("WT01"));
        if ("BInput".equals(type)) {
            billMain.setBillFlag("J");
        }else if("BAudit".equals(type)){
            billMain.setBillFlag("S");
        }else if("BPost".equals(type)){
            billMain.setBillFlag("N");
        }
        Page<BillMain> page = billMainService.findPage(new Page<BillMain>(request, response), billMain);
        BillMain mbillMain = null;
        for (int i = 0; i < page.getList().size(); i++) {
            mbillMain = page.getList().get(i);
            mbillMain.setWareEmp(employeeService.get(mbillMain.getWareEmp().getUser().getNo()));
        }
        if (!StringUtils.isEmpty(billMain.getItemCode())) {
            List<BillMain> blist = outsourcingOutputService.findBillMainByItemCode(billMain.getItemCode());
            List<BillMain> btlist = new ArrayList<>();
            if (blist != null) {
                for (int i = 0; i < blist.size(); i++) {
                    if (page.getList().contains(blist.get(i))) {
                        btlist.add(blist.get(i));
                    }
                }
            }
            page.setList(btlist);
        }System.out.println("--222---");
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑单据表单页面
     */
    @RequiresPermissions(value = {"billmain:billMain:view", "billmain:billMain:add", "billmain:billMain:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BillMain billMain, Model model, String type) {
        if ("Post".equals(type)) {
            billMain.setBillFlag("N");
        }
        if (StringUtils.isBlank(billMain.getId())) {//如果ID是空为添加
            User u = UserUtils.getUser();
            //添加经办人
            billMain.setBillPerson(u);
            //添加日期
            Date date = new Date();
            billMain.setBillDate(date);
            //单据状态
            billMain.setBillFlag("A");
            //从billMainService找到根据日期定义的核算期
            Map<String, Object> map = new HashMap<>();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            map.put("today", sdf1.format(date));//查询核算期
            Period period = outsourcingOutputService.findPeriodByTime(map);
            billMain.setPeriod(period);
//			billMain.setWareEmp(employeeService.get(billMain.getWareEmp().getId()));
          //设置单据组成规则 WGI+日期+四位自动生成流水
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String newBillNum = billMainService.getMaxIdByTypeAndDate("WGI" + sdf.format(date));
            if (StringUtils.isEmpty(newBillNum)) {
                newBillNum = "0000";
            } else {
                newBillNum = String.format("%04d", Integer.parseInt(newBillNum.substring(11, 15)) + 1);
            }
            //设置单据编号
            billMain.setBillNum("WGI" + sdf.format(date) + newBillNum);
            if("BInput".equals(type)){
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                String newBillNum2 = billMainService.getMaxIdByTypeAndDate("WGT" + sdf2.format(date));
                if (StringUtils.isEmpty(newBillNum2)) {
                    newBillNum2 = "0000";
                } else {
                    newBillNum2 = String.format("%04d", Integer.parseInt(newBillNum2.substring(11, 15)) + 1);
                }
                billMain.setBillNum("WGT" + sdf.format(date) + newBillNum2);
            }
            model.addAttribute("isAdd", true);
        } else {
            List<BillDetailCode> codeList = outsourcingOutputService.findCodeByBillNum(billMain.getBillNum());
            billMain.setBillDetailCodeList(codeList);
            for (BillDetail billDetail:billMain.getBillDetailList()){
                billDetail.setItem(itemService.get(billDetail.getItem().getId()));
            }
        }
        model.addAttribute("billMain", billMain);
        model.addAttribute("type", type);
        if("BInput".equals(type)||"BAudit".equals(type)||"BPost".equals(type)||"BQuery".equals(type)){
//            if(auditCache.containsKey("Audit")) {
//                model.addAttribute("aduitComment", auditCache.get("Audit"));
//            }
            return "inventorymanage/outsourcinginput/outsourcingBackForm";
        }else {
            return "inventorymanage/outsourcinginput/outsourcingInputForm";
        }
    }

    /**
     * 保存单据
     */
    @RequiresPermissions(value = {"billmain:billMain:add", "billmain:billMain:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(BillMain billMain, Model model, RedirectAttributes redirectAttributes, String type) throws Exception {
        if ("Unpost".equals(type)){
            try {
                String curperiod=wareHouseService.get(billMain.getWare().getId()).getCurrPeriod();
                logger.debug("gid:"+billMain.getWare());
                //判断核算期是否一致
                if(!curperiod.equals(billMain.getPeriod().getPeriodId())) {
                    addMessage(redirectAttributes, "反过账失败，当前核算期与仓库核算期不一致！");
                    return "redirect:"+Global.getAdminPath()+"/outsourcinginput/outsourcingInput/?type=" + type + "&repage";
                }
                InvAccountCode invAccountCode = new InvAccountCode();
                Map<String, Object> resultMap = invAccountService.post(billMain);
                if ((boolean) resultMap.get("result")) {
                    billMain.setBillFlag("N");
                    deleteRealQty(billMain);
                    billMainService.save(billMain);
                    addMessage(redirectAttributes, "单据反过账成功");
                    return "redirect:" + Global.getAdminPath() + "/outsourcinginput/outsourcingInput/?type=" + type + "&repage";
                } else {
                    addMessage(redirectAttributes, "单据反过账失败" + resultMap.get("msg"));
                    return "redirect:" + Global.getAdminPath() + "/outsourcinginput/outsourcingInput/?type=" + type + "&repage";
                }
            } catch (Exception e) {
                e.printStackTrace();
                addMessage(redirectAttributes, "单据反过账失败");
                return "redirect:" + Global.getAdminPath() + "/outsourcinginput/outsourcingInput/?type=" + type + "&repage";
            }
        }
        if ("Post".equals(type)) {
            List<String> itemList = new ArrayList<>();
            int itemSum = 0;
            for (int i = 0; i < billMain.getBillDetailList().size(); i++) {
                itemSum += billMain.getBillDetailList().get(i).getItemQty();
                itemList.add(billMain.getBillDetailList().get(i).getItem().getCode());
            }
            if (itemSum != billMain.getBillDetailCodeList().size()) {
                addMessage(redirectAttributes, "过账失败，请领数量与实际数量不一致！");
                return "redirect:" + Global.getAdminPath() + "/outsourcinginput/outsourcingInput/?type=" + type + "&repage";
            } else {
                for (int i = 0; i < billMain.getBillDetailCodeList().size(); i++) {
                    String itemCode = billMain.getBillDetailCodeList().get(i).getItemBarcode().substring(15, 27);
                    if (!itemList.contains(itemCode)) {
                        addMessage(redirectAttributes, "过账失败，物料信息不一致");
                        return "redirect:" + Global.getAdminPath() + "/outsourcinginput/outsourcingInput/?type=" + type + "&repage";
                    }
                }
            }
            String curperiod = wareHouseService.get(billMain.getWare().getId()).getCurrPeriod();
            logger.debug("gid:" + billMain.getWare());
            //判断核算期是否一致
            if (!curperiod.equals(billMain.getPeriod().getPeriodId())) {
                addMessage(redirectAttributes, "过账失败，当前核算期与仓库核算期不一致！");
                return "redirect:" + Global.getAdminPath() + "/outsourcinginput/outsourcingInput/?type=" + type + "&repage";
            }
            try {
                Map<String, Object> resultMap = invAccountService.post(billMain);
                Boolean judge = (Boolean) resultMap.get("result");

                if (judge) {
                    InvAccountCode invAccountCode = new InvAccountCode();
                    for (BillDetailCode billDetailCode : billMain.getBillDetailCodeList()){
                        invAccountCode.setItemBarcode(billDetailCode.getItemBarcode());
                        invAccountCode.setWare(billMain.getWare());
                        Item item = itemService.get(billDetailCode.getItemBarcode().substring(15,27));
                        invAccountCode.setItem(item);
                        invAccountCode.setLoc(billDetailCode.getLoc());
                        invAccountCode.setBin(billDetailCode.getBin());
                        //不知道什么原因报错先注释掉-黄志兵
//                        invAccountCodeService.save(invAccountCode);
                    }
                    billMain.setBillFlag("T");
                    addRealQty(billMain);
                    billMainService.save(billMain);
                    addMessage(redirectAttributes, "过账成功");
                } else {
                    addMessage(redirectAttributes, "过账失败: " + resultMap.get("msg"));
                }
                return "redirect:" + Global.getAdminPath() + "/outsourcinginput/outsourcingInput/?type=" + type + "&repage";
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                addMessage(redirectAttributes, "过账失败");
                return "redirect:" + Global.getAdminPath() + "/outsourcinginput/outsourcingInput/?type=" + type + "&repage";
            }

        }
        //新增或编辑表单保存
        User u = UserUtils.getUser();
        //设置出入库单据类型
//        increaseAccount(billMain);
        BillType billType = new BillType();
        billType.setIoType("WI01");
        List<BillType> billTypes = billTypeService.findList(billType);
        billMain.setIo(billTypes.get(0));
        //设置制单人
        billMain.setBillEmp(u);
        //设置接口序号
        billMain.setOrderCode(255);
        if (!beanValidator(model, billMain)) {
            return form(billMain, model, "Input");
        }
        if ("Input".equals(type)) {
            billMain.setBillFlag("N");
            billMainService.save(billMain);//保存
            for(BillDetailCode billDetailCode : billMain.getBillDetailCodeList()) {
                if(StringUtils.isNotBlank(billDetailCode.getItemBarcode())) {
                    Item item = itemService.get(billDetailCode.getItemBarcode().substring(15, 27));
                    billDetailCode.setItem(item);
                    if (StringUtils.isNotBlank(billDetailCode.getBin().getId())) {
                        Bin bin = binService.get(billDetailCode.getBin().getId());
                        billDetailCode.setBin(bin);
                    }
                    if (StringUtils.isNotBlank(billDetailCode.getLoc().getId())) {
                        Location location = locationService.get(billDetailCode.getLoc().getId());
                        billDetailCode.setLoc(location);
                    }
                    billMainService.saveBillCode(billMain, billDetailCode);
                }
            }
            addMessage(redirectAttributes, "保存单据成功");
        }
        return "redirect:" + Global.getAdminPath() + "/outsourcinginput/outsourcingInput/?type=" + type + "&repage";
    }

    /**
     * 采购到货列表数据
     */
    @ResponseBody
    @RequiresPermissions("billmain:billMain:list")
    @RequestMapping(value = {"dataBackList"})
    public Map<String, Object> dataBackAudit(InvCheckMain invCheckMain, HttpServletRequest request, HttpServletResponse response, Model model) {
        invCheckMain.setBillType("T");
        invCheckMain.setBillStateFlag("E");
        List<InvCheckMain> list=invCheckMainService.findListbyBillStateFlag(invCheckMain);
        invCheckMainService.ListToChinese(list);
        Page<InvCheckMain> page=new Page<InvCheckMain>(request, response);
        //分页
        String intPage= request.getParameter("pageNo");
        int pageNo=Integer.parseInt(intPage);
        int pageSize= page.getPageSize();
        page.setCount(list.size());
        if(pageNo==1&&pageSize==-1){
            page.setList(list);
        }else{
            List<InvCheckMain> reportL=  new ArrayList<InvCheckMain>();
            for(int i=(pageNo-1)*pageSize;i<list.size()&& i<pageNo*pageSize;i++){
                reportL.add(list.get(i));
            }
            page.setList(reportL);

        }
        return getBootstrapData(page);
    }
    /**
     * 保存单据
     */
    @RequiresPermissions(value = {"billmain:billMain:add", "billmain:billMain:edit"}, logical = Logical.OR)
    @RequestMapping(value = "BackSave")
    public String BackSave(BillMain billMain, Model model, RedirectAttributes redirectAttributes, String type,String Audit,String AuditComment) throws Exception {
        if("BInput".equals(type)){
            billMain.setBillDate(new Date());
            if(billMainService.findPeriodByTime(billMain.getBillDate())!=null) {
                billMain.setPeriod(billMainService.findPeriodByTime(billMain.getBillDate()));
            }
            BillType billType = new BillType();
            billType.setIoType("WT01");
            if(billTypeService.findList(billType).size()==1){
                billMain.setIo(billTypeService.findList(billType).get(0));
            }
            billMainService.save(billMain);
            addMessage(redirectAttributes, "保存单据成功");
        }else if("BPost".equals(type)){
            String curperiod = wareHouseService.get(billMain.getWare().getId()).getCurrPeriod();
            //判断核算期是否一致
            if (!curperiod.equals(billMain.getPeriod().getPeriodId())) {
                addMessage(redirectAttributes, "过账失败，当前核算期与仓库核算期不一致！");
                return "redirect:"+Global.getAdminPath()+"/outsourcinginput/outsourcingInput/Blist?type=" + type + "&repage";
            }
            try {
                List<BillDetail> detaillist = billMain.getBillDetailList();
                for (int i = 0; i < detaillist.size(); i++) {
                    detaillist.get(i).setItemQty(detaillist.get(i).getItemQty() * (-1));
                }
                billMain.setBillDetailList(detaillist);
                BillType db=billMain.getIo();
                BillType billType=new BillType();
                billType.setIoType("WI01");
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
                    billMain.setBillFlag("T");
                    deleteRealQty(billMain);
                    billMainService.save(billMain);
                    addMessage(redirectAttributes, "过账成功");
                } else {
                    addMessage(redirectAttributes, "过账失败: " + resultMap.get("msg"));
                }
                return "redirect:"+Global.getAdminPath()+"/outsourcinginput/outsourcingInput/Blist?type=" + type + "&repage";
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                addMessage(redirectAttributes, "过账失败");
                return "redirect:"+Global.getAdminPath()+"/outsourcinginput/outsourcingInput/Blist?type=" + type + "&repage";
            }
        }else{
            if(",true".equals(Audit)){
                billMain.setBillFlag("N");
                billMainService.save(billMain);
                addMessage(redirectAttributes,"审核通过");
            }else if("false".equals(Audit)){
//                auditCache.put("Audit",AuditComment);
                billMain.setBillFlag("V");
                billMainService.save(billMain);
                addMessage(redirectAttributes,"审核不通过");
            }
        }
        return "redirect:"+Global.getAdminPath()+"/outsourcinginput/outsourcingInput/Blist?type=" + type + "&repage";
   }

    /**
     * 删除单据
     */
    @ResponseBody
    @RequiresPermissions("billmain:billMain:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(BillMain billMain, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        billMainService.delete(billMain);
        j.setMsg("删除单据成功");
        return j;
    }

    /**
     * 批量删除单据
     */
    @ResponseBody
    @RequiresPermissions("billmain:billMain:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            billMainService.delete(billMainService.get(id));
        }
        j.setMsg("删除单据成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("billmain:billMain:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BillMain billMain, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "单据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BillMain> page = billMainService.findPage(new Page<BillMain>(request, response, -1), billMain);
            new ExportExcel("单据", BillMain.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出单据记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "detail")
    public BillMain detail(String id) {
        return billMainService.get(id);
    }


    /**
     * 导入Excel数据
     */
    @RequiresPermissions("billmain:billMain:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BillMain> list = ei.getDataList(BillMain.class);
            for (BillMain billMain : list) {
                try {
                    billMainService.save(billMain);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条单据记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条单据记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入单据失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/billmain/billMain/?repage";
    }

    /**
     * 下载导入单据数据模板
     */
    @RequiresPermissions("billmain:billMain:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "单据数据导入模板.xlsx";
            List<BillMain> list = Lists.newArrayList();
            new ExportExcel("单据数据", BillMain.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/billmain/billMain/?repage";
    }

    /**
     * 增加库存帐的可用量
     */
    public boolean addRealQty(BillMain billMain) {
        InvAccount invAccount = new InvAccount();
        InvAccount invAccount1;
        double realQty;
        for (BillDetail billDetail : billMain.getBillDetailList()) {
            invAccount.setWare(billMain.getWare());
            invAccount.setItem(billDetail.getItem());
            invAccount.setYear(billMain.getPeriod().getPeriodId().substring(0, 4));
            invAccount.setPeriod(billMain.getPeriod().getPeriodId().substring(4, 6));
            invAccount1 = invAccountService.getByInvAccount(invAccount);

            if (invAccount1.getRealQty() == null || invAccount1.getRealQty() == 0) {
                realQty = billDetail.getItemQty();
            } else {
                realQty = invAccount1.getRealQty() + billDetail.getItemQty();
            }
            invAccount1.setRealQty(realQty);
            invAccountService.save(invAccount1);
        }
        return true;
    }
    //反过账减少库存量
    public boolean deleteRealQty(BillMain billMain) {
        InvAccount invAccount = new InvAccount();
        InvAccount invAccount1;
        double realQty;
        for (BillDetail billDetail : billMain.getBillDetailList()) {
            invAccount.setWare(billMain.getWare());
            invAccount.setItem(billDetail.getItem());
            invAccount.setYear(billMain.getPeriod().getPeriodId().substring(0, 4));
            invAccount.setPeriod(billMain.getPeriod().getPeriodId().substring(4, 6));
            invAccount1 = invAccountService.getByInvAccount(invAccount);
            if (invAccount1.getRealQty() == null || invAccount1.getRealQty() == 0) {
                realQty = billDetail.getItemQty();
            } else {
                realQty = invAccount1.getRealQty() - billDetail.getItemQty();
            }
        invAccount1.setRealQty(realQty);
        invAccountService.save(invAccount1);
    }
    return true;
    }
    /**
     * 采购合同管理列表页面
     */
    @RequiresPermissions("billmain:billMain:list")
    @RequestMapping(value = "printList")
    public String printList() {
        return "inventorymanage/outsourcinginput/outsourcingInputPrintList";
    }

    /**
     * 打印验收入库单的数据data
     */
    @ResponseBody
    @RequiresPermissions("billmain:billMain:list")
    @RequestMapping(value = "printData")
    public Map<String,Object> printData(BillMain billMain,HttpServletRequest request, HttpServletResponse response){
        List<BillMain> billMainList = billMainMapper.findAllList(billMain);
        List<BillMain> billMainListBychk =new ArrayList<>();
        Page<BillMain> page = new Page<>();
        for(int i=0;i<billMainList.size();i++) {
            billMain = billMainList.get(i);
            int k=0;
            if(StringUtils.isNotBlank(billMain.getCorBillNum())) {
                if ("chk".equals(billMain.getCorBillNum().substring(0, 3))) {
                    billMain.setBillDetailList(billDetailMapper.findList(new BillDetail(billMain)));
                    billMainListBychk.add(k,billMain);
                    k++;
                }
            }
        }
        page.setList(billMainListBychk);
        return getBootstrapData(page);
    }

    /**
     * 打印验收入库单的
     */

    @RequestMapping(value = "printForm")
    public String form(BillMain billMain, Model model) {
        if(StringUtils.isNotBlank(billMain.getCorBillNum())){
            InvCheckMain invCheckMain = new InvCheckMain();
            invCheckMain.setBillnum(billMain.getCorBillNum());
            List<InvCheckMain> invCheckMainList = invCheckMainService.findList(invCheckMain);
            if(invCheckMainList.size() != 0) {
                //供应商
                billMain.setCorName(invCheckMainList.get(0).getSupName());
                //采购单号
                Relations relations = new Relations();
                relations.setAfterNum(invCheckMainList.get(0).getBillnum());
                List<Relations> relationsList = relationsService.findList(relations);
                if(relationsList.size() != 0){
                    relations.setFrontNum(relationsList.get(0).getFrontNum());
                    model.addAttribute("relations",relations);
                }
                //验收时间
                invCheckMain.setMakeDate(invCheckMainList.get(0).getMakeDate());
                model.addAttribute("invCheckMain",invCheckMain);
                //将invcheckdetail的checkQty存到billdetail里面的itemqtytemp传到前端
                invCheckMain.setInvCheckDetailList(invCheckDetailMapper.findList(new InvCheckDetail(invCheckMain)));
                List<BillDetail> billDetailList = billMain.getBillDetailList();
                List<InvCheckDetail> invCheckDetailList = invCheckMain.getInvCheckDetailList();
                for(int h=0;h<billDetailList.size();h++){
                    billDetailList.get(h).setItemQtyTemp(invCheckDetailList.get(h).getCheckQty());
                }
                billMain.setBillDetailList(billDetailList);
            }
        }

        model.addAttribute("billMain", billMain);
        return "inventorymanage/outsourcinginput/outsourcingInputPrintForm";
    }



}
