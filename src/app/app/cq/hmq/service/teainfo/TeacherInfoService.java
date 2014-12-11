package app.cq.hmq.service.teainfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import app.cq.hmq.pojo.teacherinfo.TeacherInfo;

import common.cq.hmq.model.JqGridData;
import common.cq.hmq.model.JqPageModel;
import common.cq.hmq.pojo.sys.Attach;
import common.cq.hmq.pojo.sys.UserRole;
import common.cq.hmq.service.AttachService;

import core.cq.hmq.dao.PageList;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.service.BaseService;
import core.cq.hmq.util.tools.Encrypt;
import core.cq.hmq.util.tools.ResourceUtil;
import core.cq.hmq.util.tools.StringUtil;

@Service
public class TeacherInfoService extends BaseService {

	/**
	 * 
	 * @param tId
	 * @return
	 */
	public TeacherInfo findById(Long tId) {
		return dao.findOne(TeacherInfo.class, "id", tId);
	}

	/**
	 * 根据教师id查询出该教师所属组织
	 * 
	 * @param tId
	 * @return
	 */
	public String getZuzhiByTeaId(Long tId) {
		List list = dao
				.getHelperDao()
				.find("select wmsys.wm_concat(r.name_f) from userrole_t ur, "
						+ "role_t r where ur.roleid_f = r.id_f and ur.teacher_f = 1 and ur.userid_f = ? and r.type_f = 1",
						tId);
		if (null != list && list.size() > 0) {
			return String.valueOf(list.get(0));
		}
		return "";
	}

	/**
	 * 检索老师信息
	 * 
	 * @param query
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JqGridData<Map> findAll(JqPageModel model) {
		JqGridData<Map> jq = new JqGridData<Map>();

		/*
		 * String hql = "from " + TeacherInfo.class.getName() + " where 1=1"; if
		 * (model.getSearchKey() != null && !"".equals(model.getSearchKey())) {
		 * hql += " and name like'%" + model.getSearchKey() + "%'"; } if
		 * (model.getSidx() != null && !"".equals(model.getSidx())) { hql +=
		 * " order by " + model.getSidx() + " " + model.getSord(); } else { hql
		 * += " order by id asc"; }
		 */

		StringBuffer sql = new StringBuffer();
		String condition = "";
		/** 部门类型传入4 虚拟组织类型传入1 */
		sql.append("select t.id_f,t.name_f,t.telephone_f,t.gender_f,(select GROUP_CONCAT(r.name_f) from userrole_t ur, "
				+ "role_t r where ur.roleid_f = r.id_f and ur.teacher_f = 1 and ur.userid_f = t.id_f and r.type_f = 1) zuzhi,"
				+ "o.name_f orgname from teacherinfo_t t,org_t o where t.org_f = o.id_f and o.type_f = 4");
		sql.append(" and t.status_f=1 ");
		if (model.getSearchKey() != null && !"".equals(model.getSearchKey())) {
			condition = "and t.name_f like'%" + model.getSearchKey() + "%'";
			sql.append(condition);
		}
		if (model.getSidx() != null && !"".equals(model.getSidx())) {
			sql.append(" order by t." + model.getSidx() + "_f "
					+ model.getSord());
		} else {
			sql.append(" order by t.id_f asc");
		}

		PageList<Object[]> pageList = dao.getHelperDao().pageByTotal(
				model.getPage(), model.getRows(), sql.toString(),
				"select count(1) from teacherinfo_t t where 1 = 1" + condition);
		List<Map> rList = new ArrayList<Map>();
		if (null != pageList && null != pageList.getList()) {
			Map<String, Object> map = null;
			for (Object[] objs : pageList.getList()) {
				map = new HashMap<String, Object>();
				map.put("id", objs[0]);
				map.put("name", objs[1]);
				map.put("tel", objs[2]);
				map.put("gender", objs[3]);
				map.put("zuzhi", objs[4]);
				map.put("org", objs[5]);
				rList.add(map);
			}
		}

		// PageList<TeacherInfo> plt = page(model, sql.toString());

		jq.setPage(pageList.getPageNo());
		jq.setRecords(pageList.getTotalCount());
		jq.setRows(rList);
		jq.setTotal(pageList.getPageCount());
		return jq;
	}

	/**
	 * 查找当前登录老师
	 * 
	 * @return
	 */
	public TeacherInfo currentTeacher() {
		return findById(currentUserId());
	}

	/**
	 * 重置老师密码
	 * 
	 * @param tId
	 * @return
	 */
	public AjaxMsg resetPwd(Long tId) {
		AjaxMsg am = new AjaxMsg();
		try {
			TeacherInfo tInfo = dao.findOne(TeacherInfo.class, "id", tId);
			tInfo.setPwd(Encrypt.md5("123456"));
			dao.update(tInfo);
			am.setMsg("密码重置成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			am.setType(am.ERROR);
			am.setMsg("密码重置失败");
		}
		return am;
	}

	@Transactional
	public void saveOrUpdate(TeacherInfo teacherInfo, String roleIds) {
		// TODO Auto-generated method stub
		if (teacherInfo.getId() == null) {
			teacherInfo.setNo(teacherInfo.getTelePhone());
			teacherInfo.setPwd(Encrypt.md5("123456"));
			Attach attach = new Attach();
			if (teacherInfo.getGender() == 0) {
				attach.setId(32L);
			} else if (teacherInfo.getGender() == 1) {
				attach.setId(32L);
			} else {
				attach.setId(33L);
			}
			teacherInfo.setHeadpic(attach);
			dao.insert(teacherInfo);
			if (StringUtil.isEmpty(roleIds)) {
				return;
			}
			String[] roles = roleIds.split(",");
			for (String id : roles) {
				UserRole ur = new UserRole();
				ur.setRoleId(Long.parseLong(id));
				ur.setTeacher(true);
				ur.setUserId(teacherInfo.getId());
				dao.insert(ur);
			}
		} else {
			// dao.getHelperDao()
			// .excute("DELETE　from userrole_t ur where ur.userId_f=? and ur.teacher=1",
			// teacherInfo.getId());
			TeacherInfo dbTeacherInfo = dao.findOne(TeacherInfo.class, "id",
					teacherInfo.getId());
			BeanUtils.copyProperties(teacherInfo, dbTeacherInfo, new String[] {
					"id", "headpic", "no", "pwd" });
			dbTeacherInfo.setNo(teacherInfo.getTelePhone());
			dao.update(dbTeacherInfo);

			if (StringUtil.isEmpty(roleIds)) {
				return;
			}

			dao.excute(
					"delete UserRole ur where exists(select 1 from Role r where ur.teacher = 1 and ur.userId = ? and r.type = 1 and r.id = ur.roleId)",
					teacherInfo.getId());
			String[] roles = roleIds.split(",");
			for (String id : roles) {
				UserRole ur = new UserRole();
				ur.setRoleId(Long.parseLong(id));
				ur.setTeacher(true);
				ur.setUserId(teacherInfo.getId());
				dao.insert(ur);
			}
		}

	}

	@Autowired
	private AttachService attachService;

	/**
	 * 删除老师信息
	 * 
	 * @param sId
	 * @return
	 */
	@Transactional
	public void deleteById(Long id) {
		TeacherInfo t = dao.findOne(TeacherInfo.class, "id", id);
		if (t.getHeadpic() != null) {
			// attachService.delete(t.getHeadpic().getId());// 删除头像
		}
		t.setStatus(0);
		dao.update(t);
		// dao.delete(t);
	}

	/**
	 * 
	 * 
	 * @param id
	 * @return
	 */
	public List<TeacherInfo> findTeacherInfoByOrgType(Long id) {
		return dao.find(TeacherInfo.class, "org.id", id);
	}

	/**
	 * 查找班主任角色下的人员
	 * 
	 * @return
	 */
	public List<Object[]> findTeacherInfoByRoleBZR() {
		return dao
				.getHelperDao()
				.find("select t.id_f,t.name_f,o.name_f as orgname from userrole_t ur,teacherinfo_t t,org_t o where ur.roleId_f=1047 and ur.userId_f=t.id_f and ur.teacher_f=1 and o.id_f=t.org_f");
	}

	/**
	 * 
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> findTeacherInfoAllByOrgId() {
		return dao.find("select id,name,org.name from "
				+ TeacherInfo.class.getName() + " order by org.id");
	}

	/**
	 * 
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> findTeacherInfoByOrgId(Long id) {
		return dao.find("select id,name from " + TeacherInfo.class.getName()
				+ " where org.id=?", id);
	}

	/**
	 * 
	 * @title
	 * @author Limit
	 * @date 2014-4-17 下午2:32:19
	 * @version 1.0
	 * @Description 通过用户登录ID查询此用户是否是班主任和和 具有 导入权限
	 * 
	 * @param id
	 * @return
	 * 
	 * 
	 */
	public Map<String, Object> resizeteaType(Long id) {
		Map<String, Object> resap = new HashMap<String, Object>();
		if ("2".equals(currentSessionModel().getUserType())) {
			// 查询是否是班主任
			String querybjzr = "select org.id_f from org_t org "
					+ "where instr(org.mleader_f, CONCAT(',',"+id+",',')) > 0 and org.type_f = 3 LIMIT 1";
			/*
			 * // 判断是否是年级主任 String querynjzr = "select ORG.ID_F from org_t org "
			 * + "where org.type_f = 2 and instr(org.mleader_f,','||" + id +
			 * "||',') > 0 AND ROWNUM =1";
			 */

			/** 判断是否具有成绩导入的权限 */
			HttpSession session = ((ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes()).getRequest().getSession();
			Set<String> setPer = (Set<String>) session
					.getAttribute(ResourceUtil.getPermissionCookie());
			/** 具有导入权限 */
			if (setPer.contains("1000")) {
				resap.put("drqx", 1);
			} else {
				resap.put("drqx", null);
			}
			List bjzr = dao.getHelperDao().find(querybjzr);
			/* List njzr = dao.getHelperDao().find(querynjzr); */
			if (null != bjzr) {
				if (bjzr.size() != 0) {
					/**
					 * 当一个教师只是一个班级的班主任，该取法是正确的， 若一个教师是多个班级的班主任，则会取出第一个班级，忽略其它班级
					 */
					resap.put("bjzr", bjzr.get(0));
				} else {
					resap.put("bjzr", null);
				}
			} else {
				resap.put("bjzr", null);
			}
			/*
			 * if (null != njzr) { if (njzr.size() != 0) { resap.put("njzr",
			 * njzr.get(0)); } else { resap.put("njzr", null); } }
			 */
		}
		return resap;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> findPushMsg(String receiveIds) {
		return dao
				.getHelperDao()
				.find("select channelId_f,userId_f,android_f,no_f  from TeacherInfo_t  where id_f in("
						+ receiveIds + ")");
	}

	public List<Object[]> getRoleByType(Long type, Long userId) {
		return dao
				.getHelperDao()
				.find("select re.id_f, re.name_f, ur.roleid_f from (select id_f,name_f from role_t where type_f = ? order by id_f asc) re"
						+ " left join userrole_t ur on (re.id_f = ur.roleid_f and ur.teacher_f = 1 and ur.userid_f = ?)",
						type, userId);
	}

}
