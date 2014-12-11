/**
 * Limit
 *
 */
package app.cq.hmq.service.cjmanage;

import java.util.List;

import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.jxt.PushUtil;
import com.baidu.yun.jxt.model.PushMsgModel;
import common.cq.hmq.util.SendSMS;

import core.cq.hmq.util.tools.StringUtil;

/**
 * @author Administrator
 *
 */
public class ScoreManagePush implements Runnable {
	
	private List noticeList;
	
	private PushMsgModel model;
	
	private String userName;
	
	private Long userId;
	
	
	public String getUseName() {
		return userName;
	}

	public void setUseName(String useName) {
		this.userName = useName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List getNoticeList() {
		return noticeList;
	}

	public void setNoticeList(List noticeList) {
		this.noticeList = noticeList;
	}
	
	
	public PushMsgModel getModel() {
		return model;
	}

	public void setModel(PushMsgModel model) {
		this.model = model;
	}

	@Override
	public void run() {
		Object[] object = null;
		for (int i = 0; i < noticeList.size(); i++) {
			object = (Object[]) noticeList.get(i);
			if(null != object){
				
				if(null != object[3] && !"".equals(object[3])){//在这里写发送短信的方法，短信内容是家长您好！有新成绩发布，请进入APP查看详情
					SendSMS.sendMsg("家长您好！有新成绩发布，请进入APP查看详情", object[3].toString(),userId,userName);
				}
				//发布通知
				
				try {
					if(StringUtil.isEmpty(object[2])){ 
						continue;
					}
					if(null != object[0]
						&& !"".equals(object[0])
						&& null != object[1]
						&& !"".equals(object[1])){//判断channelid_f, userid_f, android_f 都非空然后进行信息推送(push)
						
						if("1".equals(object[2].toString())){
							PushUtil.androidPushUserMsg(Long.parseLong(object[0].toString()), object[1].toString(), model);
						}else if("0".equals(object[2].toString())){
							PushUtil.iosPushUserMsg(Long.parseLong(object[0].toString()), object[1].toString(), false, model.getTitle(),null);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}

	}

}
