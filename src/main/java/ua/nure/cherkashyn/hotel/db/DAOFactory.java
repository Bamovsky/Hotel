package ua.nure.cherkashyn.hotel.db;

import ua.nure.cherkashyn.hotel.db.dao.ApartmentDAO;
import ua.nure.cherkashyn.hotel.db.dao.BookingDAO;
import ua.nure.cherkashyn.hotel.db.dao.OrdersDAO;
import ua.nure.cherkashyn.hotel.db.dao.UserDAO;


/**
 * DAOFactory.
 * Implementation of pattern Factory Method.
 * This class gets Factory of available implementations.
 *
 * @author Vladimir Cherkashyn
 */
public abstract class DAOFactory {

    public static final int MYSQL = 1;

    public abstract UserDAO getUserDAO();

    public abstract ApartmentDAO getApartmentDAO();

    public abstract OrdersDAO getOrdersDAO();

    public abstract BookingDAO getBookingDAO();

    /**
     * Get a DAO Factory.
     *
     * @param whichFactory for choosing available Factory.
     */
    public static DAOFactory getDAOFactory(int whichFactory) {

        switch (whichFactory) {
            case MYSQL:
                return new MySqlDAOFactory();
            default:
                return null;
        }
    }


}
