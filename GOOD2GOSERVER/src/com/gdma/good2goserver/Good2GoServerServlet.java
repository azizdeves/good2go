package com.gdma.good2goserver;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.*;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.HashSet;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

import com.gdma.good2goserver.Occurrence;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SuppressWarnings("serial")
public class Good2GoServerServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(Good2GoServerServlet.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {	
		
		Good2GoDatabaseManager dbm = new Good2GoDatabaseManager();

		/* Gson debugging		
		Gson gson = new Gson();
		JsonArray jsonArray = new JsonArray();
		JsonObject jsonKarma = null;
		DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);
		
		String dateString = dateFormatter.format(new Date());
		
		jsonKarma = new JsonObject();
		jsonKarma.addProperty("Date", dateString);
		jsonKarma.addProperty("Event", "testEvent");
		jsonKarma.addProperty("Points", (long) 200010102);
		
		jsonArray.add(jsonKarma);
		
		String json = gson.toJson(jsonArray);
		
		JsonParser parser = new JsonParser();
		
		jsonArray = parser.parse(json).getAsJsonArray();
		for (int i=0;i<jsonArray.size();i++){
			jsonKarma = (JsonObject) jsonArray.get(i);
			String eventName = jsonKarma.getAsJsonPrimitive("Event").getAsString();
			String date = jsonKarma.getAsJsonPrimitive("Date").getAsString();
			long points = jsonKarma.getAsJsonPrimitive("Points").getAsLong();
		}
		*/
		
		PrintWriter pw = resp.getWriter();
		
		String action = req.getParameter(new String("action"));
			
		if (action.compareToIgnoreCase("addEvents")==0){
			
			Set<Event.VolunteeringWith> vw = new HashSet<Event.VolunteeringWith>();
			vw.add(Event.VolunteeringWith.CHILDREN);
			vw.add(Event.VolunteeringWith.DISABLED);
			
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
			a.setGood2GoPoint(new Good2GoPoint(32069156, 34774003));
			
			e = new Event("Fun Horseback Riding", "Help handicapped teenagers and enjoy a horseack ride",
						  "Assist handicapped teenagers in therapeutic horse-back riding, lead their horse and help them follow instructors commands.",
						  600, false, a, "FilthyRichForPoorPersons", vw, sf, wt, true);
			
			Occurrence o = new Occurrence();
			
			o.setOccurrenceDate(2011, Calendar.DECEMBER, 31);
			o.setStartTime(10, 0);
			o.setEndTime(20, 20);
			//o.addRegisteredUser("Dana");
			//o.addRegisteredUser("Gil");
			
			e.addOccurrence(o);
			
			o = new Occurrence();
			
			o.setOccurrenceDate(2012,Calendar.JANUARY,1);
			o.setStartTime(12, 30);
			o.setEndTime(15, 40);
			//o.addRegisteredUser("Gil");
			//o.addRegisteredUser("Mor");
			
			e.addOccurrence(o);
			
			dbm.addEvent(e);

			// Second event.
			
			vw = new HashSet<Event.VolunteeringWith>();
			vw.add(Event.VolunteeringWith.ANIMALS);
			
			sf = new HashSet<Event.SuitableFor>();
			sf.add(Event.SuitableFor.GROUPS);
			sf.add(Event.SuitableFor.INDIVIDUALS);
			sf.add(Event.SuitableFor.KIDS);
			
			wt = new HashSet<Event.WorkType>();
			wt.add(Event.WorkType.MENIAL);

			a = new Event.Address();
			
			a.setCity("TLV");
			a.setStreet("somewhere");
			a.setNumber((short) 1);
			a.setGood2GoPoint(new Good2GoPoint(32069211, 34763403));
			
			e = new Event("Dogs are our best friends", "Have a walk with a city chelter dog","Make a furry cute friend for life!",
						  130, false, a, "CityShelter", vw, sf, wt, true);
			
			o = new Occurrence();
			
			o.setOccurrenceDate(2011, Calendar.DECEMBER, 31);
			o.setStartTime(11, 30);
			o.setEndTime(13, 40);
			//o.addRegisteredUser("Mor");
			//o.addRegisteredUser("Adi");
			
			e.addOccurrence(o);
			
			o = new Occurrence();
			
			o.setOccurrenceDate(2012,Calendar.JANUARY,1);
			o.setStartTime(9, 20);
			o.setEndTime(20, 15);
			//o.addRegisteredUser("Dana");
			//o.addRegisteredUser("Shimon Peres");
			
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
			a.setGood2GoPoint(new Good2GoPoint(32086865,34789581));
			
			e = new Event("Surf the internet", "Show the wonders of Google and Wikipedia to children","Share what you know by teaching internet to kids!",
						  205, false, a, "Google Inc.", vw, sf, wt, true);
			
			o = new Occurrence();
			
			o.setOccurrenceDate(2011, Calendar.DECEMBER, 31);
			o.setStartTime(10, 00);
			o.setEndTime(13, 25);
			//o.addRegisteredUser("Avi");
			//o.addRegisteredUser("Hezi");
			
			e.addOccurrence(o);
			
			o = new Occurrence();
			
			o.setOccurrenceDate(2012,Calendar.JANUARY,1);
			o.setStartTime(14, 55);
			o.setEndTime(23, 00);
			//o.addRegisteredUser("Shlomo");
			
			e.addOccurrence(o);
			
			dbm.addEvent(e);
	
			// Fourth event.
			
			vw = new HashSet<Event.VolunteeringWith>();
			vw.add(Event.VolunteeringWith.ELDERLY);
			
			sf = new HashSet<Event.SuitableFor>();
			sf.add(Event.SuitableFor.INDIVIDUALS);
			sf.add(Event.SuitableFor.KIDS);
			
			wt = new HashSet<Event.WorkType>();
			wt.add(Event.WorkType.MENTAL);
	
			a = new Event.Address();
			
			a.setCity("TLV");
			a.setStreet("Einstein");
			a.setNumber((short) 30);
			a.setGood2GoPoint(new Good2GoPoint(32074938,34775591));
			
			e = new Event("Read your favourite book", "Make someone happy and provide company to the elderly","Read anything you like to the elderly",
						  60, false, a, "Mishan", vw, sf, wt, true);
			
			o = new Occurrence();
			
			o.setOccurrenceDate(2011, Calendar.DECEMBER, 31);
			o.setStartTime(10, 00);
			o.setEndTime(11, 00);
			//o.addRegisteredUser("Johnny");
			//o.addRegisteredUser("Franky");
			
			e.addOccurrence(o);
			
			o = new Occurrence();
			
			o.setOccurrenceDate(2012,Calendar.JANUARY,1);
			o.setStartTime(13, 00);
			o.setEndTime(14, 00);
			//o.addRegisteredUser("Amos");
			//o.addRegisteredUser("Hilbert");
			
			e.addOccurrence(o);
			
			dbm.addEvent(e);
			
			// Fifth event.
			
			vw = new HashSet<Event.VolunteeringWith>();
			vw.add(Event.VolunteeringWith.ELDERLY);
			vw.add(Event.VolunteeringWith.DISABLED);
			vw.add(Event.VolunteeringWith.CHILDREN);
			
			sf = new HashSet<Event.SuitableFor>();
			sf.add(Event.SuitableFor.INDIVIDUALS);
			sf.add(Event.SuitableFor.GROUPS);
			sf.add(Event.SuitableFor.KIDS);
			
			wt = new HashSet<Event.WorkType>();
			wt.add(Event.WorkType.MENIAL);
	
			a = new Event.Address();
			
			a.setCity("TLV");
			a.setStreet("Hayarkon");
			a.setNumber((short) 20);
			a.setGood2GoPoint(new Good2GoPoint(32055555,34769572));
			
			e = new Event("Shake what your mamma gave ya", "Get jiggy with it!","Konichiwa bithez !!!!",
						  119, false, a, "Pussycat", vw, sf, wt, true);
			
			o = new Occurrence();
			
			o.setOccurrenceDate(2011, Calendar.DECEMBER, 31);
			o.setStartTime(22, 00);
			o.setEndTime(23, 59);
			//o.addRegisteredUser("Maya");
			//o.addRegisteredUser("Avner");
			
			e.addOccurrence(o);
			
			o = new Occurrence();
			
			o.setOccurrenceDate(2012,Calendar.JANUARY,1);
			o.setStartTime(22, 00);
			o.setEndTime(23, 59);
			//o.addRegisteredUser("Terry");
			//o.addRegisteredUser("Giliam");
			
			e.addOccurrence(o);
			
			dbm.addEvent(e);
	
			// Sixth event.
			
			vw = new HashSet<Event.VolunteeringWith>();
			vw.add(Event.VolunteeringWith.SPECIAL);
			
			sf = new HashSet<Event.SuitableFor>();
			sf.add(Event.SuitableFor.INDIVIDUALS);
			sf.add(Event.SuitableFor.GROUPS);
			sf.add(Event.SuitableFor.KIDS);
			
			wt = new HashSet<Event.WorkType>();
			wt.add(Event.WorkType.MENIAL);
	
			a = new Event.Address();
			
			a.setCity("TLV");
			a.setStreet("Jaffa port");
			a.setNumber((short) 1);
			a.setGood2GoPoint(new Good2GoPoint(32063374,34773080));
			
			e = new Event("Give a hot meal to the needy", "Help pack and distribute hot meals to those in need","They are very hungry. Help them.",
						  360, false, a, "Jaffa for Jaffa", vw, sf, wt, true);
			
			o = new Occurrence();
			
			o.setOccurrenceDate(2011, Calendar.DECEMBER, 31);
			o.setStartTime(8, 0);
			o.setEndTime(14, 0);
			
			e.addOccurrence(o);
			
			o = new Occurrence();
			
			o.setOccurrenceDate(2012,Calendar.JANUARY,1);
			o.setStartTime(8, 0);
			o.setEndTime(15, 0);
			
			e.addOccurrence(o);
			
			dbm.addEvent(e);
				
		}
		
		
		
		/******************************
		********End of addEvents*******
		******************************/
		
		
		
		else if (action.compareToIgnoreCase("getUserHistory")==0){
			String userName = req.getParameter(new String("userName"));
			Date userDate = null;
			String userDateString = req.getParameter(new String("userDate"));
			
			if (userDateString!=null){
				userDate = new Date();
				userDate.setTime(Long.parseLong(userDateString));
			}
			
			if (userName!=null && userDate!=null){
			String js = dbm.getUserHistory(userName, userDate);
			pw.write(js);
			}
			
			else
				pw.print("Missing Parameters");
		}
		
		else if (action.compareToIgnoreCase("getRegisteredFutureEvents")==0){
			String userName = req.getParameter(new String("userName"));
			Date userDate = null;
			String userDateString = req.getParameter(new String("userDate"));
			
			if (userDateString!=null){
				userDate = new Date();
				userDate.setTime(Long.parseLong(userDateString));
			}

			if (userName!=null && userDate!=null){
				List<Event> events = dbm.getRegisteredFutureEvents(userName, userDate);
				for (Event e : events){
					e.setOccurrenceKeys(null);
					for (Occurrence o : e.getOccurrences())
						o.setRegisteredUserNames(null);
				}
				
				String js = new JSONSerializer().include("address", "occurrences", "volunteeringWith", "suitableFor", "workType").exclude("occurrences.registeredUserNames", "occurrenceKeys").serialize(events);
				
				resp.setContentType("text/plain");
				
				pw.print(js);
			}
			
			else
				pw.print("Missing Parameters");
		}
		
		else if (action.compareToIgnoreCase("getEvents")==0){
			
			Date userDate = null;
			int duration = -1;
			double distance = -1;
			String userDateString = req.getParameter(new String("userDate"));
			String durationString = req.getParameter(new String("duration"));
			String distanceString = req.getParameter(new String("distance"));
			
			/*debugging
			Calendar c = Calendar.getInstance();
			c.set(2011, Calendar.DECEMBER,31,8,0,0);
			c.set(Calendar.MILLISECOND,0);
			Date myDate = c.getTime();
			
			int d = myDate.getDay();
			int m = myDate.getMonth();
			int y = myDate.getYear();
			int h = myDate.getHours();
			int min = myDate.getMinutes();
			int s = myDate.getSeconds();

			String dateToSend = Long.toString(myDate.getTime());
			
			debugging*/
			
			if (userDateString!=null){
				userDate = new Date();
				userDate.setTime(Long.parseLong(userDateString));
			}
			
			if (durationString!=null)
				duration = Integer.parseInt(durationString);
			
			if (distanceString!=null)
				distance = Double.parseDouble(distanceString);
			
			String allTypes = req.getParameter(new String("type"));
			
			int lon = -1;
			String lonString = req.getParameter(new String("lon"));
			int lat = -1;
			String latString = req.getParameter(new String("lat"));
			
			if (lonString!=null && latString!=null){
				lon = Integer.parseInt(lonString);
				lat = Integer.parseInt(latString);
			}
			
			List<Event.VolunteeringWith> vw = new LinkedList<Event.VolunteeringWith>();
			List<Event.SuitableFor> sf = new LinkedList<Event.SuitableFor>();
			List<Event.WorkType> wt = new LinkedList<Event.WorkType>();
			
			if (allTypes!=null){
				String[] types = allTypes.split(",");
				
				for (String type : types){
					if (Event.VolunteeringWith.isMember(type))
						vw.add(Event.VolunteeringWith.valueOf(type));
					else if (Event.SuitableFor.isMember(type))
						sf.add(Event.SuitableFor.valueOf(type));
					else if (Event.WorkType.isMember(type))
						wt.add(Event.WorkType.valueOf(type));
				}
			}
			
			Good2GoPoint gp = null;
			if (lon!=-1 && lat!=-1)
				gp = new Good2GoPoint(lon,lat);
			
			if (userDate!=null && gp!=null){
				List<Event> events = dbm.getNextEventsByGood2GoPoint(gp, userDate, duration, distance, vw, sf, wt);
				
				String js = new String("No results");
				
				if (events != null){
					for (Event e : events){
						e.setOccurrenceKeys(null);
						for (Occurrence o : e.getOccurrences())
							o.setRegisteredUserNames(null);
					}
				
					js = new JSONSerializer().transform(new DateTransformer("yyyy.MM.dd.HH.aa.mm.ss.SSS"), Date.class).include("address", "occurrences", "volunteeringWith", "suitableFor", "workType", "occurrences.endTime").exclude("occurrences.registeredUserNames", "occurrenceKeys").serialize(events);
				
				}
				
				resp.setContentType("text/plain");
				
				pw.print(js);
			}
			
			else
				pw.print("Missing Parameters");
		}
		
		else if (action.compareToIgnoreCase("getEventsToFeedback")==0){
			
			String userName = req.getParameter(new String("userName"));
			Date userDate = null;
			String userDateString = req.getParameter(new String("userDate"));
			
			if (userDateString!=null){
				userDate = new Date();
				userDate.setTime(Long.parseLong(userDateString));
			}
			
			if (userName!=null && userDate!=null){
				List<Event> results = dbm.getEventsToFeedback(userName,userDate);
				
				for (Event e : results){
					e.setOccurrenceKeys(null);
					for (Occurrence o : e.getOccurrences())
						o.setRegisteredUserNames(null);
				}
				
				String js = new JSONSerializer().transform(new DateTransformer("yyyy.MM.dd.HH.aa.mm.ss.SSS"), Date.class).include("address", "occurrences", "volunteeringWith", "suitableFor", "workType").exclude("occurrences.registeredUserNames", "occurrenceKeys").serialize(results);
				
				resp.setContentType("text/plain");
				
				pw.print(js);
			}
			
			else
				pw.print("Missing Parameters");
		
		}
		
		else if (action.compareToIgnoreCase("addUser")==0){
			String userName = req.getParameter(new String("userName"));
			String firstName = req.getParameter(new String("firstName"));
			String lastName = req.getParameter(new String("lastName"));
			String email = req.getParameter(new String("email"));
			String birthString = req.getParameter(new String("birthYear")); 
			int birthYear=0;
			
			if (userName!=null){
				if (birthString!=null)
					birthYear = Integer.parseInt(birthString);
				User user = new User(userName, firstName, lastName, email, birthYear, new Date());
				dbm.addUser(user);
			}
			else{
				pw.print("Unspecified username");
			}
			
		}
		
		else if (action.compareToIgnoreCase("addKarma")==0){
			String userName = req.getParameter(new String("userName"));
			String type = req.getParameter(new String("type"));
			
			if (userName!=null && type!=null && Karma.ActionType.isMember(type)){
				
				Karma karma = new Karma(userName,Karma.ActionType.valueOf(type),new Date());
				dbm.addKarma(karma);
			}
			else{
				pw.print("illegal/missing parameters");
			}
			

		}
		
		else if (action.compareToIgnoreCase("getUserDetails")==0){
			String userName = req.getParameter(new String("userName"));
			
			if (userName!=null){
				
				User user = dbm.getUserDetails(userName);
				
				String js = new JSONSerializer().transform(new DateTransformer("yyyy.MM.dd.HH.aa.mm.ss.SSS"), Date.class).exclude("registeredOccurrenceKeys").serialize(user);
				
				/*for debuging*/
				User u= new JSONDeserializer<User>().use(Date.class, new DateTransformer("yyyy.MM.dd.HH.aa.mm.ss.SSS")).deserialize(js); 
				
				resp.setContentType("text/plain");
				
				pw.print(js);
				
			}
			else{
				pw.print("Unspecified username");
			}
		}
		else if (action.compareToIgnoreCase("registerToOccurrence")==0){
			String userName = req.getParameter(new String("userName"));
			String occurrenceKey = req.getParameter(new String("occurrenceKey"));
			
			if (userName!=null && occurrenceKey!=null)
				dbm.registerToOccurrence(userName, occurrenceKey);
			else
				pw.print("Missing Parameters");
		
		}
		
		else if (action.compareToIgnoreCase("cancelRegistration")==0){
			String userName = req.getParameter(new String("userName"));
			String occurrenceKey = req.getParameter(new String("occurrenceKey"));
			
			if (userName!=null && occurrenceKey!=null)
				dbm.CancelRegistration(userName, occurrenceKey);
			else
				pw.print("Missing Parameters");
		
		}
		
		else if (action.compareToIgnoreCase("addFeedback")==0){
			String userName = req.getParameter(new String("userName"));
			String occurrenceKey = req.getParameter(new String("occurrenceKey"));
			int rating=-1;
			if (req.getParameter(new String("rating"))!=null)
				rating = Integer.parseInt(req.getParameter(new String("rating")));
			
			if ((rating>=0 && rating<=5) || rating==-1){
				if (userName!=null && occurrenceKey!=null)
					dbm.addFeedback(userName, occurrenceKey,rating);
				else
					pw.print("Missing Parameters");
			}
			else
				pw.print("the range of parameter 'rating' is not correct");
		
		}
		
		else if (action.compareToIgnoreCase("getKarma")==0) {
			String userName = req.getParameter(new String("userName"));
			if (userName!=null){
				long points=dbm.getKarma(userName);
				pw.print(Long.toString(points));
			}
			else
				pw.print("Missing Parameters");
		}
		
		
	}
}

