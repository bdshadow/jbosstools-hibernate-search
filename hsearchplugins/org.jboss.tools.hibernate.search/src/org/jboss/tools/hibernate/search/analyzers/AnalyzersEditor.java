package org.jboss.tools.hibernate.search.analyzers;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.eclipse.console.AbstractQueryEditor;
import org.hibernate.eclipse.console.ComboContribution;
import org.hibernate.eclipse.console.views.QueryPageTabView;

public class AnalyzersEditor extends AbstractQueryEditor {
	
	private AnalyzersEditorDocumentSetupParticipant docSetupParticipant;
	private ToolBarManager analyzersTbm;
	
	public AnalyzersEditor() {
		super();
	}
	
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1,false));
		
		createToolbar(parent);
		//createAnalyzersToolbar(parent);
		super.createPartControl( parent );
		if (getSourceViewer() != null ){
			getSourceViewer().addTextListener(new ITextListener(){

				public void textChanged(TextEvent event) {
					updateExecButton();
				}});
		}
		
		Control control = parent.getChildren()[1];
		control.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		
		// the following is needed to make sure the editor area gets focus when editing after query execution
	    // TODO: find a better way since this is triggered on every mouse click and key stroke in the editor area
    	// one more remark: without this code -> JBIDE-4446
	    StyledText textWidget = getSourceViewer().getTextWidget();
		textWidget.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				getSite().getPage().activate(AnalyzersEditor.this);
			}
		});
		textWidget.addMouseListener(new MouseAdapter() {

			public void mouseDown(MouseEvent e) {
				getSite().getPage().activate(AnalyzersEditor.this);
			}

		});
		initTextAndToolTip("Analyze");
	}
	
	private AnalyzersEditorDocumentSetupParticipant getDocumentSetupParticipant() {
        if (docSetupParticipant == null) {
            docSetupParticipant = new AnalyzersEditorDocumentSetupParticipant();
        }
        return docSetupParticipant;
    }
	
	protected void doSetInput(IEditorInput input) throws CoreException {

		super.doSetInput( input );

		/* Make sure the document partitioner is set up. The document setup
         * participant sets up document partitioning, which is used for text
         * colorizing and other text features.
         */
        IDocumentProvider docProvider = this.getDocumentProvider();
        if (docProvider != null) {
            IDocument doc = docProvider.getDocument( input );
            if (doc != null) {
            	AnalyzersEditorDocumentSetupParticipant docSetupParticipant = getDocumentSetupParticipant();
                docSetupParticipant.setup( doc );
            }
        }

	}
	
	public void executeQuery(ConsoleConfiguration cfg) {
		final IWorkbenchPage activePage = getEditorSite().getPage();
		try {
			activePage.showView(QueryPageTabView.ID);
		} catch (PartInitException e) {
			// ignore
		}
	}
	
	protected void createAnalyzersToolbar(Composite parent) {
		ToolBar bar = new ToolBar( parent, SWT.HORIZONTAL );
		bar.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		
		analyzersTbm = new ToolBarManager( bar );
		
/*		ComboContribution cc = new ComboContribution() {

			@Override
			protected org.eclipse.swt.events.SelectionListener getSelectionAdapter() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			void populateComboBox() {
				// TODO Auto-generated method stub
				
			}
			
		}*/
	}


	@Override
	protected String getConnectedImageFilePath() {
		return null;
	}

	@Override
	protected String getSaveAsFileExtension() {
		return "*.nlz";
	}

}
