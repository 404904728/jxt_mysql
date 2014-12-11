package app.cq.hmq.service.teachertop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import app.cq.hmq.pojo.teacherinfo.TeacherInfo;
import app.cq.hmq.pojo.teachertopic.TeacherTopic;
import app.cq.hmq.pojo.teachertopic.teachertopicre;

import common.cq.hmq.model.JqGridData;
import common.cq.hmq.model.JqPageModel;
import common.cq.hmq.pojo.sys.Attach;
import common.cq.hmq.pojo.sys.User;
import common.cq.hmq.service.AttachService;

import core.cq.hmq.dao.PageList;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.modal.SessionModal;
import core.cq.hmq.service.BaseService;
import core.cq.hmq.util.tools.DateUtil;
import core.cq.hmq.util.tools.StringUtil;

@Service
public class TeacherTopicService extends BaseService {

	@Autowired
	private AttachService attachService;

	public JqGridData<?> findAllTopic(JqPageModel pageModel, Long classId) {
		JqGridData jqGridData = new JqGridData<List<?>>();
		if (null != classId) {
			StringBuffer hql = new StringBuffer();
			hql.append("from TeacherTopic where 1=1");
			if (!StringUtil.isEmpty(pageModel.getSearchKey())) {
				hql.append(" and title " + escapeChar(pageModel.getSearchKey()));
			}
			hql.append(" and classId = " + classId);

			if (!StringUtil.isEmpty(pageModel.getSort())
					&& !StringUtil.isEmpty(pageModel.getOrder())) {
				hql.append(" order by " + pageModel.getSort() + " "
						+ pageModel.getOrder());
			}else{
				hql.append(" order by id desc");
			}
			PageList<TeacherTopic> ts = dao.page(pageModel.getPage(), pageModel.getRows(),
					hql.toString());
			jqGridData.setPage(ts.getPageNo());
			for (TeacherTopic teacherTopic : ts.getList()){
				if(null != teacherTopic && teacherTopic.getContent().length() > 11){
					teacherTopic.setContent(teacherTopic.getContent().substring(0, 10)+"...");
				}
			}
			jqGridData.setRows(ts.getList());
			jqGridData.setRecords(ts.getTotalCount());
			jqGridData.setTotal(ts.getPageCount());
		}
		return jqGridData;
	}
		
	public Map<String, Object> findTeacherTopicByid(Long id,int start, int end){
			Map<String, Object> rmap = new HashMap<String, Object>();
			List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
			if(start == 0){
				TeacherTopic teacherTopic = (TeacherTopic) dao.findOne("from TeacherTopic where id = ?", id);
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
				}
			}
			
			StringBuffer sb = new StringBuffer();
			sb.append("select re.recontent_f,re.redate_f,case re.reusertype_f when '1'");
			sb.append(" then (select concat(name_f,',',headpic_f) from studentinfo_t where id_f = re.reuserid_f) ");
			sb.append("when '9' then (select concat(name_f,',',headpic_f) from teacherinfo_t where id_f = re.reuserid_f) ");
			sb.append(" end as username,re.voiceid_f from teachertopicre_t re where re.topicid_f = ? order by re.id_f limit ?,? ");
			List list =	 dao.getHelperDao().find(sb.toString(),id,start,end-start);
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
						map.put("voice", objs[3]);
					    map.put("pictrueids", null);
						listMap.add(map);
				}
			}
			rmap.put("1", listMap);
			return rmap;
		}

	public String escapeChar(String str) {
		return "like '%"
				+ str.replace("%", "/%").replace("'", "''").replace("_", "/_")
				+ "%' escape '/' ";
	}

	/**
	 * 
	 * @title
	 * @author Limit
	 * @date 2014-4-10 下午1:31:30
	 * @version 1.0
	 * @Description 教师心语新增
	 * 
	 * @param classId
	 * @return
	 * 
	 * 
	 */
	@Transactional
	public Map<String, Object> saveTeacherTopic(HttpServletRequest request,
			TeacherTopic pic) {
		AjaxMsg msg = null;
		Map<String, Object> resMap = new HashMap<String, Object>();
		if((request instanceof MultipartHttpServletRequest)){
			msg = attachService.uploadImage(request);
		}else{
			msg = new AjaxMsg();
		}
		if(null == pic){
			resMap.put("flg", 0);
			return resMap;
		}
		
//		if(null == msg.getMsgId() || "".equals(msg.getMsgId())){
//			resMap.put("flg", 1);
//			return resMap;
//		}
		if(0 != msg.getType()){
			resMap.put("msg", msg.getMsg());
			resMap.put("flg", 0);
			return resMap;
		}
		if(null != msg.getMsgId() && !"".equals(msg.getMsgId())){
			pic.setPictrueIds(msg.getMsgId());
		}
		pic.setReCount(0);
		SessionModal currentSessionModel = currentSessionModel();
		TeacherInfo tea = new TeacherInfo();
		tea.setId(currentSessionModel.getId());
		pic.setTea(tea);
		pic.setWriteDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		dao.insert(pic);
		if(null != msg.getMsgId() && !"".equals(msg.getMsgId())){
			String[] split = msg.getMsgId().split(",");
			for (int i = 0; i < split.length; i++) {
				Attach a = dao.get(Attach.class, Long.parseLong(split[i]));
				if(null != a){
					a.setRelId(pic.getId());
					a.setRelType("teacherTopic");
				}
			}
		}
		resMap.put("flg", 2);
		return resMap;
		}
	
	    /**
		 * 回复话题  没回复一次就 增加回复数一次
		 * @param id
		 * @param content
		 * @param sm
		 * @return
		 */
		@Transactional
		public AjaxMsg reTeacherTopicByid(Long id,String content,SessionModal sm){
			AjaxMsg am = new AjaxMsg();
			if(null != id){
				teachertopicre re = new teachertopicre();
				re.setTopicId(id);
				re.setReUserId(sm.getId());
				re.setReContent(content);
				if(User.STUDENTTYPE.equals(sm.getUserType())){
					re.setReUserType("1");
				}else{
					re.setReUserType("9");
				}
				re.setReDate(DateUtil.format(new Date(), DateUtil.DATETIME_PATTERN));
				try {
					dao.insert(re);
					dao.getHelperDao().excute("update TeacherTopic_t set reCount_f = reCount_f+1 where id_f = ?", id);
					am.setMsg("发送成功！");
					return am;
				} catch (Exception e) {
					am.setMsg("发送失败！");
					am.setType(AjaxMsg.ERROR);
					return am;
				}
			}
			return am;
		}
		
}
