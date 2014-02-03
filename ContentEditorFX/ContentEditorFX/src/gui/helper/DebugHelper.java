package gui.helper;

import geometry.libgdxmath.Rectangle;
import javafx.scene.paint.Color;
import document.TextStyle;

public class DebugHelper {

	public static Rectangle rect1 = new Rectangle(20, 20, 300, 250); 
	public static Rectangle rect2 = new Rectangle(20, 350, 200, 150); 
	public static Rectangle rect3 = new Rectangle(340, 50, 280, 400);
	public static TextStyle debugStyle1;
	public static TextStyle debugStyle2;
	public static TextStyle debugStyle3;
	
	
	public DebugHelper() {
		debugStyle1 = new TextStyle();
		debugStyle1.setFontName("cafe");
		debugStyle1.setFontColor(Color.BROWN);
		debugStyle1.setFontSize(14);
		
		debugStyle2 = new TextStyle();
		debugStyle2.setFontName("coolvetica");
		debugStyle2.setFontColor(Color.DARKSLATEGREY);
		debugStyle2.setFontSize(18);
		
		debugStyle3 = new TextStyle();
		debugStyle3.setFontName("vera");
		debugStyle3.setFontColor(Color.CRIMSON);
		debugStyle3.setFontSize(9.5);
	}
	
}
