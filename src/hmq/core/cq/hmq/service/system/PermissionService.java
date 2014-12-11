package core.cq.hmq.service.system;

import java.util.List;

import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.pojo.sys.Permission;
import core.cq.hmq.pojo.sys.Role;
import core.cq.hmq.pojo.sys.Role2Permission;
import core.cq.hmq.service.TestService;

@Service(value = "permissionService")
public class PermissionService extends TestService {

	/**
	 * // 根据角色id查找所有权限，且表示该角色已经有的权限
	 */
	public List<Permission> findAll(Long id) {
		List<Permission> pers = dao.find(Permission.class);// 查找出所有权限
		List<Role2Permission> r2ps = dao.find(Role2Permission.class, "role.id",
				id);// 查找该角色下的权限
		for (Role2Permission role2Permission : r2ps) {
			for (Permission permission : pers) {
				if (permission.getId() == role2Permission.getPermission()
						.getId()) {
					permission.setChecked(true);
				}
			}
		}
		return pers;
	}

	/**
	 * // 根据角色id查找权限
	 */
	@SuppressWarnings("unchecked")
	@Test
	public List<Long> findByRorleId(Long id) {
		List<Long> list = dao.find("select permission.id from "
				+ Role2Permission.class.getName() + " where role.id=?", id);
		return list;
	}

	@Transactional()
	public void roleGantPer(Long id, String ids) {
		// TODO Auto-generated method stub
		String[] perId = ids.split(",");
		List<Role2Permission> r2ps = dao.find(Role2Permission.class, "role.id",
				id);
		if (r2ps.size() > 0) {
			dao.delete(r2ps);
		}
		for (int i = 0; i < perId.length; i++) {
			Role2Permission r2p = new Role2Permission();
			Role role = new Role();
			role.setId(id);
			r2p.setRole(role);
			Permission permission = new Permission();
			permission.setId(Long.parseLong(perId[i]));
			r2p.setPermission(permission);
			dao.insert(r2p);
		}
	}

}
