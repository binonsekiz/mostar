package document.widget;

import javafx.scene.image.Image;

public class ImageGalleryWidget extends Widget{
	
	private Image[] images;
	
	public ImageGalleryWidget(double x, double y, double d, double e){
		setBorders(x, y, d, e);
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
