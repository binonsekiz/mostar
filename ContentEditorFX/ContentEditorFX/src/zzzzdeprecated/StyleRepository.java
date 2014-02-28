package zzzzdeprecated;

import document.style.TextStyle;

@Deprecated
public class StyleRepository {
	public static StyleRepository instance;
	private static TextStyle defaultStyle;
	
	public StyleRepository(){
		instance = this;
	//	defaultStyle = new TextStyle();
	}

	public TextStyle getDefaultStyle() {
		return defaultStyle;
	}
}
