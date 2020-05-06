package ua.nure.cherkashyn.hotel.db.dao;

import ua.nure.cherkashyn.hotel.db.entity.Booking;
import ua.nure.cherkashyn.hotel.db.entity.User;
import ua.nure.cherkashyn.hotel.exception.DBException;

import java.util.List;


/**
 * Interface BookingDAO.
 *
 * @author Vladimir Cherkashyn
 */
public interface BookingDAO {
    void makeBooking(Booking booking) throws DBException;

    List<Booking> getAllBookingsByUser(User user) throws DBException;

    Booking getBookingById(long id) throws DBException;

    void payForBooking(Booking booking) throws DBException;
}
