package gui.docmodify;

import gui.columnview.DocumentView.ScrollMode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import document.project.ProjectRepository;

public class DocBottomToolbar extends ToolBar{
	private Button zoomIncreaseButton;
	private TextField zoomField;
	private Button zoomDecreaseButton;
	
	private Button continuousScroll;
	private Button discreteScroll;
	
	private ToggleButton leftDebugPanelToggle;
	private ToggleButton rightDebugPanelToggle;
	
	private double zoomFactor;
	private boolean isZoomChanged;
	
	public DocBottomToolbar(){
		this.setId("docwidget-toolbar");
		initializeGui();
		initializeEvents();
	}

	private void initializeEvents() {
		zoomIncreaseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				zoomFactor += 5;
				zoomFactor = Math.floor(zoomFactor * 100) / 100;
				zoomField.setText(zoomFactor + "");
				ProjectRepository.getActiveProjectEnvironment().documentPaneZoomChanged(zoomFactor);
			}
		});
		
		zoomDecreaseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				zoomFactor = Math.max(zoomFactor - 5, 0.01);
				zoomFactor = Math.floor(zoomFactor * 100) / 100;
				zoomField.setText(zoomFactor + "");
				ProjectRepository.getActiveProjectEnvironment().documentPaneZoomChanged(zoomFactor);
			}
		});
		
		leftDebugPanelToggle.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ProjectRepository.getActiveProjectEnvironment().toggleDebugPaneVisible(leftDebugPanelToggle.isSelected());
			}
		});
		
		rightDebugPanelToggle.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ProjectRepository.getActiveProjectEnvironment().toggleVersatilePaneVisible(rightDebugPanelToggle.isSelected());
			}
		});
		
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
					ProjectRepository.getActiveProjectEnvironment().documentPaneZoomChanged(zoomFactor);
					isZoomChanged = false;
				}
			}
		});
	
		continuousScroll.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ProjectRepository.getActiveProjectEnvironment().setDocumentViewScrollBehaviour(ScrollMode.Continuous);
			}
		});

		discreteScroll.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ProjectRepository.getActiveProjectEnvironment().setDocumentViewScrollBehaviour(ScrollMode.Discrete);
			}
		});
	}

	private void initializeGui() {
		HBox hbox1 = new HBox();
		zoomIncreaseButton = new Button("+");
		zoomDecreaseButton = new Button("-");
		
		HBox hbox3 = new HBox();
		leftDebugPanelToggle = new ToggleButton("Debug Pane");
		rightDebugPanelToggle = new ToggleButton("Versatile Pane");
		hbox3.getChildren().addAll(leftDebugPanelToggle, rightDebugPanelToggle);
		leftDebugPanelToggle.setTranslateX(50);
		rightDebugPanelToggle.setTranslateX(50);
		
		//using a double value prevents dot and comma misuse.
		// In English 100.0 = 100,0 in Turkish.
		double value = 100.0;
		zoomFactor = 100;
		zoomField = new TextField(value + "");
		zoomField.setPrefWidth(50);
		
		zoomDecreaseButton.setTranslateX(20);
		zoomField.setTranslateX(20);
		zoomIncreaseButton.setTranslateX(20);
		
		hbox1.getChildren().addAll(zoomDecreaseButton, zoomField, zoomIncreaseButton);
		
		HBox hbox2 = new HBox();
		hbox2.setTranslateX(50);
		continuousScroll = new Button("Continuous");
		discreteScroll = new Button("Discrete");
		hbox2.getChildren().addAll(continuousScroll, discreteScroll);

		this.getItems().addAll(hbox1, hbox2, hbox3);
	}
	
}
