package storage;

import geometry.libgdxmath.LineSegment;
import geometry.libgdxmath.Polygon;
import geometry.libgdxmath.Rectangle;
import geometry.libgdxmath.Vector2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javafx.concurrent.Task;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import document.Column;
import document.DocumentText;
import document.PageInsets;
import document.PageSpecs.Measurement;
import document.Paragraph;
import document.ParagraphSet;
import document.ParagraphSpace;
import document.TextLine;
import document.persistentproperties.interfaces.PersistentIndexedObject;
import document.persistentproperties.interfaces.PersistentObject;
import document.style.TextStyle;
import document.widget.ImageGalleryWidget;
import document.widget.MediaWidget;
import document.widget.SingleImageWidget;
import document.widget.ThreeDViewerWidget;
import document.widget.WebViewWidget;
import document.widget.Widget;

public class XmlManager {
	
	private static DocumentBuilderFactory factory;
	private static DocumentBuilder docBuilder;
	private static TransformerFactory transformerFactory;
	private static Transformer transformer;
	
	public XmlManager() {
		factory = DocumentBuilderFactory.newInstance();
		try {
			transformerFactory = TransformerFactory.newInstance();
			transformer = transformerFactory.newTransformer();
			docBuilder = factory.newDocumentBuilder();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			
			System.out.println("factory: " + factory);
			System.out.println("docbuilder: " + docBuilder);
			System.out.println("transformerFactory: " + transformerFactory);
			System.out.println("transformer: " + transformer);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
	}

	private static Document getXmlDocument(document.Document document){
		System.out.println("1");
		Document doc = docBuilder.newDocument();
		System.out.println("xml doc: " + doc);
		System.out.println("document: " + document);
		doc.appendChild(document.saveToXmlNode(doc));
		System.out.println("3");
		return doc;
	}
	
	public static document.Document loadDocument(File file) {
		document.Document doc = null;
		try {
			Document xmlDoc = docBuilder.parse(file);
			doc = parseDocumentFromXml(xmlDoc);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

	public static void saveDocument(final document.Document document, final File selectedFile) {
//		Task<Void> saveTask = new Task<Void>() {
//			@Override
//			protected Void call() throws Exception {
				System.out.println("SAVE TASK: file: " + selectedFile);
				Document xmlDocument = getXmlDocument(document);
				System.out.println("xmldocument: " + xmlDocument);
				DOMSource source = new DOMSource(xmlDocument);
				System.out.println("source: " + source);
				StreamResult streamResult = new StreamResult(selectedFile);
				System.out.println("streamresult: " + streamResult);
				try {
					transformer.transform(source, streamResult);
				} catch (TransformerException e) {
					e.printStackTrace();
				}
//				return null;
//			}
//		};
//		
//		new Thread(saveTask).start();
	}
	
	public static document.Document parseDocumentFromXml(final Document xmlDocument) {
		document.Document loadedDocument = null;
		
		Task<document.Document> loadTask = new Task<document.Document>() {
			document.Document document = null;
			@Override
			protected document.Document call() throws Exception {		
				xmlDocument.normalize();
				document = new document.Document(xmlDocument.getDocumentElement());
				
				return document;
			}
		};
		
		new Thread(loadTask).start();
		
		try {
			loadedDocument = loadTask.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		return loadedDocument;
	}
	
	public static Number loadNumberFromXmlElement(String name, Element element) {
		NodeList measurementList = element.getElementsByTagName(name);
		Node tempNode = null;
		Element temp = null;
		Number number = 0;
		
		for(int i = 0; i < measurementList.getLength(); i++) {
			tempNode = measurementList.item(i);
			if(tempNode instanceof Element) {
				temp = (Element) tempNode;
			}
			else continue;
			
			if(temp.getTagName().equals(name)) {
				number = Double.parseDouble(temp.getAttribute("value"));
				break;
			}
		}
		return number;
	}
	
	public static float[] loadFloatArrayFromXmlElement(String arrayName, Element element) {
		float[] retVal = null;
		NodeList localVerticesElement = element.getElementsByTagName(arrayName);
		for(int i = 0; i < localVerticesElement.getLength(); i++){
			Node temp = localVerticesElement.item(i);
			Element tempElement = null;
			if(temp instanceof Element){
				tempElement = (Element) temp;
			}
			else continue;
			
			int localVerticeCount = Integer.parseInt(tempElement.getAttribute("count"));
			retVal = new float[localVerticeCount];
			
			for(int j = 0; j < localVerticeCount; j++) {
				retVal[j] = XmlManager.loadNumberFromXmlElement("Float" + j, tempElement).floatValue();
			}
		}
		return retVal;
	}
	
	public static String loadStringFromXmlElement(String name, Element element) {
		NodeList measurementList = element.getElementsByTagName(name);
		Node tempNode = null;
		Element temp = null;
		String text = "";
		
		for(int i = 0; i < measurementList.getLength(); i++) {
			tempNode = measurementList.item(i);
			if(tempNode instanceof Element) {
				temp = (Element) tempNode;
			}
			else continue;
			
			if(temp.getTagName().equals(name)) {
				text = temp.getTextContent();
				break;
			}
		}
		return text;
	}
	
	public static String loadStringAttributeFromXmlElement(String name, Element element) {
		NodeList measurementList = element.getElementsByTagName(name);
		Node tempNode = null;
		Element temp = null;
		String text = "";
		
		for(int i = 0; i < measurementList.getLength(); i++) {
			tempNode = measurementList.item(i);
			if(tempNode instanceof Element) {
				temp = (Element) tempNode;
			}
			else continue;
			
			if(temp.getTagName().equals(name)) {
				text = temp.getAttribute("value");
				break;
			}
		}
		return text;
	}
	
	public static Color loadColorFromXmlElement(String name, Element element) {
		NodeList measurementList = element.getElementsByTagName(name);
		Node tempNode = null;
		Element temp = null;
		double r = 0;
		double g = 0;
		double b = 0;
		double o = 0;
		
		for(int i = 0; i < measurementList.getLength(); i++) {
			tempNode = measurementList.item(i);
			if(tempNode instanceof Element) {
				temp = (Element) tempNode;
			}
			else continue;
			
			if(temp.getTagName().equals(name)) {
				r = Double.parseDouble(temp.getAttribute("R"));
				g = Double.parseDouble(temp.getAttribute("G"));
				b = Double.parseDouble(temp.getAttribute("B"));
				o = Double.parseDouble(temp.getAttribute("O"));
			}
			break;
		}
		return new Color(r,g,b,o);
	}
	
	public static PersistentObject loadObjectFromXmlElement(String tagName, Element element) {
		NodeList measurementList = element.getElementsByTagName(tagName);
		Node tempNode = null;
		Element temp = null;
		PersistentObject emptyObject = null;
		
		for(int i = 0; i < measurementList.getLength(); i++) {
			tempNode = measurementList.item(i);
			if(tempNode instanceof Element) {
				temp = (Element) tempNode;
			}
			else continue;
			
			if(temp.getTagName().equals(tagName)) {
				emptyObject = createPersistentObject(tagName, temp);
				break;
			}
		}
		
		return emptyObject;
	}
	
	public static ArrayList<Integer> loadArrayListIdFromXmlElement(String collectionTagName, String className, Element element) {
		NodeList nodes = element.getElementsByTagName(collectionTagName);
		Node tempNode = null;
		Element temp = null;
		ArrayList<Integer> returnValue = new ArrayList<Integer>();
		
		for(int i = 0; i < nodes.getLength(); i++) {
			tempNode = nodes.item(i);
			
			if(tempNode instanceof Element) {
				temp = (Element) tempNode;
			}
			else continue;
			
			NodeList innerNodes = temp.getElementsByTagName(className);
			Node innerTempNode = null;
			Element innerTemp = null;
			
			for(int j = 0; j < innerNodes.getLength(); j++) {
				innerTempNode = innerNodes.item(j);
				
				if(innerTempNode instanceof Element) {
					innerTemp = (Element) innerTempNode;
				}
				else continue;
				
				returnValue.add(Integer.parseInt(innerTemp.getAttribute("index")));
			}
		}
		
		return returnValue;
	}
	
	public static ArrayList<? extends PersistentObject> loadArrayListFromXmlElement(String collectionTagName, String className, Element element) {
		NodeList nodes = element.getElementsByTagName(collectionTagName);
		Node tempNode = null;
		Element temp = null;
		ArrayList<PersistentObject> returnValue = new ArrayList<PersistentObject>();
		
		for(int i = 0; i < nodes.getLength(); i++) {
			tempNode = nodes.item(i);
			
			if(tempNode instanceof Element) {
				temp = (Element) tempNode;
			}
			else continue;
			
			NodeList innerNodes = temp.getElementsByTagName(className);
			Node innerTempNode = null;
			Element innerTemp = null;
			
			for(int j = 0; j < innerNodes.getLength(); j++) {
				innerTempNode = innerNodes.item(j);
				
				if(innerTempNode instanceof Element) {
					innerTemp = (Element) innerTempNode;
				}
				else continue;
				
				if(innerTemp.getTagName().equals(className)) {
					PersistentObject object = createPersistentObject(className, innerTemp);
					returnValue.add(object);
				}
			}
		}
		
		return returnValue;
	}
	
	private static PersistentObject createPersistentObject(String type, Element element) {
		switch (type){
		case "ImageGalleryWidget": return new ImageGalleryWidget(element);
		case "MediaWidget": return new MediaWidget(element);
		case "SingleImageWidget": return new SingleImageWidget(element);
		case "ThreeDViewerWidget": return new ThreeDViewerWidget(element);
		case "WebViewWidget": return new WebViewWidget(element);
		case "Widget": return new Widget(element);
		case "Column": return new Column(element);
		case "Document": return new document.Document(element);
		case "DocumentText": return new DocumentText(element);
		case "PageInsets": return new PageInsets(element);
		case "Measurement": return new Measurement(element);
		case "Paragraph": return new Paragraph(element);
		case "ParagraphSet": return new ParagraphSet(element);
		case "ParagraphSpace": return new ParagraphSpace(element);
		case "TextLine": return new TextLine(element);
		case "LineSegment": return new LineSegment(element);
		case "Polygon": return new Polygon(element);
		case "Rectangle": return new Rectangle(element);
		case "Vector2": return new Vector2(element);
		case "TextStyle": return new TextStyle(element);
		}
		throw new RuntimeException("Unknown xml element type: " + type);
	}
	
	public static void insertNumberElement(Document doc, Element parent, String name, Number value) {
		Element tempElement = doc.createElement(name);
		tempElement.setAttribute("value", value.toString());
		parent.appendChild(tempElement);
	}
	
	public static void insertFloatArrayElement(Document doc, Element parent, String arrayName, float[] array) {
		Element localVerticesElement = doc.createElement(arrayName);
		localVerticesElement.setAttribute("count", array.length + "");
		for(int i = 0; i < array.length; i++) {
			XmlManager.insertNumberElement(doc, localVerticesElement, "Float" + i, array[i]);
		}
		parent.appendChild(localVerticesElement);
	}
	
	public static void insertStringElement(Document doc, Element parent, String name, String value) {
		Element tempElement = doc.createElement(name);
		tempElement.setTextContent(value);
		parent.appendChild(tempElement);
	}
	
	public static void insertStringAttributeElement(Document doc, Element parent, String name, String value) {
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
		parent.appendChild(object.saveToXmlNode(doc));
	}

	public static void insertArrayListId(Document doc, Element parent, String name, String innerClassName, ArrayList<? extends PersistentIndexedObject> objects) {
		Element tempElement = doc.createElement(name);
		for(int i = 0; i < objects.size(); i++) {
			insertSingleId(doc, tempElement, innerClassName, objects.get(i));
		}
		parent.appendChild(tempElement);
	}
	
	public static void insertSingleId(Document doc, Element parent, String name, PersistentIndexedObject object) {
		Element tempElement = doc.createElement(name);
		tempElement.setAttribute("index", object.getPersistenceId() + "");
		parent.appendChild(tempElement);
	}

}
