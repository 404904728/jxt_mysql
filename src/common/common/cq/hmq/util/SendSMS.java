package common.cq.hmq.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class SendSMS {

    public static void main(String[] args) throws IOException {
        // 159893563 159888633
        //sendMsg("[暑假办公注意事项提醒]各位班主任，在愉快的暑假期间需要你们随时抽空上网看班主任群信息内容，保持与学校的沟通", "18980097600");
        // SendSMS.sendSMSPost("15123308745", "教务处通知：请全校教师马上到教务处开会");
        reportGet();
        //sendMsg1("[暑假办公注意事项提醒]各位班主任，在愉快的暑假期间需要你们随时抽空上网看班主任群信息内容，保持与学校的沟通", "18980097600");
    }

    /**
     * 发送短信
     *
     * @param content 短信内容 70个字以内
     * @param tel     电话号码以英文逗号分隔
     * @return 返回200成功 返回0 电话号码或内容为空
     */
    public static int sendMsg(String content, String tel, Long userId, String username,Long... noticeId) {
        if (java.util.ResourceBundle.getBundle("config").getString("sortMsg")
                .equals("false")) {
            return 0;
        }
        String result = "";
        if (tel.split(",").length > 50) {
            result = sendSMSPost(tel, content);
        } else {

            result = sendSMSGet(tel, content);
        }
        if (Integer.parseInt(result) > 0) {
            //String tel, String smsId, String content, long userId, String name
            //启动线程保存发送的电话号码
            SendSmsThread sst = new SendSmsThread(tel, result, content, noticeId.length == 0 ? null : noticeId, userId, username);
            Thread thread = new Thread(sst);
            thread.start();
            return 200;
        } else {
            return Integer.parseInt(result);
        }
    }

    /**
     * get方式
     *
     * @param Mobile
     * @param Content
     * @return
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    public static String sendSMSGet(String Mobile, String Content) {
        URL url = null;
        String CorpID = "LKSDK0001369";
        String Pwd = "232380";
        String send_content;
        try {
            send_content = URLEncoder.encode(
                    (Content).replaceAll("<br/>", " "), "GBK");
            url = new URL("http://mb345.com:999/ws/BatchSend2.aspx?CorpID="
                    + CorpID + "&Pwd=" + Pwd + "&Mobile=" + Mobile
                    + "&Content=" + send_content + "&Cell=&SendTime=");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        BufferedReader in = null;
        String line = "";
        String result = "";
        try {
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("异常");
        }
        System.out.println(result);
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param Mobile       电话号码
     * @param send_content 内容
     * @return 所代表远程资源的响应结果
     */
    public static String sendSMSPost(String Mobile, String send_content) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        String CorpID = "LKSDK0001369";
        String Pwd = "232380";
        try {
            URL realUrl = new URL("http://mb345.com:999/ws/BatchSend2.aspx");
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print("CorpID="
                    + CorpID
                    + "&Pwd="
                    + Pwd
                    + "&Mobile="
                    + Mobile
                    + "&Content="
                    + URLEncoder.encode(
                    (send_content).replaceAll("<br/>", " "),
                    "GBK") + "&Cell=&SendTime=");
            out.flush();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println(result);
        return result;
    }

    /**
     * 159653393$$$$$15123870585$$$$$2014-06-09
     * 11:51:25$$$$$1$$$$$DELIVRD$$$$$2014-06-09 11:52:08|||
     *
     * @return
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    public static String reportGet() throws MalformedURLException,
            UnsupportedEncodingException {
        URL url = null;
        String CorpID = "LKSDK0001369";
        String Pwd = "232380";
        url = new URL("http://mb345.com:999/ws/GetReportSMS.aspx?CorpID="
                + CorpID + "&Pwd=" + Pwd);
        BufferedReader in = null;
        String line = "";
        String result = "";
        try {
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发送短信
     *
     * @param content 短信内容 70个字以内
     * @param tel     电话号码以英文逗号分隔
     * @return 返回200成功 返回0 电话号码或内容为空
     */
    public static int sendMsg1(String content, String tel) {
//		if (java.util.ResourceBundle.getBundle("config").getString("sortMsg")
//				.equals("false")) {
//			return 0;
//		}
//		if (content == null || content.trim().equals("")) {
//			return 0;
//		}
//		if (StringUtil.isEmpty(tel)) {
//			return 0;
//		}
        HttpClient client = new HttpClient();
        client.getHostConfiguration().setHost("118.244.214.125", 8888, "http");
        // client.getHostConfiguration().setHost("118.244.214.125:8888/sms.aspx");
        HttpMethod method = getPostMethod(content, tel);// 使用POST方式提交数据
        try {
            client.executeMethod(method);
        } catch (HttpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 打印服务器返回的状态
        System.out.println(method.getStatusLine());
        // 打印结果页面
        String response = null;
        try {
            response = new String(method.getResponseBodyAsString().getBytes(
                    "UTF-8"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 打印返回的信息
        System.out.println(response);
        method.releaseConnection();
        return method.getStatusCode();
    }

    private static class UTF8PostMethod extends PostMethod {
        public UTF8PostMethod(String url) {
            super(url);
        }

        @Override
        public String getRequestCharSet() {
            // return super.getRequestCharSet();
            return "UTF-8";
        }
    }

    //http://sms.mdjc.net.cn:8888/Index.aspx
    private static HttpMethod getPostMethod(String msg, String tel) {
        PostMethod post = new UTF8PostMethod("/sms.aspx");
        NameValuePair userid = new NameValuePair("userid", "56");
        NameValuePair account = new NameValuePair("account", "lantu");
        NameValuePair password = new NameValuePair("password", "88117298");
        System.out.println("发送的电话：" + tel);
        NameValuePair mobile = new NameValuePair("mobile", tel);
        NameValuePair content = new NameValuePair("content", msg);
        NameValuePair sendTime = new NameValuePair("sendTime", null);
        NameValuePair action = new NameValuePair("action", "send");
        post.setRequestBody(new NameValuePair[]{userid, account, password,
                mobile, content, sendTime, action});
        return post;
    }
}
