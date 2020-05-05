package ua.nure.cherkashyn.hotel.db.dao;

import ua.nure.cherkashyn.hotel.db.entity.User;
import ua.nure.cherkashyn.hotel.exception.DBException;


/**
 * Interface UserDAO.
 *
 * @author Vladimir Cherkashyn
 */
public interface UserDAO {
    User findApprovedUserByEmail(String email) throws DBException;

    User findUserByEmail(String email) throws DBException;

    User findUserByApprovedToken(String approvedToken) throws DBException;

    void insertUser(User user) throws DBException;

    void approveUser(User user) throws DBException;

    void updateApprovedToken(User user, String token) throws DBException;

    void updateUserPassword(User user, String repeatedPassword) throws DBException;
}
