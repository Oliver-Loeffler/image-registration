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

import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import net.raumzeitfalle.registration.Orientation;
import net.raumzeitfalle.registration.Orientable;
import net.raumzeitfalle.registration.displacement.Displacement;

final class RigidModelEquation implements Orientable {
	
	public static Stream<RigidModelEquation> from(Displacement d) {
		
		Builder<RigidModelEquation> builder = Stream.builder();
		
		if (Double.isFinite(d.getXd())) {
			builder.accept(RigidModelEquation.forX(d));
		}
		
		if (Double.isFinite(d.getYd())) {
			builder.accept(RigidModelEquation.forY(d));
		}
		
		return builder.build();
	}
	
	public static RigidModelEquation forX(Displacement d) {
		
		double xf = 1.0;
		double yf = 0.0;
		
		double design = -d.getY();
		double delta = d.dX();
		
		return new RigidModelEquation(xf, yf, design, delta, Orientation.X);
	}
	
	public static RigidModelEquation forY(Displacement d) {
		
		double xf = 0.0;
		double yf = 1.0;
		
		double design = d.getX();
		double delta = d.dY();
		
		return new RigidModelEquation(xf, yf, design, delta, Orientation.Y);
	}
	
	private final double xf;
	
	private final double yf;
	
	private final double designValue;
	
	private final double deltaValue;
	
	private final Orientation direction;
	
	private RigidModelEquation(double xf, double yf, double design, double delta, Orientation direction) {
		this.xf = xf;
		this.yf = yf;
		this.designValue = design;
		this.deltaValue = delta;
		this.direction = direction;
	}

	@Override
	public String toString() {
		return String.format("RigidModelEquation [xf=%s, yf=%s, designValue=%s, deltaValue=%s, direction=%s]",
                              xf, yf, designValue, deltaValue, direction);
	}

	public double getXf() {
		return xf;
	}

	public double getYf() {
		return yf;
	}

	public double getDesignValue() {
		return designValue;
	}

	public double getDeltaValue() {
		return deltaValue;
	}
	
	public Orientation getOrientation() {
		return direction;
	}
	
}
