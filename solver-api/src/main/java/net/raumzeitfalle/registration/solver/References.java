/*-
 * #%L
 * Image-Registration
 * %%
 * Copyright (C) 2019, 2021 Oliver Loeffler, Raumzeitfalle.net
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
package net.raumzeitfalle.registration.solver;

/**
 * A matrix of design locations.
 * The design desribes where features are expected to be located. 
 *
 */
public interface References {

    /**
     * @return A 2D array describing the X-Y-location of design locations.
     */
	double[][] getArray();
	
	/**
	 * @return The data set size, usually the number of measurements obtained. 
	 */
	default int rows() {
		return getArray().length;
	}
}
