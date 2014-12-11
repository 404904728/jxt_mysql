/**
 * Limit
 *
 */
package app.cq.hmq.service.appservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.quartz.jobs.ee.mail.SendMailJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;

import app.cq.hmq.pojo.letter.PrivateLetter;
import app.cq.hmq.pojo.letter.PrivateLetterRe;
import app.cq.hmq.pojo.stuinfo.StudentInfo;
import app.cq.hmq.pojo.teacherinfo.TeacherInfo;
import app.cq.hmq.service.letter.PrivateLetterService;

import common.cq.hmq.model.JqGridData;
import common.cq.hmq.model.JqPageModel;
import common.cq.hmq.pojo.sys.Attach;
import common.cq.hmq.pojo.sys.Org;
import common.cq.hmq.pojo.sys.User;
import common.cq.hmq.util.SendSMS;

import core.cq.hmq.dao.PageList;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.modal.SessionModal;
import core.cq.hmq.service.BaseService;
import core.cq.hmq.util.tools.DateUtil;
import core.cq.hmq.util.tools.Encrypt;
import core.cq.hmq.util.tools.StringUtil;

/**
 * @author Administrator
 *
 */
@Service(value = "appLetterService")
public class AppPrivateLetterService extends BaseService {

	@Autowired
	private PrivateLetterService zbservice;
	
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-21 上午10:30:53
	 *@version 1.0
	 *@Description 保存发送的私信信息
	 *
	 *@param lk
	 *@param letter
	 *@return
	 *
	 *
	 */
	@Transactional
	public AjaxMsg savePrivateLetter(String lk, PrivateLetter letter) {
		AjaxMsg msg = new AjaxMsg();
		msg.setType(AjaxMsg.ERROR);
		if (!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)) {
			if(null == letter){
				return msg;
			}
			
			if (null == letter.getReceiverId()
					|| "".equals(letter.getReceiverId())
					|| null == letter.getSenderId()
					|| "".equals(letter.getSenderId())
					|| null == letter.getSenderName()
					|| StringUtil.isEmpty(letter.getSenderName())
					|| null == letter.getSenderType()
					|| "".equals(letter.getSenderType())
					|| null == letter.getSendTime()
					|| StringUtil.isEmpty(letter.getSendTime())
					|| null == letter.getReceicerName()
					|| StringUtil.isEmpty(letter.getReceicerName())
					|| null == letter.getReceiverType()
					|| "".equals(letter.getReceiverType())
					|| null == letter.getContent()
					|| StringUtil.isEmpty(letter.getContent())) {
				return msg;
			}
			if(null == letter.getReceicerOrg()){
				if("1".equals(letter.getReceiverType())){
					StudentInfo studentInfo = dao.findOne(StudentInfo.class, "id", letter.getReceiverId());
					if(null == studentInfo){
						return msg;
					}
					Org org = new Org();
					org.setId(studentInfo.getOrg().getId());
					letter.setReceicerOrg(org);
				}
				if("2".equals(letter.getReceiverType())){
					TeacherInfo teacherInfo = dao.findOne(TeacherInfo.class, "id", letter.getReceiverId());
					if(null ==teacherInfo){
						return msg;
					}
					Org org = new Org();
					org.setId(teacherInfo.getOrg().getId());
					letter.setReceicerOrg(org);
				}
			}
		
			
			//type 1: 家长 2：老师
			
			if(null == letter.getSenderOrg()){
				if("1".equals(letter.getSenderType())){
					StudentInfo studentInfo = dao.findOne(StudentInfo.class, "id", letter.getSenderId());
					if(null == studentInfo){
						return msg;
					}
					Org org = new Org();
					org.setId(studentInfo.getOrg().getId());
					letter.setSenderOrg(org);
				}
				if("2".equals(letter.getSenderType())){
					TeacherInfo teacherInfo = dao.findOne(TeacherInfo.class, "id", letter.getSenderId());
					if(null ==teacherInfo){
						return msg;
					}
					Org org = new Org();
					org.setId(teacherInfo.getOrg().getId());
					letter.setSenderOrg(org);
				}
			}
			SessionModal currentSessionModel = new SessionModal();
			currentSessionModel.setId(letter.getSenderId());
			currentSessionModel.setName(letter.getSenderName());
			currentSessionModel.setUserType(letter.getSenderType());
			currentSessionModel.setOrgId(letter.getSenderOrg().getId());
			zbservice.savaSendLetter(letter, currentSessionModel);
//			dao.insert(letter);
			if(null != letter.getVoiceId() && !"".equals(letter.getVoiceId())){
				Attach attach = dao.findOne(Attach.class, "id", letter.getVoiceId());
				attach.setRelId(letter.getId());
				attach.setRelType("PrivateLetter");
				dao.update(attach);
			}
			msg.setType(AjaxMsg.SUCCESS);
			return msg;
		}
		return msg;
	}

	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-23 下午3:01:20
	 *@version 1.0
	 *@Description 查询私信列表
	 *
	 *@param lk
	 *@param userId
	 *@param userType
	 *@param ps
	 *@param pn
	 *@return
	 *
	 *
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JqGridData<?> queryPrivateLetter(String lk, Long userId, String userType,
			int ps, int pn) {
		JqGridData jqGridData = new JqGridData<List<Map<String, Object>>>();
		jqGridData.setPage(-100);
		if (!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)) {
			JqPageModel mode = new JqPageModel();
			if(null == userId || "".equals(userId) || null == userType || "".equals(userType)){
				return jqGridData;
			}
			if(pn <= 0){
				mode.setPage(1);
			}else{
				mode.setPage(pn);
			}
			mode.setRows(ps);
			SessionModal sessionModel = new SessionModal();
			sessionModel.setId(userId);
			sessionModel.setUserType(userType);
			StringBuffer totalHql =new StringBuffer(" select 1 ");
			StringBuffer hqlString =new StringBuffer(" select p.id_f,case  '"+sessionModel.getUserType()+sessionModel.getId()+"' " );
			hqlString.append("when p.sendertype_f || p.senderid_f then ");
			hqlString.append(" (case p.receivertype_f when '1' then (select concat(s.headpic_f,',',s.name_f) from studentinfo_t s where s.id_f = p.receiverid_f)  when '2' then (select t.headpic_f||','||t.name_f  from teacherinfo_t t where t.id_f = p.receiverid_f) end) ");
			hqlString.append(" when p.receivertype_f || p.receiverid_f then ");
			hqlString.append(" (case p.sendertype_f when '1' then (select concat(s.headpic_f,',',s.name_f) from studentinfo_t s where s.id_f = p.senderid_f)  when '2' then (select t.headpic_f||','||t.name_f  from teacherinfo_t t where t.id_f = p.senderid_f) end) ");
			hqlString.append(" end, ");
			hqlString.append(" left(p.newcontent_f,20), p.newredate_f,p.voiceid_f from PrivateLetter_t p");
			hqlString.append(" where (");
			hqlString.append(" p.senderid_f = "+sessionModel.getId()+" and p.sendertype_f = "+sessionModel.getUserType()+") or(");
			
			hqlString.append(" p.receiverid_f = "+sessionModel.getId()+" and p.receivertype_f = "+sessionModel.getUserType()+")");
			hqlString.append(" order by p.newReDate_f desc");
			
			totalHql.append("from PrivateLetter_t p");
			totalHql.append(" where (");
			totalHql.append(" p.senderid_f = "+sessionModel.getId()+" and p.sendertype_f = "+sessionModel.getUserType()+") or(");
			
			totalHql.append(" p.receiverid_f = "+sessionModel.getId()+" and p.receivertype_f = "+sessionModel.getUserType()+")");
	        
			 PageList<Object[]> ts  = dao.getHelperDao().pageByTotal(mode.getPage(), mode.getRows(), hqlString.toString(), totalHql.toString());
			 List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
	         if(null != ts && null != ts.getList()){
	        	 for (Object[] objs : ts.getList()) {
					if(null != objs && objs.length > 0){
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("id", objs[0]);
						String temp = String.valueOf(objs[1]);
						String[] nameAndOrg = temp.split(",");
						if(null != nameAndOrg){
							if(null != nameAndOrg[0]){
								if(!"".equals(nameAndOrg[0])){
									map.put("url", "download/" + nameAndOrg[0] + ".mp3");
								}else{
									map.put("url", "");
								}
							}else{
								map.put("url", "");
							}
							if(null != nameAndOrg[1]){
								if(!"".equals(nameAndOrg[1].toCharArray())){
									map.put("sendName", nameAndOrg[1]);
								}else{
									map.put("sendName", "");
								}
							}else{
								map.put("sendName", "");
							}
						}
						map.put("content", objs[2]);
						map.put("sendTime", DateUtil.format((Date)objs[3], DateUtil.DATETIME_PATTERN));
						if(null != objs[4] && !"".equals(objs[4].toString())){
							map.put("voiceId", objs[4].toString());
						}else{
							map.put("voiceId", "");
						}
						list.add(map);
					}
				}
	         }
			jqGridData.setPage(ts.getPageNo());
			jqGridData.setRows(list);
			jqGridData.setRecords(ts.getTotalCount());
			jqGridData.setTotal(ts.getPageCount());
			return jqGridData;
		}
		return jqGridData;
	}


	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-24 上午9:55:42
	 *@version 1.0
	 *@Description 查询一对一具体私信信息
	 * 
	 *@param lk
	 *@param userId
	 *@param userType
	 *@param start
	 *@param end
	 * @param plId 
	 *@return
	 *
	 *
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, Object> queryOnePrivateLetter(String lk, Long userId,
			String userType, int start, int end, Long plId) {
		List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
		Map<String, Object> plcontent = null;
		Map<String,Object> resultMap = new HashMap<String,Object>();
		if (!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)) {
			if(null == userId 
					|| "".equals(userId) 
					|| null == userType
					|| "".equals(userType)
					|| start < 0
					|| end <= 0){
					return resultMap;
			}
			String num = dao.getHelperDao().find("select count(1) from privateletterre_t where plid_f = "+ plId).get(0).toString();
			resultMap.put("num", Integer.parseInt(num) + 1);
			if(Integer.parseInt(num) <= 0 && end > 0){
				StringBuffer sb = new StringBuffer();
				sb.append(" select a.id_f,g.sendtime_f,g.content_f,g.voiceid_f ");
				sb.append(" from (select case r.sendertype_f ");
				sb.append(" when '1' then (select s.headpic_f from studentinfo_t s where s.id_f = r.senderid_f)  ");
				sb.append(" when '2' then (select t.headpic_f from teacherinfo_t t where t.id_f = r.senderid_f) end as touxiang, ");
				sb.append(" r.sendername_f, r.sendtime_f,r.senderorg_f,  r.content_f, r.voiceid_f ");
				sb.append(" from PrivateLetter_t r where r.id_f = "+ plId +") g");
				sb.append(" left join attach_t a on a.id_f = g.touxiang ");
				List mainLetter = dao.getHelperDao().find(sb.toString());
				if(mainLetter.size() <= 0){
					return resultMap;
				}
				Object[] object = (Object[]) mainLetter.get(0);
				if(null != object && object.length != 0){
					for (int i = 0; i < object.length; i++) {
						plcontent = new HashMap<String, Object>();
						if(null != object[0] && !"".equals(object[0].toString())){
							plcontent.put("touxiang", "download/" + object[0].toString() + ".jpg");
						}else{
							plcontent.put("touxiang", "");
						}
						if(null != object[1] && !"".equals(object[1].toString())){
							plcontent.put("time", object[1].toString());
						}else{
							plcontent.put("time", "");
						}
						if(null != object[2] && !"".equals(object[2].toString())){
							plcontent.put("countent", object[2].toString());
						}else{
							plcontent.put("countent", "");
						}
						if(null != object[3] && !"".equals(object[3].toString())){
							plcontent.put("voicdId", "dowmload/"+object[3].toString()+".mp3");
						}else{
							plcontent.put("voicdId", "");
						}
						plcontent.put("isMyself", "0");
					}
					resList.add(plcontent);
					resultMap.put("result", resList);
					return resultMap;
				}
			}
			
			if(end < Integer.parseInt(num)){
				resultMap.put("result", getCountentMessage(userId,userType,start,end,plId));
//				return getCountentMessage(userId,userType,start,end,plId);
				return resultMap;
			}else if(end >= Integer.parseInt(num)){
				List<Map<String, Object>> countentMessage = getCountentMessage(userId,userType,start,end,plId);
				StringBuffer sb = new StringBuffer();
				sb.append(" select a.id_f,g.sendtime_f,g.content_f,g.voiceid_f ");
				sb.append(" from (select case r.sendertype_f ");
				sb.append(" when '1' then (select s.headpic_f from studentinfo_t s where s.id_f = r.senderid_f)  ");
				sb.append(" when '2' then (select t.headpic_f from teacherinfo_t t where t.id_f = r.senderid_f) end as touxiang, ");
				sb.append(" r.sendername_f, r.sendtime_f,r.senderorg_f,  r.content_f, r.voiceid_f ");
				sb.append(" from PrivateLetter_t r where r.id_f = "+ plId +") g");
				sb.append(" left join attach_t a on a.id_f = g.touxiang ");
				List mainLetter = dao.getHelperDao().find(sb.toString());
				if(mainLetter.size() <= 0){
					resultMap.put("result", countentMessage);
					return resultMap;
				}
				Object[] object = (Object[]) mainLetter.get(0);
				if(null != object && object.length != 0){
					for (int i = 0; i < object.length; i++) {
						plcontent = new HashMap<String, Object>();
						if(null != object[0] && !"".equals(object[0].toString())){
							plcontent.put("touxiang", "download/" + object[0].toString() + ".jpg");
						}else{
							plcontent.put("touxiang", "");
						}
						if(null != object[1] && !"".equals(object[1].toString())){
							plcontent.put("time", object[1].toString());
						}else{
							plcontent.put("time", "");
						}
						if(null != object[2] && !"".equals(object[2].toString())){
							plcontent.put("countent", object[2].toString());
						}else{
							plcontent.put("countent", "");
						}
						if(null != object[3] && !"".equals(object[3].toString())){
							plcontent.put("voicdId", "download/"+object[3].toString()+".mp3");
						}else{
							plcontent.put("voicdId", "");
						}
						plcontent.put("isMyself", "0");
					}
					countentMessage.add(plcontent);
					resultMap.put("result", countentMessage);
					return resultMap;
				}
			}
		}
		return resultMap;
	}
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-24 上午10:51:00
	 *@version 1.0
	 *@Description 内部方法
	 *
	 *@param userId
	 *@param userType
	 *@param start
	 *@param end
	 *@param plId
	 *@return
	 *
	 *
	 */
	@SuppressWarnings("rawtypes")
	private List<Map<String, Object>> getCountentMessage(Long userId,
			String userType, int start, int end, Long plId){
		List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
		Map<String, Object> plcontent = null;
		StringBuffer sb = new StringBuffer();
		sb.append("  select touxiang,");
//		sb.append(" reusername_f, ");
		sb.append(" retime_f, ");
		sb.append(" recontent_f, ");
		sb.append(" voiceid_f, ");
		sb.append(" flg ");
		sb.append(" from (select case r.reusertype_f ");
		sb.append(" when '1' then (select s.headpic_f from studentinfo_t s where s.id_f = r.reuserid_f) ");
		sb.append("  when '2' then (select t.headpic_f from teacherinfo_t t where t.id_f = r.reuserid_f) ");
		sb.append(" end as touxiang, ");
		sb.append(" r.reusername_f,r.retime_f, r.reuserorg_f,r.recontent_f,r.voiceid_f, ");
		sb.append(" case '"+ userId + userType +"' when r.reuserid_f || r.reusertype_f then '0' else '1' end as flg, ");
		sb.append(" r.id_f from privateletterre_t r  ");
		sb.append(" where r.plid_f =  " + plId + "  order by r.retime_f desc) a limit " + start + "," + (end-start));
		List rsList = dao.getHelperDao().find(sb.toString());
		if(rsList.size() <= 0){
			return resList;
		}
		for (int i = 0; i < rsList.size(); i++) {
			Object[] obj = (Object[]) rsList.get(i);
			plcontent = new HashMap<String, Object>();
			if(null != obj){
				if(null != obj[0] && !"".equals(obj[0].toString())){
					plcontent.put("touxiang", "download/" + obj[0].toString() + ".jpg");
				}else{
					plcontent.put("touxiang", "");
				}
				
				if(null != obj[1] && !"".equals(obj[1].toString())){
					plcontent.put("time", obj[1].toString());
				}else{
					plcontent.put("time", "");
				}
				
				if(null != obj[2] && !"".equals(obj[2].toString())){
					plcontent.put("countent", obj[2].toString());
				}else{
					plcontent.put("countent", "");
				}
				
				if(null != obj[3] && !"".equals(obj[3].toString())){
					plcontent.put("voicdId", "download/"+obj[3].toString()+".mp3");
				}else{
					plcontent.put("voicdId", "");
				}
				
				if(null != obj[4] && !"".equals(obj[4].toString())){
					if("1".equals(obj[4].toString())){//0代表自己
						plcontent.put("isMyself", obj[4].toString());
					}else if("0".equals(obj[4].toString())){//1代表其他人
						plcontent.put("isMyself", obj[4].toString());
					}
				}else{
					plcontent.put("isMyself", "");
				}
				resList.add(plcontent);
			}
		}
		return resList;
	}

	@Autowired
	private PrivateLetterService privateLetterService;

	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-24 上午11:25:11
	 *@version 1.0
	 *@Description 保存一对一具体私信信息
	 *
	 *@param lk
	 *@param re
	 *@return
	 * @throws ChannelServerException 
	 * @throws ChannelClientException 
	 * @throws NumberFormatException 
	 *
	 *
	 */
	@Transactional
	public AjaxMsg saveOnePrivateLetter(String lk,
			PrivateLetterRe re) throws NumberFormatException, ChannelClientException, ChannelServerException {
		AjaxMsg msg = new AjaxMsg();
		msg.setType(AjaxMsg.ERROR);
		if (!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)) {
			if(null == re){
				return msg;
			}
			if(null == re.getPlId() 
					|| "".equals(re.getPlId())
					|| null == re.getReContent()
					|| "".equals(re.getReContent())
					|| null == re.getReTime()
					|| "".equals(re.getReTime())
					|| null == re.getReUserId()
					|| "".equals(re.getReUserId())
					|| null == re.getReUserName()
					|| "".equals(re.getReUserName())
					|| null == re.getReUserType()
					|| "".equals(re.getReUserType())){
				return msg;
			}
			dao.insert(re);
			if(null != re.getVoiceId() && !"".equals(re.getVoiceId())){
				Attach att = dao.findOne(Attach.class, "id", re.getVoiceId());
				att.setRelId(re.getId());
				att.setRelType("PrivateLetter");
				dao.update(att);
			}
			//to_date(?,'yyyy-MM-dd HH24:mi:ss')
			dao.getHelperDao().excute("update privateletter_t ser set ser.newcontent_f = '"+ re.getReContent() +"', ser.newredate_f = to_date(?,'yyyy-MM-dd HH24:mi:ss'),ser.newReUserId_f = "+ re.getReUserId() +",ser.newReUserType_f = '"+ re.getReUserType() +"' where ser.id_f = ? ",re.getReTime(),re.getPlId());
			msg.setType(AjaxMsg.SUCCESS);
			PrivateLetter pl = dao.findOne(PrivateLetter.class, "id",re.getPlId());
			if(re.getReUserId().equals(pl.getReceiverId())
				&& re.getReUserType().equals(pl.getReceiverType())){
				privateLetterService.getPushPerson(pl.getSenderId(),pl.getSenderType(),"您有新私信,请查看!");
			}else{
				privateLetterService.getPushPerson(pl.getReceiverId(),pl.getReceiverType(),"您有新私信,请查看!");
			}
			return msg;
		}
		return msg;
	}


	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-5-9 下午2:28:07
	 *@version 1.0
	 *@Description 修改密码
	 *
	 *@param lk
	 *@param op
	 *@param np
	 *@param id
	 *@param userType
	 * @param request 
	 *@return
	 *
	 *
	 */
	@Transactional
	public AjaxMsg updatePassword(String lk, String op, String np, Long id, String userType) {
		AjaxMsg aMsg = new AjaxMsg();
		aMsg.setType(AjaxMsg.ERROR);
		if (!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)) {
			if(StringUtil.isEmpty(op) || StringUtil.isEmpty(np)){
				aMsg.setMsg("密码不能为空");
				return aMsg;
			}
			if(null == id || StringUtil.isEmpty(id)){
				aMsg.setMsg("密码不能为空");
				return aMsg;
			}
			if(null == userType || StringUtil.isEmpty(userType)){
				aMsg.setMsg("密码不能为空");
				return aMsg;
			}
			SessionModal sm = currentSessionModel();
			if ("1".equals(userType)) {
				List<StudentInfo> list = dao.find(StudentInfo.class, "id",
						id);
				if(Encrypt.md5(op).equals(list.get(0).getPwd())){
					for (StudentInfo studentInfo : list) {
						studentInfo.setPwd(Encrypt.md5(np));
						dao.update(studentInfo);
					}
				}else{
					aMsg.setType(AjaxMsg.ERROR);
					aMsg.setMsg("原密码错误");
					return aMsg;
				}
			}
			if ("2".equals(userType)) {
				TeacherInfo teaInfo = dao.findOne(TeacherInfo.class, "id",
						id);
				if(Encrypt.md5(op).equals(teaInfo.getPwd())){
					teaInfo.setPwd(Encrypt.md5(np));
					dao.update(teaInfo);
				}else{
					aMsg.setType(AjaxMsg.ERROR);
					aMsg.setMsg("原密码错误");
					return aMsg;
				}
			}
			aMsg.setType(AjaxMsg.SUCCESS);
			aMsg.setMsg("修改成功!");
			return aMsg;
		}
		return aMsg;
	}


	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-5-9 下午2:48:22
	 *@version 1.0
	 *@Description  发送验证码
	 *
	 *@param lk
	 *@param no
	 *@param userType
	 * @param request 
	 *
	 *
	 */
	public AjaxMsg regNoExists(String lk, String no, String userType, HttpServletRequest request) {
		AjaxMsg aMsg = new AjaxMsg();
		aMsg.setType(AjaxMsg.ERROR);
		if (!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)) {
			if(null == no 
			|| StringUtil.isEmpty(no)
			|| null == userType
			|| StringUtil.isEmpty(userType)){
				aMsg.setMsg("数据为空");
				return aMsg;
			}
			HttpSession session = request.getSession();
			//设置session超时时间10分钟
			session.setMaxInactiveInterval(600);
			Random random = new Random();
			if("1".equals(userType)){
				StudentInfo findOne = dao.findOne(StudentInfo.class, "no", no);
				if(null == findOne){
					aMsg.setMsg("没有当前用户");
					return aMsg;
				}
				if(null == findOne.getNo() || "".equals(findOne.getNo())){
					aMsg.setMsg("没有当前用户");
				}
				int rand = 1000+random.nextInt(9000);
				session.setAttribute(findOne.getNo()+","+userType, rand + "");
				SendSMS.sendMsg("您修改密码的验证码为:" + rand + ",有效时间为10分钟", findOne.getNo(),null,"修改密码-短信验证码");
				aMsg.setType(AjaxMsg.SUCCESS);
				aMsg.setMsg("发送成功");
				return aMsg;
			}
			if("2".equals(userType)){
				TeacherInfo findOne = dao.findOne(TeacherInfo.class, "no", no);
				if(null == findOne){
					aMsg.setMsg("没有当前用户");
					return aMsg;
				}
				if(null == findOne.getNo() || "".equals(findOne.getNo())){
					aMsg.setMsg("没有当前用户");
				}
				int rand = 1000+random.nextInt(9000);
				session.setAttribute(findOne.getNo()+","+userType, rand+"");
				SendSMS.sendMsg("您修改密码的验证码为： " + rand + ",有效时间为10分钟", findOne.getNo(),null,"修改密码-短信验证码");
				aMsg.setType(AjaxMsg.SUCCESS);
				aMsg.setMsg("发送成功");
				return aMsg;
			}
		}
		return aMsg;
	}


	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-5-9 下午3:07:54
	 *@version 1.0
	 *@Description 重置密码
	 *
	 *@param lk
	 *@param reg
	 *@param no
	 *@param userType
	 *@param request
	 * @param np 
	 *@return
	 *
	 *
	 */
	@Transactional
	public AjaxMsg resetPassword(String lk, String reg, String no,
			String userType, HttpServletRequest request, String np) {
		AjaxMsg aMsg = new AjaxMsg();
		aMsg.setType(AjaxMsg.ERROR);
		if (!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)) {
			if(null == reg 
			   || StringUtil.isEmpty(reg)
			   || null == no
			   || StringUtil.isEmpty(no)
			   || null == userType
			   || StringUtil.isEmpty(userType)
			   || null == np
			   || StringUtil.isEmpty(np)){
				return aMsg;
			}
			HttpSession session = request.getSession();
			String attribute = (String) session.getAttribute(no+","+userType);
			if(null == attribute || "".equals(attribute)){
				aMsg.setMsg("验证码错误");
				return aMsg;
			}
			if(reg.equals(attribute)){
				if("1".equals(userType)){
					StudentInfo findOne = dao.findOne(StudentInfo.class, "no", no);
					if(null == findOne){
						aMsg.setMsg("没有当前用户");
						return aMsg;
					}
					if(null == findOne.getNo() || "".equals(findOne.getNo())){
						aMsg.setMsg("没有当前用户");
					}
					findOne.setPwd(Encrypt.md5(np));
					dao.update(findOne);
					session.removeAttribute(no+","+userType);
					aMsg.setType(AjaxMsg.SUCCESS);
					aMsg.setMsg("修改成功");
					return aMsg;
				}
				if("2".equals(userType)){
					TeacherInfo findOne = dao.findOne(TeacherInfo.class, "no", no);
					if(null == findOne){
						aMsg.setMsg("没有当前用户");
						return aMsg;
					}
					if(null == findOne.getNo() || "".equals(findOne.getNo())){
						aMsg.setMsg("没有当前用户");
					}
					findOne.setPwd(Encrypt.md5(np));
					dao.update(findOne);
					session.removeAttribute(no+","+userType);
					aMsg.setType(AjaxMsg.SUCCESS);
					aMsg.setMsg("修改成功");
					return aMsg;
				}
			}else{
				return aMsg;
			}
		}
		return aMsg;
	}
}
