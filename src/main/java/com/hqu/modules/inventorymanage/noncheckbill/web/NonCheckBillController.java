package com.hqu.modules.inventorymanage.noncheckbill.web;

import com.alibaba.fastjson.JSON;
import com.hqu.modules.basedata.period.service.PeriodService;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billmain.service.BillMainService;
import com.hqu.modules.inventorymanage.billtype.entity.BillType;
import com.hqu.modules.inventorymanage.billtype.service.BillTypeService;
import com.hqu.modules.inventorymanage.employee.service.EmployeeService;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.Logical;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/noncheckbill/nonCheckBill")
public class NonCheckBillController extends BaseController {

    @Autowired
    private BillMainService billMainService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private BillTypeService billTypeService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private PeriodService periodService;
    @Autowired
    private InvAccountService invAccountService;


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

    /**
     * 单据列表页面
     */
    @RequiresPermissions("billmain:billMain:list")
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        return "inventorymanage/noncheckbill/nonCheckBillList";
    }


    /**
     * 单据列表数据
     */
    @ResponseBody
    @RequiresPermissions("billmain:billMain:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BillMain billMain, HttpServletRequest request, HttpServletResponse response, Model model ) {
        billMain.setBillFlag("N");
        Page<BillMain> page = billMainService.findPage(new Page<BillMain>(request, response), billMain);
        logger.debug("page:"+ JSON.toJSONString(page));
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑单据表单页面
     */
//    @RequiresPermissions(value={"billmain:billMain:view","billmain:billMain:add","billmain:billMain:edit"},logical= Logical.OR)
//    @RequestMapping(value = "form")
//    public String form(BillMain billMain, Model model,String type) {
//
//        if("post".equals(type)){
//            billMain.setBillFlag("T");
//        }
//        if(StringUtils.isBlank(billMain.getId())){//如果ID是空为添加
//            User u = UserUtils.getUser();
//            Office o = officeService.get(u.getOffice().getId());
//            Date date = new Date();
//            billMain.setBillDate(date);
//            billMain.setDept(o);
//            billMain.setDeptName(o.getName());
//            billMain.setBillFlag("A");
//            billMain.setBillPerson(u);
//            billMain.setPeriod(billMainService.findPeriodByTime(date));
//            billMain.setWareEmp(employeeService.get("9744f7c5715f413ab61e2be5181eff1d"));
//            SimpleDateFormat sdf =   new SimpleDateFormat( "yyyyMMdd" );
//            String newBillNum = billMainService.getMaxIdByTypeAndDate("PYI"+sdf.format(date));
//            if(StringUtils.isEmpty(newBillNum)){
//                newBillNum = "0000";
//            } else {
//                newBillNum = String.format("%04d", Integer.parseInt(newBillNum.substring(11,15))+1);
//            }
//            logger.debug("billNum:"+newBillNum);
//            billMain.setBillNum("PYI" + sdf.format(date) + newBillNum);
//            model.addAttribute("isAdd", true);
//        } else {
//            billMain.setWareEmp(employeeService.get(billMain.getWareEmp().getId()));
//        }
//        logger.debug(JSON.toJSONString(billMain));
//        model.addAttribute("billMain", billMain);
//        model.addAttribute("type",type);
//        return "inventorymanage/inventoryprofit/inventoryProfitForm";
//    }

    /**
     * 保存单据
     */
    @RequiresPermissions(value={"billmain:billMain:add","billmain:billMain:edit"},logical=Logical.OR)
    @RequestMapping(value = "save")
    public String save(BillMain billMain, Model model, RedirectAttributes redirectAttributes, String type) throws Exception{
        if("post".equals(type)){
            Map<String,Object> resultMap = invAccountService.post(billMain);
            if((boolean)resultMap.get("result")){
                billMain.setBillFlag("T");
                billMainService.save(billMain);//保存
                addMessage(redirectAttributes, "单据过账成功");
                return "redirect:"+ Global.getAdminPath()+"/inventoryprofit/inventoryProfit/?type="+type+"&repage";
            } else {

                addMessage(redirectAttributes, "单据过账失败:"+resultMap.get("msg"));
                return "redirect:"+Global.getAdminPath()+"/inventoryprofit/inventoryProfit/?type="+type+"&repage";
            }
        }
        User u = UserUtils.getUser();
        BillType billType = new BillType();
        billType.setIoType("PI01");
        billMain.setIo(billTypeService.get("5a1048f566f74d20a5582f908fbb6785"));
        billMain.setOrderCode(290);
        if("input".equals(type)) {
            billMain.setBillFlag("N");
        }
        billMain.setBillEmp(u);
        logger.debug(JSON.toJSONString(billMain));

        //新增或编辑表单保存
        billMainService.save(billMain);//保存
        addMessage(redirectAttributes, "保存单据成功");
        return "redirect:"+Global.getAdminPath()+"/inventoryprofit/inventoryProfit/?type="+type+"&repage";
    }

    @ResponseBody
    @RequiresPermissions("billmain:billMain:edit")
    @RequestMapping(value = "update")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        BillMain b = null;
        for (String id : idArray) {
            try {
                b = billMainService.get(id);
                b.setPeriod(billMainService.findPeriodByTime(new Date()));

                billMainService.save(b);
            } catch (Exception e) {
                e.printStackTrace();
                j.setMsg("更新" + b.getBillNum() + "失败");
                return j;
            }
        }
        j.setMsg("更新成功");
        return j;
    }
}
