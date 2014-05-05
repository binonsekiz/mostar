
package geometry.libgdxmath.utils.reflect;

/** Thrown when an exception occurs during reflection.
 * @author nexsoftware
 */
public class ReflectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1535296903639873309L;

	public ReflectionException () {
		super();
	}

	public ReflectionException (String message) {
		super(message);
	}

	public ReflectionException (Throwable cause) {
		super(cause);
	}

	public ReflectionException (String message, Throwable cause) {
		super(message, cause);
	}

}
