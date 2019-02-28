/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.salorder.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.salemanage.salorder.entity.SalOrderItem;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.hqu.modules.basedata.product.entity.Product;
import com.hqu.modules.basedata.product.service.ProductService;
import com.hqu.modules.salemanage.salorder.entity.SalOrder;
import com.hqu.modules.salemanage.salorder.service.SalOrderService;

/**
 * 内部订单Controller
 * @author dongqida
 * @version 2018-05-27
 */
@Controller
@RequestMapping(value = "${adminPath}/salorder/salOrder")
public class SalOrderController extends BaseController {

	@Autowired
	private SalOrderService salOrderService;
	@Autowired
	private ProductService productService;
	@ModelAttribute
	public SalOrder get(@RequestParam(required=false) String id) {
		SalOrder entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = salOrderService.get(id);
		}
		if (entity == null){
			entity = new SalOrder();
		}
		return entity;
	}
	
	/**
	 * 内部订单列表页面
	 */
	@RequiresPermissions("salorder:salOrder:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "salemanage/salorder/salOrderList";
	}
	@RequiresPermissions("salorder:salOrder:list")
	@RequestMapping(value ="salOrderSearch")
	public String listSearch() {
		return "salemanage/salorder/salOrderListSearch";
	}
		/**
	 * 内部订单列表数据
	 */
	@ResponseBody
	@RequiresPermissions("salorder:salOrder:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(SalOrder salOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SalOrder> page = salOrderService.findPage(new Page<SalOrder>(request, response), salOrder); 
		if(salOrder.getProduct()!=null) {
			Product pt=salOrder.getProduct();
			if((pt.getItem()!=null&&!StringUtils.isEmpty(pt.getItem().getCode()))||!StringUtils.isEmpty(pt.getItemName())||!StringUtils.isEmpty(pt.getItemSpec())) {
				List<String> slist=salOrderService.findProductList(salOrder.getProduct());
				List<SalOrder> salist=new ArrayList<>();logger.debug("ordercode=:"+slist.size());
				for(int i=0;i<slist.size();i++) {
					if(slist.get(i).equals(page.getList().get(i).getId())) {
						salist.add(page.getList().get(i));
					}
				}
				page.setList(salist);
			}
		}
		
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑内部订单表单页面
	 */
	@RequiresPermissions(value={"salorder:salOrder:view","salorder:salOrder:add","salorder:salOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SalOrder salOrder, Model model) {
		model.addAttribute("salOrder", salOrder);
		if(StringUtils.isBlank(salOrder.getId())){//如果ID是空为添加
			//生成业务主键
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String newBillNum = salOrderService.getMaxIdByTypeAndDate("ord" + sdf.format(date));
			if (StringUtils.isEmpty(newBillNum)) {
				newBillNum = "0000";
			} else {
				newBillNum = String.format("%04d", Integer.parseInt(newBillNum.substring(11, 15)) + 1);
			}
			logger.debug("billNum:" + newBillNum);
			salOrder.setOrderCode("ord" + sdf.format(date) + newBillNum);
			salOrder.setInputDate(date);
			User user=UserUtils.getUser();//获取当前用户
			salOrder.setInputPerson(user);
			model.addAttribute("isAdd", true);
		}
		return "salemanage/salorder/salOrderForm";
	}
	@RequiresPermissions(value={"salorder:salOrder:view","salorder:salOrder:add","salorder:salOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "formSearch")
	public String formSearch(SalOrder salOrder, Model model) {
		model.addAttribute("salOrder", salOrder);
		if(StringUtils.isBlank(salOrder.getId())){//如果ID是空为添加
			//生成业务主键
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String newBillNum = salOrderService.getMaxIdByTypeAndDate("ord" + sdf.format(date));
			if (StringUtils.isEmpty(newBillNum)) {
				newBillNum = "0000";
			} else {
				newBillNum = String.format("%04d", Integer.parseInt(newBillNum.substring(11, 15)) + 1);
			}
			logger.debug("billNum:" + newBillNum);
			salOrder.setOrderCode("ord" + sdf.format(date) + newBillNum);
			salOrder.setInputDate(date);
			User user=UserUtils.getUser();
			salOrder.setInputPerson(user);
			model.addAttribute("isAdd", true);
		}
		return "salemanage/salorder/salOrderFormSearch";
	}
	/**
	 * 保存内部订单
	 */
	@Transactional(rollbackFor = Exception.class)
	@RequiresPermissions(value={"salorder:salOrder:add","salorder:salOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(SalOrder salOrder, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, salOrder)){
			return form(salOrder, model);
		}
		//新增或编辑表单保存
		salOrderService.save(salOrder);//保存
		List<String> procode=new ArrayList<>();
		for(SalOrderItem salOrderItem:salOrder.getSalOrderItemList()){
			if(salOrderItem.getProdCode()!=null&&StringUtils.isNotBlank(salOrderItem.getProdCode().getId())) {
				procode.add(salOrderItem.getProdCode().getId());
			}
		}
		if(procode.size()>0) {
			List<SalOrderItem> salOrderItems=salOrderService.selectPartCode(procode);
			if(salOrderItems.size()>0) {
				int i = 1;
				for (SalOrderItem item : salOrderItems) {
					item.setSeqId(String.valueOf(i++));
				}
				salOrder.setId("");
				salOrder.setOrderCode(String.format("ord%d", Long.parseLong(salOrder.getOrderCode().substring(3)) + 1));
				salOrder.setSalOrderItemList(salOrderItems);
				salOrderService.save(salOrder);
			}
		}
		addMessage(redirectAttributes, "保存内部订单成功");
		return "redirect:"+Global.getAdminPath()+"/salorder/salOrder/?repage";
	}
	
	/**
	 * 删除内部订单
	 */
	@ResponseBody
	@RequiresPermissions("salorder:salOrder:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(SalOrder salOrder, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		salOrderService.delete(salOrder);
		j.setMsg("删除内部订单成功");
		return j;
	}
	
	/**
	 * 批量删除内部订单
	 */
	@ResponseBody
	@RequiresPermissions("salorder:salOrder:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			salOrderService.delete(salOrderService.get(id));
		}
		j.setMsg("删除内部订单成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("salorder:salOrder:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(SalOrder salOrder, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "内部订单"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SalOrder> page = salOrderService.findPage(new Page<SalOrder>(request, response, -1), salOrder);
    		new ExportExcel("内部订单", SalOrder.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出内部订单记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public SalOrder detail(String id) {
		return salOrderService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("salorder:salOrder:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SalOrder> list = ei.getDataList(SalOrder.class);
			for (SalOrder salOrder : list){
				try{
					salOrderService.save(salOrder);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条内部订单记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条内部订单记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入内部订单失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/salorder/salOrder/?repage";
    }
	
	/**
	 * 下载导入内部订单数据模板
	 */
	@RequiresPermissions("salorder:salOrder:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "内部订单数据导入模板.xlsx";
    		List<SalOrder> list = Lists.newArrayList(); 
    		new ExportExcel("内部订单数据", SalOrder.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/salorder/salOrder/?repage";
    }
	

}