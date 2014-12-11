package common.cq.hmq.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.cq.hmq.pojo.stuinfo.StudentInfo;
import app.cq.hmq.pojo.teacherinfo.TeacherInfo;
import common.cq.hmq.pojo.sys.Attach;
import common.cq.hmq.pojo.sys.User;
import common.cq.hmq.pojo.sys.UserRole;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.modal.LogonAjaxMsg;
import core.cq.hmq.modal.SessionModal;
import core.cq.hmq.pojo.sys.Role2Permission;
import core.cq.hmq.pojo.sys.User2Role;
import core.cq.hmq.service.BaseService;
import core.cq.hmq.util.tools.Encrypt;
import core.cq.hmq.util.tools.LogUtil;
import core.cq.hmq.util.tools.ResourceUtil;
import core.cq.hmq.util.tools.SessionUtil;
import core.cq.hmq.util.tools.StringUtil;

@Service(value = "userService")
public class UserService extends BaseService {

	/**
	 * 新增用户
	 * 
	 * @param user
	 */
	@Transactional()
	public AjaxMsg insert(User user) {
		User dbUser = dao.findOne(User.class, "no", user.getNo());
		AjaxMsg am = new AjaxMsg();
		if (dbUser != null) {
			am.setMsg("账号已经存在");
			am.setType(am.ERROR);
			return am;
		} else {
			user.setTel(user.getNo());
			user.setPwd(Encrypt.md5(user.getPwd()));
			dao.insert(user);
			am.setMsg("注册成功");
			return am;
		}
	}

	/**
	 * 更新
	 * 
	 * @param user
	 */
	public AjaxMsg update(User user) {
		AjaxMsg am = new AjaxMsg();
		if (validNo(user.getNo()) == 0 || validNo(user.getNo()) != user.getId()) {
			User dbUser = dao.findOne(User.class, "id", user.getId());
			try {
				dbUser.setAddress(user.getAddress());
				dbUser.setAge(user.getAge());
				dbUser.setEmail(user.getEmail());
				dbUser.setHead(user.getHead());
				dbUser.setIdentity(user.getIdentity());
				dbUser.setName(user.getName());
				dbUser.setNickName(user.getNickName());
				dbUser.setNo(user.getNo());
				dbUser.setProfession(user.getProfession());
				dbUser.setSex(user.getSex());
				dao.update(dbUser);
				am.setMsg("更新成功");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				am.setMsg("更新失败");
				am.setType(am.ERROR);
			}
		} else {
			am.setMsg("您输入的电话号码已经存在，请重新输入");
			am.setType(am.ERROR);
		}
		return am;
	}

	/**
	 * 验证账号是否存在
	 * 
	 * @param no
	 * @return
	 */
	public Long validNo(String no) {
		User dbUser = dao.findOne(User.class, "no", no);
		AjaxMsg am = new AjaxMsg();
		if (dbUser != null) {
			return dbUser.getId();
		} else {
			return (long) 0;
		}
	}

	public AjaxMsg logon(User user, HttpSession session) {
		User dbUser = dao.findOne(User.class, "no", user.getNo());
		AjaxMsg am = new AjaxMsg();
		if (dbUser == null) {
			am.setType(AjaxMsg.ERROR);
			am.setMsg("帐号不存在！");
			return am;
		} else {
			if (!dbUser.getPwd().equals(Encrypt.md5(user.getPwd()))) {
				am.setType(AjaxMsg.ERROR);
				am.setMsg("密码错误！");
				return am;
			}
			LogUtil.getLog(UserService.class).info(
					"警告：" + dbUser.getName() + "(" + dbUser.getNo() + ")登录");
			SessionUtil.saveSession(dbUser, session);
			dbUser.setDate(new Date());
			savePerByUserId(session, dbUser.getId());
			return am;
		}
	}

	/**
	 * 涉及多个学生
	 * 
	 * @param no
	 * @param pwd
	 * @param session
	 * @param userType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public AjaxMsg StudentLogon(String no, String pwd, HttpSession session,
			String userType, Long uId) {
		AjaxMsg am = new AjaxMsg();
		if (User.STUDENTTYPE.equals(userType)) {
			List<StudentInfo> dbstu = new ArrayList<StudentInfo>();
			if (uId != null) {
				dbstu.add((StudentInfo) dao
						.findOne(
								"from StudentInfo where id=? and status=0 and deleteType is null",
								uId));
			} else {
				// dbstu = dao.find(StudentInfo.class, "no", no);
				dbstu = dao
						.find("from StudentInfo where no = ? and status = 0 and deleteType is null",
								no);
			}
			if (dbstu.size() == 0) {
				am.setType(AjaxMsg.ERROR);
				am.setMsg("帐号不存在！");
				return am;
			} else {
				if (!dbstu.get(0).getPwd().equals(Encrypt.md5(pwd))) {
					am.setType(AjaxMsg.ERROR);
					am.setMsg("密码错误！");
					return am;	
				}
				if (dbstu.size() == 1) {
					LogUtil.getLog(UserService.class).info(
							"警告：家长" + dbstu.get(0).getName() + "("
									+ dbstu.get(0).getNo() + ")登录");
					SessionModal sessionModal = new SessionModal();
					sessionModal.setId(dbstu.get(0).getId());
					sessionModal.setNo(dbstu.get(0).getNo());
					sessionModal.setName(dbstu.get(0).getName());
					sessionModal.setUserType(userType);
					sessionModal.setPng(dbstu.get(0).getHeadPic() == null ? ""
							: dbstu.get(0).getHeadPic().getHtmlUrl());
					if (null != dbstu.get(0).getOrg()) {
						sessionModal.setOrgId(dbstu.get(0).getOrg().getId());
						sessionModal
								.setOrgName(dbstu.get(0).getOrg().getName());
					}
					sessionModal.setSessionId(dbstu.get(0).getId() + ":"
							+ dbstu.get(0).getNo());
					session.setAttribute(ResourceUtil.getSessionInfoName(),
							sessionModal);
					savePerById(session, dbstu.get(0).getId(), false);
					return am;
				} else {
					String studentInfos = "";
					for (StudentInfo sio : dbstu) {
						studentInfos += sio.getId() + ":" + sio.getName() + ",";
					}
					am.setMsgId(studentInfos.substring(0,
							studentInfos.length() - 1));
					am.setType(am.INFO);
					return am;
				}

			}
		} else if (User.TEACHERTYPE.equals(userType)) {
			TeacherInfo teacherInfo = (TeacherInfo) dao.findOne(" from "
					+ TeacherInfo.class.getName() + " where no=? and status=1",
					no);
			// TeacherInfo teacherInfo = dao.findOne(TeacherInfo.class, "no",
			// no);
			if (null == teacherInfo) {
				am.setType(AjaxMsg.ERROR);
				am.setMsg("帐号不存在！");
				return am;
			} else {
				if (!teacherInfo.getPwd().equals(Encrypt.md5(pwd))) {
					am.setType(AjaxMsg.ERROR);
					am.setMsg("密码错误！");
					return am;
				}
				LogUtil.getLog(UserService.class).info(
						"警告：老师" + teacherInfo.getName() + "("
								+ teacherInfo.getNo() + ")登录");
				SessionModal sessionModal = new SessionModal();
				sessionModal.setId(teacherInfo.getId());
				sessionModal.setNo(teacherInfo.getNo());
				sessionModal.setName(teacherInfo.getName());
				sessionModal.setUserType(userType);
				sessionModal.setPng(teacherInfo.getHeadpic() == null ? ""
						: teacherInfo.getHeadpic().getHtmlUrl());
				sessionModal.setSessionId(teacherInfo.getId() + ":"
						+ teacherInfo.getNo());
				session.setAttribute(ResourceUtil.getSessionInfoName(),
						sessionModal);
				if (null != teacherInfo.getOrg()) {
					sessionModal.setOrgId(teacherInfo.getOrg().getId());
					sessionModal.setOrgName(teacherInfo.getOrg().getName());
				}
				savePerById(session, teacherInfo.getId(), true);
				return am;
			}
		}
		// TODO 暂时注释
		// savePerByUserId(session, studentInfo.getId());
		return am;
	}

	public AjaxMsg logonImgAuth(User user, HttpSession session, String authImg) {
		AjaxMsg am = new AjaxMsg();
		if (!session.getAttribute("AUTH_IMG_IN_SESSION").equals(authImg)) {
			am.setType(AjaxMsg.ERROR);
			am.setMsg("验证码错误！");
			return am;
		}
		User dbUser = dao.findOne(User.class, "no", user.getNo());
		if (dbUser == null) {
			am.setType(AjaxMsg.ERROR);
			am.setMsg("帐号不存在！");
			return am;
		} else {
			if (!dbUser.getPwd().equals(Encrypt.md5(user.getPwd()))) {
				am.setType(AjaxMsg.ERROR);
				am.setMsg("密码错误！");
				return am;
			}
			SessionUtil.saveSession(dbUser, session);
			dbUser.setDate(new Date());
			savePerByUserId(session, dbUser.getId());
			am.setMsg("登录成功");
			return am;
		}
	}

	/**
	 * 判断是否登录
	 * 
	 * @param session
	 * @return
	 */
	public boolean isLogon(HttpSession session) {
		SessionModal sModal = (SessionModal) session.getAttribute(ResourceUtil
				.getSessionInfoName());
		if (sModal != null) {
			if (sModal.getSessionId() != null) {
				return true;
			}
		}
		return false;
	}

	public boolean isNull() {
		return dao.findOne(User.class) == null ? true : false;
	}

	public User findUserById(Long id) {
		return dao.findOne(User.class, "id", id);
	}

	/**
	 * 获取用户的所有权限，并保存到session中
	 * 
	 * @param uid
	 * @return
	 */
	public void savePerByUserId(HttpSession session, Long uid) {
		Set<String> setPer = findPerByUserId(uid);
		session.setAttribute(ResourceUtil.getPermissionCookie(), setPer);
	}

	/**
	 * 获取用户的所有权限，并保存到session中 家校通项目
	 * 
	 * @param uid
	 * @return
	 */
	public void savePerById(HttpSession session, Long uid, boolean b) {
		Set<String> setPer = findPerByUserIdAndType(uid, b);
		session.setAttribute(ResourceUtil.getPermissionCookie(), setPer);
	}

	/**
	 * 获取用户的所有权限
	 * 
	 * @param type
	 * @param uId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Set<String> findPerByUserIdAndType(Long uId, boolean type) {
		Set<String> setPer = new HashSet<String>();

		List<Object> urss = dao
				.getHelperDao()
				.find("select u.roleId_f from userrole_t u, role_t r where r.type_f=0 and u.roleid_f= r.id_f  and u.userid_f=? and u.teacher_f=? ",
						uId, type);
		for (Object obj : urss) {
			List<Role2Permission> r2ps = dao.find(Role2Permission.class,
					"role.id", Long.parseLong(obj.toString()));
			for (Role2Permission role2Permission : r2ps) {
				setPer.add(role2Permission.getPermission().getId() + "");
			}
		}
		// List<UserRole> urs = dao.find("from " + UserRole.class.getName()
		// + " ur where ur.userId=? and ur.teacher=?", uId, type);

		// for (UserRole userRole : urs) {
		// List<Role2Permission> r2ps = dao.find(Role2Permission.class,
		// "role.id", userRole.getRoleId());
		// for (Role2Permission role2Permission : r2ps) {
		// setPer.add(role2Permission.getPermission().getId() + "");
		// }
		// }
		if (setPer.size() == 0) {
			setPer.add("-1");
		}
		return setPer;
	}

	/**
	 * 获取用户的所有权限
	 * 
	 * @param uId
	 * @return
	 */
	public Set<String> findPerByUserId(Long uId) {
		Set<String> setPer = new HashSet<String>();
		List<User2Role> u2rs = dao.find(User2Role.class, "user.id", uId);
		for (User2Role user2Role : u2rs) {
			List<Role2Permission> r2ps = dao.find(Role2Permission.class,
					"role.id", user2Role.getRole().getId());
			for (Role2Permission role2Permission : r2ps) {
				setPer.add(role2Permission.getPermission().getId() + "");
			}
		}
		if (setPer.size() == 0) {
			setPer.add("-1");
		}
		return setPer;
	}

	/**
	 * 该角色是否有该权限 有时间再做缓存(已把权限做到session中)
	 * 
	 * @deprecated
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean canPermissionByUserId(Long uid, Long pid) {
		List<Role2Permission> r2ps = dao.find(Role2Permission.class,
				"permission.id", pid);
		List<User2Role> u2rs = dao.find(User2Role.class, "user.id", uid);
		for (User2Role u2r : u2rs) {
			for (Role2Permission r2p : r2ps) {
				if (r2p.getRole().getId() == u2r.getRole().getId()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 密码修改
	 * 
	 * @param id
	 * @param oldPwd
	 * @param pwd
	 * @return
	 */
	public AjaxMsg pwdChange(Long id, String oldPwd, String pwd) {
		User user = dao.findOne(User.class, "id", id);
		AjaxMsg am = new AjaxMsg();
		if (user.getPwd().equals(Encrypt.md5(oldPwd))) {
			user.setPwd(Encrypt.md5(pwd));
			try {
				dao.update(user);
				am.setMsg("密码更新成功");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				am.setMsg("密码更新失败");
				am.setType(am.ERROR);
			}
		} else {
			am.setMsg("历史密码输入不正确");
			am.setType(am.ERROR);
		}
		return am;
	}

	/**
	 * 根据角色id查找该角色下的用户
	 * 
	 * @param id
	 * @return
	 */
	public List<User> findUserByRoleId(Long id) {
		List<User> users = new ArrayList<User>();
		List<User2Role> u2r = dao.find(User2Role.class, "role.id", id);
		for (User2Role user2Role : u2r) {
			users.add(user2Role.getUser());
		}
		return users;
	}

	/** 根据当前用户ID，查询当前用户的部门负责人 */
	public Long findLeaderByCurrentUserId() {
		// User dbUser = findUserById(currentUserId());
		// if (dbUser == null) {
		// return null;
		// } else if (dbUser.getOrg() == null) {
		// return null;
		// } else if (dbUser.getOrg().getLeader() == null) {
		// return null;
		// } else {
		// return dbUser.getOrg().getLeader().getId();
		// }
		return null;
	}

	/**
	 * 修改自己头像
	 * 
	 * @return
	 */
	@Transactional
	public AjaxMsg changeImg(Long attachId, HttpSession session) {
		SessionModal sm = currentSessionModel();
		if (sm.getUserType().equals("1")) {
			StudentInfo stuInfo = dao.findOne(StudentInfo.class, "id",
					sm.getId());
			Attach attach = new Attach();
			attach.setId(attachId);
			stuInfo.setHeadPic(attach);
		}
		if (sm.getUserType().equals("2")) {
			TeacherInfo teaInfo = dao.findOne(TeacherInfo.class, "id",
					sm.getId());
			Attach attach = new Attach();
			attach.setId(attachId);
			teaInfo.setHeadpic(attach);
		}
		sm.setPng("download/" + attachId + ".png");
		session.setAttribute(ResourceUtil.getSessionInfoName(), sm);
		return new AjaxMsg("更新成功");
	}

	@Transactional
	public AjaxMsg modifyPwd(String oP, String nP) {
		AjaxMsg aMsg = new AjaxMsg();
		if (StringUtil.isEmpty(oP) || StringUtil.isEmpty(nP)) {
			aMsg.setType(AjaxMsg.ERROR);
			aMsg.setMsg("密码不能为空");
			return aMsg;
		}
		SessionModal sm = currentSessionModel();
		if (sm.getUserType().equals(User.STUDENTTYPE)) {
			List<StudentInfo> list = dao.find(StudentInfo.class, "no",
					sm.getNo());
			if (Encrypt.md5(oP).equals(list.get(0).getPwd())) {
				for (StudentInfo studentInfo : list) {
					studentInfo.setPwd(Encrypt.md5(nP));
					dao.update(studentInfo);
				}
			} else {
				aMsg.setType(AjaxMsg.ERROR);
				aMsg.setMsg("原密码错误");
				return aMsg;
			}
		}
		if (sm.getUserType().equals(User.TEACHERTYPE)) {
			TeacherInfo teaInfo = dao.findOne(TeacherInfo.class, "id",
					sm.getId());
			if (Encrypt.md5(oP).equals(teaInfo.getPwd())) {
				teaInfo.setPwd(Encrypt.md5(nP));
				dao.update(teaInfo);
			} else {
				aMsg.setType(AjaxMsg.ERROR);
				aMsg.setMsg("原密码错误");
				return aMsg;
			}
		}
		aMsg.setMsg("修改成功!");
		return aMsg;
	}
}
