package core.cq.hmq.service.process;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import common.cq.hmq.service.UserService;

import core.cq.hmq.service.BaseService;


@Service(value = "processUelService")
@Transactional
public class ProcessUelService extends BaseService {

	@Resource
	private UserService userService;

	// 返回当前人物ID--必须要string类型，不然会报错
	public String findCurrentUserId() {
		return String.valueOf(currentUserId());
	}

	// 返回当前用户部门的领导
	public String findCurrentUserOrgLeader() {
		return String.valueOf(userService.findLeaderByCurrentUserId());
	}

}
