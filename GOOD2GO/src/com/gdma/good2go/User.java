package com.gdma.good2go;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Extension;
import java.util.Date;
import java.util.List;
import java.util.LinkedList;

@PersistenceCapable
public class User {
	@Persistent
	@PrimaryKey
	private String userName;
	
	@Persistent
	@Extension(vendorName="datanucleus", key="gae.unindexed", value="true")
	private String firstName;
	
	@Persistent
	@Extension(vendorName="datanucleus", key="gae.unindexed", value="true")
	private String lastName;
	
	@Persistent
	@Extension(vendorName="datanucleus", key="gae.unindexed", value="true")
	private String email;
	
	@Persistent
	private int birthYear;
	
	@Persistent
	private Date registrationDate;
	
	@Persistent
	private List<String> registeredOccurrenceKeys;

	public User(){
		this.registeredOccurrenceKeys = new LinkedList<String>();
	}
	
	public User(String userName, String firstName, String lastName, String email, int birthYear, Date registrationDate) {
		this.userName = userName;
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setEmail(email);
		this.setBirthYear(birthYear);
		this.setregistrationDate(registrationDate);
		this.registeredOccurrenceKeys = new LinkedList<String>();
	}
	
	public String getUserName() {
		return userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public int getBirthYear() {
		return birthYear;
	}

	public Date getregistrationDate() {
		return registrationDate;
	}

	public List<String> getRegisteredOccurrenceKeys() {
		return registeredOccurrenceKeys;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}

	public void setregistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public void addRegisteredOccurrenceKey(String newKey) {
		this.registeredOccurrenceKeys.add(newKey);
	}
	
	public void removeRegisteredOccurrenceKey(String registeredKey) {
		this.registeredOccurrenceKeys.remove(registeredKey);
	}
}
