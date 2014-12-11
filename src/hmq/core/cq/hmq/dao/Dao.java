package core.cq.hmq.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;

/**
 * 主要基于HQL访问数据库的Dao
 * 
 * @since fanhoujun 2007-7-24
 */
public interface Dao {

	/**
	 * 根据实体类和主键 获取实体对象，不存在返回null
	 * 
	 * @param clazz
	 *            实体类
	 * @param id
	 *            主键
	 */
	public <T> T get(final Class<T> clazz, final Serializable id);

	/**
	 * 返回HelperDao
	 */
	public HelperDao getHelperDao();

	/**
	 * 根据实体类和主键 获取实体对象，不存在抛出异常
	 * 
	 * @param clazz
	 *            实体类
	 * @param id
	 *            主键
	 */
	public <T> T load(final Class<T> clazz, final Serializable id);

	/**
	 * 根据Id将数据库中的记录映射成实体对象vo
	 * 
	 * @param vo
	 *            实体对象
	 * @param id
	 *            主键
	 */
	public void load(final Object vo, final Serializable id);

	/**
	 * 根据Id将数据库中的记录映射成实体对象vo
	 * 
	 * @param vo
	 *            实体对象
	 */
	public void load(final Object vo);

	/**
	 * 更新实体对象
	 * 
	 * @param entities
	 *            可以是单个实体对象，也可以是多个实体对象的集合（java.util.Collection）
	 */
	public void update(final Object entities);

	/**
	 * 插入实体对象
	 * 
	 * @param entities
	 *            可以是单个实体对象，也可以是多个实体对象的集合（java.util.Collection）
	 */
	public void insert(final Object entities);

	/**
	 * 插入或更新实体对象，不推荐使用
	 * 
	 * @param entities
	 *            可以是单个实体对象，也可以是多个实体对象的集合（java.util.Collection）
	 */
	public void insertOrUpdate(final Object entities);

	/**
	 * 删除对象
	 * 
	 * @param entities
	 *            可以是单个实体对象，也可以是多个实体对象的集合（java.util.Collection）
	 */
	public void delete(final Object entities);

	/**
	 * 根据Id删除指定实体类型对象，如果对象不存在，后台会有警告输出，但不抛出异常
	 * 
	 * @param clazz
	 *            实体类
	 * @param ids
	 *            主键，可以是多个主键
	 */
	public void delete(final Class clazz, final Serializable... ids);

	/**
	 * 根据HQL语句执行写操作,返回被影响的记录条数
	 * 
	 * @param hql
	 *            Hibernate标准的HQL语句，用“？”表示参数占位符
	 * @param args
	 *            与hql中“？”依次对应的参数列表
	 */
	public int excute(final String hql, final Object... args);

	/**
	 * 根据指定实体类型查询所有对象列表
	 * 
	 * @param clazz
	 *            实体类
	 */
	public <T> List<T> find(final Class<T> clazz);

	/**
	 * 根据指定实体类型查询第一个对象
	 * 
	 * @param clazz
	 *            实体类
	 */
	public <T> T findOne(final Class<T> clazz);

	/**
	 * 根据HQL语句查询符合条件所有对象列表
	 * 
	 * @param hql
	 *            Hibernate标准的HQL语句，用“？”表示参数占位符
	 * @param args
	 *            与hql中“？”依次对应的参数列表
	 */
	@SuppressWarnings("unchecked")
	public List find(final String hql, final Object... args);

	/**
	 * 根据HQL语句查询符合条件有限对象列表
	 * 
	 * @param hql
	 *            Hibernate标准的HQL语句，用“？”表示参数占位符
	 * @param args
	 *            与hql中“？”依次对应的参数列表
	 */
	public Object findOne(final String hql, final Object... args);

	/**
	 * 查找最前limit条记录
	 * 
	 * @param limit
	 *            返回结果集的最大记录条数
	 * @param hql
	 *            Hibernate标准的HQL语句，用“？”表示参数占位符
	 * @param args
	 *            与hql中“？”依次对应的参数列表
	 */
	public List findLimit(int limit, final String hql, final Object... args);

	/**
	 * 分页查询指定实体类型所有对象列表
	 * 
	 * @param pageNo
	 *            从第0页开始
	 * @param pageSize
	 *            分页大小
	 * @param clazz
	 *            实体类
	 */
	public <T> PageList<T> page(final int pageNo, final int pageSize, final Class<T> clazz);

	/**
	 * 分页查询
	 * 
	 * @param pageNo
	 *            从第0页开始
	 * @param pageSize
	 *            分页大小
	 * @param hql
	 *            Hibernate标准的HQL语句，用“？”表示参数占位符
	 * @param args
	 *            与hql中“？”依次对应的参数列表
	 */
	public PageList page(final int pageNo, final int pageSize, final String hql, final Object... args);

	/**
	 * 根据实体类获取全部对象,带排序字段与升降序参数
	 * 
	 * @param clazz
	 *            实体类
	 * @param oderBy
	 *            排序的字段
	 * @param isAsc
	 *            true升序，false降序
	 */
	public <T> List<T> sort(final Class<T> clazz, final String orderBy, final boolean isAsc);

	/**
	 * 分页获取对象,带排序字段与升降序参数
	 * 
	 * @param pageNo
	 *            从第0页开始
	 * @param pageSize
	 *            分页大小
	 * @param clazz
	 *            实体类
	 * @param oderBy
	 *            排序的字段
	 * @param isAsc
	 *            true升序，false降序
	 */
	public <T> PageList<T> pageSort(final int pageNo, final int pageSize, final Class<T> clazz, final String orderBy,
			boolean isAsc);

	/**
	 * 根据字段名和字段值查询对象
	 * 
	 * @param clazz
	 *            实体类
	 * @param field
	 *            字段名
	 * @param value
	 *            字段值
	 */
	public <T> List<T> find(final Class<T> clazz, final String field, final Object value);

	/**
	 * 根据属性名和属性值分页查询
	 * 
	 * @param pageNo
	 *            从第0页开始
	 * @param pageSize
	 *            分页大小
	 * 
	 * @param clazz
	 *            实体类
	 * @param field
	 *            字段名
	 * @param value
	 *            字段值
	 */
	public <T> PageList<T> page(final int pageNo, final int pageSize, final Class<T> clazz, final String propertyName,
			final Object value);

	/**
	 * 根据属性名和属性值查询对象,带排序参数
	 * 
	 * @param clazz
	 *            实体类
	 * @param field
	 *            字段名
	 * @param value
	 *            字段值
	 * @param oderBy
	 *            排序的字段
	 * @param isAsc
	 *            true升序，false降序
	 */
	public <T> List<T> find(final Class<T> clazz, final String field, final Object value, final String orderBy,
			final boolean isAsc);

	/**
	 * 根据属性名和属性值分页查询对象,带排序参数
	 * 
	 * @param pageNo
	 *            从第0页开始
	 * @param pageSize
	 *            分页大小
	 * @param clazz
	 *            实体类
	 * @param oderBy
	 *            排序的字段
	 * @param isAsc
	 *            true升序，false降序
	 */
	public <T> PageList<T> page(final int pageNo, final int pageSize, final Class<T> clazz, final String propertyName,
			final Object value, final String orderBy, final boolean isAsc);

	/**
	 * 根据属性名和属性值仅获取第一个对象
	 * 
	 * @param clazz
	 *            实体类
	 * @param field
	 *            字段名
	 * @param value
	 *            字段值
	 */
	public <T> T findOne(final Class<T> clazz, final String field, final Object value);

	/**
	 * 根据实体类创建org.hibernate.Criteria
	 * 
	 * @param clazz
	 *            实体类
	 */
	public Criteria createCriteria(Class clazz);

	/**
	 * 根据org.hibernate.Criteria进行分页查询
	 * 
	 * @param pageNo
	 *            从第0页开始
	 * @param pageSize
	 *            分页大小
	 * @param criteria
	 *            org.hibernate.Criteria
	 */
	public PageList page(final int pageNo, final int pageSize, Criteria criteria);

}
