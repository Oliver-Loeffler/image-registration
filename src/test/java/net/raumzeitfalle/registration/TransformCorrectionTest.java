package net.raumzeitfalle.registration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.displacement.Displacement;
import net.raumzeitfalle.registration.distortions.AffineTransform;
import net.raumzeitfalle.registration.distortions.AffineTransformBuilder;
import net.raumzeitfalle.registration.distortions.SimpleAffineTransform;

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
	
	@Test
	void skip() {
		
		AffineTransform template = SimpleAffineTransform.with(0,0,0,0,0,0,0,0);
		AffineTransform skipTransform = new AffineTransformBuilder(template).build();
		
		assertTrue(skipTransform.skip());
		
		TransformCorrection correctionFunction = new TransformCorrection();
		
		Displacement sourceElement = Displacement.at(1, 1, 1000, 1000);
		Collection<Displacement> source = Arrays.asList(sourceElement);
		
		List<Displacement> result = (List<Displacement>) correctionFunction.apply(skipTransform, source);
		
		assertEquals(1, result.size());
		assertEquals(sourceElement, result.get(0));
	 	assertEquals(source, result);
	 	
	}

}
