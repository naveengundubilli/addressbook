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
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import code.restful.AddressBookApplication;
import code.restful.addressbook.AddressBook;
import code.restful.addressbook.dao.ContactDetails;
import code.restful.addressbook.dao.IndexedContactDetailsInfo;
import code.restful.persistence.addressbook.InMemoryAddressBook;
import net.minidev.json.JSONObject;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AddressBookApplication.class)
@WebAppConfiguration
public class MockServerTests {

  private static final String addressBookUrl = "http://localhost:8080/addressbook";

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private AddressBook addressBook;

  /**
   * The contacts that will be used to fill the addressbook and run the tests.
   */
  private List<IndexedContactDetailsInfo> contactDetailsInfo;

  /**
   * A factory that will help with creation of contacts.
   */
  private ResourcesContactsFactory contactsFactory;

  private ArrayList<JSONObject> jsonContacts;

  private MockMvc mockMvc;

  // private RestTemplate testTemplate;

  private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
      MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

  @Before
  public void setup() {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
    // testTemplate = new TestRestTemplate(); // Use for end-to-end tests

    if (addressBook instanceof InMemoryAddressBook) {
      contactsFactory = new ResourcesContactsFactory();
      ((InMemoryAddressBook) addressBook).setContactDetailsFactory(contactsFactory);
      ((InMemoryAddressBook) addressBook).init(); // FIXME
      contactDetailsInfo =
          new ArrayList<IndexedContactDetailsInfo>(contactsFactory.createInitialContactDetails());
      jsonContacts = new ArrayList<>(
          contactDetailsInfo.stream().map(this::createJsonObject).collect(Collectors.toList()));
    }
  }

  private JSONObject createJsonObject(ContactDetails contactDetails) {
    return createJsonObjectFromContactDetails(contactDetails.getFirstName(),
        contactDetails.getLastName(), contactDetails.getPhone());
  }

  private JSONObject createJsonObject(IndexedContactDetailsInfo indexedContactDetailsInfo) {
    return createJsonObjectFromIndexedContact(indexedContactDetailsInfo.getId(),
        indexedContactDetailsInfo.getFirstName(), indexedContactDetailsInfo.getLastName(),
        indexedContactDetailsInfo.getPhone());
  }

  private JSONObject createJsonObjectFromIndexedContact(long aId, String aFirstName,
      String aLastName, String aPhone) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("phone", aPhone);
    jsonObject.put("lastName", aLastName);
    jsonObject.put("firstName", aFirstName);
    jsonObject.put("id", aId);

    return jsonObject;
  }

  private JSONObject createJsonObjectFromContactDetails(String aFirstName, String aLastName,
      String aPhone) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("aPhone", aPhone);
    jsonObject.put("aLastName", aLastName);
    jsonObject.put("aFirstName", aFirstName);
    return jsonObject;
  }

  @Test
  public void getAllContacts() throws Exception {
    mockMvc.perform(get(addressBookUrl + "/contactdetails").accept(contentType))
        .andExpect(status().isOk()).andExpect(content().contentType(contentType)).andDo(result -> {
          System.out.println("Response: " + result.getResponse().getContentAsString());
        }).andExpect(jsonPath("$", hasSize(jsonContacts.size()))).andExpect(
            result -> jsonContacts.stream().forEach(contact -> jsonPath("$", hasItem(contact))));
  }

  @Test
  public void findContactById() throws Exception {
    IndexedContactDetailsInfo randomContact = getRandomContact();
    mockMvc
        .perform(
            get(addressBookUrl + "/contactdetails/{id}", randomContact.getId()).accept(contentType))
        .andExpect(status().isOk()).andExpect(content().contentType(contentType)).andDo(result -> {
          System.out.println("Response: " + result.getResponse().getContentAsString());
        }).andExpect(jsonPath("$.id", is(Integer.valueOf(randomContact.getId().toString()))))
        .andExpect(jsonPath("$.firstName", is(randomContact.getFirstName())))
        .andExpect(jsonPath("$.lastName", is(randomContact.getLastName())))
        .andExpect(jsonPath("$.phone", is(randomContact.getPhone())));
  }

  @Test
  public void findNotExistingContactById() throws Exception {
    mockMvc.perform(get(addressBookUrl + "/contactdetails/{id}", contactDetailsInfo.size() + 1)
        .accept(contentType)).andDo(result -> {
          System.out.println("Response: " + result.getResponse().getContentAsString());
        }).andExpect(status().isNotFound());
  }

  @Test
  public void findContactsByFirstName() throws Exception {
    IndexedContactDetailsInfo randomContact = getRandomContact();
    mockMvc
        .perform(get(addressBookUrl + "/contactdetails?aFirstName={aFirstName}",
            randomContact.getFirstName()).accept(contentType))
        .andExpect(status().isOk()).andExpect(content().contentType(contentType)).andDo(result -> {
          System.out.println("Response: " + result.getResponse().getContentAsString());
        }).andExpect(jsonPath("$.[*].aFirstName", everyItem(is(randomContact.getFirstName()))));
  }

  @Test
  public void findContactsByNotExistingFirstName() throws Exception {
    String impropableNameToApper = "asdfghjkqwertqewtqadfafdlkhouwe";
    mockMvc.perform(
        get(addressBookUrl + "/contactdetails?firstName={aFirstName}", impropableNameToApper)
            .accept(contentType))
        .andDo(result -> {
          System.out.println("Response: " + result.getResponse().getContentAsString());
        }).andExpect(status().isOk()).andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  public void findcontactDetailsInfoByLastName() throws Exception {
    IndexedContactDetailsInfo randomContact = getRandomContact();
    mockMvc
        .perform(
            get(addressBookUrl + "/contactdetails?surname={surname}", randomContact.getLastName())
                .accept(contentType))
        .andExpect(status().isOk()).andExpect(content().contentType(contentType)).andDo(result -> {
          System.out.println("Response: " + result.getResponse().getContentAsString());
        }).andExpect(jsonPath("$.[*].surname", everyItem(is(randomContact.getLastName()))));
  }

  @Test
  public void findcontactDetailsInfoByFirstAndLastName() throws Exception {
    IndexedContactDetailsInfo randomContact = getRandomContact();
    mockMvc
        .perform(get(addressBookUrl + "/contactdetails?name={name}&surname={surname}",
            randomContact.getFirstName(), randomContact.getLastName()).accept(contentType))
        .andExpect(status().isOk()).andExpect(content().contentType(contentType)).andDo(result -> {
          System.out.println("Response: " + result.getResponse().getContentAsString());
        }).andExpect(jsonPath("$.[*].name", everyItem(is(randomContact.getFirstName()))))
        .andExpect(jsonPath("$.[*].surname", everyItem(is(randomContact.getLastName()))));
  }

  @Test
  public void findContactsByPhone() throws Exception {
    IndexedContactDetailsInfo randomContact = getRandomContact();
    mockMvc
        .perform(get(addressBookUrl + "/contactdetails?phone={phone}", randomContact.getPhone())
            .accept(contentType))
        .andExpect(status().isOk()).andExpect(content().contentType(contentType)).andDo(result -> {
          System.out.println("Response: " + result.getResponse().getContentAsString());
        }).andExpect(jsonPath("$.[*].phone", everyItem(is(randomContact.getPhone()))));
  }

  @Test
  public void createNotExistingContact() throws Exception {
    IndexedContactDetailsInfo demoContact = createDemoContact();
    mockMvc
        .perform(post(addressBookUrl + "/contactdetails").contentType(contentType)
            .content(createJsonObject(demoContact).toString()).accept(contentType))
        .andExpect(status().isCreated()).andExpect(content().contentType(contentType))
        .andDo(result -> {
          System.out.println("Response: " + result.getResponse().getContentAsString());
        }).andExpect(jsonPath("$.firstName", is(demoContact.getFirstName())))
        .andExpect(jsonPath("$.lastName", is(demoContact.getLastName())))
        .andExpect(jsonPath("$.phone", is(demoContact.getPhone())));
  }

  @Test
  public void createContactTwice() throws Exception {
    IndexedContactDetailsInfo demoContact = createDemoContact();

    MvcResult mvcResult = mockMvc
        .perform(post(addressBookUrl + "/contactdetails").contentType(contentType)
            .content(createJsonObject(demoContact).toString()).accept(contentType))
        .andExpect(status().isCreated()).andExpect(content().contentType(contentType))
        .andDo(result -> {
          System.out.println("Response: " + result.getResponse().getContentAsString());
        }).andExpect(jsonPath("$.firstName", is(demoContact.getFirstName())))
        .andExpect(jsonPath("$.lastName", is(demoContact.getLastName())))
        .andExpect(jsonPath("$.phone", is(demoContact.getPhone()))).andReturn();

    Long id1 = convertJsonToPojo(IndexedContactDetailsInfo.class,
        mvcResult.getResponse().getContentAsString()).getId();

    mockMvc.perform(get(addressBookUrl + "/contactdetails/{id}", id1).accept(contentType))
        .andExpect(status().isOk()).andExpect(content().contentType(contentType)).andDo(result -> {
          System.out.println("Response: " + result.getResponse().getContentAsString());
        }).andExpect(jsonPath("$.id", is(Integer.valueOf(id1.toString()))))
        .andExpect(jsonPath("$.firstName", is(demoContact.getFirstName())))
        .andExpect(jsonPath("$.lastName", is(demoContact.getLastName())))
        .andExpect(jsonPath("$.phone", is(demoContact.getPhone())));

    mockMvc
        .perform(post(addressBookUrl + "/contactdetails").contentType(contentType)
            .content(createJsonObject(demoContact).toString()).accept(contentType))
        .andExpect(status().isCreated()).andExpect(content().contentType(contentType))
        .andDo(result -> {
          System.out.println("Response: " + result.getResponse().getContentAsString());
        }).andExpect(jsonPath("$.id", not(id1)))
        .andExpect(jsonPath("$.firstName", is(demoContact.getFirstName())))
        .andExpect(jsonPath("$.lastName", is(demoContact.getLastName())))
        .andExpect(jsonPath("$.phone", is(demoContact.getPhone())));
  }


  @Test
  public void putExistingContact() throws Exception {
    IndexedContactDetailsInfo randomContact = getRandomContact();
    randomContact.setFirstName("newName");
    randomContact.setLastName("newSurname");
    randomContact.setPhone("newPhone");

    mockMvc
        .perform(put(addressBookUrl + "/contactdetails/" + randomContact.getId())
            .contentType(contentType).content(createJsonObject(randomContact).toString())
            .accept(contentType))
        .andExpect(status().isOk()).andExpect(content().contentType(contentType)).andDo(result -> {
          System.out.println("Response: " + result.getResponse().getContentAsString());
        }).andExpect(jsonPath("$.id", is(Integer.valueOf(randomContact.getId().toString()))))
        .andExpect(jsonPath("$.firstName", is(randomContact.getFirstName())))
        .andExpect(jsonPath("$.lastName", is(randomContact.getLastName())))
        .andExpect(jsonPath("$.phone", is(randomContact.getPhone())));

    mockMvc
        .perform(
            get(addressBookUrl + "/contactdetails/{id}", randomContact.getId()).accept(contentType))
        .andExpect(status().isOk()).andExpect(content().contentType(contentType)).andDo(result -> {
          System.out.println("Response: " + result.getResponse().getContentAsString());
        }).andExpect(jsonPath("$.id", is(Integer.valueOf(randomContact.getId().toString()))))
        .andExpect(jsonPath("$.firstName", is(randomContact.getFirstName())))
        .andExpect(jsonPath("$.lastName", is(randomContact.getLastName())))
        .andExpect(jsonPath("$.phone", is(randomContact.getPhone())));
  }

  @Test
  public void deleteContact() throws Exception {
    IndexedContactDetailsInfo randomContact = getRandomContact();
    mockMvc.perform(delete(addressBookUrl + "/contactdetails/" + randomContact.getId())
        .contentType(contentType).accept(contentType)).andExpect(status().isNoContent());

    mockMvc
        .perform(
            get(addressBookUrl + "contactdetails/{id}", randomContact.getId()).accept(contentType))
        .andExpect(status().isNotFound());
  }

  private <T extends Object> T convertJsonToPojo(Class<T> clazz, String json) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(json, clazz);
  }

  private IndexedContactDetailsInfo createDemoContact() {
    String firstName = "demoName";
    String lastName = "demoSurname";
    String phone = "+61-426-123-999";
    return new IndexedContactDetailsInfo((int) (Math.random()),
        new ContactDetails(firstName, lastName, phone));
  }

  private IndexedContactDetailsInfo getRandomContact() {
    Random randomIndexGenerator = new Random();
    int randomContactIndex = randomIndexGenerator.nextInt(contactDetailsInfo.size() - 1);
    return contactDetailsInfo.get(randomContactIndex);
  }

}
