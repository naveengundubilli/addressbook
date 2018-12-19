package code.restful.addressbook.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST)
public class InvalidDetailsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidDetailsException(String message) {
		super(message);
	}
}
