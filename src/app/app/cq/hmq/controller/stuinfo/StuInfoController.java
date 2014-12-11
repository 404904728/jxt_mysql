/**
 * 
 */
package app.cq.hmq.controller.stuinfo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import app.cq.hmq.pojo.stuinfo.StudentInfo;
import app.cq.hmq.service.stuinfo.StudentInfoService;
import common.cq.hmq.model.JqGridData;
import common.cq.hmq.model.JqPageModel;
import common.cq.hmq.pojo.sys.Org;
import common.cq.hmq.service.OrgService;
import core.cq.hmq.controller.core.BaseController;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.util.tools.StringUtil;

/**
 * @author Limit 处理学生的Controller
 */
@Controller
@RequestMapping(value = "/stuinfo")
public class StuInfoController extends BaseController {

	// @Autowired
	// Log log = LogFactory.getLog(StuInfoController.class);

	@Autowired
	private StudentInfoService StuService;

	/**
	 * 
	 * @title
	 * @author Limit
	 * @date 2014-3-12 下午5:28:50
	 * @version 1.0
	 * @Description 删除学生信息
	 * 
	 * @param request
	 * 
	 * 
	 */
	@RequestMapping(params = "deleteImg_")
	@ResponseBody
	public Map<String, Object> deleteImageInfo(Long imgId) {
		return StuService.deleteImageInfo(imgId);
	}

	/**
	 * 删除学生信息
	 * 
	 * @param sId
	 * @return
	 */
	@RequestMapping(params = "deleteById")
	@ResponseBody
	public AjaxMsg deleteById(Long sId,Integer deleteType) {
		return StuService.deleteById(sId,deleteType);
	}

	/**
	 * 
	 * @title
	 * @author Limit
	 * @date 2014-3-14 下午3:48:00
	 * @version 1.0
	 * @Description 点击相册查看按钮后通过此方法返回相册信息
	 * 
	 * @param stuId
	 * @return
	 * 
	 * 
	 */
	@RequestMapping(params = "queryOneStuInfostotea_")
	public ModelAndView queryOneStuMeaageByIdToTea(Long stuId) {
		return StuService.queryOneStuMeaageById(stuId);
	}
	
	
	/**
	 * 
	 * @title
	 * @author Limit
	 * @date 2014-3-14 下午3:48:00
	 * @version 1.0
	 * @Description 家长查看学生信息
	 * 
	 * @param stuId
	 * @return
	 * 
	 * 
	 */
	@RequestMapping(params = "queryOneStuInfos_")
	public ModelAndView queryOneStuMeaageById() {
		return StuService.queryOneStuMeaageById();
	}

	/**
	 * 
	 * @title
	 * @author Limit
	 * @date 2014-3-12 下午2:10:47
	 * @version 1.0
	 * @Description 点击相册查看按钮后通过此方法返回相册信息
	 * 
	 * @param request
	 * 
	 * 
	 */
	@RequestMapping(params = "queryImage_")
	public ModelAndView queryImageBystu(HttpServletRequest request) {
		return StuService.queryImageBystu(request);
	}

	/**
	 * 
	 * @title
	 * @author Limit
	 * @date 2014-3-11 下午1:30:20
	 * @version 1.0
	 * @Description 家长或者老师查询具体某一班级的所有学生信息
	 * 
	 * @param request
	 * @param id
	 * @param page
	 * @param rows
	 * @return
	 * 
	 * 
	 */
	@RequestMapping(params = "query_1")
	@ResponseBody
	public JqGridData<StudentInfo> getStuInfoToClass(
			HttpServletRequest request, JqPageModel mode) {
		JqGridData<StudentInfo> stuInfoToClass = null;
		try {
			stuInfoToClass = StuService.getStuInfoToClass(request, mode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stuInfoToClass;
	}

	/**
	 * 
	 * @title
	 * @author Limit
	 * @date 2014-3-12 上午9:33:32
	 * @version 1.0
	 * @Description 教师修改学生信息
	 * 
	 * @param stu
	 * @param request
	 * @return
	 * 
	 * 
	 */
	@RequestMapping(params = "oneStu_")
	@ResponseBody
	public AjaxMsg updateOnlyOneStu(@ModelAttribute StudentInfo stu,
			HttpServletRequest request) {
		AjaxMsg updateOnlyOneStu = null;
		try {
			updateOnlyOneStu = StuService.updateOnlyOneStu(stu, request);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return updateOnlyOneStu;
	}

	/**
	 * 
	 * @title
	 * @author Limit
	 * @date 2014-3-11 上午11:27:35
	 * @version 1.0
	 * @Description 点击学生信息后初始化班级下拉框
	 * 
	 * @param request
	 * 
	 * 
	 */
	@RequestMapping(params = "classinfo_")
	@ResponseBody
	public List<Org> queryClassInfo(HttpServletRequest request) {
		return StuService.queryClassInfo(request);
	}

	/**
	 * 
	 * @title
	 * @author Limit
	 * @date 2014-3-11 上午11:27:35
	 * @version 1.0
	 * @Description 家长点击学生信息后显示学生所在班级
	 * 
	 * @param request
	 * 
	 * 
	 */
	@RequestMapping(params = "StuClassinfo_")
	@ResponseBody
	public Map<String, Object> queryStuClass(HttpServletRequest request) {
		Map<String, Object> queryStuClass = null;
		try {
			queryStuClass = StuService.queryStuClass(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queryStuClass;

	}

	/**
	 * 
	 * @title
	 * @author Limit
	 * @date 2014-3-11 上午11:27:35
	 * @version 1.0
	 * @Description 家长点击学生信息后显示学生所在班级
	 * 
	 * @param request
	 * 
	 * 
	 */
	@RequestMapping(params = "queryStuToUpdate_")
	public ModelAndView queryStuMessToUpdate(Long stuId) {
		return StuService.queryStuMessToUpdate(stuId);
	}

	/**
	 * 学生信息修改或新增
	 * 
	 * @param studentInfo
	 * @return
	 */
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMsg saveOrUpdate(StudentInfo studentInfo) {
		return StuService.saveOrUpdate(studentInfo);
	}

	/**
	 * 学生信息查找带回页面跳转
	 * 
	 * @param id
	 * @deprecated studentPageEasy()
	 * @return
	 */
	@RequestMapping(value = "/studentPage")
	public ModelAndView studentPage(String id) {
		ModelAndView mav = new ModelAndView("/app/notice/findback");
		mav.addObject("students",
				StuService.findStudentByClassId(Long.parseLong(id)));
		return mav;
	}

	/**
	 * 根据班级id查询学生
	 * 
	 * @return
	 */
	@RequestMapping(value = "/findStudentByClassId")
	@ResponseBody
	public List<Map<String, String>> findStudentByClassId(Long id) {
		return StuService.findStudentByClassId(id);
	}
	
	@Autowired
	private OrgService orgService;

	/**
	 * 根据班级id查找学生，id 1，2，3
	 * 
	 * @return
	 */
	@RequestMapping(value = "/findstudentbyclassids")
	@ResponseBody
	public List<Map<String, Object>> findstudentbyclassids(String id) {
		if (StringUtil.isEmpty(id)) {
			return new ArrayList<Map<String, Object>>();
		}
		return orgService.findstudentbyclassids(id);
	}

	/**
	 * 学生信息查找带回页面跳转 easy
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/studentPageEasy")
	public ModelAndView studentPageEasy(String id) {
		ModelAndView mav = new ModelAndView("/app/notice/findbackEasy");
		mav.addObject("students",
				StuService.findStudentByClassId(Long.parseLong(id)));
		return mav;
	}
}
