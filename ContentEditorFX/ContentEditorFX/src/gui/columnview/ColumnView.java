package gui.columnview;

import gui.ShapedPane;
import gui.helper.DebugHelper;
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
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import settings.GlobalAppSettings;
import control.TextModifyFacade;
import document.Column;
import document.DocumentText;
import document.layout.LayoutMachine;
import document.widget.Widget;
import event.ShapeDrawFacade;
import event.ShapeDrawFacade.ShapeDrawingMode;
import event.modification.ModificationInstance;
import event.modification.ModificationType;
import event.modification.ResizeModification;
import event.modification.TranslateModification;

public class ColumnView extends Pane implements VisualView, CanvasOwner{
	
	private ColumnView selfReference;
	
	protected Column column;
	
	private Canvas canvas;
	private GraphicsContext context;
	private GraphicsContext overlayContext;
	
	private DocumentView parent;
	private SimpleObjectProperty<DocumentText> text;
	private ShapeDrawFacade shapeDrawFacade;
	private TextModifyFacade textModifyFacade;
		
	private boolean isRefreshInProgress;
	
	private ArrayList<ShapedPane> visuals;
	private ArrayList<ParagraphOnCanvas> paragraphsOnCanvas;

	private HashMap<ShapedPane, ModificationInstance> modificationHash;

	public ColumnView(DocumentView parent, TextModifyFacade textModifyFacade, ShapeDrawFacade shapeDrawFacade){
		System.out.println("ColumnView initialized");
		this.parent = parent;
		this.textModifyFacade = textModifyFacade;
		this.shapeDrawFacade = shapeDrawFacade;
		selfReference = this;
		isRefreshInProgress = false;
		canvas = new Canvas();
		canvas.toBack();
		context = canvas.getGraphicsContext2D();
		context.setStroke(Color.BLACK);
		visuals = new ArrayList<ShapedPane>();
		paragraphsOnCanvas = new ArrayList<ParagraphOnCanvas>();
		this.setId("columnview-selected");
		this.text = new SimpleObjectProperty<DocumentText>();
		modificationHash = new HashMap<ShapedPane, ModificationInstance>();
		initEvents();
		this.getChildren().add(canvas);
	}
	
	private void populateParagraphViews() {
		for(int i = 0; i < text.get().getParagraphSetsInColumn(this.column).size(); i++) {
			paragraphsOnCanvas.add(new ParagraphOnCanvas(this, text.get().getParagraphSet(i), textModifyFacade));
		}
	}

	private void initEvents(){
		text.addListener(new ChangeListener<DocumentText>(){
			@Override
			public void changed(ObservableValue<? extends DocumentText> arg0,
					DocumentText arg1, DocumentText newText) {	
				populateParagraphViews();
				refreshTextCanvas();
			}
		});
		
		this.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent event) {
		        selfReference.onMouseClick(event);
		        parent.refocusTextField();
		        DebugHelper.mouseClickEvent();
		    }
		});
		
		this.addEventFilter(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent> () {
			@Override
			public void handle(MouseEvent arg0) {
				selfReference.onMouseMoved(arg0);
			}
		});
		
		this.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				selfReference.onMouseDragged(arg0);
				DebugHelper.mouseDragEvent();
			}
		});
		
		this.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				textModifyFacade.getCaret().changeMousePointer(Cursor.TEXT);
			}
		});
		
		this.addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				textModifyFacade.getCaret().changeMousePointer(Cursor.DEFAULT);
			}
		});
	}
	
	protected void onMouseMoved(MouseEvent event) {
		ShapeDrawFacade facade = parent.getShapeDrawFacade();
		if(facade.getDrawingMode() == ShapeDrawingMode.Off) {
			for(int i = 0; i < paragraphsOnCanvas.size(); i++) {
				if(paragraphsOnCanvas.get(i).containsCoordinate((float)event.getX(), (float)event.getY())) {
					paragraphsOnCanvas.get(i).mouseMoved(event);
				}
			}
		}
		else{
			facade.onMouseMoved(event, selfReference);
			refreshOverlayCanvas();
		}
	}

	protected void onMouseClick(MouseEvent event) {
		ShapeDrawFacade facade = parent.getShapeDrawFacade();
		if(facade.getDrawingMode() == ShapeDrawingMode.Off) {
			for(int i = 0; i < paragraphsOnCanvas.size(); i++) {
				if(paragraphsOnCanvas.get(i).containsCoordinate((float)event.getX(), (float)event.getY())) {
					paragraphsOnCanvas.get(i).mouseClick(event);
				}
			}
		}
		else {
			facade.onMouseClick(event, selfReference);
			refreshOverlayCanvas();
		}
	}

	protected void onMouseDragged(MouseEvent event) {
		ShapeDrawFacade facade = parent.getShapeDrawFacade();
		if(facade.getDrawingMode() == ShapeDrawingMode.Off) {
			for(int i = 0; i < paragraphsOnCanvas.size(); i++) {
				if(paragraphsOnCanvas.get(i).containsCoordinate((float)event.getX(), (float)event.getY())) {
					paragraphsOnCanvas.get(i).mouseDrag(event);
				}
			}
		}
		else {
			facade.onMouseDragged(event, selfReference);
			refreshOverlayCanvas();
		}
	}

	
	public void associateWithColumn(Column column){
		this.column = column;
		this.setWidth(column.getWidth());
		this.setHeight(column.getHeight());
		this.setPrefSize(column.getWidth(), column.getHeight());
		canvas.setWidth(column.getWidth());
		canvas.setHeight(column.getHeight());
		parent.refresh();
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

	private void refreshAll(){
		if(parent.isOverlayCanvasVisible()) {
			refreshOverlayCanvas();
		}
		if(parent.isTextCanvasVisible()) {
			refreshTextCanvas();
		}
		else {
			clearTextCanvas();
		}
	}

	public void refreshOverlayCanvas(){
		overlayContext = parent.getGraphicsContext();
		overlayContext.clearRect(0, 0, overlayContext.getCanvas().getWidth(), overlayContext.getCanvas().getHeight());
		
		overlayContext.save();
		overlayContext.translate(canvas.getBoundsInParent().getMinX(), canvas.getBoundsInParent().getMinY());
		
		for(int i = 0; i < visuals.size(); i++){
			VisualView view = visuals.get(i);
			if(view instanceof ShapedPane){
				((ShapedPane) view).paintShape(overlayContext);
			}
		}
		
		for(int i = 0; i < paragraphsOnCanvas.size(); i++) {
			paragraphsOnCanvas.get(i).refreshOverlay();
		}
		
		if(GlobalAppSettings.areGuiDebugGuidelinesVisible()){
			drawWidgetGuidelines();
		}
		
		if(parent.getShapeDrawFacade().getDrawingMode() != ShapeDrawingMode.Off) {
			parent.getShapeDrawFacade().paintCurrentShape(overlayContext);
		}
		
		overlayContext.restore();
	}
	
	private void refreshTextValuesOnly() {
		clearTextCanvas();
		if(parent.isInsetVisible()) {
			drawInsets();
		}
		for(ParagraphOnCanvas paragraph: paragraphsOnCanvas) {
			paragraph.refresh();
		}
	}
	
	private void refreshTextCanvas(){
		clearTextCanvas();
		canvas.toBack();
		drawInsets();
		refreshTextValuesOnly();
	}
	
	/**
	 * should only be called while drawing
	 */
	private void drawWidgetGuidelines() {
		for(int i = 0; i < column.getWidgets().size(); i++){
			Widget widget = column.getWidgets().get(i);
			overlayContext.strokeRect(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight());
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
		column.addWidget(widgetModifier.getWidget());
	}

	/**
	 * This means that the child is in need of repaints.
	 */
	@Override
	public void notifyRepaintNeeded() {
		parent.refresh();
	}
	
	@Override
	public void notifyOverlayRepaintNeeded() {
		parent.notifyOverlayRepaintNeeded();
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
	
	private void clearTextCanvas() {
		context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	public void setDocumentText(DocumentText documentText) {
		this.text.set(documentText);
	}

	public GraphicsContext getOverlayContext() {
		return overlayContext;
	}

	public void notifyTextRepaintNeeded() {
		refreshTextValuesOnly();
	}

	public ArrayList<ParagraphOnCanvas> getParagraphsOnCanvas() {
		return paragraphsOnCanvas;
	}

	public int getStartIndex() {
		return paragraphsOnCanvas.get(0).getStartIndex();
	}

	public int getEndIndex() {
		return paragraphsOnCanvas.get(paragraphsOnCanvas.size() - 1).getEndIndex();
	}
	
	public DocumentView getDocumentView() {
		return parent;
	}

	public LayoutMachine getLayoutMachine() {
		return column.getLayoutMachine();
	}
	
}