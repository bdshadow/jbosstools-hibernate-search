package org.hibernate.eclipse.console;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class AnalyzersCombo extends ComboContribution {
	
	protected SelectionAdapter selectionAdapter;
	private String analyzer = null;

	public AnalyzersCombo(String id) {
		super(id);
		setSelectionAdapter();
	}

	@Override
	protected SelectionListener getSelectionAdapter() {
		return selectionAdapter;
	}

	@Override
	void populateComboBox() {
		comboControl.getDisplay().syncExec( new Runnable() {

			public void run() {
				String[] items = new String[] {
						"org.apache.lucene.analysis.standard.StandardAnalyzer"
				};
				comboControl.setItems( items );
			}

		} );

	}
	
	protected void setSelectionAdapter() {
		selectionAdapter = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				analyzer = getText();
			}
		};
	}
	
	protected int getComboWidth() {
		return 300;
	}
	
	public String getAnalyzer() {
		return this.analyzer;
	}

}
