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

import net.raumzeitfalle.registration.displacement.Displacement;

final class SimilarityModelEquation {
    
    static Stream<SimilarityModelEquation> from(Displacement d) {
        
        Builder<SimilarityModelEquation> builder = Stream.builder();
        
        if (Double.isFinite(d.getXd())) {
            builder.accept(SimilarityModelEquation.forX(d));
        }
        
        if (Double.isFinite(d.getYd())) {
            builder.accept(SimilarityModelEquation.forY(d));
        }
        
        return builder.build();
    }
    
    static SimilarityModelEquation forX(Displacement d) {
        return new SimilarityModelEquation(d.getX(), -d.getY(), 1.0, 0.0, d.dX());
    }
    
    static SimilarityModelEquation forY(Displacement d) {
        return new SimilarityModelEquation(d.getY(), d.getX(), 0.0, 1.0, d.dY());
    }
        
    private final double mag;
    
    private final double rot;
    
    private final double tx;
    
    private final double ty;
    
    private final double deltaValue;
    
    private SimilarityModelEquation(double mag, double rot, double tx, double ty, double delta) {
        this.mag = mag;
        this.rot = rot;
        this.tx = tx;
        this.ty = ty;
        this.deltaValue = delta;
    }

    @Override
    public String toString() {
        return "FirstOrderAlignmentEquation [REF: (" + mag + ", " + rot + ", " + tx + ", " + ty + "), Delta: (" + deltaValue + ") ]";
    }

    double getMag() {
        return mag;
    }

    double getRot() {
        return rot;
    }

    double getTx() {
        return tx;
    }

    double getTy() {
        return ty;
    }

    double getDeltaValue() {
        return deltaValue;
    }
       
}
