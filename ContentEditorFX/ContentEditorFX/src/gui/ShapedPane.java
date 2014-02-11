package gui;

import event.modification.ModificationType;
import geometry.GeometryHelper;
import geometry.libgdxmath.Polygon;
import geometry.libgdxmath.Rectangle;
import geometry.libgdxmath.Vector2;
import gui.columnview.CanvasOwner;
import gui.columnview.VisualView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public abstract class ShapedPane extends Pane implements VisualView {
	private Polygon shape;
	
	private Rectangle relocationRectangle;
	private final float relocationRectangleOffsetX = 5;
	private final float relocationRectangleOffsetY = 5;
	
	private Rectangle[] resizeRectangles;
	private Vector2[] resizeVectors;
	
	private Color strokeColor;
	private Color fillColor;
	private Color edgeColor;
	private Color relocateBoxColor;
	private Color resizeBoxColor;
	private double lineWidth;
	
	private boolean isMousePositionVisible;
	private Vector2 mousePosition;
	private boolean isSnapPositionVisible;
	private Vector2 snapPosition;
	private boolean isMouseInside;
	
	private boolean isResizable;
	private boolean isResizeHappening;
	protected int resizeIndex;
	private boolean isRelocatable;
	private boolean isRelocateHappening;
	private boolean isInitialShapeAssigned;
	
	private ShapedPane selfReference;
	private CanvasOwner canvasOwner;

	private boolean isResizeEventsDisabled;
	private Vector2 resizeVector;

	private Paint resizeVectorColor;

	protected boolean isShapeInitialized;
	
	public ShapedPane(CanvasOwner canvasOwner) {
		this(canvasOwner, GeometryHelper.getRectanglePolygon(250, 250));
	}
	
	public ShapedPane(CanvasOwner canvasOwner, Polygon shape){ 
		this.canvasOwner = canvasOwner;
		selfReference = this;
		initEvents();
		setDefaults();
		initMouseEvents();
		initializeShape(shape);
	}
	
	private void setDefaults() {
		strokeColor = Color.GRAY;
		fillColor = Color.CORNFLOWERBLUE;
		edgeColor = Color.GREEN;
		relocateBoxColor = Color.AQUA;
		resizeBoxColor = Color.OLIVE;
		resizeVectorColor = Color.RED;
		lineWidth = 1;
		relocationRectangle = new Rectangle(35,35,24,24);
		mousePosition = new Vector2();
		snapPosition = new Vector2();
		resizeVector = new Vector2();
		
		isMouseInside = false;
		isRelocatable = true;
		isRelocateHappening = false;
		isResizable = true;
		isResizeHappening = false;
		isResizeEventsDisabled = false;
		isInitialShapeAssigned = false;
		
		setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);		
	}

	private void initEvents() {
		this.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				updateControls();
			}
		});
		
		this.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				updateControls();
			}
		});
		
		this.layoutXProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				updateControls();
			}
		});
		
		this.layoutYProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				updateControls();
			}
		});
		
		this.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent event) {
		        
		    }
		});
		
		this.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent event) {
		        onMouseEntered(event);
		    }
		});
		
		this.addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent event) {
		        onMouseExited(event);
		    }
		});
		
		this.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent event) {
		        onMousePressed(event);
		    }
		});
		
		this.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent event) {
		        onMouseDragged(event);
		    }
		});
		
		this.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent event) {
		        onMouseReleased(event);
		    }
		});
		
		this.addEventFilter(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent event) {
		        onMouseMoved(event);
		    }
		});
	}

	protected void updateControls() {
		relocationRectangle.setPositionX((float) (getLayoutX() + relocationRectangleOffsetX));
		relocationRectangle.setPositionY((float) (getLayoutY() + relocationRectangleOffsetY));
		
		for(int i = 0; i < resizeRectangles.length; i++){
			resizeRectangles[i].setPositionX((float) (shape.getEdgeMidpoint(i).x-7));
			resizeRectangles[i].setPositionY((float) (shape.getEdgeMidpoint(i).y-7));
		}
	}

	private void initMouseEvents(){
	}
	
	public void onMouseEntered(MouseEvent event) {
		isMouseInside = true;
		if(isShapeInitialized == false){
			System.out.println("SHAPE INIT YO");
			initializeShape();
			isShapeInitialized = true;
		}
		System.out.println("mouse entered");
	}
	
	public void onMouseExited(MouseEvent event) {
		eraseMousePointer();
		canvasOwner.notifyOverlayRepaintNeeded();
	}

	public void onMouseMoved(MouseEvent event) {
		updateMousePosition(event);
		canvasOwner.notifyOverlayRepaintNeeded();
	}
	
	public void onMousePressed(MouseEvent event) {
		updateMousePosition(event);
		if(isRelocatable && relocationRectangle.contains(mousePosition)){
			canvasOwner.notifyModificationStart(ModificationType.Transform, selfReference, event);
			isRelocateHappening = true;
		}
		if (isResizable) {
			for(int i=0; i < resizeRectangles.length; i++){
				if(resizeRectangles[i].contains(mousePosition)){
					resizeIndex = i;
					canvasOwner.notifyModificationStart(ModificationType.Resize, selfReference, event);
					isResizeHappening = true;
					break;
				}
			}
		}
	}
	
	public void onMouseDragged(MouseEvent event) {
		if(isResizeHappening || isRelocateHappening){
			canvasOwner.notifyMouseMovement(selfReference, event);
			updateMousePosition(event);
			canvasOwner.notifyRepaintNeeded();
		}
	}
	
	public void onMouseReleased(MouseEvent event){
		if(isRelocateHappening){
			canvasOwner.notifyModificationEnd(selfReference, event);
			isRelocateHappening = false;
			canvasOwner.notifyRepaintNeeded();
		}
		if(isResizeHappening){
			canvasOwner.notifyModificationEnd(selfReference, event);
			isResizeHappening = false;
			canvasOwner.notifyRepaintNeeded();
		}
	}

	private void initializeShape(Polygon shape) {
		this.shape = shape;
		resizeRectangles = new Rectangle[shape.getVerticeCount()];
		resizeVectors = new Vector2[shape.getVerticeCount()];
		for(int i = 0; i < shape.getVerticeCount(); i++) {
			resizeRectangles[i] = new Rectangle(shape.getEdgeMidpoint(i).x-7,shape.getEdgeMidpoint(i).y-7,15,15);
			resizeVectors[i] = shape.getEdgeNormal(i);
		}
	}

	public void initializeShape() {
		Polygon defaultShape = GeometryHelper.getRectanglePolygon(this.getWidth(), this.getHeight());
		defaultShape.move((float)this.getLayoutX(), (float)this.getLayoutY());
		initializeShape(defaultShape);
	}

	private void updateMousePosition(MouseEvent arg0){
		updateMousePosition((float) (arg0.getX() + getLayoutX()), (float) (arg0.getY() + getLayoutY()));
	}
	
	private void updateMousePosition(float i, float j) {
		mousePosition.x = i;
		mousePosition.y = j;
	}
	
	private void eraseMousePointer() {
		updateMousePosition(Integer.MIN_VALUE, Integer.MIN_VALUE);
	}

	
	public void paintShape(GraphicsContext context) {
		context.save();

		context.setLineWidth(lineWidth);
		paintBorder(context);
		paintEdges(context);
		
		if(isMouseInside){
			paintMousePosition(context);
			paintSnapPosition(context);
			if(isRelocatable){
				paintRelocateBox(context);
			}
			if(isResizable){
				paintResizeBoxes(context);
				paintResizeVector(context);
			}
		}
		
		context.restore();
	}
	
	private void paintResizeBoxes(GraphicsContext context){
		context.setStroke(resizeBoxColor);
		for(int i = 0; i < shape.getEdgeCount(); i++) {
			context.strokeRect(resizeRectangles[i].x, resizeRectangles[i].y, resizeRectangles[i].width, resizeRectangles[i].height);
		}
	}
	
	private void paintRelocateBox(GraphicsContext context) {
		context.setStroke(relocateBoxColor);
		context.strokeRect(relocationRectangle.x, relocationRectangle.y, relocationRectangle.width, relocationRectangle.height);
	}

	private void paintBorder(GraphicsContext context) {
		context.setStroke(strokeColor);
		context.setFill(fillColor);
		shape.draw(context);
	}
	
	private void paintEdges(GraphicsContext context) {
		context.setStroke(edgeColor);
		double[] xEdges = shape.getTransformedXVertices();
		double[] yEdges = shape.getTransformedYVertices();
		
		for(int i = 0; i < shape.getVerticeCount() / 2; i++){
			paintPoint(context, xEdges[i], yEdges[i], 5, edgeColor);
		}
	}
	
	private void paintMousePosition(GraphicsContext context){
		paintPoint(context, mousePosition.x, mousePosition.y, 4, Color.RED);
	}
	
	private void paintSnapPosition(GraphicsContext context){
		snapPosition = shape.snapToEdge(mousePosition, 15);
		if(snapPosition != null){
			paintPoint(context, snapPosition.x, snapPosition.y, 4, Color.BLUE);
		}
	}
	
	private void paintResizeVector(GraphicsContext context){
		if(resizeVector.x != 0 || resizeVector.y != 0) {
			context.setStroke(resizeVectorColor);
			context.strokeLine(mousePosition.x, mousePosition.y, mousePosition.x + resizeVector.x * 50, mousePosition.y + resizeVector.y*50);
		}
	}
	
	public int getResizeIndex(){
		return resizeIndex;
	}
	
	private void paintPoint(GraphicsContext context, double x, double y, double radius, Color edgeColor) {
		context.setFill(edgeColor);
		context.fillOval(x, y, radius, radius);
	}
	
	public void translate(Vector2 movement) {
		translate(movement.x, movement.y);
	}
	
	public Polygon getPaneShape(){
		return shape;
	}
	
	enum BasicShape {
		Circle,
		Rectangle,
		Polygon,
	}

	public void translate(float xDiff, float yDiff) {
		this.setLayoutX(this.getLayoutX() + xDiff);
		this.setLayoutY(this.getLayoutY() + yDiff);
		System.out.println("####Translate by x: " + xDiff + ", y: " + yDiff);
	}
	
	public void translateWithShape(float xDiff, float yDiff){
		this.setLayoutX(this.getLayoutX() + xDiff);
		this.setLayoutY(this.getLayoutY() + yDiff);
		this.shape.translate(xDiff, yDiff);
		updateControls();
	}

	public void resize(float newWidth, float newHeight) {
		this.setWidth(newWidth);
		this.setHeight(newHeight);
		this.setPrefSize(newWidth, newHeight);
	}

	public void setShape(Polygon newShape) {
		this.shape = newShape;
		Rectangle border = newShape.getBoundingRectangle();
		isResizeEventsDisabled = true;
		this.setLayoutX(border.x);
		this.setLayoutY(border.y);
		this.setWidth(border.width);
		this.setHeight(border.height);
		isResizeEventsDisabled = false;
	}

	public void setBoundaries(Rectangle rect) {
		this.setWidth(rect.width);
		this.setHeight(rect.height);
	}

	/**
	 * Updates the pane location with respect to changes in shape
	 */
	public void resizeUpdate() {
		isResizeEventsDisabled = true;
		setBoundaries(shape.getBoundingRectangle());
	}

	/**
	 * Sets a resize vector, STRICTLY for painting purposes
	 * @param resizeVector
	 */
	public void setResizeVector(Vector2 resizeVector){
		this.resizeVector = resizeVector;
	}
	
	
	
}
