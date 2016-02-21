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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
		execAction = new ExecuteAnalyzerAction(this);
	}
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1,false));
		
		createToolbar(parent);
		super.createPartControl(parent);
		if (getSourceViewer() != null ) {
			getSourceViewer().addTextListener(new ITextListener() {

				public void textChanged(TextEvent event) {
					execAction.run();
				}});
		}
		Control control = parent.getChildren()[1];
		control.setLayoutData( new GridData( GridData.FILL_BOTH ) );
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
		
		tbm = new ToolBarManager(bar);
		clearAction = new ClearAction(this);
		analyzersCombo = new AnalyzersCombo(this, "analyzer");
		
		tbm.add(analyzersCombo);
		tbm.add(clearAction);
		tbm.update(true);
	}

	@Override
	public void showEditorInput(IEditorInput editorInput) {
		// TODO Auto-generated method stub
		
	}
	
	public String getEditorText() {
		IEditorInput editorInput = getEditorInput();
		IDocumentProvider docProvider = getDocumentProvider();
		IDocument doc = docProvider.getDocument( editorInput );
		return doc.get();
	}
	
	public String getAnalyzerSelected() {
		return analyzersCombo.getAnalyzer();
	}
	
	public String getConsoleConfigurationName() {
		return ((AnalyzersEditorStorage)((AnalyzersEditorInput)this.getEditorInput()).getStorage()).getConsoleConfiguration();
	}
	
	public ExecuteAnalyzerAction getExecuteAnalyzerAction() {
		return this.execAction;
	}
	
	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		AnalyzersEditorInput input = (AnalyzersEditorInput)getEditorInput();
		input.setEditorInputContents(getEditorText());
		input.setAnalyzerSelected(getAnalyzerSelected());
		super.doSave(progressMonitor);
	}
			

}
