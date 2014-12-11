/**
 * Limit
 *
 */
package app.cq.hmq.service.cjmanage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import app.cq.hmq.mode.ScoreQueryMode;

/**
 * @author Administrator
 *
 */
public class TurnTORight {
	
	public static List<ScoreQueryMode> testIder(List list,List<String> reglist) throws JsonParseException, JsonMappingException, IOException{
		ScoreQueryMode mode = null;
		List<ScoreQueryMode> modelist = new ArrayList<ScoreQueryMode>();
		
		for (int i = 0; i < list.size(); i++) {
			Object[] object = (Object[]) list.get(i);
			mode = new ScoreQueryMode();
			
			mode.setTitle(object[0].toString());
			mode.setNo(object[1].toString());
			mode.setName(object[2].toString());
			for (int j = 0; j < reglist.size(); j++) {
				String vvvv = (String) object[3].toString();
				Map<String,Object> map = new HashMap<String,Object>();
				ObjectMapper o = new ObjectMapper();
				map = o.readValue(vvvv, Map.class);
				if("A卷成绩".equals(reglist.get(j))){
					mode.setScoreA(map.get(reglist.get(j))+"");
				}
				if("B卷成绩".equals(reglist.get(j))){
					mode.setScoreB(map.get(reglist.get(j))+"");
				}
				if("年排".equals(reglist.get(j))){
					mode.setDrandOrder(map.get(reglist.get(j))+"");
				}
				if("年排涨幅".equals(reglist.get(j))){
					mode.setDrandAsc(map.get(reglist.get(j))+"");
				}
			}
			
			mode.setTotalScore(object[4].toString());
			modelist.add(mode);
		}
		return modelist;
	}

}
