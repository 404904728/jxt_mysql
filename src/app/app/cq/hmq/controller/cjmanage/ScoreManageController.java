/**
 * Limit
 *
 */
package app.cq.hmq.controller.cjmanage;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;

import app.cq.hmq.mode.ScoreQueryMode;
import app.cq.hmq.mode.UtilsMode;
import app.cq.hmq.pojo.cjmanage.CJManage;
import app.cq.hmq.pojo.notice.SmsState;
import app.cq.hmq.service.cjmanage.ScoreManageService;
import app.cq.hmq.service.teainfo.TeacherInfoService;
import common.cq.hmq.model.JqGridData;
import common.cq.hmq.model.JqPageModel;
import common.cq.hmq.pojo.sys.Attach;
import common.cq.hmq.service.AttachService;
import common.cq.hmq.service.OrgService;
import core.cq.hmq.annotation.ControllerAnn;
import core.cq.hmq.controller.core.DateBaseController;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.modal.SessionModal;

/**
 * @author Limit
 *
 */
@Controller
@RequestMapping(value = "/scoreManage")
public class ScoreManageController extends DateBaseController {

	@Autowired
	private ScoreManageService scoreManageService;
	@Resource
	private TeacherInfoService teacherInfoService;
	
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-3-17 下午3:38:29
	 *@version 1.0
	 *@Description 查询具体某一个班级中考试的科目
	 *
	 *
	 *
	 */
	@RequestMapping(params = "querySub_")
	@ResponseBody
	public List<UtilsMode> querySubInfo(HttpServletRequest request, String status, Long classId){
		return scoreManageService.querySubInfo(request,status,classId);
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
	@RequestMapping(params = "StuClassinfo_")
	@ResponseBody
	public Map<String, Object> queryStuClass(HttpServletRequest request) {
		return scoreManageService.queryStuClass(request);
	}
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-3-17 下午3:38:29
	 *@version 1.0
	 *@Description 查询具体某一个班级中考试的科目
	 *
	 *
	 *
	 */
	@RequestMapping(params = "queryksnr_")
	@ResponseBody
	public List<UtilsMode> queryksnr(HttpServletRequest request, Long subID,Long classId){
		return scoreManageService.queryksnr(request,subID,classId);
	}
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-3-11 上午11:27:35
	 *@version 1.0
	 *@Description 点击成绩管理后初始化班级下拉框
	 *
	 *@param request
	 *
	 *
	 */
	@RequestMapping(params = "classinfo_")
	@ResponseBody
	public List<UtilsMode> queryClassInfo(HttpServletRequest request){
		return scoreManageService.queryClassInfo(request);
	}
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-3-11 下午1:30:20
	 *@version 1.0
	 *@Description 家长或者老师查询具体某一班级的所有学生信息成绩
	 *
	 *@param request
	 *@param id
	 *@param page
	 *@param rows
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "queryClass_1")
	@ResponseBody
	public JqGridData<ScoreQueryMode> getStuInfoToClass(HttpServletRequest request,Long classId, String kaoshiSub, String kaoshinr, JqPageModel mode,String status){
		JqGridData<ScoreQueryMode> stuInfoToClass = null;
		try {
			stuInfoToClass = scoreManageService.getStuInfoToClass(request, classId, kaoshiSub, kaoshinr, mode,status);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stuInfoToClass;
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
	@RequestMapping(params = "queryClass_2")
	@ResponseBody
	public JqGridData<ScoreQueryMode> getStuInfoByClass(Long classId, JqPageModel mode,String name){
		JqGridData<ScoreQueryMode> stuInfoToClass = null;
		try {
			stuInfoToClass = scoreManageService.getStuInfoByClass(classId, mode,name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stuInfoToClass;
	}
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-3-11 下午1:30:20
	 *@version 1.0
	 *@Description 添加学生成绩
	 *
	 *@param request
	 *@param id
	 *@param page
	 *@param rows
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "addStuScore_")
	public ModelAndView addStuScore(HttpServletRequest request){
		return scoreManageService.addStuScore(request);
		
	}
	
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-3-11 下午1:30:20
	 *@version 1.0
	 *@Description 验证新增的成绩中内同是否存在
	 *
	 *@param request
	 *@param id
	 *@param page
	 *@param rows
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "regToNR_")
	@ResponseBody
	public List<UtilsMode> isExistForNeiRong(Long classId, Long subId, String nr,String ksDate){
		return scoreManageService.isExistForNeiRong(classId, subId, nr,ksDate);
		
	}
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-3-11 下午1:30:20
	 *@version 1.0
	 *@Description 点击新增按钮后返回一个班中所有学生信息
	 *
	 *@param request
	 *@param id
	 *@param page
	 *@param rows
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "queryOneClassStudentInfo_")
	@ResponseBody
	public JqGridData<CJManage> queryAllStudentsByClass(Long classId,String subId, String ksnr, Date ksDate,JqPageModel mode,String status){
		try {
			return scoreManageService.queryAllStudentsByClass(classId,subId, ksnr, ksDate, mode,status);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-3-11 下午1:30:20
	 *@version 1.0
	 *@Description 新增具体某一个学生的成绩信息
	 *
	 *@param request
	 *@param id
	 *@param page
	 *@param rows
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "saveData_")
	@ResponseBody
	public String saveStudentInfo(CJManage cj){
		
		return scoreManageService.saveStudentInfo(cj);
	}
	
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-3-19 下午4:36:33
	 *@version 1.0
	 *@Description 打开修改具体某位学生的成绩的页面
	 *
	 *@param cjManageId
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "queryStuToUpdate_")
	public ModelAndView updateScore(Long cjManageId){
		return scoreManageService.updateScore(cjManageId);
	}
	
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-3-19 下午4:36:33
	 *@version 1.0
	 *@Description 修改具体某位学生的成绩
	 *
	 *@param cjManageId
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "oneScoreToUpdate_")
	@ResponseBody
	public AjaxMsg updateScoreToOneStu(CJManage cj){
		return scoreManageService.updateScoreToOneStu(cj);
	}
	
	@Resource
	AttachService attachService;
	
	/**
	 * 
	 * @param attachID
	 * @param scoreType
	 * @param userType 为班级id 或者年级id（暂未用上）
	 * @return
	 */
	@RequestMapping(params = "importScoreXls")
	@ResponseBody
	//"C:/Users/Administrator/Desktop/2016届初中七年级2014年2月入学考试.xls"
	public AjaxMsg importScoreXls(Long attachID,int scoreType,Long userType){
		AjaxMsg am = new AjaxMsg();
		Attach a = null;
		try {
			if(null != attachID){
				a = attachService.findById(attachID);
				am = attachService.judgeFileExistForCj(a, "TeacherImportScore");
				if(2 == am.getType()){
					return am;
				}
				if(2 == scoreType){
					am = scoreManageService.importScoreXls(a,scoreType);
				}else if(1 == scoreType){
					am = scoreManageService.importScoreXlsWeek(a,scoreType);
				}
				if(am.getType() == AjaxMsg.SUCCESS){
					/** relid 存入导入成绩附件人的id */
					a.setRelId(currentSessionModel().getId());
					a.setRelType("TeacherImportScore");
					attachService.update(a);
				}else{
					attachService.delete(attachID);
				}
			}else{
				am.setMsg("导入失败...");
				am.setType(AjaxMsg.ERROR);
			}
		} catch (Exception e) {
			attachService.delete(attachID);
			e.printStackTrace();
			am.setMsg("导入错误!");
			am.setType(AjaxMsg.ERROR);
		}
		return am;
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	@RequestMapping(params = "paimingByweek")
	@ResponseBody
	public AjaxMsg paimingByweek(String str){
		return scoreManageService.paimingByweek(str);
	}
	
	/**
	 * File Uploader 上传插件跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "excelfileupload")
	public ModelAndView excelfileupload(Long bjzr,Long njzr) {
		ModelAndView mav = new ModelAndView("app/cjmanage/excelfileupload");
		mav.addObject("bjzr", bjzr);
		mav.addObject("njzr", njzr);
		return mav;
	}
	
	@RequestMapping(params = "lookimportfile")
	@ResponseBody
	public JqGridData<?> lookimportfile(JqPageModel mode){
		JqGridData<?> stuInfoToClass = null;
		try {
			stuInfoToClass = scoreManageService.lookimportfile(mode,currentSessionModel().getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stuInfoToClass;
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
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "queryimportappendix")
	@ResponseBody
	public JqGridData<?> queryImportAppendix(JqPageModel mode,String bjzr,String njzr){
		return scoreManageService.queryImportAppendix(mode,currentSessionModel().getId(),bjzr,njzr);
	}
	
	/**
	 * 查找某个班级所有未发布的学生成绩
	 * @param mode
	 * @param classId
	 * @return
	 */
	@RequestMapping(params = "findAllUnPublishScoreByClassCode")
	@ResponseBody
	public JqGridData<?> findAllUnPublishScoreByClassCode(JqPageModel mode,Long classId,String title){
		return scoreManageService.findAllUnPublishScoreByClassCode(mode,classId,0,title);
	}
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-5-30 下午1:07:01
	 *@version 1.0
	 *@Description 查询一个班级中所有考试的标题和状态
	 *
	 *@param mode
	 *@param classId
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "findAlltitleByClassId")
	@ResponseBody
	public JqGridData<?> findAlltitleByClassId(JqPageModel mode,Long classId){
		return scoreManageService.findAlltitleByClassId(mode,classId);
	}
	
	/**
	 * 为某个学生添加评语
	 * @param scId
	 * @param comment
	 * @return
	 */
	@RequestMapping(params = "addCommentsForScore")
	@ResponseBody
	public AjaxMsg addCommentsForScore(Long scId,String comment,Long bjzr){
		AjaxMsg am = new AjaxMsg();
		try {
			am = scoreManageService.addCommentsForScore(scId,comment,bjzr);
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
	 *@date 2014-4-17 下午3:59:24
	 *@version 1.0
	 *@Description 发布成绩并更改状态
	 *
	 *@param id 此处为班级id
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "updateAnnounceStatus_")
	@ResponseBody
	public AjaxMsg updateAnnounceStatus(Long id,String title){
		AjaxMsg updateAnnounceStatus = null;
		try {
			updateAnnounceStatus = scoreManageService.updateAnnounceStatus(id,title);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updateAnnounceStatus;
	}
	
	@Resource
    private	OrgService orgService;
	
	@RequestMapping(params = "analyCjByStudentNameAndOrg")
	@ResponseBody
	@ControllerAnn(toLogon = false)
	public Object analyCjByStudentNameAndOrg(Long orgId,String name){
		SessionModal sm = new SessionModal();
		if(null == orgId || null == name){
			sm = currentSessionModel();
		}else{
			sm.setOrgId(orgId);
			sm.setName(name);
		}
		return scoreManageService.analyCjByStudentNameAndOrg(sm.getOrgId(),
				orgService.findById(sm.getOrgId()).getCode(), sm.getName());
	}
	
	
	/**
	 * File Uploader 上传插件跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "queryScoreByTitle")
	public ModelAndView queryScoreByTitle(String title,String code,String bjzr) {
		SessionModal sessionModal = currentSessionModel();
		ModelAndView mav = new ModelAndView("app/cjmanage/cjpublish");
		/** 定义service 查询当前教师是否有导入权限 */
		Map<String, Object> resizeteaType = teacherInfoService
				.resizeteaType(sessionModal.getId());
		mav.addObject("title", title);
		mav.addObject("code", code);
		mav.addObject("bjzr", bjzr);
		/**
		 * 为教师在request中添加导入权限
		 */
		mav.addObject("drqx", resizeteaType.get("drqx"));
		mav.addObject("userType", sessionModal.getUserType());
		return mav;
	}
	/**
	 * 
	 * 
	 * 
	 *@title
	 *@author Limit
	 *@date 2014年9月9日 下午4:54:48
	 *@version 1.0
	 *@Description 查询发布成绩短信信息方法
	 *
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "showdetail")
    public ModelAndView showdetail(String title,Long id) {
        ModelAndView modelAndView = new ModelAndView("/app/cjmanage/messagedetail");
        modelAndView.addObject("title", title);
        modelAndView.addObject("classId", id);
        return modelAndView;
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
    @RequestMapping(params = "findDataDetail", method = RequestMethod.POST)
    @ResponseBody
    public JqGridData<?> findDataDetail(JqPageModel model, Long id,String title) {
        return scoreManageService.findByNoticeId(model, id,title);
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
     *
     *
     */
    @RequestMapping(params = "publishScoreByImportter", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMsg publishScoreByImportter(String title) {
    	AjaxMsg publishScoreByImportter = null;
    	try {
			publishScoreByImportter = scoreManageService.publishScoreByImportter(title);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return publishScoreByImportter;
    }
}
