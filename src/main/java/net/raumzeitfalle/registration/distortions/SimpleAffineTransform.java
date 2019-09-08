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

import java.util.Locale;

import net.raumzeitfalle.registration.displacement.Displacement;

public class SimpleAffineTransform implements AffineTransform {

    public static SimpleAffineTransform with(double transx, double transy,
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
                                
                                @Deprecated  
                                double mx, 
                                
                                @Deprecated
                                double my) {
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
    
    public double getMagnification() {
    	return (this.scaleX + this.scaleY)/2;
    }

    public double getOrthoX() {
        return orthoX;
    }

    public double getOrthoY() {
        return orthoY;
    }
    
    public double getOrtho() {
    	return orthoY - orthoX;
    }
    
    public double getCenterX() {
        return meanX;
    }

    public double getCenterY() {
        return meanY;
    }
    
    @Override
    public String toString() {
        return "AffineTransform [" +
               "tx=" + format(translationX) + " um, sx=" + format(scaleX * 1E6) + " ppm, ox=" + format(orthoX * 1E6) + " urad" +
               System.lineSeparator() + "                 ty=" + format(translationY) + " um, sy=" + format(scaleY * 1E6) + " ppm, oy=" +
               format(orthoY * 1E6) + " urad,"
               + System.lineSeparator() + "                 mx=" + format(meanX) + " um, my=" + format(meanY)+" um]";
    }

    protected String format(double value) {
        return String.format(Locale.US, "%10.7f", value);
    }

    @Override
    public Displacement apply(Displacement d) {
    	return Displacement.from(d,
    			d.getXd() - d.getX() * this.getScaleX() + d.getY() * this.getOrthoX(),
                d.getYd() - d.getY() * this.getScaleY() - d.getX() * this.getOrthoY());
    }

	@Override
	public boolean skip() {
		return false;
	}

}
