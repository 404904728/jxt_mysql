/**
 * 
 */
package app.cq.hmq.pojo.teacherinfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import common.cq.hmq.pojo.sys.Attach;
import common.cq.hmq.pojo.sys.Org;

/**
 * @author all
 * 
 *         教师信息
 */
@Entity
public class TeacherInfo {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 登录电话号码 */
	private String no;

	/** 密码 */
	private String pwd;

	@ManyToOne
	private Attach headpic;

	/** 姓名 */
	private String name;

	/** 邮件地址 */
	private String email;

	/** 所属部门 */
	@ManyToOne
	private Org org;

	/** 状态 1:正常 0：不正常 */
	private Integer status = 1;

	/** 性别 1：男 0：女 2:未知 */
	private Integer gender = 2;

	/** 生日 */
	private String birthday;

	/** 职称 */
	@Column(length = 30)
	private String zhiCheng;

	/** 描述 */
	private String descript;

	/** 教师资格证号 */
	@Column(length = 30)
	private String teachNo;

	/** 教师电话 */
	@Column(length = 30)
	private String telePhone;

	/** 普通话等级 */
	@Column(length = 30)
	private String pTHLevel;

	private String channelId;

	private String userId;

	private Boolean android = true;

	public String getTelePhone() {
		return telePhone;
	}

	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getZhiCheng() {
		return zhiCheng;
	}

	public void setZhiCheng(String zhiCheng) {
		this.zhiCheng = zhiCheng;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getTeachNo() {
		return teachNo;
	}

	public void setTeachNo(String teachNo) {
		this.teachNo = teachNo;
	}

	public String getpTHLevel() {
		return pTHLevel;
	}

	public void setpTHLevel(String pTHLevel) {
		this.pTHLevel = pTHLevel;
	}

	public void setHeadpic(Attach headpic) {
		this.headpic = headpic;
	}

	public Attach getHeadpic() {
		return headpic;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Boolean getAndroid() {
		return android;
	}

	public void setAndroid(Boolean android) {
		this.android = android;
	}
	

}
