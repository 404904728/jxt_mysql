package common.cq.hmq.model;

import java.util.List;

public class JqGridData<T> {

	/**
	 * 总页数
	 */
	private int total;

	/**
	 * 当前第几页
	 */
	private int page;

	/**
	 * 总纪录数
	 */
	private int records;

	/**
	 * 结果集
	 */
	private List<T> rows;

	public int getTotal() {
		return total;
	}

	/**
	 * 总页数
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	public int getPage() {
		return page;
	}


	/**
	 * 当前第几页
	 */
	public void setPage(int page) {
		this.page = page;
	}

	public int getRecords() {
		return records;
	}

	/**
	 * 总纪录数
	 */
	public void setRecords(int records) {
		this.records = records;
	}

	public List<T> getRows() {
		return rows;
	}

	/**
	 * 结果集
	 */
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	
}
