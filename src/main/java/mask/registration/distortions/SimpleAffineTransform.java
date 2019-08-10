package mask.registration.distortions;

import mask.registration.Displacement;

public class SimpleAffineTransform implements AffineTransform {

    public static AffineTransform with(double transx, double transy,
            double scalex, double scaley,
            double orthox, double orthoy,
            double meanx, double meany) {
        return new SimpleAffineTransform(transx, transy, scalex, scaley, orthox, orthoy, meanx, meany);
    }

    private final double translationX;

    private final double translationY;

    private final double scaleX;

    private final double scaleY;

    private final double orthoX;

    private final double orthoY;
    
    private final double meanX;

    private final double meanY;

    protected SimpleAffineTransform(double tx, double ty,
                                double sx, double sy,
                                double ox, double oy,
                                double mx, double my) {
        this.translationX = tx;
        this.translationY = ty;
        this.scaleX = sx;
        this.scaleY = sy;
        this.orthoX = ox;
        this.orthoY = oy;
        
        this.meanX = mx;
        this.meanY = my;
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
    
    public double getCenterX() {
        return meanX;
    }

    public double getCenterY() {
        return meanY;
    }
    
    @Override
    public String toString() {
        return "FirstOrderTransform [" +
               "tx=" + format(translationX) + " um, sx=" + format(scaleX * 1E6) + " ppm, ox=" + format(orthoX * 1E6) + " urad" +
               System.lineSeparator() + "                     ty=" + format(translationY) + " um, sy=" + format(scaleY * 1E6) + " ppm, oy=" +
               format(orthoY * 1E6) + " urad,"
               + System.lineSeparator() + "                     mx=" + format(meanX) + " um, my=" + format(meanY)+" um]";
    }

    protected String format(double value) {
        return String.format("%10.7f", value);
    }

    @Override
    public Displacement apply(Displacement d) {
    	return Displacement.from(d,
    			d.getXd() - d.getXd() * this.getScaleX() + d.getY() * this.getOrthoX(),
                d.getYd() - d.getYd() * this.getScaleY() - d.getX() * this.getOrthoY());
    }

}
