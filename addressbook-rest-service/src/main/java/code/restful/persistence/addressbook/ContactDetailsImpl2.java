package code.restful.persistence.addressbook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import code.restful.addressbook.ContactDetails;
import code.restful.addressbook.dao.IndexedContactDetailsInfo;

public class ContactDetailsImpl2 implements ContactDetailsFactory {

	private long identifier = 0;

	@Override
	public Collection<IndexedContactDetailsInfo> createInitialContactDetails() {
		Collection<IndexedContactDetailsInfo> contactDetailsInfoList = new ArrayList<>();
		contactDetailsInfoList.add(new IndexedContactDetailsInfo(newUniqueIdentifier(), "Contact", "FOUR", "+61-456-555-001", "contact.four@example.com", "123 Four St"));
		contactDetailsInfoList.add(new IndexedContactDetailsInfo(newUniqueIdentifier(), "Contact", "FIVE", "+61-456-555-002", "contact.five@example.com", "456 Five Ave"));
		contactDetailsInfoList.add(new IndexedContactDetailsInfo(newUniqueIdentifier(), "Contact", "SIX", "+61-456-555-003", "contact.six@example.com", "789 Six Rd"));
		return contactDetailsInfoList;
	}

	@Override
	public Map<Long, IndexedContactDetailsInfo> createInitialContactDetailsMap() {
		Map<Long, IndexedContactDetailsInfo> contactDetailsMap = new HashMap<>();

		Long nextId = newUniqueIdentifier();
		contactDetailsMap.put(nextId, new IndexedContactDetailsInfo(nextId, "Contact", "FOUR", "+61-456-555-001", "contact.four@example.com", "123 Four St"));

		nextId = newUniqueIdentifier();
		contactDetailsMap.put(nextId, new IndexedContactDetailsInfo(nextId, "Contact", "FIVE", "+61-456-555-002", "contact.five@example.com", "456 Five Ave"));

		nextId = newUniqueIdentifier();
		contactDetailsMap.put(nextId, new IndexedContactDetailsInfo(nextId, "Contact", "SIX", "+61-456-555-003", "contact.six@example.com", "789 Six Rd"));

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

	public static final ContactDetails JOHN_DOE = new IndexedContactDetailsInfo(1L, "John", "Doe", "+1234567890", "john.doe@example.com", "123 Main St");
	public static final ContactDetails JANE_SMITH = new IndexedContactDetailsInfo(2L, "Jane", "Smith", "+1987654321", "jane.smith@example.com", "456 Oak Ave");
	public static final ContactDetails BOB_JOHNSON = new IndexedContactDetailsInfo(3L, "Bob", "Johnson", "+1122334455", "bob.johnson@example.com", "789 Pine Rd");

	public ContactDetails createContact(Long id, String firstName, String lastName) {
		return new IndexedContactDetailsInfo(id, firstName, lastName, null, null, null);
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
