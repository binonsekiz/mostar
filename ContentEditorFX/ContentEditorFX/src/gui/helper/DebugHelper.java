package gui.helper;

import geometry.GeometryHelper;
import geometry.libgdxmath.Polygon;
import geometry.libgdxmath.Rectangle;
import gui.docmodify.DocDebugView;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import document.Column;
import document.ParagraphSpace;
import document.style.TextStyle;
import document.style.TextStyleRepository;

public class DebugHelper {

	public static Rectangle rect1 = new Rectangle(20, 20, 300, 250); 
	public static Rectangle rect2 = new Rectangle(20, 350, 200, 150); 
	public static Rectangle rect3 = new Rectangle(340, 50, 280, 400);
	public static Rectangle rect4 = new Rectangle(30,30, 420, 600);
	public static TextStyle debugStyle1;
	public static TextStyle debugStyle2;
	public static TextStyle helperStyle1;
	public static Polygon debugPolygon1;
	public static Polygon debugPolygon2;
	public static Polygon debugPolygon3;
	public static Polygon debugPolygon4;
	
	public DebugHelper() {
		debugStyle1 = TextStyleRepository.getTextStyle("cafe", 14, Color.BROWN, Color.AQUA, 3);
		debugStyle2 = TextStyleRepository.getTextStyle("coolvetica", 18, Color.DARKSLATEGRAY, Color.ANTIQUEWHITE, 5);
		helperStyle1 = TextStyleRepository.getTextStyle("vera", 12, Color.MAGENTA, Color.WHITE, 3);
		
		rects = new ArrayList<Rectangle>();
		rects.add(rect1);
		rects.add(rect2);
		rects.add(rect3);
		rects.add(rect4);
		
		debugPolygon1 = GeometryHelper.getRegularPolygon(200, 200, 160, 6, 30);
		debugPolygon2 = GeometryHelper.getRegularPolygon(200, 200, 200, 3, 0);
		debugPolygon3 = GeometryHelper.getRegularPolygon(250, 250, 240, 4, 45);
		debugPolygon4 = GeometryHelper.getRegularPolygon(250, 250, 200, 64, 0);
		
		paragraphSpaces = new ArrayList<ParagraphSpace>();
		paragraphSpaces.add(new ParagraphSpace(Column.debugInstance, debugPolygon4));
		paragraphSpaces.add(new ParagraphSpace(Column.debugInstance, debugPolygon1));
		paragraphSpaces.add(new ParagraphSpace(Column.debugInstance, debugPolygon2));
		paragraphSpaces.add(new ParagraphSpace(Column.debugInstance, debugPolygon3));
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
