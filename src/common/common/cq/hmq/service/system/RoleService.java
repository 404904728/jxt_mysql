package common.cq.hmq.service.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import common.cq.hmq.model.JqGridData;
import common.cq.hmq.model.JqPageModel;
import common.cq.hmq.pojo.sys.User;
import common.cq.hmq.pojo.sys.UserRole;
import core.cq.hmq.dao.PageList;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.modal.EasyData;
import core.cq.hmq.modal.PageModel;
import core.cq.hmq.pojo.sys.Role;
import core.cq.hmq.pojo.sys.User2Role;
import core.cq.hmq.service.BaseService;
import core.cq.hmq.util.tools.StringUtil;

@Service(value = "roleService")
public class RoleService extends BaseService {

    @SuppressWarnings("unchecked")
    public EasyData<Role> find(PageModel model) {
        return findClass(model, Role.class);
    }

    /**
     * 查找所有角色/除开超级管理员
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> findAll() {
        return dao.getHelperDao().find(
                "select id_f,name_f from role_t where id_f!=1");
    }

    /**
     * 查询所有角色
     *
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    public JqGridData<Role> findAll(JqPageModel model) {
        JqGridData<Role> jq = new JqGridData<Role>();
        String hql = "";
        if (StringUtil.isEmpty(model.getSearchKey())) {
            hql += "from " + Role.class.getName() + " r where r.type=0 ";
        } else {
            hql += "from " + Role.class.getName()
                    + " r where r.type=0  and  r.name like '%"
                    + model.getSearchKey() + "%' ";
        }
        if (!StringUtil.isEmpty(model.getSort())) {
            hql += "  order by " + model.getSort() + " " + model.getOrder();
        }
        PageList<Role> roles = page(model, hql);
        jq.setPage(roles.getPageNo());
        jq.setRecords(roles.getTotalCount());
        jq.setRows(roles.getList());
        jq.setTotal(roles.getPageCount());
        return jq;
    }

    public Role findById(Long id) {
        return dao.findOne(Role.class, "id", id);
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> findByIdImpl(Long id) {
        return dao
                .getHelperDao()
                .find("select t.id_f,t.name_f from userrole_t ur,teacherInfo_t t where ur.roleId_f=? and ur.userId_f=t.id_f and ur.teacher_f=1",
                        id);
    }

    @Transactional
    public AjaxMsg save(Role role) {
        AjaxMsg am = new AjaxMsg();
        try {
            dao.insert(role);
            am.setMsg("添加成功");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            am.setMsg("添加失败：error" + e.getMessage());
        }
        return am;
    }

    @Transactional
    public AjaxMsg update(Role role) {
        AjaxMsg am = new AjaxMsg();
        try {
            Role dbRole = dao.findOne(Role.class, "id", role.getId());
            BeanUtils.copyProperties(role, dbRole, new String[]{"id"});
            dao.update(dbRole);
            am.setMsg("更新成功");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            am.setMsg("更新失败：error" + e.getMessage());
        }
        return am;
    }

    @Transactional
    public AjaxMsg del(long id) {
        return deleteClass(Role.class, id);
    }

    /**
     * 根据用户id获取用户的角色
     *
     * @param id
     * @return
     */
    public List<Long> currentRoleByUserId(Long id) {
        List<User2Role> u2rList = dao.find(User2Role.class, "user.id", id);
        List<Long> roleIdList = new ArrayList<Long>();
        for (User2Role u2r : u2rList) {
            roleIdList.add(u2r.getRole().getId());
        }
        return roleIdList;
    }

    @Transactional
    public AjaxMsg addUser(Long userId, Long roleId) {
        User2Role dbU2R = (User2Role) dao.findOne(
                "from " + User2Role.class.getName()
                        + " u2r where user.id=? and role.id=?", userId, roleId);
        if (dbU2R != null) {
            return new AjaxMsg("该用户已经拥有该角色");
        }
        User dbUser = dao.findOne(User.class, "id", userId);
        Role dbRole = dao.findOne(Role.class, "id", roleId);
        User2Role u2r = new User2Role();
        u2r.setRole(dbRole);
        u2r.setUser(dbUser);
        dao.insert(u2r);
        return new AjaxMsg("添加成功");
    }

    /**
     * 为角色绑定 老师
     *
     * @param roleId
     * @param userIds
     * @return
     */
    @Transactional
    public AjaxMsg roleuserbinding(Long roleId, String userIds) {
        AjaxMsg am = new AjaxMsg();
        try {
            dao.getHelperDao()
                    // 删除该角色下的所有人
                    .excute("delete from userrole_t where teacher_f=1 and roleId_f=?",
                            roleId);
            String[] teacherIds = userIds.split(",");
            Set<Long> set = new HashSet<Long>();
            for (String userId : teacherIds) {
                set.add(Long.parseLong(userId));
            }
            for (Long id : set) {
                UserRole ur = new UserRole();
                ur.setRoleId(roleId);
                ur.setUserId(id);
                dao.insert(ur);
            }
            am.setMsg("配置成功");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            am.setType(am.ERROR);
            am.setMsg("配置失败");
        }
        return am;
    }

    @Transactional
    public AjaxMsg delUser(Long userId, Long roleId) {
        User2Role dbU2R = (User2Role) dao.findOne(
                "from " + User2Role.class.getName()
                        + " u2r where user.id=? and role.id=?", userId, roleId);
        dao.delete(User2Role.class, dbU2R.getId());
        return new AjaxMsg("删除成功");
    }

    /**
     * 根据角色ID 查询老师的id与名字
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> findUserByRoleId(Long id) {
        return dao
                .getHelperDao()
                .find("select t.id_f,t.name_f from UserRole_t ur,TeacherInfo_t t where t.id_f=ur.userId_f and ur.roleId_f=? and ur.teacher_f=1",
                        id);
    }

    /**
     * 根据登录人的id，查询该人是否具有中学教务处\中学学生出(1045\1042)的角色 125(1043,1046)
     * 小学教务处、小学学生处(1041\1044) id暂时固定为上述id 1 小/中 学生处 2 小/中 学教务处
     *
     * @param uId
     * @return
     */
    public int judgeSorMRole(Long uId) {
        if (null != uId) {
            List resultList = null;
            resultList = dao
                    .getHelperDao()
                    .find("select 1 from userrole_t ur where ur.roleid_f in (1041,1044) and ur.userid_f = ?",
                            uId);
            if (null != resultList && resultList.size() > 0) {
                return 1;
            } else {
                resultList = dao
                        .getHelperDao()
                        .find("select 1 from userrole_t ur where ur.roleid_f in (1043,1046) and ur.userid_f = ?",
                                uId);
                if (null != resultList && resultList.size() > 0) {
                    return 2;
                }
            }
        }
        return 0;
    }

    /**
     * 成都服务器 根据登录人的id，查询该人是否具有中学教务处\中学学生出(1\1043)的角色 小学教务处、小学学生处(1281\1041)
     * id暂时固定为上述id 1 小/中 学生处 2 小/中 学教务处
     *
     * @param type 1查询是否有中学教务处学生处，年级组长权限，其他查询是否有小学教务处，学生处，年级组长权限
     * @return
     */
    public boolean judgeSorMRole_权限(int type, Long userId) {
        if (type == 1) {
            // 中学权限
            return !dao
                    .getHelperDao()
                    .find("select 1 from userrole_t ur where ur.roleid_f in (1,1043,1251) and ur.userid_f = ?",
                            userId).isEmpty();
        } else {
            // 小学权限
            return !dao
                    .getHelperDao()
                    .find("select 1 from userrole_t ur where ur.roleid_f in (1281,1041,1322) and ur.userid_f = ?",
                            userId).isEmpty();
        }

    }

    /**
     * 老师查找
     *
     * @param tid
     * @param noticeType
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findTeacherFindBack(Long tid,
                                                         int noticeType) {
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        List<Role> roles = null;
        // 当前发送通知的人含有哪些权限
        List<Object> list = dao
                .getHelperDao()
                .find("select roleid_f from userrole_t ur where ur.userid_f = ?",
                        tid);
        String roleids = "";
        for (Object object : list) {
            try {
                String nowroles = ResourceBundle.getBundle("notice").getString(
                        object.toString());
                if (StringUtil.isEmpty(nowroles)) {
                    continue;
                }
                roleids += nowroles + ",";
            } catch (Exception e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
                continue;// 没有找到
            }
        }
        if (noticeType == 4) {
            for (Object obj : list) {
                if (obj.toString().equals("1251")) { // 中学年级组长
                    List<String> listRoles = dao.getHelperDao().find("select name_f from role_t where id_f in(" + list.toString().replace("[", "").replace("]", "") + ")");
                    for (String strRole : listRoles) {
                        if (strRole.endsWith("年级组")) {
                            roles = dao.find("from Role where use=1 and name_f like '" + strRole.replace("年级组", "") + "%'");
                        }
                    }
                    continue;
                }
                if (obj.toString().equals("1322")) {  // 小学年级组长
                    List<String> listRoles = dao.getHelperDao().find("select name_f from role_t where id_f in(" + list.toString().replace("[", "").replace("]", "") + ")");
                    for (String strRole : listRoles) {
                        if (strRole.endsWith("年级组")) {
                            roles = dao.find("from Role where use=1 and name_f like '" + strRole.replace("年级组", "") + "%'");
                        }
                    }
                    continue;
                }
            }
        } else {
            if (tid == 2) {
                roles = dao.find(Role.class, "type", 1);
            } else {
                roles = dao.find("from Role where  use=1 and id in(" + roleids
                        + "-1)");
            }
        }
        if (null == roles) {
            return listMap;
        }
        for (Role role : roles) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", role.getId());
            map.put("text", role.getName());
            map.put("iconCls", "icon-org");
            listMap.add(map);
        }
        return listMap;
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> findTeacherFindBackApp(Long tid, int noticeType) {
        List<Object[]> roles = null;
        if ((null != tid && 1 != tid) && (2 == noticeType || 3 == noticeType)) {
            if (1 == judgeSorMRole(tid)) {
                roles = dao
                        .find("select id,name from Role where type = 1 and isSorM in (0,1)");
            } else if (2 == judgeSorMRole(tid)) {
                roles = dao
                        .find("select id,name from Role where type = 1 and isSorM in (0,2)");
            }
        } else {
            roles = dao.find("select id,name from Role where type = 1");
        }
        if (null == roles) {
            return new ArrayList<Object[]>();
        }
        return roles;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findTeacherRoleZtree(String id) {
        // TODO Auto-generated method stub
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
                // map.put("children", "[]");
                list.add(map);
            }
            return list;
        }

    }

}
