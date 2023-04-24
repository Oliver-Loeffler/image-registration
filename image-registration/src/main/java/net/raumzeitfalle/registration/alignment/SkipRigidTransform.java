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

import net.raumzeitfalle.registration.SkipTransform;
import net.raumzeitfalle.registration.displacement.Displacement;

public final class SkipRigidTransform implements RigidTransform, SkipTransform {
	
	private final double tx = 0.0;
	
	private final double ty = 0.0;
	
	private final double rot = 0.0;

	protected SkipRigidTransform() {
		// Object creation limited to package level
	}
	
	@Override
	public double getTranslationX() { return tx; }

	@Override
	public double getTranslationY() { return ty; }

	@Override
	public double getRotation() { return rot; }

	@Override
	public Displacement apply(Displacement t) { return t; }
	
	@Override
	public int hashCode() {
		return Objects.hash(getRotation(), getTranslationX(), getTranslationY());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof RigidTransform))
			return false;
		RigidTransform other = (RigidTransform) obj;
		return Double.doubleToLongBits(getRotation()) == Double.doubleToLongBits(other.getRotation())
				&& Double.doubleToLongBits(getTranslationX()) == Double.doubleToLongBits(other.getTranslationX())
				&& Double.doubleToLongBits(getTranslationY()) == Double.doubleToLongBits(other.getTranslationY());
	}

}
