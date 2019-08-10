package mask.registration.alignment;

import mask.registration.Displacement;

/**
 * 
 * Allows to correct rotation and translation (x,y) for a given displacement.
 *  
 * @author oliver
 *
 */
class SimpleRigidTransform implements RigidTransform {

	public static SimpleRigidTransform with(double translationX, double translationY, double rotation) {
		return new SimpleRigidTransform(translationX, translationY, rotation);
	}
	
	private final double translationX;
	
	private final double translationY;
	
	private final double rotation;

	private SimpleRigidTransform(double tx, double ty, double rot) {
		this.translationX = tx;
		this.translationY = ty;
		this.rotation = rot;
	}

	public double getTranslationX() {
		return translationX;
	}

	public double getTranslationY() {
		return translationY;
	}

	public double getRotation() {
		return rotation;
	}

	@Override
	public String toString() {
		
		return    "AlignmentTransform [x=" + format(translationX) 
				+ ", y=" + format(translationY) + ", rotation="
				+ format(rotation * 1E6) + " urad ]";
	}
	
	private String format(double value) {
		return String.format("%10.7f", value);
	}

	@Override
	public Displacement apply(Displacement d) {
		return Displacement.from(d, 
				d.getXd() - this.getTranslationX() + d.getY() * this.getRotation(), 
				d.getYd() - this.getTranslationY() - d.getX() * this.getRotation());
	}
	
}
