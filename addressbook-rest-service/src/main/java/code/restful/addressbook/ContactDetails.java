package code.restful.addressbook;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Contact information for the address book")
public interface ContactDetails {
    Long getId();
    void setId(Long id);
    
    String getFirstName();
    void setFirstName(String firstName);
    
    String getLastName();
    void setLastName(String lastName);
    
    String getEmail();
    void setEmail(String email);
    
    String getPhoneNumber();
    void setPhoneNumber(String phoneNumber);
    
    String getAddress();
    void setAddress(String address);
} 