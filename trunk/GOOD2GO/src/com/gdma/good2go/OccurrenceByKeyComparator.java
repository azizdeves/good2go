package com.gdma.good2go;

import java.util.Comparator;

public class OccurrenceByKeyComparator implements Comparator<Occurrence>{

	@Override
	public int compare(Occurrence occ1, Occurrence occ2) {
		return occ1.getOccurrenceKey().compareTo(occ2.getOccurrenceKey());
	}
	
}
