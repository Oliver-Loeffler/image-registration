package mask.registration.distortions;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import mask.registration.Displacement;

public class AffineTransformCorrection implements BiFunction<AffineTransform, Collection<Displacement>, List<Displacement>> {
    
    @Override
    public List<Displacement> apply(AffineTransform t, Collection<Displacement> u) {

        return u.stream()
                .map(t)
                .collect(Collectors.toList());
        
    }
    
    

}