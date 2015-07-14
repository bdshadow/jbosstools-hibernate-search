package org.jboss.tools.hibernate.search.analyzers;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;

public class AnalyzersEditorInputFactory implements IElementFactory {
	
	public final static String ID_FACTORY =  "org.jboss.tools.hibernate.search.analyzers.AnalyzersTestEditorInputFactory"; //$NON-NLS-1$
    public final static String ID_STORAGE_EDITOR_INPUT = "AnalyzersTestEditorInput"; //$NON-NLS-1$
    
    public final static String KEY_EDITOR_INPUT_TYPE = "editorInputType"; //$NON-NLS-1$ 
    public final static String KEY_STORAGE_CONTENT = "storageContent"; //$NON-NLS-1$
    public final static String KEY_STORAGE_NAME = "storageName"; //$NON-NLS-1$

	@Override
	public IAdaptable createElement(IMemento memento) {      
        // Create a Storage object from the memento.
        String contentName = memento.getString( KEY_STORAGE_NAME );
        String contentString = memento.getString( KEY_STORAGE_CONTENT );
        AnalyzersEditorStorage storage = new AnalyzersEditorStorage(contentName, contentString );
        
        AnalyzersEditorInput analyzersInput = new AnalyzersEditorInput( storage );
        return analyzersInput; 
	}
	
	public static void saveState(IMemento memento, AnalyzersEditorInput input) {
        // Save the editor input type.
        memento.putString( KEY_EDITOR_INPUT_TYPE, ID_STORAGE_EDITOR_INPUT );
        
        String storageName = null;
        String storageContent = ""; //$NON-NLS-1$
        IStorage storage = input.getStorage();
        if (storage != null) {
            storageName = storage.getName();            
            if (storage instanceof AnalyzersEditorStorage) {
                AnalyzersEditorStorage sqlEditorStorage = (AnalyzersEditorStorage) storage;
                storageContent = sqlEditorStorage.getContentsString();
            }
        }
     
        // Save the storage content name in the memento.
        memento.putString( KEY_STORAGE_NAME, storageName );
        
        // Save the storage content string in the memento.
        memento.putString( KEY_STORAGE_CONTENT, storageContent );
    }

}
