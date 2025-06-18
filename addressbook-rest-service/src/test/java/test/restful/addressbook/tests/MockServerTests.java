package test.restful.addressbook.tests;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import code.restful.AddressBookApplication;
import code.restful.addressbook.AddressBook;
import code.restful.addressbook.ContactDetails;
import code.restful.addressbook.ContactDetailsImpl;
import code.restful.addressbook.dao.IndexedContactDetailsInfo;
import code.restful.persistence.addressbook.InMemoryAddressBook;
import code.restful.persistence.addressbook.InMemoryAddressBook2;
import net.minidev.json.JSONObject;
import code.restful.persistence.addressbook.ContactDetailsFactory;
import test.restful.addressbook.tests.ResourcesContactsFactory;

@SpringBootTest(classes = AddressBookApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MockServerTests {

	private static final String BASE_URL = "/api/contacts";

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private AddressBook addressBook;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * The contacts that will be used to fill the addressbook and run the tests.
	 */
	private List<IndexedContactDetailsInfo> contactDetailsInfo;

	/**
	 * A factory that will help with creation of contacts.
	 */
	private ResourcesContactsFactory contactsFactory;

	private ArrayList<JSONObject> jsonContacts;

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@BeforeEach
	public void setup() {
		// Clear the address book
		if (addressBook instanceof InMemoryAddressBook) {
			((InMemoryAddressBook) addressBook).clear();
		} else if (addressBook instanceof InMemoryAddressBook2) {
			((InMemoryAddressBook2) addressBook).clear();
		}

		// Initialize the contacts factory
		contactsFactory = new ResourcesContactsFactory();

		// Set up the address book with the factory
		if (addressBook instanceof InMemoryAddressBook) {
			((InMemoryAddressBook) addressBook).setContactDetailsFactory(contactsFactory);
			((InMemoryAddressBook) addressBook).init();
		} else if (addressBook instanceof InMemoryAddressBook2) {
			((InMemoryAddressBook2) addressBook).setContactDetailsFactory(contactsFactory);
			((InMemoryAddressBook2) addressBook).init();
		}

		// Initialize contact details
		if (contactDetailsInfo == null) {
			contactDetailsInfo = new ArrayList<>(contactsFactory.createInitialContactDetails());
		} else {
			contactDetailsInfo.clear();
			contactDetailsInfo.addAll(contactsFactory.createInitialContactDetails());
		}

		// Initialize JSON contacts
		if (jsonContacts == null) {
			jsonContacts = new ArrayList<>(
					contactDetailsInfo.stream().map(this::createJsonObject).collect(Collectors.toList()));
		} else {
			jsonContacts.clear();
			jsonContacts.addAll(
					contactDetailsInfo.stream().map(this::createJsonObject).collect(Collectors.toList()));
		}
	}

	private JSONObject createJsonObject(ContactDetails contactDetails) {
		return createJsonObjectFromContactDetails(contactDetails.getFirstName(), contactDetails.getLastName(),
				contactDetails.getPhoneNumber());
	}

	private JSONObject createJsonObject(IndexedContactDetailsInfo indexedContactDetailsInfo) {
		return createJsonObjectFromIndexedContact(indexedContactDetailsInfo.getId(),
				indexedContactDetailsInfo.getFirstName(), indexedContactDetailsInfo.getLastName(),
				indexedContactDetailsInfo.getPhoneNumber());
	}

	private JSONObject createJsonObjectFromIndexedContact(long aId, String aFirstName, String aLastName,
			String aPhone) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("phoneNumber", aPhone);
		jsonObject.put("lastName", aLastName);
		jsonObject.put("firstName", aFirstName);
		jsonObject.put("id", aId);

		return jsonObject;
	}

	private JSONObject createJsonObjectFromContactDetails(String aFirstName, String aLastName, String aPhone) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("phoneNumber", aPhone);
		jsonObject.put("lastName", aLastName);
		jsonObject.put("firstName", aFirstName);
		return jsonObject;
	}

	@Test
	public void getAllContacts() throws Exception {
		mockMvc.perform(get(BASE_URL))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray());
	}

	@Test
	public void findContactById() throws Exception {
		IndexedContactDetailsInfo randomContact = getRandomContact();
		mockMvc.perform(get(BASE_URL + "/{id}", randomContact.getId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(randomContact.getId()));
	}

	@Test
	public void findNotExistingContactById() throws Exception {
		mockMvc.perform(get(BASE_URL + "/{id}", contactDetailsInfo.size() + 1))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	@Test
	public void findContactsByFirstName() throws Exception {
		IndexedContactDetailsInfo randomContact = getRandomContact();
		mockMvc.perform(get(BASE_URL)
				.param("firstName", randomContact.getFirstName()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].firstName").value(randomContact.getFirstName()));
	}

	@Test
	public void findContactsByNotExistingFirstName() throws Exception {
		String improbableName = "asdfghjkqwertqewtqadfafdlkhouwe";
		mockMvc.perform(get(BASE_URL)
				.param("firstName", improbableName))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isEmpty());
	}

	@Test
	public void findcontactDetailsInfoByLastName() throws Exception {
		IndexedContactDetailsInfo randomContact = getRandomContact();
		mockMvc.perform(get(BASE_URL)
				.param("lastName", randomContact.getLastName()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].lastName").value(randomContact.getLastName()));
	}

	@Test
	public void findcontactDetailsInfoByFirstAndLastName() throws Exception {
		IndexedContactDetailsInfo randomContact = getRandomContact();
		mockMvc.perform(get(BASE_URL)
				.param("firstName", randomContact.getFirstName())
				.param("lastName", randomContact.getLastName()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].firstName").value(randomContact.getFirstName()))
				.andExpect(jsonPath("$[0].lastName").value(randomContact.getLastName()));
	}

	@Test
	public void findContactsByPhone() throws Exception {
		IndexedContactDetailsInfo randomContact = getRandomContact();
		mockMvc.perform(get(BASE_URL)
				.param("phoneNumber", randomContact.getPhoneNumber()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].phoneNumber").value(randomContact.getPhoneNumber()));
	}

	@Test
	public void createNotExistingContact() throws Exception {
		IndexedContactDetailsInfo demoContact = createDemoContact();
		String contactJson = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"phoneNumber\":\"1234567890\",\"email\":\"john.doe@example.com\",\"address\":\"123 Main St\"}";

		mockMvc.perform(post(BASE_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(contactJson))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.firstName").value("John"))
				.andExpect(jsonPath("$.lastName").value("Doe"));
	}

	@Test
	public void createContactTwice() throws Exception {
		IndexedContactDetailsInfo demoContact = createDemoContact();
		String contactJson = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"phoneNumber\":\"1234567890\",\"email\":\"john.doe@example.com\",\"address\":\"123 Main St\"}";

		mockMvc.perform(post(BASE_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(contactJson))
				.andDo(print())
				.andExpect(status().isCreated());

		mockMvc.perform(post(BASE_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(contactJson))
				.andDo(print())
				.andExpect(status().isConflict());
	}

	@Test
	public void putExistingContact() throws Exception {
		IndexedContactDetailsInfo randomContact = getRandomContact();
		String contactJson = "{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Doe\",\"phoneNumber\":\"1234567890\",\"email\":\"john.doe@example.com\",\"address\":\"123 Main St\"}";

		mockMvc.perform(put(BASE_URL + "/{id}", randomContact.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(contactJson))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName").value("John"))
				.andExpect(jsonPath("$.lastName").value("Doe"));
	}

	@Test
	public void deleteContact() throws Exception {
		IndexedContactDetailsInfo randomContact = getRandomContact();
		mockMvc.perform(delete(BASE_URL + "/{id}", randomContact.getId()))
				.andDo(print())
				.andExpect(status().isNoContent());

		mockMvc.perform(get(BASE_URL + "/{id}", randomContact.getId()))
				.andExpect(status().isNotFound());
	}

	private <T extends Object> T convertJsonToPojo(Class<T> clazz, String json) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

	private IndexedContactDetailsInfo createDemoContact() {
		String firstName = "demoFirstName";
		String lastName = "demoLastName";
		String phone = "+61-426-123-999";
		// Use null for id, email, and address for demo
		ContactDetailsImpl contact = new ContactDetailsImpl(null, firstName, lastName, phone, null, null);
		return new IndexedContactDetailsInfo(contact.getId(), contact.getFirstName(), contact.getLastName(), contact.getPhoneNumber(), contact.getEmail(), contact.getAddress());
	}

	private IndexedContactDetailsInfo getRandomContact() {
		Random randomIndexGenerator = new Random();
		int randomContactIndex = randomIndexGenerator.nextInt(contactDetailsInfo.size() - 1);
		return contactDetailsInfo.get(randomContactIndex);
	}

}
