package ua.nure.cherkashyn.hotel.db.mysql;

import org.apache.log4j.Logger;
import ua.nure.cherkashyn.hotel.db.DBUtils;
import ua.nure.cherkashyn.hotel.db.Fields;
import ua.nure.cherkashyn.hotel.db.MySqlDAOFactory;
import ua.nure.cherkashyn.hotel.db.dao.BookingDAO;
import ua.nure.cherkashyn.hotel.db.entity.Apartment;
import ua.nure.cherkashyn.hotel.db.entity.Booking;
import ua.nure.cherkashyn.hotel.db.entity.User;
import ua.nure.cherkashyn.hotel.exception.DBException;
import ua.nure.cherkashyn.hotel.exception.Messages;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


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
    private static final String SQL_GET_ALL_BOOKINGS_FOR_USER = "SELECT * FROM booking where users_id = ? ORDER BY arrivalDate";
    private static final String SQL_PAY_FOR_BOOKING = "UPDATE booking SET isPaid = 1, status_id = 2  WHERE id = ?";
    private static final String SQL_GET_BOOKING_BY_ID = "SELECT * FROM booking WHERE id = ?";


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


    @Override
    public List<Booking> getAllBookingsByUser(User user) throws DBException {
        List<Booking> bookings = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = MySqlDAOFactory.createConnection();
            pstmt = con.prepareStatement(SQL_GET_ALL_BOOKINGS_FOR_USER);
            pstmt.setLong(1, user.getId());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                bookings.add(extractBooking(rs));
            }
            con.commit();
        } catch (SQLException ex) {
            DBUtils.rollback(con);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            DBUtils.close(con, pstmt, rs);
        }
        return bookings;
    }

    @Override
    public Booking getBookingById(long id) throws DBException {
        Booking booking = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = MySqlDAOFactory.createConnection();
            pstmt = con.prepareStatement(SQL_GET_BOOKING_BY_ID);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                booking = extractBooking(rs);
            }
            con.commit();
        } catch (SQLException ex) {
            DBUtils.rollback(con);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            DBUtils.close(con, pstmt, rs);
        }
        return booking;
    }

    @Override
    public void payForBooking(Booking booking) throws DBException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = MySqlDAOFactory.createConnection();
            pstmt = con.prepareStatement(SQL_PAY_FOR_BOOKING);
            pstmt.setLong(1, booking.getId());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException ex) {
            DBUtils.rollback(con);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            DBUtils.close(con, pstmt, rs);
        }
    }


    /**
     * Extract a booking from result set.
     *
     * @param rs ResultSet.
     */
    private Booking extractBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getLong(Fields.ENTITY_ID));
        booking.setBookingDate(LocalDate.parse(rs.getString(Fields.BOOKING_DATE)));
        booking.setArrivalDate(LocalDate.parse(rs.getString(Fields.BOOKING_ARRIVAL_DATE)));
        booking.setDepartureDate(LocalDate.parse(rs.getString(Fields.BOOKING_DEPARTURE_DATE)));
        booking.setApartmentId(rs.getLong(Fields.BOOKING_APARTMENT_ID));
        booking.setUserId(rs.getLong(Fields.BOOKING_USER_ID));
        booking.setPaid(rs.getBoolean(Fields.BOOKING_IS_PAID));
        booking.setStatusId(rs.getLong(Fields.BOOKING_STATUS_ID));
        return booking;
    }


}
