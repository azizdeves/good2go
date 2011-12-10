package com.gdma.good2goserver;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;


import com.google.appengine.api.datastore.GeoPt;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.HashSet;
import flexjson.JSONSerializer;
import flexjson.JSONDeserializer;

@SuppressWarnings("serial")
public class Good2GoServerServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(Good2GoServerServlet.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {	
		
		PrintWriter pw = resp.getWriter();
		
		String action = req.getParameter(new String("action"));
		
		if (action.compareToIgnoreCase("getEvents")==0){
			String lo = req.getParameter(new String("lon"));
			String la = req.getParameter(new String("lat"));
			float lon = Float.valueOf(lo.trim()).floatValue();
			float lat = Float.valueOf(la.trim()).floatValue();
			/*pw.println(lon);
			pw.println(lat);*/
		}
		
		Set<Event.VolunteeringWith> vw = new HashSet<Event.VolunteeringWith>();
		vw.add(Event.VolunteeringWith.CHILDREN);
		vw.add(Event.VolunteeringWith.MENTALLY_CHALLENGED);
		
		Set<Event.SuitableFor> sf = new HashSet<Event.SuitableFor>();
		sf.add(Event.SuitableFor.GROUPS);
		
		Set<Event.WorkType> wt = new HashSet<Event.WorkType>();
		wt.add(Event.WorkType.METNAL);
		
		Event e = null;
		Event ev2 = null;
		try{
			Event.Address a = null;
		
			a = new Event.Address();
			
			a.setCity("TLV");
			a.setStreet("Weissman");
			a.setNumber((short) 100);
			a.setGeoPoint(new GeoPt((float) 1.0, (float) 2.0));
			
			e = new Event("newEvent", "Fundraiser", "You have to be filthy rich", new Date(), false, a, "FilthyRichForPoorPersons", vw, sf, wt, true);
			ev2 = new Event("oldEvent", "e2Event", "none", new Date(), false, new Event.Address(a), "ABCDE", null, null, null, true);
		}
		catch (Exception ex){
			pw.println(ex.getMessage());
		}
		
		Event.Occurrence o = new Event.Occurrence();
		
		o.setEventDate(2011, 12, 12);
		o.setStartTime(10, 0);
		o.setEndTime(20, 20);
		o.addRegisteredUser("Dana");
		o.addRegisteredUser("Gil");
		
		e.addOccurrence(o);
		
		o = new Event.Occurrence();
		
		o.setEventDate(2011,12,30);
		o.setStartTime(12, 30);
		o.setEndTime(15, 40);
		o.addRegisteredUser("Gil");
		o.addRegisteredUser("Mor");
		
		e.addOccurrence(o);
		
		List<Event> l1 = new LinkedList<Event>();
		l1.add(e);
		l1.add(ev2);
		
		String js = new JSONSerializer().include("occurrences", "occurrences.registeredUserNames", "volunteeringWith", "suitableFor", "workType").serialize(l1);
		
		resp.setContentType("text/plain");
		
		pw.println(js);
		
		List<Event> l2 = new JSONDeserializer<List<Event>>().deserialize(js);
		
		pw.println("!");
	}
}
