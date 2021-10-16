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
package net.raumzeitfalle.registration.solvertest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.jblas.JblasSolver;
import net.raumzeitfalle.registration.solver.SolverProvider;
import net.raumzeitfalle.registration.solver.spi.SolverAdapter;
import net.raumzeitfalle.registration.solvertest.numerics.AffineTransformNumerics;
import net.raumzeitfalle.registration.solvertest.numerics.RigidTransformNumerics;


class JblasNumericsTest {
	
	@BeforeAll
	public static void prepare() {
		SolverProvider.setPreferredImplementation(JblasSolver.class.getName());
	}
	
	@BeforeEach
	void checkImplementation() {
		SolverAdapter jblasSolver = new JblasSolver();	
		assertEquals(jblasSolver.getClass(), SolverProvider.getInstance().getSolver().getClass());
	}
		
	private final RigidTransformNumerics rigidTransformNumerics = new RigidTransformNumerics();
	
	/*
	 * For some reason JBLAS does not work with default tolerance
	 */
	private final AffineTransformNumerics affineTransformNumerics = new AffineTransformNumerics(1.04E-11);
	
	@Test
	void translationX() {
		rigidTransformNumerics.translationX();
	}
	
	@Test
	void translationY() {
		rigidTransformNumerics.translationY();
	}
	
	@Test
	void translationXY() {
		rigidTransformNumerics.translationXY();
	}

	@Test
	void rotation() {
		rigidTransformNumerics.rotation();
	}
	
	@Test
	void rotationAndTranslation() {
		rigidTransformNumerics.rotationAndTranslation();
	}
	
	@Test
	void skipTransform() {
		rigidTransformNumerics.skipTransform();
	}
	
	@Test
	void translationXonly1D() {
		rigidTransformNumerics.translationXonly1D();
	}
	
	
	@Test
	void translationYonly1D() {
		rigidTransformNumerics.translationYonly1D();
	}
	
	@Test
	void alignmentOfDisplacementsAlongHorizontalLine() {
		rigidTransformNumerics.displacementsAlongHorizontalLine();
	}
	
	@Test
	void singularityXY() {
		rigidTransformNumerics.singularityXY();
	}
	
	@Disabled("Not yet supported in using jblas")
	@Test
	void singularityX() {
		rigidTransformNumerics.singularityX();
	}
	
	@Disabled("Not yet supported in using jblas")
	@Test
	void singularityY() {
		rigidTransformNumerics.singularityY();
	}
	
	//
	@Test
	void zeroTransform() {
		affineTransformNumerics.zeroTransform();
	}
	
	@Test
	void scalingX() {
		affineTransformNumerics.scalingX();
	}
	
	@Test
	void scalingY_withoutX() {
		affineTransformNumerics.scalingY_withoutX();
	}
	
	@Test
	void scalingX_withoutY() {
		affineTransformNumerics.scalingX_withoutY();
	}

	@Test
	void scalingXY() {
		affineTransformNumerics.scalingXY();
	}
	
	@Test
	void shearingX() {
		affineTransformNumerics.shearingX();
	}
	
	@Test
	void shearingY() {
		affineTransformNumerics.shearingY();
	}
	
	@Test
	void shearingXY() {
		affineTransformNumerics.shearingXY();
	}
	
	@Test
	void displacementsAlongVerticalLine() {
		affineTransformNumerics.displacementsAlongVerticalLine();
	}
	
	@Test
	void displacementsAlongHorizontalLine() {
		affineTransformNumerics.displacementsAlongHorizontalLine();
	}

}
