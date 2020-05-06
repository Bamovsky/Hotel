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
import ua.nure.cherkashyn.hotel.web.service.AuthenticationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;


public class ShowBooking extends Command {

    private static final Logger LOG = Logger.getLogger(ShowBooking.class);

    private static final long serialVersionUID = 3342278073175960049L;

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException {

        User user = (User) req.getSession().getAttribute("user");
        BookingDAO dao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getBookingDAO();
        List<Booking> bookings = null;

        try {
            bookings = dao.getAllBookingsByUser(user);

        } catch (DBException e) {

        }

        LOG.debug("bookings" + bookings);

        req.setAttribute("bookings", bookings);
        return WebPath.PAGE_BOOKING;
    }
}

