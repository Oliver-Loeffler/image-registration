package net.raumzeitfalle.registration;

import java.util.*;
import java.util.function.*;

import net.raumzeitfalle.registration.displacement.Displacement;

public class DegreeOfFreedom implements Consumer<Displacement>, UnaryOperator<Displacement> {
	
	private final Set<Double> xLocations = new HashSet<>(1000);
	
	private final Set<Double> yLocations = new HashSet<>(1000);

	@Override
	public void accept(Displacement t) {
		double x  = t.getX();
		double dx = t.dX();
		
		double y = t.getY();
		double dy = t.dY();
		
		if (Double.isFinite(x) && Double.isFinite(dx)) {
			xLocations.add(x);
		}
		
		if (Double.isFinite(y) && Double.isFinite(dy)) {
			yLocations.add(y);
		}
	}
	
	public Orientation getDirection() {
		if (xLocations.size() == 0 && yLocations.size() == 0) {
			return Orientation.UNKNOWN;
		}
		
		if (xLocations.size() > 0 && yLocations.size() > 0) {
			return Orientation.XY;
		}
		
		if (xLocations.size() > 0) {
			return Orientation.X;
		}
		return Orientation.Y;	
	}
	
	@Override
	public Displacement apply(Displacement t) {
		accept(t);
		return t;
	}
	
	public int getX() {
		return this.xLocations.size()-1;
	}

	public int getY() {
		return this.yLocations.size()-1;
	}
	
	public int getCombined() {
		return Math.max(this.getX(),this.getY());
	}

	public int getDimensions() {
		return getDirection().getDimensions();
	}

}
