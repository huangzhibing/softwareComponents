/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.mdmpos.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.service.OfficeService;
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
import com.hqu.modules.basedata.mdmpos.entity.Pos;
import com.hqu.modules.basedata.mdmpos.service.PosService;

/**
 * 岗位维护表Controller
 * @author luyumiao
 * @version 2018-04-11
 */
@Controller
@RequestMapping(value = "${adminPath}/mdmpos/pos")
public class PosController extends BaseController {

	@Autowired
	private PosService posService;

	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute
	public Pos get(@RequestParam(required=false) String id) {
		Pos entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = posService.get(id);
		}
		if (entity == null){
			entity = new Pos();
		}
		return entity;
	}
	
	/**
	 * 岗位维护表列表页面
	 */
	@RequiresPermissions("mdmpos:pos:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "basedata/mdmpos/posList";
	}
	
		/**
	 * 岗位维护表列表数据
	 */
	@ResponseBody
	@RequiresPermissions("mdmpos:pos:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Pos pos, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Pos> page = posService.findPage(new Page<Pos>(request, response), pos); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑岗位维护表表单页面
	 */
	@RequiresPermissions(value={"mdmpos:pos:view","mdmpos:pos:add","mdmpos:pos:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Pos pos, Model model) {
		model.addAttribute("pos", pos);
		if(StringUtils.isBlank(pos.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "basedata/mdmpos/posForm";
	}

	/**
	 * 保存岗位维护表
	 */
	@RequiresPermissions(value={"mdmpos:pos:add","mdmpos:pos:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Pos pos, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, pos)){
			return form(pos, model);
		}
		Office office = officeService.get(pos.getOrgzCode().getId());
		pos.getOrgzCode().setId(office.getCode());
		//新增或编辑表单保存
		posService.save(pos);//保存
		addMessage(redirectAttributes, "保存岗位维护表成功");
		return "redirect:"+Global.getAdminPath()+"/mdmpos/pos/?repage";
	}
	
	/**
	 * 删除岗位维护表
	 */
	@ResponseBody
	@RequiresPermissions("mdmpos:pos:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Pos pos, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		posService.delete(pos);
		j.setMsg("删除岗位维护表成功");
		return j;
	}
	
	/**
	 * 批量删除岗位维护表
	 */
	@ResponseBody
	@RequiresPermissions("mdmpos:pos:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			posService.delete(posService.get(id));
		}
		j.setMsg("删除岗位维护表成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("mdmpos:pos:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Pos pos, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "岗位维护表"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Pos> page = posService.findPage(new Page<Pos>(request, response, -1), pos);
    		new ExportExcel("岗位维护表", Pos.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出岗位维护表记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("mdmpos:pos:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Pos> list = ei.getDataList(Pos.class);
			for (Pos pos : list){
				try{
					posService.save(pos);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条岗位维护表记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条岗位维护表记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入岗位维护表失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mdmpos/pos/?repage";
    }
	
	/**
	 * 下载导入岗位维护表数据模板
	 */
	@RequiresPermissions("mdmpos:pos:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "岗位维护表数据导入模板.xlsx";
    		List<Pos> list = Lists.newArrayList(); 
    		new ExportExcel("岗位维护表数据", Pos.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mdmpos/pos/?repage";
    }

}