/**
 * Limit
 *
 */
package app.cq.hmq.service.appservice;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.cq.hmq.pojo.leave.CheckIn;
import app.cq.hmq.pojo.teachertopic.TeacherTopic;
import app.cq.hmq.pojo.teachertopic.teachertopicre;

import common.cq.hmq.pojo.sys.Attach;
import common.cq.hmq.pojo.sys.User;

import core.cq.hmq.dao.PageList;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.service.BaseService;
import core.cq.hmq.util.tools.DateUtil;
import core.cq.hmq.util.tools.StringUtil;

/**
 * @author Administrator
 *
 */
@Service(value = "appCheckService")
public class AppCheckManageService extends BaseService {

	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-11 上午11:43:37
	 *@version 1.0
	 *@Description 根据班级ID 教师ID查询科目名称
	 *
	 *@param lk
	 *@param classId
	 *@param teaId
	 *@return
	 *
	 *
	 */
	@Transactional
	public String queryTeacherName(String lk, Long classId, Long teaId) {
		
		String bachContent = "";
		if (!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)) {
			if(null == classId || "".equals(classId) || null == teaId || "".equals(teaId)){
				return bachContent;
			}
			List list = dao.getHelperDao().find("select " +
					"(select subt.name_f from subjectinfo_t subt where subt.id_f = subm.subjectinfo_f) " +
					"as sddd " +
					"from subjectmapping_t subm " +
					"where subm.org_f = "+ classId +" and subm.teacher_f = "+ teaId);
			if(list.size() == 0){
				return bachContent;
			}else{
				Object object = list.get(0);
				return object.toString();
			}
		}
		return null;
	}

	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-11 下午3:55:21
	 *@version 1.0
	 *@Description 存储考勤信息
	 *
	 *@param lk
	 *@param ck
	 *@return
	 *
	 *
	 */
	@Transactional
	public AjaxMsg saveCheckContent(String lk, CheckIn ck) {
		AjaxMsg msg = new AjaxMsg();
		msg.setType(AjaxMsg.ERROR);
		if (!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)) {
			//判断学生ID
	//		if(null == ck.getUnArriveIds() || "".equals(ck.getUnArriveIds())){
	//			return msg;
	//		}
			//判断主题
			if(null == ck.getTitle() || "".equals(ck.getTitle())){
				return msg;
			}
			//判断班级ID
			if(null == ck.getClassId() || "".equals(ck.getClassId())){
				return msg;
			}
			//判断学生总数
			if("".equals(ck.getHadCount())){
				return msg;
			}
			//判断未到学生总数
			if("".equals(ck.getUnCount())){
				return msg;
			}
			//判断考勤类型
			if("".equals(ck.getCheckType())){
				return msg;
			}
			//判断考勤教师
			if(null == ck.getTea()){
				return msg;
			}
			
			ck.setCheckDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			double baiy = (ck.getHadCount()) * 1.0;
	        double baiz = (ck.getHadCount() + ck.getUnCount()) * 1.0;
	        NumberFormat nf = NumberFormat.getPercentInstance();
	        nf.setMinimumFractionDigits(0);
			ck.setAttendance(Integer.parseInt((nf.format(baiy / baiz)).replace("%","")));
			dao.insert(ck);
			msg.setType(AjaxMsg.SUCCESS);
			return msg;
		}
		return msg;
		
	}


	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-14 上午9:40:03
	 *@version 1.0
	 *@Description 家长查询学生考勤
	 *
	 *@param lk
	 *@param classId
	 *@param StuId
	 *@return
	 *
	 *
	 */
	public List<?> queryStudentCHeck(String lk, Long classId, String stuId) {
		List<?> resList = new ArrayList<Object>();
		if (!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)) {
			if(null == classId || "".equals(classId) || null == stuId || "".equals(stuId)){
				return resList;
			}
			String querySql = "select che.title_f " +
					"from checkin_t che " +
					"where che.classid_f = " + classId +
					"and che.unarriveids_f like '%"+ stuId +"%'";
			resList = dao.getHelperDao().find(querySql);
			return resList;
		}
		return null;
	}


	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-14 下午12:03:17
	 *@version 1.0
	 *@Description 查询班级中的心语信息
	 *
	 *@param lk
	 *@param classId
	 *@return
	 *
	 *
	 */
	public List<Map<String, Object>> queryTeacherTopic(String lk, Long classId,int page,int rows) {
		List<TeacherTopic> resList = new ArrayList<TeacherTopic>();
		List<Map<String, Object>> rList = new ArrayList<Map<String,Object>>(); 
		if (!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)) {
			if(null == classId){
				return rList;
			}
			PageList<TeacherTopic> pageList = dao.page(page, rows, "from TeacherTopic top where top.classId = "+ classId +" order by id desc");
			if(null != pageList && null != pageList.getList()){
				Map<String, Object> map = null;
				for (TeacherTopic teacherTopic :  pageList.getList()) {
					map  = new HashMap<String, Object>();
					map.put("id", teacherTopic.getId());
					map.put("tname", teacherTopic.getTea().getName());
					map.put("thead", "download/"+teacherTopic.getTea().getHeadpic().getId()+"."+teacherTopic.getTea().getHeadpic().getSuffix());
					map.put("sendtime", teacherTopic.getWriteDate());
					map.put("title", teacherTopic.getTitle());
					map.put("content", teacherTopic.getContent());
					/** 自己拼接下载地址 download/ */
					map.put("vid", teacherTopic.getVoiceId());
					map.put("pids", teacherTopic.getPictrueIds());
					map.put("relist", dao.getHelperDao().findLimit(3, "select recontent_f,redate_f,case re.reusertype_f when '1' then (select name_f " +
							" from studentinfo_t where id_f = re.reuserid_f) when '9' then (select name_f from teacherinfo_t where id_f = re.reuserid_f) end from teachertopicre_t" +
							" re where re.topicid_f = ? order by re.id_f desc", teacherTopic.getId()));
					rList.add(map);
				}
			}
			return rList;
		}
		return null;
	}


	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-14 下午2:37:59
	 *@version 1.0
	 *@Description 查询具体一个心语的回复信息
	 *
	 *@param lk
	 *@param topId
	 *@param start
	 *@param end
	 *@return
	 *
	 *
	 */
	public List<Map<String, Object>> queryTeacherTopicRecounts(String lk, Long topId,
			Integer start, Integer end) {
		List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
		if (!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)) {
			/*if(start == 0){
				TeacherTopic teacherTopic = (TeacherTopic) dao.findOne("from TeacherTopic where id = ?", topId);
				if(null != teacherTopic){
					rmap.put("2", teacherTopic.getReCount());
					Map<String, Object> map = new HashMap<String, Object>();
					if(null != teacherTopic.getTea().getHeadpic()){
						map.put("headpic", "download/"+teacherTopic.getTea().getHeadpic().getId()
								+"."+teacherTopic.getTea().getHeadpic().getSuffix());
					}else{
						map.put("headpic", null);
					}
					map.put("sendname", teacherTopic.getTea().getName());
					map.put("sendtime", teacherTopic.getWriteDate());
					map.put("content", teacherTopic.getContent() == null ? "" : teacherTopic.getContent());
					map.put("voice", teacherTopic.getVoiceId());
					if(null != teacherTopic.getPictrueIds()){
						map.put("pictrueids", teacherTopic.getPictrueIds().split(","));
					}else{
						map.put("pictrueids", null);
					}
					listMap.add(map);
				}else{
					return rmap;
				}
			}*/
			
			StringBuffer sb = new StringBuffer();
			sb.append("select re.recontent_f,re.redate_f,case re.reusertype_f when '1'");
			sb.append(" then (select concat(name_f,',',headpic_f) from studentinfo_t where id_f = re.reuserid_f) ");
			sb.append("when '9' then (select concat(name_f,',',headpic_f) from teacherinfo_t where id_f = re.reuserid_f) ");
			sb.append(" end as username from teachertopicre_t re where re.topicid_f = ? order by re.id_f limit ?,? ");
			List list =	 dao.getHelperDao().find(sb.toString(),topId,start,end-start);
			if(null != list && list.size() > 0){
				Object[] objs = null;
				String[] str = null;
				for(Object o : list){
					objs = (Object[]) o;
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("content", objs[0] == null ? "" : objs[0]);
					map.put("sendtime", objs[1]);
					str = String.valueOf(objs[2]).split(",");
					map.put("headpic", "download/"+str[1]+"."+"jpg");
					map.put("sendname", str[0]);
					map.put("voice", null);
					map.put("pictrueids", null);
					listMap.add(map);
				}
			}
		}
		return listMap;
	}


	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-14 下午3:30:20
	 *@version 1.0
	 *@Description 新增教师心语
	 *
	 *@param lk
	 *@param top
	 *@return
	 *
	 *
	 */
	@Transactional
	public AjaxMsg saveTeacherTopic(String lk, TeacherTopic top) {
		AjaxMsg msg = new AjaxMsg();
		msg.setType(AjaxMsg.ERROR);
		if (!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)) {
			if(null == top){
				return msg;
			}
			if(StringUtil.isEmpty(top.getTitle())
					|| StringUtil.isEmpty(top.getContent())){
				return msg;
			}
			
			if(null == top.getTea()){
				return msg;
			}
			
			top.setReCount(0);
			top.setWriteDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			dao.insert(top);
			if(null != top.getVoiceId()){
				Attach voice = dao.findOne(Attach.class, "id", top.getVoiceId());
				voice.setRelId(top.getId());
				voice.setRelType("teacherTopic");
				dao.update(voice);
			}
			if(null != top.getPictrueIds() && !"".equals(top.getPictrueIds())){
				String[] split = top.getPictrueIds().split(",");
				for (int i = 0; i < split.length; i++) {
					if(!StringUtil.isEmpty(split[i])){
						Attach a = dao.get(Attach.class, Long.parseLong(split[i]));
						if(null != a){
							a.setRelId(top.getId());
							a.setRelType("teacherTopic");
							dao.update(a);
						}
					}
				}
			}
			msg.setType(AjaxMsg.SUCCESS);
			return msg;
		}
		return msg;
	}


	@Transactional
	public AjaxMsg reTeacherTopic(String lk, teachertopicre re) {
		AjaxMsg msg = new AjaxMsg();
		msg.setType(AjaxMsg.ERROR);
		if (!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)) {
			if(null == re){
				return msg;
			}
			
			if(StringUtil.isEmpty(re.getReContent())
					|| null == re.getTopicId()){
				return msg;
			}
			/** student 1    teacher  9 */
			if(null == re.getReUserId()
					|| StringUtil.isEmpty(re.getReUserType())){
				return msg;
			}
			re.setReDate(DateUtil.format(new Date(), DateUtil.DATETIME_PATTERN));
			dao.insert(re);
			dao.excute("update TeacherTopic set reCount = reCount + 1 where id = ?", re.getTopicId());
			msg.setMsg("回复成功");
			msg.setType(AjaxMsg.SUCCESS);
		}
		return msg;
	}
	
}
