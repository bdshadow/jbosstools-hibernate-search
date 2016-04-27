package org.jboss.tools.hibernate.search.toolkit;

import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.eclipse.console.ComboContributionProxy;
import org.hibernate.eclipse.console.utils.LaunchHelper;

public class ConfigurationCombo extends ComboContributionProxy {
	
	private String consoleConfigName;

	public ConfigurationCombo(String consoleConfigName, String id) {
		super(id);
		this.consoleConfigName = consoleConfigName;	
	}

	@Override
	protected void populateComboBox() {
		ConsoleConfiguration[] configurations = LaunchHelper.findFilteredSortedConsoleConfigs();
		final String[] names = new String[configurations.length];
		for (int i = 0; i < configurations.length; i++) {
			names[i] = configurations[i].getName();
		}
		comboControl.setItems(names);
		if (this.consoleConfigName != null) {
			comboControl.setText(this.consoleConfigName);
		} else {
			comboControl.select(0);
		}
	}
	
	public String getConsoleConfigSelected() {
		return getText();
	}
	
	public void setConsoleConfigSelected(String consoleConfigName) {
		comboControl.setText(consoleConfigName);
	}

	@Override
	protected SelectionListener getSelectionAdapter() {
		return null;
	}

	protected int getComboWidth() {
		return 150;
	}
	
	public void addModifyListener(ModifyListener modifyListener) {
		comboControl.addModifyListener(modifyListener);
	}
}
