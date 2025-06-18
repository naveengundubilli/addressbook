package code.restful.exception;

public class InvalidContactException extends RuntimeException {
    public InvalidContactException(String message) {
        super(message);
    }

    public InvalidContactException(String field, String reason) {
        super("Invalid contact: " + field + " - " + reason);
    }
} 