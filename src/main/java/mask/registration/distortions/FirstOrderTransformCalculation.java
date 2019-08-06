package mask.registration.distortions;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import Jama.Matrix;
import mask.registration.Displacement;
import mask.registration.DisplacementSummary;
import mask.registration.SiteSelection;
import mask.registration.alignment.SimilarityModel;
import mask.registration.alignment.SimilarityModelEquation;

public class FirstOrderTransformCalculation implements BiFunction<Collection<Displacement>, SiteSelection, AffineTransform>{

    @Override
    public FirstOrderTransform apply(Collection<Displacement> t, SiteSelection u) {
        
    	DisplacementSummary summary = Displacement.summarize(t, u.getFirstOrderSelection());
    	double meanx = summary.designMeanX();
    	double meany = summary.designMeanY();
    	
    	List<Displacement> sites = t.stream()
                .filter(u.getFirstOrderSelection())
                .map(d->d.moveBy(-meanx, -meany))
                .collect(Collectors.toList());
    	
    	List<SimilarityModelEquation> alignmentEquations = sites
				 .stream()
                .flatMap(SimilarityModelEquation::from)
                .collect(Collectors.toList());
    	 
        SimilarityModel alignModel = new SimilarityModel(alignmentEquations);
        
        Matrix alignment = alignModel.solve();
        double transx = alignment.get(2, 0);
        double transy = alignment.get(3, 0);
        double rotation = alignment.get(1, 0);
        
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
        		.with(transx, transy, scalex, scaley, orthox, orthoy, rotation);
    }

    

}
