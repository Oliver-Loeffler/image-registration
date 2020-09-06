package net.raumzeitfalle.registration.solvertest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.jblas.JblasSolver;
import net.raumzeitfalle.registration.solver.SolverProvider;
import net.raumzeitfalle.registration.solver.spi.Solver;
import net.raumzeitfalle.registration.solvertest.numerics.AffineTransformNumerics;
import net.raumzeitfalle.registration.solvertest.numerics.RigidTransformNumerics;


class JblasNumericsTest {
	
	@BeforeAll
	public static void prepare() {
		SolverProvider.setPreferredImplementation(JblasSolver.class.getName());
	}
	
	@BeforeEach
	void checkImplementation() {
		Solver jblasSolver = new JblasSolver();	
		assertEquals(jblasSolver.getClass(), SolverProvider.getInstance().getSolver().getClass());
	}
		
	private final RigidTransformNumerics rigidTransformNumerics = new RigidTransformNumerics();
	
	private final AffineTransformNumerics affineTransformNumerics = new AffineTransformNumerics();
	
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
