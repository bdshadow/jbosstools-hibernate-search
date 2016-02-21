package org.jboss.tools.hibernate.search.analyzers;

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
import org.hibernate.eclipse.console.ComboContributionProxy;
import org.hibernate.eclipse.console.HibernateConsolePlugin;
import org.hibernate.eclipse.console.utils.LaunchHelper;
import org.hibernate.eclipse.console.utils.ProjectUtils;
import org.hibernate.eclipse.launch.IConsoleConfigurationLaunchConstants;

public class AnalyzersCombo extends ComboContributionProxy {
	
	public static final String DEFAULT_ANALYZER = "org.apache.lucene.analysis.standard.StandardAnalyzer";

	protected SelectionAdapter selectionAdapter;
	private AnalyzersEditor editor = null;

	public AnalyzersCombo(final AnalyzersEditor editor, String id) {
		super(id);
		this.editor = editor;
		this.selectionAdapter = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				((AnalyzersEditorStorage)((AnalyzersEditorInput)editor.getEditorInput()).getStorage()).setAnalyzerSelected(comboControl.getText());
				editor.getExecuteAnalyzerAction().run();
			}
		};
	}

	@Override
	protected SelectionListener getSelectionAdapter() {
		return selectionAdapter;
	}

	@Override
	protected void populateComboBox() {
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
					setSelected();					
				} catch (JavaModelException e) {
					HibernateConsolePlugin.getDefault().log(e);
				}
			}
		});
	}
	
	protected void setSelected() {
		String selectedText = ((AnalyzersEditorStorage)((AnalyzersEditorInput)editor.getEditorInput()).getStorage()).getAnalyzerSelected();
		if (comboControl.indexOf(selectedText) == -1) { // was not recovered from memento
			comboControl.setText(DEFAULT_ANALYZER);
		} else {
			comboControl.setText(selectedText);
			this.editor.getExecuteAnalyzerAction().run();
		}
	}

	protected int getComboWidth() {
		return 300;
	}

	public String getAnalyzer() {
		return comboControl.getText();
	}

}
