package mask.registration.distortions;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import Jama.Matrix;
import mask.registration.Displacement;
import mask.registration.alignment.SimilarityModel;
import mask.registration.alignment.SimilarityModelEquation;

public class FirstOrderTransformCalculation implements BiFunction<Collection<Displacement>, Predicate<Displacement>, AffineTransform>{

    @Override
    public FirstOrderTransform apply(Collection<Displacement> t, Predicate<Displacement> selection) {
        return apply(t,selection,selection);
    }

    public FirstOrderTransform apply(Collection<Displacement> t, Predicate<Displacement> alignOn, Predicate<Displacement> calculateWith) {
    	
    	double meanx = Displacement.average(t, alignOn, Displacement::getX);
    	double meany = Displacement.average(t, alignOn, Displacement::getY);
    	
    	/* TODO:
    	 * Rework configuration of SimilarityModel and AffineModel, attempt to place both into one loop. 
    	 */
    	
    	List<Displacement> sites = t.stream()
    			.filter(calculateWith)
                .map(d->d.moveBy(-meanx, -meany))
                .collect(Collectors.toList());
    	
    	List<SimilarityModelEquation> alignmentEquations = t.stream()
                .filter(alignOn)
                .map(d->d.moveBy(-meanx, -meany))
                .flatMap(SimilarityModelEquation::from)
                .collect(Collectors.toList());
    	 
        SimilarityModel alignModel = new SimilarityModel(alignmentEquations);
        	
        Matrix alignment = alignModel.solve();
        double rotation = alignment.get(1, 0);
        double transx = alignment.get(2, 0);
        double transy = alignment.get(3, 0);
           
        List<AffineModelEquation> finalEquations = sites
				 .stream()
                 .flatMap(AffineModelEquation::from)
                 .collect(Collectors.toList());

        AffineModel distortionModel = new AffineModel(finalEquations);
        Matrix distortion = distortionModel.solve();
        
        double scalex = distortion.get(0, 0);
        double scaley = distortion.get(1, 0);
        double orthox = distortion.get(2, 0);
        double orthoy = distortion.get(3, 0);
        
        return FirstOrderTransform
        			.with(transx, transy, scalex, scaley, orthox, orthoy, rotation, meanx, meany);
    }
    
}
