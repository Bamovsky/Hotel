package ua.nure.cherkashyn.hotel.db.dao;

import ua.nure.cherkashyn.hotel.db.entity.Booking;
import ua.nure.cherkashyn.hotel.exception.DBException;


/**
 * Interface BookingDAO.
 *
 * @author Vladimir Cherkashyn
 */
public interface BookingDAO {
    void makeBooking(Booking booking) throws DBException;
}
