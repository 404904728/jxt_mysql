package app.cq.hmq.pojo.leave;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import app.cq.hmq.pojo.teacherinfo.TeacherInfo;

@Entity
public class CheckIn {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/**
	 * 班级
	 */
	private Long classId;
	
	/**
	 * 考勤标题(或描述)
	 */
	private String title;
	
	/**
	 * 考勤时间
	 */
	private String checkDate;
	
	/**
	 * 考勤教师
	 */
	@ManyToOne
	private TeacherInfo tea;
	
	/**
	 * 未到学生Id(1,2,3)
	 */
	private String unArriveIds;
	
	/**
	 * 未到人数
	 */
	private int unCount;
	
	/**
	 * 已到人数
	 */
	private int hadCount;
	
	/**
	 * 出勤率
	 */
	private int attendance;
	
	/**
	 * 考勤类型 如班主任考勤 1 任课老师考勤 2
	 */
	private int checkType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public TeacherInfo getTea() {
		return tea;
	}

	public void setTea(TeacherInfo tea) {
		this.tea = tea;
	}

	public String getUnArriveIds() {
		return unArriveIds;
	}

	public void setUnArriveIds(String unArriveIds) {
		this.unArriveIds = unArriveIds;
	}

	public int getUnCount() {
		return unCount;
	}

	public void setUnCount(int unCount) {
		this.unCount = unCount;
	}

	public int getHadCount() {
		return hadCount;
	}

	public void setHadCount(int hadCount) {
		this.hadCount = hadCount;
	}

	public int getAttendance() {
		return attendance;
	}

	public void setAttendance(int attendance) {
		this.attendance = attendance;
	}

	public int getCheckType() {
		return checkType;
	}

	public void setCheckType(int checkType) {
		this.checkType = checkType;
	}

	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}

	public String getCheckDate() {
		return checkDate;
	}
}
