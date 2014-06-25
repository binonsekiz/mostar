package document.visual;

import geometry.libgdxmath.LineSegment;
import geometry.libgdxmath.Polygon.LineSegmentIntersection;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import document.persistentproperties.VisualCompositeProperties;

public class VisualComposite extends VisualCompositeProperties implements VisualComponent {

	public VisualComposite() {
		children = new ArrayList<VisualComponent>();
	}
	
	public void addChild(VisualComponent child) {
		this.children.add(child);
	}
	
	public void removeChild(VisualComponent child) {
		this.children.remove(child);
	}
	
	public void removeChild(int index) {
		this.children.remove(index);
	}

	@Override
	public boolean contains(float mx, float my) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public LineSegmentIntersection intersectWithHeight(LineSegment segment,
			float height) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float area() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void draw(GraphicsContext overlayContext) {
		// TODO Auto-generated method stub
		
	}
}
