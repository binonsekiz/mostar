package document.widget;


public class MediaWidget extends Widget{

	public MediaWidget(double x, double y, double d, double e){
		setBorders(x, y, d, e);
	}
	
	@Override
	public WidgetType getType() {
		return WidgetType.MediaWidget;
	}

}
