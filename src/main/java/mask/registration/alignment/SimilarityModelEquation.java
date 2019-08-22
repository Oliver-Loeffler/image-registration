package mask.registration.alignment;

import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import mask.registration.displacement.Displacement;

public class SimilarityModelEquation {
    
    public static Stream<SimilarityModelEquation> from(Displacement d) {
        
        Builder<SimilarityModelEquation> builder = Stream.builder();
        
        if (Double.isFinite(d.getXd())) {
            builder.accept(SimilarityModelEquation.forX(d));
        }
        
        if (Double.isFinite(d.getYd())) {
            builder.accept(SimilarityModelEquation.forY(d));
        }
        
        return builder.build();
    }
    
    public static SimilarityModelEquation forX(Displacement d) {
        return new SimilarityModelEquation(d.getX(), -d.getY(), 1.0, 0.0, d.dX());
    }
    
    public static SimilarityModelEquation forY(Displacement d) {
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
