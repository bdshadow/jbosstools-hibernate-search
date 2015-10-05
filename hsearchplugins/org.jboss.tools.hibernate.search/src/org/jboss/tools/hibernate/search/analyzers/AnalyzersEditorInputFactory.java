package org.jboss.tools.hibernate.search.analyzers;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;

public class AnalyzersEditorInputFactory implements IElementFactory {
	
	public final static String ID_FACTORY =  "org.jboss.tools.hibernate.search.analyzers.AnalyzersTestEditorInputFactory"; //$NON-NLS-1$
    public final static String ID_STORAGE_EDITOR_INPUT = "AnalyzersTestEditorInput"; //$NON-NLS-1$
    
    public final static String KEY_CONFIGURATION_NAME = "configurationname"; //$NON-NLS-1$
    public final static String KEY_EDITOR_INPUT_TYPE = "editorInputType"; //$NON-NLS-1$ 
    public final static String KEY_STORAGE_CONTENT = "storageContent"; //$NON-NLS-1$
    public final static String KEY_STORAGE_NAME = "storageName"; //$NON-NLS-1$
    public final static String KEY_STORAGE_ANALYZER = "analyzerSelected"; //$NON-NLS-1$
    
	@Override
	public IAdaptable createElement(IMemento memento) {      
        // Create a Storage object from the memento.
        String contentName = memento.getString( KEY_STORAGE_NAME );
        String contentString = memento.getString( KEY_STORAGE_CONTENT );
        String configurationName = memento.getString(KEY_CONFIGURATION_NAME);
        AnalyzersEditorStorage storage = new AnalyzersEditorStorage(configurationName, contentName, contentString );
        storage.setAnalyzerChosen(memento.getString(KEY_STORAGE_ANALYZER));
        
        AnalyzersEditorInput analyzersInput = new AnalyzersEditorInput( storage );
        return analyzersInput; 
	}
	
	public static void saveState(IMemento memento, AnalyzersEditorInput input) {
        // Save the editor input type.
        memento.putString( KEY_EDITOR_INPUT_TYPE, ID_STORAGE_EDITOR_INPUT );
        
        String storageName = null;
        String storageContent = ""; //$NON-NLS-1$
        IStorage storage = input.getStorage();
        String configurationName = ""; //$NON-NLS-1$
        String analyzer = "";
        if (storage != null) {
            storageName = storage.getName();            
            if (storage instanceof AnalyzersEditorStorage) {
                AnalyzersEditorStorage analyzersEditorStorage = (AnalyzersEditorStorage) storage;
                storageContent = analyzersEditorStorage.getContentsString();
                configurationName = analyzersEditorStorage.getConsoleConfiguration();
                analyzer = analyzersEditorStorage.getAnalyzerChosen();
            }
        }
     
        // Save the storage content name in the memento.
        memento.putString( KEY_STORAGE_NAME, storageName );
        
        // Save the storage content string in the memento.
        memento.putString( KEY_STORAGE_CONTENT, storageContent );
        
        memento.putString( KEY_CONFIGURATION_NAME, configurationName);
        
        memento.putString(KEY_STORAGE_ANALYZER,  analyzer);
    }

}
