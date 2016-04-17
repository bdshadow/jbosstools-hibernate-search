package org.jboss.tools.hibernate.search;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.hibernate.console.ConsoleConfiguration;
import org.jboss.tools.hibernate.search.analyzers.AnalyzersCompositeBuilder;

public class IndexToolkitView extends ViewPart {
	public IndexToolkitView() {
	}
	
	private static final String ANALYZERS_TAB_NAME = "Analyzers";
	
	private ConsoleConfiguration initialConsoleConfig = null;

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, true));
		ConfigurationCombo consoleConfigCombo = new ConfigurationCombo(initialConsoleConfig == null ? null : initialConsoleConfig.getName(), "Toolkit.ConsoleConfigCombo");
		consoleConfigCombo.fill(parent);
		
		CTabFolder folder = new CTabFolder(parent, SWT.TOP);
		folder.setLayoutData(new GridData(GridData.FILL, SWT.FILL, true, true));
				
		CTabItem analyzersTab = new CTabItem(folder, SWT.NONE);
		analyzersTab.setText(ANALYZERS_TAB_NAME);
		
		AnalyzersCompositeBuilder analyzersBuilder = AnalyzersCompositeBuilder.getInstance();
		analyzersTab.setControl(analyzersBuilder.getTabContainer(folder, consoleConfigCombo.getConsoleConfigSelected()));
		folder.setSelection(0);
	}
	
	public void setInitialConsoleConfig(ConsoleConfiguration consoleConfig) {
		this.initialConsoleConfig = consoleConfig;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
