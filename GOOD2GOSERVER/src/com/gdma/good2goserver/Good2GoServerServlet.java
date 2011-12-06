package com.gdma.good2goserver;

import java.io.IOException;
import javax.servlet.http.*;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class Good2GoServerServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(Good2GoServerServlet.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {	
		
		try{
		log.info("I'm here!");
		
		resp.setContentType("text/plain");
		resp.getWriter().println(req.getParameter(new String("a")));
		}
		catch (IOException e){
			log.info(e.getMessage());
			throw e;
		}
	}
}
