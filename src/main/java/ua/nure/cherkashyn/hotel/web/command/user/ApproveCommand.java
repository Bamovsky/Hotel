package ua.nure.cherkashyn.hotel.web.command.user;

import org.apache.log4j.Logger;
import ua.nure.cherkashyn.hotel.db.DAOFactory;
import ua.nure.cherkashyn.hotel.db.dao.UserDAO;
import ua.nure.cherkashyn.hotel.db.entity.User;
import ua.nure.cherkashyn.hotel.exception.AppException;
import ua.nure.cherkashyn.hotel.web.WebPath;
import ua.nure.cherkashyn.hotel.web.command.Command;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command for approving an user
 *
 * @author Vladimir Cherkashyn
 */
public class ApproveCommand extends Command {

    private static final long serialVersionUID = -5599568387891847468L;

    private static final Logger LOG = Logger.getLogger(ApproveCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException {
        String approvedToken;

        try {
            approvedToken = req.getParameter("approvedToken").trim();
        } catch (NullPointerException e) {
            throw new AppException("No param approvedToken");
        }

        LOG.debug("Obtained approved token ==> " + approvedToken);

        UserDAO dao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getUserDAO();
        User user = dao.findUserByApprovedToken(approvedToken);

        if (user != null) {
            dao.approveUser(user);
        } else {
            throw new AppException("user with this token was't found");
        }

        LOG.trace("User was approved by token");

        req.setAttribute("approved", true);
        req.setAttribute("title", "Approving");
        req.setAttribute("message", "Your email has been approved.");

        return WebPath.PAGE_LOGIN;
    }
}
