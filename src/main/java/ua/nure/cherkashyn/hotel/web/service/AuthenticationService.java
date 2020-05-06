package ua.nure.cherkashyn.hotel.web.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import ua.nure.cherkashyn.hotel.db.DAOFactory;
import ua.nure.cherkashyn.hotel.db.dao.UserDAO;
import ua.nure.cherkashyn.hotel.db.entity.Role;
import ua.nure.cherkashyn.hotel.db.entity.User;
import ua.nure.cherkashyn.hotel.exception.DBException;
import ua.nure.cherkashyn.hotel.exception.Messages;
import ua.nure.cherkashyn.hotel.mail.MailMessages;
import ua.nure.cherkashyn.hotel.mail.Sender;
import ua.nure.cherkashyn.hotel.mail.SenderFactory;
import ua.nure.cherkashyn.hotel.web.WebPath;
import ua.nure.cherkashyn.hotel.web.service.entity.AuthenticationChecker;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * AuthenticationService.
 * that service returns only data
 * in JSON format.
 * <p>
 * Supported operation :
 * 1) AUTHORIZE - user that registered can authorize.
 * 2) REGISTER - register a new user.
 * 3) RECOVERY - if user forget the password.
 *
 * @author Vladimir Cherkashyn
 */
@WebServlet("/authorization")
public class AuthenticationService extends HttpServlet {

    private static final long serialVersionUID = 8482367365154984169L;

    private static final Logger LOG = Logger.getLogger(AuthenticationService.class);

    /**
     * Google library to transform object to JSON.
     */
    private Gson gson = null;


    /**
     * Initialization Gson library.
     */
    @Override
    public void init() {
        gson = new GsonBuilder().disableHtmlEscaping().create();
    }


    /**
     * What service will do, if client send post request.
     * return JSON.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String operation = req.getParameter("operation").trim();
        String email = null;
        String password = null;
        String repeatedPassword = null;
        String approvedToken = null;
        AuthenticationServiceOperation op = AuthenticationServiceOperation.valueOf(operation);

        LOG.trace("Obtained operation => " + op);


        if (op != AuthenticationServiceOperation.EXCHANGE_PASSWORD) {
            email = req.getParameter("email").trim();
            password = req.getParameter("password").trim();
            LOG.trace("Obtained email => " + email);
        } else {
            approvedToken = req.getParameter("approvedToken").trim();
            password = req.getParameter("password").trim();
            repeatedPassword = req.getParameter("repeatedPassword").trim();
        }


        AuthenticationChecker checker = null;
        switch (op) {
            case AUTHORIZE:
                checker = authorizeUser(email, password, req);
                break;
            case REGISTER:
                checker = registerUser(email, password, req);
                break;
            case RECOVERY:
                checker = recoveryPassword(email, req);
                break;
            case EXCHANGE_PASSWORD:
                checker = exchangePassword(approvedToken, password, repeatedPassword, req);
                break;
        }


        String checkerJson = gson.toJson(checker);
        OutputStream out = resp.getOutputStream();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        byte[] utf8JsonString = checkerJson.getBytes("UTF8");
        out.write(utf8JsonString, 0, utf8JsonString.length);
        out.flush();
    }


    /**
     * Check if user exist in DB and approved.
     * <p>
     * If user does't exist or user is't approved
     * or password is't correct,
     * then access denied.
     *
     * @param email    user email.
     * @param password user password.
     * @return AuthorizationChecker messages to user.
     */
    private AuthenticationChecker authorizeUser(String email, String password, HttpServletRequest req) {
        ResourceBundle bundle = getI18NBundle(req);
        AuthenticationChecker checker = checkFieldsOnValid(email, password, req);
        LOG.debug("here");
        HttpSession session = req.getSession();
        try {

            User user = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getUserDAO().findApprovedUserByEmail(email);
            if (!(checker.isEmailValid() && checker.isPasswordValid())) {
                return checker;
            }

            if (user == null) {
                checker.setEmailValid(false);
                checker.setPasswordValid(false);
                checker.setEmailMessage(bundle.getString("authorizeUserNotFoundInDataBaseEmail"));
                checker.setPasswordMessage(bundle.getString("authorizeUserNotFoundInDataBasePassword"));
                return checker;
            }

            if (password.equals(user.getPassword())) {
                checker.setPasswordValid(true);
                checker.setLinkToRedirect(req.getRequestURL().
                        toString().
                        replace(req.getRequestURI(), "/hotel"));
                Role role = Role.getRole(user);
                session.setAttribute("user", user);
                session.setAttribute("role", role.getName());
                LOG.trace("User ==> " + user.getEmail() + "singed in as ==>" + role.getName());
            } else {
                checker.setPasswordValid(false);
                checker.setEmailMessage(bundle.getString("authorizeEmailOk"));
                checker.setPasswordMessage(bundle.getString("authorizePasswordInvalid"));
            }
        } catch (DBException e) {
            checker.setError(true);
        }

        return checker;
    }


    /**
     * Check if user with that mail is unregistered
     * and register him/her.
     * That method insert into database new user that not approved.
     *
     * @param email    user email
     * @param password user password
     * @return AuthorizationChecker messages to user.
     */
    private AuthenticationChecker registerUser(String email, String password, HttpServletRequest req) {

        ResourceBundle bundle = getI18NBundle(req);

        AuthenticationChecker checker = checkFieldsOnValid(email, password, req);
        try {

            UserDAO dao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getUserDAO();
            User user = dao.findUserByEmail(email);

            if (!(checker.isEmailValid() && checker.isPasswordValid())) {
                return checker;
            }

            LOG.trace("#registerUser Fields are valid");

            if (user == null) {
                user = new User();
                user.setEmail(email);
                user.setPassword(password);
                user.setApprovedToken(getApprovedToken(email));
                dao.insertUser(user);
                checker.setEmailMessage(bundle.getString("registerSuccess"));
                new Thread(() -> sendRegistrationMail(email)).start();

                LOG.trace("#registerUser New user was register");
            } else {
                checker.setEmailValid(false);
                checker.setEmailMessage(bundle.getString("registerFail"));
            }

        } catch (DBException e) {
            checker.setError(true);
        }

        return checker;
    }


    /**
     * Send to obtained email a message that will help to recovery password.
     *
     * @param email user email
     * @return AuthorizationChecker messages to user.
     */
    private AuthenticationChecker recoveryPassword(String email, HttpServletRequest req) {
        ResourceBundle bundle = getI18NBundle(req);
        AuthenticationChecker checker = new AuthenticationChecker();
        try {
            if (!isEmailValid(email)) {
                checker.setEmailValid(false);
                checker.setEmailMessage(bundle.getString("emailInvalid"));
                return checker;
            }

            UserDAO dao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getUserDAO();
            User user = dao.findApprovedUserByEmail(email);

            if (user != null) {
                checker.setEmailMessage(bundle.getString("recoverySuccess"));
                dao.updateApprovedToken(user, getApprovedToken(email));
                new Thread(() -> sendRecoveryPasswordMail(email)).start();
            } else {
                checker.setEmailValid(false);
                checker.setEmailMessage(bundle.getString("recoveryFail"));
            }

        } catch (DBException e) {
            checker.setError(true);
        }

        return checker;
    }


    /**
     * exchangePassword service
     *
     * @param approvedToken    token
     * @param password         new password
     * @param repeatedPassword repeated password
     * @param req              HttpServletRequest
     */
    private AuthenticationChecker exchangePassword(String approvedToken, String password, String repeatedPassword, HttpServletRequest req) {
        ResourceBundle bundle = getI18NBundle(req);
        AuthenticationChecker checker = new AuthenticationChecker();
        try {
            if (!(isPasswordValid(password) && isPasswordValid(repeatedPassword))) {
                checker.setPasswordValid(false);
                checker.setPasswordMessage(bundle.getString("exchangePasswordsNotValid"));
                return checker;
            }

            if (!password.equals(repeatedPassword)) {
                checker.setPasswordValid(false);
                checker.setPasswordMessage(bundle.getString("exchangePasswordsNotEqual"));
                return checker;
            }

            UserDAO dao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getUserDAO();
            User user = dao.findUserByApprovedToken(approvedToken);

            if (user != null) {
                dao.updateUserPassword(user, password);
                checker.setLinkToRedirect(req.getRequestURL().
                        toString().
                        replace(req.getRequestURI(), "/hotel/" + WebPath.COMMAND_RECOVERY_HELPER));
            } else {
                checker.setPasswordValid(false);
                checker.setPasswordMessage(bundle.getString("exchangeUserWithApprovedTokenNotFound"));
            }

        } catch (DBException e) {
            checker.setError(true);
        }

        return checker;
    }


    /**
     * Check If fields are valid and
     * set a messages to user.
     *
     * @param email    user email
     * @param password user password
     * @return AuthorizationChecker messages to user.
     */
    private AuthenticationChecker checkFieldsOnValid(String email, String password, HttpServletRequest req) {
        ResourceBundle bundle = getI18NBundle(req);
        AuthenticationChecker checker = new AuthenticationChecker();
        checker.setEmailMessage(bundle.getString("authorizeEmailOk"));
        checker.setPasswordMessage(bundle.getString("passwordOk"));
        if (!isEmailValid(email)) {
            checker.setEmailValid(false);
            checker.setEmailMessage(bundle.getString("emailInvalid"));
        }
        if (!isPasswordValid(password)) {
            checker.setPasswordValid(false);
            checker.setPasswordMessage(bundle.getString("passwordInvalid"));
        }
        return checker;
    }


    /**
     * Check "is email valid?".
     *
     * @param email user email
     * @return boolean (valid or not)
     */
    private boolean isEmailValid(String email) {
        boolean emailValid = false;
        Pattern emailPattern = Pattern.compile(ServiceConstants.EMAIL_VALIDATION_REG_EXP);
        Matcher matcher = emailPattern.matcher(email);
        if (matcher.find()) {
            emailValid = true;
        }
        return emailValid;
    }

    /**
     * Check "is password valid?".
     * <p>
     * valid password is a password that consist minimum 8 symbols and
     * at least one of them is number.
     *
     * @param password user password.
     * @return boolean (valid or not)
     */
    private boolean isPasswordValid(String password) {
        boolean passwordValid = false;
        Pattern passwordPattern = Pattern.compile(ServiceConstants.PASSWORD_VALIDATION_REG_EXP);
        Matcher matcher = passwordPattern.matcher(password);
        if (matcher.find()) {
            passwordValid = true;
        }
        return passwordValid;
    }


    /**
     * Send registration email
     *
     * @param email email to send registration email
     */
    private void sendRegistrationMail(String email) {
        Sender sender = SenderFactory.getSender(SenderFactory.GOOGLE_MAIL_TLS);
        String message = getApprovedToken(email);
        String text = MailMessages.MESSAGE_TEXT_REGISTER + message;
        sender.send(MailMessages.MESSAGE_TITLE_REGISTER, text, email);
    }


    /**
     * Send recovery email
     *
     * @param email email to send recovery email
     */
    private void sendRecoveryPasswordMail(String email) {
        Sender sender = SenderFactory.getSender(SenderFactory.GOOGLE_MAIL_TLS);
        String message = getApprovedToken(email);
        String text = MailMessages.MESSAGE_TEXT_RECOVERY + message;
        sender.send(MailMessages.MESSAGE_TITLE_RECOVERY, text, email);
    }


    /**
     * Get approved token;
     *
     * @param email email from witch generated token.
     */
    private String getApprovedToken(String email) {
        email += ServiceConstants.SALT;
        try {
            MessageDigest md = MessageDigest.getInstance(ServiceConstants.ALGORITHM);
            byte[] messageDigest = md.digest(email.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            return no.toString(16);
        } catch (NoSuchAlgorithmException e) {
            LOG.error(Messages.ERR_NO_SUCH_ALGORITHM, e);
        }
        return "";
    }


    /**
     * I18N helper
     */
    private ResourceBundle getI18NBundle(HttpServletRequest req) {
        Locale locale = Locale.forLanguageTag((String) req.getSession().getAttribute("locale"));
        ResourceBundle bundle = ResourceBundle.getBundle("hotel", locale);
        return bundle;
    }

}
