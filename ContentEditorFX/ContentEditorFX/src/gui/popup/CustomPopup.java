package gui.popup;

import gui.GFrame;
import gui.helper.GuiStyleHelper;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import settings.GlobalAppSettings;

public abstract class CustomPopup extends StackPane{
	
	protected CustomPopup() {
		setPadding(GlobalAppSettings.popupInsets);
		GuiStyleHelper.setBorderColor(this, GlobalAppSettings.accentColor);
		GuiStyleHelper.setBackgroundColor(this, Color.WHITESMOKE);
	}
	
	public void show() {
		GFrame.instance.showPopup(this);
	}
	
	public void hide() {
		GFrame.instance.hidePopup();
	}

	public abstract boolean isDisposedWhenClickedOutside();
	
}
