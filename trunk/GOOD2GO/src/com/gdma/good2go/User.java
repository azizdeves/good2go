package com.gdma.good2go;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import android.inputmethodservice.Keyboard.Key;

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
	private List<Key> registeredOccurrenceKeys;

	public User(String userName, String firstName, String lastName, String email, int birthYear, Date registrationDate) {
		this.userName = userName;
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setEmail(email);
		this.setregistrationDate(registrationDate);
		this.registeredOccurrenceKeys = new LinkedList<Key>();
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

	public List<Key> getRegisteredOccurrenceKeys() {
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

	public void addRegisteredOccurrenceKey(Key newKey) {
		this.registeredOccurrenceKeys.add(newKey);
	}
	
	public void removeRegisteredOccurrenceKey(Key registeredKey) {
		this.registeredOccurrenceKeys.remove(registeredKey);
	}
}
