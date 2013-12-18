package gui.docmodify;

import settings.Translator;
import zzzzdeprecated.DocSelectionModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import event.DocModifyScreenGuiFacade;

public class DocOverview extends StackPane{
	
	@SuppressWarnings("rawtypes")
	private TreeView treeView;
	
	private TreeItem treeRoot;
	private int activeSection;

	private DocModifyScreenGuiFacade guiFacade;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DocOverview(){
		treeView = new TreeView<>();
		treeRoot = new TreeItem(Translator.get("New Book"));
		treeRoot.setExpanded(true);
	/*	treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem> arg0,
					TreeItem arg1, TreeItem arg2) {
				if(treeView.getSelectionModel().getSelectedIndex() > 0){
					activeSection = treeView.getSelectionModel().getSelectedIndex() - 1;
					DocSelectionModel.instance.sectionSelectionFromOverview(activeSection);
					System.out.println("Tree selection changed to :" + (treeView.getSelectionModel().getSelectedIndex() - 1));
				}
			}
		});*/
		this.setId("doc-overview");
	}

	@SuppressWarnings("unchecked")
	public void populateTreeView(){
	/*	this.getChildren().remove(treeView);
		
		int columnCounter = 0;
		
		treeRoot.getChildren().clear();
		for(int i = 0; i < guiFacade.getDocument().getChapterCount(); i++){
			@SuppressWarnings("rawtypes")
			TreeItem chapterItem = new TreeItem<String>(guiFacade.getDocument().getChapter(i).getName());
			chapterItem.setExpanded(true);
			treeRoot.getChildren().add(chapterItem);
			
			for(int j = 0; j < guiFacade.getDocument().getChapter(i).getSectionCount(); j++){
				@SuppressWarnings("rawtypes")
				TreeItem sectionItem = new TreeItem<String>(guiFacade.getDocument().getChapter(i).getSection(j).getName());
				sectionItem.setExpanded(true);
				chapterItem.getChildren().add(sectionItem);
				
				for(int k=0; k < guiFacade.getDocument().getChapter(i).getSection(j).getColumnCount(); k++){
					TreeItem tempItem;
					//take a snapshot of the columnviewpane
					ImageView snapshot = new ImageView(guiFacade.takeSnapshot(columnCounter));
					snapshot.setPreserveRatio(true);
					snapshot.setFitHeight(90);
					tempItem = new TreeItem(snapshot);
					columnCounter ++;
					sectionItem.getChildren().add(tempItem);
				}
				
			}
		}
		treeView.setRoot(treeRoot);
		
		this.getChildren().add(treeView);*/
	}
	
	/**
	 * Refreshes the view for the document attached.
	 * Should only be called by GuiFacade.
	 */
	public void refresh(){
		populateTreeView();
	}
	
	public int getActivePage(){
		return activeSection;
	}

	public void setGuiFacade(DocModifyScreenGuiFacade docModifyScreenGuiFacade) {
		this.guiFacade = docModifyScreenGuiFacade;
	}

}
