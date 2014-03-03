package document.style;

import gui.helper.FontHelper;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import settings.GlobalAppSettings;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;

public class TextStyle implements Comparable<TextStyle>{
	
	public static String defaultFontName = "Vera";
	public static double defaultFontSize = 12;
	public static Color defaultStrokeColor = Color.DARKSLATEGRAY;
	public static Color defaultFillColor = Color.ALICEBLUE;
	public static float defaultLineSpacingHeight = 20;
	private static Color defaultSelectionColor = new Color(Color.DARKBLUE.getRed(), Color.DARKBLUE.getGreen(), Color.DARKBLUE.getBlue(), 1f);
	
	public static final TextStyle defaultStyle = new TextStyle();
	private String fontName;
	private double fontSize;
	private float lineSpacingHeight;
	private Color strokeColor;
	private Color fillColor;
	
	protected TextStyle(String fontName, double fontSize, Color strokeColor, Color fillColor, float lineSpacingHeight){
		this.fontName = fontName;
		this.fontSize = fontSize;
		this.strokeColor = strokeColor;
		this.lineSpacingHeight = lineSpacingHeight;
		this.fillColor = fillColor;
	}
	
	public boolean isEqual(TextStyle other) {
		if( this.getFontName().equals(other.getFontName()) &&
			Math.abs(this.getFontSize() - other.getFontSize()) < GlobalAppSettings.ignoreValuesBelow &&
			this.getFillColor().equals(other.getFillColor()) && 
			this.getSelectionColor().equals(other.getSelectionColor()) && 
			this.getStrokeColor().equals(other.getStrokeColor()) && 
			Math.abs(this.getLineSpacingHeight() - other.getLineSpacingHeight()) < GlobalAppSettings.ignoreValuesBelow)
			return true;
		else return false;
	}
	
	protected TextStyle() {
		this(defaultFontName, defaultFontSize, defaultStrokeColor, defaultFillColor, defaultLineSpacingHeight);
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
	
	public Color getFillColor(){
		return fillColor;
	}

	public FontMetrics getFontMetrics() {
		return Toolkit.getToolkit().getFontLoader().getFontMetrics(FontHelper.getFont(fontName, fontSize));
	}

	public Font getFont() {
		return FontHelper.getFont(fontName, fontSize);
	}

	public float getLineSpacingHeight() {
		return lineSpacingHeight;
	}

	public Paint getSelectionColor() {
		return defaultSelectionColor;
	}

	public Paint getInvertedStrokeColor() {
		return strokeColor.invert();
	}

	public void prepareContext(GraphicsContext context) {
		context.setFont(getFont());
		context.setStroke(strokeColor);
		context.setFill(fillColor);
	}

	@Override
	public int compareTo(TextStyle o) {
		String hash = TextStyleRepository.buildHash(this);
		String otherHash = TextStyleRepository.buildHash(o);
		return hash.compareTo(otherHash);
	}

}
