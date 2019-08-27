/**
 * Contains transform functions which are mainly intended to be used to align data to a given reference. 
 * <p>
 * Alignment means, finding the right translation (X,Y) and rotation values in order to properly overlay the displaced points vs. the reference points.
 * For photomask applications, a rigid body transformation is used, as the shape of the displaced data shall remain as is. A wafer lithography tool such as a scanner or a stepper is able to perform such an operation.
 * <p>
 * Nevertheless, there are use cases where so called uniform or non-uniform similarity transformations may be used.
 * <p>
 * <ul>
 * 	<li>Rigid Body Transform: calculates translation (X,Y) and rotation</li>
 * 	<li>Similarity Transform: calculations magnification (i.e. uniform scaling), translation (X,Y) and rotation</li>
 * 	<li>Non-Uniform Similarity Transform: calculates scaling (X,Y), translation (X,Y) and rotation</li>
 * </ul>
 */
package net.raumzeitfalle.registration.alignment;