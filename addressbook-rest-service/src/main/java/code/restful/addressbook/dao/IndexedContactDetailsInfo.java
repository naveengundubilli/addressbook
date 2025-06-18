package code.restful.addressbook.dao;

import code.restful.addressbook.ContactDetails;
import code.restful.addressbook.ContactDetailsImpl;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Class to generate an unique id to index the contact details to avoid
 * duplicate names and quicker retrieval
 * 
 * @author NaveenG
 *
 */
public class IndexedContactDetailsInfo implements ContactDetails {
	private Long id;
	
	@NotBlank(message = "First name is required")
	@Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
	private String firstName;
	
	@NotBlank(message = "Last name is required")
	@Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
	private String lastName;
	
	@NotBlank(message = "Phone number is required")
	@Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
	private String phoneNumber;
	
	@Email(message = "Invalid email format")
	private String email;
	
	@Size(max = 200, message = "Address must not exceed 200 characters")
	private String address;

	public IndexedContactDetailsInfo() {
	}

	public IndexedContactDetailsInfo(Long id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public IndexedContactDetailsInfo(Long id, String firstName, String lastName, String phoneNumber) {
		this(id, firstName, lastName);
		this.phoneNumber = phoneNumber;
	}

	public IndexedContactDetailsInfo(Long id, String firstName, String lastName, String phoneNumber, String email, String address) {
		this(id, firstName, lastName, phoneNumber);
		this.email = email;
		this.address = address;
	}

	public static IndexedContactDetailsInfo fromContactDetails(ContactDetails contact) {
		return new IndexedContactDetailsInfo(
			contact.getId(),
			contact.getFirstName(),
			contact.getLastName(),
			contact.getPhoneNumber(),
			contact.getEmail(),
			contact.getAddress()
		);
	}

	public ContactDetails toContactDetails() {
		return new ContactDetailsImpl(
			id,
			firstName,
			lastName,
			email,
			phoneNumber,
			address
		);
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String getPhoneNumber() {
		return phoneNumber;
	}

	@Override
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getAddress() {
		return address;
	}

	@Override
	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		IndexedContactDetailsInfo that = (IndexedContactDetailsInfo) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
		if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
		if (phoneNumber != null ? !phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null) return false;
		if (email != null ? !email.equals(that.email) : that.email != null) return false;
		return address != null ? address.equals(that.address) : that.address == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (address != null ? address.hashCode() : 0);
		return result;
	}
}
