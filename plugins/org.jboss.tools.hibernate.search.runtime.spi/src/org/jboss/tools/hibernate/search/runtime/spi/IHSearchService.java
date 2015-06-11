package org.jboss.tools.hibernate.search.runtime.spi;

import org.jboss.tools.hibernate.runtime.spi.IService;
import org.jboss.tools.hibernate.runtime.spi.ISessionFactory;

public interface IHSearchService extends IService {

	void newIndexRebuild(ISessionFactory sessionFactory);
}
