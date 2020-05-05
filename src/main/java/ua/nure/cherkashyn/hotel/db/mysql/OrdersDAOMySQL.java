package ua.nure.cherkashyn.hotel.db.mysql;

import org.apache.log4j.Logger;
import ua.nure.cherkashyn.hotel.db.DBUtils;
import ua.nure.cherkashyn.hotel.db.Fields;
import ua.nure.cherkashyn.hotel.db.MySqlDAOFactory;
import ua.nure.cherkashyn.hotel.db.dao.OrdersDAO;
import ua.nure.cherkashyn.hotel.db.entity.Order;
import ua.nure.cherkashyn.hotel.db.entity.User;
import ua.nure.cherkashyn.hotel.exception.DBException;
import ua.nure.cherkashyn.hotel.exception.Messages;

import java.sql.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Implementation of OrdersDAO for MySQL database.
 *
 * @author Vladimir Cherkashyn
 */
public class OrdersDAOMySQL implements OrdersDAO {

    private static final Logger LOG = Logger.getLogger(OrdersDAOMySQL.class);

    // //////////////////////////////////////////////////////////
    // SQL queries
    // //////////////////////////////////////////////////////////
    private static final String SQL_MAKE_ORDER = "INSERT INTO orders (orderDate, numberOfRooms, arrivalDate, departureDate, isProcessed, users_id, class_id, apartment_id) VALUES (?,?,?,?,0,?,?,null)";


    /**
     * Create an order in db
     *
     * @param order
     * @param user
     */
    @Override
    public void makeOrder(Order order, User user) throws DBException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = MySqlDAOFactory.createConnection();
            pstmt = con.prepareStatement(SQL_MAKE_ORDER);
            pstmt.setString(1, order.getOrderDate().format(formatter));
            pstmt.setInt(2, order.getNumberOfRooms());
            pstmt.setString(3, order.getArrivalDate().format(formatter));
            pstmt.setString(4, order.getDepartureDate().format(formatter));
            pstmt.setLong(5, user.getId());
            pstmt.setLong(6, order.getClassId());
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


    /**
     * Extract a user from result set.
     *
     * @param rs ResultSet.
     */
    private Order extractOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong(Fields.ENTITY_ID));
        order.setOrderDate(rs.getDate(Fields.ORDER_DATE).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        order.setNumberOfRooms(rs.getInt(Fields.ORDER_NUMBER_OF_ROOMS));
        order.setArrivalDate(rs.getDate(Fields.ORDER_ARRIVAL_DATE).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        order.setDepartureDate(rs.getDate(Fields.ORDER_DEPARTURE_DATE).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        order.setProcessed(rs.getBoolean(Fields.ORDER_IS_PROCESSED));
        order.setUserId(rs.getLong(Fields.ORDER_USER_ID));
        order.setApartmentId(rs.getLong(Fields.ORDER_APARTMENT_ID));
        order.setClassId(rs.getLong(Fields.ORDER_CLASS_ID));
        return order;
    }
}
