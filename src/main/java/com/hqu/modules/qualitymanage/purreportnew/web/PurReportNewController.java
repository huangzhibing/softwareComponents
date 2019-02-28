/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreportnew.web;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

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
import com.hqu.modules.qualitymanage.mattertype.entity.MatterType;
import com.hqu.modules.qualitymanage.mattertype.service.MatterTypeService;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckDetail;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckMain;
import com.hqu.modules.qualitymanage.purinvcheckmain.service.InvCheckMainService;
import com.hqu.modules.qualitymanage.purreport.service.PurReportService;
import com.hqu.modules.qualitymanage.purreportnew.entity.PurReportNew;
import com.hqu.modules.qualitymanage.purreportnew.service.PurReportNewService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;

/**
 * IQC来料检验报告Controller
 * @author syc
 * @version 2018-08-18
 */
@Controller
@RequestMapping(value = "${adminPath}/purreportnew/purReportNew")
public class PurReportNewController extends BaseController {

	@Autowired
	private MatterTypeService matterTypeService;
	@Autowired
	private PurReportNewService purReportNewService;
	
	@Autowired
	private PurReportService purReportService;
	
	@Autowired
	private InvCheckMainService invCheckMainService;
	
	@ModelAttribute
	public PurReportNew get(@RequestParam(required=false) String id) {
		PurReportNew entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = purReportNewService.get(id);
		}
		if (entity == null){
			entity = new PurReportNew();
		}
		return entity;
	}
	
	/**
	 * IQC来料检验报告列表页面
	 */
	@RequiresPermissions("purreportnew:purReportNew:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "qualitymanage/purreportnew/purReportNewList";
	}
	
		/**
	 * IQC来料检验报告列表数据
	 */
	@ResponseBody
	@RequiresPermissions("purreportnew:purReportNew:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PurReportNew purReportNew, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PurReportNew> page = purReportNewService.findPage(new Page<PurReportNew>(request, response), purReportNew); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑IQC来料检验报告表单页面
	 */
	@RequiresPermissions(value={"purreportnew:purReportNew:view","purreportnew:purReportNew:add","purreportnew:purReportNew:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PurReportNew purReportNew, Model model) {
		
		if(StringUtils.isBlank(purReportNew.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			//purReportNew.setItemCount(new Double(0));
			purReportNew.setReportId(purReportNewService.getReportID());
		}
		model.addAttribute("purReportNew", purReportNew);
		return "qualitymanage/purreportnew/purReportNewForm";
	}

	/**
	 * 保存IQC来料检验报告
	 */
	@RequiresPermissions(value={"purreportnew:purReportNew:add","purreportnew:purReportNew:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PurReportNew purReportNew, Model model, RedirectAttributes redirectAttributes) throws Exception{
//		if (!beanValidator(model, purReportNew)){
//			return form(purReportNew, model);
//		}
		//新增或编辑表单保存
		InvCheckMain inv = purReportNew.getInv();


	//InvCheckMain invcheckMain = purReport.getInvCheckMain();
//		String id = inv.getId();
	//	inv = invCheckMainService.get(id);


		String billnum = inv.getBillnum();
		InvCheckMain temp = new InvCheckMain();
		InvCheckMain invCheckMain = invCheckMainService.findList(inv).get(0);

		invCheckMain.setQmsFlag("已质检");
		invCheckMainService.updateQmsFlag(invCheckMain);

		inv.setId(invCheckMain.getId());
		purReportNew.setInv(inv);
		
		//检验处理为退货则写入到货单相关信息
		if("退货".equals(purReportNew.getHandleResult())){
			InvCheckMain backInv = invCheckMainService.get(purReportNew.getInv().getBillnum());
			if(backInv != null){
				backInv.setBackReason(purReportNew.getBackReason());
				Iterator<InvCheckDetail> it = backInv.getInvCheckDetailList().iterator();
				while(it.hasNext()){
					InvCheckDetail next = it.next();
					if(next.getItemCode().equals(purReportNew.getIcode())){
						next.setBackFlag("1");
						break;
					}
				}
				invCheckMainService.save(backInv);
			}
		}
		purReportNewService.save(purReportNew);//保存
		addMessage(redirectAttributes, "保存IQC来料检验报告成功");
		return "redirect:"+Global.getAdminPath()+"/purreportnew/purReportNew/?repage";
	}
	
	/**
	 * 删除IQC来料检验报告
	 */
	@ResponseBody
	@RequiresPermissions("purreportnew:purReportNew:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PurReportNew purReportNew, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		purReportNewService.delete(purReportNew);
		j.setMsg("删除IQC来料检验报告成功");
		return j;
	}
	
	/**
	 * 批量删除IQC来料检验报告
	 */
	@ResponseBody
	@RequiresPermissions("purreportnew:purReportNew:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			purReportNewService.delete(purReportNewService.get(id));
		}
		j.setMsg("删除IQC来料检验报告成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("purreportnew:purReportNew:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PurReportNew purReportNew, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "IQC来料检验报告"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PurReportNew> page = purReportNewService.findPage(new Page<PurReportNew>(request, response, -1), purReportNew);
    		new ExportExcel("IQC来料检验报告", PurReportNew.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出IQC来料检验报告记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public PurReportNew detail(String id) {
		return purReportNewService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("purreportnew:purReportNew:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PurReportNew> list = ei.getDataList(PurReportNew.class);
			for (PurReportNew purReportNew : list){
				try{
					purReportNewService.save(purReportNew);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条IQC来料检验报告记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条IQC来料检验报告记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入IQC来料检验报告失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purreportnew/purReportNew/?repage";
    }
	
	/**
	 * 下载导入IQC来料检验报告数据模板
	 */
	@RequiresPermissions("purreportnew:purReportNew:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "IQC来料检验报告数据导入模板.xlsx";
    		List<PurReportNew> list = Lists.newArrayList(); 
    		new ExportExcel("IQC来料检验报告数据", PurReportNew.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purreportnew/purReportNew/?repage";
    }
	
	
	/**
 * 功能  质量问题类型列表数据
 */
@ResponseBody
@RequiresPermissions("mattertype:matterType:list")
@RequestMapping(value = "data1")
public Map<String, Object> data1(MatterType matterType, HttpServletRequest request, HttpServletResponse response, Model model) {
	matterType.setType("功能检查");
	Page<MatterType> page = matterTypeService.findPage(new Page<MatterType>(request, response), matterType); 
	return getBootstrapData(page);
}

/**
*  外观  质量问题类型列表数据
*/
@ResponseBody
@RequiresPermissions("mattertype:matterType:list")
@RequestMapping(value = "data2")
public Map<String, Object> data2(MatterType matterType, HttpServletRequest request, HttpServletResponse response, Model model) {
	matterType.setType("外观检查");
	Page<MatterType> page = matterTypeService.findPage(new Page<MatterType>(request, response), matterType); 
return getBootstrapData(page);
}


/**
* 尺寸  质量问题类型列表数据
*/
@ResponseBody
@RequiresPermissions("mattertype:matterType:list")
@RequestMapping(value = "data3")
public Map<String, Object> data3(MatterType matterType, HttpServletRequest request, HttpServletResponse response, Model model) {
	matterType.setType("产品尺寸");
	Page<MatterType> page = matterTypeService.findPage(new Page<MatterType>(request, response), matterType); 
return getBootstrapData(page);
}

}