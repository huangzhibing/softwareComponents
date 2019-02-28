/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.cospersonother.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.basedata.period.mapper.PeriodMapper;
import com.hqu.modules.costmanage.personwage.entity.PersonWage;
import com.hqu.modules.costmanage.personwage.mapper.PersonWageMapper;
import com.hqu.modules.costmanage.personwage.service.PersonWageService;
import com.hqu.modules.inventorymanage.billmain.service.BillMainService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.hqu.modules.costmanage.cospersonother.entity.CosPersonOther;
import com.hqu.modules.costmanage.cospersonother.mapper.CosPersonOtherMapper;
import com.hqu.modules.costmanage.cospersonother.service.CosPersonOtherService;

/**
 * 其它工资单据稽核Controller
 * @author hzm
 * @version 2018-09-01
 */
@Controller
@RequestMapping(value = "${adminPath}/cospersonother/cosPersonOther")
public class CosPersonOtherController extends BaseController {

    @Autowired
    private PersonWageService personWageService;
	@Autowired
	private CosPersonOtherService cosPersonOtherService;
	@Autowired
	private BillMainService billMainService;
	@Autowired
	private PeriodMapper periodMapper;
	@Autowired
	private PersonWageMapper personWageMapper;
	@Autowired
	private CosPersonOtherMapper cosPersonOtherMapper;

	@ModelAttribute
	public CosPersonOther get(@RequestParam(required=false) String id) {
		CosPersonOther entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = cosPersonOtherService.get(id);
		}
		if (entity == null){
			entity = new CosPersonOther();
		}
		return entity;
	}
	
	/**
	 * 其它工资单据稽核列表页面
	 */
	@RequiresPermissions("cospersonother:cosPersonOther:list")
	@RequestMapping(value = {"list", ""})
	public String list(String flag,Model model) {
		if(flag!=null&&flag.equals("fenpei")){
			String periodId = periodMapper.getPeriodByClose();
			if(periodId == null || periodId == ""){
				model.addAttribute("nowPeriod",billMainService.findPeriodByTime(new Date()).getPeriodId());
			}else{
				String nextPeriodId = getNextPeriod(periodId);
				model.addAttribute("nowPeriod",nextPeriodId);
			}
			
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			model.addAttribute("nowDate",sdf.format(new Date()));
			
			return "costmanage/cospersonother/distributionForm";
		}
		model.addAttribute("flag",flag);
		return "costmanage/cospersonother/cosPersonOtherList";
	}

	/**
	 * 其他工资分配计算
	 */
	@RequestMapping(value = "distribution")
	public String distribution(String nowPeriod,RedirectAttributes redirectAttributes) {
	    
	    PersonWage personWage=new PersonWage();
	    personWage.setPeriodId(nowPeriod);
//	    personWage.setBillStatus("B");
        List<PersonWage> personWages=personWageService.findList(personWage);
        
        for(PersonWage p:personWages){
        	double numberSum=0;//产品总量
            double otherSum=0;//工资总和
            double wageUnit=0;//单位分配工资
            
        	//计算当前核算期内人工工资的单产品总量
        	PersonWage wage=new PersonWage();
        	wage.setPeriodId(p.getPeriodId());
        	wage.setTeam(p.getTeam());
//        	wage.setBillStatus("B");
        	if(personWageMapper.getQty(wage) != null)
        		numberSum = personWageMapper.getQty(wage);
        	
        	//计算当前核算期内其他工资单的工资总和
            CosPersonOther cosPersonOther=new CosPersonOther();
            cosPersonOther.setPeriodID(p.getPeriodId());
            cosPersonOther.setBillStatus("B");
            cosPersonOther.setPersonCode(p.getTeam());
            if(cosPersonOtherMapper.getSum(cosPersonOther) != null){
            	otherSum = cosPersonOtherMapper.getSum(cosPersonOther);
            	
            	//分配工资
                if(numberSum != 0){
                	wageUnit = otherSum/numberSum;
                }
                p.setWageUnit(Double.toString(wageUnit));
                personWageService.save(p);
                //更新cos_account
                double itemSum=0;
                itemSum+=((wageUnit+p.getItemUnit())*p.getItemQty());
				cosPersonOtherService.updataReal(Double.toString(itemSum),p.getCosBillNum());
            }
        }
        
        for(PersonWage p:personWages){
        	CosPersonOther cosPersonOther=new CosPersonOther();
            cosPersonOther.setPeriodID(p.getPeriodId());
            cosPersonOther.setBillStatus("B");
            cosPersonOther.setPersonCode(p.getTeam());
        	//修改单据状态
            List<CosPersonOther> cosPersonOthers=cosPersonOtherService.findList(cosPersonOther);
    		for (CosPersonOther c:cosPersonOthers){
            	c.setBillStatus("C");
            	cosPersonOtherService.save(c);
    		}
        }
        
        
		addMessage(redirectAttributes,"分配成功！");
		return "redirect:"+Global.getAdminPath()+"/cospersonother/cosPersonOther/?repage&flag=fenpei";
	}

	/**
	 * 其它工资单据稽核列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cospersonother:cosPersonOther:list")
	@RequestMapping(value = "data")
	public Map<String, Object> distribution(String flag,CosPersonOther cosPersonOther, HttpServletRequest request, HttpServletResponse response, Model model) {
		switch (flag){
			case "jihe":
				cosPersonOther.setBillStatus("A");
				break;
			case "chexiao":
				cosPersonOther.setBillStatus("B");
				break;
			case "chaxun":
				break;
			default:
				cosPersonOther.setBillStatus("S");
				break;
		}
		Page<CosPersonOther> page = cosPersonOtherService.findPage(new Page<CosPersonOther>(request, response), cosPersonOther);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑其它工资单据稽核表单页面
	 */
	@RequiresPermissions(value={"cospersonother:cosPersonOther:view","cospersonother:cosPersonOther:add","cospersonother:cosPersonOther:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CosPersonOther cosPersonOther, Model model,String flag) {
		model.addAttribute("flag",flag);
		model.addAttribute("cosPersonOther", cosPersonOther);
		if(StringUtils.isBlank(cosPersonOther.getId())){//如果ID是空为添加
			try {
				cosPersonOther.setPeriodID(billMainService.findPeriodByTime(new Date()).getPeriodId());
			}catch (Exception e){
				e.printStackTrace();
			}
			model.addAttribute("isAdd", true);
		}
		return "costmanage/cospersonother/cosPersonOtherForm";
	}

	/**
	 * 保存其它工资单据稽核
	 */
	@RequiresPermissions(value={"cospersonother:cosPersonOther:add","cospersonother:cosPersonOther:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(CosPersonOther cosPersonOther, Model model, RedirectAttributes redirectAttributes,String flag) throws Exception{
		if (!beanValidator(model, cosPersonOther)){
			return form(cosPersonOther, model,flag);
		}
		User user = UserUtils.getUser();
		switch (flag){
			case "save":
				cosPersonOther.setBillMode("A");
				cosPersonOther.setMakeDate(DateUtils.getDateTime());
				cosPersonOther.setMakeId(user.getNo());
				cosPersonOther.setMakeName(user.getName());
				cosPersonOther.setBillStatus("S");
				flag="";
				break;
			case "jihe":
				cosPersonOther.setCheckDate(DateUtils.getDateTime());
				cosPersonOther.setCheckId(user.getNo());
				cosPersonOther.setCheckName(user.getName());
				cosPersonOther.setBillStatus("B");
				break;
			case "chexiao":
				cosPersonOther.setBillStatus("S");
				break;
			default:
				cosPersonOther.setBillStatus("A");
		}
		//新增或编辑表单保存
		cosPersonOtherService.save(cosPersonOther);//保存
		addMessage(redirectAttributes, "保存其它工资单据成功");
		return "redirect:"+Global.getAdminPath()+"/cospersonother/cosPersonOther/?repage&flag="+flag;
	}
	
	/**
	 * 删除其它工资单据稽核
	 */
	@ResponseBody
	@RequiresPermissions("cospersonother:cosPersonOther:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CosPersonOther cosPersonOther, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		cosPersonOtherService.delete(cosPersonOther);
		j.setMsg("删除其它工资单据稽核成功");
		return j;
	}
	
	/**
	 * 批量删除其它工资单据稽核
	 */
	@ResponseBody
	@RequiresPermissions("cospersonother:cosPersonOther:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			cosPersonOtherService.delete(cosPersonOtherService.get(id));
		}
		j.setMsg("删除其它工资单据稽核成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cospersonother:cosPersonOther:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CosPersonOther cosPersonOther, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "其它工资单据稽核"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CosPersonOther> page = cosPersonOtherService.findPage(new Page<CosPersonOther>(request, response, -1), cosPersonOther);
    		new ExportExcel("其它工资单据稽核", CosPersonOther.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出其它工资单据稽核记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cospersonother:cosPersonOther:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CosPersonOther> list = ei.getDataList(CosPersonOther.class);
			for (CosPersonOther cosPersonOther : list){
				try{
					cosPersonOtherService.save(cosPersonOther);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条其它工资单据稽核记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条其它工资单据稽核记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入其它工资单据稽核失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cospersonother/cosPersonOther/?repage";
    }
	
	/**
	 * 下载导入其它工资单据稽核数据模板
	 */
	@RequiresPermissions("cospersonother:cosPersonOther:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "其它工资单据稽核数据导入模板.xlsx";
    		List<CosPersonOther> list = Lists.newArrayList(); 
    		new ExportExcel("其它工资单据稽核数据", CosPersonOther.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cospersonother/cosPersonOther/?repage";
    }
	
	public String getNextPeriod(String periodId)
	{
		String resualt = null;
		int year, month;
		if (periodId.length() != 6) {
            return null;
        }
		year = Integer.parseInt(periodId.substring(0, 4));
		month = Integer.parseInt(periodId.substring(4, 6));
        if (month == 12) {
        	month = 1;
            year++;
        } else {
        	month++;
        }

        resualt =
                String.valueOf(year)
                + String.valueOf(100 + month).substring(1, 3);
        return (resualt == null ? "" : resualt).trim();
		
	}

}