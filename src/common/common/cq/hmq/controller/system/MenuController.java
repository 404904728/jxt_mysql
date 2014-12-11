package common.cq.hmq.controller.system;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import core.cq.hmq.controller.core.BaseController;
import core.cq.hmq.service.system.MenuService;


@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController {

	@Autowired
	private MenuService menuService;

	// 用户获取到的菜单
	@RequestMapping(params = "findData", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> findData(Long id) {
		return menuService.findMenu(id);
	}

	// 流程菜单管理
	@RequestMapping(params = "findProcessMenu", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> findProcessMenu(Long id) {
		return menuService.findProcessMenu(id);
	}

	@RequestMapping(params = "page")
	public String page() {
		return view("core/system/menu");
	}
}
