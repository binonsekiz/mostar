package zzzzdeprecated;

import gui.widget.WidgetModifier;

import java.util.ArrayList;

import document.widget.Widget.TextWrapType;

@Deprecated
public class WidgetFocusHandler {

	private static ArrayList<WidgetModifier> widgetModifiers = new ArrayList<WidgetModifier>();
	
	public static void addWidgetModifier(WidgetModifier modifier){
		widgetModifiers.add(modifier);
	}
	
	public static void removeWidgetModifier(WidgetModifier modifier){
		widgetModifiers.remove(modifier);
	}
	
	public static void clearAll(){
		widgetModifiers.clear();
	}
	
	public static void gainedFocusSignal(WidgetModifier focusedModifier){
		for(WidgetModifier modifier: widgetModifiers){
			if(modifier != focusedModifier)
				modifier.loseFocus();
		}
	}
	
	public static void loseAllFocus(){
		for(WidgetModifier modifier: widgetModifiers){
			modifier.loseFocus();
		}
	//	DocModifyScreenGuiFacade.instance.clearVersatilePane();
	}
	
	public static void refreshWidgetTextOrder(){
		for(WidgetModifier modifier: widgetModifiers){
			if(modifier.getWidget().getTextWrap() == TextWrapType.Behind)
				modifier.toBack();
			else if(modifier.getWidget().getTextWrap() == TextWrapType.Front){
				modifier.toFront();
			}
		}
	}
}
