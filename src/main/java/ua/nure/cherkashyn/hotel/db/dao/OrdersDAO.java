package ua.nure.cherkashyn.hotel.db.dao;

import ua.nure.cherkashyn.hotel.db.entity.Order;
import ua.nure.cherkashyn.hotel.db.entity.User;
import ua.nure.cherkashyn.hotel.exception.DBException;

/**
 * Interface OrdersDAO.
 *
 * @author Vladimir Cherkashyn
 */
public interface OrdersDAO {
    void makeOrder(Order order, User user) throws DBException;
}
