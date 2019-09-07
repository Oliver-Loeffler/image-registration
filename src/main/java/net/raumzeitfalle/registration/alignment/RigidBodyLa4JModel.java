package net.raumzeitfalle.registration.alignment;

import java.util.Collection;
import java.util.Iterator;

import org.la4j.LinearAlgebra.InverterFactory;
import org.la4j.Matrix;
import org.la4j.decomposition.QRDecompositor;

public class RigidBodyLa4JModel implements RigidBodyModel {

	@Override
	public SimpleRigidTransform solve(Collection<RigidModelEquation> equations) {
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

		Matrix q = laQrResult[0];
		Matrix qTrans = q.transpose();

		Matrix r = laQrResult[1];
		Matrix rInv = InverterFactory.GAUSS_JORDAN.create(r).inverse();

		Matrix laRinvQTransp = rInv.multiply(qTrans);
		
		return laRinvQTransp.multiply(laDeltas);
	}

	private void prepare(Collection<RigidModelEquation> equations, Matrix laRef, Matrix laDeltas) {
		int row = 0;
		Iterator<RigidModelEquation> it = equations.iterator();
		while(it.hasNext()) {
			row = addEquation(laRef, laDeltas, row, it.next());
		}
	}

	private int addEquation(Matrix laRef, Matrix laDeltas, int row, RigidModelEquation eq) {
		laRef.set(row, 0, eq.getXf());
		laRef.set(row, 1, eq.getYf());
		laRef.set(row, 2, eq.getDesignValue());
		laDeltas.set(row, 0, eq.getDeltaValue());
		row++;
		return row;
	}
}
