/**
 * Limit
 *
 */
package app.cq.hmq.service.cjmanage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.quartz.jobs.ee.mail.SendMailJob;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

import app.cq.hmq.mode.ScoreQueryMode;
import app.cq.hmq.mode.UtilsMode;
import app.cq.hmq.pojo.cjmanage.CJManage;
import app.cq.hmq.pojo.cjmanage.Score;
import app.cq.hmq.pojo.cjmanage.ScoreContentMapping;
import app.cq.hmq.pojo.cjmanage.ScoreSubjectMapping;
import app.cq.hmq.pojo.notice.SmsState;
import app.cq.hmq.pojo.stuinfo.StudentInfo;
import app.cq.hmq.pojo.subject.SubjectInfo;
import app.cq.hmq.pojo.subject.SubjectMapping;

import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.jxt.PushUtil;
import com.baidu.yun.jxt.model.PushMsgModel;

import common.cq.hmq.model.JqGridData;
import common.cq.hmq.model.JqPageModel;
import common.cq.hmq.pojo.sys.Attach;
import common.cq.hmq.pojo.sys.Org;
import common.cq.hmq.pojo.sys.User;
import common.cq.hmq.service.AttachService;
import common.cq.hmq.util.SendSMS;
import core.cq.hmq.dao.PageList;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.modal.SessionModal;
import core.cq.hmq.pojo.LogRecord;
import core.cq.hmq.service.BaseService;
import core.cq.hmq.util.tools.DateUtil;
import core.cq.hmq.util.tools.FileUtil;
import core.cq.hmq.util.tools.ResourceUtil;
import core.cq.hmq.util.tools.StringUtil;

/**
 * @author Administrator
 * 
 */
@Service
public class ScoreManageService extends BaseService {

	@Resource
	AttachService attachService;
	private static final String NUMBER_PATTERN = "(^\\d+)(\\.[\\d]{1,3})?"; 

	/**
	 * 
	 * @title
	 * @author Limit
	 * @date 2014-3-17 下午3:38:29
	 * @version 1.0
	 * @Description 查询具体某一个班级中考试的科目
	 * 
	 * 
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	public List<UtilsMode> querySubInfo(HttpServletRequest request,
			String status, Long classId) {
		if (null != status && !"".equals(status)) {
			if ("1".equals(status)) {
				List<UtilsMode> modeList = new ArrayList<UtilsMode>();
				SessionModal sessionModal = (SessionModal) request.getSession()
						.getAttribute("sessionModal");
				List<SubjectMapping> subjectMapping = dao
						.find("from SubjectMapping s where s.org.id = (select org.id from StudentInfo where status = 0 and id=?)",
								sessionModal.getId());
				UtilsMode mode = null;
				for (int i = 0; i < subjectMapping.size(); i++) {
					mode = new UtilsMode();
					SubjectInfo subjectInfo = subjectMapping.get(i)
							.getSubjectInfo();
					mode.setFlgId(subjectInfo.getId() + "");
					mode.setFlgValue(subjectInfo.getName());
					modeList.add(mode);
				}
				logRecord(LogRecord.QUERY, "家长查询自己孩子所在的班级的科目");
				return modeList;
			} else if ("2".equals(status)) {
				if (null != classId) {
					//List find = dao.getHelperDao().find("select sc.colname_f, sub.name_f from scoresubjectmapping_t sc , subjectinfo_t sub, subjectmapping_t adssad where sc.subject_f = sub.id_f and sub.id_f = adssad.subjectinfo_f and adssad.org_f = ?",classId);
					List find = dao.getHelperDao().find("select stu.id_f,stu.name_f from studentinfo_t stu where stu.org_f = " + classId + " and stu.status_f = 0");
					List<UtilsMode> modeList = new ArrayList<UtilsMode>();
					UtilsMode mode = null;
					for (int i = 0; i < find.size(); i++) {
						mode = new UtilsMode();
						Object[] object = (Object[]) find.get(i);
						mode.setFlgId(object[1].toString() + "");
						mode.setFlgValue(object[1].toString());
						modeList.add(mode);
					}
					logRecord(LogRecord.QUERY, "老师查询具体一个班级中的科目");
					return modeList;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @title
	 * @author Limit
	 * @date 2014-3-11 上午11:27:35
	 * @version 1.0
	 * @Description 家长登录后获取学生班级名称
	 * 
	 * @param request
	 * 
	 * 
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
			Long id = studentInfo.getOrg().getId();
			infoMap.put("flg", 1);
			infoMap.put("value", calssName + "," + id);
		} else {
			infoMap.put("flg", 2);
		}
		logRecord(LogRecord.QUERY, "家长登录后获取学生班级名称");
		return infoMap;
	}

	/**
	 * 
	 * @title
	 * @author Limit
	 * @date 2014-3-17 下午3:38:29
	 * @version 1.0
	 * @param classId
	 * @Description 查询具体某一个班级中考试的科目
	 * 
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<UtilsMode> queryksnr(HttpServletRequest request, Long subID,
			Long classId) {
		if (null != subID && null != classId) {
			List cjList = dao
					.find("select c.ksnr from CJManage c where c.org.id = ? and c.sub.id = ? group by c.ksnr",
							classId, subID);
			List<UtilsMode> modeList = new ArrayList<UtilsMode>();
			UtilsMode mode = null;
			for (int i = 0; i < cjList.size(); i++) {
				mode = new UtilsMode();
				mode.setFlgId((String) cjList.get(i));
				mode.setFlgValue((String) cjList.get(i));
				modeList.add(mode);
			}
			logRecord(LogRecord.QUERY, "查询具体某一个班级中考试的科目");
			return modeList;
		}
		return null;
	}

	/**
	 * 
	 * @title
	 * @author Limit
	 * @date 2014-3-11 上午11:27:35
	 * @version 1.0
	 * @Description 点击成绩管理后初始化班级下拉框
	 * 
	 * @param request
	 * 
	 * 
	 */
	@SuppressWarnings({ "unchecked", "null" })
	@Transactional
	public List<UtilsMode> queryClassInfo(HttpServletRequest request) {
		SessionModal sessionModal = (SessionModal) request.getSession()
				.getAttribute("sessionModal");
		Long id = sessionModal.getId();
		List<UtilsMode> modeList = new ArrayList<UtilsMode>();
//		List<Org> resList = dao
//				.find("from Org where id in (select org.id from SubjectMapping where teacher = "
//						+ id + ")");
		List resList = dao.getHelperDao().find("select distinct(o.id_f),o.name_f " +
				"from org_t o where o.id_f  in (select sm.org_f from subjectmapping_t sm where sm.teacher_f = " + id + ") " +
				"union select o.id_f,o.name_f from org_t o where instr(o.mleader_f, concat(',',"+ id +",',')) > 0 and o.type_f = 3");
		if(null == resList || resList.size() == 0){
			return modeList;
		}
		if (null != resList || resList.size() != 0) {
			
			UtilsMode mode = null;
			Object[] object = null;
			for (int i = 0; i < resList.size(); i++) {
				object = (Object[]) resList.get(i);
				if(null != object){
					mode = new UtilsMode();
					mode.setFlgId(object[0].toString());
					mode.setFlgValue(object[1].toString());
					modeList.add(mode);
				}
			}
			logRecord(LogRecord.QUERY, "点击成绩管理后初始化班级下拉框");
			return modeList;
		}
		return null;
	}

	/**
	 * 
	 * @title
	 * @author Limit
	 * @date 2014-3-11 下午1:30:20
	 * @version 1.0
	 * @Description 家长或者老师查询具体某一班级的所有学生信息成绩
	 * 
	 * @param request
	 * @param id
	 * @param page
	 * @param rows
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * 
	 * 
	 */
	@SuppressWarnings("rawtypes")
	@Transactional
	public JqGridData<ScoreQueryMode> getStuInfoToClass(
			HttpServletRequest request, Long classId, String kaoshiSub,
			String kaoshinr, JqPageModel mode, String status)
			throws JsonParseException, JsonMappingException, IOException {

		JqGridData<ScoreQueryMode> jqdt = new JqGridData<ScoreQueryMode>();
		List<ScoreQueryMode> testIder = new ArrayList<ScoreQueryMode>();
		ScoreQueryMode scoremode = null;
		PageList page = null;
//		String grand = "";
//		String classValue = "";
		String grandClass = "";
		if (null != classId && !"".equals(classId)) {
			Org org = (common.cq.hmq.pojo.sys.Org) dao.findOne(
					"from Org o where o.id = ?", classId);
			grandClass = org.getCode();
//			if(null == org){
//				return null;
//			}
//			if(null == org.getParent()){
//				return null;
//			}
//			if(null == org.getParent().getParent()){
//				return null;
//			}
//			String name = org.getParent().getParent().getName();
//			if(null == name || "".equals(name)){
//				return null;
//			}
//			// 2011级3班
//			if (org.getName().length() == 8) {
//				grand = org.getName().substring(2, 4);
//				classValue = org.getName().substring(5, 7);
//			} else if (org.getName().length() == 7) {
//				grand = org.getName().substring(2, 4);
//				classValue = org.getName().substring(5, 6);
//			}
//			if ("小学".equals(name)) {
//				int i = Integer.parseInt(grand) + 6;
//				if (org.getName().length() == 8) {
//					grandClass = i + classValue;
//				} else if (org.getName().length() == 7) {
//					grandClass = i + "0" + classValue;
//				}
//			} else if ("初中".equals(name)) {
//				int i = Integer.parseInt(grand) + 3;
//				if (org.getName().length() == 8) {
//					grandClass = i + classValue;
//				} else if (org.getName().length() == 7) {
//					grandClass = i + "0" + classValue;
//				}
//			} else if ("高中".equals(name)) {
//				int i = Integer.parseInt(grand) + 3;
//				if (org.getName().length() == 8) {
//					grandClass = i + classValue;
//				} else if (org.getName().length() == 7) {
//					grandClass = i + "0" + classValue;
//				}
//			}

			if ("2".equals(status)) {
				String ksnr = "";
				if (null != classId && null != kaoshiSub && null != kaoshinr
						&& !"".equals(kaoshinr)) {

				}
				try {
					ksnr = URLDecoder.decode(kaoshinr, "UTF-8").trim();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				String sor = "order by s.score1_f desc ";
				if (null != mode.getSort() && !"".equals(mode.getSort())) {
					sor = " order by s.s" + mode.getSort() + "_f "
							+ mode.getOrder();
				}
				String quertSql = "select r.title_f, r.sno_f, r.stuname, "
						+ "scm.contentdata_f, r.total from "
						+ "(select s.sno_f, s.sname_f,s.title_f,s.sclass_f,"
						+ "s.sname_f as stuname, case s."
						+ kaoshiSub
						+ "_f when -1 then '缺考' "
						+ "else to_char(s."
						+ kaoshiSub
						+ "_f) end as total from score_t s where s.title_f like '%"
						+ ksnr
						+ "%'"
						+ "and s.sclass_f = '"
						+ grandClass
						+ "' "
						+ sor
						+ ") r left join scorecontentmapping_t scm "
						+ "on scm.title_f = concat(r.title_f,r.sclass_f,r.stuname) and scm.colname_f = '"
						+ kaoshiSub + "'";
				page = dao.getHelperDao().page(mode.getPage(), mode.getRows(),
						quertSql);
				List list = page.getList();
				// ScoreQueryMode
				if (null == list || list.size() == 0) {
					return jqdt;
				}
				for (int i = 0; i < list.size(); i++) {
					Object[] object = (Object[]) list.get(i);
					scoremode = new ScoreQueryMode();

					scoremode.setTitle(object[0].toString());
					scoremode.setNo(object[1].toString());
					scoremode.setName(object[2].toString());
					if(null != object[3] && !"".equals(object[3].toString())){
						scoremode.setScoreA(object[3].toString().replace("{", "")
								.replace("}", "").replace("\"", "")
								.replace(".0", ""));
					}else{
						scoremode.setScoreA("无");
					}
					if(null != object[4] && !"".equals(object[4].toString())){
						scoremode.setTotalScore(object[4].toString());
					}else{
						scoremode.setTotalScore("无");
					}
					testIder.add(scoremode);
				}
				page.setResult(testIder);
			}
		}
		if ("1".equals(status)) {
			SessionModal sessionModal = (SessionModal) request.getSession()
					.getAttribute("sessionModal");
			String subSql = "select scm.colname_f,sub.name_f from scoresubjectmapping_t scm,subjectmapping_t sm,subjectinfo_t sub where" +
					" scm.subject_f = sub.id_f and sm.subjectinfo_f = sub.id_f and sm.org_f = '"+sessionModal.getOrgId()+"' order by scm.colname_f asc";
			List subList = dao.getHelperDao().find(subSql);
			List<String>  subjectNameList = new ArrayList<String>(); 
			String colNameStrs = "";
			if (subList.size() != 0) {
				Object[] object =  null;
				for (int i = 0; i < subList.size(); i++) {
					object = (Object[]) subList.get(i);
					colNameStrs += "scro." + object[0].toString() + "_f,";
					subjectNameList.add(object[1].toString());
				}
			}else{
				return jqdt;
			}
			String querySql = "select scro.title_f,scro.sno_f,scro.sname_f,"
					+ colNameStrs+ "scro.totalscore_f," 
					+ "scro.classorder_f,scro.classasc_f,scro.gradeorder_f,scro.gradeasc_f "
					+ "from score_t scro where scro.sclass_f = '" + grandClass
					+ "' and scro.sname_f = '" + sessionModal.getName()
					+ "' and scro.publishStatus_f = 1 order by scro.importdate_f desc";
			page = dao.getHelperDao().page(mode.getPage(), mode.getRows(),
					querySql);
			List list = page.getList();
			if (null == list || list.size() == 0) {
				return jqdt;
			}
			String subValue = "";
			for (int i = 0; i < list.size(); i++) {
				Object[] object = (Object[]) list.get(i);
				scoremode = new ScoreQueryMode();
				if (null != object[0]) {
					scoremode.setTitle(object[0].toString());
					
				}
				if (null != object[1]) {
					scoremode.setNo(object[1].toString());
				}
				if (null != object[2]) {
					scoremode.setName(object[2].toString());
				}
				
				for (int j = 0; j < subjectNameList.size(); j++) {
					if(!StringUtil.isEmpty(object[3+j])){
						subValue += subjectNameList.get(j) + ":"
						+ object[3+j].toString() + ", ";
					}
				}
				/*if (null != object[3] && !"".equals(object[3].toString())) {
					subValue += subMap.get("Score1") + ":"
							+ object[3].toString() + ", ";
				}
				if (null != object[4] && !"".equals(object[4].toString())) {
					subValue += subMap.get("Score2") + ":"
							+ object[4].toString() + ", ";
				}
				if (null != object[5] && !"".equals(object[5].toString())) {
					subValue += subMap.get("Score3") + ":"
							+ object[5].toString() + ", ";
				}
				if (null != object[6] && !"".equals(object[6].toString())) {
					subValue += subMap.get("Score4") + ":"
							+ object[6].toString() + ", ";
				}
				if (null != object[7] && !"".equals(object[7].toString())) {
					subValue += subMap.get("Score5") + ":"
							+ object[7].toString() + ", ";
				}
				if (null != object[8] && !"".equals(object[8].toString())) {
					subValue += subMap.get("Score6") + ":"
							+ object[8].toString() + ", ";
				}
				if (null != object[9] && !"".equals(object[9].toString())) {
					subValue += subMap.get("Score7") + ":"
							+ object[9].toString() + ", ";
				}
				if (null != object[10] && !"".equals(object[10].toString())) {
					subValue += subMap.get("Score8") + ":"
							+ object[10].toString() + ", ";
				}
				if (null != object[11] && !"".equals(object[11].toString())) {
					subValue += subMap.get("Score9") + ":"
							+ object[11].toString() + ", ";
				}
				if (null != object[12] && !"".equals(object[12].toString())) {
					subValue += subMap.get("Score10") + ":"
							+ object[12].toString() + ", ";
				}
				if (null != object[13] && !"".equals(object[13].toString())) {
					subValue += subMap.get("Score11") + ":"
							+ object[13].toString() + ", ";
				}
				if (null != object[14] && !"".equals(object[14].toString())) {
					subValue += subMap.get("Score12") + ":"
							+ object[14].toString() + ", ";
				}
				if (null != object[15] && !"".equals(object[15].toString())) {
					subValue += subMap.get("Score13") + ":"
							+ object[15].toString() + ", ";
				}
				if (null != object[16] && !"".equals(object[16].toString())) {
					subValue += subMap.get("Score14") + ":"
							+ object[16].toString() + ", ";
				}
				if (null != object[17] && !"".equals(object[17].toString())) {
					subValue += subMap.get("Score15") + ":"
							+ object[17].toString() + ", ";
				}
				if (null != object[18] && !"".equals(object[18].toString())) {
					subValue += subMap.get("Score16") + ":"
							+ object[18].toString() + ", ";
				}
				if (null != object[19] && !"".equals(object[19].toString())) {
					subValue += subMap.get("Score17") + ":"
							+ object[19].toString() + ", ";
				}
				if (null != object[20] && !"".equals(object[20].toString())) {
					subValue += subMap.get("Score18") + ":"
							+ object[20].toString() + ", ";
				}*/
				scoremode.setScoreA(subValue);
				subValue = "";

				if (!StringUtil.isEmpty(object[3+subjectNameList.size()])) {
					scoremode.setTotalScore(object[3+subjectNameList.size()].toString());
				}
				if (!StringUtil.isEmpty(object[4+subjectNameList.size()])) {
					scoremode.setClassOrder(object[3+subjectNameList.size()+1].toString());
				}
				if (!StringUtil.isEmpty(object[5+subjectNameList.size()])) {
					scoremode.setClassAsc(object[5+subjectNameList.size()].toString());
				}
				if (!StringUtil.isEmpty(object[6+subjectNameList.size()])) {
					scoremode.setDrandOrder(object[6+subjectNameList.size()].toString());
				}
				if (!StringUtil.isEmpty(object[7+subjectNameList.size()])) {
					scoremode.setDrandAsc(object[7+subjectNameList.size()].toString());
				}
				testIder.add(scoremode);
			}
			page.setResult(testIder);
		}
		jqdt.setPage(page.getPageNo());
		jqdt.setRecords(page.getTotalCount());
		jqdt.setRows(page.getList());
		jqdt.setTotal(page.getPageCount());
		logRecord(LogRecord.QUERY, "家长或者老师查询具体某一班级的所有学生信息成绩");
		return jqdt;

	}

	/**
	 * 
	 * @title
	 * @author Limit
	 * @date 2014-3-11 下午1:30:20
	 * @version 1.0
	 * @Description 添加学生成绩
	 * 
	 * @param request
	 * @param id
	 * @param page
	 * @param rows
	 * @return
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public ModelAndView addStuScore(HttpServletRequest request) {
		ModelAndView mvMode = new ModelAndView("app/cjmanage/addstuscore");
		SessionModal sessionModal = (SessionModal) request.getSession()
				.getAttribute("sessionModal");
		Long id = sessionModal.getId();
		List<Org> resList = dao
				.find("from Org where id in (select org.id from SubjectMapping where teacher = ?)",
						id);
		if (null != resList || resList.size() != 0) {
			List<UtilsMode> modeList = new ArrayList<UtilsMode>();
			UtilsMode mode = null;
			for (int i = 0; i < resList.size(); i++) {
				mode = new UtilsMode();
				mode.setFlgId(resList.get(i).getId() + "");
				mode.setFlgValue(resList.get(i).getName());
				modeList.add(mode);
			}
			mvMode.addObject("classInfo", modeList);
		}
		return mvMode;
	}

	/**
	 * 
	 * @title
	 * @author Limit
	 * @date 2014-3-11 下午1:30:20
	 * @version 1.0
	 * @param ksDate
	 * @Description 验证新增的成绩中内同是否存在
	 * 
	 * @param request
	 * @param id
	 * @param page
	 * @param rows
	 * @return
	 * 
	 * 
	 */
	@Transactional
	public List<UtilsMode> isExistForNeiRong(Long classId, Long subId,
			String nr, String ksDate) {
		List<UtilsMode> modeList = new ArrayList<UtilsMode>();
		UtilsMode mode = null;
		try {
			String ksnr = URLDecoder.decode(nr, "UTF-8").trim();
			String replace = ksDate.replace("-", "/");
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			Date parse = format.parse(replace);
			String sql = "from CJManage c where c.org.id = " + classId
					+ " and c.sub.id = " + subId + " and c.ksnr = '" + ksnr
					+ "' and c.kSTime = to_date('" + format.format(parse)
					+ "','yyyy/MM/dd')";
			List<CJManage> cjList = dao.find(sql);
			if (cjList.size() > 0) {
				mode = new UtilsMode();
				mode.setFlgId("1");
				mode.setFlgValue("当前日期考试内容" + ksnr + "已经存在,是否继续新增？");
				modeList.add(mode);
			} else {
				mode = new UtilsMode();
				mode.setFlgId("2");
				mode.setFlgValue("可以使用");
				modeList.add(mode);
			}
		} catch (Exception e) {
			mode = new UtilsMode();
			mode.setFlgId("3");
			mode.setFlgValue("系统错误, 请稍后重试");
			modeList.add(mode);
			e.printStackTrace();
		}
		return modeList;
	}

	/**
	 * 
	 * @title
	 * @author Limit
	 * @date 2014-3-11 下午1:30:20
	 * @version 1.0
	 * @param ksnr
	 * @param subId
	 * @param ksDate
	 * @param status
	 * @Description 点击新增按钮后返回一个班中所有学生信息
	 * 
	 * @param request
	 * @param id
	 * @param page
	 * @param rows
	 * @return
	 * @throws ParseException
	 * 
	 * 
	 */
	@Transactional
	public JqGridData<CJManage> queryAllStudentsByClass(Long classId,
			String subId, String ksnr, Date ksDate, JqPageModel mode,
			String status) throws ParseException {
		if (null != classId) {
			PageList<StudentInfo> page = dao.page(mode.getPage(),
					mode.getRows(), "from StudentInfo s where s.org.id = ?",
					classId);
			JqGridData<CJManage> jqdt = new JqGridData<CJManage>();
			List<StudentInfo> list = page.getList();
			jqdt.setPage(page.getPageNo());
			jqdt.setRecords(page.getTotalCount());
			jqdt.setRows(turnToRightStyle(page.getList(), classId, subId, ksnr,
					ksDate, status));
			jqdt.setTotal(page.getPageCount());
			return jqdt;
		}
		return null;
	}

	@SuppressWarnings({ "unused", "unchecked" })
	private List<CJManage> turnToRightStyle(List<StudentInfo> List,
			Long classId, String subId, String ksnr, Date ksDate, String status)
			throws ParseException {

		if (null != List && List.size() != 0) {
			String newKsnr = null;
			try {
				newKsnr = URLDecoder.decode(ksnr, "UTF-8").trim();
			} catch (Exception e) {
				e.printStackTrace();
			}
			List<CJManage> cjList = new ArrayList<CJManage>();
			if ("2".equals(status)) {
				CJManage cj = null;
				Org org = null;
				SubjectInfo sub = null;
				for (int i = 0; i < List.size(); i++) {
					cj = new CJManage();
					cj.setId(i + 1L);
					org = new Org();
					sub = new SubjectInfo();
					org.setId(classId);
					sub.setId(Long.parseLong(subId));
					cj.setStu(List.get(i));
					cj.setOrg(org);
					cj.setSub(sub);
					cj.setKsnr(newKsnr);
					cj.setkSTime(ksDate);
					cjList.add(cj);
				}
				return cjList;
			} else if ("1".equals(status)) {
				String studentId = "";
				Map<Long, CJManage> idMap = new HashMap<Long, CJManage>();
				for (int i = 0; i < List.size(); i++) {
					if ((i + 1) == List.size()) {
						studentId += List.get(i).getId();
					} else {
						studentId += List.get(i).getId() + ", ";
					}
				}
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String format2 = format.format(ksDate);
				String ksdate = format2.replace("-", "/");
				List<CJManage> cjResList = dao
						.find("from CJManage c where c.org.id = " + classId
								+ " and c.sub.id = " + subId
								+ " and c.ksnr = '" + newKsnr
								+ "' and c.kSTime = to_date('" + ksdate
								+ "','yyyy/MM/dd') and c.stu.id in ("
								+ studentId + ")");
				if (!CollectionUtils.isEmpty(cjResList)) {
					for (int j = 0; j < cjResList.size(); j++) {
						idMap.put(cjResList.get(j).getStu().getId(),
								cjResList.get(j));
					}
					for (int i = 0; i < List.size(); i++) {
						Long stuTableId = List.get(i).getId();
						CJManage cjManageData = idMap.get(stuTableId);
						if (null == cjManageData) {
							CJManage cj = null;
							Org org = null;
							SubjectInfo sub = null;
							cj = new CJManage();
							cj.setId(i + 1L);
							org = new Org();
							sub = new SubjectInfo();
							org.setId(classId);
							sub.setId(Long.parseLong(subId));
							cj.setStu(List.get(i));
							cj.setOrg(org);
							cj.setSub(sub);
							cj.setKsnr(newKsnr);
							cj.setkSTime(ksDate);
							cjList.add(cj);
						} else {
							cjList.add(cjManageData);
						}
					}
					return cjList;
				} else {
					CJManage cj = null;
					Org org = null;
					SubjectInfo sub = null;
					for (int i = 0; i < List.size(); i++) {
						cj = new CJManage();
						cj.setId(i + 1L);
						org = new Org();
						sub = new SubjectInfo();
						org.setId(classId);
						sub.setId(Long.parseLong(subId));
						cj.setStu(List.get(i));
						cj.setOrg(org);
						cj.setSub(sub);
						cj.setKsnr(newKsnr);
						cj.setkSTime(ksDate);
						cjList.add(cj);
					}
					return cjList;
				}
			}

		}
		return null;
	}

	/**
	 * 
	 * @title
	 * @author Limit
	 * @date 2014-3-11 下午1:30:20
	 * @version 1.0
	 * @Description 新增具体某一个学生的成绩信息
	 * 
	 * @param request
	 * @param id
	 * @param page
	 * @param rows
	 * @return
	 * 
	 * 
	 */
	@Transactional
	public String saveStudentInfo(CJManage cj) {

		if (null != cj) {
			Pattern patten = Pattern.compile(NUMBER_PATTERN);
			Matcher match = patten.matcher(String.valueOf(cj.getChengJi()));
			if (!match.matches()) {
				return null;
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			String StrTime = format.format(cj.getkSTime());
			CJManage res = (CJManage) dao
					.findOne("from CJManage c where c.stu.id = "
							+ cj.getStu().getId() + " and c.org.id = "
							+ cj.getOrg().getId() + " and c.sub.id = "
							+ cj.getSub().getId() + " and c.ksnr = '"
							+ cj.getKsnr() + "' and c.kSTime = to_date('"
							+ StrTime + "','yyyy/MM/dd')");
			if (null == res) {
				cj.setId(null);
				if (null == cj.getChengJi()) {
					return null;
				}
				dao.insert(cj);
			} else {
				res.setChengJi(cj.getChengJi());
				res.setReCommon(cj.getReCommon());
				dao.update(res);
			}
		}
		return null;
	}

	/**
	 * 
	 * @title
	 * @author Limit
	 * @date 2014-3-19 下午4:36:33
	 * @version 1.0
	 * @Description 修改具体某位学生的成绩
	 * 
	 * @param cjManageId
	 * @return
	 * 
	 * 
	 */
	@Transactional
	public ModelAndView updateScore(Long cjManageId) {
		if (null == cjManageId) {
			return null;
		}
		ModelAndView view = new ModelAndView("app/cjmanage/studentScoreUpdate");
		CJManage cJManage = (app.cq.hmq.pojo.cjmanage.CJManage) dao.findOne(
				"from CJManage c where c.id = ?", cjManageId);
		view.addObject("cj", cJManage);
		return view;
	}

	/**
	 * 
	 * @title
	 * @author Limit
	 * @date 2014-3-19 下午4:36:33
	 * @version 1.0
	 * @Description 修改具体某位学生的成绩
	 * 
	 * @param cjManageId
	 * @return
	 * 
	 * 
	 */
	@Transactional
	public AjaxMsg updateScoreToOneStu(CJManage cj) {
		AjaxMsg am = new AjaxMsg();
		if (null == cj || cj.getId() == null || cj.getChengJi() == null) {
			am.setMsg("修改失败,请稍后在试!");
			return am;
		}
		CJManage dataCJ = (CJManage) dao.findOne(
				"from CJManage c where c.id = ?", cj.getId());
		if (null == dataCJ) {
			am.setMsg("修改失败,请稍后在试!");
			return am;
		}
		dataCJ.setChengJi(cj.getChengJi());
		dao.update(dataCJ);
		am.setMsg("修改成功!");
		return am;
	}

	/**
	 * 月考模板
	 * @param a
	 * @param scoreType
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public AjaxMsg importScoreXls(Attach a,int scoreType) throws Exception {
		final String  DANKEZCJ = "总成绩"; 
		final String  AllKEZF = "总分";
		/** 用于存放标题以及参考人数以及发布状态 */
		StringBuilder tittemp = new StringBuilder();
		String file = ResourceUtil.getUploadPath() + "/" + a.getId()
				+ ".attach";
		AjaxMsg am = new AjaxMsg();
		String title = null;
		SessionModal cur = currentSessionModel();
		String date = DateUtil.format(a.getDate(), DateUtil.DATETIME_PATTERN);
		StringBuffer tempContent = new StringBuffer();
		/** 用于拼接某个科目下的考试内容串 */
		String tempContentFlag = null;
		// System.out.println(DateUtil.format(new Date(),
		// DateUtil.DATETIME_PATTERN));
		/** 计算列头是否已经到达“总分”的这一列，保存列数 */
		int arriveTotalScoreCount = 0;
		/** 保存上次临时的列名 所有成绩的列名对于所有的数据来说是相同的，只查询一次 */
		String lastColName = null;
		/** 存放对应科目的 映射列名 */
		Map<Integer, String> sMap = new HashMap<Integer, String>();
		/** 存放各科下的考试内容 对所有成绩来说  一个科目下的考试内容是相同的 只取一次 */
		Map<Integer, String> cMap = new HashMap<Integer, String>();
		/** 该文件中在数据库中已经存在的数据班级id */
		Set<String> classExistSet = new HashSet<String>();
		/** 该文件中在数据库中不存在的数据班级id */
		Set<String> classNOExistSet = new HashSet<String>();
		/** 有效成绩的学生人数 */
		int effectiveStu = 0;
		InputStream is = new FileInputStream(new File(file));
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
			if (hssfSheet == null) {
				throw new Exception();
			}
			if (hssfSheet.getLastRowNum() < 4) {
				System.err.println("该页无成绩数据");
				throw new Exception();
			}
			/** 获取标题 */
			int titleNum = 0;
			HSSFRow hssfRow = hssfSheet.getRow(titleNum);
			if (hssfRow == null) {
				throw new Exception();
			}
			HSSFCell titleCell = hssfRow.getCell(0);
			if (titleCell == null) {
				throw new Exception();
			}
			title = getValue(titleCell);
			if(!title.equals(a.getFileName().split("\\.")[0])){
				am.setMsg("文件名与考试标题不一致");
				am.setType(AjaxMsg.ERROR);
				return am;
			}
			
			if(judgeScoreTitleExistByImporter(title,cur.getId())){
				am.setMsg(title+"已导入");
				am.setType(AjaxMsg.ERROR);
				return am;
			}
			tittemp.append(title);
			tittemp.append("#");
			
			/** 列名列开始行 */
			int colNameNum = 1;
			/** 内容列开始行 */
			int contentNum = 2;
			HSSFRow colNameRow = hssfSheet.getRow(colNameNum);
			if (colNameRow == null) {
				throw new Exception();
			}

			HSSFRow contentRow = hssfSheet.getRow(contentNum);
			if (contentRow == null) {
				throw new Exception();
			}

			/** 查找循环结束位置 */
			HSSFCell ce =  null;
			for (int i = 0; i < colNameRow.getLastCellNum(); i++) {
				ce = colNameRow.getCell(i);
				if (ce == null) {
					continue;
				}
				if (getValue(ce).indexOf(AllKEZF) == -1) {
					arriveTotalScoreCount++;
				} else {
					break;
				}
			}
			
			HSSFCell colNameCell = null;
			HSSFCell contentCell = null;
			ScoreSubjectMapping ssm = null;
			for (int col = 3; col < arriveTotalScoreCount; col++) {
				/** 列名列 */
				colNameCell = colNameRow.getCell(col);
				if (colNameCell == null) {
					break;
				}
				 String subjectName = getValue(colNameCell);
					if (null !=  subjectName && !subjectName.matches("\\s*")) {
							ssm = (ScoreSubjectMapping) dao
									.findOne("from ScoreSubjectMapping where subject.name = ? ",subjectName);
							if (null == ssm) {
								continue;
							}
							lastColName = ssm.getColName();
							sMap.put(col, lastColName);
					}else{
						sMap.put(col, lastColName);
					}
					
					/** 内容列 如语文A卷 语文B卷 */
					contentCell = contentRow.getCell(col);
					if (contentCell == null) {
						continue;
					}
					cMap.put(col, getValue(contentCell));
			}
			
			/**开始循环数据	 */
			int num = 3;
			tittemp.append(hssfSheet.getLastRowNum()-2);
			tittemp.append("#");
			String tempTitle =null;
			for (;num <= hssfSheet.getLastRowNum(); num++) {
				HSSFRow dataRow = hssfSheet.getRow(num);
				if (dataRow == null) {
					continue;
				}
				Score sc = new Score();
				sc.setTitle(title);
				HSSFCell dataCell = null;
				Method setMethod = null;
				String colName = null;
				String contentName = null;
				String val = null;
				for (int c = 0; c < dataRow.getLastCellNum(); c++) {
					dataCell = dataRow.getCell(c);
					if (c == 0) {
						/** 当学号列无值的情况 */
						if(null == dataCell){
							continue;
						}
						dataCell.setCellType(Cell.CELL_TYPE_STRING);
						sc.setSno(getValue(dataCell));
						continue;
					}else if (c == 1) {
						sc.setsName(getValue(dataCell).trim());
						continue;
					}else if (c == 2) {
						dataCell.setCellType(Cell.CELL_TYPE_STRING);
						sc.setsClass(getValue(dataCell).trim());
						tempTitle = title + sc.getsClass()+ sc.getsName();
						
						/** 判断标题、班级、姓名是否已经在数据库中存在  true 存在 false 不存在*/
						if(classExistSet.contains(sc.getsClass())){
							break;
						}else{
							/** 查询某个班级的某个学生在某次考试内容中是否已经存在 */
							if(!classNOExistSet.contains(sc.getsClass()) &&
									judgeScoreExistByTitleandClass(sc.getTitle(),sc.getsClass(),sc.getsName())){
								classExistSet.add(sc.getsClass());
								break;
							}else{
								classNOExistSet.add(sc.getsClass());
							}
						}
						continue;
					}

					if (3 <= c && c < arriveTotalScoreCount) {
						/** 列名列 */
						/*colNameCell = colNameRow.getCell(c);
						if (colNameCell == null) {
							break;
						}*/
						/** 如语文 */
						/*colName = getValue(colNameCell);
						if (null != colName) {
							if (!colName.matches("\\s*")) {
								lastColName = colName;
								if (null == sMap.get(colName)) {
									ssm = (ScoreSubjectMapping) dao
											.findOne("from ScoreSubjectMapping where subject.name = ? ",colName);
									if (null == ssm) {
										continue;
									}
									sMap.put(colName, ssm.getColName());
								}
							} else {
								colName = lastColName;
							}*/

							/** 内容列 如语文A卷 语文B卷 *//*
							if (null == cMap.get(colName + c)) {
								contentCell = contentRow.getCell(c);
								if (contentCell == null) {
									continue;
								}
								cMap.put(colName + c, getValue(contentCell));
							}*/
							contentName = cMap.get(c);
							colName = sMap.get(c);
							
							if (null != contentName && DANKEZCJ.equals(contentName.trim())) {
								    setMethod = sc.getClass()
										.getDeclaredMethod("set" + colName,Float.class);
								val = getValue(dataCell);
								if (val.matches(NUMBER_PATTERN)) {
									setMethod.invoke(sc, Float.parseFloat(val));
								} else {
									setMethod.invoke(sc, -1f);
								}
								
								if(null != tempContentFlag && !tempContentFlag.equals(colName)){
									if(tempContent.length() != 0){
										ScoreContentMapping scm = new ScoreContentMapping();
										scm.setTitle(tempTitle);
										/** 当遇到某些科目直接是总成绩开始时，需保存前一科目的考试内容 */
										scm.setColName(sMap.get(c-1));
										scm.setContentData("{"+ tempContent.deleteCharAt(tempContent.length() - 1).toString() + "}");
										dao.insert(scm);
										tempContentFlag = colName;
										tempContent = new StringBuffer();
									}
								}
							} else {
								if (null == tempContentFlag
										|| tempContentFlag.equals(colName)) {
									tempContent.append("\"" + contentName+ "\":\"" + getValue(dataCell)+ "\",");
									tempContentFlag = colName;
								} else {
									if(tempContent.length() != 0){
										ScoreContentMapping scm = new ScoreContentMapping();
										scm.setTitle(tempTitle);
										scm.setColName(sMap.get(c-1));
										// scm.setsContent(contentName);
										// scm.setContentData(getValue(dataCell));
										scm.setContentData("{"+ tempContent.deleteCharAt(tempContent.length() - 1).toString() + "}");
										dao.insert(scm);
										tempContentFlag = colName;
										tempContent = new StringBuffer();
										tempContent.append("\"" + contentName+ "\":\"" + getValue(dataCell)	+ "\",");
									}
								}
							}
						}else{
							/** 总分 */
							if (c == arriveTotalScoreCount) {
								String t = getValue(dataCell);
								if (null == t || t.matches("\\s*")) {
									sc.setTotalScore(0f);
								} else {
									float ts = Float.parseFloat(t);
									if(ts > 0){
										effectiveStu++;
									}
									sc.setTotalScore(Float.parseFloat(t));
								}

								/** 当设置总分的时候，需要把之前的科目内容保存 */
								if(tempContent.length() != 0){
									ScoreContentMapping scm = new ScoreContentMapping();
									scm.setTitle(title + sc.getsClass() + sc.getsName());
									scm.setColName(sMap.get(c-1));
									scm.setContentData("{"+ tempContent.deleteCharAt(tempContent.length() - 1).toString()+ "}");
									dao.insert(scm);
									tempContentFlag = null;
									tempContent = new StringBuffer();
									continue;
								}
							}else if (c == 1 + arriveTotalScoreCount) {/** 班排 */
								String t = getValue(dataCell);
								if (null == t || t.matches("\\s*")) {
								} else {
									sc.setClassOrder(Short.parseShort(t.replace(".0", "")));
								}
								continue;
							}else if (c == 2 + arriveTotalScoreCount) {/** 年排 */
								String t = getValue(dataCell);
								if (null == t || t.matches("\\s*")) {
								} else {
									sc.setGradeOrder(Short.parseShort(t.replace(".0", "")));
								}
								continue;
							}else if (c == 3 + arriveTotalScoreCount) {	/**班级涨幅 */
								dataCell.setCellType(Cell.CELL_TYPE_STRING);
								String t = getValue(dataCell);
								if (null == t || t.matches("\\s*")) {
								} else {
									sc.setClassAsc(t);
								}
								continue;
							}else if (c == 4 + arriveTotalScoreCount) {/**年排涨幅 */
								dataCell.setCellType(Cell.CELL_TYPE_STRING);
								String t = getValue(dataCell);
								if (null == t || t.matches("\\s*")) {
								} else {
									sc.setGradeAsc(t);
								}
								continue;
							}
						}
				}
				if(classExistSet.contains(sc.getsClass())){
					continue;
				}
				sc.setImportDate(date);
				sc.setImportUser(cur.getId());
				try {
					sc.setScoreType(scoreType);
					dao.insert(sc);
				} catch (Exception e) {
					am.setMsg("导入失败！" +(num+1)+"行数据有误。" + e.getMessage());
					am.setType(AjaxMsg.ERROR);
					FileUtil.delete(ResourceUtil.getUploadPath() + "\\" + a.getId() + ".attach");
					dao.delete(a);
					return am;
				}
			}
			
			/** 考试成绩总分不为0的学生个数 */
			tittemp.append(effectiveStu);
			tittemp.append("#");
			tittemp.append(cur.getName());
			tittemp.append("#");
			tittemp.append(cur.getId());
			tittemp.append("#");
			tittemp.append(date);
			tittemp.append("#");
			/** 初始化发布状态为0 */
			tittemp.append(0);
			a.setDesc(tittemp.toString());
			am.setMsg("导入成功！");
		// System.out.println(DateUtil.format(new Date(),
		// DateUtil.DATETIME_PATTERN));
		return am;
	}
	
	@Transactional
	public AjaxMsg importScoreXlsWeek(Attach a,int scoreType) throws Exception {
		final String  AllKEZF = "总分";
		/** 用于存放标题以及参考人数以及发布状态 */
		StringBuilder tittemp = new StringBuilder();
		String file = ResourceUtil.getUploadPath() + "/" + a.getId()
				+ ".attach";
		AjaxMsg am = new AjaxMsg();
		String title = null;
		SessionModal cur = currentSessionModel();
		String date = DateUtil.format(a.getDate(), DateUtil.DATETIME_PATTERN);
		/** 存放第一个班级code */
		String tempClass = null;
		/** 保存上次临时的列名 所有成绩的列名对于所有的数据来说是相同的，只查询一次 */
		String lastColName = null;
		/** 存放对应科目的 映射列名 */
		Map<String, String> sMap = new HashMap<String, String>();
		/** 用于拼接哪些科目级标题班级 */
		String tempContent = "";
		/** 计算列头是否已经到达“总分”的这一列，保存列数 */
		int arriveTotalScoreCount = 0;
		/** 有效成绩的学生人数 */
		int effectiveStu = 0;
		InputStream is = new FileInputStream(new File(file));
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			if (hssfSheet.getLastRowNum() < 3) {
				System.err.println("该页无成绩数据");
				continue;
			}
			/** 获取标题 */
			int titleNum = 0;
			HSSFRow hssfRow = hssfSheet.getRow(titleNum);
			if (hssfRow == null) {
				continue;
			}
			HSSFCell titleCell = hssfRow.getCell(0);
			if (titleCell == null) {
				continue;
			}
			title = getValue(titleCell);
			
			if(!title.equals(a.getFileName().split("\\.")[0])){
				am.setMsg("文件名与考试标题不一致");
				am.setType(AjaxMsg.ERROR);
				return am;
			}
			
			if(judgeScoreTitleExistByImporter(title,cur.getId())){
				am.setMsg(title+"已导入");
				am.setType(AjaxMsg.ERROR);
				return am;
			}
			tittemp.append(title);
			tittemp.append("#");
			
			/** 列名列开始行 */
			int colNameNum = 1;
			HSSFRow colNameRow = hssfSheet.getRow(colNameNum);
			if (colNameRow == null) {
				continue;
			}

			/** 查找循环结束位置 */
			HSSFCell ce =  null;
			for (int i = 0; i < colNameRow.getLastCellNum(); i++) {
				ce = colNameRow.getCell(i);
				if (ce == null) {
					continue;
				}
				if (getValue(ce).indexOf(AllKEZF) == -1) {
					arriveTotalScoreCount++;
				} else {
					break;
				}
			}
			/**开始循环数据	 */
			int num = 2;
			tittemp.append(hssfSheet.getLastRowNum()-1);
			tittemp.append("#");
			for (;num <= hssfSheet.getLastRowNum(); num++) {
				HSSFRow dataRow = hssfSheet.getRow(num);
				if (dataRow == null) {
					continue;
				}
				Score sc = new Score();
				sc.setTitle(title);
				HSSFCell dataCell = null;
				Method setMethod = null;
				ScoreSubjectMapping ssm = null;
				String colName = null;
				HSSFCell colNameCell = null;
				String val = null;
				for (int c = 0; c < dataRow.getLastCellNum(); c++) {
					dataCell = dataRow.getCell(c);
					if (c == 0) {
						sc.setsName(getValue(dataCell).trim());
						continue;
					}
					if (c == 1) {
						if( null == tempClass){
							val = getValue(dataCell);
							if(null != val){
								Pattern pa= Pattern.compile("\\d{2}(\\d{2})届(\\d{1,3})班");
								Matcher ma = pa.matcher(val);
								if(ma.find()){
									tempClass = ma.group(1);
									if(ma.group(2).length() == 1){
										tempClass += "0"+ma.group(2);
									}else{
										tempClass += ma.group(2);
									}
									sc.setsClass(tempClass);
								}
								
							}else{
								sc.setsClass(val);
							}
						
						}else{
							sc.setsClass(tempClass);
						}
						continue;
					}

					/** 判断标题、班级、姓名是否已经在数据库中存在  true 存在 false 不存在*/
					if(judgeScoreExistByTitleandClass(sc.getTitle(),sc.getsClass(),sc.getsName())){
						am.setMsg("该成绩已经导入!");
						am.setType(AjaxMsg.ERROR);
						return am;
					}
					
					if (2 <= c && c < arriveTotalScoreCount) {
						/** 列名列 */
						colNameCell = colNameRow.getCell(c);
						if (colNameCell == null) {
							break;
						}
						/** 如语文 */
						colName = getValue(colNameCell);
						if (null != colName) {
							if (!colName.matches("\\s*")) {
								lastColName = colName;
								if (null == sMap.get(colName)) {
									ssm = (ScoreSubjectMapping) dao
											.findOne("from ScoreSubjectMapping where subject.name = ? ",colName);
									if (null == ssm) {
										continue;
									}
									sMap.put(colName, ssm.getColName());
								}
								
								/** 当第一个单元格科目不为null时，设置单科总分 */
								setMethod = sc.getClass().getDeclaredMethod("set" + sMap.get(colName),Float.class);
								val = getValue(dataCell);
								if (val.matches(NUMBER_PATTERN)) {
									setMethod.invoke(sc, Float.parseFloat(val));
								} else {
									setMethod.invoke(sc, -1f);
								}
							} else {
								colName = lastColName;
								//TODO 暂无内容列，如排名 需自动算
							}
						}
					}
					/** 总分 */
					if (c == arriveTotalScoreCount) {
						String t = getValue(dataCell);
						if (null == t || t.matches("\\s*")) {
							sc.setTotalScore(0f);
						} else {
							float ts = Float.parseFloat(t);
							if(ts > 0){
								effectiveStu++;
							}
							sc.setTotalScore(Float.parseFloat(t));
						}
					}
					
					/** 班排 */
					 if (c == 1 + arriveTotalScoreCount) {
							String t = getValue(dataCell);
							if (null == t || t.matches("\\s*")) {
							} else {
								sc.setClassOrder(Short.parseShort(t.replace(".0", "")));
							}
							continue;
						}
				}
				sc.setImportDate(date);
				sc.setImportUser(cur.getId());
				try {
					sc.setScoreType(scoreType);
					dao.insert(sc);
				} catch (Exception e) {
					am.setMsg("导入失败！" +(num+1)+"行数据有误。" + e.getMessage());
					am.setType(AjaxMsg.ERROR);
					FileUtil.delete(ResourceUtil.getUploadPath() + "\\" + a.getId() + ".attach");
					dao.delete(a);
					return am;
				}
			}
			
			/** 考试成绩总分不为0的学生个数 */
			tittemp.append(effectiveStu);
			tittemp.append("#");
			tittemp.append(cur.getName());
			tittemp.append("#");
			tittemp.append(cur.getId());
			tittemp.append("#");
			tittemp.append(date);
			tittemp.append("#");
			/** 初始化发布状态为0 */
			tittemp.append(0);
			a.setDesc(tittemp.toString());
			for (Entry<String, String> e : sMap.entrySet()) {
				tempContent += e.getValue()+",";
			}
			tempContent += "_"+title+"_"+tempClass+"_"+a.getId();
			am.setMsgId(tempContent);
			am.setMsg("导入成功！");
		}
		return am;
	}
	
	@Transactional
	public AjaxMsg paimingByweek(String str){
		AjaxMsg ajaxMsg = new AjaxMsg();
		if(null != str){
			String[] tem =  str.split("_");
			String[] cols = tem[0].split(",");
			SessionModal smModal = currentSessionModel();
			String viewName = "pmView"+smModal.getId()+"_"+new Date().getTime();
			try {
				dao.getHelperDao().excute("CREATE or REPLACE view "+viewName+" as select cj.title_f,cj.sclass_f," +
						" cj.sname_f, rank() over (order by cj.totalscore_f desc) zorder  from Score_t cj" +
						" where cj.title_f = '"+tem[1]+"' and cj.sclass_f = '"+tem[2]+"'");
				
				dao.getHelperDao().excute("update Score_t c set c.classorder_f = " +
						"(select p.zorder from "+viewName+" p where p.sname_f = c.sname_f and p.sclass_f = c.sclass_f and p.title_f = c.title_f) " +
						"where exists (select 1 from "+viewName+" p where p.sname_f = c.sname_f and p.sclass_f = c.sclass_f and p.title_f = c.title_f)");
					if(null != cols && cols.length > 0){
						List<Object[]> list = null;
						for (String colName : cols) {
							list = dao.getHelperDao().find("select sName_f,rank() over (order by "+colName
									+"_f desc) from Score_t where title_f = ? and sClass_f = ?", tem[1], tem[2]);
							if(null != list && list.size() > 0){
								for (Object[] objects : list) {
									ScoreContentMapping scm = new ScoreContentMapping();
									scm.setTitle(tem[1] + tem[2]+ objects[0]);
									scm.setColName(colName);
									scm.setContentData("{\"班排\":"+ objects[1]+ "}");
									dao.insert(scm);
								}
							}
						}
				}
			} catch (Exception e) {
				ajaxMsg.setType(2);
				ajaxMsg.setMsg("排名总分失败");
				dao.getHelperDao().excute("delete from score_t where title_f = ? and sclass_f = ? and importUser_f = ?",
						tem[1], tem[2],smModal.getId());
				attachService.delete(Long.parseLong(tem[3]));
				return ajaxMsg;
			}finally{
				dao.getHelperDao().excute("drop view "+viewName);
			}
		}
		ajaxMsg.setMsg("导入成功！");
		return ajaxMsg;
	}
	
	public boolean judgeScoreExistByTitleandClass(String ti,String cc,String na){
		List o = dao.getHelperDao().find("select count(1) from score_t s where  s.sname_f = ? and s.sclass_f = ? and s.title_f = ?",
				na,cc,ti);
		if(null != o && o.size() > 0 && Integer.parseInt((String.valueOf(o.get(0)))) > 0){
			return true;
		}
		return false;
	}
	
	public boolean judgeScoreTitleExistByImporter(String ti,Long uid){
		List o = dao.getHelperDao().find("select count(1) from score_t s where s.importUser_f = ? and s.title_f = ?",
				uid,ti);
		if(null != o && o.size() > 0 && Integer.parseInt((String.valueOf(o.get(0)))) > 0){
			return true;
		}
		return false;
	}
	
	public void insertScoreContentMapping(ScoreContentMapping scm){
		dao.insert(scm);
	}
	
	@Transactional
	public void insertScore(Score sc){
		dao.insert(sc);
	}

	/**
	 * 
	 * @param hssfCell
	 * @return
	 */
	private String getValue(HSSFCell hssfCell) {
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}

	public JqGridData<Attach> lookimportfile(JqPageModel mode, Long id) {
		JqGridData<Attach> jqGridData = new JqGridData<Attach>();
		StringBuffer sb = new StringBuffer(
				"from Attach where relId = ? and relType = ?");
		if (!StringUtil.isEmpty(mode.getSort())
				|| !StringUtil.isEmpty(mode.getOrder())) {
			sb.append(" order by ");
			sb.append(mode.getSort());
			sb.append(" ");
			sb.append(mode.getOrder());
		}
		PageList<Attach> pa = dao.page(mode.getPage(), mode.getRows(),
				sb.toString(), id, "TeacherImportScore");
		jqGridData.setPage(pa.getPageNo());
		jqGridData.setRows(pa.getList());
		jqGridData.setRecords(pa.getTotalCount());
		jqGridData.setTotal(pa.getPageCount());
		return jqGridData;
	}

	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-16 下午3:49:59
	 *@version 1.0
	 *@Description 查询附件信息
	 *
	 *@param mode
	 *@param teaType
	 * @param njzr 
	 * @param bjzr 
	 *@return
	 *
	 *
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JqGridData<?> queryImportAppendix(JqPageModel mode, Long id,
			String bjzr, String njzr) {
		JqGridData<?> jqGridData = new JqGridData<Object>();
		List<Map<String,Object>> appList = new ArrayList<Map<String,Object>>();
		Map<String,Object> apdixMap = null;
		if(null == id || "".equals(id)){
			return null;
		}
		String sql = null;
		String totalSql = "";
		PageList page = null;
		if(null != njzr && !"".equals(njzr) && !"null".equals(njzr)){
			//根据年级主任ID查询
			sql = "select att.desc_f,att.filename_f,att.size_f,att.id_f from attach_t att where att.reltype_f = 'TeacherImportScore'  and (att.relid_f in ( select id_f from org_t where parent_f = "+  Long.parseLong(njzr) +" ) or att.relid_f = "+ Long.parseLong(njzr) +")";
			totalSql = "select count(*) from attach_t att where att.reltype_f = 'TeacherImportScore'  and (att.relid_f in ( select id_f from org_t where parent_f = "+  Long.parseLong(njzr) +" ) or att.relid_f = "+ Long.parseLong(njzr) +")";
		}else if(null != bjzr && !"".equals(bjzr) && !"null".equals(bjzr)){
			//根据班级ID查询
			sql = "select att.desc_f,att.filename_f,att.size_f,att.id_f from attach_t att where att.reltype_f = 'TeacherImportScore'  and (att.relid_f in ( select parent_f  from org_t where id_f = "+ Long.parseLong(bjzr) +") or att.relid_f = "+ Long.parseLong(bjzr) +")";
			totalSql = "select count(*) from attach_t att where att.reltype_f = 'TeacherImportScore'  and (att.relid_f in ( select parent_f  from org_t where id_f = "+ Long.parseLong(bjzr) +") or att.relid_f = "+ Long.parseLong(bjzr) +")";
		}else{
		sql ="select att.desc_f,att.filename_f,att.size_f,att.id_f from attach_t att where att.relid_f = '"+id+"' and att.reltype_f = 'TeacherImportScore' order by id_f desc";
			/*sql = "select files.desc_f, files.filename_f, files.size_f, files.id_f,sco.pub from (select att.desc_f, att.filename_f, att.size_f, att.id_f "
					+ " from attach_t att where att.relid_f = '"+id+"' and att.reltype_f = 'TeacherImportScore' order by id_f desc) files, "
							+ " (select sc.title_f,decode(sign(sum(sc.publishstatus_f) - 1), -1, 0, 0, 1, 1, 1) pub from score_t sc group by sc.title_f) sco where files.filename_f = sco.title_f || '.xls' order by files.id_f desc";
			totalSql = 	"select count(1) from (select att.desc_f, att.filename_f, att.size_f, att.id_f\n" +
							"  from attach_t att\n" + 
							" where att.relid_f = '"+id+"'\n" + 
							"   and att.reltype_f = 'TeacherImportScore'\n" + 
							" order by id_f desc) files,\n" + 
							" (select sc.title_f,decode(sign(sum(sc.publishstatus_f) - 1), -1, 0, 0, 1, 1, 1) pub\n" + 
							"            from score_t sc group by sc.title_f) sco where files.filename_f = sco.title_f || '.xls' order by files.id_f desc";
			page = dao.getHelperDao().pageByTotal(mode.getPage(), mode.getRows(),sql,totalSql);*/
		}
		if(null != sql){
//			PageList page = dao.page(mode.getPage(), mode.getRows(),sql);
			page = dao.getHelperDao().page(mode.getPage(), mode.getRows(),sql);
			//page = dao.getHelperDao().pageByTotal(mode.getPage(), mode.getRows(),sql,totalSql);
			List list = page.getList();
			Object[] object = null;
			if(list != null && list.size() > 0){
				for (int i = 0; i < list.size(); i++) {
					object = (Object[]) list.get(i);
					apdixMap = new HashMap<String, Object>();
					if(null != object[0] && !"".equals(object[0].toString()) && !"null".equals(object[0].toString())){
						String[] split = object[0].toString().split("#");
						apdixMap.put("ksTitle", split[0]);
						apdixMap.put("ksTotal", split[1]);
						apdixMap.put("sjCount", split[2]);
						apdixMap.put("impoTea", split[3]);
						apdixMap.put("impoDate", split[5]);
						/*apdixMap.put("impoStatus", split[6]);*/
						if(null != object[1] && !"".equals(object[1].toString()) && !"null".equals(object[1].toString())){
							apdixMap.put("appTtitle", object[1].toString());
						}
						if(null != object[2] && !"".equals(object[2].toString()) && !"null".equals(object[2].toString())){
							apdixMap.put("appSize", Integer.parseInt(object[2].toString())/1000 + "KB");
						}
						if(null != object[3] && !"".equals(object[3].toString()) && !"null".equals(object[3].toString())){
							apdixMap.put("id", object[3]);
						}
						if(split.length > 6){
							if(!StringUtil.isEmpty(split[6]) && !"null".equals(split[6].toString())){
								if("1".equals(split[6].toString())){
									apdixMap.put("pubStatus", "已发布");
								}else{
									apdixMap.put("pubStatus", "未发布");
								}
							}
						}else{
							apdixMap.put("pubStatus", "已发布");
						}
					}
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
		return null;
	}

	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-17 下午3:59:24
	 *@version 1.0
	 *@Description 发布未发布的成绩并更改状态
	 *
	 *@param id
	 * @param title 
	 *@return
	 * @throws ChannelServerException 
	 * @throws ChannelClientException 
	 * @throws NumberFormatException 
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public AjaxMsg updateAnnounceStatus(Long id, String title) throws NumberFormatException, ChannelClientException, ChannelServerException {
		AjaxMsg msg = new AjaxMsg();
		boolean oneflag = true;
		if(null == id || "".equals(id)){
			msg.setType(AjaxMsg.ERROR);
			return msg;
		}
		Org org = dao.findOne(Org.class, "id", id);
		/** search one subject score */
		String colNameStrs = "";
		List<Object> subjectNameList = new ArrayList<Object>();
		String subjectAndColName = "select scm.colname_f,sub.name_f from scoresubjectmapping_t scm,subjectmapping_t sm,subjectinfo_t sub where " +
				"scm.subject_f = sub.id_f and sm.subjectinfo_f = sub.id_f and sm.org_f = ?";
		List<Object[]> subjectAndColNameList = dao.getHelperDao().find(subjectAndColName,String.valueOf(org.getId()));
		if(null != subjectAndColNameList && subjectAndColNameList.size() > 0){
			Object[] object =  null;
			for (int i = 0; i < subjectAndColNameList.size(); i++) {
				object = (Object[]) subjectAndColNameList.get(i);
				colNameStrs += "scro." + object[0] + "_f,";
				subjectNameList.add(object[1]);
			}
		}
		
		String queryScoreHql = "select "+colNameStrs+" scro.id_f,scro.title_f,scro.sname_f,"
			+ "scro.totalscore_f,scro.gradeorder_f,scro.classorder_f "
			+ "from score_t scro where scro.sclass_f = '" + org.getCode()
			+ "' and scro.publishStatus_f = 0 and scro.title_f = '" + title + "'";
		List<Object[]> scoreList = dao.getHelperDao().find(queryScoreHql);
		if(null != scoreList && !scoreList.isEmpty()){
			/** 从在学生信息里未找到和成绩对应的学生 */
			String tempRecord = "";
			StringBuffer smscontent = null;
			int len = subjectNameList.size();
			List<Object[]> stuList = null;
			Object[] stuObject = null;
	    	PushMsgModel model = new PushMsgModel();
			model.setTitle("成绩通知");
			model.setDescription("尊敬的家长您好,学生最近考试成绩已发布,请查看!");
			SessionModal sessionModel = currentSessionModel();
			for (Object[] scoreObj : scoreList) {
				smscontent =  new StringBuffer(title);
			    stuList = dao.getHelperDao().findLimit(1, "select stu.channelid_f, stu.userid_f,stu.android_f,stu.selftel_f" +
			    		" from studentinfo_t stu where stu.org_f = ? and stu.name_f = ?", id,String.valueOf(scoreObj[len+2]));
			    if(null != stuList && !stuList.isEmpty()){
			    	stuObject = stuList.get(0);
				   try {
						/** 短信  */
						if(!StringUtil.isEmpty(stuObject[3])){
							smscontent.append(scoreObj[len+2]);
							smscontent.append(":");
							for(int index=0; index<len;index++){
								if(!StringUtil.isEmpty(scoreObj[index])){
									smscontent.append(subjectNameList.get(index));
									smscontent.append(scoreObj[index]);
									smscontent.append(",");
								}
							}
							smscontent.append("总分");
							smscontent.append(scoreObj[len+3]);
							smscontent.append(",");
							if(!StringUtil.isEmpty(scoreObj[len+5])){
								smscontent.append("班排");
								smscontent.append(scoreObj[len+5]);
								smscontent.append(",");
							}
							if(!StringUtil.isEmpty(scoreObj[len+4])){
								smscontent.append("年排");
								smscontent.append(scoreObj[len+4]);
							}
							 /*if(200 != SendSMS.sendMsg(smscontent.toString(),stuObject[3].toString(),sessionModel.getId(),sessionModel.getName(),
									 new Long[]{null,Long.parseLong(String.valueOf(scoreObj[len+0]))})){
								 tempRecord+= scoreObj[len+2]+",";
							 }else{*/
								 dao.excute("update Score set publishStatus = ? where id = ? ",
										 		true,Long.parseLong(String.valueOf(scoreObj[len+0])));
								 if(oneflag){
									 dao.getHelperDao().excute("update attach_t att set att.desc_f = REPLACE(att.desc_f, '#0','#1') " +
									 		"where instr(att.filename_f,?) > 0 and  att.desc_f is not null and att.reltype_f = 'TeacherImportScore'", title+".");
									 oneflag = false;
								 }
							 /*}*/
						}else{
							tempRecord+= scoreObj[len+2]+",";
						}
					} catch (Exception e) {
						logRecord(LogRecord.ADD, "发布学生"+scoreObj[len+2]+"的成绩时,短信发送出错");
						tempRecord+= scoreObj[len+2]+",";
						continue;
					}
			    	 /** 通知 */
			    	if(StringUtil.isEmpty(stuObject[2])){
			    		continue;
			    	}
			    	if(StringUtil.isEmpty(stuObject[0]) && StringUtil.isEmpty(stuObject[1])){
			    		continue;
			    	}
				
					try {
						if("1".equals(stuObject[2].toString())){
							//PushUtil.androidPushUserMsg(Long.parseLong(stuObject[0].toString()), stuObject[1].toString(), model);
						}else if("0".equals(stuObject[2].toString())){
							//PushUtil.iosPushUserMsg(Long.parseLong(stuObject[0].toString()), stuObject[1].toString(), false, model.getTitle());
						}
					} catch (Exception e) {
						logRecord(LogRecord.ADD, "发布学生"+scoreObj[len+2]+"的成绩时,app通知推送出错");
					}
			    }else{
			    	tempRecord+= scoreObj[len+2]+",";
			    }
			}
			
			if(!"".equals(tempRecord)){
				msg.setType(200);
				msg.setMsg("未发布的同学有:"+tempRecord+":可能由姓名差异、联系号码异常等原因导致,请联系管理员后再发布。");
			}
		}else{
			msg.setMsg("没有找到需要发布的成绩!");
		}
		return msg;
		
		/** 此处查询标题与 班级下的 未发布的成绩*/
		/*String querySql = "select scro.id_f,scro.title_f,scro.sname_f,"
				+ "scro.totalscore_f,scro.gradeorder_f,scro.classorder_f "
				+ "from score_t scro where scro.sclass_f = '" + org.getCode()
				+ "' and scro.publishStatus_f = 0 and scro.title_f = '" + title + "'";
		List<Object[]> scList = dao.getHelperDao().find(querySql);
		if(null != scList && !scList.isEmpty()){
			*//** 从在学生信息里未找到和成绩对应的学生 *//*
			String tempRecord = "";
			String smscontent = null;
			List<Object[]> stuList = null;
			Object[] stuObject = null;
	    	PushMsgModel model = new PushMsgModel();
			model.setTitle("成绩通知");
			model.setDescription("尊敬的家长您好,学生最近考试成绩已发布,请查看!");
			SessionModal sessionModel = currentSessionModel();
			for (Object[] objects : scList) {
			    stuList = dao.getHelperDao().findLimit(1, "select stu.channelid_f, stu.userid_f,stu.android_f,stu.selftel_f from studentinfo_t stu" +
						" where stu.org_f = ? and stu.name_f = ?", id,String.valueOf(objects[2]));
			    if(null != stuList && !stuList.isEmpty()){
			    	stuObject = stuList.get(0);
				   try {
						*//** 短信  *//*
						if(!StringUtil.isEmpty(stuObject[3])){
							smscontent = "家长您好,"+objects[2]+"近期考试总成绩:"+ objects[3];
							if(!StringUtil.isEmpty(objects[5])){
								smscontent += ",班排:"+ objects[5];
							}
							if(!StringUtil.isEmpty(objects[4])){
								smscontent += ",年排:"+ objects[4];
							}
							 if(200 != SendSMS.sendMsg(smscontent,stuObject[3].toString(),sessionModel.getId(),sessionModel.getName())){
								 tempRecord+= objects[2]+",";
							 }else{
								 dao.excute("update Score set publishStatus = ? where id = ? ",
										 		true,Long.parseLong(String.valueOf( objects[0])));
							 }
						}else{
							tempRecord+= objects[2]+",";
						}
					} catch (Exception e) {
						logRecord(LogRecord.ADD, "发布学生"+objects[2]+"的成绩时,短信发送出错");
						tempRecord+= objects[2]+",";
						continue;
					}
					smscontent = "";
			    	 *//** 通知 *//*
			    	if(StringUtil.isEmpty(stuObject[2])){
			    		continue;
			    	}
			    	if(StringUtil.isEmpty(stuObject[0]) && StringUtil.isEmpty(stuObject[1])){
			    		continue;
			    	}
				
					try {
						if("1".equals(stuObject[2].toString())){
							PushUtil.androidPushUserMsg(Long.parseLong(stuObject[0].toString()), stuObject[1].toString(), model);
						}else if("0".equals(stuObject[2].toString())){
							PushUtil.iosPushUserMsg(Long.parseLong(stuObject[0].toString()), stuObject[1].toString(), false, model.getTitle());
						}
					} catch (Exception e) {
						logRecord(LogRecord.ADD, "发布学生"+objects[2]+"的成绩时,app通知推送出错");
					}
			    }else{
			    	tempRecord+= objects[2]+",";
			    }
			}
			
			if(!"".equals(tempRecord)){
				msg.setType(200);
				msg.setMsg("未发布的同学有："+tempRecord+"可能由姓名差异、联系号码异常等原因导致,请联系管理员后再发布。");
			}
		}else{
			msg.setMsg("没有找到需要发布的成绩!");
		}*/

		/*Attach att = dao.findOne(Attach.class, "id", id);
		String desc = att.getDesc();
		String status = desc.substring(desc.length()-1, desc.length());
		String newDesc = desc.substring(0, desc.length()-1);
		if("0".equals(status)){
			
			String[] split = desc.split("#");
			String impoTitle = split[0].toString();
			String impoId = split[4].toString();
			if(!(currentSessionModel().getId()+"").equals(impoId)){
				//判断是否是同一个
				msg.setMsg("成绩非您上传,您无权限发布");
				msg.setType(100);
				return msg;
			}
			Org org = dao.findOne(Org.class, "id", att.getRelId());
			if(null == org){
				return msg;
			}
			String typeFlg = org.getType()+"";
			if(null == typeFlg || "".equals(typeFlg)){
				return msg;
			}
			List noticeList = null;
			//2:代表年级
			if("2".equals(typeFlg)){
				StringBuilder noticeSqlBygrand =  new StringBuilder("select rrr.channelid_f, rrr.userid_f,rrr.android_f,rrr.no_f from ");
				noticeSqlBygrand.append("(select svn.sname_f, svn.sclass_f ");
				noticeSqlBygrand.append("from score_t svn ");
				noticeSqlBygrand.append("where svn.importuser_f = " + Long.parseLong(impoId));
				noticeSqlBygrand.append("and svn.title_f = '"+ impoTitle +"' " );
				noticeSqlBygrand.append("and svn.totalscore_f > 0 " );
				noticeSqlBygrand.append("and svn.sclass_f " );
				noticeSqlBygrand.append("in (select code_f from org_t where parent_f = "+ org.getId() +" and type_f = 3)) ttt,");
				noticeSqlBygrand.append(" (select org.code_f, stu.name_f, stu.channelid_f, ");
				noticeSqlBygrand.append("stu.userid_f,stu.android_f,stu.no_f from org_t org, studentinfo_t stu ");
				noticeSqlBygrand.append("where  ");
				noticeSqlBygrand.append(" org.id_f = stu.org_f ");
				noticeSqlBygrand.append("and org.parent_f = "+ org.getId());
				noticeSqlBygrand.append(" and org.type_f = 3) rrr " );
				noticeSqlBygrand.append("where ttt.sname_f = rrr.name_f ");
				noticeSqlBygrand.append("and ttt.sclass_f = rrr.code_f");
				noticeList = dao.getHelperDao().find(noticeSqlBygrand.toString());
			}else if("3".equals(typeFlg)){// 3： 代表班级
				StringBuilder noticeSqlByclass =  new StringBuilder("select rrr.channelid_f, rrr.userid_f,rrr.android_f,rrr.no_f from ");
				noticeSqlByclass.append("(select svn.sname_f, svn.sclass_f ");
				noticeSqlByclass.append("from score_t svn ");
				noticeSqlByclass.append("where svn.importuser_f = " + Long.parseLong(impoId));
				noticeSqlByclass.append(" and svn.title_f = '"+ impoTitle +"' ");
				noticeSqlByclass.append("and svn.totalscore_f > 0 ");
				noticeSqlByclass.append("and svn.sclass_f ");
				noticeSqlByclass.append("in (select code_f from org_t where id_f = "+ org.getId() +")) ttt,");
				noticeSqlByclass.append(" (select org.code_f, stu.name_f, stu.channelid_f, ");
				noticeSqlByclass.append("stu.userid_f,stu.android_f,stu.no_f from org_t org, studentinfo_t stu ");
				noticeSqlByclass.append("where  ");
			    noticeSqlByclass.append(" org.id_f = stu.org_f ");
			    noticeSqlByclass.append(" and org.id_f = "+ org.getId() +") rrr ");
			    noticeSqlByclass.append("where ttt.sname_f = rrr.name_f ");
			    noticeSqlByclass.append("and ttt.sclass_f = rrr.code_f");
				noticeList = dao.getHelperDao().find(noticeSqlByclass.toString());
			}
			if(null == noticeList || noticeList.size() == 0){
				msg.setMsg("未查找到需要发布成绩的学生信息,故无法发布");
				msg.setType(300);
				return msg;
			}
			Object[] object = null;
			PushMsgModel model = new PushMsgModel();
			model.setTitle("成绩通知");
			model.setDescription("尊敬的家长您好,学生最近考试成绩已发布,请查看!");
			//创建新线程
			ScoreManagePush push = new ScoreManagePush();
			push.setNoticeList(noticeList);
			push.setModel(model);
			Thread tr = new Thread(push);
			tr.start();
			newDesc += "1";
			att.setDesc(newDesc);
			dao.update(att);
			msg.setType(AjaxMsg.SUCCESS);
			return msg;
		}else{
			msg.setMsg("成绩已经发布,无需再次发布");
			msg.setType(200);
			return msg;
		}*/
	}
	
	public static void main(String[] args) {
//		String aa = "{\"年排\":\"37\"}"; 
//		Pattern pa= Pattern.compile("\\S*\\s*年排\":\"?(\\d+)\"?[.0]?\\s*\\S*");
//		Matcher ma = pa.matcher(String.valueOf(aa));
//		System.out.println(ma.find());
//		System.out.println(ma.group(1));
		String substring = "初2014级（2017届）9班2014年9月第入学数学考试".substring(0, 1);
		System.out.println(substring);
		AjaxMsg msg = new AjaxMsg();
		msg.setType(AjaxMsg.ERROR);
		System.out.println(msg.getType());
	}
	
	public Object analyCjByStudentNameAndOrg(Long orgId, String orgCode,String uName){
		/** 查询班级对应的科目列名 */
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Object[] x = null;
		Map[] y =  null;
		List<Object> listY =  new ArrayList<Object>();
		
		String nearTitleStr = "select title_f from (select sc.title_f, sc.importdate_f from " +
				"score_t sc where sc.scoretype_f = 2 and sc.sname_f = ? and sc.sclass_f = ? order by sc.importdate_f desc) a limit 5";
		List<String> nearTitleList = dao.getHelperDao().find(nearTitleStr, uName,orgCode);
		/** 为in查询拼接标题 */
		String inTitleForScore = "";
		if(null != nearTitleList && nearTitleList.size() > 0){
			int len = nearTitleList.size();
			x = new String[len];
			for (int i = nearTitleList.size()-1; i >=0 ; i--) {
				inTitleForScore +="'"+nearTitleList.get(i)+"',";
				x[len-i-1] = nearTitleList.get(i);
			}
			inTitleForScore = inTitleForScore.substring(0, inTitleForScore.lastIndexOf(","));
			String colNameStr = "select ssm.colname_f,si.name_f from subjectmapping_t sm,scoresubjectmapping_t ssm,subjectinfo_t si " +
					"where si.id_f = ssm.subject_f and ssm.subject_f = sm.subjectinfo_f and sm.org_f = ?";
			List<Object[]> colNameList = dao.getHelperDao().find(colNameStr, orgId);
			if(null !=  colNameList && colNameList.size() > 0){
				y = new Map[colNameList.size()];
				int t = 0;
				Pattern pa= Pattern.compile("\\S*\\s*年排\":\"?(\\d+)\"?[.0]?\\s*\\S*");
				for (Object[] col : colNameList) {
					Map<String, Object> mMap = new HashMap<String, Object>();
					mMap.put("name", col[1]);
					StringBuilder sb = new StringBuilder();
					/** 为查询拼接列名 */
						sb.append("select r."+col[0]+"_f,r.title_f,r.totalscore_f, r.gradeorder_f,scm.contentdata_f");
						sb.append(" from (select sc."+col[0]+"_f,sc.title_f,sc.sclass_f, sc.sname_f, sc.importdate_f,sc.totalscore_f,sc.gradeorder_f");
						sb.append(" from score_t sc where sc.sname_f = ? and sc."+col[0]+"_f is not null");
						sb.append(" and sc.sclass_f = ? and sc.title_f in ("+inTitleForScore+") and sc.publishstatus_f = 1 order by sc.id_f) r");
						sb.append(" left join scorecontentmapping_t scm on scm.colname_f = '"+col[0]+"'");
						sb.append(" and scm.title_f = concat(r.title_f,r.sclass_f,r.sname_f)");
						List<Object[]> resultList = dao.getHelperDao().find(sb.toString(), uName,orgCode);	
						if(null != resultList && resultList.size() > 0){
							Map[] sArray =  new Map[len];
							int temp = 0;
							for (Object[] objects : resultList) {
								Map<String, Object> sMap = new HashMap<String, Object>();
								temp = len-nearTitleList.indexOf(objects[1])-1;
								sMap.put("name",  "总分："+objects[0]);
								Matcher ma = pa.matcher(String.valueOf(objects[4]));
								if(ma.find()){
									if(ma.group(1).matches("\\d+")){
										sMap.put("y", Integer.parseInt(ma.group(1)));
									}else{
										sMap.put("y", null);
									}
								}else{
									sMap.put("y", null);
								}
								sArray[temp] = sMap;
							}
							mMap.put("data", sArray);
						}else{
							continue;
						}
					y[t] = mMap;
					t++;
				}
				
				for (Object obj : y) {
					if(null != obj){
						listY.add(obj);
					}
				}
				
				resultMap.put("x",x);
				resultMap.put("y",listY.toArray());
				return resultMap;
			}
		}
		return null;
	}
	
	/**
	 * 默认查询出班主任所带班级的，且是未发布的状态（如有审核，应该是审核过了的）
	 * @param mode
	 * @param title 
	 * @param classCode
	 */
	public JqGridData<?> findAllUnPublishScoreByClassCode(JqPageModel mode,
			Long classId,int publishStatus, String title) {
		JqGridData<ScoreQueryMode> jqdt = new JqGridData<ScoreQueryMode>();
		List<ScoreQueryMode> scoreList = new ArrayList<ScoreQueryMode>();
		PageList page = null;
		if (null != classId) {
			Org org = dao.findOne(Org.class, "id", classId);
			String colNameSql = "select scm.colname_f,sub.name_f from scoresubjectmapping_t scm,subjectmapping_t sm,subjectinfo_t sub where" +
			" scm.subject_f = sub.id_f and sm.subjectinfo_f = sub.id_f and sm.org_f = '"+classId+"' order by scm.colname_f asc";
			List subList = dao.getHelperDao().find(colNameSql);
			List<String>  subjectNameList = new ArrayList<String>(); 
			String colNameStrs = "";
			if (subList.size() != 0) {
				Object[] object =  null;
				for (int i = 0; i < subList.size(); i++) {
					object = (Object[]) subList.get(i);
					colNameStrs += "scro." + object[0].toString() + "_f,";
					subjectNameList.add(object[1].toString());
				}
			}else{
				return jqdt;
			}
			String querySql = "select scro.id_f,scro.title_f,scro.sname_f,"
					+ colNameStrs+ "scro.totalscore_f," 
					+ "scro.gradeorder_f,scro.classorder_f,scro.bzrcomments_f,scro.publishStatus_f "
					+ "from score_t scro where scro.sclass_f = '" + org.getCode()
					+ "' and scro.title_f = '"+ title +"' order by scro.id_f";
			page = dao.getHelperDao().page(mode.getPage(), mode.getRows(),querySql);
			List list = page.getList();
			if (null == list || list.size() == 0) {
				return jqdt;
			}
			String subValue = "";
			for (int i = 0; i < list.size(); i++) {
				Object[] object = (Object[]) list.get(i);
				ScoreQueryMode scoremode = new ScoreQueryMode();
				scoremode.setClassCode(org.getCode());
				if (null != object[0]) {
					scoremode.setId(Long.parseLong(String.valueOf(object[0])));
				}
				if (null != object[1]) {
					scoremode.setTitle(object[1].toString());
					
				}
				if (null != object[2]) {
					scoremode.setName(object[2].toString());
				}
				
				for (int j = 0; j < subjectNameList.size(); j++) {
					if(!StringUtil.isEmpty(object[3+j])){
						subValue += subjectNameList.get(j) + ":"
						+ object[3+j].toString() + ", ";
					}
				}
				scoremode.setScoreA(subValue);
				subValue = "";
				if (!StringUtil.isEmpty(object[3+subjectNameList.size()])) {
					scoremode.setTotalScore(object[3+subjectNameList.size()].toString());
				}
				if (!StringUtil.isEmpty(object[4+subjectNameList.size()])) {
					scoremode.setDrandOrder(object[4+subjectNameList.size()].toString());
				}
				if (!StringUtil.isEmpty(object[5+subjectNameList.size()])) {
					scoremode.setClassOrder(object[5+subjectNameList.size()].toString());
				}
				
				if (!StringUtil.isEmpty(object[6+subjectNameList.size()])) {
					scoremode.setBzrComments(object[6+subjectNameList.size()].toString());
				}
				if (!StringUtil.isEmpty(object[7+subjectNameList.size()])) {
					scoremode.setPublishStatus(object[7+subjectNameList.size()].toString());
				}
				scoreList.add(scoremode);
			}
			page.setResult(scoreList);
			jqdt.setPage(page.getPageNo());
			jqdt.setRecords(page.getTotalCount());
			jqdt.setRows(page.getList());
			jqdt.setTotal(page.getPageCount());
			//logRecord(LogRecord.QUERY, "教师查询未发布的学生成绩");
		}
		return jqdt;
	}

	@Transactional
	public AjaxMsg addCommentsForScore(Long scId, String comment, Long bjzr) {
		AjaxMsg am = new AjaxMsg();	
		if(!StringUtil.isEmpty(comment) && comment.length() > 10){
			am.setMsg("评语不能超过10个字符");
			am.setType(AjaxMsg.ERROR);
			return am;
		}
		Score sco = dao.findOne(Score.class, "id", scId);
		String queryStuSql = "select stu.selftel_f,stu.channelid_f,stu.userid_f,stu.android_f from studentinfo_t stu where stu.name_f = '"+ sco.getsName() +"' and stu.org_f = " + bjzr;
		List list = dao.getHelperDao().find(queryStuSql);
		try {
			if(null != list && list.size() != 0){
				PushMsgModel model = new PushMsgModel();
				model.setTitle("成绩通知");
				model.setDescription("尊敬的家长您好,学生最近考试成绩已发布,请查看!");
				Object[] object = (Object[]) list.get(0);
				//推送
				if(!StringUtil.isEmpty(object[1]) && !StringUtil.isEmpty(object[2]) && !StringUtil.isEmpty(object[3])){
					if("1".equals(object[3].toString())){
						//PushUtil.androidPushUserMsg(Long.parseLong(object[1].toString()), object[2].toString(), model);
					}else if("0".equals(object[3].toString())){
						//PushUtil.iosPushUserMsg(Long.parseLong(object[1].toString()), object[2].toString(), false, model.getTitle());
					}
				}
				//短信
				if(!StringUtil.isEmpty(object[0])){
					StringBuffer smscontent = new StringBuffer(sco.getTitle());
					smscontent.append(sco.getsName());
					smscontent.append(":");
					
					/** search one subject score */
					String subjectAndColName = "select scm.colname_f,sub.name_f from scoresubjectmapping_t scm,subjectmapping_t sm,subjectinfo_t sub where " +
							"scm.subject_f = sub.id_f and sm.subjectinfo_f = sub.id_f and sm.org_f = ?";
					List<Object[]> subjectAndColNameList = dao.getHelperDao().find(subjectAndColName,String.valueOf(bjzr));
					if(null != subjectAndColNameList && subjectAndColNameList.size() > 0){
						Object[] objs =  null;
						String score_str = "";
						for (int i = 0; i < subjectAndColNameList.size(); i++) {
							objs = (Object[]) subjectAndColNameList.get(i);
							score_str = String.valueOf(sco.getClass().getDeclaredMethod("get"+objs[0]).invoke(sco));
							if(!StringUtil.isEmpty(score_str) && !"null".equalsIgnoreCase(score_str)){
								smscontent.append(objs[1]);
								smscontent.append(score_str.replace(".0", ""));
								smscontent.append(",");
							}
						}
					}
					
					smscontent.append("总分");
					smscontent.append(sco.getTotalScore());
					smscontent.append(",");
					if(!StringUtil.isEmpty(sco.getClassOrder())){
						smscontent.append("班排");
						smscontent.append(sco.getClassOrder());
						smscontent.append(",");
					}
					if(!StringUtil.isEmpty(sco.getGradeOrder())){
						smscontent.append("年排");
						smscontent.append(sco.getGradeOrder());
						smscontent.append(",");
					}
					/** 目前评语10个字符*/
					if(!StringUtil.isEmpty(comment)){
						smscontent.append(comment);
						smscontent.append(",");
					}
					SessionModal sessionModel = currentSessionModel();
					//SendSMS.sendMsg(smscontent.substring(0, smscontent.lastIndexOf(",")), object[0].toString(),sessionModel.getId(),sessionModel.getName(),new Long[]{null,sco.getId()});
				}
				sco.setBzrComments(comment);
				sco.setPublishStatus(true);
				dao.update(sco);
				dao.getHelperDao().excute("update attach_t att set att.desc_f = REPLACE(att.desc_f, '#0','#1') " +
				 		"where instr(att.filename_f,?) > 0 and  att.desc_f is not null and att.reltype_f = 'TeacherImportScore'", sco.getTitle()+".");
				am.setMsg("添加成功并已发送！");
			}else{
				am.setMsg("成绩发布失败！");
				am.setType(AjaxMsg.ERROR);
			}
		} catch (Exception e) {
			am.setMsg("添加评语失败！");
			am.setType(AjaxMsg.ERROR);
		}
		return am;
	}
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-5-30 下午1:22:16
	 *@version 1.0
	 *@Description 查询一个班级中所有考试的标题和状态
	 *
	 *@param mode
	 *@param classId
	 *@return
	 *
	 *
	 */
	public JqGridData<?> findAlltitleByClassId(JqPageModel mode, Long classId) {
		JqGridData<ScoreQueryMode> jqdt = new JqGridData<ScoreQueryMode>();
		List<ScoreQueryMode> scoreList = new ArrayList<ScoreQueryMode>();
		PageList page = null;
		if(null == classId) {
			return jqdt;
		}
		/** 先一个班主任只带一个班 */
		Org org = dao.findOne(Org.class, "id", classId);
		/*String sql = "select sc.title_f,decode(sign(sum(sc.publishstatus_f)-1),-1,0,0,1,1,1) " +
				"from score_t sc where sc.sclass_f = '"+ org.getCode() +"' group by sc.title_f";*/
		String sql = "select r.*,a.id_f,a.suffix_f from (select sc.title_f,CASE WHEN sign(sum(sc.publishstatus_f)-1) = -1 then 0 else 1 end pub from score_t sc where " +
				" sc.title_f like '"+ org.getName().substring(0, 1) +"%' and sc.sclass_f = '"+ org.getCode() +"' group by sc.title_f) r left join attach_t a  " +
		" on instr(a.desc_f,r.title_f) > 0 where  a.desc_f is not null and a.reltype_f = 'TeacherImportScore' order by a.id_f desc";
		//page = dao.getHelperDao().page(mode.getPage(), mode.getRows(),sql);
		page = dao.getHelperDao().pageByTotal(mode.getPage(), mode.getRows(),sql, "select count(1) from (select 1 from score_t sc where sc.sclass_f = '"+ org.getCode() +"' group by sc.title_f) a");
		List list = page.getList();
		if(null == list || list.size() == 0){
			return jqdt;
		}
		for (int i = 0; i < list.size(); i++) {
			ScoreQueryMode sqmode = new ScoreQueryMode();
			Object[] object = (Object[]) list.get(i);
			sqmode.setId(Long.parseLong(String.valueOf(i)));
			sqmode.setClassCode(org.getCode());
			if (null != object[0]) {
				sqmode.setTitle(object[0].toString());
			}
			if (null != object[1]) {
				sqmode.setPublishStatus(object[1].toString());
			}
			/** 此处为 附件id*/
			if (!StringUtil.isEmpty(object[2])) {
				sqmode.setClassOrder(object[2].toString());
			}
			/** 此处为 附件后缀*/
			if (null != object[3]) {
				sqmode.setClassAsc(object[3].toString());
			}
			scoreList.add(sqmode);
		}
		page.setResult(scoreList);
		jqdt.setPage(page.getPageNo());
		jqdt.setRecords(page.getTotalCount());
		jqdt.setRows(page.getList());
		jqdt.setTotal(page.getPageCount());
		return jqdt;
	}

	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-6-3 上午10:59:20
	 *@version 1.0
	 *@Description 教师查询具体某一位学生的成绩
	 *
	 *@param classId
	 *@param mode
	 *@param name
	 *@return
	 *
	 *
	 */
	@Transactional
	public JqGridData<ScoreQueryMode> getStuInfoByClass(Long classId,
			JqPageModel mode, String name) {
		JqGridData<ScoreQueryMode> jqdt = new JqGridData<ScoreQueryMode>();
		List<ScoreQueryMode> testIder = new ArrayList<ScoreQueryMode>();
		ScoreQueryMode scoremode = null;
		PageList page = null;
		String grandClass = "";
		if (null != classId && !"".equals(classId)) {
			Org org = (common.cq.hmq.pojo.sys.Org) dao.findOne(
					"from Org o where o.id = ?", classId);
			grandClass = org.getCode();

		}
		String subSql = "select scm.colname_f,sub.name_f from scoresubjectmapping_t scm,subjectmapping_t sm,subjectinfo_t sub where" +
				" scm.subject_f = sub.id_f and sm.subjectinfo_f = sub.id_f and sm.org_f = '"+ classId +"' order by scm.colname_f asc";
		List subList = dao.getHelperDao().find(subSql);
		List<String>  subjectNameList = new ArrayList<String>(); 
		String colNameStrs = "";
		if (subList.size() != 0) {
			Object[] object =  null;
			for (int i = 0; i < subList.size(); i++) {
				object = (Object[]) subList.get(i);
				colNameStrs += "scro." + object[0].toString() + "_f,";
				subjectNameList.add(object[1].toString());
			}
		}else{
			return jqdt;
		}
		String querySql = "select scro.title_f,scro.sno_f,scro.sname_f,"
				+ colNameStrs+ "scro.totalscore_f," 
				+ "scro.classorder_f,scro.classasc_f,scro.gradeorder_f,scro.gradeasc_f "
				+ "from score_t scro where scro.sclass_f = '" + grandClass
				+ "' and scro.sname_f = '" + name
				+ "'  order by scro.importdate_f desc";
		page = dao.getHelperDao().page(mode.getPage(), mode.getRows(),
				querySql);
		List list = page.getList();
		if (null == list || list.size() == 0) {
			return jqdt;
		}
		String subValue = "";
		for (int i = 0; i < list.size(); i++) {
			Object[] object = (Object[]) list.get(i);
			scoremode = new ScoreQueryMode();
			scoremode.setId(Long.parseLong(String.valueOf(i)));
			if (null != object[0]) {
				scoremode.setTitle(object[0].toString());
				
			}
			if (null != object[1]) {
				scoremode.setNo(object[1].toString());
			}
			if (null != object[2]) {
				scoremode.setName(object[2].toString());
			}
			
			for (int j = 0; j < subjectNameList.size(); j++) {
				if(!StringUtil.isEmpty(object[3+j])){
					subValue += subjectNameList.get(j) + ":"
					+ object[3+j].toString() + ", ";
				}
			}
			scoremode.setScoreA(subValue);
			subValue = "";

			if (!StringUtil.isEmpty(object[3+subjectNameList.size()])) {
				scoremode.setTotalScore(object[3+subjectNameList.size()].toString());
			}
			if (!StringUtil.isEmpty(object[4+subjectNameList.size()])) {
				scoremode.setClassOrder(object[3+subjectNameList.size()+1].toString());
			}
			if (!StringUtil.isEmpty(object[5+subjectNameList.size()])) {
				scoremode.setClassAsc(object[5+subjectNameList.size()].toString());
			}
			if (!StringUtil.isEmpty(object[6+subjectNameList.size()])) {
				scoremode.setDrandOrder(object[6+subjectNameList.size()].toString());
			}
			if (!StringUtil.isEmpty(object[7+subjectNameList.size()])) {
				scoremode.setDrandAsc(object[7+subjectNameList.size()].toString());
			}
			testIder.add(scoremode);
		}
		page.setResult(testIder);
		jqdt.setPage(page.getPageNo());
		jqdt.setRecords(page.getTotalCount());
		jqdt.setRows(page.getList());
		jqdt.setTotal(page.getPageCount());
		logRecord(LogRecord.QUERY, "家长或者老师查询具体某一班级的所有学生信息成绩");
		return jqdt;
	}

	/**
	 * 
	 * 
	 * 
	 *@title
	 *@author Limit
	 *@date 2014年9月10日 上午10:33:22
	 *@version 1.0
	 *@Description 查询发送成绩的短信内容和状态
	 *
	 *@return
	 *
	 *
	 */
	public JqGridData<?> findByNoticeId(JqPageModel model, Long id,
			String title) {
		String sql = "select o.code_f from org_t o where o.id_f = ?";
		List codeList = dao.getHelperDao().find(sql, id);
		JqGridData jq = new JqGridData();
		if(null == codeList || 0 == codeList.size()){
			return jq;
		}
		String querySql = "select t.sname_f, ssc.content_f, ssc.smsid_f, ssc.date_f,sta.mark_f,sta.recipient_f,sta.report_f\n" +
						" from (select sc.id_f,sc.sname_f\n" + 
						"         from score_t sc\n" + 
						"        where sc.sclass_f = '"+ codeList.get(0).toString() +"'\n" + 
						"          and sc.title_f = '"+ title +"') t\n" + 
						" join smsstatecontent_t ssc on ssc.scoreid_f = t.id_f and ssc.scoreid_f is not null  join smsstate_t sta on sta.smsid_f =  ssc.smsid_f";
       
		String totalSql = "select count(1)\n" +
						"  from (select sc.id_f,sc.sname_f\n" + 
						"          from score_t sc\n" + 
						"         where sc.sclass_f = '"+ codeList.get(0).toString() +"'\n" + 
						"           and sc.title_f = '"+ title +"') t\n" + 
						"  join smsstatecontent_t ssc on ssc.scoreid_f = t.id_f and ssc.scoreid_f is not null  join smsstate_t sta on sta.smsid_f =  ssc.smsid_f";

        PageList pageList = dao.getHelperDao().pageByTotal(model.getPage(), model.getRows(), querySql, totalSql);
        if(null == pageList){
        	return jq;
        }
        List list = pageList.getList();
        Map<String,Object> resMap = null;
        List resList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
        	Object[] object = (Object[]) list.get(i);
        	resMap = new HashMap<String, Object>();
        	resMap.put("smsId", object[2].toString());//短信id
        	resMap.put("name", object[0].toString());//学生姓名
        	resMap.put("recipient", object[5].toString());//学生电话
        	resMap.put("content", object[1].toString());//短信内容
        	resMap.put("sendTime", object[3].toString());//发送时间
        	if(null != object[4] && !"".equals(object[4].toString())){
        		resMap.put("mark", object[4].toString());//报告标识
        	}else{
        		resMap.put("mark", 3);//报告标识
        	}
        	if(null != object[6] && !"".equals(object[6].toString())){
        		resMap.put("report", object[6].toString());//状态报告值
        	}else{
        		resMap.put("report", "无");//状态报告值
        	}
        	resList.add(resMap);
		}
        pageList.setResult(resList);
        jq.setPage(pageList.getPageNo());
        jq.setRecords(pageList.getTotalCount());
        jq.setRows(pageList.getList());
        jq.setTotal(pageList.getPageCount());
        return jq;
	}

	/**
     * 
     * 
     * 
     *@title
     *@author Limit
     *@date 2014年9月19日 上午10:17:05
     *@version 1.0
     *@Description  上传的成绩通过上传人直接发送
     *
     *@return
	 * @throws ChannelServerException 
	 * @throws ChannelClientException 
	 * @throws NumberFormatException 
     *
     *
     */
	@Transactional
	public AjaxMsg publishScoreByImportter(String title) throws NumberFormatException, ChannelClientException, ChannelServerException {
		AjaxMsg msg = new AjaxMsg();
		msg.setType(0);
		if(null == title || "".equals(title)){
			return msg;
		}

		String sql = "select og.id_f from org_t og where\n" +
		"  og.code_f in (select sc.sclass_f from score_t sc where sc.title_f = '"+ title +"' group by sc.sclass_f)\n" + 
		"  and og.name_f like '"+ title.substring(0, 1) +"%' and og.type_f = 3";
		List<Object> list = dao.getHelperDao().find(sql);
		if(null == list || list.size() == 0){
			msg.setType(1);//没有查询到班级信息
			return msg;
		}
		AjaxMsg returnmsg = null;
		StringBuffer sb = new StringBuffer("未发布的同学有");
		
		for (Object object : list) {
			returnmsg = updateAnnounceStatus(Long.parseLong(object.toString()),title);
			if(null != returnmsg && !"没有找到需要发布的成绩!".equals(returnmsg.getMsg()) && !StringUtil.isEmpty(returnmsg.getMsg())){
				String[] split = returnmsg.getMsg().split(":");
				sb.append(split[1]);
			}
		}
		
		if("未发布的同学有".equals(sb.toString())){
			msg.setType(2);//成绩发布成功
			return msg;
		}
		sb.append(":可能由姓名差异、联系号码异常等原因导致,请联系管理员后再发布。");
		msg.setType(100);
		msg.setMsg("部分发送完成,"+sb.toString());
		return msg;
	}
}
