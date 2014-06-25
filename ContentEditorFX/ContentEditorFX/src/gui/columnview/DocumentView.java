package gui.columnview;

import gui.ShapedPane;
import gui.helper.CustomScrollPane;
import gui.helper.CustomScrollPane.ScrollMode;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import settings.GlobalAppSettings;
import document.Column;
import document.Document;
import document.project.ProjectRepository;
import event.modification.ModificationType;

/**
 * This is a pane that has a corresponding document attached.
 * It is a collection of column views.
 * @author sahin
 *
 */
public class DocumentView extends Pane implements CanvasOwner{
	
	private CustomScrollPane continuousScrollPane;
	private ArrayList<ColumnView> columnViews;
	private double zoomFactor;
	
	private GraphicsContext overlayContext;
	private Document document;
	private static DocumentView selfReference;

	private boolean isOverlayCanvasVisible;
	private boolean isTextCanvasVisible;
	private boolean isRefreshInProgress;
	private boolean areLinePolygonsVisible;
	private boolean isPageInsetVisible;

	protected double offsetX;
	protected double offsetY;

	private Rectangle debugBox;

	public DocumentView(){
		selfReference = this;
		columnViews = new ArrayList<ColumnView>();
		initGui();
		initEvents();	
		System.out.println("Document view initialized");
	}
	
	public void setOverlayContext(GraphicsContext overlayContext) {
		this.overlayContext = overlayContext;
	}
	
	public GraphicsContext getOverlayContext() {
		return overlayContext;
	}

	private void initGui() {
		continuousScrollPane = new CustomScrollPane();
		continuousScrollPane.setContent(columnViews);
		
		debugBox = new Rectangle(0, 0, 4, 4);
		
		isOverlayCanvasVisible = true;
		isTextCanvasVisible = true;
		isRefreshInProgress = false;
		areLinePolygonsVisible = true;
		zoomFactor = 1;	
		
		this.getChildren().add(continuousScrollPane);
	}
	
	private void initEvents(){
		this.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				continuousScrollPane.setMinWidth(widthProperty().get());
				setClip(new Rectangle(0,0,getWidth(), getHeight()));
			}
		});
		
		this.heightProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				continuousScrollPane.setMinHeight(heightProperty().get());
				setClip(new Rectangle(0,0,getWidth(), getHeight()));
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
		ProjectRepository.getActiveProjectEnvironment().updateVisualStyleControls();
	}
	
	public void associateWithDocument(Document document) {
		if(this.document != null) {
			removeDocument();
		}
		this.document = document;
		initialPopulate();
	}

	public void addColumn(Column c, int index) {
		ColumnView newView = new ColumnView(this, ProjectRepository.getActiveProjectEnvironment().getTextModifyFacade(), ProjectRepository.getActiveProjectEnvironment().getShapeDrawFacade());
		newView.associateWithColumn(c);
		newView.setDocumentText(document.getDocumentText());
		columnViews.add(index, newView);
		
		continuousScrollPane.validateContent();
	}
	
	private void removeDocument() {
		columnViews.clear();
		continuousScrollPane.validateContent();
	}
	
	private void initialPopulate() {
		for(int i = 0; i < document.getColumns().size(); i++){
			Column tempColumn = document.getColumns().get(i);
			ColumnView tempColumnView = new ColumnView(this, ProjectRepository.getActiveProjectEnvironment().getTextModifyFacade(), ProjectRepository.getActiveProjectEnvironment().getShapeDrawFacade());
			tempColumnView.associateWithColumn(tempColumn);
			tempColumnView.setDocumentText(document.getDocumentText());
			columnViews.add(tempColumnView);
		}
		continuousScrollPane.validateContent();
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
		
		ProjectRepository.getActiveProjectEnvironment().notifyRefreshHappened();
	//	fixCanvasSize();
	}

	@Override
	public void notifyOverlayRepaintNeeded() {
		refreshOverlay();
	}
	
	public void refreshOverlay() {
        refreshAllOverlay();
	}

	protected void refreshAllOverlay() {
		Bounds bounds = localToScene(this.getBoundsInLocal());
		overlayContext.clearRect(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
		
		Bounds visibleRectangle = getVisibleViewportBounds();
		
		for(int i = 0; i < columnViews.size(); i++){
			if(columnViews.get(i).getBoundsInParent().intersects(visibleRectangle)) {
				columnViews.get(i).refreshOverlayCanvas();
			}
		}
		
		ProjectRepository.getActiveProjectEnvironment().notifyRefreshHappened();
	}

	/*private void fixCanvasSize() {
		if(!isFixCanvasSizeActive) {
			isFixCanvasSizeActive = true;
			Timeline timer = new Timeline(new KeyFrame(Duration.millis(GlobalAppSettings.fastDeviceFrameMillis), new EventHandler<ActionEvent>(){
				@Override
			    public void handle(ActionEvent event) {
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
	*/
	public double getOverlayOffsetX() {
		return this.getBoundsInParent().getMinX();
	}

	public double getOverlayOffsetY() {
		return this.getBoundsInParent().getMinY();
	}
	
	public ColumnView getActiveColumnView() {
		return columnViews.get(0);
	}
	
	public void changeZoom(double zoomFactor) {
		continuousScrollPane.setZoomFactor(zoomFactor);
	}

	public double getZoomFactor() {
		return zoomFactor;
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

	public int getActiveColumnIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setScrollBehaviour(ScrollMode mode) {
		continuousScrollPane.setScrollMode(mode);
	}

	public double getOverlayScale() {
		return zoomFactor;
	}
	
	public Bounds getVisibleViewportBounds() {
	//	BoundingBox retVal = new BoundingBox(offsetX * -1 / zoomFactor, offsetY * -1 / zoomFactor, continuousScrollPane.getViewportBounds().getWidth() / zoomFactor, continuousScrollPane.getViewportBounds().getHeight() / zoomFactor);
		return continuousScrollPane.getVisibleViewportBounds();
	}
}
