package core.cq.hmq.activiti;

import org.activiti.editor.rest.application.ModelerServicesInit;
import org.activiti.rest.api.DefaultResource;
import org.activiti.rest.application.ActivitiRestApplication;
import org.activiti.rest.filter.JsonpFilter;
import org.restlet.Restlet;
import org.restlet.routing.Router;

/**
 * 
 * @author hejian 2014-01-18
 * 注册了一些REST路由
 */
public class ExplorerRestApplication extends ActivitiRestApplication {

	public ExplorerRestApplication() {
		super();
	}

	/**
	 * Creates a root Restlet that will receive all incoming calls.
	 */
	@Override
	public synchronized Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attachDefault(DefaultResource.class);
		ModelerServicesInit.attachResources(router);
		JsonpFilter jsonpFilter = new JsonpFilter(getContext());
		jsonpFilter.setNext(router);
		return jsonpFilter;
	}
}
