package gui.docmodify.test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StyledTextTest {
	
	public static int textIndex = 0;
	
	
	public static String getText(){
		String retVal = getText(textIndex);
		textIndex++;
		return retVal;
	}
	
	public static String getText(int index){
		if(index == 0) {
			return readFile("res/test/styledTextTestStrings.txt");
		}
		return "";	
	}
	
	
	public static String readFile(String path) 
	{
		System.out.println("Reading: " + path);
		Charset encoding = StandardCharsets.UTF_16;
		byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}
}
