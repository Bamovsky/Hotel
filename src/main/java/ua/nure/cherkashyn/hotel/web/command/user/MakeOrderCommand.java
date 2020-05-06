package ua.nure.cherkashyn.hotel.web.command.user;


import org.apache.log4j.Logger;
import ua.nure.cherkashyn.hotel.db.DAOFactory;
import ua.nure.cherkashyn.hotel.db.dao.OrdersDAO;
import ua.nure.cherkashyn.hotel.db.entity.Order;
import ua.nure.cherkashyn.hotel.db.entity.User;
import ua.nure.cherkashyn.hotel.exception.AppException;
import ua.nure.cherkashyn.hotel.exception.DBException;
import ua.nure.cherkashyn.hotel.web.WebPath;
import ua.nure.cherkashyn.hotel.web.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;


/**
 * MakeOrderCommand
 *
 * @author Vladimir Cherkashyn
 */
public class MakeOrderCommand extends Command {

    private static final Logger LOG = Logger.getLogger(MakeOrderCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException {

        LocalDate orderDate = LocalDate.now();
        long apartmentClassId;
        int numberOfRooms;
        LocalDate arrivalDate;
        LocalDate departureDate;

        Order order = new Order();

        User user = (User) req.getSession().getAttribute("user");
        OrdersDAO dao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getOrdersDAO();

        try {
            apartmentClassId = Long.parseLong( req.getParameter("apartmentClass").trim());
            LOG.debug("apartmentClassId " + apartmentClassId);
            numberOfRooms = Integer.parseInt(req.getParameter("numberOfRooms").trim());
            LOG.debug("numberOfRooms " + numberOfRooms);
            arrivalDate = LocalDate.parse(req.getParameter(("arrivalDate")));
            LOG.debug("arrivalDate " + arrivalDate);
            departureDate = LocalDate.parse(req.getParameter(("departureDate")));
            LOG.debug("departureDate " + departureDate);
        } catch (DateTimeParseException e) {
            req.setAttribute("approved", true);
            req.setAttribute("title", "Error");
            req.setAttribute("message", "Поля даты заезда и выезда не заполнены");
            return WebPath.PAGE_INDEX;
        }


        if(arrivalDate.compareTo(departureDate) > 0) {
            req.setAttribute("approved", true);
            req.setAttribute("title", "Error");
            req.setAttribute("message", "Дата заезда не может быть раньше даты выезда");
            return WebPath.PAGE_INDEX;
        } else if (arrivalDate.compareTo(departureDate) == 0) {
            req.setAttribute("approved", true);
            req.setAttribute("title", "Error");
            req.setAttribute("message", "Дата заезда не может быть равна дате выезда");
            return WebPath.PAGE_INDEX;
        } else if(orderDate.compareTo(arrivalDate) >= 0){
            req.setAttribute("approved", true);
            req.setAttribute("title", "Error");
            req.setAttribute("message", "Дата заезда не может быть раньше чем завтра");
            return WebPath.PAGE_INDEX;
        }

        order.setOrderDate(orderDate);
        order.setNumberOfRooms(numberOfRooms);
        order.setArrivalDate(arrivalDate);
        order.setDepartureDate(departureDate);
        order.setClassId(apartmentClassId);

        LOG.debug("order " + order);

        try {
            dao.makeOrder(order, user);
        } catch (DBException e) {
            throw new AppException("Can't make an order");
        }

        return WebPath.PAGE_INDEX;
    }
}
