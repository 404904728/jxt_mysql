package common.cq.hmq.model;

import core.cq.hmq.modal.PageModel;
import core.cq.hmq.util.tools.StringUtil;

public class JqPageModel extends PageModel {
	
	/**
	 * 	排序列名
	 */
	private String sidx;
	
	/**
	 * 	排序 asc desc
	 */
	private String sord;
	
	/**
	 * 	某个查询关键字
	 */
	private String searchKey;

	public String getSidx() {
		return sidx;
	}

	public void setSidx(String sidx) { 
		if(StringUtil.isEmpty(sidx)){
			this.sidx = null;
			super.setSort(null);
		}else{
			this.sidx = sidx;
			super.setSort(sidx);
		}
	}

	public String getSord() {
		return sord;
	}

	public void setSord(String sord) {
		this.sord = sord;
		super.setOrder(sord);
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public String getSearchKey() {
		return searchKey;
	}
	
	
}
