package org.jboss.tools.hibernate.search.actions;

import org.eclipse.jface.viewers.StructuredViewer;
import org.hibernate.eclipse.console.actions.ConsoleConfigReadyUseBaseAction;

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
		// TODO Auto-generated method stub

	}
}
