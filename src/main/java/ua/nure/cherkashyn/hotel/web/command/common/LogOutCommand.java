package ua.nure.cherkashyn.hotel.web.command.common;


import ua.nure.cherkashyn.hotel.web.WebPath;
import ua.nure.cherkashyn.hotel.web.command.Command;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * LogOutCommand
 *
 * @author Vladimir Cherkashyn
 */
public class LogOutCommand extends Command {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().invalidate();
        return WebPath.PAGE_INDEX;
    }
}
