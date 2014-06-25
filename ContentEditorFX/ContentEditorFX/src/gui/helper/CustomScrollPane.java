package gui.helper;

import geometry.libgdxmath.Vector2;
import gui.columnview.ColumnView;

import java.util.ArrayList;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import settings.GlobalAppSettings;

public class CustomScrollPane extends Pane{

	private CustomScrollPane instance;
	
	private ScrollMode scrollMode;
	private ArrayList<ColumnView> content;
	
	private SimpleDoubleProperty minH;
	private SimpleDoubleProperty maxH;
	private SimpleDoubleProperty hvalue;
	
	private SimpleDoubleProperty minV;
	private SimpleDoubleProperty maxV;
	private SimpleDoubleProperty vvalue;
	
	private SimpleDoubleProperty zoomValue;
	private SimpleDoubleProperty minZoom;
	private SimpleDoubleProperty maxZoom;
	
	private SimpleDoubleProperty vGap;
	private SimpleDoubleProperty hGap;
	
	private SimpleIntegerProperty visiblePageStart;

	private double offsetFromOverlayX;
	private double offsetFromOverlayY;
	
	public CustomScrollPane() {
		minH = new SimpleDoubleProperty();
		maxH = new SimpleDoubleProperty();
		hvalue = new SimpleDoubleProperty();
		minV = new SimpleDoubleProperty();
		maxV = new SimpleDoubleProperty();
		vvalue = new SimpleDoubleProperty();
		zoomValue = new SimpleDoubleProperty();
		minZoom = new SimpleDoubleProperty();
		maxZoom = new SimpleDoubleProperty();
		visiblePageStart = new SimpleIntegerProperty();
		vGap = new SimpleDoubleProperty(GlobalAppSettings.gridVGap);
		hGap = new SimpleDoubleProperty(GlobalAppSettings.gridHGap);
		scrollMode = GlobalAppSettings.defaultDocumentViewScrollMode;
		initEvents();
		setId("red-pane");
		instance = this;
	}

	public void setContent(ArrayList<ColumnView> columnViews) {
		this.content = columnViews;
	}
	
	public void validateContent() {
		if(scrollMode == ScrollMode.Discrete)
		{
			this.getChildren().clear();
			this.getChildren().add(content.get(getVisiblePageStart()));
		}
		
		else if(scrollMode == ScrollMode.ContinuousHorizontal) 
		{
			this.getChildren().clear();
			this.getChildren().addAll(content);
			
			double widthSoFar = 0;
			double maxHeight = 0;
			
			for(int i = 0; i < content.size(); i++) {
				ColumnView current = content.get(i);
				
				current.setLayoutX(widthSoFar);
				
				if(current.getHeight() > maxHeight){ 
					maxHeight = current.getHeight() + vGap.get();
				}
				widthSoFar = widthSoFar + current.getWidth() + hGap.get();
			}
			
			minH.set(0);
			maxH.set(widthSoFar);
			hvalue.set(0);
			
			minV.set(0);
			maxV.set(maxHeight);
			vvalue.set(0);
			
			zoomValue.set(0.5f);
			minZoom.set(0.01);
			maxZoom.set(100);
		}
	}
	
	private void initEvents() {
		zoomValue.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				if(arg2.doubleValue() <= minZoom.doubleValue()) {
					zoomValue.set(minZoom.doubleValue());
				}
			}
		});
		
		setOnScroll(new EventHandler<ScrollEvent>(){
			@Override
			public void handle(ScrollEvent arg0) {
				hvalue.set(hvalue.get() + arg0.getDeltaX() + arg0.getDeltaY());
			}
		});
		
		hvalue.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				setLayoutX(hvalue.get() * -1);
			}
		});
		
		vvalue.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				setLayoutY(vvalue.get() * -1);
			}
		});
		
		zoomValue.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				setScaleX(zoomValue.get());
				setScaleY(zoomValue.get());
			}
		});
	}	
	
	public Vector2 convertScreenCoordinateToContentCoordinate(Vector2 input) {
		return convertScreenCoordinateToContentCoordinate(input.x, input.y);
	}

	/**
	 * Converts a point on the document view canvas to the custom scroll pane coordinates.
	 * If we want to modify the model with mouse (like drawing a shape) this function should
	 * be called to translate the coordinates
	 * @param x
	 * @param y
	 * @return
	 */
	public Vector2 convertScreenCoordinateToContentCoordinate(double x, double y) {
		Vector2 output = new Vector2();
		Bounds scrollPaneBounds = this.getBoundsInParent();
		output.x = -1 * (float) (scrollPaneBounds.getMinX() / zoomValue.get());
		output.y = -1 * (float) (scrollPaneBounds.getMinY() / zoomValue.get());
		
		output.x += x / zoomValue.get();
		output.y += y / zoomValue.get();
		
		return output;
	}
	
	/**
	 * Converts a point on the custom scroll pane to the overlay canvas coordinate. 
	 * If we want to draw a temporary overlay on a content in scroll pane,
	 * convert the point on the scroll pane to overlay content using this function.
	 * @param x
	 * @param y
	 * @return
	 */
	public Vector2 convertContentCoordinateToScreenCoordinate(double x, double y) {
		Vector2 output = new Vector2();
		Bounds scrollPaneBounds = this.getBoundsInParent();
		
		output.x = (float) (x * zoomValue.get() + scrollPaneBounds.getMinX());
		output.y = (float) (y * zoomValue.get() + scrollPaneBounds.getMinY());
		
		return output;
	}

	public SimpleDoubleProperty hvalueProperty() {
		return hvalue;
	}

	public SimpleDoubleProperty vvalueProperty() {
		return vvalue;
	}
	
	public SimpleDoubleProperty zoomProperty() {
		return zoomValue;
	}
	
	public SimpleDoubleProperty minZoomProperty() {
		return minZoom;
	}
	
	public SimpleDoubleProperty maxZoomProperty() {
		return maxZoom;
	}
	
	public void setScrollMode(ScrollMode mode) {
		this.scrollMode = mode;
	}
	
	public ScrollMode getScrollMode() {
		return scrollMode;
	}
	
	public void setVisiblePage(int value) {
		this.visiblePageStart.set(value);
	}
	
	public SimpleIntegerProperty visiblePageStartProperty() {
		return visiblePageStart;
	}
	
	public int getVisiblePageStart() {
		return visiblePageStart.get();
	}

	public Bounds getVisibleViewportBounds() {
		Bounds retVal = new BoundingBox(hvalue.get() / zoomValue.get(), vvalue.get() / zoomValue.get(), (hvalue.get() + this.getWidth()) / zoomValue.get(), (vvalue.get() + this.getHeight()) / zoomValue.get());
		return retVal;
	}

	public double getScaledOffsetX() {
		return hvalue.get() / zoomValue.get();
	}

	public double getScaledOffsetY() {
		return vvalue.get() / zoomValue.get();
	}

	public enum ScrollMode {
		ContinuousHorizontal,
		ContinuousVertical,
		Discrete
	}

	public void setZoomFactor(double zoomFactor) {
		zoomValue.set(zoomFactor);
	}

	
	/*offsetX = -1 * ((continuousScrollPane.getHvalue() - continuousScrollPane.getHmin()) / (continuousScrollPane.getHmax() - continuousScrollPane.getHmin())) 
	* (zoomGroup.getBoundsInParent().getWidth() - continuousScrollPane.getViewportBounds().getWidth());
offsetY = -1 * ((continuousScrollPane.getVvalue() - continuousScrollPane.getVmin()) / (continuousScrollPane.getVmax() - continuousScrollPane.getVmin())) 
	* (zoomGroup.getBoundsInParent().getHeight() - continuousScrollPane.getViewportBounds().getHeight());

scaledOffsetX = offsetX / zoomFactor;
scaledOffsetY = offsetY / zoomFactor;

debugBox.setX(-1 * scaledOffsetX);
debugBox.setY(-1 * scaledOffsetY);*/

}
