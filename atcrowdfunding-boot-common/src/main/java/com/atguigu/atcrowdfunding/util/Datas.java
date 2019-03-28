package com.atguigu.atcrowdfunding.util;

import java.util.ArrayList;
import java.util.List;

import com.atguigu.atcrowdfunding.bean.MemberCert;

public class Datas {

	private List<Integer> ids = new ArrayList<Integer>();
	
	private List<MemberCert> memberCertList = new ArrayList<MemberCert>();

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	public List<MemberCert> getMemberCertList() {
		return memberCertList;
	}

	public void setMemberCertList(List<MemberCert> memberCertList) {
		this.memberCertList = memberCertList;
	}

	
}
