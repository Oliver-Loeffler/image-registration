package net.raumzeitfalle.registration.alignment;

import java.util.List;
import java.util.function.Function;

import org.la4j.LinearAlgebra.InverterFactory;
import org.la4j.decomposition.QRDecompositor;
import org.la4j.Matrix;

public class RigidBodyLa4JModel implements Function<List<RigidModelEquation>, SimpleRigidTransform> {

	@Override
	public SimpleRigidTransform apply(List<RigidModelEquation> equations) {
		int n = 3;

		Matrix laRef = org.la4j.Matrix.zero(equations.size(), n);
		Matrix laDeltas = org.la4j.Matrix.zero(equations.size(), 1);

		prepare(equations, laRef, laDeltas);

		Matrix laResult = solve(laRef, laDeltas);

		double translationX = laResult.get(0, 0);
		double translationY = laResult.get(1, 0);
		double rotation = laResult.get(2, 0);

		return SimpleRigidTransform.with(translationX, translationY, rotation);
	}

	private Matrix solve(org.la4j.Matrix laRef, org.la4j.Matrix laDeltas) {
		QRDecompositor laQr = new QRDecompositor(laRef);
		Matrix[] laQrResult = laQr.decompose();

		Matrix Q = laQrResult[0];
		Matrix Qtrans = Q.transpose();

		Matrix R = laQrResult[1];
		Matrix Rinv = InverterFactory.GAUSS_JORDAN.create(R).inverse();

		Matrix laRinvQTransp = Rinv.multiply(Qtrans);
		Matrix laResult = laRinvQTransp.multiply(laDeltas);

		return laResult;
	}

	private void prepare(List<RigidModelEquation> equations, Matrix laRef, Matrix laDeltas) {
		for (int m = 0; m < equations.size(); m++) {
			RigidModelEquation eq = equations.get(m);

			laRef.set(m, 0, eq.getXf());
			laRef.set(m, 1, eq.getYf());
			laRef.set(m, 2, eq.getDesignValue());
			laDeltas.set(m, 0, eq.getDeltaValue());
		}
	}

}
