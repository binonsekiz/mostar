package gui.login;

import gui.GFrame;
import gui.GFrame.WindowType;
import gui.ScreenType;
import gui.helper.EffectHelper;
import gui.helper.FontHelper;

import java.util.Iterator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import settings.Translator;

public class SignupScreen extends BorderPane implements ScreenType{

	private TextField usernameField;
	private TextField emailField;
	private PasswordField passField;
	private PasswordField pass2Field;
	
	private Text usernameText;
	private Text emailText;
	private Text passText;
	private Text pass2Text;
	
	private Button signupButton;
	private Button backButton;
	
	private boolean isUsernameAcceptable = false;
	private boolean isEmailAcceptable = false;
	private boolean isPassAcceptable = false;
	private boolean isPass2Acceptable = false;
	private WindowType referrer;
	
	public SignupScreen(){
		initializeGui();
		initializeEvents();
	}
	
	private void initializeEvents() {
		signupButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Iterator<String> i = passField.getStyleClass().iterator();
				
				while(i.hasNext()){
					System.out.println(i.next());
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
		
		emailField.textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> arg0,	String arg1, String value) {
				if(value.contains("@") && value.contains(".")){
					emailField.getStyleClass().removeAll("textfield-error" , "textfield-warning");
					isEmailAcceptable = true;
				}
				else{
					emailField.getStyleClass().removeAll("textfield-error" , "textfield-warning");
					emailField.getStyleClass().add("textfield-warning");
					isEmailAcceptable = false;
				}
				checkInputsAcceptable();
			}
		});
		
		passField.textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> arg0,	String arg1, String value) {
				if(value.length() < 6){
					passField.getStyleClass().removeAll("textfield-error" , "textfield-warning");
					passField.getStyleClass().add("textfield-warning");
					isPassAcceptable = false;
				}
				else{
					passField.getStyleClass().removeAll("textfield-error" , "textfield-warning");
					isPassAcceptable = true;
				}
				
				if(pass2Field.getText().equals(value)){
					pass2Field.getStyleClass().removeAll("textfield-error" , "textfield-warning");
					isPass2Acceptable = true;
				}
				else if(pass2Field.getText().length() > 0){
					pass2Field.getStyleClass().removeAll("textfield-error" , "textfield-warning");
					pass2Field.getStyleClass().add("textfield-warning");
					isPass2Acceptable = false;
				}
				checkInputsAcceptable();
			}
		});
		
		pass2Field.textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> arg0,	String arg1, String value) {
				if(value.equals(passField.getText())){
					pass2Field.getStyleClass().removeAll("textfield-error" , "textfield-warning");
					isPass2Acceptable = true;
				}
				else{
					pass2Field.getStyleClass().removeAll("textfield-error" , "textfield-warning");
					pass2Field.getStyleClass().add("textfield-warning");
					isPass2Acceptable = false;
				}
				checkInputsAcceptable();
			}
		});
	}

	protected void checkInputsAcceptable() {
		if(isUsernameAcceptable && isEmailAcceptable && isPassAcceptable && isPass2Acceptable){
			signupButton.setDefaultButton(true);
		}
		else{
			signupButton.setDefaultButton(false);
		}
	}

	private void initializeGui() {
		usernameField = new TextField();
		emailField = new TextField();
		passField = new PasswordField();
		pass2Field = new PasswordField();
		
		usernameText = new Text(Translator.get("username"));
		emailText = new Text(Translator.get("email"));
		passText = new Text(Translator.get("password"));
		pass2Text = new Text(Translator.get("confirm"));
		
		signupButton = new Button(Translator.get("sign up"));
		Reflection ref = new Reflection();
		ref.setTopOffset(15);
		signupButton.setEffect(ref);
		
		backButton = new Button(Translator.get("back"));
		backButton.setEffect(ref);
		
		GridPane grid = new GridPane();
		
		EffectHelper.setAsATitleNoReflection(usernameText, 40);
		EffectHelper.setAsATitleNoReflection(emailText, 40);
		EffectHelper.setAsATitleNoReflection(passText, 40);
		EffectHelper.setAsATitleNoReflection(pass2Text, 40);
		
		usernameField.setId("textfield-custom");
		emailField.setId("textfield-custom");
		passField.setId("textfield-custom");
		pass2Field.setId("textfield-custom");
//		signupButton.setDefaultButton(true);
		signupButton.setFont(FontHelper.getFont("Vera", 40));
		backButton.setFont(FontHelper.getFont("Vera", 40));
		
		GridPane.setHalignment(usernameField, HPos.LEFT);
		GridPane.setHalignment(emailField, HPos.LEFT);
		GridPane.setHalignment(passField, HPos.LEFT);
		GridPane.setHalignment(pass2Field, HPos.LEFT);
		GridPane.setHalignment(usernameText, HPos.RIGHT);
		GridPane.setHalignment(emailText, HPos.RIGHT);
		GridPane.setHalignment(passText, HPos.RIGHT);
		GridPane.setHalignment(pass2Text, HPos.RIGHT);
		GridPane.setHalignment(signupButton, HPos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setTranslateY(30);
		
		grid.add(usernameText, 0, 0);
		grid.add(usernameField, 1, 0);
		grid.add(emailText, 0, 1);
		grid.add(emailField, 1, 1);
		grid.add(passText, 0, 2);
		grid.add(passField, 1, 2);
		grid.add(pass2Text, 0, 3);
		grid.add(pass2Field, 1, 3);
			
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().add(grid);
		
		HBox hbox2 = new HBox();
		hbox2.setAlignment(Pos.CENTER);
		hbox2.setSpacing(10);
		hbox2.getChildren().addAll(backButton, signupButton);
		hbox2.setTranslateY(40);
		
		grid.add(hbox2, 0, 4, 2, 1);
		this.setCenter(hbox);
		signupButton.requestFocus();
	}

	@Override
	public WindowType getType() {
		// TODO Auto-generated method stub
		return WindowType.SignupScreen;
	}

	@Override
	public void setReferrer(WindowType referrer) {
		this.referrer = referrer;
	}

}
