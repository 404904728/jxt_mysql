package app.cq.hmq.controller.notice;

import app.cq.hmq.pojo.notice.Notice;
import app.cq.hmq.pojo.subject.SubjectMapping;
import app.cq.hmq.pojo.teacherinfo.TeacherInfo;
import app.cq.hmq.service.notice.NoticeService;
import app.cq.hmq.service.subject.SubjectMappingService;
import app.cq.hmq.service.teainfo.TeacherInfoService;
import common.cq.hmq.service.OrgService;
import core.cq.hmq.controller.core.BaseController;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.modal.PageModel;
import core.cq.hmq.modal.SessionModal;
import core.cq.hmq.util.tools.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/notice")
public class NoticeController extends BaseController {

    /**
     * 跳转 通知详细
     *
     * @return
     */
    @RequestMapping(params = "noticedetail")
    public ModelAndView noticedetail(Long nId) {
        ModelAndView mav = new ModelAndView("/app/notice/noticedetail");
        Notice notice = noticeService.findNoticeById(nId);
        mav.addObject("notice", notice);
        mav.addObject("totalSms", noticeService.findSendTotal(nId));
        return mav;
    }

    /**
     * 跳转 未读通知详细，查阅后则状态改变为已读
     *
     * @return
     */
    @RequestMapping(params = "noticedetaillook")
    public ModelAndView noticedetaillook(Long nId) {
        ModelAndView mav = new ModelAndView("/app/notice/sendnoticedetail");
        mav.addObject("notice", noticeService.findNoticeByIdAndChangeLook(nId));
        return mav;
    }

    /**
     * 跳转 通知草稿详细
     *
     * @return
     */
    @RequestMapping(params = "noticedraft")
    public ModelAndView noticedraft(Long nId) {
        ModelAndView mav = new ModelAndView("/app/notice/noticedraft");
        mav.addObject("notice", noticeService.findNoticeById(nId));
        return mav;
    }

    @Autowired
    private SubjectMappingService subjectMappingService;

    @Autowired
    private OrgService orgService;

    /**
     * 跳转发送通知
     *
     * @return
     */
    @RequestMapping(value = "/noticeform")
    public ModelAndView noticeform(int noticeType) {
        ModelAndView mav = new ModelAndView("/app/notice/noticeform");
        if (noticeType == 0) {// 作业通知
            List<SubjectMapping> list = subjectMappingService
                    .findSubjectMapingByTeacherId(currentSessionModel().getId());
            mav.addObject("team", list);
        } else if (noticeType == 1) {// 班级通知
            mav.addObject("team", orgService
                    .findLeaderByTeacherId(currentSessionModel().getId()));
            // mav.addObject("team", list);
            // orgService.findLeader(currentSessionModel().getId());
        } else if (noticeType == 2 || noticeType == 4 || noticeType == 3) {// 教务处，学生处，校内
            // mav.addObject("teaOrgs", orgService.findAllObject(4));
        }
        mav.addObject("noticeType", noticeType);
        return mav;
    }

    /**
     * 发送/草稿通知分页
     *
     * @param model
     * @param type
     * @param draft
     * @return
     */
    @RequestMapping(value = "/noticeinfo")
    public ModelAndView noticeinfo(PageModel model, int type, boolean draft) {
        ModelAndView mav = new ModelAndView();
        if (draft) {
            mav.addObject("draftlist", noticeService.findDraftList(model, type));
            mav.setViewName("/app/notice/noticesdraftlist");
        } else {
            mav.addObject("sendlist", noticeService.findSendList(model, type));
            mav.setViewName("/app/notice/noticesendlist");
        }
        mav.addObject("noticeType", type);
        return mav;
    }

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private TeacherInfoService teacherInfoService;

    /**
     * 新增或修改通知 作业通知 1：班级通知 2：教务处通知 3：学生处通知 4校内通告
     *
     * @param notice     通知
     * @param receive    接受人
     * @param noticeType 接收类型
     * @param cls        班级id
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate")
    @ResponseBody
    public AjaxMsg saveOrUpdate(Notice notice, String receive,
                                String receiveName, String receivestu, int noticeType,
                                String classIds, String roleIds, String stutels, String teatels) {
        AjaxMsg am = new AjaxMsg();
        TeacherInfo teaInfo =teacherInfoService.currentTeacher();
        notice.setTeacherInfo(teaInfo);
        notice.setGenre(noticeType);
        if (noticeType == 0 || noticeType == 1) {// 班级通知或作业通知
            if (receive == null || receive.equals("")) {// 只选择班级
                am = noticeService.push(notice, classIds, null, null, null,
                        null, null, null, null);
            } else {

                am = noticeService.push(notice, null, null, receive, null,
                        null, null, null, null);
            }
        } else {// 学生处通知，教务处通知，校内通告
            if (noticeType == 5) {// 招办
                if (!StringUtil.isEmpty(stutels)) {
                    if (stutels.indexOf("，") >= 0) {
                        am.setType(am.ERROR);
                        am.setMsg("自己输入的家长号码中包含有中文逗号不能发送");
                        return am;
                    }
                } else {
                    stutels = "";
                }
                if (!StringUtil.isEmpty(teatels)) {
                    if (teatels.indexOf("，") >= 0) {
                        am.setType(am.ERROR);
                        am.setMsg("自己输入的老师号码中包含有中文逗号不能发送");
                        return am;
                    }

                } else {
                    teatels = "";
                }
                am = noticeService.push(notice, null, null, null,
                        null, null, null, stutels, teatels);
            } else {
                am = noticeService.push(notice, classIds, roleIds, receivestu,
                        receive, null, null, null, null);
            }

        }
        return am;
    }

    /**
     * 删除
     *
     * @return
     */
    @RequestMapping(value = "/del")
    @ResponseBody
    public AjaxMsg del(Long id) {
        return noticeService.del(id);
    }

    /**
     * 登录后查找最近的通知
     *
     * @return
     */
    @RequestMapping(value = "/findNearNotice")
    @ResponseBody
    public List<Map<String, Object>> findNearNotice() {
        SessionModal sm = currentSessionModel();
        return noticeService.findNearNotice(sm.getId(), sm.getUserType());
    }

}
