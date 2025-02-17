package net.raumzeitfalle.registration.distortions;

import java.util.stream.Stream;

import net.raumzeitfalle.registration.ModelEquation;
import net.raumzeitfalle.registration.Orientation;
import net.raumzeitfalle.registration.Orientations;
import net.raumzeitfalle.registration.displacement.Displacement;

public class HigherOrderEquation implements ModelEquation {
    
    public static Stream<HigherOrderEquation> from(Displacement d) {

        Stream.Builder<HigherOrderEquation> builder = Stream.builder();

        if (Double.isFinite(d.getXd())) {
            builder.accept(HigherOrderEquation.byOrientation(d, Orientations.X));
        }

        if (Double.isFinite(d.getYd())) {
            builder.accept(HigherOrderEquation.byOrientation(d, Orientations.Y));
        }

        return builder.build();
    }

    // K1 - translation X - K1
    // K2 - translation Y - K2
    // K3 - scale X - K3
    // K4 - scale Y - K4
    // K5 - orthox - K5
    // K6 - orthoy - K6
    // K7 - X^2 - dx
    // K8 - Y^2 - dy
    // K9 - XY - dx
    // K10 - YX - dy

    private final double deltaValue;
    private final Orientation direction;
    private double[] modelCoeffs;

    static HigherOrderEquation byOrientation(Displacement d, Orientation direction) {
        double refx = d.getX();
        double refy = d.getY();
        if (Orientations.X.equals(direction)) {
            // K1 - K2 - K3 - K4 - K5 - K6 - K7 - K8 - K9 - K10
            // X: 0 0 0 0 0
            double[] ks = new double[] {1.0, 0.0, refx, 0.0, refy, 0.0, refx * refx, 0.0, refx * refy, 0.0};
            return new HigherOrderEquation(ks, d.dX(), direction);
        }

        if (Orientations.Y.equals(direction)) {
            // K1 - K2 - K3 - K4 - K5 - K6 - K7 - K8 - K9 - K10
            // X: 0 1 0 1 0 1 0 1 0 1
            double[] ks = new double[] {0.0, 1.0, 0.0, refy, 0.0, -refx, 0.0, refy * refy, 0.0, refx * refx};
            return new HigherOrderEquation(ks, d.dY(), direction);
        }

        throw new IllegalArgumentException("An equation is only one-dimensional. Hence it can be only X or Y.");
    }

    private HigherOrderEquation(double[] coeffs, double delta, Orientation direction) {
        this.deltaValue = delta;
        this.direction = direction;
        this.modelCoeffs = new double[coeffs.length];
        System.arraycopy(coeffs, 0, this.modelCoeffs, 0, coeffs.length);
    }

    @Override
    public String toString() {
        StringBuilder bldr = new StringBuilder("HigherOrderEforqn [REF: (");
        
        int r = this.modelCoeffs.length;
        for (int i = 0; i < this.modelCoeffs.length; i++) {
            bldr.append("K").append(i).append("=").append(Double.toString(this.modelCoeffs[i]));
            if (r > 0) {
                bldr.append(",");
            }
        }
        bldr.append("), Delta: (")
            .append(Double.toString(deltaValue))
            .append(")]");
            
        return bldr.toString();
    }

    @Override
    public Orientation getOrientation() {
        return this.direction;
    }

    @Override
    public double getDeltaValue() {
        return this.deltaValue;
    }

    private double getCoeff(int k) {
        if (modelCoeffs.length < 2) {
            return 0.0;
        }
        return this.modelCoeffs[k - 1];
    }

    public double getK1() {
        return getCoeff(1);
    }

    public double getK2() {
        return getCoeff(2);
    }

    public double getK3() {
        return getCoeff(3);
    }

    public double getK4() {
        return getCoeff(4);
    }

    public double getK5() {
        return getCoeff(5);
    }

    public double getK6() {
        return getCoeff(6);
    }

    public double getK7() {
        return getCoeff(7);
    }

    public double getK8() {
        return getCoeff(8);
    }

    public double getK9() {
        return getCoeff(9);
    }

    public double getK10() {
        return getCoeff(10);
    }

}
