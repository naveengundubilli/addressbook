package code.restful.exception;

public class DuplicateContactException extends RuntimeException {
    public DuplicateContactException(String message) {
        super(message);
    }

    public DuplicateContactException(String firstName, String lastName) {
        super("Contact already exists with name: " + firstName + " " + lastName);
    }
} 