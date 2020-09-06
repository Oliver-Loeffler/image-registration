package net.raumzeitfalle.registration.solvertest.spi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.displacement.Displacement;
import net.raumzeitfalle.registration.distortions.AffineTransform;
import net.raumzeitfalle.registration.distortions.SimpleAffineTransform;
import net.raumzeitfalle.registration.jblas.JblasSolver;
import net.raumzeitfalle.registration.solver.SolverProvider;
import net.raumzeitfalle.registration.solver.spi.Solver;
import net.raumzeitfalle.registration.solvertest.NumericsTestRunner;


class JblasSolverTest {
	
	private static final Double TOLERANCE = 1E-11;

	@BeforeAll
	public static void prepare() {
		SolverProvider.setPreferredImplementation(JblasSolver.class.getName());
	}

	@Test
	void testServiceDiscovery() {
		
		Solver solver = SolverProvider.getInstance().getSolver();
		
		assertEquals(JblasSolver.class.getName(), solver.getClass().getName());
		
	}
	
	@Test
	void scalingXY() {
		
		double dx =  0.075; // 150nm over 150um is 1ppm
		double dy = -0.140; // 140nm over 140um is 1ppm
		
		List<Displacement> undisplaced = new ArrayList<>(5);
		undisplaced.add(Displacement.at(0, 0,      0,      0,      0-dx,      0-dy));
		undisplaced.add(Displacement.at(1, 1,      0, 140000,      0-dx, 140000+dy));
		undisplaced.add(Displacement.at(2, 2, 150000, 140000, 150000+dx, 140000+dy));
		undisplaced.add(Displacement.at(3, 3, 150000,      0, 150000+dx,      0-dy));
		undisplaced.add(Displacement.at(4, 4,  75000,  70000, Double.NaN, Double.NaN));
		
		
		run(undisplaced);
		AffineTransform result = getUncorrectedFirstOrder();
		
		
		assertNotNull(result);
		assertEquals(SimpleAffineTransform.class, result.getClass());
		
		assertEquals( 0.0, result.getTranslationX(),          TOLERANCE);
		assertEquals( 0.0, result.getTranslationY(),          TOLERANCE);
		
		assertEquals( 75000.0, result.getCenterX(),           TOLERANCE);
		assertEquals( 70000.0, result.getCenterY(),           TOLERANCE);
		
		assertEquals(  1.0, toPPM(result.getScaleX()),        TOLERANCE);
		assertEquals( -2.0, toPPM(result.getScaleY()),        TOLERANCE);
		assertEquals( -0.5, toPPM(result.getMagnification()), TOLERANCE);
		
		assertEquals( 0.0, result.getOrthoX(),                TOLERANCE);
		assertEquals( 0.0, result.getOrthoY(),                TOLERANCE);
		assertEquals( 0.0, result.getOrtho(),                 TOLERANCE);
	}

	private final NumericsTestRunner runner = new NumericsTestRunner();

	public void run(List<Displacement> source) {
		this.runner.accept(source);
	}
	
	public AffineTransform getUncorrectedFirstOrder() {
		return this.runner.getUncorrectedFirstOrder();
	}
	
	private double toPPM(double value) {
		BigDecimal bd = BigDecimal.valueOf(value * 1E6);
	    bd = bd.setScale(3, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}
