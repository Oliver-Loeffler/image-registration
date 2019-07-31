package mask.registration.alignment;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import Jama.Matrix;
import mask.registration.Displacement;

public class AlignmentCalculation implements BiFunction<Collection<Displacement>, Predicate<Displacement>, AlignmentTransform>{

	@Override
	public AlignmentTransform apply(Collection<Displacement> t, Predicate<Displacement> u) {
						
		List<AlignmentEquation> equations = t.stream()
											 .filter(u)
											 .flatMap(AlignmentEquation::from)
											 .collect(Collectors.toList());
		
		AlignmentModel model = new AlignmentModel(equations);
		
		Matrix result = model.solve();
		
        double translationX = result.get(0, 0);
        double translationY = result.get(1, 0);
        double rotation = result.get(2, 0);
        
        return AlignmentTransform.with(translationX, translationY, rotation);
	}

	

}
