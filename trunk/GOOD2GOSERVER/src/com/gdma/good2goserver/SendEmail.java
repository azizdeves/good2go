package com.gdma.good2goserver;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {

	public static void ActSend(String toEmail, String fName, String lName, String userEmail, String age, String sex) throws UnsupportedEncodingException, MessagingException{
		
	
		/*
		String host = "appspot.gserviceaccount.com";
		String to = toEmail;
		String from = "good-2-go@appspot.gserviceaccount.com";
		String subject = "NEW User Registration To Your Event";
		String messageText = "Hello, <br>This person has registered to you event:<br><b>Name:</b> " + fName + " " + lName + "<br><b>Email: <b>" + userEmail + "<br>Age: " + age + "<br>Gender: " + sex;

		Properties props = System.getProperties();
		props.put("mail.host", host);
		props.put("mail.transport.protocol", "smtp");
		Session session = Session.getDefaultInstance(props, null);

		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(from, "DO-NOT-REPLY: Good2Go User Registration"));
			
		InternetAddress[] address = {new InternetAddress(to)};
		msg.setRecipients(Message.RecipientType.TO, address);
		
		msg.setSubject(subject);
		msg.setSentDate(new Date());
		msg.setText(messageText);

		Transport.send(msg);*/
		
		
		
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		
		String msgBody = "Hello, \nThis person has registered to you event:\nName: " + fName + " " + lName + "\nEmail: " + userEmail + "\nAge: " + age + "\nGender: " + sex;
		
		Message msg = new MimeMessage(session);
		
		msg.setFrom(new InternetAddress("good2gomailbox@gmail.com", "DO-NOT-REPLY: Good2Go User Registration"));
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail, fName + " " + lName));
		msg.setSubject("NEW User Registration To Your Event");
		msg.setText(msgBody);
		Transport.send(msg);
	}
}
