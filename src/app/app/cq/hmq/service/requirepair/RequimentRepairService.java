/**
 * Limit
 *
 */
package app.cq.hmq.service.requirepair;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import app.cq.hmq.pojo.equirepair.EquiRepair;
import app.cq.hmq.pojo.teacherinfo.TeacherInfo;

import common.cq.hmq.pojo.sys.Attach;
import common.cq.hmq.service.AttachService;

import core.cq.hmq.dao.PageList;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.modal.SessionModal;
import core.cq.hmq.pojo.LogRecord;
import core.cq.hmq.service.BaseService;

/**
 * @author Administrator
 * 设备报修
 */
@Service
public class RequimentRepairService extends BaseService {

	@Autowired
	private AttachService attachService;
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-3-21 下午4:32:03
	 *@version 1.0
	 *@Description  查询具体一封设备维修的信息
	 *
	 *@param emailId
	 * @param queryFlg 
	 *@return
	 *
	 *
	 */
	@Transactional
	public ModelAndView queryEmailInfos(HttpServletRequest request, Long emailId, String queryFlg) {
		ModelAndView view = new ModelAndView("app/equirepair/emaildetial");
//		EquiRepair equiRepair = (EquiRepair) dao.findOne("from EquiRepair e where e.id = ?", emailId);
		Map<String,Object> equiMap = new HashMap<String,Object>();
		String querySql = "select eq.id_f," +
				"eq.repaircontent_f," +
				"eq.repairstatus_f," +
				"eq.repairtime_f," +
				"eq.repairtitle_f," +
				"(select teaa.name_f from teacherinfo_t teaa where teaa.id_f = eq.receivertea_f) as receivertea," +
				"(select concat(send.name_f,',',send.headpic_f) from teacherinfo_t send where send.id_f = eq.sendtea_f) as sendtea," +
				"eq.atcid_f," +
				"eq.cackcontent_f," +
				"eq.soundid_f from equirepair_t eq where eq.id_f = "+ emailId ;
		List equList = dao.getHelperDao().find(querySql);
		if(null == equList || equList.size() == 0){
			return null;
		}
		for (int i = 0; i < equList.size(); i++) {
			Object[] object = (Object[]) equList.get(i);
			if(null != object[0] || !"".equals(object[0].toString())){
				equiMap.put("id", object[0].toString());
			}
			if(null != object[1] || !"".equals(object[1].toString())){
				equiMap.put("repaircontent", object[1].toString());
			}
			if(null != object[2] || !"".equals(object[2].toString())){
				if("1".equals(object[2].toString())){
					EquiRepair equiRepair = (EquiRepair) dao.findOne("from EquiRepair e where e.id = ?", emailId);
					equiRepair.setRepairStatus(0);
					dao.update(equiRepair);
				}
			}
			if(null != object[3] || !"".equals(object[3].toString())){
				equiMap.put("repairtime", object[3].toString());
			}
			if(null != object[4] || !"".equals(object[4].toString())){
				equiMap.put("repairtitle", object[4].toString());
			}
			if(null == object[5]){
				equiMap.put("receivertea", "");
			}else{
				equiMap.put("receivertea", object[5].toString());
			}
			
			if(null != object[6] || !"".equals(object[6].toString())){
				String[] split = object[6].toString().split(",");
				equiMap.put("sendTeaName", split[0]);
				equiMap.put("sendHeadpic", split[1]);
			}
			if(null == object[7]){
				equiMap.put("atcid", "");
			}else{
				equiMap.put("atcid", object[7].toString());
			}
			if(null != object[8] || !"".equals(object[8].toString())){
				equiMap.put("cackcontent", object[8].toString());
			}
			if(null == object[9]){
				equiMap.put("soundid", "");
			}else{
				equiMap.put("soundid", object[9].toString());
			}
		}
		
//		if(equiRepair.getRepairStatus() == 1){
//			equiRepair.setRepairStatus(0);
//		}
//		dao.update(equiRepair);
		view.addObject("equRepair", equiMap);
		view.addObject("queryFlg", queryFlg);
		logRecord(LogRecord.QUERY, "查询具体一封设备维修的信息");
		return view;
	}

	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-3-21 上午10:50:31
	 *@version 1.0
	 *@Description 查询设备报修收件箱
	 *
	 *@param request
	 * @param recommon 
	 * @param status 
	 * @param page 
	 * @param rows 
	 *@return
	 * @throws UnsupportedEncodingException 
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public Map<String, Object> queryReceiveEmail(HttpServletRequest request, int rows, int page, String status, String recommon) throws UnsupportedEncodingException {
		SessionModal sessionModal = (SessionModal) request.getSession().getAttribute("sessionModal");
		PageList<EquiRepair> allresultList = null;
		List<EquiRepair> countList = null;
		String coonTitle = "";
		if(null != recommon && !"".equals(recommon)){
			coonTitle = URLDecoder.decode(recommon, "UTF-8").trim();
		}
		
		if("1".equals(status)){//收件箱
			String sql1 = "from EquiRepair e where 1=1 ";
			String sql2 = "from EquiRepair e where e.repairStatus = 1 and e.acceptBoxStatus = 1 ";
			if(!"".equals(coonTitle)){
				sql1 += " and e.repairTitle like '%" + coonTitle + "%' ";
				sql2 += " and e.repairTitle like '%" + coonTitle + "%' ";
			}
			countList = dao.find(sql2);
			if(countList.size() == 0){
				sql1 += " and e.acceptBoxStatus = 1 order by e.repairTime desc";
			}else{
				sql1 += " and e.acceptBoxStatus = 1 order by e.repairStatus desc";
			}
			allresultList = dao.page((page-1), rows, sql1);
		}else if("2".equals(status)){//发件箱
			String sql1 = "from EquiRepair e where e.sendTea.id = ? ";
			String sql2 = "from EquiRepair e where e.sendTea.id = ? and e.sendBoxStatus = 1 ";
			if(!"".equals(coonTitle)){
				sql1 += " and e.repairTitle like '%" + coonTitle + "%' ";
				sql2 += " and e.repairTitle like '%" + coonTitle + "%' ";
			}
			sql1 += " and e.sendBoxStatus = 1 order by e.id desc";
			allresultList = dao.page((page-1), rows, sql1, sessionModal.getId());
			countList = dao.find(sql2, sessionModal.getId());
		}
		Map<String, Object> resMap = new HashMap<String, Object>();
		//总记录数
		resMap.put("totalCount", allresultList.getTotalCount());
		//总页数
		resMap.put("totalPage", allresultList.getPageCount());
		//当前页
		resMap.put("pageNo", allresultList.getPageNo());
		//数据
		resMap.put("equList", allresultList.getList());
		//未读邮件个数
		resMap.put("emailCount", countList.size());
		logRecord(LogRecord.QUERY, "查询设备报修收件箱和发件箱的报修记录");
		return resMap;
	}


	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-3-21 下午5:09:28
	 *@version 1.0
	 * @param request 
	 *@Description 初始化填写上报邮件
	 *
	 *@return
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public ModelAndView writeRepairEmail(HttpServletRequest request) {
		ModelAndView view = new ModelAndView("app/equirepair/equirepairsendform");
		SessionModal sessionModal = (SessionModal) request.getSession().getAttribute("sessionModal");
		List<TeacherInfo> teaList = dao.find("from TeacherInfo t where t.id != ?", sessionModal.getId());
		view.addObject("teaList", teaList);
		logRecord(LogRecord.QUERY, "初始化填写上报邮件");
		return view;
	}


	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-3-22 下午1:43:53
	 *@version 1.0
	 *@Description 发送设备维修通知
	 *
	 *@param request
	 *@param repair
	 *@return
	 *
	 *
	 */
	@Transactional
	public Map<String, Object> submitFormInfo(HttpServletRequest request,
			EquiRepair repair) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		SessionModal sessionModal = currentSessionModel();
		AjaxMsg msg = attachService.upload(request);
		if(!"保存成功".equals(msg.getMsg())){
			resMap.put("flg", 1);
			return resMap;
		}
		if(null == repair){
			resMap.put("flg", 0);
			return resMap;
		}
		repair.setRepairStatus(1);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format2 = format.format(new Date());
		repair.setRepairTime(format2);
//		Attach ac = new Attach();
//		ac.setId(msg.getId());
//		repair.setAtc(ac);
		repair.setAtcid(msg.getId());
		TeacherInfo send = new TeacherInfo();
		send.setId(sessionModal.getId());
		repair.setSendTea(send);
		repair.setCackContent("未处理");
		repair.setSendBoxStatus(1);
		repair.setAcceptBoxStatus(1);
		repair.setRepairTitle(repair.getLevel());
		
		dao.insert(repair);
		Attach attach = (Attach) dao.findOne("from Attach at where at.id = ?", msg.getId());
		attach.setRelType("equiRepair");
		attach.setRelId(repair.getId());
		dao.update(attach);
		resMap.put("flg", 2);
		logRecord(LogRecord.ADD, "添加设备维修通知");
		return resMap;
	}


	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-3-25 上午9:19:15
	 *@version 1.0
	 *@Description 删除设备维修通知
	 *
	 *@param emailId
	 * @param status 
	 *@return
	 *
	 *
	 */
	@Transactional
	public AjaxMsg deleteEuqiEmail(Long emailId, String status) {
		AjaxMsg msg = new AjaxMsg();
		if(null == emailId){
			msg.setType(AjaxMsg.ERROR);
		}else{
			if("1".equals(status)){//收件箱
				EquiRepair equiRepair = (EquiRepair) dao.findOne("from EquiRepair e where e.id = ?", emailId);
				equiRepair.setAcceptBoxStatus(0);
				dao.update(equiRepair);
				msg.setType(AjaxMsg.SUCCESS);
			}
			if("2".equals(status)){//发件箱
				EquiRepair equiRepair = (EquiRepair) dao.findOne("from EquiRepair e where e.id = ?", emailId);
				equiRepair.setSendBoxStatus(0);
				dao.update(equiRepair);
				msg.setType(AjaxMsg.SUCCESS);
			}
		}
		logRecord(LogRecord.DELETE, "删除设备维修通知");
		return msg;
	}


	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-3-25 上午9:19:15
	 *@version 1.0
	 * @param request 
	 *@Description 更改设备维修状态
	 *
	 *@param emailId
	 *@return
	 *
	 *
	 */
	@Transactional
	public AjaxMsg updateBackStatus(HttpServletRequest request, Long emailId, String state) {
		SessionModal sessionModal = (SessionModal) request.getSession().getAttribute("sessionModal");
		EquiRepair equiRepair = (EquiRepair) dao.findOne("from EquiRepair e where e.id = ? ", emailId);
		AjaxMsg msg = new AjaxMsg();
		if(null == emailId || null == state || "".equals(state)){
			msg.setType(AjaxMsg.ERROR);
			return msg;
		}
		if("1".equals(state)){
			equiRepair.setCackContent(" 已处理： 不需维修 ");
		}
		if("2".equals(state)){
			equiRepair.setCackContent(" 已处理： 维修完成 ");
		}
		TeacherInfo teacher = new TeacherInfo();
		teacher.setId(sessionModal.getId());
		equiRepair.setReceiverTea(teacher);
		dao.update(equiRepair);
		msg.setType(AjaxMsg.SUCCESS);
		logRecord(LogRecord.UPDATE, "更改设备维修状态");
		return msg;
	}
	
}
