package core.cq.hmq.modal;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import core.cq.hmq.service.util.JsonDateSerializer;


public class TaskModal {

	private String id;

	private String name;

	/**
	 * 任务创建时间
	 */
	private Date createDate;

	/**
	 * 任务签收时间
	 */
	private Date dueDate;

	/**
	 * 任务人
	 */
	private String userName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
