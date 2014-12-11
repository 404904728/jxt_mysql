package test.cq.hmq.ws;

import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;

public class WebService {
	public static void main(String[] args) throws AxisFault{
		//RPCServiceClient是RPC方式调用
		RPCServiceClient client = new RPCServiceClient();
		Options options = client.getOptions();
		//设置调用WebService的URL
		String address = "http://localhost/hmq/services/ServiceManage";
		EndpointReference epf = new EndpointReference(address);
		options.setTo(epf);
		/**
		 * 设置将调用的方法，http://ws.apache.org/axis2是方法
		 * 默认（没有package）命名空间，如果有包名
		 * 就是http://service.hoo.com 包名倒过来即可
		 * sayHello就是方法名称了
		 */
		QName qname = new QName("http://services.hmq.cq.core", "getMenuInfo");
		//指定调用的方法和传递参数数据，及设置返回值的类型
		Object[] result = client.invokeBlocking(qname, new Object[] {}, new Class[]{ String.class });
		System.out.println(result[0]);
	}

}
