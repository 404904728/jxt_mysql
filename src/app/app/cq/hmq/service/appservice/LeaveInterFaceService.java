package app.cq.hmq.service.appservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.cq.hmq.pojo.leave.LeaveInfo;
import common.cq.hmq.pojo.sys.User;
import core.cq.hmq.dao.PageList;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.service.BaseService;
import core.cq.hmq.util.tools.DateUtil;
import core.cq.hmq.util.tools.StringUtil;
@Service
public class LeaveInterFaceService extends BaseService{

	public PageList<?> obtainLeaveInfos(Long classId,Long uId,String apperType,int ps,int pn){
		PageList<Map<String, Object>> pageList = null;
		if(!StringUtil.isEmpty(apperType)){
			StringBuffer sql = new StringBuffer();
			StringBuffer totalsql = new StringBuffer();
			if(User.STUDENTTYPE.equals(apperType)){
				/** 请假人与申请人是同一个人 */
				sql.append("select s.name_f, 'download/'||a.id_f||'.'||a.suffix_f as touxiang, l.leavereason_f, l.startdate_f, ");
				sql.append("l.enddate_f,l.writedate_f,l.status_f,l.approvestatus_f,l.proposertype_f,(select name_f from studentinfo_t where id_f = l.leaveuser_f) as sname from leaveinfo_t l, ");
				sql.append("studentinfo_t s,attach_t a where a.id_f = s.headpic_f and s.id_f = l.proposerid_f ");
				sql.append("and l.proposerid_f = "+uId+" and l.proposertype_f = 1 order by l.id_f desc");
				pageList = dao.getHelperDao().pageByTotal(pn, ps,sql.toString(),"select count(1) from leaveinfo_t l where l.proposerid_f = "+uId+" and l.proposertype_f = 1");
			}else if(User.TEACHERTYPE.equals(apperType)){
				sql.append("select t.name_f, 'download/'||a.id_f||'.'||a.suffix_f as touxiang, l.leavereason_f, l.startdate_f,");
				sql.append("l.enddate_f,l.writedate_f,l.status_f,l.approvestatus_f,l.proposertype_f,(select name_f from studentinfo_t where id_f = l.leaveuser_f) as sname from leaveinfo_t l,");
				sql.append("teacherinfo_t t,attach_t a where a.id_f = t.headpic_f and t.id_f = l.proposerid_f and l.proposerclass_f = "+classId+" ");
				sql.append("and l.proposerid_f = "+uId+" and l.proposertype_f = 9 order by l.id_f desc");
				pageList = dao.getHelperDao().pageByTotal(pn, ps,sql.toString(),"select count(1) from leaveinfo_t l where l.proposerclass_f = "+
						classId+"and l.proposerid_f = "+uId+" and l.proposertype_f = 9");
			}else if("bzr".equals(apperType)){
				sql.append("select l.id_f, case l.proposertype_f when 1 then (select s.name_f||',download/'||a.id_f||'.'||a.suffix_f from studentinfo_t s,attach_t a ");
				sql.append("where a.id_f = s.headpic_f and s.id_f = l.proposerid_f) ");
				sql.append("when 9 then (select t.name_f||',download/'||a.id_f||'.'||a.suffix_f from teacherinfo_t t,attach_t a ");
				sql.append("where a.id_f = t.headpic_f and t.id_f = l.proposerid_f) end as nameandhead, l.leavereason_f, l.startdate_f, ");
				sql.append("l.enddate_f,l.writedate_f,l.status_f,l.approvestatus_f,l.proposertype_f ,(select name_f from studentinfo_t where id_f = l.leaveuser_f) as sname from leaveinfo_t l ");
				sql.append("where l.receiver_f = "+uId+" and l.proposerclass_f = "+classId+" order by l.id_f desc");
				pageList = dao.getHelperDao().pageByTotal(pn, ps,sql.toString(),"select count(1) from leaveinfo_t l where l.receiver_f = "+uId+" and l.proposerclass_f = "+classId);
			}
			
			if(null != pageList && null != pageList.getList()){
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				Object[] os = null;
				for(Object o : pageList.getList()){
					Map<String, Object> map = new HashMap<String, Object>();
					os = (Object[]) o;
					String[] nameandhead = null;
					if(!"bzr".equals(apperType)){
						map.put("name", os[0]);
						map.put("headPic", os[1]);
						map.put("leaveReason",os[2]);
						map.put("startDate", os[3]);
						map.put("endDate", os[4]);
						map.put("writeDate", os[5]);
						map.put("status", os[6]);
						map.put("approveStatus", os[7]);
						map.put("proposerType", os[8]);
						map.put("leaveUserName", os[9]);
					}else{
						map.put("id", os[0]);
						nameandhead = (String.valueOf(os[1]).split(","));
						map.put("name", nameandhead[0]);
						map.put("headPic", nameandhead[1]);
						map.put("leaveReason",os[2]);
						map.put("startDate", os[3]);
						map.put("endDate", os[4]);
						map.put("writeDate", os[5]);
						map.put("status", os[6]);
						map.put("approveStatus", os[7]);
						map.put("proposerType", os[8]);
						map.put("leaveUserName", os[9]);
					}
					list.add(map);
				}
				pageList.setResult(list);
			}
			return pageList;
		}
		return pageList;
	}
	
	@Transactional
	public AjaxMsg saveLeaveInfo(LeaveInfo lea){
		AjaxMsg  am = new AjaxMsg();
		if(null != lea){
			if(StringUtil.isEmpty(lea.getLeaveReason())){
				am.setType(AjaxMsg.ERROR);
				am.setMsg("申请事由不能为空");
				return am;
			}
			if(StringUtil.isEmpty(lea.getLeaveUser())){
				am.setType(AjaxMsg.ERROR);
				am.setMsg("请假人不能为空");
				return am;
			}
			
			if(StringUtil.isEmpty(lea.getReceiver())){
				am.setType(AjaxMsg.ERROR);
				am.setMsg("接收人不能为空");
				return am;
			}
			if("9".equals(lea.getProposerType())){
				lea.setApproveStatus("已确认");
			}
			lea.setWriteDate(DateUtil.format(new Date(), DateUtil.DATE_PATTERN));
			try {
				dao.insert(lea);
				am.setMsg("保存成功！");
			} catch (Exception e) {
				am.setType(AjaxMsg.ERROR);
				am.setMsg("保存失败！");
				return am;
			}
		}
		return am;
	}
	
	@Transactional
	public AjaxMsg updateLeaveInfo(Long lid,int status){
		AjaxMsg  am = new AjaxMsg();
		if(null != lid){
			String str = null;
			try {
				if(1 == status){
					str = "已确认";
				}else{
					str = "拒绝";
				}
				dao.getHelperDao().excute("update leaveinfo_t  t set t.approvestatus_f = ? ,t.status_f = 1 where t.id_f = ?", str,lid);
				am.setMsg("保存成功！");
			} catch (Exception e) {
				am.setType(AjaxMsg.ERROR);
				am.setMsg("保存失败！");
				return am;
			}
		}
		return am;
	}
}
