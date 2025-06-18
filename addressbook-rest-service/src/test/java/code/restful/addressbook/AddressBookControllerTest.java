package code.restful.addressbook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import code.restful.addressbook.dao.IndexedContactDetailsInfo;
import code.restful.exception.ContactNotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AddressBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AddressBook addressBook;

    private IndexedContactDetailsInfo sampleContact;
    private ContactDetailsImpl sampleContactImpl;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        sampleContact = new IndexedContactDetailsInfo();
        sampleContact.setId(1L);
        sampleContact.setFirstName("John");
        sampleContact.setLastName("Doe");
        sampleContact.setPhoneNumber("1234567890");
        sampleContact.setEmail("john.doe@example.com");
        
        sampleContactImpl = new ContactDetailsImpl(1L, "John", "Doe", "john.doe@example.com", "1234567890", null);
    }

    @Test
    void addContact_WithValidData_ShouldCreateContact() throws Exception {
        when(addressBook.addContact(any(IndexedContactDetailsInfo.class))).thenReturn(sampleContact);

        mockMvc.perform(post("/api/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleContactImpl)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void updateContact_WithValidData_ShouldUpdateContact() throws Exception {
        when(addressBook.updateContact(anyLong(), any(IndexedContactDetailsInfo.class))).thenReturn(sampleContact);

        mockMvc.perform(put("/api/contacts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleContactImpl)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void updateContact_WithMismatchedId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/api/contacts/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleContactImpl)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteContact_WithInvalidId_ShouldReturnNotFound() throws Exception {
        doThrow(new ContactNotFoundException(999L)).when(addressBook).deleteContact(anyLong());
        mockMvc.perform(delete("/api/contacts/999"))
                .andExpect(status().isNotFound());
    }
} 