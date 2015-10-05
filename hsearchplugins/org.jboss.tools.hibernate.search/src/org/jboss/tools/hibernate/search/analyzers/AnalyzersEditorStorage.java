package org.jboss.tools.hibernate.search.analyzers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IPath;

public class AnalyzersEditorStorage implements IStorage {

	private String contents;
	private String nameLabel;
	private String cc;
	private String analyzerChosen;

	public AnalyzersEditorStorage(String cc, String name, String source) {
		setConsoleConfiguration(cc);
		setName(name);
		setContents(source);
		setAnalyzerChosen("org.apache.lucene.analysis.standard.StandardAnalyzer");
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public IPath getFullPath() {
		return null;
	}

	@Override
	public String getName() {
		return nameLabel;
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public InputStream getContents() {
		return new ByteArrayInputStream(contents.getBytes());
	}

	public String getContentsString() {
		String contentsString = ""; //$NON-NLS-1$

		InputStream contentsStream = getContents();

		// The following code was adapted from
		// StorageDocumentProvider.setDocumentContent method.
		Reader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(contentsStream));
			StringBuffer buffer = new StringBuffer();
			char[] readBuffer = new char[2048];
			int n = in.read(readBuffer);
			while (n > 0) {
				buffer.append(readBuffer, 0, n);
				n = in.read(readBuffer);
			}
			contentsString = buffer.toString();
		} catch (IOException x) {
			// ignore and save empty content
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException x) {
					// ignore, too late to do anything here
				}
			}
		}

		return contentsString;
	}

	public void setName(String name) {
		nameLabel = name;
	}

	public void setContents(String text) {
		this.contents = text;
	}

	public String getConsoleConfiguration() {
		return cc;
	}

	public void setConsoleConfiguration(String cc) {
		this.cc = cc;
	}

	public String getAnalyzerChosen() {
		return analyzerChosen;
	}

	public void setAnalyzerChosen(String analyzerChosen) {
		this.analyzerChosen = analyzerChosen;
	}
}
