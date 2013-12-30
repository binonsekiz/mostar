package gui.columnview;

import java.util.ArrayList;
import java.util.Random;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
	
	public DocumentView(){
		selfReference = this;
		columnViews = new ArrayList<ColumnView>();
		initGui();
		initEvents();

		zoomFactor = 1;		
		
		debug();
	}

	private void debug(){
//		scrollPane.setId("scrollpane-custom");
//		scrollContent.setId("scrollpane-content");
//		this.setId("docmodify-pane");
	}
	
	private void initGui() {
		this.setClip(new Rectangle(0,0,this.getWidth(), this.getHeight()));
		this.setId("docmodify-pane");
		
		scrollPane = new ScrollPane();
		scrollContent = new Group();
		scrollPane.setContent(scrollContent);
		
		gridPane = new GridPane();
		gridPane.setVgap(20);
		gridPane.setHgap(20);
		scrollContent.getChildren().add(gridPane);
		overlayCanvas = new OverlayCanvas(this);
		overlayContext = overlayCanvas.getGraphicsContext2D();
		fixCanvasSize();
		scrollPane.toFront();
		overlayCanvas.toFront();
		this.getChildren().addAll(scrollPane, overlayCanvas);
		
	}
	
	private void initEvents(){
		this.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				setClip(new Rectangle(0,0,getWidth(), getHeight()));
				scrollPane.setPrefSize(getWidth(), getHeight());
			}
		});
		
		this.heightProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				setClip(new Rectangle(0,0,getWidth(), getHeight()));
				scrollPane.setPrefSize(getWidth(), getHeight());
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
			ColumnView tempColumnView = new ColumnView(this);
			tempColumnView.associateWithColumn(tempColumn);
			tempColumnView.setStyledText(document.getStyledText());
			columnViews.add(tempColumnView);
			gridPane.add(tempColumnView, i, 0);
		}
		refresh();
	}

	public void refresh() {
		//TODO: increase perf by not refreshing everything
	//	for(int i = 0; i < columnViews.size(); i++){
	//		columnViews.get(i).refresh();
	//	}
		fixCanvasSize();
		overlayContext.setStroke(Color.BLACK);
		overlayContext.setFill(Color.BLUE);
		System.out.println("DOC VIEW REFRESH");
		overlayContext.setLineWidth(3);
		overlayContext.strokeOval(250,250,100,100);
		overlayContext.fillRect(250,250,100,100);
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

	@Override
	public GraphicsContext getGraphicsContext() {
		return overlayContext;
	}
}
