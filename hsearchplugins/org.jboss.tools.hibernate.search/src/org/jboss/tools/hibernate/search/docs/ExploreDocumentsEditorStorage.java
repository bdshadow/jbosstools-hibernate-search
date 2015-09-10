package org.jboss.tools.hibernate.search.docs;

import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

public class ExploreDocumentsEditorStorage implements IStorage {
	
	private String cc;
	private String name;
	
	public ExploreDocumentsEditorStorage(String cc, String name) {
		setConsoleConfiguration(cc);
		setName(name);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return null;
	}

	@Override
	public InputStream getContents() throws CoreException {
		return null;
	}

	@Override
	public IPath getFullPath() {
		return null;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getConsoleConfiguration() {
		return cc;
	}

	public void setConsoleConfiguration(String cc) {
		this.cc = cc;
	}

}
