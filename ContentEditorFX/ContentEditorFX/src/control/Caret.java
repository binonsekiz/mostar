package control;

import geometry.libgdxmath.Interpolation;
import geometry.libgdxmath.Vector2;
import gui.columnview.ColumnView;
import gui.columnview.LineOnCanvas;
import gui.docmodify.DocDebugView;
import gui.helper.MathHelper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import settings.GlobalAppSettings;
import document.Column;
import document.DocumentText;
import document.Paragraph;
import document.style.TextStyle;

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
					textModifyFacade.getLineViewWithIndex(caretIndex).getColumnView().getDocumentView().refreshOverlay();
				}
		    }
		}));
		caretTimer.setCycleCount(-1);
		caretTimer.play();
		
		caretMovementTimer = new Timeline(new KeyFrame(Duration.millis(GlobalAppSettings.fastDeviceFrameMillis), new EventHandler<ActionEvent>(){
			@Override
		    public void handle(ActionEvent event) {
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
					textModifyFacade.getLineViewWithIndex(caretIndex).getColumnView().getDocumentView().refreshOverlay();
				}
		    }
		}));
		caretMovementTimer.setCycleCount(-1);
		
		caretIndex = 0;
		anchor = 0;
	}
	
	private void calculateCaretLine(int index) {
		this.caretIndex = index;
		this.activeLineView = textModifyFacade.getLineViewWithIndex(index);

		caretParagraph = documentText.getParagraphThatIncludesIndex(caretIndex);
		Vector2 caretPos = activeLineView.getLetterPosition(caretIndex - activeLineView.getStartIndex());
		anchor = caretIndex;
		anchorParagraph = caretParagraph;
		anchorLineView = activeLineView;
				
		setVisualPosition(caretPos.x, caretPos.y);
		isCaretVisible = true;
		
		textModifyFacade.textSelectionSet(caretIndex, anchor);
		caretTimer.playFromStart();
		
		DocDebugView.instance.setDebugText("Caret Index: " + caretIndex, 2);
		DocDebugView.instance.setDebugText("Anchor Index: " + anchor, 3);

		activeLineView.getColumnView().refresh();
	}
	
	private void calculateAnchorLine(int anchorIndex) {
		this.anchor = anchorIndex;
		this.anchorLineView = textModifyFacade.getLineViewWithIndex(anchorIndex);
		
		anchorParagraph = documentText.getParagraphThatIncludesIndex(anchor);

		textModifyFacade.textSelectionSet(caretIndex, anchor);
		
		DocDebugView.instance.setDebugText("Caret Index: " + caretIndex, 2);
		DocDebugView.instance.setDebugText("Anchor Index: " + anchor, 3);
		
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
	
	public void drawCaret(double xOffset, double yOffset, GraphicsContext context) {
		if(!isCaretVisible) return;
		if(anchor == caretIndex) {
			context.setStroke(Color.BLACK);
			context.setLineWidth(1);
			if(caretParagraph != null ) {
				float lineAngle = caretParagraph.getParagraphSet().getAngle();
				TextStyle style = caretParagraph.getStyle();
				context.strokeLine(screenX + xOffset, screenY + yOffset, screenX + xOffset - style.getLineSpacingHeight() * Math.sin(Math.toRadians(lineAngle)), screenY + yOffset + style.getLineSpacingHeight() * Math.cos(Math.toRadians(lineAngle)));
			}
		}
		else{
			//don't draw the blinking caret as there is a selection
		}
	}

	public Paragraph getActiveParagraph() {
		return caretParagraph;
	}

	public ColumnView getActiveColumnView() {
		return activeLineView.getColumnView();
	}
	
	public LineOnCanvas getActiveLineView() {
		return activeLineView;
	}

	/**
	 * This sets both caret and anchor
	 * @param index
	 */
	public void setCaretIndex(int index) {
		calculateCaretLine(index);
	}
	
	public void setAnchorIndex(int index) {
		calculateAnchorLine(index);
	}

	public void insertString(String text) {
		if(caretParagraph == null) caretParagraph = documentText.getParagraph(0);
		
		if(caretIndex == anchor) {
			caretParagraph.insertText(text, caretIndex);
		}
		else {
			caretParagraph.insertText(text, Math.min(caretIndex, anchor), Math.max(caretIndex,anchor));
		}
		//getActiveColumnView().refresh();
		caretParagraph.validateLineOnCanvases(getActiveColumnView());
		setCaretIndex(Math.min(caretIndex, anchor) + text.length());
	}

	public void leftKey(boolean shiftDown, boolean controlDown) {
		int indexCountToMove = -1;
		if(controlDown) {
	//		indexCountToMove = documentText.getRelativeWordStartIndexBefore(caretIndex);
		}
		
		setCaretIndexRelative(indexCountToMove);
		if(shiftDown) {
			setAnchorIndexRelative(-1 * indexCountToMove);
		}
	}

	public void rightKey(boolean shiftDown, boolean controlDown) {
		int indexCountToMove = 1;
		if(controlDown) {
	//		indexCountToMove = documentText.getRelativeWordStartIndexBefore(caretIndex);
		}

		setCaretIndexRelative(indexCountToMove);
		if(shiftDown) {
			setAnchorIndexRelative(-1 * indexCountToMove);
		}
	}

	public void changeMousePointer(Cursor cursorType) {
		textModifyFacade.changeMousePointer(cursorType);
	}

	public boolean isSelectionStyleEquals(TextStyle style) {
		if(caretIndex == anchor && caretParagraph.getStyle().isEqual(style))
			return true;
	
		//iterate over paragraphs to see if all of them have the same style
		for(int i = Math.min(caretIndex, anchor); i <= Math.max(caretIndex, anchor); i++){
			if(!documentText.getStyleAt(i).isEqual(style))
				return false;
		}
		
		return true;
	}

	public boolean isAtEnd() {
		if(caretIndex == anchor && caretIndex == documentText.getEndIndex())
			return true;
		else return false;
	}

	public void setAnchorIndexRelative(int i) {
		setAnchorIndex(MathHelper.clamp(0, anchor + i, documentText.getEndIndex()));
	}
	
	public void setCaretIndexRelative(int i) {
		setCaretIndex(MathHelper.clamp(0, caretIndex + i, documentText.getEndIndex()));
	}

	public int getSelectionStart() {
		return Math.min(caretIndex, anchor);
	}

	public int getSelectionEnd() {
		return Math.max(caretIndex, anchor);
	}
}
