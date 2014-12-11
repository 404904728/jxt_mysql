package app.cq.hmq.pojo.cjmanage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Score {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/**
	 * 考试内容
	 */
	@Column(length=80)
	private String title;
	
	/**
	 * 学号
	 */
	@Column(length=50)
	private String sno;
	
	/**
	 * 姓名
	 */
	@Column(length=50)
	private String sName;
	
	/**
	 * 班级
	 */
	private String sClass;
	
	/**
	 * 分数1
	 */
	private Float score1;
	
	private Float score2;
	
	private Float score3;
	
	private Float score4;
	
	private Float score5;
	
	private Float score6;
	
	private Float score7;
	
	private Float score8;
	
	private Float score9;
	
	private Float score10;
	private Float score11;
	private Float score12;
	private Float score13;
	private Float score14;
	private Float score15;
	private Float score16;
	private Float score17;
	private Float score18;
	
	/**
	 * 总分
	 */
	private Float totalScore;
	
	/**
	 * 总班级排名
	 */
	private Short classOrder;
	
	/**
	 * 总年级排名
	 */
	private Short gradeOrder;
	
	/**
	 * 班级涨幅
	 */
	@Column(length=10)
	private String classAsc;
	
	/**
	 * 年级涨幅
	 */
	@Column(length=10)
	private String gradeAsc;
	
	/**
	 * 导入人
	 */
	private Long importUser;
	
	/**
	 * 导入时间
	 */
	@Column(length=50)
	private String importDate; 
	
	/**
	 * 此次考试分数类型  1 班级考试  2年级统考
	 */
	private Integer scoreType;
	
	/**
	 * 默认为未发布，成绩的发布状态,true 发布 反之未发布
	 * @return
	 */
    private boolean publishStatus = false;
	
    /**
     * 班主任评语，为某些学生添加评语
     */
    private String bzrComments;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSno() {
		return sno;
	}

	public void setSno(String sno) {
		this.sno = sno;
	}

	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

	public String getsClass() {
		return sClass;
	}

	public void setsClass(String sClass) {
		this.sClass = sClass;
	}

	public Float getScore1() {
		return score1;
	}

	public void setScore1(Float score1) {
		this.score1 = score1;
	}

	public Float getScore2() {
		return score2;
	}

	public void setScore2(Float score2) {
		this.score2 = score2;
	}

	public Float getScore3() {
		return score3;
	}

	public void setScore3(Float score3) {
		this.score3 = score3;
	}

	public Float getScore4() {
		return score4;
	}

	public void setScore4(Float score4) {
		this.score4 = score4;
	}

	public Float getScore5() {
		return score5;
	}

	public void setScore5(Float score5) {
		this.score5 = score5;
	}

	public Float getScore6() {
		return score6;
	}

	public void setScore6(Float score6) {
		this.score6 = score6;
	}

	public Float getScore7() {
		return score7;
	}

	public void setScore7(Float score7) {
		this.score7 = score7;
	}

	public Float getScore8() {
		return score8;
	}

	public void setScore8(Float score8) {
		this.score8 = score8;
	}

	public Float getScore9() {
		return score9;
	}

	public void setScore9(Float score9) {
		this.score9 = score9;
	}

	public Float getScore10() {
		return score10;
	}

	public void setScore10(Float score10) {
		this.score10 = score10;
	}

	public Float getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Float totalScore) {
		this.totalScore = totalScore;
	}

	public Short getClassOrder() {
		return classOrder;
	}

	public void setClassOrder(Short classOrder) {
		this.classOrder = classOrder;
	}

	public Short getGradeOrder() {
		return gradeOrder;
	}

	public void setGradeOrder(Short gradeOrder) {
		this.gradeOrder = gradeOrder;
	}

	public String getClassAsc() {
		return classAsc;
	}

	public void setClassAsc(String classAsc) {
		this.classAsc = classAsc;
	}

	public String getGradeAsc() {
		return gradeAsc;
	}

	public void setGradeAsc(String gradeAsc) {
		this.gradeAsc = gradeAsc;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setImportDate(String importDate) {
		this.importDate = importDate;
	}

	public String getImportDate() {
		return importDate;
	}

	public void setImportUser(Long importUser) {
		this.importUser = importUser;
	}

	public Long getImportUser() {
		return importUser;
	}

	public void setScoreType(Integer scoreType) {
		this.scoreType = scoreType;
	}

	public Integer getScoreType() {
		return scoreType;
	}

	public Float getScore11() {
		return score11;
	}

	public void setScore11(Float score11) {
		this.score11 = score11;
	}

	public Float getScore12() {
		return score12;
	}

	public void setScore12(Float score12) {
		this.score12 = score12;
	}

	public Float getScore13() {
		return score13;
	}

	public void setScore13(Float score13) {
		this.score13 = score13;
	}

	public Float getScore14() {
		return score14;
	}

	public void setScore14(Float score14) {
		this.score14 = score14;
	}

	public Float getScore15() {
		return score15;
	}

	public void setScore15(Float score15) {
		this.score15 = score15;
	}

	public Float getScore16() {
		return score16;
	}

	public void setScore16(Float score16) {
		this.score16 = score16;
	}

	public Float getScore17() {
		return score17;
	}

	public void setScore17(Float score17) {
		this.score17 = score17;
	}

	public Float getScore18() {
		return score18;
	}

	public void setScore18(Float score18) {
		this.score18 = score18;
	}

	public void setPublishStatus(boolean publishStatus) {
		this.publishStatus = publishStatus;
	}

	public boolean isPublishStatus() {
		return publishStatus;
	}

	public void setBzrComments(String bzrComments) {
		this.bzrComments = bzrComments;
	}

	public String getBzrComments() {
		return bzrComments;
	}
	
}
