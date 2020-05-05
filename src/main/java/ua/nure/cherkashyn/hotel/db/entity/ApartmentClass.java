package ua.nure.cherkashyn.hotel.db.entity;


/**
 * ApartmentClass entity.
 *
 * @author Vladimir Cherkashyn
 */
public enum ApartmentClass {
    STANDARD, IMPROVED, LUXURY;

    public static ApartmentClass getClass(Apartment apartment) {
        int classId = (int) apartment.getClassId();
        return ApartmentClass.values()[classId];
    }


    /**
     * Get name in lower case.
     */
    public String getName() {
        return name().toLowerCase();
    }
}

