package net.raumzeitfalle.registration;

import java.util.function.Consumer;

public class Dimension implements Consumer<Direction> {

	private int xCount = 0;
	
	private int yCount = 0;
	
	@Override
	public void accept(Direction t) {
		if (t.equals(Direction.X)) xCount++;
		if (t.equals(Direction.Y)) yCount++;
	}

	public int getXCoordinateCount() {
		return xCount;
	}
	
	public int getYCoordinateCount() {
		return yCount;
	}
	
	public Direction getDirection() {
		if (xCount == 0 && yCount == 0) {
			return Direction.UNKNOWN;
		}
		
		if (xCount > 0 && yCount > 0) {
			return Direction.XY;
		}
		
		if (xCount > 0) {
			return Direction.X;
		}
		return Direction.Y;	
	}

	@Override
	public String toString() {
		return String.format("Dimension [%s (x=%s,y=%s)]", getDirection(), xCount, yCount);
	}
	
	

}
