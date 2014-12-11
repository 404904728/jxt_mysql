package core.cq.hmq.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.identity.NativeGroupQuery;
import org.activiti.engine.identity.NativeUserQuery;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;

import core.cq.hmq.dao.Dao;
import core.cq.hmq.util.tools.Encrypt;


/**
 * 重构activiti 的identity
 * 
 * @author Administrator
 * 
 */
public class IdentityServiceImpl implements IdentityService {

	@Resource
	protected Dao dao;

	/**
	 * 检查密码是否为相等密码 Checks if the password is valid for the given
	 */
	public boolean checkPassword(String userId, String password) {
		// TODO Auto-generated method stub
		User user = dao.findOne(User.class, "id", Long.parseLong(userId));
		if (user == null) {
			return false;
		}
		if (user.getPassword().equals(Encrypt.md5(password))) {
			return true;
		}
		return false;
	}

	@Override
	public GroupQuery createGroupQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 创建一个成员身份 userId - the userId, cannot be null. groupId - the groupId,
	 * cannot be null.
	 */
	@Override
	public void createMembership(String userId, String groupId) {
		// TODO Auto-generated method stub
		System.out.println("createMembership");

	}

	/**
	 * 返回一个native任务
	 */
	@Override
	public NativeGroupQuery createNativeGroupQuery() {
		// TODO Auto-generated method stub
		System.out.println("createNativeGroupQuery");
		return null;
	}

	@Override
	public NativeUserQuery createNativeUserQuery() {
		// TODO Auto-generated method stub
		System.out.println("createNativeUserQuery");
		return null;
	}

	@Override
	public UserQuery createUserQuery() {
		// TODO Auto-generated method stub
		System.out.println("createUserQuery");
		return null;
	}

	@Override
	public void deleteGroup(String arg0) {
		// TODO Auto-generated method stub
		System.out.println("deleteGroup");

	}

	@Override
	public void deleteMembership(String arg0, String arg1) {
		// TODO Auto-generated method stub
		System.out.println("deleteMembership");

	}

	@Override
	public void deleteUser(String arg0) {
		// TODO Auto-generated method stub
		System.out.println("deleteUser");

	}

	@Override
	public void deleteUserInfo(String arg0, String arg1) {
		// TODO Auto-generated method stub
		System.out.println("deleteUserInfo");

	}

	@Override
	public String getUserInfo(String arg0, String arg1) {
		// TODO Auto-generated method stub
		System.out.println("getUserInfo");
		return null;
	}

	@Override
	public List<String> getUserInfoKeys(String arg0) {
		// TODO Auto-generated method stub
		System.out.println("getUserInfoKeys");
		return null;
	}

	@Override
	public Picture getUserPicture(String arg0) {
		// TODO Auto-generated method stub
		System.out.println("getUserPicture");
		return null;
	}

	@Override
	public Group newGroup(String arg0) {
		// TODO Auto-generated method stub
		System.out.println("newGroup");
		return null;
	}

	@Override
	public User newUser(String arg0) {
		// TODO Auto-generated method stub
		System.out.println("newUser");
		return null;
	}

	@Override
	public void saveGroup(Group arg0) {
		System.out.println("saveGroup");
		// TODO Auto-generated method stub

	}

	@Override
	public void saveUser(User user) {
		System.out.println("saveUser");
		// TODO Auto-generated method stub

	}

	@Override
	public void setAuthenticatedUserId(String arg0) {
		System.out.println("setAuthenticatedUserId");
		// TODO Auto-generated method stub

	}

	@Override
	public void setUserInfo(String arg0, String arg1, String arg2) {
		System.out.println("setUserInfo");
		// TODO Auto-generated method stub

	}

	@Override
	public void setUserPicture(String arg0, Picture arg1) {
		System.out.println("setUserPicture");
		// TODO Auto-generated method stub

	}

}
