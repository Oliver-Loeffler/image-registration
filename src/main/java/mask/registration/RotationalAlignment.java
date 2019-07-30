package mask.registration;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import Jama.Matrix;
import Jama.QRDecomposition;
import mask.registration.alignment.AlignmentEquation;
import mask.registration.alignment.AlignmentTransform;

public class RotationalAlignment implements BiFunction<Collection<Displacement>, Predicate<Displacement>, Rotation> {

	@Override
	public Rotation apply(Collection<Displacement> t, Predicate<Displacement> u) {
		
		logMeans("Before", t);
		
		List<Displacement> alignmentSites = new TranslationCorrection().apply(t, u);
		
		logMeans("Translation Corrected", t);
		
		Matrix resultRaw = calculateAlignment(alignmentSites);
		
		for (int i=0; i < 2;i++) {
		
			 double translationX = resultRaw.get(0, 0);
	         double translationY = resultRaw.get(1, 0);
	    
	         AlignmentTransform tf = AlignmentTransform.with(translationX, translationY, 0.0);
	        
	         alignmentSites = t.stream()
				      .map(tf::apply)
				      .collect(Collectors.toList());

		}
        
		double translationX = resultRaw.get(0, 0);
        double translationY = resultRaw.get(1, 0);
        double rotation = resultRaw.get(2, 0);
        
        Rotation alignment = Rotation
        		.with(translationX, translationY, rotation);
		 
		return alignment;
	}

	private void logMeans(String title, Collection<Displacement> t) {
		
		double mx = Displacement.average(t, Displacement::dX);
		double my = Displacement.average(t, Displacement::dY);
		
		System.out.println(title + ": " + mx + ", " + my);	
		
	}

	private Matrix calculateAlignment(List<Displacement> alignmentSites) {
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
		return resultRaw;
	}

}
