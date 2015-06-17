package org.jboss.tools.hibernate.search;

import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.jboss.tools.hibernate.proxy.ServiceProxy;
import org.jboss.tools.hibernate.proxy.SessionFactoryProxy;
import org.jboss.tools.hibernate.runtime.spi.ISessionFactory;
import org.jboss.tools.hibernate.search.runtime.spi.IHSearchService;

public class HSearchServiceProxy extends ServiceProxy implements IHSearchService {
	
	@Override
	public void newIndexRebuild(ISessionFactory sessionFactory) {
		try {
			SessionFactoryImpl factory = (SessionFactoryImpl) ((SessionFactoryProxy) sessionFactory).getTarget();
			FullTextSession fullTextSession = Search.getFullTextSession(factory.getCurrentSession());
			fullTextSession.createIndexer().startAndWait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
