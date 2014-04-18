package storage;

import java.io.File;
import java.io.ObjectOutputStream;

import document.Document;

public class LocalPersistentRepository implements DocumentRepository, TemplateRepository{
	
	private XmlManager xmlManager;
	private ObjectOutputStream oos;
	
	public LocalPersistentRepository() {
		
	}
	
	public void setProjectDirectory() {
		
	}

	public void saveDocument(Document document, File selectedFile) {
		if(xmlManager == null) {
			initXmlManager();
		}
		xmlManager.saveDocument(document, selectedFile);
	}

	private void initXmlManager() {
		xmlManager = new XmlManager();
	}

	public void loadDocument(File selectedFile) {
		if(xmlManager == null) {
			initXmlManager();
		}
		xmlManager.loadDocument(selectedFile);
	}
	
}
