package com.hqu.modules.qualitymanage.purreport.entity;

import java.util.ArrayList;
import java.util.List;

import com.hqu.modules.qualitymanage.verifyqcnorm.entity.VerifyQCNorm;

public class VerifyQCNormCust {
	List<VerifyQCNorm> verifyQCNormList = new ArrayList<VerifyQCNorm>();

	public List<VerifyQCNorm> getVerifyQCNormList() {
		return verifyQCNormList;
	}

	public void setVerifyQCNormList(List<VerifyQCNorm> verifyQCNormList) {
		this.verifyQCNormList = verifyQCNormList;
	}
	
	

}
