/**
 * Limit
 *
 */
package app.cq.hmq.pojo.cjmanage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import app.cq.hmq.pojo.teacherinfo.TeacherInfo;

/**
 * @author Administrator
 *
 */
@Entity
public class CjAppendix {
	
	@Id
	private Long id;
    @GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 考试标题
	 */
	@Column(length = 100)
	private String examTitle;
	
	/**
	 * 考试附件名称
	 */
	@Column(length = 100)
	private Long examAppendixTitle;
	
	/**
	 * 参考人数
	 */
	private int examStudnetNum;
	
	/**
	 * 参考年级
	 */
	@Column(length = 32)
	private String examGeand;
	
	/**
	 * 参考班级
	 */
	@Column(length = 32)
	private String examClass;
	
	/**
	 * 导入日期
	 */
	@Column(length = 32)
	private String impoDate;
	
	/**
	 * 附件大小
	 */
	private int appendixSize;
	
	/**
	 * 导入人
	 */
	@ManyToOne
	private TeacherInfo impoTea;
	
	/**
	 * 发布状态 0：为发布 1： 发布
	 */
	private int publishStatus;

	public String getExamTitle() {
		return examTitle;
	}

	public void setExamTitle(String examTitle) {
		this.examTitle = examTitle;
	}

	public Long getExamAppendixTitle() {
		return examAppendixTitle;
	}

	public void setExamAppendixTitle(Long examAppendixTitle) {
		this.examAppendixTitle = examAppendixTitle;
	}

	public int getExamStudnetNum() {
		return examStudnetNum;
	}

	public void setExamStudnetNum(int examStudnetNum) {
		this.examStudnetNum = examStudnetNum;
	}

	public String getExamGeand() {
		return examGeand;
	}

	public void setExamGeand(String examGeand) {
		this.examGeand = examGeand;
	}

	public String getExamClass() {
		return examClass;
	}

	public void setExamClass(String examClass) {
		this.examClass = examClass;
	}

	public String getImpoDate() {
		return impoDate;
	}

	public void setImpoDate(String impoDate) {
		this.impoDate = impoDate;
	}

	public int getAppendixSize() {
		return appendixSize;
	}

	public void setAppendixSize(int appendixSize) {
		this.appendixSize = appendixSize;
	}

	public TeacherInfo getImpoTea() {
		return impoTea;
	}

	public void setImpoTea(TeacherInfo impoTea) {
		this.impoTea = impoTea;
	}

	public int getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(int publishStatus) {
		this.publishStatus = publishStatus;
	}
	
	
	

}
