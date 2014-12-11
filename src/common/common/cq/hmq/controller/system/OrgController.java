package common.cq.hmq.controller.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.impl.util.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import common.cq.hmq.pojo.sys.Org;
import common.cq.hmq.service.OrgService;
import core.cq.hmq.annotation.ControllerAnn;
import core.cq.hmq.controller.core.DateBaseController;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.util.tools.StringUtil;

@Controller
@RequestMapping("org")
public class OrgController extends DateBaseController {

    @Resource
    private OrgService orgService;

    @RequestMapping(params = "findTeacher")
    @ResponseBody
    public List<Map<String, Object>> findTeacher(String id) {
        return orgService.findOrgsTeacherZtree(id);
    }

    @RequestMapping(params = "findTeacherByRole")
    @ResponseBody
    public List<Map<String, Object>> findTeacherByRole(String id, Long rId) {
        return orgService.findTeacherByRoleCheckedZtree(id, rId);
    }

    @RequestMapping(params = "findStudent")
    @ResponseBody
    public List<Map<String, Object>> findStudent(String id) {
        return orgService.findOrgsStudentZtree(id);
    }


    /**
     * 短信统计数
     *
     * @param id
     * @return
     */
    @RequestMapping(params = "findsto")
    @ResponseBody
    public List<Map<String, Object>> findsto(String id) {
        return orgService.findsto(id);
    }

    @RequestMapping(value = "saveOrUpdate")
    @ResponseBody
    public AjaxMsg saveOrUpdate(Org org) {
        AjaxMsg am = new AjaxMsg();
        try {
            if (org.getId() == null) {
                orgService.insert(org);
            } else {
                orgService.update(org);
            }
            am.setMsg("操作成功");
        } catch (Exception e) {
            // TODO: handle exception
            am.setType(am.ERROR);
            am.setMsg("操作失败");
        }
        return am;
    }

    @RequestMapping(value = "del")
    @ResponseBody
    public AjaxMsg del(Long id) {
        AjaxMsg am = new AjaxMsg();
        try {
            orgService.del(id);
            am.setMsg("操作成功");
        } catch (Exception e) {
            // TODO: handle exception
            am.setType(am.ERROR);
            am.setMsg("操作失败");
        }
        return am;
    }

    @RequestMapping(value = "delclassornj")
    @ResponseBody
    public AjaxMsg delclassornj(Long id, boolean nj) {
        AjaxMsg am = new AjaxMsg();
        try {
            String msg = orgService.delclassornj(id, nj);
            if (StringUtil.isEmpty(msg)) {
                am.setMsg("操作成功");
            } else {
                am.setType(am.ERROR);
                am.setMsg(msg);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            am.setType(am.ERROR);
            am.setMsg("操作失败");
        }
        return am;
    }

    /**
     * 班级信息查找带回页面跳转
     *
     * @param id
     * @return
     */
    @RequestMapping(params = "classPage")
    public ModelAndView classPage() {
        ModelAndView mav = new ModelAndView("/app/notice/findbackEasy");
        // mav.addObject("stuClasses", orgService.findOrgByType(3));
        return mav;
    }

    /**
     * 班主任信息查找带回页面跳转
     *
     * @param id
     * @return
     */
    @RequestMapping(params = "headmaster")
    public ModelAndView headmaster() {
        ModelAndView mav = new ModelAndView("/app/notice/findbackHeadmaster");
        return mav;
    }

    /**
     * 查找所有班级
     *
     * @return
     */
    @RequestMapping(value = "/findClassData")
    @ResponseBody
    public List<Map<String, Object>> findClassData() {
        List<Object[]> obj = orgService.findOrgByType(3);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Object[] objects : obj) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", objects[0]);
            // map.put("text", objects[1] + "(" + objects[2] + ")");
            /** 因班级包含了级，所以暂不要父级名称，但是已经查询 */
            map.put("text", objects[1]);
            list.add(map);
        }
        return list;
    }

    /**
     * 查找所有班级
     *
     * @return
     */
    @RequestMapping(value = "/findClassDatapush")
    @ResponseBody
    public List<Map<String, Object>> findClassDatapush(Integer type) {
        List<Object[]> obj = orgService.findOrgByTypePush(currentSessionModel().getId(), type);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Object[] objects : obj) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", objects[0]);
            // map.put("text", objects[1] + "(" + objects[2] + ")");
            /** 因班级包含了级，所以暂不要父级名称，但是已经查询 */
            map.put("text", objects[1]);
            list.add(map);
        }
        return list;
    }

    @RequestMapping(value = "/findheadmaster")
    @ResponseBody
    public List<Map<String, Object>> findheadmaster() {
        return orgService.findAllLeader();
    }

    /**
     * 年级新增，修改，页面
     *
     * @param id
     * @param save
     * @return
     */
    @RequestMapping(params = "njform")
    public ModelAndView njform(Long id, boolean save) {
        ModelAndView mav = new ModelAndView();
        Org org = orgService.findById(id);
        if (save) {
            mav.addObject("orgParent", org);
        } else {
            mav.addObject("orgParent", org.getParent());
            mav.addObject("org", org);
        }
        mav.setViewName("app/system/njform");
        return mav;
    }

}
