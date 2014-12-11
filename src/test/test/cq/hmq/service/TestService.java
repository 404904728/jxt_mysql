package test.cq.hmq.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import app.cq.hmq.pojo.grab.Grab;
import common.cq.hmq.util.GrabWebpage;
import core.cq.hmq.service.BaseService;
import core.cq.hmq.util.tools.JSONDeBug;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-spring.xml" })
@Transactional
public class TestService extends BaseService {

	@SuppressWarnings("unchecked")
	@Test
	public void testfind() {
		String sql = "select r.id_f,r.name_f,r.type_f from role_t r,userrole_t ur where ur.userId_f=? and ur.teacher_f=1 and r.id_f=ur.roleId_f";
		List<Object[]> list = dao.getHelperDao().find(sql,1L);
		JSONDeBug.vaildJson(list);
	}

	// @Test
	// @Rollback(false)
	public void findClass() {
		try {
			List<Map<String, String>> listNews = GrabWebpage.loadNews();
			System.out
					.println("----------------------------------------------------------");
			List<Map<String, String>> listIntroduction = GrabWebpage
					.loadIntroduction();
			System.out
					.println("----------------------------------------------------------");
			for (Map<String, String> map : listIntroduction) {
				Grab grab = (Grab) dao.findOne("from " + Grab.class.getName()
						+ " where title=? and date=? and type=1",
						map.get("title"), map.get("date"));
				if (grab == null) {
					System.out.println("插入新数据" + map.get("title"));
					grab = new Grab();
					grab.setDate(map.get("date"));
					grab.setHref(map.get("href"));
					grab.setTitle(map.get("title"));
					grab.setType(1);
					dao.insert(grab);
				}
			}
			for (Map<String, String> map : listNews) {
				Grab grab = (Grab) dao.findOne("from " + Grab.class.getName()
						+ " where title=? and date=? and type=0",
						map.get("title"), map.get("date"));
				if (grab == null) {
					System.out.println("插入新数据" + map.get("title"));
					grab = new Grab();
					grab.setDate(map.get("date"));
					grab.setHref(map.get("href"));
					grab.setTitle(map.get("title"));
					grab.setType(0);
					dao.insert(grab);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
