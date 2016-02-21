package org.jboss.tools.hibernate.search.analyzers;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

public class AnalyzersEditorInput implements IStorageEditorInput, IPersistableElement {

	/** The name of the editor input. */
	private String name;
	/** The storage object used by this editor input. */
	private IStorage storage;

	public AnalyzersEditorInput(IStorage storage) {
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
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IPersistableElement getPersistable() {
		return this;
	}

	@Override
	public String getToolTipText() {
		return getName();
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public void saveState(IMemento memento) {
		AnalyzersEditorInputFactory.saveState(memento, this);
	}

	@Override
	public String getFactoryId() {
		return AnalyzersEditorInputFactory.ID_FACTORY;
	}
	
	public void setEditorInputContents(String contents) {
		((AnalyzersEditorStorage)getStorage()).setContents(contents);
	}
	
	public void setAnalyzerSelected(String analyzer) {
		((AnalyzersEditorStorage)getStorage()).setAnalyzerSelected(analyzer);
	}

	@Override
	public IStorage getStorage() {
		return storage;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStorage(IStorage storage) {
		this.storage = storage;
	}

}
