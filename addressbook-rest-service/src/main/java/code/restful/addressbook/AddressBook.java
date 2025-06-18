package code.restful.addressbook;

import java.util.Collection;
import java.util.Optional;

import code.restful.addressbook.dao.ContactDetails;
import code.restful.addressbook.dao.IndexedContactDetailsInfo;

public interface AddressBook {

	/**
	 * Search contacts based on filter conditions
	 * 
	 * @param firstName Optional first name filter
	 * @param lastName Optional last name filter
	 * @param phoneNumber Optional phone number filter
	 * @return Collection of matching contacts
	 */
	Collection<IndexedContactDetailsInfo> searchContacts(Optional<String> firstName,
			Optional<String> lastName, Optional<String> phoneNumber);

	/**
	 * Returns specific contact details based on ID
	 * 
	 * @param id Contact ID
	 * @return Optional containing the contact if found
	 */
	Optional<IndexedContactDetailsInfo> getContactById(Long id);
	
	/**
	 * Add a new contact to the address book
	 * 
	 * @param contact Contact details to add
	 * @return The added contact with generated ID
	 */
	IndexedContactDetailsInfo addContact(IndexedContactDetailsInfo contact);

	/**
	 * Update an existing contact
	 * 
	 * @param id Contact ID to update
	 * @param contact New contact details
	 * @return The updated contact
	 */
	IndexedContactDetailsInfo updateContact(Long id, IndexedContactDetailsInfo contact);

	/**
	 * Delete contact based on ID
	 * 
	 * @param id Contact ID to delete
	 */
	void deleteContact(Long id);

}
