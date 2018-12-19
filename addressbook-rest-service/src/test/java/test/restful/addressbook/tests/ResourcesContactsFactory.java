package test.restful.addressbook.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import code.restful.addressbook.dao.ContactDetails;
import code.restful.addressbook.dao.IndexedContactDetailsInfo;
import code.restful.persistence.addressbook.ContactDetailsFactory;

public class ResourcesContactsFactory implements ContactDetailsFactory {

	private static final ResourceBundle resourceContacts = ResourceBundle.getBundle("contactdetails");

	private static final Pattern contactPattern = Pattern.compile("(?m)^firstName=(.*?),lastName=(.*?),phone=(.*)$");

	@Override
	public Collection<IndexedContactDetailsInfo> createInitialContactDetails() {
		Collection<IndexedContactDetailsInfo> contactList = new ArrayList<IndexedContactDetailsInfo>();
		for (String id : resourceContacts.keySet()) {
			String contact = resourceContacts.getString(id);
			Matcher contactMatcher = contactPattern.matcher(contact);
			if (contactMatcher.matches()) {
				String name = contactMatcher.group(1);
				String surname = contactMatcher.group(2);
				String phone = contactMatcher.group(3);
				contactList.add(new IndexedContactDetailsInfo(Long.valueOf(id), name, surname, phone));
			}
		}
		return contactList;
	}

	@Override
	public Map<Long, IndexedContactDetailsInfo> createInitialContactDetailsMap() {
		return createInitialContactDetails().stream()
				.collect(Collectors.toMap(IndexedContactDetailsInfo::getId, contactDetails -> contactDetails));
	}

	@Override
	public IndexedContactDetailsInfo createIndexedContact(ContactDetails aContactDetails) {
		return new IndexedContactDetailsInfo(UUID.randomUUID().timestamp(), aContactDetails);
	}

}
