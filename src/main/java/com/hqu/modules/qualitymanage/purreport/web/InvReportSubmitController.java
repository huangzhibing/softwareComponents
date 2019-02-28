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

import com.hqu.modules.inventorymanage.newinvcheckmain.entity.NewinvCheckMain;
import com.hqu.modules.inventorymanage.newinvcheckmain.service.NewinvCheckMainService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.hqu.modules.inventorymanage.invcheckmain.entity.ReinvCheckMain;
import com.hqu.modules.inventorymanage.invcheckmain.service.ReinvCheckMainService;
import com.hqu.modules.purchasemanage.linkman.entity.Paccount;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckMain;
import com.hqu.modules.qualitymanage.purinvcheckmain.service.InvCheckMainService;
import com.hqu.modules.qualitymanage.purreport.entity.PurReport;
import com.hqu.modules.qualitymanage.purreport.entity.PurReportRSn;
import com.hqu.modules.qualitymanage.purreport.service.InvReportSubmitService;
import com.hqu.modules.qualitymanage.purreport.service.PurReportSubmitService;
import com.hqu.modules.qualitymanage.qmreport.entity.QmReport;
import com.hqu.modules.qualitymanage.qmreport.entity.QmReportRSn;
import com.hqu.modules.qualitymanage.qmreport.service.QmReportService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.StringUtils;
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
@RequestMapping(value = "${adminPath}/purreport/invReportSubmit")
public class InvReportSubmitController extends BaseController {

    @Autowired
    private InvReportSubmitService purReportService;
    @Autowired
	private NewinvCheckMainService newinvCheckMainService;
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
            entity = purReportService.get(id);
        }
        if (entity == null){
            entity = new PurReport();
        }
        return entity;
    }

    /**
     * 检验单列表页面
     */
    @RequiresPermissions("purreport:invReportSubmit:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "qualitymanage/purreportsubmit/invReportSubmitList";
    }

    /**
     * 检验单列表数据查询
     */
	/*
	@ResponseBody
	@RequiresPermissions("purreport:purReportSubmit:list")
	@RequestMapping(value = "search")
	public Map<String, Object> dataSearch(PurReport purReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		purReport.setState("未审核");
		User user=UserUtils.getUser();
		purReport.setCpersonCode(user.getNo());
		String checktName=purReport.getChecktName();

		System.out.println("++++++++++++++++++++++++++++++++++checktName"+checktName);

       if("cg".equals(checktName)){
        	purReport.setChecktName("采购");
        }

		Page<PurReport> page = purReportService.findPage(new Page<PurReport>(request, response), purReport);
		return getBootstrapData(page);
	}
*/
    /**
     * 检验单列表数据加载
     */
    @ResponseBody
    @RequiresPermissions("purreport:invReportSubmit:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(PurReport purReport, HttpServletRequest request, HttpServletResponse response, Model model) {
        purReport.setState("已审核");
    	//purReport.setJustifyResult("通过");
        //用户代码
        User user=UserUtils.getUser();
        purReport.setCpersonCode(user.getNo());
        String checktName=purReport.getChecktName();
        if("cqfy".equals(checktName)){
            purReport.setChecktName("超期复验");
        }
        //获取采购的数据
        purReport.setChecktName("超期复验");
        //获取所有满足条件的PurReport（已审核、当前用户、查询条件）       
        List<PurReport>	purReports=new ArrayList<PurReport>();
        Page<PurReport> page=new Page<PurReport>(request, response);
        purReport.setPage(page);
        //未发送筛选
        String sendState=purReport.getSendState();   	
        if(sendState==null||"N".equals(sendState)||"".equals(sendState)){  
        	purReport.setSendState("N");
        	purReports=purReportService.findListNew(purReport);
            /*
            Iterator<PurReport> iterator = purReports.iterator();
            while(iterator.hasNext()){
            	PurReport integer = iterator.next();
                String checkResult=integer.getCheckResult();
                String isQmatter=integer.getIsQmatter();	
                if(!("合格".equals(checkResult))&&!("2".equals(isQmatter))){//合格部分	
                	iterator.remove();
                }
            }
            */
         //查找已发送的超期检单
    	}else if("Y".equals(sendState)){
        	purReports=purReportService.findListNew(purReport);
    	}
    	
        /**
         * 查询结果分页显示
         */
        String intPage= request.getParameter("pageNo");
        int pageNo=Integer.parseInt(intPage);
        int pageSize= page.getPageSize();
        //不分页
        if(pageNo==1&&pageSize==-1){
	    	page.setList(purReports);
	    }else{
	    //分页显示
	    	List<PurReport> reportL=  new ArrayList<PurReport>();
	        for(int i=(pageNo-1)*pageSize;i<purReports.size() && i<pageNo*pageSize;i++){
	             reportL.add(purReports.get(i));	       
		      } 
	        page.setList(reportL);
	        page.setCount(purReports.size()); 
	    }
        //得到已审核且合格的检验单或者已审核的、非合格的、已完成质量报告处理的检验单（均未发送过通知的）
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑检验单表单页面
     */
    @RequiresPermissions("purreport:invReportSubmit:list")
//
    //@RequiresPermissions(value={"purreport:purReport:view","purreport:purReport:add","purreport:purReport:edit"},logical=Logical.OR)
    @RequestMapping(value = "form")
    public String form(PurReport purReport, Model model) {

        model.addAttribute("purReport", purReport);
        String invCheckMainID= purReport.getReinvCheckMain().getId();
    //    NewinvCheckMain newinvCheckMain=new NewinvCheckMain();
    //    newinvCheckMain.setId(invCheckMainID);
        NewinvCheckMain reinvCheckMain= newinvCheckMainService.get(invCheckMainID);
        //已发送的单据，不显示提交按钮
        if("已质检".equals(reinvCheckMain.getQmsFlag()))
        	purReport.setSendState("1");
        if(StringUtils.isBlank(purReport.getId())){//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "qualitymanage/purreportsubmit/invReportSubmitForm";
    }

	/*
	@RequiresPermissions(value={"purreport:purReportSubmit:view","purreport:purReportSubmit:list"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PurReport purReport, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, purReport)){
			return form(purReport, model);
		}
				//purReportService.save(purReport);//保存
		invCheckMainService.submitPurReort(purReport);
		addMessage(redirectAttributes, "检验单发送成功");
		return "redirect:"+Global.getAdminPath()+"/purreportsubmit/purReportSubmit/?repage";
	}
	*/


    @ResponseBody
    @RequestMapping(value = "detail")
    public PurReport detail(String id) {
        return purReportService.get(id);
    }
    /**
     * 发送检验单
     */

    //  @RequestMapping(value = "updateqmsflag")
    @RequestMapping(value = "save")
    public String save(PurReport purReport, Model model, RedirectAttributes redirectAttributes) throws Exception{
        String invCheckMainID= purReport.getReinvCheckMain().getId();
     //   ReinvCheckMain reinvCheckMain=new ReinvCheckMain();
    //    reinvCheckMain.setId(invCheckMainID);
        NewinvCheckMain reinvCheckMain=newinvCheckMainService.get(invCheckMainID);
        if(reinvCheckMain==null){
            addMessage(redirectAttributes, "超期复验单尚未创建");
        }else{
        	reinvCheckMain.setQmsFlag("已质检");
            newinvCheckMainService.save(reinvCheckMain);
            addMessage(redirectAttributes, "超期复验单发送成功");
        }


        // 提交流程任务
        //	Map<String, Object> vars = Maps.newHashMap();
        //	List<Task> list = taskService.createTaskQuery().processInstanceId(pi.getId()).list();
		/*
		ProcessInstance pi = runtimeService.startProcessInstanceById(purReport.getAct().getProcDefId());;
		List<org.activiti.engine.task.Task> list = taskService.createTaskQuery().processInstanceId(pi.getId()).list();

		for(org.activiti.engine.task.Task temp:list){
			taskService.complete(temp.getId());
		}
		*/
        return "redirect:"+Global.getAdminPath()+"/purreport/invReportSubmit/?repage";
    }
}
