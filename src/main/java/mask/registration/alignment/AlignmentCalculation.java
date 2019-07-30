package mask.registration.alignment;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import Jama.Matrix;
import Jama.QRDecomposition;
import mask.registration.Displacement;

public class AlignmentCalculation implements BiFunction<Collection<Displacement>, Predicate<Displacement>, AlignmentTransform>{

	@Override
	public AlignmentTransform apply(Collection<Displacement> t, Predicate<Displacement> u) {
				
		List<Displacement> alignmentSites = t.stream()
											 .filter(u)
					 						 .collect(Collectors.toList());
			
		List<AlignmentEquation> equations = alignmentSites
												.stream()
												.flatMap(Displacement::equations)
												.collect(Collectors.toList());
		
		int n = 3;
		
		Matrix reference = new Matrix(equations.size(), n);
		Matrix deltas = new Matrix(equations.size(), 1);
		
		for (int m = 0; m < equations.size(); m++) {
			AlignmentEquation eq = equations.get(m);
			reference.set(m, 0, eq.getXf());
			reference.set(m, 1, eq.getYf());
			reference.set(m, 2, eq.getDesignValue());
			deltas.set(m, 0, eq.getDeltaValue());
		}
		
		QRDecomposition qr = new QRDecomposition(reference);
        Matrix rInvQTrans = qr.getR().inverse().times(qr.getQ().transpose());
        Matrix resultRaw = rInvQTrans.times(deltas);
		
        double translationX = resultRaw.get(0, 0);
        double translationY = resultRaw.get(1, 0);
        double rotation = resultRaw.get(2, 0);
        
        return AlignmentTransform.with(translationX, translationY, rotation);
	}

	

}
