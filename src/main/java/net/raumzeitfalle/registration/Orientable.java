package net.raumzeitfalle.registration;

public interface Orientable {
	Orientation getOrientation();
	
	default boolean matches(Orientation orientation) {
		return getOrientation().equals(orientation);
	}
}
