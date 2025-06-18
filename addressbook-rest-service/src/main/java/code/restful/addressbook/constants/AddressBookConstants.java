package code.restful.addressbook.constants;

public final class AddressBookConstants {
    private AddressBookConstants() {
        // Private constructor to prevent instantiation
    }

    // API Paths
    public static final String API_BASE_PATH = "/api/v1/contacts";
    
    // Validation Messages
    public static final String ERROR_CONTACT_NOT_FOUND = "Contact not found with ID: %s";
    public static final String ERROR_INVALID_ID_FORMAT = "Invalid ID format: %s";
    public static final String ERROR_ID_MISMATCH = "The contact's ID must match the URI's ID";
    public static final String ERROR_NULL_CONTACT = "Contact details cannot be null";
    
    // Validation Patterns
    public static final String NAME_PATTERN = "^[a-zA-Z\\s-']+$";
    public static final String PHONE_PATTERN = "^\\+?[1-9]\\d{1,14}$";
    public static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    
    // Validation Limits
    public static final int NAME_MIN_LENGTH = 2;
    public static final int NAME_MAX_LENGTH = 50;
    public static final int EMAIL_MAX_LENGTH = 100;
    public static final int ADDRESS_MAX_LENGTH = 200;
    
    // HTTP Headers
    public static final String CONTENT_TYPE_JSON = "application/json";
} 