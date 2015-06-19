package org.jboss.tools.hibernate.search.actions;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.hibernate.console.execution.ExecutionContext.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.hibernate.console.ConsoleConfiguration;
import org.jboss.tools.hibernate.runtime.spi.IConfiguration;
import org.jboss.tools.hibernate.search.runtime.spi.HSearchServiceLookup;
import org.jboss.tools.hibernate.search.runtime.spi.IHSearchService;

public class IndexRebuildAction extends AbstractHandler {

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
			try {
				config.execute(new Command() {
					public Object execute() {
						final IConfiguration cfg = config.getConfiguration();
						if (cfg == null) {
							return null;
						}
						IHSearchService service = HSearchServiceLookup.findService("4.5");
						service.newIndexRebuild(config.getSessionFactory());
						return null;
					}
				});
			} catch (Exception he) {

			}

		}
		return null;
	}
}
