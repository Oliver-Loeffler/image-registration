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
package net.raumzeitfalle.registration.distortions;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import net.raumzeitfalle.registration.displacement.Displacement;

public class AffineTransformCorrection implements BiFunction<AffineTransform, Collection<Displacement>, Collection<Displacement>> {
    
    @Override
    public Collection<Displacement> apply(AffineTransform t, Collection<Displacement> u) {

        return u.stream()
                .map(t)
                .collect(Collectors.toList());
        
    }
    
    

}