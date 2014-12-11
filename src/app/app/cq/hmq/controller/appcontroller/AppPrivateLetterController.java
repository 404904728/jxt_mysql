/**
 * Limit
 *
 */
package app.cq.hmq.controller.appcontroller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;

import app.cq.hmq.pojo.letter.PrivateLetter;
import app.cq.hmq.pojo.letter.PrivateLetterRe;
import app.cq.hmq.service.appservice.AppPrivateLetterService;

import common.cq.hmq.model.JqGridData;

import core.cq.hmq.annotation.ControllerAnn;
import core.cq.hmq.controller.core.BaseController;
import core.cq.hmq.modal.AjaxMsg;

/**
 * @author Administrator
 * 
 *私信借口
 */
@Controller
@RequestMapping(value = "/appLetter")
public class AppPrivateLetterController extends BaseController {
	
	@Autowired
	private AppPrivateLetterService appLetterService;
	
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-21 上午10:30:53
	 *@version 1.0
	 *@Description 保存发送的私信信息
	 *
	 *@param lk
	 *@param letter
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "savePrivateLetter")
	@ControllerAnn(toLogon = false, interval = 3)
	@ResponseBody
	public AjaxMsg savePrivateLetter(String lk, PrivateLetter letter) {
		return appLetterService.savePrivateLetter(lk, letter);
	}
	
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-23 下午3:01:20
	 *@version 1.0
	 *@Description 查询私信列表
	 *
	 *@param lk
	 *@param userId
	 *@param userType
	 *@param ps
	 *@param pn
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "queryPrivateLetter")
	@ControllerAnn(toLogon = false)
	@ResponseBody
	public JqGridData<?> queryPrivateLetter(String lk, Long userId,String userType,int ps,int pn) {
		return appLetterService.queryPrivateLetter(lk, userId,userType,ps,pn);
	}
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-24 上午9:55:42
	 *@version 1.0
	 *@Description 查询一对一具体私信信息
	 * 
	 *@param lk
	 *@param userId
	 *@param userType
	 *@param start
	 *@param end
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "queryOnePrivateLetter")
	@ControllerAnn(toLogon = false)
	@ResponseBody
	public Map<String, Object> queryOnePrivateLetter(String lk, Long userId,String userType,int start,int end,Long plId) {
		return appLetterService.queryOnePrivateLetter(lk, userId,userType,start,end,plId);
	}
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-24 上午11:25:11
	 *@version 1.0
	 *@Description 保存一对一具体私信信息
	 *
	 *@param lk
	 *@param re
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "saveOnePrivateLetter")
	@ControllerAnn(toLogon = false, interval = 1)
	@ResponseBody
	public AjaxMsg saveOnePrivateLetter(String lk, PrivateLetterRe re) {
		AjaxMsg saveOnePrivateLetter = null;
		try {
			saveOnePrivateLetter = appLetterService.saveOnePrivateLetter(lk, re);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return saveOnePrivateLetter;
	}
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-5-9 下午2:28:16
	 *@version 1.0
	 *@Description 修改密码
	 *
	 *@param lk
	 *@param op
	 *@param np
	 *@param id
	 *@param userType
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "updatePassword")
	@ControllerAnn(toLogon = false, interval = 1)
	@ResponseBody
	public AjaxMsg updatePassword(String lk, String op,String np,Long id,String userType) {
		return appLetterService.updatePassword(lk, op, np,id,userType);
	}
	
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-5-9 下午2:48:22
	 *@version 1.0
	 *@Description  发送验证码
	 *
	 *@param lk
	 *@param no
	 *@param userType
	 *
	 *
	 */
	@RequestMapping(params = "sendMessage")
	@ControllerAnn(toLogon = false, interval = 1)
	@ResponseBody
	public AjaxMsg regNoExists(String lk, String no,String userType,HttpServletRequest request) {
		return appLetterService.regNoExists(lk, no,userType,request);
	}
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-5-9 下午3:07:54
	 *@version 1.0
	 *@Description 重置密码
	 *
	 *@param lk
	 *@param reg
	 *@param no
	 *@param userType
	 *@param request
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "resetPassword")
	@ControllerAnn(toLogon = false, interval = 1)
	@ResponseBody
	public AjaxMsg resetPassword(String lk, String reg,String no,String userType,HttpServletRequest request,String np) {
		return appLetterService.resetPassword(lk, reg, no,userType,request,np);
	}
}
