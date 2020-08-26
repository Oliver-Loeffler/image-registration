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

import java.util.Locale;

/**
 * 
 * Allows to translation (x,y) for a given displacement.
 *
 */
public final class SimpleTranslation implements Translation {

	public static Translation shiftX(double translationX) {
		return new SimpleTranslation(translationX, 0.0);
	}

	public static Translation shiftY(double translationY) {
		return new SimpleTranslation(0.0, translationY);
	}

	public static Translation with(double translationX, double translationY) {
		return new SimpleTranslation(translationX, translationY);
	}

	private final double translationX;

	private final double translationY;
	
	private SimpleTranslation(double tx, double ty) {
		this.translationX = tx;
		this.translationY = ty;
	}

	public double getTranslationX() {
		return translationX;
	}

	public double getTranslationY() {
		return translationY;
	}


	@Override
	public String toString() {
		return "Translation [x=" + format(translationX) + ", y=" + format(translationY) + "]";
	}

	private String format(double value) {
		return String.format(Locale.US, "%10.7f", value);
	}

}
