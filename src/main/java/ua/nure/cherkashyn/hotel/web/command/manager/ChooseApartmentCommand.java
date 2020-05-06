package ua.nure.cherkashyn.hotel.web.command.manager;


import ua.nure.cherkashyn.hotel.db.DAOFactory;
import ua.nure.cherkashyn.hotel.db.dao.BookingDAO;
import ua.nure.cherkashyn.hotel.db.dao.OrdersDAO;
import ua.nure.cherkashyn.hotel.db.dao.UserDAO;
import ua.nure.cherkashyn.hotel.db.entity.Booking;
import ua.nure.cherkashyn.hotel.db.entity.Order;
import ua.nure.cherkashyn.hotel.exception.AppException;
import ua.nure.cherkashyn.hotel.web.WebPath;
import ua.nure.cherkashyn.hotel.web.command.Command;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;


public class ChooseApartmentCommand extends Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException {


        long orderId;
        long apartmentId;

        try {
            apartmentId = Long.parseLong(req.getParameter("apartmentId").trim());
            orderId = Long.parseLong(req.getParameter("orderId").trim());
        } catch (NullPointerException e) {
            throw new AppException("No needed params");
        }


        OrdersDAO ordersDAO = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getOrdersDAO();
        BookingDAO bookingDAO = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getBookingDAO();


        Order order = ordersDAO.getOrderById(orderId);

        Booking booking = new Booking();
        booking.setBookingDate(LocalDate.now());
        booking.setArrivalDate(order.getArrivalDate());
        booking.setDepartureDate(order.getDepartureDate());
        booking.setApartmentId(apartmentId);
        booking.setUserId(order.getUserId());

        bookingDAO.makeBooking(booking);

        ordersDAO.deleteOrder(order);

        return WebPath.COMMAND_SHOW_ORDERS;
    }
}
