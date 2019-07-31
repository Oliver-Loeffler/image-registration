package mask.registration.firstorder;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import mask.registration.Displacement;

public class FirstOrderCorrection implements BiFunction<FirstOrderTransform, Collection<Displacement>, List<Displacement>> {
    
    @Override
    public List<Displacement> apply(FirstOrderTransform t, Collection<Displacement> u) {

        return u.stream()
                .map(t)
                .collect(Collectors.toList());
        
    }
    
    

}