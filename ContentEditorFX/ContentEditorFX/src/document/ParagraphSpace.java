package document;

import geometry.libgdxmath.Polygon;
import geometry.libgdxmath.Rectangle;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * This is a small set of ParagraphWithStyle's bundled in a single shape.
 * @author sahin
 *
 */
public class ParagraphSpace {

	private ArrayList<Paragraph> paragraphs;
	private Polygon allowedShape;
	private Column parent;
	
	public ParagraphSpace(Column parent, Polygon shape) {
		this.parent = parent;
		paragraphs = new ArrayList<Paragraph>();
		this.allowedShape = shape;
		System.out.println("PARAGRAPH SHAPE: " + shape);
	}

	public Polygon getShape() {
		return allowedShape;
	}

	public void setShape(Polygon allowedShape) {
		this.allowedShape = allowedShape;
	}

	public void draw(GraphicsContext graphicsContext) {
		graphicsContext.setStroke(Color.GAINSBORO);
		graphicsContext.setLineWidth(2);
		allowedShape.draw(graphicsContext);
	}
	
	
}
