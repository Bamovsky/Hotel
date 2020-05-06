package ua.nure.cherkashyn.hotel.web.command.user;

import ua.nure.cherkashyn.hotel.db.DAOFactory;
import ua.nure.cherkashyn.hotel.db.dao.ApartmentDAO;
import ua.nure.cherkashyn.hotel.db.entity.Role;
import ua.nure.cherkashyn.hotel.db.entity.User;
import ua.nure.cherkashyn.hotel.exception.AppException;
import ua.nure.cherkashyn.hotel.exception.DBException;
import ua.nure.cherkashyn.hotel.web.WebPath;
import ua.nure.cherkashyn.hotel.web.command.Command;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;


/**
 * ShowApartmentsCommand
 *
 * @author Vladimir Cherkashyn
 */
public class ShowApartmentsCommand extends Command {


    private static final long serialVersionUID = 3342278073175960049L;

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException {
        BigDecimal maxPrice = null;
        BigDecimal minPrice = null;


        User user = (User) req.getSession().getAttribute("user");

        if(user != null) {
            if(Role.getRole(user) == Role.MANAGER){
                req.setAttribute("approved", true);
                req.setAttribute("title", "Error");
                req.setAttribute("message", "Неоходимо зайти в мои заказы и выбрать оттуда");
                return WebPath.PAGE_INDEX;
            }
        }


        ApartmentDAO dao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getApartmentDAO();
        try {
            maxPrice = dao.getMaxApartmentPrice();
            minPrice = dao.getMinApartmentPrice();
        } catch (DBException e) {
            throw new AppException("No params minPrice or maxPrice");
        }

        req.setAttribute("maxPrice", maxPrice);
        req.setAttribute("minPrice", minPrice);

        return WebPath.PAGE_APARTMENTS;
    }
}
