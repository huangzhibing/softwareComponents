/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contractmain.web;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.task.Comment;
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
import com.hqu.modules.purchasemanage.contractmain.entity.ContractDetailSummary;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractDetailView;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractDetailWarning;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractMain;
import com.hqu.modules.purchasemanage.contractmain.service.ContractMainQueryService;
import com.hqu.modules.purchasemanage.contractmain.service.ContractMainSummaryService;
import com.hqu.modules.purchasemanage.contractmain.service.ContractMainViewService;
import com.hqu.modules.purchasemanage.contractmain.service.ContractMainWarningService;
import com.hqu.modules.purchasemanage.linkman.entity.LinkMan;

/**
 * 采购订单汇总Controller
 * @author ltq
 * @version 2018-04-28
 */
@Controller
@RequestMapping(value = "${adminPath}/contractmain/contractMainPrint")
public class ContractMainPrintController extends BaseController {

	@Autowired
	private ContractMainQueryService contractMainService;
	
	@ModelAttribute
	public ContractMain get(@RequestParam(required=false) String id) {
		ContractMain entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = contractMainService.get(id);
		}
		if (entity == null){
			entity = new ContractMain();
		}
		return entity;
	}
	
	/**
	 * 采购合同管理列表页面
	 */
	@RequiresPermissions("contractmainprint:contractMain:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		//return "purchasemanage/contractmain/contractMainDetailViewList";
		return "purchasemanage/contractmain/contractMainPrintList";
	}	
	
	/**
	 * 采购合同管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("contractmainprint:contractMain:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ContractMain contractMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ContractMain> page = contractMainService.findPage(new Page<ContractMain>(request, response), contractMain); 
		 //补全制单人、供应商编号等信息
		   page=contractMainService.addInf(page);
		  //分页
	        String intPage= request.getParameter("pageNo");
	        int pageNo=Integer.parseInt(intPage);
	        int pageSize= page.getPageSize();
	        List<ContractMain> pageList=page.getList();
	       if(pageNo==1&&pageSize==-1){
	    	   page.setList(pageList);
	       }else{
	    	   List<ContractMain> reportL=  new ArrayList<ContractMain>();        
		        for(int i=(pageNo-1)*pageSize;i<pageList.size() && i<pageNo*pageSize;i++){
		        	reportL.add(pageList.get(i));
			    } 
		        page.setList(reportL);
	       }
	    
		return getBootstrapData(page);
	}
	@RequestMapping(value = "form")
	public String form(ContractMain contractMain, Model model) {
		      //补全供应商信息
				if(contractMain.getAccount()!=null&&contractMain.getAccount().getId()!=null){
					List<LinkMan> linkMans=contractMainService.getLinkManList(contractMain.getAccount().getId());
				    //查找该供应商的联系人
					for(LinkMan linkMan:linkMans){
						if(linkMan.getState().equals("生效状态")){
							//将供应商联系人添加进合同对象的accountLinkMam属性
							contractMain.setAccountLinkMam(linkMan.getLinkName());
						}
					}
				}
		model.addAttribute("contractMain", contractMain);
//		System.out.println("contractMain.getAccountLinkMam()+++++++++++++++++++"+contractMain.getAccountLinkMam());
		return "purchasemanage/contractmain/contractMainPrintForm";
	}

	@ResponseBody
	@RequestMapping(value = "writeIsPrint")
	public Map<String, Object> writeIsPrint(@RequestParam(required=true) String id, Model model) {
//    	System.out.println("writeIsPrintwriteIsPrint++++++++++++++++++++++++++++++++++++++++++++"+id);
		//更新打印标志
		contractMainService.executeUpdateSql("UPDATE pur_contractmain SET " + "is_print = '1'" + " WHERE id='"+id+"'");
//		ContractMain contractMain=contractMainService.get(id);
//    	contractMain.setIsPrint("1");
//		contractMainService.save(contractMain);
//		ContractMain contractMain=new ContractMain();
//		contractMain.setId(id);
//		contractMain.setIsPrint("1");
//		contractMainService.writeIsPrint(contractMain);
		HashMap<String ,Object> map=new HashMap<String ,Object>();
		map.put("status","0");
		return map;
	}
	
	
	
}