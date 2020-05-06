package ua.nure.cherkashyn.hotel.db.mysql;

import org.apache.log4j.Logger;
import ua.nure.cherkashyn.hotel.db.DBUtils;
import ua.nure.cherkashyn.hotel.db.Fields;
import ua.nure.cherkashyn.hotel.db.MySqlDAOFactory;
import ua.nure.cherkashyn.hotel.db.dao.ApartmentDAO;
import ua.nure.cherkashyn.hotel.db.entity.Apartment;
import ua.nure.cherkashyn.hotel.exception.DBException;
import ua.nure.cherkashyn.hotel.exception.Messages;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Implementation of ApartmentDAO for MySQL database.
 *
 * @author Vladimir Cherkashyn
 */
public class ApartmentDAOMySQL implements ApartmentDAO {

    private static final Logger LOG = Logger.getLogger(ApartmentDAOMySQL.class);

    // //////////////////////////////////////////////////////////
    // SQL queries
    // //////////////////////////////////////////////////////////

    private static final String SQL_GET_MAX_PRICE = " select max(price) from apartment";
    private static final String SQL_GET_MIN_PRICE = " select min(price) from apartment";

    private static final String SQL_GET_I18N_APARTMENT_NAME = "SELECT text from translationApartment where i18nFieldsApartment_id = (select id from i18nFieldsApartment where fieldName = 'name') and language_id = (select id from language where name = ?) and apartment_id =?";
    private static final String SQL_GET_I18N_APARTMENT_STATUS = "SELECT statusName from translationStatus where status_id =? and language_id = (select id from language where name = ?)";
    private static final String SQL_GET_I18N_APARTMENT_CLASS = "SELECT className from translationClass where class_id =? and language_id = (select id from language where name = ?)";
    private static final String SQL_GET_I18N_APARTMENT_SHORT_DESCRIPTION = "SELECT text from translationApartment where i18nFieldsApartment_id = (select id from i18nFieldsApartment where fieldName = 'shortDescription') and language_id = (select id from language where name = ?) and apartment_id =?";

    private static final String SQL_GET_BOOKED_APARTMENTS_WITH_PARAMS_DESC = "Select * from (Select * from apartment left outer join (Select apartment_id from booking where ((arrivalDate between ? and ?) or (departureDate  between ? and ? )) and status_id = 1) as T on T.apartment_id = id) as G where g.apartment_id is not null and G.class_id=? and G.quantityOfRooms=? and G.price>=? and G.price <=? ORDER BY G.price DESC LIMIT ?, ?";
    private static final String SQL_GET_BOOKED_APARTMENTS_WITH_PARAMS_ASC = "Select * from (Select * from apartment left outer join (Select apartment_id from booking where ((arrivalDate between ? and ?) or (departureDate  between ? and ? )) and status_id = 1) as T on T.apartment_id = id) as G where g.apartment_id is not null and G.class_id=? and G.quantityOfRooms=? and G.price>=? and G.price <=? ORDER BY G.price ASC LIMIT ?, ?";
    private static final String SQL_GET_QUANTITY_OF_BOOKED_APARTMENTS = "Select COUNT(id) from (Select * from apartment left outer join (Select apartment_id from booking where ((arrivalDate between ? and ?) or (departureDate  between ? and ? )) and status_id = 1) as T on T.apartment_id = id) as G where g.apartment_id is not null and G.class_id=? and G.quantityOfRooms=? and G.price>=? and G.price <=?";

    private static final String SQL_GET_OCCUPIED_APARTMENTS_WITH_PARAMS_DESC = "Select * from (Select * from apartment left outer join (Select apartment_id from booking where ((arrivalDate between ? and ?) or (departureDate  between ? and ? )) and status_id = 2) as T on T.apartment_id = id) as G where g.apartment_id is not null and G.class_id=? and G.quantityOfRooms=? and G.price>=? and G.price <=? ORDER BY G.price DESC LIMIT ?, ?";
    private static final String SQL_GET_OCCUPIED_APARTMENTS_WITH_PARAMS_ASC = "Select * from (Select * from apartment left outer join (Select apartment_id from booking where ((arrivalDate between ? and ?) or (departureDate  between ? and ? )) and status_id = 2) as T on T.apartment_id = id) as G where g.apartment_id is not null and G.class_id=? and G.quantityOfRooms=? and G.price>=? and G.price <=? ORDER BY G.price ASC LIMIT ?, ?";
    private static final String SQL_GET_QUANTITY_OF_OCCUPIED_APARTMENTS = "Select COUNT(id) from (Select * from apartment left outer join (Select apartment_id from booking where ((arrivalDate between ? and ?) or (departureDate  between ? and ? )) and status_id = 2) as T on T.apartment_id = id) as G where g.apartment_id is not null and G.class_id=? and G.quantityOfRooms=? and G.price>=? and G.price <=?";

    private static final String SQL_GET_FREE_APARTMENTS_WITH_PARAMS_DESC = "Select * from (Select * from apartment left outer join (Select apartment_id from booking where (arrivalDate between ? and ?) or (departureDate  between ? and ? )) as T on T.apartment_id = id) as G where g.apartment_id is null and G.class_id=? and G.quantityOfRooms=? and G.price>=? and G.price <=? ORDER BY G.price DESC LIMIT ?, ?";
    private static final String SQL_GET_FREE_APARTMENTS_WITH_PARAMS_ASC = "Select * from (Select * from apartment left outer join (Select apartment_id from booking where (arrivalDate between ? and ?) or (departureDate  between ? and ? )) as T on T.apartment_id = id) as G where g.apartment_id is null and G.class_id=? and G.quantityOfRooms=? and G.price>=? and G.price <=? ORDER BY G.price ASC LIMIT ?, ?";
    private static final String SQL_GET_QUANTITY_OF_FREE_APARTMENTS = " Select COUNT(id) from (Select * from apartment left outer join (Select apartment_id from booking where (arrivalDate between ? and ?) or (departureDate  between ? and ? )) as T on T.apartment_id = id) as G where g.apartment_id is null and G.class_id=? and G.quantityOfRooms=? and G.price>=? and G.price <=?";


    /**
     * get max price from all apartments
     * <p>
     * needed for information to view layer
     */
    @Override
    public BigDecimal getMaxApartmentPrice() throws DBException {
        BigDecimal price = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = MySqlDAOFactory.createConnection();
            pstmt = con.prepareStatement(SQL_GET_MAX_PRICE);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                price = rs.getBigDecimal(1);
            }
            con.commit();
        } catch (SQLException ex) {
            DBUtils.rollback(con);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            DBUtils.close(con, pstmt, rs);
        }

        return price;
    }


    /**
     * get min price from all apartments
     * <p>
     * needed for information to view layer
     */
    @Override
    public BigDecimal getMinApartmentPrice() throws DBException {
        BigDecimal price = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = MySqlDAOFactory.createConnection();
            pstmt = con.prepareStatement(SQL_GET_MIN_PRICE);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                price = rs.getBigDecimal(1);
            }
            con.commit();
        } catch (SQLException ex) {
            DBUtils.rollback(con);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            DBUtils.close(con, pstmt, rs);
        }
        return price;
    }


    /**
     * Get booked apartments from needed dates.
     *
     * <p>
     *
     * @param offset        how many apartments skip (for pagination)
     * @param howMany       how many apartments show
     * @param orderBy       ASC or DESC (false = DESC , true = ASC)
     * @param arrivalDate   arrival date that user want
     * @param departureDate departure date that user want
     * @param locale        needed for i18n
     */
    @Override
    public List<Apartment> getBookedApartments(int offset, int howMany, long classId, int quantityOfRooms, BigDecimal priceFrom, BigDecimal priceUntil, boolean orderBy, LocalDate arrivalDate, LocalDate departureDate, Locale locale) throws DBException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Apartment> apartments = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = MySqlDAOFactory.createConnection();
            if (orderBy) {
                pstmt = con.prepareStatement(SQL_GET_BOOKED_APARTMENTS_WITH_PARAMS_ASC);
            } else {
                pstmt = con.prepareStatement(SQL_GET_BOOKED_APARTMENTS_WITH_PARAMS_DESC);
            }
            pstmt.setString(1, arrivalDate.format(formatter));
            pstmt.setString(2, departureDate.format(formatter));
            pstmt.setString(3, arrivalDate.format(formatter));
            pstmt.setString(4, departureDate.format(formatter));
            pstmt.setLong(5, classId);
            pstmt.setInt(6, quantityOfRooms);
            pstmt.setBigDecimal(7, priceFrom);
            pstmt.setBigDecimal(8, priceUntil);
            pstmt.setInt(9, offset);
            pstmt.setInt(10, howMany);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                apartments.add(extractApartment(rs, locale, 1));
            }
            con.commit();
        } catch (SQLException ex) {
            DBUtils.rollback(con);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            DBUtils.close(con, pstmt, rs);
        }

        return apartments;
    }


    /**
     * Get quantity of booked apartments from needed dates.
     * <p>
     *
     * @param arrivalDate   arrival date that user want
     * @param departureDate departure date that user want
     */
    @Override
    public int getQuantityOfApartmentsWithBookedStatus(long classId, int quantityOfRooms, BigDecimal priceFrom, BigDecimal priceUntil, LocalDate arrivalDate, LocalDate departureDate) throws DBException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int result = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = MySqlDAOFactory.createConnection();
            pstmt = con.prepareStatement(SQL_GET_QUANTITY_OF_BOOKED_APARTMENTS);
            pstmt.setString(1, arrivalDate.format(formatter));
            pstmt.setString(2, departureDate.format(formatter));
            pstmt.setString(3, arrivalDate.format(formatter));
            pstmt.setString(4, departureDate.format(formatter));
            pstmt.setLong(5, classId);
            pstmt.setInt(6, quantityOfRooms);
            pstmt.setBigDecimal(7, priceFrom);
            pstmt.setBigDecimal(8, priceUntil);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
            con.commit();
        } catch (SQLException ex) {
            DBUtils.rollback(con);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            DBUtils.close(con, pstmt, rs);
        }

        return result;
    }


    /**
     * Get occupied apartments from needed dates.
     *
     * <p>
     *
     * @param offset        how many apartments skip (for pagination)
     * @param howMany       how many apartments show
     * @param orderBy       ASC or DESC (false = DESC , true = ASC)
     * @param arrivalDate   arrival date that user want
     * @param departureDate departure date that user want
     * @param locale        needed for i18n
     */
    @Override
    public List<Apartment> getOccupiedApartments(int offset, int howMany, long classId, int quantityOfRooms, BigDecimal priceFrom, BigDecimal priceUntil, boolean orderBy, LocalDate arrivalDate, LocalDate departureDate, Locale locale) throws DBException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Apartment> apartments = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = MySqlDAOFactory.createConnection();
            if (orderBy) {
                pstmt = con.prepareStatement(SQL_GET_OCCUPIED_APARTMENTS_WITH_PARAMS_ASC);
            } else {
                pstmt = con.prepareStatement(SQL_GET_OCCUPIED_APARTMENTS_WITH_PARAMS_DESC);
            }
            pstmt.setString(1, arrivalDate.format(formatter));
            pstmt.setString(2, departureDate.format(formatter));
            pstmt.setString(3, arrivalDate.format(formatter));
            pstmt.setString(4, departureDate.format(formatter));
            pstmt.setLong(5, classId);
            pstmt.setInt(6, quantityOfRooms);
            pstmt.setBigDecimal(7, priceFrom);
            pstmt.setBigDecimal(8, priceUntil);
            pstmt.setInt(9, offset);
            pstmt.setInt(10, howMany);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                apartments.add(extractApartment(rs, locale, 2));
            }
            con.commit();
        } catch (SQLException ex) {
            DBUtils.rollback(con);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            DBUtils.close(con, pstmt, rs);
        }
        return apartments;
    }


    /**
     * Get quantity of occupied apartments from needed dates.
     *
     * <p>
     *
     * @param arrivalDate   arrival date that user want
     * @param departureDate departure date that user want
     */
    @Override
    public int getQuantityOfApartmentsWithOccupiedStatus(long classId, int quantityOfRooms, BigDecimal priceFrom, BigDecimal priceUntil, LocalDate arrivalDate, LocalDate departureDate) throws DBException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int result = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = MySqlDAOFactory.createConnection();
            pstmt = con.prepareStatement(SQL_GET_QUANTITY_OF_OCCUPIED_APARTMENTS);
            pstmt.setString(1, arrivalDate.format(formatter));
            pstmt.setString(2, departureDate.format(formatter));
            pstmt.setString(3, arrivalDate.format(formatter));
            pstmt.setString(4, departureDate.format(formatter));
            pstmt.setLong(5, classId);
            pstmt.setInt(6, quantityOfRooms);
            pstmt.setBigDecimal(7, priceFrom);
            pstmt.setBigDecimal(8, priceUntil);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
            con.commit();
        } catch (SQLException ex) {
            DBUtils.rollback(con);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            DBUtils.close(con, pstmt, rs);
        }

        return result;
    }


    /**
     * Get free apartments from needed dates.
     *
     * <p>
     *
     * @param offset        how many apartments skip (for pagination)
     * @param howMany       how many apartments show
     * @param orderBy       ASC or DESC (false = DESC , true = ASC)
     * @param arrivalDate   arrival date that user want
     * @param departureDate departure date that user want
     * @param locale        needed for i18n
     */
    @Override
    public List<Apartment> getFreeApartments(int offset, int howMany, long classId, int quantityOfRooms, BigDecimal priceFrom, BigDecimal priceUntil, boolean orderBy, LocalDate arrivalDate, LocalDate departureDate, Locale locale) throws DBException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Apartment> apartments = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = MySqlDAOFactory.createConnection();
            if (orderBy) {
                pstmt = con.prepareStatement(SQL_GET_FREE_APARTMENTS_WITH_PARAMS_DESC);
            } else {
                pstmt = con.prepareStatement(SQL_GET_FREE_APARTMENTS_WITH_PARAMS_ASC);
            }
            pstmt.setString(1, arrivalDate.format(formatter));
            pstmt.setString(2, departureDate.format(formatter));
            pstmt.setString(3, arrivalDate.format(formatter));
            pstmt.setString(4, departureDate.format(formatter));
            pstmt.setLong(5, classId);
            pstmt.setInt(6, quantityOfRooms);
            pstmt.setBigDecimal(7, priceFrom);
            pstmt.setBigDecimal(8, priceUntil);
            pstmt.setInt(9, offset);
            pstmt.setInt(10, howMany);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                apartments.add(extractApartment(rs, locale, 0));
            }
            con.commit();
        } catch (SQLException ex) {
            DBUtils.rollback(con);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            DBUtils.close(con, pstmt, rs);
        }

        return apartments;
    }


    /**
     * Get quantity of free apartments from needed dates.
     *
     * <p>
     *
     * @param arrivalDate   arrival date that user want
     * @param departureDate departure date that user want
     */
    @Override
    public int getQuantityOfApartmentsWithFreeStatus(long classId, int quantityOfRooms, BigDecimal priceFrom, BigDecimal priceUntil, LocalDate arrivalDate, LocalDate departureDate) throws DBException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int result = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = MySqlDAOFactory.createConnection();
            pstmt = con.prepareStatement(SQL_GET_QUANTITY_OF_FREE_APARTMENTS);
            pstmt.setString(1, arrivalDate.format(formatter));
            pstmt.setString(2, departureDate.format(formatter));
            pstmt.setString(3, arrivalDate.format(formatter));
            pstmt.setString(4, departureDate.format(formatter));
            pstmt.setLong(5, classId);
            pstmt.setInt(6, quantityOfRooms);
            pstmt.setBigDecimal(7, priceFrom);
            pstmt.setBigDecimal(8, priceUntil);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
                LOG.debug("result" + result);
            }
            con.commit();
        } catch (SQLException ex) {
            DBUtils.rollback(con);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            DBUtils.close(con, pstmt, rs);
        }

        return result;
    }


    /**
     * Extract a user from result set.
     *
     * @param rs       ResultSet.
     * @param locale   for i18n
     * @param statusId that will be extracted
     */
    private Apartment extractApartment(ResultSet rs, Locale locale, long statusId) throws SQLException, DBException {
        Apartment apartment = new Apartment();
        apartment.setId(rs.getLong(Fields.ENTITY_ID));
        apartment.setName(getApartmentName(apartment, locale));
        apartment.setShortDescription(getApartmentShortDescription(apartment, locale));
        apartment.setPrice(rs.getBigDecimal(Fields.APARTMENT_PRICE));
        apartment.setQuantityOfRooms(rs.getInt(Fields.APARTMENT_QUANTITY_OF_ROOMS));
        apartment.setImg(rs.getString(Fields.APARTMENT_IMG));
        apartment.setClassId(rs.getLong(Fields.APARTMENT_CLASS_ID));
        apartment.setStatusId(statusId);
        apartment.setStatusI18N(getApartmentStatus(apartment, locale));
        apartment.setClassI18N(getApartmentClass(apartment, locale));
        return apartment;
    }


    /**
     * Get apartment name in needed language
     *
     * @param apartment into that apartment will be put a name in needed language
     * @param locale    for i18n
     */
    private String getApartmentName(Apartment apartment, Locale locale) throws DBException {
        String result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = MySqlDAOFactory.createConnection();
            pstmt = con.prepareStatement(SQL_GET_I18N_APARTMENT_NAME);
            LOG.debug(locale.toString().toLowerCase());
            pstmt.setString(1, locale.toString().toLowerCase());
            pstmt.setLong(2, apartment.getId());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
            }
            con.commit();
        } catch (SQLException | DBException ex) {
            DBUtils.rollback(con);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            DBUtils.close(con, pstmt, rs);
        }
        return result;
    }

    /**
     * Get apartment short description in needed language
     *
     * @param apartment into that apartment will be put a name in needed language
     * @param locale    for i18n
     */
    private String getApartmentShortDescription(Apartment apartment, Locale locale) throws DBException {
        String result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = MySqlDAOFactory.createConnection();
            pstmt = con.prepareStatement(SQL_GET_I18N_APARTMENT_SHORT_DESCRIPTION);
            LOG.debug(locale.toString().toLowerCase());
            pstmt.setString(1, locale.toString().toLowerCase());
            pstmt.setLong(2, apartment.getId());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
            }
            con.commit();
        } catch (SQLException | DBException ex) {
            DBUtils.rollback(con);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            DBUtils.close(con, pstmt, rs);
        }
        return result;
    }


    /**
     * Get apartment status in needed language
     *
     * @param apartment into that apartment will be put a status in needed language
     * @param locale    for i18n
     */
    private String getApartmentStatus(Apartment apartment, Locale locale) throws DBException {
        String result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = MySqlDAOFactory.createConnection();
            pstmt = con.prepareStatement(SQL_GET_I18N_APARTMENT_STATUS);
            pstmt.setLong(1, apartment.getStatusId());
            pstmt.setString(2, locale.toString().toLowerCase());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
            }
            con.commit();
        } catch (SQLException | DBException ex) {
            DBUtils.rollback(con);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            DBUtils.close(con, pstmt, rs);
        }
        return result;
    }


    /**
     * Get apartment class in needed language
     *
     * @param apartment into that apartment will be put a class in needed language
     * @param locale    for i18n
     */
    private String getApartmentClass(Apartment apartment, Locale locale) throws DBException {
        String result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = MySqlDAOFactory.createConnection();
            pstmt = con.prepareStatement(SQL_GET_I18N_APARTMENT_CLASS);
            pstmt.setLong(1, apartment.getClassId());
            pstmt.setString(2, locale.toString().toLowerCase());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
            }
            con.commit();
        } catch (SQLException | DBException ex) {
            DBUtils.rollback(con);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            DBUtils.close(con, pstmt, rs);
        }
        return result;
    }

}
