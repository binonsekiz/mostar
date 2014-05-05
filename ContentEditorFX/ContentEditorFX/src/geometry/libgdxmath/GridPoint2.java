package geometry.libgdxmath;

/**
 * A point in a 2D grid, with integer x and y coordinates
 * @author badlogic
 *
 */
@SuppressWarnings("ucd")
public class GridPoint2 {
	public int x;
	public int y;
	
	public GridPoint2() {
	}
	
	@SuppressWarnings("ucd")
	public GridPoint2(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@SuppressWarnings("ucd")
	public GridPoint2(GridPoint2 point) {
		this.x = point.x;
		this.y = point.y;
	}
	
	@SuppressWarnings("ucd")
	public GridPoint2 set(GridPoint2 point) {
		this.x = point.x;
		this.y = point.y;
		return this;
	}
	
	@SuppressWarnings("ucd")
	public GridPoint2 set(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}
}
