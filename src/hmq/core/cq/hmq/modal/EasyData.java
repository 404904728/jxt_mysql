package core.cq.hmq.modal;

import java.util.ArrayList;
import java.util.List;

/**
 * easy数据表格
 * 
 * @author monster
 * @param <T>
 * 
 */
public class EasyData<T> {

	private List<T> rows;

	private int total;

	private Object footer;

	public List<T> getRows() {
		if (rows == null) {//防止rows为null时，前台报错
			rows = new ArrayList<T>();
		}
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Object getFooter() {
		return footer;
	}

	public void setFooter(Object footer) {
		this.footer = footer;
	}
}
