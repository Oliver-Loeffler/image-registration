package mask.registration;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.la4j.LinearAlgebra.InverterFactory;
import org.la4j.decomposition.QRDecompositor;

import mask.registration.alignment.AlignmentEquation;
import mask.registration.alignment.AlignmentTransform;

public class La4JAlignment implements BiFunction<Collection<Displacement>, Predicate<Displacement>, AlignmentTransform>{

	@Override
	public AlignmentTransform apply(Collection<Displacement> t, Predicate<Displacement> u) {
		
		List<Displacement> alignmentSites = t.stream().filter(u).collect(Collectors.toList());
		
		List<AlignmentEquation> equations = alignmentSites
												.stream()
												.flatMap(Displacement::equations)
												.collect(Collectors.toList());
		
		int n = 3;

		org.la4j.Matrix laRef = org.la4j.Matrix.zero(equations.size(), n);
		org.la4j.Matrix laDeltas = org.la4j.Matrix.zero(equations.size(), 1);
		
		for (int m = 0; m < equations.size(); m++) {
			AlignmentEquation eq = equations.get(m);
			
			laRef.set(m, 0, eq.getXf());
			laRef.set(m, 1, eq.getYf());
			laRef.set(m, 2, eq.getDesignValue());
			laDeltas.set(m, 0, eq.getDeltaValue());
		}
	    
        QRDecompositor laQr = new QRDecompositor(laRef);
        org.la4j.Matrix[] laQrResult = laQr.decompose();
        
        
        org.la4j.Matrix Q = laQrResult[0];
        org.la4j.Matrix Qtrans = Q.transpose();
        		
        org.la4j.Matrix R = laQrResult[1];
        org.la4j.Matrix Rinv = InverterFactory.GAUSS_JORDAN.create(R).inverse();
        
        org.la4j.Matrix laRinvQTransp = Rinv.multiply(Qtrans);
        org.la4j.Matrix laResult = laRinvQTransp.multiply(laDeltas);
		
        double translationX = laResult.get(0, 0);
        double translationY = laResult.get(1, 0);
        double rotation = laResult.get(2, 0);
        
        AlignmentTransform alignment = AlignmentTransform
        		.with(translationX, translationY, rotation);
		
		return alignment;
	}

}
