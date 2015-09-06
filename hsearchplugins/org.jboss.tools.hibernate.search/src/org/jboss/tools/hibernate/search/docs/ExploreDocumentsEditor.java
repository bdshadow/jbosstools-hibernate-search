package org.jboss.tools.hibernate.search.docs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class ExploreDocumentsEditor extends EditorPart {
	
	private TableViewer tableViewer;

	public ExploreDocumentsEditor() {
		super();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, true));
		new Label(parent, SWT.NONE).setText("Browse by document number");
		new Text(parent, SWT.SEARCH);
		
		this.tableViewer = new TableViewer(parent, SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createColumns();
		
		Table table = tableViewer.getTable();
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	    
	    tableViewer.setContentProvider(new ArrayContentProvider());
	    GridData gridData = new GridData();
	    gridData.verticalAlignment = SWT.FILL;
	    gridData.horizontalSpan = 2;
	    gridData.horizontalAlignment = SWT.FILL;
	    gridData.grabExcessVerticalSpace = true;
	    tableViewer.getControl().setLayoutData(gridData);			
		
	}
	
	protected void createColumns() {
		TableViewerColumn column = createSingleColumn("Field");
		column.setLabelProvider(new ColumnLabelProvider() /*{
			@Override
			public String getText(Object element) {
				IndexableField p = (IndexableField) element;
			}
		}*/);
		column = createSingleColumn("Value");
		column.setLabelProvider(new ColumnLabelProvider());
	}
	
	protected TableViewerColumn createSingleColumn(String title) {
		TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(150);
		column.setResizable(true);
		return viewerColumn;
	}

	@Override
	public void setFocus() {
		tableViewer.getControl().setFocus();
	}

}
