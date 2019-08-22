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

import net.raumzeitfalle.registration.displacement.Displacement;

/**
 * The {@link SkipAffineTransform} is ised in cases where an {@link AffineTransform} is integrated into a processing pipeline but no modification to given data is expeced as all affine transform parameters have been set to zero (0.0).
 * 
 * @author oliver
 *
 */
public class SkipAffineTransform extends AffineTransform {

	public SkipAffineTransform() {
		super(0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d);
	}

	@Override
	public Displacement apply(Displacement t) {
		return t;
	}

}
