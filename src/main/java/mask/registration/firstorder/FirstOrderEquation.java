package mask.registration.firstorder;

import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import mask.registration.Displacement;

public class FirstOrderEquation {
    
    public static Stream<FirstOrderEquation> from(Displacement d) {
        
        Builder<FirstOrderEquation> builder = Stream.builder();
        
        if (Double.isFinite(d.getXd())) {
            builder.accept(FirstOrderEquation.forX(d));
        }
        
        if (Double.isFinite(d.getYd())) {
            builder.accept(FirstOrderEquation.forY(d));
        }
        
        return builder.build();
    }
    
    public static FirstOrderEquation forX(Displacement d) {
        return new FirstOrderEquation(d.getX(), 0.0, 0.0, d.getY(), 0.0, 0.0, d.dX());
    }
    
    public static FirstOrderEquation forY(Displacement d) {
        return new FirstOrderEquation(0.0, d.getY(), -d.getX(), 0.0, 0.0, 0.0, d.dY());
    }
       
    private final double sx;
    
    private final double sy;
    
    private final double ox;
    
    private final double oy;
    
    private final double tx;
    
    private final double ty;
    
    private final double deltaValue;
    
    private FirstOrderEquation(double sx, double sy, double ox, double oy, double tx, double ty, double delta) {
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
