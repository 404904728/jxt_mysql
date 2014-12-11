package core.cq.hmq.util.tools;

import core.cq.hmq.annotation.Sequence;

/**
 * 
 * @author hejian
 * 
 */
public class StringUtil {

	/**
	 * 判断字符串是否为null或者空白字符或者空字符串
	 */
	public static boolean isEmpty(Object str) {
		return str == null || str.toString().matches("\\s*");
	}

	public static Class getVoClass(Object vo) {
		final Class clazz = vo.getClass();
		return clazz.getName().indexOf("$") > 0 ? clazz.getSuperclass() : clazz;
	}

	/**
	 * 获取自定sequence name
	 * 
	 * @param sequence
	 * @param clazz
	 * @return
	 * @since Fan Houjun 2009-8-14
	 */
	public static String getSeqName(Sequence sequence, Class clazz) {
		return isEmpty(sequence.name()) ? (clazz.getSimpleName() + "_Seq")
				: sequence.name();
	}

	/**
	 * 文件长度友好显示
	 */
	public static String fomatFileSize(long size) {
		if (size < 1024) {
			return size + "B";
		} else if (size < 1024 * 1024) {
			return size / 1024 + "K";
		} else {
			return size / 1024 / 1024 + "M";
		}
	}

}
