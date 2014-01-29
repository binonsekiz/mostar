package gui.docmodify;

import event.DocModifyScreenGuiFacade;
import gui.SecretTextField;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

public class DocDebugView extends FlowPane {

	public static DocDebugView instance;
	
	private DocModifyScreenGuiFacade guiFacade;
	
	private Text title;
	
	private CheckBox overlayVisible;
	private CheckBox textCanvasVisible;
	private CheckBox debugPointsVisible;
	private CheckBox linePolygonsVisible;
	private Button refresh;
	
	private Text totalRefreshCount;
	private SimpleIntegerProperty refreshCount;
	
	public DocDebugView() {
		instance = this;
		this.setMaxWidth(150);
		
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
		
		debugPointsVisible.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				guiFacade.setDebugPointsVisible(debugPointsVisible.selectedProperty().get());
			}
		});
		
		linePolygonsVisible.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				guiFacade.setLinePolygonsVisible(linePolygonsVisible.selectedProperty().get());
			}
		});
		
		refreshCount.addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				totalRefreshCount.setText("Total refresh count: " + refreshCount.get());
			}
		});
	}
	
	public SimpleIntegerProperty refreshCountProperty(){
		return refreshCount;
	}

	private void initGui() {
		title = new Text("Debug menu:\n");
		refreshCount = new SimpleIntegerProperty(0);
		overlayVisible = new CheckBox("Overlay Visible");
		textCanvasVisible = new CheckBox("Text Canvas Visible");
		debugPointsVisible = new CheckBox("Debug Points Visible");
		linePolygonsVisible = new CheckBox("Line Polygons Visible");
		refresh = new Button("Refresh");
		totalRefreshCount = new Text("Total refresh count: " + refreshCount.get());
		
		overlayVisible.selectedProperty().set(true);
		textCanvasVisible.selectedProperty().set(true);
		debugPointsVisible.selectedProperty().set(true);
		linePolygonsVisible.selectedProperty().set(true);
		
		this.getChildren().addAll(title, overlayVisible, textCanvasVisible, debugPointsVisible, linePolygonsVisible, refresh, totalRefreshCount);
	}

	public void setGuiFacade(DocModifyScreenGuiFacade docModifyScreenGuiFacade) {
		this.guiFacade = docModifyScreenGuiFacade;
	}

	public void putText(String string) {
		Text text = new Text(string);
		this.getChildren().add(text);
	}
	
}
