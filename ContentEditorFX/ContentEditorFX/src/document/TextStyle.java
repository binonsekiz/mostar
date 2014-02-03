package document;

import gui.helper.FontHelper;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;

public class TextStyle {
	
	public static String defaultFontName = "Vera";
	public static double defaultFontSize = 24;
	public static Color defaultStrokeColor = Color.DARKSLATEGRAY;
	public static final TextStyle defaultStyle = new TextStyle();
	public static float defaultLineSpacingHeight = 3; 
	
	private static Color defaultSelectionColor = new Color(Color.DARKBLUE.getRed(), Color.DARKBLUE.getGreen(), Color.DARKBLUE.getBlue(), 1f);
	
	private String fontName;
	private double fontSize;
	private float lineSpacingHeight;
	private Color strokeColor;
	
	/**
	 * Initializes textstyle from 
	 * @param definition: <font/size/RGBA>
	 */
	public TextStyle(String definition){
		if(definition.startsWith("<") && definition.endsWith(">")){
			definition = definition.substring(1, definition.length() - 1 );
			
			String[] tokens = definition.split("/");
			
			if(tokens.length > 0 && tokens[0].length() > 0){
				fontName = tokens[0];
			}
			if(tokens.length > 1 && tokens[1].length() > 0){
				fontSize = Double.parseDouble(tokens[1]);
			}
			if(tokens.length > 2 && tokens[2].length() > 0){
				strokeColor = FontHelper.getColorByName(tokens[2]);
			}
		}
	}
	
	public TextStyle(String fontName, double fontSize, Color strokeColor){
		this.fontName = fontName;
		this.fontSize = fontSize;
		this.strokeColor = strokeColor;
		this.lineSpacingHeight = defaultLineSpacingHeight;
	}
	
	public TextStyle() {
		this(defaultFontName, defaultFontSize, defaultStrokeColor);
	}

	public String getFontName(){
		return fontName;
	}
	
	public double getFontSize(){
		return fontSize;
	}
	
	public Color getStrokeColor(){
		return strokeColor;
	}

	public FontMetrics getFontMetrics() {
		return Toolkit.getToolkit().getFontLoader().getFontMetrics(FontHelper.getFont(fontName, fontSize));
	}

	public void setFontName(String string) {
		this.fontName = string;
	}

	public void setFontSize(double parseInt) {
		this.fontSize = parseInt;
	}

	public void setFontColor(String string) {
		this.strokeColor = Color.web(string);
	}
	
	public void setFontColor(Color color) {
		this.strokeColor = color;
	}

	public Font getFont() {
		return FontHelper.getFont(fontName, fontSize);
	}

	public float getLineSpacingHeight() {
		FontMetrics metrics = getFontMetrics();
		return metrics.getLineHeight() + lineSpacingHeight;
	}

	public Paint getSelectionColor() {
		return defaultSelectionColor;
	}

	public Paint getInvertedStrokeColor() {
		return strokeColor.invert();
	}

}
