package com.nexgrid.cgsg.admin.vo;

public class PagingInfo {

	/*페이징*/
	private int iDisplayStart;
	private int iDisplayLength;
	
	private int recordsTotal;

	
	public int getiDisplayStart() {
		return iDisplayStart;
	}


	public void setiDisplayStart(int iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}


	public int getiDisplayLength() {
		return iDisplayLength;
	}


	public void setiDisplayLength(int iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}


	public int getRecordsTotal() {
		return recordsTotal;
	}


	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

}
