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
package net.raumzeitfalle.registration.alignment;

import net.raumzeitfalle.registration.Transform;
import net.raumzeitfalle.registration.displacement.Displacement;

/**
 * Allows to correct rotation and translation (x,y) and non-uniform magnification (different scale in x and y direction) for a given displacement.
 * 
 * This transform will not preserve angles and distances.
 * 
 * <ul>
 * 	<li>Translation (x,y)</li>
 *  <li>Rotation</li>
 *  <li>Non-uniform scale (scale<sub>X</sub> and scale<sub>Y</sub>)</li>
 * </ul>
 *  
 */
public interface NonUniformSimilarityTransform extends Transform {
	
	double getTranslationX();
	double getTranslationY();
	double getRotation();
	double getScaleX();
	double getScaleY();
	
	@Override
	default Displacement apply(Displacement source) {
		return Displacement.from(source, 
		source.getXd() - this.getTranslationX() - source.getX() * this.getScaleX() + source.getY() * this.getRotation(), 
		source.getYd() - this.getTranslationY() - source.getY() * this.getScaleY() - source.getX() * this.getRotation());
	}
	
}
