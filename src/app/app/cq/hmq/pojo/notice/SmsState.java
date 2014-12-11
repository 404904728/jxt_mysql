package app.cq.hmq.pojo.notice;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import core.cq.hmq.service.util.JsonDateSerializer;

/**
 * 159653393$$$$$15123870585$$$$$2014-06-09
 * 11:51:25$$$$$1$$$$$DELIVRD$$$$$2014-06-09 11:52:08|||
 *
 * @author cqmonster
 */
@Entity
public class SmsState {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 报告ID
     */
    private String smsId;

    /**
     * 接收人电话
     */
    private String recipient;

    /**
     * 发送者
     */
    private Long teacherInfo;

    /**
     * 发送者名称
     */
    private String name;

    /**
     * 发送时间
     */
    private Date date;

    /**
     * 推送时间
     */
    private String sendTime;

    /**
     * 返回标识时间
     */
    private String markTime;

    /**
     * 报告标识：0，无状态；1，成功；2，失败；3，其他
     */
    private String mark;

    /**
     * 各运营商直接返回的状态报告值
     */
    private String report;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Long getTeacherInfo() {
        return teacherInfo;
    }

    public void setTeacherInfo(Long teacherInfo) {
        this.teacherInfo = teacherInfo;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMarkTime() {
        return markTime;
    }

    public void setMarkTime(String markTime) {
        this.markTime = markTime;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
