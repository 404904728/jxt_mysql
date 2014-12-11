package app.cq.hmq.pojo.teachertopic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import app.cq.hmq.pojo.teacherinfo.TeacherInfo;

@Entity
public class TeacherTopic {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/**
	 * 发起教师
	 */
	@ManyToOne
	private TeacherInfo tea;
	
	/**
	 * 标题
	 */
	@Column(length = 50)
	private String title;
	
	/**
	 * 班级id
	 */
	private Long classId;
	
	/**
	 * 主题内容
	 */
	@Column(length = 3000)
	private String content;
	
	/**
	 * 发起时间
	 */
	private String writeDate;
	
	/**
	 * 图片ids
	 */
	private String pictrueIds;
	
	/**
	 * 语音id
	 */
	private Long voiceId;
	
	/**
	 * 回复数
	 */
	private int reCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TeacherInfo getTea() {
		return tea;
	}

	public void setTea(TeacherInfo tea) {
		this.tea = tea;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getWriteDate() {
		return writeDate;
	}

	public void setWriteDate(String writeDate) {
		this.writeDate = writeDate;
	}

	public String getPictrueIds() {
		return pictrueIds;
	}

	public void setPictrueIds(String pictrueIds) {
		this.pictrueIds = pictrueIds;
	}

	public Long getVoiceId() {
		return voiceId;
	}

	public void setVoiceId(Long voiceId) {
		this.voiceId = voiceId;
	}

	public int getReCount() {
		return reCount;
	}

	public void setReCount(int reCount) {
		this.reCount = reCount;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public Long getClassId() {
		return classId;
	}
	
}
