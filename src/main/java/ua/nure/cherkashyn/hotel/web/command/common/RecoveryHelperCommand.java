package ua.nure.cherkashyn.hotel.web.command.common;

import org.apache.log4j.Logger;
import ua.nure.cherkashyn.hotel.web.WebPath;
import ua.nure.cherkashyn.hotel.web.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * RecoveryHelperCommand
 *
 * @author Vladimir Cherkashyn
 */
public class RecoveryHelperCommand extends Command {
    private static final long serialVersionUID = 4919651873454406565L;

    private static final Logger LOG = Logger.getLogger(RecoveryCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("approved", true);
        req.setAttribute("title", "Recovery");
        req.setAttribute("message", "Your password has been changed");
        return WebPath.PAGE_LOGIN;
    }
}
