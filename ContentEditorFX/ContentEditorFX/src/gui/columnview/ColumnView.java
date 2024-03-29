package gui.columnview;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
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
import document.ParagraphSet;
import document.layout.LayoutMachine;
import document.project.ProjectRepository;
import document.visual.VisualComponent;
import document.widget.Widget;
import event.ShapeDrawFacade;
import event.ShapeDrawFacade.ShapeDrawingMode;
import event.modification.ModificationInstance;
import event.modification.ModificationType;
import event.modification.ResizeModification;
import event.modification.TranslateModification;
import geometry.libgdxmath.Vector2;
import gui.ShapedPane;
import gui.docmodify.DocDebugView;
import gui.helper.CustomScrollPane;
import gui.helper.DebugHelper;
import gui.widget.WidgetModifier;

public class ColumnView extends Pane implements VisualView, CanvasOwner{
	private static int totalCount = 0;
	private ColumnView selfReference;
	private int debugId;
	
	protected Column column;
	
	private Canvas canvas;
	private GraphicsContext context;
	private GraphicsContext overlayContext;
	
	private DocumentView parent;
	private SimpleObjectProperty<DocumentText> text;
	private ShapeDrawFacade shapeDrawFacade;
	private TextModifyFacade textModifyFacade;
		
	private boolean isRefreshInProgress;
	private int selectedShapeIndex;
	
	private ArrayList<ShapedPane> visuals;
	private ArrayList<ParagraphOnCanvas> paragraphsOnCanvas;

	private HashMap<ShapedPane, ModificationInstance> modificationHash;

	public ColumnView(DocumentView parent, TextModifyFacade textModifyFacade, ShapeDrawFacade shapeDrawFacade){
		totalCount ++;
		debugId = totalCount;
		System.out.println("ColumnView initialized " + debugId);
		this.parent = parent;
		this.shapeDrawFacade = shapeDrawFacade;
		this.textModifyFacade = textModifyFacade;
		this.shapeDrawFacade = shapeDrawFacade;
		selfReference = this;
		isRefreshInProgress = false;
		selectedShapeIndex = -1;
		canvas = new Canvas();
		canvas.toBack();
		context = canvas.getGraphicsContext2D();
		context.setStroke(Color.BLACK);
		visuals = new ArrayList<ShapedPane>();
		paragraphsOnCanvas = new ArrayList<ParagraphOnCanvas>();
		this.text = new SimpleObjectProperty<DocumentText>();
		modificationHash = new HashMap<ShapedPane, ModificationInstance>();
		initEvents();
		this.getChildren().add(canvas);
	}
	
	public void insertParagraphSet(ParagraphSet newSet) {
		newSet.setColumn(column);
		paragraphsOnCanvas.add(new ParagraphOnCanvas(this, newSet, textModifyFacade));
		column.getLayoutMachine().initialSetup();
	}
	
	private void populateParagraphViews() {
		paragraphsOnCanvas.clear();
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
		        selfReference.onMouseEvent(event);
		        selfReference.requestFocus();
		        DebugHelper.mouseClickEvent();
		    }
		});
		
		this.addEventFilter(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent> () {
			@Override
			public void handle(MouseEvent arg0) {
				selfReference.onMouseEvent(arg0);
			}
		});
		
		this.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				selfReference.onMouseEvent(arg0);
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
	
	protected void onMouseEvent(MouseEvent event) {
		if(shapeDrawFacade.getDrawingMode() == ShapeDrawingMode.Off) {
			checkParagraphs(event);
			checkShapes(event);
		}
		else{
			shapeDrawFacade.onMouseEvent(event, selfReference);
			parent.refreshOverlay();
		}
	}

	private void checkShapes(MouseEvent event) {
		float mx = (float) event.getX();
		float my = (float) event.getY();
		float smallestAreaSize = Float.MAX_VALUE;
		int smallestAreaIndex = -1;
		for(int i = 0; i < column.getShapes().size(); i++) {
			VisualComponent p = column.getShapes().get(i);
			if(p.contains(mx, my)) {
				float area = p.area();
				if(area < smallestAreaSize) {
					smallestAreaSize = area;
					smallestAreaIndex = i;
				}
			}
		}
		
		if(selectedShapeIndex != smallestAreaIndex) {
			this.selectedShapeIndex = smallestAreaIndex;
			parent.refreshOverlay();
			DocDebugView.instance.setDebugText(smallestAreaSize + "", 3);
		}
	}

	private void checkParagraphs(MouseEvent event) {
		float mx = (float) event.getX();
		float my = (float) event.getY();
		for(int i = 0; i < paragraphsOnCanvas.size(); i++) {
			if(paragraphsOnCanvas.get(i).containsCoordinate(mx, my)) {
				if(event.getEventType() == MouseEvent.MOUSE_MOVED) {
					paragraphsOnCanvas.get(i).mouseMoved(event);
				}
				else if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {
					paragraphsOnCanvas.get(i).mouseClick(event);
				}
				else if(event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
					paragraphsOnCanvas.get(i).mouseDrag(event);
				}
			}
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
			        isRefreshInProgress = false;
			    }
			}));
			timer.play();
		}
	}

	private void refreshAll(){
		if(parent.isOverlayCanvasVisible()) {
			parent.refreshAllOverlay();
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
		
		//make proper transforms for the overlay canvas
		overlayContext.save();
		CustomScrollPane scrollPane = (CustomScrollPane) getParent();
		Vector2 translateVector = scrollPane.convertContentCoordinateToScreenCoordinate(0, 0);
		
		overlayContext.translate(translateVector.x, translateVector.y);
		overlayContext.translate(parent.getOverlayOffsetX(), parent.getOverlayOffsetY());
		Bounds bounds = this.getBoundsInParent();
		
		overlayContext.scale(scrollPane.zoomProperty().get(), scrollPane.zoomProperty().get());
		overlayContext.translate(bounds.getMinX(), bounds.getMinY());
		//transform ends here
		
		overlayContext.setLineWidth(2);
		for(int i = 0; i < visuals.size(); i++){
			VisualView view = visuals.get(i);
			if(view instanceof ShapedPane){
				((ShapedPane) view).paintShape(overlayContext);
			}
		}
		
		for(int i = 0; i < paragraphsOnCanvas.size(); i++) {
			paragraphsOnCanvas.get(i).refreshOverlay();
		}
		
		if(GlobalAppSettings.guiDebugGuidelines){
			drawWidgetGuidelines();
		}
		
		ShapeDrawFacade facade = ProjectRepository.getActiveProjectEnvironment().getShapeDrawFacade();
		if(facade.getDrawingMode() != ShapeDrawingMode.Off && facade.isCallerColumnView(this)) {
			facade.paintCurrentShape(overlayContext);
		}
		
		if(selectedShapeIndex >= 0) {
			overlayContext.setStroke(Color.GREEN);
			overlayContext.setLineWidth(1.5f);
			column.getShapes().get(selectedShapeIndex).draw(overlayContext);
		}
		
		overlayContext.restore();
	}
	
	private void refreshTextValuesOnly() {
		for(ParagraphOnCanvas paragraph: paragraphsOnCanvas) {
			paragraph.refresh();
		}
	}
	
	private void refreshTextCanvas(){
		clearTextCanvas();
		canvas.toBack();
		drawInsets();
		drawShapes();
		refreshTextValuesOnly();
	}
	
	private void drawShapes() {
		context.setStroke(Color.DARKRED);
		for(int i = 0; i < column.getShapes().size(); i++) {
			column.getShapes().get(i).draw(context);
		}
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
		if(paragraphsOnCanvas.size() == 0) {
			//TODO: return previous columns end index
			return 0;
		}
		return paragraphsOnCanvas.get(0).getStartIndex();
	}

	public int getEndIndex() {
		if(paragraphsOnCanvas.size() == 0){
			//TODO: return previous columns end index
			return 0;
		}
		return paragraphsOnCanvas.get(paragraphsOnCanvas.size() - 1).getEndIndex();
	}
	
	public DocumentView getDocumentView() {
		return parent;
	}

	public LayoutMachine getLayoutMachine() {
		return column.getLayoutMachine();
	}
	
}