package net.raumzeitfalle.registration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.alignment.RigidTransform;
import net.raumzeitfalle.registration.displacement.Displacement;

class TransformCorrectionTest {

	@Test
	void apply() {
		
		Transform transform = new Transform() {
			
			@Override
			public Displacement apply(Displacement t) {
				return t.correctBy(10, -10);
			}
			
			@Override
			public boolean skip() { return false; }
		};
		
		TransformCorrection correctionFunction = new TransformCorrection();
		
		Collection<Displacement> displacements = Arrays.asList(Displacement.at(1, 1, 1000, 1000));
		
		Collection<Displacement> result = correctionFunction.apply(transform, displacements);
		
		assertEquals(1, result.size());
		
		result.forEach(d -> {
			assertEquals( 990, d.getXd());
			assertEquals(1010, d.getYd());
		});
	}

}
