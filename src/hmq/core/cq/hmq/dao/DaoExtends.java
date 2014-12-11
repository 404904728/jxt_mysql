package core.cq.hmq.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import core.cq.hmq.modal.PageModel;

@Repository
@Transactional(readOnly = true, rollbackFor = Exception.class)
public abstract class DaoExtends {

	// @Resource
	// protected BaseDaoI baseDaoI;

	@Autowired
	protected Dao dao;

	/**
	 * ①根据实体类和pageModel中的参数做分页查询 ②包含排序，若有排序则会排序
	 * 
	 * @param pageModel
	 *            分页参数的包装
	 * @param voClass
	 */
	@SuppressWarnings("unchecked")
	protected PageList page(PageModel model, Class voClass) {
		if (model.getSort() == null) {
			return page(model, dao.createCriteria(voClass), (String[]) null);
		}
		boolean isAsc = true;
		if (model.getOrder().equals("desc")) {
			isAsc = false;
		}
		return dao.pageSort(model.getPage(), model.getRows(), voClass,
				model.getSort(), isAsc);
	}

	/**
	 * 根据hql，pageModel做分页查询
	 * 
	 * @param model
	 *            分页包装的参数
	 * @param hql
	 *            hql语句
	 * @param args
	 *            与hql中“？”依次对应的参数列表
	 * @return
	 */
	protected PageList page(PageModel model, String hql, String... args) {
		return dao.page(model.getPage(), model.getRows(), hql, (Object[]) args);
	}

	/**
	 * 根据Criteria、pageModel做分页查询
	 * 
	 * @param pageModel
	 *            分页参数的包装
	 * @param criteria
	 *            org.hibernate.Criteria
	 * @param matches
	 *            pageModel中searchKey相匹配的字段范围
	 */
	private PageList page(PageModel pageModel, Criteria criteria,
			String... matches) {
		// String key = pageModel.getSearchKey();
		String key = null;
		if (key != null && matches != null && matches.length > 0) {
			Criterion like = null;
			for (int i = 0; i < matches.length; i++) {
				final Criterion c = Restrictions.like(matches[i], key,
						MatchMode.ANYWHERE);
				if (like == null) {
					like = c;
				} else {
					like = Restrictions.or(like, c);
				}
			}
			criteria.add(like);
		}
		return dao.page(pageModel.getPage(), pageModel.getRows(), criteria);
	}
}
