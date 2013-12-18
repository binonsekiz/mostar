package gui.login;

import gui.GFrame;
import gui.GFrame.WindowType;
import gui.ScreenType;
import gui.helper.EffectHelper;
import gui.helper.FontHelper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import settings.Translator;

public class TemplateScreen extends Pane implements ScreenType {

	private Button template1;
	private Button template2;
	private Button emptyTemplate;
	private Button backButton;

	//default referrer
	private WindowType referrer = WindowType.WelcomeScreen;
	
	public TemplateScreen(){
		initGui();
		initEvents();
	}
	
	private void initEvents() {
		template1.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				GFrame.instance.changePane(WindowType.DocModifyScreen, WindowType.TemplateScreen);
			}
		});
		
		template2.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				GFrame.instance.changePane(WindowType.DocModifyScreen, WindowType.TemplateScreen);
			}
		});
		
		emptyTemplate.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				GFrame.instance.changePane(WindowType.DocModifyScreen, WindowType.TemplateScreen);
			}
		});
		
		backButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				GFrame.instance.changePane(referrer, null);
			}
		});
	}

	private void initGui() {
		HBox hbox = new HBox();
		hbox.setTranslateY(200);
		hbox.setTranslateX(100);
		
		template1 = new Button(Translator.get("Template") + " 1");
		template2 = new Button(Translator.get("Template") + " 2");
		emptyTemplate = new Button(Translator.get("Empty Template"));
		backButton = new Button(Translator.get("Back"));
		
		EffectHelper.setAsATemplate(template1, 20);
		EffectHelper.setAsATemplate(template2, 20);
		EffectHelper.setAsATemplate(emptyTemplate, 20);
		
		template1.setTranslateX(-50);
		emptyTemplate.setTranslateX(50);
		
		EffectHelper.setAsABackButton(backButton);
		
		hbox.getChildren().addAll(template1, template2, emptyTemplate);
		backButton.setLayoutX(600);
		backButton.setLayoutY(400);
		
		this.getChildren().addAll(hbox, backButton);
	}

	@Override
	public WindowType getType() {
		return WindowType.TemplateScreen;
	}

	@Override
	public void setReferrer(WindowType referrer) {
		this.referrer = referrer;
	}

}
