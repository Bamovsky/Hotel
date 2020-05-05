package ua.nure.cherkashyn.hotel.web.service;

public final class ServiceConstants {

    private ServiceConstants() {

    }

    // validation constants
    public static final String EMAIL_VALIDATION_REG_EXP = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
    public static final String PASSWORD_VALIDATION_REG_EXP = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

    // common messages
    public static final String EMAIL_INVALID = "Email is't valid";
    public static final String PASSWORD_INVALID = "Password must consist minimum eight characters, at least one letter and one number";
    public static final String EMAIL_OK = "Email is OK";
    public static final String PASSWORD_OK = "Password is OK";


    // authorizeUser messages
    public static final String AUTHORIZE_USER_NOT_FOUND_IN_DATABASE_EMAIL = "User with this email unregistered or not approved";
    public static final String AUTHORIZE_USER_NOT_FOUND_IN_DATABASE_PASSWORD = "Password was't checked";

    public static final String AUTHORIZE_EMAIL_OK = "Email is OK";
    public static final String AUTHORIZE_PASSWORD_INVALID = "Password isn't correct";

    // registerUser messages
    public static final String REGISTER_SUCCESS = "User successfully was registered. Envelope was sent on that email";
    public static final String REGISTER_FAIL = "User with this email is registered";

    // recoveryPassword messages
    public static final String RECOVERY_SUCCESS = "The link for recovery password was sent on that email";
    public static final String RECOVERY_FAIL = "User with this mail is unregistered or not approved";

    // exchangePassword messages
    public static final String EXCHANGE_PASSWORD_NOT_VALID = "Passwords are't valid";
    public static final String EXCHANGE_PASSWORDS_NOT_EQUAL = "Passwords not equal";
    public static final String EXCHANGE_USER_WITH_APPROVED_TOKEN_NOT_FOUND = "User with this approvedToken not found";

    // Approved token
    public static final String ALGORITHM = "SHA-512";
    public static final String SALT = "VLADIMIR_CHERKASHYN";


}


