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
		rigidTransformNumerics.assertTranslationX();
	}
	
	@Test
	void translationY() {
		rigidTransformNumerics.assertTranslationY();
	}
	
	@Test
	void translationXY() {
		rigidTransformNumerics.assertTranslationXY();
	}

	@Test
	void rotation() {
		rigidTransformNumerics.assertRotation();
	}
	
	@Test
	void rotationAndTranslation() {
		rigidTransformNumerics.assertRotationAndTranslation();
	}
	
	@Test
	void skipTransform() {
		rigidTransformNumerics.assertSkipTransform();
	}
	
	@Test
	void translationXonly1D() {
		rigidTransformNumerics.assertTranslationXonly1D();
	}
	
	
	@Test
	void translationYonly1D() {
		rigidTransformNumerics.assertTranslationYonly1D();
	}
	
	@Test
	void alignmentOfDisplacementsAlongHorizontalLine() {
		rigidTransformNumerics.assertDisplacementsAlongHorizontalLine();
	}
	
	@Test
	void singularityXY() {
		rigidTransformNumerics.assertSingularityXY();
	}
	
	@Disabled("Not yet supported in using jblas")
	@Test
	void singularityX() {
		rigidTransformNumerics.assertSingularityX();
	}
	
	@Disabled("Not yet supported in using jblas")
	@Test
	void singularityY() {
		rigidTransformNumerics.assertSingularityY();
	}
	
	//
	@Test
	void zeroTransform() {
		affineTransformNumerics.assertZeroTransform();
	}
	
	@Test
	void scalingX() {
		affineTransformNumerics.assertScalingX();
	}
	
	@Test
	void scalingY_withoutX() {
		affineTransformNumerics.assert_scalingY_withoutX();
	}
	
	@Test
	void scalingX_withoutY() {
		affineTransformNumerics.assert_scalingX_withoutY();
	}

	@Test
	void scalingXY() {
		affineTransformNumerics.assertScalingXY();
	}
	
	@Test
	void shearingX() {
		affineTransformNumerics.assertShearingX();
	}
	
	@Test
	void shearingY() {
		affineTransformNumerics.assertShearingY();
	}
	
	@Test
	void shearingXY() {
		affineTransformNumerics.assertShearingXY();
	}
	
	@Test
	void displacementsAlongVerticalLine() {
		affineTransformNumerics.assertDisplacementsAlongVerticalLine();
	}
	
	@Test
	void displacementsAlongHorizontalLine() {
		affineTransformNumerics.assertDisplacementsAlongHorizontalLine();
	}

}
