/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.employee.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.hqu.modules.inventorymanage.bin.entity.Bin;
import com.hqu.modules.inventorymanage.bin.service.BinService;
import com.hqu.modules.inventorymanage.employee.entity.EmployeeWareHouse;
import com.hqu.modules.inventorymanage.employee.service.EmployeeWareHouseService;
import com.hqu.modules.inventorymanage.location.entity.Location;
import com.hqu.modules.inventorymanage.location.service.LocationService;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.hqu.modules.inventorymanage.warehouse.service.WareHouseService;
import com.jeeplus.modules.sys.entity.User;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;
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
import com.hqu.modules.inventorymanage.employee.entity.Employee;
import com.hqu.modules.inventorymanage.employee.service.EmployeeService;

/**
 * 仓库人员定义Controller
 * @author liujiachen
 * @version 2018-04-17
 */
@Controller
@RequestMapping(value = "${adminPath}/employee/employee")
public class EmployeeController extends BaseController {

	@Autowired
	private EmployeeService employeeService;
    @Autowired
    private WareHouseService wareHouseService;
    @Autowired
    private BinService binService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private EmployeeWareHouseService employeeWareHouseService;

	
	@ModelAttribute
	public Employee get(@RequestParam(required=false) String id) {
		Employee entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = employeeService.get(id);
		}
		if (entity == null){
			entity = new Employee();
		}
		return entity;
	}
	
	/**
	 * 仓库人员列表页面
	 */
	@RequiresPermissions("employee:employee:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "inventorymanage/employee/employeeList";
	}
	
		/**
	 * 仓库人员列表数据
	 */
	@ResponseBody
	@RequiresPermissions("employee:employee:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Employee employee, HttpServletRequest request, HttpServletResponse response, Model model,String type,String typeid) {
		logger.debug("type:"+type);
		logger.debug("typeid:"+typeid);
		EmployeeWareHouse employeeWareHouse = new EmployeeWareHouse();
        Page<Employee> result = new Page<Employee>();
        Page<EmployeeWareHouse> employeeWareHousePage = null;
        Employee temp;
        if("ware".equals(type)){
            logger.debug("ware");
            employeeWareHouse.setWareHouse(wareHouseService.getByWareHouseId(typeid));
            employeeWareHousePage = employeeWareHouseService.findPage(new Page<EmployeeWareHouse>(), employeeWareHouse);

        } else if("bin".equals(type)) {
            logger.debug("bin");
            employeeWareHouse.setBin(binService.getByBinId(typeid));
            employeeWareHousePage = employeeWareHouseService.findPage(new Page<EmployeeWareHouse>(), employeeWareHouse);

        } else if("loc".equals(type)){
            logger.debug("loc");
            employeeWareHouse.setLocation(locationService.getByLocId(typeid));
            employeeWareHousePage = employeeWareHouseService.findPage(new Page<EmployeeWareHouse>(), employeeWareHouse);

        }
        logger.debug("employeeWareHousePage:"+JSON.toJSONString(employeeWareHousePage));
	    Page<Employee> page = employeeService.findPage(new Page<Employee>(request, response), employee);
        if(employeeWareHousePage != null){
            logger.debug("size:"+employeeWareHousePage.getList().size());
            if(employeeWareHousePage.getList().size() == 0){
                logger.debug("empty page");
                return getBootstrapData(new Page<Employee>(request,response));
            }
            page.setCount(employeeWareHousePage.getCount());
            List<String> ids = Lists.newArrayList();
            List<Employee> removeList = Lists.newArrayList();
            for(EmployeeWareHouse house : employeeWareHousePage.getList()){
                ids.add(house.getEmp().getId());
            }
            List<Employee> employeeList = page.getList();
            for(Employee e : employeeList){
                if(!ids.contains(e.getUser().getNo())){
                    removeList.add(e);
                }
            }
            employeeList.removeAll(removeList);
            page.setList(employeeList);
            page.setCount(employeeList.size());
        }

        logger.debug(JSON.toJSONString(page));
        return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑仓库人员表单页面
	 */
	@RequiresPermissions(value={"employee:employee:view","employee:employee:add","employee:employee:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Employee employee, Model model,String type,String typeid) {
		model.addAttribute("employee", employee);
		EmployeeWareHouse employeeWareHouse = employeeWareHouseService.getByEmployee(employee);
		if(StringUtils.isBlank(employee.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			employee.setNote("仓库号为"+typeid+"的管理员");
            if("ware".equals(type)){
                model.addAttribute("typeid",typeid);
            } else if("bin".equals(type)) {
                model.addAttribute("typeid",typeid);

            } else if("loc".equals(type)){
                model.addAttribute("typeid",typeid);

            }
            model.addAttribute("type",type);
		} else {
		    if(employeeWareHouse!=null){
		        if(employeeWareHouse.getWareHouse() != null){
		            model.addAttribute("type","ware");
		            model.addAttribute("typeid",employeeWareHouse.getWareHouse().getWareID());
                } else if(employeeWareHouse.getBin() != null){
                    model.addAttribute("type","bin");
                    model.addAttribute("typeid",employeeWareHouse.getBin().getBinId());
                } else if(employeeWareHouse.getLocation() != null){
                    model.addAttribute("type","loc");
                    model.addAttribute("typeid",employeeWareHouse.getLocation().getLocId());
                }
            }
        }
		return "inventorymanage/employee/employeeForm";
	}

	/**
	 * 保存仓库人员
	 */
	@RequiresPermissions(value={"employee:employee:add","employee:employee:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Employee employee, Model model, RedirectAttributes redirectAttributes,String type,String typeid) throws Exception{
		if (!beanValidator(model, employee)){
			return form(employee, model,type,typeid);
		}
		if (StringUtils.isEmpty(type)){
            addMessage(redirectAttributes, "保存仓库人员失败");
            return "redirect:"+Global.getAdminPath()+"/employee/employee/?repage";
        }
        if(employeeWareHouseService.getCount(employee) > 0){
            addMessage(redirectAttributes, "该员工已是仓管员");
            return "redirect:"+Global.getAdminPath()+"/employee/employee/form?repage";
        }
		WareHouse w;
		Bin b;
		Location l;
		Employee dbEmployee = employeeService.get(employee.getId());
        EmployeeWareHouse e = employeeWareHouseService.getByEmployee(dbEmployee);
        if(e == null) {
            e = new EmployeeWareHouse();
        }
		//新增或编辑表单保存
		employeeService.save(employee);//保存
        e.setEmp(employee);
        if("ware".equals(type)){
            w = wareHouseService.getByWareHouseId(typeid);
            e.setWareHouse(w);
        } else if("bin".equals(type)) {
            b = binService.getByBinId(typeid);
            e.setBin(b);
        } else if("loc".equals(type)){
            l = locationService.getByLocId(typeid);
            e.setLocation(l);
        }
        employeeWareHouseService.save(e);
		addMessage(redirectAttributes, "保存仓库人员成功");
		return "redirect:"+Global.getAdminPath()+"/employee/employee/?repage";
	}
	
	/**
	 * 删除仓库人员
	 */
	@ResponseBody
	@RequiresPermissions("employee:employee:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Employee employee, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		EmployeeWareHouse employeeWareHouse = employeeWareHouseService.getByEmployee(employee);
		employeeWareHouseService.delete(employeeWareHouse);
		employeeService.delete(employee);
		j.setMsg("删除仓库人员成功");
		return j;
	}
	
	/**
	 * 批量删除仓库人员
	 */
	@ResponseBody
	@RequiresPermissions("employee:employee:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids,String type,String typeid, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");

        Employee employee;
        for(String id : idArray){
            employee = employeeService.get(id);
            EmployeeWareHouse employeeWareHouse = employeeWareHouseService.getByEmployee(employee);
            employeeWareHouseService.delete(employeeWareHouse);
			employeeService.delete(employeeService.get(id));
		}
		j.setMsg("删除仓库人员成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("employee:employee:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Employee employee, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "仓库人员"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Employee> page = employeeService.findPage(new Page<Employee>(request, response, -1), employee);
    		new ExportExcel("仓库人员", Employee.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出仓库人员记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("employee:employee:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Employee> list = ei.getDataList(Employee.class);
			for (Employee employee : list){
				try{
					employeeService.save(employee);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条仓库人员记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条仓库人员记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入仓库人员失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/employee/employee/?repage";
    }
	
	/**
	 * 下载导入仓库人员数据模板
	 */
	@RequiresPermissions("employee:employee:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "仓库人员数据导入模板.xlsx";
    		List<Employee> list = Lists.newArrayList(); 
    		new ExportExcel("仓库人员数据", Employee.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/employee/employee/?repage";
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