package ua.nure.cherkashyn.hotel.exception;

/**
 * Holder for messages of exceptions.
 *
 * @author Vladimir Cherkashyn
 */
public class Messages {

    private Messages() {
        // util class
    }


    public static final String ERR_CANNOT_OBTAIN_CONNECTION = "Cannot obtain a connection from the pool";

    public static final String ERR_CANNOT_OBTAIN_USER_BY_LOGIN = "Cannot obtain a user by its login";

    public static final String ERR_CANNOT_CLOSE_CONNECTION = "Cannot close a connection";

    public static final String ERR_CANNOT_CLOSE_RESULTSET = "Cannot close a result set";

    public static final String ERR_CANNOT_CLOSE_STATEMENT = "Cannot close a statement";

    public static final String ERR_CANNOT_OBTAIN_DATA_SOURCE = "Cannot obtain the data source";






    public static final String ERR_NO_SUCH_ALGORITHM = "Algorithm not found";

}