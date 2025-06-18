package code.restful.addressbook;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import code.restful.persistence.addressbook.InMemoryAddressBook2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import code.restful.addressbook.dao.IndexedContactDetailsInfo;
import code.restful.exception.ContactNotFoundException;
import code.restful.exception.DuplicateContactException;
import code.restful.exception.InvalidContactException;

@RestController
@RequestMapping("/api/contacts")
@Tag(name = "Address Book", description = "APIs for managing contacts in the address book")
public class AddressBookController {

    private final AddressBook addressBook;

    @Autowired
    public AddressBookController(AddressBook addressBook) {
        this.addressBook = addressBook;
    }

    @GetMapping
    @Operation(summary = "Get all contacts", description = "Retrieves a list of all contacts in the address book")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all contacts")
    public ResponseEntity<List<ContactDetails>> getContacts(
            @RequestParam Optional<String> firstName,
            @RequestParam Optional<String> lastName,
            @RequestParam Optional<String> phoneNumber) {
        List<ContactDetails> contacts = addressBook.searchContacts(firstName, lastName, phoneNumber)
            .stream()
            .map(IndexedContactDetailsInfo::toContactDetails)
            .collect(Collectors.toList());
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get contact by ID", description = "Retrieves a specific contact by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the contact"),
        @ApiResponse(responseCode = "404", description = "Contact not found")
    })
    public ResponseEntity<ContactDetails> getContactById(@PathVariable Long id) {
        return addressBook.getContactById(id)
            .map(IndexedContactDetailsInfo::toContactDetails)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new contact", description = "Creates a new contact in the address book")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Contact successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid contact data provided"),
        @ApiResponse(responseCode = "409", description = "Contact already exists")
    })
    public ResponseEntity<ContactDetails> createContact(@Valid @RequestBody ContactDetailsImpl contact) {
        try {
            IndexedContactDetailsInfo indexedContact = new IndexedContactDetailsInfo(
                contact.getId(),
                contact.getFirstName(),
                contact.getLastName(),
                contact.getPhoneNumber(),
                contact.getEmail(),
                contact.getAddress()
            );
            ContactDetails createdContact = addressBook.addContact(indexedContact).toContactDetails();
            return new ResponseEntity<>(createdContact, HttpStatus.CREATED);
        } catch (DuplicateContactException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (InvalidContactException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update contact", description = "Updates an existing contact's information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contact successfully updated"),
        @ApiResponse(responseCode = "404", description = "Contact not found"),
        @ApiResponse(responseCode = "400", description = "Invalid contact data provided")
    })
    public ResponseEntity<ContactDetails> updateContact(
            @PathVariable Long id,
            @Valid @RequestBody ContactDetailsImpl contact) {
        if (!id.equals(contact.getId())) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            IndexedContactDetailsInfo indexedContact = new IndexedContactDetailsInfo(
                id,
                contact.getFirstName(),
                contact.getLastName(),
                contact.getPhoneNumber(),
                contact.getEmail(),
                contact.getAddress()
            );
            ContactDetails updatedContact = addressBook.updateContact(id, indexedContact).toContactDetails();
            return ResponseEntity.ok(updatedContact);
        } catch (ContactNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidContactException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete contact", description = "Removes a contact from the address book")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Contact successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Contact not found")
    })
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        try {
            addressBook.deleteContact(id);
            return ResponseEntity.noContent().build();
        } catch (ContactNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 