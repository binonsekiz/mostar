package document.widget;



public class WebViewWidget extends Widget{

	public WebViewWidget(double x, double y, double d, double e){
		setBorders(x, y, d, e);
	}

	@Override
	public WidgetType getType() {
		return WidgetType.WebViewWidget;
	}
	
}
