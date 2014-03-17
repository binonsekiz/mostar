package gui.widget;

import gui.columnview.ColumnView;
import gui.threed.ThreeDModelViewer;
import control.WidgetModifyFacade;
import document.Column;
import document.widget.ThreeDViewerWidget;

public class ThreeDViewerWidgetModifier extends WidgetModifier{
	
	private ThreeDModelViewer viewer;

	public ThreeDViewerWidgetModifier(WidgetModifyFacade widgetModifyFacade, Column parent, ColumnView parentPane) {
		super(widgetModifyFacade, parent, parentPane);
		initGui();
		widget = new ThreeDViewerWidget(this.getLayoutX(), this.getLayoutY(), this.getWidth(), this.getHeight());
		widget.setShape(getPaneShape());
		
		super.initializeGui();
		super.initializeEvents();
		initEvents();
	}

	private void initEvents() {
		
	}
	
	private void initGui() {
		viewer = new ThreeDModelViewer();
		this.widgetNode = viewer;
	}

	@Override
	public WidgetModifierType getType() {
		return WidgetModifierType.ThreeDViewerWidgetModifier;
	}

}
