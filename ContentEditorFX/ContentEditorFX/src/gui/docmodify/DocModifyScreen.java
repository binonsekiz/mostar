package gui.docmodify;

import event.DocModifyScreenGuiFacade;
import gui.GFrame.WindowType;
import gui.ScreenType;
import gui.columnview.DocumentView;
import gui.helper.StyleRepository;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class DocModifyScreen extends BorderPane implements ScreenType{

	private DocumentView documentView;
	private DocWidgetToolbar docWidgetToolbar;
	private DocOverview docOverview;
	private DocBottomToolbar docBottomToolbar;
	private DocVersatilePane docVersatilePane;
	private DocDebugView docDebugView;
	
	private DocModifyScreenGuiFacade guiFacade;
	private StyleRepository styleRepository;
	private WindowType referrer;
	
	public DocModifyScreen(){
		initGui();
		initEvents();
	}
	
	private void initGui(){
		docWidgetToolbar = new DocWidgetToolbar();
		docOverview = new DocOverview();
		
		documentView = new DocumentView();
		docBottomToolbar = new DocBottomToolbar();		
		docVersatilePane = new DocVersatilePane();
		docDebugView = new DocDebugView();
		guiFacade = new DocModifyScreenGuiFacade(documentView, docWidgetToolbar, docOverview, docBottomToolbar, docVersatilePane, docDebugView);
		styleRepository = new StyleRepository();
		
		//TODO: for demo use only
		VBox toolbarBox = new VBox();
		toolbarBox.getChildren().addAll(docWidgetToolbar);
		
//		this.setId("docmodify-screen");
		
		this.setTop(toolbarBox);
		this.setLeft(docDebugView);
		this.setCenter(documentView);
		this.setRight(docVersatilePane);
		this.setBottom(docBottomToolbar);

	}
	
	private void initEvents(){
	/*	this.widthProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number oldValue, Number newValue) {
				docModifyPane.setPrefWidth((Double) newValue - docOverview.getWidth() - docVersatilePane.getWidth());
			}
		});
		
		this.heightProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				docModifyPane.setPrefHeight((Double) newValue);
			}
		});*/
	}
	
	public DocModifyScreenGuiFacade getGuiFacade(){
		return guiFacade;
	}

	@Override
	public WindowType getType() {
		return WindowType.DocModifyScreen;
	}

	@Override
	public void setReferrer(WindowType referrer) {
		this.referrer = referrer;
	}
	
}
