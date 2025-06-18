package code.restful.persistence.addressbook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import code.restful.addressbook.ContactDetails;
import code.restful.addressbook.dao.IndexedContactDetailsInfo;

public class ContactDetailsImpl implements ContactDetailsFactory {

	private long identifier = 0;

	@Override
	public Collection<IndexedContactDetailsInfo> createInitialContactDetails() {
		Collection<IndexedContactDetailsInfo> contactDetailsInfoList = new ArrayList<>();
		contactDetailsInfoList.add(new IndexedContactDetailsInfo(newUniqueIdentifier(), "Contact", "ONE", "+61-456-555-001", "contact.one@example.com", "123 One St"));
		contactDetailsInfoList.add(new IndexedContactDetailsInfo(newUniqueIdentifier(), "Contact", "TWO", "+61-456-555-002", "contact.two@example.com", "456 Two Ave"));
		contactDetailsInfoList.add(new IndexedContactDetailsInfo(newUniqueIdentifier(), "Contact", "THREE", "+61-456-555-003", "contact.three@example.com", "789 Three Rd"));
		return contactDetailsInfoList;
	}

	@Override
	public Map<Long, IndexedContactDetailsInfo> createInitialContactDetailsMap() {
		Map<Long, IndexedContactDetailsInfo> contactDetailsMap = new HashMap<>();

		Long nextId = newUniqueIdentifier();
		contactDetailsMap.put(nextId, new IndexedContactDetailsInfo(nextId, "Contact", "ONE", "+61-456-555-001", "contact.one@example.com", "123 One St"));

		nextId = newUniqueIdentifier();
		contactDetailsMap.put(nextId, new IndexedContactDetailsInfo(nextId, "Contact", "TWO", "+61-456-555-002", "contact.two@example.com", "456 Two Ave"));

		nextId = newUniqueIdentifier();
		contactDetailsMap.put(nextId, new IndexedContactDetailsInfo(nextId, "Contact", "THREE", "+61-456-555-003", "contact.three@example.com", "789 Three Rd"));

		return contactDetailsMap;
	}

	@Override
	public IndexedContactDetailsInfo createIndexedContact(ContactDetails contact) {
		return new IndexedContactDetailsInfo(
			contact.getId(),
			contact.getFirstName(),
			contact.getLastName(),
			contact.getPhoneNumber(),
			contact.getEmail(),
			contact.getAddress()
		);
	}

	private long newUniqueIdentifier() {
		return identifier++;
	}

	public ContactDetails createContact(Long id, String firstName, String lastName) {
		return new IndexedContactDetailsInfo(id, firstName, lastName);
	}

	public ContactDetails createContact(Long id, String firstName, String lastName, String phoneNumber) {
		return new IndexedContactDetailsInfo(id, firstName, lastName, phoneNumber, null, null);
	}

	public ContactDetails createContact(Long id, String firstName, String lastName, String phoneNumber, String email) {
		return new IndexedContactDetailsInfo(id, firstName, lastName, phoneNumber, email, null);
	}

	public ContactDetails createContact(Long id, String firstName, String lastName, String phoneNumber, String email, String address) {
		return new IndexedContactDetailsInfo(id, firstName, lastName, phoneNumber, email, address);
	}
}
