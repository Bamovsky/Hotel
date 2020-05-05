package ua.nure.cherkashyn.hotel.web.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import ua.nure.cherkashyn.hotel.db.DAOFactory;
import ua.nure.cherkashyn.hotel.db.dao.BookingDAO;
import ua.nure.cherkashyn.hotel.db.dao.OrdersDAO;
import ua.nure.cherkashyn.hotel.db.entity.Booking;
import ua.nure.cherkashyn.hotel.db.entity.User;
import ua.nure.cherkashyn.hotel.exception.DBException;
import ua.nure.cherkashyn.hotel.web.WebPath;
import ua.nure.cherkashyn.hotel.web.service.entity.BookingServiceEntity;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;


@WebServlet("/book")
public class BookService extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(BookService.class);

    private Gson gson = null;


    @Override
    public void init() {
        gson = new GsonBuilder().disableHtmlEscaping().create();
    }

    /**
     * What service will do, if client send post request.
     * return JSON.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        LocalDate bookingDate = LocalDate.now();
        LocalDate arrivalDate = null;
        LocalDate departureDate = null;
        long apartmentId = 0;

        BookingServiceEntity bookingServiceEntity = new BookingServiceEntity();
        Booking booking = new Booking();
        User user = (User) req.getSession().getAttribute("user");

        if (user == null) {
            bookingServiceEntity.setTitle("Ошибка");
            bookingServiceEntity.setMessage("Необходимо войти в систему");
            bookingServiceEntity.setError(true);
        } else {

            try {
                arrivalDate = LocalDate.parse(req.getParameter(("arrivalDate")));
                departureDate = LocalDate.parse(req.getParameter(("departureDate")));
                apartmentId = Long.parseLong(req.getParameter("apartmentId"));
            } catch (DateTimeParseException e) {
                bookingServiceEntity.setTitle("Ошибка");
                bookingServiceEntity.setMessage("Поля даты заезда и выезда не заполнены");
                bookingServiceEntity.setError(true);
            }

            if (arrivalDate.compareTo(departureDate) > 0) {
                bookingServiceEntity.setTitle("Ошибка");
                bookingServiceEntity.setMessage("Дата заезда не может быть раньше даты выезда");
                req.setAttribute("message", "Дата заезда не может быть раньше даты выезда");
                bookingServiceEntity.setError(true);
            } else if (arrivalDate.compareTo(departureDate) == 0) {
                bookingServiceEntity.setTitle("Ошибка");
                bookingServiceEntity.setMessage("Дата заезда не может быть равна дате выезда");
                bookingServiceEntity.setError(true);
            } else if (bookingDate.compareTo(arrivalDate) >= 0) {
                bookingServiceEntity.setTitle("Ошибка");
                bookingServiceEntity.setMessage("Дата заезда не может быть раньше чем завтра");
                bookingServiceEntity.setError(true);
            }


            if (!bookingServiceEntity.isError()) {
                booking.setBookingDate(bookingDate);
                booking.setArrivalDate(arrivalDate);
                booking.setDepartureDate(departureDate);
                booking.setApartmentId(apartmentId);
                booking.setUserId(user.getId());

                BookingDAO dao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getBookingDAO();

                try {
                    dao.makeBooking(booking);
                    bookingServiceEntity.setTitle("ОК");
                    bookingServiceEntity.setMessage("Бронирование успешно выполнено");

                } catch (DBException e) {

                }
            }

        }


        String checkerJson = gson.toJson(bookingServiceEntity);
        OutputStream out = resp.getOutputStream();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        byte[] utf8JsonString = checkerJson.getBytes("UTF8");
        out.write(utf8JsonString, 0, utf8JsonString.length);
        out.flush();
        LOG.debug("6");
    }


}
