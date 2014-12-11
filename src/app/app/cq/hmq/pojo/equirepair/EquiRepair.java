/**
 * Limit
 *
 */
package app.cq.hmq.pojo.equirepair;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import app.cq.hmq.pojo.teacherinfo.TeacherInfo;
import common.cq.hmq.pojo.sys.Attach;

/**
 * @author Administrator
 *
 */
@Entity
public class EquiRepair {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/**
	 * 发送方
	 */
	@ManyToOne
	private TeacherInfo sendTea;
	
	/**
	 * 报修主题
	 */
	private String repairTitle;
	
	/**
	 * 报修时间
	 */
	private String repairTime;
	
	/**
	 * 报修内容
	 */
	@Column(length = 3000)
	private String repairContent;
	
	
	/**
	 * 接收方
	 */
	@ManyToOne
	private TeacherInfo receiverTea;
	
	/**
	 * 阅读状态，0：已读 1：未读
	 */
	private Integer repairStatus;
	
	/**
	 * 关联上传的图片文件
	 */
	@Transient
	private Attach atc;
	
	/**
	 * 关联上传的图片文件
	 */
	@Transient
	private String level;
	
	
	private Long atcid;
	/**
	 * 关联上传的语音文件
	 */
	@Transient
	private Attach sound;
	
	private Long soundid;
	
	/**
	 * 维修内容
	 */
	private String cackContent;
	
	/**
	 * 发件箱状态 1：正常 0：删除 不许显示
	 */
	private Integer sendBoxStatus;
	
	/**
	 * 收件箱状态 1：正常 0：删除 不许显示
	 */
	private Integer acceptBoxStatus;
	

	public Attach getSound() {
		return sound;
	}

	public void setSound(Attach sound) {
		this.sound = sound;
	}

	public Integer getSendBoxStatus() {
		return sendBoxStatus;
	}

	public void setSendBoxStatus(Integer sendBoxStatus) {
		this.sendBoxStatus = sendBoxStatus;
	}

	public Integer getAcceptBoxStatus() {
		return acceptBoxStatus;
	}

	public void setAcceptBoxStatus(Integer acceptBoxStatus) {
		this.acceptBoxStatus = acceptBoxStatus;
	}

	public String getCackContent() {
		return cackContent;
	}

	public void setCackContent(String cackContent) {
		this.cackContent = cackContent;
	}

	public Attach getAtc() {
		return atc;
	}

	public void setAtc(Attach atc) {
		this.atc = atc;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public TeacherInfo getSendTea() {
		return sendTea;
	}

	public void setSendTea(TeacherInfo sendTea) {
		this.sendTea = sendTea;
	}

	public String getRepairTitle() {
		return repairTitle;
	}

	public void setRepairTitle(String repairTitle) {
		this.repairTitle = repairTitle;
	}

	public String getRepairTime() {
		return repairTime;
	}

	public void setRepairTime(String repairTime) {
		this.repairTime = repairTime;
	}

	public String getRepairContent() {
		return repairContent;
	}

	public void setRepairContent(String repairContent) {
		this.repairContent = repairContent;
	}

	public TeacherInfo getReceiverTea() {
		return receiverTea;
	}

	public void setReceiverTea(TeacherInfo receiverTea) {
		this.receiverTea = receiverTea;
	}

	public Integer getRepairStatus() {
		return repairStatus;
	}

	public void setRepairStatus(Integer repairStatus) {
		this.repairStatus = repairStatus;
	}

	public void setAtcid(Long atcid) {
		this.atcid = atcid;
	}

	public Long getAtcid() {
		return atcid;
	}

	public void setSoundid(Long soundid) {
		this.soundid = soundid;
	}

	public Long getSoundid() {
		return soundid;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
	
}
