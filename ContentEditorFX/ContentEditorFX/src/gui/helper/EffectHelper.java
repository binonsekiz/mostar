package gui.helper;

import java.util.Random;

import javafx.animation.FillTransition;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Reflection;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class EffectHelper {
	
	private static Blend titleBlend;
	private static Reflection titleReflection;
	private static GaussianBlur titleBlur;
	
	private static Stop[] stops;
	private static LinearGradient linearGradient;
	private static Random random;
	
	private static boolean isInitialized = false;
	
	private static void initialize(){
		isInitialized = true;
		titleBlend = new Blend();
		titleBlend.setMode(BlendMode.MULTIPLY);
		titleBlur = new GaussianBlur(1.5);
		titleReflection = new Reflection();
		titleReflection.setFraction(0.5f);
		titleBlend.setTopInput(titleBlur);
		titleBlend.setBottomInput(titleReflection);
		random = new Random();
		
		stops = new Stop[]{new Stop(0, Color.WHITE), new Stop(0.2, Color.BLACK), new Stop(0.8, Color.BLACK), new Stop(1, Color.WHITE)};
		linearGradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
	}
	
	public static void setAsATemplate(Button button, double size){
		if(isInitialized == false) initialize();
		GridPane.setHalignment(button, HPos.CENTER);
		button.setFont(FontHelper.getFont("Vera", size));
		button.setTextAlignment(TextAlignment.CENTER);
		button.setEffect(titleBlend);
	}
	
	public static void setAsATitle(Text text, double size){
		if(isInitialized == false) initialize();
		GridPane.setHalignment(text, HPos.CENTER);
		text.setFont(FontHelper.getFont("Vera", size));
		text.setTextAlignment(TextAlignment.CENTER);
		text.setEffect(titleBlend);
	}
	
	public static void setAsATitleNoReflection(Text text, double size){
		if(isInitialized == false) initialize();
		GridPane.setHalignment(text, HPos.CENTER);
		text.setFont(FontHelper.getFont("Vera", size));
		text.setTextAlignment(TextAlignment.CENTER);
	}
	
	public static void setAsABackButton(Button backButton){
		backButton.setFont(FontHelper.getFont("Vera", 40));
		Reflection ref = new Reflection();
		ref.setTopOffset(15);
		backButton.setEffect(ref);
	}
	
	public static void setAsWidgetHeaderText(Text text){
		text.setFont(FontHelper.getFont("Cambria", 14));
		text.setFill(Color.web("0x333333"));
		text.setTextAlignment(TextAlignment.CENTER);
	}
	
	public static void setAsWidgetFooterText(Text text){
		text.setFont(FontHelper.getFont("Calibri", 12));
		text.setFill(Color.web("0x333333"));
		text.setTextAlignment(TextAlignment.CENTER);
	}
	
	public static void addMouseRolloverColorTransition(Shape shape){
		addMouseRolloverColorTransition(shape, Color.BLACK, Color.TURQUOISE, 200);
	}
	
	public static void addMouseRolloverColorTransition(Shape shape, Color start, Color end, int milliseconds){
		final FillTransition ft = new FillTransition(Duration.millis(milliseconds), shape, start, end);
		ft.setAutoReverse(false);
		
		shape.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				ft.setRate(1);
				ft.play();
			}
		});
		
		shape.addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				ft.setRate(-1);
				ft.play();
			}
		});	
	}
	
	public static Line getSmoothLine(double startX, double startY, double endX, double endY){
		Line line = new Line(startX, startY, endX, endY);
		line.setStroke(linearGradient);
		return line;
	}
	
	public static void setAsATitle(Text text){
		setAsATitle(text, 48);
	}
	
	public static Color getRandomColor() {
		return new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1);
	}
	
}
