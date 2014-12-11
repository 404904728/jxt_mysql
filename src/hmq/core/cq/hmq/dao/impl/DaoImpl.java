package core.cq.hmq.dao.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import core.cq.hmq.annotation.Sequence;
import core.cq.hmq.dao.Dao;
import core.cq.hmq.dao.HelperDao;
import core.cq.hmq.dao.PageList;
import core.cq.hmq.pojo.sys.HbMySq;
import core.cq.hmq.util.tools.HqlUtil;
import core.cq.hmq.util.tools.StringUtil;

/**
 * 基于Spring对Hibernate的封装实现Dao接口
 * 
 * @author fanhoujun 2007-9-17
 */
@SuppressWarnings("unchecked")
public class DaoImpl extends HibernateDaoSupport implements Dao {

	private HelperDao helperDao;

	public void setHelperDao(HelperDao helperDao) {
		this.helperDao = helperDao;
	}

	public HelperDao getHelperDao() {
		return helperDao;
	}

	public List find(final String hql, final Object... args) {
		return createQuery(hql, args).list();
	}

	public Object findOne(final String hql, final Object... args) {
		return first(findLimit(1, hql, args));
	}

	private Object first(final List list) {
		return list.isEmpty() ? null : list.get(0);
	}

	public List findLimit(int limit, final String hql, final Object... args) {
		return createQuery(hql, args).setMaxResults(limit).list();
	}

	public <T> T get(final Class<T> clazz, final Serializable id) {
		return (T) getHibernateTemplate().get(clazz, id);
	}

	public <T> T load(final Class<T> clazz, final Serializable id) {
		return (T) getHibernateTemplate().load(clazz, id);
	}

	public void load(Object vo, Serializable id) {
		if (!getHibernateTemplate().contains(vo)) {
			getHibernateTemplate().load(vo, id);
		}
	}

	public void load(Object vo) {
		if (vo == null || getSession().contains(vo)) {
			return;
		}
		final Serializable id = getHelperDao().getId(vo);
		if (id == null) {
			return;
		}
		load(vo, id);
	}

	public <T> List<T> find(final Class<T> clazz) {
		return createCriteria(clazz).list();
	}

	public <T> T findOne(final Class<T> clazz) {
		return (T) first(createCriteria(clazz).setMaxResults(1).list());
	}

	public void delete(final Object entities) {
		if (entities instanceof Collection) {
			getHibernateTemplate().deleteAll((Collection) entities);
		} else {
			getHibernateTemplate().delete(entities);
		}
	}

	/**
	 * 根据Id删除指定实体类型对象，如果对象不存在，后台会有警告输出，但不抛出异常
	 */
	public void delete(final Class clazz, final Serializable... ids) {
		for (Serializable id : ids) {
			delteVo(clazz, id);
		}
	}

	private void delteVo(final Class clazz, Serializable id) {
		Object o = get(clazz, id);
		if (o != null) {
			getHibernateTemplate().delete(o);
		} else {
			logger.warn(clazz.getName() + "(id=" + id + ")已被删除，请求可能出错");
		}
	}

	public void update(final Object entities) {
		if (entities instanceof Collection) {
			for (Iterator it = ((Collection) entities).iterator(); it.hasNext();) {
				getHibernateTemplate().update(it.next());
			}
		} else {
			getHibernateTemplate().update(entities);
		}
	}

	public void insert(final Object entities) {
		if (entities instanceof Collection) {
			for (Iterator it = ((Collection) entities).iterator(); it.hasNext();) {
				save(it.next());
			}
		} else {
			save(entities);
		}
	}

	private Serializable save(Object entity) {
		checkId(entity);
		final Serializable id = getHibernateTemplate().save(entity);
		// OpLogDao.instance().rememberCreateVo(entity);// 便于统一回退
		return id;
	}

	public void insertOrUpdate(final Object entities) {
		if (entities instanceof Collection) {
			for (Iterator it = ((Collection) entities).iterator(); it.hasNext();) {
				saveOrUpdate(it.next());
			}
		} else {
			saveOrUpdate(entities);
		}
	}

	private boolean checkId(Object entity) {
		Class clazz = StringUtil.getVoClass(entity);
		Sequence sequence = (Sequence) clazz.getAnnotation(Sequence.class);
		if (helperDao.isSequence()) {
			sequence = (Sequence) HbMySq.class.getAnnotation(Sequence.class);
		}
		if (sequence != null && sequence.seqId()) {
			if (getHelperDao().getId(entity) != null) {
				return false;
			}
			final Number id;
			if (helperDao.isSequence()) {
				id = (Number) helperDao.find(
						"select " + StringUtil.getSeqName(sequence, clazz)
								+ ".nextval from dual").get(0);
			} else {
				synchronized (DaoImpl.class) {
					id = getSeqId();
				}
			}
			getHelperDao().setId(entity, id.longValue());
			return true;
		}
		return false;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	private long getSeqId() {
		HbMySq hbMySq = findOne(HbMySq.class);
		final Long val = hbMySq.getVal();
		hbMySq.setVal(val + 1);
		return val;
	}

	private void saveOrUpdate(Object entity) {
		if (checkId(entity)) {
			getHibernateTemplate().save(entity);
		} else {
			getHibernateTemplate().saveOrUpdate(entity);
		}
	}

	public int excute(final String hql, final Object... args) {
		return createQuery(hql, args).executeUpdate();
	}

	public <T> PageList<T> page(final int pageNo, final int pageSize,
			final Class<T> clazz) {
		return page(pageNo, pageSize, createCriteria(clazz));
	}

	public PageList page(final int pageNo, final int pageSize, Criteria criteria) {
		final PageList pc = new PageList(pageNo, pageSize);
		if (pageSize < 1) {
			pc.setResult(criteria.list());
			pc.setTotalCount(pc.getList().size());
			return pc;
		}
		CriteriaImpl criteriaImpl = (CriteriaImpl) criteria;
		Projection p = criteriaImpl.getProjection();
		List orderList = null;
		Field field = null;
		if (criteriaImpl.iterateOrderings().hasNext()) {
			// TODO 由于hibernate或者sqlserver的原因，带order
			// by子句的统计总数查询会出错，因此去除order条件后再统计
			// hibernate的封装及其严格，只能采取反射访问CriteriaImpl的私域
			try {
				field = CriteriaImpl.class.getDeclaredField("orderEntries");
				field.setAccessible(true);
				orderList = (List) field.get(criteriaImpl);
				field.set(criteriaImpl, Collections.EMPTY_LIST);
			} catch (Exception e) {
				throw new HibernateException("底层排序出错", e);
			}
		}
		ResultTransformer rt = criteriaImpl.getResultTransformer();
		criteriaImpl.setProjection(Projections.rowCount());
		pc.setTotalCount(HqlUtil.toNumber(criteriaImpl.list()).intValue());
		if (orderList != null && !orderList.isEmpty() && field != null) {
			try {
				field.set(criteriaImpl, orderList);
			} catch (Exception e) {
				throw new HibernateException("底层排序出错", e);
			}
		}
		criteriaImpl.setProjection(p);
		criteriaImpl.setResultTransformer(rt);
		if (pc.getTotalCount() > pageNo * pageSize) {
			pc.setResult(criteriaImpl.setFirstResult(pc.firstIndex())
					.setMaxResults(pc.pageSize()).list());
		}
		return pc;
	}

	public PageList page(final int pageNo, final int pageSize, String hql,
			final Object... args) {
		final PageList pc = new PageList(pageNo, pageSize);
		if (pageSize < 1) {
			pc.setResult(createQuery(hql, args).list());
			pc.setTotalCount(pc.getList().size());
			return pc;
		}
		pc.setTotalCount(HqlUtil.toNumber(
				createQuery(createCountHql(hql), args).list()).intValue());
		if (pc.getTotalCount() > pageNo * pageSize) {
			pc.setResult(createQuery(hql, args).setFirstResult(pc.firstIndex())
					.setMaxResults(pc.pageSize()).list());
		}
		return pc;
	}

	private static String createCountHql(String hql) {
		hql = hql.trim();
		final String hql2 = hql.toLowerCase();

		// TODO 由于hibernate或者sqlserver的原因，带order by子句的统计总数查询会出错，因此去除order条件
		final int endIndex = hql2.lastIndexOf("order by");
		if (endIndex > 0) {
			hql = hql.substring(0, endIndex);
		}

		final int fromIndex = hql2.indexOf("from ");
		if (fromIndex == 0) {
			return "select count(*) " + hql;
		} else {
			int a = hql2.indexOf(" from ");
			int b = hql2.indexOf(")from ");
			return "select count(*) "
					+ hql.substring(((a >= 0 && (b < 0 || a < b)) ? a : b) + 1);
		}
	}

	private Query createQuery(final String hql, final Object[] args) {
		final Query query = getSession().createQuery(hql);
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		return query;
	}

	// ///////////////////////////////////////////////////////////////////////
	// 新补充的接口实现
	// //////////////////////////////////////////////////////////////////////
	public <T> List<T> sort(final Class<T> clazz, final String orderBy,
			final boolean isAsc) {
		return createCriteria(clazz, orderBy, isAsc).list();
	}

	public <T> PageList<T> pageSort(final int pageNo, final int pageSize,
			final Class<T> clazz, final String orderBy, boolean isAsc) {
		Criteria criteria = createCriteria(clazz);
		criteria.addOrder(isAsc ? Order.asc(orderBy) : Order.desc(orderBy));
		return page(pageNo, pageSize, criteria);
	}

	private Criteria createCriteria(Class entityClass, String orderBy,
			boolean isAsc) {
		final Order order = isAsc ? Order.asc(orderBy) : Order.desc(orderBy);
		return createCriteria(entityClass).addOrder(order).setCacheable(true);
	}

	public Criteria createCriteria(final Class entityClass) {
		return getSession().createCriteria(entityClass);
	}

	private Criteria createCriteria(final Class entityClass,
			final Criterion criterion) {
		return createCriteria(entityClass).add(criterion);
	}

	public <T> List<T> find(final Class<T> clazz, final String propertyName,
			final Object value) {
		return createCriteria(clazz, Restrictions.eq(propertyName, value))
				.list();
	}

	public <T> PageList<T> page(final int pageNo, final int pageSize,
			final Class<T> clazz, final String propertyName, final Object value) {
		Criteria criteria = createCriteria(clazz);
		criteria.add(Restrictions.eq(propertyName, value));
		return page(pageNo, pageSize, criteria);
	}

	public <T> List<T> find(final Class<T> clazz, final String propertyName,
			final Object value, final String orderBy, final boolean isAsc) {
		return createCriteria(clazz, orderBy, isAsc,
				Restrictions.eq(propertyName, value)).list();
	}

	private Criteria createCriteria(final Class clazz, final String orderBy,
			final boolean isAsc, final Criterion criterions) {
		return createCriteria(clazz, criterions).addOrder(
				isAsc ? Order.asc(orderBy) : Order.desc(orderBy));
	}

	public <T> PageList<T> page(final int pageNo, final int pageSize,
			final Class<T> clazz, final String propertyName,
			final Object value, final String orderBy, final boolean isAsc) {
		Criteria criteria = createCriteria(clazz);
		criteria.add(Restrictions.eq(propertyName, value));
		criteria.addOrder(isAsc ? Order.asc(orderBy) : Order.desc(orderBy));
		return page(pageNo, pageSize, criteria);
	}

	public <T> T findOne(final Class<T> clazz, final String propertyName,
			final Object value) {
		final List list = createCriteria(clazz,
				Restrictions.eq(propertyName, value)).setMaxResults(1).list();
		return (T) first(list);
	}
}
