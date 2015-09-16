package org.jboss.tools.hibernate.search.docs;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

public class ExploreDocumentsEditorInput implements IStorageEditorInput {
	
	private String name;
	
	private IStorage storage;
	
	public ExploreDocumentsEditorInput(IStorage storage) {
		if (storage == null) {
			throw new IllegalArgumentException();
        }
		setStorage(storage);
		setName(storage.getName());
	}

	@Override
	public boolean exists() {
		if (storage != null) {
        	return true;
        }
        return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStorage(IStorage storage) {
		this.storage = storage;
	}

	@Override
	public IStorage getStorage() {
		return this.storage;
	}
}
