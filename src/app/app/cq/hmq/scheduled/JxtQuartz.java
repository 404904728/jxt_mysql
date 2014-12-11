package app.cq.hmq.scheduled;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import app.cq.hmq.service.grab.GrabService;

import common.cq.hmq.service.SmsService;
import common.cq.hmq.util.SendSMS;

import core.cq.hmq.dao.util.BeanUtil;
import core.cq.hmq.util.tools.LogUtil;
import core.cq.hmq.util.tools.StringUtil;

/**
 * 自动取短线发送状态
 *
 * @author cqmonster
 */
@Component
public class JxtQuartz {


    /**
     * 取短信
     *
     * @throws HttpException
     * @throws IOException
     */
	@Scheduled(cron = "0/15 * * * * ?")//每15秒取一次
    public void run() throws HttpException, IOException {
        String smsMsg = SendSMS.reportGet();
        if (StringUtil.isEmpty(smsMsg)) {
            LogUtil.getLog("获取状态标识").info("本次获取短信标识为空");
            return;
        }
        //159653393$$$$$15123870585$$$$$2014-06-09 11:51:25$$$$$1$$$$$DELIVRD$$$$$2014-06-09 11:52:08|||
        LogUtil.getLog("获取短信发送状态").info(smsMsg);
        String[] smsMsgs = smsMsg.split("\\|\\|\\|");
        for (String strMsg : smsMsgs) {
            String[] strMsgs = strMsg.split("\\$\\$\\$\\$\\$");
            SmsService smsService = (SmsService) BeanUtil
                    .getBean("smsService");
            smsService.update(strMsgs[0], strMsgs[1], strMsgs[2], strMsgs[3], strMsgs[4], strMsgs[5]);
        }
    }

    /**
     * 取新闻
     *
     * @throws HttpException
     * @throws IOException
     */
    @Scheduled(cron = "0 0 23 * * ?")//每天晚上11点取
    public void GrabQ() throws HttpException, IOException {
        GrabService grabService = (GrabService) BeanUtil.getBean("grabService");
        grabService.insert();
    }

}
