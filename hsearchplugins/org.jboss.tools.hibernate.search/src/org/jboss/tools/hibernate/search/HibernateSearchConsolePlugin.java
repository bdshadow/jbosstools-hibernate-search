package org.jboss.tools.hibernate.search;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.hibernate.eclipse.console.HibernateConsolePlugin;
import org.jboss.tools.hibernate.search.analyzers.AnalyzersEditorInput;
import org.jboss.tools.hibernate.search.analyzers.AnalyzersEditorStorage;

public class HibernateSearchConsolePlugin extends AbstractUIPlugin {
	public static final String ID = "org.hibernate.search.eclipse.console";
	private static HibernateSearchConsolePlugin plugin;
	
	public static HibernateSearchConsolePlugin getDefault() {
		return plugin;
	}

	public HibernateSearchConsolePlugin() {
		super();
		setPlugin(this);
	}
	
	private static void setPlugin(HibernateSearchConsolePlugin plugin) {
		HibernateSearchConsolePlugin.plugin = plugin;
	}
	
	public IEditorPart openAnalyzersTestEditor() {
		try {
			IWorkbenchPage page = getActiveWorkbenchWindow().getActivePage();
			AnalyzersEditorStorage storage = new AnalyzersEditorStorage("Analyzers Test", "");
			AnalyzersEditorInput editorInput = new AnalyzersEditorInput(storage);
			return page.openEditor(editorInput, "org.jboss.tools.hibernate.search.analyzers.AnalyzersTestEditor", true); //$NON-NLS-1$
		} catch (PartInitException pie) {
			return null;
		}
	}
	
	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return getDefault().getWorkbench().getActiveWorkbenchWindow();
	}
	
}
