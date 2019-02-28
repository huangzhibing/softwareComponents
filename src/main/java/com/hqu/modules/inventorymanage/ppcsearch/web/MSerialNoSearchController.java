/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.ppcsearch.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.inventorymanage.ppcsearch.entity.MSerialNoSearch;
import com.hqu.modules.inventorymanage.ppcsearch.service.MSerialNoSearchService;
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
import com.hqu.modules.workshopmanage.ppc.entity.MSerialNo;
import com.hqu.modules.workshopmanage.ppc.service.MSerialNoService;

/**
 * 编码关联表Controller
 * @author yxb
 * @version 2018-10-31
 */
@Controller
@RequestMapping(value = "${adminPath}/mpsplan/mSerialNoSearch")
public class MSerialNoSearchController extends BaseController {

	@Autowired
	private MSerialNoSearchService mSerialNoService;
	
	@ModelAttribute
	public MSerialNoSearch get(@RequestParam(required=false) String id) {
		MSerialNoSearch entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mSerialNoService.get(id);
		}
		if (entity == null){
			entity = new MSerialNoSearch();
		}
		return entity;
	}
	
	/**
	 * 编码关联表列表页面
	 */
	@RequiresPermissions("mpsplan:mSerialNo:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "inventorymanage/mserialnosearch/mSerialNoSearch";
	}
	
		/**
	 * 编码关联表列表数据
	 */
	@ResponseBody
//	@RequiresPermissions("mpsplan:mSerialNo:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(MSerialNoSearch mSerialNoSearch, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MSerialNoSearch> page = mSerialNoService.findPage(new Page<MSerialNoSearch>(request, response), mSerialNoSearch);
		return getBootstrapData(page);
	}

}