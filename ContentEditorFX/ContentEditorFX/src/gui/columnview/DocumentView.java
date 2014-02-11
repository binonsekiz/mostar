package gui.columnview;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
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

	private boolean isRefreshInProgress;

	private boolean areLinePolygonsVisible;

	private boolean isPageInsetVisible;

	public DocumentView(){
		selfReference = this;
		columnViews = new ArrayList<ColumnView>();
		initGui();
		initEvents();	
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
		isRefreshInProgress = false;
		areLinePolygonsVisible = true;
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

	public LineOnCanvas getLineThatIncludesIndex(int index) {
		int columnIndex = 0;
		int paragraphIndex = 0;
		int lineIndex = 0;
		
//		System.out.println("\n\n################\nQuerying with " + index);
		
		for(int i = 0; i < columnViews.size(); i++) {
//			System.out.println("Looking at column " + i + ", has start index: " + columnViews.get(i).getStartIndex() + ", end index: " + columnViews.get(i).getEndIndex());
			if(index >= columnViews.get(i).getStartIndex() && index <= columnViews.get(i).getEndIndex()) {
				columnIndex = i;
//				System.out.println("Found in column " + i);
				break;
			}
		}
		
		ArrayList<ParagraphOnCanvas> paragraphs = columnViews.get(columnIndex).getParagraphsOnCanvas();
		for(int i = 0; i < paragraphs.size(); i++) {
//			System.out.println("Looking at paragraph " + i + ", has start index: " + paragraphs.get(i).getStartIndex() + ", end index: " + paragraphs.get(i).getEndIndex());
			if(index >= paragraphs.get(i).getStartIndex() && index <= paragraphs.get(i).getEndIndex()) {
				paragraphIndex = i;
//				System.out.println("Found in paragraph " + i);
				break;
			}
		}
		
		ArrayList<LineOnCanvas> lines = paragraphs.get(paragraphIndex).getLinesOnCanvas();
		for(int i = 0; i < lines.size(); i++) {
//			System.out.println("Looking at line " + i + ", has start index: " + lines.get(i).getStartIndex() + ", end index: " + lines.get(i).getEndIndex());
			if(index >= lines.get(i).getStartIndex() && index <= lines.get(i).getEndIndex()) {
				lineIndex = i;
//				System.out.println("Found in lines " + i);
				return lines.get(lineIndex);
			}
		}
		
		//if not returned so far, find the last modified place
		return null;	
	}
	
	public void textSelectionSet(int lowerIndex, int higherIndex) {
		LineOnCanvas.selectedStartIndex = lowerIndex;
		LineOnCanvas.selectedEndIndex = higherIndex;
	/*	int columnIndex = 0;
		int paragraphIndex = 0;
		int lineIndex = 0;
		
		for(int i = 0; i < columnViews.size(); i++) {
			if(lowerIndex > columnViews.get(i).getStartIndex() && lowerIndex < columnViews.get(i).getEndIndex()) {
				columnIndex = i;
				break;
			}
		}
		
		ArrayList<ParagraphOnCanvas> paragraphs = columnViews.get(columnIndex).getParagraphsOnCanvas();
		for(int i = 0; i < paragraphs.size(); i++) {
			if(lowerIndex > paragraphs.get(i).getStartIndex() && lowerIndex < paragraphs.get(i).getEndIndex()) {
				paragraphIndex = i;
				break;
			}
		}
		
		ArrayList<LineOnCanvas> lines = paragraphs.get(paragraphIndex).getLinesOnCanvas();
		for(int i = 0; i < lines.size(); i++) {
			if(lowerIndex > lines.get(i).getStartIndex() && lowerIndex < lines.get(i).getEndIndex()) {
				lineIndex = i;
				lines.get(i).setSelectedIndex(lowerIndex, higherIndex);
				break;
			}
		}
		
		//System.out.println("Found first index at: " + columnIndex + ", " + paragraphIndex + ", " + lineIndex);
		
		int lastSetEndIndex = 0;
		
		//now we'll select all the lines until 
		for(int i = columnIndex; i < columnViews.size(); i++) {
			for(int j = paragraphIndex; j < paragraphs.size(); j++){
				for(int k = lineIndex; k < lines.size(); k++){
					if(higherIndex < lines.get(k).getEndIndex() || lines.get(k).getEndIndex() == lastSetEndIndex) {
						break;
					}
				//	System.out.println("Found second index at: " + i + ", " + j + ", " + k);
					lines.get(k).setSelectedIndex(lowerIndex, higherIndex);
					lastSetEndIndex = lines.get(k).getEndIndex();
				}
				if(higherIndex < paragraphs.get(j).getEndIndex()) {
					break;
				}
			}
			if(higherIndex < columnViews.get(i).getEndIndex()){
				break;
			}
		}*/
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
	//	TextModifyFacade textModifyFacade = guiFacade.getTextModifyFacade();
	//	textModifyFacade.getCaret().drawCaret(overlayContext);
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
	
	public boolean isOverlayCanvasVisible() {
		return this.isOverlayCanvasVisible;
	}
	
	public boolean isTextCanvasVisible() {
		return this.isTextCanvasVisible;
	}

	public Affine getOverlayContextTransformForChild(ColumnView columnView) {
		return null;
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

}
