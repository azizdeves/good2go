package com.gdma.good2goserver;

import java.util.HashSet;
import java.util.Set;

import com.gdma.good2goserver.Event.SuitableFor;
import com.gdma.good2goserver.Event.VolunteeringWith;
import com.gdma.good2goserver.Event.WorkType;

public class HTTranslate implements NPOTranslate{

	@Override
	public Set<Event.VolunteeringWith> getVolunteeringWith(String type) {
		Set<Event.VolunteeringWith> vw = new HashSet<Event.VolunteeringWith>();
		
		if (type.equals(new String("חסד ונזקקים")))
			vw.add(Event.VolunteeringWith.DISADVANTAGED);
		else if (type.equals(new String("כנסים ואירועים")))
			vw.add(Event.VolunteeringWith.OTHER);
		else if (type.equals(new String("סביבה ובעלי חיים"))){
			vw.add(Event.VolunteeringWith.ENVIRONMENT);
			vw.add(Event.VolunteeringWith.ANIMALS);
		}
		else if (type.equals(new String("פעילות עם אנשים")))
			vw.add(Event.VolunteeringWith.OTHER);
		else if (type.equals(new String("שונות")))
			vw.add(Event.VolunteeringWith.OTHER);
		else if (type.equals(new String("שיפוצים וצביעות")))
			vw.add(Event.VolunteeringWith.OTHER);
		
		return vw;
	}

	@Override
	public Set<WorkType> getWorkType(String type) {
		Set<Event.WorkType> wt = new HashSet<Event.WorkType>();
		
		if (type.equals(new String("חסד ונזקקים")))
			wt.add(Event.WorkType.MENIAL);
		else if (type.equals(new String("כנסים ואירועים"))){
			wt.add(Event.WorkType.MENIAL);
			wt.add(Event.WorkType.MENTAL);
		}
		else if (type.equals(new String("סביבה ובעלי חיים")))
			wt.add(Event.WorkType.MENIAL);
		else if (type.equals(new String("פעילות עם אנשים"))){
			wt.add(Event.WorkType.MENIAL);
			wt.add(Event.WorkType.MENTAL);
		}
		else if (type.equals(new String("שונות"))){
			wt.add(Event.WorkType.MENIAL);
			wt.add(Event.WorkType.MENTAL);
		}
		else if (type.equals(new String("שיפוצים וצביעות")))
			wt.add(Event.WorkType.MENIAL);
		
		return wt;
	}

}
