package com.gdma.good2goserver;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class NPO {
	
	@Persistent
	@PrimaryKey
	private String NPOName;
	
	@Persistent
	private Text description;
	
	@Persistent
	String email;
	
	@Persistent
	String phoneNumber;
	
	@Persistent
	String contact;
	
	@Persistent
	String contactNumber;

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public NPO(String nPOName) {
		NPOName = nPOName;
	}
	
	public String getNPOName() {
		return NPOName;
	}

	public Text getDescription() {
		return description;
	}

	public String getemail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setDescription(Text description) {
		this.description = description;
	}

	public void setemail(String email) {
		this.email = email;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
