package common.cq.hmq.util;

import java.util.Date;

import app.cq.hmq.pojo.notice.SmsState;
import app.cq.hmq.pojo.notice.SmsStateContent;

import common.cq.hmq.service.SmsService;

import core.cq.hmq.dao.util.BeanUtil;
import core.cq.hmq.util.tools.DateUtil;
import core.cq.hmq.util.tools.LogUtil;
import core.cq.hmq.util.tools.StringUtil;

public class SendSmsThread implements Runnable {

    private String tel;

    private String smsId;

    private String content;

    private Long userId;

    private String name;

    private Long[] args;

    @Override
    public void run() {
        // TODO Auto-generated method stub
        SmsService smsService = (SmsService) BeanUtil.getBean("smsService");

        // 保存内容
        SmsStateContent smsStateContent = new SmsStateContent();
        smsStateContent.setSmsId(smsId);
        smsStateContent.setContent(content);
        if (null != args) {
            if (args.length == 1) {
                smsStateContent.setNoticeId(args[0]);
            } else if (args.length == 2) {
                smsStateContent.setScoreId(args[1]);
            }
        }
        smsStateContent.setDate(new Date());
        smsService.saveContent(smsStateContent);

        // 保存短信状态
        Date strDate = new Date();
        String[] tels = tel.split(",");
        for (String string : tels) {
            SmsState sms = new SmsState();
            sms.setSmsId(smsId);
            sms.setName(name);
            sms.setDate(strDate);
            sms.setTeacherInfo(userId);
            sms.setRecipient(string);
            smsService.save(sms);
        }
    }

    public SendSmsThread(String tel, String smsId, String content,
                         Long[] args, Long userId, String name) {
        this.tel = tel;
        this.smsId = smsId;
        this.content = content;
        this.userId = userId;
        this.name = name;
        this.args = args;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArgs(Long[] args) {
        this.args = args;
    }

    public Long[] getArgs() {
        return args;
    }
}
