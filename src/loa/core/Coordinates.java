package loa.core;


/**
 * Created by ryang on 14/06/2017.
 */
public class Coordinates {

	public Coordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	/**
	 * From java.awt.geom.Point2D hashCode
	 * @return int
	 */
	@Override
	public int hashCode() {
		long bits = java.lang.Double.doubleToLongBits(getX());
		bits ^= java.lang.Double.doubleToLongBits(getY()) * 31;
		return (((int) bits) ^ ((int) (bits >> 32)));
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Coordinates) {
			Coordinates pt = (Coordinates)obj;
			return (x == pt.x) && (y == pt.y);
		}
		return super.equals(obj);
	}
	@Override
	public String toString() {
		return getClass().getName() + "[x=" + x + ",y=" + y + "]";
	}


	private int x;
	private int y;
}
