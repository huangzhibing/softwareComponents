/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tools.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.FileUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.tools.utils.TwoDimensionCode;

/**
 * 二维码Controller
 * @author jeeplus
 * @version 2015-11-30
 */
@Controller
@RequestMapping(value = "${adminPath}/tools/TwoDimensionCodeController")
public class TwoDimensionCodeController extends BaseController {

	@Autowired
	private SystemService systemService;
	/**
	 * 二维码页面
	 */
	@RequestMapping(value = {"index", ""})
	public String index() throws Exception{
		return "modules/tools/qrcode/TwoDimensionCode";
	}
	
	/**
	 *	生成二维码
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="createTwoDimensionCode")
	@ResponseBody
	public AjaxJson createTwoDimensionCode(HttpServletRequest request, String encoderContent){
		AjaxJson j = new AjaxJson();
		Principal principal = (Principal) UserUtils.getPrincipal();
		User user = UserUtils.getUser();
		if (principal == null){
			j.setSuccess(false);
			j.setErrorCode("0");
			j.setMsg("没有登录");
		}
		String realPath = Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL
							+ principal + "/qrcode/";
		FileUtils.createDirectory(realPath);
		String name="test.png"; //encoderImgId此处二维码的图片名
			try {
				String filePath = realPath + name;  //存放路径
				TwoDimensionCode.encoderQRCode(encoderContent, filePath, "png");//执行生成二维码
				user.setQrCode(request.getContextPath()+Global.USERFILES_BASE_URL
						+ principal + "/qrcode/"+name);
				systemService.updateUserInfo(user);
				j.setSuccess(true);
				j.setMsg("二维码生成成功");
				j.put("filePath", request.getContextPath()+Global.USERFILES_BASE_URL
						+ principal + "/qrcode/"+name);
			} catch (Exception e) {
				
			}
		return j;
	}


	/**
	 *	批量生成二维码
	 * @param encoderContent 多个二维码字符串
	 * @throws Exception
	 */
	@RequestMapping(value="createBatchTwoDimensionCode")
	@ResponseBody
	public AjaxJson createBatchTwoDimensionCode(HttpServletRequest request, String encoderContent){
		AjaxJson j = new AjaxJson();
		Principal principal = (Principal) UserUtils.getPrincipal();
		User user = UserUtils.getUser();
		if (principal == null){
			j.setSuccess(false);
			j.setErrorCode("0");
			j.setMsg("没有登录");
		}
		String realPath = Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL
				+ principal + "/qrcode/";
		FileUtils.createDirectory(realPath);
		String[] names = encoderContent.split(",");
		for (int i = 0; i < names.length; i++) {
			String name=names[i]+".png"; //encoderImgId此处二维码的图片名
			try {
				String filePath = realPath + name;  //存放路径
				TwoDimensionCode.encoderQRCode(names[i], filePath, "png");//执行生成二维码
				user.setQrCode(request.getContextPath()+Global.USERFILES_BASE_URL
						+ principal + "/qrcode/"+name);

			} catch (Exception e) {
				logger.debug("二维码生成失败"+name);
			}
		}
		j.setSuccess(true);
		j.setMsg("二维码生成成功");
		j.put("fartherFilePath", request.getContextPath()+Global.USERFILES_BASE_URL
				+ principal + "/qrcode/");
		j.put("names",encoderContent);
		return j;
	}

}