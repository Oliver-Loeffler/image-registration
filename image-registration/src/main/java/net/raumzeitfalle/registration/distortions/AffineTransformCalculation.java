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

import java.lang.System.Logger.Level;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.raumzeitfalle.registration.DegreesOfFreedom;
import net.raumzeitfalle.registration.alignment.TranslateFunction;
import net.raumzeitfalle.registration.displacement.Displacement;

public final class AffineTransformCalculation implements BiFunction<Collection<Displacement>, Predicate<Displacement>, AffineTransform> {

	private final AffineModel model;
	
	private Function<Exception, AffineTransform> errorHandler;
	
	private static final System.Logger LOGGER = System.getLogger(AffineTransformCalculation.class.getName());
	
	public AffineTransformCalculation() {
		this(new BasicAffineModel());
	}
	
	
	public AffineTransformCalculation(AffineModel model) {
		this(model, ex->{
			LOGGER.log(Level.WARNING, "Model calculation error -> continuing with a SkipTransform.", ex);
			return SkipAffineTransform.centeredAt(0, 0);
		});
	}
	
	public AffineTransformCalculation(AffineModel model, Function<Exception, AffineTransform> onError) {
		this.model = Objects.requireNonNull(model, "The used AffineModel for calculation must not be null.");
		this.errorHandler = Objects.requireNonNull(onError, "The error handler (onError) must not be null.");
	}
	
	@Override
	public AffineTransform apply(Collection<Displacement> t, Predicate<Displacement> u) {
		if (t.isEmpty())
    		return SkipAffineTransform.centeredAt(0, 0);

    	TranslateFunction translate = Displacement.translationToCenter(t, u);
    
    	DegreesOfFreedom degreesOfFreedom = new DegreesOfFreedom();
    	
    	List<AffineModelEquation> finalEquations = t.stream()
    												.filter(u)
    												.map(translate)
    												.map(degreesOfFreedom)
									                .flatMap(AffineModelEquation::from)
									                .collect(Collectors.toList());
    	
    	if (finalEquations.isEmpty())
    		return SkipAffineTransform.centeredAt(0, 0);

        AffineTransform transform = tryCalculation(finalEquations, degreesOfFreedom);
        
        return new AffineTransformBuilder(transform, -translate.getX(), -translate.getY()).build();
		        						
	}

	private AffineTransform tryCalculation( List<AffineModelEquation> finalEquations,
											DegreesOfFreedom degreesOfFreedom ) {
		try {
			return model.solve(finalEquations,degreesOfFreedom); 
		}
		catch (Exception e) {
			return tryOneDimModel(finalEquations, degreesOfFreedom);
		}
	}

	private AffineTransform tryOneDimModel( List<AffineModelEquation> finalEquations,
											DegreesOfFreedom degreesOfFreedom ) {
		
		try {	
			OneDimensionalAffineModel oneDimModel = new OneDimensionalAffineModel();
			return oneDimModel.solve(finalEquations, degreesOfFreedom);			
		}
		catch (Exception e) {
			return errorHandler.apply(e);
		}
	}
}
