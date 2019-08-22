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
package net.raumzeitfalle.registration.displacement;

import java.util.function.Predicate;

public class SiteSelection {
	
	public static SiteSelection multipoint() {
		Predicate<Displacement> all = d->true;
		return SiteSelection
					.forAlignment(all)
					.forCalculation(all)
					.forFirstOrderCalculation(all);
	}

	public static AlignmentSelection forAlignment(Predicate<Displacement> alignment) {
		return new AlignmentSelection(alignment);
	}
	
	public static AlignmentSelection alignOn(DisplacementClass siteClass) {
		return new AlignmentSelection(d->d.isOfType(siteClass));
	}
	
	private final Predicate<Displacement> alignment;
	
	private final Predicate<Displacement> calculation;
	
	private final Predicate<Displacement> firstOrderSelection;
	
	private final Predicate<Displacement> sitesToRemove;

	SiteSelection(Predicate<Displacement> alignment, Predicate<Displacement> calculation,
			Predicate<Displacement> firstOrderSelection) {
		this(alignment,calculation,firstOrderSelection,d->false);
	}
	
	SiteSelection(Predicate<Displacement> alignment, Predicate<Displacement> calculation,
			Predicate<Displacement> firstOrderSelection, Predicate<Displacement> sitesToRemove) {
		this.alignment = alignment;
		this.calculation = calculation;
		this.firstOrderSelection = firstOrderSelection;
		this.sitesToRemove = sitesToRemove;
	}
	
	public SiteSelection remove(Predicate<Displacement> sitesToRemove) {
		return new SiteSelection(alignment, calculation, firstOrderSelection, sitesToRemove);
	}
	
	public Predicate<Displacement> getAlignment() {
		return alignment;
	}

	public Predicate<Displacement> getCalculation() {
		return calculation.and(sitesToRemove.negate());
	}

	public Predicate<Displacement> getFirstOrderSelection() {
		return firstOrderSelection;
	}
	
	public Predicate<Displacement> getSitesToRemove() {
		return sitesToRemove;
	}
	
	
}
