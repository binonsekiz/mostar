package gui;

import gui.GFrame.WindowType;

public interface ScreenType {
	public WindowType getType();
	public void setReferrer(WindowType referrer);
}
