package net.raumzeitfalle.registration.ejml.bundle;

/**
 * This library is just a workaround to get EJML (Efficient Java Matrix Library) working with Java Modules.
 * It is basically a replica of the {@code "org.ejml:ejml-simple:0.41"} artifact with all classes included (fat jar).
 * 
 * @author Oliver L&ouml;ffler
 * 
 */
public final class Readme {
    Readme() {
        throw new UnsupportedOperationException("Please use \"org.ejml:ejml-simple:0.41\" for working with EJML");
    }
}
