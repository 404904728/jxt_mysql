package core.cq.hmq.service;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import common.cq.hmq.pojo.sys.User;

import core.cq.hmq.dao.DaoExtends;
import core.cq.hmq.dao.PageList;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.modal.EasyData;
import core.cq.hmq.modal.PageModel;
import core.cq.hmq.modal.SessionModal;
import core.cq.hmq.pojo.LogRecord;
import core.cq.hmq.util.tools.LogUtil;
import core.cq.hmq.util.tools.ResourceUtil;

@Service(value = "baseService")
// 控制所有继承于该类的service，让开发人员在方法上面写上@Transactional
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class BaseService extends DaoExtends {

	@Transactional
	public int initData(String sql) {
		int i = 0;
		try {
			i = dao.getHelperDao().excute("insert into  " + sql);
			LogUtil.getLog("xml数据加载成功，成功sql：").info(sql);
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.getLog("xml数据加载出错，出错sql：").info(sql);
		}
		return i;
	}

	/**
	 * 添加日志
	 * 
	 * @param type
	 *            类型
	 * @param content
	 *            内容
	 */
	protected void logRecord(int type, String content) {
		LogRecord logrecord = new LogRecord();
		logrecord.setType(type);
		logrecord.setContent(content);
		logrecord.setDate(new Date());
		logrecord.setUserId(currentUserId());
		dao.insert(logrecord);
	}

	/**
	 * 取出数据列的通用方法， 查询放到后面做-
	 * 
	 * @param model
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected EasyData findClass(PageModel model, Class cls) {
		PageList classes = page(model, cls);
		EasyData ed = new EasyData();
		ed.setRows(classes.getList());
		ed.setTotal(classes.getTotalCount());
		/**
		 * 报500错误时，打开看看什么错误 vaildJson(ed);
		 */
		// vaildJson(ed);
		return ed;
	}

	/**
	 * 验证json
	 * 
	 * @param ed
	 */
	// *由于在Spring MVC3 中通过jackson直接return
	// * object发生错误不会返回错误detail，让开发者不明白错误原因。
	// * 因此建议以后碰到此情况最好在return前用jackson的ObjectMapper进行一次json的序列化如有异常会有详细的错误信息。
	private void vaildJson(EasyData ed) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			LogUtil.getLog("提示").info(mapper.writeValueAsString(ed));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除对象
	 * 
	 * @param cls
	 * @param id
	 * @return
	 */
	@Transactional
	protected AjaxMsg deleteClass(Class cls, Long id) {
		AjaxMsg am = new AjaxMsg();
		try {
			dao.delete(cls, id);
			am.setMsg("删除成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			am.setMsg("删除失败：error" + e.getMessage());
		}
		return am;
	}

	@Transactional
	protected AjaxMsg inserClass(Object obj) {
		AjaxMsg am = new AjaxMsg();
		try {
			dao.insert(obj);
			am.setMsg("新增成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			am.setMsg("新增失败：error" + e.getMessage());
		}
		return am;
	}

	/**
	 * 获取当前用户的ID
	 * 
	 * @param session
	 * @return
	 */
	protected Long currentUserId() {
		HttpSession session1 = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest().getSession();
		SessionModal sModal = (SessionModal) session1.getAttribute(ResourceUtil
				.getSessionInfoName());
		return sModal.getId();
	}

	/**
	 * 获取当前用户session
	 * 
	 * @param session
	 * @return
	 */
	protected SessionModal currentSessionModel() {
		HttpSession session1 = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest().getSession();
		SessionModal sModal = (SessionModal) session1.getAttribute(ResourceUtil
				.getSessionInfoName());
		return sModal;
	}

	/**
	 * 获取当前用户用户
	 * 
	 * @param session
	 * @return
	 */
	protected User currentUser() {
		return dao.findOne(User.class, "id", currentUserId());
	}
}
