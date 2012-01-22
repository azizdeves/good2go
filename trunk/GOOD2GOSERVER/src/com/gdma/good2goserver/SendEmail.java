package com.gdma.good2goserver;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {

	public static void ActSend(String toEmail, String eventName, Date eventDate, String fName, String lName, String userName, String age, String sex, String phone, String city) throws UnsupportedEncodingException, MessagingException{
		
		Calendar c = Calendar.getInstance();
		c.setTime(eventDate);
		String year = new Integer(c.get(Calendar.YEAR)).toString();
		String month = new Integer(c.get(Calendar.MONTH) + 1).toString();
		String day = new Integer(c.get(Calendar.DATE)).toString();
		
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		
		String msgBody = "Hello, \n\nA Good2Go user has just registered to the following event:\n" 
						+ eventName + "\nthat takes place on: " + day + "." + month + "." + year + 
						"\n\nThe user's details are:\nName: " + fName + " " + lName + "\nEmail: " 
						+ userName + "\nAge: " + age + "\nGender: " + sex + "\nPhone number: " + phone + "\nCity: " + city;
		
		Message msg = new MimeMessage(session);
		
		msg.setFrom(new InternetAddress("good2gomailbox@gmail.com", "DO-NOT-REPLY: Good2Go User Registration"));
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail, fName + " " + lName));
		msg.setSubject("NEW User Registration To Your Event");
		msg.setText(msgBody);
		Transport.send(msg);
	}
}
