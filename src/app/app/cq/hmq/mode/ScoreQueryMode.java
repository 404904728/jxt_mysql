/**
 * Limit
 *
 */
package app.cq.hmq.mode;

/**
 * @author Administrator
 *
 */
public class ScoreQueryMode {

	private Long id;
	
	/**
	 * 标题
	 */
	private String title;
	
	private String no;
	
	/**
	 * 姓名
	 */
	private String name;
	
	/**
	 * 总成绩
	 */
	private String totalScore;
	
	/**
	 * A成绩
	 */
	private String scoreA;
	
	/**
	 * B成绩
	 */
	private String scoreB;
	
	/**
	 * 年排
	 */
	private String drandOrder;
	/**
	 * 班排
	 */
	private String classOrder;
	

	
	/**
	 * 年排涨幅
	 */
	private String drandAsc;
	/**
	 * 班排涨幅
	 */
	private String classAsc;
	
	/**
	 * 评语
	 */
	private String bzrComments;
	/**
	 * 发布状态
	 */
	private String publishStatus;
	
	/**
	 * 班级
	 * @return
	 */
	private String classCode;

	public String getClassOrder() {
		return classOrder;
	}

	public void setClassOrder(String classOrder) {
		this.classOrder = classOrder;
	}

	public String getClassAsc() {
		return classAsc;
	}

	public void setClassAsc(String classAsc) {
		this.classAsc = classAsc;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(String totalScore) {
		this.totalScore = totalScore;
	}

	public String getScoreA() {
		return scoreA;
	}

	public void setScoreA(String scoreA) {
		this.scoreA = scoreA;
	}

	public String getScoreB() {
		return scoreB;
	}

	public void setScoreB(String scoreB) {
		this.scoreB = scoreB;
	}

	public String getDrandOrder() {
		return drandOrder;
	}

	public void setDrandOrder(String drandOrder) {
		this.drandOrder = drandOrder;
	}

	public String getDrandAsc() {
		return drandAsc;
	}

	public void setDrandAsc(String drandAsc) {
		this.drandAsc = drandAsc;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setBzrComments(String bzrComments) {
		this.bzrComments = bzrComments;
	}

	public String getBzrComments() {
		return bzrComments;
	}

	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}

	public String getPublishStatus() {
		return publishStatus;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getClassCode() {
		return classCode;
	}

	
}
