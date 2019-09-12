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
package net.raumzeitfalle.registration.firstorder;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Predicate;

import net.raumzeitfalle.registration.displacement.Displacement;
import net.raumzeitfalle.registration.displacement.SiteSelection;

public final class FirstOrderSetup {
	
	private SiteSelection siteSelection;
	
	private Alignments alignment;
	
	private Set<Compensations> compensations;
	
	public static FirstOrderSetup usingAlignment(Alignments alignment) {
		return new FirstOrderSetup().withAlignment(alignment);
	}
	
	public FirstOrderSetup() {
		this.siteSelection = SiteSelection.multipoint();
		this.alignment = Alignments.UNALIGNED;
		this.compensations = EnumSet.noneOf(Compensations.class);
	}
	
	public FirstOrderSetup withAlignment(Alignments newAlignment) {
		this.alignment = newAlignment;
		return this;
	}
	
	public FirstOrderSetup withCompensations(Compensations ...compensation) {
		for (Compensations c : compensation) {
			this.compensations.add(c);
		}
		return this;
	}
	
	public FirstOrderSetup withSiteSelection(SiteSelection selection) {
		this.siteSelection = selection;
		return this;
	}
	
	

	public Alignments getAlignment() {
		return alignment;
	}

	public Set<Compensations> getCompensations() {
		return compensations;
	}

	@Override
	public String toString() {
		return "FirstOrderSetup [siteSelection=" + siteSelection + ", alignment=" + alignment + ", compensations="
				+ compensations + "]";
	}

	public Predicate<Displacement> getAlignmenSelection() {
		return this.siteSelection.getAlignment();
	}

	public Predicate<Displacement> getCalculationSelection() {
		if (alignment.equals(Alignments.SCANNER_SELECTED)) {
			return siteSelection.getAlignment();
		}
		return siteSelection.getCalculation();
	}
	
	
}
