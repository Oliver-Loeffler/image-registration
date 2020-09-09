package net.raumzeitfalle.registration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.*;

import net.raumzeitfalle.registration.alignment.*;
import net.raumzeitfalle.registration.displacement.Displacement;
import net.raumzeitfalle.registration.distortions.*;

class AffineTransformDegreesOfFreedomTest {
	
	private DegreesOfFreedom classUnderTest;
	
	@BeforeEach
	public void prepare() {
		 classUnderTest = new DegreesOfFreedom();
	}
	
	@Test
	void translation_scaling_skew_enabled_XY() {
		
		/*
		 * 6 Parameters:  -> requires 6 equations (3 x-equations, 3 y-equations)
		 * 
		 * translation(x,y)
		 * scale(x,y)
		 * ortho(x,y)
		 * 
		 */
		List<Displacement> undisplaced = new ArrayList<>(4);
		undisplaced.add(Displacement.at(0, 0,    0,    0,    0,    0));
		undisplaced.add(Displacement.at(1, 1,    0, 8000,    0, 8000));
		undisplaced.add(Displacement.at(2, 2, 8000, 8000, 8000, 8000));

		Translation translation = SimpleTranslation.with(0.010, -0.020);
		AffineTransform mytransform = SimpleAffineTransform.with(translation,
				-1E-6, -2E-6, -1E-6, -3E-6, 0, 0);
		
		List<Displacement> displaced = undisplaced.stream()
												  .map(mytransform)
												  .collect(Collectors.toList());
		
		displaced.forEach(classUnderTest);
		
		assertEquals( 2, classUnderTest.getX(), "degree of freedom in X");
		assertEquals( 2, classUnderTest.getY(), "degree of freedom in Y");
		assertEquals( 2, classUnderTest.getCombined(), "degree of freedom in XY");
		
		AffineTransform transform = calculateTransform(displaced);
		
		assertNotNull(transform);
		
	}

	private AffineTransform calculateTransform(List<Displacement> undisplaced) {
		return new AffineTransformCalculation().apply(undisplaced, d->true);
	}
	
	public List<Displacement> listOf(Displacement... d) {
		return Arrays.asList(d);
	}

}
