package app.cq.hmq.service.leave;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.cq.hmq.pojo.leave.LeaveInfo;
import app.cq.hmq.pojo.stuinfo.StudentInfo;
import app.cq.hmq.pojo.teacherinfo.TeacherInfo;
import app.cq.hmq.service.letter.PrivateLetterService;

import common.cq.hmq.pojo.sys.User;

import core.cq.hmq.dao.PageList;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.modal.SessionModal;
import core.cq.hmq.service.BaseService;
import core.cq.hmq.util.tools.DateUtil;
import core.cq.hmq.util.tools.StringUtil;
@Service
public class LeaveInfoService extends BaseService {
	
	@Autowired
	private PrivateLetterService privateService;
		
	public PageList<?> findLeaveInfoList(int pageNo,int pageRows,String searchKey,String type,Long uid,Long rkclassid){
		if(null != type && "1".equals(type)){
			 return findLeaveInfoListBystudent(pageNo, pageRows,searchKey,uid);
		}else if("2".equals(type)){
			return findLeaveInfoLisByTeacher(pageNo,pageRows,searchKey,uid,rkclassid);
		}
		return null;
	}
	
	public PageList<?> findLeaveInfoListBystudent(int pageNo,int pageRows,String searchKey,Long uid){
		StringBuffer hqlString = new StringBuffer();
			hqlString.append("select le.id_f, le.leavereason_f, le.leavetype_f, le.status_f, s.name_f from leaveinfo_t le,studentinfo_t s ");
			hqlString.append("where s.id_f =  "+uid);
			if(!StringUtil.isEmpty(searchKey) && !"null".equalsIgnoreCase(searchKey)){
				hqlString .append(" and le.approvestatus_f "+escapeChar(searchKey));
			}
			hqlString.append(" and le.proposerid_f = "+uid+" and le.proposertype_f = 1 ");
			hqlString.append(" order by le.status_f asc,le.id_f desc");
		PageList<Map<String, Object>> pageList = dao.getHelperDao().page(pageNo, pageRows,hqlString.toString());
		if(null != pageList && null != pageList.getList()){
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Object[] os = null;
			String str = null;
			for(Object o : pageList.getList()){
				Map<String, Object> map = new HashMap<String, Object>();
				os = (Object[]) o;
				map.put("id", os[0]);
				str =(String) os[1];
				if(null != str && str.length() > 20){
					str= str.substring(0, 19) +"...";
				}
				map.put("leaveReason", str);
				map.put("leaveType", os[2]);
				map.put("status", os[3]);
				map.put("name", os[4]);
				list.add(map);
			}
			pageList.setResult(list);
		}
		return pageList;
	}
	
	public PageList<?> findLeaveInfoLisByTeacher(int pageNo,int pageRows,String searchKey,Long uid,Long rkclassid){
		StringBuffer hqlString = new StringBuffer();
		PageList<Map<String, Object>> pageList =  null;
		if(null != rkclassid){
			if(0 == judgeClassBanzhuren(rkclassid, uid)){
				hqlString.append("select le.id_f, le.leavereason_f, le.leavetype_f, le.status_f, concat(t.name_f,'(教师)') from leaveinfo_t le,teacherinfo_t t ");
				hqlString.append("where t.id_f =  "+uid);
				if(!StringUtil.isEmpty(searchKey) && !"null".equalsIgnoreCase(searchKey)){
					hqlString .append(" and t.name_f "+escapeChar(searchKey));
				}
				hqlString.append(" and le.proposerid_f = "+uid+" and le.proposerClass_f = "+rkclassid+" and le.proposertype_f =  9");
				hqlString.append(" order by le.status_f asc,le.id_f desc");
				pageList = dao.getHelperDao().page(pageNo, pageRows,hqlString.toString());
			}else{
				hqlString.append("select le.id_f, le.leavereason_f, le.leavetype_f, le.status_f,");
				hqlString.append("case le.proposertype_f when 1 then (select s.name_f ");
				hqlString.append("from studentinfo_t s where s.id_f = le.proposerid_f ");
				if(!StringUtil.isEmpty(searchKey) && !"null".equalsIgnoreCase(searchKey)){
					hqlString .append(" and s.name_f "+escapeChar(searchKey) +") ");
				}else{
					hqlString .append(") ");
				}
				hqlString.append("when 9 then (select t.name_f from teacherinfo_t t where t.id_f = le.proposerid_f ");
				if(!StringUtil.isEmpty(searchKey) && !"null".equalsIgnoreCase(searchKey)){
					hqlString .append(" and t.name_f "+escapeChar(searchKey) +") end  ");
				}else{
					hqlString .append(")  end ");
				}
				hqlString.append("from leaveinfo_t le where le.receiver_f = ");
				hqlString.append(uid);
				hqlString.append(" and le.proposerClass_f = "+rkclassid+" order by le.status_f asc,le.id_f desc");
				pageList = dao.getHelperDao().pageByTotal(pageNo, pageRows,
						hqlString.toString(),"select count(1) from leaveinfo_t le where le.receiver_f = "+uid);
			}
		}
		
		if(null != pageList && null != pageList.getList()){
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Object[] os = null;
			String str = null;
			for(Object o : pageList.getList()){
				Map<String, Object> map = new HashMap<String, Object>();
				os = (Object[]) o;
				map.put("id", os[0]);
				str =(String) os[1];
				if(null != str && str.length() > 20){
					str= str.substring(0, 19) +"...";
				}
				map.put("leaveReason", str);
				map.put("leaveType", os[2]);
				map.put("status", os[3]);
				map.put("name", os[4]);
				list.add(map);
			}
			pageList.setResult(list);
		}
		return pageList;
	}
	
	
	@Transactional
	public Map<String, Object>  findLeaveInfoById(Long id,String type,Long uid,Long classid){
		 Map<String, Object> map =  new HashMap<String, Object>();
		if(null != id){
			LeaveInfo l = (LeaveInfo) dao.findOne(LeaveInfo.class, "id", id);
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("approveStatus", l.getApproveStatus());
			dataMap.put("id", l.getId());
			dataMap.put("startDate", l.getStartDate());
			dataMap.put("endDate", l.getEndDate());
			dataMap.put("leaveType", l.getLeaveType());
			dataMap.put("leaveReason", l.getLeaveReason());
			dataMap.put("writeDate", l.getWriteDate());
			dataMap.put("status", l.isStatus());
			dataMap.put("sendusertype", l.getProposerType());
			dataMap.put("leaveUserName", l.getLeaveUser().getName());
			dataMap.put("leaveUserTel", l.getLeaveUser().getSelftel());
			if(User.STUDENTTYPE.equals(String.valueOf(l.getProposerType()))){
				StudentInfo s = (StudentInfo) dao.findOne("from StudentInfo where id=?", l.getProposerId());
				dataMap.put("name", s.getName());
				dataMap.put("headPic", s.getHeadPic().getHtmlUrl());
				map.put("1", "none");
			}else if("9".equals(String.valueOf(l.getProposerType()))){
				TeacherInfo t = (TeacherInfo) dao.findOne("from TeacherInfo where id=?", l.getProposerId());
				dataMap.put("name", t.getName());
				dataMap.put("headPic", t.getHeadpic().getHtmlUrl());
			}
			if(User.TEACHERTYPE.equals(type)){
				if(0 != judgeClassBanzhuren(classid, uid)){
					map.put("3", "isClassLeader");
					if(!l.isStatus() ){
						l.setStatus(true);
						dao.update(l);
						map.put("1", "update");
					}else {
						map.put("1", "none");
					}
				}
			}
			map.put("2", dataMap);
			return map;
		}
		return null;
	}
	
	
	public String escapeChar(String str) {
		return "like '%"
				+ str.replace("%", "/%").replace("'", "''").replace("_", "/_")
				+ "%' escape '/' ";
	}

	/**
	 * 根据班级id 得到该班级的所有教师信息
	 * @param orgId
	 * @return
	 */
	public Map<Object, Object> findMyClassTs(Long orgId) {
		Map<Object, Object> map = null;
		if(null != orgId){
			List<?> list =  dao.getHelperDao().find("select t.id_f,t.name_f from " +
					"TeacherInfo_t t, SubjectMapping_t sm where t.id_f = sm.teacher_f and sm.org_f = ?", orgId);
			if(null != list && list.size() > 0){
				map = new HashMap<Object, Object>();
				Object[] objs = {};
				for(Object o : list){
					objs = (Object[]) o;
					map.put(objs[0], objs[1]);
				}
			}
		}
		return map;
	}
	
	/**
	 * 根据班级id和教师Id,判断是否是该班级主任
	 * @param oid uid
	 * @return 0 非班主任 1 班主任
	 */
	public int judgeClassBanzhuren(Long oid,Long uid) {
			List<?> list =  dao.getHelperDao().find("select count(1) from org_t o " +
					" where instr(o.mLeader_f,?)>0 and o.id_f = ?", ","+uid+",",oid);
			if(null != list && list.size() > 0){
				return Integer.parseInt(String.valueOf(list.get(0)));
			}
		return 0;
	}
	
	public List<Map<String , String >> findTeacherByOrg(Long orgId) {
		List<Map<String , String >> rlist = new ArrayList<Map<String,String>>();
		List<?> list = dao.getHelperDao()
			.find("select t.id_f, t.name_f from teacherInfo_t t,subjectMapping_t s where t.id_f=s.teacher_f  and s.org_f=?",
				orgId);
		if(null != list && list.size() > 0){
			Object[]  os = {};
			for(Object o : list){
				Map<String,String > map = new HashMap<String, String>();
				os = (Object[]) o;
				map.put("id", String.valueOf(os[0]));
				map.put("name", String.valueOf(os[1]));
				rlist.add(map);
			}
		}
		return rlist;
	}
	
	/**
	 * 根据班级id 得到该班级的所有教师信息
	 * @param orgId
	 * @return
	 */
	public Map<Object, Object> findMyClassBanzhuren(Long orgId) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		if(null != orgId){
			List<?> list =  dao.getHelperDao().find("select t.id_f,t.name_f from org_t o," +
					"teacherinfo_t t where instr(o.mleader_f, concat(',',t.id_f,','))>0 and o.id_f = ?", orgId);
			if(null != list && list.size() > 0){
				Object[] objs = {};
				for(Object o : list){
					objs = (Object[]) o;
					map.put(objs[0], objs[1]);
				}
			}
		}
		return map;
	}
	

	@Transactional
	public AjaxMsg addLeaveInfo(LeaveInfo leaveInfo,SessionModal sm,Long classid) {
		AjaxMsg am = new AjaxMsg();
		if(null != leaveInfo){
			try {
				if(null == leaveInfo.getLeaveUser()){
					am.setType(AjaxMsg.ERROR);
					am.setMsg("请假人为空！");
					return am;
				}
				if(null == leaveInfo.getReceiver()){
					am.setType(AjaxMsg.ERROR);
					am.setMsg("接收人为空！");
					return am;
				}
				leaveInfo.setProposerId(sm.getId());
				if(User.STUDENTTYPE.equals(sm.getUserType())){
					leaveInfo.setProposerType(1);
					leaveInfo.setProposerClass(sm.getOrgId());
				}else if(User.TEACHERTYPE.equals(sm.getUserType())){
					leaveInfo.setProposerType(9);
					leaveInfo.setApproveStatus("已确认");
					leaveInfo.setProposerClass(classid);
				}
				leaveInfo.setWriteDate(DateUtil.format(new Date(), DateUtil.DATE_PATTERN));
				dao.insert(leaveInfo);
				privateService.getPushPerson(leaveInfo.getReceiver().getId(), "2", "您收到请假信息,请查看!");
				
			} catch (Exception e) {
				am.setType(AjaxMsg.ERROR);
				am.setMsg("申请失败！");
				return am;
			}
		}
		am.setMsg("申请成功！");
		return am;
	}
	
	@Transactional
	public AjaxMsg deleteLeaveInfo(Long lid) {
		AjaxMsg am = new AjaxMsg();
		if(null != lid){
			try {
				dao.delete(LeaveInfo.class,lid);
			} catch (Exception e) {
				am.setType(AjaxMsg.ERROR);
				am.setMsg("删除失败！");
				return am;
			}
		}
		am.setMsg("删除成功！");
		return am;
	}

	@Transactional
	public AjaxMsg updateLeaveInfo(Long id, String status) {
		AjaxMsg am = new AjaxMsg();
		if(null != id){
			try {
				LeaveInfo l = (LeaveInfo) dao.findOne(LeaveInfo.class, "id", id);
				l.setApproveStatus(status);
				dao.update(l);
				privateService.getPushPerson(l.getProposerId(),l.getProposerType() == 1 ? "1" : "2", "您的请假已处理:"+status);
			} catch (Exception e) {
				am.setType(AjaxMsg.ERROR);
				am.setMsg("操作失败！");
				return am;
			}
		}
		am.setMsg("操作成功！");
		return am;
	}
}
