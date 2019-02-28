/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreport.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import java.util.List;
import com.hqu.modules.qualitymanage.purreport.entity.PurReport;

/**
 * 检验单(采购/装配/整机检测)MAPPER接口
 * @author 孙映川
 * @version 2018-04-13
 */
@MyBatisMapper
public interface InvReportSubmitMapper extends BaseMapper<PurReport> {
	public int updateProcessInstanceId(PurReport purReport);
    public	List<PurReport> findListNew(PurReport purReport);
}