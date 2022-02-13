/*-
 * #%L
 * Image-Registration
 * %%
 * Copyright (C) 2019, 2021 Oliver Loeffler, Raumzeitfalle.net
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
