/*
 *   Copyright 2019 Oliver Löffler, Raumzeitfalle.net
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package net.raumzeitfalle.registration.distortions;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import Jama.Matrix;
import net.raumzeitfalle.registration.displacement.Displacement;

public class AffineTransformCalculation implements BiFunction<Collection<Displacement>, Predicate<Displacement>, AffineTransform> {

	@Override
	public AffineTransform apply(Collection<Displacement> t, Predicate<Displacement> u) {

    	double meanx = Displacement.average(t, u, Displacement::getX);
    	double meany = Displacement.average(t, u, Displacement::getY);
    
    	List<Displacement> displacements = t.stream()
    			 							.filter(u)
    			 							.map(d->d.moveBy(-meanx, -meany))
    			 							.collect(Collectors.toList());
    	
    	List<AffineModelEquation> finalEquations = displacements.stream()
									                .flatMap(AffineModelEquation::from)
									                .collect(Collectors.toList());

        AffineModel distortionModel = new AffineModel(finalEquations);
        Matrix distortion = distortionModel.solve();
        
        double scalex = distortion.get(0, 0);
        double scaley = distortion.get(1, 0);
        double orthox = distortion.get(2, 0);
        double orthoy = distortion.get(3, 0);
        double transx = distortion.get(4, 0);
        double transy = distortion.get(5, 0);
        
        return new AffineTransform(transx, transy, scalex, scaley, orthox, orthoy, meanx, meany);
	}

}
