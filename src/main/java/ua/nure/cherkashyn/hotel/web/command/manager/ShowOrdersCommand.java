package ua.nure.cherkashyn.hotel.web.command.manager;

import ua.nure.cherkashyn.hotel.db.DAOFactory;
import ua.nure.cherkashyn.hotel.db.dao.BookingDAO;
import ua.nure.cherkashyn.hotel.db.dao.OrdersDAO;
import ua.nure.cherkashyn.hotel.db.entity.Booking;
import ua.nure.cherkashyn.hotel.db.entity.Order;
import ua.nure.cherkashyn.hotel.db.entity.User;
import ua.nure.cherkashyn.hotel.exception.AppException;
import ua.nure.cherkashyn.hotel.exception.DBException;
import ua.nure.cherkashyn.hotel.web.WebPath;
import ua.nure.cherkashyn.hotel.web.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ShowOrdersCommand extends Command {

    private static final long serialVersionUID = 7011087953138898282L;

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException, AppException {

        OrdersDAO dao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getOrdersDAO();
        List<Order> orders = null;

        try {
            orders = dao.getAllUnprocessedOrders();

        } catch (DBException e) {

        }


        req.setAttribute("orders", orders);

        return WebPath.PAGE_ORDERS;
    }
}
