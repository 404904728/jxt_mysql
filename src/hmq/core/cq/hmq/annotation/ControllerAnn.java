package core.cq.hmq.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author hejian 2013-12-2
 */
@Retention(RetentionPolicy.RUNTIME)
// ①声明注解的保留期限
@Target(ElementType.METHOD)
// 定义注解的目标类型（方法，类等）
public @interface ControllerAnn {// 定义注解名称

	/** 权限，拥有相应权限才能访问，默认值 */
	String permission() default "";

	/** 是否需要登录 */
	boolean toLogon() default true;

	/** 该方法是否需要刷新权限 */
	boolean refPermission() default false;

	/** 方法时间间隔控制，需要多少秒才能第二次进入 */
	int interval() default 0;

	/**
	 * 是否需要通过日志记录本次操作，如果需要就给该属性设置一个名称，建议设为本操作的中文说明
	 */
	String log() default "";
}
