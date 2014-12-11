package app.cq.hmq.service.letter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.jxt.PushUtil;
import com.baidu.yun.jxt.model.PushMsgModel;

import app.cq.hmq.pojo.letter.PrivateLetter;
import app.cq.hmq.pojo.letter.PrivateLetterRe;
import app.cq.hmq.pojo.stuinfo.StudentInfo;
import app.cq.hmq.pojo.teacherinfo.TeacherInfo;

import common.cq.hmq.model.JqGridData;
import common.cq.hmq.model.JqPageModel;
import common.cq.hmq.pojo.sys.Org;
import common.cq.hmq.pojo.sys.User;

import core.cq.hmq.dao.PageList;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.modal.SessionModal;
import core.cq.hmq.service.BaseService;
import core.cq.hmq.util.tools.DateUtil;
import core.cq.hmq.util.tools.StringUtil;

@Service
public class PrivateLetterService extends BaseService{
	
	public JqGridData<?> obtainInboxInfos(JqPageModel jqPagemodel,SessionModal sessionModal){
		/*StringBuffer hqlString =new StringBuffer(" from PrivateLetter where 1=1 ");
		JqGridData jqGridData = new JqGridData<List<PrivateLetter>>();
		hqlString.append(" and receiverType = " +sessionModal.getUserType());
		hqlString.append(" and receiverId = " +sessionModal.getId());
		
		if(!StringUtil.isEmpty(jqPagemodel.getSearchKey())){
			hqlString.append(" and senderName "+ escapeChar(jqPagemodel.getSearchKey()));
		}
		
		if(!StringUtil.isEmpty(jqPagemodel.getSort()) && !StringUtil.isEmpty(jqPagemodel.getOrder())){
			hqlString.append(" order by "+jqPagemodel.getSort()+" " + jqPagemodel.getOrder());
		}else{
			hqlString.append(" order by newReDate desc,status");
		}
		PageList<?> ts = dao.page(jqPagemodel.getPage(), jqPagemodel.getRows(), hqlString.toString());
		List<PrivateLetter> pl = (List<PrivateLetter>) ts.getList();
		String content = null;
		for(PrivateLetter privateLetter : pl){
			content = privateLetter.getContent();
			if(null != content && content.length() > 10){
				privateLetter.setContent(content.substring(0, 10)+"...");
			}
		}
		jqGridData.setPage(ts.getPageNo());
		jqGridData.setRows(pl);
		jqGridData.setRecords(ts.getTotalCount());
		jqGridData.setTotal(ts.getPageCount());*/
		JqGridData jqGridData = new JqGridData<List<Map<String, Object>>>();
		StringBuffer totalHql =new StringBuffer(" select 1 ");
		StringBuffer hqlString =new StringBuffer(" select p.id_f,case  '"+sessionModal.getUserType()+sessionModal.getId()+"' " );
		hqlString.append("when CONCAT(p.sendertype_f , p.senderid_f) then ");
		hqlString.append(" CONCAT(p.receicername_f ,',',");
		hqlString.append("(select name_f from org_t where id_f = p.receicerorg_f)) ");
		hqlString.append(" when CONCAT(p.receivertype_f, p.receiverid_f) then ");
		hqlString.append(" CONCAT(p.sendername_f,',',");
		hqlString.append("(select name_f from org_t where id_f = p.senderorg_f)) end,");
		hqlString.append(" left(p.newcontent_f,20), p.newredate_f from PrivateLetter_t p");
		hqlString.append(" where (");
		if(!StringUtil.isEmpty(jqPagemodel.getSearchKey())){
			hqlString.append(" p.receicername_f "+ escapeChar(jqPagemodel.getSearchKey()));
			hqlString.append(" and ");
		}
		hqlString.append(" p.senderid_f = "+sessionModal.getId()+" and p.sendertype_f = "+sessionModal.getUserType()+") or(");
		
		if(!StringUtil.isEmpty(jqPagemodel.getSearchKey())){
			hqlString.append(" p.sendername_f "+ escapeChar(jqPagemodel.getSearchKey()));
			hqlString.append(" and ");
		}
		hqlString.append(" p.receiverid_f = "+sessionModal.getId()+" and p.receivertype_f = "+sessionModal.getUserType()+")");
		if(!StringUtil.isEmpty(jqPagemodel.getSort()) && !StringUtil.isEmpty(jqPagemodel.getOrder())){
			hqlString.append(" order by p."+jqPagemodel.getSort()+"_f " + jqPagemodel.getOrder());
		}else{
			hqlString.append(" order by p.newReDate_f desc");
		}
		
		totalHql.append("from PrivateLetter_t p");
		totalHql.append(" where (");
		if(!StringUtil.isEmpty(jqPagemodel.getSearchKey())){
			totalHql.append(" p.receicername_f "+ escapeChar(jqPagemodel.getSearchKey()));
			totalHql.append(" and ");
		}
		totalHql.append(" p.senderid_f = "+sessionModal.getId()+" and p.sendertype_f = "+sessionModal.getUserType()+") or(");
		
		if(!StringUtil.isEmpty(jqPagemodel.getSearchKey())){
			totalHql.append(" p.sendername_f "+ escapeChar(jqPagemodel.getSearchKey()));
			totalHql.append(" and ");
		}
		totalHql.append(" p.receiverid_f = "+sessionModal.getId()+" and p.receivertype_f = "+sessionModal.getUserType()+")");
        
		 PageList<Object[]> ts  = dao.getHelperDao().pageByTotal(jqPagemodel.getPage(), jqPagemodel.getRows(), hqlString.toString(), totalHql.toString());
		 List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
         if(null != ts && null != ts.getList()){
        	 for (Object[] objs : ts.getList()) {
				if(null != objs && objs.length > 0){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", objs[0]);
					String temp = String.valueOf(objs[1]);
					String[] nameAndOrg = temp.split(",");
					if(null != nameAndOrg && nameAndOrg.length == 2){
						map.put("senderName", nameAndOrg[0]);
						map.put("senderOrgName", nameAndOrg[1]);
					}
					map.put("content", objs[2]);
					map.put("sendTime", DateUtil.format((Date)objs[3], DateUtil.DATETIME_PATTERN));
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
	

	public JqGridData<?> obtainOutboxInfos(JqPageModel jqPagemodel,
			SessionModal sessionModal) {
		StringBuffer hqlString =new StringBuffer(" from PrivateLetter where 1=1");
		JqGridData jqGridData = new JqGridData<List<PrivateLetter>>();
		hqlString.append(" and senderType = " +sessionModal.getUserType());
		hqlString.append(" and senderId = " +sessionModal.getId());
		
		if(!StringUtil.isEmpty(jqPagemodel.getSearchKey())){
			hqlString.append(" and receicerName "+ escapeChar(jqPagemodel.getSearchKey()));
		}
		
		if(!StringUtil.isEmpty(jqPagemodel.getSort()) && !StringUtil.isEmpty(jqPagemodel.getOrder())){
			hqlString.append(" order by "+jqPagemodel.getSort()+" " + jqPagemodel.getOrder());
		}else{
			hqlString.append(" order by newReDate,status desc");
		}
		PageList<?> ts = dao.page(jqPagemodel.getPage(), jqPagemodel.getRows(), hqlString.toString());
		List<PrivateLetter> pl = (List<PrivateLetter>) ts.getList();
		String content = null;
		for(PrivateLetter privateLetter : pl){
			content = privateLetter.getContent();
			if(null != content && content.length() > 10){
				privateLetter.setContent(content.substring(0, 10)+"...");
			}
		}
		jqGridData.setPage(ts.getPageNo());
		jqGridData.setRows(pl);
		jqGridData.setRecords(ts.getTotalCount());
		jqGridData.setTotal(ts.getPageCount());
		return jqGridData;
	}
	
	@Transactional
	public Map<String, Object> findPrivateLetterByInboxId(Long lId,int start, int end){
		Map<String, Object> rmap = new HashMap<String, Object>();
		List<Map<String, Object>>  listMap = new ArrayList<Map<String,Object>>();
		
		if(start == 0 && end == 0){
			/** click row record, update status is true when new reuser is not self */
			SessionModal sm = currentSessionModel();
			dao.excute("update PrivateLetter set status = true where (newReUserType != ? or (newReUserType = ? and newReUserId != ?))" +
					"and status != true and id = ?",
					sm.getUserType(),sm.getUserType(),sm.getId(),lId);
			end = Integer.parseInt(String.valueOf(dao.getHelperDao().find("select count(1) from privateletterre_t where plid_f = "+ lId).get(0)));
			rmap.put("t", end);
			start = end - 10;
		}
		
		if(start >= 0){
			/** 开始位置为start 结束为end */
		}else{
			/** 开始位置为0 结束为end */
			start = 0;
		}
		/** 还有多少条记录未查询 */
		if(start != 0 && start == end){
			end = start + 10;
		}else{
			rmap.put("s", start);
		}
		
		if(start == 0){
			StringBuilder lbBuilder = new StringBuilder("select a.id_f,a.suffix_f,g.sendername_f,g.sendtime_f,g.content_f,g.voiceid_f ");
			lbBuilder.append("from (select case r.sendertype_f when '");
			lbBuilder.append(User.STUDENTTYPE);
			lbBuilder.append("' then (select s.headpic_f from studentinfo_t s ");
			lbBuilder.append("where s.id_f = r.senderid_f) ");
			lbBuilder.append("when '");
			lbBuilder.append(User.TEACHERTYPE);
			lbBuilder.append("' then (select  t.headpic_f from teacherinfo_t t ");
			lbBuilder.append("where t.id_f = r.senderid_f) ");
			lbBuilder.append("end  as touxiang,r.sendername_f,r.sendtime_f ,r.senderorg_f,r.content_f,r.voiceid_f ");
			lbBuilder.append("from PrivateLetter_t r where r.id_f = ?) g ");
			lbBuilder.append("left join attach_t a on a.id_f = g.touxiang");
			List<?> list = dao.getHelperDao().find(lbBuilder.toString(), lId);
				if(null != list && list.size() > 0){
					Object[] objs = null;
					for(Object o : list){
						objs = (Object[])o;
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("headpic", "download/"+objs[0]+"."+objs[1]);
						map.put("sendname", objs[2]);
						map.put("sendtime", objs[3]);
						map.put("content", objs[4] == null ? "" : objs[4]);
						map.put("voice", objs[5]);
						listMap.add(map);
					}
				}
		}
		StringBuilder rebBuilder = new StringBuilder("select touxiang,'jpg',reusername_f,retime_f,recontent_f,voiceid_f ");
		rebBuilder.append(" from (select case r.reusertype_f when '");
		rebBuilder.append(User.STUDENTTYPE);
		rebBuilder.append("' then (select s.headpic_f ");
		rebBuilder.append("from studentinfo_t s where s.id_f = r.reuserid_f) ");
		rebBuilder.append("when '");
		rebBuilder.append(User.TEACHERTYPE);
		rebBuilder.append("' then (select  t.headpic_f from teacherinfo_t t ");
		rebBuilder.append("where t.id_f = r.reuserid_f)  end  as touxiang,r.reusername_f,r.retime_f, ");
		rebBuilder.append("r.reuserorg_f,r.recontent_f,r.voiceid_f,r.id_f from privateletterre_t r ");
		rebBuilder.append("where r.plid_f = ? order by r.id_f) a limit ?,? ");
		List<?> relist = dao.getHelperDao().find(rebBuilder.toString(), lId,start,end-start);
		if(null != relist && relist.size() > 0){
			Object[] objs = null;
			for(Object o : relist){
				objs = (Object[] )o;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("headpic", "download/"+objs[0]+"."+objs[1]);
				map.put("sendname", objs[2]);
				map.put("sendtime", objs[3]);
				map.put("content", objs[4] == null ? "" : objs[4]);
				map.put("voice", objs[5]);
				listMap.add(map);
			}
		}
		rmap.put("r", listMap);
		return rmap;
	}
	
	@Transactional
	public AjaxMsg rePrivateLetter(Long id,String content,SessionModal sm) {
		AjaxMsg am = new AjaxMsg();
		if(null != id){
			String date = DateUtil.format(new Date(), DateUtil.DATETIME_PATTERN);
			
			PrivateLetterRe re = new PrivateLetterRe();
			re.setPlId(id);
			re.setReUserName(sm.getName());
			re.setReUserId(sm.getId());
			re.setReContent(content);
			re.setReUserType(sm.getUserType());
			re.setReTime(date);
			try {
				dao.insert(re);
				dao.getHelperDao().excute("update PRIVATELETTER_T p set p.newcontent_f = ?," +
						"p.newredate_f = to_date(?,'yyyy-MM-dd HH24:mi:ss'),p.newReUserId_f = ?,p.newReUserType_f = ?," +
						"p.status_f = 0 where" +
						" p.id_f = ?",content,date,re.getReUserId(),re.getReUserType(),id);
				PrivateLetter pl = dao.findOne(PrivateLetter.class, "id",id);
				if(sm.getId().equals(pl.getReceiverId())
					&& sm.getUserType().equals(pl.getReceiverType())){
					getPushPerson(pl.getSenderId(),pl.getSenderType(),"您有新私信,请查看!");
				}else{
					getPushPerson(pl.getReceiverId(),pl.getReceiverType(),"您有新私信,请查看!");
				}
				am.setMsg("发送成功！");
				return am;
			} catch (Exception e) {
				am.setMsg("发送失败！");
				am.setType(AjaxMsg.ERROR);
				return am;
			}
		}
		am.setMsg("发送失败！");
		am.setType(AjaxMsg.ERROR);
		return am;
	}
	
	@Transactional
	public AjaxMsg savaSendLetter(PrivateLetter pl,
			SessionModal currentSessionModel) {
		AjaxMsg am = new AjaxMsg();
		if(null != pl){
			Date curDate = new Date();
			String date = DateUtil.format(curDate, DateUtil.DATETIME_PATTERN);
			pl.setSenderId(currentSessionModel.getId());
			pl.setSenderName(currentSessionModel.getName());
			pl.setSenderType(currentSessionModel.getUserType());
			pl.setSendTime(date);
			pl.setNewReDate(curDate);
			Org senderOrg = new Org();
			senderOrg.setId(currentSessionModel.getOrgId());
			pl.setSenderOrg(senderOrg);
			pl.setNewContent(pl.getContent());
			pl.setNewReUserId(pl.getSenderId());
			pl.setNewReUserType(pl.getSenderType());
			
			/** 判断是否双方是否已经存在私信*/
			String privateLetterID = null;
			String judgeSql = "select p.id_f from PrivateLetter_t p where " +
					"(p.senderid_f = ? and p.sendertype_f = ? and p.receiverid_f = ? and p.receivertype_f = ?)" +
					" or (p.receiverid_f = ? and p.receivertype_f = ? and p.senderid_f = ? and p.sendertype_f = ?)  limit 1";
			List<String> list = dao.getHelperDao().find(judgeSql, pl.getSenderId(),pl.getSenderType(),pl.getReceiverId(),pl.getReceiverType()
					,pl.getSenderId(),pl.getSenderType(),pl.getReceiverId(),pl.getReceiverType());
			if(null != list && list.size() > 0){
				privateLetterID = String.valueOf(list.get(0));
				if(!StringUtil.isEmpty(privateLetterID) && !"null".equalsIgnoreCase(privateLetterID)){
					PrivateLetterRe re = new PrivateLetterRe();
					re.setPlId(Long.parseLong(privateLetterID));
					re.setReUserName(pl.getSenderName());
					re.setReUserId(pl.getSenderId());
					re.setReContent(pl.getContent());
					re.setReUserType(pl.getSenderType());
					re.setReTime(date);
					re.setVoiceId(pl.getVoiceId());
					try {
						dao.insert(re);
						dao.getHelperDao().excute("update PRIVATELETTER_T p set p.newcontent_f = ?," +
								"p.newredate_f = str_to_date(?, '%Y-%m-%d %H:%i:%s'),p.newReUserId_f = ?,p.newReUserType_f = ?," +
								"p.status_f = 0 where" +
								" p.id_f = ?",pl.getContent(),date,pl.getSenderId(),pl.getSenderType(),privateLetterID);
						//getPushPerson(pl.getReceiverId(),pl.getReceiverType(),"您有新私信,请查看!");
						am.setMsg("发送成功！");
						return am;
					} catch (Exception e) {
						am.setMsg("发送失败！");
						am.setType(AjaxMsg.ERROR);
						return am;
					}
				}else{
					try {
						if(User.TEACHERTYPE.equals(pl.getReceiverType())){
							TeacherInfo te = (TeacherInfo)dao.findOne("from TeacherInfo where id=?",pl.getReceiverId());
							if(null != te){
								pl.setReceicerName(te.getName());
								pl.setReceicerOrg(te.getOrg());
							}
						}else if(User.STUDENTTYPE.equals(pl.getReceiverType())){
							StudentInfo st = (StudentInfo)dao.findOne("from StudentInfo where id=?",pl.getReceiverId());
							if(null != st){
								pl.setReceicerName(st.getName());
								pl.setReceicerOrg(st.getOrg());
							}
						}
						dao.insert(pl);
						//getPushPerson(pl.getReceiverId(),pl.getReceiverType(),"您有新私信,请查看!");
						am.setMsg("发送成功！");
					} catch (Exception e) {
						am.setType(AjaxMsg.ERROR);
						am.setMsg("发送失败！");
					}
				}
			}else{
				try {
					if(User.TEACHERTYPE.equals(pl.getReceiverType())){
						TeacherInfo te = (TeacherInfo)dao.findOne("from TeacherInfo where id=?",pl.getReceiverId());
						if(null != te){
							pl.setReceicerName(te.getName());
							pl.setReceicerOrg(te.getOrg());
						}
					}else if(User.STUDENTTYPE.equals(pl.getReceiverType())){
						StudentInfo st = (StudentInfo)dao.findOne("from StudentInfo where id=?",pl.getReceiverId());
						if(null != st){
							pl.setReceicerName(st.getName());
							pl.setReceicerOrg(st.getOrg());
						}
					}
					dao.insert(pl);
					//推送
					//getPushPerson(pl.getReceiverId(),pl.getReceiverType(),"您有新私信,请查看!");
					am.setMsg("发送成功！");
				} catch (Exception e) {
					am.setType(AjaxMsg.ERROR);
					am.setMsg("发送失败！");
				}
			}
		}
		return am;
	}


	public JqGridData<?> obtainStudentByClassId(Long cid,JqPageModel jqPagemodel) {
		JqGridData jqGridData = new JqGridData<List<?>>();
		List<Object> slist = new ArrayList<Object>();
		if(null != cid){
			StringBuilder sql = new StringBuilder("select s.id_f,s.name_f,s.studentcode_f,s.sex_f,s.parentname_f from studentinfo_t s where s.org_f = "
					+cid);
			if(!StringUtil.isEmpty(jqPagemodel.getSearchKey())){
				sql.append(" and s.name_f "+escapeChar(jqPagemodel.getSearchKey()));
			}
			
			if(!StringUtil.isEmpty(jqPagemodel.getSort()) && !StringUtil.isEmpty(jqPagemodel.getOrder())){
				sql.append(" order by "+jqPagemodel.getSort()+"_f " + jqPagemodel.getOrder());
			}
			PageList<Object> list = dao.getHelperDao().page(jqPagemodel.getPage(),jqPagemodel.getRows(), sql.toString());
			if(null != list && list.size() > 0){
				Object[] objs = null;
				for(Object o : list.getList()){
					Map<String, Object> map = new HashMap<String, Object>();
					objs = (Object[]) o;
					map.put("id", objs[0]);
					map.put("name", objs[1]);
					map.put("studentCode", objs[2]);
					map.put("sex", objs[3]);
					map.put("parentName", objs[4]);
					slist.add(map);
				}
			}
			jqGridData.setPage(list.getPageNo());
			jqGridData.setRows(slist);
			jqGridData.setRecords(list.getTotalCount());
			jqGridData.setTotal(list.getPageCount());
		}
		return jqGridData;
	}
	
	public String escapeChar(String str) {
		return "like '%"
				+ str.replace("%", "/%").replace("'", "''").replace("_", "/_")
				+ "%' escape '/' ";
	}

	

	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-14 下午4:51:23
	 *@version 1.0
	 *@Description 查询所有教师
	 *
	 *@param jqPagemodel
	 *@return
	 *
	 *
	 */
	public JqGridData<?> obtainStudentBySchool(JqPageModel jqPagemodel) {
		JqGridData jqGridData = new JqGridData<List<?>>();
		List<Object> slist = new ArrayList<Object>();
			StringBuilder sql = new StringBuilder("select info.id_f,info.telephone_f,info.name_f,info.gender_f,info.zhicheng_f from teacherinfo_t info where 1=1 ");
			if(!StringUtil.isEmpty(jqPagemodel.getSearchKey())){
				sql.append(" and info.name_f "+escapeChar(jqPagemodel.getSearchKey()));
			}
			
			if(!StringUtil.isEmpty(jqPagemodel.getSort()) && !StringUtil.isEmpty(jqPagemodel.getOrder())){
				sql.append(" order by info."+jqPagemodel.getSort()+"_f " + jqPagemodel.getOrder());
			}
			PageList<Object> list = dao.getHelperDao().page(jqPagemodel.getPage(),jqPagemodel.getRows(), sql.toString());
			if(null != list && list.size() > 0){
				Object[] objs = null;
				for(Object o : list.getList()){
					Map<String, Object> map = new HashMap<String, Object>();
					objs = (Object[]) o;
					map.put("id", objs[0]);
					map.put("telePhone", objs[1]);
					map.put("name", objs[2]);
					map.put("sex", objs[3]);
					map.put("zhiCheng", objs[4]);
					slist.add(map);
				}
			}
			jqGridData.setPage(list.getPageNo());
			jqGridData.setRows(slist);
			jqGridData.setRecords(list.getTotalCount());
			jqGridData.setTotal(list.getPageCount());
		return jqGridData;
	}
	
	/**
	 * 在登录的时候查询接收的私信有多少条  
	 * 0 is uncheck status, new reuser is not self
	 * @param uId
	 * @param uType
	 * @return
	 */
	public int obtainPrivateCount(Long uId,String uType){
	  List list = dao.getHelperDao().find("select count(1) from privateletter_t p where" +
	  		" ((p.senderid_f = ? and p.sendertype_f = ?) or (p.receiverid_f = ? and p.receivertype_f = ?))" +
	  		" and  p.newreuserid_f is not null and p.status_f = 0 and" +
	  		"((p.newreuserid_f != ? and p.newreusertype_f = ?) or (p.newreusertype_f != ?))",
	  		uId,uType,uId,uType,uId,uType,uType);
	  if(null != list && list.size() > 0){
		  return Integer.parseInt(String.valueOf(list.get(0)));
	  }
	  return 0;
	}
	
	/**
	 * 查询在私信表中接收人是自己、发信人是自己且最近回复不是自己的记录
	 * @param uId
	 * @param uType
	 * @return
	 */
	public List<Map<String, Object>> findNearPrivate(Long uId,String uType){
		List<Map<String, Object>> rlist = new ArrayList<Map<String, Object>>();
		List<Object[]> list = dao.getHelperDao()
				.find("select case p.newreusertype_f when '2' then (select CONCAT(t.name_f,',',t.headpic_f) from teacherinfo_t t where t.id_f = p.newreuserid_f) "
								+ "when '1' then (select CONCAT(s.name_f,',',s.headpic_f) from studentinfo_t s where s.id_f = p.newreuserid_f) end ,left(p.newcontent_f,9), p.newredate_f "
								+ "from privateletter_t p "
								+ "where ((p.newreuserid_f != ? and p.newreusertype_f = ?) "
								+ "or (p.newreusertype_f != ?)) and ((p.senderid_f = ? and p.sendertype_f = ?) or (p.receiverid_f = ? and p.receivertype_f = ?))" +
								" and p.newreuserid_f is not null  order by p.newredate_f desc limit 4",
						uId, uType, uType,uId, uType,uId, uType);
		if (null != list && list.size() > 0) {
			String[] temp = null;
			for (Object[] objects : list) {
				Map<String, Object> map = new HashMap<String, Object>();
				temp = String.valueOf(objects[0]).split(",");
				if (null != temp && temp.length == 2) {
					map.put("name", temp[0]);
					map.put("head", temp[1]);
				} else {
					map.put("name", temp[0]);
					map.put("head", null);
				}
				map.put("content", objects[1]);
				map.put("date", DateUtil.format((Date)objects[2], DateUtil.DATETIME_PATTERN));
				rlist.add(map);
			}
		}
		return rlist;
	}
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-5-5 下午4:31:05
	 *@version 1.0
	 *@Description 推送信息
	 *
	 *@param userId
	 *@param type
	 *@param model
	 *@throws NumberFormatException
	 *@throws ChannelClientException
	 *@throws ChannelServerException
	 *
	 *
	 */
	public void getPushPerson(Long userId,String type,String content) throws NumberFormatException, ChannelClientException, ChannelServerException{
		PushMsgModel model = new PushMsgModel();
		model.setTitle(content);
		model.setDescription(content);
		try {
			if("1".equals(type)){//家长
				StudentInfo findOne = dao.findOne(StudentInfo.class, "id", userId);
				if(null != findOne){
					if(!StringUtil.isEmpty(findOne.getChannelId())
					&& !StringUtil.isEmpty(findOne.getUserId())){
						if(findOne.getAndroid()){
							PushUtil.androidPushUserMsg(Long.parseLong(findOne.getChannelId()), findOne.getUserId(), model);
						}else{
							PushUtil.iosPushUserMsg(Long.parseLong(findOne.getChannelId()), findOne.getUserId(), false, model.getDescription(),null);
						}
					}
				}
			}else if("2".equals(type)){//老师
				TeacherInfo findOne = dao.findOne(TeacherInfo.class, "id", userId);
				if(null != findOne){
					if(!StringUtil.isEmpty(findOne.getChannelId())
					&& !StringUtil.isEmpty(findOne.getUserId())){
						if(findOne.getAndroid()){
							PushUtil.androidPushUserMsg(Long.parseLong(findOne.getChannelId()), findOne.getUserId(), model);
						}else{
							PushUtil.iosPushUserMsg(Long.parseLong(findOne.getChannelId()), findOne.getUserId(), true, model.getDescription(),null);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("推送通知异常");
		}
	}
}
