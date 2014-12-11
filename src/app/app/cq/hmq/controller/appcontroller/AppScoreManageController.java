/**
 * Limit
 *
 */
package app.cq.hmq.controller.appcontroller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import app.cq.hmq.service.appservice.AppScoreManageService;

import core.cq.hmq.annotation.ControllerAnn;
import core.cq.hmq.controller.core.BaseController;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/appScore")
public class AppScoreManageController extends BaseController {
	
	
	@Autowired
	private AppScoreManageService appScoreManageService;
	
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-3-29 下午3:55:24
	 *@version 1.0
	 *@Description 查询具体某个班级的所有科目信息
	 *
	 *@param lk
	 *@param classId
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "classSubject_")
	@ControllerAnn(toLogon = false)
	@ResponseBody
	public List<Map<String, Object>> queryClassSubject(String lk, String classId) {
		return appScoreManageService.queryClassSubject(lk, classId);
	}
	
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-3-22 下午3:19:59
	 *@version 1.0
	 *@Description 获取具体某个班级中某一科目所有学生的信息
	 *
	 *@param lk
	 *@param ut
	 *@return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 *
	 *
	 */
	@RequestMapping(params = "studentsScore")
	@ControllerAnn(toLogon = false)
	@ResponseBody
	public Map<String, Object> queryStudentScore(String lk, String classID,String subId, String title) throws JsonParseException, JsonMappingException, IOException {
		return appScoreManageService.queryStudentScore(lk, classID, subId,title);
	}
	
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-1 上午9:47:30
	 *@version 1.0
	 *@Description 查询一颗考试中标题
	 *
	 *@param lk
	 *@param classID
	 *@param subId
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "queryStudentScoreTitle")
	@ControllerAnn(toLogon = false)
	@ResponseBody
	public List<Map<String, Object>> queryStudentScoreTitle(String lk, String classID) {
		return appScoreManageService.queryStudentScoreTitle(lk, classID);
	}
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-3 下午6:07:23
	 *@version 1.0
	 *@Description 家长登录后查询自己学生的总成绩
	 *
	 *@param lk
	 *@param stuId
	 *@param StuName
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "queryAllScoresByHome")
	@ControllerAnn(toLogon = false)
	@ResponseBody
	public Map<String, Object> queryAllScores(String lk, Long stuId, String StuName){
		return appScoreManageService.queryStudentScoreTitle(lk, stuId,StuName);
	}
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-3 下午6:07:23
	 *@version 1.0
	 *@Description 家长登录后查询自己学生的各科成绩
	 *
	 *@param lk
	 *@param stuId
	 *@param StuName
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "queryOneStudentScore")
	@ControllerAnn(toLogon = false,interval=3)
	@ResponseBody
	public List<Map<String, Object>> queryOneStudentScore(String lk, Long classId, String classValue,String stuName){
		return appScoreManageService.queryOneStudentScore(lk, classId,classValue,stuName);
	}
	
	
}
