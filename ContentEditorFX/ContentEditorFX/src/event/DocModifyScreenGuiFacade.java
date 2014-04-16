package event;

import gui.columnview.DocumentView;
import gui.columnview.DocumentView.ScrollMode;
import gui.docmodify.DocBottomToolbar;
import gui.docmodify.DocDebugView;
import gui.docmodify.DocOverview;
import gui.docmodify.DocVersatilePane;
import gui.docmodify.DocWidgetToolbar;
import gui.helper.DebugHelper;
import gui.storage.FilePickerWrapper;
import gui.storage.FilePickerWrapper.FilePickerType;
import gui.threed.ThreeDModifyScreen;
import gui.widget.WidgetModifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import settings.Translator;
import storage.RepositoryManager;
import control.Caret;
import control.StyleModifyFacade;
import control.TextModifyFacade;
import control.ThreeDEventFacade;
import control.WidgetModifyFacade;
import document.Column;
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
	private ThreeDModifyScreen tdModifyScreen;
	private DebugHelper debugHelper;
	private FilePickerWrapper filePicker;
	private RepositoryManager repositoryManager;
	
	private TextModifyFacade textModifyFacade;
	private WidgetModifyFacade widgetModifyFacade;
	private StyleModifyFacade styleModifyFacade;
	private ShapeDrawFacade shapeDrawFacade;
	private ThreeDEventFacade threeDEventFacade;
	
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
		threeDEventFacade = new ThreeDEventFacade();
		repositoryManager = new RepositoryManager();
		filePicker = new FilePickerWrapper(this, repositoryManager);
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
		threeDEventFacade.setDocumentAndView(document, documentView);
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
		int activeColumnIndex = documentView.getActiveColumnIndex();
		Column newColumn = new Column(document.getMeasurement(), document.getPageInsets());
		document.addColumn(newColumn, activeColumnIndex);
		documentView.addColumn(newColumn, activeColumnIndex);
		documentView.requestFocus();
	}

	public void addImageWidgetPressed() {
	//	docModifyPane.getPageView(docBottomToolbar.getActivePage()).addEmptyImage();
	//TODO: temp solution
//		docModifyPane.getActivePageView().addEmptyImage();
		widgetModifyFacade.addImageWidget();
		documentView.refresh();
		documentView.requestFocus();
	}

	public void addHtmlWidgetPressed() {
	//	docModifyPane.getPageView(docBottomToolbar.getActivePage()).addWebView();
	//TODO: temp solution
//		docModifyPane.getActivePageView().addWebView();
		widgetModifyFacade.addWebViewWidget();
		documentView.refresh();
		documentView.requestFocus();
	}

	public void addMediaWidgetPressed() {
		// TODO Auto-generated method stub
//		docModifyPane.getActivePageView().addMediaView();
		widgetModifyFacade.addMediaViewWidget();
		documentView.refresh();
		documentView.requestFocus();
	}
	
	public void addImageGalleryWidgetPressed() {
		// TODO Auto-generated method stub
//		docModifyPane.getActivePageView().addImageGallery();
		widgetModifyFacade.addImageGalleryWidget();
		documentView.refresh();
		documentView.requestFocus();
	}
	
	public void addThreeDWidgetPressed() {
		// TODO Auto-generated method stub
		widgetModifyFacade.addThreeDWidgetPressed();
		documentView.refresh();
		documentView.requestFocus();
	}
	
	public void addTextBoxPressed() {
		// TODO Auto-generated method stub
		documentView.requestFocus();
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
	    documentView.requestFocus();
	}
	
	public void changeVersatilePane(WidgetModifier modifier){
		docVersatilePane.show(modifier);
		documentView.requestFocus();
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

	public void setOverlayCanvasVisible(boolean value) {
		documentView.setOverlayCanvasVisible(value);
		documentView.requestFocus();
	}

	public void setTextCanvasVisible(boolean value) {
		documentView.setTextCanvasVisible(value);
		documentView.requestFocus();
	}

	public void requestDocumentViewRefresh() {
		documentView.refresh();
	}

	public void setLinePolygonsVisible(boolean value) {
		documentView.setLinePolygonsVisible(value);
		documentView.requestFocus();
	}

	public void setInsetVisible(boolean value) {
		documentView.setInsetVisible(value);
		documentView.requestFocus();
	}

	public void changeFontName(String arg2) {
		styleModifyFacade.changeFontName(arg2);
		documentView.requestFocus();
	}

	public void changeFontSize(String arg2) {
		styleModifyFacade.changeFontSize(Double.parseDouble(arg2));
		documentView.requestFocus();
	}

	public void debugResetTextIndices() {
		document.getDocumentText().debugValidateAllTextLines();
		documentView.requestFocus();
	}

	public void updateVisualStyleControls() {
		docWidgetToolbar.updateVisualStyleControls();
		documentView.requestFocus();
	}

	public void drawShapeButtonPressed() {
		shapeDrawFacade.changeShapeDrawingMode(ShapeDrawingMode.PolygonDrawing);
		documentView.requestFocus();
	}

	public ShapeDrawFacade getShapeDrawFacade() {
		return shapeDrawFacade;
	}

	public ThreeDEventFacade getThreeDEventFacade() {
		return threeDEventFacade;
	}

	public void newButtonPressed() {
		System.out.println("new button");
		documentView.requestFocus();
	}

	public void saveButtonPressed() {
		System.out.println("save button");
		documentView.requestFocus();
		filePicker.open(FilePickerType.SaveDocument);
	}

	public void loadButtonPressed() {
		System.out.println("load button");
		documentView.requestFocus();
		filePicker.open(FilePickerType.OpenDocument);
	}

	public void setDocumentViewScrollBehaviour(ScrollMode mode) {
		documentView.setScrollBehaviour(mode);
	}

	public void setDebugCanvasVisible(boolean b) {
		documentView.setDebugCanvasVisible(b);
	}

	

}
