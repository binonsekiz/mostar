package document.persistentproperties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import document.persistentproperties.interfaces.PersistentObject;
import storage.XmlManager;
import javafx.scene.paint.Color;

public class TextStyleProperties implements PersistentObject{

	protected String fontName;
	protected double fontSize;
	protected float lineSpacingHeight;
	protected Color strokeColor;
	protected Color fillColor;
	
	public TextStyleProperties() {
		// Default constructor
	}
	
	public TextStyleProperties(Element element) {
		loadFromXmlElement(element);
	}
	
	@Override
	public Node saveToXmlNode(Document doc) {
		Element textStyleElement = doc.createElement("TextStyle");
		XmlManager.insertStringAttributeElement(doc, textStyleElement, "FontName", fontName);
		XmlManager.insertNumberElement(doc, textStyleElement, "FontSize", fontSize);
		XmlManager.insertNumberElement(doc, textStyleElement, "LineSpacingHeight", lineSpacingHeight);
		XmlManager.insertColorElement(doc, textStyleElement, "StrokeColor", strokeColor);
		XmlManager.insertColorElement(doc, textStyleElement, "FillColor", fillColor);
		
		return textStyleElement;
	}
	
	@Override
	public void loadFromXmlElement(Element element) {
		fontName = XmlManager.loadStringAttributeFromXmlElement("FontName", element);
		fontSize = XmlManager.loadNumberFromXmlElement("FontSize", element).doubleValue();
		lineSpacingHeight = XmlManager.loadNumberFromXmlElement("LineSpacingHeight", element).floatValue();
		strokeColor = XmlManager.loadColorFromXmlElement("StrokeColor", element);
		fillColor = XmlManager.loadColorFromXmlElement("FillColor", element);
	}
	
}
