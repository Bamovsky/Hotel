package ua.nure.cherkashyn.hotel.web;

import org.apache.log4j.Logger;
import ua.nure.cherkashyn.hotel.exception.AppException;
import ua.nure.cherkashyn.hotel.web.command.Command;
import ua.nure.cherkashyn.hotel.web.command.CommandContainer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * The main controller that proses all command
 *
 * @author Vladimir Cherkashyn
 */
@WebServlet("/controller")
public class Controller extends HttpServlet {

    private static final long serialVersionUID = 6693137329750013149L;

    private static final Logger LOG = Logger.getLogger(Controller.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.debug("Controller starts");

        String commandName = req.getParameter("command");
        LOG.trace("Request parameter: command --> " + commandName);

        Command command = CommandContainer.get(commandName);
        LOG.trace("Obtained command --> " + command);

        String forward = WebPath.PAGE_ERROR_PAGE;
        try {
            forward = command.execute(req, resp);
        } catch (AppException ex) {
            LOG.debug("err message " + ex.getMessage());
            req.setAttribute("errorMessage", ex.getMessage());
        }
        LOG.trace("Forward address --> " + forward);

        LOG.debug("Controller finished, now go to forward address --> " + forward);

        req.getRequestDispatcher(forward).forward(req, resp);
    }
}
