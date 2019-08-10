package mask.registration.distortions;

import java.util.function.Function;

import mask.registration.Displacement;

public interface AffineTransform extends Function<Displacement, Displacement>  {

	double getTranslationX();
	double getTranslationY();
	
    double getScaleX();
    double getScaleY();

    
    double getOrthoX();
    double getOrthoY();
    
    double getCenterX();
    double getCenterY();
    
}
