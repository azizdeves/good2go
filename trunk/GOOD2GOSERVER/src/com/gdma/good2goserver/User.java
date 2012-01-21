package com.gdma.good2goserver;
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
	@Extension(vendorName="datanucleus", key="gae.unindexed", value="true")
	private String phone;

	@Persistent
	@Extension(vendorName="datanucleus", key="gae.unindexed", value="true")
	private String city;
	
	@Persistent
	@Extension(vendorName="datanucleus", key="gae.unindexed", value="true")
	private Sex sex;
	
	@Persistent
	private int birthYear;
	
	@Persistent
	private Date registrationDate;
	
	@Persistent
	private List<String> registeredOccurrenceKeys;

	public User(){
		this.userName = "";
		this.firstName = "";
		this.lastName = "";
		this.email = "";
		this.phone = "";
		this.city = "";
		this.sex = null;
		this.birthYear = 0;
		this.registrationDate = null;
		this.registeredOccurrenceKeys = new LinkedList<String>();
	}
	
	public User(String userName, String firstName, String lastName, String email, String phone, String city, Sex sex, int birthYear, Date registrationDate) {
		this.userName = userName;
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setEmail(email);
		this.setPhone(phone);
		this.setCity(city);
		this.setSex(sex);
		this.setBirthYear(birthYear);
		this.setRegistrationDate(registrationDate);
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
	
	public String getPhone() {
		return phone;
	}
	
	public String getCity() {
		return city;
	}
	
	public Sex getSex() {
		return sex;
	}

	public int getBirthYear() {
		return birthYear;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public List<String> getRegisteredOccurrenceKeys() {
		return registeredOccurrenceKeys;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
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
	
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public void addRegisteredOccurrenceKey(String newKey) {
		this.registeredOccurrenceKeys.add(newKey);
	}
	
	public void removeRegisteredOccurrenceKey(String registeredKey) {
		this.registeredOccurrenceKeys.remove(registeredKey);
	}
	
	public enum Sex{
		MALE("Male"),
		FEMALE("Female");
		
		private final String sexString;
		
		Sex(String sexString){
			this.sexString = sexString; 
		}
		
		public static Sex getSex(String input){
			if (input.toLowerCase().equals(new String("male")))
				return Sex.MALE;
			
			if (input.equals(new String("זכר")))
				return Sex.MALE;
			
			if (input.toLowerCase().equals(new String("female")))
				return Sex.FEMALE;
			
			if (input.equals(new String("נקבה")))
				return Sex.FEMALE;
			
			return null;
		}
		
		public boolean isMale(){
			return this.equals(Sex.MALE);
		}
		
		public boolean isFemale(){
			return this.equals(Sex.FEMALE);
		}
		
		public String toString(){
			return this.sexString;
		}
	}
}
