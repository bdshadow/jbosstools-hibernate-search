package org.jboss.tools.hibernate.search.analyzers;

import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.hibernate.eclipse.console.HibernateConsolePlugin;

public class AnalyzersEditorDocumentSetupParticipant {
	
	/**
     * Sets up the document to be ready for use by a text file buffer.
     * 
	 * @see org.eclipse.core.filebuffers.IDocumentSetupParticipant#setup(org.eclipse.jface.text.IDocument)
	 */
	public void setup( IDocument document ) {
		JavaTextTools tools= HibernateConsolePlugin.getDefault().getJavaTextTools();
		IDocumentPartitioner partitioner= tools.createDocumentPartitioner();
		partitioner.connect(document);
		if (document instanceof IDocumentExtension3) {
			IDocumentExtension3 extension3= (IDocumentExtension3) document;
			extension3.setDocumentPartitioner(IJavaPartitions.JAVA_PARTITIONING, partitioner);
		} else {
			document.setDocumentPartitioner(partitioner);
		}
	}

}
