package core.cq.hmq.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import core.cq.hmq.dao.BaseDaoI;


/**
 * 数据库操作基类
 * 
 * @author 何建
 * 
 */
@Repository(value = "baseDao")
public class BaseDaoImpl implements BaseDaoI {
	
	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		hibernateTemplate.setCacheQueries(true);// 开启二级查询缓存
		return hibernateTemplate;
	}

	@Autowired
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	@Override
	public void delete(Object o) {
		this.getHibernateTemplate().delete(o);
	}

	@Override
	public Integer delete(String hql) {
		return delete(hql, (Object[]) null);
	}

	@Override
	public Integer delete(final String hql, final Object... values) {
		return this.getHibernateTemplate().execute(
				new HibernateCallback<Integer>() {
					@Override
					public Integer doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query q = session.createQuery(hql);
						if (values != null && values.length > 0) {
							for (int i = 0; i < values.length; i++) {
								q.setParameter(i, values[i]);
							}
						}
						return q.executeUpdate();
					}
				});
	}

	@Override
	public void deleteAll(Collection l) {
		this.getHibernateTemplate().deleteAll(l);
	}

	@Override
	public List find(String hql) {
		return this.getHibernateTemplate().find(hql);
	}

	@Override
	public List find(final String hql, final int page, final int rows,
			final List values) {
		return this.getHibernateTemplate().execute(
				new HibernateCallback<List>() {
					@Override
					public List doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query q = session.createQuery(hql);
						if (values != null && values.size() > 0) {
							for (int i = 0; i < values.size(); i++) {
								q.setParameter(i, values.get(i));
							}
						}
						return q.setFirstResult((page - 1) * rows)
								.setMaxResults(rows).list();
					}
				});
	}

	@Override
	public List find(final String hql, final int page, final int rows,
			final Object... values) {
		return this.getHibernateTemplate().execute(
				new HibernateCallback<List>() {
					@Override
					public List doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query q = session.createQuery(hql);
						if (values != null && values.length > 0) {
							for (int i = 0; i < values.length; i++) {
								q.setParameter(i, values[i]);
							}
						}
						return q.setFirstResult((page - 1) * rows)
								.setMaxResults(rows).list();
					}
				});
	}

	@Override
	public List find(String hql, Object... values) {
		return this.getHibernateTemplate().find(hql, values);
	}

	@Override
	public Object get(Class c, Serializable id) {
		return this.getHibernateTemplate().get(c, id);
	}

	@Override
	public Object load(Class c, Serializable id) {
		return this.getHibernateTemplate().load(c, id);
	}

	@Override
	public void save(Object o) {
		this.getHibernateTemplate().save(o);
	}

	@Override
	public void saveOrUpdate(Object o) {
		this.getHibernateTemplate().saveOrUpdate(o);
	}

	@Override
	public void saveOrUpdateAll(Collection l) {
		this.getHibernateTemplate().saveOrUpdateAll(l);
	}

	@Override
	public Long total(final String hql, final List values) {
		return this.getHibernateTemplate().execute(
				new HibernateCallback<Long>() {
					@Override
					public Long doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query q = session.createQuery(hql);
						if (values != null && values.size() > 0) {
							for (int i = 0; i < values.size(); i++) {
								q.setParameter(i, values.get(i));
							}
						}
						return (Long) q.uniqueResult();
					}
				});
	}

	@Override
	public Long total(final String hql, final Object... values) {
		return this.getHibernateTemplate().execute(
				new HibernateCallback<Long>() {
					@Override
					public Long doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query q = session.createQuery(hql);
						if (values != null && values.length > 0) {
							for (int i = 0; i < values.length; i++) {
								q.setParameter(i, values[i]);
							}
						}
						return (Long) q.uniqueResult();
					}
				});
	}

	@Override
	public void update(Object o) {
		this.getHibernateTemplate().update(o);
	}

	@Override
	public <T> List<T> find(Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T findOne(Class<T> clazz, String field, Object value) {
		return null;
	}
	
}
