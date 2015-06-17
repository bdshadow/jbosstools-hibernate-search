package org.jboss.tools.hibernate.search.actions;

import java.util.Iterator;

import org.eclipse.jface.viewers.StructuredViewer;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.console.execution.ExecutionContext.Command;
import org.hibernate.eclipse.console.actions.ConsoleConfigReadyUseBaseAction;
import org.jboss.tools.hibernate.runtime.spi.IConfiguration;
import org.jboss.tools.hibernate.search.runtime.spi.HSearchServiceLookup;
import org.jboss.tools.hibernate.search.runtime.spi.IHSearchService;

public class IndexRebuildAction extends ConsoleConfigReadyUseBaseAction {
	
	public static final String INDEXREBUILD_ACTIONID = "actionid.indexrebuild";
	
	public IndexRebuildAction() {
		super("Run Index Rebuild");
		setId(INDEXREBUILD_ACTIONID);
	}

	public IndexRebuildAction(String text) {
		super(text);
		setId(INDEXREBUILD_ACTIONID);
		init(null);
	}
	
	/**
	 * @param selectionProvider
	 */
	public IndexRebuildAction(StructuredViewer selectionProvider) {
		super("Run Index Rebuild");
		setId(INDEXREBUILD_ACTIONID);
		init(selectionProvider);
	}

	@Override
	protected void doRun() {
		for (Iterator<?> i = getSelectedNonResources().iterator(); i.hasNext();) {
			Object node = i.next();
			if (!(node instanceof ConsoleConfiguration)) {
				continue;
			}
			final ConsoleConfiguration config = (ConsoleConfiguration) node;
			try {
				config.execute( new Command() {
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

	}
}
