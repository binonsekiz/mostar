package gui;

import document.project.ProjectEnvironment;
import gui.GFrame.WindowType;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class TemplateModifyScreen extends Pane implements ScreenType{

	private WindowType referrer;

	public TemplateModifyScreen() {
		initGui();
		initEvents();
	}

	private void initGui() {
		Text dummyText = new Text("Template Page");
		
		dummyText.setLayoutX(10);
		dummyText.setLayoutY(10);
		
		this.getChildren().add(dummyText);
	}

	private void initEvents() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WindowType getType() {
		return WindowType.TemplateModifyScreen;
	}

	@Override
	public void setReferrer(WindowType referrer) {
		this.referrer = referrer;
	}

}
