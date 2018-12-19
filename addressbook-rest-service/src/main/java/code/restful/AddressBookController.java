package code.restful;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import code.restful.addressbook.AddressBook;
import code.restful.addressbook.dao.ContactDetails;
import code.restful.addressbook.dao.IndexedContactDetailsInfo;
import code.restful.addressbook.exceptions.InvalidDetailsException;
import code.restful.addressbook.exceptions.NoContactDetailsFoundException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/addressbook/contactdetails")
public class AddressBookController {

	@Autowired
	private AddressBook addressBook;

	/**
	 * API to show all the contacts in InMemory Table
	 * 
	 * @param firstName
	 * @param lastName
	 * @param phone
	 * @returnaddressBook1
	 */
	@ApiOperation("Search inside the address book contacts by firstName, lastName or phone. If you provide no search criteria all the contacts will be returned.")
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public Collection<IndexedContactDetailsInfo> getContacts(
			@ApiParam(value = "Search contacts by firstName") @RequestParam(value = "firstName", required = false) String firstName,
			@ApiParam(value = "Search contacts by lastName") @RequestParam(value = "lastName", required = false) String lastName,
			@ApiParam(value = "Search contacts by phone") @RequestParam(value = "phone", required = false) String phone) {
		return addressBook.retreiveContactDetails(Optional.ofNullable(firstName), Optional.ofNullable(lastName),
				Optional.ofNullable(phone));
	}

	/**
	 * API to show details of single contact based on ID
	 * 
	 * @param id
	 * @return
	 * @throws NoContactDetailsFoundException
	 */
	@ApiOperation("Get a single contact using its id.")
	@RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "application/json")
	public IndexedContactDetailsInfo getContact(
			@ApiParam(value = "The id of the contact to be retrieved") @PathVariable String id)
			throws NoContactDetailsFoundException {
		return addressBook.retreiveContactDetailsById(Long.valueOf(id))
				.orElseThrow(() -> new NoContactDetailsFoundException(id));
	}

	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public IndexedContactDetailsInfo addContact(
			@ApiParam(value = "The contact to be added in the address book") @RequestBody ContactDetails contactDetails) {
		return addressBook.addContact(contactDetails);
	}

	/**
	 * Api to add contacts into In Memory Table
	 * 
	 * @param id
	 * @param indexedContact
	 * @return
	 * @throws InvalidDetailsException
	 */
	@ApiOperation("Add an indexed contact to the address book. If the id belongs to an older contact, the contact will be replaced.")
	@RequestMapping(value = "{id}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public IndexedContactDetailsInfo addContact(
			@ApiParam(value = "The id of the contact to be added") @PathVariable String id,
			@ApiParam(value = "The contact to be added, or replace the older one with the same id") @RequestBody IndexedContactDetailsInfo indexedContact)
			throws InvalidDetailsException {
		if (indexedContact.getId() == null || !id.equals(indexedContact.getId().toString())) {
			throw new InvalidDetailsException("The contact's id should be the same with the URI's id.");
		}
		return addressBook.addContact(indexedContact);
	}

	/***
	 * 
	 * Deletes the contacts**
	 * 
	 * @param id-
	 *            the id of the contact to be deleted.
	 */

	@ApiOperation("Delete a contact from the address book using its id.")
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteContact(@ApiParam(value = "The id of the contact to be deleted") @PathVariable String id) {
		addressBook.deleteContactDetails(Long.valueOf(id));
	}

}
