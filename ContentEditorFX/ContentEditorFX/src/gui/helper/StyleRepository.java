package gui.helper;

import document.TextStyle;

public class StyleRepository {
	public static StyleRepository instance;
	private static TextStyle defaultStyle;
	
	public StyleRepository(){
		instance = this;
		defaultStyle = new TextStyle();
	}

	public TextStyle getDefaultStyle() {
		return defaultStyle;
	}
}
