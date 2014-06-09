package document.visual;

import javafx.scene.canvas.GraphicsContext;
import geometry.libgdxmath.LineSegment;
import geometry.libgdxmath.Polygon.LineSegmentIntersection;
import document.persistentproperties.interfaces.PersistentObject;

public interface VisualComponent extends PersistentObject{

	boolean contains(float mx, float my);

	LineSegmentIntersection intersectWithHeight(LineSegment segment, float height);

	float area();

	void draw(GraphicsContext overlayContext);

}
