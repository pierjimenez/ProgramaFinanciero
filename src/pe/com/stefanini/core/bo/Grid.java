package pe.com.stefanini.core.bo;

import java.util.List;


public class Grid<E> {
	
	private List dataModel;
	private Integer rows = 0;
	private Integer page = 0;
	private Integer total = 0;
	private Integer sord = 0;
	private Integer sidx=0;
	private Integer records=0;
	

	public Integer getRecords() {
		return records;
	}
	public void setRecords(Integer records) {
		this.records = records;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getSord() {
		return sord;
	}
	public void setSord(Integer sord) {
		this.sord = sord;
	}
	public Integer getSidx() {
		return sidx;
	}
	public void setSidx(Integer sidx) {
		this.sidx = sidx;
	}
	public List getDataModel() {
		return dataModel;
	}
	public void setDataModel(List dataModel) {
		this.dataModel = dataModel;
	}

	
	
	
}
