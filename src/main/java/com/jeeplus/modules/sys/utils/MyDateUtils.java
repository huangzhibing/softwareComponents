package com.jeeplus.modules.sys.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.mapper.InvAccountMapper;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.common.utils.number.NumberUtil;
import com.jeeplus.modules.sys.mapper.DataRuleMapper;

public class MyDateUtils {
	//InvAccountMapper invAccountMapper;
	private static InvAccountMapper invAccountMapper = SpringContextHolder.getBean(InvAccountMapper.class);
	
	public static String getMyDate(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(new Date());
	}
	
	public static String getMyTime(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		return simpleDateFormat.format(new Date());
	}
	
	public InvAccount getInvAccountById(String id){
		
		return invAccountMapper.get(id);
		
	}
	
	public int strAddOne(String str){
		return NumberUtil.toInt(str)+1;
	}
	

}
