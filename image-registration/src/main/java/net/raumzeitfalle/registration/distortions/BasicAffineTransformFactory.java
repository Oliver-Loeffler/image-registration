package net.raumzeitfalle.registration.distortions;

import java.util.Objects;

import net.raumzeitfalle.registration.OrientedOperation;
import net.raumzeitfalle.registration.solver.Solution;

class BasicAffineTransformFactory implements OrientedOperation<AffineTransform> {
	
	private final Solution solution;
	
	public BasicAffineTransformFactory(Solution solved) {
		this.solution = Objects.requireNonNull(solved, "solved must not be null");
	}

	@Override
	public AffineTransform getX() {
		double tx = this.solution.get(2);
		double ox = this.solution.get(1);
		double sx = this.solution.get(0);	
		return SimpleAffineTransform.forX(tx, sx, ox);
	}

	@Override
	public AffineTransform getY() {
		double ty = this.solution.get(2);
		double oy = this.solution.get(1);
		double sy = this.solution.get(0);
		return SimpleAffineTransform.forX(ty, sy, oy);
	}

	@Override
	public AffineTransform getCombined() {
		double sx = this.solution.get(0);
	    double sy = this.solution.get(1);
	    double ox = this.solution.get(2);
	    double oy = this.solution.get(3);
	    double tx = this.solution.get(4);
	    double ty = this.solution.get(5);
		return SimpleAffineTransform.forXY(tx, ty, sx, sy, ox, oy);
	}

}
