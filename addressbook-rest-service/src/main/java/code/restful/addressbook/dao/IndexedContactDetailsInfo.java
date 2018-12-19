package code.restful.addressbook.dao;

/**
 * Class to generate an unique id to index the contact details to avoid
 * duplicate names and quicker retrieval
 * 
 * @author NaveenG
 *
 */
public class IndexedContactDetailsInfo extends ContactDetails {

	private Long id;

	public IndexedContactDetailsInfo(long anId, String aFirstName, String aLastName, String aPhoneNumber) {
		super(aFirstName, aLastName, aPhoneNumber);
		this.id = anId;
	}

	public IndexedContactDetailsInfo(long anId, ContactDetails aContactDetails) {
		super(aContactDetails);
		this.id = anId;
	}

	public IndexedContactDetailsInfo() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long anId) {
		this.id = anId;
	}
}
