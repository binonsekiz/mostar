package geometry.libgdxmath;

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 ******************************************************************************/
import java.io.Serializable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import storage.XmlManager;
import document.persistentproperties.interfaces.PersistentObject;

/** Encapsulates a 2D rectangle defined by it's bottom corner point and its extends in x (width) and y (height).
 * @author badlogicgames@gmail.com */
public class Rectangle implements Serializable, PersistentObject{
	/** Static temporary rectangle. Use with care! Use only when sure other code will not also use this. */
	static public final Rectangle tmp = new Rectangle();

	/** Static temporary rectangle. Use with care! Use only when sure other code will not also use this. */
	static public final Rectangle tmp2 = new Rectangle();

	private static final long serialVersionUID = 5733252015138115702L;
	public float x, y;
	public float width, height;

	/** Constructs a new rectangle with all values set to zero */
	public Rectangle () {

	}
	
	public Node saveToXmlNode(Document doc) {
		Element rectElement = doc.createElement("Rectangle");
		
		XmlManager.insertNumberElement(doc, rectElement, "X", x);
		XmlManager.insertNumberElement(doc, rectElement, "Y", y);
		XmlManager.insertNumberElement(doc, rectElement, "Width", width);
		XmlManager.insertNumberElement(doc, rectElement, "Height", height);

		return rectElement;
	}
	
	@Override
	public void loadFromXmlElement(Element element) {
		x = XmlManager.loadNumberFromXmlElement("X", element).floatValue();
		y = XmlManager.loadNumberFromXmlElement("Y", element).floatValue();
		width = XmlManager.loadNumberFromXmlElement("Width", element).floatValue();
		height = XmlManager.loadNumberFromXmlElement("Height", element).floatValue();
	}

	/** Constructs a new rectangle with the given corner point in the bottom left and dimensions.
	 * @param x The corner point x-coordinate
	 * @param y The corner point y-coordinate
	 * @param width The width
	 * @param height The height */
	public Rectangle (float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/** Constructs a rectangle based on the given rectangle
	 * @param rect The rectangle */
	public Rectangle (Rectangle rect) {
		x = rect.x;
		y = rect.y;
		width = rect.width;
		height = rect.height;
	}

	public Rectangle(Element element) {
		loadFromXmlElement(element);
	}

	/** @param x bottom-left x coordinate
	 * @param y bottom-left y coordinate
	 * @param width width
	 * @param height height
	 * @return this rectangle for chaining */
	public Rectangle set (float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		return this;
	}

	/** @return the x-coordinate of the bottom left corner */
	public float getX () {
		return x;
	}

	/** Sets the x-coordinate of the bottom left corner
	 * @param x The x-coordinate
	 * @return this rectangle for chaining */
	public Rectangle setX (float x) {
		this.x = x;
		
		return this;
	}

	/** @return the y-coordinate of the bottom left corner */
	public float getY () {
		return y;
	}

	/** Sets the y-coordinate of the bottom left corner
	 * @param y The y-coordinate 
	 * @return this rectangle for chaining */
	public Rectangle setY (float y) {
		this.y = y;
		
		return this;
	}

	/** @return the width */
	public float getWidth () {
		return width;
	}

	/** Sets the width of this rectangle
	 * @param width The width 
	 * @return this rectangle for chaining */
	public Rectangle setWidth (float width) {
		this.width = width;
		
		return this;
	}

	/** @return the height */
	public float getHeight () {
		return height;
	}

	/** Sets the height of this rectangle
	 * @param height The height 
	 * @return this rectangle for chaining */
	public Rectangle setHeight (float height) {
		this.height = height;
		
		return this;
	}

	/** return the Vector2 with coordinates of this rectangle
	 * @param position The Vector2 */
	public Vector2 getPosition (Vector2 position) {
		return position.set(x, y);
	}

	/** Sets the x and y-coordinates of the bottom left corner from vector
	 * @param position The position vector
	 * @return this rectangle for chaining */
	public Rectangle setPosition (Vector2 position) {
		this.x = position.x;
		this.y = position.y;
		
		return this;
	}

	/** Sets the x and y-coordinates of the bottom left corner
	 * @param x The x-coordinate
	 * @param y The y-coordinate 
	 * @return this rectangle for chaining */
	@SuppressWarnings("ucd")
	public Rectangle setPosition (float x, float y) {
		this.x = x;
		this.y = y;
		
		return this;
	}

	/** Sets the width and height of this rectangle
	 * @param width The width
	 * @param height The height 
	 * @return this rectangle for chaining */
	@SuppressWarnings("ucd")
	public Rectangle setSize (float width, float height) {
		this.width = width;
		this.height = height;
		
		return this;
	}
	
	/**
	 * Relocates the rectangle with respect to given coordinates
	 * @param dx
	 * @param dy
	 * @return
	 */
	public Rectangle translate(float dx, float dy){
		this.x += dx;
		this.y += dy;
		
		return this;
	}

	/** Sets the squared size of this rectangle
	 * @param sizeXY The size 
	 * @return this rectangle for chaining */
	public Rectangle setSize (float sizeXY) {
		this.width = sizeXY;
		this.height = sizeXY;
		
		return this;
	}

	/** @return the Vector2 with size of this rectangle
	 * @param size The Vector2 */
	public Vector2 getSize (Vector2 size) {
		return size.set(width, height);
	}

	/** @param x point x coordinate
	 * @param y point y coordinate
	 * @return whether the point is contained in the rectangle */
	public boolean contains (float x, float y) {
		return this.x <= x && this.x + this.width >= x && this.y <= y && this.y + this.height >= y;
	}

	/** @param vector The coordinates vector
	 * @return whether the vector is contained in the rectangle */
	public boolean contains (Vector2 vector) {
		return contains(vector.x, vector.y);
	}

	/** @param rectangle the other {@link Rectangle}.
	 * @return whether the other rectangle is contained in this rectangle. */
	public boolean contains (Rectangle rectangle) {
		float xmin = rectangle.x;
		float xmax = xmin + rectangle.width;

		float ymin = rectangle.y;
		float ymax = ymin + rectangle.height;

		return ((xmin > x && xmin < x + width) && (xmax > x && xmax < x + width))
			&& ((ymin > y && ymin < y + height) && (ymax > y && ymax < y + height));
	}

	/** @param r the other {@link Rectangle}
	 * @return whether this rectangle overlaps the other rectangle. */
	public boolean overlaps (Rectangle r) {
		return x < r.x + r.width && x + width > r.x && y < r.y + r.height && y + height > r.y;
	}

	/** Sets the values of the given rectangle to this rectangle.
	 * @param rect the other rectangle
	 * @return this rectangle for chaining */
	public Rectangle set (Rectangle rect) {
		this.x = rect.x;
		this.y = rect.y;
		this.width = rect.width;
		this.height = rect.height;
		
		return this;
	}

	/** Merges this rectangle with the other rectangle.
	 * @param rect the other rectangle
	 * @return this rectangle for chaining */
	public Rectangle merge (Rectangle rect) {
		float minX = Math.min(x, rect.x);
		float maxX = Math.max(x + width, rect.x + rect.width);
		x = minX;
		width = maxX - minX;

		float minY = Math.min(y, rect.y);
		float maxY = Math.max(y + height, rect.y + rect.height);
		y = minY;
		height = maxY - minY;
		
		return this;
	}
	
	/** Calculates the aspect ratio ( width / height ) of this rectangle
	 * @return the aspect ratio of this rectangle.
	 * Returns Float.NaN if height is 0 to avoid ArithmeticException */
	public float getAspectRatio () {
		return (height == 0) ? Float.NaN : width / height;
	}
	
	/** Calculates the center of the rectangle. Results are located in the given Vector2
	 * @param vector the Vector2 to use 
	 * @return the given vector with results stored inside */
	public Vector2 getCenter (Vector2 vector) {
		vector.x = x + width / 2;
		vector.y = y + height / 2;
		return vector;
	}

	/** Moves this rectangle so that its center point is located at a given position
	 * @param x the position's x
	 * @param y the position's y
	 * @return this for chaining */
	public Rectangle setCenter (float x, float y) {
		setPosition(x - width / 2, y - height / 2);
		return this;
	}

	/** Moves this rectangle so that its center point is located at a given position
	 * @param position the position
	 * @return this for chaining */
	public Rectangle setCenter (Vector2 position) {
		setPosition(position.x - width / 2, position.y - height / 2);
		return this;
	}

	/** Fits this rectangle around another rectangle while maintaining aspect ratio
	 * This scales and centers the rectangle to the other rectangle
	 * (e.g. Having a camera translate and scale to show a given area)
	 * @param rect the other rectangle to fit this rectangle around
	 * @return this rectangle for chaining */
	public Rectangle fitOutside (Rectangle rect) {
		float ratio = getAspectRatio();

		if (ratio > rect.getAspectRatio()) {
			// Wider than tall
			setSize(rect.height * ratio, rect.height);
		} else {
			// Taller than wide
			setSize(rect.width, rect.width / ratio);
		}

		setPosition((rect.x + rect.width / 2) - width / 2, (rect.y + rect.height / 2) - height / 2);
		return this;
	}

	/** Fits this rectangle into another rectangle while maintaining aspect ratio.
	 * This scales and centers the rectangle to the other rectangle
	 * (e.g. Scaling a texture within a arbitrary cell without squeezing)
	 * @param rect the other rectangle to fit this rectangle inside
	 * @return this rectangle for chaining */
	public Rectangle fitInside (Rectangle rect) {
		float ratio = getAspectRatio();

		if (ratio < rect.getAspectRatio()) {
			// Taller than wide
			setSize(rect.height * ratio, rect.height);
		} else {
			// Wider than tall
			setSize(rect.width, rect.width / ratio);
		}

		setPosition((rect.x + rect.width / 2) - width / 2, (rect.y + rect.height / 2) - height / 2);
		return this;
	}

	public String toString () {
		return x + "," + y + "," + width + "," + height;
	}

	public Rectangle setPositionX(float floatValue) {
		this.x = floatValue;
		return this;
	}
	
	public Rectangle setPositionY(float floatValue) {
		this.y = floatValue;
		return this;
	}
}