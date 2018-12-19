package code.restful.persistence.addressbook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import code.restful.addressbook.dao.ContactDetails;
import code.restful.addressbook.dao.IndexedContactDetailsInfo;

public class ContactDetailsImpl implements ContactDetailsFactory {

	private long identifier = 0;

	@Override
	public Collection<IndexedContactDetailsInfo> createInitialContactDetails() {
		Collection<IndexedContactDetailsInfo> contactDetailsInfoList = new ArrayList<IndexedContactDetailsInfo>();
		contactDetailsInfoList
				.add(new IndexedContactDetailsInfo(newUniqueIdentifier(), "Contact", "ONE", "+61-456-555-001"));
		contactDetailsInfoList
				.add(new IndexedContactDetailsInfo(newUniqueIdentifier(), "Contact", "TWO", "+61-456-555-002"));
		contactDetailsInfoList
				.add(new IndexedContactDetailsInfo(newUniqueIdentifier(), "Contact", "THREE", "+61-456-555-003"));
		return contactDetailsInfoList;
	}

	@Override
	public Map<Long, IndexedContactDetailsInfo> createInitialContactDetailsMap() {

		Map<Long, IndexedContactDetailsInfo> contactDetailsMap = new HashMap<Long, IndexedContactDetailsInfo>();

			Long nextId = newUniqueIdentifier();
			contactDetailsMap.put(nextId, new IndexedContactDetailsInfo(nextId, "Contact", "ONE", "+61-456-555-001"));

			nextId = newUniqueIdentifier();
			contactDetailsMap.put(nextId, new IndexedContactDetailsInfo(nextId, "Contact", "TWO", "+61-456-555-002"));

			nextId = newUniqueIdentifier();
			contactDetailsMap.put(nextId, new IndexedContactDetailsInfo(nextId, "Contact", "THREE", "+61-456-555-003"));
		
		return contactDetailsMap;
	}

	@Override
	public IndexedContactDetailsInfo createIndexedContact(ContactDetails aContactDetails) {
		return new IndexedContactDetailsInfo(newUniqueIdentifier(), aContactDetails);
	}

	private long newUniqueIdentifier() {
		return identifier++;
	}
}
