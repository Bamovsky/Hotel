package ua.nure.cherkashyn.hotel.db.mysql;

import org.apache.log4j.Logger;
import ua.nure.cherkashyn.hotel.db.DBUtils;
import ua.nure.cherkashyn.hotel.db.Fields;
import ua.nure.cherkashyn.hotel.db.MySqlDAOFactory;
import ua.nure.cherkashyn.hotel.db.dao.UserDAO;
import ua.nure.cherkashyn.hotel.db.entity.User;
import ua.nure.cherkashyn.hotel.exception.DBException;
import ua.nure.cherkashyn.hotel.exception.Messages;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Implementation of UserDAO for MySQL database.
 *
 * @author Vladimir Cherkashyn
 */
public class UserDAOMySQL implements UserDAO {

    private static final Logger LOG = Logger.getLogger(UserDAOMySQL.class);

    // //////////////////////////////////////////////////////////
    // SQL queries
    // //////////////////////////////////////////////////////////
    private static final String SQL_FIND_APPROVED_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ? AND isApproved = 1";
    private static final String SQL_FIND_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
    private static final String SQL_FIND_USER_BY_APPROVED_TOKEN = "SELECT * FROM users WHERE approvedToken = ?";
    private static final String SQL_INSERT_USER = "INSERT INTO users (email, password, isApproved, approvedToken, role_id) VALUES (?,?,0,?,0)";
    private static final String SQL_APPROVE_USER = "UPDATE users SET isApproved = 1 WHERE id = ?";
    private static final String SQL_SET_APPROVED_TOKEN_TO_NULL = "UPDATE users SET approvedToken = NULL WHERE id = ?";
    private static final String SQL_SET_APPROVED_TOKEN = "UPDATE users SET approvedToken = ? WHERE id = ?";
    private static final String SQL_SET_USER_PASSWORD = "UPDATE users SET password = ? WHERE id = ?";

    /**
     * Find an approved user with required email.
     *
     * @param email email of user.
     */
    public User findApprovedUserByEmail(String email) throws DBException {
        User user = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = MySqlDAOFactory.createConnection();
            pstmt = con.prepareStatement(SQL_FIND_APPROVED_USER_BY_EMAIL);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                user = extractUser(rs);
            }
            con.commit();
        } catch (SQLException ex) {
            DBUtils.rollback(con);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            DBUtils.close(con, pstmt, rs);
        }
        return user;
    }


    /**
     * Find an user with required email.
     * This user can be not approved.
     *
     * @param email email of user.
     */
    public User findUserByEmail(String email) throws DBException {
        User user = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = MySqlDAOFactory.createConnection();
            pstmt = con.prepareStatement(SQL_FIND_USER_BY_EMAIL);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                user = extractUser(rs);
            }
            con.commit();
        } catch (SQLException ex) {
            DBUtils.rollback(con);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            DBUtils.close(con, pstmt, rs);
        }
        return user;
    }


    /**
     * Find an user with required required token
     *
     * @param approvedToken token that was generated.
     */
    @Override
    public User findUserByApprovedToken(String approvedToken) throws DBException {
        User user = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = MySqlDAOFactory.createConnection();
            pstmt = con.prepareStatement(SQL_FIND_USER_BY_APPROVED_TOKEN);
            pstmt.setString(1, approvedToken);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                user = extractUser(rs);
            }
            con.commit();
        } catch (SQLException ex) {
            DBUtils.rollback(con);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            DBUtils.close(con, pstmt, rs);
        }
        return user;
    }


    /**
     * Insert an user into database
     *
     * @param user user to insert into database
     */
    public void insertUser(User user) throws DBException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = MySqlDAOFactory.createConnection();
            pstmt = con.prepareStatement(SQL_INSERT_USER);
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getApprovedToken());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException ex) {
            DBUtils.rollback(con);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            DBUtils.close(con, pstmt, rs);
        }
    }


    /**
     * Approve an user
     * that method helps approve user,
     * without approving user can't login into system.
     *
     * @param user user to approve
     */
    @Override
    public void approveUser(User user) throws DBException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = MySqlDAOFactory.createConnection();
            pstmt = con.prepareStatement(SQL_APPROVE_USER);
            pstmt.setLong(1, user.getId());
            pstmt.executeUpdate();

            pstmt = con.prepareStatement(SQL_SET_APPROVED_TOKEN_TO_NULL);
            pstmt.setLong(1, user.getId());
            pstmt.executeUpdate();

            con.commit();
        } catch (SQLException ex) {
            DBUtils.rollback(con);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            DBUtils.close(con, pstmt, rs);
        }
    }


    /**
     * Update user approved token
     *
     * @param user  user to approve
     * @param token token that generated
     */
    @Override
    public void updateApprovedToken(User user, String token) throws DBException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {

            con = MySqlDAOFactory.createConnection();
            pstmt = con.prepareStatement(SQL_SET_APPROVED_TOKEN);
            pstmt.setString(1, token);
            pstmt.setLong(2, user.getId());
            pstmt.executeUpdate();

            con.commit();
        } catch (SQLException ex) {
            DBUtils.rollback(con);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            DBUtils.close(con, pstmt, rs);
        }
    }


    /**
     * Update user password
     *
     * @param user
     * @param newPassword
     */
    @Override
    public void updateUserPassword(User user, String newPassword) throws DBException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = MySqlDAOFactory.createConnection();
            pstmt = con.prepareStatement(SQL_SET_USER_PASSWORD);
            pstmt.setString(1, newPassword);
            pstmt.setLong(2, user.getId());
            pstmt.executeUpdate();
            pstmt = con.prepareStatement(SQL_SET_APPROVED_TOKEN_TO_NULL);
            pstmt.setLong(1, user.getId());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException ex) {
            DBUtils.rollback(con);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            DBUtils.close(con, pstmt, rs);
        }
    }

    /**
     * Extract a user from result set.
     *
     * @param rs ResultSet.
     */
    private User extractUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong(Fields.ENTITY_ID));
        user.setEmail(rs.getString(Fields.USER_EMAIL));
        user.setPassword(rs.getString(Fields.USER_PASSWORD));
        user.setApproved(rs.getBoolean(Fields.USER_IS_APPROVED));
        user.setApprovedToken(rs.getString(Fields.USER_APPROVED_TOKEN));
        user.setRoleId(rs.getLong(Fields.USER_ROLE_ID));
        return user;
    }

}
