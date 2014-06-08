package gui.popup;

import gui.helper.EffectHelper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import network.NetworkEventType;
import network.NetworkFacade;
import network.NetworkListener;
import settings.GlobalAppSettings;
import settings.Translator;

public class LoginPopup extends CustomPopup implements NetworkListener{

	//pre connection
	private Text title;
	private ProgressIndicator progressIndicator;
	private Button offlineButton;
	private Timeline connectionTimer;
	
	//post connection
	private Text usernameText;
	private Text passwordText;
	private TextField usernameField;
	private PasswordField passwordField;
	private Button loginButton;
	private Button signUpButton;
	private Button forgotPasswordButton;
	
	//failed connection
	private Button retryButton;
	private LoginPopup instance;
	
	public LoginPopup() {
		instance = this;
		initPreConnectionGui();
		//initPostConnectionGui();
	}

	private void initPreConnectionEvents() {
		NetworkFacade.instance.establishInitialConnection(this);
		
		connectionTimer = new Timeline(new KeyFrame(Duration.millis(GlobalAppSettings.defaultServerTimeout), new EventHandler<ActionEvent>(){
			@Override
		    public void handle(ActionEvent event) {
				if(NetworkFacade.instance.isConnectedToServer()) {
					initPostConnectionGui();
				}
				else{
					initConnectionFailedGui();
				}
		    }
		}));
		connectionTimer.setCycleCount(1);
		connectionTimer.play();
	}

	private void initPostConnectionEvents() {
		setupOfflineButton();
		connectionTimer.stop();
		
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				NetworkFacade.instance.tryLogin(usernameField.getText(), passwordField.getText(), instance);
			}
		});
		
		signUpButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				
			}
		});
		
		forgotPasswordButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				
			}
		});
	}
	
	private void setupOfflineButton() {
		offlineButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				WelcomePopup welcomePopup = new WelcomePopup();
				welcomePopup.show();
			}
		});
	}

	private void initConnectionFailedEvents() {
		connectionTimer.stop();
		retryButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				initPreConnectionGui();
			}
		});
		setupOfflineButton();
	}
	
	private void initPreConnectionGui() {
		this.getChildren().clear();
		
		GridPane gridPane = new GridPane();
		
		gridPane.setHgap(GlobalAppSettings.gridHGap);
		gridPane.setVgap(GlobalAppSettings.gridVGap);
		
		title = new Text(Translator.get("#Wait Connection Text#"));
		progressIndicator = new ProgressIndicator();
		
		gridPane.add(title, 0, 0);
		gridPane.add(progressIndicator, 0, 1);
		
		this.getChildren().add(gridPane);
		
		initPreConnectionEvents();
	}
	
	private void initConnectionFailedGui() {
		this.getChildren().clear();
		
		GridPane gridPane = new GridPane();
	
		gridPane.setHgap(GlobalAppSettings.gridHGap);
		gridPane.setVgap(GlobalAppSettings.gridVGap);
		
		title = new Text(Translator.get("#Connection Failed Text#"));
		retryButton = new Button(Translator.get("Retry"));
		offlineButton = new Button(Translator.get("Work Offline"));
		
		GridPane.setHalignment(title, HPos.CENTER);
		GridPane.setHalignment(retryButton, HPos.CENTER);
		GridPane.setHalignment(offlineButton, HPos.CENTER);
		gridPane.add(title, 0, 0);
		gridPane.add(retryButton, 0, 1);
		gridPane.add(offlineButton, 0, 2);
		
		this.getChildren().add(gridPane);
		
		initConnectionFailedEvents();
	}

	private void initPostConnectionGui() {
		this.getChildren().clear();
		
		GridPane pane = new GridPane();
		pane.setHgap(GlobalAppSettings.gridHGap);
		pane.setVgap(GlobalAppSettings.gridVGap);
		
		title = new Text(Translator.get("#Welcome Text#"));
		usernameText = new Text(Translator.get("Username"));
		passwordText = new Text(Translator.get("Password"));
		usernameField = new TextField();
		passwordField = new PasswordField();
		loginButton = new Button(Translator.get("Login"));
		signUpButton = new Button(Translator.get("Sign Up"));
		offlineButton = new Button(Translator.get("Work Offline"));
		forgotPasswordButton = new Button(Translator.get("Forgot Password"));
		
		pane.add(title, 0, 0);
		pane.add(usernameText, 0, 1);
		pane.add(usernameField, 0, 2);
		pane.add(passwordText, 0, 3);
		pane.add(passwordField, 0, 4);
		pane.add(signUpButton, 0, 5);
		pane.add(loginButton, 1, 5);
		pane.add(offlineButton, 0, 6);
		pane.add(forgotPasswordButton, 1, 6);
		
		EffectHelper.setAsATitle(usernameText, 18);
		EffectHelper.setAsATitle(passwordText, 18);
		
		loginButton.setDefaultButton(true);
		this.getChildren().add(pane);
		
		initPostConnectionEvents();
	}

	@Override
	public boolean isDisposedWhenClickedOutside() {
		return false;
	}

	@Override
	public void notifyWithNetworkEvent(NetworkEventType type) {
		System.out.println("NOTIFIED");
		if(type == NetworkEventType.EstablishConnection){
			initPostConnectionGui();
		}
	}

}
