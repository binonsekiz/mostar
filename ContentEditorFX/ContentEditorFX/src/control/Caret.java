package control;

import geometry.libgdxmath.Interpolation;
import geometry.libgdxmath.Vector2;
import gui.columnview.ColumnView;
import gui.columnview.LineOnCanvas;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import settings.GlobalAppSettings;
import document.Column;
import document.DocumentText;
import document.Paragraph;
import document.TextStyle;

public class Caret{
	//reference to the text
	private DocumentText documentText;
	private TextModifyFacade textModifyFacade;
	
	//index relative to styled text start.
	private int caretIndex;
	private Paragraph caretParagraph;
	 
	//this is absolute caret index.
	private int anchor;
	private Paragraph anchorParagraph;
	
	//visual positions
	private float startX;
	private float startY;
	private float screenX;
	private float screenY;
	private float destinationX;
	private float destinationY;
	
	private LineOnCanvas activeLineView;
	private LineOnCanvas anchorLineView;
	protected boolean isCaretVisible;
	
	private Interpolation interpolation = Interpolation.pow2;
	private float totalDestination;
	private Timeline caretTimer;
	private Timeline caretMovementTimer;
	
	public Caret(TextModifyFacade facade){
		this.textModifyFacade = facade;
		caretIndex = 0;
		anchor = 0;
		initialSetup();
	}
	
	private void initialSetup() {
		//TODO: make it so that the caret starts from saved position
		caretTimer = new Timeline(new KeyFrame(Duration.millis(GlobalAppSettings.caretBlinkRate), new EventHandler<ActionEvent>(){
			@Override
		    public void handle(ActionEvent event) {
				isCaretVisible = !isCaretVisible;				
				if(caretParagraph!= null && caretIndex == anchor) {
					textModifyFacade.getLineViewWithIndex(caretIndex).getColumnView().refreshOverlayCanvas();
				}
		    }
		}));
		caretTimer.setCycleCount(-1);
		caretTimer.play();
		
		caretMovementTimer = new Timeline(new KeyFrame(Duration.millis(GlobalAppSettings.fastDeviceFrameMillis), new EventHandler<ActionEvent>(){
			@Override
		    public void handle(ActionEvent event) {
			//	System.out.println("Start x: " + startX + ", y: " + startY + ", Destination x: " + destinationX + ", y: " + destinationY + ", Screen x: " + screenX + ", y:" + screenY);
				totalDestination += GlobalAppSettings.fastDeviceFrameMillis;
				screenX = interpolation.apply(startX, destinationX, totalDestination / GlobalAppSettings.caretMovementTime);
				screenY = interpolation.apply(startY, destinationY, totalDestination / GlobalAppSettings.caretMovementTime);
				if(totalDestination > GlobalAppSettings.caretMovementTime){
					screenX = destinationX;
					screenY = destinationY;
					caretMovementTimer.stop();
					totalDestination = 0;
				}
				if(caretParagraph!= null && caretIndex == anchor) {
					textModifyFacade.getLineViewWithIndex(caretIndex).getColumnView().refreshOverlayCanvas();
				}
		    }
		}));
		caretMovementTimer.setCycleCount(-1);
	}
	
	private void calculateCaretLine(int index){
		this.caretIndex = index;
		this.activeLineView = textModifyFacade.getLineViewWithIndex(index);
		caretParagraph = activeLineView.getParentParagraph();
		textModifyFacade.textSelectionSet(caretIndex, anchor);
		Vector2 caretPos = activeLineView.getLetterPosition(caretIndex);
		
		System.out.println("Setting visual position to " + caretPos);
		setVisualPosition(caretPos.x, caretPos.y);
		isCaretVisible = true;
		caretTimer.playFromStart();
		
		activeLineView.getColumnView().refresh();
	}
	
	private void calculateAnchorLine(int index){
		this.anchor = index;
		this.anchorLineView = textModifyFacade.getLineViewWithIndex(index);
		anchorParagraph = anchorLineView.getParentParagraph();
		textModifyFacade.textSelectionSet(caretIndex, anchor);
		
		anchorLineView.getColumnView().refresh();
	}
	
	public int getCaretIndex(){
		return caretIndex;
	}
	
	public int getAnchor(){
		return anchor;
	}
	
	public void setVisualPosition(float x, float y){
		totalDestination = 0;
		startX = destinationX;
		startY = destinationY;
		this.destinationX = x;
		this.destinationY = y;
		caretMovementTimer.playFromStart();
	}

	public Column getActiveColumn() {
		return activeLineView.getColumnView().getColumn();
	}

	public void setDocumentText(DocumentText documentText) {
		this.documentText = documentText;
	}
	
	public boolean isCaretOnParagraph(Paragraph paragraph) {
		return caretParagraph == paragraph;
	}
	
	public void drawCaret(GraphicsContext context) {
		if(!isCaretVisible) return;
		if(anchor == caretIndex) {
			context.setStroke(Color.BLACK);
			context.setLineWidth(1);
			if(caretParagraph != null ) {
				float lineAngle = caretParagraph.getAngle();
				TextStyle style = caretParagraph.getStyle();
				context.strokeLine(screenX, screenY, screenX + style.getLineSpacingHeight() * Math.sin(Math.toRadians(lineAngle)), screenY + style.getLineSpacingHeight() * Math.cos(Math.toRadians(lineAngle)));
			}
		}
		else{
			//iterate over lines to draw selection
			textModifyFacade.textSelectionSet(caretIndex, anchor);
		}
	}

	public Paragraph getActiveParagraph() {
		return caretParagraph;
	}

	public ColumnView getActiveColumnView() {
		return activeLineView.getColumnView();
	}

	public void setCaretIndex(int index) {
		this.caretIndex = index;
		calculateCaretLine(index);
	}
	
	public void setAnchorIndex(int index) {
		this.anchor = index;
		calculateAnchorLine(anchor);
	}
	
}
