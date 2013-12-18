package gui.helper;


import java.util.ArrayList;

import javax.xml.ws.handler.MessageContext.Scope;

import zzzzdeprecated.ColumnViewPane;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.control.ScrollPane;
import javafx.util.Duration;

public class PageChangeTransition extends Transition{

	private ScrollPane scrollPane;
	private ArrayList<ColumnViewPane> pageViewPanes;
	private double startValue;
	private double totalWidthDifference;
	private double endValue;
	private double delta;
	private double hgap;
	
	private int tempIndex1, tempIndex2;

	public PageChangeTransition(ScrollPane scrollPane, ArrayList<ColumnViewPane> pageViewPanes, double hgap){
		this.scrollPane = scrollPane;
		this.pageViewPanes = pageViewPanes;
		setInterpolator(Interpolator.EASE_BOTH);
		startValue = scrollPane.getHvalue();
		this.hgap = hgap;
	}
	
	public void fromPageToPage(int index1, int index2){
		startValue = scrollPane.getHvalue();
		
		//calculate total distance between two pages:
		if(index1 < 0) index1 = 0;
		if(index1 >= pageViewPanes.size()) index1 = pageViewPanes.size() - 1;
		if(index2 < 0) index2 = 0;
		if(index2 >= pageViewPanes.size()) index2 = pageViewPanes.size() - 1;
		
		totalWidthDifference = 0;
		
		//each page may have different width, so iterate on pages:
		for(int i = Math.min(index1, index2); i < Math.max(index1, index2); i++){
			totalWidthDifference += pageViewPanes.get(i).getWidth() + hgap ;
		}

		//TODO: there is a subtle bug here, if index2 < index1 and the widths of those pages are different,
		//the resulting place would not be where we would want it. fix this after demo.
		if(index2 < index1){
			totalWidthDifference = -1 * totalWidthDifference;
		}
		
		endValue = startValue + (totalWidthDifference/scrollPane.getContent().getBoundsInParent().getWidth());
		
		delta = endValue - startValue;
		
		//TODO: debug
		tempIndex1 = index1;
		tempIndex2 = index2;
	}
	
	@Override
	protected void interpolate(double arg0) {
		scrollPane.setHvalue(startValue + delta * arg0);
	}

	public void setDuration(Duration millis) {
		setCycleDuration(millis);
	}

}
