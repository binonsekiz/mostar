package gui.columnview;

import java.util.ArrayList;
import java.util.Random;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
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
import event.input.CustomMouseHandler;
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
public class DocumentView extends Pane implements CanvasOwner, CustomMouseHandler{
	
	private DocModifyScreenGuiFacade guiFacade;
	
	private ScrollPane scrollPane;
	private Group scrollContent;
	private GridPane gridPane;
	
	private ArrayList<ColumnView> columnViews;
	private double zoomFactor;
	
	private OverlayCanvas overlayCanvas;
	private GraphicsContext overlayContext;
	
	private Document document;
	
	public DocumentView(){
		columnViews = new ArrayList<ColumnView>();
		initGui();
		initEvents();

		zoomFactor = 1;		
		
		debug();
	}

	private void debug(){
		
	}
	
	private void initGui() {
		this.setClip(new Rectangle(0,0,this.getWidth(), this.getHeight()));
		this.setId("docmodify-pane");
		
		scrollPane = new ScrollPane();
		scrollContent = new Group();
		scrollPane.setContent(scrollContent);
		
		scrollPane.setId("scrollpane-custom");
		scrollContent.setId("scrollpane-content");
		
		gridPane = new GridPane();
		gridPane.setVgap(20);
		gridPane.setHgap(20);
		overlayCanvas = new OverlayCanvas(this);
		overlayContext = overlayCanvas.getGraphicsContext2D();
		this.setId("docmodify-pane");
		scrollContent.getChildren().add(gridPane);
		overlayCanvas.setLayoutX(0);
		overlayCanvas.setLayoutY(0);
		overlayCanvas.setWidth(800);
		overlayCanvas.setHeight(600);
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
		
		if(overlayCanvas.getWidth() != this.getWidth()){
			System.out.println("fixing width: " + overlayCanvas.getWidth());
			overlayCanvas.setWidth(this.getWidth());
		}
		if(overlayCanvas.getHeight() != this.getHeight()){
			System.out.println("fixing height: " + overlayCanvas.getHeight());
			overlayCanvas.setHeight(this.getHeight());
		}

		overlayContext.setStroke(Color.BLACK);
		overlayContext.setFill(Color.BLUE);
		System.out.println("DOC VIEW REFRESH");
		overlayContext.setLineWidth(3);
		overlayContext.strokeOval(250,250,100,100);
		overlayContext.fillOval(250,250,100,100);
		overlayCanvas.toFront();
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

	public ColumnView getActiveColumnView() {
		return columnViews.get(0);
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

	private ColumnView getColumnViewForMouseEvent(MouseEvent event) {
		for(int i = 0; i < columnViews.size(); i++) {
			ColumnView view = columnViews.get(i);
			Bounds bounds = view.getBoundsInParent();
			if(bounds.contains(event.getX(), event.getY())){
				return view;
			}
		}
		return null;
	}
	
	private void passEventToColumnView(MouseEvent event, MouseEventType type) {
		ColumnView view = getColumnViewForMouseEvent(event);
		if(view == null) return;
		switch (type){
		case MouseClicked: 
			view.onMouseClicked(event); break;
		case MouseDragEntered:
			view.onMouseDragEntered(event); break;
		case MouseDragExited:
			view.onMouseDragExited(event); break;
		case MouseDragged:
			view.onMouseDragged(event); break;
		case MouseDragOver:
			view.onMouseDragOver(event); break;
		case MouseDragReleased:
			view.onMouseDragReleased(event); break;
		case MouseEntered:
			view.onMouseEntered(event); break;
		case MouseExited:
			view.onMouseExited(event); break;
		case MouseMoved:
			view.onMouseMoved(event); break;
		case MousePressed:
			view.onMousePressed(event); break;
		case MouseReleased:
			view.onMouseReleased(event); break;
		default: break;
		}
	}
	
	@Override
	public void onMouseClicked(MouseEvent event) {
		passEventToColumnView(event, MouseEventType.MouseClicked);
	}

	@Override
	public void onMouseEntered(MouseEvent event) {
		passEventToColumnView(event, MouseEventType.MouseEntered);
	}

	@Override
	public void onMouseExited(MouseEvent event) {
		passEventToColumnView(event, MouseEventType.MouseExited);
	}

	@Override
	public void onMouseMoved(MouseEvent event) {
		passEventToColumnView(event, MouseEventType.MouseMoved);
	}

	@Override
	public void onMousePressed(MouseEvent event) {
		passEventToColumnView(event, MouseEventType.MousePressed);
	}

	@Override
	public void onMouseDragEntered(MouseEvent event) {
		passEventToColumnView(event, MouseEventType.MouseDragEntered);
	}

	@Override
	public void onMouseDragExited(MouseEvent event) {
		passEventToColumnView(event, MouseEventType.MouseDragExited);
	}

	@Override
	public void onMouseDragOver(MouseEvent event) {
		passEventToColumnView(event, MouseEventType.MouseDragOver);
	}

	@Override
	public void onMouseDragReleased(MouseEvent event) {
		passEventToColumnView(event, MouseEventType.MouseDragReleased);
	}

	@Override
	public void onMouseDragged(MouseEvent event) {
		passEventToColumnView(event, MouseEventType.MouseDragged);
	}

	@Override
	public void onMouseReleased(MouseEvent event) {
		passEventToColumnView(event, MouseEventType.MouseReleased);
	}

}
