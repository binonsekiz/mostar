package gui;

import event.input.KeyboardManager;
import gui.docmodify.test.TestFacade;
import gui.popup.CustomPopup;
import gui.start.TitleScreen;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import settings.GlobalAppSettings;

public class GFrame extends Application {

	private StackPane root;
	private StackPane dimmer;
	private StackPane popupPane;
	private StackPane sceneChangePane;
	private GaussianBlur dimBlur;
	private Pane activePane;
	private CustomPopup activePopup;
	
	private Scene mainScene;
	private Stage mainStage;
	
	//these two arraylists has to be sycned, 
	//i.e. their nth elements should point to the same object
	private ArrayList<Pane> panes;
	private ArrayList<ScreenType> panesAsTypes;
	
	//whenever a new gui screen is added to the project,
	//insert it here to get it fast loaded on startup
	private DocTabbedView docTabbedView;
	private TitleScreen titleStage;
	
	private KeyboardManager keyboardManager;
		
	@Override
	public void start(final Stage mainStage){
		instance = this;
		this.mainStage = mainStage;
		activePopup = null;
		
		if(true) {
			mainPath();
		}
		else {
			titleStagePath();
		}
	}
	
	private void titleStagePath() {
		titleStage = new TitleScreen(this);
	}

	private void testPath(){
		
	}
	
	public void mainPath(){
		panes = new ArrayList<Pane>();
		panesAsTypes = new ArrayList<ScreenType>();
		
		keyboardManager = new KeyboardManager();
		
		initializeMainScene();		
		initializePanes();
		
		if(GlobalAppSettings.isTestModeOn())
			TestFacade.startTests();
		
		mainStage.centerOnScreen();
		mainStage.show();
	
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long arg0) {
				mainStage.setOpacity(Math.min(mainStage.getOpacity() + 0.02f, 1));
				if(mainStage.getOpacity() == 1)
					stop();
			}
		};
		
		timer.start();	
	}

	private void initializePanes() {
		docTabbedView = new DocTabbedView();
		
		panes.add(docTabbedView); panesAsTypes.add(docTabbedView);
		
		for(int i = 0; i < panes.size(); i++){
			panes.get(i).setOpacity(0);
			sceneChangePane.getChildren().add(panes.get(i));
		}
		
		activePane = docTabbedView;
		activePane.setOpacity(1);
		activePane.toFront();
	}

	private void initializeMainScene() {
		mainStage.setTitle("Portis - Sürüm 0.0.3 - 30.03.2014");
		mainStage.setMinHeight(GlobalAppSettings.minFrameHeight);
		mainStage.setMinWidth(GlobalAppSettings.minFrameWidth);
		mainStage.setOpacity(0);
		
		root = new StackPane();
		dimmer = new StackPane();
		
		sceneChangePane = new StackPane();
		sceneChangePane.setMinSize(GlobalAppSettings.minFrameWidth, GlobalAppSettings.minFrameHeight);
		root.setMinSize(GlobalAppSettings.minFrameWidth, GlobalAppSettings.minFrameHeight);
		dimmer.setMinSize(GlobalAppSettings.minFrameWidth, GlobalAppSettings.minFrameHeight);
		
		root.getChildren().addAll(sceneChangePane,dimmer);
		dimmer.toBack();
		dimmer.setVisible(false);
		dimmer.setId("dimmer");
		dimmer.setAlignment(Pos.CENTER);
		dimmer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {
            	if(activePopup != null && activePopup.isDisposedWhenClickedOutside()) {
            		t.consume();
                	hidePopup();
            	}
            }
        });
		dimBlur = new GaussianBlur(0);
		sceneChangePane.setEffect(dimBlur);
		
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		popupPane = new StackPane();
		
		dimmer.getChildren().add(hbox);
		hbox.getChildren().add(vbox);
		vbox.getChildren().add(popupPane);
				
		mainScene = new Scene(root, GlobalAppSettings.frameWidth, GlobalAppSettings.frameHeight, Color.WHITE);
		mainScene.getStylesheets().add("gui/styles/skin1.css");
		mainStage.setScene(mainScene);
		
		mainScene.addEventHandler(KeyEvent.KEY_PRESSED, keyboardManager);
		mainScene.addEventHandler(KeyEvent.KEY_RELEASED, keyboardManager);
		mainScene.addEventHandler(KeyEvent.KEY_TYPED, keyboardManager);
	}
	
	public void showPopup(CustomPopup popup) {
		popupPane.getChildren().clear();
		dimmer.setVisible(true);
		dimmer.toFront();
		dimmer.setOpacity(0);
		activePopup = popup;
		
		popupPane.getChildren().add(popup);
		
		Timeline dimTimeline = new Timeline();
		dimTimeline.getKeyFrames().add(
				new KeyFrame(Duration.millis(GlobalAppSettings.dimmerTime), 
				new KeyValue(dimmer.opacityProperty(), 1, Interpolator.EASE_IN),
				new KeyValue(dimBlur.radiusProperty(), 8, Interpolator.EASE_IN)));
		dimTimeline.play();
	}

	public void hidePopup() {
		activePopup = null;
		Timeline undimTimeline = new Timeline();
		undimTimeline.getKeyFrames().add(
				new KeyFrame(Duration.millis(GlobalAppSettings.dimmerTime), 
				new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						dimmer.setVisible(false);
						popupPane.getChildren().clear();
					}
				},
				new KeyValue(dimmer.opacityProperty(), 0, Interpolator.EASE_IN),
				new KeyValue(dimBlur.radiusProperty(), 0, Interpolator.EASE_IN)));
		
		undimTimeline.play();
	}
	
	public Pane changePane(WindowType windowType, WindowType referringPage){
		//find the new pane
		Pane newPane = null;
		for(int i = 0; i < panesAsTypes.size(); i++){
			if(panesAsTypes.get(i).getType() == windowType){
				newPane = (Pane)panesAsTypes.get(i);
			}
		}
		
		if(newPane == null){
			//TODO: handle dynamic loaded panes
		}
		
		FadeTransition ft1 = new FadeTransition(Duration.millis(500), activePane);
		FadeTransition ft2 = new FadeTransition(Duration.millis(500), newPane);
		ft1.setFromValue(1); ft1.setToValue(0);
		ft2.setFromValue(0); ft2.setToValue(1);
		ft1.play();
		ft2.play();
		activePane = newPane;

		if(activePane instanceof ScreenType && referringPage != null){
			ScreenType activeScreen = (ScreenType)activePane;
			activeScreen.setReferrer(referringPage);
		}
		
		Timeline timer = new Timeline(new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	activePane.toFront();
		    }
		}));
		timer.play();
		
		
		return activePane;
	}
	
	public Stage getMainStage(){
		return mainStage;
	}
	
	public Scene getMainScene(){
		return mainScene;
	}

	public static void main(String[] args){
		GlobalAppSettings.loadAppSettings();
		launch(args);
	}
	
	public double getWidth(){
		return mainStage.getWidth();
	}
	
	public double getHeight(){
		return mainStage.getHeight();
	}
	
	public static GFrame instance;
	
	public enum WindowType{
		DocModifyScreen,
		UsernameScreen,
		WelcomeScreen, 
		SignupScreen, 
		StartScreen,
		DividedPane, 
		TemplateScreen, 
		DocTabbedView, 
		ThreeDModifyScreen, 
		TitleScreen, 
		TemplateModifyScreen,
	}
	
}
