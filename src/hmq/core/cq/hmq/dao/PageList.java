package core.cq.hmq.dao;

import java.util.Collections;
import java.util.List;

/**
 * @author 何建 2009-9-10
 */
@SuppressWarnings("unchecked")
public class PageList<T> {

	public static final int DEFAULT_PAGE_SIZE = 10;

	private final int pageNo;

	private final int pageSize;

	private int totalCount;

	private List<T> list = Collections.EMPTY_LIST;

	private String[] columnNames;

	public static final PageList EMPTY = new PageList(0, DEFAULT_PAGE_SIZE);

	/**
	 * 唯一的构造函数
	 * 
	 * @param pageNo
	 *            页数序号，从0开始
	 * @param pageSize
	 *            页数大小
	 */
	public PageList(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize < 1 ? 0 : pageSize;
	}

	public boolean isDefaultEmpty() {
		return EMPTY == this;
	}

	/**
	 * 本页第一条记录在数据库中的序号
	 */
	public int firstIndex() {
		return pageSize * (pageNo);
	}

	/**
	 * 页数序号，从0开始
	 */
	public int pageNo() {
		return pageNo;
	}

	public int getPageNo() {
		return pageNo + 1;
	}

	/**
	 * 分页大小
	 */
	public int pageSize() {
		return pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 当前页实际记录条数
	 */
	public int size() {
		return list.size();
	}

	public void setResult(List<T> list) {
		this.list = list;
	}

	/**
	 * 数据库中实际记录条数，即记录总数
	 */
	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 页总数
	 */
	public int pageCount() {
		if (pageSize == 0) {
			return 1;
		}
		return totalCount / pageSize + (totalCount % pageSize == 0 ? 0 : 1);
	}

	public int getPageCount() {
		return pageCount();
	}

	/**
	 * 当前页的list
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * 是否有下一页
	 */
	public boolean hasNext() {
		return pageNo() < pageCount();
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

}
