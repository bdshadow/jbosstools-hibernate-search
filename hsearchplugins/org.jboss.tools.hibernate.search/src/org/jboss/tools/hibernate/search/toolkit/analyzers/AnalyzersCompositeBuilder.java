package org.jboss.tools.hibernate.search.toolkit.analyzers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.hibernate.console.ConsoleConfiguration;
import org.jboss.tools.hibernate.search.HSearchConsoleConfigurationPreferences;
import org.jboss.tools.hibernate.search.runtime.spi.HSearchServiceLookup;
import org.jboss.tools.hibernate.search.runtime.spi.IHSearchService;

public class AnalyzersCompositeBuilder {
	
	private static final Map<String, Composite> consoleConfigTab = new HashMap<String, Composite>();
	private static AnalyzersCompositeBuilder instance;
	
	private ToolBarManager tbm;
	private AnalyzersCombo analyzersCombo;
	
	private AnalyzersCompositeBuilder() {
		
	}
	
	public static AnalyzersCompositeBuilder getInstance() {
		if (instance == null) {
			return instance = new AnalyzersCompositeBuilder();
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
		
		createToolbar(container, consoleConfigName);
		
		final Text input = new Text(container, SWT.MULTI | SWT.BORDER);
		input.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		final Text output = new Text(container, SWT.MULTI | SWT.BORDER);
		output.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		output.setEditable(false);
		input.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				IHSearchService service = HSearchServiceLookup.findService(HSearchConsoleConfigurationPreferences.getHSearchVersion(consoleConfigName));
				String result = service.doAnalyze(((Text)e.getSource()).getText(), getAnalyzerSelected());
				output.setText(result);			
			}
		});
		container.update();
		return container;
	}
	
	protected void createToolbar(Composite parent, String consoleConfig) {
		ToolBar bar = new ToolBar( parent, SWT.HORIZONTAL );
		GridData barGridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		barGridData.horizontalSpan = 2;
		bar.setLayoutData(barGridData);
				
		tbm = new ToolBarManager(bar);
		analyzersCombo = new AnalyzersCombo(consoleConfig, "analyzer");
		
		tbm.add(analyzersCombo);
		tbm.update(true);
	}
	
	public String getAnalyzerSelected() {
		return analyzersCombo.getAnalyzer();
	}	
}
