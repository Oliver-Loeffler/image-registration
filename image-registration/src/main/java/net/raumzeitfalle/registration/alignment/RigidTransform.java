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

import java.util.Objects;

import net.raumzeitfalle.registration.Transform;
import net.raumzeitfalle.registration.displacement.Displacement;

/**
 * 
 * Allows to correct rotation and translation (x,y) for a given displacement.
 * 
 *
 */
public interface RigidTransform extends Transform {
	
	public static RigidTransform translation(double tx, double ty) {
		return new SimpleRigidTransform(tx, ty, 0.0);
	}
	
	public static RigidTransform shiftX(double tx) {
		return new SimpleRigidTransform(tx, 0.0, 0.0);
	}
	
	public static RigidTransform shiftY(double ty) {
		return new SimpleRigidTransform(0.0, ty, 0.0);
	}
	
	public static RigidTransform with(double tx, double ty, double rotation) {
		return new SimpleRigidTransform(tx, ty, rotation);
	}
	
	public static RigidTransform with(Translation translation) {
		Objects.requireNonNull(translation, "translation must not be null");
		return RigidTransform.with(translation.getTranslationX(), translation.getTranslationY(), 0.0);
	}
	
	public static RigidTransform with(Translation translation, double rotation) {
		Objects.requireNonNull(translation, "translation must not be null");
		return RigidTransform.with(translation.getTranslationX(), translation.getTranslationY(), rotation);
	}
	
	double getTranslationX();
	double getTranslationY();
	double getRotation();
	
	@Override
	default Displacement apply(Displacement source) {
		return Displacement.from(source, 
				source.getXd() - this.getTranslationX() + source.getY() * this.getRotation(), 
				source.getYd() - this.getTranslationY() - source.getX() * this.getRotation());
	}
	
	default Translation getTranslation() {
		return SimpleTranslation.with(getTranslationX(), getTranslationY());
	}
	
}
