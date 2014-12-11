package app.cq.hmq.pojo.leave;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import app.cq.hmq.pojo.stuinfo.StudentInfo;
import app.cq.hmq.pojo.teacherinfo.TeacherInfo;

/**
 * 
 * @author Administrator
 * 
 */
@Entity
public class LeaveInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	 * 申請人
	 */
	private Long proposerId;
	
	/**
	 * 请假人
	 */
	@ManyToOne
	private StudentInfo leaveUser;
	
	/**
	 * 申請人类型 1学生 9 任课教师
	 */
	private Integer proposerType;
	
	/**
	 * 发起班级id
	 */
	private Long proposerClass;

	/**
	 * 类型（事假、病假、探亲假、丧假、其他）
	 */
	private String leaveType;

	/**
	 * 申请事由
	 */
	@Column(length = 3000)
	private String leaveReason;

	/**
	 * false 未阅 true　已阅
	 */
	private boolean status = false;
	
	/**
	 * 未确认  已确认 待确认 已拒绝
	 */
	private String approveStatus = "未确认";

	/**
	 * 申请填写时间
	 */
	private String writeDate;

	/**
	 * 假期开始时间
	 */
	private String startDate;

	/**
	 * 假期结束时间
	 */
	private String endDate;

	/**
	 * 接收教师
	 */
	@ManyToOne
	private TeacherInfo receiver;

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public String getLeaveReason() {
		return leaveReason;
	}

	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getWriteDate() {
		return writeDate;
	}

	public void setWriteDate(String writeDate) {
		this.writeDate = writeDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setReceiver(TeacherInfo receiver) {
		this.receiver = receiver;
	}

	public TeacherInfo getReceiver() {
		return receiver;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setProposerId(Long proposerId) {
		this.proposerId = proposerId;
	}

	public Long getProposerId() {
		return proposerId;
	}

	public void setProposerType(Integer proposerType) {
		this.proposerType = proposerType;
	}

	public Integer getProposerType() {
		return proposerType;
	}

	public void setProposerClass(Long proposerClass) {
		this.proposerClass = proposerClass;
	}

	public Long getProposerClass() {
		return proposerClass;
	}

	public void setLeaveUser(StudentInfo leaveUser) {
		this.leaveUser = leaveUser;
	}

	public StudentInfo getLeaveUser() {
		return leaveUser;
	}

}
