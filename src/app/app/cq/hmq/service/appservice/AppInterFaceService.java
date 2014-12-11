package app.cq.hmq.service.appservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.cq.hmq.pojo.equirepair.EquiRepair;
import app.cq.hmq.pojo.stuinfo.StudentInfo;
import app.cq.hmq.pojo.teacherinfo.TeacherInfo;
import common.cq.hmq.pojo.sys.Attach;
import common.cq.hmq.pojo.sys.Org;
import common.cq.hmq.pojo.sys.User;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.service.BaseService;
import core.cq.hmq.util.tools.Encrypt;
import core.cq.hmq.util.tools.LogUtil;
import core.cq.hmq.util.tools.StringUtil;

@Service(value = "appInterFaceService")
public class AppInterFaceService extends BaseService {

    /**
     * login method
     *
     * @param ln login name
     * @param lp login pwd
     * @param lt login type
     * @return
     */
    @Transactional
    public AjaxMsg LogonMethod(String ln, String lp, String lt, String cl,
                               String ul, String b) {
        AjaxMsg am = new AjaxMsg();
        if (cl == null || ul == null) {
            am.setType(am.ERROR);
            am.setMsg("您的channelId或userId为空，不能登录");
            return am;
        }
        if (User.STUDENTTYPE.equals(lt)) {
            StudentInfo dbstu = null;
            List<StudentInfo> list = dao.find(StudentInfo.class, "no", ln);
            if (list != null && list.size() > 0) {
                dbstu = list.get(0);
            }

            // StudentInfo dbstu = dao.findOne(StudentInfo.class, "no", ln);
            if (null == dbstu) {
                am.setType(AjaxMsg.ERROR);
                am.setMsg("帐号不存在！");
                return am;
            } else {
                if (!dbstu.getPwd().equals(Encrypt.md5(lp))) {
                    am.setType(AjaxMsg.ERROR);
                    am.setMsg("密码错误！");
                    return am;
                }

                if (list != null && list.size() > 1) {
                    /** 多个学生的时候 */
                    String temString = "";
                    for (StudentInfo studentInfo : list) {
                        /** 存入学生姓名和id */
                        temString += studentInfo.getId() + ","
                                + studentInfo.getName() + ","
                                + studentInfo.getOrg().getId() + ","
                                + studentInfo.getHeadPic().getHtmlUrl() + ";";
                        studentInfo.setChannelId(cl);
                        studentInfo.setUserId(ul);
                        studentInfo.setAndroid(b.equals("0") ? false : true);
                        dao.update(studentInfo);
                    }
                    am.setType(AjaxMsg.WORN);
                    am.setMsgId(temString);
                    am.setMsg("mutiple");
                    return am;
                } else {
                    dbstu.setChannelId(cl);
                    dbstu.setUserId(ul);
                    dbstu.setAndroid(b.equals("0") ? false : true);
                    dao.update(dbstu);
                    // 一个手机只能登陆一次
                    // if (dbstu.getChannelId() == null) {
                    // dbstu.setChannelId(cl);
                    // dbstu.setUserId(ul);
                    // dao.update(dbstu);
                    // } else {
                    // if (!dbstu.getChannelId().equals(cl)
                    // || !dbstu.getUserId().equals(ul)) {
                    // am.setType(am.ERROR);
                    // am.setMsg("你所登陆的账号与绑定的手机不符合");
                    // return am;
                    // }
                    // }
                    am.setId(dbstu.getId());
                    /** 存入学生姓名和班级id */
                    am.setMsgId(dbstu.getName() + "#" + dbstu.getOrg().getId());
                    if (null != dbstu.getHeadPic() && null != dbstu.getHeadPic().getHtmlUrl()) {
                        am.setMsg(dbstu.getHeadPic().getHtmlUrl());
                    }
                    LogUtil.getLog("警告")
                            .info("家长" + dbstu.getName() + "在手机上登录");
                }
            }
        } else {
            TeacherInfo teacherInfo = dao.findOne(TeacherInfo.class, "no", ln);
            if (null == teacherInfo) {
                am.setType(AjaxMsg.ERROR);
                am.setMsg("帐号不存在！");
                return am;
            } else {
                if (!teacherInfo.getPwd().equals(Encrypt.md5(lp))) {
                    am.setType(AjaxMsg.ERROR);
                    am.setMsg("密码错误！");
                    return am;
                }
                teacherInfo.setChannelId(cl);
                teacherInfo.setUserId(ul);
                teacherInfo.setAndroid(b.equals("0") ? false : true);
                dao.update(teacherInfo);
                // if (teacherInfo.getChannelId() == null) {
                // teacherInfo.setChannelId(cl);
                // teacherInfo.setUserId(ul);
                // dao.update(teacherInfo);
                // } else {
                // if (!teacherInfo.getChannelId().equals(cl)
                // || !teacherInfo.getUserId().equals(ul)) {
                // am.setType(am.ERROR);
                // am.setMsg("你所登陆的账号与绑定的手机不符合");
                // return am;
                // }
                // }
                am.setId(teacherInfo.getId());
                am.setMsgId(teacherInfo.getName());
                am.setMsg(teacherInfo.getHeadpic().getHtmlUrl());
                LogUtil.getLog("警告").info(
                        "老师" + teacherInfo.getName() + "在手机上登录");
            }
        }
        return am;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> obtailBrzByClassId(Long id, String lk) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)) {
            List<Object> list = dao
                    .getHelperDao()
                    .find("select te.id_f,te.name_f,te.headpic_f from teacherinfo_t te where instr((select mleader_f from org_t where id_f = ?),concat(',',te.id_f,',')) > 0",
                            id);
            if (null != list && list.size() > 0) {
                Object[] objs = null;
                for (Object o : list) {
                    objs = (Object[]) o;
                    map.put("id", objs[0]);
                    map.put("name", objs[1]);
                    map.put("headpicid", objs[2]);
                }
            }
        }
        return map;
    }

    /**
     * get student detail info by student id
     *
     * @param id student id
     * @param lk login key
     * @return
     */
    public Map<String, Object> obtainSData(Long id, String lk) {
        if (!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)) {
            Map<String, Object> map = new HashMap<String, Object>();
            if (null != id) {
                StudentInfo dbstu = dao.findOne(StudentInfo.class, "id", id);
                if (null != dbstu) {
                    map.put("headPic", dbstu.getHeadPic().getHtmlUrl());
                    map.put("name", dbstu.getName());
                    map.put("sex", dbstu.getSex());
                    map.put("dutyPosition", dbstu.getDutyPosition());
                    map.put("birthday", dbstu.getBirthday());
                    map.put("studentCode", dbstu.getStudentCode());
                    map.put("idCardNo", dbstu.getIdCardNo());
                    map.put("selftel", dbstu.getSelftel());
                    map.put("parentName", dbstu.getParentName());
                    map.put("parentRelation", dbstu.getParentRelation());
                    map.put("address", dbstu.getAddress());
                    map.put("class", dbstu.getOrg().getName());
                }
            }
            return map;
        }
        return null;
    }

    /**
     * get student detail info by student id
     * <p/>
     * student id
     *
     * @param lk login key
     * @return
     */
    @Transactional
    public AjaxMsg updateSData(StudentInfo stu, String lk) {
        if (!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)) {
            AjaxMsg map = new AjaxMsg();
            if (null != stu) {
                try {
                    StudentInfo stu_new = dao.findOne(StudentInfo.class, "id",
                            stu.getId());
                    if (null != stu_new) {
                        if (null != stu.getHeadPic()) {
                            stu_new.setHeadPic(stu.getHeadPic());
                        }

                        if (!StringUtil.isEmpty(stu.getName())) {
                            stu_new.setName(stu.getName());
                        }
                        if (!StringUtil.isEmpty(stu.getSex())) {
                            stu_new.setSex(stu.getSex());
                        }
                        if (!StringUtil.isEmpty(stu.getDutyPosition())) {
                            stu_new.setDutyPosition(stu.getDutyPosition());
                        }
                        if (!StringUtil.isEmpty(stu.getBirthday())) {
                            stu_new.setBirthday(stu.getBirthday());
                        }
                        if (!StringUtil.isEmpty(stu.getStudentCode())) {
                            stu_new.setStudentCode(stu.getStudentCode());
                        }
                        if (!StringUtil.isEmpty(stu.getIdCardNo())) {
                            stu_new.setIdCardNo(stu.getIdCardNo());
                        }
                        if (!StringUtil.isEmpty(stu.getSelftel())) {
                            stu_new.setSelftel(stu.getSelftel());
                        }
                        if (!StringUtil.isEmpty(stu.getParentName())) {
                            stu_new.setParentName(stu.getParentName());
                        }
                        if (!StringUtil.isEmpty(stu.getParentRelation())) {
                            stu_new.setParentRelation(stu.getParentRelation());
                        }
                        if (!StringUtil.isEmpty(stu.getAddress())) {
                            stu_new.setAddress(stu.getAddress());
                        }
                    }
                    dao.update(stu_new);
                    map.setMsg("个人信息更新成功");
                } catch (Exception e) {
                    map.setType(AjaxMsg.ERROR);
                    map.setMsg("个人信息更新失败");
                    return map;
                }
            }
            return map;
        }
        return null;
    }

    /**
     * get teacher detail info by teacher id
     *
     * @param id teacher id
     * @param lk login key
     * @return
     */
    public Map<String, Object> obtainTData(Long id, String lk) {
        if (!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)) {
            Map<String, Object> map = new HashMap<String, Object>();
            if (null != id) {
                TeacherInfo tInfo = dao.findOne(TeacherInfo.class, "id", id);
                if (null != tInfo) {
                    map.put("headpic", tInfo.getHeadpic().getHtmlUrl());
                    map.put("name", tInfo.getName());
                    map.put("sex", tInfo.getGender());
                    map.put("email", tInfo.getEmail());
                    map.put("birthday", tInfo.getBirthday());
                    map.put("teachNo", tInfo.getTeachNo());
                    map.put("zhiCheng", tInfo.getZhiCheng());
                    map.put("telePhone", tInfo.getTelePhone());
                    map.put("orgname", tInfo.getOrg().getName());
                }
            }
            return map;
        }
        return null;
    }

    /**
     * update teacher detail info by teacher id
     *
     * @param tea teacher info
     * @param lk  login key
     * @return
     */
    @Transactional
    public AjaxMsg updateTData(TeacherInfo tea, String lk) {
        if (!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)) {
            AjaxMsg am = new AjaxMsg();
            if (null != tea) {
                try {
                    TeacherInfo tInfo_new = dao.findOne(TeacherInfo.class,
                            "id", tea.getId());
                    if (null != tInfo_new) {
                        if (null != tea.getHeadpic()) {
                            tInfo_new.setHeadpic(tea.getHeadpic());
                        }
                        if (!StringUtil.isEmpty(tea.getName())) {
                            tInfo_new.setName(tea.getName());
                        }
                        if (!StringUtil.isEmpty(tea.getGender())) {
                            tInfo_new.setGender(tea.getGender());
                        }
                        if (!StringUtil.isEmpty(tea.getEmail())) {
                            tInfo_new.setEmail(tea.getEmail());
                        }
                        if (!StringUtil.isEmpty(tea.getBirthday())) {
                            tInfo_new.setBirthday(tea.getBirthday());
                        }
                        if (!StringUtil.isEmpty(tea.getTeachNo())) {
                            tInfo_new.setTeachNo(tea.getTeachNo());
                        }
                        if (!StringUtil.isEmpty(tea.getZhiCheng())) {
                            tInfo_new.setZhiCheng(tea.getZhiCheng());
                        }
                        if (!StringUtil.isEmpty(tea.getTelePhone())) {
                            tInfo_new.setTelePhone(tea.getTelePhone());
                        }
                        dao.update(tInfo_new);
                    }
                } catch (Exception e) {
                    am.setType(AjaxMsg.ERROR);
                    am.setMsg("更新教师信息失败");
                    return am;
                }
                am.setMsg("更新教师信息成功");
            }
            return am;
        }
        return null;
    }

    /**
     * get class info by teacher id
     *
     * @param id teacher id
     * @param lk login key
     * @return
     */
    public List<Map<String, Object>> obtainClassDataByTid(Long id, String lk) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)) {
            if (null != id) {
                StringBuffer hql = new StringBuffer(
                        " select distinct(o.id_f), o.name_f,(select count(1) from studentinfo_t s  where s.status_f = 0 and s.org_f = o.id_f),o.mleader_f"
                                + " from SubjectMapping_t sm, org_t o");
                hql.append(" where  sm.org_f = o.id_f and sm.teacher_f = ?");
                //当配置班级的leader时 查询是哪个班级
                //"select distinct(o.id_f), o.name_f,(select count(1) from studentinfo_t s  where s.status_f = 0 and s.org_f = o.id_f),o.mleader_f"+
                //"from SubjectMapping_t sm, org_t o"+
                //"where  sm.org_f = o.id_f and sm.teacher_f = 1468"+
                //"union select o.id_f,o.name_f,(select count(1) from studentinfo_t s  where s.status_f = 0 and s.org_f = o.id_f), o.mleader_f from org_t o where instr(o.mleader_f,','||1468||',') > 0 and o.type_f = 3"

                List<Object> lists = dao.getHelperDao()
                        .find(hql.toString(), id);
                if (null != lists && lists.size() > 0) {
                    Object[] objs = {};
                    Map<String, Object> map = null;
                    for (Object o : lists) {
                        map = new HashMap<String, Object>();
                        objs = (Object[]) o;
                        map.put("id", objs[0]);
                        map.put("name", objs[1]);
                        map.put("count", objs[2]);
                        map.put("leaderid", objs[3]);
                        list.add(map);
                    }
                }
            }
        }
        return list;
    }

    /**
     * get student list by class id
     *
     * @param id class id
     * @param lk login key
     * @return
     */
    public List<Map<String, Object>> obtainStudentInfoList(Long id, String lk) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)) {
            if (null != id) {
                List<StudentInfo> sInfo = dao.find(
                        "from StudentInfo where status = 0 and org.id = ? ", id);
                if (null != sInfo && sInfo.size() > 0) {
                    Map<String, Object> map = null;
                    for (StudentInfo s : sInfo) {
                        map = new HashMap<String, Object>();
                        map.put("id", s.getId());
                        map.put("name", s.getName());
                        map.put("sex", s.getSex());
                        map.put("birthday", s.getSelftel());
                        map.put("headpic", s.getHeadPic().getHtmlUrl());
                        list.add(map);
                    }
                }
            }
        }
        return list;
    }

    public Map<String, List<Map<String, Object>>> obtainTeacherComm(String lk,
                                                                    String ut) {
        Map<String, List<Map<String, Object>>> tmap = new HashMap<String, List<Map<String, Object>>>();
        if (!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)) {
            if (User.TEACHERTYPE.equals(ut)) {
                List<Org> ol = dao.find(Org.class, "type", 4);
                if (null != ol) {
                    for (Org o : ol) {
                        List<Map<String, Object>> slist = new ArrayList<Map<String, Object>>();
                        List<?> sl = dao
                                .getHelperDao()
                                .find("with sm as (select s.teacher_f, min(s.subjectinfo_f) subid from subjectmapping_t s where 1=1 group by s.teacher_f) " +
                                        "select t.id_f,t.name_f,t.telephone_f,sub.name_f as sname from teacherinfo_t t left join sm s on t.id_f = s.teacher_f left join subjectinfo_t sub on sub.id_f = s.subid where t.org_f = ?",
                                        o.getId());
                        if (null != sl) {
                            Object[] objs = null;
                            for (Object obj : sl) {
                                objs = (Object[]) obj;
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("tid", objs[0]);
                                map.put("tname", objs[1]);
                                map.put("ttel", objs[2]);
                                map.put("sname", objs[3]);
                                slist.add(map);
                            }
                        }
                        tmap.put(o.getName(), slist);
                    }
                }
            }
        }
        return tmap;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findTeacherByStudentId(String sId,
                                                            String lk) {

        if (!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)) {
            List<Object[]> list = dao
                    .getHelperDao()
                    .find("select t.id_f,t.name_f,t.telephone_f,o.name_f as  orgname,sub.name_f as subname"
                            + " from org_t o,TeacherInfo_t t,Subjectmapping_t s,subjectinfo_t sub "
                            + "where sub.id_f=s.subjectinfo_f and o.id_f=t.org_f and  s.teacher_f=t.id_f "
                            + "and  s.org_f=(select org_f  from studentinfo_t where id_f="
                            + Long.parseLong(sId) + ")");
            List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
            for (Object[] objects : list) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", objects[0]);
                map.put("name", objects[1]);
                map.put("telePhone", objects[2]);
                map.put("orgname", objects[3]);
                map.put("subname", objects[4]);
                listMap.add(map);
            }
            return listMap;
        }
        return null;
    }

    /**
     * @param lk
     * @return
     * @title
     * @author Limit
     * @date 2014-3-25 下午2:37:47
     * @version 1.0
     * @Description 保存设备报修记录
     */
    @Transactional
    public AjaxMsg saveEuqiRepairToEntity(String lk, EquiRepair repair) {
        AjaxMsg msg = new AjaxMsg();
        msg.setType(AjaxMsg.ERROR);
        if (!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)) {
            if (null == repair.getRepairTitle()
                    || "".equals(repair.getRepairTitle().trim())) {
                return msg;
            }
            if (null == repair.getRepairContent()
                    || "".equals(repair.getRepairContent().trim())) {
                return msg;
            }
            if (null == repair.getSendTea()
                    || "".equals(repair.getSendTea().getId())) {
                return msg;
            }
            repair.setRepairStatus(1);
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            String format2 = format.format(new Date());
            repair.setRepairTime(format2);
            repair.setCackContent("未处理");
            repair.setSendBoxStatus(1);
            repair.setAcceptBoxStatus(1);
            if (null != repair.getAtc()) {
                repair.setAtcid(repair.getAtc().getId());
            }
            if (null != repair.getSound()) {
                repair.setSoundid(repair.getSound().getId());
            }
            dao.insert(repair);
            if (null != repair.getAtc()) {
                Attach attach = (Attach) dao.findOne(
                        "from Attach at where at.id = ?", repair.getAtc()
                        .getId());
                attach.setRelType("equiRepair");
                attach.setRelId(repair.getId());
                dao.update(attach);
            }
            if (null != repair.getSound()) {
                Attach attach = (Attach) dao.findOne(
                        "from Attach at where at.id = ?", repair.getSound()
                        .getId());
                attach.setRelType("equiRepair");
                attach.setRelId(repair.getId());
                dao.update(attach);
            }
            msg.setType(AjaxMsg.SUCCESS);
            return msg;
        }
        return msg;
    }

}
