package net.raumzeitfalle.registration;

public interface SkipTransform extends Transform {
	
	@Override
	default boolean skip() {
		return true;
	}
}
