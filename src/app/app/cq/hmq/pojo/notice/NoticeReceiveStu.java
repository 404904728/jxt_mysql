package app.cq.hmq.pojo.notice;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * 家长接收通知
 * 
 * @author cqmonster
 * 
 */
@Entity
public class NoticeReceiveStu {

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
	 * 哪一条通知
	 */
	@ManyToOne
	private Notice notice;

	/**
	 * 学生
	 */
	private Long studentInfo;

	/**
	 * 是否看过
	 */
	private boolean look = false;

	/**
	 * 是否删除
	 */
	private boolean del = false;

	/**
	 * 是否推送
	 */
	private boolean push = false;

	public Notice getNotice() {
		return notice;
	}

	public void setNotice(Notice notice) {
		this.notice = notice;
	}

	public boolean isLook() {
		return look;
	}

	public void setLook(boolean look) {
		this.look = look;
	}

	public boolean isDel() {
		return del;
	}

	public void setDel(boolean del) {
		this.del = del;
	}

	public boolean isPush() {
		return push;
	}

	public void setPush(boolean push) {
		this.push = push;
	}

	public Long getStudentInfo() {
		return studentInfo;
	}

	public void setStudentInfo(Long studentInfo) {
		this.studentInfo = studentInfo;
	}
}
