package ua.nure.cherkashyn.hotel.db;

/**
 * Holder for fields names of DB tables.
 *
 * @author Vladimir Cherkashyn
 */
public final class Fields {

    private Fields() {
        // it's a util class
    }

    // entities
    public static final String ENTITY_ID = "id";

    // users
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String USER_IS_APPROVED = "isApproved";
    public static final String USER_APPROVED_TOKEN = "approvedToken";
    public static final String USER_ROLE_ID = "role_id";

    // apartments
    public static final String APARTMENT_NAME = "name";
    public static final String APARTMENT_SHORT_DESCRIPTION = "shortDescription";
    public static final String APARTMENT_DESCRIPTION = "description";
    public static final String APARTMENT_PRICE = "price";
    public static final String APARTMENT_QUANTITY_OF_ROOMS = "quantityOfRooms";
    public static final String APARTMENT_IMG = "img";
    public static final String APARTMENT_CLASS_ID = "class_id";
    public static final String APARTMENT_STATUS_ID = "status_id";


    //orders
    public static final String ORDER_DATE = "orderDate";
    public static final String ORDER_NUMBER_OF_ROOMS = "numberOfRooms";
    public static final String ORDER_ARRIVAL_DATE = "arrivalDate";
    public static final String ORDER_DEPARTURE_DATE = "departureDate";
    public static final String ORDER_IS_PROCESSED = "isProcessed";
    public static final String ORDER_USER_ID = "users_id";
    public static final String ORDER_APARTMENT_ID = "apartment_id";
    public static final String ORDER_CLASS_ID = "class_id";

}