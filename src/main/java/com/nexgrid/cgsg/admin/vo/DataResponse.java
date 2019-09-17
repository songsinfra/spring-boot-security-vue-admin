package com.nexgrid.cgsg.admin.vo;

import java.util.List;

/**
 * @Date         : 2015. 9. 1. 
 * @작성자      : 김영호
 * @프로그램 설명 : Json 타입으로 Ajax로 가져오기 위한 객체. Data에 List 데이터를 담아서 사용한다.
 * @개정이력 :
 */
 
public class DataResponse<T> {

	private Integer total;
	
	private List<T> data;
	
	private Integer recordsTotal;

	private Integer recordsFiltered;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
	
	public Integer getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(Integer recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public Integer getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(Integer recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	
}
