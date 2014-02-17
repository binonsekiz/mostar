package gui.helper;

import geometry.GeometryHelper;
import geometry.libgdxmath.Polygon;
import geometry.libgdxmath.Rectangle;
import gui.docmodify.DocDebugView;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import document.Column;
import document.ParagraphSpace;
import document.TextStyle;

public class DebugHelper {

	public static Rectangle rect1 = new Rectangle(20, 20, 300, 250); 
	public static Rectangle rect2 = new Rectangle(20, 350, 200, 150); 
	public static Rectangle rect3 = new Rectangle(340, 50, 280, 400);
	public static Rectangle rect4 = new Rectangle(30,30, 420, 600);
	public static TextStyle debugStyle1;
	public static TextStyle debugStyle2;
	public static TextStyle debugStyle3;
	public static TextStyle debugStyle4;
	public static TextStyle helperStyle1;
	public static Polygon debugPolygon1;
	public static Polygon debugPolygon2;
	public static Polygon debugPolygon3;
	public static Polygon debugPolygon4;
	
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
		
		debugStyle4 = new TextStyle();
		debugStyle4.setFontName("coolvetica");
		debugStyle4.setFontColor(Color.BLACK);
		debugStyle4.setFontSize(18);
		
		helperStyle1 = new TextStyle();
		helperStyle1.setFontName("vera");
		helperStyle1.setFontColor(Color.MAGENTA);
		helperStyle1.setFontSize(12);
		
		rects = new ArrayList<Rectangle>();
		rects.add(rect1);
		rects.add(rect2);
		rects.add(rect3);
		rects.add(rect4);
		
		debugPolygon1 = GeometryHelper.getRegularPolygon(200, 200, 160, 6);
		debugPolygon2 = GeometryHelper.getRegularPolygon(200, 200, 200, 3);
		debugPolygon3 = GeometryHelper.getRegularPolygon(150, 150, 100, 4);
		debugPolygon4 = GeometryHelper.getRegularPolygon(250, 250, 200, 64);
		
		paragraphSpaces = new ArrayList<ParagraphSpace>();
		paragraphSpaces.add(new ParagraphSpace(Column.debugInstance, debugPolygon4));
		paragraphSpaces.add(new ParagraphSpace(Column.debugInstance, debugPolygon1));
	}
	
	public static int mouseClickCount = 0;
	public static void mouseClickEvent(){
		mouseClickCount ++;
		DocDebugView.instance.setDebugText("Mouse Click: " + mouseClickCount, 4);
	}
	
	public static int mouseDragCount = 0;
	public static ArrayList<Rectangle> rects;
	public static ArrayList<ParagraphSpace> paragraphSpaces;
	
	public static void mouseDragEvent(){
		mouseDragCount ++;
		DocDebugView.instance.setDebugText("Mouse Drag: " + mouseDragCount, 4);
	}
	
}
