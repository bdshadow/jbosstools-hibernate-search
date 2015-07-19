package org.jboss.tools.hibernate.search.actions;

import org.eclipse.jface.action.Action;
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
		IHSearchService service = HSearchServiceLookup.findService("4.5");
		String result = service.doAnalyze(editor.getEditorText(), editor.getAnalyzerSelected());
		MessageConsole myConsole = findConsole("Analysis Result");
		MessageConsoleStream out = myConsole.newMessageStream();
		out.println(result);
		out.println();
	}

	public void initTextAndToolTip(String text) {
		setText(text);
		setToolTipText(text);
	}

	public void setEditor(AnalyzersEditor editor) {
		this.editor = editor;
	}

	private MessageConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (name.equals(existing[i].getName()))
				return (MessageConsole) existing[i];
		// no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}

}
