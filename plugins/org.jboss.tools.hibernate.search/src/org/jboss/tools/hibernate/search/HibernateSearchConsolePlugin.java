package org.jboss.tools.hibernate.search;

import org.eclipse.ui.plugin.AbstractUIPlugin;

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

}
