/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.mserialnoprint.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.qualitymanage.purreport.entity.PurReportRSn;
import com.hqu.modules.qualitymanage.purreport.mapper.PurReportRSnMapper;
import com.hqu.modules.qualitymanage.purreport.service.PurReportService;
import com.hqu.modules.qualitymanage.qmreport.entity.QmReport;
import com.hqu.modules.qualitymanage.qmreport.entity.QmReportRSn;
import com.hqu.modules.qualitymanage.qmreport.service.QmReportService;
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
import com.hqu.modules.workshopmanage.mserialnoprint.entity.MSerialNoPrint;
import com.hqu.modules.workshopmanage.mserialnoprint.service.MSerialNoPrintService;
import com.hqu.modules.qualitymanage.purreport.entity.PurReport;

import java.util.Iterator;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;

import com.hqu.modules.inventorymanage.invcheckmain.service.ReinvCheckMainService;
import com.hqu.modules.inventorymanage.newinvcheckmain.service.NewinvCheckMainService;
import com.hqu.modules.qualitymanage.objectdef.service.ObjectDefService;
import com.hqu.modules.qualitymanage.purinvcheckmain.service.InvCheckMainService;
import com.hqu.modules.qualitymanage.purreport.mapper.PurReportMapper;
import com.hqu.modules.qualitymanage.qualitynorm.entity.QualityNorm;
import com.hqu.modules.qualitymanage.qualitynorm.mapper.QualityNormMapper;
import com.hqu.modules.qualitymanage.verifyqcnorm.entity.VerifyQCNorm;
import com.hqu.modules.qualitymanage.verifyqcnorm.service.VerifyQCNormService;
import com.hqu.modules.workshopmanage.processroutinedetail.service.ProcessRoutineDetailService;
import com.hqu.modules.workshopmanage.sfcinvcheckmain.service.SfcInvCheckMainService;
import com.jeeplus.modules.act.service.ActTaskService;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.service.SystemService;

/**
 * 机器序列号打印Controller
 * @author yxb
 * @version 2019-01-10
 */
@Controller
@RequestMapping(value = "${adminPath}/mserialnoprint/mSerialNoPrint")
public class MSerialNoPrintController extends BaseController {


	@Autowired
	private PurReportRSnMapper purReportRSnMapper;
	@Autowired
	private MSerialNoPrintService mSerialNoPrintService;
	@Autowired
	private QmReportService qmReportService;
	@Autowired
	private PurReportService purReportService;
	@Autowired
	private ObjectDefService objectDefService;

	@Autowired
	private ReinvCheckMainService reinvCheckMainService;

	@Autowired
	private NewinvCheckMainService newinvCheckMainService;

	@Autowired
	private ProcessRoutineDetailService processRoutineDetailService;


	@Autowired
	private OfficeService officeService;



	@Autowired
	private SystemService systemService;
	//	@Autowired
//	private QualityNormService qualitynormService;
	@Autowired
	private QualityNormMapper qualityNormMapper;
	@Autowired
	private InvCheckMainService invCheckMainService;
	@Autowired
	private PurReportMapper purReportMapper;

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

	@Autowired
	private SfcInvCheckMainService sfcInvCheckMainService;

	@Autowired
	private VerifyQCNormService verifyQCNormService;


	@ModelAttribute
	public MSerialNoPrint get(@RequestParam(required=false) String id) {
		MSerialNoPrint entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mSerialNoPrintService.get(id);
		}
		if (entity == null){
			entity = new MSerialNoPrint();
		}
		return entity;
	}

    /**
     * 整机综合查询列表页面
     */
    @RequiresPermissions("mserialnoprint:mSerialNoPrint:machinelist")
    @RequestMapping(value = "machinelist")
    public String machinelist(Model model) {
        MSerialNoPrint mSerialNoPrint = new MSerialNoPrint();
        model.addAttribute("mSerialNoPrint",mSerialNoPrint);
        return "workshopmanage/mserialnoprintSTAT/mSerialNoPrintList";
    }


	/**
	 * 机器二维码打印列表页面
	 */
	@RequiresPermissions("mserialnoprint:mSerialNoPrint:list")
	@RequestMapping(value = "objlist")
	public String objlist(Model model) {
		MSerialNoPrint mSerialNoPrint = new MSerialNoPrint();
		model.addAttribute("mSerialNoPrint",mSerialNoPrint);
		return "workshopmanage/mserialnoprint/objSnPrintList";
	}

	/**
	 * 机器序列号打印列表页面
	 */
	@RequiresPermissions("mserialnoprint:mSerialNoPrint:list")
	@RequestMapping(value = {"list", ""})
	public String list(Model model) {
		MSerialNoPrint mSerialNoPrint = new MSerialNoPrint();
		model.addAttribute("mSerialNoPrint",mSerialNoPrint);
		return "workshopmanage/mserialnoprint/mSerialNoPrintList";
	}
	
		/**
	 * 机器序列号打印列表数据
	 */
	@ResponseBody
	@RequiresPermissions("mserialnoprint:mSerialNoPrint:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(MSerialNoPrint mSerialNoPrint, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MSerialNoPrint> page = mSerialNoPrintService.findPage(new Page<MSerialNoPrint>(request, response), mSerialNoPrint); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑机器序列号打印表单页面
	 */
	@RequiresPermissions(value={"mserialnoprint:mSerialNoPrint:view","mserialnoprint:mSerialNoPrint:add","mserialnoprint:mSerialNoPrint:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(MSerialNoPrint mSerialNoPrint, Model model) {
		model.addAttribute("mSerialNoPrint", mSerialNoPrint);
		if(StringUtils.isBlank(mSerialNoPrint.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "workshopmanage/mserialnoprint/mSerialNoPrintForm";
	}

	/**
	 * 保存机器序列号打印
	 */
	@RequiresPermissions(value={"mserialnoprint:mSerialNoPrint:add","mserialnoprint:mSerialNoPrint:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(MSerialNoPrint mSerialNoPrint, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, mSerialNoPrint)){
			return form(mSerialNoPrint, model);
		}
		//新增或编辑表单保存
		mSerialNoPrintService.save(mSerialNoPrint);//保存
		addMessage(redirectAttributes, "保存机器序列号打印成功");
		return "redirect:"+Global.getAdminPath()+"/mserialnoprint/mSerialNoPrint/?repage";
	}
	
	/**
	 * 删除机器序列号打印
	 */
	@ResponseBody
	@RequiresPermissions("mserialnoprint:mSerialNoPrint:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(MSerialNoPrint mSerialNoPrint, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		mSerialNoPrintService.delete(mSerialNoPrint);
		j.setMsg("删除机器序列号打印成功");
		return j;
	}
	
	/**
	 * 批量删除机器序列号打印
	 */
	@ResponseBody
	@RequiresPermissions("mserialnoprint:mSerialNoPrint:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			mSerialNoPrintService.delete(mSerialNoPrintService.get(id));
		}
		j.setMsg("删除机器序列号打印成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("mserialnoprint:mSerialNoPrint:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(MSerialNoPrint mSerialNoPrint, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "机器序列号打印"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<MSerialNoPrint> page = mSerialNoPrintService.findPage(new Page<MSerialNoPrint>(request, response, -1), mSerialNoPrint);
    		new ExportExcel("机器序列号打印", MSerialNoPrint.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出机器序列号打印记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("mserialnoprint:mSerialNoPrint:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<MSerialNoPrint> list = ei.getDataList(MSerialNoPrint.class);
			for (MSerialNoPrint mSerialNoPrint : list){
				try{
					mSerialNoPrintService.save(mSerialNoPrint);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条机器序列号打印记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条机器序列号打印记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入机器序列号打印失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mserialnoprint/mSerialNoPrint/?repage";
    }
	
	/**
	 * 下载导入机器序列号打印数据模板
	 */
	@RequiresPermissions("mserialnoprint:mSerialNoPrint:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "机器序列号打印数据导入模板.xlsx";
    		List<MSerialNoPrint> list = Lists.newArrayList(); 
    		new ExportExcel("机器序列号打印数据", MSerialNoPrint.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mserialnoprint/mSerialNoPrint/?repage";
    }


	@RequestMapping(value = "formPZ")
	public String formPZ(MSerialNoPrint mSerialNoPrint, Model model) {

		model.addAttribute("mCode",mSerialNoPrint.getMserialno());
		return "workshopmanage/mserialnoprintSTAT/qmReportMachineList";
	}


	@ResponseBody
	@RequestMapping(value = "formPZdata")
	public Map<String, Object> formPZdata(String mCode,MSerialNoPrint mSerialNoPrint, Model model,HttpServletRequest request, HttpServletResponse response) {
		List<QmReport> resultList = new ArrayList();
		//mcode 为机台码
		String mcode = mCode;
		List<QmReport> qmReportList = qmReportService.findList(new QmReport());
		List<QmReport> qmReportList1 = new ArrayList<>();
		for(QmReport temp:qmReportList){
			qmReportList1.add(qmReportService.get(temp.getId()));
		}

		for(QmReport temp:qmReportList1){
			List<QmReportRSn> qmReportRSnList = temp.getQmReportRSnList();
			for (QmReportRSn qmReportRSn:qmReportRSnList){
				String reportId = qmReportRSn.getReportId();
				if(mcode.equals(reportId)){
					resultList.add(temp);
				}
			}

		}


		QmReport qmReport = new QmReport();
		qmReport.setQmType("整机");
		qmReport.setState("编辑中");
		Page<QmReport> page = qmReportService.findPage(new Page<QmReport>(request, response), qmReport);
		//page.setList(resultList);
		page.setPageSize(resultList.size());
		return getBootstrapData(page);
	}



	@RequestMapping(value = "formJY")
	public String formJY(MSerialNoPrint mSerialNoPrint, Model model)throws Exception {
		//机台码
		String mCode = mSerialNoPrint.getMserialno();
		//通过机台码获取整机检验单PurReport  purReportService
		List<PurReport> purReportList1 = new ArrayList<>();
		PurReport purReport = new PurReport();
		purReport.setChecktName("整机");
		List<PurReport> purReportList = purReportService.findList(purReport);
		for(PurReport temp : purReportList){
			purReportList1.add(purReportService.get(temp.getId()));
		}
		List<PurReport> resultList = new ArrayList<>();
		for(PurReport temp : purReportList1){
			List<PurReportRSn> purReportRSnList = temp.getPurReportRSnList();
			for(PurReportRSn purReportRSn :purReportRSnList){
				String mserialno = purReportRSn.getMserialno();
				if(mCode.equals(mserialno)){
					resultList.add(temp);
				}
			}
		}
		//在resultList中找日期最近的shijian
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long t = 0;
		purReport = new PurReport();
		for(PurReport temp : resultList){
			Date checkDate = temp.getCheckDate();
			String format = sdf.format(checkDate);
			String checkTime = temp.getCheckTime();
			String timeStr = format +" "+checkTime;
			Date parse = sdf2.parse(timeStr);
			long time = parse.getTime();
			if(time>t){
				t=time;
				purReport = temp ;
			}

		}




		//检验执行标准名称
		List<QualityNorm> findList = qualityNormMapper.findList(new QualityNorm());
		List<String> list = new ArrayList<String>();
		Iterator<QualityNorm> it = findList.iterator();
		while(it.hasNext()){
			QualityNorm qualityNorm = it.next();
			//		String qNormName = qualityNorm.getQNormName();
			//		list.add(qNormName);
		}
		//purReport.setQualityNormList(findList);
		model.addAttribute("qualityNormList", findList);
		String[] qualityNormArr = list.toArray(new String[0]);
		//	purReport.setQualityNormArr(qualityNormArr);

		//获取所有角色名称数组
		List<Role> allRole = systemService.findAllRole();
		List<String> roleList = new ArrayList<String>();
		//Iterator<Role> it1 = allRole.iterator();
//		while(it1.hasNext()){
//			Role role = it1.next();
//			roleList.add(role.getName());
//		}
//		String[] roleNameArr = roleList.toArray(new String[0]);
		//purReport.setRoleName(roleNameArr);

		model.addAttribute("purReport", purReport);

		if(StringUtils.isBlank(purReport.getId())){//如果ID是空为添加
			//创建日期对象
			Date date = new Date();
			purReport.setReportId(purReportService.getReportID());
			//获取时分秒
			String checktime = date.getHours()+"时"+date.getMinutes()+"分"+date.getSeconds()+"秒";
			purReport.setCheckTime(checktime);
			//purReport.setCheckDate(date);

		}

		////////////////////////////////////////////////////////////////////////////////////////////////////

		//整机追溯时，插入purReport子表
		//根据purReport_id 查询
		String reportId = purReport.getReportId();

		//purReport = purReportService.get(purReport.getId());
		List<PurReportRSn> list1 = purReportRSnMapper.findList(new PurReportRSn(purReport));
		purReport.setPurReportRSnList(list1);

		List<PurReportRSn> purReportRSnList = purReport.getPurReportRSnList();
		String mserialno = purReportRSnList.get(0).getMserialno();
//verifyQCNormService
		VerifyQCNorm verifyQCNorm = new VerifyQCNorm();
		verifyQCNorm.setReportId(reportId);
		List<VerifyQCNorm> verifyQCNormList = verifyQCNormService.findList(verifyQCNorm);

		//追溯时安规检验列表
		List<VerifyQCNorm> aList = new ArrayList<>();
		//追溯时老化检验列表
		List<VerifyQCNorm> lList = new ArrayList<>();
		//追溯时激活检验列表
		List<VerifyQCNorm> jList = new ArrayList<>();
		//追溯时包装检验列表
		List<VerifyQCNorm> bList = new ArrayList<>();

		//分组插入
		for(VerifyQCNorm verifyqcnorm : verifyQCNormList){

			verifyqcnorm.setMserialno(mserialno);
			String prjName = verifyqcnorm.getCheckprj();
			if("安规".equals(prjName)){
				aList.add(verifyqcnorm);
			}
			if("激活".equals(prjName)){
				jList.add(verifyqcnorm);
			}
			if("包装".equals(prjName)){
				bList.add(verifyqcnorm);
			}
			if("功能老化".equals(prjName)){
				lList.add(verifyqcnorm);
			}

			String isfisished = verifyqcnorm.getIsfisished();
			if(" , , ".equals(isfisished)){
				verifyqcnorm.setIsfisished("未检验");
			}
			if("0, , ".equals(isfisished)){
				verifyqcnorm.setIsfisished("正在检验");
			}
			if("1,0, ".equals(isfisished)){
				verifyqcnorm.setIsfisished("检验合格");
			}
			if("2, , ".equals(isfisished)){
				verifyqcnorm.setIsfisished("检验完成");
			}
			if("1,1,0".equals(isfisished)){
				verifyqcnorm.setIsfisished("检验不合格");
			}
		}

		purReport.setaList(aList);
		purReport.setbList(bList);
		purReport.setlList(lList);
		purReport.setjList(jList);


		model.addAttribute("purReport", purReport);
		Model isAdd = model.addAttribute("isAdd", true);


		return "workshopmanage/mserialnoprintSTAT/purReportFormDetailComCheck";
	}


}