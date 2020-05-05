package ua.nure.cherkashyn.hotel.mail;


/**
 * Interface Sender.
 *
 * @author Vladimir Cherkashyn
 */
public interface Sender {
    void send(String title, String message, String toWhom);
}
