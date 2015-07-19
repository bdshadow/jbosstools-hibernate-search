package org.jboss.tools.hibernate.search.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.editors.text.TextEditor;
import org.hibernate.console.ImageConstants;
import org.hibernate.eclipse.console.utils.EclipseImages;
import org.jboss.tools.hibernate.search.analyzers.AnalyzersEditor;
import org.jboss.tools.hibernate.search.runtime.spi.HSearchServiceLookup;
import org.jboss.tools.hibernate.search.runtime.spi.IHSearchService;

public class ExecuteAnalyzerAction extends Action {
	
	private AnalyzersEditor editor;
	
	public ExecuteAnalyzerAction() {
		setImageDescriptor(EclipseImages.getImageDescriptor(ImageConstants.EXECUTE) );
	}
	
	public ExecuteAnalyzerAction(AnalyzersEditor editor) {
		this();
		setEditor(editor);
	}
	
	public void run() {
		execute(editor );
	}
	
	protected void execute(AnalyzersEditor editor) {
		IHSearchService service = HSearchServiceLookup.findService("4.5");
		service.doAnalyze(editor.getEditorText(), editor.getAnalyzerSelected());
	}
	
	public void initTextAndToolTip(String text) {
		setText(text);
		setToolTipText(text);
	}
	
	public void setEditor(AnalyzersEditor editor) {
		this.editor = editor;
	}

}
