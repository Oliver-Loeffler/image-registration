package mask.registration.alignment;

import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import mask.registration.Displacement;

public class SimilarityAlignmentModelEquation {
    
    public static Stream<SimilarityAlignmentModelEquation> from(Displacement d) {
        
        Builder<SimilarityAlignmentModelEquation> builder = Stream.builder();
        
        if (Double.isFinite(d.getXd())) {
            builder.accept(SimilarityAlignmentModelEquation.forX(d));
        }
        
        if (Double.isFinite(d.getYd())) {
            builder.accept(SimilarityAlignmentModelEquation.forY(d));
        }
        
        return builder.build();
    }
    
    public static SimilarityAlignmentModelEquation forX(Displacement d) {
        return new SimilarityAlignmentModelEquation(d.getX(), 0.0, -d.getY(), 1.0, 0.0, d.dX());
    }
    
    public static SimilarityAlignmentModelEquation forY(Displacement d) {
        return new SimilarityAlignmentModelEquation(0.0, d.getY(), d.getX(), 0.0, 1.0, d.dY());
    }
        
    private final double scale_x;
    
    private final double scale_y;
    
    private final double rot;
    
    private final double tx;
    
    private final double ty;
    
    private final double deltaValue;
    
    private SimilarityAlignmentModelEquation(double sx, double sy, double rot, double tx, double ty, double delta) {
        this.scale_x = sx;
        this.scale_y = sy;
        this.rot = rot;
        this.tx = tx;
        this.ty = ty;
        this.deltaValue = delta;
    }

    @Override
    public String toString() {
        return "SimilarityAlignmentModelEquation [REF: (" + scale_x + ", " + scale_y + ", " + rot + ", " + tx + ", " + ty + "), Delta: (" + deltaValue + ") ]";
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
