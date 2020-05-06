package ua.nure.cherkashyn.hotel.web.service;

public final class ServiceConstants {

    private ServiceConstants() {
        // it's util class
    }

    // validation constants
    public static final String EMAIL_VALIDATION_REG_EXP = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
    public static final String PASSWORD_VALIDATION_REG_EXP = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

    // Approved token
    public static final String ALGORITHM = "SHA-512";
    public static final String SALT = "VLADIMIR_CHERKASHYN";


}


