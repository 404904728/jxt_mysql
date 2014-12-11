package app.cq.hmq.service.grab;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.cq.hmq.pojo.grab.Grab;

import common.cq.hmq.util.GrabWebpage;

import core.cq.hmq.service.BaseService;
import core.cq.hmq.util.tools.DateUtil;
import core.cq.hmq.util.tools.LogUtil;

@Service("grabService")
public class GrabService extends BaseService {

	@Transactional
	public void insert() throws HttpException, IOException {
		LogUtil.getLog("进入定时器").info(
				DateUtil.format(new Date(), DateUtil.DATETIME_PATTERN));
		List<Map<String, String>> listNews = GrabWebpage.loadNews();
		List<Map<String, String>> listIntroduction = GrabWebpage
				.loadIntroduction();
		//Dao dao = (Dao) BeanUtil.getBean("dao");
		int i = dao.getHelperDao().excute("delete from grab_t");
		LogUtil.getLog("").info("删除了" + i + "条数据");
		List<Grab> list = new ArrayList<Grab>();
		for (Map<String, String> map : listIntroduction) {
			System.out.println("插入新数据" + map.get("title"));
			Grab dbgrab = new Grab();
			dbgrab.setDate(map.get("date"));
			dbgrab.setHref(map.get("href"));
			dbgrab.setTitle(map.get("title"));
			dbgrab.setType(1);
			list.add(dbgrab);
		}
		for (Map<String, String> map : listNews) {
			System.out.println("插入新数据" + map.get("title"));
			Grab dbgrab = new Grab();
			dbgrab.setDate(map.get("date"));
			dbgrab.setHref(map.get("href"));
			dbgrab.setTitle(map.get("title"));
			dbgrab.setType(0);
			list.add(dbgrab);
		}
		dao.insert(list);
	}

	public Map<String, Object> find(Long id) throws HttpException, IOException {
		// TODO Auto-generated method stub
		Grab dbGrab = dao.findOne(Grab.class, "id", id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("grab", dbGrab);
		map.put("content", GrabWebpage.loadPage(dbGrab.getHref()));
		return map;
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Grab> findList(Integer type) {
		List<Grab> grabs = dao.findLimit(10, "from " + Grab.class.getName()
				+ " where type=? order by date desc", type);
		return grabs;
	}
}
