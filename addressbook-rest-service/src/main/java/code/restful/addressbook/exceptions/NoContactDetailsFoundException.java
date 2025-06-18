package code.restful.addressbook.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No Contact Details found in Directory.")
public class NoContactDetailsFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoContactDetailsFoundException(String message) {
		super(message);
	}

}
