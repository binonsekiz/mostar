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
	public static Polygon debugPolygon5;
	public static Polygon debugPolygon6;
	
	public DebugHelper() {
		debugStyle1 = TextStyleRepository.getTextStyle("cafe", 24, Color.BROWN, Color.AQUA, 50);
		debugStyle2 = TextStyleRepository.getTextStyle("coolvetica", 30, Color.DARKSLATEGRAY, Color.ANTIQUEWHITE, 35);
		helperStyle1 = TextStyleRepository.getTextStyle("vera", 8, Color.MAGENTA, Color.WHITE, 32);
		
		rects = new ArrayList<Rectangle>();
		rects.add(rect1);
		rects.add(rect2);
		rects.add(rect3);
		rects.add(rect4);
		
		debugPolygon1 = GeometryHelper.getRegularPolygon(340, 300, 120, 6, 0);
		debugPolygon2 = GeometryHelper.getRegularPolygon(200, 600, 200, 3, 0);
		debugPolygon3 = GeometryHelper.getRegularPolygon(250, 250, 240, 4, 45);
		debugPolygon4 = GeometryHelper.getRegularPolygon(240, 240, 200, 64, 0);
		debugPolygon5 = GeometryHelper.getRegularPolygon(350, 400, 350, 4, 45);
		debugPolygon6 = GeometryHelper.getRegularPolygon(120, 350, 100, 64, 0);
		
		paragraphSpaces = new ArrayList<ParagraphSpace>();
		paragraphSpaces.add(new ParagraphSpace(debugPolygon1));
		paragraphSpaces.add(new ParagraphSpace(debugPolygon2));
		paragraphSpaces.add(new ParagraphSpace(debugPolygon3));
		paragraphSpaces.add(new ParagraphSpace(debugPolygon4));
		paragraphSpaces.add(new ParagraphSpace(debugPolygon5));
		paragraphSpaces.add(new ParagraphSpace(debugPolygon6));
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
