package mask.registration.firstorder;

import java.util.function.Function;

import mask.registration.Displacement;

public class FirstOrderTransform implements Function<Displacement, Displacement> {

    public static FirstOrderTransform with(double transx, double transy,
            double scalex, double scaley,
            double orthox, double orthoy) {
        return new FirstOrderTransform(transx, transy, scalex, scaley, orthox, orthoy);
    }

    private final double translationX;

    private final double translationY;

    private final double scaleX;

    private final double scaleY;

    private final double orthoX;

    private final double orthoY;

    private FirstOrderTransform(double tx, double ty,
                                double sx, double sy,
                                double ox, double oy) {
        this.translationX = tx;
        this.translationY = ty;
        this.scaleX = sx;
        this.scaleY = sy;
        this.orthoX = ox;
        this.orthoY = oy;

    }

    public double getTranslationX() {
        return translationX;
    }

    public double getTranslationY() {
        return translationY;
    }

    public double getScaleX() {
        return scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public double getOrthoX() {
        return orthoX;
    }

    public double getOrthoY() {
        return orthoY;
    }

    @Override
    public String toString() {
        return "FirstOrderTransform [" +
               "tx=" + format(translationX) + ", sx=" + format(scaleX * 1E6) + ", ox=" + format(orthoX * 1E6) +
               System.lineSeparator() + "                     ty=" + format(translationY) + ", sy=" + format(scaleY * 1E6) + ", oy=" +
               format(orthoY * 1E6) + "]";
    }

    private String format(double value) {
        return String.format("%.6f", value);
    }

    @Override
    public Displacement apply(Displacement d) {
        return Displacement.of(d.getX(), d.getY(),
                d.getXd() - this.getTranslationX() + d.getXd() * this.getScaleX() + d.getY() * this.getOrthoX(),
                d.getYd() - this.getTranslationY() + d.getXd() * this.getScaleY() - d.getX() * this.getOrthoY(),
                d.getType());
    }

}
