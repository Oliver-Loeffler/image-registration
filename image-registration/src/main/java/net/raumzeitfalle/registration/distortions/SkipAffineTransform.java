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

import net.raumzeitfalle.registration.SkipTransform;
import net.raumzeitfalle.registration.displacement.Displacement;

/**
 * The {@link SkipAffineTransform} is ised in cases where an {@link SimpleAffineTransform} is integrated into a processing pipeline but no modification to given data is expeced as all affine transform parameters have been set to zero (0.0).
 * 
 * @author oliver
 *
 */
public final class SkipAffineTransform implements AffineTransform, SkipTransform {
	
	protected static AffineTransform centeredAt(double centerX, double centerY) {
		return new SkipAffineTransform(centerX, centerY);
	}
	
	private final double cx;
	
	private final double cy;

	protected SkipAffineTransform(double centerX, double centerY) {
		this.cx = centerX;
		this.cy = centerY;
	}
	
	@Override
	public Displacement apply(Displacement t) {
		return t;
	}

	@Override
	public double getTranslationX() { return 0; }

	@Override
	public double getTranslationY() { return 0; }

	@Override
	public double getScaleX() { return 0; }

	@Override
	public double getScaleY() { return 0; }

	@Override
	public double getMagnification() { return 0; }

	@Override
	public double getOrthoX() { return 0; }

	@Override
	public double getOrthoY() { return 0; }

	@Override
	public double getOrtho() { return 0; }

	@Override
	public double getCenterX() { return cx; }

	@Override
	public double getCenterY() { return cy; }

}
