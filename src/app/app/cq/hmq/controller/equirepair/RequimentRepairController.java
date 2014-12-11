/**
 * Limit
 *
 */
package app.cq.hmq.controller.equirepair;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import app.cq.hmq.pojo.equirepair.EquiRepair;
import app.cq.hmq.service.requirepair.RequimentRepairService;
import core.cq.hmq.controller.core.BaseController;
import core.cq.hmq.modal.AjaxMsg;

/**
 * @author Administrator
 * 设备报修
 */
@Controller
@RequestMapping(value = "/requimentRepair")
public class RequimentRepairController extends BaseController {
	
	
	@Autowired
	private RequimentRepairService reqRepairService;
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-3-21 下午4:32:03
	 *@version 1.0
	 *@Description  查询具体一封设备维修的信息
	 *
	 *@param emailId
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "emailInfos_")
	public ModelAndView queryEmailInfos(HttpServletRequest request, Long emailId, String queryFlg){
		return reqRepairService.queryEmailInfos(request, emailId, queryFlg);
	}
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-3-21 下午5:09:28
	 *@version 1.0
	 *@Description 初始化填写上报邮件
	 *
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "writeRepairEmail_")
	public ModelAndView writeRepairEmail(HttpServletRequest request){
		return reqRepairService.writeRepairEmail(request);
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
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "queryReceiveEmail_")
	@ResponseBody
	public Map<String, Object> queryReceiveEmail(HttpServletRequest request,int rows,int page,String status,String recommon){
		Map<String, Object> queryReceiveEmail = null;
		try {
			queryReceiveEmail = reqRepairService.queryReceiveEmail(request, rows, page,status, recommon);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queryReceiveEmail;
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
	@RequestMapping(params = "submitFormInfo_")
	@ResponseBody
	public Map<String, Object> submitFormInfo(HttpServletRequest request,EquiRepair repair){
		return reqRepairService.submitFormInfo(request, repair);
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
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "deleteEuqiEmail_")
	@ResponseBody
	public AjaxMsg deleteEuqiEmail(Long emailId, String status){
		return reqRepairService.deleteEuqiEmail(emailId, status);
	}
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-3-25 上午9:19:15
	 *@version 1.0
	 *@Description 更改设备维修状态
	 *
	 *@param emailId
	 *@return
	 *
	 *
	 */
	@RequestMapping(params = "updateBackStatus_")
	@ResponseBody
	public AjaxMsg updateBackStatus(HttpServletRequest request, Long emailId, String state){
		return reqRepairService.updateBackStatus(request,emailId,state);
	}
	
	
}
