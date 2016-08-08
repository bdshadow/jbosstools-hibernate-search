package org.jboss.tools.hibernate.search.toolkit.search;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.hibernate.console.ConsoleConfiguration;
import org.jboss.tools.hibernate.search.console.ConsoleConfigurationUtils;
import org.jboss.tools.hibernate.search.toolkit.analyzers.AnalyzersCombo;

public class SearchCompositeBuilder {
	
	private static final Map<String, Composite> consoleConfigTab = new HashMap<String, Composite>();
	
	private static SearchCompositeBuilder instance;
	
	private AnalyzersCombo analyzersCombo;
	
	private Combo entityCombo;
	private Combo fieldsCombo;
	
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
		Composite container = new Composite(folder, SWT.VERTICAL);
		container.setLayout(new GridLayout());
		
		Composite entitiesContainer = new Composite(container, SWT.TOP);
		GridData entitiesGridData = new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, false);
		entitiesContainer.setLayoutData(entitiesGridData);
		createEntityCombo(entitiesContainer, consoleConfig);
		entitiesContainer.pack();
		
		Composite searchDataComposite = new Composite(container, SWT.TOP);
		searchDataComposite.setLayout(new GridLayout(2, true));
		final Text query = new Text(searchDataComposite, SWT.MULTI | SWT.BORDER);
		GridData queryGridData = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
		queryGridData.heightHint = 5 * query.getLineHeight();
		queryGridData.verticalSpan = 2;
		query.setLayoutData(queryGridData);
		
		ToolBar bar = new ToolBar(searchDataComposite, SWT.HORIZONTAL);
		GridData barGridData = new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, false);
		bar.setLayoutData(barGridData);
		ToolBarManager tbm = new ToolBarManager(bar);
		analyzersCombo = new AnalyzersCombo(consoleConfigName, "analyzersForSearch");
		tbm.add(analyzersCombo);
		tbm.update(true);
		
		Button searchButton = new Button(searchDataComposite, SWT.PUSH);
		searchButton.setText("Search");
		searchButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
								
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		searchDataComposite.pack();
		
		container.pack();
		container.update();
		return container;
	}
	
	protected void createEntityCombo(Composite parent, final ConsoleConfiguration consoleConfig) {
		if (!ConsoleConfigurationUtils.loadSessionFactorySafely(consoleConfig)) {
			return;
		};
		
		Composite entitiesComposite = new Composite(parent, SWT.NONE);
		entitiesComposite.setLayout(new RowLayout());
		this.entityCombo = new Combo(entitiesComposite, SWT.NONE);
		
		for (Class<?> entity: ConsoleConfigurationUtils.getIndexedEntities(consoleConfig)) {
			entityCombo.add(entity.getName());
		}
		
		if (entityCombo.getItemCount() == 0) {
			new Label(entitiesComposite, SWT.NONE).setText("No entity classes anntotated @Indexed");
			return;
		}
		
		this.fieldsCombo = new Combo(entitiesComposite, SWT.NONE);
		
		this.entityCombo.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				ClassLoader classloader =  ConsoleConfigurationUtils.getClassLoader(consoleConfig);
				try {
					fieldsCombo.removeAll();
					Class<?> clazz = Class.forName(((Combo)e.getSource()).getText(), true, classloader);
					Set<String> fields = ConsoleConfigurationUtils.getHSearchService(consoleConfig)
							.getIndexedFields(consoleConfig.getSessionFactory(), clazz);
					fields.forEach(s -> fieldsCombo.add(s));
					fieldsCombo.select(0);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});	
		entityCombo.select(0);
		entitiesComposite.pack();	
	}

}
