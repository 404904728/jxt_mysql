package core.cq.hmq.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加载Vo类之前，系统自动创建序列化对象，针对Oracle数据库
 * 
 * 关于主键策略
 * 
 */
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Sequence {

	/**
	 * 序列名称
	 * 
	 * @return
	 * @since Fan Houjun 2009-8-14
	 */
	public String name() default "";

	public int initialValue() default 1000;

	public int allocationSize() default 1;

	/**
	 * 所修饰的VO是否采用自定义序列化主键类型
	 * 
	 * @return
	 * @since Fan Houjun 2009-8-14
	 */
	public boolean seqId() default true;

	/**
	 * 序列化缓存数量
	 * 
	 * @return
	 * @since Fan Houjun 2009-8-14
	 */
	public int cache() default 20;
}
