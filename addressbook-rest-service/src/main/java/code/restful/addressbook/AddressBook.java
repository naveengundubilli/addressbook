package code.restful.addressbook;

import java.util.Collection;
import java.util.Optional;

import code.restful.addressbook.dao.ContactDetails;
import code.restful.addressbook.dao.IndexedContactDetailsInfo;

public interface AddressBook {

	/**
	 * Retrieve contact details based on filter condition if every field in the
	 * filter is empty retrieve all the details in memory
	 * 
	 * @param aFirstName
	 * @param surname
	 * @param phone
	 * @return
	 */
	public Collection<IndexedContactDetailsInfo> retreiveContactDetails(Optional<String> aFirstName,
			Optional<String> aLastName, Optional<String> aPhoneNumber);

	/**
	 * Returns specific contact details based on ID
	 * 
	 * @param aId
	 * @return IndexedContacts
	 */
	public Optional<IndexedContactDetailsInfo> retreiveContactDetailsById(Long aId);
	
	/**
	 * Takes details from UI and creates a UniqueId and adds it to the in memory
	 * object.If details already exists will replace with new details else inserts
	 * it
	 * 
	 * @param aContactDetails
	 * @return IndexedContactDetailsInfo
	 */
	public IndexedContactDetailsInfo addContact(IndexedContactDetailsInfo aContactDetails);

	/**
	 * Delete contact based on ID
	 * 
	 * @param indexedContactDetailsInfo
	 */
	public void deleteContactDetails(Long id);

}
