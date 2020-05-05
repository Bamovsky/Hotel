package ua.nure.cherkashyn.hotel.web.service;


/**
 * AuthenticationServiceOperation.
 * All operation that service support.
 *
 * @author Vladimir Cherkashyn
 */
public enum AuthenticationServiceOperation {
    AUTHORIZE, REGISTER, RECOVERY, EXCHANGE_PASSWORD;

    public String getName() {
        return name().toLowerCase();
    }
}
