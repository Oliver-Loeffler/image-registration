package net.raumzeitfalle.registration;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import net.raumzeitfalle.registration.displacement.Displacement;

public class TransformCorrection implements BiFunction<Transform, Collection<Displacement>, Collection<Displacement>> {

	@Override
	public Collection<Displacement> apply(Transform t, Collection<Displacement> u) {

		if (t.skip()) {
			return u;
		}
		
		return u.stream()
				.map(t)
				.collect(Collectors.toList());
		
	}

}
