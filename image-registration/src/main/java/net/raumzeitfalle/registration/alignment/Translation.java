package net.raumzeitfalle.registration.alignment;

import net.raumzeitfalle.registration.Transform;
import net.raumzeitfalle.registration.displacement.Displacement;

public interface Translation extends Transform {
	
	public static Translation of(double x, double y) {
		return SimpleTranslation.with(x, y);
	}

	double getTranslationX();
	double getTranslationY();
	
	@Override
	default Displacement apply(Displacement source) {
		return Displacement.from(source, 
				source.getXd() - this.getTranslationX(), 
				source.getYd() - this.getTranslationY());
	}

}
