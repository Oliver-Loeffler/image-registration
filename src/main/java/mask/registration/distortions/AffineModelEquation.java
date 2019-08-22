package mask.registration.distortions;

import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import mask.registration.displacement.Displacement;

public class AffineModelEquation {
    
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
        return new AffineModelEquation(d.getX(), 0.0, 0.0, d.getY(), 1.0, 0.0, d.dX());
    }
    
    public static AffineModelEquation forY(Displacement d) {
        return new AffineModelEquation(0.0,     d.getY(), -d.getX(), 0.0, 0.0, 1.0, d.dY());
    }
        
    private final double sx;
    
    private final double sy;
    
    private final double ox;
    
    private final double oy;
    
    private final double tx;
    
    private final double ty;
    
    private final double deltaValue;
    
    private AffineModelEquation(double sx, double sy, double ox, double oy, double tx, double ty, double delta) {
        this.sx = sx;
        this.sy = sy;
        this.ox = ox;
        this.oy = oy;
        this.tx = tx;
        this.ty = ty;
        this.deltaValue = delta;
        
    }

    @Override
    public String toString() {
        return "FirstOrderEquation [REF: (" + sx + ", " + sy + ", " + ox + ", " + oy + ", " + tx + ", " + ty + "), Delta: (" + deltaValue + ") ]";
    }

    double getSx() {
        return sx;
    }

    double getSy() {
        return sy;
    }

    double getOx() {
        return ox;
    }

    double getOy() {
        return oy;
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
