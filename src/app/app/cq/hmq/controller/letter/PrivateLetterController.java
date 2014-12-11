package app.cq.hmq.controller.letter;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import app.cq.hmq.pojo.letter.PrivateLetter;
import app.cq.hmq.service.leave.LeaveInfoService;
import app.cq.hmq.service.letter.PrivateLetterService;
import app.cq.hmq.service.subject.SubjectMappingService;

import common.cq.hmq.model.JqGridData;
import common.cq.hmq.model.JqPageModel;
import common.cq.hmq.pojo.sys.User;

import core.cq.hmq.controller.core.BaseController;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.modal.SessionModal;

@Controller
@RequestMapping(value = "/pLetter")
public class PrivateLetterController extends BaseController{
	
	@Resource
	PrivateLetterService pls;
	
	/**
	 * obtain inbox infos
	 * @param session
	 * @param jqPagemodel
	 * @return
	 */
	@RequestMapping(params="obtainInboxInfos")
	@ResponseBody
	public JqGridData<?> obtainInboxInfos(HttpSession session,JqPageModel jqPagemodel){
		return pls.obtainInboxInfos(jqPagemodel, currentSessionModel());
	}
	
	/**
	 * obtain outbox infos
	 * @param session
	 * @param jqPagemodel
	 * @return
	 */
	@RequestMapping(params="obtainOutboxInfos")
	@ResponseBody
	public JqGridData<?> obtainOutboxInfos(HttpSession session,JqPageModel jqPagemodel){
		return pls.obtainOutboxInfos(jqPagemodel, currentSessionModel());
	}
	
	/**
	 * find PrivateLetter By Inbox Id
	 * @param id
	 * @return
	 */
	@RequestMapping(params="findPrivateLetterByInboxId")
	@ResponseBody
	public Map<String, Object> findPrivateLetterByInboxId(Long id,int start,int end){
		return pls.findPrivateLetterByInboxId(id,start,end);
	}
	
	
	/**
	 * re private letter
	 * @param id
	 * @return
	 */
	@RequestMapping(params="rePrivateLetter")
	@ResponseBody
	public AjaxMsg rePrivateLetter(Long id,String content){
		return pls.rePrivateLetter(id,content,currentSessionModel());
	}
	
	@Resource
	private LeaveInfoService leaveInfoService;
	@Autowired
	private SubjectMappingService subjectMappingService;
	
	@RequestMapping(params = "sendLetter")
	public ModelAndView sendLetter() {
		ModelAndView mav = new ModelAndView("app/letter/sendLetterform");
		String type = currentSessionModel().getUserType();
		if(User.STUDENTTYPE.equals(type)){
			mav.addObject("teachers",leaveInfoService.findMyClassTs(currentSessionModel().getOrgId()));
		}
		if(User.TEACHERTYPE.equals(type)){
				mav.addObject("classe", subjectMappingService
						.findClassByTeacherId(currentSessionModel().getId()));
		}
		mav.addObject("usertype", type);
		return mav;
	}
	
	@RequestMapping(params = "savaSendLetter")
	@ResponseBody
	public AjaxMsg savaSendLetter(PrivateLetter pl){
		return pls.savaSendLetter(pl,currentSessionModel());
	}
	
	@RequestMapping(params = "obtainStudentByClassId")
	@ResponseBody
	public JqGridData<?> obtainStudentByClassId(Long cid,JqPageModel jqPagemodel){
		return pls.obtainStudentByClassId(cid,jqPagemodel);
	}
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-14 下午4:51:23
	 *@version 1.0
	 *@Description 查询所有教师
	 *
	 *@param jqPagemodel
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "obtainStudentBySchool")
	@ResponseBody
	public JqGridData<?> obtainStudentBySchool(JqPageModel jqPagemodel){
		return pls.obtainStudentBySchool(jqPagemodel);
	}
	
	@RequestMapping(params = "goto_studentsPage")
	public ModelAndView goto_studentsPage(){
		ModelAndView mv = new ModelAndView("app/letter/lookupStudentByClass");
		return mv;
	}
	
	@RequestMapping(params = "goto_teachersPage")
	public ModelAndView goto_teachersPage(){
		ModelAndView mv = new ModelAndView("app/letter/lookupTeacherBySchool");
		return mv;
	}
	
	/**
	 * 点击head上的条数，加载最近3条
	 * @return
	 */
	@RequestMapping(params = "findNearPrivate")
	@ResponseBody
	public List<Map<String, Object>> findNearPrivate(){
		SessionModal sm = currentSessionModel();
		return pls.findNearPrivate(sm.getId(),sm.getUserType());
	}
	
	
}
