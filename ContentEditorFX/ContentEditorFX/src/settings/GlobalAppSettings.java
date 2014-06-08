package settings;

import gui.columnview.DocumentView.ScrollMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Loads app settings and initializes required values. Should only be used upon program load time
 * and if the user changes any settings. Keep this class static only.
 * @author sahin
 *
 */
public class GlobalAppSettings {
	
	public static final double fastDeviceFrameMillis = 16;
	public static final double mediumDeviceFrameMillis = 32;
	public static final double slowDeviceFrameMillis = 64;
	public static final float ignoreValuesBelow = 0.001f;
	public static final float ignoreValuesBelowMedium = 0.1f;
	public static final float ignoreValuesBelowLarge = 5f;
	public static final boolean bypassLogin = true;
	public static double caretBlinkRate = 1000;
	public static float caretMovementTime = 100f;
	public static float pointerJumpSize = 10;
	public static ScrollMode defaultDocumentViewScrollMode = ScrollMode.Continuous;
	public static Insets popupInsets = new Insets(20,20,20,20);
	public static double gridHGap = 20;
	public static double gridVGap = 20;
	public static double minFrameWidth = 240;
	public static double minFrameHeight = 320;
	public static double frameWidth = 1080;
	public static double frameHeight = 960;
	public static double smallFrameWidth = 600;
	public static double smallFrameHeight = 200;
	public static boolean guiDebugGuidelines;
	public static boolean isTestModeOn;
	public static boolean areLineViewCountsVisible = true;
	public static double memoryStatUpdateRate = 500f;
	public static LineFitOption selectedFitLineOption = LineFitOption.averageFit;
	public static double dimmerTime = 500;
	
	public static int defaultServerPort = 1018;
	
	public static Color accentColor; 
	public static Color complementColor;
	public static long defaultServerTimeout = 5000;
	public static int defaultThreadpoolSize = 10;
	
	public enum LineFitOption {
		strictFit,
		averageFit,
		looseFit
	}
	
	public static void loadAppSettings(){
		loadTestConfigSettings();
		loadLanguageSettings();
		accentColor = Color.rgb(247, 107, 0, 1f);
		complementColor = Color.rgb(163, 196, 186, 1);
	}
	
	private static void loadTestConfigSettings() {
		//defaultValues
		guiDebugGuidelines = false;
		isTestModeOn = false;
		
		try{
			File configFile = new File("res/config/test_config.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(configFile);
			
			NodeList nList = doc.getElementsByTagName("guiDebugGuidelines");
			for(int i = 0; i < nList.getLength(); i++){
				String value;
				Node node = nList.item(i);
				Element element = (Element) node;
				value = element.getTextContent();
				System.out.println("gui debug value :" + value);
				if(value.equalsIgnoreCase("true"))
					guiDebugGuidelines = true;
			}
			
			nList = doc.getElementsByTagName("testMode");
			for(int i = 0; i < nList.getLength(); i++){
				String value;
				Node node = nList.item(i);
				Element element = (Element) node;
				value = element.getTextContent();
				System.out.println("test value :" + value);
				if(value.equalsIgnoreCase("true"))
					isTestModeOn = true;
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void loadLanguageSettings(){
		HashMap<String, String> languageFileMap = new HashMap<String, String>();
		ArrayList<String> availableLanguages = new ArrayList<String>();
		
		try{
			File langFile = new File("res/lang/lang.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(langFile);
			
			NodeList nList = doc.getElementsByTagName("language");
			
			for(int i = 0; i < nList.getLength(); i++){
				String langName;
				String filePath;
				Node node = nList.item(i);
				Element element = (Element)node;
				langName = element.getAttribute("id");
				filePath = element.getTextContent();
				availableLanguages.add(langName);
				languageFileMap.put(langName, filePath);
				
				if(guiDebugGuidelines)
					System.out.println("Found language: " + langName + ": " + filePath);
			}
			
			Translator.buildDictionaries(availableLanguages, languageFileMap);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
