package storage;

import java.io.File;
import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import document.PersistentIndexedObject;
import document.PersistentObject;

public class XmlManager {
	
	DocumentBuilderFactory factory;
	DocumentBuilder docBuilder;
	TransformerFactory transformerFactory;
	Transformer transformer;
	
	public XmlManager() {
		factory = DocumentBuilderFactory.newInstance();
		try {
			transformerFactory = TransformerFactory.newInstance();
			transformer = transformerFactory.newTransformer();
			docBuilder = factory.newDocumentBuilder();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	private Document getXmlDocument(document.Document document){
		Document doc = docBuilder.newDocument();
		doc.appendChild(document.getXmlNode(doc));
		return doc;
	}

	public void saveDocument(document.Document document, File selectedFile) {
		Document xmlDocument = getXmlDocument(document);
		DOMSource source = new DOMSource(xmlDocument);
		StreamResult streamResult = new StreamResult(selectedFile);
		try {
			transformer.transform(source, streamResult);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
	public static void insertNumberElement(Document doc, Element parent, String name, Number value) {
		Element tempElement = doc.createElement(name);
		tempElement.setAttribute("value", value.toString());
		parent.appendChild(tempElement);
	}
	
	public static void insertStringElement(Document doc, Element parent, String name, String value) {
		Element tempElement = doc.createElement(name);
		tempElement.setAttribute("value", value);
		parent.appendChild(tempElement);
	}

	public static void insertColorElement(Document doc, Element parent, String name, Color strokeColor) {
		Element tempElement = doc.createElement(name);
		tempElement.setAttribute("R", strokeColor.getRed() + "");
		tempElement.setAttribute("G", strokeColor.getGreen() + "");
		tempElement.setAttribute("B", strokeColor.getBlue() + "");
		tempElement.setAttribute("O", strokeColor.getOpacity() + "");
		parent.appendChild(tempElement);
	}
	
	public static void insertArrayListElements(Document doc, Element parent, String name, ArrayList<? extends PersistentObject> objects) {
		Element tempElement = doc.createElement(name);
		for(int i = 0; i < objects.size(); i++) {
			insertSingleElement(doc, tempElement, objects.get(i));
		}
		parent.appendChild(tempElement);
	}

	public static void insertSingleElement(Document doc, Element parent, PersistentObject object) {
		parent.appendChild(object.getXmlNode(doc));
	}

	public static void insertArrayListId(Document doc, Element parent, String name, ArrayList<? extends PersistentIndexedObject> objects) {
		Element tempElement = doc.createElement(name);
		for(int i = 0; i < objects.size(); i++) {
			insertSingleId(doc, tempElement, name, objects.get(i));
		}
		parent.appendChild(tempElement);
	}
	
	public static void insertSingleId(Document doc, Element parent, String name, PersistentIndexedObject object) {
		Element tempElement = doc.createElement(name);
		tempElement.setAttribute("index", object.getPersistenceId() + "");
		parent.appendChild(tempElement);
	}

	
}
