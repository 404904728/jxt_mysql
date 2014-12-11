package core.cq.hmq.modal;

import java.io.Serializable;

public class SessionModal implements Serializable {

	private Long id;// 用户ID
	private String no;// 用户登录名
	private String name;// 用户姓名
	private String pwd;// 用户密码
	private String groupName;// 用户所属组名称
	private Long orgId;//
	private String orgName;
	private String ip;// 用户登录IP
	private String sessionId;// 用户session编号
	private String islock;// 1未锁定，0锁定
	private String date;
	private String png;//头像
	private String userType;//1学生  2老师

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getIslock() {
		return islock;
	}

	public void setIslock(String islock) {
		this.islock = islock;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getPng() {
		return png;
	}

	public void setPng(String png) {
		this.png = png;
	}
}
