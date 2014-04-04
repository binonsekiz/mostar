package gui.popup;

import gui.GFrame;
import javafx.scene.layout.StackPane;

public abstract class CustomPopup extends StackPane{
	
	public void show() {
		GFrame.instance.showPopup(this);
	}
	
	public void hide() {
		GFrame.instance.hidePopup();
	}

	public abstract boolean isDisposedWhenClickedOutside();
	
}
