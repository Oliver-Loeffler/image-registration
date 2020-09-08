package net.raumzeitfalle.registration.firstorder;

import java.util.*;
import java.util.stream.Collectors;

import net.raumzeitfalle.registration.displacement.Displacement;

public class PointGrid {
	
	public static PointGrid withNodes(int nodes) {
		return new PointGrid().ofNodes(nodes);
	}
	
	private int nodesPerDirection = 5;
	
	private double width = 140000d;
	
	private double height = 140000d;
	
	public PointGrid ofNodes(int nodes) {
		this.nodesPerDirection = nodes;
		return this;
	}

	public List<Displacement> create() {
		
		double pitchx = width / (nodesPerDirection-1);
		double pitchy = height / (nodesPerDirection-1);
		
		double posx = - width / 2;
		double posy = - height / 2;
		
		int index = 1;
		List<Displacement> displacements = new ArrayList<>(nodesPerDirection*nodesPerDirection);
		for (int x = 0; x < nodesPerDirection; x++) {
			for (int y = 0; y < nodesPerDirection; y++) {
				displacements.add(Displacement.at(index, index, posx + x*pitchx, 
						posy + y*pitchy, 
					    posx + x*pitchx,
					    posy + y*pitchy));
			}
		}
		
		return displacements;
	}
	
	public List<Displacement> moveBy(double shiftx, double shifty) {
		return create().stream()
					   .map(d->d.correctBy(shiftx, shifty))
					   .collect(Collectors.toList());
	}
	
	
}
