package org.jboss.tools.hibernate.search.analyzers;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IShowEditorInput;
import org.eclipse.ui.editors.text.TextEditor;

public class AnalyzersTestEditor extends TextEditor implements IShowEditorInput {

	@Override
	public void showEditorInput(IEditorInput editorInput) {
		try {
			super.doSetInput(editorInput);
		} catch (CoreException e) {
			e.printStackTrace();
		}

	}

}
