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

public class NonUniformSimilarityModelEquation {
    
    public static Stream<NonUniformSimilarityModelEquation> from(Displacement d) {
        
        Builder<NonUniformSimilarityModelEquation> builder = Stream.builder();
        
        if (Double.isFinite(d.getXd())) {
            builder.accept(NonUniformSimilarityModelEquation.forX(d));
        }
        
        if (Double.isFinite(d.getYd())) {
            builder.accept(NonUniformSimilarityModelEquation.forY(d));
        }
        
        return builder.build();
    }
    
    public static NonUniformSimilarityModelEquation forX(Displacement d) {
        return new NonUniformSimilarityModelEquation(d.getX(), 0.0, -d.getY(), 1.0, 0.0, d.dX());
    }
    
    public static NonUniformSimilarityModelEquation forY(Displacement d) {
        return new NonUniformSimilarityModelEquation(0.0, d.getY(), d.getX(), 0.0, 1.0, d.dY());
    }
        
    private final double scale_x;
    
    private final double scale_y;
    
    private final double rot;
    
    private final double tx;
    
    private final double ty;
    
    private final double deltaValue;
    
    private NonUniformSimilarityModelEquation(double sx, double sy, double rot, double tx, double ty, double delta) {
        this.scale_x = sx;
        this.scale_y = sy;
        this.rot = rot;
        this.tx = tx;
        this.ty = ty;
        this.deltaValue = delta;
    }

    @Override
    public String toString() {
        return "NonUniformSimilarityModelEquation [REF: (" + scale_x + ", " + scale_y + ", " + rot + ", " + tx + ", " + ty + "), Delta: (" + deltaValue + ") ]";
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
    
    double getSx() {
        return scale_x;
    }

    double getSy() {
        return scale_y;
    }

    double getDeltaValue() {
        return deltaValue;
    }
       
}
