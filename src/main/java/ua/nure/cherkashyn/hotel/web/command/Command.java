package ua.nure.cherkashyn.hotel.web.command;

import ua.nure.cherkashyn.hotel.exception.AppException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * Main interface for the Command pattern implementation.
 *
 * @author Vladimir Cherkashyn
 */
public abstract class Command implements Serializable {

    private static final long serialVersionUID = -7367537520814930168L;

    /**
     * Execution method for command.
     *
     * @return Address to go once the command is executed.
     */
    public abstract String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException, AppException;

    @Override
    public final String toString() {
        return getClass().getSimpleName();
    }
}