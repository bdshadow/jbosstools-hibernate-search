package org.jboss.tools.hibernate.search.toolkit.analyzers;

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
	private String consoleConfigName;

	public AnalyzersCombo(String consoleConfigName, String id) {
		super(id);
		this.consoleConfigName = consoleConfigName;
	}

	@Override
	protected SelectionListener getSelectionAdapter() {
		return selectionAdapter;
	}

	@Override
	protected void populateComboBox() {
		String projName = null;
		try {
			ILaunchConfiguration launchConfiguration = LaunchHelper.findHibernateLaunchConfig(this.consoleConfigName);
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
					comboControl.setText(DEFAULT_ANALYZER);
				} catch (JavaModelException e) {
					HibernateConsolePlugin.getDefault().log(e);
				}
			}
		});
	}

	protected int getComboWidth() {
		return 300;
	}

	public String getAnalyzer() {
		return comboControl.getText();
	}

}
