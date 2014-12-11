package com.baidu.yun.sample;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageResponse;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;

public class AndroidPushNotificationSample {

	public static void main(String[] args) {

		/*
		 * @brief 推送单播通知(Android Push SDK拦截并解析) message_type = 1 (默认为0)
		 */

		// 1. 设置developer平台的ApiKey/SecretKey
		String apiKey = "VKDmWU8EYhQALyoA12iph1PA";
		String secretKey = "iDu42qxMO1suMYK0URQ3ns2LskZz6QBN";
		ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);

		// 2. 创建BaiduChannelClient对象实例
		BaiduChannelClient channelClient = new BaiduChannelClient(pair);

		// 3. 若要了解交互细节，请注册YunLogHandler类
		channelClient.setChannelLogHandler(new YunLogHandler() {
			@Override
			public void onHandle(YunLogEvent event) {
				System.out.println(event.getMessage());
			}
		});

		try {

			// 4. 创建请求类对象
			// 手机端的ChannelId， 手机端的UserId， 先用1111111111111代替，用户需替换为自己的
			PushUnicastMessageRequest request = new PushUnicastMessageRequest();
			request.setDeviceType(3); // device_type => 1: web 2: pc 3:android
										// 4:ios 5:wp
										// request.setChannelId(Long.parseLong("3633441047554490054"));
			// request.setUserId("773228992587838855");
			request.setChannelId(Long.parseLong("3633441047554490054"));
			request.setUserId("773228992587838855");

			request.setMessageType(1);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", "通知");
			map.put("description", "今天下午全体放假");

			Map<String, Object> maps = new HashMap<String, Object>();
			maps.put("type", 2);
			map.put("custom_content", maps);
			StringWriter sw = new StringWriter();
			ObjectMapper om = new ObjectMapper();
			try {
				om.writeValue(sw, map);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(sw.toString());
			// request.setMessage("{\"title\":\"通知\",\"description\":\"今天下午全体放假\",\"type\":\"2\"}");
			request.setMessage(sw.toString());
			// 5. 调用pushMessage接口
			PushUnicastMessageResponse response = channelClient
					.pushUnicastMessage(request);

			// 6. 认证推送成功
			System.out.println("push amount : " + response.getSuccessAmount());

		} catch (ChannelClientException e) {
			// 处理客户端错误异常
			e.printStackTrace();
		} catch (ChannelServerException e) {
			// 处理服务端错误异常
			System.out.println(String.format(
					"request_id: %d, error_code: %d, error_message: %s",
					e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
		}

	}
}
