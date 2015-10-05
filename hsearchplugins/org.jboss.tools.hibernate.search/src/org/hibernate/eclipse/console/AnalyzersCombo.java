package org.hibernate.eclipse.console;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.hibernate.eclipse.console.utils.LaunchHelper;
import org.hibernate.eclipse.console.utils.ProjectUtils;
import org.hibernate.eclipse.launch.IConsoleConfigurationLaunchConstants;
import org.jboss.tools.hibernate.search.analyzers.AnalyzersEditor;
import org.jboss.tools.hibernate.search.analyzers.AnalyzersEditorInput;
import org.jboss.tools.hibernate.search.analyzers.AnalyzersEditorStorage;

public class AnalyzersCombo extends ComboContribution {

	protected SelectionAdapter selectionAdapter;
	private String analyzer = null;
	private AnalyzersEditor editor = null;

	public AnalyzersCombo(AnalyzersEditor editor, String id) {
		super(id);
		this.editor = editor;
		setSelectionAdapter();
	}

	@Override
	protected SelectionListener getSelectionAdapter() {
		return selectionAdapter;
	}

	@Override
	void populateComboBox() {
		String projName = null;
		try {
			ILaunchConfiguration launchConfiguration = LaunchHelper.findHibernateLaunchConfig(editor.getConsoleConfigurationName());
			projName = launchConfiguration.getAttribute(IConsoleConfigurationLaunchConstants.PROJECT_NAME, ""); //$NON-NLS-1$
		} catch (CoreException e) {
			HibernateConsolePlugin.getDefault().log(e);
		}
		IJavaProject project = ProjectUtils.findJavaProject(projName);
		final IType analyzersType = ProjectUtils.findType(project, "org.apache.lucene.analysis.Analyzer");

		comboControl.getDisplay().syncExec(new Runnable() {

			public void run() {
				try {
					IType[] types = analyzersType.newTypeHierarchy(new NullProgressMonitor()).getAllSubtypes(analyzersType);
					List<String> typesList = new LinkedList<String>();
					for (IType type : types) {
						try {
							if (type.getMethod(type.getElementName(), new String[0]).isConstructor()) {
								typesList.add(type.getFullyQualifiedName());
								continue;
							}
							
						} catch (JavaModelException e) {
						}
						
						try {
							if (type.getMethod(type.getElementName(), new String[] {"Lorg.apache.lucene.util.Version;"}).isConstructor()) {
								typesList.add(type.getFullyQualifiedName());
								continue;
							}
						} catch (JavaModelException e) {
						}
						
					}
					comboControl.setItems(typesList.toArray(new String[0]));
					setItemSelected();
				} catch (JavaModelException e) {
					HibernateConsolePlugin.getDefault().log(e);
				}

			}

		});
	}
	
	protected void setItemSelected() {
		String analyzerChosen = ((AnalyzersEditorStorage)((AnalyzersEditorInput)editor.getEditorInput()).getStorage()).getAnalyzerChosen();
		for (int i = 0; i < comboControl.getItemCount(); i++) {
			if (comboControl.getItem(i).equals(analyzerChosen)) {
				comboControl.select(i);
				return;
			}
		}
	}

	protected void setSelectionAdapter() {
		selectionAdapter = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				analyzer = getText();
			}
		};
	}

	protected int getComboWidth() {
		return 300;
	}

	public String getAnalyzer() {
		return this.analyzer;
	}

}
