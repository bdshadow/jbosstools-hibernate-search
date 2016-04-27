package org.jboss.tools.hibernate.search;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.eclipse.console.HibernateConsolePlugin;
import org.jboss.tools.hibernate.search.toolkit.IndexToolkitView;

public class HibernateSearchConsolePlugin {

	private static HibernateConsolePlugin hibernatePlugin;
	private static HibernateSearchConsolePlugin plugin;

	private HibernateSearchConsolePlugin() {
		hibernatePlugin = HibernateConsolePlugin.getDefault();
		setPlugin(this);
	}

	
	public IViewPart showIndexToolkitView(ConsoleConfiguration cc) {
		try {
			IWorkbenchPage page = HibernateConsolePlugin.getActiveWorkbenchWindow().getActivePage();
			IndexToolkitView indexToolkitView = (IndexToolkitView)page.showView(IndexToolkitView.INDEX_TOOLKIT_VIEW_ID);
			indexToolkitView.setInitialConsoleConfig(cc);
			return indexToolkitView;
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
