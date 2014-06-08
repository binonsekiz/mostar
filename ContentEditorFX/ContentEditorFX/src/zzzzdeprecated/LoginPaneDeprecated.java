package zzzzdeprecated;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import settings.GlobalAppSettings;
import settings.Translator;

public class LoginPaneDeprecated extends GridPane{
	
	public LoginPaneDeprecated(){
		Text title;
		Text usernameLabel;
		Text passwordLabel;
		TextField usernameField;
		PasswordField passwordField;
		Button loginButton;
		
		setAlignment(Pos.TOP_CENTER);
		setHgap(20);
		setVgap(20);
		setPadding(new Insets(25 ,25, 25, 25));
		
		if(GlobalAppSettings.guiDebugGuidelines)
			setGridLinesVisible(true);
		
		title = new Text(Translator.get("Login"));
		title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		add(title, 0,0,2,1);
		
		usernameLabel = new Text(Translator.get("Username") + ":");
		usernameField = new TextField();
		GridPane.setHalignment(usernameLabel, HPos.RIGHT);
		add(usernameLabel, 0, 1);
		add(usernameField, 1, 1);
		
		passwordField = new PasswordField();
		passwordLabel = new Text(Translator.get("Password") + ":");
		GridPane.setHalignment(passwordLabel, HPos.RIGHT);
		add(passwordLabel, 0, 2);
		add(passwordField, 1, 2);
		
		HBox hbox = new HBox();
		loginButton = new Button(Translator.get("Login"));
		hbox.getChildren().add(loginButton);
		hbox.setAlignment(Pos.CENTER);
		add(hbox, 0,3,2,1);
		this.setGridLinesVisible(true);
		
	}
	
}
