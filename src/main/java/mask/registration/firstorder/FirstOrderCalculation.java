package mask.registration.firstorder;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import Jama.Matrix;
import mask.registration.Displacement;

public class FirstOrderCalculation implements BiFunction<Collection<Displacement>, Predicate<Displacement>, FirstOrderTransform>{

    @Override
    public FirstOrderTransform apply(Collection<Displacement> t, Predicate<Displacement> u) {
                        
        List<FirstOrderEquation> equations = t.stream()
                                             .filter(u)
                                             .flatMap(FirstOrderEquation::from)
                                             .collect(Collectors.toList());
        
        FirstOrderModel model = new FirstOrderModel(equations);
        
        Matrix result = model.solve();
        
        double transx = result.get(0, 0);
        double transy = result.get(1, 0);
        double scalex = result.get(2, 0);
        double scaley = result.get(3, 0);
        double orthox = result.get(4, 0);
        double orthoy = result.get(5, 0);
        
        return FirstOrderTransform.with(transx, transy, scalex, scaley, orthox, orthoy);
    }

    

}
