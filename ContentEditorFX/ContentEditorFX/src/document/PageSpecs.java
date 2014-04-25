package document;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import document.persistentproperties.interfaces.PersistentObject;
import storage.XmlManager;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


public class PageSpecs{

	public static final Measurement A4 = new Measurement(210, 297);
	public static final Measurement A4H = new Measurement(297, 210);
	public static final Measurement Letter = new Measurement(279.4f, 215.9f);
	public static final Measurement LetterH = new Measurement(215.9f, 279.4f);
	public static final Measurement P1280x768 = new Measurement(1280, 768);
	public static final Measurement P640x768 = new Measurement(640, 768);
	public static final Measurement MiniTest = new Measurement(480, 640);
	
	public static final Measurement DefaultMeasurement = P640x768;
	
	public static class Measurement implements PersistentObject{
		private float width;
		private float height;
		
		public Measurement(float width, float height){
			this.width = width;
			this.height = height;
		}
		
		public Measurement(Element item) {
			loadFromXmlElement(item);
		}

		public float getWidth(){
			return width;
		}
		
		public float getHeight(){
			return height;
		}

		public Node saveToXmlNode(org.w3c.dom.Document doc) {
			Element measurementElement = doc.createElement("Measurement");
			XmlManager.insertNumberElement(doc, measurementElement, "Width", width);
			XmlManager.insertNumberElement(doc, measurementElement, "Height", height);
			return measurementElement;
		}

	//	@Override
		public void loadFromXmlElement(Element element) {
			width = XmlManager.loadNumberFromXmlElement("Width", element).floatValue();
			height = XmlManager.loadNumberFromXmlElement("Height", element).floatValue();
		}
	}
}
