/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.personwage.web;

import com.google.common.collect.Lists;
import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.basedata.period.mapper.PeriodMapper;
import com.hqu.modules.costmanage.billingrule.entity.CosBillRule;
import com.hqu.modules.costmanage.billingrule.service.CosBillRuleService;
import com.hqu.modules.costmanage.cositem.entity.CosItem;
import com.hqu.modules.costmanage.cositem.service.CosItemService;
import com.hqu.modules.costmanage.cosprodobject.entity.CosProdObject;
import com.hqu.modules.costmanage.cosprodobject.service.CosProdObjectService;
import com.hqu.modules.costmanage.personwage.entity.PersonWage;
import com.hqu.modules.costmanage.personwage.service.PersonWageService;
import com.hqu.modules.costmanage.realaccount.entity.RealAccount;
import com.hqu.modules.costmanage.realaccount.entity.RealAccountDetail;
import com.hqu.modules.costmanage.realaccount.service.RealAccountService;
import com.hqu.modules.costmanage.settleaccount.entity.SettleAccount;
import com.hqu.modules.inventorymanage.billmain.service.BillMainService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.*;

/**
 * 人工工资Controller
 * @author 黄志兵
 * @version 2018-09-01
 */
@Controller
@RequestMapping(value = "${adminPath}/personwagetwo/personWageTwo")
public class PersonWageTwoController extends BaseController {

	@Autowired
	private PersonWageService personWageService;
	@Autowired
	private BillMainService billMainService;
	@Autowired
	private CosBillRuleService cosBillRuleService;
	@Autowired
	private CosItemService cosItemService;
	@Autowired
	private RealAccountService realAccountService;
	@Autowired
	private CosProdObjectService cosProdObjectService;
	@Autowired
	private PeriodMapper periodMapper;
	
	@ModelAttribute
	public PersonWage get(@RequestParam(required=false) String id) {
		PersonWage entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = personWageService.get(id);
		}
		if (entity == null){
			entity = new PersonWage();
		}
		return entity;
	}
	
	/**
	 * 人工工资录入等列表页面
	 */
	@RequiresPermissions("personwage:personWage:list")
	@RequestMapping(value = {"list", ""})
	public String list(String flag,Model model) {
		model.addAttribute("flag",flag);
		return "costmanage/personwage/personWageList";
	}
	
		/**
	 * 人工工资录入等列表数据
	 */
	@ResponseBody
	@RequiresPermissions("personwage:personWage:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(String flag,PersonWage personWage, HttpServletRequest request, HttpServletResponse response, Model model) {
		if("".equals(flag)){
			personWage.setBillStatus("S");
		}
		if("jihe".equals(flag)){
			personWage.setBillStatus("A");
		}
		if("cexiao".equals(flag) || "zhidan".equals(flag)){
			personWage.setBillStatus("B");
		}
		Page<PersonWage> page = personWageService.findPage(new Page<PersonWage>(request, response), personWage);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑人工工资录入等表单页面
	 */
	@RequiresPermissions(value={"personwage:personWage:view","personwage:personWage:add","personwage:personWage:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(String flag,PersonWage personWage, Model model) {
		model.addAttribute("flag",flag);
		model.addAttribute("personWage", personWage);
		if(StringUtils.isBlank(personWage.getId())){//如果ID是空为添加
			try {
				personWage.setPeriodId(billMainService.findPeriodByTime(new Date()).getPeriodId());
			}catch (Exception e){
				e.printStackTrace();
			}
			model.addAttribute("isAdd", true);
		}
		if("zhidan".equals(flag)){
		    return "costmanage/personwage/personCostProofForm";
        }
		return "costmanage/personwage/personWageForm";
	}

	/**
	 * 保存人工工资录入
	 */
	@RequiresPermissions(value={"personwage:personWage:add","personwage:personWage:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(String flag,String saveBtn,String submitBtn,PersonWage personWage, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, personWage)){
			return form(flag,personWage, model);
		}
		Date date = new Date();
        User user = UserUtils.getUser();
		//新增或编辑表单保存
		if("true".equals(saveBtn)){
			personWage.setBillStatus("S");
			personWage.setBillMode("B");
			personWage.setMakeDate(date);
			personWage.setMakeId(user.getNo());
			personWage.setMakeName(user.getName());
			personWageService.save(personWage);//保存
			addMessage(redirectAttributes, "保存人工工资单据成功");
		}
		else if("true".equals(submitBtn)){
			personWage.setBillStatus("A");
			personWageService.save(personWage);
			addMessage(redirectAttributes,"提交人工工资单据成功");
		}else {
			if ("jihe".equals(flag)) {
				personWage.setBillStatus("B");
				personWage.setCheckDate(date);
				personWage.setCheckId(user.getNo());
				personWage.setCheckName(user.getName());
				personWageService.save(personWage);
				addMessage(redirectAttributes, "稽核人工工资单据成功");
			} else if ("cexiao".equals(flag)) {
                personWage.setBillStatus("S");
                personWageService.save(personWage);
				addMessage(redirectAttributes ,"撤销人工工资单据成功");
			}
		}

		return "redirect:"+Global.getAdminPath()+"/personwage/personWage/?repage&flag="+flag;
	}

    /**
     * 保存人工成本制单
     */



    /*
     * 生成单据
     */
    public RealAccount getRealAccount(PersonWage personWage){
		RealAccount realAccount = new RealAccount();
		String billNum = realAccountService.getRGCBBillNum();
		if(billNum == null){
			billNum = "RG" + DateUtils.getDate().replace("-", "") + "00001";
		}
		realAccount.setBillNum(billNum);
		//获得原始单据信息
//		realAccount.setOriBillId(personWage.getPeriodId()+personWage.getPersonCode().getNo()+personWage.getItemCode().getItem().getCode());
		realAccount.setOriBillDate(personWage.getMakeDate());
		//获得经办人
		User user = UserUtils.getUser();
		realAccount.setMakeId(user.getNo());
		realAccount.setMakeName(user.getName());
		//获得制单时间
		Date date = new Date();
		realAccount.setMakeDate(date);
		//获得会计时间
		realAccount.setPeriodId(personWage.getPeriodId());
		realAccount.setCosBillNumStatus("S");
		realAccount.setBillMode("B");
		realAccount.setBillType("人工");
		return realAccount;
	}


	/**
	 * 删除人工工资录入等
	 */
	@ResponseBody
	@RequiresPermissions("personwage:personWage:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PersonWage personWage, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		personWageService.delete(personWage);
		j.setMsg("删除人工工资录入等成功");
		return j;
	}
	
	/**
	 * 批量删除人工工资录入等
	 */
	@ResponseBody
	@RequiresPermissions("personwage:personWage:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			personWageService.delete(personWageService.get(id));
		}
		j.setMsg("删除人工工资录入等成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("personwage:personWage:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PersonWage personWage, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "人工工资录入等"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PersonWage> page = personWageService.findPage(new Page<PersonWage>(request, response, -1), personWage);
    		new ExportExcel("人工工资录入等", PersonWage.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出人工工资录入等记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("personwage:personWage:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PersonWage> list = ei.getDataList(PersonWage.class);
			for (PersonWage personWage : list){
				try{
					personWageService.save(personWage);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条人工工资录入等记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条人工工资录入等记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入人工工资录入等失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/personwage/personWage/?repage";
    }
	
	/**
	 * 下载导入人工工资录入等数据模板
	 */
	@RequiresPermissions("personwage:personWage:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "人工工资录入等数据导入模板.xlsx";
    		List<PersonWage> list = Lists.newArrayList(); 
    		new ExportExcel("人工工资录入等数据", PersonWage.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/personwage/personWage/?repage";
    }

}