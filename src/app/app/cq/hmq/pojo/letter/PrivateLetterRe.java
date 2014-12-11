package app.cq.hmq.pojo.letter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import common.cq.hmq.pojo.sys.Org;

@Entity
public class PrivateLetterRe {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/**
	 * 回复内容
	 */
	@Column(length = 3000)
	private String reContent;
	
	/**
	 * 语音id
	 */
	private Long voiceId;
	
	/**
	 * 回复时间
	 */
	private String reTime;
	
	/**
	 * 私信标识
	 */
	private Long plId;
	
	/**
	 * 回复人标识
	 */
	private Long reUserId;
	
	/**
	 * 回复人姓名
	 */
	private String reUserName;
	
	/**
	 * 回复人机构组织
	 */
	@ManyToOne
	private Org reUserOrg;
	
	/**
	 * 回复人类型
	 */
	private String reUserType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReContent() {
		return reContent;
	}

	public void setReContent(String reContent) {
		this.reContent = reContent;
	}

	public String getReTime() {
		return reTime;
	}

	public void setReTime(String reTime) {
		this.reTime = reTime;
	}

	public Long getPlId() {
		return plId;
	}

	public void setPlId(Long plId) {
		this.plId = plId;
	}

	public Long getReUserId() {
		return reUserId;
	}

	public void setReUserId(Long reUserId) {
		this.reUserId = reUserId;
	}

	public String getReUserName() {
		return reUserName;
	}

	public void setReUserName(String reUserName) {
		this.reUserName = reUserName;
	}

	public Org getReUserOrg() {
		return reUserOrg;
	}

	public void setReUserOrg(Org reUserOrg) {
		this.reUserOrg = reUserOrg;
	}

	public String getReUserType() {
		return reUserType;
	}

	public void setReUserType(String reUserType) {
		this.reUserType = reUserType;
	}

	public void setVoiceId(Long voiceId) {
		this.voiceId = voiceId;
	}

	public Long getVoiceId() {
		return voiceId;
	}
	
}
