package ua.nure.cherkashyn.hotel.web.command.manager;

import ua.nure.cherkashyn.hotel.exception.AppException;
import ua.nure.cherkashyn.hotel.web.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShowOrdersCommand extends Command {

    private static final long serialVersionUID = 7011087953138898282L;

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException, AppException {
        // will be done for evening 05.05
        return null;
    }
}
