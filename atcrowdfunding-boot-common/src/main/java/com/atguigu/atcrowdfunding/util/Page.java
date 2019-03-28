package com.atguigu.atcrowdfunding.util;

import java.util.List;

public class Page<T> {
	private List<T> datas;
	private int totalno;
	private int pageno;
	private int totalsize;
	private int pagesize;

	public Page() {
		pageno = 1;
		pagesize = 10;
	}

	public Page(int pageno, int pagesize) {
		super();
		if (pageno <= 0) {
			this.pageno = 1;
		} else {
			this.pageno = pageno;
		}
		if (pagesize <= 0) {
			this.pagesize = 10;
		} else {
			this.pagesize = pagesize;
		}
	}

	public List<T> getDatas() {
		return datas;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}

	public int getTotalno() {
		return totalno;
	}

	private void setTotalno(int totalno) {
		this.totalno = totalno;
	}

	public int getPageno() {
		return pageno;
	}

	public void setPageno(int pageno) {
		this.pageno = pageno;
	}

	public int getTotalsize() {
		return totalsize;
	}

	public void setTotalsize(int totalsize) {
		this.totalsize = totalsize;
		this.totalno = totalsize % pagesize == 0 ? (totalsize / pagesize) : (totalsize / pagesize + 1);
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public int getStartindex() {
		return (pageno - 1) * pagesize;
	}
}
