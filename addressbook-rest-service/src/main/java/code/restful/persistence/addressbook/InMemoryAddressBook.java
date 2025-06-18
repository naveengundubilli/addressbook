package code.restful.persistence.addressbook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;

import code.restful.addressbook.AddressBook;
import code.restful.addressbook.ContactDetails;
import code.restful.addressbook.dao.IndexedContactDetailsInfo;
import code.restful.exception.ContactNotFoundException;
import code.restful.exception.DuplicateContactException;

@Repository
public class InMemoryAddressBook implements AddressBook {

    private final Map<Long, IndexedContactDetailsInfo> contacts = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private ContactDetailsFactory contactDetailsFactory;

    public InMemoryAddressBook() {
        contactDetailsFactory = new ContactDetailsImpl();
    }

    @PostConstruct
    public void init() {
        contactDetailsFactory.createInitialContactDetailsMap().forEach((id, contactDetails) -> {
            contacts.put(id, contactDetails);
        });
    }

    @Override
    public List<IndexedContactDetailsInfo> searchContacts(Optional<String> firstName,
            Optional<String> lastName, Optional<String> phoneNumber) {
        Stream<IndexedContactDetailsInfo> contactStream = contacts.values().stream();
        if (!firstName.isPresent() && !lastName.isPresent() && !phoneNumber.isPresent()) {
            return contactStream.collect(Collectors.toList());
        }
        return contactStream
            .filter(contact -> filterByAttribute(contact::getFirstName, firstName)
                && filterByAttribute(contact::getLastName, lastName)
                && filterByAttribute(contact::getPhoneNumber, phoneNumber))
            .collect(Collectors.toList());
    }

    @Override
    public Optional<IndexedContactDetailsInfo> getContactById(Long id) {
        return Optional.ofNullable(contacts.get(id));
    }

    @Override
    public IndexedContactDetailsInfo addContact(IndexedContactDetailsInfo contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact details cannot be null");
        }
        
        // Check for duplicate contact
        if (contacts.values().stream().anyMatch(c -> 
            c.getFirstName().equals(contact.getFirstName()) && 
            c.getLastName().equals(contact.getLastName()))) {
            throw new DuplicateContactException(contact.getFirstName(), contact.getLastName());
        }

        Long id = idGenerator.getAndIncrement();
        contact.setId(id);
        contacts.put(id, contact);
        return contact;
    }

    @Override
    public IndexedContactDetailsInfo updateContact(Long id, IndexedContactDetailsInfo contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Updated contact cannot be null");
        }

        IndexedContactDetailsInfo existingContact = contacts.get(id);
        if (existingContact == null) {
            throw new ContactNotFoundException(id);
        }

        contact.setId(id);
        contacts.put(id, contact);
        return contact;
    }

    @Override
    public void deleteContact(Long id) {
        if (!contacts.containsKey(id)) {
            throw new ContactNotFoundException(id);
        }
        contacts.remove(id);
    }

    private boolean filterByAttribute(Supplier<String> getContactAttribute,
            Optional<String> comparingAttribute) {
        if (comparingAttribute.isPresent()) {
            if (!comparingAttribute.get().equals(getContactAttribute.get())) {
                return false;
            }
        }
        return true;
    }

    public void setContactDetailsFactory(ContactDetailsFactory contactDetailsFactory) {
        this.contactDetailsFactory = contactDetailsFactory;
    }

    public void clear() {
        contacts.clear();
        idGenerator.set(1);
    }
}
