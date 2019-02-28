/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.bin.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.inventorymanage.location.service.LocationService;
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
import com.hqu.modules.inventorymanage.bin.entity.Bin;
import com.hqu.modules.inventorymanage.bin.service.BinService;

/**
 * 货区定义Controller
 * @author M1ngz
 * @version 2018-04-12
 */
@Controller
@RequestMapping(value = "${adminPath}/bin/bin")
public class BinController extends BaseController {

	@Autowired
	private BinService binService;
	@Autowired
	private LocationService locationService;
	
	@ModelAttribute
	public Bin get(@RequestParam(required=false) String id) {
		Bin entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = binService.get(id);
		}
		if (entity == null){
			entity = new Bin();
		}
		return entity;
	}
	
	/**
	 * 货区列表页面
	 */
	@RequiresPermissions("warehouse:wareHouse:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "inventorymanage/bin/binList";
	}
	
		/**
	 * 货区列表数据
	 */
	@ResponseBody
	@RequiresPermissions("warehouse:wareHouse:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Bin bin, HttpServletRequest request, HttpServletResponse response, Model model) {
		logger.debug("binId:"+bin.getWareId());
	    Page<Bin> page = binService.findPage(new Page<Bin>(request, response), bin);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑货区表单页面
	 */
	@RequiresPermissions(value={"warehouse:wareHouse:view","warehouse:wareHouse:add","warehouse:wareHouse:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Bin bin, Model model) {
		if(StringUtils.isBlank(bin.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
            bin.setBinId(bin.getWareId()+binService.getMaxId(bin.getWareId()));
            logger.debug("New binId:"+bin.getBinId());
		}
		model.addAttribute("bin", bin);

        return "inventorymanage/bin/binForm";
	}

	/**
	 * 保存货区
	 */
	@RequiresPermissions(value={"warehouse:wareHouse:add","warehouse:wareHouse:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
    @ResponseBody
	public AjaxJson save(Bin bin, Model model, RedirectAttributes redirectAttributes) throws Exception{
	    AjaxJson j = new AjaxJson();
        if (!beanValidator(model, bin)){
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        //新增或编辑表单保存
        binService.save(bin);//保存
        j.setSuccess(true);
        j.setMsg("保存成功！");
        return j;
	}
	
	/**
	 * 删除货区
	 */
	@ResponseBody
	@RequiresPermissions("warehouse:wareHouse:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Bin bin, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
        if(!locationService.findAllByBinId(bin.getBinId()).isEmpty()){
            j.setSuccess(false);
            j.setMsg("该仓库下有子节点");
            return j;
        }
		binService.delete(bin);
		j.setMsg("删除货区成功");
		return j;
	}
	
	/**
	 * 批量删除货区
	 */
	@ResponseBody
	@RequiresPermissions("warehouse:wareHouse:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		Bin b = null;
		for(String id : idArray){
		    b = binService.get(id);
            if(!locationService.findAllByBinId(b.getBinId()).isEmpty()){
                j.setSuccess(false);
                j.setMsg(b.getBinDesc() +"下有子节点");
                return j;
            }
			binService.delete(binService.get(id));
		}
		j.setMsg("删除货区成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("warehouse:wareHouse:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Bin bin, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "货区"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Bin> page = binService.findPage(new Page<Bin>(request, response, -1), bin);
    		new ExportExcel("货区", Bin.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出货区记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("warehouse:wareHouse:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Bin> list = ei.getDataList(Bin.class);
			for (Bin bin : list){
				try{
					binService.save(bin);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条货区记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条货区记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入货区失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bin/bin/?repage";
    }
	
	/**
	 * 下载导入货区数据模板
	 */
	@RequiresPermissions("warehouse:wareHouse:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "货区数据导入模板.xlsx";
    		List<Bin> list = Lists.newArrayList(); 
    		new ExportExcel("货区数据", Bin.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bin/bin/?repage";
    }

}