package com.hqu.modules.qualitymanage.eInfoBoard.web;

import com.hqu.modules.qualitymanage.eInfoBoard.entity.eInfoPurStatus;
import com.hqu.modules.qualitymanage.eInfoBoard.service.eInfoPurStatusService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * 质检电子板Controller
 * @author zz
 * @version 2018-11-11
 */
@Controller
@RequestMapping(value = "${adminPath}/eInfoBoard/eInfoPurStatus")
public class eInfoPurStatusController extends BaseController {
    @Autowired
    private eInfoPurStatusService einfoPurStatusService;

    @ModelAttribute
    public eInfoPurStatus get(@RequestParam(required=false) String id) {
        eInfoPurStatus entity = null;
        if (StringUtils.isNotBlank(id)){
            entity = einfoPurStatusService.get(id);
        }
        if (entity == null){
            entity = new eInfoPurStatus();
        }
        return entity;
    }

    /**
     * 质检电子板页面
     */
    //@RequiresPermissions("eInfoBoard:eInfoPurStatus:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "qualitymanage/eInfoBoard/eInfoPurStatusList";
    }

    /**
     * 质检电子板数据
     */
    @ResponseBody
    //@RequiresPermissions("eInfoBoard:eInfoPurStatus:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(eInfoPurStatus einfoPurStatus, HttpServletRequest request, HttpServletResponse response) {
        Page<eInfoPurStatus> page = einfoPurStatusService.findPage(new Page<eInfoPurStatus>(request, response), einfoPurStatus);
        return getBootstrapData(page);
    }
}
