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
package net.raumzeitfalle.registration.firstorder;

import java.util.EnumSet;
import java.util.Set;

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
	
	public SiteSelection getSiteSelection() {
		return siteSelection;
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
	
	
}
