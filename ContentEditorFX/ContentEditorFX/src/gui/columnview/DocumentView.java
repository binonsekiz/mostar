package gui.columnview;

import java.util.ArrayList;
import java.util.Random;

import settings.GlobalAppSettings;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javafx.util.Duration;
import document.Column;
import document.Document;
import event.DocModifyScreenGuiFacade;
import event.input.OverlayCanvas;
import event.modification.ModificationType;
import geometry.libgdxmath.MathUtils;
import gui.ShapedPane;

/**
 * This is a pane that has a corresponding document attached.
 * It is a collection of column views.
 * @author sahin
 *
 */
public class DocumentView extends Pane implements CanvasOwner{
	
	private DocModifyScreenGuiFacade guiFacade;
	
	private ScrollPane scrollPane;
	private Group scrollContent;
	private GridPane gridPane;
	
	private ArrayList<ColumnView> columnViews;
	private double zoomFactor;
	
	private OverlayCanvas overlayCanvas;
	private GraphicsContext overlayContext;
	
	private Document document;
	private DocumentView selfReference;

	private boolean isOverlayCanvasVisible;
	private boolean isTextCanvasVisible;
	private boolean isDebugPointsVisible;

	private boolean isRefreshInProgress;

	public DocumentView(){
		selfReference = this;
		columnViews = new ArrayList<ColumnView>();
		initGui();
		initEvents();	
		
		debug();
	}
	
	private void initGui() {
		this.setClip(new Rectangle(0,0,this.getWidth(), this.getHeight()));
		this.setId("docmodify-pane");
		
		scrollPane = new ScrollPane();
		scrollContent = new Group();
		scrollPane.setContent(scrollContent);
		
		Pane pane = new Pane();
		gridPane = new GridPane();
		gridPane.setVgap(20);
		gridPane.setHgap(20);

		overlayCanvas = new OverlayCanvas(this);
		overlayContext = overlayCanvas.getGraphicsContext2D();
		pane.setLayoutX(0);
		pane.setLayoutY(0);
		overlayCanvas.setLayoutX(0);
		overlayCanvas.setLayoutY(0);
		fixCanvasSize();
		
		isOverlayCanvasVisible = true;
		isTextCanvasVisible = true;
		isDebugPointsVisible = true;
		isRefreshInProgress = false;
		zoomFactor = 1;	
		
		pane.getChildren().addAll(gridPane, overlayCanvas);
		scrollContent.getChildren().add(pane);
		this.getChildren().addAll(scrollPane/*, overlayCanvas*/);
		scrollPane.toFront();
		overlayCanvas.toFront();
	}
	
	private void initEvents(){
		this.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				setClip(new Rectangle(0,0,getWidth(), getHeight()));
				scrollPane.setPrefSize(getWidth(), getHeight());
				fixCanvasSize();
			}
		});
		
		this.heightProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				setClip(new Rectangle(0,0,getWidth(), getHeight()));
				scrollPane.setPrefSize(getWidth(), getHeight());
				fixCanvasSize();
			}
		});		
		
		scrollPane.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				refresh();
			}
		});
		
		scrollPane.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				refresh();
			}
		});
	}

	public void associateWithDocument(Document document) {
		this.document = document;
		initialPopulate();
	}

	private void initialPopulate() {
		for(int i = 0; i < document.getColumns().size(); i++){
			Column tempColumn = document.getColumns().get(i);
			ColumnView tempColumnView = new ColumnView(this, guiFacade.getTextModifyFacade());
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
			    }
			}));
			timer.play();
			isRefreshInProgress = false;
		}
	}

	private void refreshAll() {
		for(int i = 0; i < columnViews.size(); i++){
			columnViews.get(i).refresh();
		}
		
		guiFacade.notifyRefreshHappened();
		fixCanvasSize();
		overlayCanvas.toFront();
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
			    }
			}));
			timer.play();
			isRefreshInProgress = false;
		}
	}

	protected void refreshAllOverlay() {
		for(int i = 0; i < columnViews.size(); i++){
			columnViews.get(i).refreshOverlayCanvas();
		}
		
		guiFacade.notifyRefreshHappened();
		fixCanvasSize();
		overlayCanvas.toFront();
	}

	private void fixCanvasSize() {
		if(overlayCanvas.getWidth() != scrollPane.getViewportBounds().getWidth())
			overlayCanvas.setWidth(scrollPane.getViewportBounds().getWidth());
		if(overlayCanvas.getHeight() != scrollPane.getViewportBounds().getHeight())
			overlayCanvas.setHeight(scrollPane.getViewportBounds().getHeight());
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
	
	private void debug(){
//		scrollPane.setId("scrollpane-custom");
//		scrollContent.setId("scrollpane-content");
//		this.setId("docmodify-pane");
	}

	@Override
	public GraphicsContext getGraphicsContext() {
		return overlayContext;
	}

	public void setOverlayCanvasVisible(boolean value) {
		this.isOverlayCanvasVisible = value;
	}

	public void setTextCanvasVisible(boolean value) {
		this.isTextCanvasVisible = value;
	}
	
	public void setDebugPointsVisible(boolean value) {
		this.isDebugPointsVisible = value;
	}
	
	public boolean isOverlayCanvasVisible() {
		return this.isOverlayCanvasVisible;
	}
	
	public boolean isTextCanvasVisible() {
		return this.isTextCanvasVisible;
	}
	
	public boolean isDebugPointsVisible() {
		return this.isDebugPointsVisible;
	}

	public void setDebugText(String value) {
		columnViews.get(0).setDebugText(value);
	}

	public Affine getOverlayContextTransformForChild(ColumnView columnView) {
		return null;
	}

	public void refocusTextField() {
		guiFacade.refocusTextField();
	}

}
