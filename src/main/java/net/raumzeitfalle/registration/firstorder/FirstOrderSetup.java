package net.raumzeitfalle.registration.firstorder;

import java.util.EnumSet;
import java.util.Set;

import net.raumzeitfalle.registration.displacement.SiteSelection;

public class FirstOrderSetup {
	
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
