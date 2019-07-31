package mask.registration;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import mask.registration.alignment.AlignmentTransform;

public class NonLinearAlignment implements BiFunction<Collection<Displacement>, Predicate<Displacement>, AlignmentTransform> {

	@Override
	public AlignmentTransform apply(Collection<Displacement> t, Predicate<Displacement> u) {
		// TODO Auto-generated method stub
		return null;
	}

}
