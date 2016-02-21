package org.jboss.tools.hibernate.search;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.eclipse.console.HibernateConsolePlugin;
import org.jboss.tools.hibernate.search.analyzers.AnalyzersEditorInput;
import org.jboss.tools.hibernate.search.analyzers.AnalyzersEditorStorage;
import org.jboss.tools.hibernate.search.docs.ExploreDocumentsEditorInput;
import org.jboss.tools.hibernate.search.docs.ExploreDocumentsEditorStorage;

public class HibernateSearchConsolePlugin {

	private static HibernateConsolePlugin hibernatePlugin;
	private static HibernateSearchConsolePlugin plugin;

	private HibernateSearchConsolePlugin() {
		hibernatePlugin = HibernateConsolePlugin.getDefault();
		setPlugin(this);
	}

	public IEditorPart openAnalyzersTestEditor(ConsoleConfiguration cc) {
		try {
			IWorkbenchPage page = HibernateConsolePlugin.getActiveWorkbenchWindow().getActivePage();
			AnalyzersEditorStorage storage = new AnalyzersEditorStorage(cc.getName(), "Analyzers Test", "");
			AnalyzersEditorInput editorInput = new AnalyzersEditorInput(storage);
			return page.openEditor(editorInput, "org.jboss.tools.hibernate.search.analyzers.AnalyzersEditor", true); //$NON-NLS-1$
		} catch (PartInitException pie) {
			return null;
		}
	}

	public IEditorPart openExploreDocumentsEditor(ConsoleConfiguration cc) {
		try {
			IWorkbenchPage page = HibernateConsolePlugin.getActiveWorkbenchWindow().getActivePage();
			ExploreDocumentsEditorStorage storage = new ExploreDocumentsEditorStorage(cc.getName(), "Lucene Documents");
			ExploreDocumentsEditorInput input = new ExploreDocumentsEditorInput(storage);
			return page.openEditor(input, "org.jboss.tools.hibernate.search.docs.ExploreDocumentsEditor", true); //$NON-NLS-1$
		} catch (PartInitException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static HibernateConsolePlugin getHibernateConsolePlugin() {
		return hibernatePlugin;
	}

	public static HibernateSearchConsolePlugin getDefault() {
		if (plugin == null) {
			return new HibernateSearchConsolePlugin();
		}
		return plugin;
	}

	private static void setPlugin(HibernateSearchConsolePlugin plugin) {
		HibernateSearchConsolePlugin.plugin = plugin;
	}

}
