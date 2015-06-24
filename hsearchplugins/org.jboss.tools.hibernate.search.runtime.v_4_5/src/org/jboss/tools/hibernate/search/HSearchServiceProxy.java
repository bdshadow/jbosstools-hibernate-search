package org.jboss.tools.hibernate.search;

import java.util.Set;

import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.jboss.tools.hibernate.runtime.common.IFacade;
import org.jboss.tools.hibernate.runtime.spi.ISessionFactory;
import org.jboss.tools.hibernate.runtime.v_4_3.internal.ServiceImpl;
import org.jboss.tools.hibernate.search.runtime.spi.IHSearchService;

public class HSearchServiceProxy extends ServiceImpl implements IHSearchService {
	
	@Override
	public void newIndexRebuild(ISessionFactory sessionFactory, Set<Class> entities) {
		try {
			SessionFactoryImpl factory = (SessionFactoryImpl) ((IFacade) sessionFactory).getTarget();
			FullTextSession fullTextSession = Search.getFullTextSession(factory.openSession());
			fullTextSession.createIndexer(entities.toArray(new Class[0])).startAndWait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	}
	}
	
	
}
