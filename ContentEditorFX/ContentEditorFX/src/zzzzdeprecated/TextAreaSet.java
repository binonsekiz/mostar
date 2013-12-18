package zzzzdeprecated;
/*
import gui.docmodify.DocShape;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class TextAreaSet extends DocShape implements EventHandler<KeyEvent>{

	private TextAreaSetModel textModel;
	private float[] textCharSizes;
	
	private ArrayList<TextLine> lines;
	
	private double maxWidth;
	private double maxHeight;
	
	public TextAreaSet(double maxWidth, double maxHeight){
		this.setOnKeyPressed(this);
		textModel = new TextAreaSetModel(this);
		lines = new ArrayList<TextLine>();
		
		//add the initial line
		lines.add(new TextLine(0));
		
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.setText("Test text Test text Test text Test text tab Test text Test text Test text Test text Test text Test text Test text Test text Test text Test text Test text Test text tab Test text Test text Test text Test text Test text Test text Test text Test text");
		update();
	}
	
	public void setText(String string) {
		this.textModel.setText(string);
	}
	
	public String getText(){
		return textModel.getText();
	}
	
	public void update(){
		//update line widths
		updateLineWidths();
		updateStringWidths();
		//updateStringWidths2();
		updateText();
		updateOutline();
		//divide text into line widths
	}

	private void updateText() {
		//remove previous texts
		//remove previous lines
		List<Node> children = this.getChildren();
		for(int i = 0; i < children.size(); i++){
			if(children.get(i) instanceof Text){
				this.getChildren().remove(i);
				i--;
			}
		}
		
		// TODO Auto-generated method stub
		for(int i = 0; i < lines.size(); i++){
			Text tempText = new Text();
			tempText.setFont(textModel.font);
			tempText.relocate(lines.get(i).x, lines.get(i).y);
			
			if(i < lines.size() - 1 && lines.get(i+1).startIndex > 0)
				tempText.setText(textModel.substring(lines.get(i).startIndex, lines.get(i+1).startIndex));
			else if(lines.get(i).startIndex >= 0)
				tempText.setText(textModel.substring(lines.get(i).startIndex));
			
			this.getChildren().add(tempText);
		}
	}

	private void updateStringWidths() {
		// TODO Very poor performance, figure out a solution with mutable strings
		int startIndex = 0;
		textCharSizes = new float[textModel.length()];
		for(int i = 0; i < textModel.length(); i++){
			if(textModel.charAt(i) == ' ' || textModel.charAt(i) == '\t'){
				textCharSizes[i] = textModel.metrics.computeStringWidth(textModel.substring(0,i));
			}
			if(textModel.charAt(i) == '\n'){
				startIndex = i;
			}
		}
		
		//start fitting text to lines
		double cumulativeLength = 0;
		int lineIndex = 0;
		lines.get(0).startIndex = 0;
		for(int i = 0; i < textCharSizes.length; i++){
			if(textCharSizes[i] == 0) continue;
			if(textCharSizes[i] > cumulativeLength + lines.get(lineIndex).width){
				if(lineIndex + 1 >= lines.size()){
					addNewLine();
				}
				lines.get(lineIndex + 1).startIndex = i;
				cumulativeLength += lines.get(lineIndex).width;
				lineIndex ++;
			}
		}
		
		//disable remaining lines
		for(int i = lineIndex; i < lines.size(); i++)
			lines.get(i).startIndex = -1;
	}

	private void addNewLine() {
		lines.add(new TextLine(-1));
	}

	/**
	 * Updates the border outline of the writable part
	 *//*
	private void updateOutline() {
		//remove previous lines
		List<Node> children = this.getChildren();
		for(int i = 0; i < children.size(); i++){
			if(children.get(i) instanceof Line){
				this.getChildren().remove(i);
				i--;
			}
		}
		
		//add new lines
		Line aboveLine = new Line(lines.get(0).x, lines.get(0).y, lines.get(0).x + lines.get(0).width, lines.get(0).y);
		this.getChildren().add(aboveLine);
		for(int i = 0; i < lines.size(); i++){
			
			Line leftLine = new Line(lines.get(i).x, lines.get(i).y, lines.get(i).x, lines.get(i).y + lines.get(i).height);
			leftLine.setStroke(Color.YELLOWGREEN);
			Line rightLine = new Line(lines.get(i).x + lines.get(i).width, lines.get(i).y, lines.get(i).x + lines.get(i).width, lines.get(i).y + lines.get(i).height);
			rightLine.setStroke(Color.BLUEVIOLET);
			
			Line belowLine1;
			Line belowLine2;
			
			if(i!= lines.size()-1){
				belowLine1 = new Line();
				belowLine1.setStartX(lines.get(i).x);
				belowLine1.setStartY(lines.get(i).y + lines.get(i).height);
				belowLine1.setEndX(lines.get(i+1).x);
				belowLine1.setEndY(lines.get(i+1).y);
				belowLine1.setStroke(Color.BLUE);
				
				belowLine2 = new Line();
				belowLine2.setStartX(lines.get(i).x + lines.get(i).width);
				belowLine2.setStartY(lines.get(i).y + lines.get(i).height);
				belowLine2.setEndX(lines.get(i+1).x + lines.get(i+1).width);
				belowLine2.setEndY(lines.get(i+1).y);
				belowLine2.setStroke(Color.RED);
				
				this.getChildren().addAll(belowLine1, belowLine2);
			}
			else{
				belowLine1 = new Line();
				belowLine1.setStartX(lines.get(i).x);
				belowLine1.setStartY(lines.get(i).y + lines.get(i).height);
				belowLine1.setEndX(lines.get(i).x + lines.get(i).width);
				belowLine1.setEndY(lines.get(i).y + lines.get(i).height);
				this.getChildren().add(belowLine1);
			}
			this.getChildren().addAll(leftLine, rightLine);
		}
		
		Line outerTop = new Line(0,0,maxWidth,0);
		Line outerLeft = new Line(0,0,0,maxHeight);
		Line outerRight = new Line(maxWidth,0,maxWidth,maxHeight);
		Line outerBottom = new Line(0,maxHeight,maxWidth,maxHeight);
		
		outerTop.setStrokeWidth(3);
		outerBottom.setStrokeWidth(3);
		outerRight.setStrokeWidth(3);
		outerLeft.setStrokeWidth(3);
		
		this.getChildren().addAll(outerTop, outerLeft, outerRight, outerBottom);
	}

	private void updateLineWidths(){
		Random debugRand = new Random();
		int count = debugRand.nextInt(20);
		for(int i = 0; i < count; i++){
			lines.add(new TextLine(0));
		}
		
		for(int i = 0; i < lines.size(); i++){
			lines.get(i).width = debugRand.nextInt((int) (maxWidth - 300)) + 100;
			lines.get(i).height = 25;
			lines.get(i).x = debugRand.nextInt(100);
			lines.get(i).y = i*25;
			lines.get(i).startIndex = 0;
		}
	}
	
	/**
	 * Inner class that defines a single line of text
	 * @author sahin
	 *
	 *//*
	class TextLine{
		double x,y,width,height; //phsical boundaries
		int startIndex;			 //these refer to indices in text. 
		
		TextLine(int startIndex){
			this.startIndex = startIndex;
		}
	}
	
	/**
	 * Event handling
	 *//*
	@Override
	public void handle(KeyEvent event) {
		if(event.getEventType() == KeyEvent.KEY_PRESSED){
			
		}
		else if(event.getEventType() == KeyEvent.KEY_RELEASED){
			
		}
		else if(event.getEventType() == KeyEvent.KEY_TYPED){
			
		}
	}
	
}
*/