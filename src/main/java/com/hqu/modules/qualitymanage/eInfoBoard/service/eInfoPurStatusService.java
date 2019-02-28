package com.hqu.modules.qualitymanage.eInfoBoard.service;

import com.hqu.modules.qualitymanage.eInfoBoard.entity.eInfoPurStatus;
import com.hqu.modules.qualitymanage.eInfoBoard.mapper.eInfoPurStatusMapper;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 质检电子板Service
 * @author zz
 * @version 2018-11-11
 */
@Service
@Transactional(readOnly = true)
public class eInfoPurStatusService extends CrudService<eInfoPurStatusMapper, eInfoPurStatus> {
    public eInfoPurStatus get(String id) {
        return super.get(id);
    }

    public List<eInfoPurStatus> findList(eInfoPurStatus einfoPurStatus) {
        return super.findList(einfoPurStatus);
    }

    public Page<eInfoPurStatus> findPage(Page<eInfoPurStatus> page, eInfoPurStatus einfoPurStatus) {
        return super.findPage(page, einfoPurStatus);
    }

}
