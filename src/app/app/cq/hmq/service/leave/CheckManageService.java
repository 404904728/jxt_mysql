package app.cq.hmq.service.leave;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import app.cq.hmq.mode.UtilsMode;
import app.cq.hmq.pojo.leave.CheckIn;
import app.cq.hmq.pojo.stuinfo.StudentInfo;
import app.cq.hmq.pojo.teacherinfo.TeacherInfo;

import common.cq.hmq.model.JqGridData;
import common.cq.hmq.model.JqPageModel;
import common.cq.hmq.pojo.sys.Org;

import core.cq.hmq.dao.PageList;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.modal.SessionModal;
import core.cq.hmq.service.BaseService;
import core.cq.hmq.util.tools.StringUtil;

@Service
public class CheckManageService extends BaseService{
	/**
	 * for jqgrid data
	 * @param jqPagemodel
	 * @param sessionModal
	 * @param orgId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JqGridData<CheckIn> findCheckManageInfos(JqPageModel jqPagemodel,Long classId){
		JqGridData jqGridData = new JqGridData<List<?>>();
		if(null == classId){
			return jqGridData;
		}
		StringBuffer hql = new StringBuffer(" from CheckIn where 1=1");
		
		if(!StringUtil.isEmpty(jqPagemodel.getSearchKey())){
			hql.append(" and title "+ escapeChar(jqPagemodel.getSearchKey()));
		}
		hql.append(" and classId ="+ classId);
		
		if(!StringUtil.isEmpty(jqPagemodel.getSort()) && !StringUtil.isEmpty(jqPagemodel.getOrder())){
			hql.append(" order by "+jqPagemodel.getSort()+" "+ jqPagemodel.getOrder());
		}else{
			hql.append(" order by id desc");
		}
		PageList<?> ts = dao.page(jqPagemodel.getPage(), jqPagemodel.getRows(), hql.toString());
		jqGridData.setPage(ts.getPageNo());
		jqGridData.setRows(ts.getList());
		jqGridData.setRecords(ts.getTotalCount());
		jqGridData.setTotal(ts.getPageCount());
		return jqGridData;
	}
	
	public String escapeChar(String str) {
		return "like '%"
				+ str.replace("%", "/%").replace("'", "''").replace("_", "/_")
				+ "%' escape '/' ";
	}

	public Object getStudentInfo(Long sId, Long classId, String sDate,
			String eDate) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(null != sId){
			map.put("0", dao.find("select name from StudentInfo where id= ?",sId).get(0));
		}
		String hql = "select ch.checkdate_f,instr(ch.unarriveids_f,',"+sId+",'),ch.title_f from checkin_t ch where ch.checkdate_f > '"+sDate+"'";
		hql+= "and ch.checkdate_f < '"+eDate+"' and ch.classid_f = "+classId;
		hql += " order by ch.checkdate_f";
		List<Object> list = dao.getHelperDao().find(hql);
		if(null != list && list.size() > 0){
			String[] a =  new String[list.size()];
			Map[] yib =  new Map[list.size()];
			Map[] weib =  new Map[list.size()];
			int i = 0;
			Object[] objs = null;
			for(Object o : list){
				objs = (Object[]) o;
				a[i] = String.valueOf(objs[0]);
				Map<String, Object> map2 = new HashMap<String, Object>();
				if("0".equals(String.valueOf(objs[1]))){
					map2.put("name", String.valueOf(objs[2]));
					map2.put("y", 1);
					yib[i] = map2;
					weib[i] = null;
				}else{
					map2.put("name", String.valueOf(objs[2]));
					map2.put("y", 1);
					yib[i] = null;
					weib[i] = map2;
				}
				i++;
			}
			map.put("1", a);
			map.put("2", yib);
			map.put("3", weib);
		}
		return map;
	}
	
	public Object getClassInfo(Long classId, String sDate,
			String eDate) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(null != classId){
			map.put("0", dao.find("select name from Org where id= ?",classId).get(0));
		}
		List<CheckIn> list = dao.find("from CheckIn where checkDate> ? and checkDate<? and classId = ? order by checkDate",
				sDate, eDate,classId);
		if(null != list && list.size() > 0){
			String[] a =  new String[list.size()];
			Map[] b =  new Map[list.size()];
			int i = 0;
			for(CheckIn ci : list){
				a[i] = ci.getCheckDate();
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("name", ci.getTitle());
				map2.put("y", ci.getAttendance());
				b[i] = map2;
				i++;
			}
			map.put("1", a);
			map.put("2", b);
		}
		return map;
	}

	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-4 上午11:48:28
	 *@version 1.0
	 *@Description 跳转到新增考勤页面
	 *
	 *@param classId
	 *@return
	 *
	 *
	 */
	public ModelAndView initAddCheck(Long classId) {
		ModelAndView view = new ModelAndView("app/checkmanage/addcheck");
		view.addObject("classId", classId);
		return view;
	}
	

	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-4 上午11:48:28
	 *@version 1.0
	 *@Description 跳转到某一次具体考勤页面
	 *
	 *@param classId
	 *@return
	 *
	 *
	 */
	public ModelAndView queryCheckByClass(Long checkId) {
		ModelAndView view = new ModelAndView("app/checkmanage/checkdetail");
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<UtilsMode> resList = new ArrayList<UtilsMode>();
		if(null != checkId){
			String quertSql = "from StudentInfo stu where stu.org.id = " +
					"(select classId from CheckIn ch where ch.id = "+ checkId +")";
			List<StudentInfo> stuList = dao.find(quertSql);
			String checkSql = "from CheckIn ch where ch.id = " + checkId;
			CheckIn checkIn = (CheckIn) dao.findOne(checkSql);
			if(stuList.size() == 0 || null == checkIn){
				resMap.put("flg", AjaxMsg.ERROR);
				view.addObject("checkCount", resMap);
				return view;
			}
			String unArriveIds = checkIn.getUnArriveIds();
			UtilsMode mode = null;
			for (int i = 0; i < stuList.size(); i++) {
				mode = new UtilsMode();
				StudentInfo studentInfo = stuList.get(i);
				if(null != unArriveIds && unArriveIds.contains(","+ studentInfo.getId()+",")){
					mode.setFlgId("0");
					mode.setFlgValue(studentInfo.getName());
				}else{
					mode.setFlgId("1");
					mode.setFlgValue(studentInfo.getName());
				}
				resList.add(mode);
			}
			resMap.put("content", resList);
			resMap.put("title", checkIn.getTitle());
			resMap.put("stitle","(共："+stuList.size() +"人,未到："+ checkIn.getUnCount()+"人)");
			resMap.put("flg", AjaxMsg.SUCCESS);
		}
		view.addObject("checkCount", resMap);
		return view;
	}

	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-4 上午11:48:28
	 *@version 1.0
	 *@Description 跳转到考勤页面初始化加载学生信息
	 *
	 *@param classId
	 *@return
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	public List<StudentInfo> initAddCheckToqueryStu(Long classId) {
		List<StudentInfo> find = dao.find("from StudentInfo stu where status = 0 and stu.org.id = ?", classId);
		return find;
	}

	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-8 上午11:15:29
	 *@version 1.0
	 * @param request 
	 *@Description 保存考勤信息
	 *
	 *@param stuId
	 *@param title
	 * @param unReachCount 
	 * @param stuCount 
	 * @param classId 
	 *@return
	 * @throws UnsupportedEncodingException 
	 *
	 *
	 */
	@Transactional
	public AjaxMsg saveCheckInfos(String stuId, String title, int stuCount, int unReachCount, Long classId) throws UnsupportedEncodingException {
		AjaxMsg msg = new AjaxMsg();
		SessionModal currentSessionModel = currentSessionModel();
		msg.setType(AjaxMsg.ERROR);
		//判断学生ID
		if(null == stuId || "".equals(stuId)){
			msg.setType(AjaxMsg.ERROR);
			return msg;
		}
		//判断主题
		if(null == title || "".equals(title)){
			msg.setType(AjaxMsg.ERROR);
			return msg;
		}
		//判断班级ID
		if(null == classId || "".equals(classId)){
			msg.setType(AjaxMsg.ERROR);
			return msg;
		}
		//判断学生总数
		if(0 == stuCount || "".equals(stuCount)){
			msg.setType(AjaxMsg.ERROR);
			return msg;
		}
		//判断未到学生总数
		if("".equals(unReachCount)){
			msg.setType(AjaxMsg.ERROR);
			return msg;
		}
		Org org = (common.cq.hmq.pojo.sys.Org) dao.findOne("from Org o where o.id = ?", classId);
		String coonTitle = URLDecoder.decode(title, "UTF-8").trim();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		CheckIn ch = new CheckIn();
		ch.setCheckDate(format.format(new Date()));
		ch.setClassId(classId);
		ch.setTitle(coonTitle);
		TeacherInfo tea = new TeacherInfo();
		tea.setId(currentSessionModel.getId());
		ch.setTea(tea);
		ch.setUnArriveIds(stuId);
		ch.setUnCount(unReachCount);
		ch.setHadCount(stuCount - unReachCount);
		double baiy = (stuCount - unReachCount) * 1.0;
        double baiz = stuCount * 1.0;
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(0);
		ch.setAttendance(Integer.parseInt((nf.format(baiy / baiz)).replace("%","")));
		if(org.getmLeader().contains(","+String.valueOf(currentSessionModel.getId())+",")){
			ch.setCheckType(1);
		}else{
			ch.setCheckType(2);
		}
		dao.insert(ch);
		msg.setType(AjaxMsg.SUCCESS);
		return msg;
	}

	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-6-13 下午3:01:52
	 *@version 1.0
	 *@Description 家长查询自己学生的出勤状况
	 *
	 *@param jqPagemodel
	 *@param classId
	 *@return
	 *
	 *
	 */
	public JqGridData<?> queryStuCheck(JqPageModel jqPagemodel) {
		JqGridData<?> jqGridData = new JqGridData<Object>();
		List<Map<String,Object>> appList = new ArrayList<Map<String,Object>>();
		Map<String,Object> apdixMap = null;
		SessionModal sessionModel = currentSessionModel();
		
		String sql = "select che.title_f,che.checkdate_f from " +
				"checkin_t che where che.classid_f = "+ sessionModel.getOrgId() +" and che.unarriveids_f like '%"+ String.valueOf(sessionModel.getId()) +"%'";
		PageList page = dao.getHelperDao().page(jqPagemodel.getPage(), jqPagemodel.getRows(),sql);
		if(null == page){
			return jqGridData;
		}
		List list = page.getList();
		if(null == list || list.size() == 0){
			return jqGridData;
		}
		for (int i = 0; i < list.size(); i++) {
			Object[] object = (Object[]) list.get(i);
			apdixMap = new HashMap<String, Object>();
			apdixMap.put("id", i);
			if(null != object[0]){
				apdixMap.put("title", object[0].toString());
			}
			if(null != object[1]){
				apdixMap.put("checkDate", object[1].toString());
			}
			apdixMap.put("qk", "未到");
			appList.add(apdixMap);
		}
		page.setResult(appList);
		jqGridData.setPage(page.getPageNo());
		jqGridData.setRows(page.getList());
		jqGridData.setRecords(page.getTotalCount());
		jqGridData.setTotal(page.getPageCount());
		return jqGridData;
	}
	
}
