/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.productcopy.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.New;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.basedata.mdmproductbom.entity.ProductBom;
import com.hqu.modules.basedata.mdmproductbom.service.ProductBomService;
import com.jeeplus.common.utils.IdGen;
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
import com.hqu.modules.basedata.productcopy.entity.MDMCopy;
import com.hqu.modules.basedata.productcopy.service.MDMCopyService;

/**
 * 产品结构复制Controller
 * @author xn
 * @version 2018-04-13
 */
@Controller
@RequestMapping(value = "${adminPath}/productcopy/mDMCopy")
public class MDMCopyController extends BaseController {

	@Autowired
	private MDMCopyService mDMCopyService;
	@Autowired
	private MdmProductBomService mdmProductBomService;

	@Autowired
	private ProductBomService productBomService;
	
	@ModelAttribute
	public MDMCopy get(@RequestParam(required=false) String id) {
		MDMCopy entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mDMCopyService.get(id);
		}
		if (entity == null){
			entity = new MDMCopy();
		}
		return entity;
	}
	
//	/**
//	 * 产品结构复制列表页面
//	 */
//	@RequiresPermissions("productcopy:mDMCopy:list")
//	@RequestMapping(value = {"list", ""})
//	public String list() {
//		return "basedata/productcopy/mDMCopyList";
//	}
	
		/**
	 * 产品结构复制列表数据
	 */
	@ResponseBody
	@RequiresPermissions("productcopy:mDMCopy:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(MDMCopy mDMCopy, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MDMCopy> page = mDMCopyService.findPage(new Page<MDMCopy>(request, response), mDMCopy); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑产品结构复制表单页面
	 */
	@RequiresPermissions(value={"productcopy:mDMCopy:view","productcopy:mDMCopy:add","productcopy:mDMCopy:edit"},logical=Logical.OR)
	@RequestMapping(value = {"list", ""})
	public String form(MDMCopy mDMCopy, Model model) {
		model.addAttribute("mDMCopy", mDMCopy);
		if(StringUtils.isBlank(mDMCopy.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "basedata/productcopy/mDMCopyForm";
	}

	/**
	 * 保存产品结构复制
	 */
	@RequiresPermissions(value={"productcopy:mDMCopy:add","productcopy:mDMCopy:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(MDMCopy mDMCopy, Model model, RedirectAttributes redirectAttributes) throws Exception{
		//MdmProductBom mdmProductBom = mdmProductBomService.get(mDMCopy.getSource().getId());
		//mdmProductBom.setId(mDMCopy.getTarget().getId());

		ProductBom productBomTarget = productBomService.get(mDMCopy.getTarget().getId());
		//查询没有父物料的产品
		List<ProductBom> list = productBomService.findNodeWithoutFatherItem(mDMCopy.getSource().getId());
		for (int i = 0; i < list.size(); i++) {
			ProductBom productBomTmp = list.get(i);
			List<ProductBom> bomList = productBomService.findNodeWithFatherItem(productBomTmp.getItem().getId());
			
			productBomTmp.setId(IdGen.uuid());
			productBomTmp.setProductItemName(productBomTarget.getItemName());
			productBomTmp.getProduct().setId(productBomTarget.getProduct().getItemCode());
			productBomTmp.setUpdateDate(new Date());
			productBomService.save(productBomTmp);
			for (int j = 0; j < bomList.size(); j++) {
				ProductBom productBom = bomList.get(j);
				productBom.setId(IdGen.uuid());
				productBom.setProductItemName(productBomTarget.getItemName());
				productBom.getProduct().setId(productBomTarget.getProduct().getItemCode());
				productBom.setUpdateDate(new Date());
				productBomService.save(productBomTmp);
			}

		}
		//mdmProductBomService.save(mdmProductBom);
		mDMCopy = new MDMCopy();
		model.addAttribute("mDMCopy", mDMCopy);
		if(StringUtils.isBlank(mDMCopy.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		addMessage(redirectAttributes, "产品复制成功");
		return "redirect:"+Global.getAdminPath()+"/productcopy/mDMCopy/?repage";
	}
	
	/**
	 * 删除产品结构复制
	 */
	@ResponseBody
	@RequiresPermissions("productcopy:mDMCopy:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(MDMCopy mDMCopy, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		mDMCopyService.delete(mDMCopy);
		j.setMsg("删除产品结构复制成功");
		return j;
	}
	
	/**
	 * 批量删除产品结构复制
	 */
	@ResponseBody
	@RequiresPermissions("productcopy:mDMCopy:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			mDMCopyService.delete(mDMCopyService.get(id));
		}
		j.setMsg("删除产品结构复制成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("productcopy:mDMCopy:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(MDMCopy mDMCopy, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "产品结构复制"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<MDMCopy> page = mDMCopyService.findPage(new Page<MDMCopy>(request, response, -1), mDMCopy);
    		new ExportExcel("产品结构复制", MDMCopy.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出产品结构复制记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("productcopy:mDMCopy:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<MDMCopy> list = ei.getDataList(MDMCopy.class);
			for (MDMCopy mDMCopy : list){
				try{
					mDMCopyService.save(mDMCopy);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条产品结构复制记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条产品结构复制记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入产品结构复制失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/productcopy/mDMCopy/?repage";
    }
	
	/**
	 * 下载导入产品结构复制数据模板
	 */
	@RequiresPermissions("productcopy:mDMCopy:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "产品结构复制数据导入模板.xlsx";
    		List<MDMCopy> list = Lists.newArrayList(); 
    		new ExportExcel("产品结构复制数据", MDMCopy.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/productcopy/mDMCopy/?repage";
    }

}