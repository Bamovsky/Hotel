package ua.nure.cherkashyn.hotel.db;

import org.apache.log4j.Logger;
import ua.nure.cherkashyn.hotel.db.dao.ApartmentDAO;
import ua.nure.cherkashyn.hotel.db.dao.BookingDAO;
import ua.nure.cherkashyn.hotel.db.dao.OrdersDAO;
import ua.nure.cherkashyn.hotel.db.dao.UserDAO;
import ua.nure.cherkashyn.hotel.db.mysql.ApartmentDAOMySQL;
import ua.nure.cherkashyn.hotel.db.mysql.BookingDAOMySQL;
import ua.nure.cherkashyn.hotel.db.mysql.OrdersDAOMySQL;
import ua.nure.cherkashyn.hotel.db.mysql.UserDAOMySQL;
import ua.nure.cherkashyn.hotel.exception.DBException;
import ua.nure.cherkashyn.hotel.exception.Messages;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * Implementation of pattern AbstractFactory.
 *
 * @author Vladimir Cherkashyn
 */
public class MySqlDAOFactory extends DAOFactory {

    private static DataSource ds;

    private static final Logger LOG = Logger.getLogger(MySqlDAOFactory.class);


    /**
     * Initialization of connection pool.
     */
    static {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            ds = (DataSource) envContext.lookup("jdbc/hotel");
            LOG.debug("MySqlDAOFactory was successfully initialized");
        } catch (NamingException e) {
            LOG.error(Messages.ERR_CANNOT_OBTAIN_DATA_SOURCE, e);
        }
    }


    /**
     * Get a connection from pool.
     */
    public static Connection createConnection() throws DBException {
        Connection con = null;
        try {
            con = ds.getConnection();
        } catch (SQLException ex) {
            LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, ex);
        }
        return con;
    }


    /**
     * Implementation of UserDAO
     */
    public UserDAO getUserDAO() {
        return new UserDAOMySQL();
    }


    /**
     * Implementation of ApartmentDAO
     */
    public ApartmentDAO getApartmentDAO() {
        return new ApartmentDAOMySQL();
    }

    /**
     * Implementation of OrdersDAO
     */
    public OrdersDAO getOrdersDAO() {
        return new OrdersDAOMySQL();
    }

    /**
     * Implementation of BookingDAO
     */
    public BookingDAO getBookingDAO() {
        return new BookingDAOMySQL();
    }


}
