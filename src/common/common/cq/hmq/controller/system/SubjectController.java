package common.cq.hmq.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import app.cq.hmq.pojo.subject.SubjectInfo;
import app.cq.hmq.service.subject.SubjectService;
import common.cq.hmq.model.JqGridData;
import common.cq.hmq.model.JqPageModel;
import core.cq.hmq.modal.AjaxMsg;

/**
 * 科目
 * 
 * @author cqmonster
 * 
 */
@Controller
@RequestMapping("/subject")
public class SubjectController {

	@Autowired
	private SubjectService subjectService;

	/**
	 * 查询科目数据
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(params = "findData", method = RequestMethod.POST)
	@ResponseBody
	public JqGridData<SubjectInfo> findData(JqPageModel model) {
		return subjectService.findAll(model);
	}

	@RequestMapping(value = "/pageform")
	public ModelAndView pageform(Long id) {
		ModelAndView mav = new ModelAndView("/app/subject/subjectform");
		if (id != null) {
			mav.addObject("subject", subjectService.findById(id));
		}
		return mav;
	}

	/**
	 * 更新或修改
	 * 
	 * @param subject
	 * @return
	 */
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMsg saveOrUpdate(SubjectInfo subject) {
		AjaxMsg am = new AjaxMsg();
		try {
			int i = subjectService.saveOrUpdate(subject);
			if (i == 0) {
				am.setType(am.ERROR);
				am.setMsg("操作失败,科目名称重复");
			} else {
				am.setMsg("操作成功");
			}
		} catch (Exception e) {
			// TODO: handle exception
			am.setType(am.ERROR);
			am.setMsg("操作失败");
		}
		return am;
	}

	/**
	 * 删除科目
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/del")
	@ResponseBody
	public AjaxMsg del(Long id) {
		AjaxMsg am = new AjaxMsg();
		try {
			subjectService.del(id);
			am.setMsg("操作成功");
		} catch (Exception e) {
			// TODO: handle exception
			am.setType(am.ERROR);
			am.setMsg("操作失败");
		}
		return am;
	}

}
