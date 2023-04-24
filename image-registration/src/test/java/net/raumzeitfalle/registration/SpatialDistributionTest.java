/*-
 * #%L
 * Image-Registration
 * %%
 * Copyright (C) 2019 - 2020 Oliver Loeffler, Raumzeitfalle.net
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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.displacement.Displacement;

class SpatialDistributionTest {
	
	private DegreesOfFreedom classUnderTest;

	@BeforeEach
	void prepare() {
		 classUnderTest = new DegreesOfFreedom();
	}
	
	@Test
	void area_2D_distribution() {
		
		List<Displacement> points = new ArrayList<>(5);
		points.add(Displacement.at(0, 0,      0,      0,      0,         0));
		points.add(Displacement.at(1, 1,      0, 140000,      0,    140000));
		points.add(Displacement.at(2, 2, 150000, 140000, 150000,    140000));
		points.add(Displacement.at(3, 3, 150000,      0, 150000,         0));
		points.add(Displacement.at(4, 4,  75000,  70000, Double.NaN, Double.NaN));
		
		points.forEach(classUnderTest);
		
		Distribution distribution = classUnderTest.getDistribution();
		
		assertEquals(Distribution.AREA, distribution);
		
	}
	
	@Test
	void horizontal_1D_distribution() {
		
		List<Displacement> points = new ArrayList<>(5);
		points.add(Displacement.at(0, 0,      0,  70000,      0,     70000));
		points.add(Displacement.at(1, 1,      0,  70000,      0,     70000));
		points.add(Displacement.at(2, 2, 150000,  70000, 150000,     70000));
		points.add(Displacement.at(3, 3, 150000,  70000, 150000,     70000));
		points.add(Displacement.at(4, 4,  75000,  70000, Double.NaN, Double.NaN));
		
		points.forEach(classUnderTest);
		
		Distribution distribution = classUnderTest.getDistribution();
		
		assertEquals(Distribution.HORIZONTAL, distribution);
		assertEquals("DegreesOfFreedom [xLocations=2, yLocations=1]", classUnderTest.toString());
		
	}
	
	@Test
	void vertical_1D_distribution() {
		
		List<Displacement> points = new ArrayList<>(5);
		points.add(Displacement.at(0, 0, 0,      0,     0,          0));
		points.add(Displacement.at(1, 1, 0, 140000,     0,     140000));
		points.add(Displacement.at(2, 2, 0, 140000,     0,     140000));
		points.add(Displacement.at(3, 3, 0,      0,     0,          0));
		points.add(Displacement.at(4, 4, 0,  70000, Double.NaN, Double.NaN));
			
		points.forEach(classUnderTest);
		
		Distribution distribution = classUnderTest.getDistribution();
		
		assertEquals(Distribution.VERTICAL, distribution);
		assertEquals("DegreesOfFreedom [xLocations=1, yLocations=2]", classUnderTest.toString());
	}
	
	@Test
	void singularity_XY() {
		
		List<Displacement> points = new ArrayList<>(5);
		points.add(Displacement.at(0, 0, 0, 140000,     0,      140000));
		points.add(Displacement.at(1, 1, 0, 140000,     0,      140000));
		points.add(Displacement.at(2, 2, 0, 140000,     0,      140000));
		points.add(Displacement.at(3, 3, 0, 140000,     0,      140000));
		points.add(Displacement.at(4, 4, 0,  70000, Double.NaN, Double.NaN));
			
		points.forEach(classUnderTest);
		
		Distribution distribution = classUnderTest.getDistribution();
		
		assertEquals(Distribution.SINGULARITY, distribution);
		assertEquals("DegreesOfFreedom [xLocations=1, yLocations=1]", classUnderTest.toString());
	}
	
	@Test
	void singularity_X() {
		
		List<Displacement> points = new ArrayList<>(5);
		points.add(Displacement.at(0, 0, 0, Double.NaN, 0,      Double.NaN));
			
		points.forEach(classUnderTest);
		
		Distribution distribution = classUnderTest.getDistribution();
		
		assertEquals(Distribution.SINGULARITY, distribution);
		assertEquals("DegreesOfFreedom [xLocations=1, yLocations=0]", classUnderTest.toString());
	}
	
	@Test
	void singularity_Y() {
		
		List<Displacement> points = new ArrayList<>(5);
		points.add(Displacement.at(0, 0, Double.NaN, 14000, Double.NaN, 14000));
			
		points.forEach(classUnderTest);
		
		Distribution distribution = classUnderTest.getDistribution();
		
		assertEquals(Distribution.SINGULARITY, distribution);
		assertEquals("DegreesOfFreedom [xLocations=0, yLocations=1]", classUnderTest.toString());
	}
	
	@Test
	void undefined() {
		
		List<Displacement> points = Collections.emptyList();
			
		points.forEach(classUnderTest);
		
		assertEquals("DegreesOfFreedom [xLocations=0, yLocations=0]", classUnderTest.toString());
		
		Throwable t = assertThrows(IllegalArgumentException.class,
				()->classUnderTest.getDistribution());
		
		assertEquals("Could not determine data distribution as no valid displacements have been processed yet.", t.getMessage());
		
	}
	
	
	@Test
	void useAsMappingFunction() {
		
		List<Displacement> points = new ArrayList<>(5);
		points.add(Displacement.at(0, 0,      0,      0,      0,         0));
		points.add(Displacement.at(1, 1,      0, 140000,      0,    140000));
		points.add(Displacement.at(2, 2, 150000, 140000, 150000,    140000));
		points.add(Displacement.at(3, 3, 150000,      0, 150000,         0));
		points.add(Displacement.at(4, 4,  75000,  70000, Double.NaN, Double.NaN));
		
		List<Displacement> copyOfPoints = points.stream()
				                                .map(classUnderTest)
				                                .collect(Collectors.toList());
		
		Distribution distribution = classUnderTest.getDistribution();
		
		
		assertEquals(points,copyOfPoints);
		assertEquals(Distribution.AREA, distribution);
		
	}

}
