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
package net.raumzeitfalle.registration;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class Dimension<T extends Orientable> implements Consumer<Orientable>, UnaryOperator<T> {

	private int xCount = 0;
	
	private int yCount = 0;
	
	@Override
	public void accept(Orientable t) {
		if (t.matches(Orientation.X)) xCount++;
		if (t.matches(Orientation.Y)) yCount++;
	}
	
	@Override
	public T apply(T t) {
		this.accept(t);
		return t;
	}

	public int getXCoordinateCount() {
		return xCount;
	}
	
	public int getYCoordinateCount() {
		return yCount;
	}
	
	public Orientation getDirection() {
		if (xCount == 0 && yCount == 0) {
			return Orientation.UNKNOWN;
		}
		
		if (xCount > 0 && yCount > 0) {
			return Orientation.XY;
		}
		
		if (xCount > 0) {
			return Orientation.X;
		}
		return Orientation.Y;	
	}

	public int getDimensions() {
		if (xCount == 0 && yCount == 0) {
			return 0;
		}
		if (xCount > 0 && yCount > 0) {
			return 2;
		}
		return 1;
	}
	
	@Override
	public String toString() {
		return String.format("Dimension [%s (x=%s,y=%s)]", getDirection(), xCount, yCount);
	}
}
