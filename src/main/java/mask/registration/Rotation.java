package mask.registration;

import java.util.function.Function;

public class Rotation implements Function<Displacement, Displacement> {

	public static Rotation with(double tx, double ty, double rotation) {
		return new Rotation(tx, ty, rotation);
	}
	
	private final double translationX;
	
	private final double translationY;
	
	private final double rotation;

	private Rotation(double tx, double ty, double rot) {
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
		
		return    "Rotation [x=" + format(translationX) 
				+ ", y=" + format(translationY) + ", rotation="
				+ format(rotation * 1E6) + " urad ]";
	}
	
	private String format(double value) {
		return String.format("%.6f", value);
	}

	@Override
	public Displacement apply(Displacement d) {
		return Displacement.of(d.getX(), d.getY(), 
				d.getXd() + d.getY() * this.getRotation(), 
				d.getYd() - d.getX() * this.getRotation(), 
				d.getType());
	}
	
	
}
