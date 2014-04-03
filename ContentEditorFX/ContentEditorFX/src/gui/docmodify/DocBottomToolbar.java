package gui.docmodify;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import event.DocModifyScreenGuiFacade;

public class DocBottomToolbar extends ToolBar{
	private Button zoomIncreaseButton;
	private TextField zoomField;
	private Button zoomDecreaseButton;
		
	private double zoomFactor;
	private boolean isZoomChanged;
	
	private DocModifyScreenGuiFacade guiFacade;
	
	public DocBottomToolbar(){
		this.setId("docwidget-toolbar");
		initializeGui();
		initializeEvents();
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

	private void initializeGui() {
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

		this.getItems().addAll(zoomDecreaseButton, zoomField, zoomIncreaseButton);
	}
	
	public void setGuiFacade(DocModifyScreenGuiFacade guiFacade){
		this.guiFacade = guiFacade;
	}
	
}
