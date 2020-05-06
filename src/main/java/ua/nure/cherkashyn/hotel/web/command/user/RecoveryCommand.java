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
 * RecoveryCommand
 *
 * @author Vladimir Cherkashyn
 */
public class RecoveryCommand extends Command {

    private static final long serialVersionUID = 7186043872359971704L;

    private static final Logger LOG = Logger.getLogger(RecoveryCommand.class);

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

        if (user == null) {
            throw new AppException("User with this token was't found");
        }

        return WebPath.PAGE_RECOVERY;
    }
}
