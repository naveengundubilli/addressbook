package code.restful.addressbook;

import code.restful.addressbook.dao.IndexedContactDetailsInfo;
import code.restful.addressbook.exceptions.InvalidDetailsException;
import code.restful.addressbook.exceptions.NoContactDetailsFoundException;

import java.util.*;
import java.util.stream.Collectors;

public class AddressBookImpl implements AddressBook {
    private final Map<Long, IndexedContactDetailsInfo> contacts = new HashMap<>();
    private long nextId = 1;

    @Override
    public IndexedContactDetailsInfo addContact(IndexedContactDetailsInfo contact) {
        if (contact == null) {
            throw new InvalidDetailsException("Contact details cannot be null");
        }

        if (contact.getId() == null) {
            contact.setId(nextId++);
        }

        contacts.put(contact.getId(), contact);
        return contact;
    }

    @Override
    public Optional<IndexedContactDetailsInfo> getContactById(Long id) {
        return Optional.ofNullable(contacts.get(id));
    }

    @Override
    public Collection<IndexedContactDetailsInfo> searchContacts(
            Optional<String> firstName,
            Optional<String> lastName,
            Optional<String> phoneNumber) {
        
        return contacts.values().stream()
                .filter(contact -> firstName.isEmpty() || contact.getFirstName().equals(firstName.get()))
                .filter(contact -> lastName.isEmpty() || contact.getLastName().equals(lastName.get()))
                .filter(contact -> phoneNumber.isEmpty() || contact.getPhoneNumber().equals(phoneNumber.get()))
                .collect(Collectors.toList());
    }

    @Override
    public IndexedContactDetailsInfo updateContact(Long id, IndexedContactDetailsInfo contact) {
        if (id == null || contact == null) {
            throw new InvalidDetailsException("Contact ID and details cannot be null");
        }

        if (!contacts.containsKey(id)) {
            throw new NoContactDetailsFoundException("Contact not found with ID: " + id);
        }

        contact.setId(id);
        contacts.put(id, contact);
        return contact;
    }

    @Override
    public void deleteContact(Long id) {
        if (id == null) {
            throw new InvalidDetailsException("Contact ID cannot be null");
        }
        contacts.remove(id);
    }
} 