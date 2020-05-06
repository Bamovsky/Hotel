package ua.nure.cherkashyn.hotel.web.command.user;

import org.apache.log4j.Logger;
import ua.nure.cherkashyn.hotel.db.DAOFactory;
import ua.nure.cherkashyn.hotel.db.dao.ApartmentDAO;
import ua.nure.cherkashyn.hotel.db.entity.Apartment;
import ua.nure.cherkashyn.hotel.exception.AppException;
import ua.nure.cherkashyn.hotel.exception.DBException;
import ua.nure.cherkashyn.hotel.web.WebPath;
import ua.nure.cherkashyn.hotel.web.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;


/**
 * ShowApartmentsContentCommand
 *
 * @author Vladimir Cherkashyn
 */
public class ShowApartmentsContentCommand extends Command {

    private static final Logger LOG = Logger.getLogger(ShowApartmentsContentCommand.class);

    private final int HOW_MANY = 6;

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException {

        int offset = 0;
        int quantityOfApartments = 0;

        LocalDate arrivalDate = null;
        LocalDate departureDate = null;
        LocalDate currentDate = LocalDate.now();



        try {
            arrivalDate = LocalDate.parse(req.getParameter(("arrivalDate")));
            departureDate = LocalDate.parse(req.getParameter(("departureDate")));
        } catch (DateTimeParseException e) {
            req.setAttribute("dataError", "Заполнителя поля с датами");
            return WebPath.PAGE_APARTMENTS_CONTENT;
        }


        if (arrivalDate.compareTo(departureDate) > 0) {
            req.setAttribute("dataError", "Дата заезда не может быть раньше даты выезда");
            return WebPath.PAGE_APARTMENTS_CONTENT;
        } else if (arrivalDate.compareTo(departureDate) == 0) {
            req.setAttribute("dataError", "Дата заезда не может быть равна дате выезда");
            return WebPath.PAGE_APARTMENTS_CONTENT;
        } else if (currentDate.compareTo(arrivalDate) >= 0) {
            req.setAttribute("dataError", "Дата заезда не может быть раньше чем завтр");
            return WebPath.PAGE_APARTMENTS_CONTENT;
        }


        long statusId = 0;
        long classId = 0;
        int quantityOfRooms = 0;
        boolean orderByASC = false;

        BigDecimal priceFrom = null;
        BigDecimal priceUntil = null;

        Locale locale = Locale.forLanguageTag((String) req.getSession().getAttribute("locale"));

        try {
            offset = Integer.parseInt(req.getParameter("offset").trim());
            statusId = Long.parseLong(req.getParameter("statusId").trim());
            classId = Long.parseLong(req.getParameter("classId").trim());
            quantityOfRooms = Integer.parseInt(req.getParameter("quantityOfRooms").trim());
            priceFrom = BigDecimal.valueOf(Double.parseDouble(req.getParameter("priceFrom").trim()));
            priceUntil = BigDecimal.valueOf(Double.parseDouble(req.getParameter("priceUntil").trim()));
            orderByASC = Boolean.parseBoolean(req.getParameter("orderByASC").trim());
        } catch (NullPointerException e) {
            throw new AppException("No param approvedToken");
        }

        ApartmentDAO dao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getApartmentDAO();
        List<Apartment> apartments = null;
        try {
            if (statusId == 0) {
                apartments = dao.getFreeApartments(offset, HOW_MANY, classId, quantityOfRooms, priceFrom, priceUntil, orderByASC, arrivalDate, departureDate, locale);
                quantityOfApartments = dao.getQuantityOfApartmentsWithFreeStatus(classId, quantityOfRooms, priceFrom, priceUntil, arrivalDate, departureDate);
            }
            if (statusId == 1) {
                apartments = dao.getBookedApartments(offset, HOW_MANY, classId, quantityOfRooms, priceFrom, priceUntil, orderByASC, arrivalDate, departureDate, locale);
                quantityOfApartments = dao.getQuantityOfApartmentsWithBookedStatus(classId, quantityOfRooms, priceFrom, priceUntil, arrivalDate, departureDate);
            }
            if (statusId == 2) {
                apartments = dao.getOccupiedApartments(offset, HOW_MANY, classId, quantityOfRooms, priceFrom, priceUntil, orderByASC, arrivalDate, departureDate, locale);
                quantityOfApartments = dao.getQuantityOfApartmentsWithOccupiedStatus(classId, quantityOfRooms, priceFrom, priceUntil, arrivalDate, departureDate);
            }
        } catch (DBException e) {
            throw new AppException("No param approvedToken");
        }


        req.setAttribute("apartments", apartments);
        req.setAttribute("offset", offset);
        req.setAttribute("quantityOfPages", Math.ceil((double) quantityOfApartments / HOW_MANY));

        return WebPath.PAGE_APARTMENTS_CONTENT;
    }
}
