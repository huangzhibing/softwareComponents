/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.warehouse.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.hqu.modules.basedata.accounttype.entity.AccountType;
import com.hqu.modules.inventorymanage.bin.entity.Bin;
import com.hqu.modules.inventorymanage.bin.service.BinService;
import com.hqu.modules.inventorymanage.location.entity.Location;
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
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.hqu.modules.inventorymanage.warehouse.service.WareHouseService;

/**
 * 仓库管理Controller
 * @author M1ngz
 * @version 2018-04-12
 */
@Controller
@RequestMapping(value = "${adminPath}/warehouse/wareHouse")
public class WareHouseController extends BaseController {

	@Autowired
	private WareHouseService wareHouseService;
	@Autowired
	private BinService binService;
	@Autowired
	private LocationService locationService;

	@ModelAttribute
	public WareHouse get(@RequestParam(required=false) String id) {
		WareHouse entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wareHouseService.get(id);
		}
		if (entity == null){
			entity = new WareHouse();
		}
		return entity;
	}

	
	/**
	 * 仓库列表页面
	 */
	@RequiresPermissions("warehouse:wareHouse:list")
	@RequestMapping(value = {"list", ""})
	public String list(Model model) {

        model.addAttribute("bin", new Bin());
        model.addAttribute("location", new Location());
		return "inventorymanage/warehouse/wareHouseList";
	}


		/**
	 * 仓库列表数据
	 */
	@ResponseBody
	@RequiresPermissions("warehouse:wareHouse:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(WareHouse wareHouse, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WareHouse> page = wareHouseService.findPage(new Page<WareHouse>(request, response), wareHouse); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑仓库表单页面
	 */
	@RequiresPermissions(value={"warehouse:wareHouse:view","warehouse:wareHouse:add","warehouse:wareHouse:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WareHouse wareHouse, Model model) {
		if(StringUtils.isBlank(wareHouse.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
            wareHouse.setWareID(wareHouseService.getMaxId());
		}
        model.addAttribute("wareHouse", wareHouse);

        return "inventorymanage/warehouse/wareHouseForm";
	}

	/**
	 * 保存仓库
	 */
	@RequiresPermissions(value={"warehouse:wareHouse:add","warehouse:wareHouse:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
    @ResponseBody
	public AjaxJson save(WareHouse wareHouse, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
	    if (!beanValidator(model, wareHouse)){
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
		}
		//新增或编辑表单保存
		wareHouseService.save(wareHouse);//保存
        j.setSuccess(true);
        j.setMsg("保存成功！");
        return j;
	}
	
	/**
	 * 删除仓库
	 */
	@ResponseBody
	@RequiresPermissions("warehouse:wareHouse:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(WareHouse wareHouse, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
//		logger.debug(JSON.toJSONString(binService.findAllByWareId(wareHouse.getWareID())));
		if(!binService.findAllByWareId(wareHouse.getWareID()).isEmpty()){
		    j.setSuccess(false);
		    j.setMsg("该仓库下有子节点");
		    return j;
        }
		wareHouseService.delete(wareHouse);
		j.setSuccess(true);
		j.setMsg("删除仓库成功");
		return j;
	}
	
	/**
	 * 批量删除仓库
	 */
	@ResponseBody
	@RequiresPermissions("warehouse:wareHouse:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		WareHouse w = null;
		for(String id : idArray){
		    w = wareHouseService.get(id);
            if(!binService.findAllByWareId(w.getWareID()).isEmpty()){
                j.setSuccess(false);
                j.setMsg(w.getWareName()+ "下有子节点");
                return j;
            }
			wareHouseService.delete(w);
		}
		j.setMsg("删除仓库成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("warehouse:wareHouse:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(WareHouse wareHouse, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "仓库"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WareHouse> page = wareHouseService.findPage(new Page<WareHouse>(request, response, -1), wareHouse);
    		new ExportExcel("仓库", WareHouse.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出仓库记录失败！失败信息："+e.getMessage());
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
			List<WareHouse> list = ei.getDataList(WareHouse.class);
			for (WareHouse wareHouse : list){
				try{
					wareHouseService.save(wareHouse);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条仓库记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条仓库记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入仓库失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/warehouse/wareHouse/?repage";
    }
	
	/**
	 * 下载导入仓库数据模板
	 */
	@RequiresPermissions("warehouse:wareHouse:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "仓库数据导入模板.xlsx";
    		List<WareHouse> list = Lists.newArrayList(); 
    		new ExportExcel("仓库数据", WareHouse.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/warehouse/wareHouse/?repage";
    }


    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeData")
    public Map<String, Object> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
        Map<String,Object> result = Maps.newHashMap();
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<WareHouse> list = wareHouseService.findList(new WareHouse());
        result.put("id","");
        result.put("text","总结点");
        Map<String,Object> warehouseMap = null;
        Map<String,Object> binMap = null;
        Map<String,Object> locationMap = null;
        for (int i=0; i<list.size(); i++){
            WareHouse e = list.get(i);
            warehouseMap = Maps.newHashMap();
            warehouseMap.put("id",e.getWareID());
            warehouseMap.put("data",e.getAdminMode());
            warehouseMap.put("text",e.getWareName());
            List<Bin> bins = binService.findAllByWareId(e.getWareID());
            List<Map<String, Object>> binList = Lists.newArrayList();
            for(Bin b : bins){
                binMap = Maps.newHashMap();
                binMap.put("id",b.getBinId());
                binMap.put("text",b.getBinDesc());
                List<Location> locations = locationService.findAllByBinId(b.getBinId());
                List<Map<String, Object>> locationList = Lists.newArrayList();
                for(Location l : locations){
                    locationMap = Maps.newHashMap();
                    locationMap.put("id",l.getLocId());
                    locationMap.put("text",l.getLocDesc());
                    locationList.add(locationMap);
                }
                binMap.put("children",locationList);
                binList.add(binMap);
            }
            warehouseMap.put("children",binList);
            mapList.add(warehouseMap);
        }
        result.put("children",mapList);
        logger.debug("tree"+ JSON.toJSONString(result));
        return result;
    }

    @RequestMapping("/checkAddable")
    @ResponseBody
    public Boolean chkAddable(String wareId,String type){
	    logger.debug(JSON.toJSONString(wareId));
	    logger.debug(JSON.toJSONString(type));

	    WareHouse w = wareHouseService.getByWareHouseId(wareId);
	    logger.debug(w.getAdminMode());
	    if("W".equals(w.getAdminMode()) && ("location".equals(type) || "bin".equals(type))){
	        return false;
        } else if("B".equals(w.getAdminMode())){
	        if("location".equals(type)){
	            return false;
            } else {
	            return true;
            }
        } else {
            return true;
        }
    }

}