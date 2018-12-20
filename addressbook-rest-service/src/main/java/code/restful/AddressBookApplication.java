package code.restful;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = { "code.restful.addressbook" })
@EnableSwagger2
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = AddressBookController.class)
public class AddressBookApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(AddressBookApplication.class, args);
	}

	@Bean
	public Docket addressBookApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("Address Book").apiInfo(apiInfo()).select()
				.paths(PathSelectors.regex("/addressbook/contactdetails.*")).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Address Book Web Service")
				.description("This is a Address Book Web Service project").version("1.0").build();
	}
}
