/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.comexport.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.hqu.modules.qualitymanage.comexport.entity.Comexport;
import com.hqu.modules.qualitymanage.comexport.service.ComexportService;
import com.hqu.modules.qualitymanage.purreport.entity.PurReport;
import com.hqu.modules.qualitymanage.purreport.entity.PurReportRSn;
import com.hqu.modules.qualitymanage.purreport.mapper.PurReportMapper;
import com.hqu.modules.qualitymanage.purreport.mapper.PurReportRSnMapper;
import com.hqu.modules.qualitymanage.purreport.service.PurReportService;
import com.hqu.modules.qualitymanage.qmreport.entity.QmReportRSn;
import com.hqu.modules.qualitymanage.qmreport.mapper.QmReportRSnMapper;
import com.hqu.modules.qualitymanage.verifyqcnorm.entity.VerifyQCNorm;
import com.hqu.modules.qualitymanage.verifyqcnorm.service.VerifyQCNormService;
import com.hqu.modules.workshopmanage.processroutinedetail.entity.ProcessRoutineDetail;
import com.hqu.modules.workshopmanage.processroutinedetail.service.ProcessRoutineDetailService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;

/**
 * 功能老化激活安规导出Controller
 * @author syc
 * @version 2018-11-09
 */
@Controller
@RequestMapping(value = "${adminPath}/comexport/comexport")
public class ComexportController extends BaseController {
	
	@Autowired
	private PurReportRSnMapper purReportRSnMapper;

	@Autowired
	private PurReportMapper purReportMapper;
	
	@Autowired
	private ComexportService comexportService;
	
	@Autowired
	private VerifyQCNormService verifyQCNormService;
	
	@Autowired
	private QmReportRSnMapper qmReportRSnMapper;
	
	@Autowired
	private ProcessRoutineDetailService processRoutineDetailService;
	
	@Autowired
	private PurReportService purReportService;
	
	@ModelAttribute
	public Comexport get(@RequestParam(required=false) String id) {
		Comexport entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = comexportService.get(id);
		}
		if (entity == null){
			entity = new Comexport();
		}
		return entity;
	}
	
	/**
	 * 功能老化激活安规导出列表页面
	 */
	@RequiresPermissions("comexport:comexport:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "qualitymanage/comexport/comexportList";
	}
	
	/**
	 * 功能老化激活安规导出列表数据
	 */
	
	
	@ResponseBody
	@RequiresPermissions("comexport:comexport:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Comexport comexport, HttpServletRequest request, HttpServletResponse response, Model model) {
		//找到所有整机的数据
		PurReport purreport = new PurReport();
		purreport.setChecktName("整机");
		List<PurReport> findList = purReportMapper.findList(purreport);
		//根据beginDate 和 endDate 过滤list
		Date beginDate = comexport.getBeginDate();
		Date endDate = comexport.getEndDate();
		
		
		
		if(beginDate!=null&&endDate!=null){
			long beginTime = beginDate.getTime();
			long endTime = endDate.getTime();
			List<PurReport> tempList = new ArrayList<>();
			for(PurReport temp : findList){
				Date checkDate = temp.getCheckDate();
				long time = checkDate.getTime();
				if(time>=beginTime&&time<=endTime){
					tempList.add(temp);
				}
			}
			findList = tempList;
		}
		
		
		//获取安规、功能老化、激活的明细
		String aDetail ="";
		String gDetail ="";
		String jDetail ="";
		
		Map<String,String> aDetailMap = new HashMap<String,String>();
		Map<String,String> gDetailMap = new HashMap<String,String>();
		Map<String,String> jDetailMap = new HashMap<String,String>();
		
		VerifyQCNorm verifyQCNorm = new VerifyQCNorm();
		List<VerifyQCNorm> qcnList = verifyQCNormService.findList(verifyQCNorm);
		for(VerifyQCNorm temp :qcnList){
			String objName = temp.getObjName();
			String checkprj = temp.getCheckprj();
			if("安规".equals(checkprj)){
				aDetailMap.put(objName, "");
			}
			if("功能老化".equals(checkprj)){
				gDetailMap.put(objName, "");
			}
			if("激活".equals(checkprj)){
				jDetailMap.put(objName, "");
			}
		}
		for(String temp :aDetailMap.keySet()){
			aDetail = aDetail+temp+";";
		}
		for(String temp :gDetailMap.keySet()){
			gDetail = gDetail+temp+";";
		}
		for(String temp :jDetailMap.keySet()){
			jDetail = jDetail+temp+";";
		}
		
		List<Comexport> resultList = new ArrayList<>();
		//遍历每一台整机，每一台整机产生3个bean，分别是安规、激活和老化
		for(PurReport temp : findList){
			Comexport abean = new Comexport();
			Comexport jbean = new Comexport();
			Comexport lbean = new Comexport();
			temp = purReportService.get(temp.getId());
			if(temp.getPurReportRSnList()==null||temp.getPurReportRSnList().size()==0){continue;}
			PurReportRSn purReportRSn = temp.getPurReportRSnList().get(0);
			abean.setItemname("安规");
			jbean.setItemname("激活");
			lbean.setItemname("功能老化");
			
			abean.setDetail(aDetail);
			jbean.setDetail(gDetail);
			lbean.setDetail(jDetail);
			
			
			//objsn ->机台序列码20位
			ProcessRoutineDetail processRoutineDetail = new ProcessRoutineDetail();
			processRoutineDetail.setObjSn(purReportRSn.getObjSn());
			List<ProcessRoutineDetail> findList2 = processRoutineDetailService.findList(processRoutineDetail);
			if(findList2==null||findList2.size()==0){continue;}
			String mserialno = findList2.get(0).getMserialno();
			
			//徐汉川mapper没改完，我这边查不到数据
			
			abean.setCentercode(mserialno);
			jbean.setCentercode(mserialno);
			lbean.setCentercode(mserialno);
			
			abean.setChecktime(temp.getCheckDate().toString());
			jbean.setChecktime(temp.getCheckDate().toString());
			lbean.setChecktime(temp.getCheckDate().toString());
			
			abean.setCheckperson(temp.getCheckPerson());
			jbean.setCheckperson(temp.getCheckPerson());
			lbean.setCheckperson(temp.getCheckPerson());
			
			abean.setIsGood(isGood(purReportRSn.getObjSn(),"安规"));
			jbean.setIsGood(isGood(purReportRSn.getObjSn(),"功能老化"));
			lbean.setIsGood(isGood(purReportRSn.getObjSn(),"激活"));
			
			abean.setBaddetail(getBadDetail(purReportRSn.getObjSn(),"安规"));
			jbean.setBaddetail(getBadDetail(purReportRSn.getObjSn(),"功能老化"));
			lbean.setBaddetail(getBadDetail(purReportRSn.getObjSn(),"激活"));
			
			
			resultList.add(abean);
			resultList.add(jbean);
			resultList.add(lbean);
		}
		
		
		List<Comexport> resultListnew = new ArrayList<>();
		String itemname = comexport.getItemname();
		if(!itemname.equals("")){
			for(Comexport temp:resultList){
				if(temp.getItemname().equals(itemname)){
					resultListnew.add(temp);
				}
			}
			resultList = resultListnew;
		}
		
		
		
		Page<Comexport> page = comexportService.findPage(new Page<Comexport>(request, response), comexport); 
		page.setList(resultList);
		page.setPageSize(resultList.size());
		return getBootstrapData(page);
	}
	
	
	/*
	 * 根据40位的编号查询该整机是否合格
	 */
	public String isGood(String objSn,String item){
		QmReportRSn qmReportRSn = new QmReportRSn();
		qmReportRSn.setObjSn(objSn);
		qmReportRSn.setMatterType(item);
		List<QmReportRSn> findList2 = qmReportRSnMapper.findList(qmReportRSn);
		if(findList2.size()==0){
			return "合格";
		}
		return "不合格";
	}
	
	public String getBadDetail(String objSn,String item){
		QmReportRSn qmReportRSn = new QmReportRSn();
		qmReportRSn.setObjSn(objSn);
		qmReportRSn.setMatterType(item);
		String result= "";
		List<QmReportRSn> findList2 = qmReportRSnMapper.findList(qmReportRSn);
		for(QmReportRSn temp:findList2){
			String matterName = temp.getMatterName();
			result=result+ matterName + ";";
		}
		return result;
	}
	
	

	private PurReport getPurReportFromMCode(String mserialno) {
		
		
		PurReportRSn purReportRSn = new PurReportRSn();
		purReportRSn.setObjSn(mserialno);
		List<PurReportRSn> findList2 = purReportRSnMapper.findList(purReportRSn);
		PurReportRSn purReportRSn2 = findList2.get(0);
		String id = purReportRSn2.getPurReport().getId();
		//获取主表关联
		PurReport purReport = purReportMapper.get(id);
		return purReport;
	}
	
	/*
	@ResponseBody
	@RequiresPermissions("comexport:comexport:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Comexport comexport, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		//获取安规、功能老化、激活的明细
		String aDetail ="";
		String gDetail ="";
		String jDetail ="";
		
		Map<String,String> aDetailMap = new HashMap<String,String>();
		Map<String,String> gDetailMap = new HashMap<String,String>();
		Map<String,String> jDetailMap = new HashMap<String,String>();
		
		VerifyQCNorm verifyQCNorm = new VerifyQCNorm();
		List<VerifyQCNorm> qcnList = verifyQCNormService.findList(verifyQCNorm);
		for(VerifyQCNorm temp :qcnList){
			String objName = temp.getObjName();
			String checkprj = temp.getCheckprj();
			if("安规".equals(checkprj)){
				aDetailMap.put(objName, "");
			}
			if("功能老化".equals(checkprj)){
				gDetailMap.put(objName, "");
			}
			if("激活".equals(checkprj)){
				jDetailMap.put(objName, "");
			}
		}
		for(String temp :aDetailMap.keySet()){
			aDetail = aDetail+temp+";";
		}
		for(String temp :gDetailMap.keySet()){
			gDetail = gDetail+temp+";";
		}
		for(String temp :jDetailMap.keySet()){
			jDetail = jDetail+temp+";";
		}
		
		
		
		List<ProcessRoutineDetail> findList = processRoutineDetailService.findList(new ProcessRoutineDetail());
		Map<String,List<ProcessRoutineDetail>> map = new HashMap<String,List<ProcessRoutineDetail>>();
		for(ProcessRoutineDetail temp:findList){
			WorkCenter centercode = temp.getCentercode();
			String centerName = centercode.getCenterCode();
			List<ProcessRoutineDetail> list = map.get(centerName);
			if(list ==null){
				list = new ArrayList<ProcessRoutineDetail>();
			}
			list.add(temp);
			map.put(centerName, list);
		}
		List<Comexport> resultList = new ArrayList<>();
		for(String centercode :map.keySet()){
			
			List<ProcessRoutineDetail> list = map.get(centercode);
			ProcessRoutineDetail processRoutineDetail = list.get(0);
			
			String mserialno = processRoutineDetail.getMserialno();
			//机器码（整机的码）
			PurReport purReport = getPurReportFromMCode(mserialno);
			//获取测试时间 测试人
			String checkTime = purReport.getCheckTime();
			String checkPerson = purReport.getCheckPerson();
			
			//一次循环，分别计算出安规 老化 激活的不良品数量和相应的不良现象明细
			String abadDeatil = "";
			String lbadDeatil = "";
			String jbadDeatil = "";
			
			Map<String,String> abadDetailMap = new HashMap<String,String>();
			Map<String,String> gbadDetailMap = new HashMap<String,String>();
			Map<String,String> jbadDetailMap = new HashMap<String,String>();
			
			Map<String,Integer> acountMap = new HashMap<String,Integer>();
			Map<String,Integer> lcountMap = new HashMap<String,Integer>();
			Map<String,Integer> jcountMap = new HashMap<String,Integer>();
			for(ProcessRoutineDetail processRoutineDetail1:list){
				String matchCode =processRoutineDetail1.getMserialno();//40位的码
				QmReportRSn qmReportRSn = new QmReportRSn();
				qmReportRSn.setObjSn(matchCode);
				List<QmReportRSn> findList2 = qmReportRSnMapper.findList(qmReportRSn);
				for(QmReportRSn qmReportRSn1 : findList2){
					String matterType = qmReportRSn1.getMatterType();
					if("安规".equals(matterType)){
						abadDetailMap.put(qmReportRSn1.getMatterName(), "");
						//abadDeatil = abadDeatil+qmReportRSn1.getMatterName()+";";
						acountMap.put(matchCode, 1);
					}
					if("功能老化".equals(matterType)){
						gbadDetailMap.put(qmReportRSn1.getMatterName(), "");
						//lbadDeatil =lbadDeatil+qmReportRSn1.getMatterName()+";";
						lcountMap.put(matchCode, 1);
					}
					if("激活".equals(matterType)){
						jbadDetailMap.put(qmReportRSn1.getMatterName(), "");
						//jbadDeatil = jbadDeatil+qmReportRSn1.getMatterName()+";";
						jcountMap.put(matchCode, 1);
					}
				}
				
			}
			for(String t:abadDetailMap.keySet()){
				abadDeatil = abadDeatil+t+";";
			}
			
			for(String t:gbadDetailMap.keySet()){
				lbadDeatil = lbadDeatil+t+";";
			}
			for(String t:jbadDetailMap.keySet()){
				jbadDeatil = jbadDeatil+t+";";
			}
			
			
			int agoodnum = list.size()-acountMap.size();
			int lgoodnum = list.size()-lcountMap.size();
			int jgoodnum = list.size()-jcountMap.size();
			
			String arate = (agoodnum*1.0 / list.size())+"";
			String lrate = (lgoodnum*1.0 / list.size())+"";
			String jrate = (jgoodnum*1.0 / list.size())+"";
			
			Comexport comexport2 = new Comexport("安规",aDetail,centercode,checkTime,checkPerson,agoodnum+"",(acountMap.size())+"",arate,abadDeatil);
			Comexport comexport3 = new Comexport("老化",gDetail,centercode,checkTime,checkPerson,lgoodnum+"",(lcountMap.size())+"",lrate,lbadDeatil);
			Comexport comexport4 = new Comexport("激活",jDetail,centercode,checkTime,checkPerson,jgoodnum+"",(jcountMap.size())+"",jrate,jbadDeatil);
			resultList.add(comexport2);
			resultList.add(comexport3);
			resultList.add(comexport4);
			
		}
		
		Page<Comexport> page = comexportService.findPage(new Page<Comexport>(request, response), comexport); 
		page.setList(resultList);
		page.setPageSize(resultList.size());
		return getBootstrapData(page);
	}
	*/


	/**
	 * 查看，增加，编辑功能老化激活安规导出表单页面
	 */
	@RequiresPermissions(value={"comexport:comexport:view","comexport:comexport:add","comexport:comexport:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Comexport comexport, Model model) {
		model.addAttribute("comexport", comexport);
		if(StringUtils.isBlank(comexport.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/comexport/comexportForm";
	}

	/**
	 * 保存功能老化激活安规导出
	 */
	@RequiresPermissions(value={"comexport:comexport:add","comexport:comexport:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Comexport comexport, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, comexport)){
			return form(comexport, model);
		}
		//新增或编辑表单保存
		comexportService.save(comexport);//保存
		addMessage(redirectAttributes, "保存功能老化激活安规导出成功");
		return "redirect:"+Global.getAdminPath()+"/comexport/comexport/?repage";
	}
	
	/**
	 * 删除功能老化激活安规导出
	 */
	@ResponseBody
	@RequiresPermissions("comexport:comexport:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Comexport comexport, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		comexportService.delete(comexport);
		j.setMsg("删除功能老化激活安规导出成功");
		return j;
	}
	
	/**
	 * 批量删除功能老化激活安规导出
	 */
	@ResponseBody
	@RequiresPermissions("comexport:comexport:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			comexportService.delete(comexportService.get(id));
		}
		j.setMsg("删除功能老化激活安规导出成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("comexport:comexport:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Comexport comexport, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "功能老化激活安规导出"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Comexport> page = comexportService.findPage(new Page<Comexport>(request, response, -1), comexport);
    		new ExportExcel("功能老化激活安规导出", Comexport.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出功能老化激活安规导出记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("comexport:comexport:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Comexport> list = ei.getDataList(Comexport.class);
			for (Comexport comexport : list){
				try{
					comexportService.save(comexport);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条功能老化激活安规导出记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条功能老化激活安规导出记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入功能老化激活安规导出失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/comexport/comexport/?repage";
    }
	
	/**
	 * 下载导入功能老化激活安规导出数据模板
	 */
	@RequiresPermissions("comexport:comexport:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "功能老化激活安规导出数据导入模板.xlsx";
    		List<Comexport> list = Lists.newArrayList(); 
    		new ExportExcel("功能老化激活安规导出数据", Comexport.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/comexport/comexport/?repage";
    }

}