package org.jboss.tools.hibernate.search.actions;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.hibernate.console.ConsoleConfiguration;
import org.jboss.tools.hibernate.search.HibernateSearchConsolePlugin;

public class ExploreDocumentsAction extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection sel = HandlerUtil.getActiveMenuSelection(event);
		IStructuredSelection selection = (IStructuredSelection) sel;
		
		for (Iterator<?> i = selection.iterator(); i.hasNext();) {
			Object node = i.next();
			if (!(node instanceof ConsoleConfiguration)) {
				continue;
			}
			final ConsoleConfiguration config = (ConsoleConfiguration) node;
			HibernateSearchConsolePlugin.getDefault().openExploreDocumentsEditor(config);
		}
		
		return null;
	}

}
