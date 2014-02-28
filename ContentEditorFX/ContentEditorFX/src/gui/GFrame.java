package gui;

import event.input.KeyboardManager;
import gui.docmodify.DocModifyScreen;
import gui.docmodify.test.TestFacade;
import gui.login.SignupScreen;
import gui.login.TemplateScreen;
import gui.login.UsernameScreen;
import gui.login.WelcomeScreen;
import gui.start.StartScreen;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import settings.GlobalAppSettings;

public class GFrame extends Application {

	private StackPane sceneChangePane;
	private Pane activePane;
	
	private Scene mainScene;
	private Stage mainStage;
	
	//these two arraylists has to be sycned, 
	//i.e. their nth elements should point to the same object
	private ArrayList<Pane> panes;
	private ArrayList<ScreenType> panesAsTypes;
	
	//whenever a new gui screen is added to the project,
	//insert it here to get it fast loaded on startup
	private WelcomeScreen welcomeScreen;
	private UsernameScreen usernameScreen;
	private DocModifyScreen docModifyScreen;
	private SignupScreen signupScreen;
	private StartScreen startScreen;
	private TemplateScreen templateScreen;
	
	private KeyboardManager keyboardManager;
	
	private DividedPane dividedPane;
	
	@Override
	public void start(final Stage mainStage){
		instance = this;
		this.mainStage = mainStage;
	//	testPath();
		mainPath();
	}
	
	private void testPath(){
	
	}
	
	private void mainPath(){
		panes = new ArrayList<Pane>();
		panesAsTypes = new ArrayList<ScreenType>();
		
		keyboardManager = new KeyboardManager();
		
		initializeMainScene();		
		initializePanes();
		
		if(GlobalAppSettings.isTestModeOn())
			TestFacade.startTests();
		
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
		welcomeScreen = new WelcomeScreen();
		usernameScreen = new UsernameScreen();
		docModifyScreen = new DocModifyScreen();
		signupScreen = new SignupScreen();
		startScreen = new StartScreen();
		dividedPane = new DividedPane();
		templateScreen = new TemplateScreen();
		
		panes.add(welcomeScreen); panesAsTypes.add(welcomeScreen);
		panes.add(usernameScreen); panesAsTypes.add(usernameScreen);
		panes.add(docModifyScreen); panesAsTypes.add(docModifyScreen);
		panes.add(signupScreen); panesAsTypes.add(signupScreen);
		panes.add(startScreen); panesAsTypes.add(startScreen);
		panes.add(dividedPane); panesAsTypes.add(dividedPane);
		panes.add(templateScreen); panesAsTypes.add(templateScreen);
		
		for(int i = 0; i < panes.size(); i++){
			panes.get(i).setOpacity(0);
			sceneChangePane.getChildren().add(panes.get(i));
		}
		
	//	activePane = welcomeScreen;
	//	activePane = startScreen;
	//	activePane = dividedPane;
		activePane = docModifyScreen;
		activePane.setOpacity(1);
		activePane.toFront();
	}

	private void initializeMainScene() {
		mainStage.setTitle("Mostar - Sürüm 0.0.2 Demo - 15.08.2013");
		mainStage.setMinHeight(GlobalAppSettings.frameHeight);
		mainStage.setMinWidth(GlobalAppSettings.frameWidth);
		mainStage.setOpacity(0);
		sceneChangePane = new StackPane();
//		sceneChangePane.setId("scenechange-pane");
		sceneChangePane.setMinSize(GlobalAppSettings.frameWidth, GlobalAppSettings.frameHeight);
		mainScene = new Scene(sceneChangePane, GlobalAppSettings.frameWidth, GlobalAppSettings.frameHeight, Color.WHITE);
		mainScene.getStylesheets().add("gui/styles/skin1.css");
		mainStage.setScene(mainScene);
		
	/*	mainScene.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent event) {
				keyboardManager.keyPressed(event);
			}
		});
		
		mainScene.setOnKeyReleased(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent event) {
				keyboardManager.keyReleased(event);
			}
		});
		
		mainScene.setOnKeyTyped(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent event) {
				keyboardManager.keyTyped(event);
			}
		});*/
		
		mainScene.addEventHandler(KeyEvent.KEY_PRESSED, keyboardManager);
		mainScene.addEventHandler(KeyEvent.KEY_RELEASED, keyboardManager);
		mainScene.addEventHandler(KeyEvent.KEY_TYPED, keyboardManager);
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
	}
	
}
