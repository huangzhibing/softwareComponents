/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.realaccount.web;


import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.service.ItemService;
import com.hqu.modules.costmanage.realaccount.entity.RealAccountDetail;
import com.hqu.modules.inventorymanage.billmain.service.BillMainService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
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
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.hqu.modules.costmanage.cosbillrecord.entity.CosBillRecord;
import com.hqu.modules.costmanage.cosbillrecord.mapper.CosBillRecordMapper;
import com.hqu.modules.costmanage.realaccount.entity.RealAccount;
import com.hqu.modules.costmanage.realaccount.service.RealAccountService;

/**
 * 材料凭证单据管理Controller
 * @author hzb
 * @version 2018-09-05
 */
@Controller
@RequestMapping(value = "${adminPath}/realaccount/realAccount")
public class RealAccountController extends BaseController {

	@Autowired
	private RealAccountService realAccountService;
	@Autowired
	BillMainService billMainService;
	@Autowired
	ItemService itemService;
	@Autowired
	private CosBillRecordMapper cosBillRecordMapper;
	
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
	 * 成本凭证单初始化列表页面
	 */
	//@RequiresPermissions("realaccount:realAccount:list")
	@RequestMapping(value = "listAccounting")
	public String listAccouting(String flag,Model model) {
		model.addAttribute("flag",flag);
		return "costmanage/realaccount/realAccountInitializeList";
	}
	/**
	 * 成本凭证单初始化表单
	 */
	@RequestMapping(value = "formAccounting")
	public String formAccounting(String flag,RealAccount realAccount, Model model) {
		if(StringUtils.isBlank(realAccount.getId())){//如果ID是空为添加
			//生成凭证号
			String billNum = realAccountService.getBillNum();
			if(billNum == null){
				billNum = "PZ" + DateUtils.getDate().replace("-", "") + "00001";
			}
			realAccount.setBillNum(billNum);
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
			model.addAttribute("flag",flag);
			model.addAttribute("realAccount", realAccount);
			model.addAttribute("isAdd", true);
		}else{
			if(realAccount.getCosBillNumStatus().equals("B")){
				model.addAttribute("isAdd", false);
			}else{
				model.addAttribute("isAdd", true);
			}
		}
		return "costmanage/realaccount/realAccountInitializeForm";
	}

	/**
	 * 材料凭证单据管理列表页面
	 */
	@RequiresPermissions("realaccount:realAccount:list")
	@RequestMapping(value = {"list", ""})
	public String list(String flag,Model model) {
		model.addAttribute("flag",flag);
		return "costmanage/realaccount/realAccountList";
	}
	
		/**
	 * 材料凭证单据管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("realaccount:realAccount:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(String flag,RealAccount realAccount, HttpServletRequest request, HttpServletResponse response, Model model) {
		if("zhidan".equals(flag)){
			realAccount.setBillMode("A");
		    realAccount.setCosBillNumStatus("S");
        }
		if("xiugai".equals(flag)){
			realAccount.setBillMode("B");
		    realAccount.setCosBillNumStatus("S");
        }
        if("jihe".equals(flag)){
		    realAccount.setCosBillNumStatus("A");
        }
        realAccount.setBillType("材料");
	    Page<RealAccount> page = realAccountService.findPage(new Page<RealAccount>(request, response), realAccount);
		return getBootstrapData(page);
	}

	/**
	 * 材料凭证单据管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("realaccount:realAccount:list")
	@RequestMapping(value = "dataAccounting")
	public Map<String, Object> dataAccounting(RealAccount realAccount, HttpServletRequest request, HttpServletResponse response, Model model) {
		realAccount.setBillType("初始");
		Page<RealAccount> page = realAccountService.findAccountingPage(new Page<RealAccount>(request, response), realAccount);
		return getBootstrapData(page);
	}

	/**
	 * 通过前端传回来的物料编码查询物料
	 */
	@ResponseBody
	@RequestMapping(value = "getItemByCode")
	public Item getItemByCode(String itemCode){
		Item item = new Item();
		item.setCode(itemCode);
		List<Item> itemList = itemService.findList(item);

		return itemList.get(0);
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
        	if(jieSum == 0 || daiSum == 0){
        		if(jieSum == 0){
        			ajaxJson.setSuccess(false);
                    ajaxJson.setMsg("缺少借方,无法提交!");
        		}
        		if(daiSum == 0){
        			ajaxJson.setSuccess(false);
                    ajaxJson.setMsg("缺少贷方,无法提交!");
        		}
        	}else{
        		realAccount.setCosBillNumStatus("B");
                realAccountService.save(realAccount);//稽核
                ajaxJson.setSuccess(true);
        	}
        }else{
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("借贷金额不相等,无法提交!");
        }
        return ajaxJson;
    }



	/**
	 * 查看，增加，编辑材料凭证单据管理表单页面
	 */
	@RequiresPermissions(value={"realaccount:realAccount:view","realaccount:realAccount:add","realaccount:realAccount:edit"},logical=Logical.OR)
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
			String billNum = realAccountService.getCLCBBillNum();
			if(billNum == null){
				billNum = "CL" + DateUtils.getDate().replace("-", "") + "00001";
			}
			realAccount.setBillNum(billNum);
		}
		model.addAttribute("flag",flag);
		model.addAttribute("realAccount", realAccount);
		if(StringUtils.isBlank(realAccount.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		if("xiugai".equals(flag)){
			return "costmanage/realaccount/realAccountEditForm";
		}
		else if("jihe".equals(flag)){
			return "costmanage/realaccount/realAccountCheckForm";
		}
		else if("chaxun".equals(flag)){
			return "costmanage/realaccount/realAccountSearchForm";
		}else {
			return "costmanage/realaccount/realAccountForm";
		}
	}

	/**
	 * 保存成本凭证单据
	 */
	//@RequiresPermissions(value={"realaccount:realAccount:add","realaccount:realAccount:edit"},logical=Logical.OR)
	@RequestMapping(value = "saveAccounting")
	public String saveAccounting(String flag,RealAccount realAccount, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, realAccount)){
			return formAccounting(flag,realAccount, model);
		}
		realAccount.setBillMode("A");
		realAccount.setBillType("初始");
		realAccount.setCosBillNumStatus("S");
		//新增或编辑表单保存
		realAccountService.save(realAccount);//保存
		addMessage(redirectAttributes, "保存成本凭证单据成功");
		return "redirect:"+Global.getAdminPath()+"/realaccount/realAccount/listAccounting?repage&flag="+flag;
	}

	/**
	 * 保存材料凭证单据管理
	 */
	@RequiresPermissions(value={"realaccount:realAccount:add","realaccount:realAccount:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(String saveBtn,String submitBtn,String flag,RealAccount realAccount, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, realAccount)){
			return form(flag,realAccount, model);
		}
        
		if("true".equals(saveBtn)){
			realAccount.setBillMode("A");
			realAccount.setBillType("材料");
		    realAccount.setCosBillNumStatus("S");
			realAccountService.save(realAccount);//保存
			addMessage(redirectAttributes, "保存材料凭证单据管理成功");
		}
		else if("true".equals(submitBtn)){
            realAccount.setCosBillNumStatus("A");
			realAccountService.save(realAccount);//提交
			addMessage(redirectAttributes, "提交材料凭证单据管理成功");
		}else if("true,true".equals(saveBtn)){
		    realAccount.setCosBillNumStatus("S");
			realAccountService.save(realAccount);//保存
			addMessage(redirectAttributes, "保存材料凭证单据管理成功");
		}else if("true,true".equals(submitBtn)){
            realAccount.setCosBillNumStatus("A");
			realAccountService.save(realAccount);//提交
			addMessage(redirectAttributes, "提交材料凭证单据管理成功");
		}else{
		    if("jihe".equals(flag)){
		        Date date = new Date();
		        User user = UserUtils.getUser();
		        realAccount.setCheckId(user.getNo());
		        realAccount.setCheckName(user.getName());
		        realAccount.setCheckDate(date);
		        realAccount.setCosBillNumStatus("B");
                realAccountService.save(realAccount);//稽核
                addMessage(redirectAttributes, "稽核材料凭证单据管理成功");
            }
        }

		return "redirect:"+Global.getAdminPath()+"/realaccount/realAccount/?repage&flag="+flag;
	}
	
	/**
	 * 删除材料凭证单据管理
	 */
	@ResponseBody
	@RequiresPermissions("realaccount:realAccount:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(RealAccount realAccount, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		realAccountService.delete(realAccount);
		j.setMsg("删除材料凭证单据管理成功");
		return j;
	}
	
	/**
	 * 批量删除材料凭证单据管理
	 */
	@ResponseBody
	@RequiresPermissions("realaccount:realAccount:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			realAccountService.delete(realAccountService.get(id));
		}
		j.setMsg("删除材料凭证单据管理成功");
		return j;
	}
	
	/**
	 * 批量删除材料凭证单据管理
	 */
	@ResponseBody
	@RequiresPermissions("realaccount:realAccount:del")
	@RequestMapping(value = "beforeDelete")
	public AjaxJson beforeDelete(String id, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		realAccountService.delete(realAccountService.get(id));
		j.setMsg("删除材料凭证单据管理成功");
		return j;
	}
	
	/**
	 * 删除材料凭证单据管理
	 */
	@ResponseBody
	@RequiresPermissions("realaccount:realAccount:del")
	@RequestMapping(value = "deleteBack")
	public AjaxJson deleteBack(RealAccount realAccount, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		CosBillRecord cosBillRecord = cosBillRecordMapper.getBack(realAccount.getBillNum());
		cosBillRecord.setCosBillNum(null);
		cosBillRecord.setCosBillNumStatus("A");
		cosBillRecordMapper.update(cosBillRecord);
		realAccountService.delete(realAccount);
		j.setMsg("删除材料凭证单据管理成功");
		return j;
	}
	
	/**
	 * 批量删除材料凭证单据管理
	 */
	@ResponseBody
	@RequiresPermissions("realaccount:realAccount:del")
	@RequestMapping(value = "deleteAllBack")
	public AjaxJson deleteAllBack(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			RealAccount realAccount = realAccountService.get(id);
			CosBillRecord cosBillRecord = cosBillRecordMapper.getBack(realAccount.getBillNum());
			cosBillRecord.setCosBillNum(null);
			cosBillRecord.setCosBillNumStatus("A");
			cosBillRecordMapper.update(cosBillRecord);
			realAccountService.delete(realAccount);
		}
		j.setMsg("删除材料凭证单据管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("realaccount:realAccount:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(RealAccount realAccount, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "材料凭证单据管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<RealAccount> page = realAccountService.findPage(new Page<RealAccount>(request, response, -1), realAccount);
    		new ExportExcel("材料凭证单据管理", RealAccount.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出材料凭证单据管理记录失败！失败信息："+e.getMessage());
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
	@RequiresPermissions("realaccount:realAccount:import")
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
				failureMsg.insert(0, "，失败 "+failureNum+" 条材料凭证单据管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条材料凭证单据管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入材料凭证单据管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/realaccount/realAccount/?repage";
    }
	
	/**
	 * 下载导入材料凭证单据管理数据模板
	 */
	@RequiresPermissions("realaccount:realAccount:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "材料凭证单据管理数据导入模板.xlsx";
    		List<RealAccount> list = Lists.newArrayList(); 
    		new ExportExcel("材料凭证单据管理数据", RealAccount.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/realaccount/realAccount/?repage";
    }
	

}