package ua.nure.cherkashyn.hotel.web;

/**
 * Path holder (jsp pages, controller commands).
 *
 * @author Vladimir Cherkashyn
 */
public final class WebPath {

    private WebPath() {
        // it's an utility class
    }

    // pages
    public static final String PAGE_LOGIN = "/login.jsp";
    public static final String PAGE_RECOVERY = "/WEB-INF/jsp/recovery.jsp";
    public static final String PAGE_INDEX = "/index.jsp";
    public static final String PAGE_ERROR_PAGE = "/WEB-INF/jsp/error_page.jsp";
    public static final String PAGE_APARTMENTS = "/WEB-INF/jsp/apartments.jsp";
    public static final String PAGE_APARTMENTS_CONTENT = "/WEB-INF/jsp/apartmentsContent.jsp";
    public static final String PAGE_BOOKING = "/WEB-INF/jsp/bookings.jsp";


    // commands
    public static final String COMMAND_APPROVE_USER = "/controller?command=approve";
    public static final String COMMAND_RECOVERY_HELPER = "/controller?command=recoveryHelper";
    public static final String COMMAND_SHOW_APARTMENTS = "/controller?command=showApartments";
    public static final String COMMAND_SHOW_APARTMENTS_CONTENT = "/controller?command=showApartmentsContent&offset=0&statusId=0&classId=0&quantityOfRooms=1&priceFrom=100&priceUntil=2600&orderByASC=false";
    public static final String COMMAND_MAKE_ORDER = "/controller?command=makeOrder";
    public static final String COMMAND_SHOW_BOOKINGS = "/controller?command=showBookings";

    //services
    public static final String SERVICE_AUTHORIZATION = "/authorization";


}