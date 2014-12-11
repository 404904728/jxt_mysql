package common.cq.hmq.controller.system;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import common.cq.hmq.model.JqGridData;
import common.cq.hmq.model.JqPageModel;
import common.cq.hmq.pojo.sys.Org;
import common.cq.hmq.service.OrgService;
import common.cq.hmq.service.system.RoleService;
import core.cq.hmq.modal.SessionModal;
import core.cq.hmq.pojo.sys.Role;
import core.cq.hmq.util.tools.ResourceUtil;
import app.cq.hmq.service.stuinfo.StudentInfoService;
import app.cq.hmq.service.subject.SubjectMappingService;
import app.cq.hmq.service.teainfo.TeacherInfoService;

@Controller
@RequestMapping(value = "/systemPage")
public class SystemPageController {

    @Autowired
    private TeacherInfoService teacherInfoService;

    @Autowired
    private OrgService orgService;

    @Resource
    private SubjectMappingService subjectMappingService;

    /**
     * 老师详细信息查阅
     *
     * @param tId
     * @return
     */
    @RequestMapping(params = "teacherInfoPage")
    public ModelAndView teacherInfoPage(Long tId) {
        ModelAndView mav = new ModelAndView("app/system/teacherInfoPage");
        mav.addObject("teacherInfo", teacherInfoService.findById(tId));
        mav.addObject("zuZhiName", teacherInfoService.getZuzhiByTeaId(tId));
        mav.addObject("teacherInfoClass",
                subjectMappingService.findSubjectMapingByTeacherId(tId));
        return mav;
    }

    /**
     * 部门详细信息查阅
     *
     * @param oId
     * @return
     */
    @RequestMapping(params = "orgInfoPage")
    public ModelAndView orgInfoPage(Long oId) {
        ModelAndView mav = new ModelAndView("app/system/orgInfo");
        mav.addObject("orgInfo", orgService.findOrgInfoById(oId));
        return mav;
    }

    /**
     * 部门修改，增加页面
     *
     * @param oId
     * @return
     */
    @RequestMapping(params = "orgform")
    public ModelAndView orgform(Long oId) {
        ModelAndView mav = new ModelAndView();
        Org org = orgService.findById(oId);
        mav.addObject("orgParent", org);
        if (oId == 2) {
            mav.setViewName("app/system/orgteacherform");
        } else {
            mav.addObject("leaders",
                    teacherInfoService.findTeacherInfoByRoleBZR());
            mav.setViewName("app/system/orgform");
        }
        return mav;
    }

    @Autowired
    private RoleService roleService;

    /**
     * 跳转班级修改页面
     *
     * @param tId
     * @return
     */
    @RequestMapping(params = "classform")
    public ModelAndView classform(Long oId) {
        ModelAndView mav = new ModelAndView();
        Org org = orgService.findById(oId);
        mav.addObject("orgParent", org.getParent());
        mav.addObject("org", org);
        mav.addObject("leaders", teacherInfoService.findTeacherInfoByRoleBZR());
        mav.setViewName("app/system/orgform");
        return mav;
    }

    /**
     * 跳转老师角色修改页面
     *
     * @param id
     * @return
     */
    @RequestMapping(params = "orgformupdate")
    public ModelAndView orgformupdate(Long id) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("app/system/orgteacherupdate");
        Role role = roleService.findById(id);
        mav.addObject("role", role);
        return mav;
    }

    @Resource
    private StudentInfoService studentInfoService;

    /**
     * 新增，修改学生页面跳转
     *
     * @param sId
     * @return
     */
    @RequestMapping(params = "sutdentformPage")
    public ModelAndView sutdentformPage(Long sId) {
        ModelAndView mav = new ModelAndView("app/system/studentform");
        if (sId != null) {
            mav.addObject("student", studentInfoService.findById(sId));
        }
        mav.addObject("clss", orgService.findAllClass());
        return mav;
    }

    /**
     * @param sId
     * @return
     * @title
     * @author Limit
     * @date 2014-6-6 上午9:56:05
     * @version 1.0
     * @Description 查询删除学生的方法
     */
    @RequestMapping(params = "querySpecialStu")
    public ModelAndView querySpecialStu(Long sId) {
        ModelAndView mav = new ModelAndView("app/system/querySpecialStu");
        if (sId != null) {
            mav.addObject("student", studentInfoService.findById(sId));
        }
        mav.addObject("clss", orgService.findAllClass());
        return mav;
    }

    /**
     * @param jqPagemodel
     * @param stuName
     * @param reason
     * @return
     * @title
     * @author Limit
     * @date 2014-6-6 上午10:57:26
     * @version 1.0
     * @Description 查询删除的学生
     */
    @RequestMapping(params = "querySpecialStudent")
    @ResponseBody
    public JqGridData<List<?>> querySpecialStudent(JqPageModel jqPagemodel,
                                                   String stuName, Integer reason) {
        return studentInfoService.querySpecialStudent(jqPagemodel, stuName,
                reason);
    }
}
