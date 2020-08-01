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

import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import net.raumzeitfalle.registration.ModelEquation;
import net.raumzeitfalle.registration.Orientation;
import net.raumzeitfalle.registration.displacement.Displacement;

public final class AffineModelEquation implements ModelEquation {
    
    public static Stream<AffineModelEquation> from(Displacement d) {
        
        Builder<AffineModelEquation> builder = Stream.builder();
        
        if (Double.isFinite(d.getXd())) {
            builder.accept(AffineModelEquation.forX(d));
        }
        
        if (Double.isFinite(d.getYd())) {
            builder.accept(AffineModelEquation.forY(d));
        }
        
        return builder.build();
    }
    
    public static AffineModelEquation forX(Displacement d) {	
    	return new AffineModelEquation(d.getX(), d.getY(), d.dX(), Orientation.X);
    }
    
    public static AffineModelEquation forY(Displacement d) {
    	return new AffineModelEquation(d.getX(), d.getY(), d.dY(), Orientation.Y);
    }
        
    private final double sx;
    
    private final double sy;
    
    private final double ox;
    
    private final double oy;
    
    private final double tx;
    
    private final double ty;
    
    private final double deltaValue;

	private final Orientation direction;
    
	private AffineModelEquation(double refx, double refy, double delta, Orientation direction) {
		this.sx = Orientation.X.equals(direction) ? refx : 0.0;
		this.sy = Orientation.X.equals(direction) ? 0.0  : refy;
		
		this.ox = Orientation.X.equals(direction) ? 0.0  : -refx;
		this.oy = Orientation.X.equals(direction) ? refy : 0.0;
		
		this.tx = Orientation.X.equals(direction) ? 1.0  : 0.0;
		this.ty = Orientation.X.equals(direction) ? 0.0  : 1.0;
		
		this.deltaValue = delta;
		this.direction = direction;
	}

    @Override
    public String toString() {
        return "FirstOrderEquation [REF: (" + sx + ", " + sy + ", " + ox + ", " + oy + ", " + tx + ", " + ty + "), Delta: (" + deltaValue + ") ]";
    }

    public double getSx() {
        return sx;
    }

    public double getSy() {
        return sy;
    }

    public double getOx() {
        return ox;
    }

    public double getOy() {
        return oy;
    }

    public double getTx() {
        return tx;
    }

    public double getTy() {
        return ty;
    }

    public double getDeltaValue() {
        return deltaValue;
    }

	@Override
	public Orientation getOrientation() {
		return direction;
	}
       
}
