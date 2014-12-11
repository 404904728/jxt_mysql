package common.cq.hmq.service;

import app.cq.hmq.pojo.notice.SmsStateContent;
import org.springframework.stereotype.Service;

import app.cq.hmq.pojo.notice.SmsState;
import core.cq.hmq.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SmsService extends BaseService {

    @Transactional
    public void save(SmsState sms) {
        dao.insert(sms);

    }

    @Transactional
    public void saveContent(SmsStateContent content) {
        dao.insert(content);
    }

    // 159653393$$$$$15123870585$$$$$2014-06-09
    // 11:51:25$$$$$1$$$$$DELIVRD$$$$$2014-06-09 11:52:08|||
    @SuppressWarnings("unchecked")
    /**
     * 更新短信发送状态报告
     */
    @Transactional
    public void update(String smsId, String tel, String sendTime, String mark,
                       String report, String markTime) {
        int i = dao.getHelperDao().excute(
                "update SmsState_t set sendTime_f='" + sendTime
                        + "',markTime_f='" + markTime + "',mark_f='" + mark
                        + "',report_f='" + report
                        + "' where smsId_f=? and recipient_f=?", smsId, tel);
    }
}
