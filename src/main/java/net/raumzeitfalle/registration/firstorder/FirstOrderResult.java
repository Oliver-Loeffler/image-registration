package net.raumzeitfalle.registration.firstorder;

import java.util.Collection;
import java.util.Objects;

import net.raumzeitfalle.registration.alignment.RigidTransform;
import net.raumzeitfalle.registration.displacement.Displacement;
import net.raumzeitfalle.registration.distortions.AffineTransform;

/*
 * TODO: Consider storing of total (unaligned) alignment&first order, then also corrected alignment&first order.
 */
public class FirstOrderResult {
	
	private final RigidTransform alignment;
	
	private final AffineTransform firstOrder;
	
	private final Collection<Displacement> displacements;
	
	protected FirstOrderResult(RigidTransform alignment, AffineTransform firstOrder, Collection<Displacement> results) {
		this.alignment = Objects.requireNonNull(alignment, "Alignment (RigidTransform) must not be null.");
		this.firstOrder = Objects.requireNonNull(firstOrder, "FirstOrder (AffineTransform) must not be null.");
		this.displacements = Objects.requireNonNull(results, "Collection of Displacements (results) must not be null.");
	}

	public RigidTransform getAlignment() {
		return alignment;
	}

	public AffineTransform getFirstOrder() {
		return firstOrder;
	}

	public Collection<Displacement> getDisplacements() {
		return displacements;
	}
	
}
