/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.peopleRealAccount.web;

import com.google.common.collect.Lists;
import com.hqu.modules.costmanage.personwage.entity.PersonWage;
import com.hqu.modules.costmanage.personwage.mapper.PersonWageMapper;
import com.hqu.modules.costmanage.realaccount.entity.RealAccount;
import com.hqu.modules.costmanage.realaccount.entity.RealAccountDetail;
import com.hqu.modules.costmanage.realaccount.service.RealAccountService;
import com.hqu.modules.inventorymanage.billmain.service.BillMainService;
import com.hqu.modules.workshopmanage.processroutine.entity.ProcessRoutine;
import com.hqu.modules.workshopmanage.processroutine.mapper.ProcessRoutineMapper;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 人工凭证单据管理Controller
 * @author hzb
 * @version 2018-09-05
 */
@Controller
@RequestMapping(value = "${adminPath}/peopleRealaccount/realAccount")
public class PeopleRealAccountController extends BaseController {
	@Autowired
	private RealAccountService realAccountService;
	@Autowired
	BillMainService billMainService;
	@Autowired
	private PersonWageMapper personWageMapper;
	@Autowired
	private ProcessRoutineMapper processRoutineMapper;
	
	@ModelAttribute
	public RealAccount get(@RequestParam(required=false) String id) {
		RealAccount entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = realAccountService.get(id);
		}
		if (entity == null){
			entity = new RealAccount();
		}
		return entity;
	}
	
	/**
	 * 材料凭证单据管理列表页面
	 */
	@RequiresPermissions("peoplerealaccount:realAccount:list")
	@RequestMapping(value = {"list", ""})
	public String list(String flag,Model model) {
		model.addAttribute("flag",flag);
		return "costmanage/peoplerealaccount/realAccountList";
	}

	/**
	 *  加工路线单明细页面
	 */
	@RequestMapping(value = "jiagong")
	public String jiagongDetail(String billNum,Model model){
		ProcessRoutine processRoutine = new ProcessRoutine();
		processRoutine.setCosBillNum(billNum);
		List<ProcessRoutine> processRoutines = processRoutineMapper.findList(processRoutine);
		model.addAttribute("processRoutineDetail",processRoutines.get(0));

		return "costmanage/peoplerealaccount/processRoutineDetailForm";
	}
		/**
	 * 材料凭证单据管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("peoplerealaccount:realAccount:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(String flag,RealAccount realAccount, HttpServletRequest request, HttpServletResponse response, Model model) {
		realAccount.setBillType("人工");//人工成本
		if("zhidan".equals(flag)){
			realAccount.setCosBillNumStatus("S");
			realAccount.setBillMode("A");
		}
		if("edit".equals(flag)){
			realAccount.setCosBillNumStatus("S");
			realAccount.setBillMode("B");
		}
		if("check".equals(flag)){
			realAccount.setCosBillNumStatus("A");
		}
		Page<RealAccount> page = realAccountService.findPage(new Page<RealAccount>(request, response), realAccount);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑材料凭证单据管理表单页面
	 */
//	@RequiresPermissions(value={"peoplerealaccount:realAccount:view","peoplerealaccount:realAccount:add","peoplerealaccount:realAccount:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(String flag,RealAccount realAccount, Model model) {

		//获得经办人
		User user = UserUtils.getUser();
		realAccount.setMakeId(user.getNo());
		realAccount.setMakeName(user.getName());
		//获得制单时间
		Date date = new Date();
		realAccount.setMakeDate(date);
		//获得会计时间
		try {
			realAccount.setPeriodId(billMainService.findPeriodByTime(new Date()).getPeriodId());
		}catch (Exception e){
			e.printStackTrace();
		}
		if(StringUtils.isEmpty(realAccount.getBillNum())){
			String billNum = realAccountService.getRGCBBillNum();
			if(billNum == null){
				billNum = "RG" + DateUtils.getDate().replace("-", "") + "00001";
			}
			realAccount.setBillNum(billNum);
		}
		model.addAttribute("flag",flag);
		model.addAttribute("realAccount", realAccount);
		if(StringUtils.isBlank(realAccount.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		if("edit".equals(flag)){
			return "costmanage/peoplerealaccount/realAccountEditForm";
		}
		else if("check".equals(flag)){
			return "costmanage/peoplerealaccount/realAccountCheckForm";
		}
		else if("view".equals(flag)){
			return "costmanage/peoplerealaccount/realAccountSearchForm";
		}else {
			return "costmanage/peoplerealaccount/realAccountForm";
		}
	}

	/**
	 * 保存材料凭证单据管理
	 */
//	@RequiresPermissions(value={"peoplerealaccount:realAccount:add","peoplerealaccount:realAccount:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(String saveBtn,String submitBtn,String flag,RealAccount realAccount, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, realAccount)){
			return form(flag,realAccount, model);
		}
		if("true".equals(saveBtn)){
			realAccount.setBillMode("A");
			realAccount.setBillType("人工");
			realAccount.setCosBillNumStatus("S");
			realAccountService.save(realAccount);//保存
			addMessage(redirectAttributes, "保存人工成本凭证成功");
		}
		else if("true".equals(submitBtn)){
			realAccount.setCosBillNumStatus("A");
			realAccountService.save(realAccount);//提交
			addMessage(redirectAttributes, "提交人工成本凭证成功");
		}else if("true,true".equals(saveBtn)){
			realAccount.setCosBillNumStatus("S");
			realAccountService.save(realAccount);//保存
			addMessage(redirectAttributes, "保存人工成本凭证成功");
		}else if("true,true".equals(submitBtn)){
			realAccount.setCosBillNumStatus("A");
			realAccountService.save(realAccount);//提交
			addMessage(redirectAttributes, "提交人工成本凭证成功");
		}else{
			if("check".equals(flag)){
				Date date = new Date();
				User user = UserUtils.getUser();
				realAccount.setCheckId(user.getNo());
				realAccount.setCheckName(user.getName());
				realAccount.setCheckDate(date);
				realAccount.setCosBillNumStatus("B");
				realAccountService.save(realAccount);//稽核
				addMessage(redirectAttributes, "稽核人工成本凭证成功");
			}
		}

		return "redirect:"+Global.getAdminPath()+"/peopleRealaccount/realAccount/?flag="+flag;
	}
	
	/**
	 * 删除材料凭证单据管理
	 */
	@ResponseBody
	@RequiresPermissions("peoplerealaccount:realAccount:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(RealAccount realAccount, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		realAccountService.delete(realAccount);
		j.setMsg("删除人工成本凭证单据成功");
		return j;
	}
	
	/**
	 * 批量删除材料凭证单据管理
	 */
	@ResponseBody
	@RequiresPermissions("peoplerealaccount:realAccount:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			RealAccount realAccount = realAccountService.get(id);
			if(realAccount != null){
				realAccountService.delete(realAccount);
			}
		}
		j.setMsg("删除人工成本单据成功");
		return j;
	}
	
	/**
	 * 删除材料凭证单据管理
	 */
	@ResponseBody
	@RequiresPermissions("peoplerealaccount:realAccount:del")
	@RequestMapping(value = "deleteBack")
	public AjaxJson deleteBack(RealAccount realAccount, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		PersonWage personWage = personWageMapper.getBack(realAccount.getBillNum());
		personWage.setCosBillNum(null);
		personWage.setBillStatus("B");
		personWageMapper.update(personWage);
		realAccountService.delete(realAccount);
		j.setMsg("删除材料凭证单据管理成功");
		return j;
	}
	
	/**
	 * 批量删除材料凭证单据管理
	 */
	@ResponseBody
	@RequiresPermissions("peoplerealaccount:realAccount:del")
	@RequestMapping(value = "deleteAllBack")
	public AjaxJson deleteAllBack(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			RealAccount realAccount = realAccountService.get(id);
			PersonWage personWage = personWageMapper.getBack(realAccount.getBillNum());
			personWage.setCosBillNum(null);
			personWage.setBillStatus("B");
			personWageMapper.update(personWage);
			realAccountService.delete(realAccount);
			realAccountService.delete(realAccount);
		}
		j.setMsg("删除材料凭证单据管理成功");
		return j;
	}
	/**
	 * 批量删除材料凭证单据管理
	 */
	@ResponseBody
//	@RequiresPermissions("peoplerealaccount:realAccount:del")
	@RequestMapping(value = "deleteAllBill")
	public AjaxJson deleteAllBill(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			RealAccount realAccount = realAccountService.get(id);
			PersonWage personWage = realAccount.getPersonWageList().get(0);
			ProcessRoutine processRoutine = new ProcessRoutine();
			processRoutine.setCosBillNum(personWage.getBillNum());
			List<ProcessRoutine> processRoutines = processRoutineMapper.findList(processRoutine);
			for(int i=0;i<processRoutines.size();i++){
				processRoutines.get(i).setCosBillNum(null);
				processRoutineMapper.update(processRoutines.get(i));
			}

			personWageMapper.delete(personWage);
			realAccountService.delete(realAccount);
		}
		j.setMsg("删除人工凭证单据成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("peoplerealaccount:realAccount:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(RealAccount realAccount, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "人工成本凭证单据管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<RealAccount> page = realAccountService.findPage(new Page<RealAccount>(request, response, -1), realAccount);
    		new ExportExcel("人工成本单据管理", RealAccount.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出人工成本凭证单据管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public RealAccount detail(String id) {
		return realAccountService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("peoplerealaccount:realAccount:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<RealAccount> list = ei.getDataList(RealAccount.class);
			for (RealAccount realAccount : list){
				try{
					realAccountService.save(realAccount);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条人工成本凭证单据管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条人工成本凭证单据管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入人工成本凭证单据管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/peoplerealaccount/realAccount/?repage";
    }
	
	/**
	 * 下载导入材料凭证单据管理数据模板
	 */
	@RequiresPermissions("peoplerealaccount:realAccount:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "人工成本凭证单据管理数据导入模板.xlsx";
    		List<RealAccount> list = Lists.newArrayList(); 
    		new ExportExcel("人工成本凭证单据管理数据", RealAccount.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/peoplerealaccount/realAccount/?repage";
    }

	/**
	 * 提交之前判断借贷是否一致
	 */
	@ResponseBody
	@RequestMapping(value = "beforeSubmit")
	public AjaxJson beforeSubmit(String id){
		AjaxJson ajaxJson = new AjaxJson();
		RealAccount realAccount = realAccountService.get(id);
		List<RealAccountDetail> realAccountDetailList = realAccount.getRealAccountDetailList();
		logger.debug("realAccountDetailList"+realAccountDetailList);
		double jieSum = 0;
		double daiSum = 0;
		for(int i=0;i<realAccountDetailList.size();i++){
			if("B".equals(realAccountDetailList.get(i).getBlflag())){
				jieSum = jieSum + realAccountDetailList.get(i).getItemSum();
			}
			if("L".equals(realAccountDetailList.get(i).getBlflag())){
				daiSum = daiSum + realAccountDetailList.get(i).getItemSum();
			}
		}
		if(jieSum == daiSum){
			ajaxJson.setSuccess(true);
		}else{
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("借贷金额不相等,无法提交!");
		}
		return ajaxJson;
	}
	

}