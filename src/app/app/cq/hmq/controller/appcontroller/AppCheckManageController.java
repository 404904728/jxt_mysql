/**
 * Limit
 *
 */
package app.cq.hmq.controller.appcontroller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import app.cq.hmq.pojo.leave.CheckIn;
import app.cq.hmq.pojo.teachertopic.TeacherTopic;
import app.cq.hmq.pojo.teachertopic.teachertopicre;
import app.cq.hmq.service.appservice.AppCheckManageService;
import core.cq.hmq.annotation.ControllerAnn;
import core.cq.hmq.controller.core.BaseController;
import core.cq.hmq.modal.AjaxMsg;

/**
 * @author Limit
 *
 */
@Controller
@RequestMapping(value = "/appCheck")
public class AppCheckManageController extends BaseController {
	
	@Autowired
	private AppCheckManageService appCheckService;
	
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-11 上午11:43:37
	 *@version 1.0
	 *@Description 根据班级ID 教师ID查询科目名称
	 *
	 *@param lk
	 *@param classId
	 *@param teaId
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "queryTeacherName_")
	@ControllerAnn(toLogon = false)
	@ResponseBody
	public String queryTeacherName(String lk, Long classId,Long teaId) {
		return appCheckService.queryTeacherName(lk, classId,teaId);
	}
	
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-11 下午3:55:21
	 *@version 1.0
	 *@Description 存储考勤信息
	 *
	 *@param lk
	 *@param ck
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "saveCheckContent_")
	@ControllerAnn(toLogon = false, interval = 3)
	@ResponseBody
	public AjaxMsg saveCheckContent(String lk,CheckIn ck) {
		return appCheckService.saveCheckContent(lk, ck);
	}
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-14 上午9:40:03
	 *@version 1.0
	 *@Description 家长查询学生考勤
	 *
	 *@param lk
	 *@param classId
	 *@param StuId
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "queryStudentCHeck_")
	@ControllerAnn(toLogon = false)
	@ResponseBody
	public List<?> queryStudentCHeck(String lk,Long classId, String StuId) {
		return appCheckService.queryStudentCHeck(lk, classId, StuId);
	}
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-14 下午12:03:17
	 *@version 1.0
	 *@Description 查询班级中的心语信息
	 *
	 *@param lk
	 *@param classId
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "queryTeacherTopic_")
	@ControllerAnn(toLogon = false)
	@ResponseBody
	public List<Map<String, Object>> queryTeacherTopic(String lk,Long classId,int page,int rows) {
		return appCheckService.queryTeacherTopic(lk, classId,page,rows);
	}
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-14 下午2:37:59
	 *@version 1.0
	 *@Description 查询具体一个心语的回复信息
	 *
	 *@param lk
	 *@param topId
	 *@param start
	 *@param end
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "queryTeacherTopicRecounts_")
	@ControllerAnn(toLogon = false)
	@ResponseBody
	public List<Map<String, Object>> queryTeacherTopicRecounts(String lk,Long topId,Integer start,Integer end) {
		return appCheckService.queryTeacherTopicRecounts(lk, topId,start, end);
	}
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-14 下午3:30:20
	 *@version 1.0
	 *@Description 新增教师心语
	 *
	 *@param lk
	 *@param top
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "saveTeacherTopic_")
	@ControllerAnn(toLogon = false, interval = 3)
	@ResponseBody
	public AjaxMsg saveTeacherTopic(String lk,TeacherTopic top) {
		return appCheckService.saveTeacherTopic(lk, top);
	}
	
	@RequestMapping(params = "reTeacherTopic")
	@ControllerAnn(toLogon = false, interval = 3)
	@ResponseBody
	public AjaxMsg reTeacherTopic(String lk,teachertopicre re) {
		return appCheckService.reTeacherTopic(lk, re);
	}
	
}
