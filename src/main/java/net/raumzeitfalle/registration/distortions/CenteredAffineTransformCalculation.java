/*-
 * #%L
 * Image-Registration
 * %%
 * Copyright (C) 2019 Oliver Loeffler, Raumzeitfalle.net
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package net.raumzeitfalle.registration.distortions;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.raumzeitfalle.registration.displacement.Displacement;

public final class CenteredAffineTransformCalculation implements BiFunction<Collection<Displacement>, Predicate<Displacement>, AffineTransform> {

	@Override
	public AffineTransform apply(Collection<Displacement> t, Predicate<Displacement> u) {

    	double meanx = Displacement.average(t, u, Displacement::getX);
    	double meany = Displacement.average(t, u, Displacement::getY);
    	
    	List<AffineModelEquation> finalEquations = t.stream()
    												.filter(u)
    												.map(d->d.moveBy(-meanx, -meany))
									                .flatMap(AffineModelEquation::from)
									                .collect(Collectors.toList());

        JamaAffineModel distortionModel = new JamaAffineModel(finalEquations);
        AffineTransform transform = distortionModel.solve();
		        						
        return transform.centerAt(meanx, meany);
	}

}
