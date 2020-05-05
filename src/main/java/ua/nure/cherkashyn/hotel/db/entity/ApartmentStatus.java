package ua.nure.cherkashyn.hotel.db.entity;

/**
 * ApartmentStatus entity.
 *
 * @author Vladimir Cherkashyn
 */
public enum ApartmentStatus {
    FREE, BOOKED, OCCUPIED, INACCESSIBLE;

    public static ApartmentStatus getStatus(Apartment apartment) {
        int statusID = (int) apartment.getStatusId();
        return ApartmentStatus.values()[statusID];
    }


    /**
     * Get name in lower case.
     */
    public String getName() {
        return name().toLowerCase();
    }
}

