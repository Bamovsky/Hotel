package ua.nure.cherkashyn.hotel.db.mysql;

import org.apache.log4j.Logger;
import ua.nure.cherkashyn.hotel.db.DBUtils;
import ua.nure.cherkashyn.hotel.db.Fields;
import ua.nure.cherkashyn.hotel.db.MySqlDAOFactory;
import ua.nure.cherkashyn.hotel.db.dao.OrdersDAO;
import ua.nure.cherkashyn.hotel.db.entity.Booking;
import ua.nure.cherkashyn.hotel.db.entity.Order;
import ua.nure.cherkashyn.hotel.db.entity.User;
import ua.nure.cherkashyn.hotel.exception.DBException;
import ua.nure.cherkashyn.hotel.exception.Messages;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    private static final String SQL_GET_ALL_UNPROCESSED_ORDERS = "SELECT * FROM orders WHERE isProcessed = 0";
    private static final String SQL_GET_USER_EMAIL_BY_ORDER = "SELECT email FROM users WHERE id = ?";


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

    @Override
    public List<Order> getAllUnprocessedOrders() throws DBException {
        List<Order> orders = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = MySqlDAOFactory.createConnection();
            pstmt = con.prepareStatement(SQL_GET_ALL_UNPROCESSED_ORDERS);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                orders.add(extractOrder(rs));
            }
            setForEachOrderUserEmail(orders);
            con.commit();
        } catch (SQLException ex) {
            DBUtils.rollback(con);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            DBUtils.close(con, pstmt, rs);
        }
        return orders;
    }


    private void setForEachOrderUserEmail(List<Order> orders) throws DBException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = MySqlDAOFactory.createConnection();
            pstmt = con.prepareStatement(SQL_GET_USER_EMAIL_BY_ORDER);
            for (Order order : orders) {
                pstmt.setLong(1, order.getUserId());
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    order.setUserEmail(rs.getString(1));
                }
            }
            con.commit();
        } catch (SQLException | DBException ex) {
            DBUtils.rollback(con);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            DBUtils.close(con, pstmt, rs);
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
        order.setOrderDate(LocalDate.parse(rs.getString(Fields.ORDER_DATE)));
        order.setNumberOfRooms(rs.getInt(Fields.ORDER_NUMBER_OF_ROOMS));
        order.setArrivalDate(LocalDate.parse(rs.getString(Fields.ORDER_ARRIVAL_DATE)));
        order.setDepartureDate(LocalDate.parse(rs.getString(Fields.ORDER_DEPARTURE_DATE)));
        order.setProcessed(rs.getBoolean(Fields.ORDER_IS_PROCESSED));
        order.setUserId(rs.getLong(Fields.ORDER_USER_ID));
        order.setClassId(rs.getLong(Fields.ORDER_CLASS_ID));
        return order;
    }
}
