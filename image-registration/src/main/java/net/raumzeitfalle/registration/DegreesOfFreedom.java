package net.raumzeitfalle.registration;

import java.util.*;
import java.util.function.*;

import net.raumzeitfalle.registration.displacement.Displacement;

/**
 * 
 * <h1>Rigid Body 3-parameter Model</h1>
 * <ul>
 * 	<li>DoF  = 0 -> no calculation possible</li>
 *  <li>DoF  = 1 -> translation only</li>
 *  <li>DoF >= 2 -> translation and rotation</li>
 * </ul>
 * 
 *
 */
public class DegreesOfFreedom implements Consumer<Displacement>, UnaryOperator<Displacement>, OrientedOperation<Integer> {
	
	private final Set<Double> xLocations = new HashSet<>(1000);
	
	private final Set<Double> yLocations = new HashSet<>(1000);

	@Override
	public void accept(Displacement t) {
		
		if (Double.isFinite(t.getXd())) 
			xLocations.add(Double.valueOf(t.getX()));
		
		if (Double.isFinite(t.getYd())) 
			yLocations.add(Double.valueOf(t.getY()));
		
	}
	
	public Orientation getDirection() {
		if (xLocations.size() == 0 && yLocations.size() == 0) {
			return Orientations.XY;
		}
		
		if (xLocations.size() > 0 && yLocations.size() > 0) {
			return Orientations.XY;
		}
		
		if (xLocations.size() > 0) {
			return Orientations.X;
		}
		return Orientations.Y;	
	}
	
	public Distribution getDistribution() {
		if (xLocations.isEmpty() && yLocations.isEmpty()) {
			throw new IllegalArgumentException("Could not determine data distribution as no valid displacements have been processed yet.");	
		}
		
		if (xLocations.size() > 1  && yLocations.size() > 1)
			return Distribution.AREA;
		
		if (xLocations.size() == 1 && yLocations.size() > 1)
			return Distribution.VERTICAL;
		
		if (yLocations.size() == 1 && xLocations.size() > 1)
			return Distribution.HORIZONTAL;
		
		return Distribution.SINGULARITY;
	}
	
	@Override
	public Displacement apply(Displacement t) {
		accept(t);
		return t;
	}
	
	public Integer getX() {
		return this.xLocations.size();
	}

	public Integer getY() {
		return this.yLocations.size();
	}
	
	public Integer getCombined() {
		return Math.max(this.getX(),this.getY());
		// return this.xLocations.size() + this.yLocations.size();
	}

	public int getDimensions() {
		return getDirection().getDimensions();
	}

	public int getDegrees() {
		Orientation orientation = this.getDirection();
		return orientation.runOperation(this);
	}

	@Override
	public String toString() {
		return String.format("DegreesOfFreedom [xLocations=%s, yLocations=%s]", xLocations.size(), yLocations.size());
	}
	
	
}
