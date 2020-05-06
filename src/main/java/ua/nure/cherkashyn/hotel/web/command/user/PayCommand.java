package ua.nure.cherkashyn.hotel.web.command.user;

import org.apache.log4j.Logger;
import ua.nure.cherkashyn.hotel.db.DAOFactory;
import ua.nure.cherkashyn.hotel.db.dao.BookingDAO;
import ua.nure.cherkashyn.hotel.db.entity.Booking;
import ua.nure.cherkashyn.hotel.db.entity.User;
import ua.nure.cherkashyn.hotel.exception.AppException;
import ua.nure.cherkashyn.hotel.exception.DBException;
import ua.nure.cherkashyn.hotel.web.WebPath;
import ua.nure.cherkashyn.hotel.web.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PayCommand extends Command {
    private static final long serialVersionUID = 5249843439625304371L;

    private static final Logger LOG = Logger.getLogger(PayCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException, AppException {

        User user = (User) req.getSession().getAttribute("user");
        BookingDAO dao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getBookingDAO();
        Booking booking = null;
        long bookingId;

        try {
            bookingId = Long.parseLong(req.getParameter("bookingId").trim());
        } catch (NullPointerException e) {
            throw new AppException("No param bookingId");
        }

        try {
            booking = dao.getBookingById(bookingId);
            dao.payForBooking(booking);
        } catch (DBException e) {

        }


        return WebPath.COMMAND_SHOW_BOOKINGS;
    }
}
