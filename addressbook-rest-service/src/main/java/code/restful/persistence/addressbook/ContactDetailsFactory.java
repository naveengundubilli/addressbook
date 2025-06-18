package code.restful.persistence.addressbook;

import java.util.Collection;
import java.util.Map;

import code.restful.addressbook.ContactDetails;
import code.restful.addressbook.dao.IndexedContactDetailsInfo;

public interface ContactDetailsFactory {

	public Collection<IndexedContactDetailsInfo> createInitialContactDetails();

	public Map<Long, IndexedContactDetailsInfo> createInitialContactDetailsMap();

	public IndexedContactDetailsInfo createIndexedContact(ContactDetails contact);
}
