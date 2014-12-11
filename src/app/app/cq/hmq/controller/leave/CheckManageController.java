package app.cq.hmq.controller.leave;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import app.cq.hmq.pojo.leave.CheckIn;
import app.cq.hmq.pojo.stuinfo.StudentInfo;
import app.cq.hmq.service.leave.CheckManageService;

import common.cq.hmq.model.JqGridData;
import common.cq.hmq.model.JqPageModel;
import core.cq.hmq.controller.core.BaseController;
import core.cq.hmq.modal.AjaxMsg;

@Controller
@RequestMapping("/checkManage")
public class CheckManageController extends BaseController{
	
	@Resource
	private CheckManageService checkManageService;
	
	@RequestMapping(params = "findCheckManageInfos")
	@ResponseBody
	public JqGridData<CheckIn> findCheckManageInfos(JqPageModel jqPagemodel,Long classId){
		return checkManageService.findCheckManageInfos(jqPagemodel,classId);
	}
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-6-13 下午3:01:52
	 *@version 1.0
	 *@Description 家长查询自己学生的出勤状况
	 *
	 *@param jqPagemodel
	 *@param classId
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "queryStuCheck")
	@ResponseBody
	public JqGridData<?> queryStuCheck(JqPageModel jqPagemodel){
		return checkManageService.queryStuCheck(jqPagemodel);
	}
	
	/**
	 * 统计分析
	 * 
	 * @return
	 */
	@RequestMapping(params = "checkanaly")
	public ModelAndView checkanaly() {
		ModelAndView mav = new ModelAndView("app/checkmanage/checkanaly");
		return mav;
	}
	
	@RequestMapping(params = "getStudentInfo")
	@ResponseBody
	public Object getStudentInfo(Long sId,Long classId,String sDate,String eDate){
		return checkManageService.getStudentInfo(sId,classId,sDate,eDate);
	}
	
	
	@RequestMapping(params = "getClassInfo")
	@ResponseBody
	public Object getClassInfo(Long classId,String sDate,String eDate){
		return checkManageService.getClassInfo(classId,sDate,eDate);
	}
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-4 上午11:48:28
	 *@version 1.0
	 *@Description 跳转到新增考勤页面
	 *
	 *@param classId
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "initAddCheck_")
	public ModelAndView initAddCheck(Long classId){
		return checkManageService.initAddCheck(classId);
	}
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-4 上午11:48:28
	 *@version 1.0
	 *@Description 跳转到某一次具体考勤页面
	 *
	 *@param classId
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "queryCheckByClass_")
	public ModelAndView queryCheckByClass(Long checkId){
		return checkManageService.queryCheckByClass(checkId);
	}
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-4 上午11:48:28
	 *@version 1.0
	 *@Description 跳转到考勤页面初始化加载学生信息
	 *
	 *@param classId
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "initaddCheckToqueryStu_")
	@ResponseBody
	public List<StudentInfo> initAddCheckToqueryStu(Long classId){
		return checkManageService.initAddCheckToqueryStu(classId);
	}
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-8 上午11:15:29
	 *@version 1.0
	 *@Description 保存考勤信息
	 *
	 *@param stuId
	 *@param title
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "saveCheckInfos_")
	@ResponseBody
	public AjaxMsg saveCheckInfos(String stuId,String title, int stuCount, int unReachCount,Long classId){
		AjaxMsg saveCheckInfos = null;
		try {
			saveCheckInfos = checkManageService.saveCheckInfos( stuId, title, stuCount, unReachCount,classId);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return saveCheckInfos;
	}
}
