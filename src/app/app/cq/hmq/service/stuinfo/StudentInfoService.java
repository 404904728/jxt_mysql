/**
 *
 */
package app.cq.hmq.service.stuinfo;

import java.io.ObjectStreamClass;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import opensource.jpinyin.PinyinFormat;
import opensource.jpinyin.PinyinHelper;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import app.cq.hmq.pojo.stuinfo.StudentInfo;

import common.cq.hmq.model.JqGridData;
import common.cq.hmq.model.JqPageModel;
import common.cq.hmq.pojo.sys.Attach;
import common.cq.hmq.pojo.sys.Org;
import common.cq.hmq.service.AttachService;

import core.cq.hmq.dao.PageList;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.modal.SessionModal;
import core.cq.hmq.pojo.LogRecord;
import core.cq.hmq.service.BaseService;
import core.cq.hmq.util.tools.Encrypt;
import core.cq.hmq.util.tools.StringUtil;

/**
 * @author Limit
 *         <p/>
 *         处理具体某一位学生的service
 */
@Service
public class StudentInfoService extends BaseService {

	/**
	 * 根据id查找学生信息
	 * 
	 * @param sId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public StudentInfo findById(Long sId) {
		// TODO Auto-generated method stub
		StudentInfo stuInfo = dao.findOne(StudentInfo.class, "id", sId);
		List<Object> o = dao.getHelperDao().find(
				"select name_f from teacherInfo_t where instr('"
						+ stuInfo.getOrg().getmLeader()
						+ "',concat(',',id_f,',')) > 0");
		stuInfo.setLeader(o.toString());
		return stuInfo;
	}

	/**
	 * @param request
	 * @title
	 * @author Limit
	 * @date 2014-3-11 上午11:27:35
	 * @version 1.0
	 * @Description 教师点击学生信息后初始化班级下拉框
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Org> queryClassInfo(HttpServletRequest request) {
		SessionModal sessionModal = currentSessionModel();
		Long id = sessionModal.getId();
		List<Org> list = new ArrayList<Org>();
		// List<Org> resList = dao
		// .find("from Org where id in (select org.id from SubjectMapping where teacher = ?)",
		// id);
		List resList = dao
				.getHelperDao()
				.find("select distinct(o.id_f),o.name_f "
						+ "from org_t o where o.id_f  in (select sm.org_f from subjectmapping_t sm where sm.teacher_f = "
						+ id
						+ ") "
						+ "union select o.id_f,o.name_f from org_t o where instr(o.mleader_f,concat(',',"+id+",',')) > 0 and o.type_f = 3");
		if (null == resList || resList.size() == 0) {
			return list;
		}
		Object[] object = null;
		Org org = null;
		for (int i = 0; i < resList.size(); i++) {
			object = (Object[]) resList.get(i);
			org = new Org();
			if (null != object) {
				org.setId(Long.parseLong(object[0].toString()));
				org.setName(object[1].toString());
				list.add(org);
			}

		}
		logRecord(LogRecord.QUERY, "教师点击学生信息后初始化班级下拉框");
		return list;

	}

	/**
	 * @param stu
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 * @title
	 * @author Limit
	 * @date 2014-3-12 上午9:33:32
	 * @version 1.0
	 * @Description 教师修改学生信息
	 */
	@Transactional
	public AjaxMsg updateOnlyOneStu(StudentInfo stu, HttpServletRequest request)
			throws UnsupportedEncodingException {
		StudentInfo studentInfo = (StudentInfo) dao.findOne(
				"from StudentInfo where id = ?", stu.getId());
		AjaxMsg msg = new AjaxMsg();
		if (null != stu.getName() && !"".equals(stu.getName())) {
			String name = URLDecoder.decode(stu.getName(), "UTF-8").trim();
			studentInfo.setName(name);
		}
		// if (null != stu.getDutyPosition()) {
		// if ("".equals(stu.getDutyPosition())) {
		// studentInfo.setDutyPosition("");
		// } else {
		// String dutyPosition = URLDecoder.decode(stu.getDutyPosition(),
		// "UTF-8").trim();
		// studentInfo.setDutyPosition(dutyPosition);
		// }
		// }
		if (null != stu.getParentName() && !"".equals(stu.getParentName())) {

			String parentName = URLDecoder.decode(stu.getParentName(), "UTF-8")
					.trim();
			studentInfo.setParentName(parentName);

		}
		if (null != stu.getNo() && !"".equals(stu.getNo())) {
			studentInfo.setNo(stu.getNo());
			studentInfo.setSelftel(stu.getNo());
			studentInfo.setSex(stu.getSex());
		}
		try {
			dao.update(studentInfo);
			msg.setMsg("修改成功!");
		} catch (Exception e) {
			msg.setMsg("修改失败!");
			e.printStackTrace();
		}
		logRecord(LogRecord.UPDATE, "教师修改学生信息");
		return msg;

	}

	/**
	 * @param request
	 * @return
	 * @title
	 * @author Limit
	 * @date 2014-3-11 下午1:30:20
	 * @version 1.0
	 * @Description
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public JqGridData<StudentInfo> getStuInfoToClass(
			HttpServletRequest request, JqPageModel mode) {
		String status = request.getParameter("status");
		HttpSession session = request.getSession();
		SessionModal sessionModal = (SessionModal) session
				.getAttribute("sessionModal");
		JqGridData<StudentInfo> jqdt = new JqGridData<StudentInfo>();
		if ("1".equals(status)) {
			String orderSql = "order by t1.name";
			if (null != mode.getSort() && !"".equals(mode.getSort())) {
				orderSql = "order by t1." + mode.getSort() + " "
						+ mode.getOrder();
			}
			String querySql = "from StudentInfo as t1 where t1.status = 0 and t1.org.id = (select t2.org.id from StudentInfo t2 where t2.id = ?) "
					+ orderSql;

			PageList<StudentInfo> page = dao.page(mode.getPage(),
					mode.getRows(), querySql, sessionModal.getId());
			jqdt.setPage(page.getPageNo());
			jqdt.setRecords(page.getTotalCount());
			jqdt.setRows(page.getList());
			jqdt.setTotal(page.getPageCount());
		}
		if ("2".equals(status)) {
			String orderSql = "order by t1.name";
			if (null != mode.getSort() && !"".equals(mode.getSort())) {
				orderSql = "order by t1." + mode.getSort() + " "
						+ mode.getOrder();
			}
			String querySql = "from StudentInfo as t1 where t1.status = 0 and t1.org.id = ? "
					+ orderSql;

			String classId = request.getParameter("classid");
			if (null != classId && !"".equals(classId)) {
				PageList<StudentInfo> page = dao.page(mode.getPage(),
						mode.getRows(), querySql, Long.parseLong(classId));
				jqdt.setPage(page.getPageNo());
				jqdt.setRecords(page.getTotalCount());
				jqdt.setRows(page.getList());
				jqdt.setTotal(page.getPageCount());
			} else {
				return null;
			}
		}
		logRecord(LogRecord.QUERY, "家长或者老师查询具体某一班级的学生信息");
		return jqdt;
	}

	/**
	 * @param request
	 * @title
	 * @author Limit
	 * @date 2014-3-11 上午11:27:35
	 * @version 1.0
	 * @Description 点击学生信息后初始化班级下拉框
	 */
	@Transactional
	public Map<String, Object> queryStuClass(HttpServletRequest request) {
		SessionModal sessionModal = (SessionModal) request.getSession()
				.getAttribute("sessionModal");
		StudentInfo studentInfo = (StudentInfo) dao.findOne(
				"from StudentInfo where id = ?", sessionModal.getId());
		Map<String, Object> infoMap = new HashMap<String, Object>();

		if (null != studentInfo) {
			String calssName = studentInfo.getOrg().getName();
			infoMap.put("flg", 1);
			infoMap.put("value", calssName);
		} else {
			infoMap.put("flg", 2);
		}
		logRecord(LogRecord.QUERY, "点击学生信息后初始化班级下拉框");
		return infoMap;
	}

	/**
	 * @param request
	 * @title
	 * @author Limit
	 * @date 2014-3-12 下午2:10:47
	 * @version 1.0
	 * @Description 点击相册查看按钮后通过此方法返回相册信息
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public ModelAndView queryImageBystu(HttpServletRequest request) {
		SessionModal sessionModal = (SessionModal) request.getSession()
				.getAttribute("sessionModal");
		ModelAndView view = new ModelAndView("app/studentinfo/imageread");
		String stuid = request.getParameter("stuId");
		List<Attach> find = dao.find(
				"from Attach where relId = ? and relType = ?",
				Long.parseLong(stuid), "studentinfo");
		view.addObject("modeList", find);
		view.addObject("stuId", stuid);
		view.addObject("userType", sessionModal.getUserType());
		logRecord(LogRecord.QUERY, "点击相册查看按钮后通过此方法返回相册信息");
		return view;
	}

	/**
	 * @title
	 * @author Limit
	 * @date 2014-3-12 下午5:28:50
	 * @version 1.0
	 * @Description 删除学生相册中的照片
	 */
	@Transactional
	public Map<String, Object> deleteImageInfo(Long imgId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if (null == imgId) {
			return null;
		} else {
			try {
				Attach ch = new Attach();
				ch.setId(imgId);
				dao.delete(ch);
				resMap.put("flg", "1");
			} catch (Exception e) {
				resMap.put("flg", "2");
				e.printStackTrace();
			}
		}
		logRecord(LogRecord.DELETE, "删除学生相册中的照片");
		return resMap;
	}

	/**
	 * 查询所有学生信息
	 * 
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public JqGridData<StudentInfo> findStudentByClassId(JqPageModel model,
			Long cId) {
		PageList<StudentInfo> stuInfs = null;
		if (!StringUtil.isEmpty(model.getSearchKey())) {
			String hql = "from " + StudentInfo.class.getName()
					+ " s where s.status = 0 and (s.name like '%"
					+ model.getSearchKey() + "%'  or s.no like'%"
					+ model.getSearchKey() + "%')";
			if (model.getSidx() != null) {
				hql += " order by " + model.getSidx() + " " + model.getSord();
			}
			stuInfs = page(model, hql);
		} else {
			if (cId == null) {
				stuInfs = page(model, "from StudentInfo where status = 0");
			} else {
				String hql = "from " + StudentInfo.class.getName()
						+ " s where s.status = 0 and s.org.id=" + cId;
				if (model.getSidx() != null) {
					hql += " order by " + model.getSidx() + " "
							+ model.getSord();
				}
				stuInfs = page(model, hql);
			}
		}
		JqGridData<StudentInfo> jq = new JqGridData<StudentInfo>();
		jq.setPage(stuInfs.getPageNo());
		jq.setRecords(stuInfs.getTotalCount());
		jq.setRows(stuInfs.getList());
		jq.setTotal(stuInfs.getPageCount());
		logRecord(LogRecord.QUERY, "查询所有学生信息");
		return jq;
	}

	/**
	 * 根据班级查询学生
	 * 
	 * @param cId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findStudentByClassId(Long cId) {
		List<Object[]> stuList = dao.find("select id,name from "
				+ StudentInfo.class.getName()
				+ " where status = 0 and org.id=? order by name asc", cId);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (Object[] studentInfo : stuList) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", studentInfo[0] + "");
			String py = PinyinHelper.convertToPinyinString(
					studentInfo[1].toString(), "", PinyinFormat.WITHOUT_TONE);
			map.put("ln", py.substring(0, 1));
			map.put("name", py.substring(0, 1).toUpperCase() + "-"
					+ studentInfo[1].toString());
			list.add(map);
		}
		return list;
	}

	/**
	 * 根据班级ID查询班级下的学生 APP接口
	 * 
	 * @param cId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> findStudentByClassIdAPP(Long cId) {
		List<Object[]> stuList = dao.find("select id,name from "
				+ StudentInfo.class.getName()
				+ " where status = 0 and org.id=? order by name asc", cId);

		for (Object[] objs : stuList) {
			String py = PinyinHelper.convertToPinyinString(objs[1].toString(),
					"", PinyinFormat.WITHOUT_TONE);
			objs[1] = py.substring(0, 1).toUpperCase() + "-" + objs[1];
		}
		return stuList;
	}

	public ModelAndView queryStuMessToUpdate(Long stuId) {
		StudentInfo studentInfo = (StudentInfo) dao.findOne(
				"from StudentInfo where id = ?", stuId);
		ModelAndView view = new ModelAndView("app/studentinfo/stuupdate");
		view.addObject("student", studentInfo);
		return view;
	}

	/**
	 * @param stuId
	 * @return
	 * @title
	 * @author Limit
	 * @date 2014-3-14 下午3:48:00
	 * @version 1.0
	 * @Description 点击相册查看按钮后通过此方法返回相册信息
	 */

	@Transactional
	public ModelAndView queryOneStuMeaageById(Long stuId) {
		SessionModal sessionModel = currentSessionModel();
		StudentInfo studentInfo = (StudentInfo) dao.findOne(
				"from StudentInfo where id = ?", stuId);
		ModelAndView view = new ModelAndView("app/studentinfo/studentiInfoPage");
		view.addObject("student", studentInfo);
		logRecord(LogRecord.QUERY, "点击相册查看按钮后通过此方法返回相册信息");
		return view;
	}

	@Transactional
	public ModelAndView queryOneStuMeaageById() {
		SessionModal sessionModel = currentSessionModel();
		StudentInfo studentInfo = (StudentInfo) dao.findOne(
				"from StudentInfo where id = ?", sessionModel.getId());
		ModelAndView view = new ModelAndView("app/studentinfo/studentiInfoPage");
		view.addObject("student", studentInfo);
		logRecord(LogRecord.QUERY, "点击相册查看按钮后通过此方法返回相册信息");
		return view;
	}

	/**
	 * 新增，修改 学生信息
	 * 
	 * @param studentInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public AjaxMsg saveOrUpdate(StudentInfo studentInfo) {
		AjaxMsg am = new AjaxMsg();
		try {
			if (studentInfo.getId() == null) {
				studentInfo.setPwd(Encrypt.md5("123456"));
				List<StudentInfo> dbStudentInfo = dao
						.find("from " + StudentInfo.class.getName()
								+ " s where s.no=? and s.status=0",
								studentInfo.getNo());
				for (StudentInfo studentInfo2 : dbStudentInfo) {
					if (studentInfo2.getName().equals(studentInfo.getName())) {
						am.setType(am.ERROR);
						am.setMsg("新增失败,该账号下已经有同名学生存在");
						return am;
					}
				}
				Attach attach = new Attach();
				if (studentInfo.getSex() == 0) {
					attach.setId(30L);
				} else if (studentInfo.getSex() == 1) {
					attach.setId(30L);
				} else {
					attach.setId(31L);
				}
				studentInfo.setHeadPic(attach);

				if (null != studentInfo && null != studentInfo.getOrg()
						&& null == studentInfo.getOrg().getId()) {
					// studentInfo.setOrg(null);
					am.setType(am.ERROR);
					am.setMsg("该学生所在班级不能为空");
					return am;
				}
				studentInfo.setSelftel(studentInfo.getNo());
				dao.insert(studentInfo);
				am.setMsg("新增成功");
			} else {
				StudentInfo dbStudent = dao.findOne(StudentInfo.class, "id",
						studentInfo.getId());
				BeanUtils.copyProperties(studentInfo, dbStudent, new String[] {
						"id", "headPic", "pwd" });
				dao.update(dbStudent);
				am.setMsg("修改成功");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			am.setType(am.ERROR);
			am.setMsg("处理失败");
		}
		return am;
	}

	/**
	 * 查找当前登录学生
	 * 
	 * @return
	 */
	public StudentInfo currentStudent() {
		return findById(currentUserId());
	}

	@Resource
	private AttachService attachService;

	@Transactional
	public AjaxMsg deleteById(Long sId, Integer deleteType) {
		AjaxMsg am = new AjaxMsg();
		try {
			StudentInfo s = dao.findOne(StudentInfo.class, "id", sId);
			if (s.getHeadPic() != null) {
				if (Integer
						.parseInt(String
								.valueOf(dao
										.find("select count(*) from StudentInfo where headPic.id = ?",
												s.getHeadPic().getId()).get(0))) > 1) {
				} else {
					attachService.delete(s.getHeadPic().getId());// 删除头像
				}
			}
			s.setStatus(1);
			if (null != deleteType) {
				s.setDeleteType(deleteType);
			}
			dao.update(s);// 删除学生
			am.setMsg("删除成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			am.setType(am.ERROR);
			am.setMsg("删除失败");
		}
		return am;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> findPushMsg(String receiveIds) {
		return dao.getHelperDao().find(
				"select channelId_f,userId_f,android_f,no_f  from StudentInfo_t where id_f in("
						+ receiveIds + ")");
	}

	/**
	 * @param jqPagemodel
	 * @param stuName
	 * @param reason
	 * @return
	 * @title
	 * @author Limit
	 * @date 2014-6-6 上午10:57:26
	 * @version 1.0
	 * @Description 查询删除的学生
	 */
	public JqGridData<List<?>> querySpecialStudent(JqPageModel jqPagemodel,
			String stuName, Integer reason) {
		JqGridData jqGridData = new JqGridData<List<?>>();
		List<Map<String, Object>> list = null;
		Map<String, Object> map = null;
		StringBuffer hql = null;
		hql = new StringBuffer(
				"select r.name_f,o.name_f oname,r.sex_f,r.selftel_f,r.address_f,r.deleteType_f from (select * from  StudentInfo_t stu where stu.status_f = 1 ");
		if (null != stuName && !StringUtil.isEmpty(stuName)) {
			hql.append("and stu.name_f like '%" + stuName + "%' ");
		}
		if (!StringUtil.isEmpty(reason) && 0 != reason) {
			hql.append(" and stu.deleteType_f = " + reason);
		}
		hql.append(" ) r left join org_t o on r.org_f = o.id_f and o.type_f = 3 ");
		if (null != stuName && !StringUtil.isEmpty(stuName)) {
			hql.append(" and r.name_f like '%");
			hql.append(stuName);
			hql.append("%'");
		}
		if (!StringUtil.isEmpty(reason) && 0 != reason) {
			hql.append(" and r.deleteType_f = ");
			hql.append(reason);
		}
		PageList page = dao.getHelperDao().page(jqPagemodel.getPage(),
				jqPagemodel.getRows(), hql.toString());
		if (null == page) {
			return jqGridData;
		}
		List initList = page.getList();
		if (initList.size() != 0) {
			list = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < initList.size(); i++) {
				Object[] object = (Object[]) initList.get(i);
				map = new HashMap<String, Object>();
				map.put("id", i);
				if (null != object[0]) {
					map.put("name", object[0].toString());
				}
				if (null != object[1]) {
					map.put("orgName", object[1].toString());
				}
				if (null != object[2]) {
					map.put("sex", object[2].toString());
				}
				if (null != object[3]) {
					map.put("tel", object[3].toString());
				}
				if (null != object[4]) {
					map.put("address", object[4].toString());
				}
				if (null != object[5]) {
					map.put("type", object[5].toString());
				}
				list.add(map);
			}
			jqGridData.setPage(page.getPageNo());
			jqGridData.setRows(list);
			jqGridData.setRecords(page.getTotalCount());
			jqGridData.setTotal(page.getPageCount());
			return jqGridData;
		}
		return jqGridData;
	}
}
