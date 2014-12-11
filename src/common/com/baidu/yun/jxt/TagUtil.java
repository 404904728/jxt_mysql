package com.baidu.yun.jxt;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.DeleteTagRequest;
import com.baidu.yun.channel.model.SetTagRequest;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;

public class TagUtil {

	/**
	 * 为用户设置标签
	 * 
	 * @param tagName
	 *            标签名称(角色名称)
	 * @param userId
	 */
	public static void setTag(String tagName, String userId) {
		ChannelKeyPair pair = new ChannelKeyPair(ConstantUtil.apiKey,
				ConstantUtil.secretKey);
		BaiduChannelClient channelClient = new BaiduChannelClient(pair);
		channelClient.setChannelLogHandler(new YunLogHandler() {
			@Override
			public void onHandle(YunLogEvent event) {
				// TODO Auto-generated method stub
				System.out.println(event.getMessage());
			}
		});
		SetTagRequest request = new SetTagRequest();
		request.setTag(tagName);
		request.setUserId(userId);
		try {
			channelClient.setTag(request);
		} catch (ChannelClientException e) {
			e.printStackTrace();
		} catch (ChannelServerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 当userId为空时 会删除服务端的标签，当userId不为空时 将删除用户设置的标签
	 * 
	 * @param tag
	 *            标签名称(角色名称)
	 * @param userId
	 * 
	 */
	public static void deleteTag(String tag, String userId) {
		ChannelKeyPair pair = new ChannelKeyPair(ConstantUtil.apiKey,
				ConstantUtil.secretKey);
		BaiduChannelClient channelClient = new BaiduChannelClient(pair);
		channelClient.setChannelLogHandler(new YunLogHandler() {
			@Override
			public void onHandle(YunLogEvent event) {
				// TODO Auto-generated method stub
				System.out.println(event.getMessage());
			}
		});
		DeleteTagRequest request = new DeleteTagRequest();
		request.setTag(tag);
		if (userId != null) {
			request.setUserId(userId);
		}
		try {
			channelClient.deleteTag(request);
		} catch (ChannelClientException e) {
			e.printStackTrace();
		} catch (ChannelServerException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

	}
}
