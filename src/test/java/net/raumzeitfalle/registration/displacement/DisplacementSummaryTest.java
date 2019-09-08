package net.raumzeitfalle.registration.displacement;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class DisplacementSummaryTest {

	private static final double POS_TOLERANCE = 1E-6;
	
	private DisplacementSummary classUnderTest;
	
	@Test
	void design() {
		
		classUnderTest = DisplacementSummary.over(getRotated(), d->true);
		
		assertEquals(  333.33, classUnderTest.designMeanX(), 1E-1);
		assertEquals( -366.66, classUnderTest.designMeanY(), 1E-1);
		
		assertEquals( -1000.0, classUnderTest.designMinX(), POS_TOLERANCE);
		assertEquals( -1100.0, classUnderTest.designMinY(), POS_TOLERANCE);
		
		assertEquals(  1000.0, classUnderTest.designMaxX(), POS_TOLERANCE);
		assertEquals(  1100.0, classUnderTest.designMaxY(), POS_TOLERANCE);
		
	}
	
	
	@Test
	void deltas() {
		
		classUnderTest = DisplacementSummary.over(getRotated(), d->true);
		
		assertEquals(    1.0, classUnderTest.meanX(), POS_TOLERANCE);
		assertEquals(   -0.5, classUnderTest.meanY(), POS_TOLERANCE);
		
		assertEquals( -999.0, classUnderTest.minX(), POS_TOLERANCE);
		assertEquals(-1100.5, classUnderTest.minY(), POS_TOLERANCE);
		
		assertEquals( 1011.0, classUnderTest.maxX(), POS_TOLERANCE);
		assertEquals( 1099.5, classUnderTest.maxY(), POS_TOLERANCE);
		
		assertEquals( 2658.0, classUnderTest.sd3X(), 1E-1);
		assertEquals( 2802.2, classUnderTest.sd3Y(), 1E-1);
		
		assertEquals( 4, classUnderTest.sizeX() );
		assertEquals( 4, classUnderTest.sizeY() );
		
	}
	
	@Test
	void displacedMean() {
		
		classUnderTest = DisplacementSummary.over(getRotated(), d->true);
		
		assertEquals(  1.0, classUnderTest.displacedMeanX(), POS_TOLERANCE);
		assertEquals( -0.5, classUnderTest.displacedMeanY(), POS_TOLERANCE);
		
	}
	
	@Test
	void emptyCollection() {
		
		classUnderTest = DisplacementSummary.over(Collections.emptyList(), d->true);
		
		// deltas
		assertEquals(    0.0, classUnderTest.meanX(), POS_TOLERANCE);
		assertEquals(    0.0, classUnderTest.meanY(), POS_TOLERANCE);
		
		assertFalse( Double.isFinite(classUnderTest.minX()) );
		assertFalse( Double.isFinite(classUnderTest.minY()) );
		
		assertFalse( Double.isFinite(classUnderTest.maxX()) );
		assertFalse( Double.isFinite(classUnderTest.maxY()) );
		
		assertEquals( Double.NaN, classUnderTest.sd3X(), 1E-1);
		assertEquals( Double.NaN, classUnderTest.sd3Y(), 1E-1);
		
		assertEquals( 0, classUnderTest.sizeX() );
		assertEquals( 0, classUnderTest.sizeY() );
		
		
		// displaced mean
		assertEquals(  0.0, classUnderTest.displacedMeanX(), POS_TOLERANCE);
		assertEquals(  0.0, classUnderTest.displacedMeanY(), POS_TOLERANCE);
		
		// design
		assertEquals(  0.0, classUnderTest.designMeanX(), 1E-1);
		assertEquals(  0.0, classUnderTest.designMeanY(), 1E-1);
		
		assertFalse( Double.isFinite(classUnderTest.designMinX()) );
		assertFalse( Double.isFinite(classUnderTest.designMinX()) );
		
		assertFalse( Double.isFinite(classUnderTest.designMaxX()) );
		assertFalse( Double.isFinite(classUnderTest.designMaxY()) );
		
	}
	
	@Test
	void toStringMethodWithValues() {
		
		classUnderTest = DisplacementSummary.over(getRotated(), d->true);
		
		String text = classUnderTest.toString();
		
		String expected = "\n" + 
				"DisplacementSummary\n" + 
				"Summary			X		Y		ISO\n" + 
				"Mean		1000.00000	-500.00000\n" + 
				"3Sigma		2658041.46653	2802210.01652\n" + 
				"Min		-999000.00000	-1100500.00000\n" + 
				"Max		1011000.00000	1099500.00000\n" + 
				"Scales:		-297893218.81345	-357175653.46677	-327534436.14011\n" + 
				"Orthos:		-707106781.18655	-642824346.53323	64282434.65332\n" + 
				"Rotation:					671911421.03699\n" + 
				"Sites		4		4\n" + 
				"";
		
		assertEquals(expected,text);
	}
	
	@Test
	void toStringMethodWithoutValues() {
		
		classUnderTest = DisplacementSummary.over(Collections.emptyList(), d->true);
		
		String text = classUnderTest.toString();
		
		String expected = "\n" + 
				"DisplacementSummary\n" + 
				"Summary			X		Y		ISO\n" + 
				"Mean		   0.00000	   0.00000\n" + 
				"3Sigma		       NaN	       NaN\n" + 
				"Min		  Infinity	  Infinity\n" + 
				"Max		 -Infinity	 -Infinity\n" + 
				"Scales:		   0.00000	   0.00000	   0.00000\n" + 
				"Orthos:		   0.00000	   0.00000	   0.00000\n" + 
				"Rotation:					   0.00000\n" + 
				"Sites		0		0\n" + 
				"";
		
		assertEquals(expected,text);
	}

	
	private List<Displacement> getRotated() {
		List<Displacement> displaced = new ArrayList<>(4);
		
		double rotated = Math.sqrt(2)*1E3;
		double tx =  1.0;
		double ty = -0.5;
		
		displaced.add(Displacement.at(0, 0, -1000, -1100,        0+tx+10, -rotated+ty));
		displaced.add(Displacement.at(0, 0, -1000,  1100, -rotated+tx,           0+ty));
		displaced.add(Displacement.at(0, 0,  1000,  1100,        0+tx,     rotated+ty));
		displaced.add(Displacement.at(0, 0,  1000, -1100,  rotated+tx-10,  Double.NaN));
		displaced.add(Displacement.at(0, 0,  1000, -1100,     Double.NaN,  Double.NaN));
		displaced.add(Displacement.at(0, 0,  1000, -1100,     Double.NaN,        0+ty));
		return displaced;
	}

}
