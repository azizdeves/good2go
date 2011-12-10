package com.gdma.good2goserver;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.*;
import com.google.appengine.api.datastore.GeoPt;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.HashSet;
import flexjson.JSONSerializer;
import flexjson.JSONDeserializer;
import com.gdma.good2goserver.Occurrence;

@SuppressWarnings("serial")
public class Good2GoServerServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(Good2GoServerServlet.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {	
		
		Good2GoDatabaseManager dbm = new Good2GoDatabaseManager();
		
		PrintWriter pw = resp.getWriter();
		
		String action = req.getParameter(new String("action"));
		
		/*
		if (action.compareToIgnoreCase("getEvents")==0){
			String lo = req.getParameter(new String("lon"));
			String la = req.getParameter(new String("lat"));
			float lon = Float.valueOf(lo.trim()).floatValue();
			float lat = Float.valueOf(la.trim()).floatValue();
			pw.println(lon);
			pw.println(lat);
		}*/
		
		if (action.compareToIgnoreCase("addEvents")==0){
			
			Set<Event.VolunteeringWith> vw = new HashSet<Event.VolunteeringWith>();
			vw.add(Event.VolunteeringWith.CHILDREN);
			vw.add(Event.VolunteeringWith.PHYSICALLY_CHALLENGED);
			
			Set<Event.SuitableFor> sf = new HashSet<Event.SuitableFor>();
			sf.add(Event.SuitableFor.GROUPS);
			sf.add(Event.SuitableFor.INDIVIDUALS);
			
			Set<Event.WorkType> wt = new HashSet<Event.WorkType>();
			wt.add(Event.WorkType.MENIAL);
			
			Event e = null;
			
			// First event.
			
			Event.Address a = null;
		
			a = new Event.Address();
			
			a.setCity("TLV");
			a.setStreet("Weissman");
			a.setNumber((short) 100);
			a.setGeoPoint(new GeoPt((float) 32.069156, (float) 34.774003));
			
			e = new Event("Fun Horseback Riding", "Help handicapped teenagers and enjoy a horseack ride",
						  "Assist handicapped teenagers in therapeutic horse-back riding, lead their horse and help them follow instructors commands.",
						  new Date(), false, a, "FilthyRichForPoorPersons", vw, sf, wt, true);
			
			Occurrence o = new Occurrence();
			
			o.setOccurrenceDate(2011, 12, 12);
			o.setStartTime(10, 0);
			o.setEndTime(20, 20);
			o.addRegisteredUser("Dana");
			o.addRegisteredUser("Gil");
			
			e.addOccurrence(o);
			
			o = new Occurrence();
			
			o.setOccurrenceDate(2011,12,30);
			o.setStartTime(12, 30);
			o.setEndTime(15, 40);
			o.addRegisteredUser("Gil");
			o.addRegisteredUser("Mor");
			
			e.addOccurrence(o);
			
			dbm.addEvent(e);

			// Second event.
			
			vw = new HashSet<Event.VolunteeringWith>();
			vw.add(Event.VolunteeringWith.ANIMALS);
			
			sf = new HashSet<Event.SuitableFor>();
			sf.add(Event.SuitableFor.GROUPS);
			sf.add(Event.SuitableFor.INDIVIDUALS);
			sf.add(Event.SuitableFor.CHILDREN);
			
			wt = new HashSet<Event.WorkType>();
			wt.add(Event.WorkType.MENIAL);

			a = new Event.Address();
			
			a.setCity("TLV");
			a.setStreet("somewhere");
			a.setNumber((short) 1);
			a.setGeoPoint(new GeoPt((float) 32.069211, (float) 34.763403));
			
			e = new Event("Dogs are our best friends", "Have a walk with a city chelter dog","Make a furry cute friend for life!",
						  new Date(), false, a, "CityShelter", vw, sf, wt, true);
			
			o = new Occurrence();
			
			o.setOccurrenceDate(2011, 12, 12);
			o.setStartTime(11, 30);
			o.setEndTime(13, 40);
			o.addRegisteredUser("Mor");
			o.addRegisteredUser("Adi");
			
			e.addOccurrence(o);
			
			o = new Occurrence();
			
			o.setOccurrenceDate(2011,12,30);
			o.setStartTime(9, 20);
			o.setEndTime(20, 15);
			o.addRegisteredUser("Dana");
			o.addRegisteredUser("Shimon Peres");
			
			e.addOccurrence(o);
			
			dbm.addEvent(e);

			// Third event.
			
			vw = new HashSet<Event.VolunteeringWith>();
			vw.add(Event.VolunteeringWith.CHILDREN);
			
			sf = new HashSet<Event.SuitableFor>();
			sf.add(Event.SuitableFor.INDIVIDUALS);
			
			wt = new HashSet<Event.WorkType>();
			wt.add(Event.WorkType.MENTAL);
	
			a = new Event.Address();
			
			a.setCity("TLV");
			a.setStreet("Center");
			a.setNumber((short) 20);
			a.setGeoPoint(new GeoPt((float) 32.086865, (float) 34.789581));
			
			e = new Event("Surf the internet", "Show the wonders of Google and Wikipedia to children","Share what you know by teaching internet to kids!",
						  new Date(), false, a, "Google Inc.", vw, sf, wt, true);
			
			o = new Occurrence();
			
			o.setOccurrenceDate(2011, 12, 12);
			o.setStartTime(10, 00);
			o.setEndTime(17, 25);
			o.addRegisteredUser("Avi");
			o.addRegisteredUser("Hezi");
			
			e.addOccurrence(o);
			
			o = new Occurrence();
			
			o.setOccurrenceDate(2011,12,30);
			o.setStartTime(14, 55);
			o.setEndTime(23, 00);
			o.addRegisteredUser("Shlomo");
			
			e.addOccurrence(o);
			
			dbm.addEvent(e);
	
			// Fourth event.
			
			vw = new HashSet<Event.VolunteeringWith>();
			vw.add(Event.VolunteeringWith.ELDERLY);
			
			sf = new HashSet<Event.SuitableFor>();
			sf.add(Event.SuitableFor.INDIVIDUALS);
			sf.add(Event.SuitableFor.CHILDREN);
			
			wt = new HashSet<Event.WorkType>();
			wt.add(Event.WorkType.MENTAL);
	
			a = new Event.Address();
			
			a.setCity("TLV");
			a.setStreet("Einstein");
			a.setNumber((short) 30);
			a.setGeoPoint(new GeoPt((float) 32.074938, (float) 34.775591));
			
			e = new Event("Read your favourite book", "Make someone happy and provide company to the elderly","Read anything you like to the elderly",
						  new Date(), false, a, "Mishan", vw, sf, wt, true);
			
			o = new Occurrence();
			
			o.setOccurrenceDate(2011, 12, 12);
			o.setStartTime(10, 00);
			o.setEndTime(11, 00);
			o.addRegisteredUser("Johnny");
			o.addRegisteredUser("Franky");
			
			e.addOccurrence(o);
			
			o = new Occurrence();
			
			o.setOccurrenceDate(2011,12,30);
			o.setStartTime(13, 00);
			o.setEndTime(14, 00);
			o.addRegisteredUser("Amos");
			o.addRegisteredUser("Hilbert");
			
			e.addOccurrence(o);
			
			dbm.addEvent(e);
			
			// Fifth event.
			
			vw = new HashSet<Event.VolunteeringWith>();
			vw.add(Event.VolunteeringWith.ELDERLY);
			vw.add(Event.VolunteeringWith.MENTALLY_CHALLENGED);
			vw.add(Event.VolunteeringWith.CHILDREN);
			
			sf = new HashSet<Event.SuitableFor>();
			sf.add(Event.SuitableFor.INDIVIDUALS);
			sf.add(Event.SuitableFor.GROUPS);
			sf.add(Event.SuitableFor.CHILDREN);
			
			wt = new HashSet<Event.WorkType>();
			wt.add(Event.WorkType.MENIAL);
	
			a = new Event.Address();
			
			a.setCity("TLV");
			a.setStreet("Hayarkon");
			a.setNumber((short) 20);
			a.setGeoPoint(new GeoPt((float) 32.055555, (float) 34.769572));
			
			e = new Event("Shake what your mamma gave ya", "Get jiggy with it!","Konichiwa bithez !!!!",
						  new Date(), false, a, "Pussycat", vw, sf, wt, true);
			
			o = new Occurrence();
			
			o.setOccurrenceDate(2011, 12, 12);
			o.setStartTime(22, 00);
			o.setEndTime(23, 59);
			o.addRegisteredUser("Maya");
			o.addRegisteredUser("Avner");
			
			e.addOccurrence(o);
			
			o = new Occurrence();
			
			o.setOccurrenceDate(2011,12,30);
			o.setStartTime(22, 00);
			o.setEndTime(23, 59);
			o.addRegisteredUser("Terry");
			o.addRegisteredUser("Giliam");
			
			e.addOccurrence(o);
			
			dbm.addEvent(e);
	
			// Sixth event.
			
			vw = new HashSet<Event.VolunteeringWith>();
			vw.add(Event.VolunteeringWith.OTHER);
			
			sf = new HashSet<Event.SuitableFor>();
			sf.add(Event.SuitableFor.INDIVIDUALS);
			sf.add(Event.SuitableFor.GROUPS);
			sf.add(Event.SuitableFor.CHILDREN);
			
			wt = new HashSet<Event.WorkType>();
			wt.add(Event.WorkType.MENIAL);
	
			a = new Event.Address();
			
			a.setCity("TLV");
			a.setStreet("Jaffa port");
			a.setNumber((short) 1);
			a.setGeoPoint(new GeoPt((float) 32.063374, (float) 34.773080));
			
			e = new Event("Give a hot meal to the needy", "Help pack and distribute hot meals to those in need","They are very hungry. Help them.",
						  new Date(), false, a, "Jaffa for Jaffa", vw, sf, wt, true);
			
			o = new Occurrence();
			
			o.setOccurrenceDate(2011, 12, 12);
			o.setStartTime(8, 0);
			o.setEndTime(14, 0);
			
			e.addOccurrence(o);
			
			o = new Occurrence();
			
			o.setOccurrenceDate(2011,12,30);
			o.setStartTime(8, 0);
			o.setEndTime(15, 0);
			
			e.addOccurrence(o);
			
			dbm.addEvent(e);
				
		}
		
		else{
	
		Calendar c = Calendar.getInstance();
		c.set(2012, 12, 12, 8, 0, 0);
		Date userDate = c.getTime();

		GeoPt gp = new GeoPt((float) 32.074938, (float) 34.775591);
		
		List<Event> events = dbm.getNextEventsByGeoPt(gp, userDate);
		
		String js = new JSONSerializer().include("occurrences", "occurrences.registeredUserNames", "volunteeringWith", "suitableFor", "workType").exclude("occurrenceKeys").serialize(events);
		
		resp.setContentType("text/plain");
		
		pw.println(js);
		
		List<Event> check = new JSONDeserializer<List<Event>>().deserialize(js);
		}
	}
}

