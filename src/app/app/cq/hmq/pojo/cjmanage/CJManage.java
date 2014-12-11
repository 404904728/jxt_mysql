/**
 * Limit
 *
 */
package app.cq.hmq.pojo.cjmanage;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import app.cq.hmq.pojo.stuinfo.StudentInfo;
import app.cq.hmq.pojo.subject.SubjectInfo;

import common.cq.hmq.pojo.sys.Org;

import core.cq.hmq.service.util.JsonDateSerializer;
/**
 * @author Limit
 * 
 */
@Entity
public class CJManage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 关联学生姓名
	 */
	@ManyToOne
	private StudentInfo stu;

	/**
	 * 关联班级信息
	 */
	@ManyToOne
	private Org org;

	/**
	 * 关联科目信息
	 */
	@ManyToOne
	private SubjectInfo sub;

	/**
	 * 考试内容
	 */
	private String ksnr;

	/**
	 * 考试成绩
	 */
	private Double chengJi;

	/**
	 * 考试时间
	 */
	private Date kSTime;

	/**
	 * 备注
	 */
	private String reCommon;

	public StudentInfo getStu() {
		return stu;
	}

	public void setStu(StudentInfo stu) {
		this.stu = stu;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public SubjectInfo getSub() {
		return sub;
	}

	public void setSub(SubjectInfo sub) {
		this.sub = sub;
	}

	public String getKsnr() {
		return ksnr;
	}

	public void setKsnr(String ksnr) {
		this.ksnr = ksnr;
	}


	public Double getChengJi() {
		return chengJi;
	}

	public void setChengJi(Double chengJi) {
		this.chengJi = chengJi;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getkSTime() {
		return kSTime;
	}

	public void setkSTime(Date kSTime) {
		this.kSTime = kSTime;
	}

	public String getReCommon() {
		return reCommon;
	}

	public void setReCommon(String reCommon) {
		this.reCommon = reCommon;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	@Id
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
