package core.cq.hmq.dao.impl;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import core.cq.hmq.dao.HelperDao;
import core.cq.hmq.dao.PageList;
import core.cq.hmq.pojo.sys.IdEntity;
import core.cq.hmq.util.tools.HqlUtil;
import core.cq.hmq.util.tools.StringUtil;

/**
 * @author 范后军
 * @since 2008-5-1
 */
public class HelperDaoImpl extends HibernateDaoSupport implements HelperDao {

	public int excute(String sql, Object... args) {
		return createQuery(sql, args).executeUpdate();
	}

	@Override
	public List find(String sql, Object... args) {
		return createQuery(sql, args).list();
	}

	@Override
	public List findLimit(int limit, String sql, Object... args) {
		final Query query = createQuery(sql, args);
		query.setMaxResults(limit);
		return query.list();
	}

	private Query createQuery(final String queryStr, final Object... args) {
		final Query query = getSession().createSQLQuery(queryStr);
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		return query;
	}

	@Override
	public ClassMetadata getClassMetadata(Class clazz) {
		return getSessionFactory().getClassMetadata(clazz);
	}

	@Override
	public PageList page(int pageNo, int pageSize, String sql,
			final Object... args) {
		sql = sql.trim();
		try {
			PageList pageList = new PageList(pageNo, pageSize);
			// pageList.setColumnNames(getColumns(sql));
			final Query query = createQuery(sql, args);
			query.setFirstResult(pageList.firstIndex());
			query.setMaxResults(pageList.pageSize());
			pageList.setResult(query.list());
			pageList.setTotalCount(getTotal(sql, args));
			return pageList;
		} catch (Exception e) {
			throw new HibernateException(e);
		}
	}

	@Override
	public PageList pageByTotal(int pageNo, int pageSize, String sql,
			String totalSql, Object... args) {
		sql = sql.trim();
		try {
			PageList pageList = new PageList(pageNo, pageSize);
			//pageList.setColumnNames(getColumns(sql));
			final Query query = createQuery(sql, args);
			query.setFirstResult(pageList.firstIndex());
			query.setMaxResults(pageList.pageSize());
			pageList.setResult(query.list());
			if (totalSql == null) {
				pageList.setTotalCount(getTotal(sql, args));
			} else {
				pageList.setTotalCount(getTotal(totalSql, args));
			}
			return pageList;
		} catch (Exception e) {
			throw new HibernateException(e);
		}
	}

	private int getTotal(String sql, Object... args) {
		String countSql = "";
		for (;;) {
			try {
				countSql = createCountStr(sql);
				SQLQuery query = getSession().createSQLQuery(countSql);
				for (int i = 0; i < args.length; i++) {
					query.setParameter(i, args[i]);
				}
				return HqlUtil.toNumber(query.list()).intValue();
			} catch (RuntimeException e) {
				if (countSql.length() < 18) {
					throw e;
				}
				countSql = countSql.substring(18);
			}
		}

	}

	private String createCountStr(String str) {
		String sql = str.toLowerCase();

		final int endIndex = sql.lastIndexOf("order by");
		if (endIndex > 0 && getFromPos(sql) < endIndex) {
			sql = sql.substring(0, endIndex);
		}
		return "select count(*) " + str.substring(getFromPos(sql) + 1);
	}

	private int getFromPos(String sql) {
		int a = sql.indexOf(" from ");
		int b = sql.indexOf(")from ");
		return (a >= 0 && (b < 0 || a < b)) ? a : b;
	}

	/**
	 * @return @since Fan Houjun 2009-3-19
	 */
	@SuppressWarnings("deprecation")
	public Connection connection() {
		return getSession().connection();
	}

	@Override
	public String[] getColumns(String sql, Object... args) throws SQLException {
		Connection con = connection();
		PreparedStatement statement = con.prepareStatement(sql);
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				statement.setObject(i + 1, args[i]);
			}
		}
		statement.setMaxRows(1);
		final ResultSetMetaData metaData = statement.executeQuery()
				.getMetaData();
		String[] names = new String[metaData.getColumnCount()];
		for (int i = 1; i <= metaData.getColumnCount(); i++) {
			names[i - 1] = metaData.getColumnName(i);
		}
		statement.close();
		con.close();
		return names;
	}

	@Override
	public boolean isSequence() {
		String dialect = ((org.hibernate.impl.SessionImpl) getSession())
				.getFactory().getDialect().toString().toLowerCase();
		return dialect.indexOf("oracle") > -1;
	}

	// public void disableConstraints(String[] tables) {
	// for (int i = 0; i < tables.length; i++) {
	// String table = tables[i].toUpperCase();
	// for (Iterator it = findBySql(
	// "select t.constraint_name from dba_constraints t where t.table_name=? and
	// t.owner=?",
	// new Object[] { table, dbUser }).iterator(); it.hasNext();) {
	// try {
	// excuteBySql("ALTER TABLE " + table + " DISABLE CONSTRAINT " + it.next(),
	// new Object[0]);
	// } catch (RuntimeException e) {
	// LogUtil.getLog(getClass()).warn(e, e);
	// }
	// }
	// }
	// }
	//
	// public void enableConstraints(String[] tables) {
	// for (int i = 0; i < tables.length; i++) {
	// String table = tables[i].toUpperCase();
	// for (Iterator it = findBySql(
	// "select t.constraint_name from dba_constraints t where t.table_name=? and
	// t.owner=?",
	// new Object[] { table, dbUser }).iterator(); it.hasNext();) {
	// try {
	// excuteBySql("ALTER TABLE " + table + " ENABLE CONSTRAINT " + it.next(),
	// new Object[0]);
	// } catch (RuntimeException e) {
	// LogUtil.getLog(getClass()).warn(e, e);
	// }
	// }
	// }
	// }
	@Override
	public Class getFieldType(Class clazz, String field) {
		return getSessionFactory().getClassMetadata(clazz)
				.getPropertyType(field).getReturnedClass();
	}

	@Override
	public Serializable getId(final Object entity) {
		if (entity instanceof IdEntity) {
			return ((IdEntity) entity).getId();
		}
		return getSessionFactory().getClassMetadata(
				StringUtil.getVoClass(entity)).getIdentifier(entity,
				EntityMode.POJO);
	}

	@Override
	public Class getIdType(Class clazz) {
		return getSessionFactory().getClassMetadata(clazz).getIdentifierType()
				.getReturnedClass();
	}

	@Override
	public String getIdName(final Class clazz) {
		return getSessionFactory().getClassMetadata(clazz)
				.getIdentifierPropertyName();
	}

	@Override
	public void setId(Object entity, Serializable id) {
		getSessionFactory().getClassMetadata(StringUtil.getVoClass(entity))
				.setIdentifier(entity, id, EntityMode.POJO);
	}

	@Override
	public void clear() {
		getSession().flush();
		getSession().clear();
	}

	@Override
	public boolean contains(Object o) {
		return getSession().contains(o);
	}

	private String picDir;
	private String fileDir;

	@Override
	public String getPicDir() {
		return picDir;
	}

	@Override
	public String getFileDir() {
		return fileDir;
	}

	public void setAttachDir(String dir) {
		picDir = dir + "/pic/";
		File pic = new File(picDir);
		if (!pic.isDirectory()) {
			pic.mkdirs();
		}
		fileDir = dir + "/common/";
		File file = new File(fileDir);
		if (!file.isDirectory()) {
			file.mkdir();
		}
	}
}
