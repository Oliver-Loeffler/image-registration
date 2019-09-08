package net.raumzeitfalle.registration.distortions;

import net.raumzeitfalle.registration.Transform;

public interface AffineTransform extends Transform {
	
	public double getTranslationX();

    public double getTranslationY();

    public double getScaleX();

    public double getScaleY();
    
    public double getMagnification();

    public double getOrthoX();

    public double getOrthoY();
    
    public double getOrtho();
    
    public double getCenterX();

    public double getCenterY();
    
}
