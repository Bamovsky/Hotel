package ua.nure.cherkashyn.hotel.web.command.common;

import org.apache.log4j.Logger;
import ua.nure.cherkashyn.hotel.exception.AppException;
import ua.nure.cherkashyn.hotel.web.WebPath;
import ua.nure.cherkashyn.hotel.web.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.TreeMap;


/**
 * ChangeLocaleCommand
 *
 * @author Vladimir Cherkashyn
 */
public class ChangeLocaleCommand extends Command {

    private static final Logger LOG = Logger.getLogger(ChangeLocaleCommand.class);

    private static Map<String, String> links = new TreeMap<>();

    static {
        links.put("login", WebPath.PAGE_LOGIN);
        links.put("apartments", WebPath.COMMAND_SHOW_APARTMENTS);
        links.put("index", WebPath.PAGE_INDEX);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException {

        LOG.debug("#exucte ChangeLocaleCommand");

        String newLocale;
        String linkToForward;

        try {
            newLocale = req.getParameter("newLocale").trim();
            linkToForward = req.getParameter("linkToForward").trim();
        } catch (NullPointerException e) {
            throw new AppException("No param newLocale or linkToForward");
        }

        req.getSession().setAttribute("locale", newLocale);

        return links.get(linkToForward);
    }
}
