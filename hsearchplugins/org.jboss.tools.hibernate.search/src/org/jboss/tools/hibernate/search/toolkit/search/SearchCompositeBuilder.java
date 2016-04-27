package org.jboss.tools.hibernate.search.toolkit.search;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.hibernate.console.ConsoleConfiguration;
import org.jboss.tools.hibernate.search.toolkit.analyzers.AnalyzersCombo;

public class SearchCompositeBuilder {
	
	private static final Map<String, Composite> consoleConfigTab = new HashMap<String, Composite>();
	
	private static SearchCompositeBuilder instance;
	
	private AnalyzersCombo analyzersCombo;
	
	public static SearchCompositeBuilder getInstance() {
		if (instance == null) {
			return instance = new SearchCompositeBuilder();
		}
		return instance;
	}
	
	public Composite getTab(CTabFolder folder, ConsoleConfiguration consoleConfig) {
		final String consoleConfigName = consoleConfig.getName();
		if (consoleConfigTab.containsKey(consoleConfigName)) {
			return consoleConfigTab.get(consoleConfigName);
		}
		Composite newTab = createTab(folder, consoleConfig);
		consoleConfigTab.put(consoleConfigName, newTab);
		return newTab;
	}
	
	protected Composite createTab(CTabFolder folder, ConsoleConfiguration consoleConfig) {
		final String consoleConfigName = consoleConfig.getName();
		Composite container = new Composite(folder, SWT.TOP);
		
		container.setLayout(new GridLayout(2, true));
		final Text query = new Text(container, SWT.MULTI | SWT.BORDER);
		GridData queryGridData = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
		queryGridData.heightHint = 5 * query.getLineHeight();
		queryGridData.verticalSpan = 2;
		query.setLayoutData(queryGridData);
		
		ToolBar bar = new ToolBar(container, SWT.HORIZONTAL );
		GridData barGridData = new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, false);
		bar.setLayoutData(barGridData);
		ToolBarManager tbm = new ToolBarManager(bar);
		analyzersCombo = new AnalyzersCombo(consoleConfigName, "analyzersForSearch");
		tbm.add(analyzersCombo);
		tbm.update(true);
		
		
		Button searchButton = new Button(container, SWT.PUSH);
		searchButton.setText("Search");
		
		container.update();
		return container;
	}

}
