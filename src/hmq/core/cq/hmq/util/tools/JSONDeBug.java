package core.cq.hmq.util.tools;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * 用于spring mvc返回json数据时的调试。 主要是ajax请求报500错误
 * 
 * @author 何建
 * 
 */
public class JSONDeBug {
	public static String vaildJson(Object o) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(o);
			System.out.println(jsonString);
			// LogUtil.getLog("提示").info(mapper.writeValueAsString(o));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonString;
	}
}
