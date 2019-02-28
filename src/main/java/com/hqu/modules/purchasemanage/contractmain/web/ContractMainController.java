/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contractmain.web;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.math.NumberUtils;
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
import com.jeeplus.common.utils.FileUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.hqu.modules.basedata.account.entity.Account;
import com.hqu.modules.basedata.account.service.AccountService;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.entity.ItemClassNew;
import com.hqu.modules.basedata.item.service.ItemService;
import com.hqu.modules.basedata.unit.entity.Unit;
import com.hqu.modules.purchasemanage.adtbilltype.entity.AdtBillType;
import com.hqu.modules.purchasemanage.adtbilltype.service.AdtBillTypeService;
import com.hqu.modules.purchasemanage.adtdetail.entity.AdtDetail;
import com.hqu.modules.purchasemanage.adtdetail.service.AdtDetailService;
import com.hqu.modules.purchasemanage.adtmodel.entity.AdtModel;
import com.hqu.modules.purchasemanage.adtmodel.service.AdtModelService;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractMain;
import com.hqu.modules.purchasemanage.contractmain.service.ContractMainService;
import com.hqu.modules.purchasemanage.contractnotesmodel.entity.ContractNotesModel;
import com.hqu.modules.purchasemanage.contractnotesmodel.service.ContractNotesModelService;
import com.hqu.modules.purchasemanage.contracttype.entity.ContractType;
import com.hqu.modules.purchasemanage.contracttype.service.ContractTypeService;
import com.hqu.modules.purchasemanage.linkman.entity.LinkMan;
import com.hqu.modules.purchasemanage.linkman.entity.Paccount;
import com.hqu.modules.purchasemanage.linkman.service.PaccountService;
import com.hqu.modules.purchasemanage.supprice.entity.PurSupPrice;
import com.hqu.modules.purchasemanage.supprice.entity.PurSupPriceDetail;
import com.hqu.modules.purchasemanage.taxratio.entity.TaxRatio;
import com.hqu.modules.purchasemanage.taxratio.service.TaxRatioService;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.ItemforInv;

/**
 * 采购合同管理Controller
 * @author ltq
 * @version 2018-04-24
 */
@Controller
@RequestMapping(value = "${adminPath}/contractmain/contractMain")
public class ContractMainController extends BaseController {

	@Autowired
	private ContractMainService contractMainService;
	@Autowired
	private AdtDetailService adtDetailService;
	@Autowired
	private AdtModelService  adtModelService;
	@Autowired
	private AdtBillTypeService adtBillTypeService;
	@Autowired
	private ContractTypeService contractTypeService;
	@Autowired
	private ItemService itemService;	
	@Autowired
	private TaxRatioService taxRatioService;
	@Autowired
	private ContractNotesModelService contractNotesModelService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private PaccountService paccountService;
	
	
	
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
	@RequiresPermissions("contractmain:contractMain:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/contractmain/contractMainList";
	}	
	/**
	 * 采购合同管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("contractmain:contractMain:list")
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

	
	/**
	 * 采购税率定义列表数据
	 */
	@ResponseBody
	//@RequiresPermissions("taxratio:taxRatio:list")
	@RequestMapping(value = "ratioData")
	public Map<String, Object> ratioData(TaxRatio taxRatio, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TaxRatio> page = taxRatioService.findPage(new Page<TaxRatio>(request, response), taxRatio); 
		return getBootstrapData(page);
	}
	/**
	 * 合同类型定义列表数据
	 */
	@ResponseBody
	//@RequiresPermissions(value={"contracttype:contractType:list","contractmain:contractMain:list"},logical=Logical.OR)
	@RequestMapping(value = "contractTypeData")
	public Map<String, Object> contractTypeData(ContractType contractType, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ContractType> page = contractTypeService.findPage(new Page<ContractType>(request, response), contractType); 
		return getBootstrapData(page);
	}
	/**
	 * 合同模板定义列表数据
	 */
	@ResponseBody
	//@RequiresPermissions("contractnotesmodel:contractNotesModel:list")
	@RequestMapping(value = "contractNotesModelData")
	public Map<String, Object> contractNotesModelData(ContractNotesModel contractNotesModel, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ContractNotesModel> page = contractNotesModelService.findPage(new Page<ContractNotesModel>(request, response), contractNotesModel); 
		return getBootstrapData(page);
	}		
	
	@ResponseBody
	//@RequiresPermissions("item:item:list")
	@RequiresPermissions(value={"contractmain:contractMain:view","contractmain:contractMain:add","contractmain:contractMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "itemData")
	public Map<String, Object> itemData(ItemforInv item, HttpServletRequest request, HttpServletResponse response, Model model) {
		//Page<Item> page = itemService.findPage(new Page<Item>(request, response), item); 	
		Page<ItemforInv> page=new Page<ItemforInv>(request, response);
		List<ItemforInv> pageList=contractMainService.getItemList(page, item);
		//分页
        String intPage= request.getParameter("pageNo");
        int pageNo=Integer.parseInt(intPage);
        int pageSize= page.getPageSize();
        page.setCount(pageList.size());
        //List<ItemforInv> pageList=page.getList();
       if(pageNo==1&&pageSize==-1){
    	   page.setList(pageList);
       }else{
    	   List<ItemforInv> reportL=  new ArrayList<ItemforInv>();        
	        for(int i=(pageNo-1)*pageSize;i<pageList.size() && i<pageNo*pageSize;i++){
	        	reportL.add(pageList.get(i));
		    } 
	        page.setList(reportL);
       }
		return getBootstrapData(page);
	}
	
	/**
	 * 关系企业列表数据
	 */
	@ResponseBody
	//@RequiresPermissions("account:account:list")
	@RequestMapping(value = "accountData")
	public Map<String, Object> accountData(Account account, HttpServletRequest request, HttpServletResponse response, Model model) {
		//	Paccount paccount= new Paccount();
		//	paccount.setAccountCode(account.getAccountCode());
		//	paccount.setAccountName(account.getAccountName());
		//	Page<Paccount> page = paccountService.findPage(new Page<Paccount>(request, response), paccount); 
        //查找供应商
		Page<Account> page = accountService.findPage(new Page<Account>(request, response), account); 
 	    List<Account> accountList = new ArrayList<Account>();  
 	    //对每个供应商查找对应的联系人
        for(Account accountNew:page.getList()){
        	
            List<LinkMan> linkMans=contractMainService.getLinkManList(account.getId());
            //查找该供应商的联系人
        	for(LinkMan linkMan:linkMans){
        		if(linkMan.getState().equals("生效状态")){
        			//将供应商联系人添加进供应商对象的accountLinkMam属性
        			accountNew.setAccountLinkMam(linkMan.getLinkName());
        		}
        	}
        	
        	accountList.add(accountNew);
        }
		return getBootstrapData(page.setList(accountList));
	}
	
	
	
	/**
	 * 查看，增加，编辑采购合同管理表单页面
	 */
	@RequiresPermissions(value={"contractmain:contractMain:view","contractmain:contractMain:add","contractmain:contractMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ContractMain contractMain, Model model) {
			if(contractMain.getAct().isFinishTask()&&!StringUtils.isBlank(contractMain.getId())){
				//补全contractMaind对象中部分属性的数据
				contractMain=contractMainService.addFormContractMainInf(contractMain);
			    //获取支付方式下拉框的数据
				model.addAttribute("payList", contractMainService.getPayList());
				//获取运输方式下拉框的数据
				model.addAttribute("transList", contractMainService.getTransList());
				model.addAttribute("contractMain", contractMain);
				if(StringUtils.isBlank(contractMain.getId())){//如果ID是空为添加
					model.addAttribute("isAdd", true);
				}		
				return "purchasemanage/contractmain/contractMainViewForm";
			 }else{
				//补全contractMaind对象中部分属性的数据
				contractMain=contractMainService.addFormContractMainInf(contractMain);
				//获取支付方式下拉框的数据
			    model.addAttribute("payList", contractMainService.getPayList());
				//获取运输方式下拉框的数据
				model.addAttribute("transList", contractMainService.getTransList());
				model.addAttribute("contractMain", contractMain);
				if(StringUtils.isBlank(contractMain.getId())){//如果ID是空为添加
					model.addAttribute("isAdd", true);
				}	
				return "purchasemanage/contractmain/contractMainForm";
			 }
		//审核不通过时显示审核意见
	 /*   if("B".equals(contractMain.getBillStateFlag())){
			AdtDetail adtDetail=new AdtDetail();
			adtDetail.setBillNum(contractMain.getBillNum());
			Page<AdtDetail> page=new Page<AdtDetail>();
			page.setOrderBy("justifyDate");
			List<AdtDetail> adtList=adtDetailService.findPage(page, adtDetail).getList();
			if(adtList.size()!=0){
				contractMain.setJustifyRemark(adtList.get(adtList.size()-1).getJustifyRemark());
			}
		}
		
		model.addAttribute("contractMain", contractMain);
		if(StringUtils.isBlank(contractMain.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}		
		return "purchasemanage/contractmain/contractMainForm";
		*/
	}

	/**
	 * 提交采购合同管理
	 */
	@RequiresPermissions(value={"contractmain:contractMain:add","contractmain:contractMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "submit")
	public String submit(ContractMain contractMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, contractMain)){
			return form(contractMain, model);
		}
		try {
		//单据状态改为正在录入/修改
		contractMain.setBillStateFlag("W");
		//录入的合同
		contractMain.setContrState("M");
		//新增或编辑表单保存
		contractMainService.save(contractMain);//保存
	    AdtBillType adtBillType=new AdtBillType();
		    adtBillType.setBillTypeCode("CON001");
		    adtBillType.setBillTypeName("采购合同");
		    adtDetailService.nextStep(contractMain.getBillNum(), adtBillType, "CON", true, null);					
		    addMessage(redirectAttributes, "合同录入提交成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "系统内部错误！");
		}
		//return "redirect:"+Global.getAdminPath()+"/contractmain/contractMain/?repage";
		return "redirect:" + adminPath + "/act/task/todo/";
	}
	
	
	/**
	 * 保存采购合同管理
	 */
	
	@RequiresPermissions(value={"contractmain:contractMain:add","contractmain:contractMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ContractMain contractMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, contractMain)){
			return form(contractMain, model);
		}
		//单据状态改为录入完毕
		//contractMain.setBillStateFlag("A");
		//录入的合同
		contractMain.setContrState("M");
		//新增或编辑表单保存
		contractMainService.save(contractMain);//提交
		addMessage(redirectAttributes, "合同录入保存成功");
		return "redirect:"+Global.getAdminPath()+"/contractmain/contractMain/?repage";
	}
	/*	@ResponseBody
	public AjaxJson save(ContractMain contractMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, contractMain)){		
			j.setMsg("合同录入保存失败");
			return j;
		}
		//单据状态改为录入完毕
		contractMain.setBillStateFlag("A");
		//录入的合同
		contractMain.setContrState("M");
		//新增或编辑表单保存
		contractMainService.save(contractMain);//提交
		//保存后返回ID
		ContractMain contractF=new ContractMain();
		contractF.setBillNum(contractMain.getBillNum());
		List<ContractMain> conList=contractMainService.findList(contractF);
		String id=conList.get(0).getId();
        j.put("id", id);
		j.setMsg("合同录入保存成功");		
		return j;
	}
	*/
	/**
	 * 删除采购合同管理
	 */
	@ResponseBody
	@RequiresPermissions("contractmain:contractMain:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ContractMain contractMain, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		contractMainService.delete(contractMain);
		j.setMsg("删除采购合同管理成功");
		return j;
	}
	
	/**
	 * 批量删除采购合同管理
	 */
	@ResponseBody
	@RequiresPermissions("contractmain:contractMain:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			contractMainService.delete(contractMainService.get(id));
		}
		j.setMsg("删除采购合同管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("contractmain:contractMain:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ContractMain contractMain, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "采购合同管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ContractMain> page = contractMainService.findPage(new Page<ContractMain>(request, response, -1), contractMain);
    		new ExportExcel("采购合同管理", ContractMain.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出采购合同管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public ContractMain detail(String id) {
		return contractMainService.get(id);
	}
	    
	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("contractmain:contractMain:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ContractMain> list = ei.getDataList(ContractMain.class);
			for (ContractMain contractMain : list){
				try{
					contractMainService.save(contractMain);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条采购合同管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条采购合同管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入采购合同管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/contractmain/contractMain/?repage";
    }
	
	/**
	 * 下载导入采购合同管理数据模板
	 */
	@RequiresPermissions("contractmain:contractMain:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "采购合同管理数据导入模板.xlsx";
    		List<ContractMain> list = Lists.newArrayList(); 
    		new ExportExcel("采购合同管理数据", ContractMain.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/contractmain/contractMain/?repage";
    }
	
	/**
	 * input:物料ID(编码)、物料数量、供应商Id
	 * output:物料的单价
	 */
	//按阶梯价格计算方法
/*	 @ResponseBody
	 @RequestMapping(value = "getItemPrice")
	 public AjaxJson getItemPrice(@RequestParam(required=true) String itemId, @RequestParam(required=true) Double itemNum,@RequestParam(required=true) String accountId,@RequestParam(required=true) String condate,HttpServletRequest request, HttpServletResponse response, Model model){
		    AjaxJson j = new AjaxJson();  
		    if(accountId==null||"".equals(accountId)){
		    	j.put("info","请选择供应商！");
			    j.put("sumPrice", 0.0);
		    	return j;
		    }
		    if(condate==null||"".equals(condate)){
		    	j.put("info","请输入签订日期！");
			    j.put("sumPrice", 0.0);
		    	return j;
		    }
		    if(itemId==null||"".equals(itemId)){
			    j.put("info","请选择物料！");
			    j.put("sumPrice", 0.0);
		    	return j;
		    }
		    if(itemNum==null){
		    	j.put("info","请输入签订的物料数量！");
			    j.put("sumPrice", 0.0);
		    	return j;
		    }
		    
			List<PurSupPriceDetail> purSupPriceDetails=new ArrayList<PurSupPriceDetail>();
			//依据数量、时间查询物料总额计算时所需要的阶梯价格(按最小值从小到大排序)
			purSupPriceDetails=contractMainService.getPurSupPriceList(itemId,condate,itemNum,accountId);
			Double restItemNum=itemNum;//剩余未进行总额计算的物料数量
			Double sumPrice=0.0;//物料的总额
			//上条记录的最大值（用于比较i区间的最大值与i+1区间的最小值是否相等判断价格区间是否是连续的）
			Double preMaxQty=0.0;
				for(int i=0;i<purSupPriceDetails.size();i++){//遍历阶梯价格，依据阶梯价格计算总额
					PurSupPriceDetail purSupPriceDetail=purSupPriceDetails.get(i);
					Double minQty=purSupPriceDetail.getMinQty();
					Double maxQty=purSupPriceDetail.getMaxQty();
					
					if(preMaxQty.equals(minQty)){//判断价格区间是否是连续的，不连续则导致计算总额错误
						Double supPrice=purSupPriceDetail.getSupPrice();
						if(i==purSupPriceDetails.size()-1){//最后一个价格区间的物料计算
							if(itemNum>minQty&&itemNum<=maxQty){//物料的数量是否介于最大与最小数量之间（左开右闭）
							    sumPrice=sumPrice+restItemNum*supPrice;  
							}else{
								//j.setMsg("供应商价格信息缺失！");
								j.put("info","lack");
							    j.put("sumPrice", 0.0);
								return j;
							}
						}else{//非最后一个价格区间的总额计算
							Double curQty=maxQty-minQty;
							restItemNum=restItemNum-curQty;
							sumPrice=sumPrice+curQty*supPrice;
						}
					}else{//价格区间不连续
						//返回总额0表示价格数据缺失，总额计算失败
						//j.setMsg("供应商价格信息缺失！");
						j.put("info","lack");
					    j.put("sumPrice", 0.0);
						return j;
					}
					//当前区间的最大数量赋值
					preMaxQty=maxQty;
				}
				if(sumPrice.equals(0.0)){
					j.put("info","lack");				
				}
			    j.put("sumPrice", sumPrice);
		 return j;
	 }
*/
	
	
	 @ResponseBody
	 @RequestMapping(value = "getItemPrice")
	 public AjaxJson getItemPrice(@RequestParam(required=true) String itemId, @RequestParam(required=true) Double itemNum,@RequestParam(required=true) String accountId,@RequestParam(required=true) String condate,HttpServletRequest request, HttpServletResponse response, Model model){
		    AjaxJson j = new AjaxJson();  
		    if(accountId==null||"".equals(accountId)){
		    	j.put("info","请选择供应商！");
			    j.put("sumPrice", 0.0);
		    	return j;
		    }
		    if(condate==null||"".equals(condate)){
		    	j.put("info","请输入签订日期！");
			    j.put("sumPrice", 0.0);
		    	return j;
		    }
		    if(itemId==null||"".equals(itemId)){
			    j.put("info","请选择物料！");
			    j.put("sumPrice", 0.0);
		    	return j;
		    }
		    if(itemNum==null){
		    	j.put("info","请输入签订的物料数量！");
			    j.put("sumPrice", 0.0);
		    	return j;
		    }
		    
			List<PurSupPriceDetail> purSupPriceDetails=new ArrayList<PurSupPriceDetail>();
			//依据数量、时间查询物料总额计算时所需要的阶梯价格(按最小值从小到大排序)
			purSupPriceDetails=contractMainService.getPurSupPriceList(itemId,condate,itemNum,accountId);
//			Double restItemNum=itemNum;//剩余未进行总额计算的物料数量
			Double sumPrice=0.0;//物料的总额
			Double supPrice=0.0;//物料的总额
			//上条记录的最大值（用于比较i区间的最大值与i+1区间的最小值是否相等判断价格区间是否是连续的）
			Double preMaxQty=0.0;
				for(int i=0;i<purSupPriceDetails.size();i++){//遍历阶梯价格，依据阶梯价格计算总额
					PurSupPriceDetail purSupPriceDetail=purSupPriceDetails.get(i);
					Double minQty=purSupPriceDetail.getMinQty();
					Double maxQty=purSupPriceDetail.getMaxQty();
					
				/*	if(preMaxQty.equals(minQty)){//判断价格区间是否是连续的，不连续则导致计算总额错误
						if(itemNum>minQty&&itemNum<=maxQty){//物料的数量是否介于最大与最小数量之间（左开右闭）
							supPrice=purSupPriceDetail.getSupPrice();
							sumPrice=supPrice*itemNum;
							System.out.println("物料数量+++++++++++++++++++++++++++++++"+itemNum);
							System.out.println("物料含税单价+++++++++++++++++++++++++++++++"+supPrice);
							System.out.println("物料含税总额+++++++++++++++++++++++++++++++"+sumPrice);
							break;//结束循环
						}else{
							if(i==purSupPriceDetails.size()-1){//最后一个价格区间的物料计算
								//j.setMsg("供应商价格信息缺失！");
								j.put("info","lack");
							    j.put("sumPrice", 0.0);
								return j;
							}
						}
					}else{//价格区间不连续
						//返回总额0表示价格数据缺失，总额计算失败
						//j.setMsg("供应商价格信息缺失！");
						j.put("info","lack");
					    j.put("sumPrice", 0.0);
						return j;
					}
					//当前区间的最大数量赋值
					preMaxQty=maxQty;
				*/
					if(itemNum>minQty&&itemNum<=maxQty){//物料的数量是否介于最大与最小数量之间（左开右闭）
						supPrice=purSupPriceDetail.getSupPrice();
						sumPrice=supPrice*itemNum;
//						System.out.println("物料数量+++++++++++++++++++++++++++++++"+itemNum);
//						System.out.println("物料含税单价+++++++++++++++++++++++++++++++"+supPrice);
//						System.out.println("物料含税总额+++++++++++++++++++++++++++++++"+sumPrice);
						break;//结束循环
					}else{
						if(i==purSupPriceDetails.size()-1){//最后一个价格区间的物料计算
							//j.setMsg("供应商价格信息缺失！");
							j.put("info","lack");
						    j.put("sumPrice", 0.0);
							return j;
						}
					}
					
				}
				if(sumPrice.equals(0.0)){
					j.put("info","lack");
				}
			    j.put("sumPrice", sumPrice);//物料含税总额
			    j.put("supPrice",supPrice);//物料含税单价
		 return j;
	 }
	
	
     /**
      * excel打印采购合同/订单功能
      
	 @ResponseBody
	 @RequestMapping(value = "getItemPrice")
	*/
	
}