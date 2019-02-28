package com.hqu.modules.Common.web;

import com.hqu.modules.Common.service.CommonService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping(value = "${adminPath}/common")
public class CommonController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommonService commonService;

    @RequestMapping(value = "chkCode")
    @ResponseBody
    public String checkCode(String tableName,String fieldName,String fieldValue){
        Boolean exist = commonService.getCodeNum(tableName,fieldName,fieldValue);
        if(exist){
            logger.debug("已存在");
            return "false";
        } else {
            logger.debug("未存在");
            return "true";
        }
    }
    
    @RequestMapping(value = "chkCodeAndName")
    @ResponseBody
    public String checkCode(String tableName,String fieldName,String fieldValue,String fieldName2,String fieldValue2){
        Boolean exist = commonService.getCodeNum(tableName,fieldName,fieldValue);
        Boolean exist2 = commonService.getCodeNum(tableName,fieldName2,fieldValue2);
        if(exist){
            logger.debug("编码已存在");
            return "false";
        } else if(exist2){
        	logger.debug("名称已存在");
            return "false2";
        } else{
        	logger.debug("编码、名称未存在");
            return "true";
        }
        
    }
}
