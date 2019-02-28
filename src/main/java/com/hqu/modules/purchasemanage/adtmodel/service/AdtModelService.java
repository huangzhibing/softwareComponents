/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.adtmodel.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hqu.modules.purchasemanage.purplan.entity.Checker;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.purchasemanage.adtmodel.entity.AdtModel;
import com.hqu.modules.purchasemanage.adtmodel.mapper.AdtModelMapper;

import com.jeeplus.modules.sys.entity.Role;


/**
 * 工作流模型Service
 * @author ckw
 * @version 2018-05-08
 */
@Service
@Transactional(readOnly = true)
public class AdtModelService extends CrudService<AdtModelMapper, AdtModel> {

	public AdtModel get(String id) {
		return super.get(id);
	}
	
	public List<AdtModel> findList(AdtModel adtModel) {
		return super.findList(adtModel);
	}

    //根据modelName查找对应审核工作流模型中包含的所有角色
    public List<Checker> findRoleList(String  modelName) {
        AdtModel adtModel=new AdtModel();
        //根据mdelName获取当前模型包含的角色ID
        adtModel.setModelCode(modelName);
		List<AdtModel> adtModelList= findList(adtModel);
		//<角色ID，角色名称>
		List<Checker> nextCheckers=new ArrayList<>();

        //获取当前工作流中包含的角色ID
        for(AdtModel tmp:adtModelList){
            nextCheckers.add(new Checker(tmp.getRole().getName(),tmp.getRole().getId()));
        }
        return  nextCheckers;
    }


	public Map<String,String> findRoleListByModelName(String  modelName) {
	    AdtModel adtModel=new AdtModel();
	    //根据mdelName获取当前模型包含的角色ID
	    adtModel.setModelCode(modelName);
        List<AdtModel> adtModelList= findList(adtModel);


		//<角色ID，角色名称>
		Map<String,String> id2Name=new HashMap();
        //获取当前工作流中包含的角色ID
        for(AdtModel tmp:adtModelList){
        	id2Name.put(tmp.getRole().getId(),tmp.getRole().getName());
		}


       return id2Name;
    }

	public Page<AdtModel> findPage(Page<AdtModel> page, AdtModel adtModel) {
		return super.findPage(page, adtModel);
	}
	
	@Transactional(readOnly = false)
	public void save(AdtModel adtModel) {
		super.save(adtModel);
	}
	
	@Transactional(readOnly = false)
	public void delete(AdtModel adtModel) {
		super.delete(adtModel);
	}

	/**
	 * 根据模型编号获取第一个审核人（角色）
	 * @param modelCode
	 * @return
	 */
	public AdtModel getFirst(String modelCode){
		AdtModel m=new AdtModel();
		m.setModelCode(modelCode);
		m.setIsFirstperson("1");
		List<AdtModel> ls=super.findList(m);
		if(ls.size()>0){
			return ls.get(0);
		}else {
			return null;
		}
	}
	
}