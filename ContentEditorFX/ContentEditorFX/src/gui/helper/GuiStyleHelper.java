package gui.helper;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class GuiStyleHelper {

	public static void setBackgroundColor(Node node, Color color){
		modifyColorKeyword(node, "-fx-background-color", color);
	}
	
	public static void setBorderColor(Node node, Color color){
		modifyColorKeyword(node, "-fx-border-color", color);
	}
	
	public static void setBorderWidth(Node node, int width) {
		modifyKeyword(node, "-fx-border-width", width + "");
	}
	
	public static void setCursor(Node node, String cursor){
		modifyKeyword(node, "-fx-cursor", cursor);
	}
	
	public static void setBackgroundInsets(Node node, String value){
		modifyKeyword(node, "-fx-background-insets", value);
	}
	
	private static void modifyColorKeyword(Node node, String keyword, Color color) {
		String colorValue = color.toString();
		//trim out the "0x" part, replace it with # 
		colorValue = "#" + colorValue.substring(2);
		modifyKeyword(node, keyword, colorValue);
	}
	
	private static void modifyKeyword(Node node, String keyword, String newValue) {
		String style = node.getStyle();
		int firstIndex = style.indexOf(keyword);
		
		if(firstIndex < 0) {
			//the keyword isnt present, add it.
			style = style + keyword + ":" + newValue + ";";
		}
		else{
			int secondIndex = style.indexOf(";", firstIndex);
			int betweenIndex = style.indexOf(":", firstIndex);
			
			if(secondIndex < 0) secondIndex = style.length();
			
			style = style.substring(0, betweenIndex + 1) + newValue + style.substring(secondIndex);
		}
		node.setStyle(style);
	}
	
}
