//package zzzzdeprecated;
//
//import java.util.ArrayList;
//
//import javafx.animation.ScaleTransition;
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
//import javafx.event.EventHandler;
//import javafx.scene.Group;
//import javafx.scene.SnapshotParameters;
//import javafx.scene.control.ScrollPane;
//import javafx.scene.image.Image;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.input.ScrollEvent;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.Pane;
//import javafx.scene.shape.Rectangle;
//import javafx.util.Duration;
//import document.Column;
//import document.Document;
//import event.ProjectEnvironment;
//import gui.columnview.ColumnView;
//
///**
// * This is the pane where the user modifies the current doc. 
// * It is the view equivalent of "Document" class
// * 
// * @author sahin
// *
// */
//@Deprecated
//public class DocModifyPane extends Pane{
//
//	private ProjectEnvironment guiFacade;
//	
//	private double zoomFactor;
//	
////	private ArrayList<ColumnViewPane> pageViewPanes;
//	private ArrayList<ColumnView> pageViewPanes;
//	private Group scrollContent;
//	private ScrollPane scrollPane;
//	
////	private FlowPane singlePageLayout; 	//Single page view
//	private GridPane gridPane;
//	
//	private Document document;
////	private int activePageStart;
//	
//	public DocModifyPane(){
//		initializeResources();
//		initializeLooks();
//		initializeEvents();
//		refresh();
//		this.toBack();
//	}
//
//	private void initializeResources() {
//	//	guiFacade = DocModifyScreenGuiFacade.instance;
//		zoomFactor = 1;
//		pageViewPanes = new ArrayList<ColumnView>();
//	}
//
//	private void initializeLooks() {
//		this.setClip(new Rectangle(0,0,this.getWidth(), this.getHeight()));
//		this.setId("docmodify-pane");
//		
//		scrollPane = new ScrollPane();
//		scrollContent = new Group();
//		scrollPane.setContent(scrollContent);
//		
//		scrollPane.setId("scrollpane-custom");
//		scrollContent.setId("scrollpane-content");
//		
//		gridPane = new GridPane();
//		gridPane.setVgap(20);
//		gridPane.setHgap(20);
//		
//		this.getChildren().add(scrollPane);
//	}
//	
//	private void initializeEvents() {
//		this.setOnKeyTyped(new EventHandler<KeyEvent>(){
//			@Override
//			public void handle(KeyEvent t) {
//				//TODO: implement
//			}
//		});
//		
//		this.setOnMouseEntered(new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		
//		this.setOnMousePressed(new EventHandler<MouseEvent>(){
//			@Override
//			public void handle(MouseEvent arg0) {
//				//TODO: handle widgets losing focus
//			}
//		});
//		
//		this.widthProperty().addListener(new ChangeListener<Number>() {
//			@Override
//			public void changed(ObservableValue<? extends Number> arg0,
//					Number arg1, Number arg2) {
//				setClip(new Rectangle(0,0,getWidth(), getHeight()));
//				scrollPane.setPrefSize(getWidth(), getHeight());
//			}
//		});
//		
//		this.heightProperty().addListener(new ChangeListener<Number>(){
//			@Override
//			public void changed(ObservableValue<? extends Number> arg0,
//					Number arg1, Number arg2) {
//				setClip(new Rectangle(0,0,getWidth(), getHeight()));
//				scrollPane.setPrefSize(getWidth(), getHeight());
//			}
//		});
//		
//		this.setOnScroll(new EventHandler<ScrollEvent>(){
//			@Override
//			public void handle(ScrollEvent arg0) {
//				moveToPage(1);
//			}
//		});
//		
//		scrollPane.hvalueProperty().addListener(new ChangeListener<Number>(){
//			@Override
//			public void changed(ObservableValue<? extends Number> arg0,
//					Number arg1, Number arg2) {
//				double minX = scrollPane.getViewportBounds().getMinX();
//				
//				//convert minX to find active page
//			//	activePageStart = findColumnWithStartCoordinate(minX);
//			}
//		});
//	}
//	
//	protected int  findColumnWithStartCoordinate(double minX) {
//		double widthSoFar = 0;
//		minX = -1 * minX;
//		for(int i = 0; i < pageViewPanes.size(); i++){
//			widthSoFar += pageViewPanes.get(i).getWidth() + gridPane.getHgap();
//			if(widthSoFar > minX)
//				return i;
//		}
//		return pageViewPanes.size() -1;
//	}
//
//	public void associateWithDocument(Document doc){
//		this.document = doc;
//	//	activePageStart = 0;
//		refresh();
//	}
//
//	public void setGuiFacade(ProjectEnvironment guiFacade) {
//		this.guiFacade = guiFacade;
//	}
//
//	public void changeZoom(double newZoomFactor) {
//		newZoomFactor = newZoomFactor/100;
//		ScaleTransition st = new ScaleTransition(Duration.millis(200), gridPane);
//		st.setFromX(zoomFactor);
//		st.setFromY(zoomFactor);
//		st.setToX(newZoomFactor);
//		st.setToY(newZoomFactor);
//		st.play();
//		
//		this.zoomFactor = newZoomFactor;
//	}
//	
//	public void addPageView(Column page){
////		ColumnViewPane pane = new ColumnViewPane(page);
//	//	TextModifyFacade.instance.addColumnViewPane(pane);
////		pageViewPanes.add(pane);
//		refresh();
//	}
//	
//	public void refresh(){
//	/*	pageViewPanes.clear();
//		if(document != null){
//			//for (int i= Math.max(0, activePageStart - 1); i < document.getColumns().size() && i < activePageStart + 4; i++){
//			for(int i = 0; i < document.getColumns().size(); i++){	
//				pageViewPanes.add(new ColumnViewPane(document.getColumns().get(i)));
//			}
//		}*/
//		
//
//
//		scrollContent.getChildren().clear();
//		gridPane.getChildren().clear();
////		if(pageViewPanes.size() > 0)
////			gridPane.getChildren().addAll(pageViewPanes);
//		for(int i = 0; i < pageViewPanes.size(); i++){
//			gridPane.getChildren().add(pageViewPanes.get(i));
//			GridPane.setConstraints(pageViewPanes.get(i), (i+1), 1);
//		}
//		
//		scrollContent.getChildren().add(gridPane);
//	}
//
//
//    public double getZoomFactor() {
//		return zoomFactor;
//	}
//
//	public void moveToPage(int index) {
//		if(document == null || index >= pageViewPanes.size()) return;
//		activePageViewBorder(index);
//	/*	PageChangeTransition transition = new PageChangeTransition(scrollPane, pageViewPanes, gridPane.getHgap());
//		transition.setDuration(Duration.millis(300));
//		transition.fromPageToPage(DocSelectionModel.instance.getActiveOverallColumnIndex(), index);
//		transition.play();	*/	
//	}
//	
//	private void activePageViewBorder(int index){
//		for(int i = 0; i < pageViewPanes.size(); i++){
//			pageViewPanes.get(i).setId("columnview-disabled");
//		}
//		pageViewPanes.get(index).setId("columnview-selected");
//	}
//
//
//	public Image takeColumnSnapshot(int columnCounter) {
//		return pageViewPanes.get(columnCounter).snapshot(new SnapshotParameters(), null);
//	}
//
//	public void clearColumnPanesText() {
//	
//	}
//}
