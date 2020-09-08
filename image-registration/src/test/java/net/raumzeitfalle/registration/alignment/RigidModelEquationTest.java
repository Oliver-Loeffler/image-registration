package net.raumzeitfalle.registration.alignment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.*;
import net.raumzeitfalle.registration.displacement.Displacement;

class RigidModelEquationTest {
	
	private final double TOLERANCE = 1E-11;
	
	private RigidModelEquation equation;

	@Test
	void forX() {
		Displacement a = Displacement.at(0, 1, 2, 3, 10, -10);
		
		equation = RigidModelEquation.forX(a);
		
		assertEquals( 8.0, equation.getDeltaValue(), TOLERANCE, "X: DeltaValue");
		assertEquals(-3.0, equation.getDesignValue(), TOLERANCE, "X: DesignValue");
		assertEquals(Orientations.X, equation.getOrientation(), "Orientation");
		
		assertEquals("RigidModelEquation [xf=1.0, yf=0.0, designValue=-3.0, deltaValue=8.0, direction=X]",
				equation.toString());
	}
	
	@Test
	void forY() {
		Displacement a = Displacement.at(0, 1, 2, 3, 10, -10);
		
		equation = RigidModelEquation.forY(a);
		
		assertEquals(-13.0, equation.getDeltaValue(), TOLERANCE, "Y: DeltaValue");
		assertEquals(  2.0, equation.getDesignValue(), TOLERANCE, "Y: DesignValue");
		assertEquals(Orientations.Y, equation.getOrientation(), "Orientation");
		
		assertEquals("RigidModelEquation [xf=0.0, yf=1.0, designValue=2.0, deltaValue=-13.0, direction=Y]",
				equation.toString());
	}

}
