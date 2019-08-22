/*
 *   Copyright 2019 Oliver Löffler, Raumzeitfalle.net
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package net.raumzeitfalle.registration.alignment;

import java.util.Collection;
import java.util.function.Function;

import net.raumzeitfalle.registration.displacement.Displacement;


/**
 * 
 * Allows to correct rotation and translation (x,y) for a given displacement.
 * 
 * @author oliver
 *
 */
public interface RigidTransform extends Function<Collection<Displacement>, Collection<Displacement>> {
	
	double getTranslationX();
	double getTranslationY();
	double getRotation();
	
}
