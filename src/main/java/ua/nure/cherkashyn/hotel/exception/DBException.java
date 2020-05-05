package ua.nure.cherkashyn.hotel.exception;


/**
 * DBException
 *
 * @author Vladimir Cherkashyn
 */
public class DBException extends AppException {

    private static final long serialVersionUID = -3550446897536410392L;

    public DBException() {
        super();
    }

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }

}