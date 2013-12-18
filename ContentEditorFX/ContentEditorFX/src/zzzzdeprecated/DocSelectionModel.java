package zzzzdeprecated;

import document.Document;
import event.DocModifyScreenGuiFacade;

@Deprecated
public class DocSelectionModel {

	public static DocSelectionModel instance;
	private DocModifyScreenGuiFacade guiFacade;
	private Document document;
	
	public DocSelectionModel(Document document){
		instance = this;
		this.document = document;
//		guiFacade = DocModifyScreenGuiFacade.instance;
	}
	
}
