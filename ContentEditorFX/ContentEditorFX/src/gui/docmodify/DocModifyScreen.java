package gui.docmodify;

import document.project.ProjectRepository;
import event.input.OverlayCanvas;
import gui.GFrame.WindowType;
import gui.ScreenType;
import gui.columnview.DocumentView;
import gui.popup.LoginPopup;
import gui.popup.WelcomePopup;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import network.NetworkFacade;
import settings.GlobalAppSettings;

public class DocModifyScreen extends StackPane implements ScreenType{

	private BorderPane borderPane;
	private DocumentView documentView;
	private DocWidgetToolbar docWidgetToolbar;
	private DocBottomToolbar docBottomToolbar;
	private DocVersatilePane docVersatilePane;
	private DocDebugView docDebugView;
	
	private OverlayCanvas overlayCanvas;
	private GraphicsContext overlayContext;
	
	private WelcomePopup welcomePopup;
	private LoginPopup loginPopup;
	
	@SuppressWarnings("unused")
	private WindowType referrer;
	
	private boolean isOverlayCanvasVisible;
	
	public DocModifyScreen(){
		new ProjectRepository();
		new NetworkFacade();
		initGui();
		initEvents();
	}
	
	private void initEvents() {
		this.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				resizeOverlayCanvas();
			}
		});
		this.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				resizeOverlayCanvas();
			}
		});
	}

	protected void resizeOverlayCanvas() {
		overlayCanvas.setWidth(this.getWidth());
		overlayCanvas.setHeight(this.getHeight());
	}

	private void initGui(){
		borderPane = new BorderPane();
		
		docWidgetToolbar = new DocWidgetToolbar();
		
		documentView = new DocumentView();
		docBottomToolbar = new DocBottomToolbar();		
		docVersatilePane = new DocVersatilePane();
		docDebugView = new DocDebugView();
		
		borderPane.setTop(docWidgetToolbar);
		borderPane.setCenter(documentView);
		borderPane.setBottom(docBottomToolbar);

		overlayCanvas = new OverlayCanvas();
		overlayContext = overlayCanvas.getGraphicsContext2D();
		
		overlayContext.setFill(Color.ORANGE);
		overlayContext.fillRect(0, 0, 100, 100);
		overlayCanvas.setLayoutX(0);
		overlayCanvas.setLayoutY(0);
		
		isOverlayCanvasVisible = true;
		documentView.setOverlayContext(overlayContext);
		
		if(GlobalAppSettings.bypassLogin){
			welcomePopup = new WelcomePopup();
			welcomePopup.show();
		}
		else{
			loginPopup = new LoginPopup();
			loginPopup.show();
		}
		resizeOverlayCanvas();
		this.getChildren().addAll(borderPane, overlayCanvas);
		overlayCanvas.toFront();
	}
	
	@Override
	public WindowType getType() {
		return WindowType.DocModifyScreen;
	}

	@Override
	public void setReferrer(WindowType referrer) {
		this.referrer = referrer;
	}

	public void toggleDebugPaneVisible(boolean value) {
		if(value) {
			borderPane.setLeft(docDebugView);
		}
		else{
			borderPane.setLeft(null);
		}
	}
	
	public void toggleVersatilePaneVisible(boolean value) {
		if(value) {
			borderPane.setRight(docVersatilePane);
		}
		else{
			borderPane.setRight(null);
		}
	}

	public DocumentView getDocumentView() {
		return documentView;
	}

	public DocWidgetToolbar getWidgetToolbar() {
		return docWidgetToolbar;
	}

	public DocBottomToolbar getDocBottomToolbar() {
		return docBottomToolbar;
	}

	public DocVersatilePane getDocVersatilePane() {
		return docVersatilePane;
	}

	public DocDebugView getDocDebugView() {
		return docDebugView;
	}

	public void setOverlayCanvasVisible(boolean value) {
		this.isOverlayCanvasVisible = value;
		if(isOverlayCanvasVisible) {
			overlayCanvas.setOpacity(1);
		}
		else{
			overlayCanvas.setOpacity(0);
		}
	}
}
