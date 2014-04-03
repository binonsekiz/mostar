
package zzzzdeprecated;

import gui.GFrame;
import gui.GFrame.WindowType;
import gui.ScreenType;
import gui.helper.EffectHelper;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import settings.Translator;

@Deprecated
public class WelcomeScreen extends BorderPane implements ScreenType{
	
	private Text leftText;
	private Text rightText;
	private Text bottomText;
	private WindowType referrer;
	
	public WelcomeScreen(){
		initializeGui();
		initializeEvents();
	}

	private void initializeEvents() {
		EffectHelper.addMouseRolloverColorTransition(leftText);
		leftText.setWrappingWidth(340);
		EffectHelper.addMouseRolloverColorTransition(rightText);
		EffectHelper.addMouseRolloverColorTransition(bottomText);
		
		leftText.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				GFrame.instance.changePane(WindowType.TemplateScreen, WindowType.WelcomeScreen);
			}
		});
		
		rightText.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				GFrame.instance.changePane(WindowType.UsernameScreen, WindowType.WelcomeScreen);
			}
		});
		
		bottomText.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				GFrame.instance.changePane(WindowType.SignupScreen, WindowType.WelcomeScreen);
			}
		});
	}

	private void initializeGui() {
		leftText = new Text(Translator.get("Login as a\nGuest"));
		rightText = new Text(Translator.get("Sign in"));
		bottomText = new Text(Translator.get("Sign up"));
		
		EffectHelper.setAsATitle(leftText,72);
		EffectHelper.setAsATitle(rightText,72);
		EffectHelper.setAsATitle(bottomText, 32);
		
		BorderPane.setAlignment(leftText, Pos.CENTER_RIGHT);
		BorderPane.setAlignment(rightText, Pos.CENTER_LEFT);
		BorderPane.setAlignment(bottomText, Pos.CENTER);
		
		this.setLeft(leftText);
		this.setRight(rightText);
		this.setBottom(bottomText);
		
		bottomText.setTranslateY(-50);
		leftText.setTranslateX(50);
		rightText.setTranslateX(-50);
	}

	@Override
	public WindowType getType() {
		return WindowType.WelcomeScreen;
	}

	@Override
	public void setReferrer(WindowType referrer) {
		this.referrer = referrer;
	}
}
