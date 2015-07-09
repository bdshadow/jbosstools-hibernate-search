package org.jboss.tools.hibernate.search.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.jboss.tools.hibernate.search.HibernateSearchConsolePlugin;

public class AnalyzersScratchpadAction extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		HibernateSearchConsolePlugin.getDefault().openAnalyzersTestEditor();
		return null;
	}

}
