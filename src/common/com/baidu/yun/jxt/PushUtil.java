package com.baidu.yun.jxt;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.PushBroadcastMessageRequest;
import com.baidu.yun.channel.model.PushTagMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageResponse;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.jxt.model.PushMsgModel;

/**
 * 推送通知工具类
 *
 * @author cqmonster
 */
public class PushUtil {

    /**
     * android 标签推送
     *
     * @param tag   标签名称 一次一个标签
     * @param model
     * @return 返回true 成功，返回false 失败
     */
    public static int androidPushTagMsg(String tag, PushMsgModel model)
            throws ChannelClientException, ChannelServerException {
        ChannelKeyPair pair = new ChannelKeyPair(ConstantUtil.apiKey,
                ConstantUtil.secretKey);
        BaiduChannelClient channelClient = new BaiduChannelClient(pair);
        return channelClient.pushTagMessage(androidPushTagRequest(tag, model))
                .getSuccessAmount();
    }

    /**
     * 根据用户channelId 与userId推送通知
     *
     * @param channelId
     * @param userId
     * @param model
     * @throws ChannelServerException 服务端异常
     * @throws ChannelClientException 客户端异常
     */
    public static int androidPushUserMsg(Long channelId, String userId,
                                         PushMsgModel model) throws ChannelClientException,
            ChannelServerException {
        ChannelKeyPair pair = new ChannelKeyPair(ConstantUtil.apiKey,
                ConstantUtil.secretKey);
        BaiduChannelClient channelClient = new BaiduChannelClient(pair);
        return channelClient.pushUnicastMessage(
                androidPushUserRequest(channelId, userId, model))
                .getSuccessAmount();
    }

    /**
     * 为android手机的所有人推送通知
     *
     * @param model
     * @return
     * @throws ChannelClientException
     * @throws ChannelServerException
     */
    public static int androidPushAllMsg(PushMsgModel model)
            throws ChannelClientException, ChannelServerException {
        ChannelKeyPair pair = new ChannelKeyPair(ConstantUtil.apiKey,
                ConstantUtil.secretKey);
        BaiduChannelClient channelClient = new BaiduChannelClient(pair);
        return channelClient.pushBroadcastMessage(androidPushAllRequest(model))
                .getSuccessAmount();
    }

    /**
     * ios手机推送消息
     *
     * @param channelId
     * @param userId
     * @param teacher
     * @param title     通知标题与自定义字段 ios自定义字段 ",noticeid:1,userid:2",前面需要先加一个逗号
     * @return
     */
    public static int iosPushUserMsg(Long channelId, String userId,
                                     boolean teacher, String... title) {
        ChannelKeyPair pair = null;
        if (teacher) {
            pair = new ChannelKeyPair(ConstantUtil.apiKey,
                    ConstantUtil.secretKey);
        } else {
            pair = new ChannelKeyPair(ConstantUtil.iosApiKey_s,
                    ConstantUtil.iosSecretKey_s);
        }
        BaiduChannelClient channelClient = new BaiduChannelClient(pair);
        channelClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                System.out.println(event.getMessage());
            }
        });
        int success = -1;
        try {
            PushUnicastMessageRequest request = new PushUnicastMessageRequest();
            request.setDeviceType(4);
            request.setDeployStatus(1);// 2生产版 1.开发板
            request.setChannelId(channelId);
            request.setUserId(userId);
            request.setMessageType(1);

            // "{\"aps\":{\"alert\":\"随便看看\"},\"noticeid\":123}")
            Map<String, Object> map = new HashMap<String, Object>();
            Map<String, String> mapAlert = new HashMap<String, String>();
            mapAlert.put("alert", title[0]);
            map.put("aps", mapAlert);
            if (title[1] != null) {
                map.put(title[1], title[2]);
            }
            ObjectMapper om = new ObjectMapper();
            StringWriter sw = new StringWriter();
            try {
                om.writeValue(sw, map);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(sw.toString());
            request.setMessage(sw.toString());
            PushUnicastMessageResponse response = channelClient
                    .pushUnicastMessage(request);
            success = response.getSuccessAmount();
            System.out.println("push amount : " + response.getSuccessAmount());
        } catch (ChannelClientException e) {
            e.printStackTrace();
        } catch (ChannelServerException e) {
            System.out.println(String.format(
                    "request_id: %d, error_code: %d, error_message: %s",
                    e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
        }
        return success;

    }

    private static PushTagMessageRequest androidPushTagRequest(String tag,
                                                               PushMsgModel model) {
        ObjectMapper objMapper = new ObjectMapper();
        StringWriter sw = new StringWriter();
        try {
            objMapper.writeValue(sw, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PushTagMessageRequest request = new PushTagMessageRequest();
        request.setDeviceType(3);
        request.setTagName(tag);
        request.setMessageType(1);
        request.setMessage(sw.toString());
        return request;
    }

    private static PushUnicastMessageRequest androidPushUserRequest(
            Long channelId, String userId, PushMsgModel model) {
        PushUnicastMessageRequest request = new PushUnicastMessageRequest();
        request.setDeviceType(3);
        request.setChannelId(channelId);
        request.setUserId(userId);
        request.setMessageType(1);
        ObjectMapper objMapper = new ObjectMapper();
        StringWriter sw = new StringWriter();
        try {
            objMapper.writeValue(sw, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setMessage(sw.toString());
        return request;
    }

    private static PushBroadcastMessageRequest androidPushAllRequest(
            PushMsgModel model) {
        PushBroadcastMessageRequest request = new PushBroadcastMessageRequest();
        request.setDeviceType(3);
        request.setMessageType(1);
        ObjectMapper objMapper = new ObjectMapper();
        StringWriter sw = new StringWriter();
        try {
            objMapper.writeValue(sw, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setMessage(sw.toString());
        return request;
    }

    public static void main(String[] args) throws ChannelClientException,
            ChannelServerException {

        PushMsgModel model = new PushMsgModel();
        model.setTitle("校内通告");
        model.setDescription("紧急通知，非典蔓延，学校需要放假2个月");

        /**********************************************************
         * android 标签通知测试 /
         **********************************************************/
        // PushUtil.androidPushTagMsg("班主任", model);

        /**********************************************************
         * android 个人通知测试
         **********************************************************/
        int i = PushUtil.androidPushUserMsg(Long.parseLong("3836686271490396409"),
                "583156198076610451", model);
        System.out.println(i);

        /**********************************************************
         * android 全部发送通知测试 /
         **********************************************************/
        // System.out.println(PushUtil.androidPushAllMsg(model));
    }

}
