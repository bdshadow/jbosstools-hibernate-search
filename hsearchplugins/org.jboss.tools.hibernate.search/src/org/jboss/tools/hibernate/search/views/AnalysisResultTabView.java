package org.jboss.tools.hibernate.search.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class AnalysisResultTabView extends ViewPart {
	
	public static final String ID = "org.jboss.tools.hibernate.search.views.AnalysisResultTabView";
	
	protected Text output;

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1,false));
		
		output = new Text(parent, SWT.MULTI | SWT.BORDER);
		output.setEditable(false);

		Control control = parent.getChildren()[1];
		control.setLayoutData( new GridData( GridData.FILL_BOTH ) );

	}

	@Override
	public void setFocus() {

	}
	
	public void setResult(String result) {
		output.setText(result);
		output.update();
	}

}
