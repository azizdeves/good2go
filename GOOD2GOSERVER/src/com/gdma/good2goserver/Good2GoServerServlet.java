package com.gdma.good2goserver;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

import com.gdma.good2goserver.Occurrence;
import com.gdma.good2goserver.SendEmail;

import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

import java.net.*;
import java.io.*;

@SuppressWarnings("serial")
public class Good2GoServerServlet extends HttpServlet {
	
 	private Good2GoPoint getCoordinate(Event.Address address){
 		
 		if (address == null || address.getCity() == null || address.getCity().length() == 0)
 			return null;
 		
 		try {
 			
 			String addressString = new String("http://maps.googleapis.com/maps/api/geocode/json?sensor=false&language=iw&address=" + address.getCity());
 			if (address.getStreet() != null && address.getStreet().length() != 0){
 				addressString += " " + address.getStreet();
 				
 				if (address.getNumber() != 0)
 					addressString += " " + address.getNumber();
 			}
 			
 			addressString = addressString.replace(" ", "%20");
			URL url = new URL(addressString);
			
			HTTPRequest req = new HTTPRequest(url,HTTPMethod.GET);
			HTTPResponse res = URLFetchServiceFactory.getURLFetchService().fetch(req);
			byte[] bytes = res.getContent();
			
			String s = new String(bytes,"UTF-8");
			int begin = 0;
			int end = 0;
			
			begin = s.indexOf("location");
			begin = s.indexOf("lat",begin);
			
			while (begin<s.length() && !Character.isDigit(s.charAt(begin)))
				begin++;
			
			end = begin;
			while (end<s.length() && (Character.isDigit(s.charAt(end)) || s.charAt(end) == '.'))
				end++;
			
			if (end > begin + 9)
				end = begin + 9;
			
			int lat = Integer.parseInt(s.substring(begin,end).replace(".", ""));
			
			begin = s.indexOf("lng",begin);
			
			while (begin<s.length() && !Character.isDigit(s.charAt(begin)))
				begin++;
			
			end = begin;
			while (end<s.length() && (Character.isDigit(s.charAt(end)) || s.charAt(end) == '.'))
				end++;
			
			if (end > begin + 9)
				end = begin + 9;
			
			int lon = Integer.parseInt(s.substring(begin,end).replace(".", ""));
			
			return new Good2GoPoint(lat,lon);
					
		}
 		catch (Exception e) {
		}
 		
 		return null;
 	}
	
	private void htUpdateData(String fromDate, String toDate, PrintWriter pw){
		try{
			Good2GoDatabaseManager dbm = new Good2GoDatabaseManager();
			
			String urlString = new String("http://www.hevratova.org.il/share/");
			
			if (fromDate != null && toDate != null){
				urlString += "?FromDate=" + fromDate + "&ToDate=" + toDate;
			}
			else{
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				
				String fromDay = new Integer(cal.get(Calendar.DATE)).toString();
				String fromMonth = (new Integer((cal.get(Calendar.MONTH))+1)).toString();
				String fromYear = new Integer(cal.get(Calendar.YEAR)).toString();
				
				cal.add(Calendar.DATE, 3);
				
				String toDay = new Integer(cal.get(Calendar.DATE)).toString();
				String toMonth = (new Integer((cal.get(Calendar.MONTH))+1)).toString();
				String toYear = new Integer(cal.get(Calendar.YEAR)).toString();
				
				urlString += "?FromDate=" + fromDay + "/" + fromMonth + "/" + fromYear;
				urlString += "&ToDate=" + toDay + "/" + toMonth + "/" + toYear;
				
				pw.println("Updating from: " + fromDay + "/" + fromMonth + "/" + fromYear + " to: "  + toDay + "/" + toMonth + "/" + toYear);
			}
			
	        URL url = new URL(urlString);

			HTTPRequest req = new HTTPRequest(url,HTTPMethod.GET,com.google.appengine.api.urlfetch.FetchOptions.Builder.withDeadline(60));
			HTTPResponse res = URLFetchServiceFactory.getURLFetchService().fetch(req);
			byte[] bytes = res.getContent();
			
			String s = new String(bytes,"UTF-8");
			BufferedReader in = new BufferedReader(new StringReader(s));
			
	        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
	        NPOTranslate translate = new HTTranslate();
	        
	        List<Event> newEvents = new LinkedList<Event>();
	        List<Occurrence> newOccurrences = new LinkedList<Occurrence>();
	        boolean isNewOccurrence = false;
	        boolean isNewEvent = false;
	        
	        String eventKey = "";
	        Event e = null;
	        Occurrence o = null;
	        String occurrenceKey = null;
	        Event.Address address = null;
	        
	        boolean isSameEvent = false;
	        String lastEventKey = "";

	        String line;
	        line = in.readLine();
	        if (line != null) while ((line = in.readLine()) != null){
	        	lastEventKey = eventKey;
	        	int len = line.length();
	        	int start = 0;
	        	int end = -1;
	        	
	        	for (int i=0;i<=40;i++){
	        		end++;
	        		start = end;
	        		while (end<len && line.charAt(end)!='\t')
	        			end++;
	        		
	        		String part = line.substring(start, end);
	        		
	        		if (part.length()>500)
	        			part = part.substring(0, 497) + "...";
	        		
	        		switch(i){
	        			case 0:
	        				occurrenceKey = "HT" + part;
	        				o = dbm.getOccurrence(occurrenceKey);
	        				
	        				if (o==null){
	        					isNewOccurrence = true;
	        					o = new Occurrence();
	        					o.setOccurrenceKey(occurrenceKey);
	        				}
	        				else
	        					isNewOccurrence = false;
	        				
	        				break;
	        				
	        			case 1:
	        				eventKey = "HT" + part.substring(1, part.length()-1);
	        				
	        				if (lastEventKey.equals(eventKey)){
	        					isSameEvent = true;
	        				}
	        				
	        				else{
		        				isSameEvent = false;
		        				        					
	        					e = dbm.getEvent(eventKey);
	        					
		        				if (e==null){
		        					isNewEvent = true;
		        					e = new Event();
		        					e.setEventKey(eventKey);
		        				}
		        				else{
		        					isNewEvent = false;
		        				}
		        				
		        				if (e != null){
		        					if (isNewEvent)
		        						newEvents.add(e);
		        					else{
		        						try{
		        							dbm.editEvent(e);
		        						}
		        		        		catch(Exception ex){
		        		        			pw.print(ex.getMessage());
		        		        		}
		        					}
		        				}
		        				
	        					address = e.getEventAddress();
	        					if (address == null)
	        						address = new Event.Address();
	        				}
	        				
	        				if (!e.getOccurrenceKeys().contains(occurrenceKey)){
	        					e.addOccurrenceKey(occurrenceKey);
	        				}
	        				o.setContainingEventKey(eventKey);
	        				
	        				break;
	        				
	        			case 3:
	        				if (!isSameEvent)
	        					e.setNPOName(part);
	        				break;
	        				
	        			case 5:
	        				if (!isSameEvent)
	        					e.setEventName(part);
	        				break;
	        				
	        			case 6:
	        				if (!isSameEvent)
	        					e.setInfo(part);
	        				break;
	        				
	        			case 7:
	        				if (!isSameEvent)
	        					e.setDescription(part);
	        				break;
	        			
	        			case 9:
	        				if (!isSameEvent)
	        					e.setContent(part);
	        				break;
	        			
	        			case 10:
	        				
	        				try{
	        					Calendar c = Calendar.getInstance();
	        					Date occurrenceDate = df.parse(part);
	        					Date startTime = occurrenceDate;
	        					
	        					c.setTime(occurrenceDate);
	        					c.set(Calendar.HOUR_OF_DAY, 0);
	        					c.set(Calendar.MINUTE, 0);
	        					c.set(Calendar.SECOND, 0);
	        					c.set(Calendar.MILLISECOND, 0);
	        					occurrenceDate = c.getTime();
	        					
	        					c.setTime(startTime);
	        					c.set(Calendar.YEAR, 1970);
	        					c.set(Calendar.MONTH, Calendar.JANUARY);
	        					c.set(Calendar.DATE, 1);
	        					startTime = c.getTime();
	        					
	        					o.setOccurrenceDate(occurrenceDate);
	        					o.setStartTime(startTime);
	        				}
	        				catch(Exception ex){
	        					pw.print(ex.getMessage());
	        				}
	        				break;
	        			
	        			case 11:
	        				if (part.equals(new String("לא")))
	        					e.setArriveAnyTime(false);
	        				else
	        					e.setArriveAnyTime(true);
	        				break;
	        				
	        			case 12:
	        				try{
	        					Calendar c = Calendar.getInstance();
	        					Date endTime = df.parse(part);

	        					c.setTime(endTime);
	        					c.set(Calendar.YEAR, 1970);
	        					c.set(Calendar.MONTH, Calendar.JANUARY);
	        					c.set(Calendar.DATE, 1);
	        					endTime = c.getTime();
	        					
	        					o.setEndTime(endTime);
	        				}
	        				catch(Exception ex){
	        					pw.print(ex.getMessage());
	        				}
	        				break;
	        				
	        			case 13:
	        				if (part.equals(new String("לא")) && e.isArriveAnyTime() == false){
	        					Calendar c = Calendar.getInstance();
	        					c.setTime(o.getEndTime());
	        					
	        					Calendar startCal = Calendar.getInstance();
	        					startCal.setTime(o.getStartTime());
	        					
	        					c.add(Calendar.HOUR_OF_DAY, -startCal.get(Calendar.HOUR_OF_DAY));
	        					c.add(Calendar.MINUTE, -startCal.get(Calendar.MINUTE));
	        					
	        					e.setMinDuration(c.get(Calendar.HOUR_OF_DAY)*60 + c.get(Calendar.MINUTE));
	        				}
	        				else{
	        					e.setMinDuration(120);
	        				}
	        				break;
	        				
	        			case 19:
	        				if (!isSameEvent)
	        					address.setCity(part);
	        				break;
	        			
	        			case 20:
	        				if (!isSameEvent)
	        					address.setStreet(part);
	        				break;
	        				
	        			case 21:
	        				if (!isSameEvent){
	        					try{
	        						address.setNumber(Short.parseShort(part));
	        					}
	        					catch (Exception ex){
	        						pw.print(ex.getMessage());
	        					}
	        					
	        					if (address.getGood2GoPoint() == null || address.getGood2GoPoint().getLat() == 0 || address.getGood2GoPoint().getLon() == 0){
	        						Good2GoPoint g2gPoint = getCoordinate(address);
	        						if (g2gPoint!=null)
	        							address.setGood2GoPoint(g2gPoint);
	        					}
	        					
	        					e.setEventAddress(address);
	        				}
	        				break;
	        			
	        			case 24:
	        				if (!isSameEvent){
	        					e.setVolunteeringWith(translate.getVolunteeringWith(part));
	        					e.setWorkType(translate.getWorkType(part));
	        				}
	        				break;
	        				
	        			case 25:
	        				if (!isSameEvent)
	        					e.setPrerequisites(part);
	        				break;
	        			
	        			case 26:
	        				if (!isSameEvent && !part.equals(new String(""))){
	        					String pre = e.getPrerequisites();
	        					if (!pre.equals(""))
	        						e.setPrerequisites(pre + ", " + part);
	        					else
	        						e.setPrerequisites(part);
	        				}
	        				break;
	        				
	        			case 28:
	        				if (!isSameEvent){
	        					try{
	        						if (Integer.parseInt(part)<16)
	        							e.addSuitableFor(Event.SuitableFor.KIDS);
	        					}
	        					catch (Exception ex){
	        						pw.print(ex.getMessage());
	        					}
	        				}
	        				break;
	        			
	        			case 32:
	        				if (!isSameEvent){
	        					if (part.equals(new String("1")))
	        						e.addSuitableFor(Event.SuitableFor.GROUPS);
	        					e.addSuitableFor(Event.SuitableFor.INDIVIDUALS);
	        				}
	        				break;
	        			
	        			case 33:
	        				try{
	        					e.setHowMany(Short.parseShort(part));
	        				}
	        				catch (Exception ex){
	        					pw.print(ex.getMessage());
	        				}
	        				break;
	        			
	        			case 40:
	        				if (!part.equals(new String("")))
	        						o.setEmail(part);
	        				break;
	        		}
	        		
	        	}
	        	
	        	if (isNewOccurrence)
	        		newOccurrences.add(o);
	        	else{
	        		try{
	        			dbm.editOccurrence(o);
	        		}
	        		catch(Exception ex){
	        			pw.print(ex.getMessage());
	        		}
	        	}
	        }
	        in.close();
	        
	        try{
	        	dbm.addOccurrences(newOccurrences);
	        	dbm.addEvents(newEvents);
	        }
    		catch(Exception ex){
    			pw.print(ex.getMessage());
    		}
		}
		catch(IOException e){
			pw.print(e.getMessage());
		}
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {	
		
		Good2GoDatabaseManager dbm = new Good2GoDatabaseManager();
		
		resp.setCharacterEncoding("UTF-8");
		PrintWriter pw = resp.getWriter();
		resp.setContentType("text/html; charset=UTF-8");
		
		String action = req.getParameter(new String("action"));
		
		if (action.compareToIgnoreCase("getUserHistory")==0){
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
				
				String js = new String("[]");
				
				if (events != null){
					for (Event e : events){
						e.setOccurrenceKeys(null);
						for (Occurrence o : e.getOccurrences())
							o.setRegisteredUserNames(null);
						
					}

					js = new JSONSerializer().transform(new DateTransformer("yyyy.MM.dd.HH.aa.mm.ss.SSS"), Date.class).include("address", "occurrences", "volunteeringWith", "suitableFor", "workType", "occurrences.endTime").exclude("occurrences.registeredUserNames", "occurrenceKeys").serialize(events);
					
				}

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
				
				pw.print(js);
			}
			
			else
				pw.print("Missing Parameters");
		
		}
		
		else if (action.compareToIgnoreCase("editUser")==0 || action.compareToIgnoreCase("addUser")==0){
			String userName = req.getParameter(new String("userName"));
			String firstName = req.getParameter(new String("firstName"));
			String lastName = req.getParameter(new String("lastName"));
			String email = req.getParameter(new String("email"));
			String phone = req.getParameter(new String("phone"));
			String city = req.getParameter(new String("city"));
			String sexString = req.getParameter(new String("sex"));
			String birthString = req.getParameter(new String("birthYear"));
			
			if (userName!=null){
				User user = new User();
				
				user.setUserName(userName);
				user.setBirthYear(0);
				
				if (firstName!=null)
					user.setFirstName(firstName);
				if (lastName!=null)
					user.setLastName(lastName);
				if (email!=null)
					user.setEmail(email);
				if (phone!=null)
					user.setPhone(phone);
				if (city!=null)
					user.setCity(city);
				if (sexString!=null)
					user.setSex(User.Sex.getSex(sexString));
				if (birthString!=null)
					user.setBirthYear(Integer.parseInt(birthString));
				
				if (action.compareToIgnoreCase("editUser")==0){
					dbm.editUser(user);
				}
				else{
					Date now = new Date();
					user.setRegistrationDate(now);
					
					dbm.addUser(user);
				}
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
				
				pw.print(js);
			}
			else{
				pw.print("Unspecified username");
			}
		}
		else if (action.compareToIgnoreCase("registerToOccurrence")==0){
			String userName = req.getParameter(new String("userName"));
			String occurrenceKey = req.getParameter(new String("occurrenceKey"));
			
			if (userName!=null && occurrenceKey!=null){
				if (dbm.registerToOccurrence(userName, occurrenceKey)){
					Occurrence occurrence = dbm.getOccurrence(occurrenceKey);
					User user = dbm.getUserDetails(userName);
					Event event = dbm.getEvent(occurrence.getContainingEventKey());
					
					Calendar c = Calendar.getInstance();
					Date now = new Date();
					c.setTime(now);
					String age = new Integer(c.get(Calendar.YEAR) - user.getBirthYear()).toString();
					
					try{
						SendEmail.ActSendToNPO(/*occurrence.getEmail()*/"GOOD2GO.israel@gmail.com", event.getEventName(), occurrence.getOccurrenceDate(), user.getFirstName(), user.getLastName(), user.getUserName(), age.toString(), user.getSex().toString(), user.getPhone(), user.getCity());
						SendEmail.ActSendToUser(user.getUserName(), event.getEventName(), occurrence.getOccurrenceDate(), user.getFirstName(), user.getLastName());
					}
					catch(Exception e){
						pw.print(e.getMessage());
						pw.print("Error sending Email");
					}
				}
			}
			else{
				pw.print("Missing Parameters");
			}
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
		
		else if (action.compareToIgnoreCase("updateData")==0){
			String fromDate = req.getParameter(new String("fromDate"));
			String toDate = req.getParameter(new String("toDate"));
			
			htUpdateData(fromDate,toDate,pw);
		}
	}
}

