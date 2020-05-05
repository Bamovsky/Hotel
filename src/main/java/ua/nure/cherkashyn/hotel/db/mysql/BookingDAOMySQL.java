package ua.nure.cherkashyn.hotel.db.mysql;

import org.apache.log4j.Logger;
import ua.nure.cherkashyn.hotel.db.DBUtils;
import ua.nure.cherkashyn.hotel.db.MySqlDAOFactory;
import ua.nure.cherkashyn.hotel.db.dao.BookingDAO;
import ua.nure.cherkashyn.hotel.db.entity.Booking;
import ua.nure.cherkashyn.hotel.exception.DBException;
import ua.nure.cherkashyn.hotel.exception.Messages;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;


/**
 * Implementation of BookingDAO for MySQL database.
 *
 * @author Vladimir Cherkashyn
 */
public class BookingDAOMySQL implements BookingDAO {

    private static final Logger LOG = Logger.getLogger(BookingDAOMySQL.class);


    // //////////////////////////////////////////////////////////
    // SQL queries
    // //////////////////////////////////////////////////////////

    private static final String SQL_MAKE_BOOKING = "INSERT INTO booking (bookingDate, arrivalDate, departureDate, isPaid, users_id, apartment_id, status_id) VALUES (?,?,?,0,?,?,1)";


    /**
     * Create a booking in db
     *
     * @param booking
     */
    @Override
    public void makeBooking(Booking booking) throws DBException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = MySqlDAOFactory.createConnection();
            pstmt = con.prepareStatement(SQL_MAKE_BOOKING);
            pstmt.setString(1, booking.getBookingDate().format(formatter));
            pstmt.setString(2, booking.getArrivalDate().format(formatter));
            pstmt.setString(3, booking.getDepartureDate().format(formatter));
            pstmt.setLong(4, booking.getUserId());
            pstmt.setLong(5, booking.getApartmentId());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException | DBException ex) {
            DBUtils.rollback(con);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            DBUtils.close(pstmt);
            DBUtils.close(con);
        }
    }


}
