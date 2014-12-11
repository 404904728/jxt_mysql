package app.cq.hmq.pojo.notice;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import core.cq.hmq.service.util.JsonDateSerializer;
import app.cq.hmq.pojo.teacherinfo.TeacherInfo;

/**
 * 班级通知
 *
 * @author cqmonster
 */
@Entity
public class Notice {

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
     * 通知标题
     */
    private String title;

    /**
     * 通知时间
     */
    private Date date;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 通知发起人ID
     */
    @ManyToOne
    private TeacherInfo teacherInfo;

    /**
     * 通知等级 0，普通通知 1：重要通知 2：紧急通知
     */
    private int grade = 0;

    /**
     * 通知 大类 0：作业通知 1：班级通知 2：教务处通知 3：学生处通知 4年级组通知 5招生办通知  6行政办通知 7研修室通知
     */
    private Integer genre = 0;

    /**
     * 是否发送短信
     */
    private Boolean shortMsg = false;

    /**
     * 是否草稿 false 不是 true 是
     */
    private Boolean draft = false;

    @Transient
    private Boolean selfsms = false;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TeacherInfo getTeacherInfo() {
        return teacherInfo;
    }

    public void setTeacherInfo(TeacherInfo teacherInfo) {
        this.teacherInfo = teacherInfo;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public Integer getGenre() {
        return genre;
    }

    public void setGenre(Integer genre) {
        this.genre = genre;
    }

    public Boolean getDraft() {
        return draft;
    }

    public void setDraft(Boolean draft) {
        this.draft = draft;
    }

    public Boolean getShortMsg() {
        return shortMsg;
    }

    public void setShortMsg(Boolean shortMsg) {
        this.shortMsg = shortMsg;
    }

    public Boolean getSelfsms() {
        return selfsms;
    }

    public void setSelfsms(Boolean selfsms) {
        this.selfsms = selfsms;
    }
}
