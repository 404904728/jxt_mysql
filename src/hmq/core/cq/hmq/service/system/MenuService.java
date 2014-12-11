package core.cq.hmq.service.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import common.cq.hmq.pojo.sys.Menu;
import common.cq.hmq.service.UserService;
import core.cq.hmq.modal.SessionModal;
import core.cq.hmq.service.BaseService;

@Service(value = "menuService")
public class MenuService extends BaseService {

	@Resource
	private UserService userService;

	@SuppressWarnings("unchecked")
	public List<Menu> findData(Long pId) {
		List<Menu> menus = new ArrayList<Menu>();
		if (pId == null) {
			menus = dao.find("from " + Menu.cls() + " m where m.pid is null");
		} else {
			menus = dao.find(Menu.class, "pid.id", pId);
		}
		return menus;
	}

	/**
	 * 
	 * @param moduleId
	 *            模块id
	 * @param pId
	 *            menu父亲id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findMenu(Long pId) {
		List<Menu> menus = new ArrayList<Menu>();
		if (pId == null) {
			menus = dao.find("from " + Menu.cls()
					+ " m where m.pid is null order by m.order asc");
		} else {
			menus = dao.find("from " + Menu.cls()
					+ " m where m.pid.id=? order by m.order asc", pId);
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Menu menu : menus) {
			if (menu.getPermission() == null) {
				list.add(conversion(menu));// 菜单不需要权限时，所有用户都可以操作
			} else {
				if (userService.findPerByUserId(currentUserId()).contains(
						menu.getPermission().getId().toString())) {// 如果当前登录用户包含该菜单权限，则添加到JSON中去
					list.add(conversion(menu));
				}
			}
		}
		return list;
	}

	/**
	 * 
	 * @param moduleId
	 *            模块id
	 * @param pId
	 *            menu父亲id
	 * @param pId
	 *            角色ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findPermissionZtree(Long pId, Long rId) {
		List<Menu> menus = new ArrayList<Menu>();
		if (pId == null) {
			menus = dao.find("from " + Menu.cls()
					+ " m where m.pid is null order by m.order asc");
		} else {
			menus = dao.find("from " + Menu.cls()
					+ " m where m.pid.id=? order by m.order asc", pId);
		}

		List<Long> allPerId = permissionService.findByRorleId(rId);// 查询该角色下的所有权限

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Menu menu : menus) {
			if (menu.getPermission() != null) {
				list.add(conversionZtree(menu, allPerId));// 菜单不需要权限时，所有用户都可以操作
			}
		}
		return list;
	}

	/**
	 * 查找所有菜单
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findMenuByAce() {
		List<Menu> menus = new ArrayList<Menu>();
		menus = dao.find("from " + Menu.cls()
				+ " m where m.pid is null and m.use=true order by m.order asc");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		SessionModal sModel = currentSessionModel();
		boolean b = true;// 默认老师
		if (sModel.getUserType().equals("1"))
			b = false;
		Set<String> setPer = userService.findPerByUserIdAndType(sModel.getId(),
				b);
		for (Menu menu : menus) {
			if (menu.getPermission() == null) {
				list.add(conversionACE(menu, setPer));// 菜单不需要权限时，所有用户都可以操作
			} else {
				if (setPer.contains(menu.getPermission().getId().toString())) {// 如果当前登录用户包含该菜单权限，则添加到JSON中去
					list.add(conversionACE(menu, setPer));
				}
			}
		}
		return list;
	}

	/**
	 * 把menu 转换成ace界面框架所需的格式
	 * 
	 * @param menu
	 * @return
	 */
	private Map<String, Object> conversionACE(Menu menu, Set<String> setPer) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", menu.getId());
		map.put("name", menu.getName());
		map.put("icon", menu.getIcon());
		map.put("url", menu.getUrl());
		List<Menu> menuSon = findSonACE(menu.getId());// 查找儿子
		if (menuSon.size() > 0) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			int size = 0;
			for (Menu menu2 : menuSon) {
				if (!menu2.getUse())
					continue;
				if (menu2.getPermission() == null
						|| setPer.contains(menu2.getPermission().getId()
								.toString())) {
					Map<String, Object> mapSon = new HashMap<String, Object>();
					mapSon.put("id", menu2.getId());
					mapSon.put("name", menu2.getName());
					mapSon.put("icon", menu2.getIcon());
					mapSon.put("url", menu2.getUrl());
					list.add(mapSon);
					size++;
				}
			}
			map.put("childsize", size);
			map.put("child", list);
		}
		return map;
	}

	/**
	 * 把menu 转换成easyui所需的json格式
	 * 
	 * @param menu
	 * @return
	 */
	private Map<String, Object> conversion(Menu menu) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", menu.getId());
		map.put("text", menu.getName());
		map.put("attributes", menu.getUrl());
		if (findSon(menu.getId())) {
			map.put("state", "closed");
		} else {
			map.put("state", "open");
		}
		return map;
	}

	@Autowired
	private PermissionService permissionService;

	/**
	 * 把menu 转换成ztree所需的json格式
	 * 
	 * @param menu
	 * @return
	 */
	private Map<String, Object> conversionZtree(Menu menu, List<Long> allPerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", menu.getId());
		map.put("name", menu.getName());
		map.put("perId", menu.getPermission().getId());
		map.put("icon", "./res/img/icon/school.png");
		if (allPerId.contains(menu.getPermission().getId())) {
			map.put("checked", true);// 选中
		}
		if (findSon(menu.getId())) {
			map.put("open", false);
			map.put("isParent", "true");
		} else {
			map.put("isParent", false);
		}
		return map;
	}

	// @Resource
	// private ProcessService processService;

	/**
	 * 查询流程绑定的菜单
	 * 
	 * @param proPid
	 * @return
	 */
	public List<Map<String, Object>> findProcessMenu(Long proPid) {
		List<Map<String, Object>> mapMenu = new ArrayList<Map<String, Object>>();// 存储转换后的菜单
		List<Menu> menus = dao.find(Menu.class, "pid.id", proPid);
		for (Menu menu : menus) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", menu.getId());
			map.put("name", menu.getName());
			map.put("url", menu.getUrl());
			// if (menu.getProId() != null) {
			// List<Map<String, String>> listPro = processService
			// .findProcessDefinition(menu.getProId());
			// map.put("proName", listPro.get(0).get("name"));
			// map.put("proState", listPro.get(0).get("state"));
			// map.put("proVersion", listPro.get(0).get("version"));
			// }
			map.put("permission", menu.getPermission());
			map.put("use", menu.getUse() ? "可用" : "不可用");
			mapMenu.add(map);
		}
		return mapMenu;
	}

	/**
	 * 是否有子菜单
	 * 
	 * @param Pid
	 * @return
	 */
	private boolean findSon(Long pid) {
		List<Menu> menus = dao.find("from " + Menu.class.getName()
				+ " m where m.pid.id=?", pid);
		return menus.size() == 0 ? false : true;
	}

	/**
	 * 查询子菜单
	 * 
	 * @param Pid
	 * @return
	 */
	private List<Menu> findSonACE(Long pid) {
		return dao.find("from " + Menu.class.getName() + " m where m.pid.id=?",
				pid);
	}

	public Menu findOne(Long id) {
		return dao.findOne(Menu.class, "id", id);
	}
}
