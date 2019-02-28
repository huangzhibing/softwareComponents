/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.warehousemaintenance.creatcount.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.service.ItemService;
import com.hqu.modules.basedata.unit.entity.Unit;
import com.hqu.modules.basedata.unit.service.UnitService;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billmain.service.BillMainService;
import com.hqu.modules.inventorymanage.bin.service.BinService;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
import com.hqu.modules.inventorymanage.location.service.LocationService;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.hqu.modules.inventorymanage.warehouse.service.WareHouseService;
import com.hqu.modules.inventorymanage.warehousemaintenance.countdata.entity.CountDetail;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
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
import com.hqu.modules.inventorymanage.warehousemaintenance.countdata.entity.CountMain;
import com.hqu.modules.inventorymanage.warehousemaintenance.countdata.service.CountMainService;

/**
 * 盘点数据录入Controller
 * @author zb
 * @version 2018-04-14
 */
@Controller
@RequestMapping(value = "${adminPath}/creatcount/creatCount")
public class CreatCountController extends BaseController {

	@Autowired
	private CountMainService countMainService;
	@Autowired
	private BillMainService billMainService;
	@Autowired
	private WareHouseService wareHouseService;
	@Autowired
	private BinService binService;
    @Autowired
	private LocationService locationService;
    @Autowired
    private InvAccountService invAccountService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UnitService unitService;

	@ModelAttribute
	public CountMain get(@RequestParam(required=false) String id) {
		CountMain entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = countMainService.get(id);
		}
		if (entity == null){
			entity = new CountMain();
		}
		return entity;
	}

	/**
	 * 盘点数据录入列表页面
	 */
	@RequiresPermissions("countdata:countMain:list")
	@RequestMapping(value = {"list", ""})
	public String list(CountMain countMain,Model model) {
	    Date date = new Date();
		countMain.setCheckDate(date);
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyyMMdd" );
        logger.debug(sdf.format(date));
        String newBillNum = countMainService.getMaxIdByTypeAndDate("PDD"+sdf.format(date));
        if(StringUtils.isEmpty(newBillNum)){
            newBillNum = "0000";
        } else {
            newBillNum = String.format("%04d", Integer.parseInt(newBillNum.substring(11,15))+1);
        }
		countMain.setBillNum("PDD" +  sdf.format(date) + newBillNum);
	    return "inventorymanage/warehousemaintenance/creatcount/countMainForm";
	}

		/**
	 * 盘点数据录入列表数据
	 */
	/*@ResponseBody
	@RequiresPermissions("countdata:countMainlist")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CountMain countMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CountMain> page = countMainService.findPage(new Page<CountMain>(request, response), countMain);
		return getBootstrapData(page);
	}*/

	/**
	 * 查看，增加，编辑盘点数据录入表单页面
	 */
	@RequiresPermissions("countdata:countMain:add")
	@RequestMapping(value = "form")
	public String form(CountMain countMain, Model model) {
		model.addAttribute("countMain", countMain);
		if(StringUtils.isBlank(countMain.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "inventorymanage/warehousemaintenance/creatcount/countMainForm";
	}

	/**
	 * 保存盘点数据录入
	 */
	@RequiresPermissions(value={"countdata:countMain:add"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(CountMain countMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
        countMain.setCheckEmpId(UserUtils.getUser());
		if (!beanValidator(model, countMain)){
			return form(countMain, model);
		}
		//新增或编辑表单保存
        InvAccount invAccount = new InvAccount();
        WareHouse w = wareHouseService.get(countMain.getWare().getId());
		String adminMode = w.getAdminMode();
		invAccount.setWare(w);
		if("B".equals(adminMode)){
		    if(org.springframework.util.StringUtils.isEmpty(countMain.getBinName())){
		        model.addAttribute("message","管理标志为B,货区不能为空");
		        countMain.setWare(w);
                return form(countMain, model);
            }
            invAccount.setBin(binService.get(countMain.getBin().getId()));
        }
        if("L".equals(adminMode)){
		    if(org.springframework.util.StringUtils.isEmpty(countMain.getBinName()) || org.springframework.util.StringUtils.isEmpty(countMain.getLocName())){
                model.addAttribute("message","管理标志为L,货区与货位不能为空");
                countMain.setBinName("");
                countMain.setLocName("");
                countMain.setWare(w);
                return form(countMain, model);
            }
            invAccount.setBin(binService.get(countMain.getBin().getId()));
		    invAccount.setLoc(locationService.get(countMain.getLoc().getId()));
        }
        List<InvAccount> invAccounts = invAccountService.findAllItemByWareCondition(invAccount);
        List<CountDetail> countDetailList = Lists.newArrayList();
        CountDetail temp ;
        for(InvAccount account : invAccounts){
            temp = new CountDetail();
            temp.setCountMain(countMain);
            Item item = itemService.getByCode(account.getItem().getCode());
            temp.setItem(item);
            temp.setItemName(item.getName());
            temp.setSpecModel(item.getSpecModel());
            Unit unit = unitService.get(item.getUnitCode().getUnitCode());
            temp.setMeasUnit(unit);
            temp.setItemPdn(item.getDrawNO());
            temp.setItemQty(account.getNowQty());

            temp.setId("");
            countDetailList.add(temp);
        }
        countMain.setCountDetailList(countDetailList);
        countMain.setProcFlag("N");
		countMainService.save(countMain);//保存
		addMessage(model, "保存盘点数据录入成功");
		return list(new CountMain(),model);
	}

}