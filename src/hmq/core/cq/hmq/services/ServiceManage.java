package core.cq.hmq.services;

import javax.annotation.Resource;

import core.cq.hmq.service.system.MenuService;
import core.cq.hmq.util.tools.JSONDeBug;


public class ServiceManage {
	
	@Resource
	private MenuService menuService;
	
	public String getMessage(String userCode,String messageCode,int rows){
		 return "are you ready? " + userCode;
	}
	
	public String getMenuInfo(){
	    return JSONDeBug.vaildJson(menuService.findData(null));
	}
}
