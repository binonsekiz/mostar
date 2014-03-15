package gui.columnview;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;
import javafx.util.Duration;
import settings.GlobalAppSettings;
import document.Column;
import document.Document;
import event.DocModifyScreenGuiFacade;
import event.ShapeDrawFacade;
import event.input.OverlayCanvas;
import event.modification.ModificationType;
import gui.ShapedPane;

/**
 * This is a pane that has a corresponding document attached.
 * It is a collection of column views.
 * @author sahin
 *
 */
public class DocumentView extends Pane implements CanvasOwner{
	
	private DocModifyScreenGuiFacade guiFacade;
	
	private Pane customScrollPane;
	private ScrollBar horizontalScrollBar;
	private ScrollBar verticalScrollBar;
	private GridPane gridPane;
	
	private ArrayList<ColumnView> columnViews;
	private double zoomFactor;
	
	private OverlayCanvas overlayCanvas;
	private GraphicsContext overlayContext;
	
	private Canvas debugCanvas;
	private GraphicsContext debugContext;
	
	private Document document;
	private static DocumentView selfReference;

	private boolean isOverlayCanvasVisible;
	private boolean isTextCanvasVisible;

	private boolean isRefreshInProgress;

	private boolean areLinePolygonsVisible;

	private boolean isPageInsetVisible;

	public DocumentView(){
		selfReference = this;
		columnViews = new ArrayList<ColumnView>();
		initGui();
		initEvents();	
		System.out.println("Document view initialized");
	}
	
	private void initGui() {
		this.setClip(new Rectangle(0,0,this.getWidth(), this.getHeight()));
		this.setId("docmodify-pane");
		
		customScrollPane = new Pane();
		gridPane = new GridPane();
		gridPane.setVgap(20);
		gridPane.setHgap(20);
		horizontalScrollBar = new ScrollBar();
		verticalScrollBar = new ScrollBar();
		horizontalScrollBar.setOrientation(Orientation.HORIZONTAL);
		verticalScrollBar.setOrientation(Orientation.VERTICAL);
		
		overlayCanvas = new OverlayCanvas(this);
		overlayContext = overlayCanvas.getGraphicsContext2D();
		customScrollPane.setLayoutX(0);
		customScrollPane.setLayoutY(0);
		overlayCanvas.setLayoutX(0);
		overlayCanvas.setLayoutY(0);
		
		debugCanvas = new OverlayCanvas(this);
		debugContext = debugCanvas.getGraphicsContext2D();
		debugCanvas.setLayoutX(0);
		debugCanvas.setLayoutY(0);
		fixCanvasSize();
		
		isOverlayCanvasVisible = true;
		isTextCanvasVisible = true;
		isRefreshInProgress = false;
		areLinePolygonsVisible = true;
		zoomFactor = 1;	
		
		customScrollPane.getChildren().addAll(gridPane, overlayCanvas, debugCanvas, verticalScrollBar, horizontalScrollBar);
		this.getChildren().addAll(customScrollPane);
		overlayCanvas.toFront();
		debugCanvas.toFront();
	}
	
	private void initEvents(){
		this.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				setClip(new Rectangle(0,0,getWidth(), getHeight()));
				fixCanvasSize();
			}
		});
		
		this.heightProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				setClip(new Rectangle(0,0,getWidth(), getHeight()));
				fixCanvasSize();
			}
		});		
	}

	public LineOnCanvas getLineThatIncludesIndex(int index) {
		int columnIndex = 0;
		int paragraphIndex = 0;
		int lineIndex = 0;		
		for(int i = 0; i < columnViews.size(); i++) {
			if(index >= columnViews.get(i).getStartIndex() && index <= columnViews.get(i).getEndIndex()) {
				columnIndex = i;
				break;
			}
		}
		
		ArrayList<ParagraphOnCanvas> paragraphs = columnViews.get(columnIndex).getParagraphsOnCanvas();
		for(int i = 0; i < paragraphs.size(); i++) {
			if(index >= paragraphs.get(i).getStartIndex() && index <= paragraphs.get(i).getEndIndex()) {
				paragraphIndex = i;
				break;
			}
		}
		
		ArrayList<LineOnCanvas> lines = paragraphs.get(paragraphIndex).getLinesOnCanvas();
		for(int i = 0; i < lines.size(); i++) {
			if(index >= lines.get(i).getStartIndex() && index <= lines.get(i).getEndIndex()) {
				lineIndex = i;
				return lines.get(lineIndex);
			}
		}
		
		return null;	
	}
	
	public void textSelectionSet(int lowerIndex, int higherIndex) {
		LineOnCanvas.selectedStartIndex = lowerIndex;
		LineOnCanvas.selectedEndIndex = higherIndex;
		guiFacade.updateVisualStyleControls();
	}
	
	public void associateWithDocument(Document document) {
		this.document = document;
		initialPopulate();
	}

	public void addColumn(Column c, int index) {
		ColumnView newView = new ColumnView(this, guiFacade.getTextModifyFacade(), guiFacade.getShapeDrawFacade());
		newView.associateWithColumn(c);
		newView.setDocumentText(document.getDocumentText());
		columnViews.add(index, newView);
		
		gridPane.getChildren().clear();
		
		for(int i = 0; i < columnViews.size(); i++) {
			gridPane.add(columnViews.get(i), i, 0);
		}
	}
	
	private void initialPopulate() {
		for(int i = 0; i < document.getColumns().size(); i++){
			Column tempColumn = document.getColumns().get(i);
			ColumnView tempColumnView = new ColumnView(this, guiFacade.getTextModifyFacade(), guiFacade.getShapeDrawFacade());
			tempColumnView.associateWithColumn(tempColumn);
			tempColumnView.setDocumentText(document.getDocumentText());
			columnViews.add(tempColumnView);
			gridPane.add(tempColumnView, i, 0);
		}
		refresh();
	}

	public void refresh() {
		if(!isRefreshInProgress){
			isRefreshInProgress = true;
			Timeline timer = new Timeline(new KeyFrame(Duration.millis(GlobalAppSettings.fastDeviceFrameMillis), new EventHandler<ActionEvent>(){
				@Override
			    public void handle(ActionEvent event) {
			        refreshAll();
			        isRefreshInProgress = false;
			    }
			}));
			timer.setCycleCount(1);
			timer.play();
		}
	}

	private void refreshAll() {
		for(int i = 0; i < columnViews.size(); i++){
			columnViews.get(i).refresh();
		}
		
		guiFacade.notifyRefreshHappened();
		fixCanvasSize();
		overlayCanvas.toFront();
		debugCanvas.toFront();
	}
	

	@Override
	public void notifyOverlayRepaintNeeded() {
		refreshOverlay();
	}
	
	public void refreshOverlay() {
		if(!isRefreshInProgress){
			isRefreshInProgress = true;
			Timeline timer = new Timeline(new KeyFrame(Duration.millis(GlobalAppSettings.fastDeviceFrameMillis), new EventHandler<ActionEvent>(){
				@Override
			    public void handle(ActionEvent event) {
			        refreshAllOverlay();
			        isRefreshInProgress = false;
			    }
			}));
			timer.setCycleCount(1);
			timer.setAutoReverse(false);
			timer.play();
		}
	}

	protected void refreshAllOverlay() {
		for(int i = 0; i < columnViews.size(); i++){
			columnViews.get(i).refreshOverlayCanvas();
		}
		
		guiFacade.notifyRefreshHappened();
		fixCanvasSize();
		customScrollPane.toBack();
		overlayCanvas.toFront();
		debugCanvas.toFront();
	}

	private void fixCanvasSize() {
		if(overlayCanvas.getWidth() != getWidth())
			overlayCanvas.setWidth(getWidth());
		if(overlayCanvas.getHeight() != getHeight())
			overlayCanvas.setHeight(getHeight());
		if(debugCanvas.getWidth() != getWidth())
			debugCanvas.setWidth(getWidth());
		if(debugCanvas.getHeight() != getHeight())
			debugCanvas.setHeight(getHeight());
	
		horizontalScrollBar.setPrefHeight(10);
		verticalScrollBar.setPrefWidth(10);
		
		horizontalScrollBar.setLayoutY(getHeight() - 15);
		verticalScrollBar.setLayoutX(getWidth() - 15);
		
		horizontalScrollBar.setPrefWidth(getWidth() - verticalScrollBar.getWidth());
		verticalScrollBar.setPrefHeight(getHeight() - horizontalScrollBar.getHeight());
		//refreshOverlay();
	}

	public ColumnView getActiveColumnView() {
		return columnViews.get(0);
	}
	
	public void changeZoom(double zoomFactor) {
		this.zoomFactor = zoomFactor;
	}

	public double getZoomFactor() {
		return zoomFactor;
	}

	public void setGuiFacade(DocModifyScreenGuiFacade docModifyScreenGuiFacade) {
		this.guiFacade = docModifyScreenGuiFacade;
	}
	
	@Override
	public void notifyRepaintNeeded() {
		refresh();
	}
	
	@Override
	public void notifyModificationStart(ModificationType type, ShapedPane pane,	MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyMouseMovement(ShapedPane pane, MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyModificationEnd(ShapedPane pane, MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GraphicsContext getGraphicsContext() {
		return overlayContext;
	}

	public static GraphicsContext getDebugContext() {
		return selfReference.debugContext;
	}
	
	public void setOverlayCanvasVisible(boolean value) {
		this.isOverlayCanvasVisible = value;
	}

	public void setTextCanvasVisible(boolean value) {
		this.isTextCanvasVisible = value;
	}
	
	public boolean isOverlayCanvasVisible() {
		return this.isOverlayCanvasVisible;
	}
	
	public boolean isTextCanvasVisible() {
		return this.isTextCanvasVisible;
	}

	public void refocusTextField() {
		guiFacade.refocusTextField();
	}

	public void setLinePolygonsVisible(boolean value) {
		this.areLinePolygonsVisible = value;
	}

	public boolean getLinePolygonsVisible() {
		return areLinePolygonsVisible;
	}

	public void setInsetVisible(boolean value) {
		this.isPageInsetVisible = value;
	}
	
	public boolean isInsetVisible() {
		return isPageInsetVisible;
	}

	public ArrayList<ColumnView> getColumnViews() {
		return columnViews;
	}

	public ShapeDrawFacade getShapeDrawFacade() {
		return guiFacade.getShapeDrawFacade();
	}

	public int getActiveColumnIndex() {
		// TODO Auto-generated method stub
		return 0;
	}
}
