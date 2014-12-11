package core.cq.hmq.dao.util;

import org.hibernate.cfg.DefaultNamingStrategy;
import org.hibernate.util.StringHelper;
import org.springframework.stereotype.Component;

/**
 * vo表名、字段名自动映射策略，表名后缀：_t，字段名后缀：_f。
 * @since Fan Houjun 2010-9-15
 */
@Component
public class LocalNamingStrategy extends DefaultNamingStrategy {

	public String classToTableName(String className) {
		return StringHelper.unqualify(className + "_t");
	}

	public String propertyToColumnName(String propertyName) {
		return StringHelper.unqualify(propertyName + "_f");
	}

	/**
	 * Return the property name or propertyTableName
	 */
	public String foreignKeyColumnName(String propertyName, String propertyEntityName,
			String propertyTableName, String referencedColumnName) {
		return propertyName + "_f";
	}
}
