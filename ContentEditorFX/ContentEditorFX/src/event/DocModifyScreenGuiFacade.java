package event;

import gui.columnview.DocumentView;
import gui.docmodify.DocBottomToolbar;
import gui.docmodify.DocDebugView;
import gui.docmodify.DocOverview;
import gui.docmodify.DocVersatilePane;
import gui.docmodify.DocWidgetToolbar;
import gui.helper.DebugHelper;
import gui.widget.WidgetModifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import settings.Translator;
import control.Caret;
import control.StyleModifyFacade;
import control.TextModifyFacade;
import control.WidgetModifyFacade;
import document.Document;
import event.ShapeDrawFacade.ShapeDrawingMode;
import event.input.KeyboardManager;

public class DocModifyScreenGuiFacade {

	private DocumentView documentView;
	private DocWidgetToolbar docWidgetToolbar;
	private DocOverview docOverview;
	private DocBottomToolbar docBottomToolbar;
	private DocVersatilePane docVersatilePane;
	private DocDebugView docDebugView;
	private DebugHelper debugHelper;
	
	private TextModifyFacade textModifyFacade;
	private WidgetModifyFacade widgetModifyFacade;
	private StyleModifyFacade styleModifyFacade;
	private ShapeDrawFacade shapeDrawFacade;
	
	private Caret caret;
	
	private Document document;
	
	public DocModifyScreenGuiFacade(DocumentView documentView, DocWidgetToolbar docWidgetToolbar, DocOverview docOverview, DocBottomToolbar docBottomToolbar, DocVersatilePane docVersatilePane, DocDebugView docDebugView) {
		this.documentView = documentView;
		this.docWidgetToolbar = docWidgetToolbar;
		this.docOverview = docOverview;
		this.docBottomToolbar = docBottomToolbar;
		this.docVersatilePane = docVersatilePane;
		this.docDebugView = docDebugView;
		this.debugHelper = new DebugHelper();
			
		textModifyFacade = new TextModifyFacade();
		widgetModifyFacade = new WidgetModifyFacade(this);
		styleModifyFacade = new StyleModifyFacade();
		shapeDrawFacade = new ShapeDrawFacade();
		caret = new Caret(textModifyFacade);
		textModifyFacade.setCaret(caret);
		KeyboardManager.instance.setTextFacade(textModifyFacade);
		
		documentView.setGuiFacade(this);
		docWidgetToolbar.setGuiFacade(this);
		docOverview.setGuiFacade(this);
		docVersatilePane.setGuiFacade(this);
		docDebugView.setGuiFacade(this);
		
		widgetModifyFacade.setCaret(caret);
		styleModifyFacade.setCaret(caret);
		styleModifyFacade.setTextModifyFacade(textModifyFacade);
		docWidgetToolbar.setStyleFacade(styleModifyFacade);
	}
	
	public void createNewDocument(){
		document = new Document();
		textModifyFacade.setDocumentAndView(document, documentView);
		widgetModifyFacade.setDocumentAndView(document, documentView);
		styleModifyFacade.setDocumentAndView(document, documentView);
		docBottomToolbar.setGuiFacade(this);
		documentView.associateWithDocument(document);
		docOverview.populateTreeView();
	}
	
	public Document getDocument(){
		return document;
	}

	public void addSectionPressed(){

	}
	
	public void addChapterPressed(){

	}
	
	public void addColumnPressed(){

	}

	public void addImageWidgetPressed() {
	//	docModifyPane.getPageView(docBottomToolbar.getActivePage()).addEmptyImage();
	//TODO: temp solution
//		docModifyPane.getActivePageView().addEmptyImage();
		widgetModifyFacade.addImageWidget();
		documentView.refresh();
	}

	public void addHtmlWidgetPressed() {
	//	docModifyPane.getPageView(docBottomToolbar.getActivePage()).addWebView();
	//TODO: temp solution
//		docModifyPane.getActivePageView().addWebView();
		widgetModifyFacade.addWebViewWidget();
		documentView.refresh();
	}

	public void addMediaWidgetPressed() {
		// TODO Auto-generated method stub
//		docModifyPane.getActivePageView().addMediaView();
		widgetModifyFacade.addMediaViewWidget();
		documentView.refresh();
	}
	
	public void addImageGalleryWidgetPressed() {
		// TODO Auto-generated method stub
//		docModifyPane.getActivePageView().addImageGallery();
		widgetModifyFacade.addImageGalleryWidget();
		documentView.refresh();
	}
	
	public void addTextBoxPressed() {
		// TODO Auto-generated method stub
		
	}
	
	public void changeActiveColumn(int activeColumn) {
//		docModifyPane.moveToPage(activeColumn);
	}
	
	public void signInAsAGuestPressed(){
		
	}
	
	public void pageBackgroundPressed(){
		FileChooser chooser = new FileChooser();
	    chooser.setTitle(Translator.get("Open Background Image"));
	    chooser.setInitialDirectory(new File("res/background"));
	    File file = chooser.showOpenDialog(null);
	    
	    if(file != null){
	    	FileInputStream fis;
			try {
				fis = new FileInputStream(file);
				Image image = new Image(fis);
//		    	docModifyPane.getActivePageView().setBackground(image);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
	    }
	}
	
	public void changeVersatilePane(WidgetModifier modifier){
		docVersatilePane.show(modifier);
	}

	public void documentPaneZoomChanged(double zoomFactor) {
		documentView.changeZoom(zoomFactor);
	}
	
	public double getZoomFactor(){
		return documentView.getZoomFactor();
	}
	
	public void notifyRefreshHappened(){
		docDebugView.refreshCountProperty().set(docDebugView.refreshCountProperty().get() + 1);
	}

	public void clearVersatilePane() {
		docVersatilePane.hideAll();
	}

	public void updateOverview() {
		docOverview.populateTreeView();
	}

	public TextModifyFacade getTextModifyFacade() {
		return textModifyFacade;
	}

	public void setGuiDebugText(String string) {
		docBottomToolbar.setDebugString(string);
	}

	public void setOverlayCanvasVisible(boolean value) {
		documentView.setOverlayCanvasVisible(value);
	}

	public void setTextCanvasVisible(boolean value) {
		documentView.setTextCanvasVisible(value);
	}

	public void requestDocumentViewRefresh() {
		documentView.refresh();
	}

/*	public void debugAssignText(String value) {
		documentView.setDebugText(value);
		documentView.refresh();
	}
*/
	public void refocusTextField() {
		docBottomToolbar.refocusOnTextField();
	}

	public void setLinePolygonsVisible(boolean value) {
		documentView.setLinePolygonsVisible(value);
	}

	public void setInsetVisible(boolean value) {
		documentView.setInsetVisible(value);
	}

	public void changeFontName(String arg2) {
		styleModifyFacade.changeFontName(arg2);
	}

	public void changeFontSize(String arg2) {
		styleModifyFacade.changeFontSize(Double.parseDouble(arg2));
	}

	public void debugResetTextIndices() {
		document.getDocumentText().debugValidateAllTextLines();
	}

	public void updateVisualStyleControls() {
		docWidgetToolbar.updateVisualStyleControls();
	}

	public void drawShapeButtonPressed() {
		shapeDrawFacade.changeShapeDrawingMode(ShapeDrawingMode.PolygonDrawing);
	}

	public ShapeDrawFacade getShapeDrawFacade() {
		return shapeDrawFacade;
	}

}
