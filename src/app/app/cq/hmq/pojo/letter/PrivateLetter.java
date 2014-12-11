package app.cq.hmq.pojo.letter;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import common.cq.hmq.pojo.sys.Org;

@Entity
public class PrivateLetter {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/**
	 * 发送人唯一标识
	 */
	private Long senderId;
	
	/**
	 * 发送人姓名
	 */
	private String senderName;
	
	/**
	 * 发送人所属机构
	 */
	@ManyToOne
	private Org senderOrg;
	
	/**
	 * 发送人类型（教师、家长）
	 */
	private String senderType;
	
	/**
	 * 发送时间
	 */
	private String sendTime;
	
	/**
	 * 接收人唯一标识
	 */
	private Long receiverId;
	
	
	/**
	 * 接收人姓名
	 */
	private String receicerName;
	
	/**
	 * 接收人所属机构
	 */
	@ManyToOne
	private Org receicerOrg;
	
	
	/**
	 * 接收人类型（教师、家长）
	 */
	private String receiverType;
	
	/**
	 * 发送内容
	 */
	@Column(length = 1000)
	private String content;
	/**
	 * 最近回复内容
	 */
	private String newContent;
	
	/**
	 * 语音id
	 */
	private Long voiceId;
	
	/**
	 *是否查看  true 已看  false 未看
	 */
	private boolean status=false;
	
	/** 最近回复时间 */
	private Date newReDate;
	
	/** 最近回复人 */
	private Long newReUserId;
	
	/** 最近回复人类型 */
	@Column(length=10)
	private String newReUserType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public Org getSenderOrg() {
		return senderOrg;
	}

	public void setSenderOrg(Org senderOrg) {
		this.senderOrg = senderOrg;
	}

	public String getSenderType() {
		return senderType;
	}

	public void setSenderType(String senderType) {
		this.senderType = senderType;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public Long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}
	
	public void setReceiverId(String receiverId) {
		this.receiverId = Long.parseLong(receiverId);
	}

	public String getReceicerName() {
		return receicerName;
	}

	public void setReceicerName(String receicerName) {
		this.receicerName = receicerName;
	}

	public String getReceiverType() {
		return receiverType;
	}

	public void setReceiverType(String receiverType) {
		this.receiverType = receiverType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isStatus() {
		return status;
	}

	public void setReceicerOrg(Org receicerOrg) {
		this.receicerOrg = receicerOrg;
	}

	public Org getReceicerOrg() {
		return receicerOrg;
	}

	public void setVoiceId(Long voiceId) {
		this.voiceId = voiceId;
	}

	public Long getVoiceId() {
		return voiceId;
	}

	public Date getNewReDate() {
		return newReDate;
	}

	public void setNewReDate(Date newReDate) {
		this.newReDate = newReDate;
	}

	public void setNewContent(String newContent) {
		this.newContent = newContent;
	}

	public String getNewContent() {
		return newContent;
	}

	public void setNewReUserId(Long newReUserId) {
		this.newReUserId = newReUserId;
	}

	public Long getNewReUserId() {
		return newReUserId;
	}

	public void setNewReUserType(String newReUserType) {
		this.newReUserType = newReUserType;
	}

	public String getNewReUserType() {
		return newReUserType;
	}
}
