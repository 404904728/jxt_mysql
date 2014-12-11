package app.cq.hmq.controller.leave;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import app.cq.hmq.pojo.leave.LeaveInfo;
import app.cq.hmq.service.leave.LeaveInfoService;

import common.cq.hmq.util.SendSMS;

import core.cq.hmq.controller.core.BaseController;
import core.cq.hmq.dao.PageList;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.modal.SessionModal;
import core.cq.hmq.util.tools.StringUtil;

@Controller
@RequestMapping("/leaveinfo")
public class LeaveInfoController extends BaseController {

    @Resource
    private LeaveInfoService leaveInfoService;

    @RequestMapping(params = "judgeClassBanzhuren")
    @ResponseBody
    public Map<String, Object> judgeClassBanzhuren(Long rkClassid) {
        SessionModal sm = currentSessionModel();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isClassLeader",
                leaveInfoService.judgeClassBanzhuren(rkClassid, sm.getId()));
        if (Integer.valueOf(0).equals(map.get("isClassLeader"))) {
            map.put("data", leaveInfoService.findMyClassBanzhuren(rkClassid));
        }

        return map;
    }

    @RequestMapping(params = "obtainLeaveInfo")
    @ResponseBody
    public PageList<?> obtainLeaveInfo(int pageNo, int pageSize,
                                       String searchKey, Long rkClassid) {
        SessionModal sm = currentSessionModel();
        PageList<?> pageList = leaveInfoService.findLeaveInfoList(pageNo,
                pageSize, searchKey, sm.getUserType(), sm.getId(), rkClassid);
        return pageList;
    }

    /**
     * 教師通訊一级菜单跳转
     *
     * @return
     */
    @RequestMapping(params = "findLIById")
    public ModelAndView findLIById(Long id, Long rkClassid) {
        ModelAndView mav = new ModelAndView("app/leave/leaveinfo_l");
        SessionModal sm = currentSessionModel();
        Map<String, Object> map = leaveInfoService.findLeaveInfoById(id,
                sm.getUserType(), sm.getId(), rkClassid);
        mav.addObject("leaveinfo", map.get("2"));
        mav.addObject("u_status", map.get("1"));
        mav.addObject("isClassLeader", map.get("3"));
        return mav;
    }

    /**
     * 新增请假记录
     *
     * @return
     */
    @RequestMapping(params = "addLeaveInfo")
    @ResponseBody
    public AjaxMsg addLeaveInfo(LeaveInfo leaveInfo, Long classid) {
        return leaveInfoService.addLeaveInfo(leaveInfo, currentSessionModel(),
                classid);
    }

    /**
     * 新增请假记录
     *
     * @return
     */
    @RequestMapping(params = "deleteLeaveInfo")
    @ResponseBody
    public AjaxMsg deleteLeaveInfo(Long id) {
        return leaveInfoService.deleteLeaveInfo(id);
    }

    /**
     * 更新请假记录
     *
     * @return
     */
    @RequestMapping(params = "updateLeaveInfo")
    @ResponseBody
    public AjaxMsg updateLeaveInfo(Long id, String status) {
        return leaveInfoService.updateLeaveInfo(id, status);
    }

    /**
     * 通知家长
     *
     * @return
     */
    @RequestMapping(params = "informJZ")
    @ResponseBody
    public AjaxMsg informJZ(String tel, String content) {
        AjaxMsg am = new AjaxMsg();
        try {
            if (!StringUtil.isEmpty(tel)) {
                SessionModal sessionModal = currentSessionModel();
                SendSMS.sendMsg(content, tel, sessionModal.getId(), sessionModal.getName());
                am.setMsg("通知成功！");
            } else {
                am.setType(AjaxMsg.ERROR);
                am.setMsg("家长号码为空，无法通知！");
            }
        } catch (Exception e) {
            am.setType(AjaxMsg.ERROR);
            am.setMsg("通知失败！");
        }
        return am;
    }

}
