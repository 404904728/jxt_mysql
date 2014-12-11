package core.cq.hmq.modal;

/**
 * 对查询参数的的包装，有：分页号、分页大小、搜索关键字、排序字段、排序类型（升或降）。
 * 
 * @author hejian
 * @since 2013-02-05
 */
public class PageModel {

	/** 当前页 */
	private int page;

	/** 当前页面条数 默认30条 */
	private int rows = 10;

	/** 排序的字段名 */
	private String sort;

	/** 排序方式 **/
	private String order;

	/** 拼接的sql，用于查询 */
	private String querySql;

	/** 是否分页 默认要分页 */
	private boolean paging = true;

	public PageModel() {
		super();
	}

	public PageModel(int page, int rows, String sort, String order,
			String querySql, boolean paging) {
		super();
		this.page = page;
		this.rows = rows;
		this.sort = sort;
		this.order = order;
		this.querySql = querySql;
		this.paging = paging;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		// 分页查询均是从0开始
		// easyui是从第一页开始
		// 所以这里需要减去1
		this.page = page - 1;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getQuerySql() {
		return querySql;
	}

	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}

	public boolean isPaging() {
		return paging;
	}

	public void setPaging(boolean paging) {
		this.paging = paging;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
