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
/**
 * Contains transform functions to align data to a given reference. 
 * <p>
 * Alignment means, finding the right translation (X,Y) and rotation values in order to properly overlay the displaced points vs. the reference points.
 * For photomask applications, a rigid body transformation is used, as the shape of the displaced data shall remain as is. A wafer lithography tool such as a scanner or a stepper is able to perform such an operation.
 * <p>
 * Nevertheless, there are use cases where so called uniform or non-uniform similarity transformations may be used.
 * <ul>
 * 	<li>Rigid Body Transform: calculates translation (X,Y) and rotation</li>
 * 	<li>Similarity Transform: calculations magnification (i.e. uniform scaling), translation (X,Y) and rotation</li>
 * 	<li>Non-Uniform Similarity Transform: calculates scaling (X,Y), translation (X,Y) and rotation</li>
 * </ul>
 */
package net.raumzeitfalle.registration.alignment;
