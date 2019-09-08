package net.raumzeitfalle.registration;

import java.util.function.Consumer;
import java.util.function.Function;

public class Dimension<T extends Orientable> implements Consumer<Orientable>, Function<T,T> {

	private int xCount = 0;
	
	private int yCount = 0;
	
	@Override
	public void accept(Orientable t) {
		if (Orientation.X.equals(t.getOrientation())) xCount++;
		if (Orientation.Y.equals(t.getOrientation())) yCount++;
	}
	
	@Override
	public T apply(T t) {
		this.accept(t);
		return t;
	}

	public int getXCoordinateCount() {
		return xCount;
	}
	
	public int getYCoordinateCount() {
		return yCount;
	}
	
	public Orientation getDirection() {
		if (xCount == 0 && yCount == 0) {
			return Orientation.UNKNOWN;
		}
		
		if (xCount > 0 && yCount > 0) {
			return Orientation.XY;
		}
		
		if (xCount > 0) {
			return Orientation.X;
		}
		return Orientation.Y;	
	}

	public int getDimensions() {
		if (xCount == 0 && yCount == 0) {
			return 0;
		}
		if (xCount > 0 && yCount > 0) {
			return 2;
		}
		return 1;
	}
	
	@Override
	public String toString() {
		return String.format("Dimension [%s (x=%s,y=%s)]", getDirection(), xCount, yCount);
	}
}
