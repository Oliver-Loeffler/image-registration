package mask.registration.firstorder;

import java.util.EnumSet;
import java.util.Set;

import mask.registration.displacement.SiteSelection;

public class FirstOrderSetup {
	
	private SiteSelection siteSelection = SiteSelection.multipoint();
	
	private Alignments alignment = Alignments.UNALIGNED;
	
	private Set<Compensations> compensations = EnumSet.noneOf(Compensations.class);
	
	public static FirstOrderSetup usingAlignment(Alignments alignment) {
		return new FirstOrderSetup().withAlignment(alignment);
	}
	
	public FirstOrderSetup() {
		
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
