package document.widget;


public class SingleImageWidget extends Widget{
	
	public SingleImageWidget(double x, double y, double d, double e){
		setBorders(x, y, d, e);
	}

	@Override
	public WidgetType getType() {
		return WidgetType.SingleImageWidget;
	}

}
