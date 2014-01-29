package event;

import gui.columnview.DocumentView;
import gui.docmodify.DocBottomToolbar;
import gui.docmodify.DocDebugView;
import gui.docmodify.DocOverview;
import gui.docmodify.DocVersatilePane;
import gui.docmodify.DocWidgetToolbar;
import gui.widget.WidgetModifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import settings.Translator;
import control.Caret;
import control.TextModifyFacade;
import control.WidgetModifyFacade;
import document.Document;

public class DocModifyScreenGuiFacade {

	private DocumentView documentView;
	private DocWidgetToolbar docWidgetToolbar;
	private DocOverview docOverview;
	private DocBottomToolbar docBottomToolbar;
	private DocVersatilePane docVersatilePane;
	private DocDebugView docDebugView;
	
	private TextModifyFacade textModifyFacade;
	private WidgetModifyFacade widgetModifyFacade;
	
	private Caret caret;
	
	private Document document;
	
	public DocModifyScreenGuiFacade(DocumentView documentView, DocWidgetToolbar docWidgetToolbar, DocOverview docOverview, DocBottomToolbar docBottomToolbar, DocVersatilePane docVersatilePane, DocDebugView docDebugView) {
		this.documentView = documentView;
		this.docWidgetToolbar = docWidgetToolbar;
		this.docOverview = docOverview;
		this.docBottomToolbar = docBottomToolbar;
		this.docVersatilePane = docVersatilePane;
		this.docDebugView = docDebugView;
			
		textModifyFacade = new TextModifyFacade();
		widgetModifyFacade = new WidgetModifyFacade(this);
		caret = new Caret(textModifyFacade);
		
		documentView.setGuiFacade(this);
		docWidgetToolbar.setGuiFacade(this);
		docOverview.setGuiFacade(this);
		docVersatilePane.setGuiFacade(this);
		docDebugView.setGuiFacade(this);
		
		textModifyFacade.setCaret(caret);
		widgetModifyFacade.setCaret(caret);
	}
	
	public void createNewDocument(){
		document = new Document();
		documentView.associateWithDocument(document);
		docOverview.populateTreeView();
		textModifyFacade.setDocumentAndView(document, documentView);
		widgetModifyFacade.setDocumentAndView(document, documentView);
		docBottomToolbar.setGuiFacade(this);
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

	/*public void addLinePressed() {
		// TODO temp solution
		documentView.getActivePageView().addLine();
	}

	public void newBookPressed() {
		createNewDocument();
	}*/

/*	public void demoVideo1() {
		// TODO Auto-generated method stub
		File file = new File("res\\media\\turbo.mp4");
		try {
			docModifyPane.getActivePageView().addDemoMediaView(file.toURI().toURL().toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void demoVideo2() {
		// TODO Auto-generated method stub
		File file = new File("res\\media\\dna.mp4");
		try {
			docModifyPane.getActivePageView().addDemoMediaView(file.toURI().toURL().toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void demoInteractive1() {
		// TODO Auto-generated method stub
		File f = new File("res\\interactive\\index - Copy.html");
		try {
			docModifyPane.getActivePageView().addDemoWebView(f.toURI().toURL().toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void demoInteractive2() {
		// TODO Auto-generated method stub
		docModifyPane.getActivePageView().addDemoWebView("http://www.google.com");
	}
*/

	public void clearVersatilePane() {
		docVersatilePane.hideAll();
	}

	public Image takeSnapshot(int columnCounter) {
		// TODO Auto-generated method stub
//		return docModifyPane.takeColumnSnapshot(columnCounter);
		return null;
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

	public void setDebugPointsVisible(boolean value) {
		documentView.setDebugPointsVisible(value);
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

}
