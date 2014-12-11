package app.cq.hmq.service.notice;

import app.cq.hmq.pojo.notice.Notice;
import app.cq.hmq.pojo.notice.NoticeReceiveStu;
import app.cq.hmq.pojo.notice.NoticeReceiveTea;
import app.cq.hmq.pojo.teacherinfo.TeacherInfo;

import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.jxt.PushRunn;
import com.baidu.yun.jxt.model.PushMsgModel;

import common.cq.hmq.pojo.sys.User;
import common.cq.hmq.util.SendSMS;
import core.cq.hmq.dao.PageList;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.modal.PageModel;
import core.cq.hmq.modal.SessionModal;
import core.cq.hmq.service.BaseService;
import core.cq.hmq.util.tools.DateUtil;
import core.cq.hmq.util.tools.StringUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class NoticeService extends BaseService {

	/**
	 * 把通知信息插入学生表 且调用push
	 * 
	 * @param receive
	 * @param receive
	 */
	public void insterReceiveStu(String receive, Notice notice) {
		String[] receives = receive.split(",");
		for (String id : receives) {
			NoticeReceiveStu nrs = new NoticeReceiveStu();
			nrs.setNotice(notice);
			nrs.setStudentInfo(Long.parseLong(id.trim()));
			dao.insert(nrs);
		}

		PushMsgModel pushModel = new PushMsgModel();
		// 通知 大类 0：作业通知 1：班级通知 2：教务处通知 3：学生处通知 4校内通告
		if (notice.getGenre() == 0)
			pushModel.setTitle("作业通知");
		else if (notice.getGenre() == 1)
			pushModel.setTitle("班级通知");
		else if (notice.getGenre() == 2)
			pushModel.setTitle("教务处通知");
		else if (notice.getGenre() == 3)
			pushModel.setTitle("学生处通知");
		else if (notice.getGenre() == 4)
			pushModel.setTitle("校内通告");
		else if (notice.getGenre() == 5)
			pushModel.setTitle("招生办通知");
		else if (notice.getGenre() == 6)
			pushModel.setTitle("行政办通知");
		else
			pushModel.setTitle("研修是通知");
		pushModel.setDescription("[" + notice.getTitle() + "]"
				+ notice.getContent());
		if (receive.length() - receive.lastIndexOf(",") == 1) {// 最后一个是否是逗号
			receive = receive.substring(0, receive.lastIndexOf(","));
		}
		TeacherInfo teacherInfo = dao.findOne(TeacherInfo.class, "id", notice
				.getTeacherInfo().getId());
		PushRunn pushRunn = new PushRunn(receive.trim(), false, notice,
				teacherInfo.getId(), teacherInfo.getName());
		Thread thread = new Thread(pushRunn);
		thread.start();
	}

	/**
	 * 把通知信息插入学生表
	 * 
	 * @param receive
	 * @throws ChannelServerException
	 * @throws ChannelClientException
	 * @throws NumberFormatException
	 */
	public void insterReceiveTea(String receive, Notice notice) {
		String[] receives = receive.split(",");
		for (String id : receives) {
			NoticeReceiveTea nrt = new NoticeReceiveTea();
			nrt.setNotice(notice);
			nrt.setTeacherInfo(Long.parseLong(id.trim()));
			dao.insert(nrt);
		}
		PushMsgModel pushModel = new PushMsgModel();
		// 通知 大类 0：作业通知 1：班级通知 2：教务处通知 3：学生处通知 4校内通告
		if (notice.getGenre() == 0)
			pushModel.setTitle("作业通知");
		else if (notice.getGenre() == 1)
			pushModel.setTitle("班级通知");
		else if (notice.getGenre() == 2)
			pushModel.setTitle("教务处通知");
		else if (notice.getGenre() == 3)
			pushModel.setTitle("学生处通知");
		else if (notice.getGenre() == 4)
			pushModel.setTitle("校内通告");
		else if (notice.getGenre() == 5)
			pushModel.setTitle("招生办通知");
		else if (notice.getGenre() == 6)
			pushModel.setTitle("行政办通知");
		else
			pushModel.setTitle("研修是通知");
		pushModel.setDescription("[" + notice.getTitle() + "]"
				+ notice.getContent());
		if (receive.length() - receive.lastIndexOf(",") == 1) {// 最后一个是否是逗号
			receive = receive.substring(0, receive.lastIndexOf(","));
		}
		TeacherInfo teacherInfo = dao.findOne(TeacherInfo.class, "id", notice
				.getTeacherInfo().getId());
		PushRunn pushRunn = new PushRunn(receive.trim(), true, notice,
				teacherInfo.getId(), teacherInfo.getName());
		Thread thread = new Thread(pushRunn);
		thread.start();

	}

	/**
	 * 查找老师发送通知列表
	 * 
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageList<Object[]> appFindSendNotices(PageModel model, Long uId) {
		PageList<Object[]> list = dao
				.getHelperDao()
				.pageByTotal(
						model.getPage(),
						model.getRows(),
						"with t as "
								+ " (select n.id_f, n.content_f, n.shortmsg_f, n.date_f, n.title_f, n.genre_f "
								+ "    from notice_t n "
								+ "   where n.teacherinfo_f = "
								+ uId
								+ "), "
								+ "r as "
								+ " (select t.id_f, "
								+ "         t.content_f, "
								+ "         t.title_f, "
								+ "         t.date_f, "
								+ "         t.genre_f, "
								+ "         t.shortmsg_f, "
								+ "         smc.smsid_f "
								+ "    from t t "
								+ "    left join smsstatecontent_t smc "
								+ "      on smc.noticeid_f = t.id_f "
								+ "     and smc.noticeid_f is not null) "
								+ "select r.id_f, "
								+ "       max(r.title_f), "
								+ "       max(r.content_f), "
								+ "       max(r.genre_f), "
								+ "       max(r.date_f), "
								+ "       max(r.shortmsg_f), "
								+ "       count(ss.id_f) "
								+ "  from r r "
								+ "  left join smsstate_t ss "
								+ "    on ss.smsid_f = r.smsid_f "
								+ " group by r.id_f order by max(r.date_f) desc",
						"select count(*) from notice_t n where n.teacherinfo_f ="
								+ uId);
		return list;
	}

	/**
	 * 查找已发送的通知 通知类型
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageList<Notice> findSendList(PageModel model, int genre) {
		// TODO Auto-generated method stub
		PageList<Notice> noticeList = page(model,
				"from " + Notice.class.getName() + " where teacherInfo.id="
						+ currentUserId() + " and draft=0 and genre=" + genre
						+ " order by date desc");
		return noticeList;
	}

	/**
	 * 查找接收的通知
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageList<NoticeReceiveTea> findteaReceiveList(PageModel model) {
		// TODO Auto-generated method stub
		return page(model, "from " + NoticeReceiveTea.class.getName()
				+ " where teacherInfo=" + currentUserId()
				+ " order by notice.date desc");
	}

	@SuppressWarnings("unchecked")
	public PageList<NoticeReceiveStu> findstuReceiveList(PageModel model) {
		return page(model, "from " + NoticeReceiveStu.class.getName()
				+ " where studentInfo=" + currentUserId()
				+ " order by notice.date desc");
	}

	/**
	 * 查找自己的草稿箱 根据通知类型
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageList<Notice> findDraftList(PageModel model, int genre) {
		// TODO Auto-generated method stub
		PageList<Notice> noticeList = page(model,
				"from " + Notice.class.getName() + " where teacherInfo.id="
						+ currentUserId() + " and draft=1 and genre=" + genre
						+ " order by date desc");
		return noticeList;
	}

	@Transactional
	public Notice findNoticeByIdAndChangeLook(Long nId) {
		SessionModal sm = currentSessionModel();
		if (sm.getUserType().equals("2")) {
			NoticeReceiveTea noticeRe = (NoticeReceiveTea) dao.findOne("from "
					+ NoticeReceiveTea.class.getName()
					+ " where notice.id=? and teacherInfo=?", nId,
					currentUserId());
			noticeRe.setLook(true);
			dao.update(noticeRe);
			return noticeRe.getNotice();
		} else {
			NoticeReceiveStu noticeRe = (NoticeReceiveStu) dao.findOne("from "
					+ NoticeReceiveStu.class.getName()
					+ " where notice.id=? and studentInfo=?", nId,
					currentUserId());
			noticeRe.setLook(true);
			dao.update(noticeRe);
			return noticeRe.getNotice();
		}

	}

	public Notice findNoticeById(Long nId) {
		// TODO Auto-generated method stub
		return dao.findOne(Notice.class, "id", nId);
	}

	/**
	 * 删除草稿
	 * 
	 * @param id
	 * @return
	 */
	@Transactional
	public AjaxMsg del(Long id) {
		dao.getHelperDao().excute(
				"delete from NoticeReceiveStu_t where notice_f=?", id);
		dao.getHelperDao().excute(
				"delete from NoticeReceiveTea_t where notice_f=?", id);
		dao.delete(Notice.class, id);
		return new AjaxMsg("删除成功");
	}

	/**
	 * 推送通知
	 * 
	 * @param notice
	 * @param classids
	 * @param roleIds
	 * @param stuids
	 * @param teaids
	 * @param stunames
	 * @param teanames
	 * @param stutels
	 * @param teatels
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@Transactional
	public AjaxMsg push(Notice notice, String classids, String roleIds,
			String stuids, String teaids, String stunames, String teanames,
			String stutels, String teatels) {
		AjaxMsg am = new AjaxMsg();

		TeacherInfo teacherInfo = dao.findOne(TeacherInfo.class, "id", notice
				.getTeacherInfo().getId());
		// List<Object> list = dao.getHelperDao().find(
		// "select id_f from smsstate_t s where to_char(s.date_f)='"
		// + DateUtil.format(new Date()) + "' and teacherInfo_f="
		// + teacherInfo.getId());
		// if (list.size() > 0) {
		// am.setMsg("您今天发送短信的次数已经使用完，请明天再发");
		// am.setType(AjaxMsg.ERROR);
		// return am;
		// }
		if (Calendar.HOUR >= 22 || Calendar.HOUR <= 7) {
			am.setType(AjaxMsg.ERROR);
			am.setMsg("晚上10到早点7点间不能发送通知");
			return am;
		}
		if (StringUtil.isEmpty(notice.getTitle())) {
			am.setType(AjaxMsg.ERROR);
			am.setMsg("通知标题不能为空");
			return am;
		}
		if (notice.getTeacherInfo() == null) {
			am.setType(AjaxMsg.ERROR);
			am.setMsg("通知发起人不能为空");
			return am;
		}
		if (StringUtil.isEmpty(notice.getContent())) {
			am.setType(AjaxMsg.ERROR);
			am.setMsg("通知内容不能为空");
			return am;
		} else {
			if (notice.getContent().length() > 116) {
				am.setType(AjaxMsg.ERROR);
				am.setMsg("通知内容不能超过116个字");
				return am;
			}
		}
		int total = 0;
		notice.setDate(new Date());
		dao.insert(notice);// 保存通知
		if (!StringUtil.isEmpty(classids) && StringUtil.isEmpty(stuids)) {// 接收班级不为空
			// 且
			// 没有单独选择学生
			List<Object> listCls = new ArrayList<Object>();
			if (classids.length() - classids.lastIndexOf(",") == 1) {// 最后一个是否是逗号
				listCls = dao.getHelperDao().find(
						"select id_f from studentInfo_t where status_f=0 and org_f in("
								+ classids + "-1)");
			} else {
				listCls = dao.getHelperDao().find(
						"select id_f from studentInfo_t where status_f=0 and org_f in("
								+ classids + ")");
			}
			if (listCls.size() > 0) {
				insterReceiveStu(
						listCls.toString().replace("[", "").replace("]", ""),
						notice);
				total = total + listCls.size();
			}
		}
		if (!StringUtil.isEmpty(roleIds)) {// 老师角色不为空
			List<Object> listOrg = new ArrayList<Object>();
			if (!StringUtil.isEmpty(teaids)) {// 如果选择了人,需要去掉人所属的角色
				// 查询角色
				List<Object> o = dao
						.getHelperDao()
						.find("select distinct(r.id_f) from role_t r, userrole_t u where  u.teacher_f=1 and  u.userid_f in("
								+ teaids + ") and r.id_f=u.roleid_f");
				roleIds += ",";
				for (Object delroleId : o) {
					roleIds = roleIds.replace(delroleId.toString() + ",", "");
				}
				if (!StringUtil.isEmpty(roleIds)) {
					roleIds = roleIds.substring(0, roleIds.length() - 1);
					listOrg = dao
							.getHelperDao()
							.find("select id_f from teacherInfo_t t where t.id_f in(select u.userid_f from userrole_t u where  u.roleid_f in ("
									+ roleIds + ") and u.teacher_f=1)");
					String teacherIds = listOrg.toString().replace("[", "")
							.replace("]", "");// 所有已选择的角色
					if (listOrg.size() > 0)
						insterReceiveTea(teacherIds, notice);
				}
			} else {
				listOrg = dao
						.getHelperDao()
						.find("select id_f from teacherInfo_t t where t.id_f in(select u.userid_f from userrole_t u where  u.roleid_f in ("
								+ roleIds + ") and u.teacher_f=1)");
				String teacherIds = listOrg.toString().replace("[", "")
						.replace("]", "");//
				if (listOrg.size() > 0)
					insterReceiveTea(teacherIds, notice);
			}
			total = total + listOrg.size();
		}
		if (!StringUtil.isEmpty(stuids)) {// 接收学生id组合不为空
			insterReceiveStu(stuids, notice);
			total = total + stuids.split(",").length;
		}
		if (!StringUtil.isEmpty(teaids)) {// 接收老师id组合不为空
			insterReceiveTea(teaids, notice);
			total = total + teaids.split(",").length;
		}
		if (!StringUtil.isEmpty(stutels)) {// 学生接收电话号码组合不为空
			if (stutels.length() - stutels.lastIndexOf(",") == 1) // 最后一个是否是单引号
				stutels = stutels.substring(0, stutels.length() - 1);
			SendSMS.sendMsg(notice.getContent(), stutels, teacherInfo.getId(),
					teacherInfo.getName(), notice.getId());
			total = total + stutels.split(",").length;

		}
		if (!StringUtil.isEmpty(teatels)) {// 老师接收电话号码组合不为空
			if (teatels.length() - teatels.lastIndexOf(",") == 1) // 最后一个是否是单引号
				teatels = teatels.substring(0, teatels.length() - 1);
			SendSMS.sendMsg(notice.getContent(), teatels, teacherInfo.getId(),
					teacherInfo.getName(), notice.getId());
			total = total + teatels.split(",").length;
		}
		if (notice.getSelfsms()) {
			SendSMS.sendMsg(notice.getContent(), teacherInfo.getTelePhone(),
					teacherInfo.getId(), teacherInfo.getName(), notice.getId());
			am.setMsg("通知发送成功,本次共给：" + (total + 1) + "人推送通知,包括自己");
		} else {
			am.setMsg("通知发送成功,本次共给：" + total + "人推送通知");
		}
		return am;
	}

	/**
	 * 查找接收通知
	 * 
	 * @param uid
	 * @param usertype
	 * @param model
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", })
	public PageList<List<Map<String, String>>> findreceivepush(Long uid,
			String usertype, PageModel model) {
		// TODO Auto-generated method stub
		if (usertype == null || usertype.equals("")) {
			return null;
		}
		if (usertype.equals("2")) {// 老师
			PageList pagelist = page(model,
					"from " + NoticeReceiveTea.class.getName()
							+ " where teacherInfo=" + uid
							+ " order by notice.date  desc");
			List<NoticeReceiveTea> list = pagelist.getList();
			List<Map<String, String>> listmap = new ArrayList<Map<String, String>>();
			for (NoticeReceiveTea nrt : list) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("headpic", nrt.getNotice().getTeacherInfo()
						.getHeadpic().getHtmlUrl());
				map.put("title", nrt.getNotice().getTitle());
				map.put("content", nrt.getNotice().getContent());
				map.put("date", DateUtil.format(nrt.getNotice().getDate(),
						DateUtil.DATETIME_PATTERN));
				map.put("name", nrt.getNotice().getTeacherInfo().getName());
				map.put("noticetype", nrt.getNotice().getGenre() + "");
				listmap.add(map);
			}
			pagelist.setResult(listmap);
			return pagelist;

		} else if (usertype.equals("1")) {// 学生
			PageList pagelist = page(model,
					"from " + NoticeReceiveStu.class.getName()
							+ " where studentInfo=" + uid
							+ " order by notice.date  desc");
			List<NoticeReceiveStu> list = pagelist.getList();
			List<Map<String, String>> listmap = new ArrayList<Map<String, String>>();
			for (NoticeReceiveStu nrt : list) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("headpic", nrt.getNotice().getTeacherInfo()
						.getHeadpic().getHtmlUrl());
				map.put("title", nrt.getNotice().getTitle());
				map.put("content", nrt.getNotice().getContent());
				map.put("date", DateUtil.format(nrt.getNotice().getDate(),
						DateUtil.COMPACT_TRIM_SECOND_PATTERN));
				map.put("date", DateUtil.format(nrt.getNotice().getDate(),
						DateUtil.DATETIME_PATTERN));
				map.put("name", nrt.getNotice().getTeacherInfo().getName());
				map.put("noticetype", nrt.getNotice().getGenre() + "");
				listmap.add(map);
			}
			pagelist.setResult(listmap);
			return pagelist;
		}
		return null;
	}

	/**
	 * 在登录的时候查询接收的通知有多少条
	 * 
	 * @param uId
	 * @param uType
	 * @return
	 */
	public int obtainNoticeCount(Long uId, String uType) {
		List list = null;
		if (User.STUDENTTYPE.equals(uType)) {
			list = dao
					.getHelperDao()
					.find("select count(1) from noticereceivestu_t ns where ns.look_f = 0 and ns.studentinfo_f = ?",
							uId);
		} else if (User.TEACHERTYPE.equals(uType)) {
			list = dao
					.getHelperDao()
					.find("select count(1) from noticereceivetea_t nt where nt.look_f = 0 and nt.teacherinfo_f = ?",
							uId);
		}

		if (null != list && list.size() > 0) {
			return Integer.parseInt(String.valueOf(list.get(0)));
		}
		return 0;
	}

	/**
	 * 查询在通知表中各类通知的个数
	 * 
	 * @param uId
	 * @param uType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findNearNotice(Long uId, String uType) {
		List<Map<String, Object>> rlist = new ArrayList<Map<String, Object>>();
		List<Object[]> list = null;
		if (User.STUDENTTYPE.equals(uType)) {
			list = dao
					.getHelperDao()
					.find("select case n.genre_f when 0 then '作业通知' when 1 then '班级通知' when 2 then '教务处通知'"
							+ "  when 3 then '学生处通知' when 4 then '校内通知' else '其它' end,"
							+ " count(n.id_f) from noticereceivestu_t ns,notice_t n where n.id_f=ns.notice_f"
							+ " and  ns.look_f = 0 and ns.studentinfo_f = ? group by n.genre_f",
							uId);
		} else if (User.TEACHERTYPE.equals(uType)) {
			list = dao
					.getHelperDao()
					.find("select case n.genre_f when 0 then '作业通知' when 1 then '班级通知' when 2 then '教务处通知'"
							+ " when 3 then '学生处通知' when 4 then '校内通知' else '其它' end,"
							+ " count(n.id_f) from noticereceivetea_t nt,notice_t n where n.id_f=nt.notice_f"
							+ " and nt.look_f = 0 and nt.teacherinfo_f = ? group by n.genre_f",
							uId);
		}
		if (null != list && list.size() > 0) {
			for (Object[] objects : list) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", objects[0]);
				map.put("count", objects[1]);
				rlist.add(map);
			}
		}
		return rlist;
	}

	/**
	 * 更改通知状态
	 * 
	 * @param noticeid
	 * @param userid
	 * @param teacher
	 */
	@Transactional
	public void changeLook(Long noticeid, Boolean teacher, Long userid) {
		System.out.println(noticeid + "--" + teacher + "--" + userid);
		// TODO Auto-generated method stub
		if (teacher) {
			dao.getHelperDao()
					.excute("update NoticeReceiveStu_t n set n.look_f=1 where n.studentinfo_f=? and n.notice_f=?",
							userid, noticeid);
		} else {
			dao.getHelperDao()
					.excute("update NoticeReceiveTea_t n set n.look_f=1 where n.teacherInfo_f=? and n.notice_f=?",
							userid, noticeid);
		}

	}

	public int findSendTotal(Long nId) {
		List<Object> list = dao
				.getHelperDao()
				.find("select count(*) from smsstate_t ss where ss.smsid_f in (select s.smsid_f from smsstatecontent_t s where s.noticeId_f="
						+ nId + ")");
		return Integer.parseInt(list.get(0).toString());
	}

}
