package mask.registration.alignment;

import java.util.function.Function;

import mask.registration.Displacement;

public class AlignmentTransform implements Function<Displacement, Displacement> {

	public static AlignmentTransform with(double translationX, double translationY, double rotation) {
		return new AlignmentTransform(translationX, translationY, rotation);
	}
	
	private final double translationX;
	
	private final double translationY;
	
	private final double rotation;

	private AlignmentTransform(double tx, double ty, double rot) {
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
		return String.format("%.6f", value);
	}

	@Override
	public Displacement apply(Displacement d) {
		return Displacement.of(d.getX(), d.getY(), 
				d.getXd() - this.getTranslationX() + d.getY() * this.getRotation(), 
				d.getYd() - this.getTranslationY() - d.getX() * this.getRotation(), 
				d.getType());
	}
	
	
}
