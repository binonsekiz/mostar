package document.widget;

import geometry.GeometryHelper;
import geometry.libgdxmath.Polygon;
import javafx.scene.image.Image;

public class ImageGalleryWidget extends Widget{
	
	private Image[] images;
	
	public ImageGalleryWidget(double x, double y, double width, double height){
		Polygon shape = GeometryHelper.getRectanglePolygon(width, height); 
		shape.setPositionX((float) x);
		shape.setPositionY((float) y);
		setShape(shape);
		images = new Image[9];
	}
	
	@Override
	public WidgetType getType() {
		return WidgetType.ImageGalleryWidget;
	}

	public Image getImage(int index) {
		return images[index];
	}

	public void setImage(int i, Image image) {
		this.images[i] = image;
	}

}
