/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.objectdef.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.qualitymanage.objectdef.entity.ObjectDef;
import com.hqu.modules.qualitymanage.objectdef.mapper.ObjectDefMapper;
import com.hqu.modules.qualitymanage.objectdef.entity.ObjectItem;
import com.hqu.modules.qualitymanage.objectdef.mapper.ObjectItemMapper;

/**
 * 检验对象定义Service
 * @author M1ngz
 * @version 2018-04-07
 */
@Service
@Transactional(readOnly = true)
public class ObjectDefService extends CrudService<ObjectDefMapper, ObjectDef> {

	@Autowired
	private ObjectItemMapper objectItemMapper;
	
	public ObjectDef get(String id) {
		ObjectDef objectDef = super.get(id);
		objectDef.setObjectItemList(objectItemMapper.findList(new ObjectItem(objectDef)));
		return objectDef;
	}
	
	public List<ObjectDef> findList(ObjectDef objectDef) {
		return super.findList(objectDef);
	}
	
	public Page<ObjectDef> findPage(Page<ObjectDef> page, ObjectDef objectDef) {
		return super.findPage(page, objectDef);
	}
	
	@Transactional(readOnly = false)
	public void save(ObjectDef objectDef) {
		super.save(objectDef);
		for (ObjectItem objectItem : objectDef.getObjectItemList()){
			if (objectItem.getId() == null){
				continue;
			}
			if (ObjectItem.DEL_FLAG_NORMAL.equals(objectItem.getDelFlag())){
				if (StringUtils.isBlank(objectItem.getId())){
					objectItem.setObj(objectDef);
					objectItem.preInsert();
					objectItemMapper.insert(objectItem);
				}else{
					objectItem.preUpdate();
					objectItemMapper.update(objectItem);
				}
			}else{
				objectItemMapper.delete(objectItem);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(ObjectDef objectDef) {
		super.delete(objectDef);
		objectItemMapper.delete(new ObjectItem(objectDef));
	}

	public Integer getCodeNum(String objCode){ return mapper.getCodeNum(objCode);}

}