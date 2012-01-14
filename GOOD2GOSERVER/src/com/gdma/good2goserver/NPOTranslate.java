package com.gdma.good2goserver;

import java.util.Set;

public interface NPOTranslate {

	public Set<Event.VolunteeringWith> getVolunteeringWith(String type);
	
	public Set<Event.WorkType> getWorkType(String type);
	
}
