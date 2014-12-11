package app.cq.hmq.controller.teachercomm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import app.cq.hmq.service.teachercomm.TeacherCommService;

import common.cq.hmq.model.JqGridData;
import common.cq.hmq.model.JqPageModel;

import core.cq.hmq.controller.core.BaseController;
import core.cq.hmq.modal.SessionModal;
import core.cq.hmq.util.tools.ResourceUtil;

@Controller
@RequestMapping("/TeacherComm")
public class TeacherCommController extends BaseController{
	
	@Resource TeacherCommService teacherCommService;
	
	
	@RequestMapping(params="findOrgData")
	@ResponseBody
	public List<Map<String, Object>> findOrgTreeData(){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list.add(teacherCommService.obtailOrgDataForComm());
		return list;
	}
	
	@RequestMapping(params="findOrgTreeDataForAce")
	@ResponseBody
	public List<Map<String, Object>> findOrgTreeDataForAce(){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list.add(teacherCommService.obtailOrgDataForCommForAce());
		return list;
	}
	
	@RequestMapping(params="findTeacherGridDataForAce")
	@ResponseBody
	public JqGridData<List<?>> findTeacherGridDataForAce(HttpSession session,JqPageModel jqPagemodel,Long orgId){
		SessionModal sessionModal = (SessionModal) session.getAttribute(ResourceUtil.getSessionInfoName());
		return teacherCommService.findTeachCommunicationInfos(jqPagemodel,sessionModal,orgId);
	}
	
	@RequestMapping(params="findTeacherGridDataByRole")
	@ResponseBody
	public JqGridData<List<?>> findTeacherGridDataByRole(HttpSession session,JqPageModel jqPagemodel,Long roleId){
		return teacherCommService.findTeacherGridDataByRole(jqPagemodel,roleId);
	}
}
