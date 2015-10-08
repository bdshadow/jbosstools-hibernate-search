package org.jboss.tools.hibernate.search.analyzers;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.ActionContributionItem;
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
import org.eclipse.ui.IShowEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.hibernate.eclipse.console.AnalyzersCombo;
import org.hibernate.eclipse.console.actions.ClearAction;
import org.jboss.tools.hibernate.search.actions.ExecuteAnalyzerAction;

public class AnalyzersEditor extends TextEditor implements IShowEditorInput {
	
	private AnalyzersEditorDocumentSetupParticipant docSetupParticipant;
	private ToolBarManager tbm;
	private ExecuteAnalyzerAction execAction;
	private ClearAction clearAction = null;
	private AnalyzersCombo analyzersCombo;
	
	public AnalyzersEditor() {
		super();
	}
	
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1,false));
		
		createToolbar(parent);
		super.createPartControl( parent );
		if (getSourceViewer() != null ){
			getSourceViewer().addTextListener(new ITextListener(){

				public void textChanged(TextEvent event) {
					updateExecButton();
					execAction.run();
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
		execAction.run(); //in order to restore result view after workbench restore
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
	
	protected void createToolbar(Composite parent) {
		ToolBar bar = new ToolBar( parent, SWT.HORIZONTAL );
		bar.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		
		tbm = new ToolBarManager( bar );
		execAction = new ExecuteAnalyzerAction(this);
		clearAction = new ClearAction(this);
		analyzersCombo = new AnalyzersCombo(this, "analyzer");
		
		
		ActionContributionItem item = new ActionContributionItem(execAction);
		tbm.add(item);
		tbm.add(analyzersCombo);
		tbm.add(clearAction);
		tbm.update(true);
	}

	@Override
	public void showEditorInput(IEditorInput editorInput) {
		// TODO Auto-generated method stub
		
	}
	
	protected void updateExecButton() {
		execAction.setEnabled(getEditorText().trim().length() > 0);
	}
	
	public String getEditorText() {
		IEditorInput editorInput = getEditorInput();
		IDocumentProvider docProvider = getDocumentProvider();
		IDocument doc = docProvider.getDocument( editorInput );
		return doc.get();
	}
	
	public boolean initTextAndToolTip(String text) {
		if (execAction != null) {
			execAction.initTextAndToolTip(text);
			return true;
		}
		return false;
	}
	
	public String getAnalyzerSelected() {
		return analyzersCombo.getAnalyzer();
	}
	
	public String getConsoleConfigurationName() {
		return ((AnalyzersEditorStorage)((AnalyzersEditorInput)this.getEditorInput()).getStorage()).getConsoleConfiguration();
	}
	
	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		AnalyzersEditorInput input = (AnalyzersEditorInput)getEditorInput();
		input.setEditorInputContents(getEditorText());
		input.setAnalyzerChosen(getAnalyzerSelected());
		super.doSave(progressMonitor);
	}
			

}
