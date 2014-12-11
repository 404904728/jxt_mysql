package common.cq.hmq.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import opensource.jpinyin.PinyinFormat;
import opensource.jpinyin.PinyinHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.cq.hmq.pojo.stuinfo.StudentInfo;
import app.cq.hmq.pojo.teacherinfo.TeacherInfo;
import app.cq.hmq.service.subject.SubjectMappingService;
import common.cq.hmq.pojo.sys.Org;
import common.cq.hmq.pojo.sys.UserRole;
import common.cq.hmq.service.system.RoleService;
import core.cq.hmq.pojo.sys.Role;
import core.cq.hmq.service.BaseService;
import core.cq.hmq.util.tools.StringUtil;

@Service(value = "orgService")
public class OrgService extends BaseService {

    @Resource
    private UserService userService;

    @Resource
    private SubjectMappingService subjectMappingService;

    public Org findById(Long id) {
        return dao.findOne(Org.class, "id", id);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> findOrgInfoById(Long id) {
        Org org = dao.findOne(Org.class, "id", id);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", org.getId());
        map.put("name", org.getName());
        String tempStr = "";
        if (!StringUtil.isEmpty(org.getmLeader())) {
            List<Object> o = dao.getHelperDao().find(
                    "select name_f from teacherInfo_t where instr('"
                            + org.getmLeader() + "', concat(',',id_f,',')) > 0");
            tempStr = o.toString();
        } else {
            if (!StringUtil.isEmpty(org.getsLeader())) {
                List<Object> o = dao.getHelperDao().find(
                        "select name_f from teacherInfo_t where instr('"
                                + org.getmLeader() + "',concat(',',id_f,',')) > 0");
                tempStr = o.toString();
            }
        }
        map.put("leader", tempStr);
        map.put("partner", org.getParent() != null ? org.getParent().getName()
                : "");
        @SuppressWarnings("unchecked")
        List<String> teachers = dao
                .getHelperDao()
                .find("select t.name_f from teacherinfo_t t,subjectmapping_t s where t.id_f=s.teacher_f and s.org_f=?",
                        id);
        map.put("teacher", teachers);
        return map;
    }

    /**
     * 新增
     *
     * @param org
     */
    @Transactional
    public void insert(Org org) {
        dao.insert(org);
    }

    /**
     * id：节点id，对载入远程数据很重要。 text：显示在节点的文本。 state：节点状态，'open' or
     * 'closed'，默认为'open'。当设置为'closed'时，拥有子节点的节点将会从远程站点载入它们。 checked：表明节点是否被选择。
     * attributes：可以为节点添加的自定义属性。 children：子节点，必须用数组定义。
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, String>> findOrgs() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        String hql = "from " + Org.class.getName()
                + " o where o.status=1 and o.parent is null order by o.order";
        List<Org> lOrg = dao.find(hql);
        for (Org org : lOrg) {
            Map map = new HashMap();
            map.put("id", "org:" + org.getId());
            map.put("text", org.getName());
            map.put("state", "closed");
            map.put("iconCls", "icon-org");
            // map.put("children", "[]");spring mvc 有这行返回会出错
            list.add(map);
        }
        return list;
    }

    /**
     * 查找学校组织与老师信息
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findOrgsTeacherZtree(String id) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (id == null) {//
            String hql = "from "
                    + Org.class.getName()
                    + " o where o.status=1 and o.parent is null and o.type=0 order by o.order";
            List<Org> lOrg = dao.find(hql);
            for (Org org : lOrg) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", org.getType() + ":" + org.getId());
                map.put("name", org.getName());
                map.put("open", false);
                map.put("nocheck", true);
                map.put("isParent", "true");
                map.put("icon", "./res/img/icon/school.png");
                // map.put("children", "[]");
                list.add(map);
            }
            return list;
        } else {// 如果不为空
            String[] ids = id.split(":");
            Long orgId = Long.parseLong(ids[1]);
            int orgType = Integer.parseInt(ids[0]);
            if (orgType == 0) {
                String hql = "";
                hql += "from "
                        + Org.class.getName()
                        + " o where o.status=1 and o.parent.id=? and o.type=4 order by o.order";
                List<Org> lOrg = dao.find(hql, orgId);
                for (Org org : lOrg) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    if (org.getDate() != null) {// 如果结业年不为空，该部门是班级
                        map.put("id", org.getType() + ":" + org.getId());
                        map.put("icon", "./res/img/icon/class.png");
                    } else {
                        map.put("id", org.getType() + ":" + org.getId());
                        map.put("isParent", "true");
                        map.put("icon", "./res/img/icon/org.png");
                    }
                    map.put("name", org.getName());
                    map.put("open", false);
                    list.add(map);
                }
            }

            String hqlUser = "from " + TeacherInfo.class.getName()
                    + " u where u.org.id=?";
            List<TeacherInfo> lTeacherInfo = dao.find(hqlUser, orgId);
            for (TeacherInfo teacherInfo : lTeacherInfo) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", "teacherInfo:" + teacherInfo.getId());
                map.put("name", teacherInfo.getName());
                if (null == teacherInfo.getGender()
                        || teacherInfo.getGender() == 2) {
                    map.put("icon", "./res/img/icon/vcard.png");
                } else if (teacherInfo.getGender() == 1) {
                    map.put("icon", "./res/img/icon/boy.png");
                } else {
                    map.put("icon", "./res/img/icon/girl.png");
                }

                list.add(map);
            }
            return list;
        }
    }

    /**
     * 查找儿子部门 查询老师的组织结构
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, String>> findChildOrg(String id) {
        String[] ids = id.split(":");
        Long orgId = Long.parseLong(ids[1]);
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        String hql = "from " + Org.class.getName()
                + " o where o.status=1 and o.parent.id=? order by o.order";
        List<Org> lOrg = dao.find(hql, orgId);
        for (Org org : lOrg) {
            Map map = new HashMap();
            if (org.getDate() != null) {// 如果结业年不为空，该部门是班级
                map.put("id", "class:" + org.getId());
            } else {
                map.put("id", "org:" + org.getId());
            }
            map.put("text", org.getName());
            map.put("state", "closed");
            map.put("iconCls", "icon-org");
            list.add(map);
        }
        String hqlUser = "from " + TeacherInfo.class.getName()
                + " u where u.org.id=?";
        List<TeacherInfo> lUser = dao.find(hqlUser, orgId);
        for (TeacherInfo user : lUser) {
            Map map = new HashMap();
            map.put("id", "teacherInfo:" + user.getId());
            map.put("text", user.getName());
            map.put("state", "open");
            list.add(map);
        }
        return list;
    }

    /**
     * 根据部门类型查找部门信息
     *
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> findOrgByType(int type) {
        return dao
                .getHelperDao()
                .find("select t.id_f,t.name_f,(select tt.name_f from org_t tt where tt.id_f=t.parent_f) as parent from  org_t t where t.type_f=?",
                        type);
        // return dao.find("select id,name from " + Org.class.getName()
        // + " o where  o.type=?", type);
    }

    /**
     * 查找学生
     *
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> findOrgByTypePush(Long userId, int type) {
        boolean bz = roleService.judgeSorMRole_权限(1, userId);// 查询中学权限
        boolean bx = roleService.judgeSorMRole_权限(0, userId);// 查询小学权限
        String sql = "select t.id_f,t.name_f  from  org_t t where status_f=1 and  t.type_f=3";
        if (!(bz && bx)) {// 只有一个是true的情况才进入该判断
            if (type == 4) {
                List<Object> list = dao
                        .getHelperDao()
                        .find("select roleid_f from userrole_t ur where ur.userid_f = ?",
                                userId);
                List<String> listRoles = dao.getHelperDao().find("select name_f from role_t where id_f in(" + list.toString().replace("[", "").replace("]", "") + ")");
                for (String strRole : listRoles) {
                    if (strRole.endsWith("年级组")) {
                        String name = strRole.replace("年级组", "");
                        sql += " and t.parent_f=(select id_f from org_t tt where tt.name_f='" + name + "'  and  tt.type_f=2)";
                    }
                }
            } else if (bz) {//只有中学权限
                sql += " and t.parent_f in (select id_f from  org_t tt where tt.type_f=2 and tt.parent_f between 4 and 5 )";
            } else {
                sql += " and t.parent_f in (select id_f from  org_t tt where tt.type_f=2 and tt.parent_f=3)";
            }
        }
        return dao.getHelperDao().find(sql);
    }

    public List<Map<String, Object>> findOrgsStudentZtree(String id) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (id == null) {//
            String hql = "from "
                    + Org.class.getName()
                    + " o where o.status=1 and o.parent is null and o.type=0 and o.status=1 order by o.order";
            List<Org> lOrg = dao.find(hql);
            for (Org org : lOrg) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", org.getType() + ":" + org.getId());
                map.put("name", org.getName());
                map.put("open", false);
                map.put("isParent", "true");
                map.put("icon", "./res/img/icon/school.png");
                // map.put("children", "[]");
                list.add(map);
            }
            return list;
        } else {// 如果不为空
            String[] ids = id.split(":");
            Long orgId = Long.parseLong(ids[1]);
            String hql = "";
            hql += "from "
                    + Org.class.getName()
                    + " o where o.status=1 and o.parent.id=? and o.type between 1 and 3  order by o.order";
            List<Org> lOrg = dao.find(hql, orgId);
            for (Org org : lOrg) {
                Map<String, Object> map = new HashMap<String, Object>();
                if (org.getType() == 3) {// 如果结业年不为空，该部门是班级
                    map.put("id", org.getType() + ":" + org.getId());
                    map.put("icon", "./res/img/icon/class.png");
                } else {
                    map.put("id", org.getType() + ":" + org.getId());
                    map.put("isParent", "true");
                    map.put("icon", "./res/img/icon/org.png");
                }
                map.put("name", org.getName());
                map.put("open", false);
                list.add(map);
            }
            return list;
        }
    }


    public List<Map<String, Object>> findsto(String id) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (id == null) {//
            String hql = "from "
                    + Org.class.getName()
                    + " o where o.status=1 and o.parent is null and o.type=0 and o.status=1 order by o.order";
            List<Org> lOrg = dao.find(hql);
            for (Org org : lOrg) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", org.getType() + ":" + org.getId());
                map.put("name", org.getName());
                map.put("open", false);
                map.put("isParent", "true");
                map.put("icon", "./res/img/icon/school.png");
                // map.put("children", "[]");
                list.add(map);
            }
            return list;
        } else {// 如果不为空
            String[] ids = id.split(":");
            Long orgId = Long.parseLong(ids[1]);
            String hql = "";
            hql += "from "
                    + Org.class.getName()
                    + " o where o.status=1 and o.parent.id=? and o.type between 1 and 3  order by o.order";
            List<Org> lOrg = dao.find(hql, orgId);
            for (Org org : lOrg) {
                Map<String, Object> map = new HashMap<String, Object>();
                if (org.getType() == 3) {// 如果结业年不为空，该部门是班级
                    map.put("id", org.getType() + ":" + org.getId());
                    map.put("icon", "./res/img/icon/class.png");
                } else {
                    map.put("id", org.getType() + ":" + org.getId());
                    map.put("isParent", "true");
                    map.put("icon", "./res/img/icon/org.png");
                }
                map.put("name", org.getName());
                map.put("open", false);
                list.add(map);
            }
            if (Integer.parseInt(ids[0]) == 0) {
                Map<String, Object> mapin = new HashMap<String, Object>();
                mapin.put("id", "0:0");
                mapin.put("name", "内部短信");
                mapin.put("open", true);
                mapin.put("isParent", "false");
                mapin.put("icon", "./res/img/icon/user.png");
                list.add(mapin);
                Map<String, Object> mapOther = new HashMap<String, Object>();
                mapOther.put("id", "-1:-1");
                mapOther.put("name", "其他短信");
                mapOther.put("open", true);
                mapOther.put("isParent", "false");
                mapOther.put("icon", "./res/img/icon/class.png");
                list.add(mapOther);
            }
            return list;
        }
    }

    /**
     * 查找所有班级
     *
     * @return
     */
    public List<Map<String, Object>> findAllClass() {
        List<Org> orgs = dao.find("from " + Org.class.getName()
                + " where  type=3 and status=1");
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        for (Org org : orgs) {
            Map<String, Object> orgMap = new HashMap<String, Object>();
            orgMap.put("id", org.getId());
            // orgMap.put("name", org.getParent().getName() + "-" +
            // org.getName());
            orgMap.put("name", org.getName());
            listMap.add(orgMap);
        }
        return listMap;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findTeacherByRoleCheckedZtree(String id,
                                                                   Long rId) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (StringUtil.isEmpty(id)) {
            List<Role> roles = dao.find(Role.class, "type", 1);
            for (Role role : roles) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", "role:" + role.getId());
                map.put("name", role.getName());
                map.put("open", false);
                map.put("isParent", "true");
                map.put("icon", "./res/img/icon/school.png");
                // map.put("children", "[]");
                list.add(map);
            }
            return list;
        } else {
            List<Object[]> listObj = dao
                    .getHelperDao()
                    .find("select t.id_f,t.name_f,gender_f from teacherInfo_t t where t.id_f in(select r.userId_f from userrole_t r where r.roleId_f=? and teacher_f=1)",
                            Long.parseLong(id.split(":")[1]));
            List<Long> userList = dao.find("select userId from "
                    + UserRole.class.getName()
                    + " where roleId=? and teacher=true", rId);
            for (Object[] object : listObj) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", "teacherInfo:" + object[0]);
                map.put("name", object[1]);
                if (null == object[2] || object[2].toString().equals("2")) {
                    map.put("icon", "./res/img/icon/vcard.png");
                } else if (object[2].toString().equals("1")) {
                    map.put("icon", "./res/img/icon/boy.png");
                } else {
                    map.put("icon", "./res/img/icon/girl.png");
                }
                if (userList.contains(Long.parseLong(object[0].toString()))) {
                    map.put("checked", "true");
                }
                // map.put("children", "[]");
                list.add(map);
            }
            return list;
        }
    }

    @Autowired
    private RoleService roleService;

    /**
     * 更新部门信息
     *
     * @param org
     */
    @Transactional
    public void update(Org org) {
        // TODO Auto-generated method stub
        Org dbOrg = dao.findOne(Org.class, "id", org.getId());
        BeanUtils.copyProperties(org, dbOrg, new String[]{"id", "type",
                "parent", "status", "order"});
        dao.update(dbOrg);
    }

    /**
     * 根据老师ID 查询是否是班主任，如果是则返回班级下的学生
     *
     * @param tId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> findLeader(Long tId) {
        List<Object> list = dao.find("select id from " + Org.class.getName()
                + " where instr(mLeader,?) > 0", "," + tId + ",");
        if (list.size() == 0) {
            return new ArrayList<Object[]>();
        } else {
            List<Object[]> stuList = dao.find(
                    "select id,name from " + StudentInfo.class.getName()
                            + " where status = 0 and  org.id=? order by name asc",
                    Long.parseLong(list.get(0).toString()));
            for (Object[] objs : stuList) {
                String py = PinyinHelper.convertToPinyinString(
                        objs[1].toString(), "", PinyinFormat.WITHOUT_TONE);
                objs[1] = py.substring(0, 1).toUpperCase() + "-" + objs[1];
            }
            return stuList;
        }
    }

    /**
     * 根据老师id查询 是班主任的班级
     *
     * @param tId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> findLeaderByTeacherId(Long tId) {
        return dao.find("select id,name from " + Org.class.getName()
                + " where instr(mLeader,?) > 0", "," + tId + ",");
    }

    /**
     * 查询所有班主任
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findAllLeader() {
        List<Object> leaderIds = dao
                .getHelperDao()
                .find("select o.mleader_f from org_t o where o.type_f=3 and o.mleader_f is not null");
        String mIds = "";
        for (Object object : leaderIds) {
            String[] ids = object.toString().split(",");
            for (String mId : ids) {
                if (!StringUtil.isEmpty(mId)) {
                    mIds += mId + ",";
                }
            }
        }
        List<Object[]> l = dao.getHelperDao().find(
                "select t.id_f,t.name_f from teacherInfo_t t where t.id_f in("
                        + mIds + "-1)");
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        for (Object[] object : l) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", object[0]);
            map.put("name", object[1]);
            listmap.add(map);
        }
        return listmap;
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> findAllLeaderAPP() {
        List<Object> leaderIds = dao
                .getHelperDao()
                .find("select o.mleader_f from org_t o where o.type_f=3 and o.mleader_f is not null");
        String mIds = "";
        for (Object object : leaderIds) {
            String[] ids = object.toString().split(",");
            for (String mId : ids) {
                if (!StringUtil.isEmpty(mId)) {
                    mIds += mId + ",";
                }
            }
        }
        return dao.getHelperDao().find(
                "select t.id_f,t.name_f from teacherInfo_t t where t.id_f in("
                        + mIds + "-1)");
    }

    /**
     * 根据部门类型查找部门
     *
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> findAllObject(int type) {
        return dao.find("select id,name from " + Org.class.getName()
                + " where type=?", type);
    }

    @Transactional
    public void del(Long id) {
        dao.delete(Org.class, id);
    }

    @Transactional
    public void updateLeader(Long orgId, String leaderIds) {
        Org dbOrg = dao.findOne(Org.class, "id", orgId);
        dbOrg.setmLeader("," + leaderIds);
        dao.update(dbOrg);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findTeacherByRoles(String id) {
        String sql = "select t.id_f,t.name_f,r.name_f as roleName from teacherInfo_t t,userrole_t u,role_t r where r.id_f=u.roleid_f and u.teacher_f=1 and u.userid_f=t.id_f and u.roleid_f in("
                + id + ") order by r.name_f desc";
        List<Object[]> objects = dao.getHelperDao().find(sql);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Object[] object : objects) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("value", object[0]);
            map.put("text", object[1]);
            map.put("group", object[2]);
            list.add(map);
        }
        return list;
    }

    public List<Map<String, Object>> findstudentbyclassids(String id) {
        String sql = "select t.id_f,t.name_f,o.name_f as orgname from studentinfo_t t,org_t o where t.org_f in("
                + id + ") and t.org_f=o.id_f order by o.id_f desc";
        List<Object[]> objects = dao.getHelperDao().find(sql);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Object[] object : objects) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("value", object[0]);
            map.put("text", object[1]);
            map.put("group", object[2]);
            list.add(map);
        }
        return list;
    }

    /**
     * 删除班级/年级
     *
     * @param id
     * @return
     */
    @Transactional
    public String delclassornj(Long id, boolean nj) {
        int i = 0;
        if (nj) {// 年级
            i = dao.getHelperDao()
                    .find("select * from org_t o where o.parent_f=? ", id)
                    .size();
            if (i > 0) {
                return "该年级下包含有班级，不能删除";
            }
        } else {// 班级
            i = dao.getHelperDao()
                    .find("select * from studentinfo_t s where s.org_f=? ", id)
                    .size();
            if (i > 0) {
                return "该班级下下包含有学生，不能删除";
            }
        }
        Org dbOrg = findById(id);
        dbOrg.setStatus(false);
        dao.delete(dbOrg);
        return null;

    }
}
