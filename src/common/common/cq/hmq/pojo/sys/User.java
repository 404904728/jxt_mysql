package common.cq.hmq.pojo.sys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class User {

	public static final String STUDENTTYPE = "1";
	public static final String TEACHERTYPE = "2";
	public static final String ADMINTYPE = "0";
	public static final String LKEY = "A373F8EF47FC1F7BEB96F17C6246E1F52";

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/** 姓名 */
	private String name;

	/** 帐号 */
	private String no;

	/** 密码 */
	@Column(length = 32)
	private String pwd;

	/** 性别 0：保密 1：男 2：女 */
	private Integer sex = 0;

	/** 联系电话 */
	private String tel;

	/** 所属部门 */
	@ManyToOne
	private Org org;

	/** 昵称 */
	private String nickName;

	/** 头像 */
	private Integer head;

	private String email;

	private Integer age;

	/** 身份证号 */
	private String identity;

	/** 职业 */
	@ManyToOne
	private Profession profession;

	/** 地址 */
	private String address;

	/** 上次登录时间 */
	private Date date;

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

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getHead() {
		return head;
	}

	public void setHead(Integer head) {
		this.head = head;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public Profession getProfession() {
		return profession;
	}

	public void setProfession(Profession profession) {
		this.profession = profession;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
