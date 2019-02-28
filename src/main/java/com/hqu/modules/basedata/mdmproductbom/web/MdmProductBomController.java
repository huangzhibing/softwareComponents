/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.mdmproductbom.web;

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
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.hqu.modules.basedata.mdmproductbom.entity.MdmProductBom;
import com.hqu.modules.basedata.mdmproductbom.service.MdmProductBomService;

/**
 * 产品结构维护Controller
 * @author liujiachen
 * @version 2018-04-03
 */
@Controller
@RequestMapping(value = "${adminPath}/mdmproductbom/mdmProductBom")
public class MdmProductBomController extends BaseController {

	@Autowired
	private MdmProductBomService mdmProductBomService;
	
	@ModelAttribute
	public MdmProductBom get(@RequestParam(required=false) String id) {
		MdmProductBom entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mdmProductBomService.get(id);
		}
		if (entity == null){
			entity = new MdmProductBom();
		}
		return entity;
	}
	
	/**
	 * 产品结构维护列表页面
	 */
	@RequiresPermissions("mdmproductbom:mdmProductBom:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "basedata/mdmproductbom/mdmProductBomList";
	}
	
		/**
	 * 产品结构维护列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(MdmProductBom mdmProductBom, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MdmProductBom> page = mdmProductBomService.findPage(new Page<MdmProductBom>(request, response), mdmProductBom); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑产品结构维护表单页面
	 */
	@RequiresPermissions(value={"mdmproductbom:mdmProductBom:view","mdmproductbom:mdmProductBom:add","mdmproductbom:mdmProductBom:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(MdmProductBom mdmProductBom, Model model) {
		model.addAttribute("mdmProductBom", mdmProductBom);
		if(StringUtils.isBlank(mdmProductBom.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "basedata/mdmproductbom/mdmProductBomForm";
	}

	/**
	 * 保存产品结构维护
	 */
	@RequiresPermissions(value={"mdmproductbom:mdmProductBom:add","mdmproductbom:mdmProductBom:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(MdmProductBom mdmProductBom, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, mdmProductBom)){
			return form(mdmProductBom, model);
		}
		//新增或编辑表单保存
		mdmProductBomService.save(mdmProductBom);//保存
		addMessage(redirectAttributes, "保存产品结构维护成功");
		return "redirect:"+Global.getAdminPath()+"/mdmproductbom/mdmProductBom/?repage";
	}
	
	/**
	 * 删除产品结构维护
	 */
	@ResponseBody
	@RequiresPermissions("mdmproductbom:mdmProductBom:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(MdmProductBom mdmProductBom, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		mdmProductBomService.delete(mdmProductBom);
		j.setMsg("删除产品结构维护成功");
		return j;
	}
	
	/**
	 * 批量删除产品结构维护
	 */
	@ResponseBody
	@RequiresPermissions("mdmproductbom:mdmProductBom:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			mdmProductBomService.delete(mdmProductBomService.get(id));
		}
		j.setMsg("删除产品结构维护成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("mdmproductbom:mdmProductBom:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(MdmProductBom mdmProductBom, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "产品结构维护"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<MdmProductBom> page = mdmProductBomService.findPage(new Page<MdmProductBom>(request, response, -1), mdmProductBom);
    		new ExportExcel("产品结构维护", MdmProductBom.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出产品结构维护记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("mdmproductbom:mdmProductBom:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<MdmProductBom> list = ei.getDataList(MdmProductBom.class);
			for (MdmProductBom mdmProductBom : list){
				try{
					mdmProductBomService.save(mdmProductBom);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条产品结构维护记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条产品结构维护记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入产品结构维护失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mdmproductbom/mdmProductBom/?repage";
    }
	
	/**
	 * 下载导入产品结构维护数据模板
	 */
	@RequiresPermissions("mdmproductbom:mdmProductBom:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "产品结构维护数据导入模板.xlsx";
    		List<MdmProductBom> list = Lists.newArrayList(); 
    		new ExportExcel("产品结构维护数据", MdmProductBom.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mdmproductbom/mdmProductBom/?repage";
    }

}