package app.cq.hmq.pojo.stuinfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import common.cq.hmq.pojo.sys.Attach;
import common.cq.hmq.pojo.sys.Org;

@Entity
public class StudentInfo {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * 学生信息
	 */
	private static final long serialVersionUID = 4978467868960750788L;

	/** 姓名 */
	private String name;

	/** 登录电话号码 */
	private String no;

	/** 密码 */
	@Column(length = 32)
	private String pwd;

	/** 头像 */
	@ManyToOne
	private Attach headPic;

	/** 自己电话 */
	private String selftel;

	/** 家长姓名 */
	private String parentName;

	/** 家长关系 */
	private String parentRelation;

	/** 身份证号 */
	private String idCardNo;

	/** 状态 0为可用 1为不可以 */
	private Integer status = 0;

	/** 性别 0：女 1：男 2：保密 */
	private Integer sex = 0;

	/** 生日 */
	private String birthday;

	/** 职务 */
	private String dutyPosition;

	/** 学籍号 */
	private String studentCode;

	/** 地址 */
	private String address;

	/** 名族 */
	@Column(length = 50)
	private String nation;

	/** 籍贯 */
	@Column(length = 100)
	private String jiguan;
	
	/**
	 * 删除原因 1 转学 2退学 3 休学 4 辞退 5 其他
	 */
	private Integer deleteType; 

	/** 所属班级 */
	@ManyToOne
	private Org org;

	/**
	 * 零时属性，存班主任
	 */
	@Transient
	private String leader;

	private String channelId;

	private String userId;

	private Boolean android = true;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Attach getHeadPic() {
		return headPic;
	}

	public void setHeadPic(Attach headPic) {
		this.headPic = headPic;
	}

	public String getSelftel() {
		return selftel;
	}

	public void setSelftel(String selftel) {
		this.selftel = selftel;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getParentRelation() {
		return parentRelation;
	}

	public void setParentRelation(String parentRelation) {
		this.parentRelation = parentRelation;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getStudentCode() {
		return studentCode;
	}

	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDutyPosition() {
		return dutyPosition;
	}

	public void setDutyPosition(String dutyPosition) {
		this.dutyPosition = dutyPosition;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
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

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getNation() {
		return nation;
	}

	public void setJiguan(String jiguan) {
		this.jiguan = jiguan;
	}

	public String getJiguan() {
		return jiguan;
	}

	public Boolean getAndroid() {
		return android;
	}

	public void setAndroid(Boolean android) {
		this.android = android;
	}

	public void setDeleteType(Integer deleteType) {
		this.deleteType = deleteType;
	}

	public Integer getDeleteType() {
		return deleteType;
	}
}
