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
package net.raumzeitfalle.registration.alignment;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.raumzeitfalle.registration.displacement.Displacement;

/**
 * 
 * Allows to correct rotation and translation (x,y) for a given displacement.
 *  
 * @author oliver
 *
 */
public class NonUniformSimilarityTransform implements Function<Collection<Displacement>, Collection<Displacement>> {

	public static NonUniformSimilarityTransform with(double scaleX, double scaleY, double translationX, double translationY, double rotation) {
		return new NonUniformSimilarityTransform(scaleX, scaleY, translationX, translationY, rotation);
	}
	
	private final double translationX;
	
	private final double translationY;
	
	private final double scaleX;
	
	private final double scaleY;
	
	private final double rotation;

	private NonUniformSimilarityTransform(double sx, double sy, double tx, double ty, double rot) {
		this.scaleX = sx;
		this.scaleY = sy;
		this.translationX = tx;
		this.translationY = ty;
		this.rotation = rot;
	}

	public double getTranslationX() {
		return translationX;
	}

	public double getTranslationY() {
		return translationY;
	}

	public double getRotation() {
		return rotation;
	}
	
	public double getScaleX() {
		return scaleX;
	}
	
	public double getScaleY() {
		return scaleY;
	}

	@Override
	public String toString() {
		
		return    "NonUniformSimilarityTransform [x=" + format(translationX) 
				+ ", y=" + format(translationY) + ", rotation="
				+ format(rotation * 1E6) + " urad, "
				+ ", scale_x=" + format(scaleX * 1E6) + " ppm, "
				+ ", scale_y=" + format(scaleY * 1E6) + " ppm ]";
	}
	
	private String format(double value) {
		return String.format("%10.7f", value);
	}

	@Override
	public Collection<Displacement> apply(Collection<Displacement> d) {
		return d.stream()
				 .map(mappper())
				 .collect(Collectors.toList());
	}
	
	private Function<Displacement,Displacement> mappper() {
		return source -> Displacement.from(source, 
				source.getXd() - this.getTranslationX() - source.getX() * this.scaleX + source.getY() * this.getRotation(), 
				source.getYd() - this.getTranslationY() - source.getY() * this.scaleY - source.getX() * this.getRotation());
	}
	
}
