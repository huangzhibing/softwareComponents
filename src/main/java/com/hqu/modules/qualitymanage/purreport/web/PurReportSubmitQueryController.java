/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreport.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.hqu.modules.inventorymanage.invcheckmain.entity.ReinvCheckMain;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckMain;
import com.hqu.modules.qualitymanage.purinvcheckmain.service.InvCheckMainService;
import com.hqu.modules.qualitymanage.purreport.entity.PurReport;
import com.hqu.modules.qualitymanage.purreport.entity.PurReportRSn;
import com.hqu.modules.qualitymanage.purreport.service.InvReportSubmitService;
import com.hqu.modules.qualitymanage.purreport.service.PurReportSubmitService;
import com.hqu.modules.qualitymanage.purreport.service.SfcReportSubmitService;
import com.hqu.modules.qualitymanage.qmreport.entity.QmReport;
import com.hqu.modules.qualitymanage.qmreport.entity.QmReportRSn;
import com.hqu.modules.qualitymanage.qmreport.service.QmReportService;
import com.hqu.modules.workshopmanage.sfcinvcheckmain.entity.SfcInvCheckMain;
import com.hqu.modules.workshopmanage.sfcinvcheckmain.service.SfcInvCheckMainService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.act.service.ActTaskService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
/**
 * 检验单(采购/装配/整机检测)Controller
 * @author 孙映川
 * @version 2018-04-13
 */
@Controller
@RequestMapping(value = "${adminPath}/purreport/purReportSubmitQuery")
public class PurReportSubmitQueryController extends BaseController {

    @Autowired
    private   SfcReportSubmitService sfcReportSubmitService;
    @Autowired
    private   PurReportSubmitService  purReportSubmitService;
    @Autowired
    private   InvCheckMainService  invCheckMainService;
    @Autowired
    private  SfcInvCheckMainService sfcInvCheckMainService;
    @Autowired
    private InvReportSubmitService invReportSubmitService;
    @Autowired
    QmReportService  qmReportService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    protected TaskService taskService;
    @Autowired
    protected HistoryService historyService;
    @Autowired
    protected RepositoryService repositoryService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private ActTaskService actTaskService;

    @ModelAttribute
    public PurReport get(@RequestParam(required=false) String id) {
        PurReport entity = null;
        if (StringUtils.isNotBlank(id)){
            entity = purReportSubmitService.get(id);
        }
        if (entity == null){
            entity = new PurReport();
        }
        return entity;
    }

    /**
     * 检验单列表页面
     */
    @RequiresPermissions("purreport:purReportSubmitQuery:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "qualitymanage/purreportsubmit/purReportSubmitQueryList";
    }
 
    /**
     * 检验单列表数据查询
     */
    @ResponseBody
    @RequiresPermissions("purreport:purReportSubmitQuery:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(PurReport purReport, HttpServletRequest request, HttpServletResponse response, Model model) {
	   	 List<PurReport>	purReportsList=new ArrayList<PurReport>();
        //设置发送状态
    	 purReport.setState("已审核");      
    	 Page<PurReport> page=new Page<PurReport>(request, response);
         purReport.setPage(page);
    	 purReportsList=getResultList(purReport);   	 
         
        String intPage= request.getParameter("pageNo");
        int pageNo=Integer.parseInt(intPage);
        int pageSize= page.getPageSize();
        //不分页
        if(pageNo==1&&pageSize==-1){
	    	page.setList(purReportsList);
	    }else{
	        List<PurReport> reportL=  new ArrayList<PurReport>();
	        for(int i=(pageNo-1)*pageSize;i<purReportsList.size() && i<pageNo*pageSize;i++){
	        	reportL.add(purReportsList.get(i));
		    } 
	        page.setList(reportL);
	        page.setCount(purReportsList.size());
	    }
       return getBootstrapData(page);
    }

    public List<PurReport> getResultList(PurReport purReport){
        if(purReport.getCpersonCode()!=null&&!("".equals(purReport.getCpersonCode()))){
        	 User user=  UserUtils.get(purReport.getCpersonCode());
             purReport.setCpersonCode(user.getNo());
        }
    	List<PurReport> reportList=  new ArrayList<PurReport>();
    	//设置检验名称
        String checktName=purReport.getChecktName();
        if("newcg".equals(checktName)){
            purReport.setChecktName("采购");
            reportList=purReportSubmitService.findListNew(purReport);
          	//删去非合格中未质量处理完的检验单
            reportList=remove(reportList);
        }else if("newzj".equals(checktName)){
        	purReport.setChecktName("整机");
        	reportList=sfcReportSubmitService.findListNew(purReport);
        }else if("newcqfy".equals(checktName)){
         	purReport.setChecktName("超期复验");
        	reportList=invReportSubmitService.findListNew(purReport);
        	//删去非合格中未质量处理完的检验单
            reportList=remove(reportList);
         }else{
        	  purReport.setChecktName("采购");
        	  List<PurReport>	list1=purReportSubmitService.findListNew(purReport);
         	  //删去非合格中未质量处理完的检验单
        	  list1=remove(list1);
        	  reportList.addAll(list1);
        	  purReport.setChecktName("整机");
        	  List<PurReport>	list2=sfcReportSubmitService.findListNew(purReport);
        	  reportList.addAll(list2);
        	  purReport.setChecktName("超期复验");
        	  List<PurReport>	list3=invReportSubmitService.findListNew(purReport);
          	  //删去非合格中未质量处理完的检验单
        	  list3=remove(list3);
        	  reportList.addAll(list3);
        }
    	return reportList;
    }
    
    
	//删去非合格中未质量处理完的检验单
    public List<PurReport> remove(List<PurReport> purReports){
    	 Iterator<PurReport> iterator = purReports.iterator();
     	  //删去非合格中未质量处理完的检验单
         while(iterator.hasNext()){
          	  PurReport integer = iterator.next();
              String checkResult=integer.getCheckResult();
              String isQmatter=integer.getIsQmatter();	
              if(!("合格".equals(checkResult))&&!("2".equals(isQmatter))){//合格部分	
              	iterator.remove();
              }
          }
    	return purReports;   	
    }
    
    /**
     * 查看，增加，编辑检验单表单页面
     */
    @RequiresPermissions("purreport:purReportSubmitQuery:list")

    //@RequiresPermissions(value={"purreport:purReport:view","purreport:purReport:add","purreport:purReport:edit"},logical=Logical.OR)
    @RequestMapping(value = "form")
    public String form(PurReport purReport, Model model) {
       
        String checkName=purReport.getChecktName();
        PurReport report=new PurReport();
        if("采购".equals(checkName)){
            report=purReportSubmitService.get(purReport);
        }else if("整机".equals(checkName)){
        	report=sfcReportSubmitService.get(purReport);
        }else if("超期复验".equals(checkName)){
         	report=invReportSubmitService.get(purReport);
        }
        report.setPurReportRSnList(purReport.getPurReportRSnList());
        model.addAttribute("purReport", report);
        if(StringUtils.isBlank(purReport.getId())){//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "qualitymanage/purreportsubmit/purReportSubmitQueryForm";
    }

	
    @ResponseBody
    @RequestMapping(value = "detail")
    public PurReport detail(String id) {
        return purReportSubmitService.get(id);
    }
    
    /**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("purreport:purReport:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PurReport purReport, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "检验单"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
           // Page<PurReport> page = purReportService.findPage(new Page<PurReport>(request, response, -1), purReport);
            Page<PurReport> page=new Page<PurReport>(request, response);
            purReport.setPage(page);
   	   	    List<PurReport>	purReportsList=new ArrayList<PurReport>();

       	    purReportsList=getResultList(purReport);   	 
            
           String intPage= request.getParameter("pageNo");
           int pageNo=Integer.parseInt(intPage);
           int pageSize= page.getPageSize();
           //不分页
           if(pageNo==1&&pageSize==-1){
   	    	page.setList(purReportsList);
   	    }else{
   	        List<PurReport> reportL=  new ArrayList<PurReport>();
   	        for(int i=(pageNo-1)*pageSize;i<purReportsList.size() && i<pageNo*pageSize;i++){
   	        	reportL.add(purReportsList.get(i));
   		    } 
   	        page.setList(reportL);
   	        page.setCount(purReportsList.size());
   	    }
    		new ExportExcel("检验单", PurReport.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出检验单记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
}
