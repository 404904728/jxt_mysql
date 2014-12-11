package test.cq.hmq;

import java.util.Calendar;

public class T {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// String
		// smsMsg="159653393$$$$$15123870585$$$$$2014-06-09 11:51:25$$$$$1$$$$$DELIVRD$$$$$2014-06-09 11:52:08|||159653393$$$$$15123870585$$$$$2014-06-09 11:51:25$$$$$1$$$$$DELIVRD$$$$$2014-06-09 11:52:08|||";
		// LogUtil.getLog("获取短信发送状态").info(smsMsg);
		// String[] smsMsgs = smsMsg.split("\\|\\|\\|");
		// for (String strMsg : smsMsgs) {
		// String[] strMsgs = strMsg.split("\\$\\$\\$\\$\\$");
		// System.out.println(strMsgs[0]);
		// System.out.println(strMsgs[1]);
		// System.out.println(strMsgs[2]);
		// System.out.println(strMsgs[3]);
		// System.out.println(strMsgs[4]);
		// System.out.println(strMsgs[5]);
		// }
		// System.out.println("尊敬的家长：学校采用了先进家校沟通平台保证与您及时有效沟通，建议家长都安装手机客户端家校通。重要信息也都将会通过手机客户端更精彩的发布，也能有效克服短信通讯的延迟及手机网络环境影响接收问题。用户名是你登记的手机号码，初始密码123456，苹果手机下载地址：https://itunes.apple.com/cn/app/jia-xiao-tong-jia-zhang-duan/id866492260?mt=8，智能手机下载地址http://125.71.236.224:8088/FamiliesSchoolConmunication.apk ，也可以电脑登陆http://125.71.236.224:8088扫描家长端对应二维码下载安装。".length());
		
		if (Calendar.HOUR >= 22 || Calendar.HOUR <= 8) {
			System.out.println("asdasdasd");
		}else{
			System.out.println("123123");
		}
	}

}
