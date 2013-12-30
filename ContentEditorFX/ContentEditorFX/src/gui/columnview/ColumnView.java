package gui.columnview;

import geometry.libgdxmath.LineSegment;
import geometry.libgdxmath.Vector2;
import gui.ShapedPane;
import gui.helper.LayoutMachine;
import gui.widget.WidgetModifier;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import settings.GlobalAppSettings;
import zzzzdeprecated.StyledTextDeprecated;
import document.Column;
import document.widget.Widget;
import event.modification.ModificationInstance;
import event.modification.ModificationType;
import event.modification.ResizeModification;
import event.modification.TranslateModification;

public class ColumnView extends Pane implements VisualView, CanvasOwner{
	
	private int debugCount = 0;
	
	private ColumnView selfReference;
	
	protected Column column;
	
	private Canvas canvas;
	private GraphicsContext context;
	
	private DocumentView parent;
	private SimpleObjectProperty<StyledTextDeprecated> text;
		
	private boolean isRefreshInProgress;
	
	private ArrayList<ShapedPane> visuals;
	private ArrayList<ParagraphOnCanvas> paragraphsOnCanvas;
	
	private LayoutMachine layoutMachine;
	
	private float lastFilledLineHeight;
	private ArrayList<LineSegment> backupLines;
	
	private ArrayList<Vector2> debugPoints;
	
	private HashMap<ShapedPane, ModificationInstance> modificationHash;
	
	public ColumnView(DocumentView parent){
		this.parent = parent;
		selfReference = this;
		isRefreshInProgress = false;
		canvas = new Canvas();
		canvas.toBack();
		context = canvas.getGraphicsContext2D();
		context.setStroke(Color.BLACK);
		visuals = new ArrayList<ShapedPane>();
		paragraphsOnCanvas = new ArrayList<ParagraphOnCanvas>();
		
		this.setId("columnview-selected");
		layoutMachine = new LayoutMachine();
		this.text = new SimpleObjectProperty<StyledTextDeprecated>();
		backupLines = new ArrayList<LineSegment>();
		lastFilledLineHeight = 0;
		modificationHash = new HashMap<ShapedPane, ModificationInstance>();
		initEvents();
		this.getChildren().add(canvas);
		
		debugPoints = new ArrayList<Vector2>();
	}
	
	private void initEvents(){
		text.addListener(new ChangeListener<StyledTextDeprecated>(){
			@Override
			public void changed(ObservableValue<? extends StyledTextDeprecated> arg0,
					StyledTextDeprecated arg1, StyledTextDeprecated newText) {	
				paragraphsOnCanvas.clear();
				paragraphsOnCanvas.add(layoutMachine.getParagraphSpace(selfReference, column.getInsets().getUsableRectangle(), null));
				refresh();
			}
		});
		
		this.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent event) {
		        System.out.println("clicked on column view");
		    }
		});
	}

	public void associateWithColumn(Column column){
		this.column = column;
		this.setWidth(column.getWidth());
		this.setHeight(column.getHeight());
		this.setPrefSize(column.getWidth(), column.getHeight());
		canvas.setWidth(column.getWidth());
		canvas.setHeight(column.getHeight());
		layoutMachine.setPageInsets(column.getInsets());
		lastFilledLineHeight = column.getInsets().getMinY();
		layoutMachine.setParentShape(column.getShape());
		refresh();
	}
	
	public void refresh(){
		if(!isRefreshInProgress){
			isRefreshInProgress = true;
			Timeline timer = new Timeline(new KeyFrame(Duration.millis(GlobalAppSettings.fastDeviceFrameMillis), new EventHandler<ActionEvent>(){
				@Override
			    public void handle(ActionEvent event) {
			        refreshAll();
			    }
			}));
			timer.play();
			isRefreshInProgress = false;
		}
	}
	
	public void debugPoint(Vector2 point){
		this.debugPoints.add(new Vector2(point));
	}

	private void refreshAll(){
		System.out.println("\n\nRefreshing all");
		context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		refreshCanvasOnly();
		refreshDebugPoints();
		refreshTextOnly();
		
		parent.notifyRepaintNeeded();
	}
	
	private void refreshDebugPoints() {
		context.setStroke(Color.GREEN);
		context.setLineWidth(2);
		for(Vector2 point:debugPoints){
			context.strokeOval(point.x-1, point.y-1, 3, 3);
		}
		debugPoints.clear();
	}

	private void refreshCanvasOnly(){
		canvas.toBack();
		drawInsets();
		for(int i = 0; i < visuals.size(); i++){
			VisualView view = visuals.get(i);
			if(view instanceof ShapedPane){
				((ShapedPane) view).paintShape(context);
			}
		}
		
		if(GlobalAppSettings.areGuiDebugGuidelinesVisible()){
			drawWidgetGuidelines();
		}
	}
	
	private void refreshTextOnly(){
	/*	for(int i = 0; i < lineViewsOnCanvas.size(); i++){
			lineViewsOnCanvas.get(i).refresh();
		}*/
		System.out.println("updating text");
		paragraphsOnCanvas.clear();
		paragraphsOnCanvas.add(layoutMachine.getParagraphSpace(this, column.getInsets().getUsableRectangle(), null));
		
		System.out.println("refreshing text");
		for(ParagraphOnCanvas paragraph: paragraphsOnCanvas) {
			paragraph.refresh();
		}
		
	}
	
	/**
	 * should only be called while drawing
	 */
	private void drawWidgetGuidelines() {
		for(int i = 0; i < column.getWidgets().size(); i++){
			Widget widget = column.getWidgets().get(i);
			context.strokeRect(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight());
		}
	}
	
	private void drawInsets(){
		context.setStroke(Color.GRAY);
		context.setLineWidth(0.5f);
		context.strokeRect(column.getInsets().getMinX(), column.getInsets().getMinY(), column.getInsets().getActualWidth(), column.getInsets().getActualHeight());
	}

	public void removeWidgetModifier(WidgetModifier widgetModifier) {
		this.getChildren().remove(widgetModifier);
		visuals.remove(widgetModifier);
	}
	
	public void addWidgetModifier(WidgetModifier widgetModifier) {
		this.getChildren().add(widgetModifier);
		visuals.add(widgetModifier);
		layoutMachine.addSingleElement(widgetModifier);
	}

	public void setStyledText(StyledTextDeprecated styledText) {
		this.text.set(styledText);
	}

	/**
	 * This means that the child is in need of repaints.
	 */
	@Override
	public void notifyRepaintNeeded() {
		refresh();
	}

	@Override
	public void notifyModificationStart(ModificationType type, ShapedPane pane, MouseEvent event) {
		ModificationInstance modInstance = null;
		if(modificationHash.containsKey(pane)){
			modInstance = modificationHash.get(pane);
		}
		else{
			switch(type){
			case Resize:
				modInstance = new ResizeModification(pane, pane.getResizeIndex());
				break;
			case Transform:
				modInstance = new TranslateModification(pane);
				break;
			case Scale:
			default:
			}
			
			modificationHash.put(pane, modInstance);
		}
		modInstance.modificationStart(event);
	}

	@Override
	public void notifyMouseMovement(ShapedPane pane, MouseEvent event) {
		ModificationInstance modInstance = modificationHash.get(pane);
		modInstance.mouseMovement(event);
	}

	@Override
	public void notifyModificationEnd(ShapedPane pane, MouseEvent event) {
		ModificationInstance modInstance = modificationHash.get(pane);
		modInstance.modificationEnd(event);
		modificationHash.remove(pane);
	}

	@Override
	public GraphicsContext getGraphicsContext() {
		return context;
	}

	public Column getColumn() {
		return column;
	}
}