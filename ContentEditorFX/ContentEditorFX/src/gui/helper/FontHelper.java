package gui.helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;

import document.TextStyle;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class FontHelper {
	
	private static HashMap<String, Font> fonts = new HashMap<String,Font>();
	private static HashMap<String, Color> colorsFromName = null;
	private static HashMap<String, FontMetrics> fontMetrics = new HashMap<String, FontMetrics>();
	
	private static int fontCount = 0;
	
	/**
	 * Simple loader for ttf type fonts. For other types of fonts, write new functions.
	 * @param name
	 * @param size
	 * @return
	 */
	public static Font getFont(String name, double size){
		if(fonts.containsKey(name + size)){
			return fonts.get(name + size);
		}
		else{
			Font retVal;
			try {
				retVal = Font.loadFont(new FileInputStream("res/font/" + name + ".ttf"), size);
				fonts.put(name + size, retVal);
				fontMetrics.put(name + size, Toolkit.getToolkit().getFontLoader().getFontMetrics(retVal));
				fontCount ++;
				return retVal;
			} catch (FileNotFoundException e) {
				try{
					retVal = Font.font(name, size);
					fonts.put(name + size, retVal);
					fontMetrics.put(name + size, Toolkit.getToolkit().getFontLoader().getFontMetrics(retVal));
					return retVal;
				}catch(Exception ex){
					e.printStackTrace();
					ex.printStackTrace();
					return Font.getDefault();
				}
			}
		}
	}
	
	public static Color getColorByName(String name){
		if(colorsFromName == null){
			colorsFromName = getJavaFXColorMap();
		}
		return colorsFromName.get(name);
	}

	private static HashMap<String, Color> getJavaFXColorMap() {
	    Field[] declaredFields = Color.class.getDeclaredFields();
	    HashMap<String, Color> colors = new HashMap<>();
	    for (Field field : declaredFields) {
	        if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())) {
	            try {
	                colors.put(field.getName(), (Color)field.get(null));
	            } catch (Exception e) {
	                e.printStackTrace();
	            } 
	        }
	    }
	    return colors;
	}
	
	public static ArrayList<Float> calculateFontSizeArray(String textBuffer, Font font){
		ArrayList<Float> cummulativeWordSizes = new ArrayList<Float>();
		FontMetrics metrics = Toolkit.getToolkit().getFontLoader().getFontMetrics(font);
		
		if(textBuffer == null)
			return cummulativeWordSizes;
		
		for(int i = 0; i < textBuffer.length(); i++){
			float stringWidth = metrics.computeStringWidth(textBuffer.substring(0, i + 1));
			cummulativeWordSizes.add(stringWidth);
		}
		return cummulativeWordSizes;
	}

	public static int getFittingSubstringSize(String text, float maxAllowedSize, FontMetrics metrics) {
		for(int i = 0; i < text.length(); i++){
			metrics.computeStringWidth(text.substring(0, i));
		}
		return fontCount;
	}
	
}
