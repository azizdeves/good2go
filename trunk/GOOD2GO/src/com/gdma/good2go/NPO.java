package com.gdma.good2go;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class NPO {
	
	@Persistent
	@PrimaryKey
	private String NPOName;
	
	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private String description;
	
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

	public String getDescription() {
		return description;
	}

	public String getemail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setemail(String email) {
		this.email = email;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
