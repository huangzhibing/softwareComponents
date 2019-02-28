/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreportnewexport.web;

import java.util.ArrayList;
import java.util.Date;
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
import com.hqu.modules.qualitymanage.mattertype.entity.MatterType;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckMain;
import com.hqu.modules.qualitymanage.purreportnew.entity.PurReportNew;
import com.hqu.modules.qualitymanage.purreportnew.entity.Purreportfundetail;
import com.hqu.modules.qualitymanage.purreportnew.entity.Purreportnewsn;
import com.hqu.modules.qualitymanage.purreportnew.entity.Purreportsizedetail;
import com.hqu.modules.qualitymanage.purreportnew.service.PurReportNewService;
import com.hqu.modules.qualitymanage.purreportnewexport.entity.Purreportnewexport;
import com.hqu.modules.qualitymanage.purreportnewexport.service.PurreportnewexportService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;

/**
 * 采购检验IQC导出Controller
 * @author syc
 * @version 2018-11-08
 */
@Controller
@RequestMapping(value = "${adminPath}/purreportnewexport/purreportnewexport")
public class PurreportnewexportController extends BaseController {

	@Autowired
	private PurreportnewexportService purreportnewexportService;
	
	@Autowired
	private PurReportNewService purReportNewService;
	
	@ModelAttribute
	public Purreportnewexport get(@RequestParam(required=false) String id) {
		Purreportnewexport entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = purreportnewexportService.get(id);
		}
		if (entity == null){
			entity = new Purreportnewexport();
		}
		return entity;
	}
	
	/**
	 * 采购检验IQC导出列表页面
	 */
	@RequiresPermissions("purreportnewexport:purreportnewexport:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "qualitymanage/purreportnewexport/purreportnewexportList";
	}
	
	/**
	 * 采购检验IQC导出列表数据
	 */
	@ResponseBody
	@RequiresPermissions("purreportnewexport:purreportnewexport:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Purreportnewexport purreportnewexport,PurReportNew purReportNew, HttpServletRequest request, HttpServletResponse response, Model model) {
		//1.偷梁换柱
		Page<PurReportNew> page = purReportNewService.findPage(new Page<PurReportNew>(request, response), purReportNew); 
		//2.杀鸡取卵
		List<PurReportNew> list = page.getList();
		//3.交换数值
		List<Purreportnewexport> targetList = new ArrayList<>();
		for(PurReportNew temp : list){
			
			temp = purReportNewService.get(temp.getId());
			
			List<Purreportfundetail> purreportfundetailList = temp.getPurreportfundetailList();
			List<Purreportnewsn> purreportnewsnList = temp.getPurreportnewsnList();
			List<Purreportsizedetail> purreportsizedetailList = temp.getPurreportsizedetailList();
			if(purreportfundetailList!=null){
				for(Purreportfundetail A :purreportfundetailList){
					Purreportnewexport exportBean = new Purreportnewexport();
					//获取明细（物料名称）
					String itemName = temp.getItemName();
					exportBean.setItemName(itemName);
					//获取物料类别
					String itemSpecmodel = temp.getItemSpecmodel();
					exportBean.setItemSpecmodel(itemSpecmodel);
					//获取检验时间
					Date d = temp.getCheckDate();
					String checkDate = null;
					if(d!=null){
						checkDate = d.toString();
					}
					
					exportBean.setCheckDate(checkDate);
					//获取供应商
					String supname1 = temp.getSupname1();
					exportBean.setSupname(supname1);
					//获取进料单号
					InvCheckMain inv = temp.getInv();
					String billnum =null;
					if(null!=inv){
						billnum = inv.getBillnum();
					}
					exportBean.setBillNum(billnum);
					//获取检验人
					String name = temp.getUser().getName();
					exportBean.setCheckPerson(name);
					
					//获取检验单号
					String reportId = temp.getReportId();
					exportBean.setReportId(reportId);
					
					//获取良品数量
					double itemCount = temp.getItemCount();
					int sum1 = 0;
					if(A.getSum()!=null){
						sum1 = A.getSum();
					}
					
					double goodNum = itemCount-sum1;
					if(goodNum<0){
						goodNum = 0;
					}
					exportBean.setGoodNum(goodNum+"");
					 
					//不良品数量
					
					exportBean.setBadNum(sum1+"");
					
					//设置良率
					exportBean.setRate(""+(goodNum/temp.getItemCount()));
					if(temp.getItemCount()==0){
						exportBean.setRate("0");
					}
					//设置不良明细
					MatterType matterType = A.getMatterType();
					String matterdes = null;
					if(matterType!=null){
						matterdes = matterType.getMattername();
					}
					exportBean.setBadDetail(matterdes);
					//设置项目
					exportBean.setItemNameDetail("功能检测");
					targetList.add(exportBean);
				}
			}
			
			System.out.println();
			
			if(purreportnewsnList!=null){
				for(Purreportnewsn A :purreportnewsnList){
					Purreportnewexport exportBean = new Purreportnewexport();
					//获取明细（物料名称）
					String itemName = temp.getItemName();
					exportBean.setItemName(itemName);
					//获取物料类别
					String itemSpecmodel = temp.getItemSpecmodel();
					exportBean.setItemSpecmodel(itemSpecmodel);
					//获取检验时间
					Date d = temp.getCheckDate();
					String checkDate = null;
					if(d!=null){
						checkDate = d.toString();
					}
					
					exportBean.setCheckDate(checkDate);
					//获取供应商
					String supname1 = temp.getSupname1();
					exportBean.setSupname(supname1);
					//获取检验单号
					String reportId = temp.getReportId();
					exportBean.setReportId(reportId);
					
					//获取进料单号
					InvCheckMain inv = temp.getInv();
					String billnum =null;
					if(null!=inv){
						billnum = inv.getBillnum();
					}
					exportBean.setBillNum(billnum);
					//获取检验人
					String name = temp.getUser().getName();
					exportBean.setCheckPerson(name);
					
					//获取良品数量
					double itemCount = temp.getItemCount();
					int sum1 = 0;
					if(A.getSum()!=null){
						sum1 = A.getSum();
					}
					
					double goodNum = itemCount-sum1;
					if(goodNum<0){
						goodNum = 0;
					}
					exportBean.setGoodNum(goodNum+"");
					 
					//不良品数量
					exportBean.setBadNum(sum1+"");
					
					//设置良率
					exportBean.setRate(""+(goodNum/temp.getItemCount()));
					if(temp.getItemCount()==0){
						exportBean.setRate("0");
					}
					//设置不良明细
					MatterType matterType = A.getMatterType();
					String matterdes = null;
					if(matterType!=null){
						matterdes = matterType.getMattername();
					}
					exportBean.setBadDetail(matterdes);
					//设置项目
					exportBean.setItemNameDetail("外观检测");
					targetList.add(exportBean);
				}
			}
			
			System.out.println();
			if(purreportsizedetailList!=null){
				for(Purreportsizedetail A :purreportsizedetailList){
					Purreportnewexport exportBean = new Purreportnewexport();
					//获取明细（物料名称）
					String itemName = temp.getItemName();
					exportBean.setItemName(itemName);
					//获取物料类别
					String itemSpecmodel = temp.getItemSpecmodel();
					exportBean.setItemSpecmodel(itemSpecmodel);
					//获取检验时间
					Date d = temp.getCheckDate();
					String checkDate = null;
					if(d!=null){
						checkDate = d.toString();
					}
					
					exportBean.setCheckDate(checkDate);
					
					//获取检验单号
					String reportId = temp.getReportId();
					exportBean.setReportId(reportId);
					
					//获取供应商
					String supname1 = temp.getSupname1();
					exportBean.setSupname(supname1);
					//获取进料单号
					InvCheckMain inv = temp.getInv();
					String billnum =null;
					if(null!=inv){
						billnum = inv.getBillnum();
					}
					exportBean.setBillNum(billnum);
					//获取检验人
					String name = temp.getUser().getName();
					exportBean.setCheckPerson(name);
					
					//获取良品数量
					double itemCount = temp.getItemCount();
					int sum1 = 0;
					if(A.getSum()!=null){
						sum1 = A.getSum();
					}
					
					double goodNum = itemCount-sum1;
					if(goodNum<0){
						goodNum = 0;
					}
					exportBean.setGoodNum(goodNum+"");
					 
					//不良品数量
					exportBean.setBadNum(sum1+"");
					
					//设置良率
					exportBean.setRate(""+(goodNum/temp.getItemCount()));
					if(temp.getItemCount()==0){
						exportBean.setRate("0");
					}
					
					//设置不良明细
					MatterType matterType = A.getMatterType();
					String matterdes = null;
					if(matterType!=null){
						matterdes = matterType.getMattername();
					}
					exportBean.setBadDetail(matterdes);
					//设置项目
					exportBean.setItemNameDetail("尺寸检测");
					targetList.add(exportBean);
				}
			}
			
			
			
			System.out.println();
			
			
			
		}
		
		
		Page<Purreportnewexport> page1 = purreportnewexportService.findPage(new Page<Purreportnewexport>(request, response), purreportnewexport); 
		page1.setList(targetList);
		
		return getBootstrapData(page1);
	}

	/**
	 * 查看，增加，编辑采购检验IQC导出表单页面
	 */
	@RequiresPermissions(value={"purreportnewexport:purreportnewexport:view","purreportnewexport:purreportnewexport:add","purreportnewexport:purreportnewexport:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Purreportnewexport purreportnewexport, Model model) {
		model.addAttribute("purreportnewexport", purreportnewexport);
		if(StringUtils.isBlank(purreportnewexport.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/purreportnewexport/purreportnewexportForm";
	}

	/**
	 * 保存采购检验IQC导出
	 */
	@RequiresPermissions(value={"purreportnewexport:purreportnewexport:add","purreportnewexport:purreportnewexport:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Purreportnewexport purreportnewexport, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, purreportnewexport)){
			return form(purreportnewexport, model);
		}
		//新增或编辑表单保存
		purreportnewexportService.save(purreportnewexport);//保存
		addMessage(redirectAttributes, "保存采购检验IQC导出成功");
		return "redirect:"+Global.getAdminPath()+"/purreportnewexport/purreportnewexport/?repage";
	}
	
	/**
	 * 删除采购检验IQC导出
	 */
	@ResponseBody
	@RequiresPermissions("purreportnewexport:purreportnewexport:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Purreportnewexport purreportnewexport, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		purreportnewexportService.delete(purreportnewexport);
		j.setMsg("删除采购检验IQC导出成功");
		return j;
	}
	
	/**
	 * 批量删除采购检验IQC导出
	 */
	@ResponseBody
	@RequiresPermissions("purreportnewexport:purreportnewexport:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			purreportnewexportService.delete(purreportnewexportService.get(id));
		}
		j.setMsg("删除采购检验IQC导出成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("purreportnewexport:purreportnewexport:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Purreportnewexport purreportnewexport, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "采购检验IQC导出"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Purreportnewexport> page = purreportnewexportService.findPage(new Page<Purreportnewexport>(request, response, -1), purreportnewexport);
    		new ExportExcel("采购检验IQC导出", Purreportnewexport.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出采购检验IQC导出记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("purreportnewexport:purreportnewexport:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Purreportnewexport> list = ei.getDataList(Purreportnewexport.class);
			for (Purreportnewexport purreportnewexport : list){
				try{
					purreportnewexportService.save(purreportnewexport);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条采购检验IQC导出记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条采购检验IQC导出记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入采购检验IQC导出失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purreportnewexport/purreportnewexport/?repage";
    }
	
	/**
	 * 下载导入采购检验IQC导出数据模板
	 */
	@RequiresPermissions("purreportnewexport:purreportnewexport:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "采购检验IQC导出数据导入模板.xlsx";
    		List<Purreportnewexport> list = Lists.newArrayList(); 
    		new ExportExcel("采购检验IQC导出数据", Purreportnewexport.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purreportnewexport/purreportnewexport/?repage";
    }

}