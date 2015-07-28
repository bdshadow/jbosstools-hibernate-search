package org.jboss.tools.hibernate.search.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class AnalysisResultTabView extends ViewPart {
	
	protected CTabFolder tabs = null;

	@Override
	public void createPartControl(Composite parent) {
		tabs = new CTabFolder(parent, SWT.CLOSE | SWT.BOTTOM);
		tabs.setSimple( false );
		tabs.setUnselectedCloseVisible( false );

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
