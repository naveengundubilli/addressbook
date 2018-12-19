package code.restful.persistence.addressbook;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import code.restful.addressbook.AddressBook;
import code.restful.addressbook.dao.ContactDetails;
import code.restful.addressbook.dao.IndexedContactDetailsInfo;

@Repository
public class InMemoryAddressBook implements AddressBook {

	/**
	 * Holds the contacts that will be available in this phone book.
	 */
	private Map<Long, IndexedContactDetailsInfo> contactDetailsMap;

	/**
	 * Is used to create contacts for this phone book.
	 */
	private ContactDetailsFactory contactDetailsFactory;

	public InMemoryAddressBook() {
		contactDetailsFactory = new ContactDetailsImpl();
	}

	@PostConstruct
	public void init() {
		contactDetailsMap = contactDetailsFactory.createInitialContactDetailsMap();
	}

	public Collection<IndexedContactDetailsInfo> retreiveContactDetails(Optional<String> aFirstName,
			Optional<String> aLastName, Optional<String> aPhoneNumber) {
		Stream<IndexedContactDetailsInfo> contactStream = contactDetailsMap.entrySet().stream().map(Entry::getValue);
		if (!aFirstName.isPresent() && !aLastName.isPresent() && !aPhoneNumber.isPresent()) {
			return contactStream.collect(Collectors.toList());
		}
		return contactStream.filter(contactDetails -> (filterByAttribute(contactDetails::getFirstName, aFirstName)
				&& filterByAttribute(contactDetails::getLastName, aLastName)
				&& filterByAttribute(contactDetails::getPhone, aPhoneNumber))).collect(Collectors.toList());
	}

	public Optional<IndexedContactDetailsInfo> retreiveContactDetailsById(Long aId) {
		return Optional.ofNullable(contactDetailsMap.get(aId));
	}

	public IndexedContactDetailsInfo addContact(ContactDetails aContactDetails) {
		IndexedContactDetailsInfo indexedContactDetailsInfo = contactDetailsFactory
				.createIndexedContact(aContactDetails);
		contactDetailsMap.put(indexedContactDetailsInfo.getId(), indexedContactDetailsInfo);
		return indexedContactDetailsInfo;
	}

	public void deleteContactDetails(Long id) {
		contactDetailsMap.remove(id);

	}

	/**
	 * Compares a String attribute with another String value
	 * 
	 * If comparingAttribute, which is Optional, contains no value, boolean true
	 * will be returned.
	 * 
	 * @param getContactAttribute
	 * @param comparingAttribute
	 * @return
	 */
	private boolean filterByAttribute(Supplier<String> getContactDetailsAttribute,
			Optional<String> comparingAttribute) {
		if (comparingAttribute.isPresent()) {
			if (!comparingAttribute.get().equals(getContactDetailsAttribute.get())) {
				return false;
			}
		}
		return true;
	}

	public void setContactDetailsFactory(ContactDetailsFactory aContactDetailsFactory) {
		this.contactDetailsFactory = aContactDetailsFactory;
	}

	public class DuplicateContactException extends Exception {
		private static final long serialVersionUID = 1L;

		public DuplicateContactException(String message) {
			super(message);
		}
	}

}
