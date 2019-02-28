/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.team.web;

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
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.hqu.modules.basedata.personwork.entity.PersonWork;
import com.hqu.modules.basedata.personwork.service.PersonWorkService;
import com.hqu.modules.basedata.team.entity.Team;
import com.hqu.modules.basedata.team.service.TeamService;

/**
 * 班组维护Controller
 * @author xn
 * @version 2018-04-16
 */
@Controller
@RequestMapping(value = "${adminPath}/team/team")
public class TeamController extends BaseController {

	@Autowired
	private TeamService teamService;
	@Autowired
	private PersonWorkService personWorkService;
	
	@ModelAttribute
	public Team get(@RequestParam(required=false) String id) {
		Team entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = teamService.get(id);
		}
		if (entity == null){
			entity = new Team();
		}
		return entity;
	}
	
	/**
	 * 班组列表页面
	 */
	@RequiresPermissions("team:team:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "basedata/team/teamList";
	}
	
		/**
	 * 班组列表数据
	 */
	@ResponseBody
	@RequiresPermissions("team:team:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Team team, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Team> page = teamService.findPage(new Page<Team>(request, response), team); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑班组表单页面
	 */
	@RequiresPermissions(value={"team:team:view","team:team:add","team:team:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Team team, Model model) {
		model.addAttribute("team", team);
		if(StringUtils.isBlank(team.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "basedata/team/teamForm";
	}

	/**
	 * 保存班组
	 */
	@RequiresPermissions(value={"team:team:add","team:team:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Team team, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, team)){
			return form(team, model);
		}
		//新增或编辑表单保存
		teamService.save(team);//保存
		addMessage(redirectAttributes, "保存班组成功");
		return "redirect:"+Global.getAdminPath()+"/team/team/?repage";
	}
	
	/**
	 * 删除班组
	 */
	@ResponseBody
	@RequiresPermissions("team:team:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Team team, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		teamService.delete(team);
		j.setMsg("删除班组成功");
		return j;
	}
	
	/**
	 * 批量删除班组
	 */
	@ResponseBody
	@RequiresPermissions("team:team:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			teamService.delete(teamService.get(id));
		}
		j.setMsg("删除班组成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("team:team:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Team team, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "班组"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Team> page = teamService.findPage(new Page<Team>(request, response, -1), team);
    		new ExportExcel("班组", Team.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出班组记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public Team detail(String id) {
		return teamService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("team:team:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Team> list = ei.getDataList(Team.class);
			for (Team team : list){
				try{
					teamService.save(team);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条班组记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条班组记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入班组失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/team/team/?repage";
    }
	
	/**
	 * 下载导入班组数据模板
	 */
	@RequiresPermissions("team:team:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "班组数据导入模板.xlsx";
    		List<Team> list = Lists.newArrayList(); 
    		new ExportExcel("班组数据", Team.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/team/team/?repage";
    }
	
	/**
	 * 动态修改人员编码、人员名称、工种代码、工种名称
	 */
	@RequestMapping(value = "changeInfo")
    @ResponseBody
    public PersonWork checkCode(String personWorkId){
        PersonWork personWork = personWorkService.get(personWorkId);
        return personWork;
    }

}