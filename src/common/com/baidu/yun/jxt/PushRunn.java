package com.baidu.yun.jxt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.cq.hmq.pojo.notice.Notice;
import app.cq.hmq.service.stuinfo.StudentInfoService;
import app.cq.hmq.service.teainfo.TeacherInfoService;

import com.baidu.yun.jxt.model.PushMsgModel;

import common.cq.hmq.util.SendSMS;
import core.cq.hmq.dao.util.BeanUtil;
import core.cq.hmq.util.tools.LogUtil;
import core.cq.hmq.util.tools.StringUtil;

public class PushRunn implements Runnable {

    private String ids;

    private boolean teacher = false;

    private Notice notice;

    private Long userId;

    private String username;

    @Override
    public void run() {
        // TODO Auto-generated method stub
        PushMsgModel pmm = new PushMsgModel();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("noticeid", notice.getId());
        if (teacher) {
            map.put("teacher", true);
            pmm.setCustom_content(map);
            pmm.setTitle(notice.getTitle());
            pmm.setDescription(notice.getContent());
            TeacherInfoService teacherInfoService = (TeacherInfoService) BeanUtil
                    .getBean("teacherInfoService");
            List<Object[]> obj = teacherInfoService.findPushMsg(ids);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < obj.size(); i++) {
                System.out.println(obj.get(i)[0] + "===" + obj.get(i)[1]
                        + "====" + obj.get(i)[2]);
                sb.append(obj.get(i)[3] + ",");
                if (StringUtil.isEmpty(obj.get(i)[2])) {
                    continue;
                }
                try {
                    if (!StringUtil.isEmpty(obj.get(i)[0])
                            && !StringUtil.isEmpty(obj.get(i)[1])) {
                        if (obj.get(i)[2].toString().equals("1")) {// android
                            int success = PushUtil.androidPushUserMsg(
                                    Long.parseLong(obj.get(i)[0].toString()),
                                    obj.get(i)[1].toString(), pmm);
                            System.out.println("android 手机推送-老师-(userid)"
                                    + obj.get(i)[1].toString() + ":" + success);
                        } else if (obj.get(i)[2].toString().equals("0")) {// ios
                            int success = PushUtil.iosPushUserMsg(
                                    Long.parseLong(obj.get(i)[0].toString()),
                                    obj.get(i)[1].toString(), teacher,
                                    pmm.getDescription(), "noticeid",
                                    notice.getId() + "");
                            System.out.println("ios 手机推送-老师-(userid)"
                                    + obj.get(i)[1].toString() + ":" + success);
                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.out.println("老师-推送通知不成功：" + obj.get(i)[3]);
                }
            }
            if (notice.getShortMsg()) {
                SendSMS.sendMsg(pmm.getDescription(),
                        sb.substring(0, sb.lastIndexOf(",")), userId, username,notice.getId());
            }

        } else {
            map.put("teacher", false);
            pmm.setCustom_content(map);
            pmm.setTitle(notice.getTitle());
            pmm.setDescription(notice.getContent());
            StudentInfoService studentInfoService = (StudentInfoService) BeanUtil
                    .getBean("studentInfoService");
            List<Object[]> obj = studentInfoService.findPushMsg(ids);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < obj.size(); i++) {
                System.out.println(obj.get(i)[0] + "===" + obj.get(i)[1]
                        + "====" + obj.get(i)[2]);
                sb.append(obj.get(i)[3] + ",");
                if (StringUtil.isEmpty(obj.get(i)[2])) {
                    continue;
                }
                try {
                    if (!StringUtil.isEmpty(obj.get(i)[0])
                            && !StringUtil.isEmpty(obj.get(i)[1])) {
                        if (obj.get(i)[2].toString().equals("1")) {// android
                            int success = PushUtil.androidPushUserMsg(
                                    Long.parseLong(obj.get(i)[0].toString()),
                                    obj.get(i)[1].toString(), pmm);
                            System.out.println("android 手机推送-家长-(userid)"
                                    + obj.get(i)[1].toString() + ":" + success);
                        } else if (obj.get(i)[2].toString().equals("0")) {// ios
                            int success = PushUtil.iosPushUserMsg(
                                    Long.parseLong(obj.get(i)[0].toString()),
                                    obj.get(i)[1].toString(), teacher,
                                    pmm.getDescription(), "noticeid",
                                    notice.getId() + "");
                            System.out.println("ios 手机推送-家长-(userid)"
                                    + obj.get(i)[1].toString() + ":" + success);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("家长-推送通知不成功：" + obj.get(i)[3]);
                }

            }
            if (notice.getShortMsg()) {
                SendSMS.sendMsg(pmm.getDescription(),
                        sb.substring(0, sb.lastIndexOf(",")), userId, username,notice.getId());
            }

        }
    }

    public PushRunn(String ids, boolean teacher, Notice notice, Long userId, String username) {
        this.ids = ids;
        this.teacher = teacher;
        this.notice = notice;
        this.userId = userId;
        this.username = username;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public boolean isTeacher() {
        return teacher;
    }

    public void setTeacher(boolean teacher) {
        this.teacher = teacher;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

