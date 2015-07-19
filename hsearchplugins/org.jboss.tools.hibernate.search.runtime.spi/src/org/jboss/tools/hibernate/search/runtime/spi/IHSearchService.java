package org.jboss.tools.hibernate.search.runtime.spi;

import java.util.Set;

import org.jboss.tools.hibernate.runtime.spi.IService;
import org.jboss.tools.hibernate.runtime.spi.ISessionFactory;

public interface IHSearchService extends IService {

	void newIndexRebuild(ISessionFactory sessionFactory, Set<Class> entities);
	
	void doAnalyze(String text, String analyzerClassName);
}
