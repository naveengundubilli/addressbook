package code.restful.addressbook.dao;

/**
 * DAO class for ContactDetails which would be stored for address book
 * 
 * @author NaveenG
 */
public class ContactDetails {

	private String firstName;
	private String lastName;
	private String phone;

	public ContactDetails(String aFirstName, String aLastName, String aPhoneNumber) {
		this.firstName = aFirstName;
		this.lastName = aLastName;
		this.phone = aPhoneNumber;
	}

	public ContactDetails(ContactDetails contactDetails) {
		this.firstName = contactDetails.getFirstName();
		this.lastName = contactDetails.getLastName();
		this.phone = contactDetails.getPhone();
	}

	public ContactDetails() {
		super();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String aFirstName) {
		this.firstName = aFirstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String aLastName) {
		this.lastName = aLastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String aPhoneNumber) {
		this.phone = aPhoneNumber;
	}

}
