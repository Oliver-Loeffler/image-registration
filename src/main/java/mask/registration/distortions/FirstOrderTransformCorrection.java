package mask.registration.distortions;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

import mask.registration.Displacement;
import mask.registration.DisplacementSummary;
import mask.registration.SiteSelection;
import mask.registration.alignment.RigidTransform;
import mask.registration.alignment.RigidTransformCalculation;
import mask.registration.alignment.SimilarityAlignmentCalculation;
import mask.registration.alignment.SimilarityAlignmentTransform;
import mask.registration.alignment.SkipRigidTransform;
import mask.registration.alignment.TranslateToCenter;

public class FirstOrderTransformCorrection implements BiFunction<Collection<Displacement>, SiteSelection, Collection<Displacement>> {

	private final RigidTransform alignment;
	
	private final AffineTransform firstOrder;
	
	public FirstOrderTransformCorrection(RigidTransform alignment, AffineTransform firstOrder) {
		this.alignment = alignment;
		this.firstOrder = firstOrder;
	}
	
	@Override
	public Collection<Displacement> apply(Collection<Displacement> t, SiteSelection u) {
		
		if (this.alignment instanceof SkipRigidTransform 
			&& this.firstOrder instanceof SkipAffineTransform) {
			return t;
		}
		
		
		DisplacementSummary start = DisplacementSummary.over(t, u.getCalculation());
		
		// DO ALIGNMENT
		TranslateToCenter translateToCenter = new TranslateToCenter();
		List<Displacement> aligned = alignment.andThen(translateToCenter)
											  .apply(t);
		
		DisplacementSummary afterAlignment = DisplacementSummary.over(aligned, u.getCalculation());
		
		
		/* 
		 * TODO:
		 * Here after alignment, correct only scale & shear (use only 4 params)
		 * But now the name is no longer appropriate.
		 */
		AffineTransformCorrection affineCorrection = new AffineTransformCorrection();
		List<Displacement> corrected = affineCorrection
				.andThen(translateToCenter.reverse())
				.apply(firstOrder, aligned);
		
		DisplacementSummary correctedSummary = DisplacementSummary.over(corrected, u.getCalculation());
		
		
		RigidTransform residualAlignment = new RigidTransformCalculation().apply(corrected, u.getAlignment());
		List<Displacement> finalCorrected = residualAlignment
												.andThen(translateToCenter.reverse())
												.apply(corrected);
		
		DisplacementSummary fc = DisplacementSummary.over(finalCorrected, u.getCalculation());
		AffineTransform fca = new FirstOrderTransformCalculation().apply(finalCorrected, u.getCalculation());
		
		
		SimilarityAlignmentTransform alternateResidualAlign = new SimilarityAlignmentCalculation().apply(corrected, u.getCalculation());
		List<Displacement> alternateFinal = alternateResidualAlign
												.andThen(translateToCenter.reverse())
												.apply(corrected);
		
		DisplacementSummary sa = DisplacementSummary.over(alternateFinal, u.getCalculation());
		AffineTransform fsa = new FirstOrderTransformCalculation().apply(alternateFinal, u.getCalculation());

		if (this.alignment instanceof SkipRigidTransform) {
			return translateToCenter.reverse().apply(corrected);
		}
		return finalCorrected;
	}

}
