package code.restful.addressbook.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Contact Details already exist.")
public class DuplicateContactDetailsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicateContactDetailsException(String aId) {
		super("Contact Details already exist for '" + aId);
	}
}
