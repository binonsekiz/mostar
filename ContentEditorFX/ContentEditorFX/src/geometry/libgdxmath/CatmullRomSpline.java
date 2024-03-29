/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package geometry.libgdxmath;


/** @author Xoppa */
@SuppressWarnings("ucd")
public class CatmullRomSpline<T extends Vector<T>> implements Path<T> {
	/** Calculates the catmullrom value for the given position (t).
	 * @param out The Vector to set to the result.
	 * @param t The position (0<=t<=1) on the spline
	 * @param points The control points
	 * @param continuous If true the b-spline restarts at 0 when reaching 1
	 * @param tmp A temporary vector used for the calculation
	 * @return The value of out */
	public static <T extends Vector<T>> T calculate(final T out, final float t, final T[] points, final boolean continuous, final T tmp) {
		final int n = continuous ? points.length : points.length - 3;
		float u = t * n;
		int i = (t >= 1f) ? (n - 1) : (int)u;
		u -= (float)i;
		return calculate(out, i, u, points, continuous, tmp);
	}
	
	/** Calculates the catmullrom value for the given span (i) at the given position (u).
	 * @param out The Vector to set to the result.
	 * @param i The span (0<=i<spanCount) spanCount = continuous ? points.length : points.length - degree
	 * @param u The position (0<=u<=1) on the span
	 * @param points The control points
	 * @param continuous If true the b-spline restarts at 0 when reaching 1
	 * @param tmp A temporary vector used for the calculation
	 * @return The value of out */
	public static <T extends Vector<T>> T calculate(final T out, final int i, final float u, final T[] points, final boolean continuous, final T tmp) {
		final int n = points.length;
		final float u2 = u * u;
		final float u3 = u2 * u;
		out.set(points[i]).scl(1.5f * u3 - 2.5f * u2 + 1.0f);
		if (continuous || i > 0) out.add(tmp.set(points[(n+i-1)%n]).scl(-0.5f * u3 + u2 - 0.5f * u));
		if (continuous || i < (n - 1)) out.add(tmp.set(points[(i + 1)%n]).scl(-1.5f * u3 + 2f * u2 + 0.5f * u));
		if (continuous || i < (n - 2)) out.add(tmp.set(points[(i + 2)%n]).scl(0.5f * u3 - 0.5f * u2));
		return out;
	}
	
	public T[] controlPoints;
	public boolean continuous;
	public int spanCount;
	private T tmp;
	private T tmp2;
	
	public CatmullRomSpline() { }
	public CatmullRomSpline(final T[] controlPoints, final boolean continuous) {
		set(controlPoints, continuous);
	}
	
	@SuppressWarnings("rawtypes")
	public CatmullRomSpline set(final T[] controlPoints, final boolean continuous) {
		if (tmp == null)
			tmp = controlPoints[0].cpy();
		if (tmp2 == null)
			tmp2 = controlPoints[0].cpy();
		this.controlPoints = controlPoints;
		this.continuous = continuous;
		this.spanCount = continuous ? controlPoints.length : controlPoints.length - 3;
		return this;
	}

	@Override
	public T valueAt (T out, float t) {
		final int n = spanCount;
		float u = t * n;
		int i = (t >= 1f) ? (n - 1) : (int)u;
		u -= (float)i;
		return valueAt(out, i, u);
	}
	
	/** @return The value of the spline at position u of the specified span */ 
	public T valueAt(final T out, final int span, final float u) {
		return calculate(out, continuous ? span : (span + 1), u, controlPoints, continuous, tmp);
	}
	
	/** @return The span closest to the specified value */ 
	public int nearest(final T in) {
		return nearest(in, 0, spanCount);
	}
	
	/** @return The span closest to the specified value, restricting to the specified spans. */
	public int nearest(final T in, int start, final int count) {
		while (start < 0) start += spanCount;
		int result = start % spanCount;
		float dst = in.dst2(controlPoints[result]);
		for (int i = 1; i < count; i++) {
			final int idx = (start + i) % spanCount;
			final float d = in.dst2(controlPoints[idx]);
			if (d < dst) {
				dst = d;
				result = idx;
			}
		}
		return result;
	}
	
	@Override
	public float approximate (T v) {
		return approximate(v, nearest(v));
	}
	
	public float approximate(final T in, int start, final int count) {
		return approximate(in, nearest(in, start, count));
	}
	
	public float approximate(final T in, final int near) {
		int n = near;
		final T nearest = controlPoints[n];
		final T previous = controlPoints[n>0?n-1:spanCount-1];
		final T next = controlPoints[(n+1)%spanCount];
		final float dstPrev2 = in.dst2(previous);
		final float dstNext2 = in.dst2(next);
		T P1, P2, P3;
		if (dstNext2 < dstPrev2) {
			P1 = nearest;
			P2 = next;
			P3 = in;
		} else {
			P1 = previous;
			P2 = nearest;
			P3 = in;
			n = n>0?n-1:spanCount-1;
		}
		float L1 = P1.dst(P2);
		float L2 = P3.dst(P2);
		float L3 = P3.dst(P1);
		float s = (L2*L2 + L1*L1 - L3*L3) / (2*L1);
		float u = MathUtils.clamp((L1-s)/L1, 0f, 1f);
		return ((float)n + u) / spanCount;
	}
	
	@Override
	public float locate (T v) {
		return approximate(v);
	}
}
