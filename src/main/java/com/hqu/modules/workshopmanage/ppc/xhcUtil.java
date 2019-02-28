package com.hqu.modules.workshopmanage.ppc;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

import com.hqu.modules.workshopmanage.ppc.entity.MpsPlan;
import com.hqu.modules.workshopmanage.ppc.mapper.MpsPlanMapper;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.time.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hqu.modules.workshopmanage.ppc.entity.MrpPlan;
import com.hqu.modules.workshopmanage.ppc.mapper.MrpPlanMapper;
import com.hqu.modules.workshopmanage.ppc.xhcUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.time.DateUtil;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.workshopmanage.ppc.entity.MpsPlan;

import com.hqu.modules.workshopmanage.ppc.mapper.MpsPlanMapper;
import com.jeeplus.modules.sys.utils.UserUtils;


@Service
@Transactional(readOnly = true)

public class xhcUtil extends CrudService<MpsPlanMapper, MpsPlan> {

	@Autowired
	private MpsPlanMapper mpsPlanMapper;
	/**
	 * 为了方便书写SQL语句，在select语句中的各字段之间增加分隔符，以便于使用concat()函数
	 * 
	 * @param toBeSplitted
	 * @param splitter
	 * @return
	 */
	public static String addSplitter(String toBeSplitted, String splitter) {
		String splittedStr = "";
		if (toBeSplitted == null || toBeSplitted.length() == 0)
			return splittedStr;
		
		
		// 在每个列名字之后增加 ",'分隔符',"
		String[] tmpStr = toBeSplitted.split(",");
		if(tmpStr.length==1)  //没有分隔符，返回自身
			return toBeSplitted;
		
		for (String s : tmpStr) {
			s=s.trim();
			splittedStr = splittedStr + "(case when concat(" + s +",'')='' or "+ s +" is null then '***' else " + s +" end),'" + splitter + "',";
			//logger.debug(splittedStr);

		}
		// logger.debug(splittedStr);
		// 去掉最后添加的内容
		splittedStr = splittedStr.substring(0, splittedStr.length() - 4 - splitter.length());
		//logger.debug(splittedStr);
		return splittedStr;

	}

	/**
	 * 将select语句返回的用分隔符分割的字符串结果，拆分成字符串数组返回
	 * 
	 * @param
	 * @return
	 */
	public static List<Map<String, String>> resultToList(List<Object> input, String attributesStr, String splitter) {
		List<Map<String, String>> list = new ArrayList<>(); // 保存返回值
		String[] attributes = attributesStr.split(","); // 列的名字
		for (Object o : input) { // 处理每一行的数据
			//logger.debug(o);
			String[] values = o.toString().split(splitter); // 当前行的数据，根据分隔符拆分为String
			
			Map<String, String> map = new HashMap<>();
			for (int i = 0; i < attributes.length; i++) {
				if (values[i].trim().equals("***")){
					map.put(attributes[i].trim(), "");
				}else
					map.put(attributes[i].trim(), values[i].trim());
			}
			list.add(map);
		}
		return list;
	}

	/**
	 * 通过物料code找到id
	 *
	 * 
	 * @param code
	 *            mdm_item中的code
	 * @param caller
	 *            调用此方法的对象
	 * @return 对应的id
	 * 
	 */
	public static String getIdByCode(String code, CrudService caller) {

		String selectSql = "SELECT distinct id from mdm_item where code='" + code + "'";
		//System.out.println(selectSql);
		List returnList = caller.executeSelectSql(selectSql);

		if (returnList != null && returnList.size() > 0) {
			if (returnList.get(0).toString() != "") {
				return returnList.get(0).toString();
			}
		}
		return "";
	}

	/**
	 * 通过物料ID找到code
	 * 
	 * @param id
	 *            mdm_item中的id
	 * @param caller
	 *            调用此方法的对象
	 * @return 对应的物料编码
	 */
	public static String getCodeById(String id, CrudService caller) {

		String selectSql = "SELECT distinct code from mdm_item where id='" + id + "'";
		//System.out.println(selectSql);
		List returnList = caller.executeSelectSql(selectSql);

		if (returnList != null && returnList.size() > 0) {
			if (returnList.get(0).toString() != "") {
				return returnList.get(0).toString();
			}
		}
		return "";
	}

	public static Date newDate(String strDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date d  = null;
		//这里会有一个异常，所以要用try catch捕获异常
		try {
			d  = sdf.parse(strDate);
		}catch (Exception e){
			e.printStackTrace();
		}
		return d;
	}

	/**
	 * 计算指定日期是每年的第几周。
	 * 每年的第一周按照第一个星期一开始计算
	 * @return 四位年二位周号
	 */
	public static String getWeekofYearXHC(Date date){
		//现有DateUtil.getWeekOfYear()方法是只有有一天在某年，即认为某周在某年中，不符合要求，需要减1
		Date firstDayofYear= DateUtil.beginOfYear(date);  //一年的第一天
		DecimalFormat df=new DecimalFormat("00");  //周的输出格式2位

		//1.如果date所在年的第1天是周一，则直接调用DateUtil.getWeekOfYear()方法的结果。
		//返回日期所在年+2位数字；
		if (DateUtil.getDayOfWeek(firstDayofYear)==1){ //一年的第一天是周一
			return String.format("%tY",date)+ df.format(DateUtil.getWeekOfYear(date));
		}else if (DateUtil.getWeekOfYear(date)>1){
			//2.如果date所在年的第1天不是周一，且date不是第一周，则返回DateUtil.getWeekOfYear()-1
			return String.format("%tY",date)+ df.format(DateUtil.getWeekOfYear(date)-1);
		}else{
			//3. 返回上年最后一周的值

			Date lastDayofPreviousYear= DateUtil.subDays(firstDayofYear,1);
			//System.out.println("  dd  " + DateUtil.getWeekOfYear(lastDayofPreviousYear));
			return getWeekofYearXHC(lastDayofPreviousYear);
		}

	}

	/**
	 * 产生每年每周的编号和每周周一和周日日期
	 */
	public void generateWeeks(){
		//2000年到3000年的
		int weekno=1;
		Date tmpDate=new Date();
		String insertSQL="";
		for(int year=2000;year<=3000;year++){
			weekno=1;
			Date firstDayofYear= newDate(year + "-01-01");  //得到year的第1天日期
			if (DateUtil.getDayOfWeek(firstDayofYear)==1){
				tmpDate=firstDayofYear;
			}else
				tmpDate=DateUtil.addDays(firstDayofYear, 7 - DateUtil.getDayOfWeek(firstDayofYear) + 1);

			for (; ; ) {
				insertSQL = "insert into ppc_week("
						+ "year,"
						+ "weekno,"
						+ "monday,"
						+ "sunday)"

						+ " values('"
						+ year + "',"
						+ weekno + ",'"
						+ tmpDate.toString() + "','"
						+ DateUtil.addDays(tmpDate, 6) + "')";
				logger.debug(" week insert" + insertSQL);
				super.mapper.execInsertSql(insertSQL);

				weekno++;
				tmpDate = DateUtil.addDays(tmpDate, 7);

				String curYear=String.format("%tY", tmpDate);
				if (Integer.valueOf(curYear)!=year)
					break;
			}
		}
	}

	/*打印输出*/
	public static void println(String s){
		System.out.println(s);
	}

	public static void main(String[] args) {
		Date tmpDate= newDate("2019-01-01");

		System.out.println(tmpDate.toString());
		System.out.println(DateUtil.getWeekOfYear(tmpDate));

		System.out.println(getWeekofYearXHC(tmpDate));





	}

}
