package net.raumzeitfalle.registration.solvertest;

import java.util.*;

import net.raumzeitfalle.registration.alignment.RigidTransform;
import net.raumzeitfalle.registration.displacement.*;
import net.raumzeitfalle.registration.distortions.AffineTransform;

public abstract class NumericsTestBase {

	private final NumericsTestRunner runner = new NumericsTestRunner();

	public void run(List<Displacement> source) {
		this.runner.accept(source);
	}
	
	public NumericsTestRunner getRunner() {
		return this.runner;
	}
	
	public RigidTransform getCorrectedAlignment() {
		return this.runner.getCorrectedAlignment();
	}
	
	public RigidTransform getUncorrectedAlignment() {
		return this.runner.getUncorrectedAlignment();
	}
	
	public AffineTransform getUncorrectedFirstOrder() {
		return this.runner.getUncorrectedFirstOrder();
	}

	public AffineTransform getFirstOrder() {
		return this.runner.getFirstOrder();
	}
	
	public AffineTransform getCorrectedFirstOrder() {
		return this.runner.getCorrectedFirstOrder();
	}
	
	public List<Displacement> listOf(Displacement... d) {
		return Arrays.asList(d);
	}

}
