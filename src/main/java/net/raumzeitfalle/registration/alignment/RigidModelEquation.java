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

import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import net.raumzeitfalle.registration.displacement.Displacement;

public class RigidModelEquation {
	
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
		
		return new RigidModelEquation(xf, yf, design, delta);
	}
	
	public static RigidModelEquation forY(Displacement d) {
		
		double xf = 0.0;
		double yf = 1.0;
		
		double design = d.getX();
		double delta = d.dY();
		
		return new RigidModelEquation(xf, yf, design, delta);
	}
	
	private final double xf;
	
	private final double yf;
	
	private final double designValue;
	
	private final double deltaValue;
	
	private RigidModelEquation(double xf, double yf, double design, double delta) {
		this.xf = xf;
		this.yf = yf;
		this.designValue = design;
		this.deltaValue = delta;
	}

	@Override
	public String toString() {
		return "AlignmentEquation [xf=" + xf + ", yf=" + yf + ", designValue=" + designValue + ", deltaValue="
				+ deltaValue + "]";
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
	
	
}
