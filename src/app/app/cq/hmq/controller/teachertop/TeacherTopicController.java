package app.cq.hmq.controller.teachertop;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import app.cq.hmq.pojo.teachertopic.TeacherTopic;
import app.cq.hmq.service.teachertop.TeacherTopicService;

import common.cq.hmq.model.JqGridData;
import common.cq.hmq.model.JqPageModel;

import core.cq.hmq.controller.core.BaseController;
import core.cq.hmq.modal.AjaxMsg;

@Controller
@RequestMapping("/teachertopic")
public class TeacherTopicController extends BaseController{

	@Resource
	TeacherTopicService teacherTopicService;
	
	@RequestMapping(params="findAllTopic")
	@ResponseBody
	public JqGridData<?> findAllTopic(JqPageModel jqPagemodel,Long classId){
		return teacherTopicService.findAllTopic(jqPagemodel, classId);
	}
	
	@RequestMapping(params="findTeacherTopicByid")
	@ResponseBody
	public Map<String, Object> findTeacherTopicByid(Long pid,int start, int end){
		if(null != pid){
			return teacherTopicService.findTeacherTopicByid(pid, start, end);
		}
		return null;
	}
	
	@RequestMapping(params="reTeacherTopicByid")
	@ResponseBody
	public AjaxMsg reTeacherTopicByid(Long id,String content){
		if(null != id){
			return teacherTopicService.reTeacherTopicByid(id, content, currentSessionModel());
		}
		return null;
	}
	
	
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-10 下午1:31:30
	 *@version 1.0
	 *@Description 教师心语新增页面初始化方法
	 *
	 *@param classId
	 *@return
	 *
	 *
	 */
	@RequestMapping(params="initAddTeacherTopic_")
	public ModelAndView initAddTeacherTopic(Long classId){
		ModelAndView view = new ModelAndView("app/teachertopic/teachertopiccreate");
		view.addObject("classId", classId);
		return view;
	}
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-10 下午1:31:30
	 *@version 1.0
	 *@Description 教师心语新增
	 *
	 *@param classId
	 *@return
	 *
	 *
	 */
	@RequestMapping(params="saveTeacherTopic_")
	@ResponseBody
	public Map<String, Object> saveTeacherTopic(HttpServletRequest request, TeacherTopic pic){
		return teacherTopicService.saveTeacherTopic(request, pic);
	}
	
}
