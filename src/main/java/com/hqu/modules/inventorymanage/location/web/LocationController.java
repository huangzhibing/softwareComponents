/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.location.web;

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
import com.hqu.modules.inventorymanage.bin.entity.Bin;
import com.hqu.modules.inventorymanage.bin.service.BinService;
import com.hqu.modules.inventorymanage.location.entity.Location;
import com.hqu.modules.inventorymanage.location.service.LocationService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;

/**
 * 货位管理Controller
 * @author M1ngz
 * @version 2018-04-12
 */
@Controller
@RequestMapping(value = "${adminPath}/location/location")
public class LocationController extends BaseController {

	@Autowired
	private LocationService locationService;
	@Autowired
	private BinService binService;
	
	@ModelAttribute
	public Location get(@RequestParam(required=false) String id) {
		Location entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = locationService.get(id);
		}
		if (entity == null){
			entity = new Location();
		}
		return entity;
	}
	
	/**
	 * 货位列表页面
	 */
	@RequiresPermissions("warehouse:wareHouse:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "inventorymanage/location/locationList";
	}
	
		/**
	 * 货位列表数据
	 */
	@ResponseBody
	@RequiresPermissions("warehouse:wareHouse:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Location location, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Location> page = locationService.findPage(new Page<Location>(request, response), location); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑货位表单页面
	 */
	@RequiresPermissions(value={"warehouse:wareHouse:view","warehouse:wareHouse:add","warehouse:wareHouse:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Location location, Model model) {
		if(StringUtils.isBlank(location.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
            Bin bin = binService.getByBinId(location.getBinId());
            location.setLocId(location.getBinId() + locationService.getMaxId(bin.getBinId()));
            location.setWareId(bin.getWareId());
		}
        model.addAttribute("location", location);

        return "inventorymanage/location/locationForm";
	}

	/**
	 * 保存货位
	 */
	@RequiresPermissions(value={"warehouse:wareHouse:add","warehouse:wareHouse:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
    @ResponseBody
	public AjaxJson save(Location location, Model model, RedirectAttributes redirectAttributes) throws Exception{
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, location)){
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        //新增或编辑表单保存
        locationService.save(location);//保存
        j.setSuccess(true);
        j.setMsg("保存成功！");
        return j;
	}
	
	/**
	 * 删除货位
	 */
	@ResponseBody
	@RequiresPermissions("warehouse:wareHouse:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Location location, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		locationService.delete(location);
		j.setMsg("删除货位成功");
		return j;
	}
	
	/**
	 * 批量删除货位
	 */
	@ResponseBody
	@RequiresPermissions("warehouse:wareHouse:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			locationService.delete(locationService.get(id));
		}
		j.setMsg("删除货位成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("warehouse:wareHouse:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Location location, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "货位"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Location> page = locationService.findPage(new Page<Location>(request, response, -1), location);
    		new ExportExcel("货位", Location.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出货位记录失败！失败信息："+e.getMessage());
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
			List<Location> list = ei.getDataList(Location.class);
			for (Location location : list){
				try{
					locationService.save(location);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条货位记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条货位记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入货位失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/location/location/?repage";
    }
	
	/**
	 * 下载导入货位数据模板
	 */
	@RequiresPermissions("warehouse:wareHouse:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "货位数据导入模板.xlsx";
    		List<Location> list = Lists.newArrayList(); 
    		new ExportExcel("货位数据", Location.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/location/location/?repage";
    }

}