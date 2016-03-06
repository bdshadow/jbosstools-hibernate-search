package org.jboss.tools.hibernate.search;

import java.io.IOException;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
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
import org.apache.lucene.util.Version;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.annotations.Indexed;
import org.jboss.tools.hibernate.runtime.common.IFacade;
import org.jboss.tools.hibernate.runtime.spi.IClassMetadata;
import org.jboss.tools.hibernate.runtime.spi.ISessionFactory;
import org.jboss.tools.hibernate.runtime.v_4_0.internal.ServiceImpl;
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
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String doAnalyze(String text, String analyzerClassName) {
		Analyzer analyzer = null;
		try {
			Class analyzerClass = Class.forName(analyzerClassName);
			for (Constructor constructor : analyzerClass.getConstructors()) {
				if (constructor.getParameterTypes().length == 0) {
					constructor.setAccessible(true);
					analyzer = (Analyzer) constructor.newInstance();
					break;
				}
				if (constructor.getParameterTypes().length == 1 && constructor.getParameterTypes()[0].equals(Version.class)) {
					constructor.setAccessible(true);
					Version luceneVersion = Version.LUCENE_34;
					analyzer = (Analyzer) constructor.newInstance(luceneVersion);
					break;
				}
			}
			
		} catch (ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
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

	@Override
	public List<Map<String, String>> getEntityDocuments(ISessionFactory sessionFactory, Class... entities) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SessionFactoryImpl factory = (SessionFactoryImpl) ((IFacade) sessionFactory).getTarget();
		FullTextSession fullTextSession = Search.getFullTextSession(factory.openSession());
		IndexReader ireader = fullTextSession.getSearchFactory().getIndexReaderAccessor().open(entities);

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

}
