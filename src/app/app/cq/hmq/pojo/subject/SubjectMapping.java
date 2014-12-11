package app.cq.hmq.pojo.subject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import common.cq.hmq.pojo.sys.Org;

/**
 * 
 * @author Administrator
 *
 */
@Entity
public class SubjectMapping{
	
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
	
	/**
	 * 对应科目
	 */
	@ManyToOne
	private SubjectInfo subjectInfo;

	/**
	 * 对应班级
	 */
	@ManyToOne
	private Org org;
	
	/**
	 * 对应教师唯一标识
	 */
	private Long teacher;

	public SubjectInfo getSubjectInfo() {
		return subjectInfo;
	}

	public void setSubjectInfo(SubjectInfo subjectInfo) {
		this.subjectInfo = subjectInfo;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public void setTeacher(Long teacher) {
		this.teacher = teacher;
	}

	public Long getTeacher() {
		return teacher;
	}

}
