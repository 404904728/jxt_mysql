package app.cq.hmq.pojo.teachertopic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class teachertopicre {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/**
	 * 回复主题id
	 */
	private Long topicId;
	
	/**
	 * 回复人id
	 */
	private Long reUserId;
	
	/**
	 * 回复人类型  1学生 9 教师
	 */
	private String reUserType;
	
	/**
	 * 回复内容
	 */
	@Column(length = 1000)
	private String reContent;
	
	/**
	 * 回复时间
	 */
	private String reDate;
	
	/**
	 * 回复语音id
	 */
	private Long voiceId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public Long getReUserId() {
		return reUserId;
	}

	public void setReUserId(Long reUserId) {
		this.reUserId = reUserId;
	}

	public String getReUserType() {
		return reUserType;
	}

	public void setReUserType(String reUserType) {
		this.reUserType = reUserType;
	}

	public String getReContent() {
		return reContent;
	}

	public void setReContent(String reContent) {
		this.reContent = reContent;
	}

	public String getReDate() {
		return reDate;
	}

	public void setReDate(String reDate) {
		this.reDate = reDate;
	}

	public Long getVoiceId() {
		return voiceId;
	}

	public void setVoiceId(Long voiceId) {
		this.voiceId = voiceId;
	}
}
