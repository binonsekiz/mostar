package gui.storage;

import java.io.File;

import event.DocModifyScreenGuiFacade;
import settings.Translator;
import storage.LocalPersistentRepository;
import storage.RepositoryManager;
import javafx.stage.FileChooser;

public class FilePickerWrapper {

	private FileChooser fileChooser;
	private FileChooser.ExtensionFilter extensionFilter;
	private DocModifyScreenGuiFacade guiFacade;
	private File fileChooserDirectory;
	private RepositoryManager repositoryManager;
	
	public FilePickerWrapper(DocModifyScreenGuiFacade guiFacade, RepositoryManager repoManager) {
		this.guiFacade = guiFacade;
		this.repositoryManager = repoManager;
		initGui();
		initEvents();
		String userDirectoryString = System.getProperty("user.home");
		File userDirectory = new File(userDirectoryString);
		if(!userDirectory.canRead()) {
		    userDirectory = new File("c:/");
		}
		fileChooserDirectory = userDirectory;
	}

	private void initGui() {
		fileChooser = new FileChooser();
		extensionFilter = new FileChooser.ExtensionFilter(Translator.get("Portis "), ".prts");
	}

	private void initEvents() {
		
	}
	
	private void setDefaultFolder(File selectedFile) {
		if(selectedFile != null){ 
			if(selectedFile.isDirectory()) {
				fileChooserDirectory = selectedFile;
			}
			else{
				fileChooserDirectory = selectedFile.getParentFile();
			}
		}
	}
	
	public void open(FilePickerType type){
		File selectedFile = null;
		fileChooser.setInitialDirectory(fileChooserDirectory);
				
		if(type == FilePickerType.SaveDocument) {
			fileChooser.setTitle(Translator.get("Save Document"));
			selectedFile = fileChooser.showSaveDialog(null);
			if(selectedFile!= null){
				LocalPersistentRepository localRepo = repositoryManager.getDefaultLocalRepository();
				localRepo.saveDocument(guiFacade.getDocument(), selectedFile);
			}
		}
		else if(type == FilePickerType.OpenDocument) {
			fileChooser.setTitle(Translator.get("Open Document"));
			selectedFile = fileChooser.showOpenDialog(null);
		}
		
		setDefaultFolder(selectedFile);
	}
	
	public enum FilePickerType{
		SaveDocument,
		OpenDocument,
	}
	
	
}
