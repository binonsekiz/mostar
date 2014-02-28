package document.style;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javafx.scene.paint.Color;
import settings.Translator;

public class TextStyleRepository {
	
	private static HashMap<String, TextStyle> styles = new HashMap<String, TextStyle>();
	private static ArrayList<TextStyle> stylesList = new ArrayList<TextStyle>();;
	
	public static TextStyle getDefaultStyle(){
		return TextStyle.defaultStyle;
	}
	
	public static TextStyle getTextStyle(String fontName, double fontSize, Color strokeColor, Color fillColor, float lineSpacingHeight) {
		String hash = buildHash(fontName, fontSize, strokeColor, fillColor, lineSpacingHeight);
		if(styles.containsKey(hash)) {
			return styles.get(hash);
		}
		TextStyle style = new TextStyle(fontName, fontSize, strokeColor, fillColor, lineSpacingHeight);
		styles.put(hash, style);
		stylesList.add(style);
		return style;
	}
	
	public static ArrayList<TextStyle> getAlphabeticallySortedStyleList() {
		Collections.sort(stylesList);
		return stylesList;
	}
	
	protected static String buildHash(TextStyle style) {
		return buildHash(style.getFontName(), style.getFontSize(), style.getStrokeColor(), style.getFillColor(), style.getLineSpacingHeight());
	}
	
	protected static String buildHash(String fontName, double fontSize, Color strokeColor, Color fillColor, float lineSpacingHeight) {
		String retVal;
		retVal = fontName + "#" + fontSize + "#" + lineSpacingHeight + "#" + strokeColor + "#" + fillColor;
		return retVal;
	}
	
	protected static String buildStyleName(TextStyle style){
		return buildStyleName(style.getFontName(), style.getFontSize());
	}
	
	protected static String buildStyleName(String fontName, double fontSize){
		return fontName + ", " + fontSize + Translator.get("pt");
	}
	
}
