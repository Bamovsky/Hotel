package ua.nure.cherkashyn.hotel.mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


/**
 * GoogleMailSenderTLS implementation of Sender
 *
 * @author Vladimir Cherkashyn
 */
public class GoogleMailSenderTLS implements Sender {
    private String username;
    private String password;
    private Properties props;

    public GoogleMailSenderTLS(String username, String password) {
        this.username = username;
        this.password = password;
        props = new Properties();

        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
    }


    /**
     * Send a message
     */
    public void send(String title, String text, String toWhom) {
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            // From whom
            message.setFrom(new InternetAddress(username));
            // To whom
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toWhom));
            // Title
            message.setSubject(title);
            // Message
            message.setText(text);
            // Send message
            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
