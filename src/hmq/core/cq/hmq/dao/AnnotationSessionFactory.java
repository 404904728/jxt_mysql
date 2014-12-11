package core.cq.hmq.dao;

import java.util.Set;

import org.apache.ibatis.io.ResolverUtil;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

import core.cq.hmq.service.InitDataService;

public class AnnotationSessionFactory extends AnnotationSessionFactoryBean {

	public AnnotationSessionFactory() {
	}

	public void setAnnotatedClasses(Class[] classes) {
	}

	public void setAnnotatedPackages(String[] annotatedPackages) {
		ResolverUtil resovlerUtil = new ResolverUtil();
		resovlerUtil.findAnnotated(javax.persistence.Entity.class,
				annotatedPackages);
		Set<Class> set = resovlerUtil.getClasses();
		Class[] voClasses = new Class[set.size()];
		set.toArray(voClasses);
		InitDataService.setVoClasses(voClasses);
		super.setAnnotatedClasses(voClasses);
	}
}