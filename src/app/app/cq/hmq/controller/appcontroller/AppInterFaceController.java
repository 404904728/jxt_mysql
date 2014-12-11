package app.cq.hmq.controller.appcontroller;

import app.cq.hmq.pojo.equirepair.EquiRepair;
import app.cq.hmq.pojo.stuinfo.StudentInfo;
import app.cq.hmq.pojo.teacherinfo.TeacherInfo;
import app.cq.hmq.service.appservice.AppInterFaceService;
import common.cq.hmq.pojo.sys.User;
import core.cq.hmq.annotation.ControllerAnn;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.util.tools.StringUtil;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/app")
public class AppInterFaceController {

    @Resource
    private AppInterFaceService appInterFaceService;

    /**
     * http://125.71.236.224:8088/FamiliesSchoolConmunication.apk
     * http://125.71.236.224:8088/HomeSchoolConmunication.apk
     *
     * @param ln 登录名
     * @param lp 登录密码
     * @param lt 登录类型
     * @param cl 手机channelId
     * @param ul 手机userId
     * @param b 是否是android =1andorid =0 ios
     * @return
     */
    @RequestMapping(params = "logon_")
    @ControllerAnn(toLogon = false)
    @ResponseBody
    public AjaxMsg logon_(String ln, String lp, String lt, String cl,
                          String ul, String b) {
        AjaxMsg msg = new AjaxMsg(0);
        if (b == null || b.equals("")) {
            msg.setType(AjaxMsg.WORN);
            msg.setMsg("请传入手机类型参数！");
            return msg;
        }
        if (StringUtil.isEmpty(ln)) {
            msg.setType(AjaxMsg.WORN);
            msg.setMsg("请输入用户名！");
            return msg;
        }

        if (StringUtil.isEmpty(lp)) {
            msg.setType(AjaxMsg.WORN);
            msg.setMsg("请输入密码！");
            return msg;
        }

        if (StringUtil.isEmpty(lt)) {
            msg.setType(AjaxMsg.WORN);
            msg.setMsg("类型错误！");
            return msg;
        }
        return appInterFaceService.LogonMethod(ln, lp, lt, cl, ul, b);
    }

    /**
     * 获取学生信息
     *
     * @return
     */
    @RequestMapping(params = "obtainSData")
    @ControllerAnn(toLogon = false)
    @ResponseBody
    public Map<String, Object> obtainSData(Long id, String lk) {
        return appInterFaceService.obtainSData(id, lk);
    }

    /**
     * 获取教師信息
     *
     * @return
     */
    @RequestMapping(params = "obtainTData")
    @ControllerAnn(toLogon = false)
    @ResponseBody
    public Map<String, Object> obtainTData(Long id, String lk) {
        return appInterFaceService.obtainTData(id, lk);
    }

    /**
     * 获取教师所授课班级
     *
     * @return
     */
    @RequestMapping(params = "obtainClassDataByTid")
    @ControllerAnn(toLogon = false)
    @ResponseBody
    public List<Map<String, Object>> obtainClassDataByTid(Long id, String lk) {
        return appInterFaceService.obtainClassDataByTid(id, lk);
    }

    /**
     * 获取班級同學列表
     *
     * @return
     */
    @RequestMapping(params = "obtainClassList")
    @ControllerAnn(toLogon = false)
    @ResponseBody
    public List<Map<String, Object>> obtainClassList(Long id, String lk) {
        return appInterFaceService.obtainStudentInfoList(id, lk);
    }

    /**
     * 通过班级id获取班主任信息
     *
     * @return
     */
    @RequestMapping(params = "obtailBrzByClassId")
    @ControllerAnn(toLogon = false)
    @ResponseBody
    public Map<String, Object> obtailBrzByClassId(Long id, String lk) {
        return appInterFaceService.obtailBrzByClassId(id, lk);
    }

    /**
     * 教师通讯录列表
     *
     * @return
     */
    @RequestMapping(value = "/findTeacherPhoneByStudentId")
    @ControllerAnn(toLogon = false)
    @ResponseBody
    public List<Map<String, Object>> findTeacherPhoneByStudentId(String sId,
                                                                 String lk) {
        return appInterFaceService.findTeacherByStudentId(sId, lk);
    }

    /**
     * 更新教師信息
     *
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    @RequestMapping(params = "updateTData")
    @ControllerAnn(toLogon = false, interval = 3)
    @ResponseBody
    public AjaxMsg updateTData(TeacherInfo tea, String lk)
            throws JsonParseException, JsonMappingException, IOException {
        /*
           * ObjectMapper om = new ObjectMapper(); tea =
           * om.readValue("{'id':1,'telphone':'13312341237'}", TeacherInfo.class);
           */
        return appInterFaceService.updateTData(tea, lk);
    }

    /**
     * 更新学生信息
     *
     * @return
     */
    @RequestMapping(params = "updateSData")
    @ControllerAnn(toLogon = false, interval = 3)
    @ResponseBody
    public AjaxMsg updateSData(StudentInfo stu, String lk) {
        return appInterFaceService.updateSData(stu, lk);
    }

    /**
     * 获取教师通讯录
     *
     * @return
     */
    @RequestMapping(params = "obtainTeacherComm")
    @ControllerAnn(toLogon = false)
    @ResponseBody
    public Map<String, List<Map<String, Object>>> obtainTeacherComm(String lk,
                                                                    String ut) {
        return appInterFaceService.obtainTeacherComm(lk, ut);
    }

    /**
     * @param lk
     * @param teaId
     * @param title
     * @param content
     * @param picId
     * @return
     * @title
     * @author Limit
     * @date 2014-3-25 下午2:37:47
     * @version 1.0
     * @Description 保存设备报修记录
     */
    @RequestMapping(params = "saveEuqiRepair_1")
    @ControllerAnn(toLogon = false, interval = 3)
    @ResponseBody
    public AjaxMsg saveEuqiRepairToEntity(String lk, EquiRepair repair) {
        return appInterFaceService.saveEuqiRepairToEntity(lk, repair);
    }

    /**
     * 学生本学期成绩统计图
     *
     * @param lk
     * @param uId
     * @return
     */
    @RequestMapping(params = "studentScoreChart_")
    @ControllerAnn(toLogon = false)
    public ModelAndView studentScoreChart(String lk, Long orgId, String name) {
        if (StringUtil.isEmpty(lk) && User.LKEY.equals(lk)) {
            return null;
        }
        ModelAndView mav = new ModelAndView("app/chart/studentScoreChart");
        mav.addObject("orgId", orgId);
        mav.addObject("name", name);
        return mav;
    }

    @RequestMapping(params = "test")
    @ControllerAnn(toLogon = false)
    @ResponseBody
    public String test(String s, HttpSession session) {
        return s;
    }

}
