/*-
 * #%L
 * Image-Registration
 * %%
 * Copyright (C) 2019, 2021 Oliver Loeffler, Raumzeitfalle.net
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
package net.raumzeitfalle.registration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.displacement.Displacement;

class RigidBodyDegreesOfFreedomTest {
	
	private DegreesOfFreedom classUnderTest;
	
	@BeforeEach
	public void prepare() {
		 classUnderTest = new DegreesOfFreedom();
	}
	
	@Test
	void singularityXY() {
		
		classUnderTest.accept(Displacement.at(0, 0, -1.0, 1.0, -1.1, 1.1));
		classUnderTest.accept(Displacement.at(0, 0, -1.0, 1.0, -1.1, 1.1));
		
		assertEquals( 1, classUnderTest.getX(), "degree of freedom in X");
		assertEquals( 1, classUnderTest.getY(), "degree of freedom in Y");
		assertEquals( 1, classUnderTest.getCombined(), "degree of freedom in XY");
		
	}
	
	@Test
	void singularityX() {
		
		classUnderTest.accept(Displacement.at(0, 0, -1.0, 1.0, -1.1, Double.NaN));
		classUnderTest.accept(Displacement.at(0, 0, -1.0, 1.0, -1.1, Double.NaN));
		
		assertEquals( 1, classUnderTest.getX(), "degree of freedom in X");
		assertEquals( 0, classUnderTest.getY(), "degree of freedom in Y");
		assertEquals( 1, classUnderTest.getCombined(), "degree of freedom in XY");
		
	}
	
	@Test
	void singularityY() {
		
		classUnderTest.accept(Displacement.at(0, 0, -1.0, 1.0, Double.NaN, 1.1));
		classUnderTest.accept(Displacement.at(0, 0, -1.0, 1.0, Double.NaN, 1.1));
		
		assertEquals( 0, classUnderTest.getX(), "degree of freedom in X");
		assertEquals( 1, classUnderTest.getY(), "degree of freedom in Y");
		assertEquals( 1, classUnderTest.getCombined(), "degree of freedom in XY");
		
	}
	
	@Test
	void none() {
		
		classUnderTest.accept(Displacement.at(0, 0, -1.0, 1.0, Double.NaN, Double.NaN));
		classUnderTest.accept(Displacement.at(0, 0, Double.NaN, Double.NaN, 
															   Double.NaN, Double.NaN));
		
		assertEquals( 0, classUnderTest.getX(), "degree of freedom in X");
		assertEquals( 0, classUnderTest.getY(), "degree of freedom in Y");
		assertEquals( 0, classUnderTest.getCombined(), "degree of freedom in XY");
		
	}
	
	@Test
	void trans_rot_x_enabled() {
			
		classUnderTest.accept(Displacement.at(0, 0, -1.0, Double.NaN, -1.1, Double.NaN));
		assertEquals( 1, classUnderTest.getX(), "degree of freedom in X");

		classUnderTest.accept(Displacement.at(0, 0, -1.0, Double.NaN, -1.2, Double.NaN));
		assertEquals( 1, classUnderTest.getX(), "degree of freedom in X");
		
		classUnderTest.accept(Displacement.at(0, 0, -2.0, Double.NaN, -1.2, Double.NaN));
		assertEquals( 2, classUnderTest.getX(), "degree of freedom in X");
		
		assertEquals( 0, classUnderTest.getY(), "degree of freedom in Y");
		assertEquals( 2, classUnderTest.getCombined(), "degree of freedom in XY");
		
	}
	
	@Test
	void trans_rot_y_enabled() {
			
		classUnderTest.accept(Displacement.at(0, 0, Double.NaN, 1.1, Double.NaN, 1.2));
		assertEquals( 1, classUnderTest.getY(), "degree of freedom in Y");

		classUnderTest.accept(Displacement.at(0, 0, Double.NaN, 1.1, Double.NaN, 1.2));
		assertEquals( 1, classUnderTest.getY(), "degree of freedom in Y");
		
		classUnderTest.accept(Displacement.at(0, 0, Double.NaN, 2.0, Double.NaN, 1.2));
		assertEquals( 2, classUnderTest.getY(), "degree of freedom in Y");
		
		assertEquals( 0, classUnderTest.getX(), "degree of freedom in X");
		assertEquals( 2, classUnderTest.getCombined(), "degree of freedom in XY");
		
	}
	
	@Test
	void full() {
		
		classUnderTest.accept(Displacement.at(0, 0, -1.0, -1.0, -1.1, -1.1));
		classUnderTest.accept(Displacement.at(0, 0,  1.0, -1.0,  1.1, -1.1));
		
		classUnderTest.accept(Displacement.at(0, 0, -2.0,  2.0, -2.1,  2.1));
		classUnderTest.accept(Displacement.at(0, 0,  2.0,  2.0,  2.1,  2.1));
		
		
		assertEquals( 4, classUnderTest.getX(), "degree of freedom in X");
		assertEquals( 2, classUnderTest.getY(), "degree of freedom in Y");
		assertEquals( 4, classUnderTest.getCombined(), "degree of freedom in XY");
		
	}

}
