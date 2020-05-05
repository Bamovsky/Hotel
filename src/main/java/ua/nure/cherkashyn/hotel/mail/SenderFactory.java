package ua.nure.cherkashyn.hotel.mail;


/**
 * SenderFactory.
 * Implementation of pattern Factory Method.
 * This class gets Factory of available implementations.
 *
 * @author Vladimir Cherkashyn
 */
public class SenderFactory {
    public static final int GOOGLE_MAIL_TLS = 1;

    public static Sender getSender(int whichSender) {

        switch (whichSender) {
            case GOOGLE_MAIL_TLS:
                return new GoogleMailSenderTLS(GoogleMailSenderConstants.LOGIN, GoogleMailSenderConstants.PASSWORD);
            default:
                return null;
        }
    }
}
