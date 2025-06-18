package code.restful;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

import java.util.List;

import code.restful.persistence.addressbook.InMemoryAddressBook;
import code.restful.addressbook.AddressBook;

@SpringBootApplication
@ComponentScan(basePackages = "code.restful")
public class AddressBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(AddressBookApplication.class, args);
	}
	
	@Bean
	@Primary
	public AddressBook addressBook() {
		return new InMemoryAddressBook();
	}
}
