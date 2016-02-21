package org.hibernate.eclipse.console;

public abstract class ComboContributionProxy extends ComboContribution {
	
	protected ComboContributionProxy(String id) {
		super(id);
	}

	protected String getText() {
		return super.getText();
	}

	@Override
	protected abstract void populateComboBox();

}
