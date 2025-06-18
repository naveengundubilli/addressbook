package code.restful.addressbook;

import code.restful.addressbook.dao.IndexedContactDetailsInfo;
import code.restful.addressbook.exceptions.InvalidDetailsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AddressBookTest {

    private AddressBook addressBook;
    private IndexedContactDetailsInfo contact1;
    private IndexedContactDetailsInfo contact2;

    @BeforeEach
    void setUp() {
        addressBook = new AddressBookImpl();
        
        contact1 = new IndexedContactDetailsInfo();
        contact1.setId(1L);
        contact1.setFirstName("John");
        contact1.setLastName("Doe");
        contact1.setPhoneNumber("1234567890");

        contact2 = new IndexedContactDetailsInfo();
        contact2.setId(2L);
        contact2.setFirstName("Jane");
        contact2.setLastName("Smith");
        contact2.setPhoneNumber("0987654321");
    }

    @Test
    void addContact_WithValidData_ShouldAddContact() {
        IndexedContactDetailsInfo addedContact = addressBook.addContact(contact1);
        
        assertNotNull(addedContact);
        assertEquals(contact1.getId(), addedContact.getId());
        assertEquals(contact1.getFirstName(), addedContact.getFirstName());
        assertEquals(contact1.getLastName(), addedContact.getLastName());
        assertEquals(contact1.getPhoneNumber(), addedContact.getPhoneNumber());
    }

    @Test
    void addContact_WithNullData_ShouldThrowException() {
        assertThrows(InvalidDetailsException.class, () -> addressBook.addContact(null));
    }

    @Test
    void retrieveContactDetailsById_WithExistingId_ShouldReturnContact() {
        addressBook.addContact(contact1);
        
        Optional<IndexedContactDetailsInfo> foundContact = addressBook.getContactById(1L);
        
        assertTrue(foundContact.isPresent());
        assertEquals(contact1.getId(), foundContact.get().getId());
    }

    @Test
    void retrieveContactDetailsById_WithNonExistingId_ShouldReturnEmpty() {
        Optional<IndexedContactDetailsInfo> foundContact = addressBook.getContactById(999L);
        
        assertTrue(foundContact.isEmpty());
    }

    @Test
    void retrieveContactDetails_WithNoFilters_ShouldReturnAllContacts() {
        addressBook.addContact(contact1);
        addressBook.addContact(contact2);
        
        Collection<IndexedContactDetailsInfo> contacts = addressBook.searchContacts(
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
        );
        
        assertEquals(2, contacts.size());
    }

    @Test
    void retrieveContactDetails_WithFirstNameFilter_ShouldReturnMatchingContacts() {
        addressBook.addContact(contact1);
        addressBook.addContact(contact2);
        
        Collection<IndexedContactDetailsInfo> contacts = addressBook.searchContacts(
            Optional.of("John"),
            Optional.empty(),
            Optional.empty()
        );
        
        assertEquals(1, contacts.size());
        assertEquals("John", contacts.iterator().next().getFirstName());
    }

    @Test
    void deleteContactDetails_WithExistingId_ShouldDeleteContact() {
        addressBook.addContact(contact1);
        addressBook.deleteContact(1L);
        
        Optional<IndexedContactDetailsInfo> foundContact = addressBook.getContactById(1L);
        assertTrue(foundContact.isEmpty());
    }

    @Test
    void deleteContactDetails_WithNonExistingId_ShouldNotThrowException() {
        assertDoesNotThrow(() -> addressBook.deleteContact(999L));
    }
} 