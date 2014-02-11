package gui.docmodify;

import settings.GlobalAppSettings;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import document.Document;
import event.DocModifyScreenGuiFacade;

public class DocDebugView extends VBox {

	public static DocDebugView instance;
	
	private DocModifyScreenGuiFacade guiFacade;
	
	private Text debugLabel1;
	private Text debugLabel2;
	private Text debugLabel3;
	private Text debugLabel4;
	private Text title;
	
	private CheckBox overlayVisible;
	private CheckBox textCanvasVisible;
	private CheckBox linePolygonsVisible;
	private CheckBox insetVisible;
	private Button refresh;
	
	private TextArea totalDocument;
	private TextArea memoryStats;
	
	private Text totalRefreshCount;
	private SimpleIntegerProperty refreshCount;
	private Timeline memoryCounter;
	private Runtime rt;
	
	public DocDebugView() {
		instance = this;
		rt = Runtime.getRuntime();
		this.setMinWidth(200);
		this.setPrefWidth(200);
		this.setMaxWidth(200);
		
		initGui();
		initEvents();
	}

	private void initEvents() {
		overlayVisible.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				guiFacade.setOverlayCanvasVisible(overlayVisible.selectedProperty().get());
			}
		});
		
		textCanvasVisible.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				guiFacade.setTextCanvasVisible(textCanvasVisible.selectedProperty().get());
			}
		});
		
		refresh.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				guiFacade.requestDocumentViewRefresh();
			}
		});
		
		linePolygonsVisible.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				guiFacade.setLinePolygonsVisible(linePolygonsVisible.selectedProperty().get());
			}
		});
		
		insetVisible.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				guiFacade.setInsetVisible(insetVisible.selectedProperty().get());
			}
		});
		
		refreshCount.addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				totalRefreshCount.setText("Total refresh count: " + refreshCount.get());
			}
		});
		
		memoryCounter = new Timeline(new KeyFrame(Duration.millis(GlobalAppSettings.memoryStatUpdateRate), new EventHandler<ActionEvent>(){
			@Override
		    public void handle(ActionEvent event) {
				 memoryStats.setText(
						 "Memory summary: \nUsed in VM: " + ((rt.totalMemory() - rt.freeMemory())/1000000) +
						 " MB\nFree in VM: " + (rt.freeMemory()/1000000) + 
						 " MB\nTotal in VM: " + (rt.totalMemory()/1000000) + 
						 " MB");
		    }
		}));
		memoryCounter.setCycleCount(-1);
		memoryCounter.play();
	}
	
	public SimpleIntegerProperty refreshCountProperty(){
		return refreshCount;
	}

	private void initGui() {
		title = new Text("Debug menu:\n");
		refreshCount = new SimpleIntegerProperty(0);
		overlayVisible = new CheckBox("Overlay Visible");
		textCanvasVisible = new CheckBox("Text Canvas Visible");
		linePolygonsVisible = new CheckBox("Line Polygons Visible");
		insetVisible = new CheckBox("Page Insets Visible");
		refresh = new Button("Refresh");
		debugLabel1 = new Text("Debug label1");
		debugLabel2 = new Text("Debug label2");
		debugLabel3 = new Text("Debug label3");
		debugLabel4 = new Text("Debug label4");
		totalRefreshCount = new Text("Total refresh count: " + refreshCount.get());
		totalDocument = new TextArea();
		memoryStats = new TextArea();
		totalDocument.setWrapText(true);
		memoryStats.setWrapText(true);
		
		overlayVisible.selectedProperty().set(true);
		textCanvasVisible.selectedProperty().set(true);
		linePolygonsVisible.selectedProperty().set(true);
		insetVisible.selectedProperty().set(true);
		
		this.getChildren().addAll(title, overlayVisible, textCanvasVisible, linePolygonsVisible, 
				insetVisible, refresh, totalRefreshCount, debugLabel1, debugLabel2, debugLabel3, debugLabel4, totalDocument, memoryStats);
	}

	public void setGuiFacade(DocModifyScreenGuiFacade docModifyScreenGuiFacade) {
		this.guiFacade = docModifyScreenGuiFacade;
	}
	
	public void setDebugText(String text, int index){
		switch (index) {
			case 1: debugLabel1.setText(text); break;
			case 2: debugLabel2.setText(text); break;
			case 3: debugLabel3.setText(text); break;
			case 4: debugLabel4.setText(text); break;
			default: break;
		}
	}
	
	public void appendDebugText(String text){
		debugLabel1.setText(debugLabel1.getText() + text);
	}
	
	public void debugRefreshTotalDocument(Document document) {
		totalDocument.setText(document.getDocumentText().exportString());
	}

	public void putText(String string) {
		Text text = new Text(string);
		this.getChildren().add(text);
	}
	
}
