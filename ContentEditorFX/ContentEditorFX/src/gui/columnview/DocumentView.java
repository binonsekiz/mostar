package gui.columnview;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
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
	
	private ScrollPane continuousScrollPane;
	private BorderPane discreteScrollPane;
	private ScrollBar discreteSlider;
	private ScrollMode activeScrollMode;
	private StackPane gridStack;
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

	private double gridHGap;
	private double gridVGap;
	private double gridStackWidth;
	private double gridStackHeight;

	protected double offsetX;
	protected double offsetY;

	private boolean isFixCanvasSizeActive;

	private boolean isDebugCanvasVisible;

	private ScaleTransition transition;
	private Group contentGroup;
	private Group zoomGroup;
	
	public DocumentView(){
		selfReference = this;
		columnViews = new ArrayList<ColumnView>();
		initGui();
		initEvents();	
		System.out.println("Document view initialized");
	}

	private void initGui() {
		this.setId("docmodify-pane");

		continuousScrollPane = new ScrollPane() {
			@Override
			public void requestFocus() {
				//do nothing
			}
		};
		discreteScrollPane = new BorderPane();
		discreteSlider = new ScrollBar();
		
		//this is to prevent gridpane to layout its children on top of each other
		gridStack = new StackPane();
		gridStack.setId("red-pane");
		
		gridPane = new GridPane();
		gridHGap = GlobalAppSettings.gridHGap;
		gridVGap = GlobalAppSettings.gridVGap;
		gridPane.setVgap(gridVGap);
		gridPane.setHgap(gridHGap);
		gridStack.getChildren().add(gridPane);
		gridStack.setAlignment(Pos.CENTER_LEFT);
				
		overlayCanvas = new OverlayCanvas(this);
		overlayContext = overlayCanvas.getGraphicsContext2D();
		overlayCanvas.setLayoutX(0);
		overlayCanvas.setLayoutY(0);
		overlayCanvas.setId("scenechange-pane");
		
		debugCanvas = new OverlayCanvas(this);
		debugContext = debugCanvas.getGraphicsContext2D();
		debugCanvas.setLayoutX(0);
		debugCanvas.setLayoutY(0);
		fixCanvasSize();
		
		contentGroup = new Group();
		zoomGroup = new Group();
		zoomGroup.getChildren().add(gridStack);
		contentGroup.getChildren().add(zoomGroup);
	
		transition = new ScaleTransition(Duration.millis(200), zoomGroup);
		
		isOverlayCanvasVisible = true;
		isTextCanvasVisible = true;
		isRefreshInProgress = false;
		areLinePolygonsVisible = true;
		isFixCanvasSizeActive = false;
		zoomFactor = 1;	
		
		activeScrollMode = GlobalAppSettings.defaultDocumentViewScrollMode;
		if(activeScrollMode == ScrollMode.Continuous) {
			initContinuousScroll();
		}
		else if(activeScrollMode == ScrollMode.Discrete) {
			initDiscreteScroll();
		}

		this.getChildren().addAll(overlayCanvas, debugCanvas);
		overlayCanvas.toFront();
		debugCanvas.toFront();
	}
	
	private void initContinuousScroll() {
		continuousScrollPane.setContent(contentGroup);
		continuousScrollPane.setFocusTraversable(false);
		continuousScrollPane.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		continuousScrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		continuousScrollPane.setFitToHeight(true);
		continuousScrollPane.setFitToWidth(true);
		setFocusTraversable(true);
		
		this.getChildren().add(continuousScrollPane);
	}
	
	private void initDiscreteScroll() {
		discreteScrollPane.setCenter(gridStack);
		gridStack.setLayoutX(0);
		gridStack.setLayoutY(0);
		discreteScrollPane.setBottom(discreteSlider);
		
		this.getChildren().add(discreteScrollPane);
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
		
		continuousScrollPane.hvalueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				refreshOverlay();
			}
		});
		
		continuousScrollPane.vvalueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				refreshOverlay();
			}
		});
	}

	protected void recalculateOffsets() {
		//offsetX = -1 * (gridStackWidth - continuousScrollPane.getViewportBounds().getWidth()) * continuousScrollPane.getHvalue()/* + zoomGroup.getBoundsInParent().getMinX()*/;
		//offsetY = (gridStackHeight - continuousScrollPane.getViewportBounds().getHeight()) * continuousScrollPane.getVvalue()/*+ zoomGroup.getBoundsInParent().getMinY()*/;
	
		offsetX = -1 * ((continuousScrollPane.getHvalue() - continuousScrollPane.getHmin()) / (continuousScrollPane.getHmax() - continuousScrollPane.getHmin())) 
				* (zoomGroup.getBoundsInParent().getWidth() - continuousScrollPane.getViewportBounds().getWidth());
		offsetY = ((continuousScrollPane.getVvalue() - continuousScrollPane.getVmin()) / (continuousScrollPane.getVmax() - continuousScrollPane.getVmin())) 
				* (zoomGroup.getBoundsInParent().getHeight() - continuousScrollPane.getViewportBounds().getHeight());
		
		System.out.println("Offset X: " + offsetX + ", offset y: " + offsetY);
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
		gridStackWidth = 0;
		gridStackHeight = 0;
		
		for(int i = 0; i < columnViews.size(); i++) {
			gridPane.add(columnViews.get(i), i, 0);
			gridStackWidth = gridStackWidth + columnViews.get(i).getWidth() + gridHGap;
			if(columnViews.get(i).getHeight() + gridVGap > gridStackHeight)
				gridStackHeight = columnViews.get(i).getHeight() + gridVGap;
		}
		
		gridStack.setMinWidth(gridStackWidth);
		gridStack.setPrefWidth(gridStackWidth);
		gridStack.setMaxWidth(gridStackWidth);
		gridStack.setMinHeight(gridStackHeight);
		gridStack.setPrefHeight(gridStackHeight);
		gridStack.setMaxHeight(gridStackHeight);
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
        refreshAllOverlay();
	}

	protected void refreshAllOverlay() {
		recalculateOffsets();
		overlayContext.clearRect(0, 0, overlayCanvas.getWidth(), overlayCanvas.getHeight());
		
		Bounds visibleRectangle = getVisibleViewportBounds();
		
		for(int i = 0; i < columnViews.size(); i++){
			if(columnViews.get(i).getBoundsInParent().intersects(visibleRectangle)) {
				columnViews.get(i).refreshOverlayCanvas();
			}
		}
		
		guiFacade.notifyRefreshHappened();
		overlayCanvas.toFront();
		debugCanvas.toFront();
	}

	private void fixCanvasSize() {
		if(!isFixCanvasSizeActive) {
			isFixCanvasSizeActive = true;
			Timeline timer = new Timeline(new KeyFrame(Duration.millis(GlobalAppSettings.fastDeviceFrameMillis), new EventHandler<ActionEvent>(){
				@Override
			    public void handle(ActionEvent event) {
					overlayCanvas.setWidth(getWidth() - 25);
					overlayCanvas.setHeight(getHeight() - 25);
					debugCanvas.setWidth(getWidth() - 25);
					debugCanvas.setHeight(getHeight() - 25);

					gridStack.setLayoutX(0);
					gridStack.setLayoutY(0);

					if(activeScrollMode == ScrollMode.Continuous) {
						continuousScrollPane.setMinWidth(getWidth());
						continuousScrollPane.setMinHeight(getHeight());
						continuousScrollPane.setPrefWidth(getWidth());
						continuousScrollPane.setPrefHeight(getHeight());
						continuousScrollPane.setMaxWidth(getWidth());
						continuousScrollPane.setMaxHeight(getHeight());
					}
					else if(activeScrollMode == ScrollMode.Discrete) {
						discreteScrollPane.setMinWidth(getWidth());
						discreteScrollPane.setMinHeight(getHeight());
						discreteScrollPane.setPrefWidth(getWidth());
						discreteScrollPane.setPrefHeight(getHeight());
						discreteScrollPane.setMaxWidth(getWidth());
						discreteScrollPane.setMaxHeight(getHeight());
					}
					
					refreshOverlay();
					isFixCanvasSizeActive = false;
				}}
			));
			timer.play();
		}
	}
	
	public double getOverlayOffsetX() {
		return offsetX;
	}

	public double getOverlayOffsetY() {
		return offsetY;
	}
	
	public ColumnView getActiveColumnView() {
		return columnViews.get(0);
	}
	
	public void changeZoom(double zoomFactor) {
		zoomFactor = zoomFactor / 100.0;
		
		double oldFactor = this.zoomFactor;
		System.out.println("old factor: " + oldFactor + ", new factor: " + zoomFactor);
		transition.setFromX(oldFactor);
		transition.setFromY(oldFactor);
		transition.setToX(zoomFactor);
		transition.setToY(zoomFactor);
		transition.setCycleCount(1);
		transition.setAutoReverse(false);
		transition.play();
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
		if(isOverlayCanvasVisible) {
			overlayCanvas.setOpacity(1);
		}
		else{
			overlayCanvas.setOpacity(0);
		}
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

	public enum ScrollMode {
		Continuous,
		Discrete
	}

	public void setScrollBehaviour(ScrollMode mode) {
		if(activeScrollMode != mode) {
			activeScrollMode = mode;
			//update layout
		}
	}

	public void setDebugCanvasVisible(boolean b) {
		isDebugCanvasVisible = b;
		if(isDebugCanvasVisible) {
			debugCanvas.setOpacity(1);
		}
		else{
			debugCanvas.setOpacity(0);
		}
	}

	public double getOverlayScale() {
		return zoomFactor;
	}
	
	public Bounds getVisibleViewportBounds() {
		BoundingBox retVal = new BoundingBox(offsetX * -1, offsetY * -1, continuousScrollPane.getViewportBounds().getWidth() / zoomFactor, continuousScrollPane.getViewportBounds().getHeight() / zoomFactor);
		return retVal;
	}
}
