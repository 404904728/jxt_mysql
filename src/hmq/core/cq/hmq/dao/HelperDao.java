package core.cq.hmq.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.metadata.ClassMetadata;

/**
 * 通过sql语句操作数据库，获取vo类的OR映射信息
 * 
 * @since 范后军 2008-5-1
 */
public interface HelperDao {

	/**
	 * 数据库是否支持sequence,目前只认为oracle支持。
	 * 
	 * @return
	 * @since Fan Houjun 2009-8-14
	 */
	public boolean isSequence();

	/**
	 * 获取实体类org.hibernate.metadata.ClassMetadata
	 * 
	 * @param clazz
	 */
	public ClassMetadata getClassMetadata(Class clazz);

	/**
	 * 执行写数据库操作语句
	 * 
	 * @param sql
	 *            原生的SQL语句，用“？”表示参数占位符
	 * @param args
	 *            与sql中“？”依次对应的参数列表
	 */
	public int excute(String sql, Object... args);

	/**
	 * 取得实体对象的主键值,辅助接口
	 * 
	 * @param entity
	 *            实体对象
	 */
	public Serializable getId(final Object entity);

	/**
	 * 取得实体类的主键名,辅助接口,如果是复合主键或无主键将返回null
	 * 
	 * @param clazz
	 *            实体类
	 */
	public String getIdName(final Class clazz);

	/**
	 * 执行查询操作语句,当sql中select查询多个（大于1个）字段的时候，返回结果集List中的元素为Object[]，否则为Object
	 * 
	 * @param sql
	 *            原生的SQL语句，用“？”表示参数占位符
	 * @param args
	 *            与sql中“？”依次对应的参数列表
	 */
	public List find(String sql, Object... args);

	/**
	 * 执行查询操作语句,当sql中select查询多个（大于1个）字段的时候，返回结果集List中的元素为Object[]，否则为Object
	 * 
	 * @param limit
	 *            设置返回结果集的最大记录条数
	 * @param sql
	 *            原生的SQL语句，用“？”表示参数占位符
	 * @param args
	 *            与sql中“？”依次对应的参数列表
	 */
	public List findLimit(int limit, String sql, Object... args);

	/**
	 * 根据sql语句获取对应的字段名列表
	 * 
	 * @param sql
	 *            原生的SQL语句，用“？”表示参数占位符
	 * @param args
	 *            与sql中“？”依次对应的参数列表
	 */
	public String[] getColumns(String sql, Object... args) throws SQLException;

	/**
	 * 执行分页查询,当sql中select查询多个（大于1个）字段的时候，返回结果集PageList中的元素为Object[]，否则为Object
	 * 
	 * 
	 * @param pageNo
	 *            从第0页开始
	 * @param pageSize
	 *            分页大小
	 * @param sql
	 *            原生的SQL语句，用“？”表示参数占位符
	 * @param args
	 *            与sql中“？”依次对应的参数列表
	 * @return
	 */
	public PageList page(int pageNo, int pageSize, String sql, Object... args);

	/**
	 * 执行分页查询,当sql中select查询多个（大于1个）字段的时候，返回结果集PageList中的元素为Object[]，否则为Object
	 * 
	 * 
	 * @param pageNo
	 *            从第0页开始
	 * @param pageSize
	 *            分页大小
	 * @param sql
	 *            原生的SQL语句，用“？”表示参数占位符
	 * @param totalSql
	 *            总数获取错误时，用该sql获取总数
	 * @param args
	 *            与sql中“？”依次对应的参数列表
	 * @return
	 */
	public PageList pageByTotal(int pageNo, int pageSize, String sql,
			String totalSql, Object... args);

	/**
	 * 获取实体类对应字段的类型
	 * 
	 * @param clazz
	 *            实体类
	 * @param field
	 *            字段
	 */
	public Class getFieldType(Class clazz, String field);

	/**
	 * 获取实体类主键类型
	 * 
	 * @param clazz
	 *            实体类
	 */
	public Class getIdType(Class clazz);

	/**
	 * 为实体对象设置主键
	 * 
	 * @param vo
	 *            实体对象
	 * @param id
	 *            主键
	 */
	public void setId(Object vo, Serializable id);

	/**
	 * 清空hibernate一级缓存
	 */
	public void clear();

	/**
	 * 判断对象是否已经处在hibernate的session（即1级缓存）中
	 * 
	 * @param vo
	 *            实体对象
	 */
	public boolean contains(Object vo);

	/**
	 * 获取图片存储路径
	 * 
	 * @return
	 * @since 2010-5-20
	 */
	public String getPicDir();

	/**
	 * 获取普通存储路径
	 * 
	 * @return
	 * @since 2010-5-20
	 */
	public String getFileDir();
}
