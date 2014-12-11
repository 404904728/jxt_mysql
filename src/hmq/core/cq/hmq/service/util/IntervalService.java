package core.cq.hmq.service.util;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.cq.hmq.pojo.IntervalMethod;
import core.cq.hmq.service.BaseService;

@Service(value = "intervalService")
public class IntervalService extends BaseService {

	public IntervalMethod findBefor(String beanName, String methodName) {
		Object o = dao.findOne("from " + IntervalMethod.class.getName()
				+ " where beanName=? and methodName=?", beanName, methodName);
		if (o == null) {
			return null;
		}
		return (IntervalMethod) o;

	}

	@Transactional
	public void insert(IntervalMethod im) {
		dao.insert(im);
	}

	@Transactional
	public void update(IntervalMethod im) {
		// TODO Auto-generated method stub
		dao.update(im);
	}

}
