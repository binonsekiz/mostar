package gui.docmodify;

import settings.Translator;
import zzzzdeprecated.DocSelectionModel;
import event.DocModifyScreenGuiFacade;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class DocBottomToolbar extends ToolBar{
	
	private Button prevColumnButton;
	private Button nextColumnButton;
	private Text currentPageText;
	private int activePage;	//this is the actual page number, starting from 0.
	
	private Button prevChapterButton;
	private Button nextChapterButton;
	private Text currentChapterText;
	private int activeChapter;
	
	private Button prevSectionButton;
	private Button nextSectionButton;
	private Text currentSectionText;
	private int activeSection;
	
	private Button zoomIncreaseButton;
	private TextField zoomField;
	private Button zoomDecreaseButton;
	
	private double zoomFactor;
	private boolean isZoomChanged;
	
	private boolean isDebugLabelVisible;
	private Label debugLabel;
	
	private DocModifyScreenGuiFacade guiFacade;
	
	public DocBottomToolbar(){
		isDebugLabelVisible = true;
		initializeGui();
		initializeEvents();
		activePage = 1;
		activeSection = 1;
		activeChapter = 1;
	}

	private void initializeEvents() {
		zoomIncreaseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				zoomFactor += 10;
				zoomFactor = Math.floor(zoomFactor * 100) / 100;
				zoomField.setText(zoomFactor + "");
				guiFacade.documentPaneZoomChanged(zoomFactor);
			}
		});
		
		zoomDecreaseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				zoomFactor = Math.max(zoomFactor - 10, 0.01);
				zoomFactor = Math.floor(zoomFactor * 100) / 100;
				zoomField.setText(zoomFactor + "");
				guiFacade.documentPaneZoomChanged(zoomFactor);
			}
		});
	}
	
	public void updateLabels(){

	}

	private void initializeGui() {
		HBox pageBox = new HBox();
		HBox chapterBox = new HBox();
		HBox sectionBox = new HBox();
		
		prevColumnButton = new Button("<");
		nextColumnButton = new Button(">");
		currentPageText = new Text(" " + Translator.get("Page") + " 1 ");
		
		prevChapterButton = new Button("<");
		nextChapterButton = new Button(">");
		currentChapterText = new Text(" " + Translator.get("Chapter") + " 1 ");
		
		prevSectionButton = new Button("<");
		nextSectionButton = new Button(">");
		currentSectionText = new Text(" " + Translator.get("Section") + " 1 ");
		
		zoomIncreaseButton = new Button("+");
		zoomDecreaseButton = new Button("-");
		
		//using a double value prevents dot and comma misuse.
		// In English 100.0 = 100,0 in Turkish.
		double value = 100.0;
		zoomFactor = 100;
		zoomField = new TextField(value + "");
		zoomField.setPrefWidth(50);
		zoomField.textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String oldValue, String newValue) {
				double val;
				if(newValue.endsWith("."))
					return;
				try{
					val = Double.parseDouble(newValue);
				}
				catch(Exception e){
					zoomField.getStyleClass().removeAll("textfield-error" , "textfield-warning");
					zoomField.getStyleClass().add("textfield-warning");
					return;
				}
				//this prevents race conditions
				if(val <= 0.01) val = 0.01;
				val = Math.floor(val * 100) / 100;
				zoomField.setText(val + "");
				zoomField.getStyleClass().removeAll("textfield-error" , "textfield-warning");
				
				//launch zoom event
				if(val != zoomFactor){	
					zoomFactor = Math.floor(zoomFactor * 100) / 100;
					isZoomChanged = true;
					zoomFactor = val;
				}
			}
		});
		
		zoomField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(isZoomChanged){
					guiFacade.documentPaneZoomChanged(zoomFactor);
					isZoomChanged = false;
				}
			}
		});
		
		zoomDecreaseButton.setTranslateX(20);
		zoomField.setTranslateX(20);
		zoomIncreaseButton.setTranslateX(20);
		
		pageBox.getChildren().addAll(prevColumnButton,currentPageText,nextColumnButton);
		sectionBox.getChildren().addAll(prevSectionButton, currentSectionText, nextSectionButton);
		chapterBox.getChildren().addAll(prevChapterButton, currentChapterText, nextChapterButton);
		
		if(isDebugLabelVisible){
			debugLabel = new Label();
			this.getItems().add(debugLabel);
		}
		
		this.getItems().addAll(chapterBox, sectionBox, pageBox, zoomDecreaseButton, zoomField, zoomIncreaseButton);
	}
	
	public void setGuiFacade(DocModifyScreenGuiFacade guiFacade){
		this.guiFacade = guiFacade;
	}

	public int getActivePage() {
		return activePage;
	}

	public void changePageSelection(int selectionIndex) {
		this.activePage = selectionIndex + 1;
		currentPageText.setText(activePage + "");
	}

	public int getActiveChapter() {
		return 0;
	}
	
	public void setDebugString(String text){
		if(isDebugLabelVisible){
			debugLabel.setText(text);
		}
	}
	
	public enum DocNavigationButtons{
		NextColumn,
		PreviousColumn,
		NextChapter,
		PreviousChapter,
		NextSection,
		PreviousSection
	}
	
}
