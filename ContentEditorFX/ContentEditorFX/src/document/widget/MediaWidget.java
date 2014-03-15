package document.widget;

import geometry.GeometryHelper;
import geometry.libgdxmath.Polygon;


public class MediaWidget extends Widget{

	public MediaWidget(double x, double y, double width, double height){
		Polygon shape = GeometryHelper.getRectanglePolygon(width, height); 
		shape.setPositionX((float) x);
		shape.setPositionY((float) y);
		setShape(shape);
	}
	
	@Override
	public WidgetType getType() {
		return WidgetType.MediaWidget;
	}

}
