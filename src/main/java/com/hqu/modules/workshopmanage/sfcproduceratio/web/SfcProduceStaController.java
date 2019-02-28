/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.sfcproduceratio.web;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.*;
import com.github.abel533.echarts.data.PointData;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.style.ItemStyle;
import com.github.abel533.echarts.style.LineStyle;
import com.github.abel533.echarts.style.itemstyle.Normal;
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
import com.hqu.modules.workshopmanage.sfcproduceratio.entity.SfcProduceSta;
import com.hqu.modules.workshopmanage.sfcproduceratio.service.SfcProduceStaService;

import static java.lang.Thread.sleep;

/**
 * 车间产出量统计Controller
 * @author yxb
 * @version 2018-11-25
 */
@Controller
@RequestMapping(value = "${adminPath}/sfcproduceratio/sfcProduceSta")
public class SfcProduceStaController extends BaseController {

	@Autowired
	private SfcProduceStaService sfcProduceStaService;

	private static volatile List<SfcProduceSta> refreshChartList;
	
	@ModelAttribute
	public SfcProduceSta get(@RequestParam(required=false) String id) {
		SfcProduceSta entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sfcProduceStaService.get(id);
		}
		if (entity == null){
			entity = new SfcProduceSta();
		}
		return entity;
	}
	
	/**
	 * 车间产出量统计列表页面
	 */
	@RequiresPermissions("sfcproduceratio:sfcProduceSta:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "workshopmanage/sfcproduceratio/sfcProduceStaList";
	}
	
		/**
	 * 车间产出量统计列表数据
	 */
	@ResponseBody
	@RequiresPermissions("sfcproduceratio:sfcProduceSta:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(SfcProduceSta sfcProduceSta, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SfcProduceSta> page = sfcProduceStaService.findPage(new Page<SfcProduceSta>(request, response), sfcProduceSta);
		refreshChartList=page.getList();
		return getBootstrapData(page);
	}



	@ResponseBody
	@RequestMapping("topBarOption")
	public GsonOption getTopBarOption(){
		int len=0;
		while(refreshChartList==null){
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(refreshChartList!=null){
			len=refreshChartList.size();
		}
		String[] cLabels= new String[len];
		Integer[] actValues=new Integer[len];
		Integer[] planValues=new Integer[len];
		Integer[] badValues=new Integer[len];
		//PointData[] pointData=new PointData[2];
		int maxPlanNum=0;
		int maxActualNum=0;
		for(int i=0;i<len;i++){
			cLabels[i]=refreshChartList.get(i).getNo();
			actValues[i]=refreshChartList.get(i).getActualNum();
			planValues[i]=refreshChartList.get(i).getPlanNum();
			badValues[i]=refreshChartList.get(i).getFailNum();
			if(refreshChartList.get(i).getPlanNum()>maxPlanNum) {
				maxPlanNum = refreshChartList.get(i).getPlanNum();
			}
			if(refreshChartList.get(i).getActualNum()>maxActualNum) {
				maxActualNum = refreshChartList.get(i).getActualNum();
			}
			//pointData[i]=new PointData().text(cLabels[i]).setyAxis(planValues[i]).setValue(planValues[i]).setxAxis(140*i).symbol(Symbol.none);

		}

		GsonOption option = new GsonOption();
		option.title().text("各工位的产出量统计").subtext("当前一个小时内");
		option.tooltip().trigger(Trigger.axis);
		option.calculable(true);
		option.legend("产出量", "异常量");
		option.grid().borderWidth(0).y(60).y2(60);
		option.toolbox().show(true).feature(Tool.mark, Tool.dataView, new MagicType(Magic.line, Magic.bar).show(true), Tool.restore, Tool.saveAsImage);
		option.xAxis(new CategoryAxis().data(cLabels));
		if(maxActualNum<maxPlanNum) {  //当实际产量超过标准产量时，柱状图Y轴自适应
			option.yAxis(new ValueAxis().max(maxPlanNum + 2));
		}else {
			option.yAxis(new ValueAxis());
		}

		Bar bar = new Bar("产出量");
		bar.data(actValues);

		//bar.markPoint().data(new PointData().type(MarkType.max).name("最大值"), new PointData().type(MarkType.min).name("最小值"));

		bar.markLine().data(new PointData().setSymbol(Symbol.arrow).setItemStyle(new ItemStyle().normal(new Normal().lineStyle(new LineStyle().type(LineType.solid)))).setyAxis(maxPlanNum).setName("标准产出"));

		Bar bar2 = new Bar("异常量");
		//List<Double> list = Arrays.asList(2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3);
		//bar2.setData(list);
		bar2.data(badValues);
		//bar2.markPoint().data(new PointData("年最高", 182.2).xAxis(7).yAxis(183).symbolSize(18), new PointData("年最低", 2.3).xAxis(11).yAxis(3));
		//bar2.markLine().data(new PointData().type(MarkType.average).name("平均值"));

		option.series(bar, bar2);


		return option;
	}






	/**
	 * 查看，增加，编辑车间产出量统计表单页面
	 */
	@RequiresPermissions(value={"sfcproduceratio:sfcProduceSta:view","sfcproduceratio:sfcProduceSta:add","sfcproduceratio:sfcProduceSta:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SfcProduceSta sfcProduceSta, Model model) {
		model.addAttribute("sfcProduceSta", sfcProduceSta);
		if(StringUtils.isBlank(sfcProduceSta.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "workshopmanage/sfcproduceratio/sfcProduceStaForm";
	}

	/**
	 * 保存车间产出量统计
	 */
	@RequiresPermissions(value={"sfcproduceratio:sfcProduceSta:add","sfcproduceratio:sfcProduceSta:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(SfcProduceSta sfcProduceSta, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, sfcProduceSta)){
			return form(sfcProduceSta, model);
		}
		//新增或编辑表单保存
		sfcProduceStaService.save(sfcProduceSta);//保存
		addMessage(redirectAttributes, "保存车间产出量统计成功");
		return "redirect:"+Global.getAdminPath()+"/sfcproduceratio/sfcProduceSta/?repage";
	}
	
	/**
	 * 删除车间产出量统计
	 */
	@ResponseBody
	@RequiresPermissions("sfcproduceratio:sfcProduceSta:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(SfcProduceSta sfcProduceSta, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		sfcProduceStaService.delete(sfcProduceSta);
		j.setMsg("删除车间产出量统计成功");
		return j;
	}
	
	/**
	 * 批量删除车间产出量统计
	 */
	@ResponseBody
	@RequiresPermissions("sfcproduceratio:sfcProduceSta:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			sfcProduceStaService.delete(sfcProduceStaService.get(id));
		}
		j.setMsg("删除车间产出量统计成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("sfcproduceratio:sfcProduceSta:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(SfcProduceSta sfcProduceSta, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "车间产出量统计"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SfcProduceSta> page = sfcProduceStaService.findPage(new Page<SfcProduceSta>(request, response, -1), sfcProduceSta);
    		new ExportExcel("车间产出量统计", SfcProduceSta.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出车间产出量统计记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("sfcproduceratio:sfcProduceSta:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SfcProduceSta> list = ei.getDataList(SfcProduceSta.class);
			for (SfcProduceSta sfcProduceSta : list){
				try{
					sfcProduceStaService.save(sfcProduceSta);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条车间产出量统计记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条车间产出量统计记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入车间产出量统计失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sfcproduceratio/sfcProduceSta/?repage";
    }
	
	/**
	 * 下载导入车间产出量统计数据模板
	 */
	@RequiresPermissions("sfcproduceratio:sfcProduceSta:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "车间产出量统计数据导入模板.xlsx";
    		List<SfcProduceSta> list = Lists.newArrayList(); 
    		new ExportExcel("车间产出量统计数据", SfcProduceSta.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sfcproduceratio/sfcProduceSta/?repage";
    }

}