package core.cq.hmq.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 数据库操作基类
 * 
 * @author 何建
 * 
 */
public interface BaseDaoI {

	/**
	 * 删除一个对象
	 * 
	 * @param o
	 */
	public void delete(Object o);

	/**
	 * 删除
	 * 
	 * @param hql
	 *            语句
	 * @return
	 */
	public Integer delete(String hql);

	/**
	 * 删除
	 * 
	 * @param hql
	 *            语句
	 * @param values
	 *            参数
	 * @return
	 */
	public Integer delete(String hql, Object... values);

	/**
	 * 删除所有对象
	 * 
	 * @param l
	 *            对象集合
	 */
	public void deleteAll(Collection l);
	
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
	 * 根据指定实体类型查询所有对象列表
	 * 
	 * @param clazz
	 *            实体类
	 */
	public <T> List<T> find(final Class<T> clazz);
	

	/**
	 * 查找对象集合
	 * 
	 * @param hql
	 * @return
	 */
	public List find(String hql);

	/**
	 * 查找对象集合，带分页
	 * 
	 * @param hql
	 *            语句
	 * @param page
	 *            当前页
	 * @param rows
	 *            每页显示记录数
	 * @param values
	 *            参数
	 * @return
	 */
	public List find(String hql, int page, int rows, List values);

	/**
	 * 查找对象集合，带分页
	 * 
	 * @param hql
	 *            语句
	 * @param page
	 *            当前页
	 * @param rows
	 *            每页显示记录数
	 * @param values
	 *            参数
	 * @return
	 */
	public List find(String hql, int page, int rows, Object... values);

	/**
	 * 查找对象集合
	 * 
	 * @param hql
	 *            语句
	 * @param values
	 *            参数
	 * @return
	 */
	public List find(String hql, Object... values);

	/**
	 * 获取一个对象
	 * 
	 * @param c
	 *            对象Class
	 * @param id
	 *            对象ID
	 * @return
	 */
	public Object get(Class c, Serializable id);

	/**
	 * 获取一个对象
	 * 
	 * @param c
	 *            对象Class
	 * @param id
	 *            对象ID
	 * @return
	 */
	public Object load(Class c, Serializable id);

	/**
	 * 保存一个对象
	 * 
	 * @param o
	 */
	public void save(Object o);

	/**
	 * 保存或更新对象
	 * 
	 * @param o
	 */
	public void saveOrUpdate(Object o);

	/**
	 * 保存或更新所有对象
	 * 
	 * @param l
	 *            对象集合
	 */
	public void saveOrUpdateAll(Collection l);

	/**
	 * 获取总记录数
	 * 
	 * @param hql
	 *            语句
	 * @param values
	 *            参数
	 * @return
	 */
	public Long total(String hql, List values);

	/**
	 * 获取总记录数
	 * 
	 * @param hql
	 *            语句
	 * @param values
	 *            参数
	 * @return
	 */
	public Long total(String hql, Object... values);

	/**
	 * 更新对象
	 * 
	 * @param o
	 */
	public void update(Object o);
}