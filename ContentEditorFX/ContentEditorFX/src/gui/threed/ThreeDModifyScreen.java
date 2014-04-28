package gui.threed;

import gui.GFrame.WindowType;
import gui.ScreenType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import settings.Translator;
import control.ThreeDEventFacade;
import document.project.ProjectEnvironment;

public class ThreeDModifyScreen extends BorderPane implements ScreenType{

	private WindowType referrer;
	private ThreeDEventFacade facade;
	
	private Pane leftPane;
	private Button boxButton;
	
	private FlowPane topPane;
	private Button moveButton;
	private Button rotateButton;
	private Button scaleButton;
	
	private ThreeDModifyPane threeDModifyPane;

	public ThreeDModifyScreen() {
		initGui();
		initEvents();
	}
	
	private void initEvents() {
		boxButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				facade.addBoxPressed();
			}
		});
		
		rotateButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				facade.rotatePressed();
			}
		});
		
		moveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				facade.movePressed();
			}
		});
		
		scaleButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				facade.scalePressed();
			}
		});
	}

	private void initGui() {
		initLeftPane();
		initTopPane();
		threeDModifyPane = new ThreeDModifyPane();	
		
		this.setLeft(leftPane);
		this.setTop(topPane);
		this.setCenter(threeDModifyPane);
	}

	private void initTopPane() {
		topPane = new FlowPane();
		topPane.setPadding(new Insets(3));
		HBox hbox = new HBox();
		moveButton = new Button(Translator.get("Move"));
		rotateButton = new Button(Translator.get("Rotate"));
		scaleButton = new Button(Translator.get("Scale"));
		
		hbox.getChildren().addAll(moveButton, rotateButton, scaleButton);
		topPane.setId("docwidget-toolbar");
		topPane.getStyleClass().add("segmented-button-bar-class");
		
		topPane.getChildren().addAll(hbox);
	}

	private void initLeftPane() {
		leftPane = new Pane();
		leftPane.setPadding(new Insets(3));
		VBox vbox = new VBox();
		
		boxButton = new Button(Translator.get("Box"));
		
		vbox.getChildren().addAll(boxButton);
		
		leftPane.setId("docwidget-toolbar");
		leftPane.getStyleClass().add("segmented-button-bar-class");
		leftPane.getChildren().addAll(vbox);
	}

	@Override
	public WindowType getType() {
		return WindowType.ThreeDModifyScreen;
	}

	@Override
	public void setReferrer(WindowType referrer) {
		this.referrer = referrer;
	}

}
