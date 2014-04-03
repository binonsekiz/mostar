package zzzzdeprecated;

import gui.GFrame;
import gui.GFrame.WindowType;
import gui.ScreenType;
import gui.helper.EffectHelper;
import gui.helper.FontHelper;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import settings.Translator;

@Deprecated
public class UsernameScreen extends BorderPane implements ScreenType{

	private TextField usernameField;
	private PasswordField passwordField;
	private Text usernameText;
	private Text passwordText;
	private Button loginButton;
	private Button backButton;
	
	private boolean isUsernameAcceptable;
	private boolean isPasswordAcceptable;
	private WindowType referrer;
	
	public UsernameScreen(){
		initializeGui();
		initializeEvents();
	}

	private void initializeEvents() {
		loginButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				try {
					Socket sock = new Socket("localhost", 1018);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				GFrame.instance.changePane(WindowType.WelcomeScreen, null);
			}
		});
		
		usernameField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String value) {
				if(value.length() < 4){
					usernameField.getStyleClass().removeAll("textfield-error" , "textfield-warning");
					usernameField.getStyleClass().add("textfield-warning");
					isUsernameAcceptable = false;
				}
				else{
					usernameField.getStyleClass().removeAll("textfield-error" , "textfield-warning");
					isUsernameAcceptable = true;
				}
				checkInputsAcceptable();
			}
		});
		
		passwordField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String value) {
				if(value.length() < 6){
					passwordField.getStyleClass().removeAll("textfield-error" , "textfield-warning");
					passwordField.getStyleClass().add("textfield-warning");
					isPasswordAcceptable = false;
				}
				else{
					passwordField.getStyleClass().removeAll("textfield-error" , "textfield-warning");
					isPasswordAcceptable = true;
				}
				checkInputsAcceptable();
			}
		});
	}

	protected void checkInputsAcceptable() {
		if(isUsernameAcceptable && isPasswordAcceptable){
			loginButton.setDefaultButton(true);
		}
		else{
			loginButton.setDefaultButton(false);
		}
	}

	private void initializeGui() {
		usernameField = new TextField();
		passwordField = new PasswordField();
		
		GaussianBlur blur = new GaussianBlur(1);
		usernameField.setEffect(blur);
		passwordField.setEffect(blur);
		
		usernameText = new Text(Translator.get("Username"));
		passwordText = new Text(Translator.get("Password"));
		
		loginButton = new Button(Translator.get("Login"));
		backButton = new Button(Translator.get("back"));
		
		usernameField.setId("textfield-custom");
		passwordField.setId("textfield-custom");
		
		loginButton.setFont(FontHelper.getFont("Vera", 40));
		backButton.setFont(FontHelper.getFont("Vera", 40));
		
		EffectHelper.setAsATitleNoReflection(usernameText, 40);
		EffectHelper.setAsATitleNoReflection(passwordText, 40);
		Reflection ref = new Reflection();
		ref.setTopOffset(15);
		loginButton.setEffect(ref);
		backButton.setEffect(ref);
		
		GridPane grid = new GridPane();
		
		GridPane.setHalignment(usernameField, HPos.LEFT);
		GridPane.setHalignment(passwordField, HPos.LEFT);
		GridPane.setHalignment(usernameText, HPos.RIGHT);
		GridPane.setHalignment(passwordText, HPos.RIGHT);
		GridPane.setHgrow(usernameField, Priority.NEVER);
		GridPane.setHgrow(passwordField, Priority.NEVER);
		GridPane.setHalignment(loginButton, HPos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		
		grid.setTranslateY(100);
		
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().add(grid);
		
		HBox hbox2 = new HBox();
		hbox2.setAlignment(Pos.CENTER);
		hbox2.setSpacing(10);
		hbox2.getChildren().addAll(backButton, loginButton);
		hbox2.setTranslateY(110);
		
		grid.add(usernameText, 0, 0);
		grid.add(usernameField, 1, 0);
		grid.add(passwordText, 0, 1);
		grid.add(passwordField, 1, 1);
		grid.add(hbox2, 0, 2, 2, 1);
		this.setCenter(hbox);
				
		BorderPane.setAlignment(grid, Pos.BASELINE_CENTER);
	}

	@Override
	public WindowType getType() {
		return WindowType.UsernameScreen;
	}

	@Override
	public void setReferrer(WindowType referrer) {
		this.referrer = referrer;
	}
	
}
