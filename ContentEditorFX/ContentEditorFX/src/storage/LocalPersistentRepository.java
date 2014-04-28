package storage;

import java.io.File;
import java.io.ObjectOutputStream;

import document.Document;
import document.project.ProjectEnvironment;
import document.project.ProjectRepository;

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
		XmlManager.saveDocument(document, selectedFile);
	}

	private void initXmlManager() {
		xmlManager = new XmlManager();
	}

	public void loadDocument(File selectedFile) {
		if(xmlManager == null) {
			initXmlManager();
		}
		ProjectRepository.getActiveProjectEnvironment().associateWithNewDocument(XmlManager.loadDocument(selectedFile));
	}
	
}
