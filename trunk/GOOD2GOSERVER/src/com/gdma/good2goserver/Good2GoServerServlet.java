package com.gdma.good2goserver;

import java.io.IOException;
import javax.servlet.http.*;

import com.gdma.good2goserver.Event.Address;
import com.gdma.good2goserver.Event.SuitableFor;
import com.gdma.good2goserver.Event.VolunteeringWith;
import com.gdma.good2goserver.Event.WorkType;
import com.google.appengine.api.datastore.GeoPt;

import java.util.Date;
import java.util.Set;
import java.util.logging.Logger;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import flexjson.JSONSerializer;

@SuppressWarnings("serial")
public class Good2GoServerServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(Good2GoServerServlet.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {	
		
		try{
		log.info("I'm here!");
		
		String action = req.getParameter(new String("action"));
		
		if (action=="getEvents"){
			
		}
		
		Set<Event.VolunteeringWith> vw = new HashSet<Event.VolunteeringWith>();
		vw.add(Event.VolunteeringWith.CHILDREN);
		vw.add(Event.VolunteeringWith.MENTALLY_CHALLENGED);
		
		Set<Event.SuitableFor> sf = new HashSet<Event.SuitableFor>();
		sf.add(Event.SuitableFor.GROUPS);
		
		Set<Event.WorkType> wt = new HashSet<Event.WorkType>();
		wt.add(Event.WorkType.METNAL);
		
		Event e = new Event("newEvent", "Fundraiser", "You have to be filthy rich", new Date(4), false, "FilthyRichForPoorPersons", vw, sf, wt, true);
		e.setEventAddress("TLV", "TAGORE", (short) 100, new GeoPt((float) 1.0,(float) 2.0));
		
		JSONSerializer j = new JSONSerializer().include("occurrences","volunteeringWith","suitableFor","workType");
		String js = j.serialize(e);
		
		resp.setContentType("text/plain");
		resp.getWriter().println(js);
		}
		catch (IOException e){
			log.info(e.getMessage());
			throw e;
		}
	}
}
