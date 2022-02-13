/*-
 * #%L
 * Image-Registration
 * %%
 * Copyright (C) 2019 Oliver Loeffler, Raumzeitfalle.net
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
		String cr = System.lineSeparator(); 
		String expected = cr +
				"DisplacementSummary" + cr + 
				"Summary			X		Y		ISO" + cr +
				"Mean		1000.00000	-500.00000" + cr +
				"3Sigma		2658041.46653	2802210.01652" + cr +
				"Min		-999000.00000	-1100500.00000" + cr +
				"Max		1011000.00000	1099500.00000" + cr +
				"Scales:		-297893218.81345	-357175653.46677	-327534436.14011" + cr +
				"Orthos:		-707106781.18655	-642824346.53323	64282434.65332" + cr + 
				"Rotation:					671911421.03699" + cr + 
				"Sites		4		4" + cr + 
				"";
		
		assertEquals(expected,text);
	}
	
	@Test
	void toStringMethodWithoutValues() {
		
		classUnderTest = DisplacementSummary.over(Collections.emptyList(), d->true);
		
		String text = classUnderTest.toString();
		String cr = System.lineSeparator(); 
		String expected = cr + 
				"DisplacementSummary" + cr + 
				"Summary			X		Y		ISO" + cr + 
				"Mean		   0.00000	   0.00000" + cr +
				"3Sigma		       NaN	       NaN" + cr + 
				"Min		  Infinity	  Infinity" + cr +
				"Max		 -Infinity	 -Infinity" + cr + 
				"Scales:		   0.00000	   0.00000	   0.00000" + cr + 
				"Orthos:		   0.00000	   0.00000	   0.00000" + cr +
				"Rotation:					   0.00000" + cr + 
				"Sites		0		0" + cr + 
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
