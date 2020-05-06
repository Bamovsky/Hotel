package ua.nure.cherkashyn.hotel.mail;


/**
 * Holder for messages
 *
 * @author Vladimir Cherkashyn
 */
public class MailMessages {

    private MailMessages() {
        // utility class
    }

    public static final String MESSAGE_TITLE_REGISTER = "Registration";
    public static final String MESSAGE_TITLE_RECOVERY = "Recovery";
    public static final String MESSAGE_TITLE_BOOKING = "Booking";

    public static final String MESSAGE_TEXT_REGISTER = "http://localhost:8080/hotel/controller?command=approve&approvedToken=";
    public static final String MESSAGE_TEXT_RECOVERY = "http://localhost:8080/hotel/controller?command=recovery&approvedToken=";

}
