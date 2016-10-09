package org.jboss.tools.hibernate.search;

import java.io.IOException;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexReader.FieldOption;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.util.Version;
import org.hibernate.Query;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.annotations.Indexed;
import org.jboss.tools.hibernate.runtime.common.IFacade;
import org.jboss.tools.hibernate.runtime.spi.IClassMetadata;
import org.jboss.tools.hibernate.runtime.spi.IService;
import org.jboss.tools.hibernate.runtime.spi.ISessionFactory;
import org.jboss.tools.hibernate.runtime.spi.ServiceLookup;
import org.jboss.tools.hibernate.search.runtime.spi.IHSearchService;

public class HSearchServiceImpl implements IHSearchService {

	@Override
	public IService getHibernateService() {
		return ServiceLookup.findService("4.0");
	}
	
	@Override
	public void newIndexRebuild(ISessionFactory sessionFactory, Set<Class> entities) {
		try {
			SessionFactoryImpl factory = (SessionFactoryImpl) ((IFacade) sessionFactory).getTarget();
			FullTextSession fullTextSession = Search.getFullTextSession(factory.openSession());
			fullTextSession.createIndexer(entities.toArray(new Class[0])).startAndWait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String doAnalyze(String text, String analyzerClassName) {
		Analyzer analyzer = getAnalyzerByName(analyzerClassName);
		if (analyzer == null) {
			return "";
		}

		try {
			TokenStream stream = analyzer.tokenStream("field", new StringReader(text));
			CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
			stream.reset();
			StringBuilder result = new StringBuilder();
			while (stream.incrementToken()) {
				result.append(termAtt.toString() + "\n");
			}

			stream.end();
			stream.close();
			return result.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	private Analyzer getAnalyzerByName(String analyzerClassName) {
		try {
			Class<?> analyzerClass = Class.forName(analyzerClassName);
			for (Constructor<?> constructor : analyzerClass.getConstructors()) {
				if (constructor.getParameterTypes().length == 0) {
					constructor.setAccessible(true);
					return (Analyzer) constructor.newInstance();
				}
				if (constructor.getParameterTypes().length == 1 && constructor.getParameterTypes()[0].equals(Version.class)) {
					constructor.setAccessible(true);
					Version luceneVersion = Version.LUCENE_34;
					return (Analyzer) constructor.newInstance(luceneVersion);
				}
			}
			
		} catch (ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Map<String, String>> getEntityDocuments(ISessionFactory sessionFactory, Class... entities) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		IndexReader ireader = getIndexReader(sessionFactory, entities);

		for (int i = 0; i < ireader.numDocs(); i++) {
			try {
				Document doc = ireader.document(i);
				Map<String, String> fieldValueMap = new TreeMap<String, String>();
				for (Fieldable field: doc.getFields()) {
					fieldValueMap.put(field.name(), field.stringValue());
				}
				list.add(fieldValueMap);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
	
	@Override
	public Set<Class<?>> getIndexedTypes(ISessionFactory sessionFactory) {
		Map<String, IClassMetadata> meta = sessionFactory.getAllClassMetadata();
		Set<Class<?>> entities = new HashSet<Class<?>>();
		for (String entity : new TreeSet<String>(meta.keySet())) {
			Class<?> entityClass = meta.get(entity).getMappedClass();
			Annotation[] annotations = entityClass.getAnnotations();
			for (Annotation annotation: annotations) {
				if (Indexed.class.isAssignableFrom(annotation.annotationType())) {
					entities.add(entityClass);
					break;
				}
			}
		}
		return entities;
	}
	
	public Set<String> getIndexedFields(ISessionFactory sessionFactory, Class<?> entity) {
		final Set<String> fields = new TreeSet<String>();
		IndexReader ireader = getIndexReader(sessionFactory, entity);
		Iterator<String> iterator = ireader.getFieldNames(FieldOption.INDEXED).iterator();
		iterator.forEachRemaining(s -> fields.add(s));
		return fields;
	}
	
	private FullTextSession getFullTextSession(ISessionFactory sessionFactory) {
		SessionFactoryImpl factory = (SessionFactoryImpl) ((IFacade) sessionFactory).getTarget();
		return Search.getFullTextSession(factory.openSession());
	}
	
	private IndexReader getIndexReader(ISessionFactory sessionFactory, Class<?>... classes) {
		return getFullTextSession(sessionFactory).getSearchFactory().getIndexReaderAccessor().open(classes);
	}
	
	@Override
	public List<Object> search(ISessionFactory sessionFactory, Class<?> entity, String defaultField, String analyzer, String request) {
		FullTextSession session = getFullTextSession(sessionFactory);
		QueryParser parser = new QueryParser(Version.LUCENE_34, defaultField, getAnalyzerByName(analyzer));
		org.apache.lucene.search.Query luceneQuery = null;
		try {
			luceneQuery = parser.parse(request);
		} catch (ParseException e) {
		}
		Query fullTextQuery = 
				entity == null ? 
						session.createFullTextQuery(luceneQuery) : 
							session.createFullTextQuery(luceneQuery, entity);
		return fullTextQuery.list();
	}
}
