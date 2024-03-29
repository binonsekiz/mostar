
package geometry.libgdxmath.utils.reflect;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/** Provides information about, and access to, a single field of a class or interface.
 * @author nexsoftware */
@SuppressWarnings("rawtypes")
public final class Field {

	private final java.lang.reflect.Field field;

	Field (java.lang.reflect.Field field) {
		this.field = field;
	}

	/** Returns the name of the field. */
	public String getName () {
		return field.getName();
	}

	/** Returns a Class object that identifies the declared type for the field. */
	public Class getType () {
		return field.getType();
	}

	/** Returns the Class object representing the class or interface that declares the field. */
	public Class getDeclaringClass () {
		return field.getDeclaringClass();
	}

	public boolean isAccessible () {
		return field.isAccessible();
	}

	public void setAccessible (boolean accessible) {
		field.setAccessible(accessible);
	}

	/** Return true if the field does not include any of the {@code private}, {@code protected}, or {@code public} modifiers. */
	public boolean isDefaultAccess () {
		return !isPrivate() && !isProtected() && !isPublic();
	}

	/** Return true if the field includes the {@code final} modifier. */
	public boolean isFinal () {
		return Modifier.isFinal(field.getModifiers());
	}

	/** Return true if the field includes the {@code private} modifier. */
	public boolean isPrivate () {
		return Modifier.isPrivate(field.getModifiers());
	}

	/** Return true if the field includes the {@code protected} modifier. */
	public boolean isProtected () {
		return Modifier.isProtected(field.getModifiers());
	}

	/** Return true if the field includes the {@code public} modifier. */
	public boolean isPublic () {
		return Modifier.isPublic(field.getModifiers());
	}

	/** Return true if the field includes the {@code static} modifier. */
	public boolean isStatic () {
		return Modifier.isStatic(field.getModifiers());
	}

	/** Return true if the field includes the {@code transient} modifier. */
	public boolean isTransient () {
		return Modifier.isTransient(field.getModifiers());
	}

	/** Return true if the field includes the {@code volatile} modifier. */
	public boolean isVolatile () {
		return Modifier.isVolatile(field.getModifiers());
	}

	/** Return true if the field is a synthetic field. */
	public boolean isSynthetic () {
		return field.isSynthetic();
	}

	/** If the type of the field is parameterized, returns the Class object representing the parameter type, null otherwise. */
	public Class getElementType () {
		Type genericType = field.getGenericType();
		if (genericType instanceof ParameterizedType) {
			Type[] actualTypes = ((ParameterizedType)genericType).getActualTypeArguments();
			if (actualTypes.length == 1) {
				Type actualType = actualTypes[0];
				if (actualType instanceof Class)
					return (Class)actualType;
				else if (actualType instanceof ParameterizedType) return (Class)((ParameterizedType)actualType).getRawType();
			}
		}
		return null;
	}

	/** Returns the value of the field on the supplied object. */
	public Object get (Object obj) throws ReflectionException {
		try {
			return field.get(obj);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException("Object is not an instance of " + getDeclaringClass(), e);
		} catch (IllegalAccessException e) {
			throw new ReflectionException("Illegal access to field: " + getName(), e);
		}
	}

	/** Sets the value of the field on the supplied object. */
	public void set (Object obj, Object value) throws ReflectionException {
		try {
			field.set(obj, value);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException("Argument not valid for field: " + getName(), e);
		} catch (IllegalAccessException e) {
			throw new ReflectionException("Illegal access to field: " + getName(), e);
		}
	}

}
