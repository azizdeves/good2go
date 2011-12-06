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
	String homepage;
	
	@Persistent
	String phoneNumber;

	public NPO(String nPOName) {
		NPOName = nPOName;
	}
	
	public String getNPOName() {
		return NPOName;
	}

	public Text getDescription() {
		return description;
	}

	public String getHomepage() {
		return homepage;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setDescription(Text description) {
		this.description = description;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
