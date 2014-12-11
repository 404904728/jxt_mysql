package app.cq.hmq.controller.appcontroller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import common.cq.hmq.pojo.sys.User;

import app.cq.hmq.pojo.leave.LeaveInfo;
import app.cq.hmq.service.appservice.LeaveInterFaceService;

import core.cq.hmq.annotation.ControllerAnn;
import core.cq.hmq.controller.core.BaseController;
import core.cq.hmq.dao.PageList;
import core.cq.hmq.modal.AjaxMsg;

@Controller
@RequestMapping(value = "/leaveInterFace")
public class LeaveInterFaceController extends BaseController{
	
	@Resource
	private LeaveInterFaceService leaveInterFaceService;
	
	/**
	 * 获取记录列表
	 * 
	 * @return
	 */
	@RequestMapping(params = "obtainLeaveInfos")
	@ControllerAnn(toLogon = false)
	@ResponseBody
	public PageList<?> obtainLeaveInfos(String lk,Long classId,Long apperId,String apperType,int ps,int pn){
		if(User.LKEY.equals(lk)){
			return leaveInterFaceService.obtainLeaveInfos(classId, apperId, apperType, ps, pn);
		}else{
			return null;
		}
	}
	
	/**
	 * 新增请假记录
	 * 
	 * @return
	 */
	@RequestMapping(params = "saveLeaveInfo")
	@ControllerAnn(toLogon = false)
	@ResponseBody
	public AjaxMsg saveLeaveInfo(String lk,LeaveInfo lev){
		if(User.LKEY.equals(lk)){
			return leaveInterFaceService.saveLeaveInfo(lev);
		}else{
			return null;
		}
	}
	
	/**
	 * 新增请假记录
	 * 
	 * @return
	 */
	@RequestMapping(params = "updateLeaveInfo")
	@ControllerAnn(toLogon = false)
	@ResponseBody
	public AjaxMsg updateLeaveInfo(String lk,Long lid,int status){
		if(User.LKEY.equals(lk)){
			return leaveInterFaceService.updateLeaveInfo(lid,status);
		}else{
			return null;
		}
	}
}
