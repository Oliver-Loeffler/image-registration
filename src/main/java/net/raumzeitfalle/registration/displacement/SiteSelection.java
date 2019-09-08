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
package net.raumzeitfalle.registration.displacement;

import java.util.function.Predicate;

/**
 * 
 * As displacements may have different purpose, depending on where (e.g. on a
 * photomask) these are located, a selection mechanism is needed in oder to
 * control the data evaluation process.
 * <p>
 * This class serves as a builder to formulate and create the required
 * {@link Predicate} instances in order to do the correct filtering and
 * calculations. {@link SiteSelection} bundles all criteria and predicates
 * required to perform any related data selection under one hood. Therefore at
 * the right point it is sufficient to create an appropriately configured
 * {@link SiteSelection} instance and pass it into the evaluation process.
 * 
 * @author Oliver Loeffler
 */
public class SiteSelection {

	/**
	 * Selects all displacements for alignment, positional and first order
	 * calculation.
	 * 
	 * @return {@link SiteSelection} with all locations selected for each step of an
	 *         evaluation.
	 */
	public static SiteSelection multipoint() {
		Predicate<Displacement> all = d -> true;
		return SiteSelection.forAlignment(all).forCalculation(all).build();
	}

	/**
	 * Uses the given {@link Predicate} to select displacements for data alignment.
	 * 
	 * @param alignmentSelector {@link Predicate} for {@link Displacement}
	 * @return {@link AlignmentSelection}
	 */
	public static AlignmentSelection forAlignment(Predicate<Displacement> alignmentSelector) {
		return new AlignmentSelection(alignmentSelector);
	}

	/**
	 * 
	 * Selects all displacements of a given {@link Category} for data alignment.
	 * 
	 * @param category {@link Category}
	 * @return {@link AlignmentSelection}
	 */
	public static AlignmentSelection alignOn(Category category) {
		return new AlignmentSelection(d -> d.belongsTo(category));
	}

	private final Predicate<Displacement> alignment;

	private final Predicate<Displacement> calculation;

	private final Predicate<Displacement> sitesToRemove;

	SiteSelection(Predicate<Displacement> alignment, Predicate<Displacement> calculation) {
		this(alignment, calculation, d -> false);
	}

	SiteSelection(Predicate<Displacement> alignment, Predicate<Displacement> calculation,
			Predicate<Displacement> sitesToRemove) {
		this.alignment = alignment;
		this.calculation = calculation;
		this.sitesToRemove = sitesToRemove;
	}

	/**
	 * All {@link Displacement} instances which match this {@link Predicate} will be
	 * removed from subsequent processes. When called, a new {@link SiteSelection}
	 * instance is created.
	 * 
	 * @param sitesToRemove {@link Predicate} for {@link Displacement}
	 * @return {@link SiteSelection}
	 */
	public SiteSelection remove(Predicate<Displacement> sitesToRemove) {
		return new SiteSelection(alignment, calculation, sitesToRemove);
	}

	/**
	 * Provides the {@link Predicate} used to select displacements for alignment.
	 * 
	 * @return {@link Predicate}
	 */
	public Predicate<Displacement> getAlignment() {
		return alignment;
	}

	/**
	 * Provides the {@link Predicate} used to select displacements for positionals
	 * calculation.
	 * 
	 * @return {@link Predicate}
	 */
	public Predicate<Displacement> getCalculation() {
		return calculation.and(sitesToRemove.negate());
	}

	/**
	 * Provides the {@link Predicate} used to find displacements which shall be
	 * removed.
	 * 
	 * @return {@link Predicate}
	 */
	public Predicate<Displacement> getSitesToRemove() {
		return sitesToRemove;
	}

}
