package ua.nure.cherkashyn.hotel.web.command.manager;

import ua.nure.cherkashyn.hotel.exception.AppException;
import ua.nure.cherkashyn.hotel.web.WebPath;
import ua.nure.cherkashyn.hotel.web.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShowApartmentManagerCommand extends Command {
    private static final long serialVersionUID = 7472115311496012717L;

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException {


        long orderId;

        try {
            orderId = Long.parseLong(req.getParameter("orderId").trim());
        } catch (NullPointerException e) {
            throw new AppException("No needed params");
        }

        req.setAttribute("orderId", orderId);

        return WebPath.COMMAND_SHOW_APARTMENTS;
    }
}
