/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.team.service;

import java.util.List;

import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.mapper.OfficeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.basedata.team.entity.Team;
import com.hqu.modules.basedata.team.mapper.TeamMapper;
import com.hqu.modules.basedata.team.entity.TeamPerson;
import com.hqu.modules.basedata.team.mapper.TeamPersonMapper;

/**
 * 班组维护Service
 * @author xn
 * @version 2018-04-16
 */
@Service
@Transactional(readOnly = true)
public class TeamService extends CrudService<TeamMapper, Team> {

	@Autowired
	private TeamPersonMapper teamPersonMapper;
	@Autowired
	private OfficeMapper officeMapper;
	
	public Team get(String id) {
		Team team = super.get(id);
		team.setTeamPersonList(teamPersonMapper.findList(new TeamPerson(team)));
		return team;
	}
	
	public List<Team> findList(Team team) {
		return super.findList(team);
	}
	
	public Page<Team> findPage(Page<Team> page, Team team) {
		return super.findPage(page, team);
	}
	
	@Transactional(readOnly = false)
	public void save(Team team) {
		if(StringUtils.isEmpty(team.getDeptName())){
			Office office = officeMapper.get(team.getDeptCode().getId());
			team.setDeptName(office.getName());
		}
		super.save(team);
		for (TeamPerson teamPerson : team.getTeamPersonList()){
			if (teamPerson.getId() == null){
				continue;
			}
			if (TeamPerson.DEL_FLAG_NORMAL.equals(teamPerson.getDelFlag())){
				if (StringUtils.isBlank(teamPerson.getId())){
					teamPerson.setTeamCode(team);
					teamPerson.preInsert();
					teamPersonMapper.insert(teamPerson);
				}else{
					teamPerson.preUpdate();
					teamPersonMapper.update(teamPerson);
				}
			}else{
				teamPersonMapper.delete(teamPerson);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(Team team) {
		super.delete(team);
		teamPersonMapper.delete(new TeamPerson(team));
	}
	
}