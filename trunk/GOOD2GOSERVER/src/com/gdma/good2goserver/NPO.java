package com.gdma.good2goserver;
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
	String site;
	
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

	public NPO(String NPOName) {
		NPOName = NPOName;
	}
	
	public String getNPOName() {
		return NPOName;
	}

	public String getDescription() {
		return description;
	}

	public String getemail() {
		return site;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEmail(String site) {
		this.site = site;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
