package org.jboss.tools.hibernate.search.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.hibernate.console.ImageConstants;
import org.hibernate.eclipse.console.utils.EclipseImages;
import org.jboss.tools.hibernate.search.analyzers.AnalyzersEditor;
import org.jboss.tools.hibernate.search.runtime.spi.HSearchServiceLookup;
import org.jboss.tools.hibernate.search.runtime.spi.IHSearchService;
import org.jboss.tools.hibernate.search.views.AnalysisResultTabView;

public class ExecuteAnalyzerAction extends Action {

	private AnalyzersEditor editor;

	public ExecuteAnalyzerAction() {
		setImageDescriptor(EclipseImages.getImageDescriptor(ImageConstants.EXECUTE));
	}

	public ExecuteAnalyzerAction(AnalyzersEditor editor) {
		this();
		setEditor(editor);
	}

	public void run() {
		execute(editor);
	}

	protected void execute(AnalyzersEditor editor) {
		IHSearchService service = HSearchServiceLookup.findService("5.3");
		String result = service.doAnalyze(editor.getEditorText(), editor.getAnalyzerSelected());
		try {
			AnalysisResultTabView resultView = (AnalysisResultTabView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(AnalysisResultTabView.ID);
			resultView.setResult(result);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initTextAndToolTip(String text) {
		setText(text);
		setToolTipText(text);
	}

	public void setEditor(AnalyzersEditor editor) {
		this.editor = editor;
	}

}
